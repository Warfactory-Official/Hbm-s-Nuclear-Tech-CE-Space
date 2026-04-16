package com.hbmspace.dim.trait;

import com.hbm.entity.mob.EntityUFO;
import com.hbm.entity.mob.glyphid.*;
import com.hbm.entity.siege.SiegeTier;
import com.hbm.main.MainRegistry;
import com.hbmspace.dim.CelestialBody;
import com.hbmspace.entity.missile.EntityCombatDropPod;
import com.hbmspace.entity.mob.siege.EntitySiegeCraft;
import com.hbmspace.entity.mob.siege.EntitySiegeUFO;
import com.hbmspace.lib.HBMSpaceSoundHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraftforge.common.DimensionManager;

import java.util.HashMap;
import java.util.Random;

public class CBT_Invasion extends CelestialBodyTrait {

    private final BossInfoServer bossInfo = (BossInfoServer) new BossInfoServer(
            new TextComponentString("Invasion"), BossInfo.Color.RED, BossInfo.Overlay.PROGRESS
    ).setDarkenSky(true);

    public int wave;
    public int kills;
    public int killreq;
    public double waveTime;
    public boolean isInvading;
    public int lastSpawns;
    public int spawndelay;
    public int podBurstCounter = 0;
    public int podCooldown = 0;
    public boolean bossSpawned = false;
    public boolean warningPlayed;

    public CBT_Invasion() {
    }

    public CBT_Invasion(int wave, double waveTime, boolean isInvading) {
        this.wave = wave;
        this.waveTime = waveTime;
        this.isInvading = isInvading;
    }

    public void prepare() {
        if (!isInvading && waveTime >= 0) {
            waveTime--;
            warningPlayed = !(waveTime <= 5);
            if (waveTime <= 0) {
                isInvading = true;
            }
        }
    }

    @Override
    public void update(boolean isRemote, CelestialBody body) {
        if (!isRemote) {
            prepare();

            if (isInvading) {
                World world = DimensionManager.getWorld(body.dimensionId);
                if (world == null || world.playerEntities.isEmpty()) return;

                logicTick(world);
                handleBurstSpawning(world);
                spawnAttempt(world);

                // Update boss bar
                bossInfo.setPercent(getHealth() / getMaxHealth());
            }
        } else {
            if (!isInvading && !warningPlayed) {
                warningPlayed = true;
                MainRegistry.proxy.me().playSound(HBMSpaceSoundHandler.alertPing, 10F, 1F);
                MainRegistry.proxy.me().sendMessage(
                        new TextComponentString("Incoming Invasion!").setStyle(new Style().setColor(TextFormatting.RED))
                );
            }
        }
    }

    public void spawnCattle(World world) {
        if (world.playerEntities.isEmpty()) return;

        EntityPlayer player = world.playerEntities.get(world.rand.nextInt(world.playerEntities.size()));

        if (!(player instanceof EntityPlayerMP)) return;
        if (player.posY < 50 && player.world.getWorldInfo().getTerrainType() != WorldType.FLAT) return;

        Random rand = world.rand;

        EntityCombatDropPod pod = new EntityCombatDropPod(world);
        pod.posX = player.posX + (rand.nextGaussian() * 15);
        pod.posY = 250;
        pod.posZ = player.posZ + (rand.nextGaussian() * 15);
        pod.motionY = -1.5;

        EntityGlyphid glyph;
        int amount = 1;

        if (wave == 1) {
            amount = 2;
            glyph = new EntityGlyphid(world);
        } else if (wave == 2) {
            int roll = rand.nextInt(3);
            amount = Math.max(1, rand.nextInt(4));
            glyph = switch (roll) {
                case 0 -> new EntityGlyphid(world);
                case 1 -> new EntityGlyphidBrawler(world);
                default -> new EntityGlyphidDigger(world);
            };
        } else if (wave >= 3) {
            int roll = rand.nextInt(5);
            amount = Math.max(1, rand.nextInt(6));
            switch (roll) {
                case 0: glyph = new EntityGlyphid(world); break;
                case 1: glyph = new EntityGlyphidBrawler(world); break;
                case 2: glyph = new EntityGlyphidDigger(world); break;
                case 3: glyph = new EntityGlyphidBlaster(world); break;
                default: glyph = new EntityGlyphidBehemoth(world); amount = 1; break;
            }
        } else {
            glyph = new EntityGlyphid(world);
        }

        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("id", EntityList.getEntityString(glyph));
        glyph.writeToNBT(nbt);

        pod.setPayload(nbt, amount, 2);
        world.spawnEntity(pod);
    }

