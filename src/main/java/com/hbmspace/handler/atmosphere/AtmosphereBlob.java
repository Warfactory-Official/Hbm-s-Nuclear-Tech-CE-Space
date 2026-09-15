package com.hbmspace.handler.atmosphere;

import com.hbm.blocks.BlockDummyable;
import com.hbm.blocks.ModBlocks;
import com.hbm.config.GeneralConfig;
import com.hbm.interfaces.IDoor;
import com.hbmspace.dim.trait.CBT_Atmosphere;
import com.hbm.handler.ThreeInts;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.lib.ForgeDirection;
import com.hbm.main.MainRegistry;
import com.hbm.tileentity.machine.TileEntityDummy;
import com.hbm.util.AdjacencyGraph;
import com.hbmspace.entity.effect.EntityDepress;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.BlockFence;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class AtmosphereBlob implements Runnable {
	
	/**
	 * Somewhat based on the Advanced-Rocketry implementation, but extended to
	 * define the gases and gas pressure inside the enclosed volume
	 */

	// Graph containing the enclosed area
	protected final AdjacencyGraph<ThreeInts> graph;

	// Handler, provides atmosphere information and receives callbacks
	protected IAtmosphereProvider handler;


	private static final ThreadPoolExecutor pool = new ThreadPoolExecutor(2, 16, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>(32));
	
	private final AtomicBoolean executing = new AtomicBoolean();
	private volatile ThreeInts blockPos;
	private volatile ThreeInts pendingPos;

    // If true, run depressurization effects on blobbing failure
    public volatile boolean runDepress;
    public volatile ForgeDirection depressDir = ForgeDirection.UP;

    private final LinkedHashMap<ThreeInts, Integer> plants = new LinkedHashMap<>();


	public AtmosphereBlob(IAtmosphereProvider handler) {
		this.handler = handler;
		graph = new AdjacencyGraph<>();
	}
	
	public boolean isPositionAllowed(World world, ThreeInts pos) {
		return !isBlockSealed(world, pos);
	}

	public static boolean isBlockSealed(World world, ThreeInts pos) {
		return isBlockSealed(world, pos.x, pos.y, pos.z);
	}

	public static boolean isBlockSealed(World world, int x, int y, int z) {
		if(y < 0 || y > 256) return false;

		// Prevent loading new chunks, or we violate thread safety!
		if(world instanceof WorldServer && !((WorldServer) world).getChunkProvider().chunkExists(x >> 4, z >> 4))
			return true;

		BlockPos pos = new BlockPos(x, y, z);
		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		if(block.isAir(state, world, pos)) return false;

		if (block instanceof BlockFarmland || block instanceof BlockFence) {
			return false;
		}
		if (block instanceof IBlockSealable) {
			return ((IBlockSealable) block).isSealed(world, x, y, z);
		}
		// Th3_Sl1ze: considering I'm not keen on doing mixins to make BlockGenericDoor IBlockSealable..
		if (block instanceof BlockDummyable dummyable) {
			TileEntity core = dummyable.findCoreTE(world, x, y, z);
			if (core instanceof IDoor door) {
				return door.getState() == IDoor.DoorState.CLOSED;
			}
			return block == ModBlocks.sliding_seal_door;
		}
		if (block.hasTileEntity(state)) {
			IDoor door = getDoor(world, pos);
			if (door != null) return door.getState() == IDoor.DoorState.CLOSED;
		}

		if(state.isFullCube() || state.isOpaqueCube()) return true;

		Material material = state.getMaterial();
		if(material.isLiquid() || !material.isSolid()) return false;
		if(material == Material.LEAVES) return false;

		AxisAlignedBB bb = null;
		try {
			bb = state.getCollisionBoundingBox(world, pos);
		} catch(Exception ignored) {}

		if(bb == null) {
			return false;
		}

		double eps = 0.001;

        return (bb.maxX - bb.minX > 1.0 - eps) &&
                (bb.maxY - bb.minY > 1.0 - eps) &&
                (bb.maxZ - bb.minZ > 1.0 - eps);
	}
	
	public static IDoor getDoor(World world, BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof IDoor door) return door;
		if (te instanceof TileEntityDummy dummy && dummy.target != null && world.getTileEntity(dummy.target) instanceof IDoor door) return door;
		return null;
	}

	public int getBlobMaxRadius() {
		return handler.getMaxBlobRadius();
	}

	public boolean hasFluid(FluidType fluid) {
		return hasFluid(fluid, 0.001);
	}

	public boolean hasFluid(FluidType fluid, double abovePressure) {
		if(handler.getFluidType() != fluid) return false;
		return handler.getFluidPressure() >= abovePressure;
	}

	public void consume(int amount) {
		handler.consume(amount);
	}

    public void produce(int amount) {
        handler.produce(amount);
    }

	/**
	 * Adds a block position to the blob
	 */
	public void addBlock(int x, int y , int z) {
		addBlock(new ThreeInts(x, y, z));
	}
	
	/**
	 * Recursively checks for contiguous blocks and adds them to the graph
	 */
	public void addBlock(ThreeInts blockPos) {
		synchronized(graph) {
			if(graph.contains(blockPos)) return;
			if(graph.size() != 0 && !hasNeighbourInGraph(blockPos)) return;
		}

		if(!executing.compareAndSet(false, true)) {
			pendingPos = blockPos;
			return;
		}

		this.blockPos = blockPos;

		if(GeneralConfig.enableThreadedAtmospheres) {
			try {
				pool.execute(this);
			} catch (RejectedExecutionException e) {
				MainRegistry.logger.warn("Atmosphere calculation at {} aborted due to oversize queue!", this.getRootPosition());
				executing.set(false);
			}
		} else {
			this.run();
		}
	}

	private boolean hasNeighbourInGraph(ThreeInts pos) {
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			if(graph.contains(pos.getPositionAtOffset(dir))) return true;
		}
		return false;
	}

	private void addSingleBlock(ThreeInts blockPos) {
		if(!graph.contains(blockPos)) {
			graph.add(blockPos, getPositionsToAdd(blockPos));
		}
	}
	
	/**
	 * @return the BlockPosition of the root of the blob
	 */
	public ThreeInts getRootPosition() {
		return handler.getRootPosition();
	}
	
	/**
	 * Gets adjacent blocks if they exist in the blob
	 * @param blockPos block to find things adjacent to
	 * @return list containing valid adjacent blocks
	 */
	protected HashSet<ThreeInts> getPositionsToAdd(ThreeInts blockPos) {
		HashSet<ThreeInts> set = new HashSet<>();
		
		for(ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
			
			ThreeInts offset = blockPos.getPositionAtOffset(direction);
			if(graph.contains(offset))
				set.add(offset);
		}
		
		return set;
	}

	/**
	 * Given a block position returns whether or not it exists in the graph
	 * @return true if the block exists in the blob
	 */
	public boolean contains(ThreeInts position) {
		boolean contains;
		
		synchronized (graph) {
			contains = graph.contains(position);
		}

		return contains;
	}
	
	/**
	 * Given a block position returns whether or not it exists in the graph
	 * @param x
	 * @param y
	 * @param z
	 * @return true if the block exists in the blob
	 */
	public boolean contains(int x, int y, int z) {
		return contains(new ThreeInts(x, y, z));
	}

	/**
	 * Removes the block at the given coords for this blob
	 * @param blockPos
	 */
	public void removeBlock(ThreeInts blockPos) {
		synchronized (graph) {
			graph.remove(blockPos);

			for(ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {

				ThreeInts newBlock = blockPos.getPositionAtOffset(direction);
				if(graph.contains(newBlock) && !graph.doesPathExist(newBlock, handler.getRootPosition()))
					runEffectOnWorldBlocks(handler.getAtmoWorld(), graph.removeAllNodesConnectedTo(newBlock));
			}
		}
	}
	
	public void removeBlocks(Collection<ThreeInts> positions) {
		synchronized (graph) {
			boolean removed = false;
			for(ThreeInts pos : positions) {
				if(graph.contains(pos)) {
					graph.remove(pos);
					removed = true;
				}
			}

			if(!removed || graph.size() == 0) return;

			ThreeInts root = handler.getRootPosition();
			if(!graph.contains(root)) {
				runEffectOnWorldBlocks(handler.getAtmoWorld(), new ArrayList<>(graph.getKeys()));
				graph.clear();
				return;
			}

			Set<ThreeInts> reachable = graph.getAllNodesConnectedToNode(root);
			if(reachable.size() == graph.size()) return;

			List<ThreeInts> detached = new ArrayList<>();
			for(ThreeInts pos : new ArrayList<>(graph.getKeys())) {
				if(!reachable.contains(pos) && graph.contains(pos)) {
					detached.addAll(graph.removeAllNodesConnectedTo(pos));
				}
			}

			runEffectOnWorldBlocks(handler.getAtmoWorld(), detached);
		}
	}

	/**
	 * Removes all nodes from the blob
	 */
	public void clearBlob() {
		World world = handler.getAtmoWorld();
		List<ThreeInts> locations;

		synchronized(graph) {
			locations = new ArrayList<>(graph.getKeys());
			graph.clear();
		}

		if(!locations.isEmpty()) runOnWorldThread(world, () -> runEffectOnWorldBlocks(world, locations));
	}
	
	/**
	 * @return a set containing all locations
	 */
	public Set<ThreeInts> getLocations() {
		return graph.getKeys();
	}
	
	/**
	 * @return the number of elements in the blob
	 */
	public int getBlobSize() {
		return graph.size();
	}

	@Override
	public void run() {
		final ThreeInts start = blockPos;
		final boolean depress = runDepress;
		final ForgeDirection dir = depressDir;
		final World world = handler.getAtmoWorld();
		final ThreeInts root = getRootPosition();
		final int maxSize = getBlobMaxRadius();
		final long maxDistSq = (long) maxSize * maxSize;

		final LongOpenHashSet addableBlocks = new LongOpenHashSet();
		final LongArrayList[] buckets = new LongArrayList[maxSize + 1];
		final SealCache cache = new SealCache(world);

		boolean success = true;

		try {
			long startKey = pack(start.x, start.y, start.z);
			addableBlocks.add(startKey);
			int top = Math.min((int) Math.sqrt(distanceSquared(start.x, start.y, start.z, root)), maxSize);
			pushToBucket(buckets, top, startKey);

			search:
			while(top >= 0) {
				LongArrayList bucket = buckets[top];
				if(bucket == null || bucket.isEmpty()) {
					top--;
					continue;
				}

				long current = bucket.removeLong(bucket.size() - 1);
				int cx = unpackX(current);
				int cy = unpackY(current);
				int cz = unpackZ(current);

				for(ForgeDirection offset : ForgeDirection.VALID_DIRECTIONS) {
					int nx = cx + offset.offsetX;
					int ny = cy + offset.offsetY;
					int nz = cz + offset.offsetZ;
					long key = pack(nx, ny, nz);

					if(addableBlocks.contains(key) || contains(nx, ny, nz) || !cache.isAllowed(nx, ny, nz)) continue;

					long distSq = distanceSquared(nx, ny, nz, root);
					if(distSq > maxDistSq) {
						MainRegistry.logger.info("Atmosphere leak at: {}, {}, {}", nx, ny, nz);
						if(depress) decompress(start, dir);
						success = false;
						break search;
					}

					addableBlocks.add(key);
					int index = (int) Math.sqrt(distSq);
					pushToBucket(buckets, index, key);
					if(index > top) top = index;
				}
			}
		} catch (Throwable e) {
			MainRegistry.logger.error("Critical error in AtmosphereBlob thread", e);
			success = false;
		}

		if(success) {
			synchronized(graph) {
				LongIterator iterator = addableBlocks.iterator();
				while(iterator.hasNext()) {
					long key = iterator.nextLong();
					addSingleBlock(new ThreeInts(unpackX(key), unpackY(key), unpackZ(key)));
				}
				handler.onBlobCreated(this);
			}
		} else {
			clearBlob();
		}

		executing.set(false);

		ThreeInts pending = pendingPos;
		pendingPos = null;
		if(pending != null && (success || pending.equals(getRootPosition()))) addBlock(pending);
	}

	private static void pushToBucket(LongArrayList[] buckets, int index, long key) {
		LongArrayList bucket = buckets[index];
		if(bucket == null) {
			bucket = new LongArrayList();
			buckets[index] = bucket;
		}
		bucket.add(key);
	}

	private static long distanceSquared(int x, int y, int z, ThreeInts root) {
		long dx = x - root.x;
		long dy = y - root.y;
		long dz = z - root.z;
		return dx * dx + dy * dy + dz * dz;
	}

	private static long pack(int x, int y, int z) {
		return ((long) x & 0x3FFFFFFL) << 38 | ((long) y & 0xFFFL) << 26 | ((long) z & 0x3FFFFFFL);
	}

	private static int unpackX(long key) {
		return (int) (key >> 38);
	}

	private static int unpackY(long key) {
		return (int) (key << 26 >> 52);
	}

	private static int unpackZ(long key) {
		return (int) (key << 38 >> 38);
	}

	private static void runOnWorldThread(World world, Runnable task) {
		if(world instanceof WorldServer server) {
			server.addScheduledTask(task);
		} else {
			task.run();
		}
	}

	private static final class SealCache {

		private final World world;
		private final IChunkProvider provider;
		private Chunk chunk;
		private int chunkX;
		private int chunkZ;

		private SealCache(World world) {
			this.world = world;
			this.provider = world.getChunkProvider();
		}

		private boolean isAllowed(int x, int y, int z) {
			if(y < 0 || y > 256) return true;

			int cx = x >> 4;
			int cz = z >> 4;
			if(chunk == null || cx != chunkX || cz != chunkZ) {
				chunk = provider.getLoadedChunk(cx, cz);
				chunkX = cx;
				chunkZ = cz;
			}

			if(chunk == null) return false;
			if(chunk.getBlockState(x, y, z).getBlock() == Blocks.AIR) return true;

			return !isBlockSealed(world, x, y, z);
		}
	}


	/**
	 * @param world
	 * @param blocks Collection containing affected locations
	 */
	protected void runEffectOnWorldBlocks(World world, Collection<ThreeInts> blocks) {
		ThreeInts root = handler.getRootPosition();
		CBT_Atmosphere newAtmosphere = ChunkAtmosphereManager.proxy.getAtmosphere(world, root.x, root.y, root.z, this);

		for(ThreeInts pos : blocks) {
			final Block block = world.getBlockState(new BlockPos(pos.x, pos.y, pos.z)).getBlock();
			ChunkAtmosphereManager.proxy.runEffectsOnBlock(newAtmosphere, world, block, pos.x, pos.y, pos.z);
		}
	}

    public void decompress(ThreeInts pos, ForgeDirection dir) {
        World world = handler.getAtmoWorld();

        runOnWorldThread(world, () -> {
            EntityDepress depress = new EntityDepress(world, dir.getOpposite().toEnumFacing(), 20);
            depress.setPosition(pos.x + 0.5, pos.y + 0.5, pos.z + 0.5);
            world.spawnEntity(depress);

            world.playSound(null, depress.posX, depress.posY, depress.posZ, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.NEUTRAL, 1.0F, 1.6F);
            world.playSound(null, depress.posX, depress.posY, depress.posZ, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.NEUTRAL, 1.0F, 0.25F);
        });
    }

    public void checkGrowth() {
        World world = handler.getAtmoWorld();

        Iterator<HashMap.Entry<ThreeInts, Integer>> iterator = plants.entrySet().iterator();
        while(iterator.hasNext()) {
            HashMap.Entry<ThreeInts, Integer> entry = iterator.next();
            ThreeInts pos = entry.getKey();
            int oldMeta = entry.getValue();
            IBlockState state = world.getBlockState(new BlockPos(pos.x, pos.y, pos.z));
            Block block = state.getBlock();

            if(!(block instanceof IGrowable)) {
                iterator.remove();
                continue;
            }

            int newMeta = state.getBlock().getMetaFromState(state);

            if(newMeta != oldMeta) {
                entry.setValue(newMeta);
                produce(Math.max(newMeta - oldMeta, 0) * ChunkAtmosphereHandler.CROP_GROWTH_CONVERSION);
            }
        }
    }

    public void addPlant(World world, int x, int y, int z) {
        IBlockState state = world.getBlockState(new BlockPos(x, y, z));
        plants.put(new ThreeInts(x, y, z), state.getBlock().getMetaFromState(state));
    }

}
