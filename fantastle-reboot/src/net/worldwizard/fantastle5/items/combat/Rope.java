package net.worldwizard.fantastle5.items.combat;

import net.worldwizard.fantastle5.creatures.StatConstants;
import net.worldwizard.fantastle5.effects.Effect;

public class Rope extends CombatUsableItem {
    public Rope() {
        super("Rope", 50, 'E');
    }

    @Override
    protected void defineFields() {
        this.sound = "focus";
        this.e = new Effect("Roped", 2);
        this.e.setAffectedStat(StatConstants.STAT_AGILITY);
        this.e.setEffect(Effect.EFFECT_MULTIPLY, 0, Effect.DEFAULT_SCALE_FACTOR,
                StatConstants.STAT_NONE);
        this.e.setMessage(Effect.MESSAGE_INITIAL,
                "You wind a rope around the enemy!");
        this.e.setMessage(Effect.MESSAGE_SUBSEQUENT,
                "The enemy is tied up, and unable to act!");
        this.e.setMessage(Effect.MESSAGE_WEAR_OFF, "The rope falls off!");
    }
}
