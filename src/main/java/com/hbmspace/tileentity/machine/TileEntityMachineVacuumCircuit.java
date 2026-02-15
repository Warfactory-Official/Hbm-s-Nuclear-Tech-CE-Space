package com.hbmspace.tileentity.machine;

import com.hbm.api.energymk2.IEnergyReceiverMK2;
import com.hbm.handler.threading.PacketThreading;
import com.hbm.inventory.RecipesCommon;
import com.hbm.inventory.UpgradeManagerNT;
import com.hbm.items.machine.ItemMachineUpgrade;
import com.hbm.lib.DirPos;
import com.hbm.lib.ForgeDirection;
import com.hbm.lib.HBMSoundHandler;
import com.hbm.lib.Library;
import com.hbm.packet.toclient.AuxParticlePacketNT;
import com.hbm.tileentity.IUpgradeInfoProvider;
import com.hbm.tileentity.TileEntityMachineBase;
import com.hbm.util.I18nUtil;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.dim.trait.CBT_Atmosphere;
import com.hbmspace.handler.atmosphere.ChunkAtmosphereManager;
import com.hbmspace.interfaces.AutoRegister;
import com.hbmspace.inventory.container.ContainerVacuumCircuit;
import com.hbmspace.inventory.gui.GUIVacuumCircuit;
import com.hbmspace.inventory.recipes.VacuumCircuitRecipes;
import com.hbmspace.tileentity.ISpaceGuiProvider;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
@AutoRegister
public class TileEntityMachineVacuumCircuit extends TileEntityMachineBase implements ITickable, IEnergyReceiverMK2, ISpaceGuiProvider, IUpgradeInfoProvider {

    public long power;
    public long maxPower = 2_000;
    public long consumption;

    public int progress;
    public int processTime = 1;

    private VacuumCircuitRecipes.VacuumCircuitRecipe recipe;
    public ItemStack display = ItemStack.EMPTY;

    public boolean canOperate = true;

    public UpgradeManagerNT upgradeManager = new UpgradeManagerNT(this);

