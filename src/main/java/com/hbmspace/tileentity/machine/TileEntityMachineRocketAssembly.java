package com.hbmspace.tileentity.machine;

import com.hbm.blocks.BlockDummyable;
import com.hbm.interfaces.IControlReceiver;
import com.hbm.items.ISatChip;
import com.hbm.lib.ForgeDirection;
import com.hbmspace.tileentity.ISpaceGuiProvider;
import com.hbm.tileentity.TileEntityMachineBase;
import com.hbm.util.BobMathUtil;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.handler.RocketStruct;
import com.hbmspace.interfaces.AutoRegister;
import com.hbmspace.inventory.container.ContainerMachineRocketAssembly;
import com.hbmspace.inventory.gui.GUIMachineRocketAssembly;
import com.hbmspace.inventory.slots.SlotRocket;
import com.hbmspace.items.ItemVOTVdrive;
import com.hbmspace.items.weapon.ItemCustomRocket;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
@AutoRegister
public class TileEntityMachineRocketAssembly extends TileEntityMachineBase implements ITickable, ISpaceGuiProvider, IControlReceiver, SlotRocket.IStage {

    public RocketStruct rocket;

    private int previousHeight = 0;
    private List<Integer> platforms = new ArrayList<>();

    private boolean platformFailed = false;

    public int currentStage;

    public boolean isBreaking;

    public TileEntityMachineRocketAssembly() {
        super(1 + RocketStruct.MAX_STAGES * 3 + 1 + RocketStruct.MAX_STAGES * 2, false, false); // capsule + stages + result + drives
        inventory = new ItemStackHandler(1 + RocketStruct.MAX_STAGES * 3 + 1 + RocketStruct.MAX_STAGES * 2){
            @Override
            public int getSlots() {
                if(isBreaking) return super.getSlots() - RocketStruct.MAX_STAGES * 2;
                return super.getSlots();
            }
            @Override
            public int getSlotLimit(int slot) {
                return 8;
            }
        };
    }

    @Override
    public String getDefaultName() {
        return "container.machineRocketAssembly";
    }



