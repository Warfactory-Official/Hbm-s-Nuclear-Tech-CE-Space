package com.hbmspace.dim.orbit;

import com.hbmspace.api.tile.IPropulsion;
import com.hbm.blocks.BlockDummyable;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.blocks.machine.BlockOrbitalStation;
import com.hbmspace.dim.CelestialBody;
import com.hbmspace.dim.SolarSystem;
import com.hbmspace.dim.SolarSystemWorldSavedData;
import com.hbm.handler.ThreeInts;
import com.hbm.lib.ForgeDirection;
import com.hbmspace.entity.missile.EntityRideableRocket;
import com.hbmspace.items.ItemVOTVdrive;
import com.hbmspace.tileentity.machine.TileEntityOrbitalStation;
import com.hbm.util.BufferUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.*;

public class OrbitalStation {

	public String name = ""; // I dub thee

	public CelestialBody orbiting;
	public CelestialBody target;

	public StationState state = StationState.ORBIT;
	public int stateTimer;
	public int maxStateTimer = 100;

	public boolean hasStation = false;

	// the coordinates of the station within the dimension
	public int dX;
	public int dZ;

	public boolean hasEngines = true;
	public List<ThreeInts> errorsAt = new ArrayList<>();
	public int errorTimer;

	public float gravityMultiplier = 1;

	public enum StationState {
		ORBIT, // big chillin
		LEAVING, // prepare engines for transfer
		TRANSFER, // going from A to B
		ARRIVING, // spool down engines
	}

	private TileEntityOrbitalStation mainPort;
	private final HashMap<ThreeInts, TileEntityOrbitalStation> ports = new HashMap<>();
	private int portIndex = 0;

	private final HashSet<IPropulsion> engines = new HashSet<>();

	public static OrbitalStation clientStation = new OrbitalStation(CelestialBody.getBody(0));
	public static List<OrbitalStation> orbitingStations = new ArrayList<>();

	public static final int STATION_SIZE = 1024; // total area for each station
	public static final int BUFFER_SIZE = 256; // size of the buffer region that drops you out of orbit (preventing seeing other stations)
	public static final int WARNING_SIZE = 32; // size of the region that warns the player about falling out of orbit


	
	/**
	 * Space station spatial space
	 * - Stations are spread out over the orbital dimension, in restricted areas
	 * - Attempting to leave the region your station is in will cause you to fall back to the orbited planet
	 * - The current station you are near is fetched using the player's XZ coordinate
	 * - The station will determine what body is being orbited, and therefore what to show in the skybox
	 */

	// For client
	public OrbitalStation(CelestialBody orbiting) {
		this.orbiting = orbiting;
		this.target = orbiting;
	}

	// For server
	public OrbitalStation(CelestialBody orbiting, int x, int z) {
		this(orbiting);
		this.dX = x;
		this.dZ = z;
	}

	public void travelTo(CelestialBody target) {
		if(state != StationState.ORBIT) return; // only when at rest can we start a new journey
		if(!canTravel(orbiting, target)) return;

		setState(StationState.LEAVING, getLeaveTime());
		this.target = target;
	}

	public void update(World world) {
		if(!world.isRemote) {
			if(state == StationState.LEAVING) {
				if(stateTimer > maxStateTimer) {
					setState(StationState.TRANSFER, getTransferTime());
				}
			} else if(state == StationState.TRANSFER) {
				if(stateTimer > maxStateTimer) {
					setState(StationState.ARRIVING, getArriveTime());
					orbiting = target;
				}
			} else if(state == StationState.ARRIVING) {
				if(stateTimer > maxStateTimer) {
					setState(StationState.ORBIT, 0);
				}
			}

			SolarSystemWorldSavedData.get().markDirty();

			hasEngines = !engines.isEmpty();

			errorTimer--;
			if(errorTimer <= 0) {
				errorsAt = new ArrayList<>();
				errorTimer = 0;
			}
		}

		stateTimer++;
	}

	private boolean canTravel(CelestialBody from, CelestialBody to) {
		if(engines.isEmpty()) return false;

		double deltaV = SolarSystem.getDeltaVBetween(from, to);
		int shipMass = 200_000; // Always static, to not punish building big cool stations
		float totalThrust = getTotalThrust();

		boolean canTravel = true;
		errorsAt = new ArrayList<>();

		for(IPropulsion engine : engines) {
			float massPortion = engine.getThrust() / totalThrust;
			if(!engine.canPerformBurn(Math.round(shipMass * massPortion), deltaV)) {
				TileEntity te = engine.getTileEntity();
				canTravel = false;
				errorsAt.add(new ThreeInts(te.getPos().getX(), te.getPos().getY(), te.getPos().getZ()));
				errorTimer = 100;
			}
		}

		return canTravel;
	}

