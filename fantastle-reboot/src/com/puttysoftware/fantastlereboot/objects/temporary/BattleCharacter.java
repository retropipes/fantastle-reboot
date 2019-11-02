package com.puttysoftware.fantastlereboot.objects.temporary;

import com.puttysoftware.fantastlereboot.creatures.Creature;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;
import com.puttysoftware.fantastlereboot.objects.OpenSpace;
import com.puttysoftware.images.BufferedImageIcon;

public final class BattleCharacter extends FantastleObject {
  // Constants
  private static final int FLAG_ACTIVE = 0;
  private static final int COUNTER_ACTIONS = 0;
  // Properties
  private final Creature creature;

  public BattleCharacter(final Creature theCreature) {
    super(-1);
    this.setSavedObject(new OpenSpace());
    this.creature = theCreature;
    this.addOneCustomFlag();
    this.addOneCustomCounter();
    this.activate();
    this.resetActions();
  }

  public int getX() {
    return this.creature.getX();
  }

  public int getY() {
    return this.creature.getY();
  }

  public void setX(final int newX) {
    this.creature.setX(newX);
  }

  public void setY(final int newY) {
    this.creature.setY(newY);
  }

  public void offsetX(final int newX) {
    this.creature.offsetX(newX);
  }

  public void offsetY(final int newY) {
    this.creature.offsetY(newY);
  }

  public void saveLocation() {
    this.creature.saveLocation();
  }

  public void restoreLocation() {
    this.creature.restoreLocation();
  }

  public void resetLocation() {
    this.creature.setX(-1);
    this.creature.setY(-1);
  }

  public Creature getCreature() {
    return this.creature;
  }

  public int getTeamID() {
    return this.creature.getTeamID();
  }

  public final String getTeamString() {
    if (this.getTeamID() == 0) {
      return "Team: Party";
    } else {
      return "Team: Enemies " + this.getTeamID();
    }
  }

  public boolean isActive() {
    return this.getCustomFlag(FLAG_ACTIVE).get();
  }

  public void deactivate() {
    this.setCustomFlag(FLAG_ACTIVE, false);
  }

  public void activate() {
    this.setCustomFlag(FLAG_ACTIVE, true);
  }

  public void resetActions() {
    this.setCustomCounter(COUNTER_ACTIONS,
        this.creature.getMapBattleActionsPerRound());
  }

  public void act(final int cost) {
    if (this.getCustomCounter(COUNTER_ACTIONS).get() > 0) {
      this.offsetCustomCounter(COUNTER_ACTIONS, -cost);
      if (this.getCustomCounter(COUNTER_ACTIONS).get() < 0) {
        this.setCustomCounter(COUNTER_ACTIONS, 0);
      }
    }
  }

  public void actExact(final int cost) {
    if (this.getCustomCounter(COUNTER_ACTIONS).get() >= cost) {
      this.offsetCustomCounter(COUNTER_ACTIONS, -cost);
    }
  }

  public boolean canAct(final int cost) {
    return this.getCustomCounter(COUNTER_ACTIONS).get() > 0;
  }

  public boolean canActExact(final int cost) {
    return this.getCustomCounter(COUNTER_ACTIONS).get() >= cost;
  }

  public int getCurrentActions() {
    return this.getCustomCounter(COUNTER_ACTIONS).get();
  }

  public final String getActionString() {
    return "Actions Left: " + this.getCurrentActions();
  }

  @Override
  public BufferedImageIcon getImageHook() {
    return this.creature.getImage();
  }

  @Override
  public BufferedImageIcon getGameImageHook() {
    return this.creature.getImage();
  }

  @Override
  public BufferedImageIcon getEditorImageHook() {
    return this.creature.getImage();
  }

  @Override
  public BufferedImageIcon getBattleImageHook() {
    return this.creature.getImage();
  }

  @Override
  public String getName() {
    return this.creature.getName();
  }
}
