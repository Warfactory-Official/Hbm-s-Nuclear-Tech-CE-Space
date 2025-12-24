package com.hbmspace.main;

import com.hbm.packet.PacketDispatcher;
import com.hbm.packet.toclient.PlayerInformPacketLegacy;
import com.hbm.util.AstronomyUtil;
import com.hbm.util.ChatBuilder;
import com.hbm.util.ParticleUtil;
import com.hbmspace.Tags;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.capability.HbmLivingCapabilitySpace;
import com.hbmspace.config.SpaceConfig;
import com.hbmspace.dim.*;
import com.hbmspace.dim.orbit.OrbitalStation;
import com.hbmspace.dim.orbit.WorldProviderOrbit;
import com.hbmspace.dim.trait.CBT_Atmosphere;
import com.hbmspace.dim.trait.CBT_Lights;
import com.hbmspace.dim.trait.CelestialBodyTrait;
import com.hbmspace.entity.missile.EntityRideableRocket;
import com.hbmspace.handler.INetherAccessor;
import com.hbmspace.handler.atmosphere.ChunkAtmosphereManager;
import com.hbmspace.items.ModItemsSpace;
import com.hbmspace.lib.HBMSpaceSoundHandler;
import com.hbmspace.lib.ModDamageSourceSpace;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockFire;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(modid = Tags.MODID)
public class ModEventHandler {

    public static final ResourceLocation ENT_HBM_PROP_ID = new ResourceLocation(Tags.MODID, "HBMLIVINGPROPS");

    public static Random rand = new Random();

    @SubscribeEvent
    public static void attachRadCap(AttachCapabilitiesEvent<Entity> e) {
        if (e.getObject() instanceof EntityLivingBase)
            e.addCapability(ENT_HBM_PROP_ID, new HbmLivingCapabilitySpace.EntityHbmPropsProvider());
    }

    @SubscribeEvent
    public static void soundRegistering(RegistryEvent.Register<SoundEvent> evt) {
        for (SoundEvent e : HBMSpaceSoundHandler.ALL_SOUNDS) {
             evt.getRegistry().register(e);
        }
    }

    @SubscribeEvent
    public static void onBlockPlaced(BlockEvent.PlaceEvent event) {
        World world = event.getWorld();
        if (world.isRemote) return;

        Block block = event.getPlacedBlock().getBlock();
        BlockPos pos = event.getPos();

        boolean placeCancelled = ChunkAtmosphereManager.proxy.runEffectsOnBlock(world, block, pos.getX(), pos.getY(), pos.getZ());
        if (placeCancelled) return;

        if (block instanceof IGrowable) {
            ChunkAtmosphereManager.proxy.trackPlant(world, pos.getX(), pos.getY(), pos.getZ());
        }

        if (SpaceConfig.allowNetherPortals && world.provider.getDimension() > 1 && block instanceof BlockFire) {
            Blocks.PORTAL.trySpawnPortal(world, pos);
        }

        // sneaky sneaky space furnace
        /*if (block == Blocks.FURNACE) {
            world.setBlockState(pos, ModBlocks.furnace.getDefaultState(), 2);
            ModBlocks.furnace.onBlockPlacedBy(world, pos, event.getPlayer(), event.getItemInHand());
        }*/

        if (pos.getY() >= world.provider.getHorizon()) {
            if (block.getLightValue(world.getBlockState(pos), world, pos) > 10) {
                CelestialBody body = CelestialBody.getBody(world);
                CBT_Lights lights = body.getTrait(CBT_Lights.class);

                if (lights == null) lights = new CBT_Lights();
                lights.addLight(block, pos.getX(), pos.getY(), pos.getZ());

                body.modifyTraits(lights);
            }
        }
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if(event.getPos().getY() > event.getWorld().provider.getHorizon()) {
            if(event.getState().getLightValue() > 10) {
                // Placing blocks onto beams counts as a break, but doesn't reduce light count
                // TODO
                /*if(!(event.getState().getBlock() instanceof BlockBeamBase)) {
                    CelestialBody body = CelestialBody.getBody(event.world);
                    CBT_Lights lights = body.getTrait(CBT_Lights.class);

                    if(lights == null) lights = new CBT_Lights();
                    lights.removeLight(event.getState().getBlock(), event.getPos().getX(), event.getPos().getY(), event.getPos().getZ());

                    body.modifyTraits(lights);
                }*/

            }
        }
    }

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        boolean isFlying = event.getEntity() instanceof EntityPlayer && ((EntityPlayer) event.getEntity()).capabilities.isFlying;

