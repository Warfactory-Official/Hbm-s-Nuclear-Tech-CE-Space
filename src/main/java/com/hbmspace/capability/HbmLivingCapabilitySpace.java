package com.hbmspace.capability;

import com.hbmspace.dim.trait.CBT_Atmosphere;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Callable;

// Th3_Sl1ze: theoretically I could mixin this into  In fact it would require a ton of shit getting into accessor
// otherwise I wouldn't be able to call it in EntityEffectHandler at all
// so I chose the simplest solution
public class HbmLivingCapabilitySpace {
    public interface IEntityHbmProps {
        int getOxy();
        CBT_Atmosphere getAtmosphere();
        boolean hasGravity();
        boolean hasWarped();
        void setOxy(int oxygen);
        void setWarped(boolean warped);
        void setAtmosphere(CBT_Atmosphere atmosphere);
        void saveNBTData(NBTTagCompound tag);
        void loadNBTData(NBTTagCompound tag);
    }

    public static class EntityHbmProps implements IEntityHbmProps {

        public static final Callable<HbmLivingCapabilitySpace.IEntityHbmProps> FACTORY = HbmLivingCapabilitySpace.EntityHbmProps::new;
        private int oxygen = 100;
        private CBT_Atmosphere atmosphere;
        private boolean gravity = false;
        private boolean hasWarped = false;

        @Override
        public int getOxy() {
            return oxygen;
        }

        @Override
        public CBT_Atmosphere getAtmosphere() {
            return atmosphere;
        }

        @Override
        public boolean hasGravity() {
            return gravity;
        }

        @Override
        public boolean hasWarped() {
            return hasWarped;
        }

        @Override
        public void setOxy(int oxygen) {
            this.oxygen = oxygen;
        }

        @Override
        public void setWarped(boolean warped) {
            this.hasWarped = warped;
        }

        @Override
        public void setAtmosphere(CBT_Atmosphere atmosphere) {
            this.atmosphere = atmosphere;
            this.gravity = atmosphere != null;
        }

        private void setGravity(boolean gravity) {
            this.gravity = gravity;
        }

        @Override
        public void saveNBTData(NBTTagCompound tag) {
            tag.setInteger("oxygen", oxygen);
            tag.setBoolean("gravity", gravity);
            tag.setBoolean("warped", hasWarped);
        }

        @Override
        public void loadNBTData(NBTTagCompound tag) {
            setOxy(tag.getInteger("oxygen"));
            setGravity(tag.getBoolean("gravity"));
            setWarped(tag.getBoolean("warped"));
        }
    }

    public static class EntityHbmPropsStorage implements Capability.IStorage<IEntityHbmProps> {

        @Override
        public NBTBase writeNBT(Capability<IEntityHbmProps> capability, IEntityHbmProps instance, EnumFacing side) {
            NBTTagCompound tag = new NBTTagCompound();
            instance.saveNBTData(tag);
            return tag;
        }

        @Override
        public void readNBT(Capability<IEntityHbmProps> capability, IEntityHbmProps instance, EnumFacing side, NBTBase nbt) {
            if(nbt instanceof NBTTagCompound){
                instance.loadNBTData((NBTTagCompound)nbt);
            }
        }

    }

    public static class EntityHbmPropsProvider implements ICapabilitySerializable<NBTBase> {

        public static final IEntityHbmProps DUMMY = new IEntityHbmProps() {
            @Override
            public int getOxy() {
                return 0;
            }

            @Override
            public CBT_Atmosphere getAtmosphere() {
                return null;
            }

            @Override
            public boolean hasGravity() {
                return false;
            }

            @Override
            public boolean hasWarped() {
                return false;
            }

            @Override
            public void setOxy(int oxygen) {
            }

            @Override
            public void setWarped(boolean warped) {
            }

            @Override
            public void setAtmosphere(CBT_Atmosphere atmosphere) {
            }

            @Override
            public void saveNBTData(NBTTagCompound tag){
            }
            @Override
            public void loadNBTData(NBTTagCompound tag){
            }
        };

        @CapabilityInject(IEntityHbmProps.class)
        public static Capability<IEntityHbmProps> ENT_HBM_PROPS_CAP = null;

        private final IEntityHbmProps instance = ENT_HBM_PROPS_CAP.getDefaultInstance();

        @Override
        public boolean hasCapability(@NotNull Capability<?> capability, EnumFacing facing) {
            return capability == ENT_HBM_PROPS_CAP;
        }

        @Override
        public <T> T getCapability(@NotNull Capability<T> capability, EnumFacing facing) {
            return capability == ENT_HBM_PROPS_CAP ? ENT_HBM_PROPS_CAP.cast(this.instance) : null;
        }

        @Override
        public NBTBase serializeNBT() {
            return ENT_HBM_PROPS_CAP.getStorage().writeNBT(ENT_HBM_PROPS_CAP, instance, null);
        }

        @Override
        public void deserializeNBT(NBTBase nbt) {
            ENT_HBM_PROPS_CAP.getStorage().readNBT(ENT_HBM_PROPS_CAP, instance, null, nbt);
        }
    }
}
