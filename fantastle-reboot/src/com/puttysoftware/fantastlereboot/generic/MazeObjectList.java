/*  Fantastle: A Maze-Solving Game
Copyright (C) 2008-2010 Eric Ahnell

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

Any questions should be directed to the author via email at: fantastle@worldwizard.net
 */
package com.puttysoftware.fantastlereboot.generic;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.loaders.old.GraphicsManager;
import com.puttysoftware.fantastlereboot.maze.Maze;
import com.puttysoftware.fantastlereboot.objects.*;
import com.puttysoftware.fantastlereboot.utilities.FormatConstants;
import com.puttysoftware.fantastlereboot.utilities.TypeConstants;
import com.puttysoftware.images.BufferedImageIcon;
import com.puttysoftware.xio.XDataReader;

public class MazeObjectList {
    // Fields
    private final MazeObject[] allObjects = { new Empty(), new Grass(),
            new Dirt(), new Sand(), new Snow(), new Tundra(), new Tile(),
            new Ice(), new Water(), new Slime(), new Lava(), new SunkenBlock(),
            new ForceField(), new Player(), new Finish(), new FakeFinish(),
            new FinishTo(), new Wall(), new InvisibleWall(), new FakeWall(),
            new BlueWallOff(), new BlueWallOn(), new GreenWallOff(),
            new GreenWallOn(), new MagentaWallOff(), new MagentaWallOn(),
            new OrangeWallOff(), new OrangeWallOn(), new PurpleWallOff(),
            new PurpleWallOn(), new RedWallOff(), new RedWallOn(),
            new WhiteWallOff(), new WhiteWallOn(), new YellowWallOff(),
            new YellowWallOn(), new CyanWallOff(), new CyanWallOn(),
            new OneWayEastWall(), new OneWayNorthWall(), new OneWaySouthWall(),
            new OneWayWestWall(), new ExplodingWall(),
            new BreakableWallHorizontal(), new BreakableWallVertical(),
            new FadingWall(), new DamageableWall(), new CrackedWall(),
            new DamagedWall(), new MasterTrappedWall(), new TrappedWall0(),
            new TrappedWall1(), new TrappedWall2(), new TrappedWall3(),
            new TrappedWall4(), new TrappedWall5(), new TrappedWall6(),
            new TrappedWall7(), new TrappedWall8(), new TrappedWall9(),
            new TrappedWall10(), new TrappedWall11(), new TrappedWall12(),
            new TrappedWall13(), new TrappedWall14(), new TrappedWall15(),
            new TrappedWall16(), new TrappedWall17(), new TrappedWall18(),
            new TrappedWall19(), new BrickWall(), new Hammer(), new Tablet(),
            new TabletSlot(), new EnergySphere(), new APlug(), new APort(),
            new BPlug(), new BPort(), new CPlug(), new CPort(), new DPlug(),
            new DPort(), new EPlug(), new EPort(), new FPlug(), new FPort(),
            new GPlug(), new GPort(), new HPlug(), new HPort(), new IPlug(),
            new IPort(), new JPlug(), new JPort(), new KPlug(), new KPort(),
            new LPlug(), new LPort(), new MPlug(), new MPort(), new NPlug(),
            new NPort(), new OPlug(), new OPort(), new PPlug(), new PPort(),
            new RPlug(), new RPort(), new UPlug(), new UPort(), new ZPlug(),
            new ZPort(), new Key(), new Lock(), new BlueKey(), new BlueLock(),
            new GreenKey(), new GreenLock(), new MagentaKey(),
            new MagentaLock(), new OrangeKey(), new OrangeLock(),
            new PurpleKey(), new PurpleLock(), new RedKey(), new RedLock(),
            new WhiteKey(), new WhiteLock(), new YellowKey(), new YellowLock(),
            new CyanKey(), new CyanLock(), new MetalKey(), new MetalDoor(),
            new Door(), new BlueButton(), new GreenButton(),
            new MagentaButton(), new OrangeButton(), new PurpleButton(),
            new RedButton(), new WhiteButton(), new YellowButton(),
            new CyanButton(), new MetalButton(), new Teleport(),
            new InvisibleTeleport(), new RandomTeleport(),
            new RandomInvisibleTeleport(), new RandomOneShotTeleport(),
            new RandomInvisibleOneShotTeleport(), new OneShotTeleport(),
            new InvisibleOneShotTeleport(), new TwoWayTeleport(),
            new ControllableTeleport(), new OneShotControllableTeleport(),
            new StairsUp(), new StairsDown(), new Pit(), new InvisiblePit(),
            new PushableBlock(), new PullableBlock(),
            new PushablePullableBlock(), new PushableBlockOnce(),
            new PushableBlockTwice(), new PushableBlockThrice(),
            new PullableBlockOnce(), new PullableBlockTwice(),
            new PullableBlockThrice(), new MovingBlock(), new HealBoots(),
            new RegenBoots(), new MoneyBoots(), new ExperienceBoots(),
            new MetalBoots(), new NoBoots(), new GlueBoots(), new AquaBoots(),
            new BioHazardBoots(), new FireBoots(), new AnnihilationWand(),
            new FinishMakingWand(), new WallMakingWand(), new TeleportWand(),
            new WallBreakingWand(), new DisarmTrapWand(),
            new RemoteActionWand(), new RotationWand(), new WarpWand(),
            new EmptyVoid(), new ClockwiseRotationTrap(),
            new CounterclockwiseRotationTrap(), new UTurnTrap(),
            new ConfusionTrap(), new DizzinessTrap(), new DrunkTrap(),
            new WallMakingTrap(), new RotationTrap(), new WarpTrap(),
            new DamageTrap(), new DrainTrap(), new EasierTrap(),
            new HarderTrap(), new ArrowTrap(), new MasterWallTrap(),
            new WallTrap0(), new WallTrap1(), new WallTrap2(), new WallTrap3(),
            new WallTrap4(), new WallTrap5(), new WallTrap6(), new WallTrap7(),
            new WallTrap8(), new WallTrap9(), new WallTrap10(),
            new WallTrap11(), new WallTrap12(), new WallTrap13(),
            new WallTrap14(), new WallTrap15(), new WallTrap16(),
            new WallTrap17(), new WallTrap18(), new WallTrap19(),
            new TreasureChest(), new DimnessGem(), new DarknessGem(),
            new LightnessGem(), new BrightnessGem(), new MinorHealPotion(),
            new MinorHurtPotion(), new MinorUnknownPotion(),
            new MajorHealPotion(), new MajorHurtPotion(),
            new MajorUnknownPotion(), new SuperHealPotion(),
            new SuperHurtPotion(), new SuperUnknownPotion(),
            new MinorRegenPotion(), new MinorDrainPotion(),
            new MinorRandomPotion(), new MajorRegenPotion(),
            new MajorDrainPotion(), new MajorRandomPotion(),
            new SuperRegenPotion(), new SuperDrainPotion(),
            new SuperRandomPotion(), new HorizontalBarrier(),
            new VerticalBarrier(), new BarrierGenerator(), new ArmorShop(),
            new Bank(), new Healer(), new ItemShop(), new Regenerator(),
            new SpellShop(), new WeaponsShop(), new Monster(), new Bomb(),
            new SmokeBomb(), new WarpBomb(), new IceBomb(), new ShuffleBomb(),
            new IceBow() };