        if(!isFlying) {
            float gravity = CelestialBody.getGravity(event.getEntityLiving());

            if(gravity == 0) {
                event.getEntityLiving().motionY /= 0.98F;
                event.getEntityLiving().motionY += (AstronomyUtil.STANDARD_GRAVITY / 20F);

                if(event.getEntityLiving() instanceof EntityPlayer player) {
                    if(player.isSneaking()) event.getEntityLiving().motionY -= 0.01F;
                    if(player.isJumping) event.getEntityLiving().motionY += 0.01F;
                } else if(event.getEntity() instanceof EntityChicken) {
                    event.getEntityLiving().motionY = 0;
                }

                event.getEntityLiving().motionY *= 0.91F;
            } else if(!event.getEntityLiving().isInWater() && event.getEntityLiving().ticksExisted > 20 && (gravity < 1.5F || gravity > 1.7F)) {
                // If gravity is basically the same as normal, do nothing
                // Also do nothing in water, or if we've been alive less than a second (so we don't glitch into the ground)

                // Minimum gravity to prevent floating bug
                if(gravity < 0.2F) gravity = 0.2F;

                // Undo falling, and add our intended falling speed
                // On high gravity planets, only apply falling speed when descending, so we can still jump up single blocks
                if((gravity < 1.5F || event.getEntityLiving().motionY < 0) && !(event.getEntity() instanceof EntityChicken)) {
                    event.getEntityLiving().motionY /= 0.98F;
                    event.getEntityLiving().motionY += (AstronomyUtil.STANDARD_GRAVITY / 20F);
                    event.getEntityLiving().motionY -= (gravity / 20F);
                    event.getEntityLiving().motionY *= 0.98F;
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEntityFall(LivingFallEvent event) {

        EntityLivingBase e = event.getEntityLiving();

        float gravity = CelestialBody.getGravity(e);

        // Reduce fall damage on low gravity bodies
        if(gravity < 0.3F) {
            event.setDistance(0);
        } else if(gravity < 1.5F) {
            event.setDistance(event.getDistance() * gravity / AstronomyUtil.STANDARD_GRAVITY);
        }
    }

    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        World world = entity.world;

        if (world.isRemote) {
            return;
        }

        if (event.getSource() == ModDamageSourceSpace.eve) {
            BlockPos basePos = new BlockPos(entity.posX, entity.posY, entity.posZ);

            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    for (int k = -1; k < 2; k++) {
                        BlockPos pos = basePos.add(i, j, k);

                        if (world.isAirBlock(pos)) {
                            if (ModBlocksSpace.flesh_block.canPlaceBlockAt(world, pos)) {
                                world.setBlockState(pos, ModBlocksSpace.flesh_block.getDefaultState(), 3);
                            }
                        }
                    }
                }
            }
        }

        if (entity instanceof EntityVillager && entity.getRNG().nextInt(1) == 0) {
            entity.entityDropItem(new ItemStack(ModItemsSpace.flesh, 5), 0.0F);
        }
    }

    @SubscribeEvent
    public static void onBucketUse(FillBucketEvent event) {
        World world = event.getWorld();
        if (world.isRemote) return;

        RayTraceResult target = event.getTarget();
        if (target == null || target.typeOfHit != RayTraceResult.Type.BLOCK) return;

        if (!(world.provider instanceof WorldProviderCelestial) && !(world.provider instanceof WorldProviderOrbit)) return;

        ItemStack current = event.getEmptyBucket();
        if (!current.isEmpty() && current.getItem() == Items.WATER_BUCKET) {
            EnumFacing dir = target.sideHit;
            BlockPos placePos = target.getBlockPos().offset(dir);

            CBT_Atmosphere atmosphere = ChunkAtmosphereManager.proxy.getAtmosphere(world, placePos.getX(), placePos.getY(), placePos.getZ());
            boolean hasLiquidPressure = ChunkAtmosphereManager.proxy.hasLiquidPressure(atmosphere);

            /*if (Loader.isModLoaded(Compat.MOD_COFH)) {
                if (!hasLiquidPressure) {
                    event.setCanceled(true);
                }
            } else {*/
                if (hasLiquidPressure) {
                    // for those curious ppl: YES I KNOW ABOUT ACCESSTRANSFORMERS
                    // my gradle simply refuses to work with it and doesn't even try reading lines in the _at.cfg file
                    ((INetherAccessor) world.provider).setNether(true);
                }
            //}
        }
    }