	private float getTotalThrust() {
		float thrust = 0;
		for(IPropulsion engine : engines) {
			thrust += engine.getThrust();
		}
		return thrust;
	}

	// Has the side effect of beginning engine burns
	private int getLeaveTime() {
		int leaveTime = 20;
		for(IPropulsion engine : engines) {
			int time = engine.startBurn();
			if(time > leaveTime) leaveTime = time;
		}
		return leaveTime;
	}

	// And this one will end engine burns
	private int getArriveTime() {
		int arriveTime = 20;
		for(IPropulsion engine : engines) {
			int time = engine.endBurn();
			if(time > arriveTime) arriveTime = time;
		}
		return arriveTime;
	}

	private int getTransferTime() {
		if(mainPort == null) return -1;

		int size = calculateSize();
		double distance = SolarSystem.calculateDistanceBetweenTwoBodies(mainPort.getWorld(), orbiting, target);
		float thrust = getTotalThrust();

        return calculateTransferTime(distance, size, thrust);
	}

    public static int calculateTransferTime(double distance, int size, float thrust) {
        return (int)(Math.log(1 + (distance * size / thrust * 100)) * 150);
    }

	public void setState(StationState state, int timeUntilNext) {
		this.state = state;
		stateTimer = 0;
		maxStateTimer = timeUntilNext;
	}

	public boolean recallPod(ItemVOTVdrive.Destination destination) {
		if(!hasStation) return false;
		if(destination.body.getBody() != orbiting) return false;

		for(TileEntityOrbitalStation port : ports.values()) {
			EntityRideableRocket rocket = port.getDocked();

			if(rocket == null || !rocket.isReusable()) continue;

			// ensure the rocket has fuel before sending it off
			EntityRideableRocket.RocketState state = rocket.getState();
			if(state != EntityRideableRocket.RocketState.AWAITING && state != EntityRideableRocket.RocketState.LANDED) continue;

			// and make sure it doesn't have a rider!!
			if(rocket.getRidingEntity() != null) continue;

			rocket.recallPod(destination);
			return true;
		}

		return false;
	}
	
	public static void addPropulsion(IPropulsion propulsion) {
		TileEntity te = propulsion.getTileEntity();
		OrbitalStation station = getStationFromPosition(te.getPos().getX(), te.getPos().getZ());
		station.engines.add(propulsion);
	}

	public static void removePropulsion(IPropulsion propulsion) {
		TileEntity te = propulsion.getTileEntity();
		OrbitalStation station = getStationFromPosition(te.getPos().getX(), te.getPos().getZ());
		station.engines.remove(propulsion);
	}

	public void addPort(TileEntityOrbitalStation port) {
		ports.put(new ThreeInts(port.getPos().getX(), port.getPos().getY(), port.getPos().getZ()), port);
		if(port.getBlockType() == ModBlocksSpace.orbital_station) mainPort = port;
	}

	public void removePort(TileEntityOrbitalStation port) {
		ports.remove(new ThreeInts(port.getPos().getX(), port.getPos().getY(), port.getPos().getZ()));
	}

	public TileEntityOrbitalStation getPort() {
		if(ports.isEmpty()) return null;

		// First, find any port that's available
		int index = 0;
		for(TileEntityOrbitalStation port : ports.values()) {
			if(!port.hasDocked && !port.isReserved) {
				portIndex = index;
				return port;
			}
			index++;
		}

		// Failing that, round robin on the occupied ports
		portIndex++;
		if(portIndex >= ports.size()) portIndex = 0;

		return ports.values().toArray(new TileEntityOrbitalStation[ports.size()])[portIndex];
	}

	public static TileEntityOrbitalStation getPort(int x, int z) {
		return getStationFromPosition(x, z).getPort();
	}

	// I can't stop pronouncing this as hors d'oeuvre
	private static final ForgeDirection[] horDir = new ForgeDirection[] { ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.WEST, ForgeDirection.EAST };

	// calculates the top down area of the station
	// super fucking fast but like, don't call it every frame
	public int calculateSize() {
		if(mainPort == null) return 0;
		World world = mainPort.getWorld();

		int minX, maxX;
		int minZ, maxZ;
		minX = maxX = mainPort.getPos().getX();
		minZ = maxZ = mainPort.getPos().getZ();

		Stack<ThreeInts> stack = new Stack<>();
		stack.push(new ThreeInts(mainPort.getPos().getX(), mainPort.getPos().getY(), mainPort.getPos().getZ()));

		HashSet<ThreeInts> visited = new HashSet<>();

		while(!stack.isEmpty()) {
			ThreeInts pos = stack.pop();
			visited.add(pos);

			if(pos.x < minX) minX = pos.x;
			if(pos.x > maxX) maxX = pos.x;
			if(pos.z < minZ) minZ = pos.z;
			if(pos.z > maxZ) maxZ = pos.z;

			for(ForgeDirection dir : horDir) {
				ThreeInts nextPos = pos.getPositionAtOffset(dir);

				if(!visited.contains(nextPos) && isInStation(world, nextPos)) {
					stack.push(nextPos);
				}
			}
		}

		return (maxX - minX + 1) * (maxZ - minZ + 1);
	}

