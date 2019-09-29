package com.puttysoftware.fantastlereboot.battle.map;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import com.puttysoftware.fantastlereboot.loaders.ImageLoader;
import com.puttysoftware.fantastlereboot.obsolete.DrawGrid;

public class MapBattleDraw extends JPanel {
    private static final long serialVersionUID = 35935343464625L;
    private final DrawGrid drawGrid;

    public MapBattleDraw(final DrawGrid grid) {
        super();
        this.drawGrid = grid;
        final int vSize = MapBattleViewingWindowManager.getViewingWindowSize();
        final int gSize = ImageLoader.getImageSize();
        this.setPreferredSize(new Dimension(vSize * gSize, vSize * gSize));
    }

    @Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);
        if (this.drawGrid != null) {
            final int gSize = ImageLoader.getImageSize();
            final int vSize = MapBattleViewingWindowManager
                    .getViewingWindowSize();
            for (int x = 0; x < vSize; x++) {
                for (int y = 0; y < vSize; y++) {
                    g.drawImage(this.drawGrid.getImageCell(y, x), x * gSize,
                            y * gSize, gSize, gSize, null);
                }
            }
        }
    }
}
