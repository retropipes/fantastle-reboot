/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.fantastlereboot.objectmodel;

import org.retropipes.diane.asset.image.BufferedImageIcon;
import org.retropipes.diane.direction.DirectionQuery;

public interface ObjectModel {
    BufferedImageIcon getBattleImage();

    BufferedImageIcon getEditorImage();

    BufferedImageIcon getGameImage();

    BufferedImageIcon getImage();

    int getTimerTicks();

    int getUniqueID();

    int getUses();

    boolean hasFriction();

    boolean isCarryable();

    boolean isChainReacting();

    boolean isChainReactingHorizontally();

    boolean isChainReactingVertically();

    boolean isDestroyable();

    boolean isDirectionallyPullable(DirectionQuery dir);

    boolean isDirectionallyPullableInto(DirectionQuery dir);

    boolean isDirectionallyPullableOut(DirectionQuery dir);

    boolean isDirectionallyPushable(DirectionQuery dir);

    boolean isDirectionallyPushableInto(DirectionQuery dir);

    boolean isDirectionallyPushableOut(DirectionQuery dir);

    boolean isDirectionallySightBlocking(DirectionQuery dir);

    boolean isDirectionallySolid(DirectionQuery dir);

    boolean isInternallyDirectionallySightBlocking(DirectionQuery dir);

    boolean isInternallyDirectionallySolid(DirectionQuery dir);

    boolean isPullable();

    boolean isPullableInto();

    boolean isPullableOut();

    boolean isPushable();

    boolean isPushableInto();

    boolean isPushableOut();

    boolean isSightBlocking();

    boolean isSolid();

    boolean isUsable();

    void resetTimer();

    void tickTimer();

    void use();
}