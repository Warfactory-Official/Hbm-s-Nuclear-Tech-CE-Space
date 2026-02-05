package com.hbmspace.handler.jei;

import com.hbm.config.GeneralConfig;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.items.ModItemsSpace;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

@JEIPlugin
public class JEIConfigSpace implements IModPlugin {

    public static final String VACUUM_CIRCUIT = "hbm.vacuum_circuit";
    public static final String DAIRY = "hbm.dairy";
    public static final String CRYO = "hbm.cryodistill";
    private VacuumCircuitHandler vacuumCircuitHandler;
    private DairyHandler dairyHandler;
    private CryoHandler cryoHandler;

    @Override
    public void register(@NotNull IModRegistry registry) {
        if (!GeneralConfig.jei)
            return;

        registry.addRecipeCatalyst(new ItemStack(ModBlocksSpace.machine_vacuum_circuit), VACUUM_CIRCUIT);

        registry.addRecipes(vacuumCircuitHandler.getRecipes(), VACUUM_CIRCUIT);
        registry.addRecipes(dairyHandler.getRecipes(), DAIRY);
        registry.addRecipes(cryoHandler.getRecipes(), CRYO);

        IIngredientBlacklist blacklist = registry.getJeiHelpers().getIngredientBlacklist();
        blacklist.addIngredientToBlacklist(new ItemStack(ModBlocksSpace.dummy_beam));
        blacklist.addIngredientToBlacklist(new ItemStack(ModBlocksSpace.orbital_station));
        blacklist.addIngredientToBlacklist(new ItemStack(ModItemsSpace.rocket_custom));
        blacklist.addIngredientToBlacklist(new ItemStack(ModBlocksSpace.machine_htrf4neo)); // it's still in-dev, afaik
        blacklist.addIngredientToBlacklist(new ItemStack(ModBlocksSpace.furnace));
        blacklist.addIngredientToBlacklist(new ItemStack(ModBlocksSpace.lit_furnace));
        //blacklist.addIngredientToBlacklist(new ItemStack(ModBlocksSpace.war_controller));
        //blacklist.addIngredientToBlacklist(new ItemStack(ModItemsSpace.sat_war));
    }

    @Override
    public void registerCategories(@NotNull IRecipeCategoryRegistration registry) {
        if (!GeneralConfig.jei)
            return;
        IGuiHelper help = registry.getJeiHelpers().getGuiHelper();
        registry.addRecipeCategories(
                vacuumCircuitHandler = new VacuumCircuitHandler(help),
                dairyHandler = new DairyHandler(help),
                cryoHandler = new CryoHandler(help)
        );
    }
}
