package com.puttysoftware.fantastlereboot.game;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.obsolete.DrawGrid;
import com.puttysoftware.fantastlereboot.obsolete.loaders.ImageTransformer;

class GameDraw extends JPanel {
    private static final long serialVersionUID = 35935343464625L;
    private transient DrawGrid drawGrid;

    public GameDraw(final DrawGrid grid) {
        super();
        this.drawGrid = grid;
        final int vSize = FantastleReboot.getBagOStuff().getPrefsManager().getViewingWindowSize();
        final int gSize = ImageTransformer.getGraphicSize();
        this.setPreferredSize(new Dimension(vSize * gSize, vSize * gSize));
    }

    @Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);
        if (this.drawGrid != null) {
            final int gSize = ImageTransformer.getGraphicSize();
            final int vSize = FantastleReboot.getBagOStuff().getPrefsManager().getViewingWindowSize();
            for (int x = 0; x < vSize; x++) {
                for (int y = 0; y < vSize; y++) {
                    g.drawImage(this.drawGrid.getImageCell(y, x), x * gSize, y
                            * gSize, gSize, gSize, null);
                }
            }
        }
    }
}
