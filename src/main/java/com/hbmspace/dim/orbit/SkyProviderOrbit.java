package com.hbmspace.dim.orbit;

import com.hbmspace.dim.CelestialBody;
import com.hbmspace.dim.SkyProviderCelestial;
import com.hbmspace.dim.SolarSystem;
import com.hbmspace.dim.SolarSystem.AstroMetric;
import com.hbmspace.dim.orbit.OrbitalStation.StationState;
import com.hbm.lib.Library;
import com.hbm.util.BobMathUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class SkyProviderOrbit extends SkyProviderCelestial {

	private static CelestialBody lastBody;

	@Override
	public void render(float partialTicks, WorldClient world, Minecraft mc) {
		WorldProviderOrbit provider = (WorldProviderOrbit) world.provider;
		OrbitalStation station = OrbitalStation.clientStation;
		double progress = station.getTransferProgress(partialTicks);
		float orbitalTilt = 80;

		GlStateManager.depthMask(false);
		GlStateManager.disableFog();
		GlStateManager.disableAlpha();
		GlStateManager.enableTexture2D();

		GlStateManager.enableBlend();
		RenderHelper.disableStandardItemLighting();

		// SRC_ALPHA, ONE_MINUS_SRC_ALPHA, ONE, ZERO
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

		float solarAngle = getCelestialAngle(world, provider.metrics, partialTicks, station);
		float siderealAngle = (float)SolarSystem.calculateSiderealAngle(world, partialTicks, station.orbiting);
		float celestialPhase = (1 - (solarAngle + 0.5F) % 1) * 2 - 1;

		float starBrightness = world.getStarBrightness(partialTicks);

		renderStars(mc, starBrightness, solarAngle + siderealAngle, orbitalTilt);

		GlStateManager.pushMatrix();
		{

			GlStateManager.rotate(orbitalTilt, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(solarAngle * 360.0F, 1.0F, 0.0F, 0.0F);

			// digma balls
			renderDigamma(world, mc, solarAngle);

			// SRC_ALPHA, ONE, ONE, ZERO
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

			double sunSize = SolarSystem.calculateSunSize(station.orbiting) * SolarSystem.SUN_RENDER_SCALE;
			if(station.state != StationState.ORBIT) {
				double sunTargetSize = SolarSystem.calculateSunSize(station.target) * SolarSystem.SUN_RENDER_SCALE;
				sunSize = BobMathUtil.lerp(progress, sunSize, sunTargetSize);
			}
			double coronaSize = sunSize * (3 - Library.smoothstep(Math.abs(celestialPhase), 0.7, 0.8));

			renderSun(partialTicks, world, mc, station.orbiting.getStar(), sunSize, coronaSize, 1, 0);

			CelestialBody orbiting = station.orbiting;
			if(station.state != StationState.ORBIT && progress > 0.5) orbiting = station.target;

			renderCelestials(partialTicks, world, mc, provider.metrics, new Vec3d(0, 0, 0), 1, 1, orbiting, SolarSystem.MAX_APPARENT_SIZE_ORBIT);

		}
		GlStateManager.popMatrix();


		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableFog();

		GlStateManager.enableTexture2D();
		GlStateManager.depthMask(true);
	}

	// All angles within are normalized to -180/180
	private float getCelestialAngle(WorldClient world, List<AstroMetric> metrics, float partialTicks, OrbitalStation station) {
		float solarAngle = world.getCelestialAngle(partialTicks);
		if(station.state == StationState.ORBIT) return solarAngle;

		solarAngle = solarAngle * 360.0F - 180.0F;

		if(station.state != StationState.ARRIVING) lastBody = station.orbiting;

		double progress = station.getUnscaledProgress(partialTicks);
		float travelAngle = -(float)SolarSystem.calculateSingleAngle(metrics, lastBody, station.target);
		travelAngle = MathHelper.wrapDegrees(travelAngle + 90.0F);

		if(station.state == StationState.TRANSFER) {
			return (travelAngle + 180.0F) / 360.0F;
		} else if(station.state == StationState.LEAVING) {
			return ((float)BobMathUtil.clerp(progress, solarAngle, travelAngle) + 180.0F) / 360.0F;
		} else {
			return ((float)BobMathUtil.clerp(progress, travelAngle, solarAngle) + 180.0F) / 360.0F;
		}
	}
	
}
