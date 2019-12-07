/*  FantastleReboot: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.files;

public class FileExtensions {
  // Constants
  private static final String WORLD_EXTENSION = "world.zip";
  private static final String GAME_EXTENSION = "game.zip";
  private static final String WORLD_TEMP_EXTENSION = "world";
  private static final String CHARACTER_EXTENSION = "partymember.xml";
  private static final String REGISTRY_EXTENSION = "registry.xml";
  private static final String PREFS_EXTENSION = "prefs.xml";
  private static final String INTERNAL_DATA_EXTENSION = "txt";

  // Methods
  public static String getWorldExtension() {
    return FileExtensions.WORLD_EXTENSION;
  }

  public static String getWorldExtensionWithPeriod() {
    return "." + FileExtensions.WORLD_EXTENSION;
  }

  public static String getWorldTempExtensionWithPeriod() {
    return "." + FileExtensions.WORLD_TEMP_EXTENSION;
  }

  public static String getGameExtension() {
    return FileExtensions.GAME_EXTENSION;
  }

  public static String getGameExtensionWithPeriod() {
    return "." + FileExtensions.GAME_EXTENSION;
  }

  public static String getCharacterExtension() {
    return FileExtensions.CHARACTER_EXTENSION;
  }

  public static String getPrefsExtensionWithPeriod() {
    return "." + FileExtensions.PREFS_EXTENSION;
  }

  public static String getCharacterExtensionWithPeriod() {
    return "." + FileExtensions.CHARACTER_EXTENSION;
  }

  public static String getRegistryExtensionWithPeriod() {
    return "." + FileExtensions.REGISTRY_EXTENSION;
  }

  public static String getInternalDataExtensionWithPeriod() {
    return "." + FileExtensions.INTERNAL_DATA_EXTENSION;
  }
}
