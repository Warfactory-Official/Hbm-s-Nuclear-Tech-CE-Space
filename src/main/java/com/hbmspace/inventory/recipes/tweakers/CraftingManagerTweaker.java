package com.hbmspace.inventory.recipes.tweakers;

import com.hbm.blocks.ModBlocks;
import com.hbm.blocks.generic.BlockAbsorber;
import com.hbm.config.GeneralConfig;
import com.hbm.crafting.MineralRecipes;
import com.hbm.crafting.RodRecipes;
import com.hbm.inventory.OreDictManager;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.material.MaterialShapes;
import com.hbm.items.ItemEnums;
import com.hbm.items.ModItems;
import com.hbm.items.tool.ItemConveyorWand;
import com.hbm.main.CraftingManager;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.enums.EnumAddonWatzTypes;
import com.hbmspace.items.ModItemsSpace;
import com.hbmspace.items.enums.ItemEnumsSpace;
import com.hbmspace.util.RecipeUtil;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import static com.hbm.crafting.MineralRecipes.addBilletToIngot;
import static com.hbm.crafting.RodRecipes.addPellet;
import static com.hbm.inventory.OreDictManager.*;
import static com.hbmspace.inventory.OreDictManagerSpace.*;

public class CraftingManagerTweaker extends CraftingManager {

