package com.hbmspace.items;

import com.google.common.collect.ImmutableMap;
import com.hbm.lib.HBMSoundHandler;
import com.hbm.util.I18nUtil;
import com.hbmspace.config.SpaceConfig;
import com.hbmspace.dim.CelestialBody;
import com.hbmspace.dim.SolarSystem;
import com.hbmspace.dim.orbit.OrbitalStation;
import com.hbmspace.entity.missile.EntityRideableRocket;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Locale;

public class ItemVOTVdrive extends ItemEnumMultiSpace {
    @SideOnly(Side.CLIENT)
    private ModelResourceLocation[] mrls;
    @SideOnly(Side.CLIENT)
    private ResourceLocation baseTex;
    @SideOnly(Side.CLIENT)
    private ResourceLocation[] overlayTex;

    public ItemVOTVdrive(String s) {
        super(s, SolarSystem.Body.class, false, false);
        this.setMaxStackSize(1);
        this.setNoRepair();
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            this.baseTex = new ResourceLocation("hbm", "items/votv_f");
            this.overlayTex = new ResourceLocation[SolarSystem.Body.values().length];
            for (int i = 0; i < overlayTex.length; i++) {
                SolarSystem.Body body = SolarSystem.Body.values()[i];
                String name = body != SolarSystem.Body.ORBIT ? body.name().toLowerCase(Locale.US) : "orbit";
                overlayTex[i] = new ResourceLocation("hbm", "items/votv." + name);
            }
        }
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        Destination destination = getDestination(stack);

        if (destination.body == SolarSystem.Body.ORBIT) {
            NBTTagCompound tag = stack.getTagCompound();
            String identifier = tag != null ? tag.getString("stationName") : "";

            if (identifier.equals("")) {
                identifier = "0x" + Integer.toHexString(new ChunkPos(destination.x, destination.z).hashCode()).toUpperCase();
            }

            tooltip.add("Destination: ORBITAL STATION");
            tooltip.add("Station: " + identifier);
            return;
        }

        int processingLevel = 0;
        if (worldIn != null) {
            processingLevel = destination.body.getProcessingLevel(CelestialBody.getBody(worldIn));
        }

        tooltip.add("Destination: " + TextFormatting.AQUA + I18nUtil.resolveKey("body." + destination.body.name));

        if (destination.x == 0 && destination.z == 0) {
            tooltip.add(TextFormatting.GOLD + "Needs destination coordinates!");
        } else if (!getProcessed(stack)) {
            tooltip.add("Process requirement: Level " + processingLevel);
            tooltip.add(TextFormatting.GOLD + "Needs processing!");
            tooltip.add("Target coordinates: " + destination.x + ", " + destination.z);
        } else {
            tooltip.add(TextFormatting.GREEN + "Processed!");
            tooltip.add("Target coordinates: " + destination.x + ", " + destination.z);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (!this.isInCreativeTab(tab)) return;

        for (int i = 0; i < theEnum.getEnumConstants().length; i++) {
            ItemStack stack = new ItemStack(this, 1, i);
            NBTTagCompound stackTag = new NBTTagCompound();
            stackTag.setInteger("x", 1);
            stackTag.setInteger("ax", 1);
            stackTag.setBoolean("Processed", true);
            stack.setTagCompound(stackTag);
            items.add(stack);
        }
    }

    public static SolarSystem.Body getBody(ItemStack stack) {
        return SolarSystem.Body.values()[stack.getMetadata() % SolarSystem.Body.values().length];
    }

    public static Destination getDestination(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound tag = stack.getTagCompound();
        SolarSystem.Body body = getBody(stack);
        int x = tag.getInteger("x");
        int z = tag.getInteger("z");
        return new Destination(body, x, z);
    }

    public static Target getTarget(ItemStack stack, World world) {
        if (stack == null || stack.isEmpty()) {
            return new Target(null, false, false);
        }

        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound tag = stack.getTagCompound();
        Destination destination = getDestination(stack);

        if (destination.body == SolarSystem.Body.ORBIT) {
            if (world.isRemote) {
                CelestialBody body = CelestialBody.getBody(tag.getInteger("sDim"));
                boolean hasStation = tag.getBoolean("sHas");

                return new Target(body, true, hasStation);
            }

            OrbitalStation station = OrbitalStation.getStation(destination.x, destination.z);
            if (!station.hasStation) station.orbiting = CelestialBody.getBody(world);

            tag.setString("stationName", station.name);
            tag.setInteger("sDim", station.orbiting.dimensionId);
            tag.setBoolean("sHas", station.hasStation);

            return new Target(station.orbiting, true, station.hasStation);
        } else {
            return new Target(destination.body.getBody(), false, true);
        }
    }

