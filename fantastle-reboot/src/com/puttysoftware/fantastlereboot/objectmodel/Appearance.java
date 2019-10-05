/*  Fantastle Reboot
 * A maze-solving RPG
 * This code is licensed under the terms of the
 * GPLv3, or at your option, any later version.
 */
package com.puttysoftware.fantastlereboot.objectmodel;

import com.puttysoftware.diane.loaders.ColorShader;
import com.puttysoftware.fantastlereboot.assets.GameImage;
import com.puttysoftware.images.BufferedImageIcon;

abstract class Appearance {
    private final String cacheName;
    private final GameImage whichImage;
    private final ColorShader shading;

    public Appearance(final String name, final GameImage imageIndex) {
        this.cacheName = name;
        this.whichImage = imageIndex;
        this.shading = null;
    }

    public Appearance(final String name, final GameImage imageIndex, final ColorShader shader) {
        this.cacheName = name;
        this.whichImage = imageIndex;
        this.shading = shader;
    }

    public final String getCacheName() {
        return this.cacheName;
    }

    protected final GameImage getWhichImage() {
        return this.whichImage;
    }

    public final boolean hasShading() {
        return this.shading != null;
    }

    public final ColorShader getShading() {
        return this.shading;
    }

    public abstract BufferedImageIcon getImage();
}
