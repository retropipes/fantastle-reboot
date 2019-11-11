/*  FantastleReboot: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.creatures.characterfiles;

import java.io.File;
import java.io.IOException;

import com.puttysoftware.commondialogs.CommonDialogs;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.creatures.party.PartyMember;
import com.puttysoftware.fantastlereboot.gui.VersionException;
import com.puttysoftware.fantastlereboot.maze.Extension;
import com.puttysoftware.xio.UnexpectedTagException;
import com.puttysoftware.xio.XDataReader;
import com.puttysoftware.xio.XDataWriter;

public class CharacterLoader {
  private static PartyMember loadCharacter(final String name) {
    final String basePath = CharacterRegistration.getBasePath();
    final String loadPath = basePath + File.separator + name
        + Extension.getCharacterExtensionWithPeriod();
    try (XDataReader loader = new XDataReader(loadPath, "character")) {
      return PartyMember.read(loader);
    } catch (VersionException | UnexpectedTagException e) {
      CharacterRegistration.autoremoveCharacter(name);
      return null;
    } catch (final IOException e) {
      FantastleReboot.logError(e);
      return null;
    }
  }

  public static PartyMember[] loadAllRegisteredCharacters() {
    final String[] registeredNames = CharacterRegistration
        .getCharacterNameList();
    if (registeredNames != null) {
      final PartyMember[] res = new PartyMember[registeredNames.length];
      // Load characters
      for (int x = 0; x < registeredNames.length; x++) {
        final String name = registeredNames[x];
        final PartyMember characterWithName = CharacterLoader
            .loadCharacter(name);
        if (characterWithName != null) {
          res[x] = characterWithName;
        } else {
          // Auto-removed character
          return CharacterLoader.loadAllRegisteredCharacters();
        }
      }
      return res;
    }
    return null;
  }

  public static void saveCharacter(final PartyMember character) {
    final String basePath = CharacterRegistration.getBasePath();
    final String name = character.getName();
    final String characterFile = basePath + File.separator + name
        + Extension.getCharacterExtensionWithPeriod();
    try (XDataWriter saver = new XDataWriter(characterFile, "character")) {
      character.write(saver);
    } catch (final IOException e) {
      FantastleReboot.logError(e);
    }
  }

  static void deleteCharacter(final String name, final boolean showResults) {
    final String basePath = CharacterRegistration.getBasePath();
    final String characterFile = basePath + File.separator + name
        + Extension.getCharacterExtensionWithPeriod();
    final File toDelete = new File(characterFile);
    if (toDelete.exists()) {
      final boolean success = toDelete.delete();
      if (success) {
        if (showResults) {
          CommonDialogs.showDialog("Character removed.");
        } else {
          CommonDialogs.showDialog(
              "Character " + name + " autoremoved due to version change.");
        }
      } else {
        if (showResults) {
          CommonDialogs.showDialog("Character removal failed!");
        } else {
          CommonDialogs
              .showDialog("Character " + name + " failed to autoremove!");
        }
      }
    } else {
      if (showResults) {
        CommonDialogs.showDialog(
            "The character to be removed does not have a corresponding file.");
      } else {
        CommonDialogs.showDialog(
            "The character to be autoremoved does not have a corresponding file.");
      }
    }
  }
}
