package com.hbmspace.entity.missile;

import com.hbm.blocks.ILookOverlay;
import com.hbm.config.SpaceConfig;
import com.hbm.entity.missile.EntityMissileBaseNT;
import com.hbm.explosion.ExplosionLarge;
import com.hbm.items.ISatChip;
import com.hbm.items.ModItems;
import com.hbm.items.weapon.ItemMissile;
import com.hbm.lib.HBMSoundHandler;
import com.hbm.main.MainRegistry;
import com.hbm.saveddata.satellites.Satellite;
import com.hbm.sound.AudioWrapper;
import com.hbm.util.*;
import com.hbmspace.dim.CelestialBody;
import com.hbmspace.dim.CelestialTeleporter;
import com.hbmspace.dim.SolarSystem;
import com.hbmspace.dim.orbit.OrbitalStation;
import com.hbmspace.handler.RocketStruct;
import com.hbmspace.interfaces.AutoRegister;
import com.hbmspace.items.ItemVOTVdrive;
import com.hbmspace.items.ModItemsSpace;
import com.hbmspace.items.weapon.ItemCustomRocket;
import com.hbmspace.main.SpaceMain;
import com.hbmspace.render.misc.RocketPart;
import com.hbmspace.tileentity.machine.TileEntityOrbitalStation;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.*;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@AutoRegister(name = "entity_rideable_rocket", trackingRange = 1000)
public class EntityRideableRocket extends EntityMissileBaseNT implements ILookOverlay {

    public ItemStack navDrive;

    public EntityRideableRocketDummy capDummy;

    private int stateTimer = 0;

    private double rocketVelocity = 0;
    private boolean sizeSet = false;

    private AudioWrapper audio;

    private RocketState lastState = RocketState.AWAITING;

    private boolean willExplode = false;

    private int satFreq = 0;

    private TileEntityOrbitalStation targetPort;

    private static final DataParameter<Integer> DP_STATE =
            EntityDataManager.createKey(EntityRideableRocket.class, DataSerializers.VARINT);
    private static final DataParameter<ItemStack> DP_DRIVE =
            EntityDataManager.createKey(EntityRideableRocket.class, DataSerializers.ITEM_STACK);
    private static final DataParameter<Integer> DP_TIMER =
            EntityDataManager.createKey(EntityRideableRocket.class, DataSerializers.VARINT);

