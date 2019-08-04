package net.worldwizard.fantastle5.objects;

import net.worldwizard.fantastle5.Fantastle5;
import net.worldwizard.fantastle5.generic.GenericDungeonObject;

public class Healer extends GenericDungeonObject {
    // Constructors
    public Healer() {
        super(false);
    }

    @Override
    public void postMoveActionHook() {
        Fantastle5.getApplication().getHealer().showShop();
    }

    @Override
    public String getName() {
        return "Healer";
    }

    @Override
    public String getPluralName() {
        return "Healers";
    }

    @Override
    public String getDescription() {
        return "Healers restore health, for a fee.";
    }

    @Override
    public byte getObjectID() {
        return (byte) 3;
    }
}
