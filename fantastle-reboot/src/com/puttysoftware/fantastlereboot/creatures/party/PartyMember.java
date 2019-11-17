/*  FantastleReboot: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.creatures.party;

import java.io.IOException;

import com.puttysoftware.fantastlereboot.creatures.Creature;
import com.puttysoftware.fantastlereboot.creatures.StatConstants;
import com.puttysoftware.fantastlereboot.creatures.faiths.Faith;
import com.puttysoftware.fantastlereboot.creatures.jobs.Job;
import com.puttysoftware.fantastlereboot.creatures.jobs.JobManager;
import com.puttysoftware.fantastlereboot.creatures.races.Race;
import com.puttysoftware.fantastlereboot.creatures.races.RaceConstants;
import com.puttysoftware.fantastlereboot.game.Game;
import com.puttysoftware.fantastlereboot.gui.PreferencesManager;
import com.puttysoftware.fantastlereboot.gui.VersionException;
import com.puttysoftware.fantastlereboot.items.ItemInventory;
import com.puttysoftware.fantastlereboot.loaders.AvatarImageLoader;
import com.puttysoftware.fantastlereboot.maze.GenerateTask;
import com.puttysoftware.fantastlereboot.spells.SpellBook;
import com.puttysoftware.images.BufferedImageIcon;
import com.puttysoftware.page.Page;
import com.puttysoftware.xio.XDataReader;
import com.puttysoftware.xio.XDataWriter;

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
  PartyMember(final Race r, final Job j, final Faith f, final String n) {
    super(true, 0, f, j, r);
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
  protected void levelUpHook() {
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

  private void loadPartyMember(final int newLevel, final int chp, final int cmp,
      final int newGold, final int newLoad, final long newExperience,
      final int bookID, final boolean[] known) {
    this.setLevel(newLevel);
    this.setCurrentHP(chp);
    this.setCurrentMP(cmp);
    this.setGold(newGold);
    this.setLoad(newLoad);
    this.setExperience(newExperience);
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

  public void onDeath(final int penalty) {
    this.offsetExperiencePercentage(penalty);
    this.healAndRegenerateFully();
    this.setGold(0);
  }

  public static PartyMember read(final XDataReader worldFile)
      throws IOException {
    final int version = worldFile.readByte();
    if (version < CharacterVersions.FORMAT_5) {
      throw new VersionException("Invalid character version found: " + version);
    }
    final int k = worldFile.readInt();
    final int pAtk = worldFile.readInt();
    final int pDef = worldFile.readInt();
    final int pHP = worldFile.readInt();
    final int pMP = worldFile.readInt();
    final int strength = worldFile.readInt();
    final int block = worldFile.readInt();
    final int agility = worldFile.readInt();
    final int vitality = worldFile.readInt();
    final int intelligence = worldFile.readInt();
    final int luck = worldFile.readInt();
    final int lvl = worldFile.readInt();
    final int cHP = worldFile.readInt();
    final int cMP = worldFile.readInt();
    final int gld = worldFile.readInt();
    final int apr = worldFile.readInt();
    final int spr = worldFile.readInt();
    final int load = worldFile.readInt();
    final long exp = worldFile.readLong();
    final int r = worldFile.readInt();
    final int j = worldFile.readInt();
    final int f = worldFile.readInt();
    final int max = worldFile.readInt();
    final boolean[] known = new boolean[max];
    for (int x = 0; x < max; x++) {
      known[x] = worldFile.readBoolean();
    }
    final String n = worldFile.readString();
    final PartyMember pm = PartyManager.getNewPCInstance(r, j, f, n);
    pm.setStrength(strength);
    pm.setBlock(block);
    pm.setAgility(agility);
    pm.setVitality(vitality);
    pm.setIntelligence(intelligence);
    pm.setLuck(luck);
    pm.setAttacksPerRound(apr);
    pm.setSpellsPerRound(spr);
    pm.setItems(ItemInventory.readItemInventory(worldFile, version));
    pm.kills = k;
    pm.permanentAttack = pAtk;
    pm.permanentDefense = pDef;
    pm.permanentHP = pHP;
    pm.permanentMP = pMP;
    pm.loadPartyMember(lvl, cHP, cMP, gld, load, exp, j, known);
    return pm;
  }

  public void write(final XDataWriter worldFile) throws IOException {
    worldFile.writeByte(CharacterVersions.FORMAT_LATEST);
    worldFile.writeInt(this.kills);
    worldFile.writeInt(this.getPermanentAttackPoints());
    worldFile.writeInt(this.getPermanentDefensePoints());
    worldFile.writeInt(this.getPermanentHPPoints());
    worldFile.writeInt(this.getPermanentMPPoints());
    worldFile.writeInt(this.getStrength());
    worldFile.writeInt(this.getBlock());
    worldFile.writeInt(this.getAgility());
    worldFile.writeInt(this.getVitality());
    worldFile.writeInt(this.getIntelligence());
    worldFile.writeInt(this.getLuck());
    worldFile.writeInt(this.getLevel());
    worldFile.writeInt(this.getCurrentHP());
    worldFile.writeInt(this.getCurrentMP());
    worldFile.writeInt(this.getGold());
    worldFile.writeInt(this.getAttacksPerRound());
    worldFile.writeInt(this.getSpellsPerRound());
    worldFile.writeInt(this.getLoad());
    worldFile.writeLong(this.getExperience());
    worldFile.writeInt(this.getRace().getRaceID());
    worldFile.writeInt(this.getJob().getJobID());
    worldFile.writeInt(this.getFaith().getFaithID());
    final int max = this.getSpellBook().getSpellCount();
    worldFile.writeInt(max);
    for (int x = 0; x < max; x++) {
      worldFile.writeBoolean(this.getSpellBook().isSpellKnown(x));
    }
    worldFile.writeString(this.getName());
    this.getItems().writeItemInventory(worldFile);
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
