package com.hbmspace.blocks.machine;

import com.hbm.tileentity.TileEntityProxyCombo;
import com.hbmspace.blocks.BlockDummyableSpace;
import com.hbmspace.tileentity.machine.TileEntityMachineSolarPanel;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class MachineSolar extends BlockDummyableSpace {

    public MachineSolar(Material p_i45394_1_, String s) {
        super(p_i45394_1_, s);
    }

    @Override
    public TileEntity createNewTileEntity(@NotNull World world, int meta) {
        if(meta >= 12)
            return new TileEntityMachineSolarPanel();
        if(meta >= extra)
            return new TileEntityProxyCombo(false, false, true);

        return null;
    }

    @Override
    public int[] getDimensions() {
        return new int[] {0, 0, 2, 2, 0, 0};
    }

    @Override
    public int getOffset() {
        return 0;
    }

}
