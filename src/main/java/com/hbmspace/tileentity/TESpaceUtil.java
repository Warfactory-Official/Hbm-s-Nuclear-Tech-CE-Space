package com.hbmspace.tileentity;

import com.hbm.inventory.fluid.Fluids;
import com.hbm.saveddata.satellites.SatelliteSavedData;
import com.hbmspace.dim.CelestialBody;
import com.hbmspace.dim.orbit.OrbitalStation;
import com.hbmspace.dim.orbit.WorldProviderOrbit;
import com.hbmspace.dim.trait.CBT_Atmosphere;
import com.hbmspace.handler.atmosphere.AtmosphereBlob;
import com.hbmspace.handler.atmosphere.ChunkAtmosphereManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class TESpaceUtil {
    public static boolean breatheAir(World world, BlockPos pos, int amount) {
        CBT_Atmosphere atmosphere = world.provider instanceof WorldProviderOrbit ? null : CelestialBody.getTrait(world, CBT_Atmosphere.class);
        if (atmosphere != null) {
            if (atmosphere.hasFluid(com.hbmspace.inventory.fluid.Fluids.EARTHAIR, 0.19) || atmosphere.hasFluid(Fluids.OXYGEN, 0.09)) {
                return true;
            }
        }

        List<AtmosphereBlob> blobs = ChunkAtmosphereManager.proxy.getBlobs(world, pos.getX(), pos.getY(), pos.getZ());
        for (AtmosphereBlob blob : blobs) {
            if (blob.hasFluid(com.hbmspace.inventory.fluid.Fluids.EARTHAIR, 0.19) || blob.hasFluid(Fluids.OXYGEN, 0.09)) {
                blob.consume(amount);
                return true;
            }
        }

        return false;
    }

    public static boolean mODE(ItemStack stack, String name) {
        if (stack.isEmpty() || name == null || name.isEmpty()) return false;

        int[] ids = OreDictionary.getOreIDs(stack);

        for (int i = 0; i < ids.length; i++) {
            if (name.equals(OreDictionary.getOreName(ids[i]))) {
                return true;
            }
        }
        return false;
    }

    public static SatelliteSavedData getData(World worldObj, int x, int z) {
        if(!worldObj.isRemote && CelestialBody.inOrbit(worldObj)) {
            int targetDimensionId = OrbitalStation.getStationFromPosition(x, z).orbiting.dimensionId;

            World orbitingWorld = DimensionManager.getWorld(targetDimensionId);
            if(orbitingWorld == null) {
                DimensionManager.initDimension(targetDimensionId);
                orbitingWorld = DimensionManager.getWorld(targetDimensionId);
            }

            if(orbitingWorld != null) {
                worldObj = orbitingWorld;
            }
        }

        return SatelliteSavedData.getData(worldObj);
    }
}
