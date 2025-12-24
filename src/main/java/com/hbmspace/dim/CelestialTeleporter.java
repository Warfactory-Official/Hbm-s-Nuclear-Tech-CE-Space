package com.hbmspace.dim;

import com.hbmspace.entity.missile.EntityRideableRocket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import org.jetbrains.annotations.NotNull;

public class CelestialTeleporter extends Teleporter {

	private final WorldServer sourceServer;
	private final WorldServer targetServer;

	private double x;
	private double y;
	private double z;

	private boolean grounded;

	private EntityPlayerMP playerMP;

	public CelestialTeleporter(WorldServer sourceServer, WorldServer targetServer, EntityPlayerMP playerMP, double x, double y, double z, boolean grounded) {
		super(targetServer);
		this.sourceServer = sourceServer;
		this.targetServer = targetServer;
		this.playerMP = playerMP;
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
		PlayerList manager = this.playerMP.server.getPlayerList();

		Entity ridingEntity = this.playerMP.getRidingEntity();

		this.playerMP.posX = this.x;
		this.playerMP.posZ = this.z;

		manager.transferPlayerToDimension(this.playerMP, this.targetServer.provider.getDimension(), this);

		if (ridingEntity != null && !ridingEntity.isDead) {
			NBTTagCompound nbt = new NBTTagCompound();
			ridingEntity.writeToNBTOptional(nbt);

			ridingEntity.setDead();

			Entity newEntity = EntityList.createEntityFromNBT(nbt, this.targetServer);
			if (newEntity != null) {
				newEntity.setPosition(this.x, newEntity.posY, this.z);
				this.targetServer.spawnEntity(newEntity);
				this.sourceServer.resetUpdateEntityTick();
				this.targetServer.resetUpdateEntityTick();
				this.playerMP.startRiding(newEntity, true);
                if(newEntity instanceof EntityRideableRocket) {
                    ((EntityRideableRocket) newEntity).setThrower(playerMP);
                }
				this.playerMP.setPositionAndUpdate(this.x, 900, this.z);
			}
		}
	}

	public static void runQueuedTeleport() {
		if (queuedTeleport == null) return;

		queuedTeleport.runTeleport();

		queuedTeleport = null;
	}

	private static CelestialTeleporter queuedTeleport;

	public static void teleport(EntityPlayer player, int dim, double x, double y, double z, boolean grounded) {
		if (player.dimension == dim) return;

		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		if (side == Side.SERVER && server != null) {
			if (player instanceof EntityPlayerMP playerMP) {
				WorldServer sourceServer = playerMP.getServerWorld();
				WorldServer targetServer = server.getWorld(dim);

				queuedTeleport = new CelestialTeleporter(sourceServer, targetServer, playerMP, x, y, z, grounded);
			}
		}
	}

}
