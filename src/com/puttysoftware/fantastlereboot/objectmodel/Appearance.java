/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.fantastlereboot.objectmodel;

import org.retropipes.diane.asset.image.BufferedImageIcon;
import org.retropipes.diane.asset.image.ColorReplaceRules;
import org.retropipes.diane.asset.image.ColorShader;
import org.retropipes.diane.asset.image.DianeImageIndex;

public abstract class Appearance {
    private final String cacheName;
    private final DianeImageIndex whichImage;
    private final ColorShader shading;
    private final ColorReplaceRules replacements;

    public Appearance(final String name, final DianeImageIndex imageIndex) {
	this.cacheName = name;
	this.whichImage = imageIndex;
	this.shading = null;
	this.replacements = null;
    }

    public Appearance(final String name, final DianeImageIndex imageIndex, final ColorReplaceRules replaceRules) {
	this.cacheName = name;
	this.whichImage = imageIndex;
	this.shading = null;
	this.replacements = replaceRules;
    }

    public Appearance(final String name, final DianeImageIndex imageIndex, final ColorShader shader) {
	this.cacheName = name;
	this.whichImage = imageIndex;
	this.shading = shader;
	this.replacements = null;
    }

    public final String getCacheName() {
	return this.cacheName;
    }

    public abstract BufferedImageIcon getImage();

    public final ColorReplaceRules getReplacementRules() {
	return this.replacements;
    }

    public final ColorShader getShading() {
	return this.shading;
    }

    protected final DianeImageIndex getWhichImage() {
	return this.whichImage;
    }

    public final boolean hasReplacementRules() {
	return this.replacements != null;
    }

    public final boolean hasShading() {
	return this.shading != null;
    }
}