    public static void setCoordinates(ItemStack stack, int x, int z) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound tag = stack.getTagCompound();
        tag.setInteger("x", x);
        tag.setInteger("z", z);
    }

    public static int getProcessingTier(ItemStack stack, CelestialBody from) {
        SolarSystem.Body body = getBody(stack);
        return body.getProcessingLevel(from);
    }

    public static boolean getProcessed(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }

        return stack.getTagCompound().getBoolean("Processed");
    }

    public static void setProcessed(ItemStack stack, boolean processed) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }

        stack.getTagCompound().setBoolean("Processed", processed);
    }

    public static Destination getApproximateDestination(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound tag = stack.getTagCompound();
        SolarSystem.Body body = getBody(stack);
        if (!tag.hasKey("ax") || !tag.hasKey("az")) {
            tag.setInteger("ax", itemRand.nextInt(SpaceConfig.maxProbeDistance * 2) - SpaceConfig.maxProbeDistance);
            tag.setInteger("az", itemRand.nextInt(SpaceConfig.maxProbeDistance * 2) - SpaceConfig.maxProbeDistance);
        }
        int ax = tag.getInteger("ax");
        int az = tag.getInteger("az");
        return new Destination(body, ax, az);
    }

    public static void markCopied(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }

        stack.getTagCompound().setBoolean("copied", true);
    }

    public static boolean wasCopied(ItemStack stack) {
        if (!stack.hasTagCompound()) return false;
        return stack.getTagCompound().getBoolean("copied");
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);

        boolean isProcessed = getProcessed(stack);
        boolean onDestination = world.provider.getDimension() == getDestination(stack).body.getDimensionId();

        if (!isProcessed && (player.capabilities.isCreativeMode || onDestination)) {
            isProcessed = true;
            setProcessed(stack, true);
        }

        ItemStack newStack = stack;

        if (isProcessed && player.getRidingEntity() != null && player.getRidingEntity() instanceof EntityRideableRocket) {
            EntityRideableRocket rocket = (EntityRideableRocket) player.getRidingEntity();

            if (rocket.getRocket().stages.size() > 0 || world.provider.getDimension() == SpaceConfig.orbitDimension || rocket.isReusable()) {
                if (rocket.getState() == EntityRideableRocket.RocketState.LANDED || rocket.getState() == EntityRideableRocket.RocketState.AWAITING) {
                    if (rocket.navDrive != null) {
                        newStack = rocket.navDrive;
                    } else {
                        newStack.shrink(newStack.getCount());
                    }

                    rocket.navDrive = stack.copy();
                    rocket.navDrive.setCount(1);

                    if (!world.isRemote) {
                        rocket.setState(EntityRideableRocket.RocketState.AWAITING);
                    }

                    world.playSound(null, player.posX, player.posY, player.posZ, HBMSoundHandler.upgradePlug, SoundCategory.PLAYERS, 1.0F, 1.0F);
                }
            }
        }

        return new ActionResult<>(EnumActionResult.SUCCESS, newStack);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        Destination destination = getDestination(stack);
        if (destination.body == SolarSystem.Body.ORBIT)
            return EnumActionResult.FAIL;

        boolean onDestination = world.provider.getDimension() == destination.body.getDimensionId();
        if (!onDestination)
            return EnumActionResult.FAIL;

        setCoordinates(stack, pos.getX(), pos.getZ());
        setProcessed(stack, true);

        if (!world.isRemote) {
            player.sendMessage(new TextComponentString(TextFormatting.YELLOW + "" + TextFormatting.ITALIC + "Set landing coordinates to: " + pos.getX() + ", " + pos.getZ()));
        }

        return EnumActionResult.SUCCESS;
    }

    public static class Destination {

        public int x;
        public int z;
        public SolarSystem.Body body;

        public Destination(SolarSystem.Body body, int x, int z) {
            this.body = body;
            this.x = x;
            this.z = z;
        }

        public ChunkPos getChunk() {
            return new ChunkPos(x >> 4, z >> 4);
        }

    }

    public static class Target {

        public CelestialBody body;
        public boolean inOrbit;
        public boolean isValid;

        public Target(CelestialBody body, boolean inOrbit, boolean isValid) {
            this.body = body;
            this.inOrbit = inOrbit;
            this.isValid = isValid;
        }

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModel() {
        ResourceLocation reg = this.getRegistryName();
        if (reg == null) return;
        int count = SolarSystem.Body.values().length;
        mrls = new ModelResourceLocation[count];
        for (int i = 0; i < count; i++) {
            ResourceLocation loc = new ResourceLocation(reg.getNamespace(), reg.getPath() + "_" + i);
            ModelResourceLocation mrl = new ModelResourceLocation(loc, "inventory");
            mrls[i] = mrl;
            ModelLoader.setCustomModelResourceLocation(this, i, mrl);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerSprite(TextureMap map) {
        map.registerSprite(baseTex);
        for (ResourceLocation rl : overlayTex) {
            map.registerSprite(rl);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void bakeModel(ModelBakeEvent event) {
        if (mrls == null || mrls.length == 0) return;

        try {
            IModel baseModel = ModelLoaderRegistry.getModel(new ResourceLocation("minecraft", "item/generated"));

            int count = SolarSystem.Body.values().length;
            for (int i = 0; i < count; i++) {
                IModel retextured = baseModel.retexture(ImmutableMap.of(
                        "layer0", baseTex.toString(),
                        "layer1", overlayTex[i].toString()
                ));

                IBakedModel baked = retextured.bake(
                        ModelRotation.X0_Y0,
                        DefaultVertexFormats.ITEM,
                        ModelLoader.defaultTextureGetter()
                );

                event.getModelRegistry().putObject(mrls[i], baked);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
