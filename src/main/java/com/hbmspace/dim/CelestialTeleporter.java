package com.hbmspace.dim;

import com.hbmspace.entity.missile.EntityRideableRocket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.Queue;

public class CelestialTeleporter extends Teleporter {

	private final WorldServer sourceServer;
	private final WorldServer targetServer;

	private final double x;
	private double y;
	private final double z;

	private final boolean grounded;

	private Entity entity;

	public CelestialTeleporter(WorldServer sourceServer, WorldServer targetServer, Entity entity, double x, double y, double z, boolean grounded) {
		super(targetServer);
		this.sourceServer = sourceServer;
		this.targetServer = targetServer;
		this.entity = entity;
		this.x = x;
		this.y = y;
		this.z = z;
		this.grounded = grounded;
	}

	@Override
	public void placeInPortal(@NotNull Entity entityIn, float rotationYaw) {
		int ix = MathHelper.floor(this.x);
		int iz = MathHelper.floor(this.z);

		if (grounded) {
			BlockPos top = this.world.getTopSolidOrLiquidBlock(new BlockPos(ix, 0, iz));
			this.y = top.getY() + 5;
		} else {
			int cx = ix >> 4;
			int cz = iz >> 4;
			this.world.getChunkProvider().provideChunk(cx, cz);
		}

		entityIn.setPosition(this.x, this.y, this.z);
	}

	private void runTeleport() {
		MinecraftServer mcServer = FMLCommonHandler.instance().getMinecraftServerInstance();
		PlayerList manager = mcServer.getPlayerList();

		// If this entity got teleported with a player rider, switch to the rider!
		// 1.12.2 supports multiple passengers, usually the driver is at index 0
		if (!entity.getPassengers().isEmpty() && entity.getPassengers().getFirst() instanceof EntityPlayerMP) {
			entity = entity.getPassengers().getFirst();
		}

		// Update position before transfer
		entity.setPosition(x, entity.posY, z);

		if (entity instanceof EntityPlayerMP playerMP) {
            Entity ridingEntity = playerMP.getRidingEntity();

			// Transfer player to the new dimension
			manager.transferPlayerToDimension(playerMP, targetServer.provider.getDimension(), this);

			if (ridingEntity != null && !ridingEntity.isDead) {
				// In 1.12.2, changeDimension handles the cloning, spawning, and removal of the old entity
				Entity newEntity = ridingEntity.changeDimension(targetServer.provider.getDimension(), this);

				if (newEntity != null) {
					newEntity.setPosition(x, newEntity.posY, z);

					// Ensure rocket stickiness
					if (newEntity instanceof EntityRideableRocket) {
						((EntityRideableRocket) newEntity).setThrower(playerMP);
					}

					// Send another packet to the client to make sure they load in correctly!
					// Using 900 Y as per original 1.7.10 code logic
					playerMP.connection.setPlayerLocation(x, 900, z, playerMP.rotationYaw, playerMP.rotationPitch);

					// Force remount
					playerMP.startRiding(newEntity, true);
				}
			}
		} else {
			// Transfer non-player entity
			Entity newEntity = entity.changeDimension(targetServer.provider.getDimension(), this);
			if (newEntity != null) {
				newEntity.setPosition(x, newEntity.posY, z);
			}
		}
	}

	public static void runQueuedTeleport() {
		CelestialTeleporter teleporter = queue.poll();
		if (teleporter != null) teleporter.runTeleport();
	}

	private static final Queue<CelestialTeleporter> queue = new ArrayDeque<>();

	public static void teleport(Entity entity, int dim, double x, double y, double z, boolean grounded) {
		if (entity.dimension == dim) return; // ignore if we're teleporting to the same place

		MinecraftServer mcServer = FMLCommonHandler.instance().getMinecraftServerInstance();
		Side sidex = FMLCommonHandler.instance().getEffectiveSide();
		if (sidex == Side.SERVER) {
			WorldServer sourceServer = mcServer.getWorld(entity.dimension);
			WorldServer targetServer = mcServer.getWorld(dim);

			queue.add(new CelestialTeleporter(sourceServer, targetServer, entity, x, y, z, grounded));
		}
	}

}
