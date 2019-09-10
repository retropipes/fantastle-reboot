/*  TallerTower: An RPG
Copyright (C) 2011-2012 Eric Ahnell


Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.creatures.party;

import java.io.IOException;

import javax.swing.JFrame;

import com.puttysoftware.commondialogs.CommonDialogs;
import com.puttysoftware.fantastlereboot.creatures.castes.Caste;
import com.puttysoftware.fantastlereboot.creatures.castes.CasteManager;
import com.puttysoftware.fantastlereboot.creatures.characterfiles.CharacterLoader;
import com.puttysoftware.fantastlereboot.creatures.characterfiles.CharacterRegistration;
import com.puttysoftware.fantastlereboot.creatures.faiths.Faith;
import com.puttysoftware.fantastlereboot.creatures.faiths.FaithManager;
import com.puttysoftware.fantastlereboot.creatures.genders.Gender;
import com.puttysoftware.fantastlereboot.creatures.genders.GenderManager;
import com.puttysoftware.fantastlereboot.creatures.personalities.Personality;
import com.puttysoftware.fantastlereboot.creatures.personalities.PersonalityManager;
import com.puttysoftware.fantastlereboot.creatures.races.Race;
import com.puttysoftware.fantastlereboot.creatures.races.RaceManager;
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
    public static boolean createParty(final JFrame owner) {
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
                    CharacterLoader.saveCharacter(pc);
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
                        CharacterRegistration
                                .autoregisterCharacter(pc.getName());
                        CharacterLoader.saveCharacter(pc);
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

    public static PartyMember getNewPCInstance(final int r, final int c,
            final int f, final int p, final int g, final String n) {
        final Race race = RaceManager.getRace(r);
        final Caste caste = CasteManager.getCaste(c);
        final Faith faith = FaithManager.getFaith(f);
        final Personality personality = PersonalityManager.getPersonality(p);
        final Gender gender = GenderManager.getGender(g);
        return new PartyMember(race, caste, faith, personality, gender, n);
    }

    public static void updatePostKill() {
        final PartyMember leader = getParty().getLeader();
        leader.initPostKill(leader.getRace(), leader.getCaste(),
                leader.getFaith(), leader.getPersonality(), leader.getGender());
    }

    private static PartyMember createNewPC() {
        final String name = CommonDialogs.showTextInputDialog("Character Name",
                "Create Character");
        if (name != null) {
            final Race race = RaceManager.selectRace();
            if (race != null) {
                final Caste caste = CasteManager.selectCaste();
                if (caste != null) {
                    final Faith faith = FaithManager.selectFaith();
                    if (faith != null) {
                        final Personality personality = PersonalityManager
                                .selectPersonality();
                        if (personality != null) {
                            final Gender gender = GenderManager.selectGender();
                            if (gender != null) {
                                return new PartyMember(race, caste, faith,
                                        personality, gender, name);
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public static String showCreationDialog(final JFrame owner,
            final String labelText, final String title, final String[] input,
            final String[] descriptions) {
        return ListWithDescDialog.showDialog(owner, null, labelText, title,
                input, input[0], descriptions[0], descriptions);
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

    private static PartyMember pickOnePartyMemberCreate(
            final PartyMember[] members) {
        final String[] pickNames = PartyManager.buildNameList(members);
        final String response = CommonDialogs.showInputDialog(
                "Pick 1 Party Member", "Create Party", pickNames, pickNames[0]);
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
