/*  TallerTower: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.creatures.monsters;

import com.puttysoftware.fantastlereboot.ai.map.AbstractMapAIRoutine;
import com.puttysoftware.fantastlereboot.ai.map.MapAIRoutinePicker;
import com.puttysoftware.fantastlereboot.ai.window.AbstractWindowAIRoutine;
import com.puttysoftware.fantastlereboot.ai.window.WindowAIRoutinePicker;
import com.puttysoftware.fantastlereboot.creatures.AbstractCreature;
import com.puttysoftware.fantastlereboot.creatures.faiths.Faith;
import com.puttysoftware.fantastlereboot.creatures.faiths.FaithManager;
import com.puttysoftware.fantastlereboot.ttprefs.PreferencesManager;
import com.puttysoftware.fantastlereboot.ttspells.SpellBook;
import com.puttysoftware.randomrange.RandomRange;

public abstract class AbstractMonster extends AbstractCreature {
    // Fields
    private String type;
    protected Element element;
    protected static final double MINIMUM_EXPERIENCE_RANDOM_VARIANCE = -5.0 / 2.0;
    protected static final double MAXIMUM_EXPERIENCE_RANDOM_VARIANCE = 5.0 / 2.0;
    protected static final int PERFECT_GOLD_MIN = 1;
    protected static final int PERFECT_GOLD_MAX = 3;
    private static final int BATTLES_SCALE_FACTOR = 2;
    private static final int BATTLES_START = 2;

    // Constructors
    AbstractMonster() {
        super(true, 1);
        this.setWindowAI(AbstractMonster.getInitialWindowAI());
        this.setMapAI(AbstractMonster.getInitialMapAI());
        this.element = new Element(FaithManager.getFaith(0));
        final SpellBook spells = new SystemMonsterSpellBook();
        spells.learnAllSpells();
        this.setSpellBook(spells);
    }

    // Methods
    @Override
    public String getName() {
        return this.element.getName() + " " + this.type;
    }

    @Override
    public Faith getFaith() {
        return this.element.getFaith();
    }

    @Override
    public boolean checkLevelUp() {
        return false;
    }

    @Override
    protected void levelUpHook() {
        // Do nothing
    }

    @Override
    protected final int getInitialPerfectBonusGold() {
        final int tough = this.getToughness();
        final int min = tough * AbstractMonster.PERFECT_GOLD_MIN;
        final int max = tough * AbstractMonster.PERFECT_GOLD_MAX;
        final RandomRange r = new RandomRange(min, max);
        return (int) (r.generate() * this.adjustForLevelDifference());
    }

    @Override
    public int getSpeed() {
        final int difficulty = PreferencesManager.getGameDifficulty();
        final int base = this.getBaseSpeed();
        if (difficulty == PreferencesManager.DIFFICULTY_VERY_EASY) {
            return (int) (base * SPEED_ADJUST_SLOWEST);
        } else if (difficulty == PreferencesManager.DIFFICULTY_EASY) {
            return (int) (base * SPEED_ADJUST_SLOW);
        } else if (difficulty == PreferencesManager.DIFFICULTY_NORMAL) {
            return (int) (base * SPEED_ADJUST_NORMAL);
        } else if (difficulty == PreferencesManager.DIFFICULTY_HARD) {
            return (int) (base * SPEED_ADJUST_FAST);
        } else if (difficulty == PreferencesManager.DIFFICULTY_VERY_HARD) {
            return (int) (base * SPEED_ADJUST_FASTEST);
        } else {
            return (int) (base * SPEED_ADJUST_NORMAL);
        }
    }

    private int getToughness() {
        return this.getStrength() + this.getBlock() + this.getAgility()
                + this.getVitality() + this.getIntelligence() + this.getLuck();
    }

    final String getType() {
        return this.type;
    }

    final Element getElement() {
        return this.element;
    }

    final void setType(final String newType) {
        this.type = newType;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result
                + ((this.element == null) ? 0 : this.element.hashCode());
        return prime * result
                + ((this.type == null) ? 0 : this.type.hashCode());
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof AbstractMonster)) {
            return false;
        }
        final AbstractMonster other = (AbstractMonster) obj;
        if (this.element == null) {
            if (other.element != null) {
                return false;
            }
        } else if (!this.element.equals(other.element)) {
            return false;
        }
        if (this.type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!this.type.equals(other.type)) {
            return false;
        }
        return true;
    }

    protected final int getBattlesToNextLevel() {
        return AbstractMonster.BATTLES_START + (this.getLevel() + 1)
                * AbstractMonster.BATTLES_SCALE_FACTOR;
    }
}
