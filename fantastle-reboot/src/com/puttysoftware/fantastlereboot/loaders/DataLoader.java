package com.puttysoftware.fantastlereboot.loaders;

import java.io.IOException;
import java.util.ArrayList;

import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.creatures.castes.CasteConstants;
import com.puttysoftware.fantastlereboot.creatures.faiths.FaithManager;
import com.puttysoftware.fantastlereboot.creatures.genders.GenderConstants;
import com.puttysoftware.fantastlereboot.creatures.personalities.PersonalityConstants;
import com.puttysoftware.fantastlereboot.creatures.races.RaceConstants;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObjectActions;
import com.puttysoftware.fileutils.ResourceStreamReader;

public class DataLoader {
  private DataLoader() {
    // Do nothing
  }

  public static int[] loadCasteData(final int c) {
    final String name = Integer.toString(c);
    try (final ResourceStreamReader rsr = new ResourceStreamReader(
        DataLoader.class
            .getResourceAsStream("/assets/data/caste/" + name + ".txt"))) {
      // Fetch data
      final int[] rawData = new int[CasteConstants.CASTES_ATTRIBUTE_COUNT];
      for (int x = 0; x < rawData.length; x++) {
        try {
          rawData[x] = rsr.readInt();
        } catch (final NumberFormatException nfe) {
          rawData[x] = 0;
        }
      }
      return rawData;
    } catch (final IOException e) {
      FantastleReboot.logError(e);
      return null;
    }
  }

  public static String[] loadFaithNameData() {
    try (final ResourceStreamReader rsr = new ResourceStreamReader(
        DataLoader.class.getResourceAsStream("/assets/data/faith/names.txt"))) {
      // Fetch data
      final String[] data = new String[FaithManager.FAITHS];
      for (int x = 0; x < data.length; x++) {
        data[x] = rsr.readString();
      }
      return data;
    } catch (final IOException e) {
      FantastleReboot.logError(e);
      return null;
    }
  }

  public static int[] loadFaithColorData(final int f) {
    final String name = Integer.toString(f);
    try (final ResourceStreamReader rsr = new ResourceStreamReader(
        DataLoader.class.getResourceAsStream(
            "/assets/data/faith/colors/" + name + ".txt"))) {
      // Fetch data
      final int[] data = new int[4];
      for (int x = 0; x < data.length; x++) {
        data[x] = rsr.readInt();
      }
      return data;
    } catch (final IOException e) {
      FantastleReboot.logError(e);
      return null;
    }
  }

  public static int[] loadFaithNumeratorData(final int f) {
    final String name = Integer.toString(f);
    try (final ResourceStreamReader rsr = new ResourceStreamReader(
        DataLoader.class.getResourceAsStream(
            "/assets/data/faith/numerator/" + name + ".txt"))) {
      // Fetch data
      final int[] data = new int[FaithManager.FAITHS];
      for (int x = 0; x < data.length; x++) {
        data[x] = rsr.readInt();
      }
      return data;
    } catch (final IOException e) {
      FantastleReboot.logError(e);
      return null;
    }
  }

  public static int[] loadFaithDenominatorData(final int f) {
    final String name = Integer.toString(f);
    try (final ResourceStreamReader rsr = new ResourceStreamReader(
        DataLoader.class.getResourceAsStream(
            "/assets/data/faith/denominator/" + name + ".txt"))) {
      // Fetch data
      final int[] data = new int[FaithManager.FAITHS];
      for (int x = 0; x < data.length; x++) {
        data[x] = rsr.readInt();
      }
      return data;
    } catch (final IOException e) {
      FantastleReboot.logError(e);
      return null;
    }
  }

