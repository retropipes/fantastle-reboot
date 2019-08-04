package com.puttysoftware.fantastlereboot.objects;

import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.generic.GenericDungeonObject;

public class Bank extends GenericDungeonObject {
    // Constructors
    public Bank() {
        super(false);
    }

    @Override
    public void postMoveActionHook() {
        FantastleReboot.getApplication().getBank().showShop();
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
