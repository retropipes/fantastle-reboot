package com.puttysoftware.fantastlereboot.creatures.jobs;

import com.puttysoftware.diane.gui.CommonDialogs;
import com.puttysoftware.fantastlereboot.spells.SpellBook;
import com.puttysoftware.fantastlereboot.spells.SpellBookManager;
import com.puttysoftware.randomrange.RandomRange;

public class JobManager {
  private static boolean CACHE_CREATED = false;
  private static Job[] CACHE;

  public static Job selectJob() {
    final String[] names = JobConstants.NAMES;
    String dialogResult = null;
    dialogResult = CommonDialogs.showInputDialog("Select a Job", "Select Job",
        names, names[0]);
    if (dialogResult != null) {
      int index;
      for (index = 0; index < names.length; index++) {
        if (dialogResult.equals(names[index])) {
          break;
        }
      }
      return JobManager.getJob(index);
    } else {
      return null;
    }
  }

  public static SpellBook getSpellBookByID(final int ID) {
    return SpellBookManager.getSpellBookByID(ID);
  }

  public static Job getJob(final int jobID) {
    JobManager.initCachesIfNeeded();
    return JobManager.CACHE[jobID];
  }

  public static Job getRandomJob() {
    JobManager.initCachesIfNeeded();
    final int jobID = new RandomRange(0, JobManager.CACHE.length - 1)
        .generate();
    return JobManager.CACHE[jobID];
  }

  private static void initCachesIfNeeded() {
    if (!JobManager.CACHE_CREATED) {
      // Create cache
      JobManager.CACHE = new Job[JobConstants.COUNT];
      for (int x = 0; x < JobConstants.COUNT; x++) {
        JobManager.CACHE[x] = new Job(x);
      }
      JobManager.CACHE_CREATED = true;
    }
  }
}
