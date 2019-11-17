/*  FantastleReboot: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.files;

import java.io.File;
import java.io.IOException;

import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.creatures.party.PartyManager;
import com.puttysoftware.fantastlereboot.creatures.party.PartyMember;
import com.puttysoftware.fantastlereboot.gui.VersionException;
import com.puttysoftware.fantastlereboot.items.ItemInventory;
import com.puttysoftware.xio.UnexpectedTagException;
import com.puttysoftware.xio.XDataReader;

public class CharacterLoader {
  private static PartyMember loadCharacter(final String name) {
    final String basePath = CharacterRegistration.getBasePath();
    final String loadPath = basePath + File.separator + name
        + FileExtensions.getCharacterExtensionWithPeriod();
    try (XDataReader loader = new XDataReader(loadPath, "character")) {
      return CharacterLoader.readCharacter(loader);
    } catch (VersionException | UnexpectedTagException e) {
      CharacterRegistration.autoremoveCharacter(name);
      return null;
    } catch (final IOException e) {
      FantastleReboot.logError(e);
      return null;
    }
  }

  public static PartyMember readCharacter(final XDataReader reader)
      throws IOException {
    final int version = reader.readByte();
    if (version < CharacterVersions.FORMAT_5) {
      throw new VersionException("Invalid character version found: " + version);
    }
    final int k = reader.readInt();
    final int pAtk = reader.readInt();
    final int pDef = reader.readInt();
    final int pHP = reader.readInt();
    final int pMP = reader.readInt();
    final int strength = reader.readInt();
    final int block = reader.readInt();
    final int agility = reader.readInt();
    final int vitality = reader.readInt();
    final int intelligence = reader.readInt();
    final int luck = reader.readInt();
    final int lvl = reader.readInt();
    final int cHP = reader.readInt();
    final int cMP = reader.readInt();
    final int gld = reader.readInt();
    final int apr = reader.readInt();
    final int spr = reader.readInt();
    final int load = reader.readInt();
    final long exp = reader.readLong();
    final int r = reader.readInt();
    final int j = reader.readInt();
    final int f = reader.readInt();
    final int max = reader.readInt();
    final boolean[] known = new boolean[max];
    for (int x = 0; x < max; x++) {
      known[x] = reader.readBoolean();
    }
    final String n = reader.readString();
    final int aFamily = reader.readInt();
    final int aSkin = reader.readInt();
    final int aHair = reader.readInt();
    final ItemInventory ii = ItemInventory.readItemInventory(reader, version);
    final PartyMember pm = PartyManager.getNewPCInstance(ii, r, j, f, n);
    pm.setStrength(strength);
    pm.setBlock(block);
    pm.setAgility(agility);
    pm.setVitality(vitality);
    pm.setIntelligence(intelligence);
    pm.setLuck(luck);
    pm.setAttacksPerRound(apr);
    pm.setSpellsPerRound(spr);
    pm.setItems(ItemInventory.readItemInventory(reader, version));
    pm.loadPartyMember(lvl, cHP, cMP, gld, load, exp, j, known, k, pAtk, pDef,
        pHP, pMP, aFamily, aSkin, aHair);
    return pm;
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
}
