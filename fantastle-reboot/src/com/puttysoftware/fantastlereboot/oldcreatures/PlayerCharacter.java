package com.puttysoftware.fantastlereboot.oldcreatures;

import com.puttysoftware.fantastlereboot.oldcreatures.castes.Caste;
import com.puttysoftware.fantastlereboot.oldcreatures.castes.CasteConstants;
import com.puttysoftware.fantastlereboot.oldcreatures.faiths.Faith;
import com.puttysoftware.fantastlereboot.oldcreatures.genders.Gender;
import com.puttysoftware.fantastlereboot.oldcreatures.genders.GenderConstants;
import com.puttysoftware.fantastlereboot.oldcreatures.personalities.Personality;
import com.puttysoftware.fantastlereboot.oldcreatures.personalities.PersonalityConstants;
import com.puttysoftware.fantastlereboot.oldcreatures.races.Race;
import com.puttysoftware.fantastlereboot.oldcreatures.races.RaceConstants;
import com.puttysoftware.fantastlereboot.spells.SpellBook;
import com.puttysoftware.fantastlereboot.spells.SpellBookManager;
import com.puttysoftware.images.BufferedImageIcon;
import com.puttysoftware.randomrange.RandomRange;

public class PlayerCharacter extends Creature {
    // Fields
    private final Race race;
    private final Caste caste;
    private final Gender gender;
    private final Faith faith;
    private final Personality personality;
    private int permanentAttack;
    private int permanentDefense;
    private int permanentHP;
    private int permanentMP;
    private int kills;
    private int bank;
    private long toNextLevel;
    private int monsterLevel;
    private final RandomRange rSTR, rBLK, rAGI, rVIT, rINT, rLUC;
    private final int mSTR, mBLK, mAGI, mVIT, mINT, mLUC;
    private static final int START_GOLD = 0;
    private static final int MIN_MONSTER_LEVEL = 1;
    private static final double FACTOR_PERMANENT_ATTACK_ATTACK = 1.0;
    private static final double FACTOR_PERMANENT_DEFENSE_DEFENSE = 1.0;

    // Constructors
    public PlayerCharacter(final Race r, final Caste c, final Faith f,
            final Gender g, final Personality p) {
        super();
        this.race = r;
        this.caste = c;
        this.gender = g;
        this.faith = f;
        this.personality = p;
        this.setLevel(1);
        this.permanentAttack = 0;
        this.permanentDefense = 0;
        this.permanentHP = 0;
        this.permanentMP = 0;
        this.kills = 0;
        final int STR = this.personality.getAttribute(
                PersonalityConstants.PERSONALITY_ATTRIBUTE_RANDOM_STRENGTH);
        this.rSTR = new RandomRange(-STR, STR);
        final int BLK = this.personality.getAttribute(
                PersonalityConstants.PERSONALITY_ATTRIBUTE_RANDOM_BLOCK);
        this.rBLK = new RandomRange(-BLK, BLK);
        final int AGI = this.personality.getAttribute(
                PersonalityConstants.PERSONALITY_ATTRIBUTE_RANDOM_AGILITY);
        this.rAGI = new RandomRange(-AGI, AGI);
        final int VIT = this.personality.getAttribute(
                PersonalityConstants.PERSONALITY_ATTRIBUTE_RANDOM_VITALITY);
        this.rVIT = new RandomRange(-VIT, VIT);
        final int INT = this.personality.getAttribute(
                PersonalityConstants.PERSONALITY_ATTRIBUTE_RANDOM_INTELLIGENCE);
        this.rINT = new RandomRange(-INT, INT);
        final int LUC = this.personality.getAttribute(
                PersonalityConstants.PERSONALITY_ATTRIBUTE_RANDOM_LUCK);
        this.rLUC = new RandomRange(-LUC, LUC);
        this.mSTR = this.gender.getAttribute(
                GenderConstants.GENDER_ATTRIBUTE_STRENGTH_MODIFIER);
        this.mBLK = this.gender
                .getAttribute(GenderConstants.GENDER_ATTRIBUTE_BLOCK_MODIFIER);
        this.mAGI = this.gender.getAttribute(
                GenderConstants.GENDER_ATTRIBUTE_AGILITY_MODIFIER);
        this.mVIT = this.gender.getAttribute(
                GenderConstants.GENDER_ATTRIBUTE_VITALITY_MODIFIER);
        this.mINT = this.gender.getAttribute(
                GenderConstants.GENDER_ATTRIBUTE_INTELLIGENCE_MODIFIER);
        this.mLUC = this.gender
                .getAttribute(GenderConstants.GENDER_ATTRIBUTE_LUCK_MODIFIER);
        this.setStrength(StatConstants.GAIN_STRENGTH + this.race
                .getAttribute(RaceConstants.RACE_ATTRIBUTE_STRENGTH_PER_LEVEL));
        this.setBlock(StatConstants.GAIN_BLOCK + this.race
                .getAttribute(RaceConstants.RACE_ATTRIBUTE_BLOCK_PER_LEVEL));
        this.setVitality(StatConstants.GAIN_VITALITY + this.race
                .getAttribute(RaceConstants.RACE_ATTRIBUTE_VITALITY_PER_LEVEL));
        this.setIntelligence(
                StatConstants.GAIN_INTELLIGENCE + this.race.getAttribute(
                        RaceConstants.RACE_ATTRIBUTE_INTELLIGENCE_PER_LEVEL));
        this.setAgility(StatConstants.GAIN_AGILITY + this.race
                .getAttribute(RaceConstants.RACE_ATTRIBUTE_AGILITY_PER_LEVEL));
        this.setLuck(StatConstants.GAIN_LUCK + this.race
                .getAttribute(RaceConstants.RACE_ATTRIBUTE_LUCK_PER_LEVEL));
        this.healAndRegenerateFully();
        this.setGold(PlayerCharacter.START_GOLD);
        this.bank = 0;
        this.setExperience(0L);
        this.toNextLevel = this.getExpToNextLevel(this.getLevel() + 1,
                this.kills);
        this.monsterLevel = this.getLevel();
        this.setSpellBook(
                SpellBookManager.getSpellBookByID(this.caste.getCasteID()));
    }

