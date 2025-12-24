package com.hbmspace.main;

import com.hbm.handler.ThreeInts;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.util.Constants;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class ChunkLoaderManager {

    private static final String DATA_NAME = "ChunkData";

    public static void loadTicket(World world, ForgeChunkManager.Ticket ticket) {
        ChunkWorldSavedData savedData = getSavedData(world, ticket);

        for(ChunkPos chunk : savedData.chunksForcedBy.values()) {
            ForgeChunkManager.forceChunk(ticket, chunk);
        }
    }

    private static ChunkWorldSavedData getSavedData(World world) {
        return getSavedData(world, null);
    }

    private static ChunkWorldSavedData getSavedData(World world, ForgeChunkManager.Ticket ticket) {
        ChunkWorldSavedData savedData = (ChunkWorldSavedData) world.getPerWorldStorage().getOrLoadData(ChunkWorldSavedData.class, DATA_NAME);

        if(savedData == null) {
            world.getPerWorldStorage().setData(DATA_NAME, new ChunkWorldSavedData(DATA_NAME));
            savedData = (ChunkWorldSavedData) world.getPerWorldStorage().getOrLoadData(ChunkWorldSavedData.class, DATA_NAME);
        }

        if(savedData.ticket == null) {
            if(ticket == null)
                ticket = ForgeChunkManager.requestTicket(SpaceMain.instance, world, ForgeChunkManager.Type.NORMAL);

            savedData.ticket = ticket;
        }

        return savedData;
    }

    // Accepts a REGULAR position and turns it into a forced chunk, and then associated with the block position
    // Only this block position can unforce a chunk
    public static void forceChunk(World world, int x, int y, int z) {
        forceChunk(world, x, y, z, getChunkPosition(x, z));
    }

    public static void forceChunk(World world, int x, int y, int z, ChunkPos chunk) {
        ChunkWorldSavedData savedData = getSavedData(world);
        savedData.chunksForcedBy.put(new ThreeInts(x, y, z), chunk);
        savedData.markDirty();

        ForgeChunkManager.forceChunk(savedData.ticket, chunk);
    }

    public static void unforceChunk(World world, int x, int y, int z) {
        unforceChunk(world, x, y, z, getChunkPosition(x, z));
    }

    public static void unforceChunk(World world, int x, int y, int z, ChunkPos chunk) {
        ChunkWorldSavedData savedData = getSavedData(world);
        savedData.chunksForcedBy.remove(new ThreeInts(x, y, z));
        savedData.markDirty();

        if(!savedData.chunksForcedBy.containsValue(chunk)) {
            ForgeChunkManager.unforceChunk(savedData.ticket, chunk);
        }
    }

    private static ChunkPos getChunkPosition(int x, int z) {
        return new ChunkPos(x >> 4, z >> 4);
    }

    public static class ChunkWorldSavedData extends WorldSavedData {

        public ChunkWorldSavedData(String name) {
            super(name);
        }

        public Map<ThreeInts, ChunkPos> chunksForcedBy = new HashMap<>();
        public ForgeChunkManager.Ticket ticket;

        @Override
        public void readFromNBT(NBTTagCompound nbt) {
            chunksForcedBy = new HashMap<>();
            NBTTagList list = nbt.getTagList("chunks", Constants.NBT.TAG_COMPOUND);
            for(int i = 0; i < list.tagCount(); i++) {
                NBTTagCompound tag = list.getCompoundTagAt(i);

                ThreeInts coords = new ThreeInts(tag.getInteger("x"), tag.getInteger("y"), tag.getInteger("z"));
                ChunkPos chunk = new ChunkPos(tag.getInteger("cx"), tag.getInteger("cz"));

                chunksForcedBy.put(coords, chunk);
            }
        }

        @Override
        public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound nbt) {
            NBTTagList list = new NBTTagList();
            for(Map.Entry<ThreeInts, ChunkPos> entry : chunksForcedBy.entrySet()) {
                ThreeInts coords = entry.getKey();
                ChunkPos chunk = entry.getValue();

                NBTTagCompound tag = new NBTTagCompound();
                tag.setInteger("x", coords.x);
                tag.setInteger("y", coords.y);
                tag.setInteger("z", coords.z);
                tag.setInteger("cx", chunk.x);
                tag.setInteger("cz", chunk.z);

                list.appendTag(tag);
            }
            nbt.setTag("chunks", list);
            return nbt;
        }

    }

}