    public static void tweak() {
        SmeltingRecipesTweaker.init();

        /* ArmorRecipes */

        // Life support
        CraftingManager.addRecipeAuto(new ItemStack(ModItemsSpace.oxy_plss, 1), "AA", "TC", "RR", 'A', AL.plate(), 'T', ModItems.tank_steel, 'C', OreDictManager.DictFrame.fromOne(ModItems.circuit, ItemEnums.EnumCircuitType.BASIC), 'R', RUBBER.ingot());

        // I AM A MAN THAT'S MADE OF MEAT
        // YOU'RE ON THE INTERNET LOOKING AT

        //Feet
        CraftingManager.addRecipeAuto(new ItemStack(ModItemsSpace.flippers, 1), "R R", "R R", 'R', RUBBER.ingot());
        CraftingManager.addRecipeAuto(new ItemStack(ModItemsSpace.heavy_boots, 1), "L L", "S S", 'L', Items.LEATHER, 'S', STEEL.ingot());

        /* ConsumableRecipes */

        CraftingManager.addShapelessAuto(new ItemStack(ModItemsSpace.flesh_burger), Items.BREAD, ModItemsSpace.grilled_flesh);
        RecipeUtil.replaceShapelessAuto(new ItemStack(ModItems.coffee), hack, ModItemsSpace.powder_coffee, Items.MILK_BUCKET, Items.POTIONITEM, Items.SUGAR);
        RecipeUtil.replaceRecipeAuto(new ItemStack(ModItems.cladding_rubber, 1), hack, "RCR", "CDC", "RCR", 'R', ANY_RUBBER.ingot(), 'C', ANY_COAL_COKE.dust(), 'D', ModItems.ducttape);

        /* ToolRecipes + i don't remember, gah */

        CraftingManager.addRecipeAuto(new ItemStack(ModItemsSpace.transporter_linker, 1), "S", "C", "P", 'S', ModItems.crt_display, 'C', DictFrame.fromOne(ModItems.circuit, ItemEnums.EnumCircuitType.BASIC), 'P', AL.plate());
        CraftingManager.addRecipeAuto(new ItemStack(ModItemsSpace.atmosphere_scanner, 1), "QCQ", "WBW", "SSS", 'Q', ModBlocks.glass_quartz, 'C', DictFrame.fromOne(ModItems.circuit, ItemEnums.EnumCircuitType.BASIC), 'W', GOLD.wireFine(), 'B', DictFrame.fromOne(ModItems.circuit, ItemEnums.EnumCircuitType.CONTROLLER_CHASSIS), 'S', STAINLESS.plate());
        CraftingManager.addRecipeAuto(new ItemStack(ModBlocks.fissure_bomb, 1), "SUS", "RPR", "SUS", 'S', ModBlocks.semtex, 'U', U238.block(), 'R', OreDictManager.getReflector(), 'P', PU239.billet());

        /* MineralRecipes */

        add1To9Pair(ModItemsSpace.powder_gallium, ModItemsSpace.powder_gallium_tiny);
        add1To9Pair(ModItemsSpace.ingot_nickel, ModItemsSpace.nugget_nickel);
        add1To9Pair(ModItemsSpace.ingot_hafnium, ModItemsSpace.nugget_hafnium);
        RecipeUtil.removeAllByOutput(new ItemStack(ModBlocks.block_lanthanium), CraftingManager.hack);
        add1To9Pair(ModBlocksSpace.block_osmiridium, ModItems.ingot_osmiridium);
        add1To9Pair(ModBlocksSpace.bf_log, ModItemsSpace.woodemium_briquette);
        addMineralSet(ModItemsSpace.nugget_lanthanium, ModItems.ingot_lanthanium, ModBlocks.block_lanthanium);

        add1To9Pair(ModItemsSpace.ingot_bk247, ModItemsSpace.nugget_bk247);// TODO: ACTINIDE NUGGETS
        add1To9Pair(ModItemsSpace.ingot_cm242, ModItemsSpace.nugget_cm242);
        add1To9Pair(ModItemsSpace.ingot_cm243, ModItemsSpace.nugget_cm243);
        add1To9Pair(ModItemsSpace.ingot_cm244, ModItemsSpace.nugget_cm244);
        add1To9Pair(ModItemsSpace.ingot_cm245, ModItemsSpace.nugget_cm245);
        add1To9Pair(ModItemsSpace.ingot_cm246, ModItemsSpace.nugget_cm246);
        add1To9Pair(ModItemsSpace.ingot_cm247, ModItemsSpace.nugget_cm247);
        add1To9Pair(ModItemsSpace.ingot_cf251, ModItemsSpace.nugget_cf251);
        add1To9Pair(ModItemsSpace.ingot_cf252, ModItemsSpace.nugget_cf252);
        add1To9Pair(ModItemsSpace.ingot_es253, ModItemsSpace.nugget_es253);

        add1To9Pair(ModItemsSpace.ingot_gaas, ModItemsSpace.nugget_gaas);
        add1To9Pair(ModItemsSpace.ingot_zinc, ModItemsSpace.nugget_zinc);
        add1To9Pair(ModItemsSpace.ingot_gallium, ModItemsSpace.nugget_gallium);
        add1To9Pair(ModItemsSpace.ingot_cm_fuel, ModItemsSpace.nugget_cm_fuel);
        add1To9Pair(ModItemsSpace.ingot_cm_mix, ModItemsSpace.nugget_cm_mix);
        add1To9Pair(ModItemsSpace.ingot_menthol, ModItemsSpace.nugget_menthol);

        MineralRecipes.addBillet(ModItemsSpace.billet_gaas, ModItemsSpace.ingot_gaas, ModItemsSpace.nugget_gaas);
        MineralRecipes.addBillet(ModItemsSpace.billet_bk247, ModItemsSpace.ingot_bk247, ModItemsSpace.nugget_bk247, BK247.all(MaterialShapes.NUGGET));
        MineralRecipes.addBillet(ModItemsSpace.billet_cf251, ModItemsSpace.ingot_cf251, ModItemsSpace.nugget_cf251, CF251.all(MaterialShapes.NUGGET));
        MineralRecipes.addBillet(ModItemsSpace.billet_cf252, ModItemsSpace.ingot_cf252, ModItemsSpace.nugget_cf252, CF252.all(MaterialShapes.NUGGET));
        MineralRecipes.addBillet(ModItemsSpace.billet_es253, ModItemsSpace.ingot_es253, ModItemsSpace.nugget_es253, ES253.all(MaterialShapes.NUGGET));
        MineralRecipes.addBillet(ModItemsSpace.billet_cm242, ModItemsSpace.ingot_cm242, ModItemsSpace.nugget_cm242, CM242.all(MaterialShapes.NUGGET));
        MineralRecipes.addBillet(ModItemsSpace.billet_cm243, ModItemsSpace.ingot_cm243, ModItemsSpace.nugget_cm243, CM243.all(MaterialShapes.NUGGET));
        MineralRecipes.addBillet(ModItemsSpace.billet_cm244, ModItemsSpace.ingot_cm244, ModItemsSpace.nugget_cm244, CM244.all(MaterialShapes.NUGGET));
        MineralRecipes.addBillet(ModItemsSpace.billet_cm245, ModItemsSpace.ingot_cm245, ModItemsSpace.nugget_cm245, CM245.all(MaterialShapes.NUGGET));
        MineralRecipes.addBillet(ModItemsSpace.billet_cm246, ModItemsSpace.ingot_cm246, ModItemsSpace.nugget_cm246, CM246.all(MaterialShapes.NUGGET));
        MineralRecipes.addBillet(ModItemsSpace.billet_cm247, ModItemsSpace.ingot_cm247, ModItemsSpace.nugget_cm247, CM247.all(MaterialShapes.NUGGET));
        MineralRecipes.addBillet(ModItemsSpace.billet_cm_mix, ModItemsSpace.ingot_cm_mix, ModItemsSpace.nugget_cm_mix);
        MineralRecipes.addBillet(ModItemsSpace.billet_cm_fuel, ModItemsSpace.ingot_cm_fuel, ModItemsSpace.nugget_cm_fuel);
        MineralRecipes.addBillet(ModItemsSpace.billet_menthol, ModItemsSpace.ingot_menthol, ModItemsSpace.nugget_menthol);


        addRecipeAutoOreShapeless(new ItemStack(ModItemsSpace.billet_bk247, 1), "nuggetBerkelium247", "nuggetBerkelium247", "nuggetBerkelium247", "nuggetBerkelium247", "nuggetBerkelium247", "nuggetBerkelium247");
        addShapelessAuto(new ItemStack(ModItemsSpace.billet_cm_fuel, 3), ModItems.billet_u238, ModItems.billet_u238, ModItemsSpace.billet_cm_mix);
        addRecipeAutoOreShapeless(new ItemStack(ModItemsSpace.billet_cm_fuel, 1), ModItemsSpace.nugget_cm_mix, ModItemsSpace.nugget_cm_mix, "nuggetUranium238", "nuggetUranium238", "nuggetUranium238", "nuggetUranium238");
        addRecipeAutoOreShapeless(new ItemStack(ModItemsSpace.billet_cm_fuel, 1), ModItemsSpace.nugget_cm_mix, ModItemsSpace.nugget_cm_mix, "tinyU238", "tinyU238", "tinyU238", "tinyU238");
        addShapelessAuto(new ItemStack(ModItemsSpace.billet_cm_mix, 3), ModItemsSpace.billet_cm244, ModItemsSpace.billet_cm245, ModItemsSpace.billet_cm245);
        addRecipeAutoOreShapeless(new ItemStack(ModItemsSpace.billet_cm_mix, 1), "nuggetCm244", "nuggetCm244", "nuggetCm245", "nuggetCm245", "nuggetCm245", "nuggetCm245");
        addRecipeAutoOreShapeless(new ItemStack(ModItemsSpace.billet_cm_mix, 1), "tinyCm244", "tinyCm244", "tinyCm245", "tinyCm245", "tinyCm245", "tinyCm245");

        addBilletToIngot(ModItemsSpace.billet_red_copper, ModItems.ingot_red_copper);

        addRecipeAutoOreShapeless(new ItemStack(ModItemsSpace.pellet_rtg_berkelium), ModItemsSpace.billet_bk247, ModItemsSpace.billet_bk247, ModItemsSpace.billet_bk247, NI.plate());
        addRecipeAutoOreShapeless(new ItemStack(ModItemsSpace.pellet_rtg_cf251), ModItemsSpace.billet_cf251, ModItemsSpace.billet_cf251, ModItemsSpace.billet_cf251, NI.plate());
        addRecipeAutoOreShapeless(new ItemStack(ModItemsSpace.pellet_rtg_cf252), ModItemsSpace.billet_cf252, ModItemsSpace.billet_cf252, ModItemsSpace.billet_cf252, NI.plate());
        addShapelessAuto(new ItemStack(ModItems.billet_am_mix, 3), new ItemStack(ModItemsSpace.pellet_rtg_americium_depleted));
        addRecipeAuto(new ItemStack(Item.getItemFromBlock(ModBlocksSpace.block_nickel), 1), "###", "###", "###", '#', ModItemsSpace.ingot_nickel);
        addRecipeAuto(new ItemStack(ModItemsSpace.ingot_nickel, 9), "#", '#', Item.getItemFromBlock(ModBlocksSpace.block_nickel));

        add1To9Pair(ModItemsSpace.ingot_australium_greater, ModItems.nugget_australium_greater);
        add1To9Pair(ModItemsSpace.ingot_australium_lesser, ModItems.nugget_australium_lesser);
        /*add9To1(OreDictManager.DictFrame.fromOne(ModItems.ore_byproduct, EnumByproduct.B_ARSENIC), new ItemStack(ModItems.ingot_arsenic));
        add9To1(OreDictManager.DictFrame.fromOne(ModItems.ore_byproduct, EnumByproduct.B_STRONTIUM), new ItemStack(ModItems.powder_strontium));*/

        /* PowderRecipes */

        CraftingManager.addShapelessAuto(new ItemStack(ModItems.powder_cement, 4), CA.dust(), KEY_SAND, Items.CLAY_BALL, Items.CLAY_BALL); // Alite cement recipe
        CraftingManager.addShapelessAuto(new ItemStack(Items.DYE, 8, 15), ModItemsSpace.ammonium_nitrate, CA.dust());
        CraftingManager.addShapelessAuto(new ItemStack(ModItems.powder_desh_ready, 1), ModItems.powder_desh_mix, ModItems.ingot_mercury, ModItems.ingot_mercury, ANY_COKE.dust());
        CraftingManager.addShapelessAuto(new ItemStack(ModItems.powder_fertilizer, 4), CA.dust(), P_RED.dust(), ModItemsSpace.ammonium_nitrate);
        CraftingManager.addShapelessAuto(new ItemStack(ModItems.powder_fertilizer, 4), ANY_ASH.any(), P_RED.dust(), KNO.dust(), ModItemsSpace.ammonium_nitrate);

        /* RodRecipes */

        RodRecipes.addRBMKRod(BK247, ModItemsSpace.rbmk_fuel_bk247);
        RodRecipes.addRBMKRod(ModItemsSpace.billet_cm_fuel, ModItemsSpace.rbmk_fuel_lecm);
        RodRecipes.addRBMKRod(CMRG, ModItemsSpace.rbmk_fuel_mecm);
        RodRecipes.addRBMKRod(CM245, ModItemsSpace.rbmk_fuel_hecm);
        addPellet(PU241, EnumAddonWatzTypes.PU241);
        addPellet(AMF, EnumAddonWatzTypes.AMF);
        addPellet(AMRG, EnumAddonWatzTypes.AMRG);
        addPellet(CMRG, EnumAddonWatzTypes.CMRG);
        addPellet(CMF, EnumAddonWatzTypes.CMF);
        addPellet(BK247, EnumAddonWatzTypes.BK247);
        addPellet(CF252, EnumAddonWatzTypes.CF252);
        addPellet(CF251, EnumAddonWatzTypes.CF251);
        addPellet(ES253, EnumAddonWatzTypes.ES253);
        CraftingManager.addShapelessAuto(new ItemStack(ModItems.rbmk_fuel_drx, 1), ModItems.rbmk_fuel_balefire, ModItems.particle_digamma);

        /* CraftingManager */

        RecipeUtil.replaceRecipeAuto(new ItemStack(ModBlocks.machine_ammo_press, 1), hack, "IPI", "C C", "SSS", 'I', IRON.ingot(), 'P', Blocks.PISTON, 'C', CU.ingot(), 'S', KEY_STONE);
        RecipeUtil.replaceRecipeAuto(new ItemStack(ModBlocks.machine_furnace_brick_off), hack, "III", "I I", "BBB", 'I', Items.BRICK, 'B', KEY_STONE);
        RecipeUtil.replaceRecipeAuto(new ItemStack(ModBlocks.reinforced_stone, 4), hack, "FBF", "BFB", "FBF", 'F', KEY_COBBLESTONE, 'B', KEY_STONE);
        RecipeUtil.replaceShapelessAuto(new ItemStack(ModBlocks.lightstone, 4), hack, KEY_STONE, KEY_STONE, KEY_STONE, ModItems.powder_limestone);
        RecipeUtil.replaceRecipeAuto(new ItemStack(ModItems.photo_panel), hack, " G ", "IPI", " C ", 'G', KEY_ANYPANE, 'I', ModItems.plate_polymer, 'P', DictFrame.fromOne(ModItems.circuit, ItemEnums.EnumCircuitType.SILICON), 'C', DictFrame.fromOne(ModItems.circuit, ItemEnums.EnumCircuitType.PCB));
        RecipeUtil.replaceRecipeAuto(new ItemStack(ModBlocks.rad_absorber, 1, BlockAbsorber.EnumAbsorberTier.BASE.ordinal()), hack, "ICI", "CPC", "ICI", 'I', CU.ingot(), 'C', ANY_COAL_COKE.dust(), 'P', PB.dust());
        RecipeUtil.replaceRecipeAuto(new ItemStack(ModBlocks.rad_absorber, 1, BlockAbsorber.EnumAbsorberTier.RED.ordinal()), hack, "ICI", "CPC", "ICI", 'I', TI.ingot(), 'C', ANY_COAL_COKE.dust(), 'P', ModBlocks.absorber);
        RecipeUtil.replaceRecipeAuto(new ItemStack(ModBlocks.refueler), hack, "SS", "HC", "SS", 'S', STAINLESS.plate(), 'H', DictFrame.fromOne(ModItems.part_generic, ItemEnums.EnumPartType.PISTON_HYDRAULIC), 'C', DictFrame.fromOne(ModItems.circuit, ItemEnums.EnumCircuitType.BASIC));
        RecipeUtil.replaceRecipeAuto(new ItemStack(ModBlocks.press_preheater), hack, "CCC", "SLS", "TST", 'C', CU.plate(), 'S', KEY_STONE, 'L', Fluids.LAVA.getDict(1000), 'T', W.ingot());
        RecipeUtil.replaceRecipeAuto(new ItemStack(ModItems.laser_crystal_bale, 1), hack, "QDQ", "SBZ", "QDQ", 'Q', ModBlocks.glass_quartz, 'D', DNT.ingot(), 'B', ModItems.egg_balefire, 'S', ModItems.powder_spark_mix, 'Z', ModItemsSpace.powder_zinc);

        addRecipeAuto(DictFrame.fromOne(ModItemsSpace.circuit, ItemEnumsSpace.EnumCircuitType.CAPACITOR_LANTHANIUM), "I", "N", "W", 'I', ModItems.plate_polymer, 'N', LA.nugget(), 'W', AL.wireFine());
        addRecipeAuto(DictFrame.fromOne(ModItemsSpace.circuit, ItemEnumsSpace.EnumCircuitType.CAPACITOR_LANTHANIUM), "I", "N", "W", 'I', ModItems.plate_polymer, 'N', LA.nugget(), 'W', CU.wireFine());
        addRecipeAuto(DictFrame.fromOne(ModItemsSpace.circuit, ItemEnumsSpace.EnumCircuitType.GASCHIP), "I", "S", "W", 'I', ModItems.plate_polymer, 'S', DictFrame.fromOne(ModItemsSpace.circuit, ItemEnumsSpace.EnumCircuitType.GAAS), 'W', CU.wireFine());
        addRecipeAuto(DictFrame.fromOne(ModItemsSpace.circuit, ItemEnumsSpace.EnumCircuitType.GASCHIP), "I", "S", "W", 'I', ModItems.plate_polymer, 'S', DictFrame.fromOne(ModItemsSpace.circuit, ItemEnumsSpace.EnumCircuitType.GAAS), 'W', GOLD.wireFine());
        addRecipeAuto(DictFrame.fromOne(ModItemsSpace.circuit, ItemEnumsSpace.EnumCircuitType.HFCHIP), "I", "S", "W", 'I', ModItemsSpace.nugget_hafnium, 'S', DictFrame.fromOne(ModItemsSpace.circuit, ItemEnumsSpace.EnumCircuitType.GASCHIP), 'W', GOLD.wireFine());
        addRecipeAuto(DictFrame.fromOne(ModItemsSpace.circuit, ItemEnumsSpace.EnumCircuitType.MOLYCHIP), "I", "S", "W", 'I', ModItems.powder_molysite, 'S', MINGRADE.billet(), 'W', GOLD.wireFine());
        addRecipeAuto(new ItemStack(ModItemsSpace.turbine_syngas, 1), "BBB", "BSB", "BBB", 'B', ModItemsSpace.blade_syngas, 'S', STAINLESS.ingot());
        addShapelessAuto(new ItemStack(ModItemsSpace.cmug_empty, 1), Items.CLAY_BALL);
        addRecipeAuto(new ItemStack(ModItemsSpace.glass_empty, 1), "G G", "GGG", " G ", 'G', Blocks.GLASS);
        addShapelessAuto(new ItemStack(ModItemsSpace.teacup_empty, 1), Items.CLAY_BALL, ModItems.powder_calcium);
        addRecipeAuto(new ItemStack(ModItemsSpace.stick_vinyl, 3), "L", "L", 'L', ModBlocksSpace.vinyl_planks);
        addRecipeAuto(new ItemStack(ModItemsSpace.stick_pvc, 3), "L", "L", 'L', ModBlocksSpace.pvc_planks);
        addShapelessAuto(new ItemStack(ModBlocksSpace.vinyl_planks, 4), new ItemStack(ModBlocksSpace.vinyl_log));
        addShapelessAuto(new ItemStack(ModBlocksSpace.pvc_planks, 4), new ItemStack(ModBlocksSpace.pvc_log));
        addRecipeAuto(new ItemStack(Items.PAPER, 3), "LL", 'L', ModBlocksSpace.vinyl_planks);
        //addRecipeAuto(new ItemStack(ModBlocksSpace.det_salt, 1), "PIP", "DCD", "PIP", 'P', ModItems.ingot_cobalt, 'D', ModItemsSpace.billet_gaas, 'C', ModBlocks.det_nuke, 'I', Mats.MAT_TCALLOY.make(ModItems.plate_cast));
        addRecipeAuto(new ItemStack(ModItemsSpace.lox_tank, 1), " S ", "BKB", " S ", 'S', STEEL.plate(), 'B', STEEL.bolt(), 'K', Fluids.OXYGEN.getDict(1000));

        addRecipeAuto(new ItemStack(ModItemsSpace.beryllium_mirror), "BBN", "BNB", "NBB", 'B', BE.billet(), 'N', ND.wireDense());
        addRecipeAuto(new ItemStack(ModBlocksSpace.air_vent), "IGI", "ICI", "IDI", 'I', IRON.plate(), 'G', Blocks.IRON_BARS, 'C', ModItems.tank_steel, 'D', Blocks.DISPENSER);
        addRecipeAuto(new ItemStack(ModItemsSpace.powder_wd2004, 1), "PPP", "PCP", "PPP", 'P', ModItemsSpace.powder_wd2004_tiny, 'C', ModItems.powder_dineutronium);
        addShapelessAuto(new ItemStack(ModBlocks.pink_log), new ItemStack(ModItemsSpace.powder_wd2004, 10), KEY_LOG);
        addRecipeAuto(new ItemStack(ModItemsSpace.plate_nickel, 4), "##", "##", '#', NI.ingot());
        addRecipeAuto(new ItemStack(ModItemsSpace.plate_stainless, 4), "##", "##", '#', STAINLESS.ingot());
        //addRecipeAuto(new ItemStack(ModItemsSpace.fence_gate, 1),"II", "II", "II", 'I', ModBlocks.fence_metal );
        //addRecipeAuto(new ItemStack(ModItems.laser_crystal_iron, 1),"QGQ", "CSC", "QGQ", 'Q', ModBlocks.glass_quartz, 'G', GAAS.ingot(), 'C', ModItems.crystal_iron, 'S', ModItems.egg_balefire_shard);

        if (!GeneralConfig.enable528) {
            addRecipeAuto(new ItemStack(ModBlocksSpace.rbmk_burner, 1), "IGI", "NCN", "IGI", 'C', ModBlocks.rbmk_blank, 'I', ModBlocks.fluid_duct_neo, 'G', ModItems.tank_steel, 'N', ModItemsSpace.plate_nickel);
        }

        addRecipeAuto(new ItemStack(ModBlocks.crane_inserter, 8), "CCC", "C C", "CBC", 'C', STAINLESS.ingot(), 'B', DictFrame.fromOne(ModItems.conveyor_wand, ItemConveyorWand.ConveyorType.REGULAR));
        addRecipeAuto(new ItemStack(ModBlocks.crane_inserter, 8), "CCC", "C C", "CBC", 'C', STAINLESS.ingot(), 'B', ModBlocks.conveyor);
        addRecipeAuto(new ItemStack(ModBlocks.crane_extractor, 8), "CCC", "CPC", "CBC", 'C', STAINLESS.ingot(), 'B', DictFrame.fromOne(ModItems.conveyor_wand, ItemConveyorWand.ConveyorType.REGULAR), 'P', DictFrame.fromOne(ModItems.part_generic, ItemEnums.EnumPartType.PISTON_PNEUMATIC));
        addRecipeAuto(new ItemStack(ModBlocks.crane_extractor, 8), "CCC", "CPC", "CBC", 'C', STAINLESS.ingot(), 'B', ModBlocks.conveyor, 'P', DictFrame.fromOne(ModItems.part_generic, ItemEnums.EnumPartType.PISTON_PNEUMATIC));
        addRecipeAuto(new ItemStack(ModBlocks.crane_grabber, 8), "C C", "P P", "CBC", 'C', STAINLESS.ingot(), 'B', DictFrame.fromOne(ModItems.conveyor_wand, ItemConveyorWand.ConveyorType.REGULAR), 'P', DictFrame.fromOne(ModItems.part_generic, ItemEnums.EnumPartType.PISTON_PNEUMATIC));
        addRecipeAuto(new ItemStack(ModBlocks.crane_grabber, 8), "C C", "P P", "CBC", 'C', STAINLESS.ingot(), 'B', ModBlocks.conveyor, 'P', DictFrame.fromOne(ModItems.part_generic, ItemEnums.EnumPartType.PISTON_PNEUMATIC));
    }
}
