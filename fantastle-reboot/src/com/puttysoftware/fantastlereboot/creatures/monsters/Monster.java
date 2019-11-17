/*  FantastleReboot: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.creatures.monsters;

import com.puttysoftware.fantastlereboot.ai.map.AbstractMapAIRoutine;
import com.puttysoftware.fantastlereboot.ai.map.MapAIRoutinePicker;
import com.puttysoftware.fantastlereboot.ai.window.AbstractWindowAIRoutine;
import com.puttysoftware.fantastlereboot.ai.window.WindowAIRoutinePicker;
import com.puttysoftware.fantastlereboot.creatures.Creature;
import com.puttysoftware.fantastlereboot.creatures.faiths.FaithManager;
import com.puttysoftware.fantastlereboot.creatures.jobs.JobManager;
import com.puttysoftware.fantastlereboot.creatures.party.PartyManager;
import com.puttysoftware.fantastlereboot.creatures.party.PartyMember;
import com.puttysoftware.fantastlereboot.creatures.races.RaceManager;
import com.puttysoftware.fantastlereboot.gui.PreferencesManager;
import com.puttysoftware.fantastlereboot.items.ItemInventory;
import com.puttysoftware.fantastlereboot.items.Shop;
import com.puttysoftware.fantastlereboot.loaders.MonsterImageLoader;
import com.puttysoftware.fantastlereboot.loaders.MonsterNames;
import com.puttysoftware.fantastlereboot.spells.SpellBook;
import com.puttysoftware.fantastlereboot.spells.SpellBookManager;
import com.puttysoftware.images.BufferedImageIcon;
import com.puttysoftware.randomrange.RandomLongRange;
import com.puttysoftware.randomrange.RandomRange;

public final class Monster extends Creature {
  // Fields
  private String name;
  // Constants
  protected static final double MINIMUM_EXPERIENCE_RANDOM_VARIANCE = -5.0 / 2.0;
  protected static final double MAXIMUM_EXPERIENCE_RANDOM_VARIANCE = 5.0 / 2.0;
  protected static final int PERFECT_GOLD_MIN = 1;
  protected static final int PERFECT_GOLD_MAX = 3;
  private static final int BATTLES_SCALE_FACTOR = 2;
  private static final int BATTLES_START = 2;
  private static final int STAT_MULT_VERY_EASY = 2;
  private static final int STAT_MULT_EASY = 3;
  private static final int STAT_MULT_NORMAL = 5;
  private static final int STAT_MULT_HARD = 7;
  private static final int STAT_MULT_VERY_HARD = 9;
  private static final double GOLD_MULT_VERY_EASY = 2.0;
  private static final double GOLD_MULT_EASY = 1.5;
  private static final double GOLD_MULT_NORMAL = 1.0;
  private static final double GOLD_MULT_HARD = 0.75;
  private static final double GOLD_MULT_VERY_HARD = 0.5;
  private static final double EXP_MULT_VERY_EASY = 1.2;
  private static final double EXP_MULT_EASY = 1.1;
  private static final double EXP_MULT_NORMAL = 1.0;
  private static final double EXP_MULT_HARD = 0.9;
  private static final double EXP_MULT_VERY_HARD = 0.8;

  // Constructors
  Monster() {
    super(new ItemInventory(), 1, FaithManager.getRandomFaith(),
        JobManager.getRandomJob(), RaceManager.getRandomRace());
    this.setWindowAI(Monster.getInitialWindowAI());
    this.setMapAI(Monster.getInitialMapAI());
    final SpellBook spells = SpellBookManager
        .getEnemySpellBookByID(PreferencesManager.getGameDifficulty());
    spells.learnAllSpells();
    this.setSpellBook(spells);
    this.image = this.getInitialImage();
  }

  // Methods
  @Override
  public String getName() {
    return this.name;
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
  protected final int getInitialPerfectBonusGold() {
    final int tough = this.getToughness();
    final int min = tough * Monster.PERFECT_GOLD_MIN;
    final int max = tough * Monster.PERFECT_GOLD_MAX;
    final RandomRange r = new RandomRange(min, max);
    return (int) (r.generate() * this.adjustForLevelDifference());
  }

  @Override
  public int getSpeed() {
    final int difficulty = PreferencesManager.getGameDifficulty();
    final int base = this.getBaseSpeed();
    if (difficulty == PreferencesManager.DIFFICULTY_VERY_EASY) {
      return (int) (base * SPEED_ADJUST_SLOWEST);
    } else {
      if (difficulty == PreferencesManager.DIFFICULTY_EASY) {
        return (int) (base * SPEED_ADJUST_SLOW);
      } else {
        if (difficulty == PreferencesManager.DIFFICULTY_NORMAL) {
          return (int) (base * SPEED_ADJUST_NORMAL);
        } else {
          if (difficulty == PreferencesManager.DIFFICULTY_HARD) {
            return (int) (base * SPEED_ADJUST_FAST);
          } else {
            if (difficulty == PreferencesManager.DIFFICULTY_VERY_HARD) {
              return (int) (base * SPEED_ADJUST_FASTEST);
            } else {
              return (int) (base * SPEED_ADJUST_NORMAL);
            }
          }
        }
      }
    }
  }

  private int getToughness() {
    return this.getStrength() + this.getBlock() + this.getAgility()
        + this.getVitality() + this.getIntelligence() + this.getLuck();
  }

  final String getType() {
    return this.name;
  }

  final void setType(final String newType) {
    this.name = newType;
  }

  protected double adjustForLevelDifference() {
    return Math.max(0.0, this.getLevelDifference() / 4.0 + 1.0);
  }

  // Helper Methods
  private static AbstractWindowAIRoutine getInitialWindowAI() {
    return WindowAIRoutinePicker.getNextRoutine();
  }

  private static AbstractMapAIRoutine getInitialMapAI() {
    return MapAIRoutinePicker.getNextRoutine();
  }

  protected final int getBattlesToNextLevel() {
    return Monster.BATTLES_START
        + (this.getLevel() + 1) * Monster.BATTLES_SCALE_FACTOR;
  }

  @Override
  protected BufferedImageIcon getInitialImage() {
    if (this.getLevel() == 0) {
      return null;
    } else {
      final String[] types = MonsterNames.getAllNames();
      final RandomRange r = new RandomRange(0, types.length - 1);
      int imageID = r.generate();
      this.setType(types[imageID]);
      return MonsterImageLoader.load(imageID, this.getFaith());
    }
  }

  @Override
  public void loadCreature() {
    final int newLevel = PartyManager.getParty().getMonsterLevel() + 1;
    this.setLevel(newLevel);
    this.setVitality(this.getInitialVitality());
    this.setCurrentHP(this.getMaximumHP());
    this.setIntelligence(this.getInitialIntelligence());
    this.setCurrentMP(this.getMaximumMP());
    this.setStrength(this.getInitialStrength());
    this.setBlock(this.getInitialBlock());
    this.setAgility(this.getInitialAgility());
    this.setLuck(this.getInitialLuck());
    this.setGold(this.getInitialGold());
    this.setExperience(
        (long) (this.getInitialExperience() * this.adjustForLevelDifference()));
    this.setAttacksPerRound(1);
    this.setSpellsPerRound(1);
    this.image = this.getInitialImage();
  }

  private int getInitialStrength() {
    final RandomRange r = new RandomRange(1,
        Math.max(this.getLevel() * getStatMultiplierForDifficulty(), 1));
    return r.generate();
  }

  private int getInitialBlock() {
    final RandomRange r = new RandomRange(0,
        this.getLevel() * getStatMultiplierForDifficulty());
    return r.generate();
  }

  private long getInitialExperience() {
    int minvar, maxvar;
    minvar = (int) (this.getLevel()
        * Monster.MINIMUM_EXPERIENCE_RANDOM_VARIANCE);
    maxvar = (int) (this.getLevel()
        * Monster.MAXIMUM_EXPERIENCE_RANDOM_VARIANCE);
    final RandomLongRange r = new RandomLongRange(minvar, maxvar);
    final long expbase = PartyManager.getParty().getPartyMaxToNextLevel();
    final long factor = this.getBattlesToNextLevel();
    return (int) ((expbase / factor) + r.generate()
        * this.adjustForLevelDifference() * getExpMultiplierForDifficulty());
  }

  private int getInitialGold() {
    final PartyMember playerCharacter = PartyManager.getParty().getLeader();
    final int needed = Shop.getEquipmentCost(playerCharacter.getLevel() + 1)
        * 4;
    final int factor = this.getBattlesToNextLevel();
    final int min = 0;
    final int max = (needed / factor) * 2;
    final RandomRange r = new RandomRange(min, max);
    return (int) (r.generate() * this.adjustForLevelDifference()
        * getGoldMultiplierForDifficulty());
  }

  private int getInitialAgility() {
    final RandomRange r = new RandomRange(1,
        Math.max(this.getLevel() * getStatMultiplierForDifficulty(), 1));
    return r.generate();
  }

  private int getInitialVitality() {
    final RandomRange r = new RandomRange(1,
        Math.max(this.getLevel() * getStatMultiplierForDifficulty(), 1));
    return r.generate();
  }

  private int getInitialIntelligence() {
    final RandomRange r = new RandomRange(0,
        this.getLevel() * getStatMultiplierForDifficulty());
    return r.generate();
  }

  private int getInitialLuck() {
    final RandomRange r = new RandomRange(0,
        this.getLevel() * getStatMultiplierForDifficulty());
    return r.generate();
  }

  private static int getStatMultiplierForDifficulty() {
    final int difficulty = PreferencesManager.getGameDifficulty();
    if (difficulty == PreferencesManager.DIFFICULTY_VERY_EASY) {
      return STAT_MULT_VERY_EASY;
    } else {
      if (difficulty == PreferencesManager.DIFFICULTY_EASY) {
        return STAT_MULT_EASY;
      } else {
        if (difficulty == PreferencesManager.DIFFICULTY_NORMAL) {
          return STAT_MULT_NORMAL;
        } else {
          if (difficulty == PreferencesManager.DIFFICULTY_HARD) {
            return STAT_MULT_HARD;
          } else {
            if (difficulty == PreferencesManager.DIFFICULTY_VERY_HARD) {
              return STAT_MULT_VERY_HARD;
            } else {
              return STAT_MULT_NORMAL;
            }
          }
        }
      }
    }
  }

  private static double getGoldMultiplierForDifficulty() {
    final int difficulty = PreferencesManager.getGameDifficulty();
    if (difficulty == PreferencesManager.DIFFICULTY_VERY_EASY) {
      return GOLD_MULT_VERY_EASY;
    } else {
      if (difficulty == PreferencesManager.DIFFICULTY_EASY) {
        return GOLD_MULT_EASY;
      } else {
        if (difficulty == PreferencesManager.DIFFICULTY_NORMAL) {
          return GOLD_MULT_NORMAL;
        } else {
          if (difficulty == PreferencesManager.DIFFICULTY_HARD) {
            return GOLD_MULT_HARD;
          } else {
            if (difficulty == PreferencesManager.DIFFICULTY_VERY_HARD) {
              return GOLD_MULT_VERY_HARD;
            } else {
              return GOLD_MULT_NORMAL;
            }
          }
        }
      }
    }
  }

  private static double getExpMultiplierForDifficulty() {
    final int difficulty = PreferencesManager.getGameDifficulty();
    if (difficulty == PreferencesManager.DIFFICULTY_VERY_EASY) {
      return EXP_MULT_VERY_EASY;
    } else {
      if (difficulty == PreferencesManager.DIFFICULTY_EASY) {
        return EXP_MULT_EASY;
      } else {
        if (difficulty == PreferencesManager.DIFFICULTY_NORMAL) {
          return EXP_MULT_NORMAL;
        } else {
          if (difficulty == PreferencesManager.DIFFICULTY_HARD) {
            return EXP_MULT_HARD;
          } else {
            if (difficulty == PreferencesManager.DIFFICULTY_VERY_HARD) {
              return EXP_MULT_VERY_HARD;
            } else {
              return EXP_MULT_NORMAL;
            }
          }
        }
      }
    }
  }
}