    public PlayerCharacter(final Race r, final Caste c, final Faith f,
            final Gender g, final Personality p, final int pAtk, final int pDef,
            final int pHP, final int pMP, final int k) {
        super();
        this.race = r;
        this.caste = c;
        this.gender = g;
        this.faith = f;
        this.personality = p;
        this.setLevel(1);
        this.permanentAttack = pAtk;
        this.permanentDefense = pDef;
        this.permanentHP = pHP;
        this.permanentMP = pMP;
        this.kills = k;
        final int STR = this.personality.getAttribute(
                PersonalityConstants.PERSONALITY_ATTRIBUTE_RANDOM_STRENGTH);
        this.rSTR = new RandomRange(-STR, STR);
        final int BLK = this.personality.getAttribute(
                PersonalityConstants.PERSONALITY_ATTRIBUTE_RANDOM_BLOCK);
        this.rBLK = new RandomRange(-BLK, BLK);
        final int AGI = this.personality.getAttribute(
                PersonalityConstants.PERSONALITY_ATTRIBUTE_RANDOM_AGILITY);
        this.rAGI = new RandomRange(-AGI, AGI);
        final int VIT = this.personality.getAttribute(
                PersonalityConstants.PERSONALITY_ATTRIBUTE_RANDOM_VITALITY);
        this.rVIT = new RandomRange(-VIT, VIT);
        final int INT = this.personality.getAttribute(
                PersonalityConstants.PERSONALITY_ATTRIBUTE_RANDOM_INTELLIGENCE);
        this.rINT = new RandomRange(-INT, INT);
        final int LUC = this.personality.getAttribute(
                PersonalityConstants.PERSONALITY_ATTRIBUTE_RANDOM_LUCK);
        this.rLUC = new RandomRange(-LUC, LUC);
        this.mSTR = this.gender.getAttribute(
                GenderConstants.GENDER_ATTRIBUTE_STRENGTH_MODIFIER);
        this.mBLK = this.gender
                .getAttribute(GenderConstants.GENDER_ATTRIBUTE_BLOCK_MODIFIER);
        this.mAGI = this.gender.getAttribute(
                GenderConstants.GENDER_ATTRIBUTE_AGILITY_MODIFIER);
        this.mVIT = this.gender.getAttribute(
                GenderConstants.GENDER_ATTRIBUTE_VITALITY_MODIFIER);
        this.mINT = this.gender.getAttribute(
                GenderConstants.GENDER_ATTRIBUTE_INTELLIGENCE_MODIFIER);
        this.mLUC = this.gender
                .getAttribute(GenderConstants.GENDER_ATTRIBUTE_LUCK_MODIFIER);
        this.setStrength(StatConstants.GAIN_STRENGTH
                + this.race.getAttribute(
                        RaceConstants.RACE_ATTRIBUTE_STRENGTH_PER_LEVEL)
                + this.mSTR + Math.min(0, this.rSTR.generate()));
        this.setBlock(StatConstants.GAIN_BLOCK
                + this.race.getAttribute(
                        RaceConstants.RACE_ATTRIBUTE_BLOCK_PER_LEVEL)
                + this.mBLK + Math.min(0, this.rBLK.generate()));
        this.setVitality(StatConstants.GAIN_VITALITY
                + this.race.getAttribute(
                        RaceConstants.RACE_ATTRIBUTE_VITALITY_PER_LEVEL)
                + this.mVIT + Math.min(0, this.rVIT.generate()));
        this.setIntelligence(StatConstants.GAIN_INTELLIGENCE
                + this.race.getAttribute(
                        RaceConstants.RACE_ATTRIBUTE_INTELLIGENCE_PER_LEVEL)
                + this.mINT + Math.min(0, this.rINT.generate()));
        this.setAgility(StatConstants.GAIN_AGILITY
                + this.race.getAttribute(
                        RaceConstants.RACE_ATTRIBUTE_AGILITY_PER_LEVEL)
                + this.mAGI + Math.min(0, this.rAGI.generate()));
        this.setLuck(StatConstants.GAIN_LUCK
                + this.race.getAttribute(
                        RaceConstants.RACE_ATTRIBUTE_LUCK_PER_LEVEL)
                + this.mLUC + Math.min(0, this.rLUC.generate()));
        this.healAndRegenerateFully();
        this.setGold(PlayerCharacter.START_GOLD);
        this.bank = 0;
        this.setExperience(0L);
        this.toNextLevel = this.getExpToNextLevel(this.getLevel() + 1,
                this.kills);
        this.monsterLevel = this.getLevel();
        this.setSpellBook(
                SpellBookManager.getSpellBookByID(this.caste.getCasteID()));
    }

