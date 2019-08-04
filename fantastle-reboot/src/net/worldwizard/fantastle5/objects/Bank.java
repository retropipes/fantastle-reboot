package net.worldwizard.fantastle5.objects;

import net.worldwizard.fantastle5.Fantastle5;
import net.worldwizard.fantastle5.generic.GenericDungeonObject;

public class Bank extends GenericDungeonObject {
    // Constructors
    public Bank() {
        super(false);
    }

    @Override
    public void postMoveActionHook() {
        Fantastle5.getApplication().getBank().showShop();
    }

    @Override
    public String getName() {
        return "Bank";
    }

    @Override
    public String getPluralName() {
        return "Banks";
    }

    @Override
    public String getDescription() {
        return "Banks store money for safe keeping.";
    }

    @Override
    public byte getObjectID() {
        return (byte) 2;
    }
}
