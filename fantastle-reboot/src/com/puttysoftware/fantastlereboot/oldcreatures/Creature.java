package com.puttysoftware.fantastlereboot.oldcreatures;

import com.puttysoftware.fantastlereboot.ai.AIRoutine;
import com.puttysoftware.fantastlereboot.effects.Effect;
import com.puttysoftware.fantastlereboot.items.ItemInventory;
import com.puttysoftware.fantastlereboot.oldcreatures.faiths.Faith;
import com.puttysoftware.fantastlereboot.spells.SpellBook;
import com.puttysoftware.images.BufferedImageIcon;

public abstract class Creature implements StatConstants {
    // Fields
    protected BufferedImageIcon image;
    private final int[] stats;
    private final boolean[] hasMax;
    private final int[] maxID;
    private long experience;
    private final Effect[] effectList;
    private SpellBook spellsKnown;
    private AIRoutine ai;
    private ItemInventory items;
    private static final int MAX_EFFECTS = 100;
    private static final int FUMBLE_BASE = 10;
    private static final int MAX_AGILITY_CONTRIB = 200;
    private static final int MAX_LUCK_CONTRIB = 200;
    public static final int FULL_HEAL_PERCENTAGE = 100;

    // Constructor
    protected Creature() {
        this.stats = new int[StatConstants.MAX_STORED_STATS];
        this.hasMax = new boolean[StatConstants.MAX_STORED_STATS];
        this.maxID = new int[StatConstants.MAX_STORED_STATS];
        for (int x = 0; x < StatConstants.MAX_STORED_STATS; x++) {
            this.stats[x] = 0;
            this.hasMax[x] = false;
            this.maxID[x] = StatConstants.STAT_NONE;
        }
        this.hasMax[StatConstants.STAT_CURRENT_HP] = true;
        this.hasMax[StatConstants.STAT_CURRENT_MP] = true;
        this.maxID[StatConstants.STAT_CURRENT_HP] = StatConstants.STAT_MAXIMUM_HP;
        this.maxID[StatConstants.STAT_CURRENT_MP] = StatConstants.STAT_MAXIMUM_MP;
        this.effectList = new Effect[Creature.MAX_EFFECTS];
        this.spellsKnown = null;
        this.image = this.getInitialImage();
        this.ai = null;
        this.items = new ItemInventory();
    }

    public void applyEffect(final Effect e) {
        int x;
        for (x = 0; x < this.effectList.length; x++) {
            if (this.get(x) == null) {
                this.set(x, e);
                e.scaleEffect(Effect.EFFECT_ADD, this);
                return;
            }
        }
    }

    public void cullInactiveEffects() {
        int x;
        for (x = 0; x < this.effectList.length; x++) {
            try {
                final Effect e = this.get(x);
                if (!e.isActive()) {
                    this.set(x, null);
                }
            } catch (final NullPointerException np) {
                // Do nothing
            } catch (final ArrayIndexOutOfBoundsException aioob) {
                // Do nothing
            }
        }
    }

    public void decrementStat(final int stat) {
        this.stats[stat]--;
    }

    public void doDamage(final int damage) {
        this.offsetCurrentHP(-damage);
        this.fixStatValue(StatConstants.STAT_CURRENT_HP);
    }

    public void doDamagePercentage(final int percent) {
        int fP = percent;
        if (fP > Creature.FULL_HEAL_PERCENTAGE) {
            fP = Creature.FULL_HEAL_PERCENTAGE;
        }
        if (fP < 0) {
            fP = 0;
        }
        final double fPMultiplier = fP / (double) Creature.FULL_HEAL_PERCENTAGE;
        int modValue = (int) (this.getEffectedMaximumHP() * fPMultiplier);
        if (modValue <= 0) {
            modValue = 1;
        }
        this.offsetCurrentHP(-modValue);
        this.fixStatValue(StatConstants.STAT_CURRENT_HP);
    }

    public void drain(final int cost) {
        this.offsetCurrentMP(-cost);
        this.fixStatValue(StatConstants.STAT_CURRENT_MP);
    }

