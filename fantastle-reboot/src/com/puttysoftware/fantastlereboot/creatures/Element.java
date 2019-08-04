package com.puttysoftware.fantastlereboot.creatures;

import java.awt.Color;

import com.puttysoftware.fantastlereboot.creatures.faiths.Faith;
import com.puttysoftware.fantastlereboot.creatures.faiths.FaithConstants;

public class Element {
    // Fields
    private static Color makeTrans = new Color(200, 100, 100);
    private static Color eye = Color.BLACK;
    private Color transformedEye;
    private final double transformRed;
    private final double transformGreen;
    private final double transformBlue;
    private final String name;
    private final Faith faith;

    // Constructor
    public Element(final Faith f) {
        this.faith = f;
        this.transformRed = f.getColor().getRed() / 256.0;
        this.transformGreen = f.getColor().getGreen() / 256.0;
        this.transformBlue = f.getColor().getBlue() / 256.0;
        if (FaithConstants.FAITH_DARK_EYES[f.getFaithID()]) {
            this.transformedEye = Color.BLACK;
        } else {
            this.transformedEye = Color.WHITE;
        }
        this.name = f.getName();
    }

    // Methods
    public String getName() {
        return this.name;
    }

    public Faith getFaith() {
        return this.faith;
    }

    public Color applyTransform(final Color source) {
        final int red = source.getRed();
        final int green = source.getGreen();
        final int blue = source.getBlue();
        Color transformed = null;
        if (source.equals(Element.makeTrans)) {
            transformed = new Color(red, green, blue, 0);
        } else if (source.equals(Element.eye)) {
            transformed = this.transformedEye;
        } else {
            final int transformedRed = (int) (red * this.transformRed);
            final int transformedGreen = (int) (green * this.transformGreen);
            final int transformedBlue = (int) (blue * this.transformBlue);
            transformed = new Color(transformedRed, transformedGreen,
                    transformedBlue);
        }
        return transformed;
    }
}
