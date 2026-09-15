package com.hbmspace.dim;

import com.hbmspace.Tags;
import com.hbmspace.dim.Ike.BiomeGenIke;
import com.hbmspace.dim.dres.biome.BiomeGenBaseDres;
import com.hbmspace.dim.duna.biome.BiomeGenBaseDuna;
import com.hbmspace.dim.duna.biome.BiomeGenDunaPolar;
import com.hbmspace.dim.duna.biome.BiomeGenDunaPolarHills;
import com.hbmspace.dim.eve.biome.BiomeGenBaseEve;
import com.hbmspace.dim.laythe.biome.BiomeGenBaseLaythe;
import com.hbmspace.dim.minmus.biome.BiomeGenBaseMinmus;
import com.hbmspace.dim.moho.biome.BiomeGenBaseMoho;
import com.hbmspace.dim.moon.BiomeGenMoon;
import com.hbmspace.dim.orbit.BiomeGenOrbit;
import com.hbmspace.dim.tekto.biome.BiomeGenBaseTekto;
import com.hbmspace.util.RegistryUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Tags.MODID)
public class ModBiomesInit {

    @SubscribeEvent
    public static void preventMinmusSnow(PopulateChunkEvent.Populate evt) {
        // Th3_Sl1ze: of course I could do setTemperature(0.0f). Though Minmus is actually cold, and that supposedly removes just ice and snow in population phase
        if (evt.getType() == PopulateChunkEvent.Populate.EventType.ICE) {
            BlockPos pos = new BlockPos(evt.getChunkX() * 16 + 8, 0, evt.getChunkZ() * 16 + 8);
            // since serene seasons tends to spawn snow.. everywhere, I have to exclude it from everywhere but duna polar biomes
            if (evt.getWorld().getBiome(pos) instanceof BiomeGenBaseCelestial biome) {
                if(!(biome instanceof BiomeGenDunaPolar || biome instanceof BiomeGenDunaPolarHills)) evt.setResult(Event.Result.DENY);
            }
        }
    }

    @SubscribeEvent
    public static void registerBiomes(RegistryEvent.Register<Biome> evt){
        MinecraftForge.TERRAIN_GEN_BUS.register(ModBiomesInit.class);
        evt.getRegistry().registerAll(
                RegistryUtil.forceRegistryName(BiomeGenBaseDuna.dunaPlains, "hbm", "duna_plains"),
                RegistryUtil.forceRegistryName(BiomeGenBaseDuna.dunaLowlands, "hbm", "duna_lowlands"),
                RegistryUtil.forceRegistryName(BiomeGenBaseDuna.dunaPolar, "hbm", "duna_polar"),
                RegistryUtil.forceRegistryName(BiomeGenBaseDuna.dunaHills, "hbm", "duna_hills"),
                RegistryUtil.forceRegistryName(BiomeGenBaseDuna.dunaPolarHills, "hbm", "duna_polar_hills"),
                RegistryUtil.forceRegistryName(BiomeGenBaseDres.dresPlains, "hbm", "dres_plains"),
                RegistryUtil.forceRegistryName(BiomeGenBaseDres.dresCanyon, "hbm", "dres_canyon"),
                RegistryUtil.forceRegistryName(BiomeGenBaseEve.evePlains, "hbm", "eve_plains"),
                RegistryUtil.forceRegistryName(BiomeGenBaseEve.eveOcean, "hbm", "eve_ocean"),
                RegistryUtil.forceRegistryName(BiomeGenBaseEve.eveMountains, "hbm", "eve_mountains"),
                RegistryUtil.forceRegistryName(BiomeGenBaseEve.eveSeismicPlains, "hbm", "eve_seismic_plains"),
                RegistryUtil.forceRegistryName(BiomeGenBaseEve.eveRiver, "hbm", "eve_river"),
                RegistryUtil.forceRegistryName(BiomeGenIke.biome, "hbm", "ike"),
                RegistryUtil.forceRegistryName(BiomeGenBaseLaythe.laytheIsland, "hbm", "laythe_island"),
                RegistryUtil.forceRegistryName(BiomeGenBaseLaythe.laytheOcean, "hbm", "laythe_ocean"),
                RegistryUtil.forceRegistryName(BiomeGenBaseLaythe.laythePolar, "hbm", "laythe_polar"),
                RegistryUtil.forceRegistryName(BiomeGenBaseLaythe.laytheCoast, "hbm", "laythe_coast"),
                RegistryUtil.forceRegistryName(BiomeGenBaseMinmus.minmusPlains, "hbm", "minmus_plains"),
                RegistryUtil.forceRegistryName(BiomeGenBaseMinmus.minmusCanyon, "hbm", "minmus_canyon"),
                RegistryUtil.forceRegistryName(BiomeGenBaseMoho.mohoCrag, "hbm", "moho_crag"),
                RegistryUtil.forceRegistryName(BiomeGenBaseMoho.mohoBasalt, "hbm", "moho_basalt"),
                RegistryUtil.forceRegistryName(BiomeGenBaseMoho.mohoLavaSea, "hbm", "moho_lava_sea"),
                RegistryUtil.forceRegistryName(BiomeGenBaseMoho.mohoPlateau, "hbm", "moho_plateau"),
                RegistryUtil.forceRegistryName(BiomeGenBaseTekto.polyvinylPlains, "hbm", "tekto_polyvinyl_plains"),
                RegistryUtil.forceRegistryName(BiomeGenBaseTekto.halogenHills, "hbm", "tekto_halogen_hills"),
                RegistryUtil.forceRegistryName(BiomeGenBaseTekto.tetrachloricRiver, "hbm", "tekto_tetrachloride_river"),
                RegistryUtil.forceRegistryName(BiomeGenBaseTekto.forest, "hbm", "tekto_forest"),
                RegistryUtil.forceRegistryName(BiomeGenBaseTekto.vinylsands, "hbm", "tekto_vinyl_desert"),
                RegistryUtil.forceRegistryName(BiomeGenMoon.biome, "hbm", "moon"),
                RegistryUtil.forceRegistryName(BiomeGenOrbit.biome, "hbm", "orbit")
        );

        addTypes();
    }

