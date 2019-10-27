package com.puttysoftware.fantastlereboot.items;

import javax.swing.JOptionPane;

import com.puttysoftware.commondialogs.CommonDialogs;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.Messager;
import com.puttysoftware.fantastlereboot.PreferencesManager;
import com.puttysoftware.fantastlereboot.assets.MusicIndex;
import com.puttysoftware.fantastlereboot.assets.SoundIndex;
import com.puttysoftware.fantastlereboot.creatures.party.PartyManager;
import com.puttysoftware.fantastlereboot.creatures.party.PartyMember;
import com.puttysoftware.fantastlereboot.items.combat.CombatItemList;
import com.puttysoftware.fantastlereboot.loaders.MusicPlayer;
import com.puttysoftware.fantastlereboot.loaders.SoundPlayer;

public class Shop {
  // Fields
  private final int type;
  private int index;
  private int defaultChoice;
  private String[] choices;
  private String[] typeChoices;
  private int typeDefault;
  private String typeResult;
  private int typeIndex;
  private String[] handChoices;
  private int handDefault;
  private String handResult;
  private boolean handIndex;
  private String result;
  private int cost;
  private Item item;
  private final CombatItemList itemList;
  private final int inflationRate;
  private boolean twoHanded;
  private static final int MAX_ENHANCEMENTS = 9;

  // Constructors
  public Shop(final int shopType) {
    this.type = shopType;
    this.inflationRate = 100;
    this.itemList = FantastleReboot.getBagOStuff().getCombatItems();
    this.index = 0;
  }

  public static int getEquipmentCost(final int x) {
    return 2 * x * x * x + 16 * x * x + 30 * x;
  }

  private String getGoldTotals() {
    final PartyMember playerCharacter = PartyManager.getParty().getLeader();
    if (this.type == ShopTypes.BANK) {
      return "Gold on Hand: " + playerCharacter.getGold() + "\nGold in Bank: "
          + PartyManager.getGoldInBank() + "\n";
    } else {
      return "";
    }
  }

  public static int getHealingCost(final int x, final int y, final int z,
      final int k) {
    return (int) (Math.log10(x) * (z - y + Math.log10((double) k + 1)));
  }

  public static int getRegenerationCost(final int x, final int y, final int z,
      final int k) {
    final int diff = z - y;
    if (diff == 0) {
      return 0;
    } else {
      final int cost = (int) (Math.log(x) / Math.log(2)
          * (z - y + Math.log((double) k + 1) / Math.log(2)));
      if (cost < 1) {
        return 1;
      } else {
        return cost;
      }
    }
  }

  private static int getSpellCost(final int i, final int k) {
    if (i == -1) {
      return 0;
    } else {
      return (int) (20 * i * i + 20 + Math.log((double) k + 1) / Math.log(2));
    }
  }

  private static int getEnhancementCost(final int i, final int x) {
    if (i > Shop.MAX_ENHANCEMENTS) {
      return 0;
    }
    final int cost = 15 * i * i + 15 * i
        + (int) (Math.sqrt(Shop.getEquipmentCost(x)));
    if (cost < 0) {
      return 0;
    } else {
      return cost;
    }
  }

  private String getShopNameFromType() {
    switch (this.type) {
    case ShopTypes.WEAPONS:
      return "Weapons";
    case ShopTypes.ARMOR:
      return "Armor";
    case ShopTypes.HEALER:
      return "Healer";
    case ShopTypes.BANK:
      return "Bank";
    case ShopTypes.REGENERATOR:
      return "Regenerator";
    case ShopTypes.SPELLS:
      return "Spells";
    case ShopTypes.ITEMS:
      return "Items";
    default:
      return null;
    }
  }

  public void showShop() {
    this.index = 0;
    this.defaultChoice = 0;
    this.choices = null;
    this.typeChoices = null;
    this.typeDefault = 0;
    this.typeResult = null;
    this.typeIndex = 0;
    this.handChoices = null;
    this.handDefault = 0;
    this.handResult = null;
    this.handIndex = false;
    this.result = null;
    this.cost = 0;
    boolean valid = this.shopStage1();
    if (valid) {
      valid = this.shopStage2();
      if (valid) {
        valid = this.shopStage3();
        if (valid) {
          valid = this.shopStage4();
          if (valid) {
            valid = this.shopStage5();
            if (valid) {
              this.shopStage6();
            }
          }
        }
      }
    }
    MusicPlayer.playMusic(MusicIndex.DUNGEON);
  }

