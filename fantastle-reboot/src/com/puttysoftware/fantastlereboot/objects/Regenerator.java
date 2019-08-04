package com.puttysoftware.fantastlereboot.objects;

import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.generic.GenericDungeonObject;

public class Regenerator extends GenericDungeonObject {
    // Constructors
    public Regenerator() {
        super(false);
    }

    @Override
    public void postMoveActionHook() {
        FantastleReboot.getApplication().getRegenerator().showShop();
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
