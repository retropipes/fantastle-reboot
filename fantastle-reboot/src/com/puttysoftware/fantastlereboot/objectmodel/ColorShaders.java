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
  private static ColorShader PLASMA;
  private static ColorShader MAGNET;

  public static ColorShader wooden() {
    if (ColorShaders.WOODEN == null) {
      ColorShaders.WOODEN = new ColorShader("wooden",
          new Color(127, 63, 0, 255));
    }
    return ColorShaders.WOODEN;
  }

  public static ColorShader scorch() {
    if (ColorShaders.SCORCH == null) {
      ColorShaders.SCORCH = new ColorShader("scorch",
          new Color(255, 0, 0, 255));
    }
    return ColorShaders.SCORCH;
  }

  public static ColorShader freeze() {
    if (ColorShaders.FREEZE == null) {
      ColorShaders.FREEZE = new ColorShader("freeze",
          new Color(0, 255, 255, 255));
    }
    return ColorShaders.FREEZE;
  }

  public static ColorShader shadow() {
    if (ColorShaders.SHADOW == null) {
      ColorShaders.SHADOW = new ColorShader("shadow",
          new Color(47, 47, 47, 255));
    }
    return ColorShaders.SHADOW;
  }

  public static ColorShader vortex() {
    if (ColorShaders.VORTEX == null) {
      ColorShaders.VORTEX = new ColorShader("vortex",
          new Color(0, 255, 0, 255));
    }
    return ColorShaders.VORTEX;
  }

  public static ColorShader charge() {
    if (ColorShaders.CHARGE == null) {
      ColorShaders.CHARGE = new ColorShader("charge",
          new Color(255, 255, 0, 255));
    }
    return ColorShaders.CHARGE;
  }

  public static ColorShader sacred() {
    if (ColorShaders.SACRED == null) {
      ColorShaders.SACRED = new ColorShader("sacred",
          new Color(223, 223, 223, 255));
    }
    return ColorShaders.SACRED;
  }

  public static ColorShader beauty() {
    if (ColorShaders.BEAUTY == null) {
      ColorShaders.BEAUTY = new ColorShader("beauty",
          new Color(255, 0, 255, 255));
    }
    return ColorShaders.BEAUTY;
  }

  public static ColorShader liquid() {
    if (ColorShaders.LIQUID == null) {
      ColorShaders.LIQUID = new ColorShader("liquid",
          new Color(0, 0, 255, 255));
    }
    return ColorShaders.LIQUID;
  }

  public static ColorShader normal() {
    if (ColorShaders.NORMAL == null) {
      ColorShaders.NORMAL = new ColorShader("normal",
          new Color(127, 127, 127, 255));
    }
    return ColorShaders.NORMAL;
  }

  public static ColorShader poison() {
    if (ColorShaders.POISON == null) {
      ColorShaders.POISON = new ColorShader("poison",
          new Color(63, 0, 127, 255));
    }
    return ColorShaders.POISON;
  }

  public static ColorShader plasma() {
    if (ColorShaders.PLASMA == null) {
      ColorShaders.PLASMA = new ColorShader("plasma",
          new Color(63, 255, 127, 255));
    }
    return ColorShaders.PLASMA;
  }

  public static ColorShader magnet() {
    if (ColorShaders.MAGNET == null) {
      ColorShaders.MAGNET = new ColorShader("magnet",
          new Color(255, 63, 0, 255));
    }
    return ColorShaders.MAGNET;
  }

  private ColorShaders() {
    // Do nothing
  }
}
