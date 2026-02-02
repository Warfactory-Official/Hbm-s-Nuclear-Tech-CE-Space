package com.hbmspace.main;

import com.hbm.entity.logic.IChunkLoader;
import com.hbm.handler.GuiHandler;
import com.hbm.items.ModItems;
import com.hbmspace.Tags;
import com.hbmspace.accessors.IHaveCorrosionProtAccessor;
import com.hbmspace.blocks.BlockEnumsSpace;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.blocks.fluid.ModFluidsSpace;
import com.hbmspace.capability.HbmLivingCapabilitySpace;
import com.hbmspace.config.SpaceConfig;
import com.hbmspace.config.WorldConfigSpace;
import com.hbmspace.dim.CommandSpaceTP;
import com.hbmspace.dim.SolarSystem;
import com.hbmspace.inventory.OreDictManagerSpace;
import com.hbmspace.inventory.recipes.tweakers.RecipeTweakerManager;
import com.hbmspace.items.ModItemsSpace;
import com.hbmspace.items.enums.EnumAddonWatzTypes;
import com.hbmspace.items.weapon.ItemCustomMissilePart;
import com.hbmspace.lib.HBMSpaceSoundHandler;
import com.hbmspace.packet.PacketRegistry;
import com.hbmspace.potion.HbmPotion;
import com.hbmspace.render.misc.RocketPart;
import com.hbmspace.world.PlanetGen;
import com.hbmspace.world.feature.OreLayer3DSpace;
import net.minecraft.item.Item;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Logger;

import java.io.File;

/**
 * Okay, so if you read this
 * It's mostly separated NTM:Space content from NTM:CE, which I did put on a separate repo. Hopefully that won't take TOO much time.
 * I will try to mostly repeat the structure as it's just comfortable for me
 *
 * "It's fun, after all these times when you've killed me, and now.. all I have to do is to kill you **once**"
 *
 * @author Th3_Sl1ze
*/
@Mod(modid = Tags.MODID, version = Tags.VERSION, name = Tags.MODNAME, acceptedMinecraftVersions = "[1.12.2]", dependencies = "required-after:hbm@[1.5.0.0,);required-after:mixinbooter@[10.6,)")
@Mod.EventBusSubscriber
public class SpaceMain {

    @SidedProxy(clientSide = "com.hbmspace.main.ClientProxy", serverSide = "com.hbmspace.main.ServerProxy")
    public static ServerProxy proxy;
    @Mod.Instance(Tags.MODID)
    public static SpaceMain instance;
    public static Logger logger;

    static {
        HBMSpaceSoundHandler.init();
    }

    @SubscribeEvent
    public static void onRegisterItems(RegistryEvent.Register<Item> event) {
        ModItemsSpace.swapStackSizes(event);
        if (ModItemsSpace.insert_cmb instanceof IHaveCorrosionProtAccessor cmb_corprot) cmb_corprot.withCorrosionProtection();
        ModItems.ALL_ITEMS.remove(ModItemsSpace.insert_cmb);
        ModItemsSpace.ALL_ITEMS.add(ModItemsSpace.insert_cmb);
        event.getRegistry().register(ModItemsSpace.insert_cmb);
    }
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        if (logger == null)
            logger = event.getModLog();


        reloadConfig();

        CapabilityManager.INSTANCE.register(HbmLivingCapabilitySpace.IEntityHbmProps.class, new HbmLivingCapabilitySpace.EntityHbmPropsStorage(), HbmLivingCapabilitySpace.EntityHbmProps.FACTORY);

        OreDictManagerSpace.registerGroups();
        SolarSystem.init();
        HbmPotion.init();
        EnumAddonWatzTypes.init();
        ModFluidsSpace.init();
        ModItemsSpace.preInit();
        ModBlocksSpace.preInit();

        proxy.registerRenderInfo();
        proxy.preInit(event);

        AutoRegistrySpace.registerTileEntities();
        AutoRegistrySpace.loadAuxiliaryData();
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());

        OreDictManagerSpace.registerOres();

        int i = 0;
        AutoRegistrySpace.registerEntities(i);
        ItemCustomMissilePart.initSpaceThrusters();
        PacketRegistry.preInit();

        ForgeChunkManager.setForcedChunkLoadingCallback(this, (tickets, world) -> {
            for(ForgeChunkManager.Ticket ticket : tickets) {
                if(ticket.getType() == ForgeChunkManager.Type.NORMAL) {
                    ChunkLoaderManager.loadTicket(world, ticket);
                    return;
                }

                if(ticket.getEntity() instanceof IChunkLoader) {
                    ((IChunkLoader) ticket.getEntity()).init(ticket);
                }
            }
        });
    }


    public static void reloadConfig() {
        Configuration config = new Configuration(new File(proxy.getDataDir().getPath() + "/config/hbm/hbm_space.cfg"));
        config.load();
        WorldConfigSpace.loadFromConfig(config);
        SpaceConfig.loadFromConfig(config);
        config.save();
    }
    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        ModFluidsSpace.setFromRegistry();

        new OreLayer3DSpace(ModBlocksSpace.stone_resource, BlockEnumsSpace.EnumStoneType.CONGLOMERATE.ordinal()).setDimension(SpaceConfig.moonDimension).setScaleH(0.04D).setScaleV(0.25D).setThreshold(220);
        new OreLayer3DSpace(ModBlocksSpace.stone_resource, BlockEnumsSpace.EnumStoneType.CONGLOMERATE.ordinal()).setDimension(SpaceConfig.ikeDimension).setScaleH(0.04D).setScaleV(0.25D).setThreshold(220);
        new OreLayer3DSpace(ModBlocksSpace.stone_resource, BlockEnumsSpace.EnumStoneType.CONGLOMERATE.ordinal()).setDimension(SpaceConfig.minmusDimension).setScaleH(0.04D).setScaleV(0.25D).setThreshold(220);

        PlanetGen.init();
        proxy.postInit(event);
        if(event.getSide() == Side.SERVER) RocketPart.registerServerParts(); // fuck me, parts were registered on client but NOT on server

        RecipeTweakerManager.initRecipeTweakers();
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent evt) {
        evt.registerServerCommand(new CommandSpaceTP());
    }
}
