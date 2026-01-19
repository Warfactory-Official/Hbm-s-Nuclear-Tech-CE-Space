package com.hbmspace.mixin.mod.hbm;

import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.trait.FT_Corrosive;
import com.hbm.render.misc.EnumSymbol;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import static com.hbmspace.inventory.fluid.Fluids.*;

import java.util.List;

@Mixin(value = Fluids.class, remap = false)
public abstract class MixinFluids {

    @Shadow
    @Final
    protected static List<FluidType> metaOrder;

    @Unique
    private static FluidType space$createFixed(String name, int color, int p, int f, int r, EnumSymbol symbol, int id) {
        FluidType fluid = new FluidType(name, color, p, f, r, symbol, name.toLowerCase(java.util.Locale.US), 0xFFFFFF, id, null);
        fluid.customFluid = false; // since otherwise I won't be able to render corresponding textures for tanks

        return fluid;
    }

    @Inject(
            method = "init",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/List;addAll(Ljava/util/Collection;)Z",
                    shift = At.Shift.AFTER
            )
    )
    private static void hbmextra$registerExtraFluids(CallbackInfo ci) {
        if (Fluids.fromName("EARTHAIR") != Fluids.NONE) return;

        int idCounter = 4000;

        EARTHAIR = space$createFixed("EARTHAIR", 0xD1CEEE, 0, 0, 0, EnumSymbol.NONE, idCounter++).addTraits(Fluids.GASEOUS);
        CCL = space$createFixed("CCL", 0x0C3B2F, 0, 0, 0, EnumSymbol.NONE, idCounter++).addTraits(Fluids.LIQUID, new FT_Corrosive(10));
        CHLOROETHANE = space$createFixed("CHLOROETHANE", 0xBBA9A0, 2, 0, 0, EnumSymbol.NONE, idCounter++).addTraits(Fluids.GASEOUS);
        CBENZ = space$createFixed("CBENZ", 0x91C6BB, 0, 0, 0, EnumSymbol.NONE, idCounter++).addTraits(Fluids.LIQUID);
        VINYL = space$createFixed("VINYL", 0xA2A2A2, 0, 0, 0, EnumSymbol.NONE, idCounter++).addTraits(LIQUID);
        AQUEOUS_COPPER = space$createFixed("AQUEOUS_COPPER", 0x4CC2A2, 0, 0, 0, EnumSymbol.NONE, idCounter++).addTraits(LIQUID, VISCOUS);
        AQUEOUS_NICKEL = space$createFixed("AQUEOUS_NICKEL", 0xDACEBA, 0, 0, 0, EnumSymbol.NONE, idCounter++).addTraits(LIQUID);
        COPPERSULFATE = space$createFixed("COPPERSULFATE", 0x55E5CF, 0, 0, 0, EnumSymbol.NONE, idCounter++).addTraits(LIQUID, VISCOUS);
        BRINE = space$createFixed("BRINE", 0xD1A73E, 3, 3, 3, EnumSymbol.NONE, idCounter++).addTraits(LIQUID, VISCOUS);
        CONGLOMERA = space$createFixed("CONGLOMERA", 0x364D47, 0, 0, 2, EnumSymbol.NONE, idCounter++).addTraits(LIQUID, VISCOUS);
        HGAS = space$createFixed("HGAS", 0x999368, 0, 0, 0, EnumSymbol.ACID, idCounter++).addTraits(GASEOUS, new FT_Corrosive(120));
        HALOLIGHT = space$createFixed("HALOLIGHT", 0xB6F9CF, 0, 0, 0, EnumSymbol.NONE, idCounter++).addTraits(LIQUID);
        LITHCARBONATE = space$createFixed("LITHCARBONATE", 0xD1CEBE, 0, 0, 0, EnumSymbol.NONE, idCounter++).addTraits(GASEOUS);

        metaOrder.add(EARTHAIR);
        metaOrder.add(CCL);
        metaOrder.add(CHLOROETHANE);
        metaOrder.add(CBENZ);
        metaOrder.add(VINYL);
        metaOrder.add(AQUEOUS_COPPER);
        metaOrder.add(AQUEOUS_NICKEL);
        metaOrder.add(COPPERSULFATE);
        metaOrder.add(BRINE);
        metaOrder.add(CONGLOMERA);
        metaOrder.add(HGAS);
        metaOrder.add(HALOLIGHT);
        metaOrder.add(LITHCARBONATE);
    }
}
