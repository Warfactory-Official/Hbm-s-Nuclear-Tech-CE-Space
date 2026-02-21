package com.hbmspace.blocks.generic;

import com.google.common.collect.ImmutableMap;
import com.hbm.lib.HBMSoundHandler;
import com.hbm.lib.ModDamageSource;
import com.hbm.packet.PacketDispatcher;
import com.hbm.packet.toclient.AuxParticlePacketNT;
import com.hbm.packet.toclient.BufPacket;
import com.hbm.render.block.BlockBakeFrame;
import com.hbm.tileentity.IBufPacketReceiver;
import com.hbmspace.blocks.BlockContainerBakeableSpace;
import com.hbmspace.interfaces.AutoRegister;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

public class BlockVolcanoV2 extends BlockContainerBakeableSpace {

    public BlockVolcanoV2(String s, String tex) {
        super(Material.ROCK, s, new BlockBakeFrame(BlockBakeFrame.BlockForm.ALL, tex));
    }

    @Override
    public @Nullable TileEntity createNewTileEntity(@NotNull World worldIn, int meta) {
        return new TileEntityLightningVolcano();
    }

    @Override
    public boolean isOpaqueCube(@NotNull IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(@NotNull IBlockState state) {
        return false;
    }
    @AutoRegister
    public static class TileEntityLightningVolcano extends TileEntity implements IBufPacketReceiver, ITickable {

        public int chargetime;
        public float flashd;

        public TileEntityLightningVolcano() {
            chargetime = (new Random()).nextInt(400) + 100;
        }

        @Override // dummy method to satisfy IBufPacketReceiver
        public void networkPackNT(int range) {
        }

        @Override
        public void update() {
            if (chargetime > 0) {
                flashd = 0;
            } else {
                flashd += 0.3f;
                flashd = Math.min(100.0f, flashd + 0.3f * (100.0f - flashd) * 0.15f);
            }

            if (!world.isRemote) {
                EnumFacing dir = EnumFacing.byIndex(this.getBlockMetadata());

                if (chargetime == 1) {
                    double x = (int) (pos.getX() + dir.getXOffset() * (world.getTotalWorldTime() / 5L) % 1) + 0.5;
                    double y = (int) (pos.getY() + dir.getYOffset() * (world.getTotalWorldTime() / 5L) % 1) + 20.5;
                    double z = (int) (pos.getZ() + dir.getZOffset() * (world.getTotalWorldTime() / 5L) % 1) + 0.5;

                    NBTTagCompound data = new NBTTagCompound();
                    data.setString("type", "plasmablast");
                    data.setFloat("r", 1F);
                    data.setFloat("g", 1F);
                    data.setFloat("b", 1F);
                    data.setFloat("scale", 10 * 4);
                    data.setFloat("yaw", 90);

                    PacketDispatcher.wrapper.sendToAllAround(new AuxParticlePacketNT(data, x, y, z), new TargetPoint(world.provider.getDimension(), x, y, z, 256));
                    world.playSound(null, x, y, z, SoundEvents.ENTITY_LIGHTNING_THUNDER, SoundCategory.WEATHER, 200, 0.8F + this.world.rand.nextFloat() * 0.2F);
                    vapor();
                }

                if (chargetime > 0) chargetime--;

                if (flashd >= 100) {
                    chargetime = world.rand.nextInt(400) + 100;
                }

                PacketDispatcher.wrapper.sendToAllAround(new BufPacket(pos.getX(), pos.getY(), pos.getZ(), this), new TargetPoint(this.world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 256));
            }
        }

        @Override
        public void readFromNBT(@NotNull NBTTagCompound nbt) {
            super.readFromNBT(nbt);
            chargetime = nbt.getInteger("charge");
        }

        @Override
        public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound nbt) {
            super.writeToNBT(nbt);
            nbt.setInteger("charge", this.chargetime);
            return nbt;
        }

        @Override
        public @NotNull AxisAlignedBB getRenderBoundingBox() {
            return TileEntity.INFINITE_EXTENT_AABB;
        }

        @Override
        @SideOnly(Side.CLIENT)
        public double getMaxRenderDistanceSquared() {
            return 65536.0D;
        }

        @Override
        public void serialize(ByteBuf buf) {
            buf.writeInt(chargetime);
        }

        @Override
        public void deserialize(ByteBuf buf) {
            chargetime = buf.readInt();
        }

        private void vapor() {
            List<Entity> entities = this.world.getEntitiesWithinAABB(Entity.class,
                    new AxisAlignedBB(this.pos.getX() - 0.5, this.pos.getY() + 0.5, this.pos.getZ() - 0.5, this.pos.getX() + 1.5,
                            this.pos.getY() + 60, this.pos.getZ() + 1.5));

            if (!entities.isEmpty()) {
                for (Entity e : entities) {

                    if (e instanceof EntityLivingBase) {
                        if (e.attackEntityFrom(ModDamageSource.electricity, MathHelper.clamp(((EntityLivingBase) e).getMaxHealth(), 3.0F, 20.0F))) {
                            world.playSound(null, e.posX, e.posY, e.posZ, HBMSoundHandler.tesla, SoundCategory.MASTER, 1.0F, 1.0F);
                        }
                    }
                }
            }
        }

    }

    @Override
    public void bakeModel(ModelBakeEvent event) {

        try {
            IModel baseModel = ModelLoaderRegistry.getModel(new ResourceLocation(blockFrame.getBaseModel()));
            ImmutableMap.Builder<String, String> textureMap = ImmutableMap.builder();

            blockFrame.putTextures(textureMap);
            IModel retexturedModel = baseModel.retexture(textureMap.build());
            IBakedModel bakedModel = retexturedModel.bake(
                    ModelRotation.X0_Y0, DefaultVertexFormats.BLOCK, ModelLoader.defaultTextureGetter()
            );

            ModelResourceLocation modelLocation = new ModelResourceLocation(getRegistryName(), "inventory");
            event.getModelRegistry().putObject(modelLocation, bakedModel);
            ModelResourceLocation worldLocation = new ModelResourceLocation(getRegistryName(), "normal");
            event.getModelRegistry().putObject(worldLocation, bakedModel);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void registerModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this),0, new ModelResourceLocation(this.getRegistryName(), "inventory"));
    }

    @Override
    public void registerSprite(TextureMap map) {
        blockFrame.registerBlockTextures(map);
    }
}
