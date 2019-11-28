/*  FantastleReboot: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.objectmodel;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.files.versions.MazeVersions;
import com.puttysoftware.fantastlereboot.loaders.DataLoader;
import com.puttysoftware.fantastlereboot.objects.ArmorShop;
import com.puttysoftware.fantastlereboot.objects.BankShop;
import com.puttysoftware.fantastlereboot.objects.BonusShop;
import com.puttysoftware.fantastlereboot.objects.ElementalShop;
import com.puttysoftware.fantastlereboot.objects.HealShop;
import com.puttysoftware.fantastlereboot.objects.Ice;
import com.puttysoftware.fantastlereboot.objects.ItemShop;
import com.puttysoftware.fantastlereboot.objects.NecklaceShop;
import com.puttysoftware.fantastlereboot.objects.Nothing;
import com.puttysoftware.fantastlereboot.objects.OpenSpace;
import com.puttysoftware.fantastlereboot.objects.Pit;
import com.puttysoftware.fantastlereboot.objects.RegenerateShop;
import com.puttysoftware.fantastlereboot.objects.SealingWall;
import com.puttysoftware.fantastlereboot.objects.SpellShop;
import com.puttysoftware.fantastlereboot.objects.Spring;
import com.puttysoftware.fantastlereboot.objects.StairsDown;
import com.puttysoftware.fantastlereboot.objects.StairsUp;
import com.puttysoftware.fantastlereboot.objects.SuperPit;
import com.puttysoftware.fantastlereboot.objects.SuperSpring;
import com.puttysoftware.fantastlereboot.objects.Tile;
import com.puttysoftware.fantastlereboot.objects.Wall;
import com.puttysoftware.fantastlereboot.objects.WeaponShop;
import com.puttysoftware.images.BufferedImageIcon;
import com.puttysoftware.xio.XDataReader;

public final class GameObjects {
  // Fields
  private static ArrayList<FantastleObjectModel> allObjectList;
  private static FantastleObjectActions[] allActionList;
  private static int[] allShopActionList;

  // Constructor
  private GameObjects() {
  }

  // Methods
  public static void initializeObjects() {
    GameObjects.allObjectList = new ArrayList<>();
    final FantastleObjectModel[] allObjects = { new ArmorShop(), new BankShop(),
        new OpenSpace(), new Nothing(), new BonusShop(), new ElementalShop(),
        new HealShop(), new Ice(), new ItemShop(), new RegenerateShop(),
        new SealingWall(), new NecklaceShop(), new SpellShop(), new Tile(),
        new Wall(), new WeaponShop(), new Wall(), new StairsUp(),
        new StairsDown(), new Pit(), new Spring(), new SuperPit(),
        new SuperSpring() };
    GameObjects.allObjectList.clear();
    // Populate lists
    for (final FantastleObjectModel allObject : allObjects) {
      GameObjects.allObjectList.add(allObject);
    }
    GameObjects.allActionList = DataLoader.loadObjectActionData();
    GameObjects.allShopActionList = DataLoader
        .loadObjectActionAddonData(FantastleObjectActions.SHOP);
  }

  public static boolean sendsDown(final FantastleObjectModel obj) {
    return GameObjects.allActionList[obj.getUniqueID()]
        .get(FantastleObjectActions.DOWN_1_FLOOR);
  }

  public static boolean sendsUp(final FantastleObjectModel obj) {
    return GameObjects.allActionList[obj.getUniqueID()]
        .get(FantastleObjectActions.UP_1_FLOOR);
  }

  public static boolean sendsDown2(final FantastleObjectModel obj) {
    return GameObjects.allActionList[obj.getUniqueID()]
        .get(FantastleObjectActions.DOWN_2_FLOORS);
  }

  public static boolean sendsUp2(final FantastleObjectModel obj) {
    return GameObjects.allActionList[obj.getUniqueID()]
        .get(FantastleObjectActions.UP_2_FLOORS);
  }

  public static boolean sendsNext(final FantastleObjectModel obj) {
    return GameObjects.allActionList[obj.getUniqueID()]
        .get(FantastleObjectActions.NEXT_LEVEL);
  }

  public static boolean sendsPrevious(final FantastleObjectModel obj) {
    return GameObjects.allActionList[obj.getUniqueID()]
        .get(FantastleObjectActions.PREVIOUS_LEVEL);
  }

  public static boolean movesRandomly(final FantastleObjectModel obj) {
    return GameObjects.allActionList[obj.getUniqueID()]
        .get(FantastleObjectActions.MOVE_SELF);
  }

  public static boolean startsBattle(final FantastleObjectModel obj) {
    return GameObjects.allActionList[obj.getUniqueID()]
        .get(FantastleObjectActions.BATTLE);
  }

  public static boolean sendsToShop(final FantastleObjectModel obj) {
    return GameObjects.allActionList[obj.getUniqueID()]
        .get(FantastleObjectActions.SHOP);
  }

  public static int sendsToWhichShop(final FantastleObjectModel obj) {
    return GameObjects.allShopActionList[obj.getUniqueID()];
  }

  public static FantastleObjectModel[] getAllObjects() {
    return GameObjects.allObjectList
        .toArray(new FantastleObjectModel[GameObjects.allObjectList.size()]);
  }

  public static BufferedImageIcon[] getAllEditorAppearances() {
    final FantastleObjectModel[] objects = GameObjects.getAllObjects();
    final BufferedImageIcon[] allEditorAppearances = new BufferedImageIcon[objects.length];
    for (int x = 0; x < allEditorAppearances.length; x++) {
      allEditorAppearances[x] = objects[x].getEditorImage();
    }
    return allEditorAppearances;
  }

  public static FantastleObjectModel[] getAllObjectsOnLayer(final int layer) {
    final FantastleObjectModel[] objects = GameObjects.getAllObjects();
    final FantastleObjectModel[] tempAll = new FantastleObjectModel[objects.length];
    int x;
    int count = 0;
    for (x = 0; x < objects.length; x++) {
      if (objects[x].getLayer() == layer) {
        tempAll[count] = objects[x];
        count++;
      }
    }
    if (count == 0) {
      return new FantastleObjectModel[0];
    } else {
      final FantastleObjectModel[] all = new FantastleObjectModel[count];
      for (x = 0; x < count; x++) {
        all[x] = tempAll[x];
      }
      return all;
    }
  }

  public static FantastleObjectModel[] getAllGroundLayerObjects() {
    final FantastleObjectModel[] objects = GameObjects.getAllObjects();
    final FantastleObjectModel[] tempAll = new FantastleObjectModel[objects.length];
    int x;
    int count = 0;
    for (x = 0; x < objects.length; x++) {
      if (objects[x].getLayer() == Layers.GROUND) {
        tempAll[count] = objects[x];
        count++;
      }
    }
    if (count == 0) {
      return new FantastleObjectModel[0];
    } else {
      final FantastleObjectModel[] all = new FantastleObjectModel[count];
      for (x = 0; x < count; x++) {
        all[x] = tempAll[x];
      }
      return all;
    }
  }

  public static BufferedImageIcon[] getAllGroundLayerEditorAppearances() {
    final FantastleObjectModel[] objects = GameObjects.getAllObjects();
    final BufferedImageIcon[] tempAll = new BufferedImageIcon[objects.length];
    int x;
    int count = 0;
    for (x = 0; x < objects.length; x++) {
      if (objects[x].getLayer() == Layers.GROUND) {
        tempAll[count] = objects[x].getEditorImage();
        count++;
      }
    }
    if (count == 0) {
      return new BufferedImageIcon[0];
    } else {
      final BufferedImageIcon[] all = new BufferedImageIcon[count];
      for (x = 0; x < count; x++) {
        all[x] = tempAll[x];
      }
      return all;
    }
  }

  public static FantastleObjectModel[] getAllObjectLayerObjects() {
    final FantastleObjectModel[] objects = GameObjects.getAllObjects();
    final FantastleObjectModel[] tempAll = new FantastleObjectModel[objects.length];
    int x;
    int count = 0;
    for (x = 0; x < objects.length; x++) {
      if (objects[x].getLayer() == Layers.OBJECT) {
        tempAll[count] = objects[x];
        count++;
      }
    }
    if (count == 0) {
      return new FantastleObjectModel[0];
    } else {
      final FantastleObjectModel[] all = new FantastleObjectModel[count];
      for (x = 0; x < count; x++) {
        all[x] = tempAll[x];
      }
      return all;
    }
  }

  public static BufferedImageIcon[] getAllObjectLayerEditorAppearances() {
    final FantastleObjectModel[] objects = GameObjects.getAllObjects();
    final BufferedImageIcon[] tempAll = new BufferedImageIcon[objects.length];
    int x;
    int count = 0;
    for (x = 0; x < objects.length; x++) {
      if (objects[x].getLayer() == Layers.OBJECT) {
        tempAll[count] = objects[x].getEditorImage();
        count++;
      }
    }
    if (count == 0) {
      return new BufferedImageIcon[0];
    } else {
      final BufferedImageIcon[] all = new BufferedImageIcon[count];
      for (x = 0; x < count; x++) {
        all[x] = tempAll[x];
      }
      return all;
    }
  }

  public static int[] getAllCarryableUIDs() {
    final FantastleObjectModel[] objects = GameObjects.getAllObjects();
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
      return new int[0];
    } else {
      final int[] allCarryableUIDs = new int[count];
      for (x = 0; x < count; x++) {
        allCarryableUIDs[x] = tempAllCarryableUIDs[x];
      }
      return allCarryableUIDs;
    }
  }

  public static FantastleObjectModel[] getAllRequired(final int layer) {
    final FantastleObjectModel[] objects = GameObjects.getAllObjects();
    final FantastleObjectModel[] tempAllRequired = new FantastleObjectModel[objects.length];
    int x;
    int count = 0;
    for (x = 0; x < objects.length; x++) {
      if (objects[x].getLayer() == layer && objects[x].isRequired()) {
        tempAllRequired[count] = objects[x];
        count++;
      }
    }
    if (count == 0) {
      return new FantastleObjectModel[0];
    } else {
      final FantastleObjectModel[] allRequired = new FantastleObjectModel[count];
      for (x = 0; x < count; x++) {
        allRequired[x] = tempAllRequired[x];
      }
      return allRequired;
    }
  }

  public static FantastleObjectModel[]
      getAllWithoutPrerequisiteAndNotRequired(final int layer) {
    final FantastleObjectModel[] objects = GameObjects.getAllObjects();
    final FantastleObjectModel[] tempAllWithoutPrereq = new FantastleObjectModel[objects.length];
    int x;
    int count = 0;
    for (x = 0; x < objects.length; x++) {
      if (objects[x].getLayer() == layer && !objects[x].isRequired()) {
        tempAllWithoutPrereq[count] = objects[x];
        count++;
      }
    }
    if (count == 0) {
      return new FantastleObjectModel[0];
    } else {
      final FantastleObjectModel[] allWithoutPrereq = new FantastleObjectModel[count];
      for (x = 0; x < count; x++) {
        allWithoutPrereq[x] = tempAllWithoutPrereq[x];
      }
      return allWithoutPrereq;
    }
  }

  public static FantastleObjectModel getNewInstanceByUniqueID(final int uid) {
    final FantastleObjectModel[] objects = GameObjects.getAllObjects();
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

  public static FantastleObjectModel readObject(final XDataReader reader,
      final int formatVersion) throws IOException {
    final FantastleObjectModel[] objects = GameObjects.getAllObjects();
    FantastleObjectModel o = null;
    int UID = -1;
    if (formatVersion == MazeVersions.LATEST) {
      UID = reader.readInt();
    }
    for (final FantastleObjectModel object : objects) {
      try {
        FantastleObjectModel instance;
        instance = object.getClass().getConstructor().newInstance();
        if (formatVersion == MazeVersions.LATEST) {
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

  public static FantastleObjectModel readSavedObject(final XDataReader reader,
      final int UID, final int formatVersion) throws IOException {
    final FantastleObjectModel[] objects = GameObjects.getAllObjects();
    FantastleObjectModel o = null;
    for (final FantastleObjectModel object : objects) {
      try {
        FantastleObjectModel instance;
        instance = object.getClass().getConstructor().newInstance();
        if (formatVersion == MazeVersions.LATEST) {
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