    public MazeObject[] getAllObjects() {
        return this.allObjects;
    }

    public String[] getAllNames() {
        final String[] allNames = new String[this.allObjects.length];
        for (int x = 0; x < this.allObjects.length; x++) {
            allNames[x] = this.allObjects[x].getName();
        }
        return allNames;
    }

    public String[] getAllNamesForCache() {
        final String[] allNames = new String[this.allObjects.length + 21];
        for (int x = 0; x < this.allObjects.length; x++) {
            allNames[x] = this.allObjects[x].getName();
        }
        allNames[this.allObjects.length] = "Darkness";
        allNames[this.allObjects.length + 1] = "Sealing Wall";
        allNames[this.allObjects.length + 2] = "Wall Trap";
        allNames[this.allObjects.length + 3] = "Arrow East";
        allNames[this.allObjects.length + 4] = "Arrow North";
        allNames[this.allObjects.length + 5] = "Arrow Northeast";
        allNames[this.allObjects.length + 6] = "Arrow Northwest";
        allNames[this.allObjects.length + 7] = "Arrow South";
        allNames[this.allObjects.length + 8] = "Arrow Southeast";
        allNames[this.allObjects.length + 9] = "Arrow Southwest";
        allNames[this.allObjects.length + 10] = "Arrow West";
        allNames[this.allObjects.length + 11] = "Iced Monster";
        allNames[this.allObjects.length + 12] = "Ice Arrow East";
        allNames[this.allObjects.length + 13] = "Ice Arrow North";
        allNames[this.allObjects.length + 14] = "Ice Arrow Northeast";
        allNames[this.allObjects.length + 15] = "Ice Arrow Northwest";
        allNames[this.allObjects.length + 16] = "Ice Arrow South";
        allNames[this.allObjects.length + 17] = "Ice Arrow Southeast";
        allNames[this.allObjects.length + 18] = "Ice Arrow Southwest";
        allNames[this.allObjects.length + 19] = "Ice Arrow West";
        allNames[this.allObjects.length + 20] = "Iced Barrier Generator";
        return allNames;
    }

