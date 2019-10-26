package com.puttysoftware.fantastlereboot.loaders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.creatures.castes.CasteConstants;
import com.puttysoftware.fantastlereboot.creatures.faiths.FaithConstants;
import com.puttysoftware.fantastlereboot.creatures.genders.GenderConstants;
import com.puttysoftware.fantastlereboot.creatures.personalities.PersonalityConstants;
import com.puttysoftware.fantastlereboot.creatures.races.RaceConstants;
import com.puttysoftware.fileutils.ResourceStreamReader;

public class DataLoader {
  private DataLoader() {
    // Do nothing
  }

  public static int[] loadCasteData(final int c) {
    final String name = CasteConstants.CASTE_NAMES[c].toLowerCase();
    try (final ResourceStreamReader rsr = new ResourceStreamReader(
        DataLoader.class
            .getResourceAsStream("/assets/data/caste/" + name + ".dat"))) {
      // Fetch data
      final int[] rawData = new int[CasteConstants.CASTES_ATTRIBUTE_COUNT];
      for (int x = 0; x < rawData.length; x++) {
        try {
          rawData[x] = rsr.readInt();
        } catch (final NumberFormatException nfe) {
          rawData[x] = 0;
        }
      }
      rsr.close();
      return rawData;
    } catch (final IOException e) {
      FantastleReboot.logError(e);
      return null;
    }
  }

  public static double[] loadFaithData(final int f) {
    final String name = Integer.toString(f);
    try (final ResourceStreamReader rsr = new ResourceStreamReader(
        DataLoader.class
            .getResourceAsStream("/assets/data/faith/" + name + ".dat"))) {
      // Fetch data
      final int[] rawData = new int[FaithConstants.FAITHS_COUNT];
      for (int x = 0; x < rawData.length; x++) {
        try {
          rawData[x] = rsr.readInt();
        } catch (final NumberFormatException nfe) {
          rawData[x] = -3;
        }
      }
      rsr.close();
      // Parse raw data
      final double[] finalData = new double[rawData.length];
      for (int x = 0; x < rawData.length; x++) {
        double d = 0.0;
        final int i = rawData[x];
        if (i == -2) {
          d = 0.5;
        } else if (i == -1) {
          d = 2.0 / 3.0;
        } else if (i == 1) {
          d = 1.5;
        } else if (i == 2) {
          d = 2.0;
        } else if (i == 0) {
          d = 1.0;
        } else {
          d = 0.0;
        }
        finalData[x] = d;
      }
      return finalData;
    } catch (final IOException e) {
      FantastleReboot.logError(e);
      return null;
    }
  }

  public static Hashtable<Integer, String> loadFaithPowerData(final int f,
      final int p) {
    try (final ResourceStreamReader rsr = new ResourceStreamReader(
        DataLoader.class
            .getResourceAsStream("/assets/data/faith/powers.dat"))) {
      // Fetch data
      final ArrayList<String> rawData = new ArrayList<>();
      String line = "";
      while (line != null) {
        line = rsr.readString();
        if (line != null) {
          rawData.add(line);
        }
      }
      // Parse raw data
      final Hashtable<Integer, String> finalData = new Hashtable<>();
      for (String entry : rawData) {
        String[] splitEntry = entry.split(" = ");
        int key = Integer.parseInt(splitEntry[0]);
        String value = splitEntry[1];
        finalData.putIfAbsent(key, value);
      }
      return finalData;
    } catch (final IOException e) {
      FantastleReboot.logError(e);
      return null;
    }
  }

  public static int[] loadGenderData(final int g) {
    final String name = GenderConstants.GENDER_NAMES[g].toLowerCase();
    try (final ResourceStreamReader rsr = new ResourceStreamReader(
        DataLoader.class
            .getResourceAsStream("/assets/data/gender/" + name + ".dat"))) {
      // Fetch data
      final int[] rawData = new int[GenderConstants.GENDERS_ATTRIBUTE_COUNT];
      for (int x = 0; x < rawData.length; x++) {
        try {
          rawData[x] = rsr.readInt();
        } catch (final NumberFormatException nfe) {
          rawData[x] = 0;
        }
      }
      rsr.close();
      return rawData;
    } catch (final IOException e) {
      FantastleReboot.logError(e);
      return null;
    }
  }

  public static String[] loadMonsterData() {
    try (final ResourceStreamReader rsr = new ResourceStreamReader(
        DataLoader.class
            .getResourceAsStream("/assets/data/monster/monsternames.dat"))) {
      // Fetch data
      final ArrayList<String> data = new ArrayList<>();
      String raw = "0";
      while (raw != null) {
        raw = rsr.readString();
        data.add(raw);
      }
      rsr.close();
      final Object[] arr = data.toArray();
      final String[] tempres = new String[arr.length];
      int count = 0;
      for (int x = 0; x < arr.length; x++) {
        if (arr[x] != null) {
          tempres[x] = arr[x].toString();
          count++;
        }
      }
      final String[] res = new String[count];
      count = 0;
      for (final String tempre : tempres) {
        if (tempre != null) {
          res[count] = tempre;
          count++;
        }
      }
      return res;
    } catch (final IOException e) {
      FantastleReboot.logError(e);
      return null;
    }
  }

  public static int[] loadPersonalityData(final int p) {
    final String name = PersonalityConstants.PERSONALITY_NAMES[p].toLowerCase();
    try (final ResourceStreamReader rsr = new ResourceStreamReader(
        DataLoader.class.getResourceAsStream(
            "/assets/data/personality/" + name + ".dat"))) {
      // Fetch data
      final int[] rawData = new int[PersonalityConstants.PERSONALITY_ATTRIBUTES_COUNT];
      for (int x = 0; x < rawData.length; x++) {
        try {
          rawData[x] = rsr.readInt();
        } catch (final NumberFormatException nfe) {
          rawData[x] = 0;
        }
      }
      rsr.close();
      return rawData;
    } catch (final IOException e) {
      FantastleReboot.logError(e);
      return null;
    }
  }

