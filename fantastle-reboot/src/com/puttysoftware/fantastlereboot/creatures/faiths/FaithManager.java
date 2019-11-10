package com.puttysoftware.fantastlereboot.creatures.faiths;

import java.awt.Color;
import java.util.Hashtable;

import com.puttysoftware.diane.loaders.ColorShader;
import com.puttysoftware.fantastlereboot.Messager;
import com.puttysoftware.fantastlereboot.loaders.DataLoader;
import com.puttysoftware.randomrange.RandomRange;

public class FaithManager {
  public static final int FAITHS = 13;
  private static boolean CACHES_INITED = false;
  private static Faith[] CACHE;

  public static Faith selectFaith() {
    FaithManager.initCachesIfNeeded();
    final String[] names = FaithConstants.FAITH_NAME_CACHE;
    String dialogResult = null;
    dialogResult = Messager.showInputDialog("Select a Faith", "Select Faith",
        names, names[0]);
    if (dialogResult != null) {
      int index;
      for (index = 0; index < names.length; index++) {
        if (dialogResult.equals(names[index])) {
          break;
        }
      }
      return FaithManager.getFaith(index);
    } else {
      return null;
    }
  }

  public static Faith getFaith(final int faithID) {
    FaithManager.initCachesIfNeeded();
    return FaithManager.CACHE[faithID];
  }

  public static Faith getRandomFaith() {
    FaithManager.initCachesIfNeeded();
    final int faithID = new RandomRange(0, FaithManager.CACHE.length - 1)
        .generate();
    return FaithManager.CACHE[faithID];
  }

  public static String getFaithName(final int faithID) {
    FaithManager.initCachesIfNeeded();
    return FaithConstants.FAITH_NAME_CACHE[faithID];
  }

  public static int getFaithAdjustedDamage(final int attackerFaithID,
      final int defenderFaithID, final int rawDamage) {
    FaithManager.initCachesIfNeeded();
    return rawDamage
        * FaithConstants.FAITH_NUMERATOR_CACHE[attackerFaithID][defenderFaithID]
        / FaithConstants.FAITH_DENOMINATOR_CACHE[attackerFaithID][defenderFaithID];
  }

  public static Color getFaithColor(final int faithID) {
    FaithManager.initCachesIfNeeded();
    return FaithConstants.FAITH_COLOR_CACHE.get(faithID);
  }

  public static String getFaithPowerName(final int faithID, final int powerID) {
    FaithManager.initCachesIfNeeded();
    return FaithManager.getFaithName(faithID);
  }

  public static ColorShader getFaithShader(final int faithID) {
    FaithManager.initCachesIfNeeded();
    return FaithConstants.FAITH_SHADER_CACHE.get(faithID);
  }

  private static void initCachesIfNeeded() {
    if (!FaithManager.CACHES_INITED) {
      FaithConstants.FAITH_NAME_CACHE = DataLoader.loadFaithNameData();
      FaithConstants.FAITH_NUMERATOR_CACHE = new int[FAITHS][FAITHS];
      FaithConstants.FAITH_DENOMINATOR_CACHE = new int[FAITHS][FAITHS];
      FaithConstants.FAITH_COLOR_CACHE = new Hashtable<>();
      FaithConstants.FAITH_SHADER_CACHE = new Hashtable<>();
      FaithManager.CACHE = new Faith[FaithManager.FAITHS];
      for (int faithID = 0; faithID < FAITHS; faithID++) {
        int[] colorData = DataLoader.loadFaithColorData(faithID);
        Color faithColor = new Color(colorData[0], colorData[1], colorData[2],
            colorData[3]);
        FaithConstants.FAITH_COLOR_CACHE.put(faithID, faithColor);
        FaithConstants.FAITH_NUMERATOR_CACHE[faithID] = DataLoader
            .loadFaithNumeratorData(faithID);
        FaithConstants.FAITH_DENOMINATOR_CACHE[faithID] = DataLoader
            .loadFaithDenominatorData(faithID);
        String faithName = getFaithName(faithID);
        ColorShader faithShader = new ColorShader(faithName, faithColor);
        FaithConstants.FAITH_SHADER_CACHE.put(faithID, faithShader);
        FaithManager.CACHE[faithID] = new Faith(faithID);
      }
      FaithManager.CACHES_INITED = true;
    }
  }
}
