/*  FantastleReboot: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.files;

import java.io.File;
import java.io.IOException;

import com.puttysoftware.diane.gui.CommonDialogs;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.creatures.party.PartyMember;
import com.puttysoftware.xio.XDataWriter;

public class CharacterSaver {
  public static void saveCharacter(final PartyMember character) {
    final String basePath = CharacterRegistration.getBasePath();
    final String name = character.getName();
    final String characterFile = basePath + name
        + FileExtensions.getCharacterExtensionWithPeriod();
    try (XDataWriter writer = new XDataWriter(characterFile, "character")) {
      CharacterSaver.writeCharacter(writer, character);
    } catch (final IOException e) {
      FantastleReboot.logError(e);
    }
  }

  public static void writeCharacter(final XDataWriter writer,
      final PartyMember character) throws IOException {
    writer.writeByte(CharacterVersions.FORMAT_LATEST);
    writer.writeInt(character.getKills());
    writer.writeInt(character.getPermanentAttackPoints());
    writer.writeInt(character.getPermanentDefensePoints());
    writer.writeInt(character.getPermanentHPPoints());
    writer.writeInt(character.getPermanentMPPoints());
    writer.writeInt(character.getStrength());
    writer.writeInt(character.getBlock());
    writer.writeInt(character.getAgility());
    writer.writeInt(character.getVitality());
    writer.writeInt(character.getIntelligence());
    writer.writeInt(character.getLuck());
    writer.writeInt(character.getLevel());
    writer.writeInt(character.getCurrentHP());
    writer.writeInt(character.getCurrentMP());
    writer.writeInt(character.getGold());
    writer.writeInt(character.getAttacksPerRound());
    writer.writeInt(character.getSpellsPerRound());
    writer.writeInt(character.getLoad());
    writer.writeLong(character.getExperience());
    writer.writeInt(character.getRace().getRaceID());
    writer.writeInt(character.getJob().getJobID());
    writer.writeInt(character.getFaith().getFaithID());
    final int max = character.getSpellBook().getSpellCount();
    writer.writeInt(max);
    for (int x = 0; x < max; x++) {
      writer.writeBoolean(character.getSpellBook().isSpellKnown(x));
    }
    writer.writeString(character.getName());
    writer.writeInt(character.getAvatarFamilyID());
    writer.writeInt(character.getAvatarSkinID());
    writer.writeInt(character.getAvatarHairID());
    character.getItems().writeItemInventory(writer);
  }

  static void deleteCharacter(final String name, final boolean showResults) {
    final String basePath = CharacterRegistration.getBasePath();
    final String characterFile = basePath + name
        + FileExtensions.getCharacterExtensionWithPeriod();
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
