package com.puttysoftware.fantastlereboot.ai.window;

import com.puttysoftware.fantastlereboot.creatures.Creature;
import com.puttysoftware.randomrange.RandomRange;

public class EasyWindowAIRoutine extends AbstractWindowAIRoutine {
  // Fields
  private int[] roundsRemaining;
  private static final int CAST_SPELL_CHANCE = 10;
  private static final int STEAL_CHANCE = 2;
  private static final int DRAIN_CHANCE = 10;
  private static final int HEAL_THRESHOLD = 10;
  private static final int FLEE_CHANCE = 20;

  // Constructors
  public EasyWindowAIRoutine() {
    // Do nothing
  }

  @Override
  public int getNextAction(final Creature c) {
    if (this.roundsRemaining == null) {
      this.roundsRemaining = new int[c.getSpellBook().getSpellCount()];
    }
    if (this.spellCheck(c)) {
      // Cast a spell
      return AbstractWindowAIRoutine.ACTION_CAST_SPELL;
    } else if (CommonWindowAIRoutines.check(STEAL_CHANCE)) {
      // Steal
      return AbstractWindowAIRoutine.ACTION_STEAL;
    } else if (CommonWindowAIRoutines.check(DRAIN_CHANCE)) {
      // Drain MP
      return AbstractWindowAIRoutine.ACTION_DRAIN;
    } else if (CommonWindowAIRoutines.check(FLEE_CHANCE)) {
      // Flee
      return AbstractWindowAIRoutine.ACTION_FLEE;
    } else {
      // Something hostile is nearby, so attack it
      return AbstractWindowAIRoutine.ACTION_ATTACK;
    }
  }

  private boolean spellCheck(final Creature c) {
    final RandomRange random = new RandomRange(1, 100);
    final int chance = random.generate();
    if (chance <= CAST_SPELL_CHANCE) {
      final int maxIndex = CommonWindowAIRoutines.getMaxCastIndex(c);
      if (maxIndex > -1) {
        // Select a random spell to cast
        final RandomRange randomSpell = new RandomRange(0, maxIndex);
        final int randomSpellID = randomSpell.generate();
        if (randomSpellID == CommonWindowAIRoutines.SPELL_INDEX_HEAL) {
          // Healing spell was selected - is healing needed?
          if (c.getCurrentHP() > c.getMaximumHP() * HEAL_THRESHOLD / 100) {
            // Do not need healing
            return false;
          }
        }
        if (this.roundsRemaining[randomSpellID] == 0) {
          this.spell = c.getSpellBook().getSpellByID(randomSpellID);
          this.roundsRemaining[randomSpellID] = this.spell.getEffect()
              .getInitialRounds();
          return true;
        } else {
          // Spell selected already active
          return false;
        }
      } else {
        // Not enough MP to cast anything
        return false;
      }
    } else {
      // Not casting a spell
      return false;
    }
  }

  @Override
  public void newRoundHook() {
    // Decrement effect counters
    for (int z = 0; z < this.roundsRemaining.length; z++) {
      if (this.roundsRemaining[z] > 0) {
        this.roundsRemaining[z]--;
      }
    }
  }
}
