package com.puttysoftware.fantastlereboot.objects.temporary;

import com.puttysoftware.fantastlereboot.ai.map.MapAIContext;
import com.puttysoftware.fantastlereboot.creatures.Creature;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;
import com.puttysoftware.fantastlereboot.objects.OpenSpace;
import com.puttysoftware.images.BufferedImageIcon;

public final class BattleCharacter extends FantastleObject {
  // Constants
  private static final int FLAG_ACTIVE = 0;
  private static final int FLAG_MARKED = 1;
  private static final int COUNTER_ACTIONS = 0;
  // Properties
  private final Creature creature;
  private MapAIContext aiContext;

  public BattleCharacter(final Creature theCreature) {
    super(-1);
    this.setSavedObject(new OpenSpace());
    this.setSolid(true);
    this.creature = theCreature;
    this.addCustomFlag(2);
    this.addOneCustomCounter();
    this.activate();
    this.unmark();
  }

  public boolean isLocationSet() {
    return this.creature.getX() != -1 && this.creature.getY() != -1;
  }

  public MapAIContext getAIContext() {
    return this.aiContext;
  }

  public void setAIContext(final MapAIContext theAIContext) {
    this.aiContext = theAIContext;
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

  public String getTeamString() {
    if (this.getTeamID() == 0) {
      return "Team: Party";
    } else {
      return "Team: Enemies " + this.getTeamID();
    }
  }

  public boolean isActive() {
    return this.getCustomFlag(BattleCharacter.FLAG_ACTIVE);
  }

  public void deactivate() {
    this.setCustomFlag(BattleCharacter.FLAG_ACTIVE, false);
  }

  public void activate() {
    this.setCustomFlag(BattleCharacter.FLAG_ACTIVE, true);
  }

  public boolean isMarked() {
    return this.getCustomFlag(BattleCharacter.FLAG_MARKED);
  }

  public void unmark() {
    this.setCustomFlag(BattleCharacter.FLAG_MARKED, false);
  }

  public void mark() {
    this.setCustomFlag(BattleCharacter.FLAG_MARKED, true);
  }

  public void resetActions() {
    int maxActions = this.creature.getMapBattleActionsPerRound();
    this.setCustomCounter(BattleCharacter.COUNTER_ACTIONS, maxActions);
  }

  public void act(final int cost) {
    if (this.getCustomCounter(BattleCharacter.COUNTER_ACTIONS) > 0) {
      this.offsetCustomCounter(BattleCharacter.COUNTER_ACTIONS, -cost);
      if (this.getCustomCounter(BattleCharacter.COUNTER_ACTIONS) < 0) {
        this.setCustomCounter(BattleCharacter.COUNTER_ACTIONS, 0);
      }
    }
  }

  public void actExact(final int cost) {
    if (this.getCustomCounter(BattleCharacter.COUNTER_ACTIONS) >= cost) {
      this.offsetCustomCounter(BattleCharacter.COUNTER_ACTIONS, -cost);
    }
  }

  public boolean canAct(final int cost) {
    return this.getCustomCounter(BattleCharacter.COUNTER_ACTIONS) > 0;
  }

  public boolean canActExact(final int cost) {
    return this.getCustomCounter(BattleCharacter.COUNTER_ACTIONS) >= cost;
  }

  public int getCurrentActions() {
    return this.getCustomCounter(BattleCharacter.COUNTER_ACTIONS);
  }

  public String getActionString() {
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
