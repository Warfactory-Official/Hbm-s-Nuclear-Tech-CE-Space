package com.hbmspace.dim.duna.biome;

import com.hbmspace.blocks.ModBlocksSpace;
import net.minecraft.block.BlockColored;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.NoiseGeneratorPerlin;

import java.util.Arrays;
import java.util.Random;

public class BiomeGenDunaHills extends BiomeGenBaseDuna {
    private IBlockState[] clayBands;
    private long worldSeed;
    private NoiseGeneratorPerlin pillarNoise;
    private NoiseGeneratorPerlin pillarRoofNoise;
    private NoiseGeneratorPerlin clayBandsOffsetNoise;
    private boolean brycePillars;
    private boolean hasForest;

    public BiomeGenDunaHills(BiomeProperties properties) {
        super(properties);
    }

    private static boolean isStainedClay(IBlockState state, EnumDyeColor color) {
        return state.getBlock() == Blocks.STAINED_HARDENED_CLAY && state.getValue(BlockColored.COLOR) == color;
    }

    @Override
    public void genTerrainBlocks(World world, Random rand, ChunkPrimer primer, int x, int z, double noise) {
        long seed = world.getSeed();
        if (this.clayBands == null || this.worldSeed != seed) {
            generateBands(seed);
        }
        if (this.pillarNoise == null || this.pillarRoofNoise == null || this.worldSeed != seed) {
            Random r = new Random(seed);
            this.pillarNoise = new NoiseGeneratorPerlin(r, 4);
            this.pillarRoofNoise = new NoiseGeneratorPerlin(r, 1);
        }
        this.worldSeed = seed;

        double pillarTopY = 0.0D;
        if (this.brycePillars) { // unreachable
            int i = (x & -16) + (z & 15);
            int j = (z & -16) + (x & 15);

            double d0 = Math.min(Math.abs(noise), this.pillarNoise.getValue(i * 0.25D, j * 0.25D));
            if (d0 > 0.0D) {
                double d2 = Math.abs(this.pillarRoofNoise.getValue(i * 0.001953125D, j * 0.001953125D));
                double d4 = d0 * d0 * 2.5D;
                double cap = Math.ceil(d2 * 50.0D) + 14.0D;
                if (d4 > cap) d4 = cap;
                pillarTopY = d4 + 64.0D;
            }
        }

        int localX = x & 15;
        int localZ = z & 15;
        int seaLevel = world.getSeaLevel();

        int surfaceDepth = (int) (noise / 3.0D + 3.0D + rand.nextDouble() * 0.25D);
        boolean cosineFlag = Math.cos(noise / 3.0D * Math.PI) > 0.0D;

        int remainingDepth = -1;
        boolean placedTop = false;
        IBlockState filler = this.fillerBlock;

        for (int y = 255; y >= 0; --y) {
            IBlockState state = primer.getBlockState(localZ, y, localX);
            if (state.getMaterial() == Material.AIR && y < (int) pillarTopY) {
                primer.setBlockState(localZ, y, localX, ModBlocksSpace.duna_rock.getDefaultState());
                state = ModBlocksSpace.duna_rock.getDefaultState();
            }
            if (y <= rand.nextInt(5)) {
                primer.setBlockState(localZ, y, localX, Blocks.BEDROCK.getDefaultState());
                continue;
            }

            if (state.getMaterial() == Material.AIR) {
                remainingDepth = -1;
                continue;
            }

            if (state.getBlock() != ModBlocksSpace.duna_rock) {
                continue;
            }

            if (remainingDepth == -1) {
                placedTop = false;
                if (surfaceDepth <= 0) {
                    filler = ModBlocksSpace.duna_rock.getDefaultState();
                } else if (y >= seaLevel - 4 && y <= seaLevel + 1) {
                    filler = this.fillerBlock;
                }

                remainingDepth = surfaceDepth + Math.max(0, y - seaLevel);

                if (y >= seaLevel - 1) {
                    if (this.hasForest && y > 86 + surfaceDepth * 2) {  // unreachable
                        primer.setBlockState(localZ, y, localX, ModBlocksSpace.duna_sands.getDefaultState());
                    } else if (y > seaLevel + 3 + surfaceDepth) {
                        // 1.7 behavior:
                        // - within 64..127: if cosineFlag == true -> rock; else -> red clay IFF band is "clay" (not rock)
                        // - outside 64..127: always red clay
                        boolean placeRedClay = (y < 64 || y > 127) || (!cosineFlag && isBandClay(x, y, z));
                        primer.setBlockState(localZ, y, localX, placeRedClay ? Blocks.STAINED_HARDENED_CLAY.getDefaultState()
                                                                                                           .withProperty(BlockColored.COLOR,
                                                                                                                   EnumDyeColor.RED) : ModBlocksSpace.duna_rock.getDefaultState());
                    } else {
                        primer.setBlockState(localZ, y, localX, this.topBlock);
                        placedTop = true;
                    }
                } else {
                    primer.setBlockState(localZ, y, localX, filler);
                }

            } else if (remainingDepth > 0) {
                --remainingDepth;

                if (placedTop) {
                    primer.setBlockState(localZ, y, localX, ModBlocksSpace.ferric_clay.getDefaultState());
                } else {
                    IBlockState band = getBand(x, y, z);

                    if (isStainedClay(band, EnumDyeColor.YELLOW)) {
                        // yellow is ugly
                        primer.setBlockState(localZ, y, localX, ModBlocksSpace.ferric_clay.getDefaultState());
                    } else if (isStainedClay(band, EnumDyeColor.WHITE)) {
                        // white is good for yer teeth
                        // primer.setBlockState(localZ, y, localX, ModBlocksSpace.stone_resource.getDefaultState());
                        primer.setBlockState(localZ, y, localX, ModBlocksSpace.duna_rock.getDefaultState());
                    } else if (band.getBlock() == Blocks.STAINED_HARDENED_CLAY) {
                        primer.setBlockState(localZ, y, localX, band);
                    } else {
                        primer.setBlockState(localZ, y, localX, ModBlocksSpace.duna_rock.getDefaultState());
                    }
                }
            }
        }
    }