    public String[] getAllPluralNames() {
        final String[] allNames = new String[this.allObjects.length];
        for (int x = 0; x < this.allObjects.length; x++) {
            allNames[x] = this.allObjects[x].getPluralName();
        }
        return allNames;
    }

    public String[] getAllDescriptions() {
        final String[] allDescriptions = new String[this.allObjects.length];
        for (int x = 0; x < this.allObjects.length; x++) {
            allDescriptions[x] = this.allObjects[x].getDescription();
        }
        return allDescriptions;
    }

    public MazeObject[] getAllGroundLayerObjects() {
        final MazeObject[] tempAllGroundLayerObjects = new MazeObject[this.allObjects.length];
        int objectCount = 0;
        for (int x = 0; x < this.allObjects.length; x++) {
            if (this.allObjects[x].getLayer() == Maze.LAYER_GROUND) {
                tempAllGroundLayerObjects[x] = this.allObjects[x];
            }
        }
        for (final MazeObject tempAllGroundLayerObject : tempAllGroundLayerObjects) {
            if (tempAllGroundLayerObject != null) {
                objectCount++;
            }
        }
        final MazeObject[] allGroundLayerObjects = new MazeObject[objectCount];
        objectCount = 0;
        for (final MazeObject tempAllGroundLayerObject : tempAllGroundLayerObjects) {
            if (tempAllGroundLayerObject != null) {
                allGroundLayerObjects[objectCount] = tempAllGroundLayerObject;
                objectCount++;
            }
        }
        return allGroundLayerObjects;
    }

    public MazeObject[] getAllObjectLayerObjects() {
        final MazeObject[] tempAllObjectLayerObjects = new MazeObject[this.allObjects.length];
        int objectCount = 0;
        for (int x = 0; x < this.allObjects.length; x++) {
            if (this.allObjects[x].getLayer() == Maze.LAYER_OBJECT) {
                tempAllObjectLayerObjects[x] = this.allObjects[x];
            }
        }
        for (final MazeObject tempAllObjectLayerObject : tempAllObjectLayerObjects) {
            if (tempAllObjectLayerObject != null) {
                objectCount++;
            }
        }
        final MazeObject[] allObjectLayerObjects = new MazeObject[objectCount];
        objectCount = 0;
        for (final MazeObject tempAllObjectLayerObject : tempAllObjectLayerObjects) {
            if (tempAllObjectLayerObject != null) {
                allObjectLayerObjects[objectCount] = tempAllObjectLayerObject;
                objectCount++;
            }
        }
        return allObjectLayerObjects;
    }

    public String[] getAllGroundLayerNames() {
        final String[] tempAllGroundLayerNames = new String[this.allObjects.length];
        int objectCount = 0;
        for (int x = 0; x < this.allObjects.length; x++) {
            if (this.allObjects[x].getLayer() == Maze.LAYER_GROUND) {
                tempAllGroundLayerNames[x] = this.allObjects[x].getName();
            }
        }
        for (final String tempAllGroundLayerName : tempAllGroundLayerNames) {
            if (tempAllGroundLayerName != null) {
                objectCount++;
            }
        }
        final String[] allGroundLayerNames = new String[objectCount];
        objectCount = 0;
        for (final String tempAllGroundLayerName : tempAllGroundLayerNames) {
            if (tempAllGroundLayerName != null) {
                allGroundLayerNames[objectCount] = tempAllGroundLayerName;
                objectCount++;
            }
        }
        return allGroundLayerNames;
    }

