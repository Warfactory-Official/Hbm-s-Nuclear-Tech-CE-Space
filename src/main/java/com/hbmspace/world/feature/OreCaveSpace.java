package com.hbmspace.world.feature;

import com.google.common.base.Predicate;
import com.hbm.inventory.RecipesCommon;
import com.hbm.world.WorldUtil;
import com.hbm.world.feature.OreCave;
import com.hbmspace.dim.WorldProviderCelestial;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStone;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Random;

public class OreCaveSpace {

    private NoiseGeneratorPerlin noise;
    private RecipesCommon.MetaBlock ore;
    /** The number that is being deducted flat from the result of the perlin noise before all other processing. Increase this to make strata rarer. */
    private double threshold = 2D;
    /** The mulitplier for the remaining bit after the threshold has been deducted. Increase to make strata wavier. */
    private int rangeMult = 3;
    /** The maximum range after multiplying - anything above this will be subtracted from (maxRange * 2) to yield the proper range. Increase this to make strata thicker. */
    private int maxRange = 4;
    /** The y-level around which the stratum is centered. */
    private int yLevel = 30;
    private Block fluid;
    int dim = 0;
    boolean allCelestials = false;
    public static Block override;
    boolean ignoreWater = false;
    boolean spawnStalagmites = true;

    public OreCaveSpace(Block ore) {
        this(ore, 0);
    }

    public OreCaveSpace(Block ore, int meta) {
        this.ore = new RecipesCommon.MetaBlock(ore, meta);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public OreCaveSpace setThreshold(double threshold) {
        this.threshold = threshold;
        return this;
    }

    public OreCaveSpace setRangeMult(int rangeMult) {
        this.rangeMult = rangeMult;
        return this;
    }

    public OreCaveSpace setMaxRange(int maxRange) {
        this.maxRange = maxRange;
        return this;
    }

    public OreCaveSpace setYLevel(int yLevel) {
        this.yLevel = yLevel;
        return this;
    }

    public OreCaveSpace withFluid(Block fluid) {
        this.fluid = fluid;
        return this;
    }

    public OreCaveSpace setDimension(int dim) {
        this.dim = dim;
        return this;
    }

    public OreCaveSpace setBlockOverride(Block override) {
        this.override = override;
        return this;
    }

    public OreCaveSpace setIgnoreWater(boolean ignoreWater) {
        this.ignoreWater = ignoreWater;
        return this;
    }

    public OreCaveSpace setStalagmites(boolean spawnStalagmites) {
        this.spawnStalagmites = spawnStalagmites;
        return this;
    }

    @SubscribeEvent
    public void onDecorate(DecorateBiomeEvent.Pre event) {

        World world = event.getWorld();

        if (world.provider == null) return;

        Block replace = Blocks.STONE;
        if(override != null) {
            replace = override;
        } else if(world.provider instanceof WorldProviderCelestial) {
            replace = ((WorldProviderCelestial)world.provider).getStone();
        }

        if(allCelestials) {
            if(!(world.provider instanceof WorldProviderCelestial) && world.provider.getDimension() != 0) return;
        } else {
            if(world.provider.getDimension() != this.dim) return;
        }

        if (this.noise == null) {
            this.noise = new NoiseGeneratorPerlin(new Random(world.getSeed() + (ore.getID() * 31) + yLevel), 2);
        }
        // Apparently getChunkPos doesn't work here at all..
        int cX = event.getPos().getX();
        int cZ = event.getPos().getZ();

        double scale = 0.01D;
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int x = cX + 8; x < cX + 24; x++) {
            for (int z = cZ + 8; z < cZ + 24; z++) {

                double n = noise.getValue(x * scale, z * scale);

                if (n > threshold) {
                    int range = (int) ((n - threshold) * rangeMult);

                    if (range > maxRange)
                        range = (maxRange * 2) - range;

                    if (range < 0)
                        continue;

                    for (int y = yLevel - range; y <= yLevel + range; y++) {
                        IBlockState genState = world.getBlockState(pos);
                        Block genBlock = genState.getBlock();

                        if (genBlock.isNormalCube(genState, world, pos)
                                && (genState.getMaterial() == Material.ROCK || genState.getMaterial() == Material.GROUND)
                                && genBlock.isReplaceableOreGen(genState, world, pos, PLANET_PREDICATE)) {

                            boolean shouldGen = false;
                            boolean canGenFluid = event.getRand().nextBoolean();

                            for (EnumFacing dir : EnumFacing.values()) {
                                BlockPos npos = pos.offset(dir);
                                IBlockState neighborState = world.getBlockState(npos);
                                Block neighborBlock = neighborState.getBlock();

                                if (neighborState.getMaterial() == Material.AIR) {
                                    // || neighborBlock instanceof BlockStalagmite) { // TODO: stalagmites
                                    shouldGen = true;
                                }

                                if (shouldGen && (fluid == null || !canGenFluid))
                                    break;

                                if (fluid != null) {
                                    switch (dir) {
                                        case UP:
                                            /*if (neighborState.getMaterial() != Material.AIR && !(neighborBlock instanceof BlockStalagmite))
                                                canGenFluid = false;*/
                                            break;
                                        case DOWN:
                                            if (!neighborBlock.isNormalCube(neighborState, world, npos))
                                                canGenFluid = false;
                                            break;
                                        case NORTH:
                                        case SOUTH:
                                        case EAST:
                                        case WEST:
                                            if (!neighborBlock.isNormalCube(neighborState, world, npos) && neighborBlock != fluid)
                                                canGenFluid = false;
                                            break;
                                    }
                                }
                            }

                            if (fluid != null && canGenFluid) {
                                world.setBlockState(pos, fluid.getDefaultState(), 2);
                                world.setBlockState(pos.down(), ore.block.getStateFromMeta(ore.meta), 2);

                                for (EnumFacing dir : EnumFacing.HORIZONTALS) {
                                    BlockPos clPos = pos.offset(dir);
                                    IBlockState neighborState = world.getBlockState(clPos);
                                    Block neighborBlock = neighborState.getBlock();

                                    if (neighborBlock.isNormalCube(neighborState, world, clPos))
                                        world.setBlockState(clPos, ore.block.getStateFromMeta(ore.meta), 2);
                                }

                            } else if (shouldGen) {
                                world.setBlockState(pos, ore.block.getStateFromMeta(ore.meta), 2);
                            }

                        }/* else {

                            if ((genState.getMaterial() == Material.AIR || !genBlock.isNormalCube(genState, world, pos))
                                    && event.getRand().nextInt(5) == 0
                                    && !genState.getMaterial().isLiquid()) {

                                if (ModBlocks.stalactite.canPlaceBlockAt(world, pos)) {
                                    world.setBlockState(pos, ModBlocks.stalactite.getStateFromMeta(BlockStalagmite.getMetaFromResource(ore.meta)), 2);
                                } else {
                                    if (ModBlocks.stalagmite.canPlaceBlockAt(world, pos)) {
                                        world.setBlockState(pos, ModBlocks.stalagmite.getStateFromMeta(BlockStalagmite.getMetaFromResource(ore.meta)), 2);
                                    }
                                }
                            }
                        }*/
                    }
                }
            }
        }
    }

    public static final Predicate<IBlockState> PLANET_PREDICATE = (state) -> state != null && state.getBlock() == override;
}
