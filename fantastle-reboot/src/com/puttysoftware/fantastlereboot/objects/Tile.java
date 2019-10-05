package com.puttysoftware.fantastlereboot.objects;

import com.puttysoftware.images.BufferedImageIcon;

public abstract class Tile {
    private Appearance editorAppearance;
    private Appearance gameAppearance;
    private Appearance battleAppearance;
    private final Appearance appearance;

    protected Tile(final Appearance look) {
        super();
        this.appearance = look;
    }

    protected final void setGameLook(final Appearance look) {
        this.gameAppearance = look;
    }

    protected final void setEditorLook(final Appearance look) {
        this.editorAppearance = look;
    }

    protected final void setBattleLook(final Appearance look) {
        this.battleAppearance = look;
    }

    public final BufferedImageIcon getImage() {
        return this.appearance.getImage();
    }

    public final BufferedImageIcon getGameImage() {
        return this.gameAppearance != null ? this.gameAppearance.getImage()
                : this.getImage();
    }

    public final BufferedImageIcon getEditorImage() {
        return this.editorAppearance != null ? this.editorAppearance.getImage()
                : this.getGameImage();
    }

    public final BufferedImageIcon getBattleImage() {
        return this.battleAppearance != null ? this.battleAppearance.getImage()
                : this.getGameImage();
    }
}
