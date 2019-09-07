package com.puttysoftware.fantastlereboot.battle.map;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import com.puttysoftware.fantastlereboot.loaders.older.BattleImageManager;
import com.puttysoftware.fantastlereboot.ttmain.DrawGrid;

public class MapBattleDraw extends JPanel {
    private static final long serialVersionUID = 35935343464625L;
    private final DrawGrid drawGrid;

    public MapBattleDraw(final DrawGrid grid) {
        super();
        this.drawGrid = grid;
        final int vSize = MapBattleViewingWindowManager.getViewingWindowSize();
        final int gSize = BattleImageManager.getGraphicSize();
        this.setPreferredSize(new Dimension(vSize * gSize, vSize * gSize));
    }

    @Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);
        if (this.drawGrid != null) {
            final int gSize = BattleImageManager.getGraphicSize();
            final int vSize = MapBattleViewingWindowManager
                    .getViewingWindowSize();
            for (int x = 0; x < vSize; x++) {
                for (int y = 0; y < vSize; y++) {
                    g.drawImage(this.drawGrid.getImageCell(y, x), x * gSize, y
                            * gSize, gSize, gSize, null);
                }
            }
        }
    }
}
