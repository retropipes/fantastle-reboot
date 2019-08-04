package net.worldwizard.fantastle5.objects;

import java.io.IOException;

import com.puttysoftware.randomrange.RandomRange;

import net.worldwizard.fantastle5.Fantastle5;
import net.worldwizard.fantastle5.generic.GenericDungeonObject;
import net.worldwizard.fantastle5.generic.MazeObject;
import net.worldwizard.fantastle5.generic.MazeObjectList;
import net.worldwizard.io.DataReader;
import net.worldwizard.io.DataWriter;

public class MovingBlock extends GenericDungeonObject {
    // Fields
    private MazeObject savedObject;

    // Constructors
    public MovingBlock() {
        super(true);
        this.savedObject = new Empty();
        final RandomRange t = new RandomRange(1, 2);
        this.activateTimer(t.generate());
    }

    @Override
    public MovingBlock clone() {
        final MovingBlock copy = new MovingBlock();
        copy.savedObject = this.savedObject.clone();
        return copy;
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
        // Move the block
        final RandomRange r = new RandomRange(0, 7);
        final int move = r.generate();
        Fantastle5.getApplication().getMazeManager().getMaze()
                .updateMovingBlockPosition(move, dirX, dirY, this);
        final RandomRange t = new RandomRange(1, 2);
        this.activateTimer(t.generate());
    }

    @Override
    public String getName() {
        return "Moving Block";
    }

    @Override
    public String getPluralName() {
        return "Moving Blocks";
    }

    @Override
    public String getDescription() {
        return "Moving Blocks move on their own. They cannot be pushed or pulled.";
    }

    @Override
    public byte getObjectID() {
        return (byte) 0;
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
}