  private boolean shopStage1() {
    // Stage 1
    // Play enter shop sound
    if (FantastleReboot.getBagOStuff().getPrefsManager()
        .getSoundEnabled(PreferencesManager.SOUNDS_SHOP)) {
      SoundPlayer.playSound(SoundIndex.SHOP);
      MusicPlayer.playMusic(MusicIndex.SHOP);
    }
    if (this.type == ShopTypes.WEAPONS) {
      this.typeChoices = WeaponConstants.getWeaponChoices();
      this.typeDefault = 0;
    } else if (this.type == ShopTypes.ARMOR) {
      this.typeChoices = ArmorConstants.getArmorChoices();
      this.typeDefault = 0;
    } else if (this.type == ShopTypes.FAITH_POWERS) {
      this.typeIndex = PartyManager.getParty().getLeader().getFaith()
          .getFaithID();
    }
    if (this.typeChoices != null) {
      this.typeResult = Messager.showInputDialog(
          this.getGoldTotals() + "Select Type", this.getShopNameFromType(),
          this.typeChoices, this.typeChoices[this.typeDefault]);
      if (this.typeResult == null) {
        return false;
      }
      this.typeIndex = 0;
      for (this.typeIndex = 0; this.typeIndex < this.typeChoices.length; this.typeIndex++) {
        if (this.typeResult.equals(this.typeChoices[this.typeIndex])) {
          break;
        }
      }
      if (this.typeIndex == this.typeChoices.length) {
        return false;
      }
      if (this.type == ShopTypes.ARMOR && this.typeIndex > 1) {
        // Adjust typeIndex, position 2 is blank
        this.typeIndex++;
      }
    }
    return true;
  }

  private boolean shopStage2() {
    // Stage 2
    final PartyMember playerCharacter = PartyManager.getParty().getLeader();
    if (this.type == ShopTypes.WEAPONS) {
      if (this.typeResult.equals(this.typeChoices[0])) {
        this.choices = EquipmentFactory.createOneHandedWeaponNames(
            playerCharacter.getCaste().getCasteID());
        // Choose Hand
        this.handChoices = WeaponConstants.getHandChoices();
        this.handDefault = 0;
        this.handResult = Messager.showInputDialog(
            this.getGoldTotals() + "Select Hand", this.getShopNameFromType(),
            this.handChoices, this.handChoices[this.handDefault]);
        if (this.handResult == null) {
          return false;
        }
        if (this.handResult.equals(this.handChoices[0])) {
          this.handIndex = true;
        } else {
          this.handIndex = false;
        }
      } else {
        this.choices = EquipmentFactory.createTwoHandedWeaponNames(
            playerCharacter.getCaste().getCasteID());
      }
    } else if (this.type == ShopTypes.ARMOR) {
      this.choices = EquipmentFactory.createArmorNames(this.typeIndex);
    } else if (this.type == ShopTypes.HEALER
        || this.type == ShopTypes.REGENERATOR) {
      this.choices = new String[10];
      int x;
      for (x = 0; x < this.choices.length; x++) {
        this.choices[x] = Integer.toString((x + 1) * 10) + "%";
      }
      this.defaultChoice = 9;
    } else if (this.type == ShopTypes.BANK) {
      this.choices = new String[2];
      this.choices[0] = "Deposit";
      this.choices[1] = "Withdraw";
    } else if (this.type == ShopTypes.SPELLS) {
      this.choices = playerCharacter.getSpellBook().getAllSpellsToLearnNames();
      if (this.choices == null) {
        this.choices = new String[1];
        this.choices[0] = "No Spells Left To Learn";
      }
    } else if (this.type == ShopTypes.ITEMS) {
      this.choices = this.itemList.getAllNames();
      if (this.choices == null) {
        this.choices = new String[1];
        this.choices[0] = "No Items To Buy";
        this.index = -1;
      } else if (this.choices.length == 0) {
        this.choices = new String[1];
        this.choices[0] = "No Items To Buy";
        this.index = -1;
      }
    } else if (this.type == ShopTypes.SOCKS) {
      this.choices = EquipmentFactory.createSocksNames();
      if (this.choices.length == 0) {
        this.choices = new String[1];
        this.choices[0] = "No Socks To Buy";
        this.index = -1;
      }
    } else if (this.type == ShopTypes.ENHANCEMENTS) {
      this.choices = PartyManager.getParty().getLeader().getItems()
          .generateEquipmentEnhancementStringArray();
      this.index = 0;
    } else if (this.type == ShopTypes.FAITH_POWERS) {
      this.choices = PartyManager.getParty().getLeader().getItems()
          .generateEquipmentEnhancementStringArray();
      this.index = 0;
    } else {
      // Invalid shop type
      return false;
    }
    return true;
  }

