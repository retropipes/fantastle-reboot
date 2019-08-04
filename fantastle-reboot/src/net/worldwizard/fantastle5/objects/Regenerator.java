package net.worldwizard.fantastle5.objects;

import net.worldwizard.fantastle5.Fantastle5;
import net.worldwizard.fantastle5.generic.GenericDungeonObject;

public class Regenerator extends GenericDungeonObject {
    // Constructors
    public Regenerator() {
        super(false);
    }

    @Override
    public void postMoveActionHook() {
        Fantastle5.getApplication().getRegenerator().showShop();
    }

    @Override
    public String getName() {
        return "Regenerator";
    }

    @Override
    public String getPluralName() {
        return "Regenerators";
    }

    @Override
    public String getDescription() {
        return "Regenerators restore magic, for a fee.";
    }

    @Override
    public byte getObjectID() {
        return (byte) 4;
    }
}
