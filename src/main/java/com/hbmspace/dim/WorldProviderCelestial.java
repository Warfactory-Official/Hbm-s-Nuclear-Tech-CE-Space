package com.hbmspace.dim;

import com.hbm.config.GeneralConfig;
import com.hbm.handler.ImpactWorldHandler;
import com.hbm.inventory.fluid.FluidStack;
import com.hbm.saveddata.satellites.Satellite;
import com.hbm.saveddata.satellites.SatelliteSavedData;
import com.hbmspace.dim.trait.CBT_Atmosphere;
import com.hbmspace.dim.trait.CBT_Atmosphere.FluidEntry;
import com.hbmspace.dim.trait.CBT_Destroyed;
import com.hbmspace.dim.trait.CBT_War;
import com.hbmspace.handler.atmosphere.ChunkAtmosphereManager;
import com.hbm.inventory.fluid.Fluids;
import com.hbmspace.saveddata.satellites.SatelliteWar;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public abstract class WorldProviderCelestial extends WorldProvider {
	public List<SolarSystem.AstroMetric> metrics;

	private double eclipseAmount;
	private long localTime = -1;
	
	@Override
	public abstract void init();

	// Ore gen will attempt to replace this block with ores
	public Block getStone() {
		return Blocks.STONE;
	}
	// What fluid is required to extract new bedrock ores
	public FluidStack getBedrockAcid() {
		return null;
	}
	// Should we generate bedrock ice
	public boolean hasIce() {
		return CelestialBody.getBody(world).hasIce;
	}

	public boolean hasLife() {
		return false;
	}

	public int getWaterOpacity() {
		return 3;
	}

	// Runs every tick, use it to decrement timers and run effects
	@Override
	public void updateWeather() {
		CBT_Atmosphere atmosphere = CelestialBody.getTrait(world, CBT_Atmosphere.class);
		if(atmosphere != null && atmosphere.getPressure() > 0.5F) {
			super.updateWeather();
			return;
		}

		this.world.getWorldInfo().setRainTime(0);
		this.world.getWorldInfo().setRaining(false);
		this.world.getWorldInfo().setThunderTime(0);
		this.world.getWorldInfo().setThundering(false);
		this.world.rainingStrength = 0.0F;
		this.world.thunderingStrength = 0.0F;
	}

	@Override
	public boolean hasSkyLight() { return true; }

	// Can be overridden to provide fog changing events based on weather
	public float fogDensity(EntityViewRenderEvent.FogDensity event) {
		CBT_Atmosphere atmosphere = CelestialBody.getTrait(world, CBT_Atmosphere.class);
		if(atmosphere == null) return 0;

		float pressure = (float)atmosphere.getPressure();

		if(pressure <= 2F) return 0;

		return pressure * pressure * 0.002F;
	}

	public World getWorld(){
		return this.world;
	}

	/**
	 * Read/write for weather data and anything else you wanna store that is per planet and not for every body
	 * the serialization function synchronizes weather data to the player
	 * 
	 * also we don't need to mark the WorldSavedData as dirty because the world time is updated every tick and marks it as such
	 */
	public void writeToNBT(NBTTagCompound nbt) {

	}

	public void readFromNBT(NBTTagCompound nbt) {

	}

	public void serialize(ByteBuf buf) {
		buf.writeLong(getWorldTime());
	}

	public void deserialize(ByteBuf buf) {
		long time = buf.readLong();

		// Allow a half second desync for smoothness
		if(Math.abs(time - getWorldTime()) > 10) {
			setWorldTime(time);
		}
	}


	/**
	 * Override to modify the lightmap, return true if the lightmap is actually modified
	 * @param lightmap a 16x16 lightmap stored in a 256 value buffer
	 * @return whether or not the dynamic lightmap texture needs to be updated
	 */
	public boolean updateLightmap(int[] lightmap) {
		return false;
	}

	protected final int packColor(final int[] colors) {
		return packColor(colors[0], colors[1], colors[2]);
	}

	protected final int packColor(final int r, final int g, final int b) {
		return 255 << 24 | r << 16 | g << 8 | b;
	}

	protected final int[] unpackColor(final int color) {
		final int[] colors = new int[3];
		colors[0] = color >> 16 & 255;
		colors[1] = color >> 8 & 255;
		colors[2] = color & 255;
		return colors;
	}

	// This is called once, at the beginning of every frame
	// so we use this to memoise expensive calcs
	@SideOnly(Side.CLIENT)
	protected void updateSky(float partialTicks) {
		CelestialBody body = CelestialBody.getBody(world);

		// First fetch the suns true size
		double sunSize = SolarSystem.calculateSunSize(body);
		float solarAngle = world.getCelestialAngle(partialTicks);

		// Get our orrery of bodies, this is cached for reuse in sky rendering
		metrics = SolarSystem.calculateMetricsFromBody(world, partialTicks, body, solarAngle);

		// Get our eclipse amount
		eclipseAmount = getEclipseFactor(metrics, sunSize, SolarSystem.MAX_APPARENT_SIZE_SURFACE);
	}

	public static double getEclipseFactor(List<SolarSystem.AstroMetric> metrics, double sunSize, double maxSize) {
		double factor = 0;
		double sunArc = getArc(sunSize);

		// Calculate eclipse
		for(SolarSystem.AstroMetric metric : metrics) {
			if(metric.apparentSize < 1) continue;

			double planetArc = getArc(MathHelper.clamp(metric.apparentSize, 0, maxSize));
			double minPhase = 1 - (planetArc + sunArc);
			double maxPhase = 1 - (planetArc - sunArc);
			if(metric.phaseObscure < minPhase) continue;

			double thisFactor = 1 - (metric.phaseObscure - maxPhase) / (minPhase - maxPhase);

			factor = Math.min(Math.max(factor, thisFactor), 1.0);
		}

		return factor;
	}

	// due to rendering, the arc is not exactly 1deg = 1deg, this converts from apparentSize to 0-1
	// note that we are rendering flat quads, so the arc size is warped more the larger you get!
	private static double getArc(double apparentSize) {
		return apparentSize * 0.0017D + Math.sqrt(apparentSize * 0.00003D);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public @NotNull Vec3d getFogColor(float celestialAngle, float y) {
		CBT_Atmosphere atmosphere = CelestialBody.getTrait(world, CBT_Atmosphere.class);

		// The cold hard vacuum of space
		if(atmosphere == null) return new Vec3d(0, 0, 0);
		
		float sun = MathHelper.clamp(MathHelper.cos(celestialAngle * (float)Math.PI * 2.0F) * 2.0F + 0.5F, 0.0F, 1.0F);

		float sunR = sun;
		float sunG = sun;
		float sunB = sun;

		if(!GeneralConfig.enableHardcoreDarkness) {
			sunR *= 0.94F;
			sunG *= 0.94F;
			sunB *= 0.91F;
		}

		float totalPressure = (float)atmosphere.getPressure();
		Vec3d color = new Vec3d(0, 0, 0);

		for(int i = 0; i < atmosphere.fluids.size(); i++) {
			FluidEntry entry = atmosphere.fluids.get(i);
			Vec3d fluidColor;

			if(entry.fluid == com.hbmspace.inventory.fluid.Fluids.EVEAIR) {
				fluidColor = new Vec3d(53F / 255F * sunR, 32F / 255F * sunG, 74F / 255F * sunB);
			} else if(entry.fluid == com.hbmspace.inventory.fluid.Fluids.DUNAAIR || entry.fluid == Fluids.CARBONDIOXIDE) {
				fluidColor = new Vec3d(212F / 255F * sunR, 112F / 255F * sunG, 78F / 255F * sunB);
			} else if(entry.fluid == com.hbmspace.inventory.fluid.Fluids.EARTHAIR || entry.fluid == Fluids.OXYGEN || entry.fluid == com.hbmspace.inventory.fluid.Fluids.NITROGEN) {
				// Default to regular ol' overworld
				fluidColor = new Vec3d(0.7529412F * sunR, 0.84705883F * sunG, sunB);
			} else {
				fluidColor = getColorFromHex(entry.fluid.getColor());
				fluidColor = new Vec3d(fluidColor.x * sunR * 1.4F, fluidColor.y * sunG * 1.4F, fluidColor.z * sunB * 1.4F);
			}

			float percentage = (float)entry.pressure / totalPressure;
			color = new Vec3d(
				color.x + fluidColor.x * percentage,
				color.y + fluidColor.y * percentage,
				color.z + fluidColor.z * percentage
			);
		}

		// Add minimum fog colour, for night-time glow
		if(!GeneralConfig.enableHardcoreDarkness) {
			float nightDensity = MathHelper.clamp(totalPressure, 0.0F, 1.0F);
			color = color.add(0.06F * nightDensity, 0.06F * nightDensity, 0.09F * nightDensity);
		}

		// Fog intensity remains high to simulate a thin looking atmosphere on low pressure planets
		float pressureFactor = MathHelper.clamp(totalPressure * 10.0F, 0.0F, 1.0F);
		color = color.scale(pressureFactor);
		if(Minecraft.getMinecraft().getRenderViewEntity() != null) {
			if(Minecraft.getMinecraft().getRenderViewEntity().posY > 600) {
				double curvature = MathHelper.clamp((1000.0F - (float)Minecraft.getMinecraft().getRenderViewEntity().posY) / 400.0F, 0.0F, 1.0F);
				color = color.scale(curvature);
			}
		}

		if (eclipseAmount > 0) {
			color = color.scale(1 - eclipseAmount * 0.3);

			float[] sunsetFog = calcSunriseSunsetColors(0.25F, 0);
			if (sunsetFog != null) {
				double sunsetAmount = MathHelper.clamp(eclipseAmount * 0.5 - (1 - sun), 0.0, 1.0);
				color = new Vec3d(
						color.x * (1.0F - sunsetAmount) + sunsetFog[0] * sunsetAmount,
						color.y * (1.0F - sunsetAmount) + sunsetFog[1] * sunsetAmount,
						color.z * (1.0F - sunsetAmount) + sunsetFog[2] * sunsetAmount
				);
			}
		}

		float dust = ImpactWorldHandler.getDustForClient(world);
		float fire = ImpactWorldHandler.getFireForClient(world);

		double cX = color.x;
		double cY = color.y * (1 - (dust * 0.5F));
		double cZ = color.z * (1 - dust);

		if (fire > 0) {
			double fireFactor = Math.max((1 - (dust * 2)), 0);
			cX *= fireFactor;
			cY *= fireFactor;
			cZ *= fireFactor;
		} else {
			double dustFactor = 1 - dust;
			cX *= dustFactor;
			cY *= dustFactor;
			cZ *= dustFactor;
		}

		color = new Vec3d(cX, cY, cZ);

		
		return color;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public @NotNull Vec3d getSkyColor(@NotNull Entity camera, float partialTicks) {
		// getSkyColor is called first on every frame, so if you want to memoise anything, do it here
		updateSky(partialTicks);

		CBT_Atmosphere atmosphere = CelestialBody.getTrait(world, CBT_Atmosphere.class);

		double cX = 0;
		double cY = 0;
		double cZ = 0;

		for (Map.Entry<Integer, Satellite> entry : SatelliteSavedData.getClientSats().entrySet()) {
			if (entry.getValue() instanceof SatelliteWar war) {
				float flame = war.getInterp();
				float alpd = 1.0F - Math.min(1.0F, flame / 100);

				cX += alpd * 1.5;
				cY += alpd * 1.5;
				cZ += alpd * 1.5;
			}
		}

		// The cold hard vacuum of space
		if (atmosphere == null) {
			return new Vec3d(cX, cY, cZ);
		}

		float sun = world.getSunBrightnessFactor(1.0F);
		float totalPressure = (float) atmosphere.getPressure();

		for (int i = 0; i < atmosphere.fluids.size(); i++) {
			FluidEntry entry = atmosphere.fluids.get(i);
			Vec3d fluidColor;

			if (entry.fluid == com.hbmspace.inventory.fluid.Fluids.EVEAIR) {
				fluidColor = new Vec3d(53F / 255F * sun, 32F / 255F * sun, 74F / 255F * sun);
			} else if (entry.fluid == com.hbmspace.inventory.fluid.Fluids.DUNAAIR || entry.fluid == Fluids.CARBONDIOXIDE) {
				fluidColor = new Vec3d(212F / 255F * sun, 112F / 255F * sun, 78F / 255F * sun);
			} else if (entry.fluid == com.hbmspace.inventory.fluid.Fluids.EARTHAIR || entry.fluid == Fluids.OXYGEN || entry.fluid == com.hbmspace.inventory.fluid.Fluids.NITROGEN) {
				// Default to regular ol' overworld
				fluidColor = super.getSkyColor(camera, partialTicks);
			} else {
				fluidColor = getColorFromHex(entry.fluid.getColor());
				fluidColor = new Vec3d(fluidColor.x * sun, fluidColor.y * sun, fluidColor.z * sun);
			}

			float percentage = (float) entry.pressure / totalPressure;

			cX += fluidColor.x * percentage;
			cY += fluidColor.y * percentage;
			cZ += fluidColor.z * percentage;
		}

		if (CelestialBody.getBody(world).hasTrait(CBT_War.class)) {
			CBT_War wardat = CelestialBody.getTrait(world, CBT_War.class);
			for (int i = 0; i < wardat.getProjectiles().size(); i++) {
				CBT_War.Projectile projectile = wardat.getProjectiles().get(i);
				float flash = projectile.getFlashtime();
				if (projectile.getAnimtime() > 0) {
					float invertedFlash = 100 - flash;

					cX += invertedFlash * 0.5;
					cY += invertedFlash * 0.5;
					cZ += invertedFlash * 0.5;
				}
			}
		}

		for (Map.Entry<Integer, Satellite> entry : SatelliteSavedData.getClientSats().entrySet()) {
			if (entry.getValue() instanceof SatelliteWar war) {
				float flame = war.getInterp();
				float alpd = 1.0F - Math.min(1.0F, flame / 100);

				cX += alpd * 1.5;
				cY += alpd * 1.5;
				cZ += alpd * 1.5;
			}
		}

		// Lower pressure sky renders thinner
		float pressureFactor = MathHelper.clamp(totalPressure, 0.0F, 1.0F);
		cX *= pressureFactor;
		cY *= pressureFactor;
		cZ *= pressureFactor;

		if (eclipseAmount > 0) {
			cX *= 1 - eclipseAmount * 0.6;
			cY *= 1 - eclipseAmount * 0.6;
			cZ *= 1 - eclipseAmount * 0.5;
		}

		float dust = ImpactWorldHandler.getDustForClient(world);
		float fire = ImpactWorldHandler.getFireForClient(world);

		if (dust > 0) {
			if (fire > 0) {
				cX *= 1.3;
				cY *= Math.max((1 - (dust * 1.4f)), 0);
            } else {
				cY *= 1 - (dust * 0.5F);
            }
            cZ *= Math.max((1 - (dust * 4)), 0);

            double commonFactor = fire + (1 - dust);
			cX *= commonFactor;
			cY *= commonFactor;
			cZ *= commonFactor;
		}

		return new Vec3d(cX, cY, cZ);
	}

	private Vec3d getColorFromHex(int hexColor) {
		float red = ((hexColor >> 16) & 0xFF) / 255.0F;
		float green = ((hexColor >> 8) & 0xFF) / 255.0F;
		float blue = (hexColor & 0xFF) / 255.0F;
		return new Vec3d(red, green, blue);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float[] calcSunriseSunsetColors(float solarAngle, float partialTicks) {
		CBT_Atmosphere atmosphere = CelestialBody.getTrait(world, CBT_Atmosphere.class);
		if(atmosphere == null || atmosphere.getPressure() < 0.05F) return null;

		float[] colors = super.calcSunriseSunsetColors(solarAngle, partialTicks);
		if(colors == null) return null;
		
		// Mars IRL has inverted blue sunsets, which look cool as
		// So carbon dioxide rich atmospheres will do the same
		// for now, it's just a swizzle between red and blue
		if(atmosphere.hasFluid(com.hbmspace.inventory.fluid.Fluids.DUNAAIR) || atmosphere.hasFluid(Fluids.CARBONDIOXIDE)) {
			float tmp = colors[0];
			colors[0] = colors[2];
			colors[2] = tmp;
		} else if (atmosphere.hasFluid(com.hbmspace.inventory.fluid.Fluids.EVEAIR)) {
			float f2 = 0.4F;
			float f3 = MathHelper.cos((solarAngle) * (float)Math.PI * 2.0F) - 0.0F;
			float f4 = -0.0F;
	
			if (f3 >= f4 - f2 && f3 <= f4 + f2) {
				float f5 = (f3 - f4) / f2 * 0.5F + 0.5F;
				float f6 = 1.0F - (1.0F - MathHelper.sin(f5 * (float)Math.PI)) * 0.99F;
				f6 *= f6;
				colors[0] = f5 * 0.01F;
				colors[1] = f5 * f5 * 0.9F + 0.3F;
				colors[2] = f5 * f5;
				colors[3] = f6;
			}
		} else if( atmosphere.hasFluid(com.hbmspace.inventory.fluid.Fluids.TEKTOAIR) ||  atmosphere.hasFluid(com.hbmspace.inventory.fluid.Fluids.JOOLGAS) || atmosphere.hasFluid(Fluids.CHLORINE)) {
			float tmp = colors[1];
			colors[1] = colors[2];
			colors[2] = tmp;
		}

		float dustFactor = 1 - ImpactWorldHandler.getDustForClient(world);
		colors[0] *= dustFactor;
		colors[1] *= dustFactor;
		colors[2] *= dustFactor;
		colors[3] *= dustFactor;

		return colors;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public @NotNull Vec3d getCloudColor(float partialTicks) {
		return super.getCloudColor(partialTicks);
	}

	@Override
	public boolean canDoLightning(@NotNull Chunk chunk) {
		CBT_Atmosphere atmosphere = CelestialBody.getTrait(world, CBT_Atmosphere.class);

		if(atmosphere != null && atmosphere.getPressure() > 0.2)
			return super.canDoLightning(chunk);

		return false;
	}

	@Override
	public boolean canDoRainSnowIce(@NotNull Chunk chunk) {
		CBT_Atmosphere atmosphere = CelestialBody.getTrait(world, CBT_Atmosphere.class);

		if(atmosphere != null && atmosphere.getPressure() > 0.2)
			return super.canDoRainSnowIce(chunk);

		return false;
	}

	// Stars do not show up during the day in a vacuum, common misconception:
	// The reason stars aren't visible during the day on Earth isn't because of the sky,
	// the sky is ALWAYS there. The reason they aren't visible is because the Sun is too bright!
	@Override
	@SideOnly(Side.CLIENT)
	public float getStarBrightness(float par1) {
		// Stars become visible during the day beyond the orbit of Duna
		// And are fully visible during the day beyond the orbit of Jool
		float distanceStart = 20_000_000;
		float distanceEnd = 80_000_000;

		float semiMajorAxisKm = CelestialBody.getPlanet(world).semiMajorAxisKm;
		float distanceFactor = MathHelper.clamp((semiMajorAxisKm - distanceStart) / (distanceEnd - distanceStart), 0F, 1F);

		float starBrightness = super.getStarBrightness(par1);

		float dust = ImpactWorldHandler.getDustForClient(world);

		return MathHelper.clamp(starBrightness, distanceFactor, 1F) * (1 - dust);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getSunBrightness(float par1) {
		if(CelestialBody.getStar(world).hasTrait(CBT_Destroyed.class))
			return 0;

		CBT_Atmosphere atmosphere = CelestialBody.getTrait(world, CBT_Atmosphere.class);

		float sunBrightness = super.getSunBrightness(par1);

		sunBrightness *= (float) (1 - eclipseAmount * 0.6);

		float dust = ImpactWorldHandler.getDustForClient(world);
		sunBrightness *= (1 - dust);


		// brightness _inside_ of the atmosphere, from effects like lightning or war weapons
		float insideBrightness = 0;

		for(Map.Entry<Integer, Satellite> entry : SatelliteSavedData.getClientSats().entrySet()) {
			if (entry instanceof SatelliteWar) {
				SatelliteWar war = (SatelliteWar) entry.getValue();
				float flame = war.getInterp();
				float alpd = 1.0F - Math.min(1.0F, flame / 100);
				insideBrightness += alpd;
			}
		}

		if(CelestialBody.getBody(world).hasTrait(CBT_War.class)) {
			CBT_War wardat = CelestialBody.getTrait(world, CBT_War.class);
			for (int i = 0; i < wardat.getProjectiles().size(); i++) {
				CBT_War.Projectile projectile = wardat.getProjectiles().get(i);
				float flash = projectile.getFlashtime();
				if(projectile.getAnimtime() > 0) {
					insideBrightness += 100 - flash;
				}
			}
		}


		if(atmosphere == null) {
			return sunBrightness + insideBrightness;
		}

		return sunBrightness * MathHelper.clamp(1.0F - ((float) atmosphere.getPressure() - 1.5F) * 0.2F, 0.25F, 1.0F) + insideBrightness;
	}

	public float[] getSunColor() {
		CBT_Atmosphere atmosphere = CelestialBody.getTrait(world, CBT_Atmosphere.class);

		if(atmosphere == null) return new float[] { 1.0F, 1.0F, 1.0F };

		float[] sunColor = { 1.0F, 1.0F, 1.0F };

		// Adjust the sun colour based on atmospheric composition
		for(FluidEntry entry : atmosphere.fluids) {
			// Chlorines all redden the sun by absorbing blue and green
			if(entry.fluid == com.hbmspace.inventory.fluid.Fluids.TEKTOAIR
					|| entry.fluid == Fluids.CHLORINE
					|| entry.fluid == com.hbmspace.inventory.fluid.Fluids.CHLOROMETHANE
					|| entry.fluid == Fluids.RADIOSOLVENT
					|| entry.fluid == com.hbmspace.inventory.fluid.Fluids.CCL) {
				float absorption = MathHelper.clamp(1.0F - (float)entry.pressure * 0.5F, 0.0F, 1.0F);
				sunColor[1] *= absorption;
				sunColor[2] *= absorption;
			}
		}

		return sunColor;
	}

	@Override
	public int getRespawnDimension(EntityPlayerMP player) {
		BlockPos coords = player.getBedLocation(getDimension());

		// If no bed, respawn in overworld
		if(coords == null)
			return 0;

		// If the bed location has no breathable atmosphere, respawn in overworld
		CBT_Atmosphere atmosphere = ChunkAtmosphereManager.proxy.getAtmosphere(world, coords.getX(), coords.getY(), coords.getZ());
		if(!ChunkAtmosphereManager.proxy.canBreathe(atmosphere))
			return 0;

		return getDimension();
	}

	// We want spawning to check for breathable, and getRespawnDimension() only runs if this is FALSE
	// BUT this also makes beds blow up (Mojang I swear), so we hook into the sleep event and set a flag
	public static boolean attemptingSleep = false;

	@Override
	public boolean canRespawnHere() {
		if(attemptingSleep) {
			attemptingSleep = false;
			return true;
		}

		return false;
	}

	// Another AWFULLY named deobfuscation function, this one is called when players have all slept,
	// which means we can set the time of day to local morning safely here!
	@Override
	public void resetRainAndThunder() {
		super.resetRainAndThunder();

		if(getDimension() == 0) return;
		if(!world.getGameRules().getBoolean("doDaylightCycle")) return;

		long dayLength = (long)getDayLength();
		long i = getWorldTime() % dayLength;
		setWorldTime(i - i % dayLength);
	}

	@Override
	public long getWorldTime() {
		if(getDimension() == 0) {
			return super.getWorldTime();
		}

		if(!world.isRemote) {
			localTime = CelestialBodyWorldSavedData.get(this).getLocalTime();
		}

		return localTime;
	}

	@Override
	public void setWorldTime(long time) {
		if(getDimension() == 0) {
			super.setWorldTime(time);
			return;
		}

		if(!world.isRemote) {
			CelestialBodyWorldSavedData.get(this).setLocalTime(time);
		}

		localTime = time;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public float getCloudHeight() {
		CBT_Atmosphere atmosphere = CelestialBody.getTrait(world, CBT_Atmosphere.class);

		if(atmosphere == null || atmosphere.getPressure() < 0.5F) return -99999;
		
		return super.getCloudHeight();
	}

	private IRenderHandler skyProvider;

	@Override
	@SideOnly(Side.CLIENT)
	public IRenderHandler getSkyRenderer() {
		// I do not condone this because it WILL confuse your players, but if you absolutely must,
		// you can uncomment this line below in your fork to get default skybox rendering on Earth.

		// mlbv: nah ill do it anyway
		if(!GeneralConfig.enableSkyboxes && this.getDimensionType() == DimensionType.OVERWORLD)
			return super.getSkyRenderer();
		
		// Make sure you also uncomment the relevant line in getMoonPhase below too.

		// This is not in a config because it is not a decision you should make lightly, as it will break:
		//  * certain atmosphere/terraforming modifications
		//  * Dyson swarm rendering
		//  * seeing weapons platforms in orbit (the big cannon from the trailer will NOT be visible)
		//  * weapon effects on the atmosphere (burning holes in the atmosphere, hitting planetary defense shields)
		//  * accurate celestial body rendering (you won't be able to see ANY other planets)
		//     * this also breaks future plans to modify orbits via huge mass drivers, if someone decides to yeet the moon at you, you won't know
		//  * sun extinction/modification events (the sun will appear normal even if it has been turned into a black hole)
		//  * player launched satellites won't be visible
		//  * artificial moons/rings (once implemented) won't be visible
		if(skyProvider == null) {
			skyProvider = new SkyProviderCelestial();
		}
		return skyProvider;
	}

	protected double getDayLength() {
		CelestialBody body = CelestialBody.getBody(world);
		return body.getRotationalPeriod() / (1 - (1 / body.getPlanet().getOrbitalPeriod()));
	}

	// This calculates SOLAR angle, not sidereal/celestial!
	@Override
	public float calculateCelestialAngle(long worldTime, float partialTicks) {
		worldTime = getWorldTime(); // the worldtime passed in is from the fucking overworld
		double dayLength = getDayLength();
		double j = worldTime % dayLength;
		double f1 = (j + partialTicks) / dayLength - 0.25F;

		if(f1 < 0.0F) {
			++f1;
		}

		if(f1 > 1.0F) {
			--f1;
		}

		double f2 = f1;
		f1 = 0.5F - Math.cos(f1 * Math.PI) / 2.0F;
		return (float)(f2 + (f1 - f2) / 3.0D);
	}

	@Override
	public int getMoonPhase(long worldTime) {
		// Uncomment this line as well to return moon phase difficulty calcs to vanilla
		if(!GeneralConfig.enableSkyboxes && getDimensionType() == DimensionType.OVERWORLD)
			return super.getMoonPhase(worldTime);

		CelestialBody body = CelestialBody.getBody(world);

		// if no moons, default to half-moon difficulty
		if(body.satellites.isEmpty()) return 2;

		// Determine difficulty phase from closest moon
		int phase = Math.round(8 - ((float)SolarSystem.calculateSingleAngle(world, body, body.satellites.getFirst()) / 45 + 4));
		if(phase >= 8) return 0;
		return phase;
	}

	public boolean isEclipse() {
		CelestialBody body = CelestialBody.getBody(world);

		// First fetch the suns true size
		double sunSize = SolarSystem.calculateSunSize(body);
		float solarAngle = world.getCelestialAngle(0);

		// Get our orrery of bodies, this is cached for reuse in sky rendering
		metrics = SolarSystem.calculateMetricsFromBody(world, 0, body, solarAngle);

		// Get our eclipse amount
		return getEclipseFactor(metrics, sunSize, SolarSystem.MAX_APPARENT_SIZE_SURFACE) > 0.0;
	}

	@Override
	public double getHorizon() {
		if(getDimension() == 0) return super.getHorizon();
		return 63;
	}

	// This is the vanilla junk table, for replacing fish on dead worlds
	//private static ArrayList<WeightedRandomFishable> junk;

	// you know what that means
	/// FISH ///

	// returning null from any of these methods will revert to overworld loot tables
	/*public ArrayList<WeightedRandomFishable> getFish() {
		if(junk == null) {
			junk = new ArrayList<>();
			junk.add((new WeightedRandomFishable(new ItemStack(Items.leather_boots), 10)).func_150709_a(0.9F));
			junk.add(new WeightedRandomFishable(new ItemStack(Items.leather), 10));
			junk.add(new WeightedRandomFishable(new ItemStack(Items.bone), 10));
			junk.add(new WeightedRandomFishable(new ItemStack(Items.potionitem), 10));
			junk.add(new WeightedRandomFishable(new ItemStack(Items.string), 5));
			junk.add((new WeightedRandomFishable(new ItemStack(Items.fishing_rod), 2)).func_150709_a(0.9F));
			junk.add(new WeightedRandomFishable(new ItemStack(Items.bowl), 10));
			junk.add(new WeightedRandomFishable(new ItemStack(Items.stick), 5));
			junk.add(new WeightedRandomFishable(new ItemStack(Items.dye, 10, 0), 1));
			junk.add(new WeightedRandomFishable(new ItemStack(Blocks.tripwire_hook), 10));
			junk.add(new WeightedRandomFishable(new ItemStack(Items.rotten_flesh), 10));
		}

		return junk;
	}

	public ArrayList<WeightedRandomFishable> getJunk() {
		return null;
	}

	public ArrayList<WeightedRandomFishable> getTreasure() {
		return null;
	}*/
	/// FISH ///

}