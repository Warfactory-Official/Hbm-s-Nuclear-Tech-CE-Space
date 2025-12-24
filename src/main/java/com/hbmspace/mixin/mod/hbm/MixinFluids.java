package com.hbmspace.mixin.mod.hbm;

import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.trait.FT_Corrosive;
import com.hbm.render.misc.EnumSymbol;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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

        EARTHAIR = new FluidType("EARTHAIR", 0xD1CEEE, 0, 0, 0, EnumSymbol.NONE).addTraits(Fluids.GASEOUS);
        CCL = new FluidType("CCL", 0x0C3B2F, 0, 0, 0, EnumSymbol.NONE).addTraits(Fluids.LIQUID, new FT_Corrosive(10));
        CHLOROETHANE = new FluidType("CHLOROETHANE", 0xBBA9A0, 2, 0, 0, EnumSymbol.NONE).addTraits(Fluids.GASEOUS);
        CBENZ = new FluidType("CBENZ", 0x91C6BB, 0, 0, 0, EnumSymbol.NONE).addTraits(Fluids.LIQUID);
        VINYL =					new FluidType("VINYL",				0xA2A2A2, 0, 0, 0, EnumSymbol.NONE).addTraits(LIQUID);
        AQUEOUS_COPPER =		new FluidType("AQUEOUS_COPPER",		0x4CC2A2, 0, 0, 0, EnumSymbol.NONE).addTraits(LIQUID, VISCOUS);
        AQUEOUS_NICKEL =		new FluidType("AQUEOUS_NICKEL",		0xDACEBA, 0, 0, 0, EnumSymbol.NONE).addTraits(LIQUID);
        COPPERSULFATE =			new FluidType("COPPERSULFATE",		0x55E5CF, 0, 0, 0, EnumSymbol.NONE).addTraits(LIQUID, VISCOUS);
        BRINE =					new FluidType("BRINE",				0xD1A73E, 3, 3, 3, EnumSymbol.NONE).addTraits(LIQUID, VISCOUS);
        CONGLOMERA =			new FluidType("CONGLOMERA",			0x364D47, 0, 0, 2, EnumSymbol.NONE).addTraits(LIQUID, VISCOUS);
        HGAS =					new FluidType("HGAS",				0x999368, 0, 0, 0, EnumSymbol.ACID).addTraits(GASEOUS,  new FT_Corrosive(120));
        HALOLIGHT =				new FluidType("HALOLIGHT",			0xB6F9CF, 0, 0, 0, EnumSymbol.NONE).addTraits(LIQUID);

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
    }
}
