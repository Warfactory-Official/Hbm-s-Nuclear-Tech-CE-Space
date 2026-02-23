package com.hbmspace.dim;

import com.hbmspace.Tags;
import com.hbmspace.dim.Ike.BiomeGenIke;
import com.hbmspace.dim.dres.biome.BiomeGenBaseDres;
import com.hbmspace.dim.duna.biome.BiomeGenBaseDuna;
import com.hbmspace.dim.eve.biome.BiomeGenBaseEve;
import com.hbmspace.dim.laythe.biome.BiomeGenBaseLaythe;
import com.hbmspace.dim.minmus.biome.BiomeGenBaseMinmus;
import com.hbmspace.dim.moho.biome.BiomeGenBaseMoho;
import com.hbmspace.dim.moon.BiomeGenMoon;
import com.hbmspace.dim.orbit.BiomeGenOrbit;
import com.hbmspace.dim.tekto.biome.BiomeGenBaseTekto;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

@Mod.EventBusSubscriber(modid = Tags.MODID)
public class ModBiomesInit {
    @SubscribeEvent
    public static void registerBiomes(RegistryEvent.Register<Biome> evt){
        evt.getRegistry().registerAll(
                BiomeGenBaseDuna.dunaPlains.setRegistryName("hbm", "duna_plains"),
                BiomeGenBaseDuna.dunaLowlands.setRegistryName("hbm", "duna_lowlands"),
                BiomeGenBaseDuna.dunaPolar.setRegistryName("hbm", "duna_polar"),
                BiomeGenBaseDuna.dunaHills.setRegistryName("hbm", "duna_hills"),
                BiomeGenBaseDuna.dunaPolarHills.setRegistryName("hbm", "duna_polar_hills"),
                BiomeGenBaseDres.dresPlains.setRegistryName("hbm", "dres_plains"),
                BiomeGenBaseDres.dresCanyon.setRegistryName("hbm", "dres_canyon"),
                BiomeGenBaseEve.evePlains.setRegistryName("hbm", "eve_plains"),
                BiomeGenBaseEve.eveOcean.setRegistryName("hbm", "eve_ocean"),
                BiomeGenBaseEve.eveMountains.setRegistryName("hbm", "eve_mountains"),
                BiomeGenBaseEve.eveSeismicPlains.setRegistryName("hbm", "eve_seismic_plains"),
                BiomeGenBaseEve.eveRiver.setRegistryName("hbm", "eve_river"),
                BiomeGenIke.biome.setRegistryName("hbm", "ike"),
                BiomeGenBaseLaythe.laytheIsland.setRegistryName("hbm", "laythe_island"),
                BiomeGenBaseLaythe.laytheOcean.setRegistryName("hbm", "laythe_ocean"),
                BiomeGenBaseLaythe.laythePolar.setRegistryName("hbm", "laythe_polar"),
                BiomeGenBaseLaythe.laytheCoast.setRegistryName("hbm", "laythe_coast"),
                BiomeGenBaseMinmus.minmusPlains.setRegistryName("hbm", "minmus_plains"),
                BiomeGenBaseMinmus.minmusCanyon.setRegistryName("hbm", "minmus_canyon"),
                BiomeGenBaseMoho.mohoCrag.setRegistryName("hbm", "moho_crag"),
                BiomeGenBaseMoho.mohoBasalt.setRegistryName("hbm", "moho_basalt"),
                BiomeGenBaseMoho.mohoLavaSea.setRegistryName("hbm", "moho_lava_sea"),
                BiomeGenBaseMoho.mohoPlateau.setRegistryName("hbm", "moho_plateau"),
                BiomeGenBaseTekto.polyvinylPlains.setRegistryName("hbm", "tekto_polyvinyl_plains"),
                BiomeGenBaseTekto.halogenHills.setRegistryName("hbm", "tekto_halogen_hills"),
                BiomeGenBaseTekto.tetrachloricRiver.setRegistryName("hbm", "tekto_tetrachloride_river"),
                BiomeGenBaseTekto.forest.setRegistryName("hbm", "tekto_forest"),
                BiomeGenBaseTekto.vinylsands.setRegistryName("hbm", "tekto_vinyl_desert"),
                BiomeGenMoon.biome.setRegistryName("hbm", "moon"),
                BiomeGenOrbit.biome.setRegistryName("hbm", "orbit")
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
