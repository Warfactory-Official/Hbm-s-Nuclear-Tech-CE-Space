package com.hbmspace.commands;

import com.hbm.util.I18nUtil;
import com.hbmspace.dim.CelestialTeleporter;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.WorldServer;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandSpaceTP extends CommandBase {

    @Override
    public @NotNull String getName() {
        return "dimtp";
    }

    @Override
    public @NotNull String getUsage(@NotNull ICommandSender sender) {
        return "/dimtp <dimension_id_or_name> [player]";
    }

    @Override
    public @NotNull List<String> getAliases() {
        return Collections.singletonList("dimtp");
    }

    @Override
    public void execute(@NotNull MinecraftServer server, @NotNull ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 1) {
            throw new CommandException(TextFormatting.RED + getUsage(sender));
        }

        int dimensionId = -1;
        try {
            dimensionId = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            for (WorldServer world : server.worlds) {
                if (world.provider.getDimensionType().getName().equalsIgnoreCase(args[0])) {
                    dimensionId = world.provider.getDimension();
                    break;
                }
            }
            if (dimensionId == -1) {
                throw new CommandException(TextFormatting.RED + I18nUtil.resolveKey("commands.dimtp.dimension_not_found"), args[0]);
            }
        }

        EntityPlayerMP targetPlayer;
        if (args.length >= 2) {
            targetPlayer = getPlayer(server, sender, args[1]);
        } else {
            if (sender instanceof EntityPlayerMP player) {
                targetPlayer = player;
            } else {
                throw new CommandException(TextFormatting.RED + I18nUtil.resolveKey("commands.dimtp.not_player"));
            }
        }

        WorldServer targetWorld = server.getWorld(dimensionId);
        if (targetWorld == null) {
            throw new CommandException(TextFormatting.RED + I18nUtil.resolveKey("commands.dimtp.dimension_not_found"), dimensionId);
        }

        BlockPos pos = targetPlayer.getPosition();
        WorldServer sourceServer = server.getWorld(targetPlayer.dimension);
        targetPlayer.changeDimension(dimensionId, new CelestialTeleporter(sourceServer, targetWorld, targetPlayer, pos.getX(), pos.getY(), pos.getZ(), true));
    }

    @Override
    public boolean checkPermission(@NotNull MinecraftServer server, ICommandSender sender) {
        return sender.canUseCommand(2, this.getName());
    }

    @Override
    public @NotNull List<String> getTabCompletions(@NotNull MinecraftServer server, @NotNull ICommandSender sender, String @NotNull [] args, @Nullable BlockPos targetPos) {
        if (args.length == 1) {
            List<String> suggestions = new ArrayList<>();
            for (WorldServer world : server.worlds) {
                suggestions.add(String.valueOf(world.provider.getDimension()));
                suggestions.add(world.provider.getDimensionType().getName());
            }
            return getListOfStringsMatchingLastWord(args, suggestions);
        } else if (args.length == 2) {
            return getListOfStringsMatchingLastWord(args, Arrays.asList(server.getOnlinePlayerNames()));
        }
        return Collections.emptyList();
    }

}