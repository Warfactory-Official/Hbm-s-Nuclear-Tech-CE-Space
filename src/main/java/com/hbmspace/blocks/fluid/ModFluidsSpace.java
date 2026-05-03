package com.hbmspace.blocks.fluid;

import com.hbm.blocks.fluid.FluidNTM;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class ModFluidsSpace {
    public static Fluid mercury_fluid = new FluidNTM("mercury", "forgefluid/mercury_still", "forgefluid/mercury_flowing").setDensity(2500).setViscosity(2000).setTemperature(70+273);
    public static Fluid ccl_fluid = new FluidNTM("ccl", "ccl_still", "ccl_flowing").setDensity(1840).setViscosity(1000).setTemperature(273);
    public static Fluid bromine_fluid = new FluidNTM("bromine", "forgefluid/bromine_still", "forgefluid/bromine_flowing").setDensity(3000).setViscosity(3000).setTemperature(273);

    public static void init() {
        registerFluid(mercury_fluid);
        registerFluid(ccl_fluid);
        registerFluid(bromine_fluid);
    }

    private static void registerFluid(Fluid fluid) {
        FluidRegistry.registerFluid(fluid);
        FluidRegistry.addBucketForFluid(fluid);
    }

    public static void setFromRegistry() {
        mercury_fluid = FluidRegistry.getFluid("mercury");
        ccl_fluid = FluidRegistry.getFluid("ccl");
        bromine_fluid = FluidRegistry.getFluid("bromine");
    }
}
