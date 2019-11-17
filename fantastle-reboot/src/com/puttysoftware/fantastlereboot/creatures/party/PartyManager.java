/*  FantastleReboot: An RPG
Copyright (C) 2011-2012 Eric Ahnell


Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.creatures.party;

import java.io.IOException;

import javax.swing.JFrame;

import com.puttysoftware.commondialogs.CommonDialogs;
import com.puttysoftware.fantastlereboot.creatures.faiths.Faith;
import com.puttysoftware.fantastlereboot.creatures.faiths.FaithManager;
import com.puttysoftware.fantastlereboot.creatures.jobs.Job;
import com.puttysoftware.fantastlereboot.creatures.jobs.JobManager;
import com.puttysoftware.fantastlereboot.creatures.races.Race;
import com.puttysoftware.fantastlereboot.creatures.races.RaceManager;
import com.puttysoftware.fantastlereboot.files.CharacterLoader;
import com.puttysoftware.fantastlereboot.files.CharacterRegistration;
import com.puttysoftware.fantastlereboot.files.CharacterSaver;
import com.puttysoftware.fantastlereboot.items.ItemInventory;
import com.puttysoftware.xio.XDataReader;
import com.puttysoftware.xio.XDataWriter;

public class PartyManager {
  // Fields
  private static Party party;
  private static int bank = 0;
  private static final int PARTY_SIZE = 1;
  private final static String[] buttonNames = new String[] { "Done", "Create",
      "Pick" };

  // Constructors
  private PartyManager() {
    // Do nothing
  }

  // Methods
  public static boolean createParty() {
    PartyManager.party = new Party();
    int mem = 0;
    final PartyMember[] pickMembers = CharacterLoader
        .loadAllRegisteredCharacters();
    for (int x = 0; x < PartyManager.PARTY_SIZE; x++) {
      PartyMember pc = null;
      if (pickMembers == null) {
        // No characters registered - must create one
        pc = PartyManager.createNewPC();
        if (pc != null) {
          CharacterRegistration.autoregisterCharacter(pc.getName());
          CharacterSaver.saveCharacter(pc);
        }
      } else {
        final int response = CommonDialogs.showCustomDialog(
            "Pick, Create, or Done?", "Create Party", buttonNames,
            buttonNames[2]);
        if (response == 2) {
          pc = PartyManager.pickOnePartyMemberCreate(pickMembers);
        } else if (response == 1) {
          pc = PartyManager.createNewPC();
          if (pc != null) {
            CharacterRegistration.autoregisterCharacter(pc.getName());
            CharacterSaver.saveCharacter(pc);
          }
        }
      }
      if (pc == null) {
        break;
      }
      PartyManager.party.addPartyMember(pc);
      mem++;
    }
    if (mem == 0) {
      return false;
    }
    return true;
  }

  public static Party getParty() {
    return PartyManager.party;
  }

  public static void addGoldToBank(final int newGold) {
    PartyManager.bank += newGold;
  }

  public static int getGoldInBank() {
    return PartyManager.bank;
  }

  public static void removeGoldFromBank(final int cost) {
    PartyManager.bank -= cost;
    if (PartyManager.bank < 0) {
      PartyManager.bank = 0;
    }
  }

  private static void setGoldInBank(final int newGold) {
    PartyManager.bank = newGold;
  }

  public static void loadGameHook(final XDataReader partyFile)
      throws IOException {
    final boolean containsPCData = partyFile.readBoolean();
    if (containsPCData) {
      final int gib = partyFile.readInt();
      PartyManager.setGoldInBank(gib);
      PartyManager.party = Party.read(partyFile);
    }
  }

  public static void saveGameHook(final XDataWriter partyFile)
      throws IOException {
    if (PartyManager.party != null) {
      partyFile.writeBoolean(true);
      partyFile.writeInt(PartyManager.getGoldInBank());
      PartyManager.party.write(partyFile);
    } else {
      partyFile.writeBoolean(false);
    }
  }

  public static PartyMember getNewPCInstance(final ItemInventory ii,
      final int r, final int j, final int f, final String n) {
    final Race race = RaceManager.getRace(r);
    final Job job = JobManager.getJob(j);
    final Faith faith = FaithManager.getFaith(f);
    return new PartyMember(ii, race, job, faith, n);
  }

  public static void updatePostKill() {
    final PartyMember leader = getParty().getLeader();
    leader.initPostKill(leader.getRace(), leader.getJob(), leader.getFaith());
  }

  private static PartyMember createNewPC() {
    final String name = CommonDialogs.showTextInputDialog("Character Name",
        "Create Character");
    if (name != null) {
      final Race race = RaceManager.selectRace();
      if (race != null) {
        final Job caste = JobManager.selectJob();
        if (caste != null) {
          final Faith faith = FaithManager.selectFaith();
          if (faith != null) {
            ItemInventory ii = new ItemInventory();
            return new PartyMember(ii, race, caste, faith, name);
          }
        }
      }
    }
    return null;
  }

  public static String showCreationDialog(final JFrame owner,
      final String labelText, final String title, final String[] input,
      final String[] descriptions) {
    return ListWithDescDialog.showDialog(owner, null, labelText, title, input,
        input[0], descriptions[0], descriptions);
  }

  private static String[] buildNameList(final PartyMember[] members) {
    final String[] tempNames = new String[1];
    int nnc = 0;
    for (int x = 0; x < tempNames.length; x++) {
      if (members != null) {
        tempNames[x] = members[x].getName();
        nnc++;
      }
    }
    final String[] names = new String[nnc];
    nnc = 0;
    for (int x = 0; x < tempNames.length; x++) {
      if (tempNames[x] != null) {
        names[nnc] = tempNames[x];
        nnc++;
      }
    }
    return names;
  }

  private static PartyMember
      pickOnePartyMemberCreate(final PartyMember[] members) {
    final String[] pickNames = PartyManager.buildNameList(members);
    final String response = CommonDialogs.showInputDialog("Pick 1 Party Member",
        "Create Party", pickNames, pickNames[0]);
    if (response != null) {
      for (int x = 0; x < members.length; x++) {
        if (members[x].getName().equals(response)) {
          return members[x];
        }
      }
      return null;
    } else {
      return null;
    }
  }
}
