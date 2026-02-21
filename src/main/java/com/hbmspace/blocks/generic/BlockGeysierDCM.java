package com.hbmspace.blocks.generic;

import com.google.common.collect.ImmutableMap;
import com.hbm.lib.ModDamageSource;
import com.hbm.render.block.BlockBakeFrame;
import com.hbmspace.blocks.BlockContainerBakeableSpace;
import com.hbmspace.interfaces.AutoRegister;
import com.hbmspace.main.SpaceMain;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlockGeysierDCM extends BlockContainerBakeableSpace {

    public BlockGeysierDCM(String s, String tex) {
        super(Material.ROCK, s, new BlockBakeFrame(BlockBakeFrame.BlockForm.ALL, tex));
    }

    @Override
    public @Nullable TileEntity createNewTileEntity(@NotNull World worldIn, int meta) {
        return new TileEntityDCM();
    }

    @Override
    public boolean isOpaqueCube(@NotNull IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(@NotNull IBlockState state) {
        return false;
    }
    @AutoRegister
    public static class TileEntityDCM extends TileEntity implements ITickable {

        public TileEntityDCM() {

        }

        @Override
        public void update() {
            if(!world.isRemote) {
                NBTTagCompound data = new NBTTagCompound();
                data.setDouble("posX", pos.getX() + 0.5);
                data.setDouble("posY", pos.getY() + 0.0);
                data.setDouble("posZ", pos.getZ() + 0.5);
                data.setString("type", "missileContrail");
                data.setFloat("scale", 1.5f);
                data.setDouble("moX", 0);
                data.setDouble("moY", 4);
                data.setDouble("moZ", 0);
                data.setInteger("maxAge", 100 + world.rand.nextInt(20));
                data.setInteger("color", 0xA4D7DD);
                SpaceMain.proxy.effectNT(data);

                vapor();
            }
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

        // copy pasting without reading makes you create a fluid geyser that... zaps you to death with electricty?
        private void vapor() {
            List<Entity> entities = this.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(this.pos.getX() - 0.5, this.pos.getY() + 0.5, this.pos.getZ() - 0.5, this.pos.getX() + 1.5, this.pos.getY() + 60, this.pos.getZ() + 1.5));

            if(!entities.isEmpty()) {
                for(Entity e : entities) {
                    if(e instanceof EntityLivingBase) {
                        if(e.attackEntityFrom(ModDamageSource.acid, MathHelper.clamp(((EntityLivingBase) e).getMaxHealth() * 0.1F, 3.0F, 20.0F))) {
                            world.playSound(null, e.posX, e.posY, e.posZ, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        }
                    }
                }
            }
        }

    }

    @Override
    public void bakeModel(ModelBakeEvent event) {

        try {
            IModel baseModel = ModelLoaderRegistry.getModel(new ResourceLocation(blockFrame.getBaseModel()));
            ImmutableMap.Builder<String, String> textureMap = ImmutableMap.builder();

            blockFrame.putTextures(textureMap);
            IModel retexturedModel = baseModel.retexture(textureMap.build());
            IBakedModel bakedModel = retexturedModel.bake(
                    ModelRotation.X0_Y0, DefaultVertexFormats.BLOCK, ModelLoader.defaultTextureGetter()
            );

            ModelResourceLocation modelLocation = new ModelResourceLocation(getRegistryName(), "inventory");
            event.getModelRegistry().putObject(modelLocation, bakedModel);
            ModelResourceLocation worldLocation = new ModelResourceLocation(getRegistryName(), "normal");
            event.getModelRegistry().putObject(worldLocation, bakedModel);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void registerModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this),0, new ModelResourceLocation(this.getRegistryName(), "inventory"));
    }

    @Override
    public void registerSprite(TextureMap map) {
        blockFrame.registerBlockTextures(map);
    }
}
