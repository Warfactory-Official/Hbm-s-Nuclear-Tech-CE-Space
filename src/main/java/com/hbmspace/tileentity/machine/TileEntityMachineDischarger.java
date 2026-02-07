package com.hbmspace.tileentity.machine;

import com.hbm.api.energymk2.IBatteryItem;
import com.hbm.api.energymk2.IEnergyProviderMK2;
import com.hbm.entity.effect.EntityCloudFleija;
import com.hbm.entity.logic.EntityNukeExplosionMK3;
import com.hbm.inventory.OreDictManager;
import com.hbm.items.ModItems;
import com.hbm.lib.HBMSoundHandler;
import com.hbm.lib.Library;
import com.hbm.main.MainRegistry;
import com.hbm.sound.AudioWrapper;
import com.hbmspace.items.ModItemsSpace;
import com.hbmspace.tileentity.ISpaceGuiProvider;
import com.hbm.tileentity.IPersistentNBT;
import com.hbm.tileentity.TileEntityMachineBase;
import com.hbmspace.interfaces.AutoRegister;
import com.hbmspace.inventory.container.ContainerMachineDischarger;
import com.hbmspace.inventory.gui.GUIMachineDischarger;
import com.hbmspace.tileentity.TESpaceUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

@AutoRegister
public class TileEntityMachineDischarger extends TileEntityMachineBase implements ITickable, IEnergyProviderMK2, ISpaceGuiProvider, IPersistentNBT{

    public long power = 0;
    public int process = 0;
    public int temp = 20;
    public static final int maxtemp = 2000;
    public static final long maxPower = 500000000;
    public static long Gen = 20000000;
    public static final int processSpeed = 100;
    public static final int CoolDown = 400;

    private AudioWrapper audio;

    public TileEntityMachineDischarger() {
        super(2, 1, false, true);
    }