  private boolean shopStage3() {
    // Stage 3
    final PartyMember playerCharacter = PartyManager.getParty().getLeader();
    // Check
    if (this.type == ShopTypes.HEALER
        && playerCharacter.getCurrentHP() == playerCharacter.getMaximumHP()) {
      Messager.showDialog("You don't need healing.");
      return false;
    } else if (this.type == ShopTypes.REGENERATOR
        && playerCharacter.getCurrentMP() == playerCharacter.getMaximumMP()) {
      Messager.showDialog("You don't need regeneration.");
      return false;
    } else if (this.type == ShopTypes.SPELLS && playerCharacter.getSpellBook()
        .getSpellsKnownCount() == playerCharacter.getSpellBook()
            .getSpellCount()) {
      Messager.showDialog("There are no more spells to learn.");
      return false;
    }
    this.result = Messager.showInputDialog(this.getGoldTotals() + "Select",
        this.getShopNameFromType(), this.choices,
        this.choices[this.defaultChoice]);
    if (this.result == null) {
      return false;
    }
    if (this.index == -1) {
      return false;
    }
    this.index = 0;
    if (this.type != ShopTypes.SPELLS) {
      for (this.index = 0; this.index < this.choices.length; this.index++) {
        if (this.result.equals(this.choices[this.index])) {
          break;
        }
      }
    } else {
      this.index = playerCharacter.getSpellBook().getSpellIDByName(this.result);
    }
    return true;
  }

