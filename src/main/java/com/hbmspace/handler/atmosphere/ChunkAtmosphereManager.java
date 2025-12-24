package com.hbmspace.handler.atmosphere;

import com.hbmspace.Tags;
import net.minecraftforge.event.terraingen.SaplingGrowTreeEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
@Mod.EventBusSubscriber(modid = Tags.MODID)
public class ChunkAtmosphereManager {
    
    public static ChunkAtmosphereHandler proxy = new ChunkAtmosphereHandler();

    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load event) {
        proxy.receiveWorldLoad(event);
    }

    @SubscribeEvent
    public static void onWorldUnload(WorldEvent.Unload event) {
        proxy.receiveWorldUnload(event);
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event) {
        proxy.receiveWorldTick(event);
    }

    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.PlaceEvent event) {
        proxy.receiveBlockPlaced(event);
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        proxy.receiveBlockBroken(event);
    }

    @SubscribeEvent
    public static void onDetonate(ExplosionEvent.Detonate event) {
        proxy.receiveDetonate(event);
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        proxy.receiveServerTick(event);
    }

    @SubscribeEvent
    public void onTreeGrow(SaplingGrowTreeEvent event) {
        proxy.receiveTreeGrow(event);
    }

}
