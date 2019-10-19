package com.puttysoftware.fantastlereboot.objectmodel;

import java.awt.Color;

import com.puttysoftware.diane.loaders.ColorShader;

public final class ColorShaders {
    private static ColorShader WOODEN;
    private static ColorShader SCORCH;
    private static ColorShader FREEZE;
    private static ColorShader SHADOW;
    private static ColorShader VORTEX;
    private static ColorShader CHARGE;
    private static ColorShader SACRED;
    private static ColorShader BEAUTY;
    private static ColorShader LIQUID;
    private static ColorShader NORMAL;
    private static ColorShader POISON;

    public static ColorShader liquid() {
        if (LIQUID == null) {
            LIQUID = new ColorShader("liquid", new Color(0, 0, 255, 255));
        }
        return LIQUID;
    }

    public static ColorShader freeze() {
        if (FREEZE == null) {
            FREEZE = new ColorShader("freeze", new Color(0, 255, 255, 255));
        }
        return FREEZE;
    }

    public static ColorShader shadow() {
        if (SHADOW == null) {
            SHADOW = new ColorShader("shadow", new Color(47, 47, 47, 255));
        }
        return SHADOW;
    }

    public static ColorShader scorch() {
        if (SCORCH == null) {
            SCORCH = new ColorShader("scorch", new Color(255, 0, 0, 255));
        }
        return SCORCH;
    }

    public static ColorShader vortex() {
        if (VORTEX == null) {
            VORTEX = new ColorShader("vortex", new Color(0, 255, 0, 255));
        }
        return VORTEX;
    }

    public static ColorShader charge() {
        if (CHARGE == null) {
            CHARGE = new ColorShader("charge", new Color(255, 255, 0, 255));
        }
        return CHARGE;
    }

    public static ColorShader sacred() {
        if (SACRED == null) {
            SACRED = new ColorShader("sacred", new Color(223, 223, 223, 255));
        }
        return SACRED;
    }

    public static ColorShader beauty() {
        if (BEAUTY == null) {
            BEAUTY = new ColorShader("beauty", new Color(255, 0, 255, 255));
        }
        return BEAUTY;
    }

    public static ColorShader normal() {
        if (NORMAL == null) {
            NORMAL = new ColorShader("normal", new Color(255, 255, 255, 255));
        }
        return NORMAL;
    }

    public static ColorShader poison() {
        if (POISON == null) {
            POISON = new ColorShader("poison", new Color(63, 0, 127, 255));
        }
        return POISON;
    }

    public static ColorShader wooden() {
        if (WOODEN == null) {
            WOODEN = new ColorShader("wooden", new Color(127, 63, 0, 255));
        }
        return WOODEN;
    }

    private ColorShaders() {
        // Do nothing
    }
}
