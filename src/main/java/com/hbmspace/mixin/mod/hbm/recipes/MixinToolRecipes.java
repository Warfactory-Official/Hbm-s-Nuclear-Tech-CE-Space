package com.hbmspace.mixin.mod.hbm.recipes;

import com.hbm.blocks.ModBlocks;
import com.hbm.crafting.ToolRecipes;
import com.hbm.inventory.OreDictManager;
import com.hbm.items.ItemEnums;
import com.hbm.items.ModItems;
import com.hbm.main.CraftingManager;
import com.hbmspace.items.ModItemsSpace;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.hbm.inventory.OreDictManager.*;
import static com.hbmspace.inventory.OreDictManagerSpace.STAINLESS;

@Mixin(value = ToolRecipes.class, remap = false)
public class MixinToolRecipes {

    @Inject(method = "register", at = @At("TAIL"))
    private static void addSpaceCrafts(CallbackInfo ci){
        CraftingManager.addRecipeAuto(new ItemStack(ModItemsSpace.transporter_linker, 1), "S", "C", "P", 'S', ModItems.crt_display, 'C', DictFrame.fromOne(ModItems.circuit, ItemEnums.EnumCircuitType.BASIC), 'P', AL.plate());
        CraftingManager.addRecipeAuto(new ItemStack(ModItemsSpace.atmosphere_scanner, 1), "QCQ", "WBW", "SSS", 'Q', ModBlocks.glass_quartz, 'C', DictFrame.fromOne(ModItems.circuit, ItemEnums.EnumCircuitType.BASIC), 'W', GOLD.wireFine(), 'B', DictFrame.fromOne(ModItems.circuit, ItemEnums.EnumCircuitType.CONTROLLER_CHASSIS), 'S', STAINLESS.plate());

        // no guys, I won't create a whole mixin class just for 1 recipe
        CraftingManager.addRecipeAuto(new ItemStack(ModBlocks.fissure_bomb, 1), "SUS", "RPR", "SUS", 'S', ModBlocks.semtex, 'U', U238.block(), 'R', OreDictManager.getReflector(), 'P', PU239.billet());
    }
}
