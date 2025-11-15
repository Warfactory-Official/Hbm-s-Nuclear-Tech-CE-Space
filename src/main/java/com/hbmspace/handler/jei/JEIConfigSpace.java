package com.hbmspace.handler.jei;

import com.hbm.config.GeneralConfig;
import com.hbmspace.blocks.ModBlocksSpace;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

@JEIPlugin
public class JEIConfigSpace implements IModPlugin {

    public static final String VACUUM_CIRCUIT = "hbm.vacuum_circuit";
    private VacuumCircuitHandler vacuumCircuitHandler;

    @Override
    public void register(@NotNull IModRegistry registry) {
        if (!GeneralConfig.jei)
            return;

        registry.addRecipeCatalyst(new ItemStack(ModBlocksSpace.machine_vacuum_circuit), VACUUM_CIRCUIT);

        registry.addRecipes(vacuumCircuitHandler.getRecipes(), VACUUM_CIRCUIT);
    }

    @Override
    public void registerCategories(@NotNull IRecipeCategoryRegistration registry) {
        if (!GeneralConfig.jei)
            return;
        IGuiHelper help = registry.getJeiHelpers().getGuiHelper();
        registry.addRecipeCategories(
                vacuumCircuitHandler = new VacuumCircuitHandler(help)
        );
    }
}