    public TileEntityMachineVacuumCircuit() {
        super(8, false, true);
        inventory = new ItemStackHandler(8) {
            @Override
            public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
                super.setStackInSlot(slot, stack);
                if(!stack.isEmpty() && slot >= 6 && slot <=7 && stack.getItem() instanceof ItemMachineUpgrade) {
                    world.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, HBMSoundHandler.upgradePlug, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }
            }
        };
    }


    @Override
    public String getDefaultName() {
        return "container.machineVacuumCircuit";
    }

    @Override
    public void update() {

        if(!world.isRemote) {
            CBT_Atmosphere atmosphere = ChunkAtmosphereManager.proxy.getAtmosphere(world, pos.getX(), pos.getY(), pos.getZ());
            canOperate = atmosphere == null || atmosphere.getPressure() <= 0.001;

            this.power = Library.chargeTEFromItems(inventory, 5, this.getPower(), this.getMaxPower());
            this.updateConnections();
            recipe = VacuumCircuitRecipes.getRecipe(new ItemStack[] {inventory.getStackInSlot(0), inventory.getStackInSlot(1), inventory.getStackInSlot(2), inventory.getStackInSlot(3)});
            long intendedMaxPower;



            upgradeManager.checkSlots(inventory, 4, 4);
            int redLevel = upgradeManager.getLevel(ItemMachineUpgrade.UpgradeType.SPEED);
            int blueLevel = upgradeManager.getLevel(ItemMachineUpgrade.UpgradeType.POWER);

            if(recipe != null) {
                this.processTime = recipe.duration - (recipe.duration * redLevel / 6) + (recipe.duration * blueLevel / 3);
                this.consumption = recipe.consumption + (recipe.consumption * redLevel) - (recipe.consumption * blueLevel / 6);
                intendedMaxPower = recipe.consumption * 20;

                if(canProcess(recipe)) {
                    this.progress++;
                    this.power -= this.consumption;

                    if(progress >= processTime) {
                        this.progress = 0;
                        this.consumeItems(recipe);

                        if(inventory.getStackInSlot(4).isEmpty()) {
                            inventory.setStackInSlot(4, recipe.output.copy());
                        } else {
                            ItemStack stack = inventory.getStackInSlot(4).copy();
                            stack.grow(recipe.output.getCount());
                            inventory.setStackInSlot(4, stack);
                        }

                        this.markDirty();
                    }

                    if(world.getTotalWorldTime() % 20 == 0) {
                        ForgeDirection dir = ForgeDirection.getOrientation(this.getBlockMetadata() - 10);
                        ForgeDirection rot = dir.getRotation(ForgeDirection.UP);
                        NBTTagCompound dPart = new NBTTagCompound();
                        dPart.setString("type", "tau");
                        dPart.setByte("count", (byte) 3);
                        PacketThreading.createAllAroundThreadedPacket(new AuxParticlePacketNT(dPart, pos.getX() + 0.5 + dir.offsetX * 0.625 + rot.offsetX * 0.5, pos.getY() + 1.25, pos.getZ() + 0.5 + dir.offsetZ * 0.625 + rot.offsetZ * 0.5), new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 25));
                    }
                } else {
                    this.progress = 0;
                }

            } else {
                this.progress = 0;
                this.consumption = 100;
                intendedMaxPower = 2000;
            }

            this.maxPower = Math.max(intendedMaxPower, power);

            this.networkPackNT(25);
        }
    }

    public boolean canProcess(VacuumCircuitRecipes.VacuumCircuitRecipe recipe) {
        if(!canOperate) return false;

        if(this.power < this.consumption) return false;

        if(!inventory.getStackInSlot(4).isEmpty()) {
            if(inventory.getStackInSlot(4).getItem() != recipe.output.getItem()) return false;
            if(inventory.getStackInSlot(4).getItemDamage() != recipe.output.getItemDamage()) return false;
            return inventory.getStackInSlot(4).getCount() + recipe.output.getCount() <= inventory.getStackInSlot(4).getMaxStackSize();
        }

        return true;
    }
    private void updateConnections() {
        for(DirPos pos : getConPos()) {
            this.trySubscribe(world, pos.getPos().getX(), pos.getPos().getY(), pos.getPos().getZ(), pos.getDir());
        }
    }
    public void consumeItems(VacuumCircuitRecipes.VacuumCircuitRecipe recipe) {

        for(RecipesCommon.AStack aStack : recipe.wafer) {
            for(int i = 0; i < 2; i++) {
                ItemStack stack = inventory.getStackInSlot(i).copy();
                if(aStack.matchesRecipe(stack, true) && stack.getCount() >= aStack.stacksize) {
                    stack.shrink(aStack.stacksize);
                    inventory.setStackInSlot(i, stack);
                    break;
                }
            }
        }

        for(RecipesCommon.AStack aStack : recipe.pcb) {
            for(int i = 2; i < 4; i++) {
                ItemStack stack = inventory.getStackInSlot(i);
                if(aStack.matchesRecipe(stack, true) && stack.getCount() >= aStack.stacksize) {
                    stack.shrink(aStack.stacksize);
                    inventory.setStackInSlot(i, stack);
                    break;
                }
            }
        }

    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        if(slot < 2) {
            for(int i = 0; i < 2; i++) if(i != slot && !inventory.getStackInSlot(i).isEmpty() && inventory.getStackInSlot(i).isItemEqual(stack)) return false;
            for(RecipesCommon.AStack t : VacuumCircuitRecipes.wafer) if(t.matchesRecipe(stack, true)) return true;
        } else if(slot < 4) {
            for(int i = 2; i < 4; i++) if(i != slot && !inventory.getStackInSlot(i).isEmpty() && inventory.getStackInSlot(i).isItemEqual(stack)) return false;
            for(RecipesCommon.AStack t : VacuumCircuitRecipes.pcb) if(t.matchesRecipe(stack, true)) return true;
        }
        return false;
    }

    @Override
    public boolean canExtractItem(int i, ItemStack itemStack, int j) {
        return i == 4;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(EnumFacing side) {
        return new int[] { 0, 1, 2, 3, 4 };
    }

    public DirPos[] getConPos() {
        return new DirPos[] {
                new DirPos(pos.getX() + 2, pos.getY(), pos.getZ() + 1, Library.POS_X),
                new DirPos(pos.getX() + 2, pos.getY(), pos.getZ() - 1, Library.POS_X),
                new DirPos(pos.getX() - 2, pos.getY(), pos.getZ() + 1, Library.NEG_X),
                new DirPos(pos.getX() - 2, pos.getY(), pos.getZ() - 1, Library.NEG_X),
                new DirPos(pos.getX() + 1, pos.getY(), pos.getZ() + 2, Library.POS_Z),
                new DirPos(pos.getX() - 1, pos.getY(), pos.getZ() + 2, Library.POS_Z),
                new DirPos(pos.getX() + 1, pos.getY(), pos.getZ() - 2, Library.NEG_Z),
                new DirPos(pos.getX() - 1, pos.getY(), pos.getZ() - 2, Library.NEG_Z)
        };
    }

    @Override
    public void serialize(ByteBuf buf) {
        super.serialize(buf);

        buf.writeLong(power);
        buf.writeLong(maxPower);
        buf.writeLong(consumption);
        buf.writeInt(progress);
        buf.writeInt(processTime);
        buf.writeBoolean(canOperate);

        if(recipe != null) {
            buf.writeBoolean(true);
            buf.writeInt(Item.getIdFromItem(recipe.output.getItem()));
            buf.writeInt(recipe.output.getItemDamage());
        } else {
            buf.writeBoolean(false);
        }
    }

    @Override
    public void deserialize(ByteBuf buf) {
        super.deserialize(buf);

        power = buf.readLong();
        maxPower = buf.readLong();
        consumption = buf.readLong();
        progress = buf.readInt();
        processTime = buf.readInt();
        canOperate = buf.readBoolean();

        if(buf.readBoolean()) {
            int id = buf.readInt();
            int meta = buf.readInt();
            display = new ItemStack(Item.getItemById(id), 1, meta);
        } else {
            display = ItemStack.EMPTY;
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        this.power = nbt.getLong("power");
        this.maxPower = nbt.getLong("maxPower");
        this.progress = nbt.getInteger("progress");
        this.processTime = nbt.getInteger("processTime");
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setLong("power", power);
        nbt.setLong("maxPower", maxPower);
        nbt.setInteger("progress", progress);
        nbt.setInteger("processTime", processTime);
        return super.writeToNBT(nbt);
    }

    @Override
    public long getPower() {
        return Math.max(Math.min(power, maxPower), 0);
    }

    @Override
    public void setPower(long power) {
        this.power = power;
    }

    @Override
    public long getMaxPower() {
        return maxPower;
    }


    @Override
    public Container provideContainer(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return new ContainerVacuumCircuit(player.inventory, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen provideGUI(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return new GUIVacuumCircuit(player.inventory, this);
    }

    AxisAlignedBB bb = null;

    @Override
    public @NotNull AxisAlignedBB getRenderBoundingBox() {

        if(bb == null) {
            bb = new AxisAlignedBB(
                    pos.getX() - 1,
                    pos.getY(),
                    pos.getZ() - 1,
                    pos.getX() + 2,
                    pos.getY() + 3,
                    pos.getZ() + 2
            );
        }

        return bb;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared() {
        return 65536.0D;
    }

    @Override
    public boolean canProvideInfo(ItemMachineUpgrade.UpgradeType type, int level, boolean extendedInfo) {
        return type == ItemMachineUpgrade.UpgradeType.SPEED || type == ItemMachineUpgrade.UpgradeType.POWER;
    }

    @Override
    public void provideInfo(ItemMachineUpgrade.UpgradeType type, int level, List<String> info, boolean extendedInfo) {
        info.add(IUpgradeInfoProvider.getStandardLabel(ModBlocksSpace.machine_vacuum_circuit));
        if(type == ItemMachineUpgrade.UpgradeType.SPEED) {
            info.add(TextFormatting.GREEN + I18nUtil.resolveKey(KEY_DELAY, "-" + (level * 100 / 6) + "%"));
            info.add(TextFormatting.RED + I18nUtil.resolveKey(KEY_CONSUMPTION, "+" + (level * 100) + "%"));
        }
        if(type == ItemMachineUpgrade.UpgradeType.POWER) {
            info.add(TextFormatting.GREEN + I18nUtil.resolveKey(KEY_CONSUMPTION, "-" + (level * 100 / 6) + "%"));
            info.add(TextFormatting.RED + I18nUtil.resolveKey(KEY_DELAY, "+" + (level * 100 / 3) + "%"));
        }
    }

    @Override
    public HashMap<ItemMachineUpgrade.UpgradeType, Integer> getValidUpgrades() {
        HashMap<ItemMachineUpgrade.UpgradeType, Integer> upgrades = new HashMap<>();
        upgrades.put(ItemMachineUpgrade.UpgradeType.SPEED, 3);
        upgrades.put(ItemMachineUpgrade.UpgradeType.POWER, 3);
        return upgrades;
    }

}