    public String[] getAllObjectLayerNames() {
        final String[] tempAllObjectLayerNames = new String[this.allObjects.length];
        int objectCount = 0;
        for (int x = 0; x < this.allObjects.length; x++) {
            if (this.allObjects[x].getLayer() == Maze.LAYER_OBJECT) {
                tempAllObjectLayerNames[x] = this.allObjects[x].getName();
            }
        }
        for (final String tempAllObjectLayerName : tempAllObjectLayerNames) {
            if (tempAllObjectLayerName != null) {
                objectCount++;
            }
        }
        final String[] allObjectLayerNames = new String[objectCount];
        objectCount = 0;
        for (final String tempAllObjectLayerName : tempAllObjectLayerNames) {
            if (tempAllObjectLayerName != null) {
                allObjectLayerNames[objectCount] = tempAllObjectLayerName;
                objectCount++;
            }
        }
        return allObjectLayerNames;
    }

    public BufferedImageIcon[] getAllEditorAppearances() {
        final BufferedImageIcon[] allEditorAppearances = new BufferedImageIcon[this.allObjects.length];
        for (int x = 0; x < allEditorAppearances.length; x++) {
            allEditorAppearances[x] = GraphicsManager
                    .getTransformedImage(this.allObjects[x].getName());
        }
        return allEditorAppearances;
    }

    public BufferedImageIcon[] getAllGroundLayerEditorAppearances() {
        final BufferedImageIcon[] tempAllGroundLayerEditorAppearances = new BufferedImageIcon[this.allObjects.length];
        int objectCount = 0;
        for (int x = 0; x < this.allObjects.length; x++) {
            if (this.allObjects[x].getLayer() == Maze.LAYER_GROUND) {
                tempAllGroundLayerEditorAppearances[x] = GraphicsManager
                        .getTransformedImage(this.allObjects[x].getName());
            }
        }
        for (final BufferedImageIcon tempAllGroundLayerEditorAppearance : tempAllGroundLayerEditorAppearances) {
            if (tempAllGroundLayerEditorAppearance != null) {
                objectCount++;
            }
        }
        final BufferedImageIcon[] allGroundLayerEditorAppearances = new BufferedImageIcon[objectCount];
        objectCount = 0;
        for (final BufferedImageIcon tempAllGroundLayerEditorAppearance : tempAllGroundLayerEditorAppearances) {
            if (tempAllGroundLayerEditorAppearance != null) {
                allGroundLayerEditorAppearances[objectCount] = tempAllGroundLayerEditorAppearance;
                objectCount++;
            }
        }
        return allGroundLayerEditorAppearances;
    }

    public BufferedImageIcon[] getAllObjectLayerEditorAppearances() {
        final BufferedImageIcon[] tempAllObjectLayerEditorAppearances = new BufferedImageIcon[this.allObjects.length];
        int objectCount = 0;
        for (int x = 0; x < this.allObjects.length; x++) {
            if (this.allObjects[x].getLayer() == Maze.LAYER_OBJECT) {
                tempAllObjectLayerEditorAppearances[x] = GraphicsManager
                        .getTransformedImage(this.allObjects[x].getName());
            }
        }
        for (final BufferedImageIcon tempAllObjectLayerEditorAppearance : tempAllObjectLayerEditorAppearances) {
            if (tempAllObjectLayerEditorAppearance != null) {
                objectCount++;
            }
        }
        final BufferedImageIcon[] allObjectLayerEditorAppearances = new BufferedImageIcon[objectCount];
        objectCount = 0;
        for (final BufferedImageIcon tempAllObjectLayerEditorAppearance : tempAllObjectLayerEditorAppearances) {
            if (tempAllObjectLayerEditorAppearance != null) {
                allObjectLayerEditorAppearances[objectCount] = tempAllObjectLayerEditorAppearance;
                objectCount++;
            }
        }
        return allObjectLayerEditorAppearances;
    }

