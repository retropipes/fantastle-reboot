/*  FantastleReboot: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.creatures.party;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.puttysoftware.diane.gui.CommonDialogs;
import com.puttysoftware.fantastlereboot.assets.SoundGroup;
import com.puttysoftware.fantastlereboot.assets.SoundIndex;
import com.puttysoftware.fantastlereboot.files.CharacterLoader;
import com.puttysoftware.fantastlereboot.files.CharacterSaver;
import com.puttysoftware.fantastlereboot.loaders.SoundPlayer;
import com.puttysoftware.fantastlereboot.maze.Maze;
import com.puttysoftware.fantastlereboot.objects.Player;
import com.puttysoftware.fantastlereboot.objects.temporary.BattleCharacter;
import com.puttysoftware.xio.XDataReader;
import com.puttysoftware.xio.XDataWriter;

public class Party {
  // Fields
  private ArrayList<PartyMember> members;
  private ArrayList<BattleCharacter> battlers;
  private int leaderID;
  private int activePCs;
  private int monsterLevel;
  private static final int MAX_SIZE = 10;

  // Constructors
  public Party() {
    this.members = new ArrayList<>();
    this.battlers = new ArrayList<>();
    this.leaderID = -1;
    this.activePCs = 0;
    this.monsterLevel = 0;
  }

  // Methods
  static int getMaxMembers() {
    return Party.MAX_SIZE;
  }

  private void generateBattleCharacters() {
    this.battlers.clear();
    for (PartyMember member : this.members) {
      this.battlers.add(new BattleCharacter(member));
    }
  }

  public List<BattleCharacter> getBattleCharacters() {
    if (this.battlers == null) {
      this.generateBattleCharacters();
    }
    return this.battlers;
  }

  public long getPartyMaxToNextLevel() {
    long result = 0L;
    for (PartyMember member : this.members) {
      long value = member.getToNextLevelValue();
      if (value > result) {
        result = value;
      }
    }
    return result;
  }

  public int getMembers() {
    return this.members.size();
  }

  public int getMonsterLevel() {
    return this.monsterLevel;
  }

  void resetMonsterLevel() {
    this.monsterLevel = 0;
  }

  public void decrementMonsterLevel() {
    this.offsetMonsterLevel(-1);
  }

  public void incrementMonsterLevel() {
    this.offsetMonsterLevel(1);
  }

  public void offsetMonsterLevel(final int offset) {
    if (this.monsterLevel + offset > Maze.getMaxLevels()
        || this.monsterLevel + offset < 0) {
      return;
    }
    this.monsterLevel += offset;
  }

  public String getMonsterLevelString() {
    return "Monster Level: " + (this.monsterLevel + 1);
  }

  public PartyMember getLeader() {
    return this.members.get(this.leaderID);
  }

  public void checkPartyLevelUp() {
    for (PartyMember member : this.members) {
      // Level Up Check
      if (member.checkLevelUp()) {
        member.levelUp();
        SoundPlayer.playSound(SoundIndex.LEVEL_UP, SoundGroup.GAME);
        CommonDialogs.showTitledDialog(
            member.getName() + " reached level " + member.getLevel() + "!",
            "Level Up");
      }
    }
  }

  public boolean isAlive() {
    boolean result = false;
    for (PartyMember member : this.members) {
      result |= member.isAlive();
    }
    return result;
  }

  public void fireStepActions() {
    for (PartyMember member : this.members) {
      member.getItems().fireStepActions(member);
    }
  }

  boolean addPartyMember(final PartyMember member) {
    if (this.members.size() < Party.MAX_SIZE) {
      this.members.add(member);
      this.leaderID++;
      Player.setAvatar(member.getAvatarFamilyID(), member.getAvatarSkinID(),
          member.getAvatarHairID());
      return true;
    }
    return false;
  }

  public void pickLeader() {
    final String[] pickNames = this.buildNameList();
    final String response = CommonDialogs.showInputDialog("Pick Leader",
        "Arrange Party", pickNames, pickNames[this.leaderID]);
    if (response != null) {
      int x = 0;
      for (PartyMember member : this.members) {
        if (member.getName().equals(response)) {
          this.leaderID = x;
          Player.setAvatar(member.getAvatarFamilyID(), member.getAvatarSkinID(),
              member.getAvatarHairID());
          break;
        }
        x++;
      }
    }
  }

  private String[] buildNameList() {
    final String[] tempNames = new String[1];
    int nnc = 0;
    for (PartyMember member : this.members) {
      if (member != null) {
        tempNames[nnc] = member.getName();
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

  static Party read(final XDataReader worldFile) throws IOException {
    final Party pty = new Party();
    final int mem = worldFile.readInt();
    final int lid = worldFile.readInt();
    final int apc = worldFile.readInt();
    final int lvl = worldFile.readInt();
    pty.leaderID = lid;
    pty.activePCs = apc;
    pty.monsterLevel = lvl;
    for (int m = 0; m < mem; m++) {
      final boolean present = worldFile.readBoolean();
      if (present) {
        pty.members.add(CharacterLoader.readCharacter(worldFile));
      }
    }
    return pty;
  }

  void write(final XDataWriter worldFile) throws IOException {
    worldFile.writeInt(this.members.size());
    worldFile.writeInt(this.leaderID);
    worldFile.writeInt(this.activePCs);
    worldFile.writeInt(this.monsterLevel);
    for (PartyMember member : this.members) {
      worldFile.writeBoolean(true);
      CharacterSaver.writeCharacter(worldFile, member);
    }
  }
}
