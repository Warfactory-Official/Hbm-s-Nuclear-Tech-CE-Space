package com.hbmspace.inventory.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.hbm.inventory.RecipesCommon;
import com.hbm.inventory.recipes.loader.SerializableRecipe;
import com.hbm.items.ItemEnums;
import com.hbm.items.ModItems;
import com.hbmspace.items.ItemEnumsSpace;
import com.hbmspace.items.ModItemsSpace;
import net.minecraft.item.ItemStack;

import java.io.IOException;
import java.util.*;

public class VacuumCircuitRecipes extends SerializableRecipe {

    public static List<VacuumCircuitRecipe> recipes = new ArrayList<>();

    @Override
    public void registerDefaults() {

        //t0 you can go to the mun when you got all the shit. :)
        recipes.add(new VacuumCircuitRecipe(new ItemStack(ModItemsSpace.circuit, 1, ItemEnumsSpace.EnumCircuitType.PROCESST1.ordinal()), 200, 250,
                new RecipesCommon.AStack[] {
                        new RecipesCommon.ComparableStack(ModItems.circuit, 4, ItemEnums.EnumCircuitType.CHIP)},
                new RecipesCommon.AStack[] {
                        new RecipesCommon.ComparableStack(ModItems.circuit, 1, ItemEnums.EnumCircuitType.PCB),
                        new RecipesCommon.ComparableStack(ModItems.circuit, 2, ItemEnums.EnumCircuitType.CAPACITOR)}
        ));

        recipes.add(new VacuumCircuitRecipe(new ItemStack(ModItemsSpace.circuit, 1, ItemEnumsSpace.EnumCircuitType.PROCESST2.ordinal()), 400, 1_000,
                new RecipesCommon.AStack[] {
                        new RecipesCommon.ComparableStack(ModItemsSpace.circuit, 1, ItemEnumsSpace.EnumCircuitType.GASCHIP)},
                new RecipesCommon.AStack[] {
                        new RecipesCommon.ComparableStack(ModItems.circuit, 1, ItemEnums.EnumCircuitType.BASIC),
                        new RecipesCommon.ComparableStack(ModItems.circuit, 2, ItemEnums.EnumCircuitType.CAPACITOR)}
        ));

        recipes.add(new VacuumCircuitRecipe(new ItemStack(ModItemsSpace.circuit, 1, ItemEnumsSpace.EnumCircuitType.PROCESST3.ordinal()), 800, 25_000,
                new RecipesCommon.AStack[] {
                        new RecipesCommon.ComparableStack(ModItemsSpace.circuit, 2, ItemEnumsSpace.EnumCircuitType.GASCHIP),
                        new RecipesCommon.ComparableStack(ModItemsSpace.circuit, 1, ItemEnumsSpace.EnumCircuitType.HFCHIP)},
                new RecipesCommon.AStack[] {
                        new RecipesCommon.ComparableStack(ModItems.circuit, 1, ItemEnums.EnumCircuitType.ADVANCED),
                        new RecipesCommon.ComparableStack(ModItemsSpace.circuit, 2, ItemEnumsSpace.EnumCircuitType.CAPACITOR_LANTHANIUM)}
        ));

    }


    public static VacuumCircuitRecipe getRecipe(ItemStack[] inputs) {

        for(VacuumCircuitRecipe recipe : recipes) {
            if(matchesIngredients(new ItemStack[] {inputs[0], inputs[1]}, recipe.wafer) &&
                    matchesIngredients(new ItemStack[] {inputs[2], inputs[3]}, recipe.pcb)) return recipe;
        }

        return null;
    }

    public static HashMap<Object, Object> getRecipes() {

        HashMap<Object, Object> recipes = new HashMap<>();

        for(VacuumCircuitRecipe recipe : VacuumCircuitRecipes.recipes) {

            List<RecipesCommon.AStack> ingredients = new ArrayList<>();
            Collections.addAll(ingredients, recipe.wafer);
            Collections.addAll(ingredients, recipe.pcb);

            recipes.put(ingredients.toArray(), recipe.output);
        }

        return recipes;
    }

    @Override
    public String getFileName() {
        return "hbmVacuumCircuit.json";
    }

    @Override
    public Object getRecipeObject() {
        return recipes;
    }

    @Override
    public void deleteRecipes() {
        recipes.clear();
        wafer.clear();
        pcb.clear();
    }

    @Override
    public void readRecipe(JsonElement recipe) {
        JsonObject obj = (JsonObject) recipe;

        RecipesCommon.AStack[] wafer = readAStackArray(obj.get("wafer").getAsJsonArray());
        RecipesCommon.AStack[] pcb = readAStackArray(obj.get("pcb").getAsJsonArray());
        ItemStack output = readItemStack(obj.get("output").getAsJsonArray());
        int duration = obj.get("duration").getAsInt();
        long consumption = obj.get("consumption").getAsLong();

        recipes.add(new VacuumCircuitRecipe(output, duration, consumption, wafer, pcb));
    }

    @Override
    public void writeRecipe(Object obj, JsonWriter writer) throws IOException {
        VacuumCircuitRecipe recipe = (VacuumCircuitRecipe) obj;

        writer.name("wafer").beginArray();
        for(RecipesCommon.AStack aStack : recipe.wafer) writeAStack(aStack, writer);
        writer.endArray();

        writer.name("pcb").beginArray();
        for(RecipesCommon.AStack aStack : recipe.pcb) writeAStack(aStack, writer);
        writer.endArray();


        writer.name("output");
        writeItemStack(recipe.output, writer);

        writer.name("duration").value(recipe.duration);
        writer.name("consumption").value(recipe.consumption);
    }

    public static HashSet<RecipesCommon.AStack> wafer = new HashSet<>();
    public static HashSet<RecipesCommon.AStack> pcb = new HashSet<>();

    public static class VacuumCircuitRecipe {

        public RecipesCommon.AStack[] wafer;
        public RecipesCommon.AStack[] pcb;
        public ItemStack output;
        public int duration;
        public long consumption;

        public VacuumCircuitRecipe(ItemStack output, int duration, long consumption, RecipesCommon.AStack[] wafer, RecipesCommon.AStack[] pcb) {
            this.wafer = wafer;
            this.pcb = pcb;
            this.output = output;
            this.duration = duration;
            this.consumption = consumption;
            VacuumCircuitRecipes.wafer.addAll(Arrays.asList(wafer));
            VacuumCircuitRecipes.pcb.addAll(Arrays.asList(pcb));
        }
    }
}
