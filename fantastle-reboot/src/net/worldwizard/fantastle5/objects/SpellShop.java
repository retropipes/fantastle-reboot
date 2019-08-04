package net.worldwizard.fantastle5.objects;

import net.worldwizard.fantastle5.Fantastle5;
import net.worldwizard.fantastle5.generic.GenericDungeonObject;

public class SpellShop extends GenericDungeonObject {
    // Constructors
    public SpellShop() {
        super(false);
    }

    @Override
    public void postMoveActionHook() {
        Fantastle5.getApplication().getSpells().showShop();
    }

    @Override
    public String getName() {
        return "Spell Shop";
    }

    @Override
    public String getPluralName() {
        return "Spell Shops";
    }

    @Override
    public String getDescription() {
        return "Spell Shops teach spells, for a fee.";
    }

    @Override
    public byte getObjectID() {
        return (byte) 6;
    }
}
