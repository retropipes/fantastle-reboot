/*  FantastleReboot: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.creatures.monsters;

import com.puttysoftware.fantastlereboot.ai.map.MapAIRoutinePicker;
import com.puttysoftware.fantastlereboot.assets.SoundGroup;
import com.puttysoftware.fantastlereboot.assets.SoundIndex;
import com.puttysoftware.fantastlereboot.creatures.Creature;
import com.puttysoftware.fantastlereboot.creatures.faiths.FaithManager;
import com.puttysoftware.fantastlereboot.creatures.jobs.JobManager;
import com.puttysoftware.fantastlereboot.creatures.party.PartyManager;
import com.puttysoftware.fantastlereboot.creatures.races.RaceManager;
import com.puttysoftware.fantastlereboot.gui.Prefs;
import com.puttysoftware.fantastlereboot.items.ItemInventory;
import com.puttysoftware.fantastlereboot.loaders.BossImageLoader;
import com.puttysoftware.fantastlereboot.loaders.SoundPlayer;
import com.puttysoftware.fantastlereboot.spells.SpellBook;
import com.puttysoftware.fantastlereboot.spells.books.BossSpellBook;
import com.puttysoftware.images.BufferedImageIcon;
import com.puttysoftware.randomrange.RandomRange;

public class BossMonster extends Creature {
  // Fields
  private static final int MINIMUM_STAT_VALUE_VERY_EASY = 200;
  private static final int MINIMUM_STAT_VALUE_EASY = 400;
  private static final int MINIMUM_STAT_VALUE_NORMAL = 500;
  private static final int MINIMUM_STAT_VALUE_HARD = 600;
  private static final int MINIMUM_STAT_VALUE_VERY_HARD = 800;
  private static final int STAT_MULT_VERY_EASY = 3;
  private static final int STAT_MULT_EASY = 5;
  private static final int STAT_MULT_NORMAL = 6;
  private static final int STAT_MULT_HARD = 7;
  private static final int STAT_MULT_VERY_HARD = 9;
  private static final int FAITH_ID = 9;
  private static final int JOB_ID = 3;
  private static final int RACE_ID = 1;

  // Constructors
  BossMonster() {
    super(new ItemInventory(), Creature.TEAM_BOSS,
        FaithManager.getFaith(BossMonster.FAITH_ID),
        JobManager.getJob(BossMonster.JOB_ID),
        RaceManager.getRace(BossMonster.RACE_ID));
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
    SoundPlayer.playSound(SoundIndex.BOSS_DIE, SoundGroup.BATTLE);
  }

  @Override
  public void onGotAnnihilated() {
    SoundPlayer.playSound(SoundIndex.BOSS_DIE, SoundGroup.BATTLE);
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
    this.setInitialStats();
    this.setGold(0);
    this.setExperience(0);
    this.setAttacksPerRound(BossMonster.getInitialAttacksPerRound());
    this.setSpellsPerRound(BossMonster.getInitialSpellsPerRound());
    this.image = this.getInitialImage();
  }

  private static int getInitialAttacksPerRound() {
    final int difficulty = Prefs.getGameDifficulty();
    if (difficulty == Prefs.DIFFICULTY_VERY_EASY) {
      return 1;
    } else {
      if (difficulty == Prefs.DIFFICULTY_EASY) {
        return 1;
      } else {
        if (difficulty == Prefs.DIFFICULTY_NORMAL) {
          return 2;
        } else {
          if (difficulty == Prefs.DIFFICULTY_HARD) {
            return 2;
          } else {
            if (difficulty == Prefs.DIFFICULTY_VERY_HARD) {
              return 3;
            } else {
              return 2;
            }
          }
        }
      }
    }
  }

  private static int getInitialSpellsPerRound() {
    final int difficulty = Prefs.getGameDifficulty();
    if (difficulty == Prefs.DIFFICULTY_VERY_EASY) {
      return 1;
    } else {
      if (difficulty == Prefs.DIFFICULTY_EASY) {
        return 2;
      } else {
        if (difficulty == Prefs.DIFFICULTY_NORMAL) {
          return 2;
        } else {
          if (difficulty == Prefs.DIFFICULTY_HARD) {
            return 3;
          } else {
            if (difficulty == Prefs.DIFFICULTY_VERY_HARD) {
              return 3;
            } else {
              return 2;
            }
          }
        }
      }
    }
  }

  @Override
  protected int getInitialStrength() {
    final int min = BossMonster.getMinimumStatForDifficulty();
    final RandomRange r = new RandomRange(min, Math.max(
        this.getLevel() * BossMonster.getStatMultiplierForDifficulty(), min));
    return r.generate() + super.getInitialStrength();
  }

  @Override
  protected int getInitialBlock() {
    final int min = BossMonster.getMinimumStatForDifficulty();
    final RandomRange r = new RandomRange(min, Math.max(
        this.getLevel() * BossMonster.getStatMultiplierForDifficulty(), min));
    return r.generate() + super.getInitialBlock();
  }

  @Override
  protected int getInitialAgility() {
    final int min = BossMonster.getMinimumStatForDifficulty();
    final RandomRange r = new RandomRange(min, Math.max(
        this.getLevel() * BossMonster.getStatMultiplierForDifficulty(), min));
    return r.generate() + super.getInitialAgility();
  }

  @Override
  protected int getInitialVitality() {
    final int min = BossMonster.getMinimumStatForDifficulty();
    final RandomRange r = new RandomRange(min, Math.max(
        this.getLevel() * BossMonster.getStatMultiplierForDifficulty(), min));
    return r.generate() + super.getInitialVitality();
  }

  @Override
  protected int getInitialIntelligence() {
    final int min = BossMonster.getMinimumStatForDifficulty();
    final RandomRange r = new RandomRange(min, Math.max(
        this.getLevel() * BossMonster.getStatMultiplierForDifficulty(), min));
    return r.generate() + super.getInitialIntelligence();
  }

  @Override
  protected int getInitialLuck() {
    final int min = BossMonster.getMinimumStatForDifficulty();
    final RandomRange r = new RandomRange(min, Math.max(
        this.getLevel() * BossMonster.getStatMultiplierForDifficulty(), min));
    return r.generate() + super.getInitialLuck();
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