    public void drainPercentage(final int percent) {
        int fP = percent;
        if (fP > Creature.FULL_HEAL_PERCENTAGE) {
            fP = Creature.FULL_HEAL_PERCENTAGE;
        }
        if (fP < 0) {
            fP = 0;
        }
        final double fPMultiplier = fP / (double) Creature.FULL_HEAL_PERCENTAGE;
        int modValue = (int) (this.getMaximumMP() * fPMultiplier);
        if (modValue <= 0) {
            modValue = 1;
        }
        this.offsetCurrentMP(-modValue);
        this.fixStatValue(StatConstants.STAT_CURRENT_MP);
    }

    public void extendEffect(final Effect e, final int rounds) {
        final int index = this.indexOf(e);
        if (index != -1) {
            this.get(index).extendEffect(rounds);
        }
    }

    private void fixStatValue(final int stat) {
        if (this.stats[stat] < 0) {
            this.stats[stat] = 0;
        }
        if (this.hasMax[stat]) {
            if (this.stats[stat] > this.getStat(this.maxID[stat])) {
                this.stats[stat] = this.getStat(this.maxID[stat]);
            }
        }
    }

    protected Effect get(final int x) {
        return this.effectList[x];
    }

    public int getActionsPerRound() {
        return (int) Math.ceil(this.getEffectedStat(StatConstants.STAT_AGILITY)
                * StatConstants.FACTOR_AGILITY_ACTIONS_PER_ROUND);
    }

    public int getAgility() {
        return this.stats[StatConstants.STAT_AGILITY];
    }

    public AIRoutine getAI() {
        return this.ai;
    }

    public String getAllCurrentEffectMessages() {
        int x;
        String s = Effect.getNullMessage();
        for (x = 0; x < this.effectList.length; x++) {
            try {
                s += this.get(x).getCurrentMessage() + "\n";
            } catch (final NullPointerException np) {
                // Do nothing
            } catch (final ArrayIndexOutOfBoundsException aioob) {
                // Do nothing
            }
        }
        // Strip final newline character, if it exists
        if (!s.equals(Effect.getNullMessage())) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }

    public String getAllEffectMessages(final int which) {
        int x;
        String s = "";
        for (x = 0; x < this.effectList.length; x++) {
            try {
                s += this.get(x).getMessage(which) + "\n";
            } catch (final NullPointerException np) {
                // Do nothing
            } catch (final ArrayIndexOutOfBoundsException aioob) {
                // Do nothing
            }
        }
        // Strip final newline character, if it exists
        if (!s.equals(Effect.getNullMessage())) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }

    public int getArmorBlock() {
        return (int) (this.getItems().getTotalAbsorb()
                * StatConstants.FACTOR_ABSORB_DEFENSE);
    }

    public int getAttack() {
        return (int) (this.getStrength()
                * StatConstants.FACTOR_STRENGTH_ATTACK);
    }

    public int getBlock() {
        return this.stats[StatConstants.STAT_BLOCK];
    }

    public String getCompleteEffectString() {
        int x;
        String s = "";
        for (x = 0; x < this.effectList.length; x++) {
            try {
                s += this.get(x).getEffectString() + "\n";
            } catch (final NullPointerException np) {
                // Do nothing
            } catch (final ArrayIndexOutOfBoundsException aioob) {
                // Do nothing
            }
        }
        // Strip final newline character, if it exists
        if (!s.equals(Effect.getNullMessage())) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }

    public int getCurrentHP() {
        return this.stats[StatConstants.STAT_CURRENT_HP];
    }

    public int getCurrentMP() {
        return this.stats[StatConstants.STAT_CURRENT_MP];
    }

    public int getDefense() {
        return (int) (this.getBlock() * StatConstants.FACTOR_BLOCK_DEFENSE);
    }

    public double getEffectedStat(final int stat) {
        int x, s, p;
        s = 0;
        p = this.getStat(stat);
        for (x = 0; x < this.effectList.length; x++) {
            try {
                final Effect e = this.get(x);
                if (e.getAffectedStat() == stat) {
                    p *= e.getEffect(Effect.EFFECT_MULTIPLY);
                }
            } catch (final NullPointerException np) {
                // Do nothing
            } catch (final ArrayIndexOutOfBoundsException aioob) {
                // Do nothing
            }
        }
        for (x = 0; x < this.effectList.length; x++) {
            try {
                final Effect e = this.get(x);
                if (e.getAffectedStat() == stat) {
                    s += e.getEffect(Effect.EFFECT_ADD);
                }
            } catch (final NullPointerException np) {
                // Do nothing
            } catch (final ArrayIndexOutOfBoundsException aioob) {
                // Do nothing
            }
        }
        return p + s;
    }