    @Override
    public void update() {
        if(!world.isRemote) {
            ItemStack fromStack = inventory.getStackInSlot(inventory.getSlots() - (RocketStruct.MAX_STAGES - currentStage) * 2);
            ItemStack toStack = inventory.getStackInSlot(inventory.getSlots() - (RocketStruct.MAX_STAGES - currentStage) * 2 + 1);

            // updates the orbital station information and syncs it to the client, if necessary
            ItemVOTVdrive.getTarget(fromStack, world);
            ItemVOTVdrive.getTarget(toStack, world);

            rocket = new RocketStruct(inventory.getStackInSlot(0));
            if(!inventory.getStackInSlot(0).isEmpty() && inventory.getStackInSlot(0).getItem() instanceof ISatChip) {
                rocket.satFreq = ISatChip.getFreqS(inventory.getStackInSlot(0));
            }
            for(int i = 1; i < RocketStruct.MAX_STAGES * 3; i += 3) {
                if(inventory.getStackInSlot(i).isEmpty() && inventory.getStackInSlot(i+1).isEmpty() && inventory.getStackInSlot(i+2).isEmpty()) {
                    // Check for later stages and shift them up into empty stages
                    if(i + 3 < RocketStruct.MAX_STAGES * 3 && (!inventory.getStackInSlot(i+3).isEmpty() || !inventory.getStackInSlot(i+4).isEmpty() || !inventory.getStackInSlot(i+5).isEmpty())) {
                        inventory.setStackInSlot(i, inventory.getStackInSlot(i+3));
                        inventory.setStackInSlot(i+1, inventory.getStackInSlot(i+4));
                        inventory.setStackInSlot(i+2, inventory.getStackInSlot(i+5));
                        inventory.setStackInSlot(i+3, ItemStack.EMPTY);
                        inventory.setStackInSlot(i+4, ItemStack.EMPTY);
                        inventory.setStackInSlot(i+5, ItemStack.EMPTY);
                    } else {
                        break;
                    }
                }
                rocket.addStage(inventory.getStackInSlot(i), inventory.getStackInSlot(i+1), inventory.getStackInSlot(i+2));
            }


            int height = (int)rocket.getHeight();
            if(height != previousHeight) {
                BlockDummyable.safeRem = true;

                // Delete previously generated platforms
                for(int platform : platforms) {
                    deletePlatform(platform);
                }
                platforms = new ArrayList<>();

                // Check headroom
                int maxHeight = Integer.MAX_VALUE;
                for(int h = 1; h < 256; h++) {
                    Block block = world.getBlockState(pos.up(h)).getBlock();
                    if(!block.isReplaceable(world, pos.up(h)) && block != ModBlocksSpace.machine_rocket_assembly) {
                        maxHeight = h;
                        break;
                    }
                }

                double checkHeight = rocket.getHeight();
                if(rocket.capsule != null) checkHeight -= rocket.capsule.height;
                if(!rocket.stages.isEmpty() && rocket.stages.getFirst().thruster != null) checkHeight -= rocket.stages.getFirst().thruster.height;

                if(checkHeight < maxHeight) {
                    // Create platforms to stand on
                    int targetHeight = 0;
                    for(int i = 0; i < rocket.stages.size(); i++) {
                        RocketStruct.RocketStage stage = rocket.stages.get(i);
                        RocketStruct.RocketStage nextStage = i < rocket.stages.size() - 1 ? rocket.stages.get(i + 1) : null;

                        if(stage.fuselage != null) targetHeight += (int) (stage.fuselage.height * stage.getStack());
                        if(nextStage != null && nextStage.thruster != null) targetHeight += (int) nextStage.thruster.height;

                        int platform = Math.round(targetHeight);

                        if(platform > 0) {
                            addPlatform(platform);
                            platforms.add(platform);
                        }
                    }

                    // Create a central spire (required so the VAB can be broken properly)
                    int meta = ForgeDirection.UP.ordinal();
                    for(int i = 1; i < targetHeight + 1; i++) {
                        if(pos.getY() + i > 255) break;
                        world.setBlockState(pos.add(0, i, 0), ModBlocksSpace.machine_rocket_assembly.getDefaultState().withProperty(BlockDummyable.META, meta), 3);
                        world.setBlockState(pos.add(-4, i, -4), ModBlocksSpace.machine_rocket_assembly.getDefaultState().withProperty(BlockDummyable.META, meta), 3);
                        world.setBlockState(pos.add(4, i, -4), ModBlocksSpace.machine_rocket_assembly.getDefaultState().withProperty(BlockDummyable.META, meta), 3);
                        world.setBlockState(pos.add(-4, i, 4), ModBlocksSpace.machine_rocket_assembly.getDefaultState().withProperty(BlockDummyable.META, meta), 3);
                        world.setBlockState(pos.add(4, i, 4), ModBlocksSpace.machine_rocket_assembly.getDefaultState().withProperty(BlockDummyable.META, meta), 3);
                    }

                    for(int i = targetHeight + 1; i < 256 && world.getBlockState(pos.up(i)).getBlock() == ModBlocksSpace.machine_rocket_assembly; i++) {
                        world.setBlockToAir(pos.up(i));
                    }

                    for(int i = targetHeight + 1; i < 256 && world.getBlockState(pos.add(-4, i, -4)).getBlock() == ModBlocksSpace.machine_rocket_assembly; i++) {
                        world.setBlockToAir(pos.add(-4, i, -4));
                    }
                    for(int i = targetHeight + 1; i < 256 && world.getBlockState(pos.add(4, i, -4)).getBlock() == ModBlocksSpace.machine_rocket_assembly; i++) {
                        world.setBlockToAir(pos.add(4, i, -4));
                    }
                    for(int i = targetHeight + 1; i < 256 && world.getBlockState(pos.add(-4, i, 4)).getBlock() == ModBlocksSpace.machine_rocket_assembly; i++) {
                        world.setBlockToAir(pos.add(-4, i, 4));
                    }
                    for(int i = targetHeight + 1; i < 256 && world.getBlockState(pos.add(4, i, 4)).getBlock() == ModBlocksSpace.machine_rocket_assembly; i++) {
                        world.setBlockToAir(pos.add(4, i, 4));
                    }

                    platformFailed = false;
                } else {
                    platformFailed = true;
                }

                BlockDummyable.safeRem = false;
                previousHeight = height;
            }

            if(platformFailed) {
                rocket.addIssue(TextFormatting.RED + "VAB ceiling too low ");
            }

            networkPackNT(250);
        }
    }

    public void addPlatform(int height) {
        for(int x = -4; x <= 4; x++) {
            for(int z = -4; z <= 4; z++) {
                int meta;

                if((x == -4 || x == 4) && (z == -4 || z == 4)) continue;

                if(x < 0) {
                    meta = ForgeDirection.WEST.ordinal();
                } else if(x > 0) {
                    meta = ForgeDirection.EAST.ordinal();
                } else if(z < 0) {
                    meta = ForgeDirection.NORTH.ordinal();
                } else if(z > 0) {
                    meta = ForgeDirection.SOUTH.ordinal();
                } else {
                    continue;
                }

                world.setBlockState(pos.add(x, height, z), ModBlocksSpace.machine_rocket_assembly.getDefaultState().withProperty(BlockDummyable.META, meta), 3);
            }
        }
    }

