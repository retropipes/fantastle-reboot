package com.puttysoftware.fantastlereboot.gui;

import java.awt.desktop.PreferencesEvent;
import java.awt.desktop.PreferencesHandler;

public class PrefsLauncher implements PreferencesHandler {
  public PrefsLauncher() {
    super();
  }

  @Override
  public void handlePreferences(final PreferencesEvent inE) {
    Prefs.showPrefs();
  }
}
