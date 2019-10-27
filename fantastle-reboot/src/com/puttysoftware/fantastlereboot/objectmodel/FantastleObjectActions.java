package com.puttysoftware.fantastlereboot.objectmodel;

import java.nio.LongBuffer;
import java.util.BitSet;

public class FantastleObjectActions {
  public static final int DISAPPEAR = 0;
  public static final int ADD_INVENTORY = 1;
  public static final int REMOVE_INVENTORY = 2;
  public static final int SOUND = 3;
  public static final int EFFECT = 4;
  public static final int BATTLE = 5;
  public static final int CHANGE_FORM_ID_1 = 6;
  public static final int CHANGE_FORM_ID_2 = 7;
  public static final int CHANGE_FORM_TYPE_1 = 8;
  public static final int CHANGE_FORM_TYPE_2 = 9;
  public static final int UP_1_FLOOR = 10;
  public static final int DOWN_1_FLOOR = 11;
  public static final int NEXT_LEVEL = 12;
  public static final int PREVIOUS_LEVEL = 13;
  public static final int SHOP = 14;
  public static final int PUSH = 15;
  public static final int PULL = 16;
  public static final int CHANGE_PUSHED = 17;
  public static final int REPLACE_SELF = 18;
  public static final int SPAWN_MANY = 19;
  public static final int MODIFY_TIMER = 20;
  public static final int UP_2_FLOORS = 21;
  public static final int DOWN_2_FLOORS = 22;
  public static final int STOP_MOVING_ID = 23;
  public static final int STOP_MOVING_TYPE = 24;
  public static final int DISPEL_EFFECTS = 25;
  public static final int MOVE_SELF = 26;
  public static final int TELEPORT = 27;
  public static final int TELEPORT_PUSHED = 28;
  public static final int TELEPORT_RANDOM = 29;
  public static final int PASS_IF_HAVE_INVENTORY = 30;
  private final BitSet bits;

  public FantastleObjectActions(final long... val) {
    super();
    this.bits = BitSet.valueOf(LongBuffer.wrap(val));
  }

  public boolean get(final int val) {
    return this.bits.get(val);
  }
}