    public String getEffectMessage(final Effect e, final int which) {
        try {
            return this.get(this.indexOf(e)).getMessage(which);
        } catch (final NullPointerException np) {
            return null;
        } catch (final ArrayIndexOutOfBoundsException aioob) {
            return null;
        }
    }

    public String getEffectName(final Effect e) {
        try {
            return this.get(this.indexOf(e)).getName();
        } catch (final NullPointerException np) {
            return null;
        } catch (final ArrayIndexOutOfBoundsException aioob) {
            return null;
        }
    }

    public int getEffectRounds(final Effect e) {
        try {
            return this.get(this.indexOf(e)).getRounds();
        } catch (final NullPointerException np) {
            return 0;
        } catch (final ArrayIndexOutOfBoundsException aioob) {
            return 0;
        }
    }

    public String getEffectString(final Effect e) {
        try {
            return this.get(this.indexOf(e)).getEffectString();
        } catch (final NullPointerException np) {
            return null;
        } catch (final ArrayIndexOutOfBoundsException aioob) {
            return null;
        }
    }

    public long getExperience() {
        return this.experience;
    }

    public Faith getFaith() {
        return null;
    }

    public String getFightingWhatString() {
        final String enemyName = this.getName();
        final boolean vowel = this.isFirstLetterVowel(enemyName);
        String fightingWhat = null;
        if (vowel) {
            fightingWhat = "You're fighting an " + enemyName;
        } else {
            fightingWhat = "You're fighting a " + enemyName;
        }
        return fightingWhat;
    }

    public int getFumbleChance() {
        final int chance = Creature.FUMBLE_BASE;
        final double agilityContrib = Math.min(
                this.getEffectedStat(StatConstants.STAT_AGILITY),
                Creature.MAX_AGILITY_CONTRIB)
                / (Creature.MAX_AGILITY_CONTRIB / (double) Creature.FUMBLE_BASE)
                * StatConstants.FACTOR_AGILITY_FUMBLE;
        final double luckContrib = Math.min(
                this.getEffectedStat(StatConstants.STAT_LUCK),
                Creature.MAX_LUCK_CONTRIB)
                / (Creature.MAX_LUCK_CONTRIB / (double) Creature.FUMBLE_BASE)
                * StatConstants.FACTOR_LUCK_FUMBLE;
        final int modifier = (int) (agilityContrib + luckContrib);
        return chance - modifier;
    }

    public int getGold() {
        return this.stats[StatConstants.STAT_GOLD];
    }

    public String getHPString() {
        return this.getCurrentHP() + "/" + this.getMaximumHP();
    }

    public String getEffectedHPString() {
        return this.getCurrentHP() + "/" + this.getEffectedMaximumHP();
    }

    public BufferedImageIcon getImage() {
        return this.image;
    }

    protected abstract BufferedImageIcon getInitialImage();

    public int getIntelligence() {
        return this.stats[StatConstants.STAT_INTELLIGENCE];
    }

    public ItemInventory getItems() {
        return this.items;
    }

    public int getLevel() {
        return this.stats[StatConstants.STAT_LEVEL];
    }

    public int getLevelDifference() {
        return 0;
    }

    public int getLuck() {
        return this.stats[StatConstants.STAT_LUCK];
    }

    public int getMaximumHP() {
        return (int) (this.getVitality()
                * StatConstants.FACTOR_VITALITY_HEALTH);
    }

    public int getMaximumMP() {
        return (int) (this.getIntelligence()
                * StatConstants.FACTOR_INTELLIGENCE_MAGIC);
    }

    public int getEffectedMaximumHP() {
        return (int) this.getEffectedStat(StatConstants.STAT_MAXIMUM_HP);
    }

    public int getEffectedMaximumMP() {
        return (int) this.getEffectedStat(StatConstants.STAT_MAXIMUM_MP);
    }

    public String getMPString() {
        return this.getCurrentMP() + "/" + this.getMaximumMP();
    }

    public String getEffectedMPString() {
        return this.getCurrentMP() + "/" + this.getEffectedMaximumMP();
    }

    public abstract String getName();

    public int getSpeed() {
        return (int) (this.getAgility() * StatConstants.FACTOR_AGILITY_SPEED
                - this.items.getTotalEquipmentWeight()
                        * StatConstants.FACTOR_WEIGHT_SPEED);
    }