    @SubscribeEvent
    public static void worldTick(TickEvent.WorldTickEvent event) {
        if(event.world == null) return;
        if(!event.world.isRemote){
            List<Entity> loadedEntityList = new ArrayList<>(event.world.loadedEntityList); // ConcurrentModificationException my balls

            for (Entity e : loadedEntityList) {

                if (e instanceof EntityPlayer player) {

                    // handle dismount events, or our players will splat upon leaving tall rockets
                    Entity riding = player.getRidingEntity();
                    if (riding instanceof EntityRideableRocket rocket && player.isSneaking()) {

                        if (player.isSneaking()) {
                            // Prevent leaving a rocket in motion, for safety
                            if (rocket.canExitCapsule() || rocket.forceExitTimer >= 60) {
                                boolean inOrbit = event.world.provider instanceof WorldProviderOrbit;
                                Entity ridingEntity = player.getRidingEntity();
                                float prevHeight = ridingEntity.height;

                                ridingEntity.height = inOrbit ? (ridingEntity.height + 1.0F) : 1.0F;
                                player.dismountRidingEntity();
                                if (!inOrbit) player.setPositionAndUpdate(player.posX + 2, player.posY, player.posZ);
                                ridingEntity.height = prevHeight;
                            } else {
                                rocket.forceExitTimer++;
                            }

                            player.setSneaking(false);
                        } else {
                            rocket.forceExitTimer = 0;
                        }
                    }
                }
            }
        }
        if (event.phase == TickEvent.Phase.END) {
            CelestialTeleporter.runQueuedTeleport();
            if (event.world.getTotalWorldTime() % 20 == 0) {
                CelestialBody.updateChemistry(event.world);
            }
        }
        if (event.phase == TickEvent.Phase.START && event.world.provider instanceof WorldProviderCelestial && event.world.provider.getDimension() != 0) {
            if (event.world.getGameRules().getBoolean("doDaylightCycle")) {
                event.world.provider.setWorldTime(event.world.provider.getWorldTime() + 1L);
            }
        }
        if (event.phase == TickEvent.Phase.START) {
            updateWaterOpacity(event.world);
        }

        if(event.phase == TickEvent.Phase.START && event.world.provider.getDimension() == SpaceConfig.orbitDimension) {
            for(Object o : event.world.loadedEntityList) {
                if(o instanceof EntityItem item) {
                    item.motionX *= 0.9D;
                    item.motionY = 0.03999999910593033D; // when entity gravity is applied, this becomes exactly 0
                    item.motionZ *= 0.9D;
                }
            }
        }
    }

    private static void updateWaterOpacity(World world) {
        // Per world water opacity!
        int waterOpacity = 3;
        if (world.provider instanceof WorldProviderCelestial) {
            waterOpacity = ((WorldProviderCelestial) world.provider).getWaterOpacity();
        }

        Blocks.WATER.setLightOpacity(waterOpacity);
        Blocks.FLOWING_WATER.setLightOpacity(waterOpacity);
    }
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;
        if (player.posY > 300 && player.posY < 1000) {
            Vec3d vec = new Vec3d(3 * rand.nextDouble(), 0, 0);
            CBT_Atmosphere thatmosphere = CelestialBody.getTrait(player.world, CBT_Atmosphere.class);

            if (thatmosphere != null && thatmosphere.getPressure() > 0.05 && !player.isRiding()) {
                if (Math.abs(player.motionX) > 1 || Math.abs(player.motionY) > 1 || Math.abs(player.motionZ) > 1) {
                    ParticleUtil.spawnGasFlame(player.world, player.posX - 1 + vec.x, player.posY + vec.y, player.posZ + vec.z, 0, 0, 0);
                }
            }
        }

