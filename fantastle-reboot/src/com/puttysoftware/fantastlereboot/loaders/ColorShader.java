/*  TallerTower: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.loaders;

import java.awt.Color;
import java.awt.color.ColorSpace;
import java.util.Objects;

public class ColorShader {
    // Fields
    private final Color shadeColor;
    private final String shadeName;
    private static final int MIN_SHADE = 0;
    private static final int MAX_SHADE = 255;

    // Constructor
    public ColorShader(final String name, final int red, final int green,
            final int blue) {
        float fixedRed = Math.max(ColorShader.MIN_SHADE, red)
                % ColorShader.MAX_SHADE / (float) 255.0;
        float fixedGreen = Math.max(ColorShader.MIN_SHADE, green)
                % ColorShader.MAX_SHADE / (float) 255.0;
        float fixedBlue = Math.max(ColorShader.MIN_SHADE, blue)
                % ColorShader.MAX_SHADE / (float) 255.0;
        this.shadeColor = new Color(
                ColorSpace.getInstance(ColorSpace.CS_sRGB),
                new float[] { fixedRed, fixedGreen, fixedBlue }, (float) 1.0);
        this.shadeName = name;
    }

    // Methods
    public String getName() {
        return this.shadeName;
    }

    public Color applyShade(final Color source) {
        if (source.getAlpha() != 255) {
            return source;
        }
        ColorSpace linear = ColorSpace.getInstance(ColorSpace.CS_LINEAR_RGB);
        float[] inputColor = source.getColorComponents(linear, null);
        float[] linearShade = this.shadeColor.getColorComponents(linear, null);
        float[] outputColor = ColorShader.doColorMath(inputColor, linearShade);
        return ColorShader.convertFromLinearRGB(outputColor);
    }

    private static float[] doColorMath(float[] inputColor, float[] linearShade) {
        float[] outputColor = new float[3];
        for (int c = 0; c < 3; c++) {
            outputColor[c] = inputColor[c] * (1 - linearShade[c]);
        }
        return outputColor;
    }

    private static Color convertFromLinearRGB(final float[] colorvalue) {
        ColorSpace sourceSpace = ColorSpace.getInstance(ColorSpace.CS_LINEAR_RGB);
        float[] colorvalueCIEXYZ = sourceSpace.toCIEXYZ(colorvalue);
        ColorSpace targetSpace = ColorSpace.getInstance(ColorSpace.CS_sRGB);
        float[] colorvalueTarget = targetSpace.fromCIEXYZ(colorvalueCIEXYZ);
        return new Color(targetSpace, colorvalueTarget, (float)1.0);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.shadeColor, this.shadeName);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ColorShader)) {
            return false;
        }
        ColorShader other = (ColorShader) obj;
        return Objects.equals(this.shadeColor, other.shadeColor)
                && Objects.equals(this.shadeName, other.shadeName);
    }
}
