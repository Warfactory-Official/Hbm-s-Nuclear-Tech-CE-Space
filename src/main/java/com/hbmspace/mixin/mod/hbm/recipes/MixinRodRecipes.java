package com.hbmspace.mixin.mod.hbm.recipes;

import com.hbm.crafting.RodRecipes;
import com.hbm.items.ModItems;
import com.hbm.items.machine.ItemWatzPellet;
import com.hbm.main.CraftingManager;
import com.hbmspace.items.ModItemsSpace;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.hbm.crafting.RodRecipes.*;
import static com.hbm.inventory.OreDictManager.*;
import static com.hbm.inventory.OreDictManager.AMRG;
import static com.hbmspace.inventory.OreDictManagerSpace.*;

@Mixin(value = RodRecipes.class, remap = false)
public class MixinRodRecipes {

    @Inject(method = "register", at = @At("TAIL"))
    private static void addSpaceCrafts(CallbackInfo ci){
        addRBMKRod(BK247, ModItemsSpace.rbmk_fuel_bk247);
        addRBMKRod(ModItemsSpace.billet_cm_fuel, ModItemsSpace.rbmk_fuel_lecm);
        addRBMKRod(CMRG, ModItemsSpace.rbmk_fuel_mecm);
        addRBMKRod(CM245, ModItemsSpace.rbmk_fuel_hecm);
        addPellet(PU241,							ItemWatzPellet.EnumWatzType.PU241);
        addPellet(AMF,								ItemWatzPellet.EnumWatzType.AMF);
        addPellet(AMRG,								ItemWatzPellet.EnumWatzType.AMRG);
        addPellet(CMRG,								ItemWatzPellet.EnumWatzType.CMRG);
        addPellet(CMF,								ItemWatzPellet.EnumWatzType.CMF);
        addPellet(BK247,							ItemWatzPellet.EnumWatzType.BK247);
        addPellet(CF252,							ItemWatzPellet.EnumWatzType.CF252);
        addPellet(CF251,							ItemWatzPellet.EnumWatzType.CF251);
        addPellet(ES253,							ItemWatzPellet.EnumWatzType.ES253);
        CraftingManager.addShapelessAuto(new ItemStack(ModItems.rbmk_fuel_drx, 1), ModItems.rbmk_fuel_balefire, ModItems.particle_digamma);
    }
}
