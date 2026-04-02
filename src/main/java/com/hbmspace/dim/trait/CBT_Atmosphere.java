package com.hbmspace.dim.trait;

import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.trait.FluidTrait;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.List;


public class CBT_Atmosphere extends CelestialBodyTrait {

	public ArrayList<FluidEntry> fluids;

	public static class FluidEntry {
		public FluidType fluid;
		public double pressure;

		public FluidEntry(FluidType fluid, double percentage) {
			this.fluid = fluid;
			this.pressure = percentage;
		}
	}

	public CBT_Atmosphere() {
		fluids = new ArrayList<>();
	}

	public CBT_Atmosphere(FluidType fluid, double pressure) {
		fluids = new ArrayList<>();
		fluids.add(new FluidEntry(fluid, pressure));
	}

	public CBT_Atmosphere and(FluidType fluid, double pressure) {
		if (fluids == null) fluids = new ArrayList<>();
		fluids.add(new FluidEntry(fluid, pressure));
		return this;
	}

	public CBT_Atmosphere clone() {
		CBT_Atmosphere clone = new CBT_Atmosphere();
		if (fluids != null) {
			for (int i = 0; i < fluids.size(); i++) {
				FluidEntry entry = fluids.get(i);
				if (entry != null) {
					clone.fluids.add(new FluidEntry(entry.fluid, entry.pressure));
				}
			}
		}
		return clone;
	}

	// Add to the pressure by incrementing existing fluid entries
	public void add(FluidType fluid, double pressure) {
		if (fluids == null) fluids = new ArrayList<>();
		for (int i = 0; i < fluids.size(); i++) {
			FluidEntry entry = fluids.get(i);
			if (entry != null && entry.fluid == fluid) {
				entry.pressure += pressure;
				return;
			}
		}

		fluids.add(new FluidEntry(fluid, pressure));
	}

	// Reduce the pressure by proportionally reducing the pressure of each entry
	public void reduce(double pressure) {
		double totalPressure = getPressure();
		if(pressure >= totalPressure) {
			fluids = new ArrayList<>();
			return;
		}

		double pressureRatio = pressure / totalPressure;
		if (fluids != null) {
			for (int i = 0; i < fluids.size(); i++) {
				FluidEntry entry = fluids.get(i);
				if (entry != null) {
					entry.pressure *= pressureRatio;
				}
			}
		}
	}

	// Fluid must be above at least 1 millbar to be "present"
	public boolean hasFluid(FluidType fluid) {
		return hasFluid(fluid, 0.001);
	}

	public boolean hasFluid(FluidType fluid, double abovePressure) {
		if (fluids == null) return false;
		for (int i = 0; i < fluids.size(); i++) {
			FluidEntry entry = fluids.get(i);
			if (entry != null && entry.fluid == fluid) {
				return entry.pressure >= abovePressure;
			}
		}

		return false;
	}

	public boolean hasTrait(Class<? extends FluidTrait> trait) {
		return hasTrait(trait, 0.001);
	}

	public boolean hasTrait(Class<? extends FluidTrait> trait, double abovePressure) {
		if (fluids == null) return false;
		for (int i = 0; i < fluids.size(); i++) {
			FluidEntry entry = fluids.get(i);
			if (entry != null && entry.pressure >= abovePressure && entry.fluid != null && entry.fluid.hasTrait(trait)) {
				return true;
			}
		}

		return false;
	}

	// Get the highest pressure fluid
	public FluidType getMainFluid() {
		if (fluids == null || fluids.isEmpty()) return Fluids.NONE;
		sortDescending();
		FluidEntry first = fluids.getFirst();
		return first != null ? first.fluid : Fluids.NONE;
	}

	public void sortDescending() {
		if (fluids != null) {
			fluids.sort((a, b) -> Double.compare(b.pressure, a.pressure));
		}
	}

	// FluidEntries store PARTIAL pressure, to get the total atmospheric pressure, use this method
	public double getPressure() {
		if (fluids == null) return 0;
		double pressure = 0;
		for (int i = 0; i < fluids.size(); i++) {
			FluidEntry entry = fluids.get(i);
			if (entry != null) {
				pressure += entry.pressure;
			}
		}
		return pressure;
	}

	public double getPressure(FluidType fluid) {
		if (fluids == null) return 0;
		for (int i = 0; i < fluids.size(); i++) {
			FluidEntry entry = fluids.get(i);
			if (entry != null && entry.fluid == fluid) {
				return entry.pressure;
			}
		}
		
		return 0;
	}

	public List<Integer> getFluidColors() {
		List<Integer> colors = new ArrayList<>();
		if (fluids != null) {
			for (int i = 0; i < fluids.size(); i++) {
				FluidEntry entry = fluids.get(i);
				if (entry != null && entry.fluid != null) {
					colors.add(entry.fluid.getColor());
				}
			}
		}
		return colors;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		NBTTagList fluidList = new NBTTagList();
		if (fluids != null) {
			for (int i = 0; i < fluids.size(); i++) {
				FluidEntry entry = fluids.get(i);
				if (entry != null && entry.fluid != null) {
					NBTTagCompound fluidTag = new NBTTagCompound();
					fluidTag.setInteger("type", entry.fluid.getID());
					fluidTag.setDouble("percentage", entry.pressure);
					fluidList.appendTag(fluidTag);
				}
			}
		}
		nbt.setTag("fluids", fluidList);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		fluids = new ArrayList<>();
		NBTTagList fluidList = nbt.getTagList("fluids", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < fluidList.tagCount(); i++) {
			NBTTagCompound fluidTag = fluidList.getCompoundTagAt(i);
			FluidType fluid = Fluids.fromID(fluidTag.getInteger("type"));
			double percentage = fluidTag.getDouble("percentage");
			fluids.add(new FluidEntry(fluid, percentage));
		}
	}

	// These methods are for client syncing, the precision loss is intentional
	@Override
	public void writeToBytes(ByteBuf buf) {
		if (fluids == null) {
			buf.writeInt(0);
			return;
		}
		buf.writeInt(fluids.size());
		for (int i = 0; i < fluids.size(); i++) {
			FluidEntry entry = fluids.get(i);
			if (entry != null && entry.fluid != null) {
				buf.writeInt(entry.fluid.getID());
				buf.writeFloat((float)entry.pressure);
			}
		}
	}

	@Override
	public void readFromBytes(ByteBuf buf) {
		int size = buf.readInt();
		ArrayList<FluidEntry> newFluids = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			FluidType fluid = Fluids.fromID(buf.readInt());
			double percentage = buf.readFloat();
			newFluids.add(new FluidEntry(fluid, percentage));
		}
		this.fluids = newFluids;
	}
}