  private boolean shopStage4() {
    // Stage 4
    final PartyMember playerCharacter = PartyManager.getParty().getLeader();
    this.cost = 0;
    if (this.type == ShopTypes.WEAPONS || this.type == ShopTypes.ARMOR) {
      this.cost = Shop.getEquipmentCost(this.index + 1);
      if (this.type == ShopTypes.WEAPONS) {
        if (this.typeResult != null) {
          if (this.typeIndex == 1) {
            // Two-Handed Weapon, price doubled
            this.cost *= 2;
          }
        }
      }
    } else if (this.type == ShopTypes.HEALER) {
      this.cost = Shop.getHealingCost(playerCharacter.getLevel(),
          playerCharacter.getCurrentHP(), playerCharacter.getMaximumHP(),
          playerCharacter.getKills());
    } else if (this.type == ShopTypes.REGENERATOR) {
      this.cost = Shop.getRegenerationCost(playerCharacter.getLevel(),
          playerCharacter.getCurrentMP(), playerCharacter.getMaximumMP(),
          playerCharacter.getKills());
    } else if (this.type == ShopTypes.BANK) {
      String stage4Result = "";
      while (stage4Result.equals("")) {
        stage4Result = Messager.showTextInputDialog(
            this.getGoldTotals() + this.result + " How Much?",
            this.getShopNameFromType());
        if (stage4Result == null) {
          return false;
        }
        try {
          this.cost = Integer.parseInt(stage4Result);
          if (this.cost <= 0) {
            throw new NumberFormatException();
          }
        } catch (final NumberFormatException nf) {
          Messager.showErrorDialog("Amount must be greater than zero.",
              this.getShopNameFromType());
          stage4Result = "";
        }
      }
    } else if (this.type == ShopTypes.SPELLS) {
      this.cost = Shop.getSpellCost(this.index, playerCharacter.getKills());
    } else if (this.type == ShopTypes.ITEMS) {
      this.item = this.itemList.getAllItems()[this.index];
      this.cost = this.item.getBuyPrice();
    } else if (this.type == ShopTypes.SOCKS) {
      this.item = EquipmentFactory.createSocks(this.index + 1,
          (this.index + 1) * 500 * playerCharacter.getLevel());
      this.cost = this.item.getBuyPrice();
    } else if (this.type == ShopTypes.ENHANCEMENTS) {
      final Equipment old = playerCharacter.getItems()
          .getEquipmentInSlot(this.index);
      if (old != null) {
        if (old.isTwoHanded()) {
          this.twoHanded = true;
        }
        final int power = old.getPotency();
        final int bonus = (power % (Shop.MAX_ENHANCEMENTS + 1)) + 1;
        this.item = EquipmentFactory.createEnhancedEquipment(old, bonus);
        this.cost = Shop.getEnhancementCost(bonus, power);
        if (this.cost == 0) {
          // Equipment is maxed out on enhancements
          CommonDialogs.showErrorDialog(
              "That equipment cannot be enhanced any further, sorry!",
              this.getShopNameFromType());
          return false;
        }
      } else {
        CommonDialogs.showErrorDialog("Nothing is equipped there!",
            this.getShopNameFromType());
        return false;
      }
    } else if (this.type == ShopTypes.FAITH_POWERS) {
      final Equipment old = playerCharacter.getItems()
          .getEquipmentInSlot(this.index);
      if (old != null) {
        if (old.isTwoHanded()) {
          this.twoHanded = true;
        }
        final int power = old.getPotency();
        final int bonus = old.getFaithPowerLevel(this.typeIndex);
        this.item = EquipmentFactory.createFaithPoweredEquipment(old,
            this.typeIndex, bonus);
        this.cost = Shop.getEnhancementCost(bonus, power);
        if (this.cost == 0) {
          // Equipment is maxed out on Faith Powers
          CommonDialogs.showErrorDialog(
              "That equipment cannot be Faith Powered any further, sorry!",
              this.getShopNameFromType());
          return false;
        }
      } else {
        CommonDialogs.showErrorDialog("Nothing is equipped there!",
            this.getShopNameFromType());
        return false;
      }
    }
    if (this.type != ShopTypes.BANK) {
      // Handle inflation
      final double actualInflation = Shop.this.inflationRate / 100.0;
      final double inflatedCost = Shop.this.cost * actualInflation;
      Shop.this.cost = (int) inflatedCost;
      // Confirm
      final int stage4Confirm = Messager.showConfirmDialog(
          "This will cost " + this.cost + " Gold. Are you sure?",
          this.getShopNameFromType());
      if (stage4Confirm == JOptionPane.NO_OPTION
          || stage4Confirm == JOptionPane.CLOSED_OPTION) {
        return false;
      }
    }
    return true;
  }

  private boolean shopStage5() {
    // Stage 5
    final PartyMember playerCharacter = PartyManager.getParty().getLeader();
    if (this.type == ShopTypes.BANK) {
      if (this.index == 0) {
        if (playerCharacter.getGold() < this.cost) {
          Messager.showErrorDialog("Not Enough Gold!",
              this.getShopNameFromType());
          return false;
        }
      } else {
        if (PartyManager.getGoldInBank() < this.cost) {
          Messager.showErrorDialog("Not Enough Gold!",
              this.getShopNameFromType());
          return false;
        }
      }
    } else {
      if (playerCharacter.getGold() < this.cost) {
        Messager.showErrorDialog("Not Enough Gold!",
            this.getShopNameFromType());
        return false;
      }
    }
    return true;
  }

