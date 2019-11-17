/*  FantastleReboot: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.files;

public class FileExtensions {
  // Constants
  private static final String GAME_EXTENSION = "frgame";
  private static final String CHARACTER_EXTENSION = "frchar";
  private static final String REGISTRY_EXTENSION = "frregi";
  private static final String PREFERENCES_EXTENSION = "frpref";
  private static final String SCORES_EXTENSION = "scores";
  private static final String INTERNAL_DATA_EXTENSION = "txt";

  // Methods
  public static String getGameExtension() {
    return FileExtensions.GAME_EXTENSION;
  }

  public static String getGameExtensionWithPeriod() {
    return "." + FileExtensions.GAME_EXTENSION;
  }

  public static String getCharacterExtension() {
    return FileExtensions.CHARACTER_EXTENSION;
  }

  public static String getPreferencesExtension() {
    return FileExtensions.PREFERENCES_EXTENSION;
  }

  public static String getCharacterExtensionWithPeriod() {
    return "." + FileExtensions.CHARACTER_EXTENSION;
  }

  public static String getScoresExtensionWithPeriod() {
    return "." + FileExtensions.SCORES_EXTENSION;
  }

  public static String getRegistryExtensionWithPeriod() {
    return "." + FileExtensions.REGISTRY_EXTENSION;
  }

  public static String getInternalDataExtensionWithPeriod() {
    return "." + FileExtensions.INTERNAL_DATA_EXTENSION;
  }
}