    public static void addTypes()
    {
        BiomeDictionary.addTypes(BiomeGenBaseDuna.dunaPlains, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD);
        BiomeDictionary.addTypes(BiomeGenBaseDuna.dunaLowlands, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD);
        BiomeDictionary.addTypes(BiomeGenBaseDuna.dunaPolar, BiomeDictionary.Type.COLD, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.SNOWY);
        BiomeDictionary.addTypes(BiomeGenBaseDuna.dunaHills, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.HILLS);
        BiomeDictionary.addTypes(BiomeGenBaseDuna.dunaPolarHills, BiomeDictionary.Type.COLD, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.SNOWY, BiomeDictionary.Type.MOUNTAIN);
        BiomeDictionary.addTypes(BiomeGenBaseDres.dresPlains, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD);
        BiomeDictionary.addTypes(BiomeGenBaseDres.dresCanyon, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD);
        BiomeDictionary.addTypes(BiomeGenBaseEve.evePlains, BiomeDictionary.Type.HOT, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.SPOOKY);
        BiomeDictionary.addTypes(BiomeGenBaseEve.eveSeismicPlains, BiomeDictionary.Type.HOT, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.SPOOKY);
        BiomeDictionary.addTypes(BiomeGenBaseEve.eveOcean, BiomeDictionary.Type.HOT, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.SPOOKY);
        BiomeDictionary.addTypes(BiomeGenBaseEve.eveRiver, BiomeDictionary.Type.HOT, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.SPOOKY);
        BiomeDictionary.addTypes(BiomeGenBaseEve.eveMountains, BiomeDictionary.Type.HOT, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.SPOOKY);
        BiomeDictionary.addTypes(BiomeGenIke.biome, BiomeDictionary.Type.COLD, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD);
        BiomeDictionary.addTypes(BiomeGenBaseLaythe.laytheIsland, BiomeDictionary.Type.COLD, BiomeDictionary.Type.WET, BiomeDictionary.Type.DENSE, BiomeDictionary.Type.SPOOKY);
        BiomeDictionary.addTypes(BiomeGenBaseLaythe.laytheOcean, BiomeDictionary.Type.COLD, BiomeDictionary.Type.WET, BiomeDictionary.Type.DENSE, BiomeDictionary.Type.SPOOKY);
        BiomeDictionary.addTypes(BiomeGenBaseLaythe.laytheCoast, BiomeDictionary.Type.COLD, BiomeDictionary.Type.WET, BiomeDictionary.Type.DENSE, BiomeDictionary.Type.SPOOKY);
        BiomeDictionary.addTypes(BiomeGenBaseLaythe.laythePolar, BiomeDictionary.Type.COLD, BiomeDictionary.Type.WET, BiomeDictionary.Type.DENSE, BiomeDictionary.Type.SPOOKY);
        BiomeDictionary.addTypes(BiomeGenBaseMinmus.minmusCanyon, BiomeDictionary.Type.COLD, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.SNOWY, BiomeDictionary.Type.MOUNTAIN);
        BiomeDictionary.addTypes(BiomeGenBaseMinmus.minmusPlains, BiomeDictionary.Type.COLD, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.SNOWY, BiomeDictionary.Type.MOUNTAIN);
        BiomeDictionary.addTypes(BiomeGenBaseMoho.mohoBasalt, BiomeDictionary.Type.HOT, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.SPOOKY);
        BiomeDictionary.addTypes(BiomeGenBaseMoho.mohoCrag, BiomeDictionary.Type.HOT, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.SPOOKY);
        BiomeDictionary.addTypes(BiomeGenBaseMoho.mohoPlateau, BiomeDictionary.Type.HOT, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.SPOOKY);
        BiomeDictionary.addTypes(BiomeGenBaseMoho.mohoLavaSea, BiomeDictionary.Type.HOT, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.SPOOKY);
        BiomeDictionary.addTypes(BiomeGenBaseTekto.polyvinylPlains, BiomeDictionary.Type.HOT, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.SPOOKY);
        BiomeDictionary.addTypes(BiomeGenBaseTekto.halogenHills, BiomeDictionary.Type.HOT, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.SPOOKY);
        BiomeDictionary.addTypes(BiomeGenBaseTekto.tetrachloricRiver, BiomeDictionary.Type.HOT, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.SPOOKY);
        BiomeDictionary.addTypes(BiomeGenBaseTekto.forest, BiomeDictionary.Type.HOT, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.SPOOKY);
        BiomeDictionary.addTypes(BiomeGenBaseTekto.vinylsands, BiomeDictionary.Type.HOT, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.SPOOKY);
        BiomeDictionary.addTypes(BiomeGenMoon.biome, BiomeDictionary.Type.COLD, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD);
        BiomeDictionary.addTypes(BiomeGenOrbit.biome, BiomeDictionary.Type.COLD, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD);

    }
}
