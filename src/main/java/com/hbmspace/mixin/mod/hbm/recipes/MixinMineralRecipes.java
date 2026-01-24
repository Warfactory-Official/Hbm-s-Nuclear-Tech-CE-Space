package com.hbmspace.mixin.mod.hbm.recipes;

import com.hbm.blocks.ModBlocks;
import com.hbm.crafting.MineralRecipes;
import com.hbm.inventory.material.MaterialShapes;
import com.hbm.items.ModItems;
import com.hbm.main.CraftingManager;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.items.ModItemsSpace;
import com.hbmspace.util.RecipeUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.hbm.crafting.MineralRecipes.*;
import static com.hbm.main.CraftingManager.addRecipeAuto;
import static com.hbm.main.CraftingManager.addRecipeAutoOreShapeless;
import static com.hbm.main.CraftingManager.addShapelessAuto;
import static com.hbmspace.inventory.OreDictManagerSpace.*;

@Mixin(value = MineralRecipes.class, remap = false)
public class MixinMineralRecipes {
    @Inject(method = "register", at = @At("TAIL"))
    private static void addSpaceCrafts(CallbackInfo ci){
        add1To9Pair(ModItemsSpace.powder_gallium, ModItemsSpace.powder_gallium_tiny);
        add1To9Pair(ModItemsSpace.ingot_nickel, ModItemsSpace.nugget_nickel);
        add1To9Pair(ModItemsSpace.ingot_hafnium, ModItemsSpace.nugget_hafnium);
        RecipeUtil.removeAllByOutput(new ItemStack(ModBlocks.block_lanthanium), CraftingManager.hack);
        add1To9Pair(ModBlocksSpace.block_osmiridium, ModItems.ingot_osmiridium);
        //add1To9Pair(ModBlocksSpace.bf_log, ModItemsSpace.woodemium_briquette);
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
        
        addBillet(ModItemsSpace.billet_gaas,					ModItemsSpace.ingot_gaas,				ModItemsSpace.nugget_gaas);
        addBillet(ModItemsSpace.billet_bk247,				ModItemsSpace.ingot_bk247,				ModItemsSpace.nugget_bk247, BK247.all(MaterialShapes.NUGGET));
        addBillet(ModItemsSpace.billet_cf251,				ModItemsSpace.ingot_cf251,				ModItemsSpace.nugget_cf251, CF251.all(MaterialShapes.NUGGET));
        addBillet(ModItemsSpace.billet_cf252,				ModItemsSpace.ingot_cf252,				ModItemsSpace.nugget_cf252, CF252.all(MaterialShapes.NUGGET));
        addBillet(ModItemsSpace.billet_es253,				ModItemsSpace.ingot_es253,				ModItemsSpace.nugget_es253, ES253.all(MaterialShapes.NUGGET));
        addBillet(ModItemsSpace.billet_cm242,				ModItemsSpace.ingot_cm242,				ModItemsSpace.nugget_cm242, CM242.all(MaterialShapes.NUGGET));
        addBillet(ModItemsSpace.billet_cm243,				ModItemsSpace.ingot_cm243,				ModItemsSpace.nugget_cm243, CM243.all(MaterialShapes.NUGGET));
        addBillet(ModItemsSpace.billet_cm244,				ModItemsSpace.ingot_cm244,				ModItemsSpace.nugget_cm244, CM244.all(MaterialShapes.NUGGET));
        addBillet(ModItemsSpace.billet_cm245,				ModItemsSpace.ingot_cm245,				ModItemsSpace.nugget_cm245, CM245.all(MaterialShapes.NUGGET));
        addBillet(ModItemsSpace.billet_cm246,				ModItemsSpace.ingot_cm246,				ModItemsSpace.nugget_cm246, CM246.all(MaterialShapes.NUGGET));
        addBillet(ModItemsSpace.billet_cm247,				ModItemsSpace.ingot_cm247,				ModItemsSpace.nugget_cm247, CM247.all(MaterialShapes.NUGGET));
        addBillet(ModItemsSpace.billet_cm_mix,				ModItemsSpace.ingot_cm_mix,				ModItemsSpace.nugget_cm_mix);
        addBillet(ModItemsSpace.billet_cm_fuel,		ModItemsSpace.ingot_cm_fuel,	ModItemsSpace.nugget_cm_fuel);
        addBillet(ModItemsSpace.billet_menthol,				ModItemsSpace.ingot_menthol,				ModItemsSpace.nugget_menthol);


        addRecipeAutoOreShapeless(new ItemStack(ModItemsSpace.billet_bk247, 1), "nuggetBerkelium247", "nuggetBerkelium247", "nuggetBerkelium247", "nuggetBerkelium247", "nuggetBerkelium247", "nuggetBerkelium247");
        addShapelessAuto(new ItemStack(ModItemsSpace.billet_cm_fuel, 3), ModItems.billet_u238, ModItems.billet_u238, ModItemsSpace.billet_cm_mix);
        addRecipeAutoOreShapeless(new ItemStack(ModItemsSpace.billet_cm_fuel, 1), ModItemsSpace.nugget_cm_mix, ModItemsSpace.nugget_cm_mix, "nuggetUranium238", "nuggetUranium238", "nuggetUranium238", "nuggetUranium238");
        addRecipeAutoOreShapeless(new ItemStack(ModItemsSpace.billet_cm_fuel, 1), ModItemsSpace.nugget_cm_mix, ModItemsSpace.nugget_cm_mix, "tinyU238", "tinyU238", "tinyU238", "tinyU238");
        addShapelessAuto(new ItemStack(ModItemsSpace.billet_cm_mix, 3), ModItemsSpace.billet_cm244, ModItemsSpace.billet_cm245, ModItemsSpace.billet_cm245);
        addRecipeAutoOreShapeless(new ItemStack(ModItemsSpace.billet_cm_mix, 1), "nuggetCm244", "nuggetCm244", "nuggetCm245", "nuggetCm245", "nuggetCm245", "nuggetCm245");
        addRecipeAutoOreShapeless(new ItemStack(ModItemsSpace.billet_cm_mix, 1), "tinyCm244", "tinyCm244", "tinyCm245", "tinyCm245", "tinyCm245", "tinyCm245");

        addBilletToIngot(ModItemsSpace.billet_red_copper,	ModItems.ingot_red_copper);

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
    }
}
