package com.puttysoftware.fantastlereboot.gui;

import java.awt.desktop.PreferencesEvent;
import java.awt.desktop.PreferencesHandler;

public class PreferencesLauncher implements PreferencesHandler {
  public PreferencesLauncher() {
    super();
  }

  @Override
  public void handlePreferences(PreferencesEvent inE) {
    PreferencesManager.showPrefs();
  }
}
