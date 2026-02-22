package com.hbmspace.mixin.mod.hbm.items;

import com.hbm.items.special.ItemBedrockOreNew;
import com.hbm.items.tool.ItemOreDensityScanner;
import com.hbm.packet.PacketDispatcher;
import com.hbm.packet.toclient.PlayerInformPacketLegacy;
import com.hbmspace.dim.CelestialBody;
import com.hbmspace.dim.SolarSystem;
import com.hbmspace.util.BedrockOreUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemOreDensityScanner.class)
public abstract class MixinItemOreDensityScanner {

    @Inject(method = "onUpdate", at = @At("HEAD"), cancellable = true)
    private void space$onUpdate(ItemStack stack, World world, Entity entity, int i, boolean bool, CallbackInfo ci) {
        ci.cancel();

        if (!(entity instanceof EntityPlayerMP player) || world.getTotalWorldTime() % 5 != 0) {
            return;
        }

        SolarSystem.Body body = CelestialBody.getEnum(world);

        for (ItemBedrockOreNew.BedrockOreType type : BedrockOreUtil.getTypesForBody(body)) {
            double level = BedrockOreUtil.getOreLevel(world, (int) Math.floor(player.posX), (int) Math.floor(player.posZ), type);

            PacketDispatcher.wrapper.sendTo(new PlayerInformPacketLegacy(
                    new TextComponentTranslation("item.bedrock_ore.type." + type.suffix + ".name")
                            .appendText(": " + ((int) (level * 100) / 100D) + " (")
                            .appendSibling(
                                    new TextComponentTranslation(ItemOreDensityScanner.translateDensity(level))
                                            .setStyle(new Style().setColor(ItemOreDensityScanner.getColor(level)))
                            )
                            .appendText(")")
                            .setStyle(new Style().setColor(TextFormatting.RESET)),
                    777 + type.ordinal(), 4000), player);
        }
    }
}
