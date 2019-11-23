package com.puttysoftware.fantastlereboot.loaders;

import java.io.IOException;
import java.util.ArrayList;

import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.creatures.faiths.FaithManager;
import com.puttysoftware.fantastlereboot.creatures.jobs.JobConstants;
import com.puttysoftware.fantastlereboot.creatures.races.RaceConstants;
import com.puttysoftware.fantastlereboot.files.FileExtensions;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObjectActions;
import com.puttysoftware.fileutils.ResourceStreamReader;

public class DataLoader {
  private DataLoader() {
    // Do nothing
  }

  public static int[] loadJobData(final int j) {
    final String name = Integer.toString(j);
    try (final ResourceStreamReader rsr = new ResourceStreamReader(
        DataLoader.class.getResourceAsStream("/assets/data/job/" + name
            + FileExtensions.getInternalDataExtensionWithPeriod()))) {
      // Fetch data
      final int[] rawData = new int[JobConstants.ATTRIBUTES_COUNT];
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
        DataLoader.class.getResourceAsStream("/assets/data/faith/names"
            + FileExtensions.getInternalDataExtensionWithPeriod()))) {
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
        DataLoader.class.getResourceAsStream("/assets/data/faith/colors/" + name
            + FileExtensions.getInternalDataExtensionWithPeriod()))) {
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
        DataLoader.class.getResourceAsStream("/assets/data/faith/numerator/"
            + name + FileExtensions.getInternalDataExtensionWithPeriod()))) {
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
        DataLoader.class.getResourceAsStream("/assets/data/faith/denominator/"
            + name + FileExtensions.getInternalDataExtensionWithPeriod()))) {
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

  public static String[] loadGivenNameData() {
    try (final ResourceStreamReader rsr = new ResourceStreamReader(
        DataLoader.class.getResourceAsStream("/assets/data/names/given"
            + FileExtensions.getInternalDataExtensionWithPeriod()))) {
      // Fetch data
      final ArrayList<String> data = new ArrayList<>();
      String raw = "0";
      while (raw != null) {
        raw = rsr.readString();
        data.add(raw);
      }
      return data.toArray(new String[data.size()]);
    } catch (final IOException e) {
      FantastleReboot.logError(e);
      return null;
    }
  }

  public static String[] loadFamilyNameData() {
    try (final ResourceStreamReader rsr = new ResourceStreamReader(
        DataLoader.class.getResourceAsStream("/assets/data/names/family"
            + FileExtensions.getInternalDataExtensionWithPeriod()))) {
      // Fetch data
      final ArrayList<String> data = new ArrayList<>();
      String raw = "0";
      while (raw != null) {
        raw = rsr.readString();
        data.add(raw);
      }
      return data.toArray(new String[data.size()]);
    } catch (final IOException e) {
      FantastleReboot.logError(e);
      return null;
    }
  }

  public static String[] loadMonsterData() {
    try (final ResourceStreamReader rsr = new ResourceStreamReader(
        DataLoader.class.getResourceAsStream("/assets/data/monster/monsternames"
            + FileExtensions.getInternalDataExtensionWithPeriod()))) {
      // Fetch data
      final ArrayList<String> data = new ArrayList<>();
      String raw = "0";
      while (raw != null) {
        raw = rsr.readString();
        data.add(raw);
      }
      return data.toArray(new String[data.size()]);
    } catch (final IOException e) {
      FantastleReboot.logError(e);
      return null;
    }
  }

  public static int[] loadRaceData(final int r) {
    final String name = Integer.toString(r);
    try (final ResourceStreamReader rsr = new ResourceStreamReader(
        DataLoader.class.getResourceAsStream("/assets/data/race/" + name
            + FileExtensions.getInternalDataExtensionWithPeriod()))) {
      // Fetch data
      final int[] rawData = new int[RaceConstants.ATTRIBUTES_COUNT];
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
        DataLoader.class.getResourceAsStream("/assets/data/music/files"
            + FileExtensions.getInternalDataExtensionWithPeriod()))) {
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
        DataLoader.class.getResourceAsStream("/assets/data/sounds/files"
            + FileExtensions.getInternalDataExtensionWithPeriod()))) {
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
        DataLoader.class.getResourceAsStream("/assets/data/objects/actions"
            + FileExtensions.getInternalDataExtensionWithPeriod()))) {
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
        DataLoader.class.getResourceAsStream("/assets/data/objects/" + name
            + FileExtensions.getInternalDataExtensionWithPeriod()))) {
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
        DataLoader.class.getResourceAsStream("/assets/data/images/attributes"
            + FileExtensions.getInternalDataExtensionWithPeriod()))) {
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
        DataLoader.class.getResourceAsStream("/assets/data/images/boss"
            + FileExtensions.getInternalDataExtensionWithPeriod()))) {
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
        DataLoader.class.getResourceAsStream("/assets/data/images/effects"
            + FileExtensions.getInternalDataExtensionWithPeriod()))) {
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
        DataLoader.class.getResourceAsStream("/assets/data/images/items"
            + FileExtensions.getInternalDataExtensionWithPeriod()))) {
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
        DataLoader.class.getResourceAsStream("/assets/data/images/objects"
            + FileExtensions.getInternalDataExtensionWithPeriod()))) {
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
        DataLoader.class.getResourceAsStream("/assets/data/images/ui"
            + FileExtensions.getInternalDataExtensionWithPeriod()))) {
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
