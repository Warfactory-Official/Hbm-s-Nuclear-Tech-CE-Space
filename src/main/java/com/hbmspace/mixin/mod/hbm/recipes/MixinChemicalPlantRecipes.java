package com.hbmspace.mixin.mod.hbm.recipes;

import com.hbm.config.GeneralConfig;
import com.hbm.inventory.RecipesCommon;
import com.hbm.inventory.fluid.FluidStack;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.recipes.ChemicalPlantRecipes;
import com.hbm.inventory.recipes.loader.GenericRecipe;
import com.hbm.items.ModItems;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.items.ModItemsSpace;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.hbm.inventory.OreDictManager.*;
import static com.hbm.inventory.OreDictManager.ANY_PLASTIC;
import static com.hbm.inventory.OreDictManager.ASBESTOS;

@Mixin(value = ChemicalPlantRecipes.class, remap = false)
public class MixinChemicalPlantRecipes {

    @Inject(method = "registerDefaults", at = @At("TAIL"), remap = false)
    private void registerSpace(CallbackInfo ci) {
        ChemicalPlantRecipes instance = (ChemicalPlantRecipes) (Object) this;
        instance.removeRecipeByName("chem.oxygen");
        instance.removeRecipeByName("chem.xenon");
        instance.removeRecipeByName("chem.xenonoxy");
        instance.removeRecipeByName("chem.birkeland");

        instance.register(new GenericRecipe("chem.vinylrubber").setup(100, 400)
                .inputFluids(new FluidStack(com.hbmspace.inventory.fluid.Fluids.VINYL, 500), new FluidStack(Fluids.UNSATURATEDS, 400, GeneralConfig.enable528 ? 2 : 0))
                .outputItems(new ItemStack(ModItems.ingot_rubber)));

        /// SPACE ///
        instance.register(new GenericRecipe("chem.coppersulf").setup(50, 100).setIcon(ModItems.fluid_icon, com.hbmspace.inventory.fluid.Fluids.COPPERSULFATE.getID())
                .inputFluids(new FluidStack(com.hbmspace.inventory.fluid.Fluids.AQUEOUS_COPPER, 500))
                .outputItems(new ItemStack(ModItemsSpace.powder_nickel, 2), new ItemStack(ModItems.powder_copper, 2))
                .outputFluids(new FluidStack(com.hbmspace.inventory.fluid.Fluids.COPPERSULFATE, 200)));

        instance.register(new GenericRecipe("chem.uraniumbromide").setup(200, 1_000).setIcon(ModItems.fluid_icon, Fluids.URANIUM_BROMIDE.getID())
                .inputItems(new RecipesCommon.OreDictStack(U235.billet(), 1), new RecipesCommon.ComparableStack(ModItems.powder_bromine), new RecipesCommon.OreDictStack(ASBESTOS.ingot(), 1))
                .inputFluids(new FluidStack(Fluids.HYDROGEN, 4_000))
                .outputFluids(new FluidStack(Fluids.URANIUM_BROMIDE, 4_000)));

        instance.register(new GenericRecipe("chem.thoriumbromide").setup(200, 1_000).setIcon(ModItems.fluid_icon, Fluids.THORIUM_BROMIDE.getID())
                .inputItems(new RecipesCommon.OreDictStack(TH232.billet(), 1), new RecipesCommon.ComparableStack(ModItems.powder_bromine), new RecipesCommon.OreDictStack(ASBESTOS.ingot(), 1))
                .inputFluids(new FluidStack(Fluids.HYDROGEN, 4_000))
                .outputFluids(new FluidStack(Fluids.THORIUM_BROMIDE, 4_000)));

        instance.register(new GenericRecipe("chem.hydrazine").setup(250, 1_000).setIcon(ModItems.canister_full, Fluids.HYDRAZINE.getID())
                .inputFluids(new FluidStack(Fluids.NITRIC_ACID, 2_000), new FluidStack(Fluids.AMMONIA, 1_000))
                .outputFluids(new FluidStack(Fluids.HYDRAZINE, 800)));

        instance.register(new GenericRecipe("chem.ammonia").setup(50, 100)
                .inputFluids(new FluidStack(Fluids.NITROGEN, 600), new FluidStack(Fluids.WATER, 1_000))
                .outputFluids(new FluidStack(Fluids.AMMONIA, 800)));

        instance.register(new GenericRecipe("chem.bloodfuel").setup(250, 1_000).setIcon(ModItems.canister_full, Fluids.BLOODGAS.getID())
                .inputFluids(new FluidStack(Fluids.AMMONIA, 350), new FluidStack(Fluids.BLOOD, 800))
                .outputFluids(new FluidStack(Fluids.BLOODGAS, 1000)));

        instance.register(new GenericRecipe("chem.hcl").setup(50, 100)
                .inputFluids(new FluidStack(Fluids.HYDROGEN, 500), new FluidStack(Fluids.CHLORINE, 500))
                .outputFluids(new FluidStack(Fluids.HCL, 1000)));

        instance.register(new GenericRecipe("chem.ammoniumnitrate").setup(250, 1_000)
                .inputFluids(new FluidStack(Fluids.AMMONIA, 500), new FluidStack(Fluids.NITROGEN, 1000))
                .outputItems(new ItemStack(ModItemsSpace.ammonium_nitrate, 4)));

        instance.register(new GenericRecipe("chem.nmass").setup(250, 10_000)
                .inputFluids(new FluidStack(Fluids.SCHRABIDIC, 650), (new FluidStack(Fluids.IONGEL, 800)))
                .inputItems(new RecipesCommon.ComparableStack(ModItems.pellet_charged, 1), new RecipesCommon.ComparableStack(ModItems.ingot_euphemium, 1))
                .outputFluids(new FluidStack(Fluids.NMASS, 1000), new FluidStack(Fluids.WASTEGAS, 2000)));

        instance.register(new GenericRecipe("chem.masscake").setup(200, 100)
                .inputFluids(new FluidStack(Fluids.CMILK, 4000), new FluidStack(Fluids.CREAM, 1000)) // why not regular milk? well its because the refined products allow for higher mass cakes while still needing less milk
                .inputItems(
                        new RecipesCommon.ComparableStack(Items.SUGAR, 8),				// if there is a hole in my logic i will shoot myself
                        new RecipesCommon.ComparableStack(Items.EGG, 4))				//ex: since a cake needs 3 buckets of milk, c-milk is more dense, leading to it being only 4 buckets of condensed milk, thats 1 bucket per cake.
                .outputItems(new ItemStack(Items.CAKE, 4)));

        instance.register(new GenericRecipe("chem.butter").setup(100, 100)
                .inputFluids(new FluidStack(Fluids.EMILK, 1000))
                .outputItems(new ItemStack(ModItemsSpace.butter)));
        // TODO
        /*instance.register(new GenericRecipe("chem.strawberryicecream").setup(150, 100)
                .inputFluids(new FluidStack(Fluids.CREAM, 1000))
                .inputItems(new RecipesCommon.ComparableStack(ModItemsSpace.butter, 2), new RecipesCommon.ComparableStack(Blocks.PACKED_ICE, 1), new RecipesCommon.ComparableStack(ModItemsSpace.strawberry, 4))
                .outputItems(new ItemStack(ModItemsSpace.s_cream, 4)));*/

        instance.register(new GenericRecipe("chem.minmusicecream").setup(150, 100)
                .inputFluids(new FluidStack(Fluids.CREAM, 1000))
                .inputItems(new RecipesCommon.ComparableStack(ModItemsSpace.butter, 4), new RecipesCommon.ComparableStack(ModBlocksSpace.minmus_smooth, 2), new RecipesCommon.ComparableStack(ModBlocksSpace.minmus_stone, 2))
                .outputItems(new ItemStack(ModItemsSpace.min_cream, 4)));

        /*instance.register(new GenericRecipe("chem.minmussmoothstone").setup(200, 1_000)
                .inputFluids(new FluidStack(Fluids.MILK, 350), new FluidStack(Fluids.EMILK, 250))
                .inputItems(new RecipesCommon.ComparableStack(Blocks.ICE, 4), new RecipesCommon.ComparableStack(ModItemsSpace.mint_leaves, 2), new RecipesCommon.ComparableStack(ModBlocksSpace.minmus_smooth, 2))
                .outputItems(new ItemStack(ModBlocksSpace.minmus_smooth, 4)));*/
        //something about a steam distilation?
        /*instance.register(new GenericRecipe("chem.menthol").setup(50, 100)
                .inputFluids(new FluidStack(Fluids.STEAM, 350))
                .inputItems(new RecipesCommon.ComparableStack(ModItemsSpace.mint_leaves, 4))
                .outputItems(new ItemStack(ModItemsSpace.ingot_menthol, 2)));*/
        //this makes no sense but we're making stone with milk and mint so it doesn't matter
        instance.register(new GenericRecipe("chem.mentholfromminmus").setup(50, 300)
                .inputFluids(new FluidStack(Fluids.STEAM, 350), new FluidStack(Fluids.SULFURIC_ACID, 500))
                .inputItems(new RecipesCommon.ComparableStack(ModBlocksSpace.minmus_smooth, 40))
                .outputItems(new ItemStack(ModItemsSpace.ingot_menthol, 15)));

        instance.register(new GenericRecipe("chem.mintslice").setup(50, 100)
                .inputFluids(new FluidStack(Fluids.MILK, 350))
                .inputItems(new RecipesCommon.ComparableStack(ModItemsSpace.billet_menthol, 3), new RecipesCommon.ComparableStack(ModBlocksSpace.minmus_stone, 1), new RecipesCommon.ComparableStack(Items.SUGAR, 6))
                .outputItems(new ItemStack(ModItemsSpace.chocolate_mint_billet, 3)));

        instance.register(new GenericRecipe("chem.minmusstone").setup(300, 500)
                .inputFluids(new FluidStack(Fluids.SULFURIC_ACID, 500))
                .inputItems(new RecipesCommon.ComparableStack(ModBlocksSpace.minmus_smooth, 2))
                .outputItems(new ItemStack(ModBlocksSpace.minmus_stone, 2)));

        instance.register(new GenericRecipe("chem.soil").setup(100, 1_000)
                .inputFluids(new FluidStack(Fluids.WATER, 4000))
                .inputItems(new RecipesCommon.ComparableStack(ModItemsSpace.ammonium_nitrate, 1), new RecipesCommon.ComparableStack(Blocks.GRAVEL, 8))
                .outputItems(new ItemStack(Blocks.DIRT, 8)));

        instance.register(new GenericRecipe("chem.chloromethane").setup(50, 1_000)
                .inputFluids(new FluidStack(Fluids.GAS, 750), new FluidStack(Fluids.CHLORINE, 250))
                .outputFluids(new FluidStack(Fluids.CHLOROMETHANE, 1000)));

        instance.register(new GenericRecipe("chem.nitricacidalt").setupNamed(50, 1_000)
                .inputFluids(new FluidStack(Fluids.WATER, 500), new FluidStack(Fluids.AMMONIA, 1000))
                .outputFluids(new FluidStack(Fluids.NITRIC_ACID, 1_000)));

        // WARNING: NILERED CHEMISTRY ZONE //
        instance.register(new GenericRecipe("chem.hydrapiss").setupNamed(250, 1_000).setIcon(ModItems.canister_full, Fluids.HYDRAZINE.getID())
                .inputFluids(new FluidStack(Fluids.NITRIC_ACID, 2000))
                .inputItems(new RecipesCommon.ComparableStack(ModItems.rag_piss)) // urea...
                .outputFluids(new FluidStack(Fluids.HYDRAZINE, 800))); // this is an emergency recipe, so it should NOT have a required blueprint

        instance.register(new GenericRecipe("chem.synleather").setup(200, 500)
                .inputFluids(new FluidStack(Fluids.PEROXIDE, 250))
                .inputItems(new RecipesCommon.OreDictStack(ANY_PLASTIC.ingot()), new RecipesCommon.ComparableStack(Items.STRING, 4))
                .outputItems(new ItemStack(Items.LEATHER)));
    }
}