    public BufferedImageIcon[] getAllContainableObjectEditorAppearances() {
        final BufferedImageIcon[] tempAllContainableObjectEditorAppearances = new BufferedImageIcon[this.allObjects.length];
        int objectCount = 0;
        for (int x = 0; x < this.allObjects.length; x++) {
            if (this.allObjects[x].isOfType(TypeConstants.TYPE_CONTAINABLE)) {
                tempAllContainableObjectEditorAppearances[x] = GraphicsManager
                        .getTransformedImage(this.allObjects[x].getName());
            }
        }
        for (final BufferedImageIcon tempAllContainableObjectEditorAppearance : tempAllContainableObjectEditorAppearances) {
            if (tempAllContainableObjectEditorAppearance != null) {
                objectCount++;
            }
        }
        final BufferedImageIcon[] allContainableObjectEditorAppearances = new BufferedImageIcon[objectCount];
        objectCount = 0;
        for (final BufferedImageIcon tempAllContainableObjectEditorAppearance : tempAllContainableObjectEditorAppearances) {
            if (tempAllContainableObjectEditorAppearance != null) {
                allContainableObjectEditorAppearances[objectCount] = tempAllContainableObjectEditorAppearance;
                objectCount++;
            }
        }
        return allContainableObjectEditorAppearances;
    }

    public MazeObject[] getAllContainableObjects() {
        final MazeObject[] tempAllContainableObjects = new MazeObject[this.allObjects.length];
        int objectCount = 0;
        for (int x = 0; x < this.allObjects.length; x++) {
            if (this.allObjects[x].isOfType(TypeConstants.TYPE_CONTAINABLE)) {
                tempAllContainableObjects[x] = this.allObjects[x];
            }
        }
        for (final MazeObject tempAllContainableObject : tempAllContainableObjects) {
            if (tempAllContainableObject != null) {
                objectCount++;
            }
        }
        final MazeObject[] allContainableObjects = new MazeObject[objectCount];
        objectCount = 0;
        for (final MazeObject tempAllContainableObject : tempAllContainableObjects) {
            if (tempAllContainableObject != null) {
                allContainableObjects[objectCount] = tempAllContainableObject;
                objectCount++;
            }
        }
        return allContainableObjects;
    }

    public String[] getAllContainableNames() {
        final String[] tempAllContainableNames = new String[this.allObjects.length];
        int objectCount = 0;
        for (int x = 0; x < this.allObjects.length; x++) {
            if (this.allObjects[x].isOfType(TypeConstants.TYPE_CONTAINABLE)) {
                tempAllContainableNames[x] = this.allObjects[x].getName();
            }
        }
        for (final String tempAllContainableName : tempAllContainableNames) {
            if (tempAllContainableName != null) {
                objectCount++;
            }
        }
        final String[] allContainableNames = new String[objectCount];
        objectCount = 0;
        for (final String tempAllContainableName : tempAllContainableNames) {
            if (tempAllContainableName != null) {
                allContainableNames[objectCount] = tempAllContainableName;
                objectCount++;
            }
        }
        return allContainableNames;
    }

    public MazeObject[] getAllInventoryableObjects() {
        final MazeObject[] tempAllInventoryableObjects = new MazeObject[this.allObjects.length];
        int objectCount = 0;
        for (int x = 0; x < this.allObjects.length; x++) {
            if (this.allObjects[x].isInventoryable()) {
                tempAllInventoryableObjects[x] = this.allObjects[x];
            }
        }
        for (final MazeObject tempAllInventoryableObject : tempAllInventoryableObjects) {
            if (tempAllInventoryableObject != null) {
                objectCount++;
            }
        }
        final MazeObject[] allInventoryableObjects = new MazeObject[objectCount];
        objectCount = 0;
        for (final MazeObject tempAllInventoryableObject : tempAllInventoryableObjects) {
            if (tempAllInventoryableObject != null) {
                allInventoryableObjects[objectCount] = tempAllInventoryableObject;
                objectCount++;
            }
        }
        return allInventoryableObjects;
    }

    public String[] getAllInventoryableNames() {
        final String[] tempAllInventoryableNames = new String[this.allObjects.length];
        int objectCount = 0;
        for (int x = 0; x < this.allObjects.length; x++) {
            if (this.allObjects[x].isInventoryable()) {
                tempAllInventoryableNames[x] = this.allObjects[x].getName();
            }
        }
        for (final String tempAllInventoryableName : tempAllInventoryableNames) {
            if (tempAllInventoryableName != null) {
                objectCount++;
            }
        }
        final String[] allInventoryableNames = new String[objectCount];
        objectCount = 0;
        for (final String tempAllInventoryableName : tempAllInventoryableNames) {
            if (tempAllInventoryableName != null) {
                allInventoryableNames[objectCount] = tempAllInventoryableName;
                objectCount++;
            }
        }
        return allInventoryableNames;
    }

