package com.hbmspace.render.shader;

import com.hbmspace.main.SpaceMain;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.FloatBuffer;

public class OptifineCompat {

    private static boolean initialized = false;
    private static Method isShaders = null;

    public static boolean shadersEnabled() {
        if(!initialized) {
            initialized = true;

            try {
                isShaders = Class.forName("net.optifine.Config").getMethod("isShaders");
            } catch(Throwable ex) {
                try {
                    isShaders = Class.forName("Config").getMethod("isShaders");
                } catch(Throwable ex2) {
                    isShaders = null;
                }
            }
        }

        if(isShaders == null) return false;

        try {
            return (Boolean)isShaders.invoke(null);
        } catch(Throwable ex) {
            isShaders = null;
            return false;
        }
    }

    private static boolean hooksInitialized = false;
    private static Method preSkyListMethod;
    private static Method preCelestialRotateMethod;
    private static Method postCelestialRotateMethod;
    private static Method setSkyColorMethod;
    private static Method enableTexture2DMethod;
    private static Method disableTexture2DMethod;
    private static Method enableFogMethod;
    private static Method disableFogMethod;
    private static Method useProgramMethod;
    private static Field programSkyTexturedField;

    private static void initHooks() {
        if(hooksInitialized) return;
        hooksInitialized = true;

        try {
            Class<?> shaders = Class.forName("net.optifine.shaders.Shaders");

            preSkyListMethod = shaders.getMethod("preSkyList");
            preCelestialRotateMethod = shaders.getMethod("preCelestialRotate");
            postCelestialRotateMethod = shaders.getMethod("postCelestialRotate");
            setSkyColorMethod = shaders.getMethod("setSkyColor", Vec3d.class);
            enableTexture2DMethod = shaders.getMethod("enableTexture2D");
            disableTexture2DMethod = shaders.getMethod("disableTexture2D");
            enableFogMethod = shaders.getMethod("enableFog");
            disableFogMethod = shaders.getMethod("disableFog");

            Class<?> programClass = Class.forName("net.optifine.shaders.Program");
            useProgramMethod = shaders.getMethod("useProgram", programClass);
            programSkyTexturedField = shaders.getField("ProgramSkyTextured");
        } catch(Throwable ex) {
            SpaceMain.logger.warn("OptiFine sky hooks unavailable, the sky will not match the shader pack");
            SpaceMain.logger.catching(ex);
        }
    }

    private static void invoke(Method method, Object... args) {
        if(method == null) return;

        try {
            method.invoke(null, args);
        } catch(Throwable ex) {
            SpaceMain.logger.catching(ex);
        }
    }

    private static Field uniformMoonPositionField;
    private static Field moonPositionField;
    private static final FloatBuffer modelViewBuffer = BufferUtils.createFloatBuffer(16);

    // OptiFine derives moonPosition as exactly opposite sunPosition, so the pack's moon glow and
    // night lighting can't follow a moon on its own orbit. Overwrite it with the real direction.
    // problem is, which pack even uses moon's actual position instead of setting it 180 degrees to the sun...
    public static void setMoonPositionFromModelView() {
        if(!shadersEnabled()) return;
        initHooks();

        try {
            if(uniformMoonPositionField == null) {
                Class<?> shaders = Class.forName("net.optifine.shaders.Shaders");
                uniformMoonPositionField = shaders.getField("uniform_moonPosition");
                moonPositionField = shaders.getDeclaredField("moonPosition");
                moonPositionField.setAccessible(true);
            }

            modelViewBuffer.clear();
            GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelViewBuffer);

            float x = modelViewBuffer.get(4) * 100.0F;
            float y = modelViewBuffer.get(5) * 100.0F;
            float z = modelViewBuffer.get(6) * 100.0F;

            float[] moonPosition = (float[])moonPositionField.get(null);
            moonPosition[0] = x;
            moonPosition[1] = y;
            moonPosition[2] = z;

            Object uniform = uniformMoonPositionField.get(null);
            uniform.getClass().getMethod("setValue", float.class, float.class, float.class).invoke(uniform, x, y, z);
        } catch(Throwable ex) {
            SpaceMain.logger.catching(ex);
        }
    }

    private static void useProgram(Field programField) {
        if(!shadersEnabled()) return;
        initHooks();
        if(useProgramMethod == null || programField == null) return;

        try {
            useProgramMethod.invoke(null, programField.get(null));
        } catch(Throwable ex) {
            SpaceMain.logger.catching(ex);
        }
    }

    public static void useSkyTexturedProgram() {
        useProgram(programSkyTexturedField);
    }

    // GlStateManager caches texture2D, so when the state is already correct it skips the glDisable
    // and OptiFine's program never swaps between skybasic and skytextured. RenderGlobal calls these
    // explicitly after every bus.z()/bus.y() for exactly this reason.
    public static void enableTexture2D() {
        if(!shadersEnabled()) return;
        initHooks();
        invoke(enableTexture2DMethod);
    }

    public static void disableTexture2D() {
        if(!shadersEnabled()) return;
        initHooks();
        invoke(disableTexture2DMethod);
    }

    public static void enableFog() {
        if(!shadersEnabled()) return;
        initHooks();
        invoke(enableFogMethod);
    }

    public static void disableFog() {
        if(!shadersEnabled()) return;
        initHooks();
        invoke(disableFogMethod);
    }

    // Feeds the pack's skyColor uniform, pass the colour the dome is actually drawn with
    public static void setSkyColor(Vec3d color) {
        if(!shadersEnabled()) return;
        initHooks();
        invoke(setSkyColorMethod, color);
    }

    // setUpPosition + OptiFine's horizon band. Without this upPosition stays (0,0,0) and the
    // pack's normalize(upPosition) returns NaN for every sky pixel
    public static void preSkyList() {
        if(!shadersEnabled()) return;
        initHooks();
        invoke(preSkyListMethod);
    }

    // Applies the pack's sunPathRotation to the matrix
    public static void preCelestialRotate() {
        if(!shadersEnabled()) return;
        initHooks();
        invoke(preCelestialRotateMethod);
    }

    // Reads the modelview back to derive sunPosition/moonPosition/shadowLightPosition
    public static void postCelestialRotate() {
        if(!shadersEnabled()) return;
        initHooks();
        invoke(postCelestialRotateMethod);
    }
}