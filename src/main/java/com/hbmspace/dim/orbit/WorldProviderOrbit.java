package com.hbmspace.dim.orbit;

import com.hbmspace.config.SpaceConfig;
import com.hbmspace.dim.CelestialBody;
import com.hbmspace.dim.SolarSystem;
import com.hbmspace.dim.WorldProviderCelestial;
import com.hbmspace.dim.trait.CBT_Atmosphere;
import com.hbmspace.dim.trait.CBT_Destroyed;
import com.hbmspace.handler.atmosphere.ChunkAtmosphereManager;
import com.hbmspace.util.AstronomyUtil;
import com.hbm.util.BobMathUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WorldProviderOrbit extends WorldProvider {

	// Orbit at an altitude that provides an hour-long realtime orbit (game time is fast so we go slow)
	// We want a consistent orbital period to prevent orbiting too slow or fast (both for player comfort and feel)
	private static final float ORBITAL_PERIOD = 7200;

	public List<SolarSystem.AstroMetric> metrics;

	private double eclipseAmount;
	private float celestialAngle;
	
	protected float getOrbitalAltitude(CelestialBody body) {
		return getAltitudeForPeriod(body.massKg, ORBITAL_PERIOD);
	}
	
	// r = ∛[(G x Me x T2) / (4π2)]
	private float getAltitudeForPeriod(float massKg, float period) {
		return (float)Math.cbrt((AstronomyUtil.GRAVITATIONAL_CONSTANT * massKg * (period * period)) / (4 * Math.PI * Math.PI));
	}

	public float getSunPower() {
		double progress = OrbitalStation.clientStation.getTransferProgress(0);
		float sunPower = OrbitalStation.clientStation.orbiting.getSunPower();
		if(progress > 0) {
			return (float)BobMathUtil.lerp(progress, sunPower, OrbitalStation.clientStation.target.getSunPower());
		}
		return sunPower;
	}

	@Override
	public boolean hasSkyLight() { return true; }

	@Override
	public void init() {
		this.biomeProvider = new BiomeProviderSingle(BiomeGenOrbit.biome);
	}
	
	@Override
	public @NotNull IChunkGenerator createChunkGenerator() {
		return new ChunkProviderOrbit(this.world);
	}

	@Override
	public void updateWeather() {
		world.prevRainingStrength = 0.0F;
		world.prevThunderingStrength = 0.0F;
		world.rainingStrength = 0.0F;
		world.thunderingStrength = 0.0F;
	}

	// This is called once, at the beginning of every frame
	// so we use this to memoise expensive calcs
	@SideOnly(Side.CLIENT)
	protected void updateSky(float partialTicks) {
		CelestialBody body = CelestialBody.getBody(world);
		OrbitalStation station = OrbitalStation.clientStation;

		// First fetch the suns true size
		double sunSize = SolarSystem.calculateSunSize(body);

		double progress = station.getTransferProgress(partialTicks);

		// Get our orrery of bodies, this is cached for reuse in sky rendering
		if(station.state == OrbitalStation.StationState.ORBIT) {
			double altitude = getOrbitalAltitude(station.orbiting);
			metrics = SolarSystem.calculateMetricsFromSatellite(world, partialTicks, station.orbiting, altitude);
		} else {
			double fromAlt = getOrbitalAltitude(station.orbiting);
			double toAlt = getOrbitalAltitude(station.target);
			metrics = SolarSystem.calculateMetricsBetweenSatelliteOrbits(world, partialTicks, station.orbiting, station.target, fromAlt, toAlt, progress);
		}

		// Get our sun angle
		float angle = (float)SolarSystem.calculateSingleAngle(world, partialTicks, metrics, station.orbiting, getOrbitalAltitude(station.orbiting));
		if(progress > 0) {
			angle = (float)BobMathUtil.clerp(progress, angle, (float)SolarSystem.calculateSingleAngle(world, partialTicks, metrics, station.target, getOrbitalAltitude(station.target)));
		}

		celestialAngle = 0.5F - (angle / 360.0F);

		// Get our eclipse amount
		eclipseAmount = WorldProviderCelestial.getEclipseFactor(metrics, sunSize, SolarSystem.MAX_APPARENT_SIZE_ORBIT);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public @NotNull Vec3d getFogColor(float x, float y) {
		return new Vec3d(0, 0, 0);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public @NotNull Vec3d getSkyColor(@NotNull Entity camera, float partialTicks) {
		// getSkyColor is called first on every frame, so if you want to memoise anything, do it here
		updateSky(partialTicks);

		return new Vec3d(0, 0, 0);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float[] calcSunriseSunsetColors(float celestialAngle, float partialTicks) {
		return null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getStarBrightness(float par1) {
		// Stars look cool in orbit, but obvs at Moho we don't want the big fuckoff sun to not extinguish
		// Stars become visible during the day part of orbit just before Earth
		// And are fully visible during the day beyond the orbit of Duna
		float distanceStart = 9_000_000;
		float distanceEnd = 30_000_000;

		double progress = OrbitalStation.clientStation.getTransferProgress(par1);
		float semiMajorAxisKm = OrbitalStation.clientStation.orbiting.getPlanet().semiMajorAxisKm;
		if(progress > 0) {
			semiMajorAxisKm = (float)BobMathUtil.lerp(progress, semiMajorAxisKm, OrbitalStation.clientStation.target.getPlanet().semiMajorAxisKm);
		}

		float distanceFactor = MathHelper.clamp((semiMajorAxisKm - distanceStart) / (distanceEnd - distanceStart), 0F, 1F);

		float starBrightness = (float) eclipseAmount;

		return MathHelper.clamp(starBrightness, distanceFactor, 1F);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getSunBrightness(float par1) {
		if(SolarSystem.kerbol.hasTrait(CBT_Destroyed.class))
			return 0;

		return 1.0F - (float) eclipseAmount;
	}

	@Override
	public boolean canDoLightning(@NotNull Chunk chunk) {
		return false;
	}

	@Override
	public boolean canDoRainSnowIce(@NotNull Chunk chunk) {
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public float getCloudHeight() {
		return -99999;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IRenderHandler getSkyRenderer() {
		return new SkyProviderOrbit();
	}

	@Override
	public float calculateCelestialAngle(long worldTime, float partialTicks) {
		return celestialAngle;
	}

	// Same shit as in Celestial
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

	@Override
	public boolean canRespawnHere() {
		if(WorldProviderCelestial.attemptingSleep) {
			WorldProviderCelestial.attemptingSleep = false;
			return true;
		}

		return false;
	}

	@Override
	public @NotNull DimensionType getDimensionType(){return DimensionType.getById(SpaceConfig.orbitDimension);}
	
}