    private void handleBurstSpawning(World world) {
        if (wave > 3) return;

        if (podCooldown > 0) {
            podCooldown--;
            return;
        }

        if (world.getTotalWorldTime() % 10 + world.rand.nextInt(3) == 0) {
            spawnCattle(world);
            podBurstCounter++;
            if (podBurstCounter >= 3 + (wave - 1)) {
                podBurstCounter = 0;
                podCooldown = 500;
            }
        }
    }

    private void logicTick(World world) {
        if (!isInvading) return;

        switch (wave) {
            case 0:
                advanceWave(world);
                break;
            case 1:
                killreq = 20;
                if (kills >= killreq) advanceWave(world);
                break;
            case 2:
                killreq = 100;
                if (kills >= killreq) advanceWave(world);
                break;
            case 3:
                killreq = 150;
                if (kills >= killreq) advanceWave(world);
                break;
            case 4:
                killreq = 1;
                if (!bossSpawned) {
                    spawnBoss(world);
                    bossSpawned = true;
                }
                break;
        }
    }

    private void advanceWave(World world) {
        wave++;
        kills = 0;
        broadcast(world, "Wave " + (wave == 4 ? "FINAL" : wave) + " is starting!", TextFormatting.GOLD);
    }

    public void spawnAttempt(World world) {
        if (wave > 3) return;

        int timer = wave == 2 ? 100 : wave == 3 ? 80 : 200;

        if (world.getTotalWorldTime() % timer == 0) {
            EntityPlayer player = world.playerEntities.get(world.rand.nextInt(world.playerEntities.size()));

            if (player.posY < 50 && player.world.getWorldInfo().getTerrainType() != WorldType.FLAT)
                return;

            double spawnX = player.posX + world.rand.nextGaussian() * 30;
            double spawnZ = player.posZ + world.rand.nextGaussian() * 30;
            double spawnY = player.posY + 30 + world.rand.nextInt(20);

            float bigUfoChance = wave == 2 ? 0.1F : wave == 3 ? 0.2F : 0.0F;
            float waveFactor = Math.min(wave * 0.05F, 0.2F);

            if (world.rand.nextFloat() < bigUfoChance) {
                EntitySiegeCraft bigUfo = new EntitySiegeCraft(world);
                bigUfo.setLocationAndAngles(spawnX, spawnY, spawnZ, world.rand.nextFloat() * 360.0F, 0.0F);
                bigUfo.setTier(world.rand.nextFloat() < 0.5F - waveFactor ? SiegeTier.STONE : SiegeTier.IRON);
                world.spawnEntity(bigUfo);
            } else {
                EntitySiegeUFO smallUfo = new EntitySiegeUFO(world);
                smallUfo.setLocationAndAngles(spawnX, spawnY, spawnZ, world.rand.nextFloat() * 360.0F, 0.0F);

                float roll = world.rand.nextFloat();
                SiegeTier chosen;
                if (roll < 0.15F - (waveFactor * 1.5F)) chosen = SiegeTier.CLAY;
                else if (roll < 0.35F - (waveFactor * 1.2F)) chosen = SiegeTier.STONE;
                else if (roll < 0.50F - waveFactor) chosen = SiegeTier.IRON;
                else if (roll < 0.65F - (waveFactor * 0.8F)) chosen = SiegeTier.SILVER;
                else if (roll < 0.80F - (waveFactor * 0.5F)) chosen = SiegeTier.GOLD;
                else if (roll < 0.90F - (waveFactor * 0.2F)) chosen = SiegeTier.DESH;
                else if (roll < 0.97F) chosen = SiegeTier.SCHRAB;
                else chosen = SiegeTier.DNT;

                smallUfo.setTier(chosen);
                world.spawnEntity(smallUfo);
            }

            lastSpawns++;
        }
    }

