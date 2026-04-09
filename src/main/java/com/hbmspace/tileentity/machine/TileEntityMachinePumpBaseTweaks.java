package com.hbmspace.tileentity.machine;

import com.hbm.blocks.ModBlocks;
import com.hbm.tileentity.machine.TileEntityMachinePumpBase;
import com.hbmspace.blocks.ModBlocksSpace;

public class TileEntityMachinePumpBaseTweaks {
    public static void addSpaceBlocks() {
        TileEntityMachinePumpBase.validBlocks.add(ModBlocksSpace.eve_silt);
        TileEntityMachinePumpBase.validBlocks.add(ModBlocksSpace.eve_rock);
        TileEntityMachinePumpBase.validBlocks.add(ModBlocksSpace.ike_regolith);
        TileEntityMachinePumpBase.validBlocks.add(ModBlocksSpace.ike_stone);
        TileEntityMachinePumpBase.validBlocks.add(ModBlocksSpace.duna_sands);
        TileEntityMachinePumpBase.validBlocks.add(ModBlocks.moon_turf);
        TileEntityMachinePumpBase.validBlocks.add(ModBlocksSpace.laythe_silt);
        TileEntityMachinePumpBase.validBlocks.add(ModBlocksSpace.moho_regolith);
        TileEntityMachinePumpBase.validBlocks.add(ModBlocksSpace.minmus_smooth);
        TileEntityMachinePumpBase.validBlocks.add(ModBlocksSpace.vinyl_sand);
        TileEntityMachinePumpBase.validBlocks.add(ModBlocksSpace.rubber_silt);
        TileEntityMachinePumpBase.validBlocks.add(ModBlocksSpace.rubber_grass);
    }
}
