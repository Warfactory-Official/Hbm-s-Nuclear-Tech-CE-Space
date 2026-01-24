package com.hbmspace.util;

public class RTGSpaceUtil {
    /**
     * Gets the lifespan of an RTG based on half-life
     * @author UFFR
     * @param halfLife The half-life
     * @param type Half-life units: {@link HalfLifeType}
     * @param realYears Whether or not to use 365 days per year instead of 100 to calculate time
     * @return The half-life calculated into Minecraft ticks
     */
    public static long getLifespan(float halfLife, HalfLifeType type, boolean realYears) {
        float life = switch (type) {
            case LONG -> (48000 * (realYears ? 365 : 100) * 100) * halfLife;
            case MEDIUM -> (48000 * (realYears ? 365 : 100)) * halfLife;
            case SHORT -> 48000 * halfLife;
        };
        return (long) life;
    }

    public enum HalfLifeType
    {
        /** Counted in days **/
        SHORT,
        /** Counted in years **/
        MEDIUM,
        /** Counted in hundreds of years **/
        LONG
    }
}
