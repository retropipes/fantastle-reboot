package net.worldwizard.fantastle5.ai;

import com.puttysoftware.randomrange.RandomRange;

public final class RandomAIRoutinePicker {
    // Constants
    public static final int ROUTINE_COUNT = 8;

    // Methods
    public static AIRoutine getNextRoutine() {
        final RandomRange r = new RandomRange(1,
                RandomAIRoutinePicker.ROUTINE_COUNT);
        final int routine = r.generate();
        switch (routine) {
        case 1:
            return new AttackAIRoutine();
        case 2:
            return new PoisonThenAttackAIRoutine();
        case 3:
            return new RandomlyFleeAIRoutine();
        case 4:
            return new HealIfHealthLowAIRoutine();
        case 5:
            return new DelevelThenAttackAIRoutine();
        case 6:
            return new ChargeThenAttackAIRoutine();
        case 7:
            return new DelevelAndChargeAIRoutine();
        case 8:
            return new PoisonAndHealAIRoutine();
        default:
            return new AttackAIRoutine();
        }
    }
}
