/*  FantastleReboot: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.creatures.monsters;

import com.puttysoftware.fantastlereboot.ai.map.MapAIRoutinePicker;
import com.puttysoftware.fantastlereboot.ai.window.AbstractWindowAIRoutine;
import com.puttysoftware.fantastlereboot.ai.window.VeryHardWindowAIRoutine;
import com.puttysoftware.fantastlereboot.creatures.Creature;
import com.puttysoftware.fantastlereboot.creatures.faiths.FaithManager;
import com.puttysoftware.fantastlereboot.creatures.jobs.JobManager;
import com.puttysoftware.fantastlereboot.creatures.party.PartyManager;
import com.puttysoftware.fantastlereboot.creatures.races.RaceManager;
import com.puttysoftware.fantastlereboot.gui.Prefs;
import com.puttysoftware.fantastlereboot.items.ItemInventory;
import com.puttysoftware.fantastlereboot.loaders.BossImageLoader;
import com.puttysoftware.fantastlereboot.spells.SpellBook;
import com.puttysoftware.fantastlereboot.spells.books.BossSpellBook;
import com.puttysoftware.images.BufferedImageIcon;
import com.puttysoftware.randomrange.RandomRange;

public class BossMonster extends Creature {
  // Fields
  private static final int MINIMUM_STAT_VALUE_VERY_EASY = 100;
  private static final int MINIMUM_STAT_VALUE_EASY = 200;
  private static final int MINIMUM_STAT_VALUE_NORMAL = 400;
  private static final int MINIMUM_STAT_VALUE_HARD = 600;
  private static final int MINIMUM_STAT_VALUE_VERY_HARD = 900;
  private static final int STAT_MULT_VERY_EASY = 3;
  private static final int STAT_MULT_EASY = 4;
  private static final int STAT_MULT_NORMAL = 5;
  private static final int STAT_MULT_HARD = 8;
  private static final int STAT_MULT_VERY_HARD = 12;
  private static final int FAITH_ID = 9;
  private static final int JOB_ID = 3;
  private static final int RACE_ID = 1;

  // Constructors
  BossMonster() {
    super(new ItemInventory(), Creature.TEAM_BOSS,
        FaithManager.getFaith(BossMonster.FAITH_ID),
        JobManager.getJob(BossMonster.JOB_ID),
        RaceManager.getRace(BossMonster.RACE_ID));
    this.setWindowAI(BossMonster.getInitialWindowAI());
    this.setMapAI(MapAIRoutinePicker.getNextRoutine());
    final SpellBook spells = new BossSpellBook();
    spells.learnAllSpells();
    this.setSpellBook(spells);
    this.loadCreature();
  }

  // Methods
  @Override
  public String getFightingWhatString() {
    return "You're fighting The Boss";
  }

  @Override
  public String getName() {
    return "The Boss";
  }

  @Override
  public boolean checkLevelUp() {
    return false;
  }

  @Override
  public void onKillEnemy() {
    // Do nothing
  }

  @Override
  public void onAnnihilateEnemy() {
    // Do nothing
  }

  @Override
  public void onGotKilled() {
    // Do nothing
  }

  @Override
  public void onGotAnnihilated() {
    // Do nothing
  }

  @Override
  protected void onLevelUp() {
    // Do nothing
  }

  @Override
  protected BufferedImageIcon getInitialImage() {
    return BossImageLoader.load();
  }

  @Override
  public int getSpeed() {
    final int difficulty = Prefs.getGameDifficulty();
    final int base = this.getBaseSpeed();
    if (difficulty == Prefs.DIFFICULTY_VERY_EASY) {
      return (int) (base * Creature.SPEED_ADJUST_SLOWEST);
    } else {
      if (difficulty == Prefs.DIFFICULTY_EASY) {
        return (int) (base * Creature.SPEED_ADJUST_SLOW);
      } else {
        if (difficulty == Prefs.DIFFICULTY_NORMAL) {
          return (int) (base * Creature.SPEED_ADJUST_NORMAL);
        } else {
          if (difficulty == Prefs.DIFFICULTY_HARD) {
            return (int) (base * Creature.SPEED_ADJUST_FAST);
          } else {
            if (difficulty == Prefs.DIFFICULTY_VERY_HARD) {
              return (int) (base * Creature.SPEED_ADJUST_FASTEST);
            } else {
              return (int) (base * Creature.SPEED_ADJUST_NORMAL);
            }
          }
        }
      }
    }
  }

  // Helper Methods
  @Override
  public void loadCreature() {
    final int newLevel = PartyManager.getParty().getMonsterLevel() + 6;
    this.setLevel(newLevel);
    this.setVitality(this.getInitialVitality());
    this.setCurrentHP(this.getMaximumHP());
    this.setIntelligence(this.getInitialIntelligence());
    this.setCurrentMP(this.getMaximumMP());
    this.setStrength(this.getInitialStrength());
    this.setBlock(this.getInitialBlock());
    this.setAgility(this.getInitialAgility());
    this.setLuck(this.getInitialLuck());
    this.setGold(0);
    this.setExperience(0);
    this.setAttacksPerRound(1);
    this.setSpellsPerRound(1);
    this.image = this.getInitialImage();
    this.healAndRegenerateFully();
  }

  private int getInitialStrength() {
    final int min = BossMonster.getMinimumStatForDifficulty();
    final RandomRange r = new RandomRange(min, Math.max(
        this.getLevel() * BossMonster.getStatMultiplierForDifficulty(), min));
    return r.generate();
  }

  private int getInitialBlock() {
    final int min = BossMonster.getMinimumStatForDifficulty();
    final RandomRange r = new RandomRange(min, Math.max(
        this.getLevel() * BossMonster.getStatMultiplierForDifficulty(), min));
    return r.generate();
  }

  private int getInitialAgility() {
    final int min = BossMonster.getMinimumStatForDifficulty();
    final RandomRange r = new RandomRange(min, Math.max(
        this.getLevel() * BossMonster.getStatMultiplierForDifficulty(), min));
    return r.generate();
  }

  private int getInitialVitality() {
    final int min = BossMonster.getMinimumStatForDifficulty();
    final RandomRange r = new RandomRange(min, Math.max(
        this.getLevel() * BossMonster.getStatMultiplierForDifficulty(), min));
    return r.generate();
  }

  private int getInitialIntelligence() {
    final int min = BossMonster.getMinimumStatForDifficulty();
    final RandomRange r = new RandomRange(min, Math.max(
        this.getLevel() * BossMonster.getStatMultiplierForDifficulty(), min));
    return r.generate();
  }

  private int getInitialLuck() {
    final int min = BossMonster.getMinimumStatForDifficulty();
    final RandomRange r = new RandomRange(min, Math.max(
        this.getLevel() * BossMonster.getStatMultiplierForDifficulty(), min));
    return r.generate();
  }

  private static AbstractWindowAIRoutine getInitialWindowAI() {
    return new VeryHardWindowAIRoutine();
  }

  private static int getStatMultiplierForDifficulty() {
    final int difficulty = Prefs.getGameDifficulty();
    if (difficulty == Prefs.DIFFICULTY_VERY_EASY) {
      return BossMonster.STAT_MULT_VERY_EASY;
    } else {
      if (difficulty == Prefs.DIFFICULTY_EASY) {
        return BossMonster.STAT_MULT_EASY;
      } else {
        if (difficulty == Prefs.DIFFICULTY_NORMAL) {
          return BossMonster.STAT_MULT_NORMAL;
        } else {
          if (difficulty == Prefs.DIFFICULTY_HARD) {
            return BossMonster.STAT_MULT_HARD;
          } else {
            if (difficulty == Prefs.DIFFICULTY_VERY_HARD) {
              return BossMonster.STAT_MULT_VERY_HARD;
            } else {
              return BossMonster.STAT_MULT_NORMAL;
            }
          }
        }
      }
    }
  }

  private static int getMinimumStatForDifficulty() {
    final int difficulty = Prefs.getGameDifficulty();
    if (difficulty == Prefs.DIFFICULTY_VERY_EASY) {
      return BossMonster.MINIMUM_STAT_VALUE_VERY_EASY;
    } else {
      if (difficulty == Prefs.DIFFICULTY_EASY) {
        return BossMonster.MINIMUM_STAT_VALUE_EASY;
      } else {
        if (difficulty == Prefs.DIFFICULTY_NORMAL) {
          return BossMonster.MINIMUM_STAT_VALUE_NORMAL;
        } else {
          if (difficulty == Prefs.DIFFICULTY_HARD) {
            return BossMonster.MINIMUM_STAT_VALUE_HARD;
          } else {
            if (difficulty == Prefs.DIFFICULTY_VERY_HARD) {
              return BossMonster.MINIMUM_STAT_VALUE_VERY_HARD;
            } else {
              return BossMonster.MINIMUM_STAT_VALUE_NORMAL;
            }
          }
        }
      }
    }
  }
}