    public SpellBook getSpellBook() {
        return this.spellsKnown;
    }

    public int getStat(final int stat) {
        try {
            return this.stats[stat];
        } catch (final ArrayIndexOutOfBoundsException aioob) {
            switch (stat) {
            case StatConstants.STAT_ATTACK:
                return this.getAttack();
            case StatConstants.STAT_DEFENSE:
                return this.getDefense();
            case StatConstants.STAT_MAXIMUM_HP:
                return this.getMaximumHP();
            case StatConstants.STAT_MAXIMUM_MP:
                return this.getMaximumMP();
            case StatConstants.STAT_FUMBLE_CHANCE:
                return this.getFumbleChance();
            case StatConstants.STAT_SPEED:
                return this.getSpeed();
            default:
                return 0;
            }
        }
    }

    public double getStatEffect(final Effect e, final int stat) {
        int s, p;
        s = (int) Effect.DEFAULT_ADDITION;
        p = (int) Effect.DEFAULT_MULTIPLIER;
        final int index = this.indexOf(e);
        if (index != -1) {
            final Effect re = this.get(index);
            if (re.getAffectedStat() == stat) {
                p *= re.getEffect(Effect.EFFECT_MULTIPLY);
                s += re.getEffect(Effect.EFFECT_ADD);
            }
        }
        return p + s;
    }

    public int getStrength() {
        return this.stats[StatConstants.STAT_STRENGTH];
    }

    public int getVitality() {
        return this.stats[StatConstants.STAT_VITALITY];
    }

    public int getWeaponPower() {
        return (int) (this.getItems().getTotalPower()
                * StatConstants.FACTOR_POWER_ATTACK);
    }

    public void heal(final int amount) {
        this.offsetCurrentHP(amount);
        this.fixStatValue(StatConstants.STAT_CURRENT_HP);
    }

    public void healAndRegenerateFully() {
        this.healFully();
        this.regenerateFully();
    }

    public void healFully() {
        this.setCurrentHP(this.getEffectedMaximumHP());
    }

    public void healPercentage(final int percent) {
        int fP = percent;
        if (fP > Creature.FULL_HEAL_PERCENTAGE) {
            fP = Creature.FULL_HEAL_PERCENTAGE;
        }
        if (fP < 0) {
            fP = 0;
        }
        final double fPMultiplier = fP / (double) Creature.FULL_HEAL_PERCENTAGE;
        final int difference = this.getEffectedMaximumHP()
                - this.getCurrentHP();
        int modValue = (int) (difference * fPMultiplier);
        if (modValue <= 0) {
            modValue = 1;
        }
        this.offsetCurrentHP(modValue);
        this.fixStatValue(StatConstants.STAT_CURRENT_HP);
    }

    public void incrementStat(final int stat) {
        this.stats[stat]++;
    }

    protected int indexOf(final Effect e) {
        int x;
        for (x = 0; x < this.effectList.length; x++) {
            final Effect le = this.get(x);
            if (le != null) {
                if (e.equals(le)) {
                    return x;
                }
            } else {
                return -1;
            }
        }
        return -1;
    }

    public boolean isAlive() {
        return this.getCurrentHP() > 0;
    }

    public boolean isEffectActive(final Effect e) {
        final int index = this.indexOf(e);
        if (index != -1) {
            return this.get(index).isActive();
        } else {
            return false;
        }
    }

    protected boolean isFirstLetterVowel(final String s) {
        final String firstLetter = s.substring(0, 1);
        if (firstLetter.equalsIgnoreCase("A")
                || firstLetter.equalsIgnoreCase("E")
                || firstLetter.equalsIgnoreCase("I")
                || firstLetter.equalsIgnoreCase("O")
                || firstLetter.equalsIgnoreCase("U")) {
            return true;
        } else {
            return false;
        }
    }

    public void offsetAgility(final int value) {
        this.stats[StatConstants.STAT_AGILITY] += value;
    }

    public void offsetBlock(final int value) {
        this.stats[StatConstants.STAT_BLOCK] += value;
    }

    public void offsetCurrentHP(final int value) {
        this.stats[StatConstants.STAT_CURRENT_HP] += value;
        this.fixStatValue(StatConstants.STAT_CURRENT_HP);
    }

    public void offsetCurrentMP(final int value) {
        this.stats[StatConstants.STAT_CURRENT_MP] += value;
        this.fixStatValue(StatConstants.STAT_CURRENT_MP);
    }

