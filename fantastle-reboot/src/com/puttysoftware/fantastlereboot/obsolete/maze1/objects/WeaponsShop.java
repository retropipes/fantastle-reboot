package com.puttysoftware.fantastlereboot.obsolete.maze1.objects;

import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.obsolete.maze1.generic.GenericDungeonObject;

public class WeaponsShop extends GenericDungeonObject {
    // Constructors
    public WeaponsShop() {
        super(false);
    }

    @Override
    public void postMoveActionHook() {
        FantastleReboot.getBagOStuff().getWeapons().showShop();
    }

    @Override
    public String getName() {
        return "Weapons Shop";
    }

    @Override
    public String getPluralName() {
        return "Weapons Shops";
    }

    @Override
    public String getDescription() {
        return "Weapons Shops sell weapons used to fight monsters.";
    }

    @Override
    public byte getObjectID() {
        return (byte) 5;
    }
}
