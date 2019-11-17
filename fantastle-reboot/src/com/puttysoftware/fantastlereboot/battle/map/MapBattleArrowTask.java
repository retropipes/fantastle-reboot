/*  FantastleReboot: A Maze-Solving Game
Copyright (C) 2008-2010 Eric Ahnell

Any questions should be directed to the author via email at: mazer5d@worldwizard.net
 */
package com.puttysoftware.fantastlereboot.battle.map;

import com.puttysoftware.diane.utilties.DirectionResolver;
import com.puttysoftware.fantastlereboot.BagOStuff;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.assets.SoundGroup;
import com.puttysoftware.fantastlereboot.assets.SoundIndex;
import com.puttysoftware.fantastlereboot.battle.Battle;
import com.puttysoftware.fantastlereboot.creatures.faiths.Faith;
import com.puttysoftware.fantastlereboot.creatures.faiths.FaithManager;
import com.puttysoftware.fantastlereboot.loaders.SoundPlayer;
import com.puttysoftware.fantastlereboot.maze.Maze;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObjectModel;
import com.puttysoftware.fantastlereboot.objectmodel.Layers;
import com.puttysoftware.fantastlereboot.objects.OpenSpace;
import com.puttysoftware.fantastlereboot.objects.Wall;
import com.puttysoftware.fantastlereboot.objects.temporary.ArrowFactory;
import com.puttysoftware.fantastlereboot.objects.temporary.ArrowType;
import com.puttysoftware.fantastlereboot.objects.temporary.BattleCharacter;

public class MapBattleArrowTask extends Thread {
  // Fields
  private final int x, y;
  private final Maze battleMaze;
  private final BattleCharacter active;

  // Constructors
  public MapBattleArrowTask(final int newX, final int newY, final Maze maze,
      final BattleCharacter ac) {
    this.x = newX;
    this.y = newY;
    this.battleMaze = maze;
    this.active = ac;
    this.setName("Arrow Handler");
  }

  @Override
  public void run() {
    try {
      boolean res = true;
      final BagOStuff app = FantastleReboot.getBagOStuff();
      final Maze m = this.battleMaze;
      final int px = this.active.getX();
      final int py = this.active.getY();
      int cumX = this.x;
      int cumY = this.y;
      final int incX = this.x;
      final int incY = this.y;
      FantastleObjectModel o = null;
      if (m.cellRangeCheck(px + cumX, py + cumY, 0)) {
        o = m.getCell(px + cumX, py + cumY, 0, Layers.OBJECT);
      } else {
        o = new Wall();
      }
      final int newDir = DirectionResolver.resolve(incX, incY);
      final FantastleObjectModel a = ArrowFactory.createArrow(ArrowType.WOODEN,
          newDir);
      SoundPlayer.playSound(SoundIndex.ARROW_SHOOT, SoundGroup.BATTLE);
      BattleCharacter hit = null;
      while (res) {
        // Check to see if the arrow hit anything
        res = app.getBattle().arrowHitCheck(px + cumX, py + cumY);
        if (res) {
          // Check to see if the arrow hit a creature
          if (o instanceof BattleCharacter) {
            // Arrow hit something
            res = false;
            // Arrow hit a creature, hurt it
            hit = (BattleCharacter) o;
            final Faith shooter = this.active.getCreature().getFaith();
            final Faith target = hit.getCreature().getFaith();
            final int damage = FaithManager.getFaithAdjustedDamage(
                shooter.getFaithID(), target.getFaithID(), 5);
            final Battle bl = app.getBattle();
            hit.getCreature().doDamage(damage);
            bl.setStatusMessage("Ow, " + target.getName() + " got shot!");
          }
        }
        if (!res) {
          break;
        }
        // Draw arrow
        app.getBattle().redrawOneBattleSquare(px + cumX, py + cumY, a);
        // Delay, for animation purposes
        Thread.sleep(MapBattleArrowSpeedConstants.getArrowSpeed());
        // Clear arrow
        app.getBattle().redrawOneBattleSquare(px + cumX, py + cumY,
            new OpenSpace());
        cumX += incX;
        cumY += incY;
        if (m.cellRangeCheck(px + cumX, py + cumY, 0)) {
          o = m.getCell(px + cumX, py + cumY, 0, Layers.OBJECT);
        } else {
          res = false;
        }
      }
      // Arrow has died
      SoundPlayer.playSound(SoundIndex.ARROW_DIE, SoundGroup.BATTLE);
      app.getBattle().arrowDone(hit);
    } catch (final Throwable t) {
      FantastleReboot.logError(t);
    }
  }
}