    public void deletePlatform(int height) {
        for(int x = -4; x <= 4; x++) {
            for(int z = -4; z <= 4; z++) {
                if(x == 0 && z == 0) continue;
                if((x == -4 || x == 4) && (z == -4 || z == 4)) continue;

                world.setBlockToAir(pos.add(x, height, z));
            }
        }
    }

    @Override
    public void serialize(ByteBuf buf) {
        rocket.writeToByteBuffer(buf);
    }

    @Override
    public void deserialize(ByteBuf buf) {
        rocket = RocketStruct.readFromByteBuffer(buf);
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setIntArray("platforms", BobMathUtil.intCollectionToArray(platforms));
        return super.writeToNBT(nbt);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        platforms = new ArrayList<>();
        for(int i : nbt.getIntArray("platforms")) platforms.add(i);
    }

    public void construct() {
        if(!rocket.validate()) return;

        inventory.setStackInSlot(inventory.getSlots() - RocketStruct.MAX_STAGES * 2 - 1, ItemCustomRocket.build(rocket));

        for(int i = 0; i < inventory.getSlots() - RocketStruct.MAX_STAGES * 2 - 1; i++) {
            inventory.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    public boolean canDeconstruct() {
        RocketStruct rocket = ItemCustomRocket.get(inventory.getStackInSlot(inventory.getSlots() - RocketStruct.MAX_STAGES * 2 - 1));
        if(rocket == null) return false;
        for(int i = 0; i < inventory.getSlots() - RocketStruct.MAX_STAGES * 2 - 1; i++) {
            if(!inventory.getStackInSlot(i).isEmpty()) return false;
        }

        return true;
    }

    public void deconstruct() {
        if(!canDeconstruct()) return;
        int satFreq = ISatChip.getFreqS(inventory.getStackInSlot(inventory.getSlots() - RocketStruct.MAX_STAGES * 2 - 1));
        RocketStruct rocket = ItemCustomRocket.get(inventory.getStackInSlot(inventory.getSlots() - RocketStruct.MAX_STAGES * 2 - 1));

        inventory.setStackInSlot(0, new ItemStack(rocket.capsule.part));
        if(inventory.getStackInSlot(0).getItem() instanceof ISatChip) {
            ISatChip.setFreqS(inventory.getStackInSlot(0), satFreq);
        }
        for(int i = 0; i < rocket.stages.size(); i++) {
            int o = i * 3;
            RocketStruct.RocketStage stage = rocket.stages.get(rocket.stages.size() - 1 - i);
            inventory.setStackInSlot(o + 1, new ItemStack(stage.fuselage.part, stage.fuselageCount));
            if(stage.fins != null) inventory.setStackInSlot(o + 2, new ItemStack(stage.fins.part));
            inventory.setStackInSlot(o + 3, new ItemStack(stage.thruster.part, stage.thrusterCount));
        }

        inventory.setStackInSlot(inventory.getSlots() - RocketStruct.MAX_STAGES * 2 - 1, ItemStack.EMPTY);
    }

    @Override
    public @NotNull AxisAlignedBB getRenderBoundingBox() {
        return TileEntity.INFINITE_EXTENT_AABB;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared() {
        return 65536.0D;
    }

    @Override
    public Container provideContainer(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return new ContainerMachineRocketAssembly(player.inventory, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen provideGUI(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return new GUIMachineRocketAssembly(player.inventory, this);
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return world.getTileEntity(pos) == this;
    }

    @Override
    public boolean hasPermission(EntityPlayer player) {
        return isUseableByPlayer(player);
    }

    @Override
    public void receiveControl(NBTTagCompound data) {
        if(data.getBoolean("construct")) {
            construct();
        }
        if(data.getBoolean("deconstruct")) {
            deconstruct();
        }
    }

    @Override
    public void setCurrentStage(int stage) {
        currentStage = stage;
    }

    @Override
    public int getSlots() {
        if(isBreaking) return (1 + RocketStruct.MAX_STAGES * 3 + 1 + RocketStruct.MAX_STAGES * 2) - RocketStruct.MAX_STAGES * 2;
        return 1 + RocketStruct.MAX_STAGES * 3 + 1 + RocketStruct.MAX_STAGES * 2;
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
        return inventory.getStackInSlot(slot);
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        return inventory.insertItem(slot, stack, simulate);
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        return inventory.extractItem(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return 8;
    }
}