  public static int[] loadGenderData(final int g) {
    final String name = Integer.toString(g);
    try (final ResourceStreamReader rsr = new ResourceStreamReader(
        DataLoader.class
            .getResourceAsStream("/assets/data/gender/" + name + ".txt"))) {
      // Fetch data
      final int[] rawData = new int[GenderConstants.GENDERS_ATTRIBUTE_COUNT];
      for (int x = 0; x < rawData.length; x++) {
        try {
          rawData[x] = rsr.readInt();
        } catch (final NumberFormatException nfe) {
          rawData[x] = 0;
        }
      }
      return rawData;
    } catch (final IOException e) {
      FantastleReboot.logError(e);
      return null;
    }
  }

  public static String[] loadMonsterData() {
    try (final ResourceStreamReader rsr = new ResourceStreamReader(
        DataLoader.class
            .getResourceAsStream("/assets/data/monster/monsternames.txt"))) {
      // Fetch data
      final ArrayList<String> data = new ArrayList<>();
      String raw = "0";
      while (raw != null) {
        raw = rsr.readString();
        data.add(raw);
      }
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
    final String name = Integer.toString(p);
    try (final ResourceStreamReader rsr = new ResourceStreamReader(
        DataLoader.class.getResourceAsStream(
            "/assets/data/personality/" + name + ".txt"))) {
      // Fetch data
      final int[] rawData = new int[PersonalityConstants.PERSONALITY_ATTRIBUTES_COUNT];
      for (int x = 0; x < rawData.length; x++) {
        try {
          rawData[x] = rsr.readInt();
        } catch (final NumberFormatException nfe) {
          rawData[x] = 0;
        }
      }
      return rawData;
    } catch (final IOException e) {
      FantastleReboot.logError(e);
      return null;
    }
  }

  public static int[] loadRaceData(final int r) {
    final String name = Integer.toString(r);
    try (final ResourceStreamReader rsr = new ResourceStreamReader(
        DataLoader.class
            .getResourceAsStream("/assets/data/race/" + name + ".txt"))) {
      // Fetch data
      final int[] rawData = new int[RaceConstants.RACE_ATTRIBUTE_COUNT];
      for (int x = 0; x < rawData.length; x++) {
        try {
          rawData[x] = rsr.readInt();
        } catch (final NumberFormatException nfe) {
          rawData[x] = 0;
        }
      }
      return rawData;
    } catch (final IOException e) {
      FantastleReboot.logError(e);
      return null;
    }
  }

  public static String[] loadMusicData() {
    try (final ResourceStreamReader rsr = new ResourceStreamReader(
        DataLoader.class.getResourceAsStream("/assets/data/music/files.txt"))) {
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

  public static FantastleObjectActions[] loadObjectActionData() {
    try (final ResourceStreamReader rsr = new ResourceStreamReader(
        DataLoader.class
            .getResourceAsStream("/assets/data/objects/actions.txt"))) {
      // Fetch data
      final ArrayList<FantastleObjectActions> data = new ArrayList<>();
      String raw = "0";
      while (raw != null) {
        raw = rsr.readString();
        if (raw != null) {
          data.add(new FantastleObjectActions(Long.parseLong(raw)));
        }
      }
      return data.toArray(new FantastleObjectActions[data.size()]);
    } catch (final IOException e) {
      FantastleReboot.logError(e);
      return null;
    }
  }

  public static int[] loadObjectActionAddonData(int actionID) {
    final String name = "action-" + Integer.toString(actionID);
    try (final ResourceStreamReader rsr = new ResourceStreamReader(
        DataLoader.class
            .getResourceAsStream("/assets/data/objects/" + name + ".txt"))) {
      // Fetch data
      final ArrayList<Integer> rawData = new ArrayList<>();
      String raw = "0";
      while (raw != null) {
        raw = rsr.readString();
        if (raw != null) {
          rawData.add(Integer.parseInt(raw));
        }
      }
      int index = 0;
      int[] data = new int[rawData.size()];
      for (Integer rawItem : rawData) {
        data[index] = rawItem.intValue();
        index++;
      }
      return data;
    } catch (final IOException e) {
      FantastleReboot.logWarningWithMessage(e,
          "Action ID " + actionID + " has no addon data file.");
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