    @Override
    public String getDefaultName() {
        return "container.machine_discharger";
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack stack) {
        switch (i) {
            case 0:
                if(TESpaceUtil.mODE(stack, OreDictManager.SA326.ingot()))
                    return true;
                break;
            case 2:
                if(TESpaceUtil.mODE(stack, OreDictManager.U233.ingot()))
                    return true;
                break;
            case 1:
            default:
                if (stack.getItem() instanceof IBatteryItem)
                    return true;
                break;
        }
        return false;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        power = nbt.getLong("power");
        process = nbt.getInteger("process");
        temp = nbt.getInteger("temp");
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(NBTTagCompound nbt) {

        nbt.setLong("power", power);
        nbt.setInteger("process", process);
        nbt.setInteger("temp", temp);
        return super.writeToNBT(nbt);
    }

    /*@Override
    public void setInventorySlotContents(int i, ItemStack itemStack) {
        inventory.setStackInSlot(i, itemStack);
        if(itemStack != null && itemStack.getCount() > getInventoryStackLimit()) {
            itemStack.getCount() = getInventoryStackLimit();
        }
    }*/

    @Override
    public int[] getAccessibleSlotsFromSide(EnumFacing e) {
        return new int[] { 0, 1 }; // hi my name is james and I copy paste furnace code without reading it
    }

    @Override
    public boolean canExtractItem(int i, ItemStack stack, int j) {

        if (i == 0) {
            return true;
        }

        if (i == 1) {
            return stack.getItem() instanceof IBatteryItem && ((IBatteryItem) stack.getItem()).getCharge(stack) == 0;
        }

        return false;
    }

    public long getPowerScaled(long i) {
        return (power * i) / maxPower;
    }

    public long getTempScaled(int i) {
        return ((long) temp * i) / maxtemp;
    }

    public int getProgressScaled(int i) {
        return (process * i) / processSpeed;
    }

    public int getCoolDownScaled(int i) {
        return (temp * i) / CoolDown;
    }

    public boolean canProcess() {
        //please PLEASE tell me how i can do better ffs
        if (temp <= 20 && !inventory.getStackInSlot(0).isEmpty() && TESpaceUtil.mODE(inventory.getStackInSlot(0), OreDictManager.SA326.ingot())) {
            return true;
        }

        if (temp <= 20 && !inventory.getStackInSlot(0).isEmpty() && TESpaceUtil.mODE(inventory.getStackInSlot(0), OreDictManager.U233.ingot())) {
            return true;
        }

        if (temp <= 20 && !inventory.getStackInSlot(0).isEmpty() && inventory.getStackInSlot(0).getItem() == ModItems.ingot_electronium) {
            return true;
        }
        return temp <= 20 && !inventory.getStackInSlot(0).isEmpty() && inventory.getStackInSlot(0).getItem() == ModItems.battery_creative;
    }

    public boolean isProcessing() {
        return process > 0;
    }

    public void process() {
        process++;
        if (process < processSpeed) return;

        process = 0;
        temp = maxtemp;

        ItemStack in = inventory.getStackInSlot(0);
        if (in.isEmpty()) return;

        ItemStack consumed = in.splitStack(1);

        inventory.setStackInSlot(0, in);

        if (in.isEmpty()) {
            Item consumedItem = consumed.getItem();

            if (consumedItem == ModItems.ingot_u233) {
                power += (long) (Gen * 0.8);
                inventory.setStackInSlot(0, new ItemStack(ModItems.ingot_titanium));
            } else if (consumedItem == ModItems.ingot_schrabidium) {
                power += Gen * 2;
                inventory.setStackInSlot(0, new ItemStack(ModItemsSpace.ingot_hafnium));
            } else if (consumedItem == ModItems.ingot_electronium) {
                power += Gen * 4;
                inventory.setStackInSlot(0, new ItemStack(ModItems.ingot_dineutronium));
            } else if (consumedItem == ModItems.battery_creative) {
                EntityNukeExplosionMK3 ex = EntityNukeExplosionMK3.statFacFleija(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 120);
                if (!ex.isDead) {
                    world.spawnEntity(ex);

                    EntityCloudFleija cloud = new EntityCloudFleija(world, 120);
                    cloud.setPosition(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                    world.spawnEntity(cloud);
                }
            }
        }

        world.playSound(null, pos, SoundEvents.ENTITY_LIGHTNING_THUNDER, SoundCategory.AMBIENT, 10000.0F, 0.8F + world.rand.nextFloat() * 0.2F);
    }

    @Override
    public void update() {

        if(!world.isRemote) {

            power = Library.chargeItemsFromTE(inventory, 1, power, maxPower);

            if(canProcess()) {
                process();
            } else {
                process = 0;
            }

            if(world.getTotalWorldTime() % 10 == 0) {
                if(temp > 20) {
                    temp = temp - 5;
                }
                if(temp < 20) { //70k for the love of fuck this was only when i was debugging
                    temp = 20;
                }

            }

            if(temp > 20) {
                if(world.getTotalWorldTime() % 7 == 0) {
                    this.world.playSound(null, this.pos.getX(), this.pos.getY() + 11, this.pos.getZ(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 0.5F);
                }

                NBTTagCompound data = new NBTTagCompound();
                data.setString("type", "tower");
                data.setFloat("lift", 0.1F);
                data.setFloat("base", 0.3F);
                data.setFloat("max", 1F);
                data.setInteger("life", 20 + world.rand.nextInt(20));

                data.setDouble("posX", pos.getX() + 0.5 + world.rand.nextDouble() - 0.5);
                data.setDouble("posZ", pos.getZ() + 0.5 + world.rand.nextDouble() -0.5);
                data.setDouble("posY", pos.getY() + 1);

                MainRegistry.proxy.effectNT(data);
            }

            this.networkPackNT(50);
        } else {

            if(process > 0) {

                if(audio == null) {
                    audio = createAudioLoop();
                    audio.startSound();
                } else if(!audio.isPlaying()) {
                    audio = rebootAudio(audio);
                }
            } else {

                if(audio != null) {
                    audio.stopSound();
                    audio = null;
                }
            }
        }

    }

    public AudioWrapper createAudioLoop() {
        return MainRegistry.proxy.getLoopedSound(HBMSoundHandler.tauChargeLoop, SoundCategory.BLOCKS, pos.getX(), pos.getY(), pos.getZ(), 1.0F, 10F, 1.0F);
    }



    public void onChunkUnload() {

        if(audio != null) {
            audio.stopSound();
            audio = null;
        }
    }

    public void invalidate() {

        super.invalidate();

        if(audio != null) {
            audio.stopSound();
            audio = null;
        }
    }

    @Override
    public void serialize(ByteBuf buf) {
        buf.writeLong(power);
        buf.writeInt(process);
        buf.writeInt(temp);
    }

    @Override
    public void deserialize(ByteBuf buf) {
        power = buf.readLong();
        process = buf.readInt();
        temp = buf.readInt();
    }

    @Override
    public void setPower(long i) {
        power = i;
    }

    @Override
    public long getPower() {
        return power;
    }

    @Override
    public long getMaxPower() {
        return maxPower;
    }

    @Override
    public Container provideContainer(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return new ContainerMachineDischarger(player.inventory, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen provideGUI(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return new GUIMachineDischarger(player.inventory, this);
    }

    @Override
    public void writeNBT(NBTTagCompound nbt) {
        NBTTagCompound data = new NBTTagCompound();
        data.setLong("power", power);
        data.setInteger("progress", process);
        data.setInteger("temp", temp);
        nbt.setTag(NBT_PERSISTENT_KEY, data);
    }

    @Override
    public void readNBT(NBTTagCompound nbt) {
        NBTTagCompound data = nbt.getCompoundTag(NBT_PERSISTENT_KEY);
        this.power = data.getLong("power");
        this.temp = data.getInteger("temp");
        this.process = data.getInteger("procsess");
    }
}
