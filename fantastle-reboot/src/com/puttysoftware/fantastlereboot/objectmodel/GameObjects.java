/*  FantastleReboot: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.objectmodel;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.assets.SoundIndex;
import com.puttysoftware.fantastlereboot.files.versions.WorldVersions;
import com.puttysoftware.fantastlereboot.loaders.DataLoader;
import com.puttysoftware.fantastlereboot.objects.ArmorShop;
import com.puttysoftware.fantastlereboot.objects.BankShop;
import com.puttysoftware.fantastlereboot.objects.BonusShop;
import com.puttysoftware.fantastlereboot.objects.ClosedDoor;
import com.puttysoftware.fantastlereboot.objects.ElementalShop;
import com.puttysoftware.fantastlereboot.objects.HealShop;
import com.puttysoftware.fantastlereboot.objects.Ice;
import com.puttysoftware.fantastlereboot.objects.ItemShop;
import com.puttysoftware.fantastlereboot.objects.NecklaceShop;
import com.puttysoftware.fantastlereboot.objects.Nothing;
import com.puttysoftware.fantastlereboot.objects.OpenDoor;
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
  private static int[] allSelfReplacementActionList;
  private static int[] allShopActionList;
  private static int[] allSoundActionList;
  private static final SoundIndex[] SOUND_MAP = new SoundIndex[] {
      SoundIndex.ACTION_FAILED, SoundIndex.ALERT, SoundIndex.ARROW_DIE,
      SoundIndex.ARROW_HIT, SoundIndex.ARROW_SHOOT, SoundIndex.AXE_CRIT,
      SoundIndex.AXE_HIT, SoundIndex.BARRIER, SoundIndex.BITE,
      SoundIndex.BLOCKED, SoundIndex.BOLT, SoundIndex.BOSS_DIE,
      SoundIndex.BREAK, SoundIndex.BUBBLE, SoundIndex.BUFF_1, SoundIndex.BUFF_2,
      SoundIndex.BUFF_3, SoundIndex.BUFF_4, SoundIndex.BUFF_5,
      SoundIndex.BUTTON, SoundIndex.CHANGE, SoundIndex.CLAW, SoundIndex.CLICK,
      SoundIndex.CLUB_CRIT, SoundIndex.CLUB_HIT, SoundIndex.CONFUSED,
      SoundIndex.COOL_OFF, SoundIndex.CRACK, SoundIndex.CREATE,
      SoundIndex.CRITICAL, SoundIndex.CRUSH, SoundIndex.DARK_STONE,
      SoundIndex.DARKNESS, SoundIndex.DEATH, SoundIndex.DEBUFF_1,
      SoundIndex.DEBUFF_2, SoundIndex.DEBUFF_3, SoundIndex.DEBUFF_4,
      SoundIndex.DEBUFF_5, SoundIndex.DESTROY, SoundIndex.DISPEL,
      SoundIndex.DIZZY, SoundIndex.DOOR_CLOSES, SoundIndex.DOOR_OPENS,
      SoundIndex.DOWN, SoundIndex.DRAIN, SoundIndex.DRAW_SWORD,
      SoundIndex.DRUNK, SoundIndex.EASIER, SoundIndex.EQUIP,
      SoundIndex.ERA_CHANGE, SoundIndex.ERAS_ENABLED, SoundIndex.ERROR,
      SoundIndex.EXPLODE, SoundIndex.FAILED, SoundIndex.FALLING,
      SoundIndex.FATAL, SoundIndex.FINISH, SoundIndex.FIREBALL,
      SoundIndex.FOCUS, SoundIndex.FREEZE, SoundIndex.FUMBLE,
      SoundIndex.GAME_OVER, SoundIndex.GENERATE, SoundIndex.GET_READY,
      SoundIndex.GRAB, SoundIndex.HAMMER_CRIT, SoundIndex.HAMMER_HIT,
      SoundIndex.HARDER, SoundIndex.HEAL, SoundIndex.HIGH_SCORE,
      SoundIndex.HURT, SoundIndex.IDENTIFY, SoundIndex.INTO,
      SoundIndex.INTO_PIT, SoundIndex.KABOOM, SoundIndex.KICK, SoundIndex.LASER,
      SoundIndex.LEVEL_UP, SoundIndex.LIGHT, SoundIndex.LIGHT_FUSE,
      SoundIndex.LIGHT_STONE, SoundIndex.LOGO, SoundIndex.LOSE,
      SoundIndex.LOW_HEALTH, SoundIndex.MACE_CRIT, SoundIndex.MACE_HIT,
      SoundIndex.MAGNET, SoundIndex.MELT, SoundIndex.MISSED, SoundIndex.MISSILE,
      SoundIndex.MONSTER_ACTION, SoundIndex.MONSTER_COUNTER,
      SoundIndex.MONSTER_HIT, SoundIndex.MONSTER_SPELL, SoundIndex.MOON_STONE,
      SoundIndex.NEXT_ROUND, SoundIndex.ON_WHO, SoundIndex.OOF, SoundIndex.OUT,
      SoundIndex.OW, SoundIndex.PARALYSIS, SoundIndex.PARTY_ACTION,
      SoundIndex.PARTY_COUNTER, SoundIndex.PARTY_DEAD, SoundIndex.PARTY_HIT,
      SoundIndex.PARTY_SPELL, SoundIndex.PICK_LOCK, SoundIndex.PLAYER_UP,
      SoundIndex.POP, SoundIndex.PULL, SoundIndex.PUNCH, SoundIndex.PUSH,
      SoundIndex.PUSH_PULL_FAILED, SoundIndex.RETURN, SoundIndex.ROTATED,
      SoundIndex.RUN, SoundIndex.SHOCKED, SoundIndex.SHOP, SoundIndex.SINK,
      SoundIndex.SLICE, SoundIndex.SLIDE, SoundIndex.SLIME, SoundIndex.SONAR,
      SoundIndex.SPARK, SoundIndex.SPEAR_CRIT, SoundIndex.SPEAR_HIT,
      SoundIndex.SPECIAL, SoundIndex.SPINNER, SoundIndex.SPRING,
      SoundIndex.STAR_STONE, SoundIndex.SUMMON, SoundIndex.SUN_STONE,
      SoundIndex.SWORD_CRIT, SoundIndex.SWORD_HIT, SoundIndex.TELEPORT,
      SoundIndex.TRANSACT, SoundIndex.TRAP, SoundIndex.U_TURNED,
      SoundIndex.UNLOCK, SoundIndex.UP, SoundIndex.USE_FAILED,
      SoundIndex.VICTORY, SoundIndex.WALK, SoundIndex.WALK_2, SoundIndex.WALK_3,
      SoundIndex.WALK_4, SoundIndex.WALK_5, SoundIndex.WALK_6,
      SoundIndex.WALK_FAILED, SoundIndex.WALK_ICE, SoundIndex.WALK_LAVA,
      SoundIndex.WALK_SLIME, SoundIndex.WALK_WATER, SoundIndex.WAND,
      SoundIndex.WEAKNESS, SoundIndex.WEAPON_TOO_WEAK, SoundIndex.WEAR_OFF,
      SoundIndex.WIN_GAME, SoundIndex.WRONG, SoundIndex.ZAP,
      SoundIndex.HAUNTED };

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
        new SuperSpring(), new ClosedDoor(), new OpenDoor() };
    GameObjects.allObjectList.clear();
    // Populate lists
    for (final FantastleObjectModel allObject : allObjects) {
      GameObjects.allObjectList.add(allObject);
    }
    GameObjects.allActionList = DataLoader.loadObjectActionData();
    GameObjects.allShopActionList = DataLoader
        .loadObjectActionAddonData(FantastleObjectActions.SHOP);
    GameObjects.allSelfReplacementActionList = DataLoader
        .loadObjectActionAddonData(FantastleObjectActions.REPLACE_SELF);
    GameObjects.allSoundActionList = DataLoader
        .loadObjectActionAddonData(FantastleObjectActions.SOUND);
  }

  public static boolean replacesSelf(final FantastleObjectModel obj) {
    return GameObjects.allActionList[obj.getUniqueID()]
        .get(FantastleObjectActions.REPLACE_SELF);
  }

  public static FantastleObjectModel replacesSelfWith(
      final FantastleObjectModel obj) {
    return GameObjects.getNewInstanceByUniqueID(
        GameObjects.allSelfReplacementActionList[obj.getUniqueID()]);
  }

  public static boolean playsSound(final FantastleObjectModel obj) {
    return GameObjects.allActionList[obj.getUniqueID()]
        .get(FantastleObjectActions.SOUND);
  }

  public static SoundIndex whichSound(final FantastleObjectModel obj) {
    return GameObjects.SOUND_MAP[GameObjects.allSoundActionList[obj
        .getUniqueID()]];
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

  public static FantastleObjectModel[] getAllWithoutPrerequisiteAndNotRequired(
      final int layer) {
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
        FantastleReboot.exception(ex);
        return null;
      }
    }
  }

  public static FantastleObjectModel readObject(final XDataReader reader,
      final int formatVersion) throws IOException {
    final FantastleObjectModel[] objects = GameObjects.getAllObjects();
    FantastleObjectModel o = null;
    int UID = -1;
    if (formatVersion == WorldVersions.LATEST) {
      UID = reader.readInt();
    }
    for (final FantastleObjectModel object : objects) {
      try {
        FantastleObjectModel instance;
        instance = object.getClass().getConstructor().newInstance();
        if (formatVersion == WorldVersions.LATEST) {
          o = instance.readObject(reader, UID);
          if (o != null) {
            return o;
          }
        }
      } catch (InstantiationException | IllegalAccessException
          | IllegalArgumentException | InvocationTargetException
          | NoSuchMethodException | SecurityException ex) {
        FantastleReboot.exception(ex);
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
        if (formatVersion == WorldVersions.LATEST) {
          o = instance.readObject(reader, UID);
          if (o != null) {
            return o;
          }
        }
      } catch (InstantiationException | IllegalAccessException
          | IllegalArgumentException | InvocationTargetException
          | NoSuchMethodException | SecurityException ex) {
        FantastleReboot.exception(ex);
      }
    }
    return null;
  }
}
