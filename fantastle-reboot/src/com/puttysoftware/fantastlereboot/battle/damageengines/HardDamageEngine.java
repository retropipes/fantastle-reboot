/*  FantastleReboot: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.battle.damageengines;

import com.puttysoftware.fantastlereboot.creatures.Creature;
import com.puttysoftware.fantastlereboot.creatures.StatConstants;
import com.puttysoftware.fantastlereboot.creatures.faiths.FaithManager;
import com.puttysoftware.fantastlereboot.items.Equipment;
import com.puttysoftware.fantastlereboot.items.EquipmentCategoryConstants;
import com.puttysoftware.fantastlereboot.items.EquipmentSlotConstants;
import com.puttysoftware.randomrange.RandomRange;

class HardDamageEngine extends AbstractDamageEngine {
  private static final int MULTIPLIER_MIN = 7000;
  private static final int MULTIPLIER_MAX = 12500;
  private static final int MULTIPLIER_MIN_CRIT = 15000;
  private static final int MULTIPLIER_MAX_CRIT = 25000;
  private static final int FUMBLE_CHANCE = 750;
  private static final int PIERCE_CHANCE = 750;
  private static final int CRIT_CHANCE = 750;
  private static final double FAITH_INCREMENT = 0.08;
  private static final double FAITH_INCREMENT_2H = 0.12;
  private static final double FAITH_DR_INCREMENT = 0.04;
  private double lastMultiplier = 1.0;
  private boolean dodged = false;
  private boolean missed = false;
  private boolean crit = false;
  private boolean pierce = false;
  private boolean fumble = false;

  @Override
  public int computeDamage(final Creature enemy, final Creature acting) {
    // Compute Damage
    final double attack = acting.getEffectedAttack();
    final double defense = enemy.getEffectedStat(StatConstants.STAT_DEFENSE);
    final int power = acting.getItems().getTotalPower();
    this.didFumble();
    if (this.fumble) {
      // Fumble!
      return CommonDamageEngineParts.fumbleDamage(power);
    } else {
      this.didPierce();
      this.didCrit();
      double rawDamage;
      if (this.pierce) {
        rawDamage = attack;
      } else {
        rawDamage = attack - defense;
      }
      final int rHit = CommonDamageEngineParts.chance();
      int aHit = acting.getHit();
      if (this.crit || this.pierce) {
        // Critical hits and piercing hits
        // always connect
        aHit = CommonDamageEngineParts.ALWAYS;
      }
      if (rHit > aHit) {
        // Weapon missed
        this.missed = true;
        this.dodged = false;
        this.crit = false;
        return 0;
      } else {
        final int rEvade = CommonDamageEngineParts.chance();
        final int aEvade = enemy.getEvade();
        if (rEvade < aEvade) {
          // Enemy dodged
          this.missed = false;
          this.dodged = true;
          this.crit = false;
          return 0;
        } else {
          // Hit
          this.missed = false;
          this.dodged = false;
          RandomRange rDamage;
          if (this.crit) {
            rDamage = new RandomRange(HardDamageEngine.MULTIPLIER_MIN_CRIT,
                HardDamageEngine.MULTIPLIER_MAX_CRIT);
          } else {
            rDamage = new RandomRange(HardDamageEngine.MULTIPLIER_MIN,
                HardDamageEngine.MULTIPLIER_MAX);
          }
          final int multiplier = rDamage.generate();
          // Weapon Faith Power Boost
          double faithMultiplier = CommonDamageEngineParts.FAITH_MULT_START;
          final int fc = FaithManager.FAITHS;
          final Equipment mainHand = acting.getItems()
              .getEquipmentInSlot(EquipmentSlotConstants.SLOT_MAINHAND);
          final Equipment offHand = acting.getItems()
              .getEquipmentInSlot(EquipmentSlotConstants.SLOT_OFFHAND);
          if (mainHand != null && mainHand.equals(offHand)) {
            for (int z = 0; z < fc; z++) {
              final int fpl = mainHand.getFaithPowerLevel(z);
              faithMultiplier += HardDamageEngine.FAITH_INCREMENT_2H * fpl;
            }
          } else {
            if (mainHand != null) {
              for (int z = 0; z < fc; z++) {
                final int fpl = mainHand.getFaithPowerLevel(z);
                faithMultiplier += HardDamageEngine.FAITH_INCREMENT * fpl;
              }
            }
            if (offHand != null && offHand
                .getEquipCategory() == EquipmentCategoryConstants.EQUIPMENT_CATEGORY_ONE_HANDED_WEAPON) {
              for (int z = 0; z < fc; z++) {
                final int fpl = offHand.getFaithPowerLevel(z);
                faithMultiplier += HardDamageEngine.FAITH_INCREMENT * fpl;
              }
            }
          }
          // Armor Faith Power Boost
          double faithDR = CommonDamageEngineParts.FAITH_MULT_START;
          final Equipment armor = acting.getItems()
              .getEquipmentInSlot(EquipmentSlotConstants.SLOT_BODY);
          if (armor != null) {
            for (int z = 0; z < fc; z++) {
              final int fpl = armor.getFaithPowerLevel(z);
              faithDR -= fpl * HardDamageEngine.FAITH_DR_INCREMENT;
            }
          }
          if (offHand != null && offHand
              .getEquipCategory() == EquipmentCategoryConstants.EQUIPMENT_CATEGORY_ARMOR) {
            for (int z = 0; z < fc; z++) {
              final int fpl = offHand.getFaithPowerLevel(z);
              faithDR -= fpl * HardDamageEngine.FAITH_DR_INCREMENT;
            }
          }
          final double unadjustedDamage = (rawDamage * multiplier
              * faithMultiplier / CommonDamageEngineParts.MULTIPLIER_DIVIDE);
          this.lastMultiplier = MultiplierValues.getRandomNormalValue();
          final int adjustedDamage = (int) (unadjustedDamage * faithDR * this.lastMultiplier);
          return acting.getFaith().getFaithAdjustedDamage(enemy.getFaith().getFaithID(), adjustedDamage);
        }
      }
    }
  }
  
  @Override
  public String getDamageString() {
    return MultiplierValues.getTextForValue(this.lastMultiplier);
  }

  @Override
  public boolean enemyDodged() {
    return this.dodged;
  }

  @Override
  public boolean weaponMissed() {
    return this.missed;
  }

  @Override
  public boolean weaponCrit() {
    return this.crit;
  }

  @Override
  public boolean weaponPierce() {
    return this.pierce;
  }

  @Override
  public boolean weaponFumble() {
    return this.fumble;
  }

  private void didPierce() {
    this.pierce = CommonDamageEngineParts
        .didSpecial(HardDamageEngine.PIERCE_CHANCE);
  }

  private void didCrit() {
    this.crit = CommonDamageEngineParts
        .didSpecial(HardDamageEngine.CRIT_CHANCE);
  }

  private void didFumble() {
    this.fumble = CommonDamageEngineParts
        .didSpecial(HardDamageEngine.FUMBLE_CHANCE);
  }
}