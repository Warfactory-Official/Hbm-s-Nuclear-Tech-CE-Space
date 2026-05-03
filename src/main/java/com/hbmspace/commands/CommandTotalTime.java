package com.hbmspace.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldInfo;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class CommandTotalTime extends CommandBase {

    @Override
    public @NotNull String getName() {
        return "totaltime";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public @NotNull String getUsage(@NotNull ICommandSender sender) {
        return "commands.time.usage";
    }

    @Override
    public void execute(@NotNull MinecraftServer server, @NotNull ICommandSender sender, String[] args) throws CommandException {
        if(args.length > 1) {
            long i;

            if(args[0].equals("set")) {
                i = parseInt(args[1], 0);
                this.setTime(server, i);
                notifyCommandListener(sender, this, "commands.time.set", i);
                return;
            }

            if(args[0].equals("add")) {
                i = parseInt(args[1], 0);
                this.addTime(server, i);
                notifyCommandListener(sender, this, "commands.time.added", i);
                return;
            }
        }

        throw new WrongUsageException("commands.time.usage");
    }

    @Override
    public @NotNull List<String> getTabCompletions(@NotNull MinecraftServer server, @NotNull ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, "set", "add") : Collections.emptyList();
    }

    protected void setTime(MinecraftServer server, long time) {
        for (WorldServer world : server.worlds) {
            world.getWorldInfo().setWorldTotalTime(time);
        }
    }

    protected void addTime(MinecraftServer server, long time) {
        for (WorldServer world : server.worlds) {
            WorldInfo worldInfo = world.getWorldInfo();
            worldInfo.setWorldTotalTime(worldInfo.getWorldTotalTime() + time);
        }
    }
}
