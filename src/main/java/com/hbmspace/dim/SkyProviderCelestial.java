package com.hbmspace.dim;

import com.hbm.capability.HbmLivingProps;
import com.hbm.util.RenderUtil;
import com.hbmspace.dim.SolarSystem.AstroMetric;
import com.hbmspace.dim.orbit.OrbitalStation;
import com.hbmspace.dim.trait.*;
import com.hbm.saveddata.satellites.Satellite;
import com.hbm.saveddata.satellites.SatelliteSavedData;
import com.hbm.util.BobMathUtil;
import com.hbmspace.main.ModEventHandlerClient;
import com.hbmspace.render.shader.ShaderSpace;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.IRenderHandler;
import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GLContext;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class SkyProviderCelestial extends IRenderHandler {

	private static final ResourceLocation planetTexture = new ResourceLocation("hbm", "textures/misc/space/planet.png");
	private static final ResourceLocation flareTexture = new ResourceLocation("hbm", "textures/misc/space/sunspike.png");
	private static final ResourceLocation nightTexture = new ResourceLocation("hbm", "textures/misc/space/night.png");
	private static final ResourceLocation digammaStar = new ResourceLocation("hbm", "textures/misc/space/star_digamma.png");
	private static final ResourceLocation lodeStar = new ResourceLocation("hbm", "textures/misc/star_lode.png");
	private static final ResourceLocation stationTexture = new ResourceLocation("hbm", "textures/misc/space/station.png");

	private static final ResourceLocation impactTexture = new ResourceLocation("hbm", "textures/misc/space/impact.png");
	private static final ResourceLocation shockwaveTexture = new ResourceLocation("hbm", "textures/particle/shockwave.png");
	private static final ResourceLocation shockFlareTexture = new ResourceLocation("hbm", "textures/particle/flare.png");

	private static final ResourceLocation ringTexture = new ResourceLocation("hbm", "textures/misc/space/rings.png");
	private static final ResourceLocation destroyedBody = new ResourceLocation("hbm", "textures/misc/space/destroyed.png");

	private static final ResourceLocation noise = new ResourceLocation("hbm", "shaders/iChannel1.png");

	protected static final ShaderSpace planetShader = new ShaderSpace(new ResourceLocation("hbm", "shaders/crescent.frag"));
	protected static final ShaderSpace swarmShader = new ShaderSpace(new ResourceLocation("hbm", "shaders/swarm.vert"), new ResourceLocation("hbm", "shaders/swarm.frag"));

	private static final ResourceLocation[] citylights = new ResourceLocation[] {
			new ResourceLocation("hbm", "textures/misc/space/citylights_0.png"),
			new ResourceLocation("hbm", "textures/misc/space/citylights_1.png"),
			new ResourceLocation("hbm", "textures/misc/space/citylights_2.png"),
			new ResourceLocation("hbm", "textures/misc/space/citylights_3.png"),
	};

	private static final ResourceLocation defaultMask = new ResourceLocation("hbm", "textures/misc/space/default_mask.png");

	public static boolean displayListsInitialized = false;
	public static int skyVBO;
	public static int sky2VBO;

	private static boolean gl13;

	public SkyProviderCelestial() {
		if (!displayListsInitialized) {
			initializeDisplayLists();
		}
	}

	private void initializeDisplayLists() {
		ContextCapabilities contextcapabilities = GLContext.getCapabilities();

		Minecraft mc = Minecraft.getMinecraft();
		skyVBO = mc.renderGlobal.glSkyList;
		sky2VBO = mc.renderGlobal.glSkyList2;

		gl13 = contextcapabilities.OpenGL13;

		displayListsInitialized = true;
	}

	private static int lastBrightestPixel = 0;

	@Override
	public void render(float partialTicks, WorldClient world, Minecraft mc) {
		if(!(world.provider instanceof WorldProviderCelestial celestialProvider)) return;

		DynamicTexture lightmapTexture = mc.entityRenderer.lightmapTexture;
		int[] lightmapColors = mc.entityRenderer.lightmapColors;
		// Without mixins, we have to resort to some very wacky ways of checking that the lightmap needs to be updated
		// fortunately, thanks to torch flickering, we can just check to see if the brightest pixel has been modified
		if(lastBrightestPixel != lightmapColors[255] + lightmapColors[250]) {
			if(celestialProvider.updateLightmap(lightmapColors)) {
				lightmapTexture.updateDynamicTexture();
			}

			lastBrightestPixel = lightmapColors[255] + lightmapColors[250];
		}
		float fogIntensity = ModEventHandlerClient.lastFogDensity * 30;
		CelestialBody body = CelestialBody.getBody(world);
		CelestialBody sun = body.getStar();
		CBT_Atmosphere atmosphere = body.getTrait(CBT_Atmosphere.class);

		boolean hasAtmosphere = atmosphere != null;

		float pressure = hasAtmosphere ? (float)atmosphere.getPressure() : 0.0F;
		float visibility = hasAtmosphere ? MathHelper.clamp(2.0F - pressure, 0.1F, 1.0F) : 1.0F;

		GlStateManager.disableTexture2D();
		Vec3d skyColor = world.getSkyColor(mc.getRenderViewEntity(), partialTicks);

		float skyR = (float) skyColor.x;
		float skyG = (float) skyColor.y;
		float skyB = (float) skyColor.z;

		// Diminish sky colour when leaving the atmosphere
		if(mc.getRenderViewEntity().posY > 300) {
			double curvature = MathHelper.clamp((800.0F - (float) mc.getRenderViewEntity().posY) / 500.0F, 0.0F, 1.0F);
			skyR *= (float) curvature;
			skyG *= (float) curvature;
			skyB *= (float) curvature;
		}

		if(mc.gameSettings.anaglyph) {
			float[] anaglyphColor = applyAnaglyph(skyR, skyG, skyB);
			skyR = anaglyphColor[0];
			skyG = anaglyphColor[1];
			skyB = anaglyphColor[2];
		}

		float planetR = skyR;
		float planetG = skyG;
		float planetB = skyB;

		if(fogIntensity > 0.01F) {
			Vec3d fogColor = world.getFogColor(partialTicks);
			planetR = (float)BobMathUtil.clampedLerp(skyR, fogColor.x, fogIntensity);
			planetG = (float)BobMathUtil.clampedLerp(skyG, fogColor.y, fogIntensity);
			planetB = (float)BobMathUtil.clampedLerp(skyB, fogColor.z, fogIntensity);
		}

		Vec3d planetTint = new Vec3d(planetR, planetG, planetB);

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();

		GlStateManager.depthMask(false);
		GlStateManager.enableFog();
		GlStateManager.color(skyR, skyG, skyB);

		// Set maximum sky fog distance to 12 chunks, works nicely with Celeritas/Distant Horizons
		// and makes for a more consistent sky in vanilla too
		RenderUtil.pushAttrib(GL11.GL_FOG_BIT);
		{
			GlStateManager.setFogStart(0.0F);
			GlStateManager.setFogEnd(Math.min(12.0F, mc.gameSettings.renderDistanceChunks) * 16.0F);

			GlStateManager.callList(skyVBO);
		}
		RenderUtil.popAttrib();

		GlStateManager.disableFog();
		GlStateManager.disableAlpha();
		GlStateManager.enableTexture2D();

		GlStateManager.enableBlend();
		RenderHelper.disableStandardItemLighting();

		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
				GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

		float starBrightness = world.getStarBrightness(partialTicks) * visibility;
		float solarAngle = world.getCelestialAngle(partialTicks);
		float siderealAngle = (float)SolarSystem.calculateSiderealAngle(world, partialTicks, body);

		// Handle any special per-body sunset rendering
		renderSunset(partialTicks, world, mc, solarAngle, pressure, body.surfaceTexture);

		renderStars(mc, starBrightness, solarAngle + siderealAngle, body.axialTilt);


		GlStateManager.pushMatrix();
		{
			GlStateManager.rotate(body.axialTilt, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(solarAngle * 360.0F, 1.0F, 0.0F, 0.0F);

			// Draw DIGAMMA STAR
			renderDigamma(world, mc, solarAngle);

			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);

			// Scale sun size for rendering (texture is 4 times larger than actual, for glow)
			double sunSize = SolarSystem.calculateSunSize(body) * SolarSystem.SUN_RENDER_SCALE;
			double coronaSize = sunSize * (3 - MathHelper.clamp(pressure, 0.0F, 1.0F));

			renderSun(partialTicks, world, mc, sun, sunSize, coronaSize, visibility, pressure);

			float blendAmount = hasAtmosphere ? MathHelper.clamp(1 - world.getSunBrightnessFactor(partialTicks), 0.25F, 1F) : 1F;

			renderCelestials(partialTicks, world, mc, celestialProvider.metrics, planetTint, visibility, blendAmount, null, SolarSystem.MAX_APPARENT_SIZE_SURFACE);

			GlStateManager.enableBlend();

			if(visibility > 0.2F) {
				// JEFF BOZOS WOULD LIKE TO KNOW YOUR LOCATION
				// ... to send you a pakedge :)))
				if(world.provider.getDimension() == 0) {
					renderSatellite(mc, solarAngle, 1916169, new float[] { 1.0F, 0.534F, 0.385F });
				}

				// Light up the sky
				for(Map.Entry<Integer, Satellite> entry : SatelliteSavedData.getClientSats().entrySet()) {
					renderSatellite(mc, solarAngle, entry.getKey(), entry.getValue().getColor());
				}

				// Stations, too
				for(OrbitalStation station : OrbitalStation.orbitingStations) {
					renderStation(mc, station, solarAngle);
				}
			}
		}
		GlStateManager.popMatrix();

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableFog();

		GlStateManager.disableTexture2D();
		GlStateManager.color(0.0F, 0.0F, 0.0F);

		Vec3d pos = mc.player.getPositionVector();
		double heightAboveHorizon = pos.y - world.getHorizon();

		if(heightAboveHorizon < 0.0D) {
			GlStateManager.pushMatrix();
			{
				GlStateManager.translate(0.0F, 12.0F, 0.0F);

				GlStateManager.callList(sky2VBO);
			}
			GlStateManager.popMatrix();

			float f8 = 1.0F;
			float f9 = -((float) (heightAboveHorizon + 65.0D));
			float opposite = -f8;

			bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
			bufferBuilder.pos(-f8, f9, f8).color(0, 0, 0, 255).endVertex();
			bufferBuilder.pos(f8, f9, f8).color(0, 0, 0, 255).endVertex();
			bufferBuilder.pos(f8, opposite, f8).color(0, 0, 0, 255).endVertex();
			bufferBuilder.pos(-f8, opposite, f8).color(0, 0, 0, 255).endVertex();
			bufferBuilder.pos(-f8, opposite, -f8).color(0, 0, 0, 255).endVertex();
			bufferBuilder.pos(f8, opposite, -f8).color(0, 0, 0, 255).endVertex();
			bufferBuilder.pos(f8, f9, -f8).color(0, 0, 0, 255).endVertex();
			bufferBuilder.pos(-f8, f9, -f8).color(0, 0, 0, 255).endVertex();
			bufferBuilder.pos(f8, opposite, -f8).color(0, 0, 0, 255).endVertex();
			bufferBuilder.pos(f8, opposite, f8).color(0, 0, 0, 255).endVertex();
			bufferBuilder.pos(f8, f9, f8).color(0, 0, 0, 255).endVertex();
			bufferBuilder.pos(f8, f9, -f8).color(0, 0, 0, 255).endVertex();
			bufferBuilder.pos(-f8, f9, -f8).color(0, 0, 0, 255).endVertex();
			bufferBuilder.pos(-f8, f9, f8).color(0, 0, 0, 255).endVertex();
			bufferBuilder.pos(-f8, opposite, f8).color(0, 0, 0, 255).endVertex();
			bufferBuilder.pos(-f8, opposite, -f8).color(0, 0, 0, 255).endVertex();
			bufferBuilder.pos(-f8, opposite, -f8).color(0, 0, 0, 255).endVertex();
			bufferBuilder.pos(-f8, opposite, f8).color(0, 0, 0, 255).endVertex();
			bufferBuilder.pos(f8, opposite, f8).color(0, 0, 0, 255).endVertex();
			bufferBuilder.pos(f8, opposite, -f8).color(0, 0, 0, 255).endVertex();
			tessellator.draw();
		}

		if(world.provider.isSkyColored()) {
			GlStateManager.color(skyR * 0.2F + 0.04F, skyG * 0.2F + 0.04F, skyB * 0.6F + 0.1F);
		} else {
			GlStateManager.color(skyR, skyG, skyB);
		}

		GlStateManager.pushMatrix();
		{
			GlStateManager.translate(0.0F, -((float) (heightAboveHorizon - 16.0D)), 0.0F);

			GlStateManager.callList(sky2VBO);
		}
		GlStateManager.popMatrix();

		double sc = 1 / (pos.y / 1000);
		double uvOffset = (pos.x / 1024) % 1;
		GlStateManager.pushMatrix();
		{
			GlStateManager.enableTexture2D();
			GlStateManager.disableAlpha();
			GlStateManager.disableFog();
			GlStateManager.enableBlend();

			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

			float sunBrightness = world.getSunBrightness(partialTicks);

			GlStateManager.color(sunBrightness, sunBrightness, sunBrightness, ((float)pos.y - 200.0F) / 300.0F);
			mc.getTextureManager().bindTexture(body.texture);
			GlStateManager.rotate(180, 1, 0, 0);

			bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
			bufferBuilder.pos(-115 * sc, 100.0D, -115 * sc).tex(0.0D + uvOffset, 0.0D).endVertex();
			bufferBuilder.pos(115 * sc, 100.0D, -115 * sc).tex(1.0D + uvOffset, 0.0D).endVertex();
			bufferBuilder.pos(115 * sc, 100.0D, 115 * sc).tex(1.0D + uvOffset, 1.0D).endVertex();
			bufferBuilder.pos(-115 * sc, 100.0D, 115 * sc).tex(0.0D + uvOffset, 1.0D).endVertex();
			tessellator.draw();

			GlStateManager.disableTexture2D();
			GlStateManager.enableAlpha();
			GlStateManager.enableFog();
			GlStateManager.disableBlend();

			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
		}
		GlStateManager.popMatrix();

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.disableBlend();

		GlStateManager.enableTexture2D();
		GlStateManager.depthMask(true);
	}

	protected void renderSunset(float partialTicks, WorldClient world, Minecraft mc, float solarAngle, float pressure, ResourceLocation surfaceTexture) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();

		float[] sunsetColor = calcSunriseSunsetColors(partialTicks, world, solarAngle, pressure);

		if(sunsetColor != null) {
			float[] anaglyphColor = mc.gameSettings.anaglyph ? applyAnaglyph(sunsetColor) : sunsetColor;
			float sunsetDirection = MathHelper.sin(world.getCelestialAngleRadians(partialTicks)) < 0.0F ? 180.0F : 0.0F;

			GlStateManager.disableTexture2D();
			GlStateManager.shadeModel(7425); // GL_SMOOTH

			GlStateManager.pushMatrix();
			{
				GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
				GlStateManager.rotate(sunsetDirection, 0.0F, 0.0F, 1.0F);
				GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);

				bufferbuilder.begin(6, DefaultVertexFormats.POSITION_COLOR); // GL_TRIANGLE_FAN
				bufferbuilder.pos(0.0D, 100.0D, 0.0D).color(anaglyphColor[0], anaglyphColor[1], anaglyphColor[2], sunsetColor[3]).endVertex();

				byte segments = 16;

				for(int j = 0; j <= segments; ++j) {
					float angle = (float)j * (float)Math.PI * 2.0F / (float)segments;
					float sinAngle = MathHelper.sin(angle);
					float cosAngle = MathHelper.cos(angle);
					bufferbuilder.pos(sinAngle * 120.0F, cosAngle * 120.0F, -cosAngle * 40.0F * sunsetColor[3])
							.color(sunsetColor[0], sunsetColor[1], sunsetColor[2], 0.0F).endVertex();
				}

				tessellator.draw();
			}
			GlStateManager.popMatrix();

			GlStateManager.shadeModel(7424); // GL_FLAT
			GlStateManager.enableTexture2D();

			// charged dust
			if(pressure < 0.05F) {
				Random rand = new Random(0);

				GlStateManager.pushMatrix();
				{
					double time = ((double)world.provider.getWorldTime() + partialTicks) * 0.002;

					GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
					GlStateManager.rotate(sunsetDirection, 0.0F, 0.0F, 1.0F);
					GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);

					mc.getTextureManager().bindTexture(surfaceTexture);

					GlStateManager.color(0.5F + rand.nextFloat() * 0.5F, 0.5F + rand.nextFloat() * 0.5F, 0.5F + rand.nextFloat() * 0.5F, rand.nextFloat() * sunsetColor[3] * 4.0F);

					// GL_SRC_ALPHA, GL_ONE_MINUS_CONSTANT_ALPHA, GL_ONE, GL_ZERO
					OpenGlHelper.glBlendFunc(770, 32772, 1, 0);

					bufferbuilder.begin(0, DefaultVertexFormats.POSITION); // GL_POINTS
					for(int i = 0; i < 1024; i++) {
						bufferbuilder.pos(rand.nextGaussian() * 50, 100, -((Math.abs(rand.nextGaussian() * 20) + time) % Math.abs(rand.nextGaussian()) * 20)).endVertex();
					}
					tessellator.draw();
				}
				GlStateManager.popMatrix();
			}
		}
	}

	// We don't want certain sunrise/sunset effects to change the fog colour, so we do them here
	protected float[] calcSunriseSunsetColors(float partialTicks, WorldClient world, float solarAngle, float pressure) {
		if(pressure < 0.05F) {
			float cutoff = 0.4F;
			float angle = MathHelper.cos(solarAngle * (float)Math.PI * 2.0F) - 0.0F;

			if(angle < -cutoff || angle > cutoff) return null;

			float colorIntensity = angle / cutoff * 0.5F + 0.5F;
			float alpha = 1.0F - (1.0F - MathHelper.sin(colorIntensity * (float)Math.PI)) * 0.99F;
			alpha *= alpha;
			return new float[] { 0.9F, 1.0F, 1.0F, alpha * 0.2F };
		}

		return world.provider.calcSunriseSunsetColors(world.getCelestialAngle(partialTicks), partialTicks);
	}

	protected void renderStars(Minecraft mc, float starBrightness, float celestialAngle, float axialTilt) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();

		if(starBrightness > 0.0F) {
			GlStateManager.pushMatrix();
			{
				GlStateManager.rotate(axialTilt, 1.0F, 0.0F, 0.0F);

				mc.getTextureManager().bindTexture(nightTexture);

				GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);

				float starBrightnessAlpha = starBrightness * 0.6f;
				GlStateManager.color(1.0F, 1.0F, 1.0F, starBrightnessAlpha);

				GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);

				GlStateManager.rotate(celestialAngle * 360.0F, 1.0F, 0.0F, 0.0F);
				GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
				GlStateManager.color(1.0F, 1.0F, 1.0F, starBrightnessAlpha);

				GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
				GlStateManager.rotate(-90.0F, 0.0F, 0.0F, 1.0F);
				renderSkyboxSide(tessellator, bufferBuilder, 4);

				GlStateManager.pushMatrix();
				GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
				renderSkyboxSide(tessellator, bufferBuilder, 1);
				GlStateManager.popMatrix();

				GlStateManager.pushMatrix();
				GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
				renderSkyboxSide(tessellator, bufferBuilder, 0);
				GlStateManager.popMatrix();

				GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
				renderSkyboxSide(tessellator, bufferBuilder, 5);

				GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
				renderSkyboxSide(tessellator, bufferBuilder, 2);

				GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
				renderSkyboxSide(tessellator, bufferBuilder, 3);

				GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			}
			GlStateManager.popMatrix();
		}
	}

	protected void renderSun(float partialTicks, WorldClient world, Minecraft mc, CelestialBody sun, double sunSize, double coronaSize, float visibility, float pressure) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();

		CBT_Dyson dyson = sun.getTrait(CBT_Dyson.class);
		int swarmCount = dyson != null ? dyson.size() : 0;

		if(sun.shader != null && sun.hasTrait(CBT_Destroyed.class)) {
			// BLACK HOLE SUN
			// WON'T YOU COME
			// AND WASH AWAY THE RAIN

			ShaderSpace shader = sun.shader;
			double shaderSize = sunSize * sun.shaderScale;

			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

			shader.use();

			float time = ((float)world.getWorldTime() + partialTicks) / 20.0F;

			mc.getTextureManager().bindTexture(noise);
			GlStateManager.pushMatrix();

			// Fix orbital plane
			GlStateManager.rotate(-90.0F, 0, 1, 0);
			shader.setUniform1f("iTime", time);
			shader.setUniform1i("iChannel1", 0);

			bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
			bufferBuilder.pos(-shaderSize, 100.0D, -shaderSize).tex(0.0D, 0.0D).endVertex();
			bufferBuilder.pos(shaderSize, 100.0D, -shaderSize).tex(1.0D, 0.0D).endVertex();
			bufferBuilder.pos(shaderSize, 100.0D, shaderSize).tex(1.0D, 1.0D).endVertex();
			bufferBuilder.pos(-shaderSize, 100.0D, shaderSize).tex(0.0D, 1.0D).endVertex();
			tessellator.draw();

			shader.stop();

			GlStateManager.popMatrix();
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		} else {
			// Some blanking to conceal the stars
			GlStateManager.disableTexture2D();
			GlStateManager.color(0.0F, 0.0F, 0.0F, 1.0F);

			bufferBuilder.begin(7, DefaultVertexFormats.POSITION);
			bufferBuilder.pos(-sunSize, 99.9D, -sunSize).endVertex();
			bufferBuilder.pos(sunSize, 99.9D, -sunSize).endVertex();
			bufferBuilder.pos(sunSize, 99.9D, sunSize).endVertex();
			bufferBuilder.pos(-sunSize, 99.9D, sunSize).endVertex();
			tessellator.draw();

			// Draw the sun to the depth buffer to block swarm members that are behind
			GlStateManager.depthMask(true);
			GlStateManager.color(0.0F, 0.0F, 0.0F, 0.0F);

			bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
			bufferBuilder.pos(-sunSize * 0.25D, 100.1D, -sunSize * 0.25D).tex(0.0D, 0.0D).endVertex();
			bufferBuilder.pos(sunSize * 0.25D, 100.1D, -sunSize * 0.25D).tex(1.0D, 0.0D).endVertex();
			bufferBuilder.pos(sunSize * 0.25D, 100.1D, sunSize * 0.25D).tex(1.0D, 1.0D).endVertex();
			bufferBuilder.pos(-sunSize * 0.25D, 100.1D, sunSize * 0.25D).tex(0.0D, 1.0D).endVertex();
			tessellator.draw();

			GlStateManager.depthMask(false);

			GlStateManager.enableTexture2D();
			GlStateManager.color(1.0F, 1.0F, 1.0F, visibility);

			mc.getTextureManager().bindTexture(SolarSystem.kerbol.texture);

			float[] sunColor = world.provider instanceof WorldProviderCelestial celestial
					? celestial.getSunColor()
					: new float[] { 1.0F, 1.0F, 1.0F };

			GlStateManager.color(sunColor[0], sunColor[1], sunColor[2], visibility);
			bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
			bufferBuilder.pos(-sunSize, 100.0D, -sunSize).tex(0.0D, 0.0D).endVertex();
			bufferBuilder.pos(sunSize, 100.0D, -sunSize).tex(1.0D, 0.0D).endVertex();
			bufferBuilder.pos(sunSize, 100.0D, sunSize).tex(1.0D, 1.0D).endVertex();
			bufferBuilder.pos(-sunSize, 100.0D, sunSize).tex(0.0D, 1.0D).endVertex();
			tessellator.draw();

			// Draw a big ol' spiky flare! Less so when there is an atmosphere
			GlStateManager.color(sunColor[0], sunColor[1], sunColor[2], 1 - MathHelper.clamp(pressure, 0.0F, 1.0F) * 0.75F);
			mc.getTextureManager().bindTexture(flareTexture);

			bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
			bufferBuilder.pos(-coronaSize, 99.9D, -coronaSize).tex(0.0D, 0.0D).endVertex();
			bufferBuilder.pos(coronaSize, 99.9D, -coronaSize).tex(1.0D, 0.0D).endVertex();
			bufferBuilder.pos(coronaSize, 99.9D, coronaSize).tex(1.0D, 1.0D).endVertex();
			bufferBuilder.pos(-coronaSize, 99.9D, coronaSize).tex(0.0D, 1.0D).endVertex();
			tessellator.draw();

			// Draw the swarm members with depth occlusion
			// We do this last so we can render transparency against the sun
			renderSwarm(partialTicks, world, mc, sunSize * 0.5, swarmCount);

			// Clear and disable the depth buffer once again, buffer has to be writable to clear it
			GlStateManager.depthMask(true);
			GlStateManager.clear(256); // GL_DEPTH_BUFFER_BIT
			GlStateManager.depthMask(false);
		}
	}

	private void renderSwarm(float partialTicks, WorldClient world, Minecraft mc, double swarmRadius, int swarmCount) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();

		// bloodseeking, parasitic, ecstatically tracing decay
		// thriving in the glow that death emits, the warm perfume it radiates

		swarmShader.use();

		// swarm members render as pixels, which can vary based on screen resolution
		// because of this, we make the pixels more transparent based on their apparent size, which varies by a fair few factors
		// this isn't a foolproof solution, analyzing the projection matrices would be best, but it works for now.
		float swarmScreenSize = (float)((mc.displayHeight / mc.gameSettings.fovSetting) * swarmRadius * 0.002);
		float time = ((float)world.getWorldTime() + partialTicks) / 800.0F;

		swarmShader.setUniform1f("iTime", time);

		int offsetLocation = swarmShader.getUniformLocation("iOffset");

		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.color(0.0F, 0.0F, 0.0F, MathHelper.clamp(swarmScreenSize, 0, 1));

		GlStateManager.pushMatrix();
		{
			GlStateManager.translate(0.0F, 100.0F, 0.0F);
			GlStateManager.scale(swarmRadius, swarmRadius, swarmRadius);

			GlStateManager.pushMatrix();
			{
				GlStateManager.rotate(80.0F, 1, 0, 0);

				bufferbuilder.begin(0, DefaultVertexFormats.POSITION); // GL_POINTS
				for(int i = 0; i < swarmCount; i += 3) {
					swarmShader.setUniform1f(offsetLocation, i);

					float t = i + time;
					double x = Math.cos(t);
					double z = Math.sin(t);

					bufferbuilder.pos(x, 0, z).endVertex();
				}
				tessellator.draw();
			}
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			{
				GlStateManager.rotate(60.0F, 0, 1, 0);
				GlStateManager.rotate(80.0F, 1, 0, 0);

				bufferbuilder.begin(0, DefaultVertexFormats.POSITION);
				for(int i = 1; i < swarmCount; i += 3) {
					swarmShader.setUniform1f(offsetLocation, i);

					float t = i + time;
					double x = Math.cos(t);
					double z = Math.sin(t);

					bufferbuilder.pos(x, 0, z).endVertex();
				}
				tessellator.draw();
			}
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			{
				GlStateManager.rotate(-60.0F, 0, 1, 0);
				GlStateManager.rotate(80.0F, 1, 0, 0);

				bufferbuilder.begin(0, DefaultVertexFormats.POSITION);
				for(int i = 2; i < swarmCount; i += 3) {
					swarmShader.setUniform1f(offsetLocation, i);

					float t = i + time;
					double x = Math.cos(t);
					double z = Math.sin(t);

					bufferbuilder.pos(x, 0, z).endVertex();
				}
				tessellator.draw();
			}
			GlStateManager.popMatrix();
		}
		GlStateManager.popMatrix();

		swarmShader.stop();

		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
	}

	protected void renderCelestials(float partialTicks, WorldClient world, Minecraft mc, List<AstroMetric> metrics, Vec3d planetTint, float visibility, float blendAmount, CelestialBody orbiting, float maxSize) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		float blendDarken = 0.1F;

		double transitionMinSize = 0.1D;
		double transitionMaxSize = 0.5D;

		for(AstroMetric metric : metrics) {
			// Ignore self
			if(metric.distance == 0)
				continue;

			boolean orbitingThis = metric.body == orbiting;

			double uvOffset = orbitingThis ? 1 - ((((double)world.getWorldTime() + partialTicks) / 1024) % 1) : 0;
			float axialTilt = orbitingThis ? 0 : metric.body.axialTilt;

			GlStateManager.pushMatrix();
			{
				double size = MathHelper.clamp(metric.apparentSize, 0, maxSize);
				boolean renderPoint = size < transitionMaxSize;
				boolean renderBody = size > transitionMinSize;

				GlStateManager.rotate((float)metric.angle, 1.0F, 0.0F, 0.0F);
				GlStateManager.rotate((float)metric.inclination, 0.0F, 0.0F, 1.0F);
				GlStateManager.rotate(axialTilt + 90.0F, 0.0F, 1.0F, 0.0F);

				if(renderBody) {
					// Draw the back half of the ring (obscured by body)
					if(metric.body.hasRings) {
						GlStateManager.pushMatrix();
						{
							GlStateManager.color(metric.body.ringColor[0], metric.body.ringColor[1], metric.body.ringColor[2], visibility);
							mc.getTextureManager().bindTexture(ringTexture);

							GlStateManager.disableCull();

							double ringSize = size * metric.body.ringSize;

							GlStateManager.translate(0.0F, 100.0F, 0.0F);
							GlStateManager.rotate((float)-metric.angle, 0.0F, 0.0F, 1.0F);
							GlStateManager.rotate(90.0F - metric.body.ringTilt, 1.0F, 0.0F, 0.0F);
							GlStateManager.rotate((float)metric.angle, 0.0F, 1.0F, 0.0F);

							bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
							bufferBuilder.pos(-ringSize, 0, -ringSize).tex(0.0D, 0.0D).endVertex();
							bufferBuilder.pos(ringSize, 0, -ringSize).tex(1.0D, 0.0D).endVertex();
							bufferBuilder.pos(ringSize, 0, 0).tex(1.0D, 0.5D).endVertex();
							bufferBuilder.pos(-ringSize, 0, 0).tex(0.0D, 0.5D).endVertex();
							tessellator.draw();

							GlStateManager.enableCull();

						}
						GlStateManager.popMatrix();
					}

					CBT_Destroyed d = metric.body.getTrait(CBT_Destroyed.class);

					if(d != null) {
						// Stop calling things "interp", that's a verb not a noun
						double interpr = d.interp + size * 0.5;

						float alpd = (float) (1.0F - Math.min(1.0F, interpr / 100));
						Random random = new Random(12);

						int numQuads = 30;
						for (int i = 0; i < numQuads; i++) {
							double radius = (random.nextDouble() * size) * d.interp;

							double randomTheta = random.nextDouble() * Math.PI * 2;
							double randomPhi = random.nextDouble() * Math.PI;

							double randomX = radius * Math.sin(randomPhi) * Math.cos(randomTheta) * 0.7;
							double randomY = radius * Math.sin(randomPhi) * Math.sin(randomTheta);
							double randomZ = radius * Math.cos(randomPhi) * 0.7;

							float randomRotation = random.nextFloat() * 360.0F;


							double uMin = random.nextDouble();
							double vMin = random.nextDouble();
							double uMax = Math.min(uMin + (random.nextDouble() * 0.2), 1.0);
							double vMax = Math.min(vMin + (random.nextDouble() * 0.2), 1.0);

							GlStateManager.pushMatrix();
							{
								GlStateManager.translate(randomX * -0.05, randomY * 0.00, randomZ * -0.05);

								GlStateManager.rotate(randomRotation * d.interp * 0.05F, 0.0F, 1.0F, 0.0F);

								mc.getTextureManager().bindTexture(metric.body.texture);
								GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

								bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
								double qsize = size * random.nextDouble() * 0.1;
								bufferBuilder.pos(-qsize, 100.0D, -qsize).tex(uMin, vMin).endVertex();
								bufferBuilder.pos(qsize, 100.0D, -qsize).tex(uMax, vMin).endVertex();
								bufferBuilder.pos(qsize, 100.0D, qsize).tex(uMax, vMax).endVertex();
								bufferBuilder.pos(-qsize, 100.0D, qsize).tex(uMin, vMax).endVertex();

								tessellator.draw();
							}
							GlStateManager.popMatrix();

							GlStateManager.pushMatrix();
							{
								GlStateManager.translate(randomX * 0.04, randomY * 0.00, randomZ * 0.04);

								GlStateManager.rotate(randomRotation * d.interp * 0.05F, 0.0F, 1.0F, 0.0F);
								mc.getTextureManager().bindTexture(destroyedBody);
								GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

								bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
								double qsize = size * random.nextDouble() * 0.07;
								bufferBuilder.pos(-qsize, 100.0D, -qsize).tex(uMin, vMin).endVertex();
								bufferBuilder.pos(qsize, 100.0D, -qsize).tex(uMax, vMin).endVertex();
								bufferBuilder.pos(qsize, 100.0D, qsize).tex(uMax, vMax).endVertex();
								bufferBuilder.pos(-qsize, 100.0D, qsize).tex(uMin, vMax).endVertex();

								tessellator.draw();
							}
							GlStateManager.popMatrix();
						}


						GlStateManager.color(1.0F, 1.0F, 1.0F, alpd);
						mc.getTextureManager().bindTexture(shockwaveTexture);
						double interpe = (d.interp * 0.5) * size * 0.1;
						bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
						bufferBuilder.pos(-interpe, 100.0D, -interpe).tex(0.0D + uvOffset, 0.0D).endVertex();
						bufferBuilder.pos(interpe, 100.0D, -interpe).tex(1.0D + uvOffset, 0.0D).endVertex();
						bufferBuilder.pos(interpe, 100.0D, interpe).tex(1.0D + uvOffset, 1.0D).endVertex();
						bufferBuilder.pos(-interpe, 100.0D, interpe).tex(0.0D + uvOffset, 1.0D).endVertex();
						tessellator.draw();


						GlStateManager.color(1.0F, 1.0F, 1.0F, alpd * 2);
						mc.getTextureManager().bindTexture(shockFlareTexture);

						interpr = size * 3;
						bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
						bufferBuilder.pos(-interpr, 100.0D, -interpr).tex(0.0D + uvOffset, 0.0D).endVertex();
						bufferBuilder.pos(interpr, 100.0D, -interpr).tex(1.0D + uvOffset, 0.0D).endVertex();
						bufferBuilder.pos(interpr, 100.0D, interpr).tex(1.0D + uvOffset, 1.0D).endVertex();
						bufferBuilder.pos(-interpr, 100.0D, interpr).tex(0.0D + uvOffset, 1.0D).endVertex();
						tessellator.draw();

					} else {
						GlStateManager.disableBlend();
						GlStateManager.color(1.0F, 1.0F, 1.0F, visibility);
						mc.getTextureManager().bindTexture(metric.body.texture);

						bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
						bufferBuilder.pos(-size, 100.0D, -size).tex(0.0D + uvOffset, 0.0D).endVertex();
						bufferBuilder.pos(size, 100.0D, -size).tex(1.0D + uvOffset, 0.0D).endVertex();
						bufferBuilder.pos(size, 100.0D, size).tex(1.0D + uvOffset, 1.0D).endVertex();
						bufferBuilder.pos(-size, 100.0D, size).tex(0.0D + uvOffset, 1.0D).endVertex();
						tessellator.draw();


						CBT_Impact impact = metric.body.getTrait(CBT_Impact.class);
						CBT_Lights light = metric.body.getTrait(CBT_Lights.class);

						double impactTime = impact != null ? (world.getTotalWorldTime() - impact.time) + partialTicks : 0;
						int lightIntensity = light != null && impactTime < 40 ? light.getIntensity() : 0;

						int blackoutInterval = 8;
						int maxBlackouts = 5;

						int activeBlackouts = Math.min((int)(impactTime / blackoutInterval), maxBlackouts);

						GlStateManager.enableBlend();
						// Draw a shader on top to render celestial phase
						GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

						GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

						planetShader.use();
						planetShader.setUniform1f("phase", (float)-metric.phase);
						planetShader.setUniform1f("offset", (float)uvOffset);
						planetShader.setUniform1i("lights", 0);
						planetShader.setUniform1i("cityMask", 1);
						planetShader.setUniform1i("blackouts", activeBlackouts);

						mc.getTextureManager().bindTexture(citylights[lightIntensity]);

						if(gl13) {
							GL13.glActiveTexture(GL13.GL_TEXTURE1);
							mc.renderEngine.bindTexture(metric.body.cityMask != null ? metric.body.cityMask : defaultMask);
							GL13.glActiveTexture(GL13.GL_TEXTURE0);
						}

						bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
						bufferBuilder.pos(-size, 100.0D, -size).tex(0.0D, 0.0D).endVertex();
						bufferBuilder.pos(size, 100.0D, -size).tex(1.0D, 0.0D).endVertex();
						bufferBuilder.pos(size, 100.0D, size).tex(1.0D, 1.0D).endVertex();
						bufferBuilder.pos(-size, 100.0D, size).tex(0.0D, 1.0D).endVertex();
						tessellator.draw();

						if(gl13) {
							GL13.glActiveTexture(GL13.GL_TEXTURE1);
							GlStateManager.bindTexture(mc.entityRenderer.lightmapTexture.getGlTextureId());
							GL13.glActiveTexture(GL13.GL_TEXTURE0);
						}

						GlStateManager.enableTexture2D();

						planetShader.stop();

						GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

						if(impact != null) {
							double lavaAlpha = Math.min(impactTime * 0.1, 1.0);

							double impactSize = (impactTime * 0.1) * size * 0.035;
							double impactAlpha = 1.0 - Math.min(1.0, impactTime * 0.0015);
							double flareSize = size * 1.5;
							double flareAlpha = 1.0 - Math.min(1.0, impactTime * 0.002);

							if(lavaAlpha > 0) {
								GlStateManager.color(1.0F, 1.0F, 1.0F, (float)lavaAlpha);
								mc.getTextureManager().bindTexture(impactTexture);

								bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
								bufferBuilder.pos(-size, 100.0D, -size).tex(0.0D + uvOffset, 0.0D).endVertex();
								bufferBuilder.pos(size, 100.0D, -size).tex(1.0D + uvOffset, 0.0D).endVertex();
								bufferBuilder.pos(size, 100.0D, size).tex(1.0D + uvOffset, 1.0D).endVertex();
								bufferBuilder.pos(-size, 100.0D, size).tex(0.0D + uvOffset, 1.0D).endVertex();
								tessellator.draw();
							}

							GlStateManager.pushMatrix();
							{

								GlStateManager.translate(-size * 0.5, 0, size * 0.4);

								// impact shockwave, increases in size and fades out
								if(impactAlpha > 0) {
									GlStateManager.color(1.0F, 1.0F, 1.0F, (float)impactAlpha);
									mc.getTextureManager().bindTexture(shockwaveTexture);

									bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
									bufferBuilder.pos(-impactSize, 100.0D, -impactSize).tex(0.0D, 0.0D).endVertex();
									bufferBuilder.pos(impactSize, 100.0D, -impactSize).tex(1.0D, 0.0D).endVertex();
									bufferBuilder.pos(impactSize, 100.0D, impactSize).tex(1.0D, 1.0D).endVertex();
									bufferBuilder.pos(-impactSize, 100.0D, impactSize).tex(0.0D, 1.0D).endVertex();
									tessellator.draw();
								}

								// impact flare, remains static in size and fades out
								if(flareAlpha > 0) {
									GlStateManager.color(1.0F, 1.0F, 1.0F, (float)flareAlpha);
									mc.getTextureManager().bindTexture(shockFlareTexture);

									bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
									bufferBuilder.pos(-flareSize, 100.0D, -flareSize).tex(0.0D, 0.0D).endVertex();
									bufferBuilder.pos(flareSize, 100.0D, -flareSize).tex(1.0D, 0.0D).endVertex();
									bufferBuilder.pos(flareSize, 100.0D, flareSize).tex(1.0D, 1.0D).endVertex();
									bufferBuilder.pos(-flareSize, 100.0D, flareSize).tex(0.0D, 1.0D).endVertex();
									tessellator.draw();
								}

							}
							GlStateManager.popMatrix();
						}


						GlStateManager.disableTexture2D();

						// Draw another layer on top to blend with the atmosphere
						GlStateManager.color((float)(planetTint.x - blendDarken), (float)(planetTint.y - blendDarken), (float)(planetTint.z - blendDarken), (1 - blendAmount * visibility));

						bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
						bufferBuilder.pos(-size, 100.0D, -size).tex(0.0D, 0.0D).endVertex();
						bufferBuilder.pos(size, 100.0D, -size).tex(1.0D, 0.0D).endVertex();
						bufferBuilder.pos(size, 100.0D, size).tex(1.0D, 1.0D).endVertex();
						bufferBuilder.pos(-size, 100.0D, size).tex(0.0D, 1.0D).endVertex();
						tessellator.draw();

						GlStateManager.enableTexture2D();
					}

					// Draw the front half of the ring (unobscured)
					if(metric.body.hasRings) {
						GlStateManager.color(metric.body.ringColor[0], metric.body.ringColor[1], metric.body.ringColor[2], visibility);
						mc.getTextureManager().bindTexture(ringTexture);

						double ringSize = size * metric.body.ringSize;

						GlStateManager.disableCull();

						GlStateManager.translate(0.0F, 100.0F, 0.0F);
						GlStateManager.rotate((float)-metric.angle, 0.0F, 0.0F, 1.0F);
						GlStateManager.rotate(90.0F - metric.body.ringTilt, 1.0F, 0.0F, 0.0F);
						GlStateManager.rotate((float)metric.angle, 0.0F, 1.0F, 0.0F);

						bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
						bufferBuilder.pos(-ringSize, 0, 0).tex(0.0D, 0.5D).endVertex();
						bufferBuilder.pos(ringSize, 0, 0).tex(1.0D, 0.5D).endVertex();
						bufferBuilder.pos(ringSize, 0, ringSize).tex(1.0D, 1.0D).endVertex();
						bufferBuilder.pos(-ringSize, 0, ringSize).tex(0.0D, 1.0D).endVertex();
						tessellator.draw();

						GlStateManager.enableCull();
					}
				}

				if(renderPoint) {
					float alpha = MathHelper.clamp((float)size * 100.0F, 0.0F, 1.0F);
					alpha *= 1 - BobMathUtil.remap01_clamp((float)size, (float)transitionMinSize, (float)transitionMaxSize);
					GlStateManager.color(metric.body.color[0], metric.body.color[1], metric.body.color[2], alpha * visibility);
					mc.getTextureManager().bindTexture(planetTexture);

					bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
					bufferBuilder.pos(-1.0D, 100.0D, -1.0D).tex(0.0D, 0.0D).endVertex();
					bufferBuilder.pos(1.0D, 100.0D, -1.0D).tex(1.0D, 0.0D).endVertex();
					bufferBuilder.pos(1.0D, 100.0D, 1.0D).tex(1.0D, 1.0D).endVertex();
					bufferBuilder.pos(-1.0D, 100.0D, 1.0D).tex(0.0D, 1.0D).endVertex();
					tessellator.draw();
				}
			}
			GlStateManager.popMatrix();
		}
	}

	protected void renderDigamma(WorldClient world, Minecraft mc, float solarAngle) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();

		GlStateManager.pushMatrix();
		{

			double var12 = 1.0D + world.rand.nextFloat() * 0.5D;
			double dist = 100.0D;

			if(ModEventHandlerClient.renderLodeStar) {
				GlStateManager.pushMatrix();
				GlStateManager.rotate(-75.0F, 1.0F, 0.0F, 0.0F);
				GlStateManager.rotate(10.0F, 0.0F, 1.0F, 0.0F);
				mc.getTextureManager().bindTexture(lodeStar); // genu-ine bona-fide ass whooping

				bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
				bufferBuilder.pos(-var12, dist, -var12).tex(0.0D, 0.0D).endVertex();
				bufferBuilder.pos(var12, dist, -var12).tex(0.0D, 1.0D).endVertex();
				bufferBuilder.pos(var12, dist, var12).tex(1.0D, 1.0D).endVertex();
				bufferBuilder.pos(-var12, dist, var12).tex(1.0D, 0.0D).endVertex();
				tessellator.draw();

				GlStateManager.popMatrix();
			}

			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);

			float brightness = (float) Math.sin(solarAngle * Math.PI);
			brightness *= brightness;
			GlStateManager.color(brightness, brightness, brightness, brightness);
			GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(solarAngle * 360.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(140.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(-40.0F, 0.0F, 0.0F, 1.0F);

			mc.getTextureManager().bindTexture(digammaStar);

			double digamma = HbmLivingProps.getDigamma(Minecraft.getMinecraft().player);
			var12 = 1.0D + digamma * 0.25D;
			dist = 100.0D - digamma * 2.5D;

			bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
			bufferBuilder.pos(-var12, dist, -var12).tex(0.0D, 0.0D).endVertex();
			bufferBuilder.pos(var12, dist, -var12).tex(0.0D, 1.0D).endVertex();
			bufferBuilder.pos(var12, dist, var12).tex(1.0D, 1.0D).endVertex();
			bufferBuilder.pos(-var12, dist, var12).tex(1.0D, 0.0D).endVertex();
			tessellator.draw();

		}
		GlStateManager.popMatrix();
	}

	// Does anyone even play with 3D glasses anymore?
	protected float[] applyAnaglyph(float... colors) {
		float r = (colors[0] * 30.0F + colors[1] * 59.0F + colors[2] * 11.0F) / 100.0F;
		float g = (colors[0] * 30.0F + colors[1] * 70.0F) / 100.0F;
		float b = (colors[0] * 30.0F + colors[2] * 70.0F) / 100.0F;

		return new float[] { r, g, b };
	}

	protected void renderSatellite(Minecraft mc, float celestialAngle, long seed, float[] color) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();

		double ticks = (double)(System.currentTimeMillis() % (600 * 50)) / 50;

		GlStateManager.pushMatrix();
		{
			GlStateManager.rotate(celestialAngle * -360.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(-40.0F + (float)(seed % 800) * 0.1F - 5.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate((float)(seed % 50) * 0.1F - 20.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate((float)(seed % 80) * 0.1F - 2.5F, 0.0F, 0.0F, 1.0F);
			GlStateManager.rotate((float)((ticks / 600.0D) * 360.0D), 1.0F, 0.0F, 0.0F);

			GlStateManager.color(color[0], color[1], color[2], 1F);

			mc.getTextureManager().bindTexture(planetTexture);

			float size = 0.5F;

			bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
			bufferBuilder.pos(-size, 100.0, -size).tex(0.0D, 0.0D).endVertex();
			bufferBuilder.pos(size, 100.0, -size).tex(0.0D, 1.0D).endVertex();
			bufferBuilder.pos(size, 100.0, size).tex(1.0D, 1.0D).endVertex();
			bufferBuilder.pos(-size, 100.0, size).tex(1.0D, 0.0D).endVertex();
			tessellator.draw();
		}
		GlStateManager.popMatrix();
	}

	// is just drawing a big cube with UVs prepared to draw a gradient
	private void renderSkyboxSide(Tessellator tessellator, BufferBuilder bufferBuilder, int side) {
		double u = side % 3 / 3.0D;
		double v = (double) side / 3 / 2.0D;
		bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		bufferBuilder.pos(-100.0D, -100.0D, -100.0D).tex(u, v).endVertex();
		bufferBuilder.pos(-100.0D, -100.0D, 100.0D).tex(u, v + 0.5D).endVertex();
		bufferBuilder.pos(100.0D, -100.0D, 100.0D).tex(u + 0.3333333333333333D, v + 0.5D).endVertex();
		bufferBuilder.pos(100.0D, -100.0D, -100.0D).tex(u + 0.3333333333333333D, v).endVertex();
		tessellator.draw();
	}

	protected void renderStation(Minecraft mc, OrbitalStation station, float solarAngle) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();

		long seed = station.dX * 1024L + station.dZ;

		double ticks = (double)(System.currentTimeMillis() % (1600 * 50)) / 50;

		GlStateManager.pushMatrix();
		{

			GlStateManager.rotate(solarAngle * -360.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(-40.0F + (float)(seed % 800) * 0.1F - 5.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate((float)(seed % 50) * 0.1F - 20.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate((float)(seed % 80) * 0.1F - 2.5F, 0.0F, 0.0F, 1.0F);
			GlStateManager.rotate((float)((ticks / 1600.0D) * -360.0D), 1.0F, 0.0F, 0.0F);

			GlStateManager.color(0.8F, 1.0F, 1.0F, 1.0F);

			mc.renderEngine.bindTexture(stationTexture);

			float size = 0.8F;

			buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
			buffer.pos(-size, 100.0, -size).tex(0.0D, 0.0D).endVertex();
			buffer.pos(size, 100.0, -size).tex(0.0D, 1.0D).endVertex();
			buffer.pos(size, 100.0, size).tex(1.0D, 1.0D).endVertex();
			buffer.pos(-size, 100.0, size).tex(1.0D, 0.0D).endVertex();
			tessellator.draw();

		}
		GlStateManager.popMatrix();
	}

}