  public static int[] loadRaceData(final int c) {
    final String name = RaceConstants.RACE_NAMES[c].toLowerCase();
    try (final ResourceStreamReader rsr = new ResourceStreamReader(
        DataLoader.class
            .getResourceAsStream("/assets/data/race/" + name + ".dat"))) {
      // Fetch data
      final int[] rawData = new int[RaceConstants.RACE_ATTRIBUTE_COUNT];
      for (int x = 0; x < rawData.length; x++) {
        try {
          rawData[x] = rsr.readInt();
        } catch (final NumberFormatException nfe) {
          rawData[x] = 0;
        }
      }
      rsr.close();
      return rawData;
    } catch (final IOException e) {
      FantastleReboot.logError(e);
      return null;
    }
  }

  public static String[] loadSoundData() {
    try (final ResourceStreamReader rsr = new ResourceStreamReader(
        DataLoader.class
            .getResourceAsStream("/assets/data/sounds/files.txt"))) {
      // Fetch data
      final ArrayList<String> data = new ArrayList<>();
      String raw = "0";
      while (raw != null) {
        raw = rsr.readString();
        if (raw != null) {
          data.add(raw);
        }
      }
      return data.toArray(new String[data.size()]);
    } catch (final IOException e) {
      FantastleReboot.logError(e);
      return null;
    }
  }

  public static String[] loadAttributeImageData() {
    try (final ResourceStreamReader rsr = new ResourceStreamReader(
        DataLoader.class
            .getResourceAsStream("/assets/data/images/attributes.txt"))) {
      // Fetch data
      final ArrayList<String> data = new ArrayList<>();
      String raw = "0";
      while (raw != null) {
        raw = rsr.readString();
        if (raw != null) {
          data.add(raw);
        }
      }
      return data.toArray(new String[data.size()]);
    } catch (final IOException e) {
      FantastleReboot.logError(e);
      return null;
    }
  }

  public static String[] loadAvatarImageData() {
    try (final ResourceStreamReader rsr = new ResourceStreamReader(
        DataLoader.class
            .getResourceAsStream("/assets/data/images/avatars.txt"))) {
      // Fetch data
      final ArrayList<String> data = new ArrayList<>();
      String raw = "0";
      while (raw != null) {
        raw = rsr.readString();
        if (raw != null) {
          data.add(raw);
        }
      }
      return data.toArray(new String[data.size()]);
    } catch (final IOException e) {
      FantastleReboot.logError(e);
      return null;
    }
  }

  public static String[] loadBossImageData() {
    try (final ResourceStreamReader rsr = new ResourceStreamReader(
        DataLoader.class.getResourceAsStream("/assets/data/images/boss.txt"))) {
      // Fetch data
      final ArrayList<String> data = new ArrayList<>();
      String raw = "0";
      while (raw != null) {
        raw = rsr.readString();
        if (raw != null) {
          data.add(raw);
        }
      }
      return data.toArray(new String[data.size()]);
    } catch (final IOException e) {
      FantastleReboot.logError(e);
      return null;
    }
  }

  public static String[] loadEffectImageData() {
    try (final ResourceStreamReader rsr = new ResourceStreamReader(
        DataLoader.class
            .getResourceAsStream("/assets/data/images/effects.txt"))) {
      // Fetch data
      final ArrayList<String> data = new ArrayList<>();
      String raw = "0";
      while (raw != null) {
        raw = rsr.readString();
        if (raw != null) {
          data.add(raw);
        }
      }
      return data.toArray(new String[data.size()]);
    } catch (final IOException e) {
      FantastleReboot.logError(e);
      return null;
    }
  }

  public static String[] loadItemImageData() {
    try (final ResourceStreamReader rsr = new ResourceStreamReader(
        DataLoader.class
            .getResourceAsStream("/assets/data/images/items.txt"))) {
      // Fetch data
      final ArrayList<String> data = new ArrayList<>();
      String raw = "0";
      while (raw != null) {
        raw = rsr.readString();
        if (raw != null) {
          data.add(raw);
        }
      }
      return data.toArray(new String[data.size()]);
    } catch (final IOException e) {
      FantastleReboot.logError(e);
      return null;
    }
  }

  public static String[] loadObjectImageData() {
    try (final ResourceStreamReader rsr = new ResourceStreamReader(
        DataLoader.class
            .getResourceAsStream("/assets/data/images/objects.txt"))) {
      // Fetch data
      final ArrayList<String> data = new ArrayList<>();
      String raw = "0";
      while (raw != null) {
        raw = rsr.readString();
        if (raw != null) {
          data.add(raw);
        }
      }
      return data.toArray(new String[data.size()]);
    } catch (final IOException e) {
      FantastleReboot.logError(e);
      return null;
    }
  }

  public static String[] loadUserInterfaceImageData() {
    try (final ResourceStreamReader rsr = new ResourceStreamReader(
        DataLoader.class.getResourceAsStream("/assets/data/images/ui.txt"))) {
      // Fetch data
      final ArrayList<String> data = new ArrayList<>();
      String raw = "0";
      while (raw != null) {
        raw = rsr.readString();
        if (raw != null) {
          data.add(raw);
        }
      }
      return data.toArray(new String[data.size()]);
    } catch (final IOException e) {
      FantastleReboot.logError(e);
      return null;
    }
  }
}
