package net.worldwizard.fantastle5.objects;

import java.io.IOException;

import com.puttysoftware.randomrange.RandomRange;

import net.worldwizard.fantastle5.Fantastle5;
import net.worldwizard.fantastle5.battle.Battle;
import net.worldwizard.fantastle5.game.ObjectInventory;
import net.worldwizard.fantastle5.generic.ArrowTypeConstants;
import net.worldwizard.fantastle5.generic.GenericDungeonObject;
import net.worldwizard.fantastle5.generic.MazeObject;
import net.worldwizard.fantastle5.generic.MazeObjectList;
import net.worldwizard.fantastle5.generic.TypeConstants;
import net.worldwizard.io.DataReader;
import net.worldwizard.io.DataWriter;

public class Monster extends GenericDungeonObject {
    // Fields
    private MazeObject savedObject;

    // Constructors
    public Monster() {
        super(false);
        this.savedObject = new Empty();
        this.activateTimer(1);
    }

    public Monster(final MazeObject saved) {
        super(false);
        this.savedObject = saved;
        this.activateTimer(1);
    }

    @Override
    public Monster clone() {
        final Monster copy = new Monster(this.savedObject.clone());
        return copy;
    }

    @Override
    public void postMoveAction(final boolean ie, final int dirX,
            final int dirY, final ObjectInventory inv) {
        if (!Battle.isInBattle()) {
            Fantastle5.getApplication().getBattle().doBattle();
            Fantastle5.getApplication().getMazeManager().getMaze()
                    .postBattle(this, dirX, dirY, true);
        }
    }

    @Override
    public boolean arrowHitAction(final int locX, final int locY,
            final int locZ, final int locW, final int dirX, final int dirY,
            final int arrowType, final ObjectInventory inv) {
        if (arrowType == ArrowTypeConstants.ARROW_TYPE_ICE) {
            // Transform into iced monster, if hit by an ice arrow
            final int pz = Fantastle5.getApplication().getGameManager()
                    .getPlayerManager().getPlayerLocationZ();
            final int pw = Fantastle5.getApplication().getGameManager()
                    .getPlayerManager().getPlayerLocationW();
            Fantastle5
                    .getApplication()
                    .getGameManager()
                    .morph(new IcedMonster(this.savedObject), locX, locY, pz,
                            pw);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean hasAdditionalProperties() {
        return true;
    }

    public MazeObject getSavedObject() {
        return this.savedObject;
    }

    public void setSavedObject(final MazeObject newSavedObject) {
        this.savedObject = newSavedObject;
    }

    @Override
    public void timerExpiredAction(final int dirX, final int dirY) {
        // Move the monster
        final RandomRange r = new RandomRange(0, 7);
        final int move = r.generate();
        Fantastle5.getApplication().getMazeManager().getMaze()
                .updateMonsterPosition(move, dirX, dirY, this);
        this.activateTimer(1);
    }

    @Override
    public String getName() {
        return "Monster";
    }

    @Override
    public String getPluralName() {
        return "Monsters";
    }

    @Override
    public String getDescription() {
        return "Monsters are dangerous. Encountering one starts a battle.";
    }

    @Override
    public byte getObjectID() {
        return (byte) 7;
    }

    @Override
    public int getCustomFormat() {
        return MazeObject.CUSTOM_FORMAT_MANUAL_OVERRIDE;
    }

    @Override
    protected void writeMazeObjectHook(final DataWriter writer)
            throws IOException {
        this.savedObject.writeMazeObject(writer);
    }

    @Override
    protected MazeObject readMazeObjectHook(final DataReader reader,
            final int formatVersion) throws IOException {
        final MazeObjectList objectList = Fantastle5.getApplication()
                .getObjects();
        this.savedObject = objectList.readMazeObject(reader, formatVersion);
        return this;
    }

    @Override
    protected void setTypes() {
        super.setTypes();
        this.type.set(TypeConstants.TYPE_REACTS_TO_ICE);
    }
}
