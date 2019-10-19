package com.puttysoftware.fantastlereboot.objectmodel;

import java.awt.Color;

import com.puttysoftware.diane.loaders.ColorShader;

public final class ColorShaders {
    private static ColorShader WOOD;
//    private static ColorShader FIRE;
//    private static ColorShader COLD;
//    private static ColorShader DARK;
//    private static ColorShader GUST;
//    private static ColorShader JOLT;
//    private static ColorShader BEAM;
    private static ColorShader LOVE;
//    private static ColorShader AQUA;
    private static ColorShader NONE;

    public static ColorShader wood() {
        if (WOOD == null) {
            WOOD = new ColorShader("wood", new Color(127, 63, 0, 255));
        }
        return WOOD;
    }
//
//    public static ColorShader fire() {
//        if (FIRE == null) {
//            FIRE = new ColorShader("fire", new Color(255, 0, 0, 255));
//        }
//        return FIRE;
//    }
//
//    public static ColorShader cold() {
//        if (COLD == null) {
//            COLD = new ColorShader("cold", new Color(0, 255, 255, 255));
//        }
//        return COLD;
//    }
//
//    public static ColorShader dark() {
//        if (DARK == null) {
//            DARK = new ColorShader("dark", new Color(47, 47, 47, 255));
//        }
//        return DARK;
//    }
//
//    public static ColorShader gust() {
//        if (GUST == null) {
//            GUST = new ColorShader("gust", new Color(0, 255, 0, 255));
//        }
//        return GUST;
//    }
//
//    public static ColorShader jolt() {
//        if (JOLT == null) {
//            JOLT = new ColorShader("jolt", new Color(255, 255, 0, 255));
//        }
//        return JOLT;
//    }
//
//    public static ColorShader beam() {
//        if (BEAM == null) {
//            BEAM = new ColorShader("beam", new Color(223, 223, 223, 255));
//        }
//        return BEAM;
//    }

    public static ColorShader love() {
        if (LOVE == null) {
            LOVE = new ColorShader("love", new Color(255, 0, 255, 255));
        }
        return LOVE;
    }
//
//    public static ColorShader aqua() {
//        if (AQUA == null) {
//            AQUA = new ColorShader("aqua", new Color(0, 0, 255, 255));
//        }
//        return AQUA;
//    }

    public static ColorShader none() {
        if (NONE == null) {
            NONE = new ColorShader("none", new Color(255, 255, 255, 255));
        }
        return NONE;
    }

    private ColorShaders() {
        // Do nothing
    }
}
