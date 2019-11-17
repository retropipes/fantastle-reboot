/*  FantastleReboot: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.creatures.party;

import com.puttysoftware.fantastlereboot.creatures.Creature;
import com.puttysoftware.fantastlereboot.creatures.StatConstants;
import com.puttysoftware.fantastlereboot.creatures.faiths.Faith;
import com.puttysoftware.fantastlereboot.creatures.jobs.Job;
import com.puttysoftware.fantastlereboot.creatures.jobs.JobManager;
import com.puttysoftware.fantastlereboot.creatures.races.Race;
import com.puttysoftware.fantastlereboot.creatures.races.RaceConstants;
import com.puttysoftware.fantastlereboot.game.Game;
import com.puttysoftware.fantastlereboot.gui.PreferencesManager;
import com.puttysoftware.fantastlereboot.items.ItemInventory;
import com.puttysoftware.fantastlereboot.loaders.AvatarImageLoader;
import com.puttysoftware.fantastlereboot.maze.GenerateTask;
import com.puttysoftware.fantastlereboot.spells.SpellBook;
import com.puttysoftware.images.BufferedImageIcon;
import com.puttysoftware.page.Page;

public class PartyMember extends Creature {
  // Fields
  private final String name;
  private int permanentAttack;
  private int permanentDefense;
  private int permanentHP;
  private int permanentMP;
  private int kills;
  private int avatarFamilyID;
  private int avatarHairID;
  private int avatarSkinID;
  private static final int START_GOLD = 0;
  private static final double BASE_COEFF = 10.0;

  // Constructors
  PartyMember(final ItemInventory ii, final Race r, final Job j, final Faith f,
      final String n) {
    super(ii, Creature.TEAM_PARTY, f, j, r);
    this.name = n;
    this.permanentAttack = 0;
    this.permanentDefense = 0;
    this.permanentHP = 0;
    this.permanentMP = 0;
    this.kills = 0;
    this.avatarFamilyID = 0;
    this.avatarHairID = 0;
    this.avatarSkinID = 0;
    this.setLevel(1);
    this.setStrength(StatConstants.GAIN_STRENGTH
        + r.getAttribute(RaceConstants.ATTRIBUTE_STRENGTH_PER_LEVEL));
    this.setBlock(StatConstants.GAIN_BLOCK
        + r.getAttribute(RaceConstants.ATTRIBUTE_BLOCK_PER_LEVEL));
    this.setVitality(StatConstants.GAIN_VITALITY
        + r.getAttribute(RaceConstants.ATTRIBUTE_VITALITY_PER_LEVEL));
    this.setIntelligence(StatConstants.GAIN_INTELLIGENCE
        + r.getAttribute(RaceConstants.ATTRIBUTE_INTELLIGENCE_PER_LEVEL));
    this.setAgility(StatConstants.GAIN_AGILITY
        + r.getAttribute(RaceConstants.ATTRIBUTE_AGILITY_PER_LEVEL));
    this.setLuck(StatConstants.GAIN_LUCK
        + r.getAttribute(RaceConstants.ATTRIBUTE_LUCK_PER_LEVEL));
    this.setAttacksPerRound(1);
    this.setSpellsPerRound(1);
    this.healAndRegenerateFully();
    this.setGold(PartyMember.START_GOLD);
    this.setExperience(0L);
    final Page nextLevelEquation = new Page(3, 1, 0, true);
    final double value = BASE_COEFF;
    nextLevelEquation.setCoefficient(1, value);
    nextLevelEquation.setCoefficient(2, value);
    nextLevelEquation.setCoefficient(3, value);
    this.setToNextLevel(nextLevelEquation);
    this.setSpellBook(JobManager.getSpellBookByID(j.getJobID()));
  }

  // Methods
  public String getXPString() {
    return this.getExperience() + "/" + this.getToNextLevelValue();
  }

  // Transformers
  @Override
  protected void onLevelUp() {
    Race r = this.getRace();
    this.offsetStrength(StatConstants.GAIN_STRENGTH
        + r.getAttribute(RaceConstants.ATTRIBUTE_STRENGTH_PER_LEVEL));
    this.offsetBlock(StatConstants.GAIN_BLOCK
        + r.getAttribute(RaceConstants.ATTRIBUTE_BLOCK_PER_LEVEL));
    this.offsetVitality(StatConstants.GAIN_VITALITY
        + r.getAttribute(RaceConstants.ATTRIBUTE_VITALITY_PER_LEVEL));
    this.offsetIntelligence(StatConstants.GAIN_INTELLIGENCE
        + r.getAttribute(RaceConstants.ATTRIBUTE_INTELLIGENCE_PER_LEVEL));
    this.offsetAgility(StatConstants.GAIN_AGILITY
        + r.getAttribute(RaceConstants.ATTRIBUTE_AGILITY_PER_LEVEL));
    this.offsetLuck(StatConstants.GAIN_LUCK
        + r.getAttribute(RaceConstants.ATTRIBUTE_LUCK_PER_LEVEL));
    this.healAndRegenerateFully();
  }

  public void loadPartyMember(final int newLevel, final int chp, final int cmp,
      final int newGold, final int newLoad, final long newExperience,
      final int bookID, final boolean[] known, final int k, final int pAtk,
      final int pDef, final int pHP, final int pMP, final int aFamily,
      final int aSkin, final int aHair) {
    this.setLevel(newLevel);
    this.setCurrentHP(chp);
    this.setCurrentMP(cmp);
    this.setGold(newGold);
    this.setLoad(newLoad);
    this.setExperience(newExperience);
    this.kills = k;
    this.permanentAttack = pAtk;
    this.permanentDefense = pDef;
    this.permanentHP = pHP;
    this.permanentMP = pMP;
    this.avatarFamilyID = aFamily;
    this.avatarSkinID = aSkin;
    this.avatarHairID = aHair;
    final SpellBook book = JobManager.getSpellBookByID(bookID);
    for (int x = 0; x < known.length; x++) {
      if (known[x]) {
        book.learnSpellByID(x);
      }
    }
    this.setSpellBook(book);
  }

  public int getKills() {
    return this.kills;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public int getSpeed() {
    final int difficulty = PreferencesManager.getGameDifficulty();
    final int base = this.getBaseSpeed();
    if (difficulty == PreferencesManager.DIFFICULTY_VERY_EASY) {
      return (int) (base * SPEED_ADJUST_FASTEST);
    } else {
      if (difficulty == PreferencesManager.DIFFICULTY_EASY) {
        return (int) (base * SPEED_ADJUST_FAST);
      } else {
        if (difficulty == PreferencesManager.DIFFICULTY_NORMAL) {
          return (int) (base * SPEED_ADJUST_NORMAL);
        } else {
          if (difficulty == PreferencesManager.DIFFICULTY_HARD) {
            return (int) (base * SPEED_ADJUST_SLOW);
          } else {
            if (difficulty == PreferencesManager.DIFFICULTY_VERY_HARD) {
              return (int) (base * SPEED_ADJUST_SLOWEST);
            } else {
              return (int) (base * SPEED_ADJUST_NORMAL);
            }
          }
        }
      }
    }
  }

  public void initPostKill(final Race r, final Job j, final Faith f) {
    this.setRace(r);
    this.setJob(j);
    this.setFaith(f);
    this.setLevel(1);
    this.setStrength(StatConstants.GAIN_STRENGTH
        + r.getAttribute(RaceConstants.ATTRIBUTE_STRENGTH_PER_LEVEL));
    this.setBlock(StatConstants.GAIN_BLOCK
        + r.getAttribute(RaceConstants.ATTRIBUTE_BLOCK_PER_LEVEL));
    this.setVitality(StatConstants.GAIN_VITALITY
        + r.getAttribute(RaceConstants.ATTRIBUTE_VITALITY_PER_LEVEL));
    this.setIntelligence(StatConstants.GAIN_INTELLIGENCE
        + r.getAttribute(RaceConstants.ATTRIBUTE_INTELLIGENCE_PER_LEVEL));
    this.setAgility(StatConstants.GAIN_AGILITY
        + r.getAttribute(RaceConstants.ATTRIBUTE_AGILITY_PER_LEVEL));
    this.setLuck(StatConstants.GAIN_LUCK
        + r.getAttribute(RaceConstants.ATTRIBUTE_LUCK_PER_LEVEL));
    this.setAttacksPerRound(1);
    this.setSpellsPerRound(1);
    this.healAndRegenerateFully();
    this.setGold(PartyMember.START_GOLD);
    this.setExperience(0L);
    this.getItems().resetInventory();
    Game.deactivateAllEffects();
    final Page nextLevelEquation = new Page(3, 1, 0, true);
    final double value = BASE_COEFF;
    nextLevelEquation.setCoefficient(1, value);
    nextLevelEquation.setCoefficient(2, value);
    nextLevelEquation.setCoefficient(3, value);
    this.setToNextLevel(nextLevelEquation);
    this.setSpellBook(JobManager.getSpellBookByID(j.getJobID()));
    PartyManager.getParty().resetMonsterLevel();
    new GenerateTask(true).start();
  }

  @Override
  public int getAttack() {
    return super.getAttack() + this.getPermanentAttackPoints();
  }

  @Override
  public int getDefense() {
    return super.getDefense() + this.getPermanentDefensePoints();
  }

  @Override
  public int getMaximumHP() {
    return super.getMaximumHP() + this.getPermanentHPPoints();
  }

  @Override
  public int getMaximumMP() {
    return super.getMaximumMP() + this.getPermanentMPPoints();
  }

  public int getAvatarFamilyID() {
    return this.avatarFamilyID;
  }

  public int getAvatarSkinID() {
    return this.avatarSkinID;
  }

  public int getAvatarHairID() {
    return this.avatarHairID;
  }

  public int getPermanentAttackPoints() {
    return this.permanentAttack;
  }

  public int getPermanentDefensePoints() {
    return this.permanentDefense;
  }

  public int getPermanentHPPoints() {
    return this.permanentHP;
  }

  public int getPermanentMPPoints() {
    return this.permanentMP;
  }

  public void spendPointOnAttack() {
    this.kills++;
    this.permanentAttack++;
  }

  public void spendPointOnDefense() {
    this.kills++;
    this.permanentDefense++;
  }

  public void spendPointOnHP() {
    this.kills++;
    this.permanentHP++;
  }

  public void spendPointOnMP() {
    this.kills++;
    this.permanentMP++;
  }

  @Override
  public void onKillEnemy() {
  }

  @Override
  public void onAnnihilateEnemy() {
  }

  @Override
  public void onGotKilled() {
  }

  @Override
  public void onGotAnnihilated() {
  }

  @Override
  protected BufferedImageIcon getInitialImage() {
    return AvatarImageLoader.load(this.avatarFamilyID, this.avatarSkinID,
        this.avatarHairID);
  }

  @Override
  public void loadCreature() {
    // Do nothing
  }
}
