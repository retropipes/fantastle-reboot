/*  FantastleReboot: An RPG
Copyright (C) 2011-2012 Eric Ahnell


Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.creatures.party;

import java.io.IOException;

import com.puttysoftware.diane.gui.CommonDialogs;
import com.puttysoftware.diane.gui.ImageListWithDescDialog;
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
import com.puttysoftware.fantastlereboot.loaders.AvatarImageLoader;
import com.puttysoftware.fantastlereboot.loaders.DataLoader;
import com.puttysoftware.images.BufferedImageIcon;
import com.puttysoftware.randomrange.RandomRange;
import com.puttysoftware.xio.XDataReader;
import com.puttysoftware.xio.XDataWriter;

public class PartyManager {
  // Fields
  private static Party party;
  private static int bank = 0;
  private static final String[] buttonNames = new String[] { "Done", "Create",
      "Pick" };
  private static String[] givenNames, familyNames;
  private static boolean namesInited = false;

  // Constructors
  private PartyManager() {
    // Do nothing
  }

  // Methods
  public static boolean createParty() {
    PartyManager.party = new Party();
    int mem = 0;
    final int maxMem = Party.getMaxMembers();
    final PartyMember[] pickMembers = CharacterLoader
        .loadAllRegisteredCharacters();
    int regLen = 0;
    if (pickMembers != null) {
      regLen = pickMembers.length;
    }
    final String[] autoButtons = new String[] { "Automatically", "Manually" };
    final int autoResponse = CommonDialogs.showCustomDialog(
        "How should the party be assembled?", "Create Party", autoButtons,
        autoButtons[0]);
    final boolean autoCreate = autoResponse != 1;
    if (autoCreate) {
      // Automatically assemble a party
      for (int x = 0; x < maxMem; x++) {
        final ItemInventory ii = new ItemInventory();
        final int r = RaceManager.getRandomID();
        final int j = JobManager.getRandomID();
        final int f = FaithManager.getRandomID();
        final String n = PartyManager.generateDefaultName();
        final int af = RandomRange.generate(0, 5);
        final int as = RandomRange.generate(0, 9);
        final int ah = RandomRange.generate(0, 9);
        final PartyMember pc = PartyManager.getNewPCInstance(ii, r, j, f, n, af,
            as, ah);
        CharacterRegistration.autoregisterCharacter(pc.getName());
        CharacterSaver.saveCharacter(pc);
        regLen++;
        PartyManager.party.addPartyMember(pc);
        mem++;
      }
      CommonDialogs.showDialog("Party automatically assembled.");
    } else {
      // Manually assemble a party
      for (int x = 0; x < maxMem; x++) {
        PartyMember pc = null;
        if (mem >= regLen) {
          // No more characters registered - must create one
          pc = PartyManager.createNewPC();
          if (pc != null) {
            CharacterRegistration.autoregisterCharacter(pc.getName());
            CharacterSaver.saveCharacter(pc);
            regLen++;
          }
        } else {
          final int response = CommonDialogs.showCustomDialog(
              "Pick, Create, or Done?", "Create Party",
              PartyManager.buttonNames, PartyManager.buttonNames[2]);
          if (response == 2) {
            pc = PartyManager.pickOnePartyMemberCreate(pickMembers);
          } else if (response == 1) {
            pc = PartyManager.createNewPC();
            if (pc != null) {
              CharacterRegistration.autoregisterCharacter(pc.getName());
              CharacterSaver.saveCharacter(pc);
              regLen++;
            }
          }
        }
        if (pc == null) {
          break;
        }
        PartyManager.party.addPartyMember(pc);
        mem++;
      }
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

  private static String generateDefaultName() {
    if (!PartyManager.namesInited) {
      PartyManager.givenNames = DataLoader.loadGivenNameData();
      PartyManager.familyNames = DataLoader.loadFamilyNameData();
      PartyManager.namesInited = true;
    }
    final int givenIndex = RandomRange.generate(0,
        PartyManager.givenNames.length - 1);
    final int familyIndex = RandomRange.generate(0,
        PartyManager.familyNames.length - 1);
    return PartyManager.givenNames[givenIndex] + " "
        + PartyManager.familyNames[familyIndex];
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
      final int r, final int j, final int f, final String n, final int af,
      final int as, final int ah) {
    final Race race = RaceManager.getRace(r);
    final Job job = JobManager.getJob(j);
    final Faith faith = FaithManager.getFaith(f);
    return new PartyMember(ii, race, job, faith, n, af, as, ah);
  }

  public static void updatePostKill() {
    final PartyMember leader = PartyManager.getParty().getLeader();
    leader.initPostKill(leader.getRace(), leader.getJob(), leader.getFaith());
  }

  private static PartyMember createNewPC() {
    final int randomSkin = RandomRange.generate(0, 9);
    final int randomHair = RandomRange.generate(0, 9);
    final String name = CommonDialogs.showTextInputDialogWithDefault(
        "Character Name", "Create Character",
        PartyManager.generateDefaultName());
    if (name != null) {
      final Race race = RaceManager.selectRace();
      if (race != null) {
        final Job caste = JobManager.selectJob();
        if (caste != null) {
          final Faith faith = FaithManager.selectFaith();
          if (faith != null) {
            final int avatarFamily = PartyManager.pickAvatarFamily(randomSkin,
                randomHair);
            if (avatarFamily != ImageListWithDescDialog.CANCEL) {
              final int avatarSkin = PartyManager
                  .pickAvatarSkinColor(avatarFamily, randomHair);
              if (avatarSkin != ImageListWithDescDialog.CANCEL) {
                final int avatarHair = PartyManager
                    .pickAvatarHairColor(avatarFamily, avatarSkin);
                if (avatarHair != ImageListWithDescDialog.CANCEL) {
                  final ItemInventory ii = new ItemInventory();
                  return new PartyMember(ii, race, caste, faith, name,
                      avatarFamily, avatarSkin, avatarHair);
                }
              }
            }
          }
        }
      }
    }
    return null;
  }

  private static int pickAvatarFamily(final int randomSkinID,
      final int randomHairID) {
    final String labelText = "Avatar Families";
    final String title = "Pick Avatar Family";
    final BufferedImageIcon[] input = new BufferedImageIcon[] {
        AvatarImageLoader.load(0, randomSkinID, randomHairID),
        AvatarImageLoader.load(1, randomSkinID, randomHairID),
        AvatarImageLoader.load(2, randomSkinID, randomHairID),
        AvatarImageLoader.load(3, randomSkinID, randomHairID),
        AvatarImageLoader.load(4, randomSkinID, randomHairID),
        AvatarImageLoader.load(5, randomSkinID, randomHairID) };
    final String[] descriptions = new String[] { "Green", "Red", "Yellow",
        "Cyan", "Pink", "Blue" };
    return ImageListWithDescDialog.showDialog(null, labelText, title, input, 0,
        descriptions[0], descriptions);
  }

  private static int pickAvatarSkinColor(final int familyID,
      final int randomHairID) {
    final String labelText = "Avatar Skin Colors";
    final String title = "Pick Avatar Skin Color";
    final BufferedImageIcon[] input = new BufferedImageIcon[] {
        AvatarImageLoader.load(familyID, 0, randomHairID),
        AvatarImageLoader.load(familyID, 1, randomHairID),
        AvatarImageLoader.load(familyID, 2, randomHairID),
        AvatarImageLoader.load(familyID, 3, randomHairID),
        AvatarImageLoader.load(familyID, 4, randomHairID),
        AvatarImageLoader.load(familyID, 5, randomHairID),
        AvatarImageLoader.load(familyID, 6, randomHairID),
        AvatarImageLoader.load(familyID, 7, randomHairID),
        AvatarImageLoader.load(familyID, 8, randomHairID),
        AvatarImageLoader.load(familyID, 9, randomHairID) };
    final String[] descriptions = new String[] { "Darkest", "Darker", "Dark",
        "Mildly Dark", "Slightly Dark", "Slightly Light", "Mildly Light",
        "Light", "Lighter", "Lightest" };
    return ImageListWithDescDialog.showDialog(null, labelText, title, input, 0,
        descriptions[0], descriptions);
  }

  private static int pickAvatarHairColor(final int familyID, final int skinID) {
    final String labelText = "Avatar Hair Colors";
    final String title = "Pick Avatar Hair Color";
    final BufferedImageIcon[] input = new BufferedImageIcon[] {
        AvatarImageLoader.load(familyID, skinID, 0),
        AvatarImageLoader.load(familyID, skinID, 1),
        AvatarImageLoader.load(familyID, skinID, 2),
        AvatarImageLoader.load(familyID, skinID, 3),
        AvatarImageLoader.load(familyID, skinID, 4),
        AvatarImageLoader.load(familyID, skinID, 5),
        AvatarImageLoader.load(familyID, skinID, 6),
        AvatarImageLoader.load(familyID, skinID, 7),
        AvatarImageLoader.load(familyID, skinID, 8),
        AvatarImageLoader.load(familyID, skinID, 9) };
    final String[] descriptions = new String[] { "Black", "Dark Gray",
        "Dark Brown", "Light Brown", "Red", "Light Gray", "Dark Yellow",
        "Yellow", "Tan", "Bright Yellow" };
    return ImageListWithDescDialog.showDialog(null, labelText, title, input, 0,
        descriptions[0], descriptions);
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
    for (final String tempName : tempNames) {
      if (tempName != null) {
        names[nnc] = tempName;
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