    public void offsetExperience(final long value) {
        this.experience += value;
    }

    public void offsetGold(final int value) {
        this.stats[StatConstants.STAT_GOLD] += value;
    }

    public void offsetIntelligence(final int value) {
        this.stats[StatConstants.STAT_INTELLIGENCE] += value;
    }

    public void offsetLevel(final int value) {
        this.stats[StatConstants.STAT_LEVEL] += value;
    }

    public void offsetLuck(final int value) {
        this.stats[StatConstants.STAT_LUCK] += value;
    }

    public void offsetStat(final int stat, final int value) {
        this.stats[stat] += value;
        this.fixStatValue(stat);
    }

    public void offsetStrength(final int value) {
        this.stats[StatConstants.STAT_STRENGTH] += value;
    }

    public void offsetVitality(final int value) {
        this.stats[StatConstants.STAT_VITALITY] += value;
    }

    public void regenerate(final int amount) {
        this.offsetCurrentMP(amount);
        this.fixStatValue(StatConstants.STAT_CURRENT_MP);
    }

    public void regenerateFully() {
        this.setCurrentMP(this.getMaximumMP());
    }

    public void regeneratePercentage(final int percent) {
        int fP = percent;
        if (fP > Creature.FULL_HEAL_PERCENTAGE) {
            fP = Creature.FULL_HEAL_PERCENTAGE;
        }
        if (fP < 0) {
            fP = 0;
        }
        final double fPMultiplier = fP / (double) Creature.FULL_HEAL_PERCENTAGE;
        final int difference = this.getMaximumMP() - this.getCurrentMP();
        int modValue = (int) (difference * fPMultiplier);
        if (modValue <= 0) {
            modValue = 1;
        }
        this.offsetCurrentMP(modValue);
        this.fixStatValue(StatConstants.STAT_CURRENT_MP);
    }

    public void restoreEffect(final Effect e) {
        final int index = this.indexOf(e);
        if (index != -1) {
            this.get(index).restoreEffect(this);
        }
    }

    protected void set(final int x, final Effect e) {
        this.effectList[x] = e;
    }

    public void setAgility(final int value) {
        this.stats[StatConstants.STAT_AGILITY] = value;
    }

    public void setAI(final AIRoutine newAI) {
        this.ai = newAI;
    }

    public void setBlock(final int value) {
        this.stats[StatConstants.STAT_BLOCK] = value;
    }

    public void setCurrentHP(final int value) {
        this.stats[StatConstants.STAT_CURRENT_HP] = value;
    }

    public void setCurrentMP(final int value) {
        this.stats[StatConstants.STAT_CURRENT_MP] = value;
    }

    public void setExperience(final long value) {
        this.experience = value;
    }

    public void setGold(final int value) {
        this.stats[StatConstants.STAT_GOLD] = value;
    }

    public void setIntelligence(final int value) {
        this.stats[StatConstants.STAT_INTELLIGENCE] = value;
    }

    public void setItems(final ItemInventory newItems) {
        this.items = newItems;
    }

    public void setLevel(final int value) {
        this.stats[StatConstants.STAT_LEVEL] = value;
    }

    public void setLuck(final int value) {
        this.stats[StatConstants.STAT_LUCK] = value;
    }

    public void setSpellBook(final SpellBook book) {
        this.spellsKnown = book;
    }

    public void setStat(final int stat, final int value) {
        this.stats[stat] = value;
        this.fixStatValue(stat);
    }

    public void setStrength(final int value) {
        this.stats[StatConstants.STAT_STRENGTH] = value;
    }

    public void setVitality(final int value) {
        this.stats[StatConstants.STAT_VITALITY] = value;
    }

    public void stripAllEffects() {
        int x;
        for (x = 0; x < this.effectList.length; x++) {
            this.set(x, null);
        }
    }

    public void stripEffect(final Effect e) {
        final int x = this.indexOf(e);
        if (x != -1) {
            this.set(x, null);
        }
    }

    public void useEffects() {
        int x;
        for (x = 0; x < this.effectList.length; x++) {
            try {
                this.get(x).useEffect(this);
            } catch (final NullPointerException np) {
                // Do nothing
            } catch (final ArrayIndexOutOfBoundsException aioob) {
                // Do nothing
            }
        }
    }
}
