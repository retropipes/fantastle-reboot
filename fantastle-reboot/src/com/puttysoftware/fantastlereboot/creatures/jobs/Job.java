package com.puttysoftware.fantastlereboot.creatures.jobs;

import com.puttysoftware.fantastlereboot.loaders.DataLoader;

public class Job {
  private final int[] data;
  private final int jobID;

  Job(final int jid) {
    this.data = DataLoader.loadJobData(jid);
    this.jobID = jid;
  }

  public int getAttribute(final int aid) {
    return this.data[aid];
  }

  public String getName() {
    return JobConstants.JOB_NAMES[this.jobID];
  }

  public int getJobID() {
    return this.jobID;
  }
}