    // Methods
    public void addGoldToBank(final int newGold) {
        this.bank += newGold;
    }

    public boolean checkLevelUp() {
        return this.getExperience() >= this.toNextLevel;
    }

    @Override
    public int getAttack() {
        return super.getAttack() + this.getWeaponPower()
                + this.getPermanentAttack() + this.caste.getAttribute(
                        CasteConstants.CASTE_ATTRIBUTE_BONUS_ATTACK);
    }

    @Override
    public int getDefense() {
        return super.getDefense() + this.getArmorBlock()
                + this.getPermanentDefense() + this.caste.getAttribute(
                        CasteConstants.CASTE_ATTRIBUTE_BONUS_DEFENSE);
    }

    @Override
    public int getMaximumHP() {
        return super.getMaximumHP() + this.getPermanentHP();
    }

    @Override
    public int getMaximumMP() {
        return super.getMaximumMP() + this.getPermanentMP();
    }

    public long getExpToNextLevel(final int x, final int k) {
        if (x == 1) {
            return 0L;
        } else {
            return 10 * x * x * x + 10 * x * x + 10 * x + 10 + 3 * x * k;
        }
    }

    @Override
    public BufferedImageIcon getInitialImage() {
        return null;
    }

    public int getGoldInBank() {
        return this.bank;
    }

    public int getKills() {
        return this.kills;
    }

    public int getPermanentAttack() {
        return (int) (this.permanentAttack
                * PlayerCharacter.FACTOR_PERMANENT_ATTACK_ATTACK);
    }

    public int getPermanentAttackPoints() {
        return this.permanentAttack;
    }

    public int getPermanentDefense() {
        return (int) (this.permanentDefense
                * PlayerCharacter.FACTOR_PERMANENT_DEFENSE_DEFENSE);
    }

    public int getPermanentDefensePoints() {
        return this.permanentDefense;
    }

    public int getPermanentHP() {
        return this.permanentHP * this.caste.getAttribute(
                CasteConstants.CASTE_ATTRIBUTE_PERMANENT_HP_PER_POINT);
    }

    public int getPermanentHPPoints() {
        return this.permanentHP;
    }

    public int getPermanentMP() {
        return this.permanentMP * this.caste.getAttribute(
                CasteConstants.CASTE_ATTRIBUTE_PERMANENT_MP_PER_POINT);
    }

    public int getPermanentMPPoints() {
        return this.permanentMP;
    }

    public long getToNextLevel() {
        return this.toNextLevel;
    }

    public int getMonsterLevel() {
        return this.monsterLevel;
    }

    public String getXPString() {
        return this.getExperience() + "/" + this.toNextLevel;
    }

