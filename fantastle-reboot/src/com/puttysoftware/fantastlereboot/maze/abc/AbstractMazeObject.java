/*  TallerTower: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.maze.abc;

import java.io.IOException;
import java.util.BitSet;

import com.puttysoftware.fantastlereboot.BagOStuff;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.Messager;
import com.puttysoftware.fantastlereboot.PreferencesManager;
import com.puttysoftware.fantastlereboot.assets.GameObjectImage;
import com.puttysoftware.fantastlereboot.assets.GameSound;
import com.puttysoftware.fantastlereboot.game.ObjectInventory;
import com.puttysoftware.fantastlereboot.loaders.ObjectImageLoader;
import com.puttysoftware.fantastlereboot.loaders.SoundPlayer;
import com.puttysoftware.fantastlereboot.maze.FormatConstants;
import com.puttysoftware.fantastlereboot.maze.Maze;
import com.puttysoftware.fantastlereboot.maze.MazeConstants;
import com.puttysoftware.fantastlereboot.utilities.ImageColorConstants;
import com.puttysoftware.fantastlereboot.utilities.RandomGenerationRule;
import com.puttysoftware.fantastlereboot.utilities.TypeConstants;
import com.puttysoftware.images.BufferedImageIcon;
import com.puttysoftware.randomrange.RandomRange;
import com.puttysoftware.xio.XDataReader;
import com.puttysoftware.xio.XDataWriter;

public abstract class AbstractMazeObject implements RandomGenerationRule {
    // Properties
    private SolidProperties sp;
    private MoveProperties mp;
    private final boolean friction;
    private final boolean blocksLOS;
    private static int templateColor = ImageColorConstants.COLOR_NONE;
    private int timerValue;
    private final int initialTimerValue;
    private boolean timerActive;
    private final boolean usable;
    private int uses;
    private final boolean destroyable;
    private final boolean chainReacts;
    private final boolean isInventoryable;
    protected BitSet type;
    private AbstractMazeObject saved;
    public static final int DEFAULT_CUSTOM_VALUE = 0;
    protected static final int CUSTOM_FORMAT_MANUAL_OVERRIDE = -1;

    // Constructors
    public AbstractMazeObject() {
        this.sp = new SolidProperties();
        this.mp = new MoveProperties();
        this.friction = true;
        this.blocksLOS = false;
        this.usable = false;
        this.uses = 0;
        this.destroyable = true;
        this.chainReacts = false;
        this.isInventoryable = false;
        this.type = new BitSet(TypeConstants.TYPES_COUNT);
        this.timerValue = 0;
        this.initialTimerValue = 0;
        this.timerActive = false;
        this.setTypes();
    }

    public AbstractMazeObject(final boolean hasFriction,
            final boolean sightBlock, final boolean isUsable,
            final int startingUses, final boolean destroy, final boolean chain,
            final boolean inventory) {
        this.sp = new SolidProperties();
        this.mp = new MoveProperties();
        this.friction = hasFriction;
        this.blocksLOS = sightBlock;
        this.usable = isUsable;
        this.uses = startingUses;
        this.destroyable = destroy;
        this.chainReacts = chain;
        this.isInventoryable = inventory;
        this.type = new BitSet(TypeConstants.TYPES_COUNT);
        this.timerValue = 0;
        this.initialTimerValue = 0;
        this.timerActive = false;
        this.setTypes();
    }

    // Methods
    public AbstractMazeObject getSavedObject() {
        if (this.saved == null) {
            throw new NullPointerException("Saved object == NULL!");
        }
        return this.saved;
    }

    public void setSavedObject(final AbstractMazeObject newSaved) {
        if (newSaved == null) {
            throw new IllegalArgumentException("New saved object == NULL!");
        }
        this.saved = newSaved;
    }

    public boolean isSolidInBattle() {
        if (this.enabledInBattle()) {
            return this.isSolid();
        } else {
            return false;
        }
    }

    public boolean isSightBlocking() {
        return this.blocksLOS;
    }

    public static int getTemplateColor() {
        return templateColor;
    }

    public static void setTemplateColor(final int newTC) {
        templateColor = newTC;
    }

    // Scripting
    /**
     *
     * @param ie
     * @param dirX
     * @param dirY
     * @param inv
     * @return
     */
    public boolean preMoveAction(final boolean ie, final int dirX,
            final int dirY) {
        return true;
    }

    public abstract void postMoveAction(final boolean ie, final int dirX,
            final int dirY);

    /**
     *
     * @param ie
     * @param dirX
     * @param dirY
     * @param inv
     */
    public void moveFailedAction(final boolean ie, final int dirX,
            final int dirY) {
        SoundPlayer.playSound(GameSound.WALK_FAILED);
        final BagOStuff bag = FantastleReboot.getBagOStuff();
        bag.showMessage("Can't go that way");
    }

    /**
     *
     * @param x
     * @param y
     * @param z
     */
    public void editorGenerateHook(final int x, final int y, final int z) {
        // Do nothing
    }

    public boolean arrowHitBattleCheck() {
        return !this.isSolid();
    }

    /**
     *
     * @param x
     * @param y
     * @param z
     * @return
     */
    public BufferedImageIcon gameRenderHook(final int x, final int y,
            final int z) {
        return ObjectImageLoader.load(this.getGameBaseID());
    }

    public BufferedImageIcon battleRenderHook() {
        return ObjectImageLoader.load(this.getBattleBaseID());
    }

    /**
     *
     * @param x
     * @param y
     * @param z
     */
    public void determineCurrentAppearance(final int x, final int y,
            final int z) {
        // Do nothing
    }

    public final void activateTimer(final int ticks) {
        this.timerActive = true;
        this.timerValue = ticks;
    }

    public final void tickTimer(final int dirX, final int dirY) {
        if (this.timerActive) {
            this.timerValue--;
            if (this.timerValue == 0) {
                this.timerActive = false;
                this.timerExpiredAction(dirX, dirY);
            }
        }
    }

    public abstract GameObjectImage getBaseID();

    public GameObjectImage getGameBaseID() {
        return this.getBaseID();
    }

    public GameObjectImage getBattleBaseID() {
        if (this.enabledInBattle()) {
            return this.getGameBaseID();
        } else {
            return GameObjectImage._NONE;
        }
    }

    public boolean enabledInBattle() {
        return true;
    }

    public final String getIdentifier() {
        return this.getName();
    }

    @Override
    public boolean shouldGenerateObject(final Maze maze, final int row,
            final int col, final int floor, final int level, final int layer) {
        if (layer == MazeConstants.LAYER_OBJECT) {
            // Handle object layer
            if (!this.isOfType(TypeConstants.TYPE_PASS_THROUGH)) {
                // Limit generation of other objects to 20%, unless required
                if (this.isRequired()) {
                    return true;
                } else {
                    final RandomRange r = new RandomRange(1, 100);
                    if (r.generate() <= 20) {
                        return true;
                    } else {
                        return false;
                    }
                }
            } else {
                // Generate pass-through objects at 100%
                return true;
            }
        } else {
            // Handle ground layer
            if (this.isOfType(TypeConstants.TYPE_FIELD)) {
                // Limit generation of fields to 20%
                final RandomRange r = new RandomRange(1, 100);
                if (r.generate() <= 20) {
                    return true;
                } else {
                    return false;
                }
            } else {
                // Generate other ground at 100%
                return true;
            }
        }
    }

    @Override
    public int getMinimumRequiredQuantity(final Maze maze) {
        return RandomGenerationRule.NO_LIMIT;
    }

    @Override
    public int getMaximumRequiredQuantity(final Maze maze) {
        return RandomGenerationRule.NO_LIMIT;
    }

    @Override
    public boolean isRequired() {
        return false;
    }

    public final void writeObject(final XDataWriter writer) throws IOException {
        writer.writeString(this.getIdentifier());
        if (this.saved == null) {
            writer.writeString("NULL");
        } else {
            this.saved.writeObject(writer);
        }
        final int cc = this.getCustomFormat();
        if (cc == AbstractMazeObject.CUSTOM_FORMAT_MANUAL_OVERRIDE) {
            this.writeObjectHook(writer);
        } else {
            for (int x = 0; x < cc; x++) {
                final int cx = this.getCustomProperty(x + 1);
                writer.writeInt(cx);
            }
        }
    }

    public final AbstractMazeObject readObject(final XDataReader reader,
            final String ident) throws IOException {
        if (ident.equals(this.getIdentifier())) {
            final String savedIdent = reader.readString();
            if (!savedIdent.equals("NULL")) {
                this.saved = FantastleReboot.getBagOStuff().getObjects()
                        .readSavedObject(reader, savedIdent,
                                FormatConstants.MAZE_FORMAT_LATEST);
            }
            final int cc = this.getCustomFormat();
            if (cc == AbstractMazeObject.CUSTOM_FORMAT_MANUAL_OVERRIDE) {
                return this.readObjectHook(reader,
                        FormatConstants.MAZE_FORMAT_LATEST);
            } else {
                for (int x = 0; x < cc; x++) {
                    final int cx = reader.readInt();
                    this.setCustomProperty(x + 1, cx);
                }
            }
            return this;
        } else {
            return null;
        }
    }

    /**
     *
     * @param writer
     * @throws IOException
     */
    protected void writeObjectHook(final XDataWriter writer)
            throws IOException {
        // Do nothing - but let subclasses override
    }

    /**
     *
     * @param reader
     * @param formatVersion
     * @return
     * @throws IOException
     */
    protected AbstractMazeObject readObjectHook(final XDataReader reader,
            final int formatVersion) throws IOException {
        // Dummy implementation, subclasses can override
        return this;
    }

    public boolean isMoving() {
        return false;
    }

    /**
     *
     * @param inv
     * @return
     */
    public boolean isConditionallySolid(final ObjectInventory inv) {
        return this.sp.isSolid();
    }

    /**
     *
     * @param ie
     * @param dirX
     * @param dirY
     * @param inv
     * @return
     */
    public boolean isConditionallyDirectionallySolid(final boolean ie,
            final int dirX, final int dirY, final ObjectInventory inv) {
        return this.sp.isDirectionallySolid(ie, dirX, dirY);
    }

    public boolean isSolid() {
        return this.sp.isSolid();
    }

    public boolean isDirectionallySolid(final boolean ie, final int dirX,
            final int dirY) {
        return this.sp.isDirectionallySolid(ie, dirX, dirY);
    }

    protected void setSolid(final boolean value) {
        this.sp.setSolid(value);
    }

    protected void setDirectionallySolid(final boolean ie, final int dir,
            final boolean value) {
        this.sp.setDirectionallySolid(ie, dir, value);
    }

    public boolean isOfType(final int testType) {
        return this.type.get(testType);
    }

    protected abstract void setTypes();

    /**
     *
     * @param inv
     * @return
     */
    public boolean isConditionallyPushable(final ObjectInventory inv) {
        return this.mp.isPushable();
    }

    /**
     *
     * @param dirX
     * @param dirY
     * @param inv
     * @return
     */
    public boolean isConditionallyDirectionallyPushable(final int dirX,
            final int dirY, final ObjectInventory inv) {
        return this.mp.isDirectionallyPushable(dirX, dirY);
    }

    /**
     *
     * @param inv
     * @return
     */
    public boolean isConditionallyPullable(final ObjectInventory inv) {
        return this.mp.isPullable();
    }

    /**
     *
     * @param dirX
     * @param dirY
     * @param inv
     * @return
     */
    public boolean isConditionallyDirectionallyPullable(final int dirX,
            final int dirY, final ObjectInventory inv) {
        return this.mp.isDirectionallyPullable(dirX, dirY);
    }

    /**
     *
     * @param inv
     * @return
     */
    public boolean isConditionallyPullableInto(final ObjectInventory inv) {
        return this.mp.isPullableInto();
    }

    /**
     *
     * @param inv
     * @return
     */
    public boolean isConditionallyPushableInto(final ObjectInventory inv) {
        return this.mp.isPushableInto();
    }

    /**
     *
     * @param dirX
     * @param dirY
     * @param inv
     * @return
     */
    public boolean isConditionallyDirectionallyPushableInto(final int dirX,
            final int dirY, final ObjectInventory inv) {
        return this.mp.isDirectionallyPushableInto(dirX, dirY);
    }

    /**
     *
     * @param dirX
     * @param dirY
     * @param inv
     * @return
     */
    public boolean isConditionallyDirectionallyPullableInto(final int dirX,
            final int dirY, final ObjectInventory inv) {
        return this.mp.isDirectionallyPullableInto(dirX, dirY);
    }

    /**
     *
     * @param inv
     * @return
     */
    public boolean isConditionallyPullableOut(final ObjectInventory inv) {
        return this.mp.isPullableOut();
    }

    /**
     *
     * @param inv
     * @return
     */
    public boolean isConditionallyPushableOut(final ObjectInventory inv) {
        return this.mp.isPushableOut();
    }

    /**
     *
     * @param dirX
     * @param dirY
     * @param inv
     * @return
     */
    public boolean isConditionallyDirectionallyPushableOut(final int dirX,
            final int dirY, final ObjectInventory inv) {
        return this.mp.isDirectionallyPushableOut(dirX, dirY);
    }

    /**
     *
     * @param dirX
     * @param dirY
     * @param inv
     * @return
     */
    public boolean isConditionallyDirectionallyPullableOut(final int dirX,
            final int dirY, final ObjectInventory inv) {
        return this.mp.isDirectionallyPullableOut(dirX, dirY);
    }

    public boolean isPushable() {
        return this.mp.isPushable();
    }

    public boolean isDirectionallyPushable(final int dirX, final int dirY) {
        return this.mp.isDirectionallyPushable(dirX, dirY);
    }

    public boolean isPullable() {
        return this.mp.isPullable();
    }

    public boolean isDirectionallyPullable(final int dirX, final int dirY) {
        return this.mp.isDirectionallyPullable(dirX, dirY);
    }

    public boolean isPullableInto() {
        return this.mp.isPullableInto();
    }

    public boolean isPushableInto() {
        return this.mp.isPushableInto();
    }

    public boolean isDirectionallyPushableInto(final int dirX, final int dirY) {
        return this.mp.isDirectionallyPushableInto(dirX, dirY);
    }

    public boolean isDirectionallyPullableInto(final int dirX, final int dirY) {
        return this.mp.isDirectionallyPullableInto(dirX, dirY);
    }

    public boolean isPullableOut() {
        return this.mp.isPullableOut();
    }

    public boolean isPushableOut() {
        return this.mp.isPushableOut();
    }

    public boolean isDirectionallyPushableOut(final int dirX, final int dirY) {
        return this.mp.isDirectionallyPushableOut(dirX, dirY);
    }

    public boolean isDirectionallyPullableOut(final int dirX, final int dirY) {
        return this.mp.isDirectionallyPullableOut(dirX, dirY);
    }

    protected void setPushable(final boolean value) {
        this.mp.setPushable(value);
    }

    protected void setDirectionallyPushable(final int dir,
            final boolean value) {
        this.mp.setDirectionallyPushable(dir, value);
    }

    protected void setPullable(final boolean value) {
        this.mp.setPullable(value);
    }

    protected void setDirectionallyPullable(final int dir,
            final boolean value) {
        this.mp.setDirectionallyPullable(dir, value);
    }

    protected void setPushableInto(final boolean value) {
        this.mp.setPushableInto(value);
    }

    protected void setDirectionallyPushableInto(final int dir,
            final boolean value) {
        this.mp.setDirectionallyPushableInto(dir, value);
    }

    protected void setPullableInto(final boolean value) {
        this.mp.setPullableInto(value);
    }

    protected void setDirectionallyPullableInto(final int dir,
            final boolean value) {
        this.mp.setDirectionallyPullableInto(dir, value);
    }

    protected void setPushableOut(final boolean value) {
        this.mp.setPushableOut(value);
    }

    protected void setDirectionallyPushableOut(final int dir,
            final boolean value) {
        this.mp.setDirectionallyPushableOut(dir, value);
    }

    protected void setPullableOut(final boolean value) {
        this.mp.setPullableOut(value);
    }

    protected void setDirectionallyPullableOut(final int dir,
            final boolean value) {
        this.mp.setDirectionallyPullableOut(dir, value);
    }

    public boolean hasFriction() {
        return this.friction;
    }

    public boolean isUsable() {
        return this.usable;
    }

    public int getUses() {
        return this.uses;
    }

    public boolean isDestroyable() {
        return this.destroyable;
    }

    public boolean doesChainReact() {
        return this.chainReacts;
    }

    public boolean isInventoryable() {
        return this.isInventoryable;
    }

    /**
     *
     * @param ie
     * @param dirX
     * @param dirY
     * @param inv
     * @return
     */
    public boolean preMoveAction(final boolean ie, final int dirX,
            final int dirY, final ObjectInventory inv) {
        return true;
    }

    /**
     *
     * @param ie
     * @param dirX
     * @param dirY
     * @param inv
     */
    public void postMoveAction(final boolean ie, final int dirX, final int dirY,
            final ObjectInventory inv) {
        // Play move success sound, if it's enabled
        if (FantastleReboot.getBagOStuff().getPrefsManager()
                .getSoundEnabled(PreferencesManager.SOUNDS_GAME)) {
            this.playMoveSuccessSound();
        }
    }

    /**
     *
     * @param ie
     * @param dirX
     * @param dirY
     * @param inv
     */
    public void moveFailedAction(final boolean ie, final int dirX,
            final int dirY, final ObjectInventory inv) {
        // Play move failed sound, if it's enabled
        if (FantastleReboot.getBagOStuff().getPrefsManager()
                .getSoundEnabled(PreferencesManager.SOUNDS_GAME)) {
            this.playMoveFailedSound();
        }
        Messager.showMessage("Can't go that way");
    }

    /**
     *
     * @param inv
     * @param moving
     * @return
     */
    public boolean hasFrictionConditionally(final ObjectInventory inv,
            final boolean moving) {
        return this.hasFriction();
    }

    public void gameProbeHook() {
        Messager.showMessage(this.getName());
    }

    public void editorPlaceHook() {
        // Do nothing
    }

    public void editorProbeHook() {
        Messager.showMessage(this.getName());
    }

    public AbstractMazeObject editorPropertiesHook() {
        return null;
    }

    public void playMoveFailedSound() {
        SoundPlayer.playSound(GameSound.OOF);
    }

    public void playMoveSuccessSound() {
        SoundPlayer.playSound(GameSound.WALK);
    }

    public final static void playPushSuccessSound() {
        SoundPlayer.playSound(GameSound.PUSH);
    }

    public final static void playPushFailedSound() {
        SoundPlayer.playSound(GameSound.PUSH_PULL_FAILED);
    }

    public final static void playPullFailedSound() {
        SoundPlayer.playSound(GameSound.PUSH_PULL_FAILED);
    }

    public final static void playPullSuccessSound() {
        SoundPlayer.playSound(GameSound.PULL);
    }

    public void playUseSound() {
        // Do nothing
    }

    public void playChainReactSound() {
        // Do nothing
    }

    public final static void playIdentifySound() {
        SoundPlayer.playSound(GameSound.IDENTIFY);
    }

    public final static void playRotatedSound() {
        SoundPlayer.playSound(GameSound.ROTATED);
    }

    public final static void playFallSound() {
        SoundPlayer.playSound(GameSound.FALLING);
    }

    public final static void playButtonSound() {
        SoundPlayer.playSound(GameSound.BUTTON);
    }

    public final static void playConfusedSound() {
        SoundPlayer.playSound(GameSound.CONFUSED);
    }

    public final static void playDarknessSound() {
        SoundPlayer.playSound(GameSound.DARKNESS);
    }

    public final static void playDizzySound() {
        SoundPlayer.playSound(GameSound.DIZZY);
    }

    public final static void playDrunkSound() {
        SoundPlayer.playSound(GameSound.DRUNK);
    }

    public final static void playFinishSound() {
        SoundPlayer.playSound(GameSound.FINISH);
    }

    public final static void playLightSound() {
        SoundPlayer.playSound(GameSound.LIGHT);
    }

    public final static void playSinkBlockSound() {
        SoundPlayer.playSound(GameSound.SINK);
    }

    public final static void playWallTrapSound() {
        SoundPlayer.playSound(GameSound.TRAP);
    }

    /**
     *
     * @param inv
     * @param mo
     * @param x
     * @param y
     * @param pushX
     * @param pushY
     */
    public void pushAction(final ObjectInventory inv,
            final AbstractMazeObject mo, final int x, final int y,
            final int pushX, final int pushY) {
        // Do nothing
    }

    /**
     *
     * @param inv
     * @param pushed
     * @param x
     * @param y
     * @param z
     * @param w
     */
    public void pushIntoAction(final ObjectInventory inv,
            final AbstractMazeObject pushed, final int x, final int y,
            final int z, final int w) {
        // Do nothing
    }

    /**
     *
     * @param inv
     * @param pushed
     * @param x
     * @param y
     * @param z
     * @param w
     */
    public void pushOutAction(final ObjectInventory inv,
            final AbstractMazeObject pushed, final int x, final int y,
            final int z, final int w) {
        // Do nothing
    }

    /**
     *
     * @param inv
     * @param x
     * @param y
     * @param pushX
     * @param pushY
     */
    public void pushFailedAction(final ObjectInventory inv, final int x,
            final int y, final int pushX, final int pushY) {
        // Play push failed sound, if it's enabled
        if (FantastleReboot.getBagOStuff().getPrefsManager()
                .getSoundEnabled(PreferencesManager.SOUNDS_GAME)) {
            AbstractMazeObject.playPushFailedSound();
        }
        FantastleReboot.getBagOStuff().getGameManager().keepNextMessage();
        Messager.showMessage("Can't push that");
    }

    /**
     *
     * @param inv
     * @param mo
     * @param x
     * @param y
     * @param pullX
     * @param pullY
     */
    public void pullAction(final ObjectInventory inv,
            final AbstractMazeObject mo, final int x, final int y,
            final int pullX, final int pullY) {
        // Do nothing
    }

    /**
     *
     * @param inv
     * @param pulled
     * @param x
     * @param y
     * @param z
     * @param w
     */
    public void pullIntoAction(final ObjectInventory inv,
            final AbstractMazeObject pulled, final int x, final int y,
            final int z, final int w) {
        // Do nothing
    }

    /**
     *
     * @param inv
     * @param pulled
     * @param x
     * @param y
     * @param z
     * @param w
     */
    public void pullOutAction(final ObjectInventory inv,
            final AbstractMazeObject pulled, final int x, final int y,
            final int z, final int w) {
        // Do nothing
    }

    /**
     *
     * @param inv
     * @param x
     * @param y
     * @param pullX
     * @param pullY
     */
    public void pullFailedAction(final ObjectInventory inv, final int x,
            final int y, final int pullX, final int pullY) {
        // Play pull failed sound, if it's enabled
        if (FantastleReboot.getBagOStuff().getPrefsManager()
                .getSoundEnabled(PreferencesManager.SOUNDS_GAME)) {
            AbstractMazeObject.playPullFailedSound();
        }
        FantastleReboot.getBagOStuff().getGameManager().keepNextMessage();
        Messager.showMessage("Can't pull that");
    }

    /**
     *
     * @param mo
     * @param x
     * @param y
     * @param z
     * @param w
     */
    public void useAction(final AbstractMazeObject mo, final int x, final int y,
            final int z, final int w) {
        // Do nothing
    }

    /**
     *
     * @param x
     * @param y
     * @param z
     * @param w
     */
    public void useHelper(final int x, final int y, final int z, final int w) {
        // Do nothing
    }

    public final boolean isTimerActive() {
        return this.timerActive;
    }

    public final void extendTimer(final int ticks) {
        if (this.timerActive) {
            this.timerValue += ticks;
        }
    }

    public final void extendTimerByInitialValue() {
        if (this.timerActive) {
            this.timerValue += this.initialTimerValue;
        }
    }

    /**
     *
     * @param dirX
     * @param dirY
     */
    public void timerExpiredAction(final int dirX, final int dirY) {
        // Do nothing
    }

    /**
     *
     * @param locX
     * @param locY
     * @param locZ
     * @param locW
     * @param dirX
     * @param dirY
     * @param arrowType
     * @param inv
     * @return
     */
    public boolean arrowHitAction(final int locX, final int locY,
            final int locZ, final int locW, final int dirX, final int dirY,
            final int arrowType, final ObjectInventory inv) {
        // Default do-nothing, return true
        return true;
    }

    /**
     *
     * @param x
     * @param y
     * @param z
     * @param w
     * @return
     */
    public String gameRenderHook(final int x, final int y, final int z,
            final int w) {
        return this.getGameName();
    }

    /**
     *
     * @param x
     * @param y
     * @param z
     * @param w
     * @return
     */
    public String editorRenderHook(final int x, final int y, final int z,
            final int w) {
        return this.getName();
    }

    /**
     *
     * @param x
     * @param y
     * @param z
     * @param w
     */
    public void chainReactionAction(final int x, final int y, final int z,
            final int w) {
        // Do nothing
    }

    public boolean defersSetProperties() {
        return false;
    }

    public boolean hasAdditionalProperties() {
        return false;
    }

    public boolean overridesDefaultPostMove() {
        return false;
    }

    public String getGameName() {
        return this.getName();
    }

    /**
     *
     * @param x
     * @param y
     * @param z
     * @param w
     */
    public void determineCurrentAppearance(final int x, final int y,
            final int z, final int w) {
        // Do nothing
    }

    public void stepAction() {
        // Do nothing
    }

    abstract public String getName();

    public String getIdentifier4() {
        return this.getName();
    }

    abstract public String getPluralName();

    abstract public String getDescription();

    abstract public byte getGroupID();

    abstract public byte getObjectID();

    abstract public int getLayer();

    abstract public int getCustomProperty(int propID);

    abstract public void setCustomProperty(int propID, int value);

    public int getCustomFormat() {
        return 0;
    }
}
