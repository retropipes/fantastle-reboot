/*  Fantastle: A Maze-Solving Game
Copyright (C) 2008-2010 Eric Ahnell

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

Any questions should be directed to the author via email at: fantastle@worldwizard.net
 */
package com.puttysoftware.fantastlereboot.game;

import com.puttysoftware.diane.utilties.DirectionResolver;
import com.puttysoftware.fantastlereboot.assets.SoundGroup;
import com.puttysoftware.fantastlereboot.assets.SoundIndex;
import com.puttysoftware.fantastlereboot.loaders.SoundPlayer;
import com.puttysoftware.fantastlereboot.maze.Maze;
import com.puttysoftware.fantastlereboot.maze.MazeManager;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObjectModel;
import com.puttysoftware.fantastlereboot.objectmodel.Layers;
import com.puttysoftware.fantastlereboot.objects.OpenSpace;
import com.puttysoftware.fantastlereboot.objects.Wall;
import com.puttysoftware.fantastlereboot.objects.temporary.ArrowFactory;
import com.puttysoftware.fantastlereboot.objects.temporary.ArrowType;

public class ArrowTask extends Thread {
  // Fields
  private int x, y;
  private final ArrowType at;

  // Constructors
  public ArrowTask(final int newX, final int newY, final ArrowType newAT) {
    this.x = newX;
    this.y = newY;
    this.at = newAT;
  }

  @Override
  public void run() {
    boolean res = true;
    final Maze m = MazeManager.getMaze();
    final int px = m.getPlayerLocationX();
    final int py = m.getPlayerLocationY();
    final int pz = m.getPlayerLocationZ();
    final int[] mod = Game.doEffects(this.x, this.y);
    this.x = mod[0];
    this.y = mod[1];
    int cumX = this.x;
    int cumY = this.y;
    final int incX = this.x;
    final int incY = this.y;
    m.tickTimers(pz);
    FantastleObjectModel o = null;
    if (m.cellRangeCheck(px + cumX, py + cumY, pz)) {
      o = m.getCell(px + cumX, py + cumY, pz, Layers.OBJECT);
    } else {
      o = new Wall();
    }
    final FantastleObjectModel a = ArrowFactory.createArrow(this.at,
        DirectionResolver.resolve(incX, incY));
    SoundPlayer.playSound(SoundIndex.ARROW_SHOOT, SoundGroup.GAME);
    while (!o.isDirectionallySolid(incX, incY)) {
      res = ArrowTask.arrowHitCheck(px + cumX, py + cumY, pz);
      if (!res) {
        break;
      }
      if (!o.isDirectionallySolid(incX, incY)) {
        Game.redrawOneSquare(px + cumX, py + cumY, a);
      }
      Game.redrawOneSquare(px + cumX, py + cumY, new OpenSpace());
      cumX += incX;
      cumY += incY;
      if (m.cellRangeCheck(px + cumX, py + cumY, pz)) {
        o = m.getCell(px + cumX, py + cumY, pz, Layers.OBJECT);
      } else {
        o = new Wall();
      }
    }
    SoundPlayer.playSound(SoundIndex.ARROW_DIE, SoundGroup.GAME);
    Game.arrowDone();
  }

  private static boolean arrowHitCheck(final int inX, final int inY,
      final int pz) {
    final Maze maze = MazeManager.getMaze();
    return maze.cellRangeCheck(inX, inY, pz)
        && !maze.getCell(inX, inY, pz, Layers.OBJECT).isSolid();
  }
}
