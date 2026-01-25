package com.hbmspace.handler.atmosphere;

import com.hbm.blocks.BlockDummyable;
import com.hbm.blocks.ModBlocks;
import com.hbm.config.GeneralConfig;
import com.hbmspace.dim.trait.CBT_Atmosphere;
import com.hbm.handler.ThreeInts;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.lib.ForgeDirection;
import com.hbm.main.MainRegistry;
import com.hbm.util.AdjacencyGraph;
import com.hbmspace.entity.effect.EntityDepress;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.BlockFence;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.*;
import java.util.concurrent.*;

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
	
	private boolean executing;
	private ThreeInts blockPos;

    // If true, run depressurization effects on blobbing failure
    public boolean runDepress;
    public ForgeDirection depressDir = ForgeDirection.UP;

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
		if(block == ModBlocks.sliding_seal_door) return true; // fuck it, I'll put a temporary bandaid

		if(state.isFullCube() || state.isOpaqueCube()) return true;

		if (block instanceof BlockFarmland || block instanceof BlockFence || block instanceof BlockDummyable) {
			return false;
		}
		if (block instanceof IBlockSealable) {
			return ((IBlockSealable) block).isSealed(world, x, y, z);
		}

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
		boolean alreadyContains;
		synchronized(graph) {
			alreadyContains = this.contains(blockPos);
		}
		if(!alreadyContains &&
				(this.graph.size() == 0 || this.contains(blockPos.getPositionAtOffset(ForgeDirection.UP)) || this.contains(blockPos.getPositionAtOffset(ForgeDirection.DOWN)) ||
						this.contains(blockPos.getPositionAtOffset(ForgeDirection.EAST)) || this.contains(blockPos.getPositionAtOffset(ForgeDirection.WEST)) ||
						this.contains(blockPos.getPositionAtOffset(ForgeDirection.NORTH)) || this.contains(blockPos.getPositionAtOffset(ForgeDirection.SOUTH)))) {
			if(!executing) {
				this.blockPos = blockPos;
				executing = true;
				
				if(GeneralConfig.enableThreadedAtmospheres) {
					try {
						pool.execute(this);
					} catch (RejectedExecutionException e) {
						MainRegistry.logger.warn("Atmosphere calculation at {} aborted due to oversize queue!", this.getRootPosition());
						executing = false;
					}
				} else {
					this.run();
				}
			}
		}
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
	
	/**
	 * Removes all nodes from the blob
	 */
	public void clearBlob() {
		World world = handler.getAtmoWorld();

		runEffectOnWorldBlocks(world, getLocations());
		
		graph.clear();
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
		Stack<ThreeInts> stack = new Stack<>();
		stack.push(blockPos);

		final int maxSize = this.getBlobMaxRadius();
		final HashSet<ThreeInts> addableBlocks = new HashSet<>();

		boolean success = true;

		try {
			// Breadth first search; non recursive
			while(!stack.isEmpty()) {
				ThreeInts stackElement = stack.pop();
				addableBlocks.add(stackElement);

				for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
					ThreeInts searchNextPosition = stackElement.getPositionAtOffset(dir);

					boolean alreadyInGraph;
					synchronized (graph) {
						alreadyInGraph = graph.contains(searchNextPosition);
					}

					// Don't path areas we have already scanned
					if(!alreadyInGraph && !addableBlocks.contains(searchNextPosition)) {

						if(isPositionAllowed(handler.getAtmoWorld(), searchNextPosition)) {
							if(searchNextPosition.getDistanceSquared(this.getRootPosition()) <= maxSize * maxSize) {
								stack.push(searchNextPosition);
								addableBlocks.add(searchNextPosition);
							} else {
                                MainRegistry.logger.info("Atmosphere leak at: {}", searchNextPosition);
								if(runDepress) decompress(blockPos, depressDir);
								success = false;
								break;
							}
						}
					}
				}

				if(!success) break;
			}
		} catch (Throwable e) {
			MainRegistry.logger.error("Critical error in AtmosphereBlob thread", e);
			success = false;
		}

		if (success) {
			synchronized (graph) {
				for(ThreeInts addableBlock : addableBlocks) {
					addSingleBlock(addableBlock);
				}
				handler.onBlobCreated(this);
			}
		} else {
			clearBlob();
		}

		executing = false;
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

        EntityDepress depress = new EntityDepress(world, dir.getOpposite().toEnumFacing(), 20);
        depress.posX = pos.x + 0.5;
        depress.posY = pos.y + 0.5;
        depress.posZ = pos.z + 0.5;
        world.spawnEntity(depress);

        world.playSound(null, depress.posX, depress.posY, depress.posZ, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.NEUTRAL, 1.0F, 1.6F);
        world.playSound(null, depress.posX, depress.posY, depress.posZ, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.NEUTRAL, 1.0F, 0.25F);
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
