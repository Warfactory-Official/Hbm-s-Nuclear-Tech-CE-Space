package com.hbmspace.render.item;

import com.hbm.render.item.ItemRenderBase;
import com.hbmspace.interfaces.AutoRegister;
import com.hbmspace.main.ResourceManagerSpace;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;

import static com.hbm.render.NTMRenderHelper.bindTexture;

@AutoRegister(item = "swarm_member")
public class ItemRenderSwarmMember extends ItemRenderBase {
    @Override
    public void renderInventory() {
        GlStateManager.translate(0, 3, 0);
        GlStateManager.scale(1.8D, 1.8D, 1.8D);
        GlStateManager.rotate(-45, 0, 0, 1);
        GlStateManager.rotate(30, 1, 0, 0);
        GlStateManager.rotate(System.currentTimeMillis() % 7200 * -0.05F, 0, 1, 0);
    }

    @Override
    public void renderCommon(ItemStack item) {
        bindTexture(ResourceManagerSpace.dyson_swarm_member_tex);
        ResourceManagerSpace.dyson_swarm_member.renderAll();
    }
}
