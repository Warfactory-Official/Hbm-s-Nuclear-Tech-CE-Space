package com.hbmspace.mixin.mod.hbm.items;

import com.hbm.items.tool.ItemOilDetector;
import com.hbm.lib.HBMSoundHandler;
import com.hbm.packet.PacketDispatcher;
import com.hbm.packet.toclient.PlayerInformPacketLegacy;
import com.hbmspace.blocks.generic.BlockOreFluid;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ItemOilDetector.class)
public abstract class MixinItemOilDetector extends Item {

    @Overwrite
    public @NotNull ActionResult<ItemStack> onItemRightClick(World world, @NotNull EntityPlayer player, @NotNull EnumHand hand) {

        if (!world.isRemote) {
            boolean direct = false;
            int x = MathHelper.floor(player.posX);
            int y = MathHelper.floor(player.posY);
            int z = MathHelper.floor(player.posZ);

            Block reserve;

            if ((reserve = space$searchDirect(world, x, y, z)) != null) {
                direct = true;
            } else {
                reserve = space$search(world, x, y, z);
            }

            String reserveType = "";
            if (reserve instanceof BlockOreFluid) {
                reserveType = ((BlockOreFluid) reserve).getUnlocalizedReserveType();
            }

            if (direct) {
                TextComponentTranslation text = new TextComponentTranslation(this.getTranslationKey() + ".bullseye" + reserveType);
                text.setStyle(new Style().setColor(TextFormatting.DARK_GREEN));
                PacketDispatcher.wrapper.sendTo(new PlayerInformPacketLegacy(text, 8), (EntityPlayerMP) player);
            } else if (reserve != null) {
                TextComponentTranslation text = new TextComponentTranslation(this.getTranslationKey() + ".detected" + reserveType);
                text.setStyle(new Style().setColor(TextFormatting.GOLD));
                PacketDispatcher.wrapper.sendTo(new PlayerInformPacketLegacy(text, 8), (EntityPlayerMP) player);
            } else {
                TextComponentTranslation text = new TextComponentTranslation(this.getTranslationKey() + ".noOil");
                text.setStyle(new Style().setColor(TextFormatting.RED));
                PacketDispatcher.wrapper.sendTo(new PlayerInformPacketLegacy(text, 8), (EntityPlayerMP) player);
            }
        }

        world.playSound(null, player.posX, player.posY, player.posZ, HBMSoundHandler.techBleep, SoundCategory.PLAYERS, 1.0F, 1.0F);

        player.swingArm(hand);

        return ActionResult.newResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }

    @Unique
    private Block space$search(World world, int x, int y, int z) {
        Block reserve;
        if ((reserve = space$searchDirect(world, x + 5, y, z)) != null) return reserve;
        if ((reserve = space$searchDirect(world, x - 5, y, z)) != null) return reserve;
        if ((reserve = space$searchDirect(world, x, y, z + 5)) != null) return reserve;
        if ((reserve = space$searchDirect(world, x, y, z - 5)) != null) return reserve;

        if ((reserve = space$searchDirect(world, x + 10, y, z)) != null) return reserve;
        if ((reserve = space$searchDirect(world, x - 10, y, z)) != null) return reserve;
        if ((reserve = space$searchDirect(world, x, y, z + 10)) != null) return reserve;
        if ((reserve = space$searchDirect(world, x, y, z - 10)) != null) return reserve;

        if ((reserve = space$searchDirect(world, x + 5, y, z + 5)) != null) return reserve;
        if ((reserve = space$searchDirect(world, x - 5, y, z + 5)) != null) return reserve;
        if ((reserve = space$searchDirect(world, x + 5, y, z - 5)) != null) return reserve;
        if ((reserve = space$searchDirect(world, x - 5, y, z - 5)) != null) return reserve;

        return null;
    }

    @Unique
    private Block space$searchDirect(World world, int x, int y, int z) {
        BlockPos.MutableBlockPos mPos = new BlockPos.MutableBlockPos();
        for (int i = y + 15; i > 5; i--) {
            Block block = world.getBlockState(mPos.setPos(x, i, z)).getBlock();
            if (block instanceof BlockOreFluid) return block;
        }

        return null;
    }
}