    public void onKill(EntityLivingBase entity, CelestialBody body) {
        if (entity instanceof EntitySiegeUFO) {
            int value = entity.getMaxHealth() >= 25 ? 2 : 1;
            kills += value;
            body.modifyTraits(this);
        } else if (entity instanceof EntitySiegeCraft) {
            kills += 10;
            body.modifyTraits(this);
        }

        if (wave >= 4 && entity instanceof EntityUFO) {
            HashMap<Class<? extends CelestialBodyTrait>, CelestialBodyTrait> currentTraits = body.getTraits();
            currentTraits.remove(CBT_Invasion.class);

            for (EntityPlayer player : entity.world.playerEntities) {
                player.sendMessage(
                        new TextComponentString("The Invasion Is Over!").setStyle(new Style().setColor(TextFormatting.YELLOW))
                );
            }

            body.setTraits(currentTraits);
        }
    }

    private void spawnBoss(World world) {
        if (world.playerEntities.isEmpty()) return;

        EntityPlayer player = world.playerEntities.get(world.rand.nextInt(world.playerEntities.size()));
        EntityUFO entity = new EntityUFO(world);
        entity.scanCooldown = 100;

        entity.setLocationAndAngles(player.posX, player.posY + 50, player.posZ,
                MathHelper.wrapDegrees(world.rand.nextFloat() * 360.0F), 0.0F);

        entity.rotationYawHead = entity.rotationYaw;
        entity.renderYawOffset = entity.rotationYaw;
        entity.onInitialSpawn(world.getDifficultyForLocation(entity.getPosition()), null);
        world.spawnEntity(entity);
    }

    private void broadcast(World world, String text, TextFormatting color) {
        TextComponentString message = new TextComponentString(text);
        message.setStyle(new Style().setColor(color).setBold(true));

        for (EntityPlayer p : world.playerEntities) {
            if (p instanceof EntityPlayer) {
                p.sendMessage(message);
            }
        }
    }

    // ====================== NBT / NETWORK ======================

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setInteger("wave", wave);
        nbt.setInteger("kills", kills);
        nbt.setInteger("killreq", killreq);
        nbt.setDouble("waveTime", waveTime);
        nbt.setBoolean("isInvading", isInvading);
        nbt.setBoolean("warningPlayed", warningPlayed);
        nbt.setInteger("podBurst", podBurstCounter);
        nbt.setInteger("podCooldown", podCooldown);
        nbt.setBoolean("bossSpawned", bossSpawned);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        wave = nbt.getInteger("wave");
        kills = nbt.getInteger("kills");
        killreq = nbt.getInteger("killreq");
        waveTime = nbt.getDouble("waveTime");
        isInvading = nbt.getBoolean("isInvading");
        warningPlayed = nbt.getBoolean("warningPlayed");
        podBurstCounter = nbt.getInteger("podBurst");
        podCooldown = nbt.getInteger("podCooldown");
        bossSpawned = nbt.getBoolean("bossSpawned");
    }

    @Override
    public void writeToBytes(ByteBuf buf) {
        buf.writeInt(wave);
        buf.writeInt(kills);
        buf.writeInt(killreq);
        buf.writeDouble(waveTime);
        buf.writeBoolean(isInvading);
        buf.writeBoolean(warningPlayed);
        buf.writeInt(podBurstCounter);
        buf.writeInt(podCooldown);
        buf.writeBoolean(bossSpawned);
    }

    @Override
    public void readFromBytes(ByteBuf buf) {
        wave = buf.readInt();
        kills = buf.readInt();
        killreq = buf.readInt();
        waveTime = buf.readDouble();
        isInvading = buf.readBoolean();
        warningPlayed = buf.readBoolean();
        podBurstCounter = buf.readInt();
        podCooldown = buf.readInt();
        bossSpawned = buf.readBoolean();
    }

    public float getMaxHealth() {
        return killreq;
    }

    public float getHealth() {
        return killreq - kills;
    }

    public TextComponentString getDisplayName() {
        return new TextComponentString("Wave " + (wave == 4 ? "FINAL" : wave));
    }

    public BossInfoServer getBossInfo() {
        bossInfo.setName(getDisplayName());
        bossInfo.setPercent(getHealth() / Math.max(1, getMaxHealth()));
        return bossInfo;
    }
}