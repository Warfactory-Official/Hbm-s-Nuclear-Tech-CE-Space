package com.hbmspace.handler.jei;

import com.hbm.blocks.ModBlocks;
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
    public static final String ALKYLATION = "hbm.alkylation";
    public static final String ATMO = "hbm.atmo_compress";
    public static final String BEDROCK = "hbm.bedrock_drill";
    public static final String OIL = "hbm.oil_extraction";
    public static final String WATER = "hbm.water_table";
    private VacuumCircuitHandler vacuumCircuitHandler;
    private DairyHandler dairyHandler;
    private CryoHandler cryoHandler;
    private AlkylationHandler alkylationHandler;
    private AtmosphericCompressorHandler atmosphericCompressorHandler;
    private BedrockDrillHandler bedrockDrillHandler;
    private OilExtractionHandler oilExtractionHandler;
    private WaterTableHandler waterTableHandler;

    @Override
    public void register(@NotNull IModRegistry registry) {
        if (!GeneralConfig.jei)
            return;

        registry.addRecipeCatalyst(new ItemStack(ModBlocksSpace.machine_vacuum_circuit), VACUUM_CIRCUIT);
        registry.addRecipeCatalyst(new ItemStack(ModBlocksSpace.machine_milk_reformer), DAIRY);
        registry.addRecipeCatalyst(new ItemStack(ModBlocksSpace.machine_cryo_distill), CRYO);
        registry.addRecipeCatalyst(new ItemStack(ModBlocksSpace.machine_alkylation), ALKYLATION);
        registry.addRecipeCatalyst(new ItemStack(ModBlocksSpace.machine_atmo_vent), ATMO);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.machine_excavator), BEDROCK);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.machine_well), OIL);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.machine_pumpjack), OIL);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.machine_fracking_tower), OIL);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.pump_electric), WATER);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.pump_steam), WATER);

        registry.addRecipes(vacuumCircuitHandler.getRecipes(), VACUUM_CIRCUIT);
        registry.addRecipes(dairyHandler.getRecipes(), DAIRY);
        registry.addRecipes(cryoHandler.getRecipes(), CRYO);
        registry.addRecipes(alkylationHandler.getRecipes(), ALKYLATION);
        registry.addRecipes(atmosphericCompressorHandler.getRecipes(), ATMO);
        registry.addRecipes(bedrockDrillHandler.getRecipes(), BEDROCK);
        registry.addRecipes(oilExtractionHandler.getRecipes(), OIL);
        registry.addRecipes(waterTableHandler.getRecipes(), WATER);

        IIngredientBlacklist blacklist = registry.getJeiHelpers().getIngredientBlacklist();
        blacklist.addIngredientToBlacklist(new ItemStack(ModBlocksSpace.dummy_beam));
        blacklist.addIngredientToBlacklist(new ItemStack(ModBlocksSpace.orbital_station));
        blacklist.addIngredientToBlacklist(new ItemStack(ModItemsSpace.rocket_custom));
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
                cryoHandler = new CryoHandler(help),
                alkylationHandler = new AlkylationHandler(help),
                atmosphericCompressorHandler = new AtmosphericCompressorHandler(help),
                bedrockDrillHandler = new BedrockDrillHandler(help),
                oilExtractionHandler = new OilExtractionHandler(help),
                waterTableHandler = new WaterTableHandler(help)
        );
    }
}
