package com.hbmspace.commands;

import com.hbmspace.config.SpaceConfig;
import com.hbmspace.dim.CelestialBody;
import com.hbmspace.dim.CelestialTeleporter;
import com.hbmspace.dim.SolarSystem;
import com.hbmspace.dim.SolarSystemWorldSavedData;
import com.hbmspace.dim.orbit.OrbitalStation;
import com.hbmspace.items.ItemVOTVdrive;
import com.hbmspace.items.ModItemsSpace;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.WorldServer;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class CommandStations extends CommandBase {

    @Override
    public @NotNull String getName() {
        return "ntmstations";
    }

    @Override
    public @NotNull String getUsage(@NotNull ICommandSender sender) {
        return String.format(Locale.US,
                """
                        %s/%s launch %s- Spawns a station for the held drive.
                        %s/%s tp %s- Teleport to held drive station.
                        %s/%s list %s- Lists all active stations.
                        %s/%s fetch <id|name> %s- Creates a drive programmed with a specific station ID or name.""",
                TextFormatting.GREEN, getName(), TextFormatting.LIGHT_PURPLE,
                TextFormatting.GREEN, getName(), TextFormatting.LIGHT_PURPLE,
                TextFormatting.GREEN, getName(), TextFormatting.LIGHT_PURPLE,
                TextFormatting.GREEN, getName(), TextFormatting.LIGHT_PURPLE
        );
    }

    @Override
    public void execute(@NotNull MinecraftServer server, @NotNull ICommandSender sender, String @NotNull [] args) throws CommandException {
        if(!(sender instanceof EntityPlayer)) {
            showMessage(sender, "commands.satellite.should_be_run_as_player", true);
            return;
        } else if(args.length == 0) {
            throw new WrongUsageException(getUsage(sender));
        }

        EntityPlayerMP player = getCommandSenderAsPlayer(sender);
        SolarSystemWorldSavedData data = SolarSystemWorldSavedData.get(player.world);

        switch (args[0]) {
            case "launch":
                ItemVOTVdrive.Destination dest = getStationDestination(sender, player.getHeldItemMainhand());
                if(dest == null) return;

                OrbitalStation.addStation(dest.x, dest.z, CelestialBody.getBody(player.world));
                showMessage(sender, "commands.station.launched", false);
                break;

            case "tp":
                ItemVOTVdrive.Destination destination = getStationDestination(sender, player.getHeldItemMainhand());
                if(destination == null) return;

                int dimensionId = destination.body.getDimensionId();
                int x = destination.x;
                int z = destination.z;

                if(dimensionId == SpaceConfig.orbitDimension) {
                    x = x * OrbitalStation.STATION_SIZE + (OrbitalStation.STATION_SIZE / 2);
                    z = z * OrbitalStation.STATION_SIZE + (OrbitalStation.STATION_SIZE / 2);
                }

                player.dismountRidingEntity();

                if(player.dimension != dimensionId) {
                    if(dimensionId == SpaceConfig.orbitDimension) {
                        CelestialTeleporter.teleport(player, dimensionId, x + 0.5D, 130.0D, z + 0.5D, false);
                    } else {
                        CelestialTeleporter.teleport(player, dimensionId, x + 0.5D, 300.0D, z + 0.5D, true);
                    }
                } else {
                    if(dimensionId == SpaceConfig.orbitDimension) {
                        player.setPositionAndUpdate(x + 0.5D, 130.0D, z + 0.5D);
                    } else {
                        int y = player.world.getHeight(new BlockPos(x, 0, z)).getY();
                        player.setPositionAndUpdate(x + 0.5D, y + 1, z + 0.5D);
                    }
                }

                if(dimensionId == SpaceConfig.orbitDimension) {
                    WorldServer targetWorld = server.getWorld(SpaceConfig.orbitDimension);
                    OrbitalStation.spawn(targetWorld, x, z);
                }

                showMessage(sender, "commands.station.teleported", false);
                break;

            case "list":
                boolean hasAnyStations = false;
                for(OrbitalStation station : data.getStations().values()) {
                    if(!station.hasStation) continue;

                    String messageText = "0x" + Integer.toHexString(new ChunkPos(station.dX, station.dZ).hashCode()).toUpperCase();
                    if(station.name != null && !station.name.isEmpty()) messageText += " - " + station.name;

                    showMessage(sender, messageText, false);
                    hasAnyStations = true;
                }

                if(!hasAnyStations) {
                    showMessage(sender, "commands.station.no_stations", true);
                }
                break;

            case "fetch":
                if(args.length < 2 || args[1] == null || args[1].isEmpty()) {
                    showMessage(sender, "commands.station.invalid_station", true);
                } else {
                    boolean hasMatch = false;
                    StringBuilder toMatchBuilder = new StringBuilder(args[1]);
                    for(int i = 2; i < args.length; i++) {
                        toMatchBuilder.append(" ").append(args[i]);
                    }

                    String toMatch = toMatchBuilder.toString().trim();

                    for(OrbitalStation station : data.getStations().values()) {
                        String stationId = "0x" + Integer.toHexString(new ChunkPos(station.dX, station.dZ).hashCode()).toUpperCase();
                        if(station.name.trim().equalsIgnoreCase(toMatch) || stationId.equalsIgnoreCase(toMatch)) {
                            ItemStack drive = new ItemStack(ModItemsSpace.full_drive, 1, SolarSystem.Body.ORBIT.ordinal());

                            NBTTagCompound nbt = new NBTTagCompound();
                            nbt.setInteger("x", station.dX);
                            nbt.setInteger("z", station.dZ);
                            nbt.setBoolean("Processed", true);
                            nbt.setString("stationName", station.name);
                            drive.setTagCompound(nbt);

                            if(!player.inventory.addItemStackToInventory(drive)) {
                                player.dropItem(drive, false);
                            }

                            showMessage(sender, "commands.station.drive_created", false);
                            hasMatch = true;
                            break;
                        }
                    }

                    if(!hasMatch) {
                        showMessage(sender, "commands.station.no_match", true);
                    }
                }
                break;
        }
    }

    private static void showMessage(ICommandSender sender, String error, boolean isError) {
        TextComponentTranslation message = new TextComponentTranslation(error);
        message.getStyle().setColor(isError ? TextFormatting.RED : TextFormatting.GREEN);
        sender.sendMessage(message);
    }

    private static ItemVOTVdrive.Destination getStationDestination(ICommandSender sender, ItemStack stack) {
        if(stack.isEmpty() || !(stack.getItem() instanceof ItemVOTVdrive)) {
            showMessage(sender, "commands.station.invalid_drive", true);
            return null;
        }

        return ItemVOTVdrive.getDestination(stack);
    }

    @Override
    public @NotNull List<String> getTabCompletions(@NotNull MinecraftServer server, @NotNull ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if(args.length == 1) {
            return getListOfStringsMatchingLastWord(args, "launch", "tp", "list", "fetch");
        }
        return Collections.emptyList();
    }
}
