/*  TallerTower: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.utilities;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.maze.FormatConstants;
import com.puttysoftware.fantastlereboot.maze.abc.AbstractMazeObject;
import com.puttysoftware.fantastlereboot.maze.objects.Amulet;
import com.puttysoftware.fantastlereboot.maze.objects.ArmorShop;
import com.puttysoftware.fantastlereboot.maze.objects.Bank;
import com.puttysoftware.fantastlereboot.maze.objects.Button;
import com.puttysoftware.fantastlereboot.maze.objects.ClockwiseRotationTrap;
import com.puttysoftware.fantastlereboot.maze.objects.ClosedDoor;
import com.puttysoftware.fantastlereboot.maze.objects.ConfusionTrap;
import com.puttysoftware.fantastlereboot.maze.objects.CounterclockwiseRotationTrap;
import com.puttysoftware.fantastlereboot.maze.objects.DarkGem;
import com.puttysoftware.fantastlereboot.maze.objects.DizzinessTrap;
import com.puttysoftware.fantastlereboot.maze.objects.DrunkTrap;
import com.puttysoftware.fantastlereboot.maze.objects.Empty;
import com.puttysoftware.fantastlereboot.maze.objects.EmptyVoid;
import com.puttysoftware.fantastlereboot.maze.objects.EnhancementShop;
import com.puttysoftware.fantastlereboot.maze.objects.FaithPowerShop;
import com.puttysoftware.fantastlereboot.maze.objects.HealShop;
import com.puttysoftware.fantastlereboot.maze.objects.HealTrap;
import com.puttysoftware.fantastlereboot.maze.objects.HurtTrap;
import com.puttysoftware.fantastlereboot.maze.objects.Ice;
import com.puttysoftware.fantastlereboot.maze.objects.ItemShop;
import com.puttysoftware.fantastlereboot.maze.objects.LightGem;
import com.puttysoftware.fantastlereboot.maze.objects.Monster;
import com.puttysoftware.fantastlereboot.maze.objects.OpenDoor;
import com.puttysoftware.fantastlereboot.maze.objects.Regenerator;
import com.puttysoftware.fantastlereboot.maze.objects.SealingWall;
import com.puttysoftware.fantastlereboot.maze.objects.SocksShop;
import com.puttysoftware.fantastlereboot.maze.objects.SpellShop;
import com.puttysoftware.fantastlereboot.maze.objects.StairsDown;
import com.puttysoftware.fantastlereboot.maze.objects.StairsUp;
import com.puttysoftware.fantastlereboot.maze.objects.Tile;
import com.puttysoftware.fantastlereboot.maze.objects.UTurnTrap;
import com.puttysoftware.fantastlereboot.maze.objects.VariableHealTrap;
import com.puttysoftware.fantastlereboot.maze.objects.VariableHurtTrap;
import com.puttysoftware.fantastlereboot.maze.objects.Wall;
import com.puttysoftware.fantastlereboot.maze.objects.WallOff;
import com.puttysoftware.fantastlereboot.maze.objects.WallOn;
import com.puttysoftware.fantastlereboot.maze.objects.WarpTrap;
import com.puttysoftware.fantastlereboot.maze.objects.WeaponsShop;
import com.puttysoftware.images.BufferedImageIcon;
import com.puttysoftware.xio.XDataReader;

public class MazeObjectList {
    // Fields
    private final ArrayList<AbstractMazeObject> allObjectList;

    // Constructor
    public MazeObjectList() {
        final AbstractMazeObject[] allObjects = { new ArmorShop(), new Bank(),
                new ClockwiseRotationTrap(), new ClosedDoor(),
                new ConfusionTrap(), new CounterclockwiseRotationTrap(),
                new DarkGem(), new DizzinessTrap(), new DrunkTrap(),
                new Empty(), new EmptyVoid(), new EnhancementShop(),
                new FaithPowerShop(), new HealShop(), new HealTrap(),
                new HurtTrap(), new Ice(), new ItemShop(), new LightGem(),
                new Monster(), new OpenDoor(), new Regenerator(),
                new SealingWall(), new SocksShop(), new SpellShop(), new Tile(),
                new UTurnTrap(), new VariableHealTrap(), new VariableHurtTrap(),
                new Wall(), new WarpTrap(), new WeaponsShop(), new StairsUp(),
                new StairsDown(), new WallOff(), new WallOn(), new Button(),
                new Amulet() };
        this.allObjectList = new ArrayList<>();
        // Add all predefined objects to the list
        for (int z = 0; z < allObjects.length; z++) {
            this.allObjectList.add(allObjects[z]);
        }
    }

    // Methods
    public AbstractMazeObject[] getAllObjects() {
        return this.allObjectList
                .toArray(new AbstractMazeObject[this.allObjectList.size()]);
    }

    public String[] getAllDescriptions() {
        final AbstractMazeObject[] objects = this.getAllObjects();
        final String[] allDescriptions = new String[objects.length];
        for (int x = 0; x < objects.length; x++) {
            allDescriptions[x] = objects[x].getDescription();
        }
        return allDescriptions;
    }

    public BufferedImageIcon[] getAllEditorAppearances() {
        final AbstractMazeObject[] objects = this.getAllObjects();
        final BufferedImageIcon[] allEditorAppearances = new BufferedImageIcon[objects.length];
        for (int x = 0; x < allEditorAppearances.length; x++) {
            allEditorAppearances[x] = ObjectImageManager.getImage(
                    objects[x].getName(), objects[x].getBaseID(),
                    AbstractMazeObject.getTemplateColor());
        }
        return allEditorAppearances;
    }

    public final AbstractMazeObject[] getAllRequired(final int layer) {
        final AbstractMazeObject[] objects = this.getAllObjects();
        final AbstractMazeObject[] tempAllRequired = new AbstractMazeObject[objects.length];
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
            final AbstractMazeObject[] allRequired = new AbstractMazeObject[count];
            for (x = 0; x < count; x++) {
                allRequired[x] = tempAllRequired[x];
            }
            return allRequired;
        }
    }

    public final AbstractMazeObject[] getAllWithoutPrerequisiteAndNotRequired(
            final int layer) {
        final AbstractMazeObject[] objects = this.getAllObjects();
        final AbstractMazeObject[] tempAllWithoutPrereq = new AbstractMazeObject[objects.length];
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
            final AbstractMazeObject[] allWithoutPrereq = new AbstractMazeObject[count];
            for (x = 0; x < count; x++) {
                allWithoutPrereq[x] = tempAllWithoutPrereq[x];
            }
            return allWithoutPrereq;
        }
    }

    public final AbstractMazeObject getNewInstanceByName(final String name) {
        final AbstractMazeObject[] objects = this.getAllObjects();
        AbstractMazeObject instance = null;
        int x;
        for (x = 0; x < objects.length; x++) {
            if (objects[x].getName().equals(name)) {
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

    public AbstractMazeObject readMazeObject(final XDataReader reader,
            final int formatVersion) throws IOException {
        final AbstractMazeObject[] objects = this.getAllObjects();
        AbstractMazeObject o = null;
        String UID = "";
        if (formatVersion == FormatConstants.MAZE_FORMAT_LATEST) {
            UID = reader.readString();
        }
        for (int x = 0; x < objects.length; x++) {
            try {
                AbstractMazeObject instance;
                instance = objects[x].getClass().getConstructor().newInstance();
                if (formatVersion == FormatConstants.MAZE_FORMAT_LATEST) {
                    o = instance.readMazeObjectV1(reader, UID);
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

    public AbstractMazeObject readSavedMazeObject(final XDataReader reader,
            final String UID, final int formatVersion) throws IOException {
        final AbstractMazeObject[] objects = this.getAllObjects();
        AbstractMazeObject o = null;
        for (int x = 0; x < objects.length; x++) {
            try {
                AbstractMazeObject instance;
                instance = objects[x].getClass().getConstructor().newInstance();
                if (formatVersion == FormatConstants.MAZE_FORMAT_LATEST) {
                    o = instance.readMazeObjectV1(reader, UID);
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
