package com.hbmspace.items.food;

import com.hbm.config.VersatileConfig;
import com.hbm.handler.threading.PacketThreading;
import com.hbm.items.IDynamicModels;
import com.hbm.items.ModItems;
import com.hbm.items.food.ItemPill;
import com.hbm.lib.HBMSoundHandler;
import com.hbm.packet.toclient.AuxParticlePacketNT;
import com.hbmspace.items.IDynamicModelsSpace;
import com.hbmspace.items.ModItemsSpace;
import com.hbmspace.lib.HBMSpaceSoundHandler;
import com.hbmspace.potion.HbmPotion;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.jetbrains.annotations.NotNull;

public class ItemPillSpace extends ItemPill implements IDynamicModelsSpace {

    public ItemPillSpace(int hunger, String s) {
        super(hunger, s);
        ModItems.ALL_ITEMS.remove(this);
        ModItemsSpace.ALL_ITEMS.add(this);
        IDynamicModels.INSTANCES.remove(this);
        IDynamicModelsSpace.INSTANCES.add(this);
    }

    @Override
    protected void onFoodEaten(@NotNull ItemStack stack, World world, @NotNull EntityPlayer player) {
        if (!world.isRemote) {
            VersatileConfig.applyPotionSickness(player, 5);

            if(this == ModItemsSpace.animan) {
                int x = MathHelper.floor(player.posX);
                int y = MathHelper.floor(player.posY);
                int z = MathHelper.floor(player.posZ);
                world.playSound(null, x, y, z, HBMSpaceSoundHandler.oil, SoundCategory.PLAYERS, 1.0F, 1.0F);

                player.addPotionEffect(new PotionEffect(HbmPotion.run, 10 * 20, 0));

                NBTTagCompound nbt = new NBTTagCompound();
                nbt.setString("type", "vomit");
                nbt.setString("mode", "normal");
                nbt.setInteger("count", 25);
                nbt.setInteger("entity", player.getEntityId());
                PacketThreading.createAllAroundThreadedPacket(new AuxParticlePacketNT(nbt, 0, 0, 0),  new NetworkRegistry.TargetPoint(player.dimension, x, y, z, 25));

                world.playSound(null, x, y, z, HBMSoundHandler.vomit, SoundCategory.PLAYERS, 1.0F, 1.0F);
                player.addPotionEffect(new PotionEffect(MobEffects.HUNGER, 60, 19));

            }
        }
    }
}