    public String[] getAllInventoryablePluralNames() {
        final String[] tempAllInventoryableNames = new String[this.allObjects.length];
        int objectCount = 0;
        for (int x = 0; x < this.allObjects.length; x++) {
            if (this.allObjects[x].isInventoryable()) {
                tempAllInventoryableNames[x] = this.allObjects[x]
                        .getPluralName();
            }
        }
        for (final String tempAllInventoryableName : tempAllInventoryableNames) {
            if (tempAllInventoryableName != null) {
                objectCount++;
            }
        }
        final String[] allInventoryableNames = new String[objectCount];
        objectCount = 0;
        for (final String tempAllInventoryableName : tempAllInventoryableNames) {
            if (tempAllInventoryableName != null) {
                allInventoryableNames[objectCount] = tempAllInventoryableName;
                objectCount++;
            }
        }
        return allInventoryableNames;
    }

    public MazeObject[] getAllInventoryableObjectsMinusBoots() {
        final MazeObject[] tempAllInventoryableObjects = new MazeObject[this.allObjects.length];
        int objectCount = 0;
        for (int x = 0; x < this.allObjects.length; x++) {
            if (this.allObjects[x].isInventoryable()
                    && !this.allObjects[x].isOfType(TypeConstants.TYPE_BOOTS)) {
                tempAllInventoryableObjects[x] = this.allObjects[x];
            }
        }
        for (final MazeObject tempAllInventoryableObject : tempAllInventoryableObjects) {
            if (tempAllInventoryableObject != null) {
                objectCount++;
            }
        }
        final MazeObject[] allInventoryableObjects = new MazeObject[objectCount];
        objectCount = 0;
        for (final MazeObject tempAllInventoryableObject : tempAllInventoryableObjects) {
            if (tempAllInventoryableObject != null) {
                allInventoryableObjects[objectCount] = tempAllInventoryableObject;
                objectCount++;
            }
        }
        return allInventoryableObjects;
    }

    public String[] getAllInventoryableNamesMinusBoots() {
        final String[] tempAllInventoryableNames = new String[this.allObjects.length];
        int objectCount = 0;
        for (int x = 0; x < this.allObjects.length; x++) {
            if (this.allObjects[x].isInventoryable()
                    && !this.allObjects[x].isOfType(TypeConstants.TYPE_BOOTS)) {
                tempAllInventoryableNames[x] = this.allObjects[x].getName();
            }
        }
        for (final String tempAllInventoryableName : tempAllInventoryableNames) {
            if (tempAllInventoryableName != null) {
                objectCount++;
            }
        }
        final String[] allInventoryableNames = new String[objectCount];
        objectCount = 0;
        for (final String tempAllInventoryableName : tempAllInventoryableNames) {
            if (tempAllInventoryableName != null) {
                allInventoryableNames[objectCount] = tempAllInventoryableName;
                objectCount++;
            }
        }
        return allInventoryableNames;
    }

    public String[] getAllInventoryablePluralNamesMinusBoots() {
        final String[] tempAllInventoryableNames = new String[this.allObjects.length];
        int objectCount = 0;
        for (int x = 0; x < this.allObjects.length; x++) {
            if (this.allObjects[x].isInventoryable()
                    && !this.allObjects[x].isOfType(TypeConstants.TYPE_BOOTS)) {
                tempAllInventoryableNames[x] = this.allObjects[x]
                        .getPluralName();
            }
        }
        for (final String tempAllInventoryableName : tempAllInventoryableNames) {
            if (tempAllInventoryableName != null) {
                objectCount++;
            }
        }
        final String[] allInventoryableNames = new String[objectCount];
        objectCount = 0;
        for (final String tempAllInventoryableName : tempAllInventoryableNames) {
            if (tempAllInventoryableName != null) {
                allInventoryableNames[objectCount] = tempAllInventoryableName;
                objectCount++;
            }
        }
        return allInventoryableNames;
    }

