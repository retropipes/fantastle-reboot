package com.puttysoftware.fantastlereboot.obsolete.objects;

import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.obsolete.generic.GenericDungeonObject;

public class SpellShop extends GenericDungeonObject {
    // Constructors
    public SpellShop() {
        super(false);
    }

    @Override
    public void postMoveActionHook() {
        FantastleReboot.getBagOStuff().getSpells().showShop();
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
