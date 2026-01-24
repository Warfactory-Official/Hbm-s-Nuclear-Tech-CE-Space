package com.hbmspace.mixin.mod.hbm.recipes;

import com.hbm.blocks.ModBlocks;
import com.hbm.blocks.generic.BlockAbsorber;
import com.hbm.config.GeneralConfig;
import com.hbm.inventory.OreDictManager;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.items.ItemEnums;
import com.hbm.items.ModItems;
import com.hbm.items.tool.ItemConveyorWand;
import com.hbm.main.CraftingManager;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.items.ItemEnumsSpace;
import com.hbmspace.items.ModItemsSpace;
import com.hbmspace.util.RecipeUtil;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.hbm.inventory.OreDictManager.*;
import static com.hbm.main.CraftingManager.addRecipeAuto;
import static com.hbm.main.CraftingManager.addShapelessAuto;
import static com.hbmspace.inventory.OreDictManagerSpace.*;

@Mixin(value = CraftingManager.class, remap = false)
public class MixinCraftingManager {

    @Shadow
    public static RegistryEvent.Register<IRecipe> hack;

    @Inject(method = "addCrafting", at = @At("TAIL"))
    private static void addSpaceCrafts(CallbackInfo ci){
        RecipeUtil.replaceRecipeAuto(
                new ItemStack(ModBlocks.refueler), hack,
                "SS", "HC", "SS",
                'S', STAINLESS.plate(),
                'H', OreDictManager.DictFrame.fromOne(ModItems.part_generic, ItemEnums.EnumPartType.PISTON_HYDRAULIC),
                'C', OreDictManager.DictFrame.fromOne(ModItems.circuit, ItemEnums.EnumCircuitType.BASIC)
        );
        RecipeUtil.replaceRecipeAuto(new ItemStack(ModBlocks.machine_ammo_press, 1), hack, "IPI", "C C", "SSS", 'I', IRON.ingot(), 'P', Blocks.PISTON, 'C', CU.ingot(), 'S', KEY_STONE);
        RecipeUtil.replaceRecipeAuto(new ItemStack(ModBlocks.machine_furnace_brick_off), hack, "III", "I I", "BBB", 'I', Items.BRICK, 'B', KEY_STONE);
        RecipeUtil.replaceRecipeAuto(new ItemStack(ModItems.laser_crystal_bale, 1), hack, "QDQ", "SBZ", "QDQ", 'Q', ModBlocks.glass_quartz, 'D', DNT.ingot(), 'B', ModItems.egg_balefire, 'S', ModItems.powder_spark_mix, 'Z', ModItemsSpace.powder_zinc);
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
        addRecipeAuto(new ItemStack(ModItemsSpace.turbine_syngas, 1),"BBB", "BSB", "BBB", 'B', ModItemsSpace.blade_syngas, 'S', STAINLESS.ingot() );
        addShapelessAuto(new ItemStack(ModItemsSpace.cmug_empty, 1), Items.CLAY_BALL);
        addRecipeAuto(new ItemStack(ModItemsSpace.glass_empty, 1), "G G", "GGG", " G ", 'G', Blocks.GLASS);
        addShapelessAuto(new ItemStack(ModItemsSpace.teacup_empty, 1), Items.CLAY_BALL, ModItems.powder_calcium);
        addRecipeAuto(new ItemStack(ModItemsSpace.stick_vinyl, 3),"L", "L", 'L', ModBlocksSpace.vinyl_planks );
        addRecipeAuto(new ItemStack(ModItemsSpace.stick_pvc, 3),"L", "L", 'L', ModBlocksSpace.pvc_planks );
        addShapelessAuto(new ItemStack(ModBlocksSpace.vinyl_planks, 4), new ItemStack(ModBlocksSpace.vinyl_log));
        addShapelessAuto(new ItemStack(ModBlocksSpace.pvc_planks, 4), new ItemStack(ModBlocksSpace.pvc_log));
        addRecipeAuto(new ItemStack(Items.PAPER, 3),"LL", 'L', ModBlocksSpace.vinyl_planks );
        //addRecipeAuto(new ItemStack(ModBlocksSpace.det_salt, 1), "PIP", "DCD", "PIP", 'P', ModItems.ingot_cobalt, 'D', ModItemsSpace.billet_gaas, 'C', ModBlocks.det_nuke, 'I', Mats.MAT_TCALLOY.make(ModItems.plate_cast));
        addRecipeAuto(new ItemStack(ModItemsSpace.lox_tank, 1), " S ", "BKB", " S ", 'S', STEEL.plate(), 'B', STEEL.bolt(), 'K', Fluids.OXYGEN.getDict(1000));

        addRecipeAuto(new ItemStack(ModItemsSpace.beryllium_mirror), "BBN", "BNB", "NBB", 'B', BE.billet(), 'N', ND.wireDense());
        addRecipeAuto(new ItemStack(ModBlocksSpace.air_vent), "IGI", "ICI", "IDI", 'I', IRON.plate(), 'G', Blocks.IRON_BARS, 'C', ModItems.tank_steel, 'D', Blocks.DISPENSER);
        addRecipeAuto(new ItemStack(ModItemsSpace.powder_wd2004, 1),"PPP", "PCP", "PPP", 'P', ModItemsSpace.powder_wd2004_tiny, 'C', ModItems.powder_dineutronium );
        addShapelessAuto(new ItemStack(ModBlocks.pink_log), new ItemStack(ModItemsSpace.powder_wd2004, 10), KEY_LOG);
        addRecipeAuto(new ItemStack(ModItemsSpace.plate_nickel, 4), "##", "##", '#', NI.ingot());
        addRecipeAuto(new ItemStack(ModItemsSpace.plate_stainless, 4), "##", "##", '#', STAINLESS.ingot());
        //addRecipeAuto(new ItemStack(ModItemsSpace.fence_gate, 1),"II", "II", "II", 'I', ModBlocks.fence_metal );
        //addRecipeAuto(new ItemStack(ModItems.laser_crystal_iron, 1),"QGQ", "CSC", "QGQ", 'Q', ModBlocks.glass_quartz, 'G', GAAS.ingot(), 'C', ModItems.crystal_iron, 'S', ModItems.egg_balefire_shard);

        if(!GeneralConfig.enable528) {
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