    // Transformers
    public void levelUp() {
        this.offsetLevel(1);
        this.offsetStrength(StatConstants.GAIN_STRENGTH
                + this.race.getAttribute(
                        RaceConstants.RACE_ATTRIBUTE_STRENGTH_PER_LEVEL)
                + Math.min(0, this.mSTR + this.rSTR.generate()));
        this.offsetBlock(StatConstants.GAIN_BLOCK
                + this.race.getAttribute(
                        RaceConstants.RACE_ATTRIBUTE_BLOCK_PER_LEVEL)
                + Math.min(0, this.mBLK + this.rBLK.generate()));
        this.offsetVitality(StatConstants.GAIN_VITALITY
                + this.race.getAttribute(
                        RaceConstants.RACE_ATTRIBUTE_VITALITY_PER_LEVEL)
                + Math.min(0, this.mVIT + this.rVIT.generate()));
        this.offsetIntelligence(StatConstants.GAIN_INTELLIGENCE
                + this.race.getAttribute(
                        RaceConstants.RACE_ATTRIBUTE_INTELLIGENCE_PER_LEVEL)
                + Math.min(0, this.mINT + this.rINT.generate()));
        this.offsetAgility(StatConstants.GAIN_AGILITY
                + this.race.getAttribute(
                        RaceConstants.RACE_ATTRIBUTE_AGILITY_PER_LEVEL)
                + Math.min(0, this.mAGI + this.rAGI.generate()));
        this.offsetLuck(StatConstants.GAIN_LUCK
                + this.race.getAttribute(
                        RaceConstants.RACE_ATTRIBUTE_LUCK_PER_LEVEL)
                + Math.min(0, this.mLUC + this.rLUC.generate()));
        this.healAndRegenerateFully();
        this.toNextLevel = this.getExpToNextLevel(this.getLevel() + 1,
                this.kills);
    }

    public void loadPlayer(final int pAttack, final int pDefense, final int pHP,
            final int pMP, final int newKills, final int newLevel,
            final int chp, final int cmp, final int newGold, final int newBank,
            final long newExperience, final int ml, final int bookID,
            final boolean[] known) {
        this.permanentAttack = pAttack;
        this.permanentDefense = pDefense;
        this.permanentHP = pHP;
        this.permanentMP = pMP;
        this.kills = newKills;
        this.setLevel(newLevel);
        this.setCurrentHP(chp);
        this.setCurrentMP(cmp);
        this.setGold(newGold);
        this.bank = newBank;
        this.setExperience(newExperience);
        this.toNextLevel = this.getExpToNextLevel(this.getLevel() + 1,
                this.kills);
        this.monsterLevel = ml;
        final SpellBook book = SpellBookManager.getSpellBookByID(bookID);
        for (int x = 0; x < known.length; x++) {
            if (known[x]) {
                book.learnSpellByID(x);
            }
        }
        this.setSpellBook(book);
    }

    private void postPointSpend() {
        this.setLevel(1);
        this.healAndRegenerateFully();
        this.setGold(PlayerCharacter.START_GOLD);
        this.bank = 0;
        this.setExperience(0L);
        this.toNextLevel = this.getExpToNextLevel(this.getLevel() + 1,
                this.kills);
        this.monsterLevel = this.getLevel();
    }

    public void removeGoldFromBank(final int cost) {
        this.bank -= cost;
        if (this.bank < 0) {
            this.bank = 0;
        }
    }

    public void setMonsterLevel(final int newMonsterLevel) {
        if (newMonsterLevel >= PlayerCharacter.MIN_MONSTER_LEVEL) {
            this.monsterLevel = newMonsterLevel;
        }
    }

    public void incrementMonsterLevel() {
        this.monsterLevel++;
    }

    public void decrementMonsterLevel() {
        if (this.monsterLevel > PlayerCharacter.MIN_MONSTER_LEVEL) {
            this.monsterLevel--;
        }
    }

    public void spendPointOnAttack() {
        this.kills++;
        this.permanentAttack++;
        this.postPointSpend();
    }

    public void spendPointOnDefense() {
        this.kills++;
        this.permanentDefense++;
        this.postPointSpend();
    }

    public void spendPointOnHP() {
        this.kills++;
        this.permanentHP++;
        this.postPointSpend();
    }

    public void spendPointOnMP() {
        this.kills++;
        this.permanentMP++;
        this.postPointSpend();
    }

    @Override
    public String getName() {
        return null;
    }

    public Race getRace() {
        return this.race;
    }

    public Caste getCaste() {
        return this.caste;
    }

    @Override
    public Faith getFaith() {
        return this.faith;
    }

    public Gender getGender() {
        return this.gender;
    }

    public Personality getPersonality() {
        return this.personality;
    }
}
