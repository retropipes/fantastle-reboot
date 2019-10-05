/*  TallerTower: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.creatures.party;

import java.io.IOException;

import com.puttysoftware.commondialogs.CommonDialogs;
import com.puttysoftware.fantastlereboot.assets.SoundIndex;
import com.puttysoftware.fantastlereboot.loaders.SoundPlayer;
import com.puttysoftware.fantastlereboot.obsolete.maze2.Maze;
import com.puttysoftware.fantastlereboot.obsolete.maze2.objects.BattleCharacter;
import com.puttysoftware.xio.XDataReader;
import com.puttysoftware.xio.XDataWriter;

public class Party {
    // Fields
    private PartyMember members;
    private BattleCharacter battlers;
    private int leaderID;
    private int activePCs;
    private int monsterLevel;

    // Constructors
    public Party() {
        this.members = null;
        this.leaderID = 0;
        this.activePCs = 0;
        this.monsterLevel = 0;
    }

    // Methods
    private void generateBattleCharacters() {
        this.battlers = new BattleCharacter(this.members);
    }

    public BattleCharacter getBattleCharacters() {
        if (this.battlers == null) {
            this.generateBattleCharacters();
        }
        return this.battlers;
    }

    public long getPartyMaxToNextLevel() {
        return this.members.getToNextLevelValue();
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
        return this.members;
    }

    public void checkPartyLevelUp() {
        // Level Up Check
        if (this.members.checkLevelUp()) {
            this.members.levelUp();
            SoundPlayer.playSound(SoundIndex.LEVEL_UP);
            CommonDialogs
                    .showTitledDialog(
                            this.members.getName() + " reached level "
                                    + this.members.getLevel() + "!",
                            "Level Up");
        }
    }

    public boolean isAlive() {
        return this.members.isAlive();
    }

    public void fireStepActions() {
        this.members.getItems().fireStepActions(this.members);
    }

    boolean addPartyMember(final PartyMember member) {
        this.members = member;
        return true;
    }

    static Party read(final XDataReader worldFile) throws IOException {
        worldFile.readInt();
        final int lid = worldFile.readInt();
        final int apc = worldFile.readInt();
        final int lvl = worldFile.readInt();
        final Party pty = new Party();
        pty.leaderID = lid;
        pty.activePCs = apc;
        pty.monsterLevel = lvl;
        final boolean present = worldFile.readBoolean();
        if (present) {
            pty.members = PartyMember.read(worldFile);
        }
        return pty;
    }

    void write(final XDataWriter worldFile) throws IOException {
        worldFile.writeInt(1);
        worldFile.writeInt(this.leaderID);
        worldFile.writeInt(this.activePCs);
        worldFile.writeInt(this.monsterLevel);
        if (this.members == null) {
            worldFile.writeBoolean(false);
        } else {
            worldFile.writeBoolean(true);
            this.members.write(worldFile);
        }
    }
}