    private boolean isBandClay(int x, int y, int z) {
        return getBand(x, y, z).getBlock() == Blocks.STAINED_HARDENED_CLAY;
    }

    public void generateBands(long seed) {
        this.clayBands = new IBlockState[64];
        Arrays.fill(this.clayBands, ModBlocksSpace.duna_rock.getDefaultState());

        Random random = new Random(seed);
        this.clayBandsOffsetNoise = new NoiseGeneratorPerlin(random, 1);

        for (int i = 0; i < 64; ++i) {
            i += random.nextInt(5) + 1;
            if (i < 64) this.clayBands[i] = Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.ORANGE);
        }

        int count = random.nextInt(4) + 2;
        for (int n = 0; n < count; ++n) {
            int len = random.nextInt(3) + 1;
            int start = random.nextInt(64);
            for (int j = 0; start + j < 64 && j < len; ++j) {
                this.clayBands[start + j] = Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.YELLOW);
            }
        }

        count = random.nextInt(4) + 2;
        for (int n = 0; n < count; ++n) {
            int len = random.nextInt(3) + 2;
            int start = random.nextInt(64);
            for (int j = 0; start + j < 64 && j < len; ++j) {
                this.clayBands[start + j] = Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.BROWN);
            }
        }

        count = random.nextInt(4) + 2;
        for (int n = 0; n < count; ++n) {
            int len = random.nextInt(3) + 1;
            int start = random.nextInt(64);
            for (int j = 0; start + j < 64 && j < len; ++j) {
                this.clayBands[start + j] = Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.RED);
            }
        }

        int stripes = random.nextInt(3) + 3;
        int cursor = 0;
        for (int n = 0; n < stripes; ++n) {
            cursor += random.nextInt(16) + 4;
            for (int j = 0; cursor + j < 64 && j < 1; ++j) {
                int idx = cursor + j;
                this.clayBands[idx] = Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.WHITE);

                if (idx > 1 && random.nextBoolean()) {
                    this.clayBands[idx - 1] = Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.SILVER);
                }
                if (idx < 63 && random.nextBoolean()) {
                    this.clayBands[idx + 1] = Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.SILVER);
                }
            }
        }
    }

    private IBlockState getBand(int x, int y, int z) {
        int offset = (int) Math.round(this.clayBandsOffsetNoise.getValue((double) x / 512.0D, (double) x / 512.0D) * 2.0D);
        return this.clayBands[(y + offset + 64) & 63];
    }
}
