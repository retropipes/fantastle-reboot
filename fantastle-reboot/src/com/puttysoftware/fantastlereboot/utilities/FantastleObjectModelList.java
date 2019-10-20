/*  FantastleReboot: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.utilities;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.maze.FormatConstants;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObjectModel;
import com.puttysoftware.fantastlereboot.objects.Amulet;
import com.puttysoftware.fantastlereboot.objects.ArmorShop;
import com.puttysoftware.fantastlereboot.objects.BankShop;
import com.puttysoftware.fantastlereboot.objects.BonusShop;
import com.puttysoftware.fantastlereboot.objects.Button;
import com.puttysoftware.fantastlereboot.objects.ClockwiseRotationTrap;
import com.puttysoftware.fantastlereboot.objects.ConfusionTrap;
import com.puttysoftware.fantastlereboot.objects.CounterclockwiseRotationTrap;
import com.puttysoftware.fantastlereboot.objects.DizzinessTrap;
import com.puttysoftware.fantastlereboot.objects.DrunkTrap;
import com.puttysoftware.fantastlereboot.objects.ElementalShop;
import com.puttysoftware.fantastlereboot.objects.HealShop;
import com.puttysoftware.fantastlereboot.objects.HealTrap;
import com.puttysoftware.fantastlereboot.objects.HurtTrap;
import com.puttysoftware.fantastlereboot.objects.Ice;
import com.puttysoftware.fantastlereboot.objects.ItemShop;
import com.puttysoftware.fantastlereboot.objects.MonsterObject;
import com.puttysoftware.fantastlereboot.objects.NecklaceShop;
import com.puttysoftware.fantastlereboot.objects.Nothing;
import com.puttysoftware.fantastlereboot.objects.OpenSpace;
import com.puttysoftware.fantastlereboot.objects.RegenerateShop;
import com.puttysoftware.fantastlereboot.objects.SealingWall;
import com.puttysoftware.fantastlereboot.objects.SpellShop;
import com.puttysoftware.fantastlereboot.objects.StairsDown;
import com.puttysoftware.fantastlereboot.objects.StairsUp;
import com.puttysoftware.fantastlereboot.objects.Tile;
import com.puttysoftware.fantastlereboot.objects.UTurnTrap;
import com.puttysoftware.fantastlereboot.objects.VariableHealTrap;
import com.puttysoftware.fantastlereboot.objects.VariableHurtTrap;
import com.puttysoftware.fantastlereboot.objects.Wall;
import com.puttysoftware.fantastlereboot.objects.WallOff;
import com.puttysoftware.fantastlereboot.objects.WallOn;
import com.puttysoftware.fantastlereboot.objects.WarpTrap;
import com.puttysoftware.fantastlereboot.objects.WeaponShop;
import com.puttysoftware.images.BufferedImageIcon;
import com.puttysoftware.xio.XDataReader;

public class FantastleObjectModelList {
    // Fields
    private final ArrayList<FantastleObjectModel> allObjectList;

    // Constructor
    public FantastleObjectModelList() {
        final FantastleObjectModel[] allObjects = { new ArmorShop(),
                new BankShop(), new ClockwiseRotationTrap(),
                new ConfusionTrap(), new CounterclockwiseRotationTrap(),
                new DizzinessTrap(), new DrunkTrap(), new OpenSpace(),
                new Nothing(), new BonusShop(), new ElementalShop(),
                new HealShop(), new HealTrap(), new HurtTrap(), new Ice(),
                new ItemShop(), new MonsterObject(), new RegenerateShop(),
                new SealingWall(), new NecklaceShop(), new SpellShop(),
                new Tile(), new UTurnTrap(), new VariableHealTrap(),
                new VariableHurtTrap(), new Wall(), new WarpTrap(),
                new WeaponShop(), new StairsUp(), new StairsDown(),
                new WallOff(), new WallOn(), new Button(), new Amulet() };
        this.allObjectList = new ArrayList<>();
        // Add all predefined objects to the list
        for (int z = 0; z < allObjects.length; z++) {
            this.allObjectList.add(allObjects[z]);
        }
    }

    // Methods
    public FantastleObjectModel[] getAllObjects() {
        return this.allObjectList
                .toArray(new FantastleObjectModel[this.allObjectList.size()]);
    }

    public BufferedImageIcon[] getAllEditorAppearances() {
        final FantastleObjectModel[] objects = this.getAllObjects();
        final BufferedImageIcon[] allEditorAppearances = new BufferedImageIcon[objects.length];
        for (int x = 0; x < allEditorAppearances.length; x++) {
            allEditorAppearances[x] = objects[x].getEditorImage();
        }
        return allEditorAppearances;
    }

    public final int[] getAllCarryableUIDs() {
        final FantastleObjectModel[] objects = this.getAllObjects();
        final int[] tempAllCarryableUIDs = new int[objects.length];
        int x;
        int count = 0;
        for (x = 0; x < objects.length; x++) {
            if (objects[x].isCarryable()) {
                tempAllCarryableUIDs[count] = objects[x].getUniqueID();
                count++;
            }
        }
        if (count == 0) {
            return null;
        } else {
            final int[] allCarryableUIDs = new int[count];
            for (x = 0; x < count; x++) {
                allCarryableUIDs[x] = tempAllCarryableUIDs[x];
            }
            return allCarryableUIDs;
        }
    }

    public final FantastleObjectModel[] getAllRequired(final int layer) {
        final FantastleObjectModel[] objects = this.getAllObjects();
        final FantastleObjectModel[] tempAllRequired = new FantastleObjectModel[objects.length];
        int x;
        int count = 0;
        for (x = 0; x < objects.length; x++) {
            if ((objects[x].getLayer() == layer) && objects[x].isRequired()) {
                tempAllRequired[count] = objects[x];
                count++;
            }
        }
        if (count == 0) {
            return null;
        } else {
            final FantastleObjectModel[] allRequired = new FantastleObjectModel[count];
            for (x = 0; x < count; x++) {
                allRequired[x] = tempAllRequired[x];
            }
            return allRequired;
        }
    }

    public final FantastleObjectModel[] getAllWithoutPrerequisiteAndNotRequired(
            final int layer) {
        final FantastleObjectModel[] objects = this.getAllObjects();
        final FantastleObjectModel[] tempAllWithoutPrereq = new FantastleObjectModel[objects.length];
        int x;
        int count = 0;
        for (x = 0; x < objects.length; x++) {
            if ((objects[x].getLayer() == layer)
                    && !(objects[x].isRequired())) {
                tempAllWithoutPrereq[count] = objects[x];
                count++;
            }
        }
        if (count == 0) {
            return null;
        } else {
            final FantastleObjectModel[] allWithoutPrereq = new FantastleObjectModel[count];
            for (x = 0; x < count; x++) {
                allWithoutPrereq[x] = tempAllWithoutPrereq[x];
            }
            return allWithoutPrereq;
        }
    }

    public final FantastleObjectModel getNewInstanceByUniqueID(final int uid) {
        final FantastleObjectModel[] objects = this.getAllObjects();
        FantastleObjectModel instance = null;
        int x;
        for (x = 0; x < objects.length; x++) {
            if (objects[x].getUniqueID() == uid) {
                instance = objects[x];
                break;
            }
        }
        if (instance == null) {
            return null;
        } else {
            try {
                return instance.getClass().getConstructor().newInstance();
            } catch (final NoSuchMethodException | InstantiationException
                    | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException | SecurityException ex) {
                FantastleReboot.logError(ex);
                return null;
            }
        }
    }

    public FantastleObjectModel readObject(final XDataReader reader,
            final int formatVersion) throws IOException {
        final FantastleObjectModel[] objects = this.getAllObjects();
        FantastleObjectModel o = null;
        String UID = "";
        if (formatVersion == FormatConstants.MAZE_FORMAT_LATEST) {
            UID = reader.readString();
        }
        for (int x = 0; x < objects.length; x++) {
            try {
                FantastleObjectModel instance;
                instance = objects[x].getClass().getConstructor().newInstance();
                if (formatVersion == FormatConstants.MAZE_FORMAT_LATEST) {
                    o = instance.readObject(reader, UID);
                    if (o != null) {
                        return o;
                    }
                }
            } catch (InstantiationException | IllegalAccessException
                    | IllegalArgumentException | InvocationTargetException
                    | NoSuchMethodException | SecurityException ex) {
                FantastleReboot.logError(ex);
            }
        }
        return null;
    }

    public FantastleObjectModel readSavedObject(final XDataReader reader,
            final String UID, final int formatVersion) throws IOException {
        final FantastleObjectModel[] objects = this.getAllObjects();
        FantastleObjectModel o = null;
        for (int x = 0; x < objects.length; x++) {
            try {
                FantastleObjectModel instance;
                instance = objects[x].getClass().getConstructor().newInstance();
                if (formatVersion == FormatConstants.MAZE_FORMAT_LATEST) {
                    o = instance.readObject(reader, UID);
                    if (o != null) {
                        return o;
                    }
                }
            } catch (InstantiationException | IllegalAccessException
                    | IllegalArgumentException | InvocationTargetException
                    | NoSuchMethodException | SecurityException ex) {
                FantastleReboot.logError(ex);
            }
        }
        return null;
    }
}
