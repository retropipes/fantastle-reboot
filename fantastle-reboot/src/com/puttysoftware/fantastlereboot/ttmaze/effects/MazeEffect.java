package com.puttysoftware.fantastlereboot.ttmaze.effects;

public class MazeEffect {
    // Fields
    protected String name;
    protected int rounds;
    protected int initialRounds;
    public static final int ROUNDS_INFINITE = -1;

    // Constructor
    public MazeEffect(final String effectName, final int newRounds) {
        this.name = effectName;
        this.rounds = newRounds;
        this.initialRounds = newRounds;
    }

    public void customExtendLogic() {
        // Do nothing
    }

    public void customTerminateLogic() {
        // Do nothing
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.initialRounds;
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
        if (!(obj instanceof MazeEffect)) {
            return false;
        }
        final MazeEffect other = (MazeEffect) obj;
        if (this.initialRounds != other.initialRounds) {
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
        this.customExtendLogic();
        this.rounds += additionalRounds;
    }

    public String getEffectString() {
        if (this.name.equals("")) {
            return "";
        } else {
            if (this.areRoundsInfinite()) {
                return this.name;
            } else {
                return this.name + " (" + this.rounds + " Steps Left)";
            }
        }
    }

    public boolean areRoundsInfinite() {
        return (this.rounds == MazeEffect.ROUNDS_INFINITE);
    }

    public boolean isActive() {
        if (this.areRoundsInfinite()) {
            return true;
        } else {
            return (this.rounds > 0);
        }
    }

    public void useEffect() {
        if (!this.areRoundsInfinite()) {
            this.rounds--;
            if (this.rounds < 0) {
                this.rounds = 0;
            }
            if (this.rounds == 0) {
                this.customTerminateLogic();
            }
        }
    }

    public void deactivateEffect() {
        this.rounds = 0;
        this.customTerminateLogic();
    }

    public int modifyMove1(final int arg) {
        return arg;
    }

    public int[] modifyMove2(final int... arg) {
        return arg;
    }
}