    private static final DataParameter<Integer> DP_ROCKET_CAPSULE =
            EntityDataManager.createKey(EntityRideableRocket.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> DP_ROCKET_STAGECOUNT =
            EntityDataManager.createKey(EntityRideableRocket.class, DataSerializers.VARINT);

    @SuppressWarnings("unchecked")
    private static final DataParameter<Integer>[] DP_ROCKET_STAGE_A = new DataParameter[RocketStruct.MAX_STAGES];
    @SuppressWarnings("unchecked")
    private static final DataParameter<Integer>[] DP_ROCKET_STAGE_B = new DataParameter[RocketStruct.MAX_STAGES];

    static {
        for(int i = 0; i < RocketStruct.MAX_STAGES; i++) {
            DP_ROCKET_STAGE_A[i] = EntityDataManager.createKey(EntityRideableRocket.class, DataSerializers.VARINT);
            DP_ROCKET_STAGE_B[i] = EntityDataManager.createKey(EntityRideableRocket.class, DataSerializers.VARINT);
        }
    }

    public enum RocketState {
        AWAITING,
        LAUNCHING,
        LANDING,
        LANDED,
        TIPPING,
        DOCKING,
        UNDOCKING,
        NEEDSFUEL,
    }

    public EntityRideableRocket(World world) {
        super(world);
        setSize(2, 8);
        sizeSet = false;
        targetX = (int)posX - 10000;
        targetZ = (int)posZ;
    }

    public EntityRideableRocket(World world, float x, float y, float z, ItemStack stack) {
        super(world, x, y, z, (int)x + 10000, (int)z);
        RocketStruct rocket = ItemCustomRocket.get(stack);
        satFreq = ISatChip.getFreqS(stack);

        setRocket(rocket);
        setSize(2, (float)rocket.getHeight() + 1);
    }

    public EntityRideableRocket withProgram(ItemStack stack) {
        this.navDrive = stack.copy();
        return this;
    }

    public EntityRideableRocket launchedBy(net.minecraft.entity.EntityLivingBase entity) {
        this.thrower = entity;
        return this;
    }

    public void beginLandingSequence() {
        motionX = 0;
        motionY = 0;
        motionZ = 0;

        ItemVOTVdrive.Target from = CelestialBody.getTarget(world, (int)posX, (int)posZ);
        ItemVOTVdrive.Target to = getTarget();

        RocketStruct rocket = getRocket();
        boolean expendStage = rocket.stages.size() > 0;
        if(getState() == RocketState.UNDOCKING && from.body == to.body) expendStage = false;

        if(expendStage) {
            rocket.stages.remove(0);

            setRocket(rocket);
            setSize(2, (float)rocket.getHeight() + 1);
        }

        setState(RocketState.LANDING);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        RocketState state = getState();

        if(!sizeSet) {
            setSize(2, (float)getRocket().getHeight() + 1);
            if(!world.isRemote && (state == RocketState.LANDED || state == RocketState.AWAITING || state == RocketState.NEEDSFUEL)) {
                TileEntity te = CompatExternal.getCoreFromPos(world, new BlockPos(MathHelper.floor(posX), MathHelper.floor(posY + height - 1.0D), MathHelper.floor(posZ)));

                if(te instanceof TileEntityOrbitalStation) {
                    ((TileEntityOrbitalStation)te).dockRocket(this);
                }
            }
        }

        EntityPlayer rider = null;
        if(!getPassengers().isEmpty() && getPassengers().get(0) instanceof EntityPlayer) {
            rider = (EntityPlayer)getPassengers().get(0);
        }

        if(!world.isRemote) {
            rotationYaw = -90.0F;

            if(navDrive != null && navDrive.getItem() instanceof ItemVOTVdrive) {
                ItemVOTVdrive.getTarget(navDrive, world);
                setDrive(navDrive);
            }

            if(state == RocketState.AWAITING && ((rider != null && rider.isJumping) || !canRide())) {
                ItemVOTVdrive.Target from = CelestialBody.getTarget(world, (int)posX, (int)posZ);
                ItemVOTVdrive.Target to = getTarget();

                RocketState transitionTo = from.inOrbit ? RocketState.UNDOCKING : RocketState.LAUNCHING;

                targetX = (int)posX - 10000;
                targetZ = (int)posZ;

                if(getRocket().hasSufficientFuel(from.body, to.body, from.inOrbit, to.inOrbit)) {
                    setState(transitionTo);
                }
            }

            if(state == RocketState.LAUNCHING) {
                if(rocketVelocity < 4)
                    rocketVelocity += MathHelper.clamp((double)stateTimer / 120D * 0.05D, 0, 0.05);

                rotationPitch = MathHelper.clamp((stateTimer - 60) * 0.3F, 0.0F, 45.0F);

                if(FMLCommonHandler.instance().getSide() == Side.CLIENT && FMLClientHandler.instance().hasOptifine()) {
                    rotationPitch = 0;
                }
            } else if(state == RocketState.LANDING) {
                double targetHeight = world.getHeight((int)posX, (int)posZ);
                rocketVelocity = MathHelper.clamp((targetHeight - posY) * 0.005, -0.5, -0.005);
                rotationPitch = 0;

                if(navDrive != null && navDrive.getItem() instanceof ItemVOTVdrive) {
                    ItemVOTVdrive.Destination destination = ItemVOTVdrive.getDestination(navDrive);

                    AxisAlignedBB bb = getEntityBoundingBox();
                    AxisAlignedBB adj = new AxisAlignedBB(bb.minX, targetHeight, bb.minZ, bb.maxX, bb.maxY, bb.maxZ);
                    if(!world.getEntitiesInAABBexcluding(this, adj, e -> e instanceof EntityRideableRocket).isEmpty() && navDrive.getTagCompound() != null) {
                        int distance = world.rand.nextBoolean() ? -5 : 5;
                        if(world.rand.nextBoolean()) {
                            destination.x += distance;
                            navDrive.getTagCompound().setInteger("x", destination.x);
                        } else {
                            destination.z += distance;
                            navDrive.getTagCompound().setInteger("z", destination.z);
                        }
                    }

                    posX = destination.x + 0.5D;
                    posZ = destination.z + 0.5D;
                }
            } else if(state == RocketState.TIPPING) {
                float tipTime = (float)stateTimer * 0.1F;
                rotationPitch = tipTime * tipTime;

                if(rotationPitch > 90) {
                    rotationPitch = 90;

                    if(willExplode) {
                        dropNDie(null);
                        ExplosionLarge.explode(world, thrower, posX, posY, posZ, 5, true, false, true);
                        ExplosionLarge.spawnShrapnelShower(world, posX, posY, posZ, motionX, motionY, motionZ, 15, 0.075);

                        world.playSound(null, posX, posY, posZ, HBMSoundHandler.pipeFail, SoundCategory.PLAYERS, 10_000, 0.8F + this.world.rand.nextFloat() * 0.4F);
                    }
                }

                rocketVelocity = 0;
            } else if(state == RocketState.DOCKING) {
                if(stateTimer > 20) {
                    rocketVelocity = 0.1;
                    rotationPitch = 0;

                    if(targetPort == null) targetPort = OrbitalStation.getPort((int)posX, (int)posZ);

                    if(targetPort != null) {
                        posX = targetPort.getPos().getX() + 0.5D;
                        posZ = targetPort.getPos().getZ() + 0.5D;

                        targetPort.despawnRocket();
                        targetPort.reservePort();

                        if(posY + height > targetPort.getPos().getY() + 1.5D) {
                            setState(isReusable() ? RocketState.NEEDSFUEL : RocketState.LANDED);
                            posY = targetPort.getPos().getY() + 1.5D - height;

                            targetPort.dockRocket(this);
                            targetPort = null;
                        }
                    } else {
                        rocketVelocity = 0;
                    }
                } else {
                    rocketVelocity = 0;
                    rotationPitch = 0;
                }
            } else if(state == RocketState.UNDOCKING) {
                rocketVelocity = -0.1;
                rotationPitch = 0;
            } else {
                rocketVelocity = 0;
                rotationPitch = 0;
            }

            if(state == RocketState.LAUNCHING) {
                Vec3d motion = BobMathUtil.getDirectionFromAxisAngle(rotationPitch - 90.0F, 180.0F - rotationYaw, rocketVelocity);
                motionX = motion.x;
                motionY = motion.y;
                motionZ = motion.z;
            } else {
                motionX = 0;
                motionY = rocketVelocity;
                motionZ = 0;
            }

            if((state == RocketState.LAUNCHING && posY > 900) || (state == RocketState.UNDOCKING && posY < 32)) {
                beginLandingSequence();
                RocketStruct rocket = getRocket();

                if(navDrive != null && navDrive.getItem() instanceof ItemVOTVdrive) {
                    ItemVOTVdrive.Destination destination = ItemVOTVdrive.getDestination(navDrive);

                    int x = destination.x;
                    int y = 800;
                    int z = destination.z;

                    int targetDimensionId = destination.body.getDimensionId();

                    if(rider != null) {
                        if(destination.body == SolarSystem.Body.ORBIT) {
                            setState(RocketState.DOCKING);

                            x = x * OrbitalStation.STATION_SIZE + (OrbitalStation.STATION_SIZE / 2);
                            y = 0;
                            z = z * OrbitalStation.STATION_SIZE + (OrbitalStation.STATION_SIZE / 2);
                        }

                        if(world.provider.getDimension() != targetDimensionId) {
                            CelestialTeleporter.teleport(rider, targetDimensionId, x + 0.5D, y, z + 0.5D, false);
                        } else {
                            posX = x + 0.5D;
                            posZ = z + 0.5D;
                        }

                        if(destination.body == SolarSystem.Body.ORBIT) {
                            WorldServer targetWorld = DimensionManager.getWorld(targetDimensionId);
                            OrbitalStation.spawn(targetWorld, x, z);
                        }
                    } else if(!canRide()) {
                        if(rocket.capsule.part instanceof ISatChip && destination.body != SolarSystem.Body.ORBIT) {
                            WorldServer targetWorld = DimensionManager.getWorld(targetDimensionId);
                            if(targetWorld == null) {
                                DimensionManager.initDimension(targetDimensionId);
                                targetWorld = DimensionManager.getWorld(targetDimensionId);
                            }
                            if(targetWorld != null) {
                                Satellite.orbit(targetWorld, Satellite.getIDFromItem(rocket.capsule.part), satFreq, posX, posY, posZ);
                            }
                        } else if(rocket.capsule.part == ModItemsSpace.rp_station_core_20) {
                            OrbitalStation.addStation(x, z, CelestialBody.getBody(world));

                            if(thrower != null && thrower instanceof EntityPlayer player) {
                                if(!player.capabilities.isCreativeMode && !ItemVOTVdrive.wasCopied(navDrive)) {
                                    //AdvancementManager.grantAchievement(player, AdvancementManager.achDriveFail);
                                }
                            }
                        }

                        setDead();
                    }
                }
            }

            if(state == RocketState.LANDING && world.getBlockState(new BlockPos(MathHelper.floor(posX), MathHelper.floor(posY), MathHelper.floor(posZ))).getMaterial() == net.minecraft.block.material.Material.WATER) {
                setState(RocketState.TIPPING);
            }

            if(height > 8) {
                double offset = height - 4;
                if(capDummy == null || capDummy.isDead) {
                    capDummy = new EntityRideableRocketDummy(world, this);
                    capDummy.parent = this;
                    capDummy.setPosition(posX, posY + offset, posZ);
                    world.spawnEntity(capDummy);
                } else {
                    capDummy.setPosition(posX, posY + offset, posZ);
                }
            } else if(capDummy != null) {
                capDummy.setDead();
                capDummy = null;
            }
        } else {
            if(state != lastState) {
                if(state == RocketState.LAUNCHING) {
                    AudioWrapper ignition = SpaceMain.proxy.getLoopedSound(HBMSoundHandler.rocketIgnition, SoundCategory.PLAYERS, (float)posX, (float)posY, (float)posZ, 1.0F, 250.0F, 1.0F, 5);
                    ignition.setDoesRepeat(false);
                    ignition.startSound();
                }

                lastState = state;
                stateTimer = 0;
            } else {
                if(state == RocketState.LAUNCHING || (state == RocketState.LANDING && motionY > -0.4)) {
                    if(audio == null || !audio.isPlaying()) {
                        SoundEvent rocketAudio = getRocket().stages.size() <= 1 ? HBMSoundHandler.rocketFlyLight : HBMSoundHandler.rocketFlyHeavy;
                        audio = MainRegistry.proxy.getLoopedSound(rocketAudio, SoundCategory.PLAYERS, (float)posX, (float)posY, (float)posZ, 1.0F, 250.0F, 1.0F, 5);
                        audio.startSound();
                    }

                    audio.updatePosition((float)posX, (float)posY, (float)posZ);
                    audio.keepAlive();
                } else {
                    if(audio != null) {
                        audio.stopSound();
                        audio = null;
                    }
                }
            }
        }

        setStateTimer(++stateTimer);
    }

    @Override
    protected double motionMult() {
        RocketState state = getState();
        if(state == RocketState.AWAITING || state == RocketState.LANDED || state == RocketState.NEEDSFUEL) return 0;
        return 4;
    }

    @Override
    public boolean processInitialInteract(@NotNull EntityPlayer player, @NotNull EnumHand hand) {
        if(!canRide()) return false;

        if(super.processInitialInteract(player, hand)) {
            return true;
        } else if(!this.world.isRemote && (this.getPassengers().isEmpty() || this.getPassengers().contains(player))) {
            player.startRiding(this);
            return true;
        } else {
            return false;
        }
    }

    public boolean canRide() {
        return getRocket().capsule.part.attributes[0] == ItemMissile.WarheadType.APOLLO;
    }

    public boolean isReusable() {
        return getRocket().capsule.part == ModItemsSpace.rp_pod_20;
    }

    @Override
    public void updatePassenger(net.minecraft.entity.@NotNull Entity passenger) {

        double length = getMountedYOffset() + passenger.getYOffset();
        Vec3d target = BobMathUtil.getDirectionFromAxisAngle(rotationPitch - 90.0F, 180.0F - rotationYaw, length);

        passenger.setPosition(posX + target.x, posY + target.y, posZ + target.z);
    }

    @Override
    public void onMissileImpact(RayTraceResult mop) {
        RocketState state = getState();
        if(state != RocketState.LANDING && state != RocketState.DOCKING)
            return;

        motionX = 0;
        motionY = 0;
        motionZ = 0;

        if(state == RocketState.DOCKING) {
            return;
        }

        RocketStruct rocket = getRocket();
        if(rocket.stages.size() > 0 && rocket.stages.get(0).fins == null) {
            setState(RocketState.TIPPING);
            willExplode = true;
        } else {
            setState(RocketState.LANDED);
        }

        posY = world.getHeight((int)posX, (int)posZ);
    }

    @Override
    public double getMountedYOffset() {
        if(isReusable()) return height - 2.5;
        return height - 3.0;
    }

    @Override
    protected void setSize(float width, float height) {
        super.setSize(width, height);
        sizeSet = true;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if(!world.isRemote && !isDead) {
            if(isEntityInvulnerable(source)) {
                return false;
            } else if(this.getControllingPassenger() == null && source.getTrueSource() instanceof EntityPlayer) {
                if((getRocket().stages.size() == 0 && getRocket().capsule.part != ModItemsSpace.rp_pod_20) || getState() == RocketState.TIPPING) {
                    dropNDie(source);
                } else {
                    EntityPlayer player = (EntityPlayer) source.getTrueSource();
                    ItemStack stack = player.getHeldItemMainhand();
                    if(!stack.isEmpty() && stack.getItem().canHarvestBlock(net.minecraft.init.Blocks.STONE.getDefaultState(), stack)) {
                        dropNDie(source);
                    }
                }
            }

        }
        return true;
    }

    public void dropNDie(DamageSource source) {
        setDead();

        RocketStruct rocket = getRocket();
        ItemStack stack;
        if(rocket.stages.size() == 0) {
            stack = new ItemStack(rocket.capsule.part);
        } else {
            stack = ItemCustomRocket.build(rocket, true);
        }
        entityDropItem(stack, 0.0F);

        if(navDrive != null) {
            entityDropItem(navDrive, 0.0F);
        }
    }

    @Override
    public void setDead() {
        super.setDead();
        if(capDummy != null) {
            capDummy.setDead();
        }
    }

    @Override
    protected void spawnContrail() {
        RocketState state = getState();

        if(state == RocketState.AWAITING
                || state == RocketState.LANDED
                || (state == RocketState.LANDING && motionY <= -0.4)
                || state == RocketState.DOCKING
                || state == RocketState.UNDOCKING
                || state == RocketState.NEEDSFUEL)
            return;

        double x = posX;
        double y = posY;
        double z = posZ;

        if(motionY > 0) {
            x = lastTickPosX;
            y = lastTickPosY;
            z = lastTickPosZ;
        }

        RocketStruct rocket = getRocket();
        if(rocket.stages.size() == 0) {
            if(state == RocketState.TIPPING) return;

            if(isReusable()) {
                ParticleUtil.spawnGasFlame(world, x + 0.5, y, z, 0, -1, 0);
                ParticleUtil.spawnGasFlame(world, x - 0.5, y, z, 0, -1, 0);
                ParticleUtil.spawnGasFlame(world, x, y, z + 0.5, 0, -1, 0);
                ParticleUtil.spawnGasFlame(world, x, y, z - 0.5, 0, -1, 0);
            } else {
                double r = rocket.capsule.part.bottom.radius * 0.5;
                ParticleUtil.spawnGasFlame(world, x + r, y, z + r, 0.25, -0.75, 0.25);
                ParticleUtil.spawnGasFlame(world, x - r, y, z + r, -0.25, -0.75, 0.25);
                ParticleUtil.spawnGasFlame(world, x + r, y, z - r, 0.25, -0.75, -0.25);
                ParticleUtil.spawnGasFlame(world, x - r, y, z - r, -0.25, -0.75, -0.25);
            }

            double groundHeight = world.getHeight((int)x, (int)z);
            double distanceToGround = y - groundHeight;
            if(distanceToGround < 10) {
                ExplosionLarge.spawnShock(world, x, groundHeight + 0.5, z, 1 + world.rand.nextInt(3), 1 + world.rand.nextGaussian());
            }

            return;
        }

        RocketStruct.RocketStage stage = rocket.stages.get(0);

        if(state == RocketState.LANDING) {
            ParticleUtil.spawnGasFlame(world, x, y, z, 0.0, -1.0, 0.0);

            double groundHeight = world.getHeight((int)x, (int)z);
            double distanceToGround = y - groundHeight;
            if(distanceToGround < 10) {
                ExplosionLarge.spawnShock(world, x, groundHeight + 0.5, z, 1 + world.rand.nextInt(3), 1 + world.rand.nextGaussian());
            }
        } else if(state == RocketState.LAUNCHING || getStateTimer() < 200) {
            spawnControlWithOffset(0, 0, 0);

            int cluster = stage.getCluster();
            for(int c = 1; c < cluster; c++) {
                float spin = (float)c / (float)(cluster - 1);
                double ox = Math.cos(spin * Math.PI * 2) * stage.fuselage.part.bottom.radius;
                double oz = Math.sin(spin * Math.PI * 2) * stage.fuselage.part.bottom.radius;
                spawnControlWithOffset(ox, 0, oz);
            }
        }
    }

    public RocketStruct getRocket() {
        RocketStruct rocket = new RocketStruct();
        rocket.capsule = RocketPart.getPart(this.dataManager.get(DP_ROCKET_CAPSULE));

        int count = this.dataManager.get(DP_ROCKET_STAGECOUNT);
        for(int i = 0; i < count && i < RocketStruct.MAX_STAGES; i++) {
            Tuple.Pair<Integer, Integer> watchable = new Tuple.Pair<>(
                    this.dataManager.get(DP_ROCKET_STAGE_A[i]),
                    this.dataManager.get(DP_ROCKET_STAGE_B[i])
            );
            rocket.stages.add(RocketStruct.RocketStage.unzipWatchable(watchable));
        }

        return rocket;
    }

    public void setRocket(RocketStruct rocket) {
        this.dataManager.set(DP_ROCKET_CAPSULE, RocketPart.getId(rocket.capsule));
        int count = Math.min(rocket.stages.size(), RocketStruct.MAX_STAGES);
        this.dataManager.set(DP_ROCKET_STAGECOUNT, count);
        for(int i = 0; i < count; i++) {
            Tuple.Pair<Integer, Integer> watchable = rocket.stages.get(i).zipWatchable();
            this.dataManager.set(DP_ROCKET_STAGE_A[i], watchable.key);
            this.dataManager.set(DP_ROCKET_STAGE_B[i], watchable.value);
        }
        for(int i = count; i < RocketStruct.MAX_STAGES; i++) {
            this.dataManager.set(DP_ROCKET_STAGE_A[i], 0);
            this.dataManager.set(DP_ROCKET_STAGE_B[i], 0);
        }
    }

    public RocketState getState() {
        return RocketState.values()[this.dataManager.get(DP_STATE)];
    }

    public void setState(RocketState state) {
        this.dataManager.set(DP_STATE, state.ordinal());
        this.dataManager.set(DP_TIMER, 0);
        stateTimer = 0;
    }

    public ItemVOTVdrive.Target getTarget() {
        ItemStack drive = this.dataManager.get(DP_DRIVE);
        return ItemVOTVdrive.getTarget(drive, world);
    }

    public void setDrive(ItemStack drive) {
        this.dataManager.set(DP_DRIVE, drive == null ? ItemStack.EMPTY : drive);
    }

    public int getStateTimer() {
        return this.dataManager.get(DP_TIMER);
    }

    public void setStateTimer(int timer) {
        this.dataManager.set(DP_TIMER, timer);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(DP_STATE, RocketState.AWAITING.ordinal());
        this.dataManager.register(DP_DRIVE, ItemStack.EMPTY);
        this.dataManager.register(DP_TIMER, 0);
        this.dataManager.register(DP_ROCKET_CAPSULE, 0);
        this.dataManager.register(DP_ROCKET_STAGECOUNT, 0);
        for(int i = 0; i < RocketStruct.MAX_STAGES; i++) {
            this.dataManager.register(DP_ROCKET_STAGE_A[i], 0);
            this.dataManager.register(DP_ROCKET_STAGE_B[i], 0);
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);

        setStateTimer(nbt.getInteger("timer"));
        setState(RocketState.values()[nbt.getInteger("state")]);

        RocketStruct loaded = RocketStruct.readFromNBT(nbt.getCompoundTag("rocket"));
        setRocket(loaded);

        if(nbt.hasKey("drive")) {
            navDrive = new ItemStack(nbt.getCompoundTag("drive"));
        } else {
            navDrive = null;
        }

        satFreq = nbt.getInteger("freq");
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);

        nbt.setInteger("timer", getStateTimer());
        nbt.setInteger("state", getState().ordinal());

        NBTTagCompound rocketTag = new NBTTagCompound();
        getRocket().writeToNBT(rocketTag);
        nbt.setTag("rocket", rocketTag);

        if(navDrive != null) {
            NBTTagCompound driveData = new NBTTagCompound();
            navDrive.writeToNBT(driveData);

            nbt.setTag("drive", driveData);
        }

        nbt.setInteger("freq", satFreq);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void printHook(RenderGameOverlayEvent.Pre event, World world, int x, int y, int z) {
        RocketState state = getState();
        if(state == RocketState.LAUNCHING
                || state == RocketState.LANDING
                || state == RocketState.TIPPING
                || state == RocketState.DOCKING
                || state == RocketState.UNDOCKING)
            return;

        RocketStruct rocket = getRocket();
        if(rocket.stages.size() == 0 && world.provider.getDimension() != SpaceConfig.orbitDimension && !isReusable()) return;

        List<String> text = new ArrayList<>();

        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;

        ItemVOTVdrive.Target from = CelestialBody.getTarget(world, (int)posX, (int)posZ);
        ItemVOTVdrive.Target to = getTarget();

        boolean canLaunch = to.body != null && state == RocketState.AWAITING;

        if(state == RocketState.NEEDSFUEL) {
            text.add(TextFormatting.RED + "Rocket has no fuel!");
        } else if(canLaunch && !rocket.hasSufficientFuel(from.body, to.body, from.inOrbit, to.inOrbit)) {
            text.add(TextFormatting.RED + "Rocket can't reach destination!");
            canLaunch = false;
        }

        if(this.getPassengers().isEmpty()) {
            text.add("Interact to enter");
        } else if(this.getPassengers().get(0) != player) {
            text.add("OCCUPIED");
        } else {
            if(to.inOrbit) {
                text.add("Destination: ORBITAL STATION");
            } else if(to.body != null) {
                text.add("Destination: " + I18nUtil.resolveKey("body." + to.body.name));
            } else {
                text.add("Destination: NO DRIVE INSTALLED");
            }

            if(canLaunch) {
                text.add("JUMP TO LAUNCH");
            } else if(state == RocketState.LANDED) {
                text.add("Insert next drive to continue");
            }

            ItemStack stack = player.getHeldItemMainhand();
            if((state == RocketState.LANDED || state == RocketState.AWAITING) && !stack.isEmpty() && stack.getItem() instanceof ItemVOTVdrive) {
                if(ItemVOTVdrive.getProcessed(stack)) {
                    text.add("Interact to swap drive");
                }
            }
        }

        ILookOverlay.printGeneric(event, "Rocket", 0xffff00, 0x404000, text);
    }

    @Override
    public boolean canBePushed() {
        return true;
    }

    @Override
    public ItemStack getMissileItemForInfo() {
        return new ItemStack(ModItemsSpace.rocket_custom);
    }

    @Override
    public List<ItemStack> getDebris() {
        return null;
    }

    @Override
    public ItemStack getDebrisRareDrop() {
        return null;
    }

    @Override
    public void init(ForgeChunkManager.Ticket ticket) {
        super.init(ticket);
    }

    @Override
    public void loadNeighboringChunks(int newChunkX, int newChunkZ) {
        if(canRide()) return;
        super.loadNeighboringChunks(newChunkX, newChunkZ);
    }

    @Override
    public void clearChunkLoader() {
        if(canRide()) return;
        super.clearChunkLoader();
    }
    @AutoRegister(name = "entity_rideable_rocket_dummy", trackingRange = 1000)
    public static class EntityRideableRocketDummy extends Entity implements ILookOverlay {

        public EntityRideableRocket parent;

        private static final DataParameter<Integer> DP_PARENT_ID =
                EntityDataManager.createKey(EntityRideableRocketDummy.class, DataSerializers.VARINT);

        public EntityRideableRocketDummy(World world) {
            super(world);
            setSize(4, 4);
        }

        public EntityRideableRocketDummy(World world, EntityRideableRocket parent) {
            this(world);
            this.parent = parent;
            this.dataManager.set(DP_PARENT_ID, parent.getEntityId());
        }

        @Override
        protected void entityInit() {
            this.dataManager.register(DP_PARENT_ID, 0);
        }

        @Override
        public void onUpdate() {
            if(!world.isRemote) {
                if(parent == null || parent.isDead) {
                    setDead();
                }
            } else {
                if(parent == null) {
                    net.minecraft.entity.Entity entity = world.getEntityByID(this.dataManager.get(DP_PARENT_ID));
                    if(entity instanceof EntityRideableRocket) {
                        parent = (EntityRideableRocket) entity;
                    }
                }
            }
        }

        @Override protected void writeEntityToNBT(@NotNull NBTTagCompound nbt) {}
        @Override public void readEntityFromNBT(@NotNull NBTTagCompound nbt) { this.setDead(); }

        @Override
        public void printHook(RenderGameOverlayEvent.Pre event, World world, int x, int y, int z) {
            if(parent == null) return;
            parent.printHook(event, world, x, y, z);
        }

        @Override
        public boolean processInitialInteract(@NotNull EntityPlayer player, @NotNull EnumHand hand) {
            if(parent == null) return false;
            return parent.processInitialInteract(player, hand);
        }

        @Override
        public boolean canBeCollidedWith() {
            return true;
        }

        @Override
        public boolean attackEntityFrom(@NotNull DamageSource source, float amount) {
            if(parent == null) return false;
            return parent.attackEntityFrom(source, amount);
        }

    }

}