        if (!player.world.isRemote && event.phase == TickEvent.Phase.START) {

            // Check for players attempting to cross over to another orbital grid
            if (player.world.provider instanceof WorldProviderOrbit && !(player.getRidingEntity() instanceof EntityRideableRocket)) {
                double rx = Math.abs(player.posX) % OrbitalStation.STATION_SIZE;
                double rz = Math.abs(player.posZ) % OrbitalStation.STATION_SIZE;

                int minBuffer = OrbitalStation.BUFFER_SIZE;
                int maxBuffer = OrbitalStation.STATION_SIZE - minBuffer;

                int minWarning = OrbitalStation.BUFFER_SIZE + OrbitalStation.WARNING_SIZE;
                int maxWarning = OrbitalStation.STATION_SIZE - minWarning;

                if (player instanceof EntityPlayerMP && (rx < minWarning || rx > maxWarning || rz < minWarning || rz > maxWarning)) {
                    PacketDispatcher.wrapper.sendTo(
                            new PlayerInformPacketLegacy(
                                    ChatBuilder.start("").nextTranslation("info.orbitfall").color(TextFormatting.RED).flush(),
                                    12,
                                    3000
                            ),
                            (EntityPlayerMP) player
                    );
                }

                if (rx < minBuffer || rx > maxBuffer || rz < minBuffer || rz > maxBuffer) {
                    OrbitalStation station = OrbitalStation.getStationFromPosition((int) player.posX, (int) player.posZ);
                    CelestialTeleporter.teleport(
                            player,
                            station.orbiting.dimensionId,
                            rand.nextInt(SpaceConfig.maxProbeDistance * 2) - SpaceConfig.maxProbeDistance,
                            800,
                            rand.nextInt(SpaceConfig.maxProbeDistance * 2) - SpaceConfig.maxProbeDistance,
                            false
                    );
                }
            }

            // keep Nether teleports localized
            // this effectively turns the Nether into a shared pocket dimension, but disallows using it to travel between celestial bodies
            /*if (player.inPortal && player instanceof EntityPlayerMP) {
                MinecraftServer minecraftserver = ((WorldServer) player.world).getMinecraftServer();
                int maxTime = player.getMaxInPortalTime();

                // portalCounter is protected in 1.12.2, so we must access it reflectively
                int portalCounter = net.minecraftforge.fml.common.ObfuscationReflectionHelper.getPrivateValue(
                        Entity.class, player, "portalCounter", "field_82153_h"
                );

                if (minecraftserver.getAllowNether() && player.getRidingEntity() == null && portalCounter + 1 >= maxTime) {
                    net.minecraftforge.fml.common.ObfuscationReflectionHelper.setPrivateValue(
                            Entity.class, player, maxTime, "portalCounter", "field_82153_h"
                    );

                    player.timeUntilPortal = player.getPortalCooldown();

                    HbmLivingCapabilitySpace props = HbmPlayerProps.getData(player);
                    int targetDimension = -1;

                    if (player.world.provider.getDimension() == -1) {
                        targetDimension = props.lastDimension;
                    } else {
                        props.lastDimension = player.world.provider.getDimension();
                    }

                    ((EntityPlayerMP) player).changeDimension(targetDimension);
                    player.inPortal = false;
                }
            }*/
        }
    }

    @SubscribeEvent
    public static void preventOrganicSpawn(DecorateBiomeEvent.Decorate event) {
        // In space, no one can hear you shroom
        if(!(event.getWorld().provider instanceof WorldProviderCelestial celestial)) return;

        if(celestial.hasLife()) return; // Except on Laythe

        switch(event.getType()) {
            case BIG_SHROOM:
            case CACTUS:
            case DEAD_BUSH:
            case LILYPAD:
            case FLOWERS:
            case GRASS:
            case PUMPKIN:
            case REED:
            case SHROOM:
            case TREE:
                event.setResult(Event.Result.DENY);
            default:
        }
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if(event.phase == TickEvent.Phase.START) {
            for(CelestialBody body : CelestialBody.getAllBodies()) {
                List<CelestialBodyTrait> traits = new ArrayList<>(body.getTraits().values());
                for (CelestialBodyTrait trait : traits) {
                    trait.update(false);
                }
            }

            // Dyson Swarms
            CelestialBody.updateSwarms();
        }
    }

    // This is really fucky, but ensures we can respawn safely on celestial bodies
    // and prevents beds exploding
    @SubscribeEvent
    public static void onTrySleep(PlayerInteractEvent.RightClickBlock event) {
        if(event.getWorld().isRemote) return;
        if(event.getWorld().provider.getDimension() == 0) return;
        if(!(event.getWorld().provider instanceof WorldProviderCelestial) && !(event.getWorld().provider instanceof WorldProviderOrbit)) return;

        if(event.getWorld().getBlockState(event.getPos()).getBlock() instanceof BlockBed) {
            WorldProviderCelestial.attemptingSleep = true;
        }
    }

    @SubscribeEvent
    public static void onGenerateOre(OreGenEvent.GenerateMinable event) {
        if (event.getWorld().provider instanceof WorldProviderCelestial && event.getWorld().provider.getDimension() != 0) {
            WorldGeneratorCelestial.onGenerateOre(event);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLoad(WorldEvent.Load event) {
        if (event.getWorld().provider.getDimension() == 0) {
            WorldProviderEarth customProvider = new WorldProviderEarth();
            customProvider.setWorld(event.getWorld());
            customProvider.setDimension(0);
            event.getWorld().provider = customProvider;
        }
    }

    @SubscribeEvent
    public static void onEntityMount(EntityMountEvent event) {
        if (event.isDismounting() && event.getEntityBeingMounted() instanceof EntityRideableRocket rocket) {
            if (rocket.getState() == EntityRideableRocket.RocketState.LAUNCHING) {
                event.setCanceled(true);
            }
        }
    }

    // TODO
    // I still don't fucking know how to deal with that without doing any atrocities
    /*@SubscribeEvent
    public static void setFish(EntityJoinWorldEvent event) {
        if(!(event.entity instanceof EntityFishHook)) return;

        updateFish(event.world);
    }

    private static ArrayList<WeightedRandomFishable> overworldFish;
    private static ArrayList<RandomFish> overworldJunk;
    private static ArrayList<WeightedRandomFishable> overworldTreasure;

    // Removes all the existing values from the fishing loot tables and replaces them per dimension
    public static void updateFish(World world) {
        if(overworldFish == null) {
            overworldFish = new ArrayList<>();
            overworldJunk = new ArrayList<>();
            overworldTreasure = new ArrayList<>();

            FishingHooks.removeFish((fishable) -> { overworldFish.add(fishable); return false; });
            FishingHooks.removeJunk((fishable) -> { overworldJunk.add(fishable); return false; });
            FishingHooks.removeTreasure((fishable) -> { overworldTreasure.add(fishable); return false; });
        } else {
            FishingHooks.removeFish((fishable) -> { return false; });
            FishingHooks.removeJunk((fishable) -> { return false; });
            FishingHooks.removeTreasure((fishable) -> { return false; });
        }

        if(world.provider instanceof WorldProviderCelestial && world.provider.dimensionId != 0) {
            WorldProviderCelestial provider = (WorldProviderCelestial) world.provider;
            ArrayList<WeightedRandomFishable> fish = provider.getFish();
            ArrayList<WeightedRandomFishable> junk = provider.getJunk();
            ArrayList<WeightedRandomFishable> treasure = provider.getTreasure();
            if(fish == null) fish = overworldFish;
            if(junk == null) junk = overworldJunk;
            if(treasure == null) treasure = overworldTreasure;
            for(WeightedRandomFishable fishable : fish) FishingHooks.addFish(fishable);
            for(WeightedRandomFishable fishable : junk) FishingHooks.addJunk(fishable);
            for(WeightedRandomFishable fishable : treasure) FishingHooks.addTreasure(fishable);
        } else {
            for(WeightedRandomFishable fishable : overworldFish) FishingHooks.addFish(fishable);
            for(WeightedRandomFishable fishable : overworldJunk) FishingHooks.addJunk(fishable);
            for(WeightedRandomFishable fishable : overworldTreasure) FishingHooks.addTreasure(fishable);
        }
    }*/
}
