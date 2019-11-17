/*  FantastleReboot: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.objectmodel;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.files.MazeVersions;
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

public final class FantastleObjectModelList {
  // Fields
  private final ArrayList<FantastleObjectModel> allObjectList;
  private FantastleObjectActions[] allActionList;
  private int[] allShopActionList;

  // Constructor
  public FantastleObjectModelList() {
    this.allObjectList = new ArrayList<>();
  }

  // Methods
  public final void initializeObjects() {
    final FantastleObjectModel[] allObjects = { new ArmorShop(), new BankShop(),
        new OpenSpace(), new Nothing(), new BonusShop(), new ElementalShop(),
        new HealShop(), new Ice(), new ItemShop(), new RegenerateShop(),
        new SealingWall(), new NecklaceShop(), new SpellShop(), new Tile(),
        new Wall(), new WeaponShop(), new Wall(), new StairsUp(),
        new StairsDown(), new Pit(), new Spring(), new SuperPit(),
        new SuperSpring() };
    this.allObjectList.clear();
    // Populate lists
    for (int z = 0; z < allObjects.length; z++) {
      this.allObjectList.add(allObjects[z]);
    }
    this.allActionList = DataLoader.loadObjectActionData();
    this.allShopActionList = DataLoader
        .loadObjectActionAddonData(FantastleObjectActions.SHOP);
  }

  public boolean sendsDown(FantastleObjectModel obj) {
    return this.allActionList[obj.getUniqueID()]
        .get(FantastleObjectActions.DOWN_1_FLOOR);
  }

  public boolean sendsUp(FantastleObjectModel obj) {
    return this.allActionList[obj.getUniqueID()]
        .get(FantastleObjectActions.UP_1_FLOOR);
  }

  public boolean sendsDown2(FantastleObjectModel obj) {
    return this.allActionList[obj.getUniqueID()]
        .get(FantastleObjectActions.DOWN_2_FLOORS);
  }

  public boolean sendsUp2(FantastleObjectModel obj) {
    return this.allActionList[obj.getUniqueID()]
        .get(FantastleObjectActions.UP_2_FLOORS);
  }

  public boolean sendsNext(FantastleObjectModel obj) {
    return this.allActionList[obj.getUniqueID()]
        .get(FantastleObjectActions.NEXT_LEVEL);
  }

  public boolean sendsPrevious(FantastleObjectModel obj) {
    return this.allActionList[obj.getUniqueID()]
        .get(FantastleObjectActions.PREVIOUS_LEVEL);
  }

  public boolean movesRandomly(FantastleObjectModel obj) {
    return this.allActionList[obj.getUniqueID()]
        .get(FantastleObjectActions.MOVE_SELF);
  }

  public boolean startsBattle(FantastleObjectModel obj) {
    return this.allActionList[obj.getUniqueID()]
        .get(FantastleObjectActions.BATTLE);
  }

  public boolean sendsToShop(FantastleObjectModel obj) {
    return this.allActionList[obj.getUniqueID()]
        .get(FantastleObjectActions.SHOP);
  }

  public int sendsToWhichShop(FantastleObjectModel obj) {
    return this.allShopActionList[obj.getUniqueID()];
  }

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

  public final FantastleObjectModel[] getAllGroundLayerObjects() {
    final FantastleObjectModel[] objects = this.getAllObjects();
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

  public final BufferedImageIcon[] getAllGroundLayerEditorAppearances() {
    final FantastleObjectModel[] objects = this.getAllObjects();
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

  public final FantastleObjectModel[] getAllObjectLayerObjects() {
    final FantastleObjectModel[] objects = this.getAllObjects();
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

  public final BufferedImageIcon[] getAllObjectLayerEditorAppearances() {
    final FantastleObjectModel[] objects = this.getAllObjects();
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
      return new int[0];
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
      return new FantastleObjectModel[0];
    } else {
      final FantastleObjectModel[] allRequired = new FantastleObjectModel[count];
      for (x = 0; x < count; x++) {
        allRequired[x] = tempAllRequired[x];
      }
      return allRequired;
    }
  }

  public final FantastleObjectModel[]
      getAllWithoutPrerequisiteAndNotRequired(final int layer) {
    final FantastleObjectModel[] objects = this.getAllObjects();
    final FantastleObjectModel[] tempAllWithoutPrereq = new FantastleObjectModel[objects.length];
    int x;
    int count = 0;
    for (x = 0; x < objects.length; x++) {
      if ((objects[x].getLayer() == layer) && !(objects[x].isRequired())) {
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
    int UID = -1;
    if (formatVersion == MazeVersions.FORMAT_LATEST) {
      UID = reader.readInt();
    }
    for (int x = 0; x < objects.length; x++) {
      try {
        FantastleObjectModel instance;
        instance = objects[x].getClass().getConstructor().newInstance();
        if (formatVersion == MazeVersions.FORMAT_LATEST) {
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
      final int UID, final int formatVersion) throws IOException {
    final FantastleObjectModel[] objects = this.getAllObjects();
    FantastleObjectModel o = null;
    for (int x = 0; x < objects.length; x++) {
      try {
        FantastleObjectModel instance;
        instance = objects[x].getClass().getConstructor().newInstance();
        if (formatVersion == MazeVersions.FORMAT_LATEST) {
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
