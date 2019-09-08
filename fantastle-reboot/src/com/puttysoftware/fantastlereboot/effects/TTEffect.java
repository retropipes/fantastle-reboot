/*  TallerTower: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.effects;

import java.util.Arrays;

import com.puttysoftware.fantastlereboot.creatures.Creature;
import com.puttysoftware.fantastlereboot.creatures.StatConstants;

public class TTEffect {
    // Fields
    private final String name;
    private final double[][] initialEffect;
    private final double[][] effect;
    private double effectScaleFactor;
    private int effectScaleStat;
    private final double[][] effectDecayRate;
    private int rounds;
    private final int initialRounds;
    private final String[] messages;
    private static final double DEFAULT_ADDITION = 0;
    private static final double DEFAULT_MULTIPLIER = 1;
    public static final int EFFECT_ADD = 0;
    public static final int EFFECT_MULTIPLY = 1;
    public static final double DEFAULT_SCALE_FACTOR = 1.0;
    public static final int DEFAULT_SCALE_STAT = StatConstants.STAT_NONE;
    private static final int ROUNDS_INFINITE = -1;
    public static final int MESSAGE_INITIAL = 0;
    public static final int MESSAGE_SUBSEQUENT = 1;
    public static final int MESSAGE_WEAR_OFF = 2;
    private static final int MAX_EFFECT_TYPES = 2;
    private static final int MAX_MESSAGES = 3;

    // Constructors
    public TTEffect() {
        super();
        this.name = "Un-named";
        this.messages = new String[TTEffect.MAX_MESSAGES];
        this.effect = new double[TTEffect.MAX_EFFECT_TYPES][StatConstants.MAX_STATS];
        this.initialEffect = new double[TTEffect.MAX_EFFECT_TYPES][StatConstants.MAX_STATS];
        this.effectScaleFactor = TTEffect.DEFAULT_SCALE_FACTOR;
        this.effectScaleStat = TTEffect.DEFAULT_SCALE_STAT;
        this.effectDecayRate = new double[TTEffect.MAX_EFFECT_TYPES][StatConstants.MAX_STATS];
        int x, y;
        for (x = 0; x < TTEffect.MAX_EFFECT_TYPES; x++) {
            for (y = 0; y < StatConstants.MAX_STATS; y++) {
                if (x == TTEffect.EFFECT_ADD) {
                    this.effect[x][y] = TTEffect.DEFAULT_ADDITION;
                    this.initialEffect[x][y] = TTEffect.DEFAULT_ADDITION;
                    this.effectDecayRate[x][y] = TTEffect.DEFAULT_ADDITION;
                } else if (x == TTEffect.EFFECT_MULTIPLY) {
                    this.effect[x][y] = TTEffect.DEFAULT_MULTIPLIER;
                    this.initialEffect[x][y] = TTEffect.DEFAULT_MULTIPLIER;
                    this.effectDecayRate[x][y] = TTEffect.DEFAULT_MULTIPLIER;
                } else {
                    this.effect[x][y] = 0;
                    this.initialEffect[x][y] = 0;
                    this.effectDecayRate[x][y] = 0;
                }
            }
        }
        for (x = 0; x < TTEffect.MAX_MESSAGES; x++) {
            this.messages[x] = "";
        }
        this.rounds = 0;
        this.initialRounds = 0;
    }

    public TTEffect(final String effectName, final int newRounds) {
        super();
        this.name = effectName;
        this.messages = new String[TTEffect.MAX_MESSAGES];
        this.effect = new double[TTEffect.MAX_EFFECT_TYPES][StatConstants.MAX_STATS];
        this.initialEffect = new double[TTEffect.MAX_EFFECT_TYPES][StatConstants.MAX_STATS];
        this.effectScaleFactor = TTEffect.DEFAULT_SCALE_FACTOR;
        this.effectScaleStat = TTEffect.DEFAULT_SCALE_STAT;
        this.effectDecayRate = new double[TTEffect.MAX_EFFECT_TYPES][StatConstants.MAX_STATS];
        int x, y;
        for (x = 0; x < TTEffect.MAX_EFFECT_TYPES; x++) {
            for (y = 0; y < StatConstants.MAX_STATS; y++) {
                if (x == TTEffect.EFFECT_ADD) {
                    this.effect[x][y] = TTEffect.DEFAULT_ADDITION;
                    this.initialEffect[x][y] = TTEffect.DEFAULT_ADDITION;
                    this.effectDecayRate[x][y] = TTEffect.DEFAULT_ADDITION;
                } else if (x == TTEffect.EFFECT_MULTIPLY) {
                    this.effect[x][y] = TTEffect.DEFAULT_MULTIPLIER;
                    this.initialEffect[x][y] = TTEffect.DEFAULT_MULTIPLIER;
                    this.effectDecayRate[x][y] = TTEffect.DEFAULT_MULTIPLIER;
                } else {
                    this.effect[x][y] = 0;
                    this.initialEffect[x][y] = 0;
                    this.effectDecayRate[x][y] = 0;
                }
            }
        }
        for (x = 0; x < TTEffect.MAX_MESSAGES; x++) {
            this.messages[x] = "";
        }
        this.rounds = newRounds;
        this.initialRounds = newRounds;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(this.effect);
        long temp;
        result = prime * result + Arrays.hashCode(this.effectDecayRate);
        temp = Double.doubleToLongBits(this.effectScaleFactor);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + this.effectScaleStat;
        result = prime * result + Arrays.hashCode(this.initialEffect);
        result = prime * result + this.initialRounds;
        result = prime * result + Arrays.hashCode(this.messages);
        result = prime * result
                + ((this.name == null) ? 0 : this.name.hashCode());
        return prime * result + this.rounds;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof TTEffect)) {
            return false;
        }
        final TTEffect other = (TTEffect) obj;
        if (!Arrays.equals(this.effect, other.effect)) {
            return false;
        }
        if (!Arrays.equals(this.effectDecayRate, other.effectDecayRate)) {
            return false;
        }
        if (Double.doubleToLongBits(this.effectScaleFactor) != Double
                .doubleToLongBits(other.effectScaleFactor)) {
            return false;
        }
        if (this.effectScaleStat != other.effectScaleStat) {
            return false;
        }
        if (!Arrays.equals(this.initialEffect, other.initialEffect)) {
            return false;
        }
        if (this.initialRounds != other.initialRounds) {
            return false;
        }
        if (!Arrays.equals(this.messages, other.messages)) {
            return false;
        }
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!this.name.equals(other.name)) {
            return false;
        }
        if (this.rounds != other.rounds) {
            return false;
        }
        return true;
    }

    public void extendEffect(final int additionalRounds) {
        this.rounds += additionalRounds;
    }

    public String getEffectString() {
        if (this.name.equals("")) {
            return "";
        } else {
            if (this.areRoundsInfinite()) {
                return this.name;
            } else {
                return this.name + " (" + this.rounds + " Rounds Left)";
            }
        }
    }

    public String getCurrentMessage() {
        String msg = TTEffect.getNullMessage();
        if (this.rounds == (this.initialRounds - 1)) {
            if (!this.messages[TTEffect.MESSAGE_INITIAL].equals(TTEffect
                    .getNullMessage())) {
                msg += this.messages[TTEffect.MESSAGE_INITIAL] + "\n";
            }
        }
        if (!this.messages[TTEffect.MESSAGE_SUBSEQUENT].equals(TTEffect
                .getNullMessage())) {
            msg += this.messages[TTEffect.MESSAGE_SUBSEQUENT] + "\n";
        }
        if (this.rounds == 0) {
            if (!this.messages[TTEffect.MESSAGE_WEAR_OFF].equals(TTEffect
                    .getNullMessage())) {
                msg += this.messages[TTEffect.MESSAGE_WEAR_OFF] + "\n";
            }
        }
        // Strip final newline character, if it exists
        if (!msg.equals(TTEffect.getNullMessage())) {
            msg = msg.substring(0, msg.length() - 1);
        }
        return msg;
    }

    public void setMessage(final int which, final String newMessage) {
        this.messages[which] = newMessage;
    }

    public static String getNullMessage() {
        return "";
    }

    public int getInitialRounds() {
        return this.initialRounds;
    }

    public void restoreEffect() {
        if (!this.areRoundsInfinite()) {
            this.rounds = this.initialRounds;
        }
    }

    public String getName() {
        return this.name;
    }

    private boolean areRoundsInfinite() {
        return (this.rounds == TTEffect.ROUNDS_INFINITE);
    }

    public boolean isActive() {
        if (this.areRoundsInfinite()) {
            return true;
        } else {
            return (this.rounds > 0);
        }
    }

    public void resetEffect() {
        int x, y;
        for (x = 0; x < TTEffect.MAX_EFFECT_TYPES; x++) {
            for (y = 0; y < StatConstants.MAX_STATS; y++) {
                this.effect[x][y] = this.initialEffect[x][y];
            }
        }
    }

    public void useEffect(final Creature target) {
        final double hpAddEffect = this.getEffect(TTEffect.EFFECT_ADD,
                StatConstants.STAT_CURRENT_HP);
        final double mpAddEffect = this.getEffect(TTEffect.EFFECT_ADD,
                StatConstants.STAT_CURRENT_MP);
        final double hpMultEffect = this.getEffect(TTEffect.EFFECT_MULTIPLY,
                StatConstants.STAT_CURRENT_HP);
        final double mpMultEffect = this.getEffect(TTEffect.EFFECT_MULTIPLY,
                StatConstants.STAT_CURRENT_MP);
        if (hpAddEffect < 0) {
            if (target.isAlive()) {
                target.doDamage((int) -hpAddEffect);
            }
        } else if (hpAddEffect > 0) {
            target.heal((int) hpAddEffect);
        }
        if (mpAddEffect < 0) {
            if (target.isAlive()) {
                target.drain((int) -mpAddEffect);
            }
        } else if (mpAddEffect > 0) {
            target.regenerate((int) mpAddEffect);
        }
        if (hpMultEffect < 1) {
            if (target.isAlive()) {
                final double damage = hpMultEffect;
                boolean max;
                if (this.effectScaleStat == StatConstants.STAT_MAXIMUM_HP) {
                    max = true;
                } else {
                    max = false;
                }
                target.doDamageMultiply(damage, max);
            }
        } else if (hpMultEffect > 1) {
            boolean max;
            if (this.effectScaleStat == StatConstants.STAT_MAXIMUM_HP) {
                max = true;
            } else {
                max = false;
            }
            target.healMultiply(hpMultEffect, max);
        }
        if (mpMultEffect < 1) {
            if (target.isAlive()) {
                boolean max;
                if (this.effectScaleStat == StatConstants.STAT_MAXIMUM_MP) {
                    max = true;
                } else {
                    max = false;
                }
                target.drainMultiply(mpMultEffect, max);
            }
        } else if (mpMultEffect > 1) {
            boolean max;
            if (this.effectScaleStat == StatConstants.STAT_MAXIMUM_MP) {
                max = true;
            } else {
                max = false;
            }
            target.regenerateMultiply(mpMultEffect, max);
        }
        if (!this.areRoundsInfinite()) {
            this.rounds--;
            if (this.rounds < 0) {
                this.rounds = 0;
            }
        }
        this.decayEffect();
    }

    public double getEffect(final int type, final int stat) {
        return this.effect[type][stat];
    }

    public void scaleEffect(final int type, final Creature scaleTo) {
        for (int stat = 0; stat < StatConstants.MAX_STATS; stat++) {
            final double base = this.effect[type][stat];
            final int scst = this.effectScaleStat;
            if (scst != StatConstants.STAT_NONE) {
                final double factor = this.effectScaleFactor;
                final int scstVal = scaleTo.getStat(scst);
                this.effect[type][stat] = (scstVal * base * factor);
            }
        }
    }

    public void setEffect(final int type, final int stat, final double value) {
        this.effect[type][stat] = value;
        this.initialEffect[type][stat] = value;
    }

    public void setScaleFactor(final double factor) {
        this.effectScaleFactor = factor;
    }

    public void setScaleStat(final int scaleStat) {
        this.effectScaleStat = scaleStat;
    }

    public void setEffect(final int type, final int stat, final double value,
            final double factor, final int scaleStat) {
        this.effect[type][stat] = value;
        this.initialEffect[type][stat] = value;
        this.effectScaleFactor = factor;
        this.effectScaleStat = scaleStat;
    }

    private void modifyEffect(final int type, final int stat,
            final double value, final double factor, final int scaleStat) {
        this.effect[type][stat] = value;
        this.effectScaleFactor = factor;
        this.effectScaleStat = scaleStat;
    }

    private double getDecayRate(final int type, final int stat) {
        return this.effectDecayRate[type][stat];
    }

    public void setDecayRate(final int type, final int stat, final double value) {
        this.effectDecayRate[type][stat] = value;
    }

    private void decayEffect() {
        double currVal = 0.0;
        for (int stat = 0; stat < StatConstants.MAX_STATS; stat++) {
            currVal = 0.0;
            for (int type = 0; type < TTEffect.MAX_EFFECT_TYPES; type++) {
                currVal += this.getEffect(type, stat);
            }
            for (int type = 0; type < TTEffect.MAX_EFFECT_TYPES; type++) {
                boolean keepGoing = true;
                final double currDecay = this.getDecayRate(type, stat);
                if (currDecay == 0) {
                    keepGoing = false;
                }
                double modVal;
                if (type == TTEffect.EFFECT_ADD) {
                    modVal = currVal - currDecay;
                } else if (type == TTEffect.EFFECT_MULTIPLY) {
                    modVal = currVal / currDecay;
                    if (currDecay > 1 && modVal < 1) {
                        this.setDecayRate(type, stat, 1);
                        modVal = 1;
                    }
                    if (currDecay < 1 && modVal > 1) {
                        this.setDecayRate(type, stat, 1);
                        modVal = 1;
                    }
                } else {
                    modVal = currVal;
                }
                if (keepGoing) {
                    final int scst = this.effectScaleStat;
                    final double factor = this.effectScaleFactor;
                    this.modifyEffect(type, stat, modVal, factor, scst);
                }
            }
        }
    }
}