  private void shopStage6() {
    // Stage 6
    final PartyMember playerCharacter = PartyManager.getParty().getLeader();
    // Play transact sound
    if (FantastleReboot.getBagOStuff().getPrefsManager()
        .getSoundEnabled(PreferencesManager.SOUNDS_SHOP)) {
      SoundPlayer.playSound(SoundIndex.TRANSACT);
    }
    if (this.type == ShopTypes.WEAPONS) {
      playerCharacter.offsetGold(-this.cost);
      if (this.typeResult.equals(this.typeChoices[0])) {
        final Equipment bought = EquipmentFactory.createOneHandedWeapon(
            this.index, playerCharacter.getCaste().getCasteID(), 0);
        playerCharacter.getItems().equipOneHandedWeapon(playerCharacter, bought,
            this.handIndex, true);
      } else {
        final Equipment bought = EquipmentFactory.createTwoHandedWeapon(
            this.index, playerCharacter.getCaste().getCasteID(), 0);
        playerCharacter.getItems().equipTwoHandedWeapon(playerCharacter, bought,
            true);
      }
    } else if (this.type == ShopTypes.ARMOR) {
      playerCharacter.offsetGold(-this.cost);
      final Equipment bought = EquipmentFactory.createArmor(this.index,
          this.typeIndex, 0);
      playerCharacter.getItems().equipArmor(playerCharacter, bought, true);
    } else if (this.type == ShopTypes.HEALER) {
      playerCharacter.offsetGold(-this.cost);
      playerCharacter.healPercentage((this.index + 1) * 10);
    } else if (this.type == ShopTypes.REGENERATOR) {
      playerCharacter.offsetGold(-this.cost);
      playerCharacter.regeneratePercentage((this.index + 1) * 10);
    } else if (this.type == ShopTypes.BANK) {
      if (this.index == 0) {
        playerCharacter.offsetGold(-this.cost);
        PartyManager.addGoldToBank(this.cost);
      } else {
        PartyManager.removeGoldFromBank(this.cost);
        playerCharacter.offsetGold(this.cost);
      }
    } else if (this.type == ShopTypes.SPELLS) {
      playerCharacter.offsetGold(-this.cost);
      if (this.index != -1) {
        playerCharacter.getSpellBook().learnSpellByID(this.index);
      }
    } else if (this.type == ShopTypes.ITEMS) {
      playerCharacter.offsetGold(-this.cost);
      playerCharacter.getItems().addItem(this.item);
    } else if (Shop.this.type == ShopTypes.SOCKS) {
      playerCharacter.offsetGold(-Shop.this.cost);
      playerCharacter.getItems().equipArmor(playerCharacter,
          (Socks) Shop.this.item, true);
    } else if (Shop.this.type == ShopTypes.ENHANCEMENTS) {
      playerCharacter.offsetGold(-Shop.this.cost);
      playerCharacter.getItems().setEquipmentInSlot(Shop.this.index,
          (Equipment) Shop.this.item);
      if (Shop.this.twoHanded) {
        if (Shop.this.index == EquipmentSlotConstants.SLOT_MAINHAND) {
          playerCharacter.getItems().setEquipmentInSlot(
              EquipmentSlotConstants.SLOT_OFFHAND, (Equipment) Shop.this.item);
        } else if (Shop.this.index == EquipmentSlotConstants.SLOT_OFFHAND) {
          playerCharacter.getItems().setEquipmentInSlot(
              EquipmentSlotConstants.SLOT_MAINHAND, (Equipment) Shop.this.item);
        }
      }
    } else if (Shop.this.type == ShopTypes.FAITH_POWERS) {
      playerCharacter.offsetGold(-Shop.this.cost);
      playerCharacter.getItems().setEquipmentInSlot(Shop.this.index,
          (Equipment) Shop.this.item);
      if (Shop.this.twoHanded) {
        if (Shop.this.index == EquipmentSlotConstants.SLOT_MAINHAND) {
          playerCharacter.getItems().setEquipmentInSlot(
              EquipmentSlotConstants.SLOT_OFFHAND, (Equipment) Shop.this.item);
        } else if (Shop.this.index == EquipmentSlotConstants.SLOT_OFFHAND) {
          playerCharacter.getItems().setEquipmentInSlot(
              EquipmentSlotConstants.SLOT_MAINHAND, (Equipment) Shop.this.item);
        }
      }
    }
  }
}
