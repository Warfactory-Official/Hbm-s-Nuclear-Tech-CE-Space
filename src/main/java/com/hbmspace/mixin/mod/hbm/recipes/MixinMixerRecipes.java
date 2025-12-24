package com.hbmspace.mixin.mod.hbm.recipes;

import com.hbm.inventory.RecipesCommon;
import com.hbm.inventory.fluid.FluidStack;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.recipes.MixerRecipes;
import com.hbm.items.ItemEnums;
import com.hbm.items.ModItems;
import com.hbmspace.blocks.BlockEnumsSpace;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.items.ModItemsSpace;
import net.minecraftforge.oredict.OreDictionary;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Constructor;

import static com.hbmspace.inventory.OreDictManagerSpace.NIM;

@Mixin(value = MixerRecipes.class, remap = false)
public class MixinMixerRecipes {
    @Shadow
    public static void register(FluidType type, MixerRecipes.MixerRecipe... rec){}

    @Inject(method = "registerDefaults", at = @At("TAIL"))
    public void registerSpace(CallbackInfo ci) {
        // TODO replace a few recipes, should find them in 1.7 upstream
        // Also remind me to change protected to public, because that's fucking atrocious

        /*register(Fluids.SMILK, new MixerRecipes.MixerRecipe(500, 50).setStack1(new FluidStack(Fluids.MILK, 500)).setSolid(new RecipesCommon.ComparableStack(ModItemsSpace.strawberry, 4)));
        register(Fluids.COFFEE, new MixerRecipes.MixerRecipe(100, 50).setStack1(new FluidStack(Fluids.WATER, 500)).setSolid(new RecipesCommon.ComparableStack(ModItemsSpace.powder_coffee, 4)));
        register(Fluids.TEA, new MixerRecipes.MixerRecipe(200, 50).setStack1(new FluidStack(Fluids.WATER, 500)).setSolid(new RecipesCommon.ComparableStack(ModItemsSpace.tea_leaf, 2)));*/

        MixerRecipes.MixerRecipe r;

        r = hbmspace$newRecipe(1000, 50);
        r.input1 = new FluidStack(Fluids.REFORMGAS, 500);
        r.input2 = new FluidStack(Fluids.SYNGAS, 500);
        register(Fluids.ELBOWGREASE, r);

        r = hbmspace$newRecipe(250, 50);
        r.input1 = new FluidStack(Fluids.NITRIC_ACID, 500);
        r.solidInput = new RecipesCommon.ComparableStack(ModItems.nugget_technetium);
        register(Fluids.HTCO4, r);

        r = hbmspace$newRecipe(500, 50);
        r.input1 = new FluidStack(Fluids.NITRIC_ACID, 500);
        r.solidInput = new RecipesCommon.ComparableStack(ModBlocksSpace.ore_mineral, 1, OreDictionary.WILDCARD_VALUE);
        register(Fluids.MINSOL, r);

        r = hbmspace$newRecipe(50, 100);
        r.input1 = new FluidStack(Fluids.HCL, 1400);
        r.solidInput = new RecipesCommon.ComparableStack(ModBlocksSpace.duna_sands, 4);
        register(Fluids.REDMUD, r);

        r = hbmspace$newRecipe(750, 50);
        r.input1 = new FluidStack(Fluids.AMMONIA, 300);
        r.input2 = new FluidStack(Fluids.UNSATURATEDS, 500);
        register(Fluids.DICYANOACETYLENE, r);

        r = hbmspace$newRecipe(450, 30);
        r.input1 = new FluidStack(com.hbmspace.inventory.fluid.Fluids.CHLOROETHANE, 250);
        r.input2 = new FluidStack(Fluids.REDMUD, 300);
        register(Fluids.ETHANOL, r);

        r = hbmspace$newRecipe(550, 50);
        r.input1 = new FluidStack(Fluids.HCL, 300);
        r.solidInput = new RecipesCommon.ComparableStack(ModItemsSpace.scuttertail);
        register(Fluids.SCUTTERBLOOD, r);

        r = hbmspace$newRecipe(1000, 70);
        r.input1 = new FluidStack(Fluids.SULFURIC_ACID, 350);
        r.solidInput = new RecipesCommon.ComparableStack(ModBlocksSpace.stone_resource, 1, BlockEnumsSpace.EnumStoneType.CONGLOMERATE.ordinal());
        register(com.hbmspace.inventory.fluid.Fluids.CONGLOMERA, r);

        r = hbmspace$newRecipe(1000, 80);
        r.input1 = new FluidStack(com.hbmspace.inventory.fluid.Fluids.BRINE, 250);
        r.solidInput = new RecipesCommon.OreDictStack(NIM.dust());
        register(com.hbmspace.inventory.fluid.Fluids.AQUEOUS_NICKEL, r);

        r = hbmspace$newRecipe(1000, 80);
        r.input1 = new FluidStack(com.hbmspace.inventory.fluid.Fluids.BRINE, 250);
        r.solidInput = new RecipesCommon.ComparableStack(ModItems.chunk_ore, 1, ItemEnums.EnumChunkType.MALACHITE);
        register(com.hbmspace.inventory.fluid.Fluids.AQUEOUS_COPPER, r);

        MixerRecipes.MixerRecipe v1 = hbmspace$newRecipe(1000, 20);
        v1.input1 = new FluidStack(Fluids.RADIOSOLVENT, 250);
        v1.solidInput = new RecipesCommon.ComparableStack(ModBlocksSpace.pvc_log);

        MixerRecipes.MixerRecipe v2 = hbmspace$newRecipe(500, 20);
        v2.input1 = new FluidStack(Fluids.RADIOSOLVENT, 125);
        v2.solidInput = new RecipesCommon.ComparableStack(ModItemsSpace.powder_pvc);

        MixerRecipes.MixerRecipe v3 = hbmspace$newRecipe(500, 20);
        v3.input1 = new FluidStack(com.hbmspace.inventory.fluid.Fluids.HGAS, 500);
        v3.input2 = new FluidStack(Fluids.UNSATURATEDS, 250);
        v3.solidInput = new RecipesCommon.ComparableStack(ModItems.powder_cadmium);

        register(com.hbmspace.inventory.fluid.Fluids.VINYL, v1, v2, v3);

        r = hbmspace$newRecipe(250, 20);
        r.input1 = new FluidStack(Fluids.HCL, 500);
        r.solidInput = new RecipesCommon.ComparableStack(ModItemsSpace.leaf_pet, 32);
        register(com.hbmspace.inventory.fluid.Fluids.CBENZ, r);
    }

    // THAT'S why I hate when accesstransformers refuse to work
    @Unique
    private static MixerRecipes.MixerRecipe hbmspace$newRecipe(int output, int processTime) {
        try {
            Constructor<MixerRecipes.MixerRecipe> c =
                    MixerRecipes.MixerRecipe.class.getDeclaredConstructor(int.class, int.class);
            c.setAccessible(true);
            return c.newInstance(output, processTime);
        } catch (Exception e) {
            throw new RuntimeException("Failed to construct MixerRecipe via reflection", e);
        }
    }
}