	private boolean isInStation(World world, ThreeInts pos) {
		if(world.getHeight(pos.x, pos.z) > 1) return true;
		return Math.abs(pos.x - mainPort.getPos().getX()) < 5 && Math.abs(pos.z - mainPort.getPos().getZ()) < 5; // minimum station size
	}

	public double getUnscaledProgress(float partialTicks) {
		if(state == StationState.ORBIT) return 0;
		return MathHelper.clamp(((double) stateTimer + partialTicks) / (double) maxStateTimer, 0, 1);
	}

	public double getTransferProgress(float partialTicks) {
		if(state != StationState.TRANSFER) return 0;
		return easeInOutCirc(getUnscaledProgress(partialTicks));
	}

	private double easeInOutCirc(double t) {
		return t < 0.5
			? (1 - Math.sqrt(1 - Math.pow(2 * t, 3))) / 2
			: (Math.sqrt(1 - Math.pow(-2 * t + 2, 3)) + 1) / 2;
	}

	// Finds a space station for a given set of coordinates
	public static OrbitalStation getStationFromPosition(int x, int z) {
		SolarSystemWorldSavedData data = SolarSystemWorldSavedData.get();
		OrbitalStation station = data.getStationFromPosition(x, z);

		// Fallback for when a station doesn't exist (should only occur when using debug wand!)
		if(station == null) {
			station = data.addStation(MathHelper.floor((float)x / STATION_SIZE), MathHelper.floor((float)z / STATION_SIZE), CelestialBody.getBody(0));
		}

		return station;
	}

	public static OrbitalStation getStation(int x, int z) {
		return getStationFromPosition(x * STATION_SIZE, z * STATION_SIZE);
	}

	public void serialize(ByteBuf buf) {
		buf.writeInt(orbiting.dimensionId);
		buf.writeInt(target.dimensionId);
		buf.writeInt(state.ordinal());
		buf.writeInt(stateTimer);
		buf.writeInt(maxStateTimer);
		buf.writeBoolean(hasEngines);
		buf.writeFloat(gravityMultiplier);

		BufferUtil.writeString(buf, name);

		buf.writeInt(errorsAt.size());
		for(ThreeInts error : errorsAt) {
			buf.writeInt(error.x);
			buf.writeInt(error.y);
			buf.writeInt(error.z);
		}
	}

	public static OrbitalStation deserialize(ByteBuf buf) {
		OrbitalStation station = new OrbitalStation(CelestialBody.getBody(buf.readInt()));
		station.target = CelestialBody.getBody(buf.readInt());
		station.state = StationState.values()[buf.readInt()];
		station.stateTimer = buf.readInt();
		station.maxStateTimer = buf.readInt();
		station.hasEngines = buf.readBoolean();
		station.gravityMultiplier = buf.readFloat();

		station.name = BufferUtil.readString(buf);

		station.errorsAt = new ArrayList<>();
		int count = buf.readInt();
		for(int i = 0; i < count; i++) {
			int x = buf.readInt();
			int y = buf.readInt();
			int z = buf.readInt();
			
			station.errorsAt.add(new ThreeInts(x, y, z));
		}
		return station;
	}

	public static void spawn(World world, int x, int z) {
		int y = 127;
		if(world.getBlockState(new BlockPos(x, y, z)).getBlock() == ModBlocksSpace.orbital_station) return;

		BlockOrbitalStation block = (BlockOrbitalStation) ModBlocksSpace.orbital_station;

		BlockDummyable.safeRem = true;
		world.setBlockState(new BlockPos(x, y, z), block.getDefaultState().withProperty(BlockDummyable.META, 12), 3);
		block.fillSpace(world, x, y, z, ForgeDirection.NORTH, 0);
		BlockDummyable.safeRem = false;
	}

	// Mark the station as travelable
	public static void addStation(int x, int z, CelestialBody body) {
		SolarSystemWorldSavedData data = SolarSystemWorldSavedData.get();
		OrbitalStation station = data.getStationFromPosition(x * STATION_SIZE, z * STATION_SIZE);

		if(station == null) {
			station = data.addStation(x, z, body);
		}

		station.orbiting = station.target = body;
		station.hasStation = true;
	}

}
