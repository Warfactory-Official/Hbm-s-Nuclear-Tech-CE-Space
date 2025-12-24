package com.hbmspace.mixin.mod.hbm.recipes;

import com.hbm.inventory.RecipesCommon;
import com.hbm.inventory.fluid.FluidStack;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.recipes.CrystallizerRecipes;
import com.hbm.items.ModItems;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.items.ModItemsSpace;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.hbm.inventory.OreDictManager.*;
import static com.hbmspace.inventory.OreDictManagerSpace.ZI;

@Mixin(value = CrystallizerRecipes.class, remap = false)
public class MixinCrystallizerRecipes {

    @Shadow
    public static void registerRecipe(Object input, CrystallizerRecipes.CrystallizerRecipe recipe) { throw new AssertionError(); }

    @Shadow
    public static void registerRecipe(Object input, CrystallizerRecipes.CrystallizerRecipe recipe, FluidStack stack) { throw new AssertionError(); }

    @Inject(method = "registerDefaults", at = @At("TAIL"))
    public void registerSpace(CallbackInfo ci) {
        final int baseTime = 600;
        final int utilityTime = 100;
        final int mixingTime = 20;
        FluidStack sulfur = new FluidStack(Fluids.SULFURIC_ACID, 500);
        FluidStack nitric = new FluidStack(Fluids.NITRIC_ACID, 500);
        FluidStack organic = new FluidStack(Fluids.SOLVENT, 500);
        FluidStack chloric = new FluidStack(Fluids.HCL, 500);
        FluidStack schrabidic = new FluidStack(Fluids.SCHRABIDIC, 1000);
        FluidStack hiperf = new FluidStack(Fluids.RADIOSOLVENT, 500);
        FluidStack technetic = new FluidStack(Fluids.HTCO4, 500);

        registerRecipe(P_RED.ore(),		new CrystallizerRecipes.CrystallizerRecipe(ModItems.crystal_phosphorus, baseTime).prod(0.05F));
        //registerRecipe(NI.ore(),		new CrystallizerRecipe(ModItems.crystal_nickel, baseTime).prod(0.05F), nitric);
        registerRecipe(ZI.ore(),		new CrystallizerRecipes.CrystallizerRecipe(ModItemsSpace.crystal_zinc, baseTime).prod(0.05F), nitric);

        registerRecipe(NB.ore(),		new CrystallizerRecipes.CrystallizerRecipe(ModItemsSpace.crystal_niobium, baseTime).prod(0.05F), sulfur);
        registerRecipe((new RecipesCommon.ComparableStack(ModBlocksSpace.ore_mineral, 1, OreDictionary.WILDCARD_VALUE)),		new CrystallizerRecipes.CrystallizerRecipe(ModItemsSpace.crystal_mineral, baseTime).prod(0.05F)); //temp
        registerRecipe(new RecipesCommon.ComparableStack(ModItemsSpace.crystal_mineral),	new CrystallizerRecipes.CrystallizerRecipe(ModItems.crystal_diamond, baseTime).prod(0.05F));

        registerRecipe(new RecipesCommon.ComparableStack(ModItemsSpace.saltleaf),	new CrystallizerRecipes.CrystallizerRecipe(ModItems.gem_sodalite, baseTime).setReq(5), new FluidStack(Fluids.SCUTTERBLOOD, 1_000));
        registerRecipe(MALACHITE.ingot(), new CrystallizerRecipes.CrystallizerRecipe(ModItems.crystal_copper, baseTime).prod(0.1F), new FluidStack(com.hbmspace.inventory.fluid.Fluids.COPPERSULFATE, 350));
        registerRecipe(new RecipesCommon.ComparableStack(ModItemsSpace.nickel_salts),	new CrystallizerRecipes.CrystallizerRecipe(ModItemsSpace.crystal_nickel, baseTime), nitric);
        registerRecipe(new RecipesCommon.ComparableStack(ModItemsSpace.leaf_rubber),	new CrystallizerRecipes.CrystallizerRecipe(ModItems.ingot_rubber, baseTime).setReq(64), chloric);
        //registerRecipe(new ComparableStack(ModItems.leaf_pet),	new CrystallizerRecipe(ModItems.ingot_pc, baseTime).setReq(32), new FluidStack(Fluids.VINYL, 250));

        int mineraltime = 300;
        registerRecipe(new RecipesCommon.ComparableStack(ModItemsSpace.mineral_dust),	new CrystallizerRecipes.CrystallizerRecipe(new ItemStack(ModItemsSpace.mineral_fragment, 1, 0), mineraltime));
        registerRecipe(new RecipesCommon.ComparableStack(ModItemsSpace.mineral_dust),	new CrystallizerRecipes.CrystallizerRecipe(new ItemStack(ModItemsSpace.mineral_fragment, 1, 1), mineraltime), nitric);
        registerRecipe(new RecipesCommon.ComparableStack(ModItemsSpace.mineral_dust),	new CrystallizerRecipes.CrystallizerRecipe(new ItemStack(ModItemsSpace.mineral_fragment, 1, 2), mineraltime), sulfur);
        registerRecipe(new RecipesCommon.ComparableStack(ModItemsSpace.mineral_dust),	new CrystallizerRecipes.CrystallizerRecipe(new ItemStack(ModItemsSpace.mineral_fragment, 1, 3), mineraltime), organic);
        registerRecipe(new RecipesCommon.ComparableStack(ModItemsSpace.mineral_dust),	new CrystallizerRecipes.CrystallizerRecipe(new ItemStack(ModItemsSpace.mineral_fragment, 1, 4), mineraltime), chloric);
        registerRecipe(new RecipesCommon.ComparableStack(ModItemsSpace.mineral_dust),	new CrystallizerRecipes.CrystallizerRecipe(new ItemStack(ModItemsSpace.mineral_fragment, 1, 5), mineraltime), schrabidic);
    }
}
