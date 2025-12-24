package com.hbmspace.tileentity.machine;

import com.hbm.blocks.BlockDummyable;
import com.hbm.explosion.vanillant.ExplosionVNT;
import com.hbm.explosion.vanillant.standard.*;
import com.hbm.items.ISatChip;
import com.hbm.lib.ForgeDirection;
import com.hbm.main.MainRegistry;
import com.hbm.saveddata.satellites.Satellite;
import com.hbm.saveddata.satellites.SatelliteSavedData;
import com.hbm.sound.AudioWrapper;
import com.hbm.tileentity.TileEntityMachineBase;
import com.hbm.util.BobMathUtil;
import com.hbm.util.ParticleUtil;
import com.hbmspace.dim.CelestialBody;
import com.hbmspace.dim.orbit.OrbitalStation;
import com.hbmspace.dim.trait.CBT_Dyson;
import com.hbmspace.explosion.vanillant.standard.CustomDamageHandlerDyson;
import com.hbmspace.interfaces.AutoRegister;
import com.hbmspace.lib.HBMSpaceSoundHandler;
import com.hbmspace.saveddata.satellites.SatelliteDysonRelay;
import com.hbmspace.tileentity.IDysonConverter;
import com.hbmspace.tileentity.TESpaceUtil;
import com.hbmspace.util.ParticleUtilSpace;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@AutoRegister
public class TileEntityDysonReceiver extends TileEntityMachineBase implements ITickable {

    // Connects to a dyson swarm via ID, receiving energy during the day
    // also receives energy at night if a satellite relay is in orbit around the planet

    // The energy received is fired as a violently powerful beam,
    // converters can collect this beam and turn it into HE/TU or used for analysis, crafting, etc.

    public boolean isReceiving;
    public int swarmId;
    public int swarmCount;
    public int swarmConsumers;
    public int beamLength;

    private AudioWrapper audio;

    public TileEntityDysonReceiver() {
        super(1, false, false);
    }

    // Sun luminosity is 4*10^26, which we can't represent in any Java integer primitive
    // therefore the upper bound for power generation is higher than a FEnSU, effectively
    // reality doesn't provide any interesting solutions that make the system engaging to use
    // so we're going to build our own power curve.
    // We need to encourage players to build large swarms, so single satellites must suck but together they produce enormous power
    // Gompertz is a funne name
    public static long getEnergyOutput(int swarmCount) {
        double adjustedDensity = (double)swarmCount / 1024.0D;
        long maxOutput = Long.MAX_VALUE / 10;
        double b = 32.0D;
        double c = 1.3D;
        double gompertz = Math.exp(-b * Math.exp(-c * adjustedDensity));
        return (long)(maxOutput * gompertz) / 20;
    }

    @Override
    public String getDefaultName() {
        return "container.machineDysonReceiver";
    }