    public MazeObject[] getAllUsableObjects() {
        final MazeObject[] tempAllUsableObjects = new MazeObject[this.allObjects.length];
        int objectCount = 0;
        for (int x = 0; x < this.allObjects.length; x++) {
            if (this.allObjects[x].isUsable()) {
                tempAllUsableObjects[x] = this.allObjects[x];
            }
        }
        for (final MazeObject tempAllUsableObject : tempAllUsableObjects) {
            if (tempAllUsableObject != null) {
                objectCount++;
            }
        }
        final MazeObject[] allUsableObjects = new MazeObject[objectCount];
        objectCount = 0;
        for (final MazeObject tempAllUsableObject : tempAllUsableObjects) {
            if (tempAllUsableObject != null) {
                allUsableObjects[objectCount] = tempAllUsableObject;
                objectCount++;
            }
        }
        return allUsableObjects;
    }

    public String[] getAllUsableNames() {
        final String[] tempAllUsableNames = new String[this.allObjects.length];
        int objectCount = 0;
        for (int x = 0; x < this.allObjects.length; x++) {
            if (this.allObjects[x].isUsable()) {
                tempAllUsableNames[x] = this.allObjects[x].getName();
            }
        }
        for (final String tempAllUsableName : tempAllUsableNames) {
            if (tempAllUsableName != null) {
                objectCount++;
            }
        }
        final String[] allUsableNames = new String[objectCount];
        objectCount = 0;
        for (final String tempAllUsableName : tempAllUsableNames) {
            if (tempAllUsableName != null) {
                allUsableNames[objectCount] = tempAllUsableName;
                objectCount++;
            }
        }
        return allUsableNames;
    }

    public String[] getAllUsablePluralNames() {
        final String[] tempAllUsableNames = new String[this.allObjects.length];
        int objectCount = 0;
        for (int x = 0; x < this.allObjects.length; x++) {
            if (this.allObjects[x].isUsable()) {
                tempAllUsableNames[x] = this.allObjects[x].getPluralName();
            }
        }
        for (final String tempAllUsableName : tempAllUsableNames) {
            if (tempAllUsableName != null) {
                objectCount++;
            }
        }
        final String[] allUsableNames = new String[objectCount];
        objectCount = 0;
        for (final String tempAllUsableName : tempAllUsableNames) {
            if (tempAllUsableName != null) {
                allUsableNames[objectCount] = tempAllUsableName;
                objectCount++;
            }
        }
        return allUsableNames;
    }

    public MazeObject readMazeObject(final XDataReader reader,
            final int formatVersion) throws IOException {
        MazeObject o = null;
        String ident = null;
        int UID = 0;
        byte groupID = 0;
        byte objectID = 0;
        if (formatVersion == FormatConstants.MAZE_FORMAT_3) {
            groupID = reader.readByte();
            objectID = reader.readByte();
        } else if (formatVersion == FormatConstants.MAZE_FORMAT_4) {
            ident = reader.readString();
        } else if (formatVersion == FormatConstants.MAZE_FORMAT_5) {
            UID = reader.readInt();
        }
        for (final MazeObject allObject : this.allObjects) {
            if (allObject.hasAdditionalProperties()) {
                try {
                    final MazeObject instance = allObject.getClass()
                            .getConstructor().newInstance();
                    if (formatVersion == FormatConstants.MAZE_FORMAT_3) {
                        o = instance.readMazeObject(reader, groupID, objectID);
                    } else if (formatVersion == FormatConstants.MAZE_FORMAT_4) {
                        o = instance.readMazeObject(reader, ident);
                    } else if (formatVersion == FormatConstants.MAZE_FORMAT_5) {
                        o = instance.readMazeObject(reader, UID);
                    }
                    if (o != null) {
                        return o;
                    }
                } catch (final InstantiationException
                        | InvocationTargetException | NoSuchMethodException
                        | IllegalAccessException ex) {
                    FantastleReboot.logError(ex);
                }
            } else {
                if (formatVersion == FormatConstants.MAZE_FORMAT_3) {
                    o = allObject.readMazeObject(reader, groupID, objectID);
                } else if (formatVersion == FormatConstants.MAZE_FORMAT_4) {
                    o = allObject.readMazeObject(reader, ident);
                } else if (formatVersion == FormatConstants.MAZE_FORMAT_5) {
                    o = allObject.readMazeObject(reader, UID);
                }
                if (o != null) {
                    return o;
                }
            }
        }
        return null;
    }
}