    @Override
    public void update() {
        ForgeDirection dir = ForgeDirection.getOrientation(this.getBlockMetadata() - BlockDummyable.offset).getOpposite();

        if(!world.isRemote) {
            swarmId = ISatChip.getFreqS(inventory.getStackInSlot(0));

            SatelliteSavedData data = TESpaceUtil.getData(world, pos.getX(), pos.getZ());
            Satellite sat = data.getSatFromFreq(swarmId);
            int sun = world.getLightFor(EnumSkyBlock.SKY, pos) - world.getSkylightSubtracted() - 11;

            boolean occluded = false;
            for(int x = -3; x <= 3; x++) {
                for(int z = -3; z <= 3; z++) {
                    if(world.getHeight(pos.getX() + x, pos.getZ() + z) > pos.getY() + 10) {
                        occluded = true;
                        break;
                    }
                }
            }

            swarmCount = CBT_Dyson.count(world, swarmId);
            swarmConsumers = CBT_Dyson.consumers(world, swarmId);

            isReceiving = (sat instanceof SatelliteDysonRelay || sun > 0) && swarmId > 0 && !occluded && swarmCount > 0 && swarmConsumers > 0;

            if(isReceiving) {
                long energyOutput = getEnergyOutput(swarmCount) / swarmConsumers;
                int maxLength = 24;

                beamLength = maxLength;
                for(int i = 9; i < maxLength; i++) {
                    int x = pos.getX() + dir.offsetX * i;
                    int y = pos.getY() + 1;
                    int z = pos.getZ() + dir.offsetZ * i;
                    BlockPos rPos = new BlockPos(x, y, z);
                    IBlockState state = world.getBlockState(rPos);
                    Block block = state.getBlock();

                    // two block gap minimum
                    boolean detonate = true;
                    TileEntity te = null;
                    if(i > 10) {
                        if(block instanceof BlockDummyable) {
                            int[] pos = ((BlockDummyable) block).findCore(world, x, y, z);
                            if(pos != null) {
                                te = world.getTileEntity(new BlockPos(pos[0], pos[1], pos[2]));
                            }
                        } else {
                            te = world.getTileEntity(rPos);
                        }

                        if(te instanceof IDysonConverter) {
                            detonate = !((IDysonConverter) te).provideEnergy(x, y, z, energyOutput);
                        }
                    }

                    if(block.isOpaqueCube(state) || te != null) {
                        if(detonate) {
                            world.setBlockToAir(rPos);

                            ExplosionVNT vnt = new ExplosionVNT(world, x, y, z, 3, null);
                            vnt.setBlockAllocator(new BlockAllocatorStandard());
                            vnt.setBlockProcessor(new BlockProcessorStandard().withBlockEffect(new BlockMutatorFire()));
                            vnt.setEntityProcessor(new EntityProcessorStandard().allowSelfDamage());
                            vnt.setPlayerProcessor(new PlayerProcessorStandard());
                            vnt.setSFX(new ExplosionEffectStandard());
                            vnt.explode();
                        }

                        beamLength = i;
                        break;
                    }
                }


                double blx = Math.min(pos.getX(), pos.getX() + dir.offsetX * beamLength) + 0.2;
                double bux = Math.max(pos.getX(), pos.getX() + dir.offsetX * beamLength) + 0.8;
                double bly = Math.min(pos.getY(), 1 + pos.getY() + dir.offsetY * beamLength) + 0.2;
                double buy = Math.max(pos.getY(), 1 + pos.getY() + dir.offsetY * beamLength) + 0.8;
                double blz = Math.min(pos.getZ(), pos.getZ() + dir.offsetZ * beamLength) + 0.2;
                double buz = Math.max(pos.getZ(), pos.getZ() + dir.offsetZ * beamLength) + 0.8;

                List<EntityLivingBase> list = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(blx, bly, blz, bux, buy, buz));

                for(EntityLivingBase entity : list) {
                    ExplosionVNT vnt = new ExplosionVNT(world, entity.posX - dir.offsetX, entity.posY + 1.5, entity.posZ - dir.offsetZ, 3, null);
                    vnt.setBlockAllocator(new BlockAllocatorStandard());
                    vnt.setBlockProcessor(new BlockProcessorStandard().withBlockEffect(new BlockMutatorFire()));
                    vnt.setEntityProcessor(new EntityProcessorStandard().withDamageMod(new CustomDamageHandlerDyson(energyOutput)));
                    vnt.setPlayerProcessor(new PlayerProcessorStandard());
                    vnt.setSFX(new ExplosionEffectStandard());
                    vnt.explode();
                }
            }

            networkPackNT(250);
        } else {
            if(isReceiving) {
                if(audio == null) {
                    audio = MainRegistry.proxy.getLoopedSound(HBMSpaceSoundHandler.dysonBeam, SoundCategory.BLOCKS, pos.getX() + dir.offsetX * 8, pos.getY(), pos.getZ() + dir.offsetZ * 8, 0.75F, 20F, 1.0F, 20);
                    audio.startSound();
                }

                audio.keepAlive();
                audio.updatePitch(0.85F);

                if(world.rand.nextInt(10) == 0) {
                    ParticleUtilSpace.spawnFlare(world, pos.getX() - 5 + world.rand.nextDouble() * 10, pos.getY() + 11, pos.getZ() - 5 + world.rand.nextDouble() * 10, 0, 0.1 + world.rand.nextFloat() * 0.1, 0, 4F + world.rand.nextFloat() * 2);
                }
            } else {
                if(audio != null) {
                    audio.stopSound();
                    audio = null;
                }
            }
        }
    }

    @Override
    public void serialize(ByteBuf buf) {
        super.serialize(buf);
        buf.writeBoolean(isReceiving);
        buf.writeInt(swarmId);
        buf.writeInt(swarmCount);
        buf.writeInt(swarmConsumers);
        buf.writeInt(beamLength);
    }

    @Override
    public void deserialize(ByteBuf buf) {
        super.deserialize(buf);
        isReceiving = buf.readBoolean();
        swarmId = buf.readInt();
        swarmCount = buf.readInt();
        swarmConsumers = buf.readInt();
        beamLength = buf.readInt();
    }

    @Override
    public void onChunkUnload() {
        super.onChunkUnload();

        if(audio != null) {
            audio.stopSound();
            audio = null;
        }
    }

    @Override
    public void invalidate() {
        super.invalidate();

        if(audio != null) {
            audio.stopSound();
            audio = null;
        }
    }

    AxisAlignedBB bb = null;

    @Override
    public @NotNull AxisAlignedBB getRenderBoundingBox() {

        if(bb == null) {
            bb = new AxisAlignedBB(
                    pos.getX() - 25,
                    pos.getY(),
                    pos.getZ() - 25,
                    pos.getX() + 25,
                    pos.getY() + 19,
                    pos.getZ() + 25
            );
        }

        return bb;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared() {
        return 65536.0D;
    }

    public static void runTests() {
        for(int i = 1; i < 5000; i *= 2) {
            MainRegistry.logger.info("{} dyson swarm members produces: {}HE/s", i, BobMathUtil.getShortNumber(getEnergyOutput(i) * 20));
        }
    }

}