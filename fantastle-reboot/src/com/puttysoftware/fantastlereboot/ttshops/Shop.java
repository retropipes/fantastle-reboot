/*  TallerTower: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.ttshops;

import javax.swing.JOptionPane;

import com.puttysoftware.commondialogs.CommonDialogs;
import com.puttysoftware.fantastlereboot.assets.GameSound;
import com.puttysoftware.fantastlereboot.creatures.party.PartyManager;
import com.puttysoftware.fantastlereboot.creatures.party.PartyMember;
import com.puttysoftware.fantastlereboot.loaders.SoundLoader;
import com.puttysoftware.fantastlereboot.ttitems.ArmorConstants;
import com.puttysoftware.fantastlereboot.ttitems.Equipment;
import com.puttysoftware.fantastlereboot.ttitems.EquipmentFactory;
import com.puttysoftware.fantastlereboot.ttitems.EquipmentSlotConstants;
import com.puttysoftware.fantastlereboot.ttitems.Item;
import com.puttysoftware.fantastlereboot.ttitems.Socks;
import com.puttysoftware.fantastlereboot.ttitems.WeaponConstants;
import com.puttysoftware.fantastlereboot.ttitems.combat.CombatItemList;

public class Shop {
    // Fields
    final int type;
    int index;
    int defaultChoice;
    String[] choices;
    String[] typeChoices;
    int typeDefault;
    String typeResult;
    int typeIndex;
    boolean handIndex;
    String result;
    int cost;
    final int inflationRate;
    Item item;
    boolean twoHanded;
    final CombatItemList itemList;
    private final ShopDialogGUI dialogGUI;
    static final int MAX_ENHANCEMENTS = 9;

    // Constructors
    public Shop(final int shopType) {
        super();
        this.dialogGUI = new ShopDialogGUI();
        this.type = shopType;
        this.itemList = new CombatItemList();
        this.index = 0;
        this.inflationRate = 100;
        this.twoHanded = false;
    }

    // Methods
    public static int getEquipmentCost(final int x) {
        return 10 * x * x * x + 10 * x * x + 10 * x + 10;
    }

    String getGoldTotals() {
        final PartyMember playerCharacter = PartyManager.getParty().getLeader();
        if (this.type == ShopTypes.SHOP_TYPE_BANK) {
            return "Gold on Hand: " + playerCharacter.getGold()
                    + "\nGold in Bank: " + PartyManager.getGoldInBank() + "\n";
        } else {
            return "";
        }
    }

    static int getHealingCost(final int x, final int y, final int z) {
        return (int) (Math.log10(x) * ((z - y)));
    }

    static int getRegenerationCost(final int x, final int y, final int z) {
        final int diff = z - y;
        if (diff == 0) {
            return 0;
        } else {
            final int cost = (int) ((Math.log(x) / Math.log(2)) * diff);
            if (cost < 1) {
                return 1;
            } else {
                return cost;
            }
        }
    }

    static int getSpellCost(final int i) {
        if (i == -1) {
            return 0;
        } else {
            return 20 * i * i + 20;
        }
    }

    static int getEnhancementCost(final int i, final int x) {
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

    String getShopNameFromType() {
        return ShopTypes.SHOP_NAMES[this.type - 1];
    }

    public void showShop() {
        this.dialogGUI.showShop();
    }

    private class ShopDialogGUI {
        public ShopDialogGUI() {
            // Do nothing
        }

        public void showShop() {
            Shop.this.index = 0;
            Shop.this.defaultChoice = 0;
            Shop.this.choices = null;
            Shop.this.typeChoices = null;
            Shop.this.typeDefault = 0;
            Shop.this.typeResult = null;
            Shop.this.typeIndex = 0;
            Shop.this.handIndex = false;
            Shop.this.result = null;
            Shop.this.cost = 0;
            Shop.this.twoHanded = false;
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
        }

        private boolean shopStage1() {
            // Stage 1
            // Play enter shop sound
            SoundLoader.playSound(GameSound.SHOP);
            if (Shop.this.type == ShopTypes.SHOP_TYPE_WEAPONS) {
                Shop.this.typeChoices = WeaponConstants.getWeaponChoices();
                Shop.this.typeDefault = 0;
            } else if (Shop.this.type == ShopTypes.SHOP_TYPE_ARMOR) {
                Shop.this.typeChoices = ArmorConstants.getArmorChoices();
                Shop.this.typeDefault = 0;
            } else if (Shop.this.type == ShopTypes.SHOP_TYPE_FAITH_POWERS) {
                Shop.this.typeIndex = PartyManager.getParty().getLeader()
                        .getFaith().getFaithID();
            }
            if (Shop.this.typeChoices != null) {
                Shop.this.typeResult = CommonDialogs.showInputDialog(
                        Shop.this.getGoldTotals() + "Select Type",
                        Shop.this.getShopNameFromType(), Shop.this.typeChoices,
                        Shop.this.typeChoices[Shop.this.typeDefault]);
                if (Shop.this.typeResult == null) {
                    return false;
                }
                for (Shop.this.typeIndex = 0; Shop.this.typeIndex < Shop.this.typeChoices.length; Shop.this.typeIndex++) {
                    if (Shop.this.typeResult
                            .equals(Shop.this.typeChoices[Shop.this.typeIndex])) {
                        break;
                    }
                }
                if (Shop.this.typeIndex == Shop.this.typeChoices.length) {
                    return false;
                }
                if (Shop.this.type == ShopTypes.SHOP_TYPE_ARMOR
                        && Shop.this.typeIndex > 1) {
                    // Adjust typeIndex, position 2 is blank
                    Shop.this.typeIndex++;
                }
            }
            return true;
        }

        private boolean shopStage2() {
            // Stage 2
            final PartyMember playerCharacter = PartyManager.getParty()
                    .getLeader();
            if (Shop.this.type == ShopTypes.SHOP_TYPE_WEAPONS) {
                if (Shop.this.typeResult.equals(Shop.this.typeChoices[0])) {
                    Shop.this.choices = EquipmentFactory
                            .createOneHandedWeaponNames(playerCharacter
                                    .getCaste().getCasteID());
                    // Choose Hand
                    final String[] handChoices = WeaponConstants
                            .getHandChoices();
                    final int handDefault = 0;
                    final String handResult = CommonDialogs.showInputDialog(
                            Shop.this.getGoldTotals() + "Select Hand",
                            Shop.this.getShopNameFromType(), handChoices,
                            handChoices[handDefault]);
                    if (handResult == null) {
                        return false;
                    }
                    if (handResult.equals(handChoices[0])) {
                        Shop.this.handIndex = true;
                    } else {
                        Shop.this.handIndex = false;
                    }
                } else {
                    Shop.this.choices = EquipmentFactory
                            .createTwoHandedWeaponNames(playerCharacter
                                    .getCaste().getCasteID());
                }
            } else if (Shop.this.type == ShopTypes.SHOP_TYPE_ARMOR) {
                Shop.this.choices = EquipmentFactory
                        .createArmorNames(Shop.this.typeIndex);
            } else if (Shop.this.type == ShopTypes.SHOP_TYPE_HEALER
                    || Shop.this.type == ShopTypes.SHOP_TYPE_REGENERATOR) {
                Shop.this.choices = new String[10];
                int x;
                for (x = 0; x < Shop.this.choices.length; x++) {
                    Shop.this.choices[x] = Integer.toString((x + 1) * 10) + "%";
                }
                Shop.this.defaultChoice = 9;
            } else if (Shop.this.type == ShopTypes.SHOP_TYPE_BANK) {
                Shop.this.choices = new String[2];
                Shop.this.choices[0] = "Deposit";
                Shop.this.choices[1] = "Withdraw";
            } else if (Shop.this.type == ShopTypes.SHOP_TYPE_SPELLS) {
                Shop.this.choices = playerCharacter.getSpellBook()
                        .getAllSpellsToLearnNames();
                if (Shop.this.choices == null) {
                    Shop.this.choices = new String[1];
                    Shop.this.choices[0] = "No Spells Left To Learn";
                }
            } else if (Shop.this.type == ShopTypes.SHOP_TYPE_ITEMS) {
                Shop.this.choices = Shop.this.itemList.getAllNames();
                if (Shop.this.choices.length == 0) {
                    Shop.this.choices = new String[1];
                    Shop.this.choices[0] = "No Items To Buy";
                    Shop.this.index = -1;
                }
            } else if (Shop.this.type == ShopTypes.SHOP_TYPE_SOCKS) {
                Shop.this.choices = EquipmentFactory.createSocksNames();
                if (Shop.this.choices.length == 0) {
                    Shop.this.choices = new String[1];
                    Shop.this.choices[0] = "No Socks To Buy";
                    Shop.this.index = -1;
                }
            } else if (Shop.this.type == ShopTypes.SHOP_TYPE_ENHANCEMENTS) {
                Shop.this.choices = PartyManager.getParty().getLeader()
                        .getItems().generateEquipmentEnhancementStringArray();
                Shop.this.index = 0;
            } else if (Shop.this.type == ShopTypes.SHOP_TYPE_FAITH_POWERS) {
                Shop.this.choices = PartyManager.getParty().getLeader()
                        .getItems().generateEquipmentEnhancementStringArray();
                Shop.this.index = 0;
            } else {
                // Invalid shop type
                return false;
            }
            return true;
        }

        private boolean shopStage3() {
            // Stage 3
            final PartyMember playerCharacter = PartyManager.getParty()
                    .getLeader();
            // Check
            if (Shop.this.type == ShopTypes.SHOP_TYPE_HEALER
                    && playerCharacter.getCurrentHP() == playerCharacter
                            .getMaximumHP()) {
                CommonDialogs.showDialog("You don't need healing.");
                return false;
            } else if (Shop.this.type == ShopTypes.SHOP_TYPE_REGENERATOR
                    && playerCharacter.getCurrentMP() == playerCharacter
                            .getMaximumMP()) {
                CommonDialogs.showDialog("You don't need regeneration.");
                return false;
            } else if (Shop.this.type == ShopTypes.SHOP_TYPE_SPELLS
                    && playerCharacter.getSpellBook().getSpellsKnownCount() == playerCharacter
                            .getSpellBook().getMaximumSpellsKnownCount()) {
                CommonDialogs.showDialog("There are no more spells to learn.");
                return false;
            }
            Shop.this.result = CommonDialogs.showInputDialog(
                    Shop.this.getGoldTotals() + "Select",
                    Shop.this.getShopNameFromType(), Shop.this.choices,
                    Shop.this.choices[Shop.this.defaultChoice]);
            if (Shop.this.result == null) {
                return false;
            }
            if (Shop.this.index == -1) {
                return false;
            }
            Shop.this.index = 0;
            if (Shop.this.type != ShopTypes.SHOP_TYPE_SPELLS) {
                for (Shop.this.index = 0; Shop.this.index < Shop.this.choices.length; Shop.this.index++) {
                    if (Shop.this.result
                            .equals(Shop.this.choices[Shop.this.index])) {
                        break;
                    }
                }
            } else {
                Shop.this.index = playerCharacter.getSpellBook()
                        .getSpellIDByName(Shop.this.result);
            }
            return true;
        }

        private boolean shopStage4() {
            // Stage 4
            final PartyMember playerCharacter = PartyManager.getParty()
                    .getLeader();
            Shop.this.cost = 0;
            if ((Shop.this.type == ShopTypes.SHOP_TYPE_WEAPONS)
                    || (Shop.this.type == ShopTypes.SHOP_TYPE_ARMOR)) {
                Shop.this.cost = Shop.getEquipmentCost(Shop.this.index);
                if (Shop.this.type == ShopTypes.SHOP_TYPE_WEAPONS) {
                    if (Shop.this.typeResult != null) {
                        if (Shop.this.typeIndex == 1) {
                            // Two-Handed Weapon, price doubled
                            Shop.this.cost *= 2;
                        }
                    }
                }
            } else if (Shop.this.type == ShopTypes.SHOP_TYPE_HEALER) {
                Shop.this.cost = Shop.getHealingCost(
                        playerCharacter.getLevel(),
                        playerCharacter.getCurrentHP(),
                        playerCharacter.getMaximumHP());
            } else if (Shop.this.type == ShopTypes.SHOP_TYPE_REGENERATOR) {
                Shop.this.cost = Shop.getRegenerationCost(
                        playerCharacter.getLevel(),
                        playerCharacter.getCurrentMP(),
                        playerCharacter.getMaximumMP());
            } else if (Shop.this.type == ShopTypes.SHOP_TYPE_BANK) {
                String stage4Result = "";
                while (stage4Result.equals("")) {
                    stage4Result = CommonDialogs.showTextInputDialog(
                            Shop.this.getGoldTotals() + Shop.this.result
                                    + " How Much?",
                            Shop.this.getShopNameFromType());
                    if (stage4Result == null) {
                        return false;
                    }
                    try {
                        Shop.this.cost = Integer.parseInt(stage4Result);
                        if (Shop.this.cost <= 0) {
                            throw new NumberFormatException();
                        }
                    } catch (final NumberFormatException nf) {
                        CommonDialogs.showErrorDialog(
                                "Amount must be greater than zero.",
                                Shop.this.getShopNameFromType());
                        stage4Result = "";
                    }
                }
            } else if (Shop.this.type == ShopTypes.SHOP_TYPE_SPELLS) {
                Shop.this.cost = Shop.getSpellCost(Shop.this.index);
            } else if (Shop.this.type == ShopTypes.SHOP_TYPE_ITEMS) {
                Shop.this.item = Shop.this.itemList.getAllItems()[Shop.this.index];
                Shop.this.cost = Shop.this.item.getBuyPrice();
            } else if (Shop.this.type == ShopTypes.SHOP_TYPE_SOCKS) {
                Shop.this.item = EquipmentFactory.createSocks(
                        Shop.this.index + 1, (Shop.this.index + 1) * 500
                                * playerCharacter.getLevel());
                Shop.this.cost = Shop.this.item.getBuyPrice();
            } else if (Shop.this.type == ShopTypes.SHOP_TYPE_ENHANCEMENTS) {
                final Equipment old = playerCharacter.getItems()
                        .getEquipmentInSlot(Shop.this.index);
                if (old != null) {
                    if (old.isTwoHanded()) {
                        Shop.this.twoHanded = true;
                    }
                    final int power = old.getPotency();
                    final int bonus = (power % (Shop.MAX_ENHANCEMENTS + 1)) + 1;
                    Shop.this.item = EquipmentFactory.createEnhancedEquipment(
                            old, bonus);
                    Shop.this.cost = Shop.getEnhancementCost(bonus, power);
                    if (Shop.this.cost == 0) {
                        // Equipment is maxed out on enhancements
                        CommonDialogs
                                .showErrorDialog(
                                        "That equipment cannot be enhanced any further, sorry!",
                                        Shop.this.getShopNameFromType());
                        return false;
                    }
                } else {
                    CommonDialogs.showErrorDialog("Nothing is equipped there!",
                            Shop.this.getShopNameFromType());
                    return false;
                }
            } else if (Shop.this.type == ShopTypes.SHOP_TYPE_FAITH_POWERS) {
                final Equipment old = playerCharacter.getItems()
                        .getEquipmentInSlot(Shop.this.index);
                if (old != null) {
                    if (old.isTwoHanded()) {
                        Shop.this.twoHanded = true;
                    }
                    final int power = old.getPotency();
                    final int bonus = old
                            .getFaithPowerLevel(Shop.this.typeIndex);
                    Shop.this.item = EquipmentFactory
                            .createFaithPoweredEquipment(old,
                                    Shop.this.typeIndex, bonus);
                    Shop.this.cost = Shop.getEnhancementCost(bonus, power);
                    if (Shop.this.cost == 0) {
                        // Equipment is maxed out on Faith Powers
                        CommonDialogs
                                .showErrorDialog(
                                        "That equipment cannot be Faith Powered any further, sorry!",
                                        Shop.this.getShopNameFromType());
                        return false;
                    }
                } else {
                    CommonDialogs.showErrorDialog("Nothing is equipped there!",
                            Shop.this.getShopNameFromType());
                    return false;
                }
            }
            if (Shop.this.type != ShopTypes.SHOP_TYPE_BANK) {
                // Handle inflation
                final double actualInflation = Shop.this.inflationRate / 100.0;
                final double inflatedCost = Shop.this.cost * actualInflation;
                Shop.this.cost = (int) inflatedCost;
                // Confirm
                final int stage4Confirm = CommonDialogs.showConfirmDialog(
                        "This will cost " + Shop.this.cost
                                + " Gold. Are you sure?",
                        Shop.this.getShopNameFromType());
                if (stage4Confirm == JOptionPane.NO_OPTION
                        || stage4Confirm == JOptionPane.CLOSED_OPTION) {
                    return false;
                }
            }
            return true;
        }

        private boolean shopStage5() {
            // Stage 5
            final PartyMember playerCharacter = PartyManager.getParty()
                    .getLeader();
            if (Shop.this.type == ShopTypes.SHOP_TYPE_BANK) {
                if (Shop.this.index == 0) {
                    if (playerCharacter.getGold() < Shop.this.cost) {
                        CommonDialogs.showErrorDialog("Not Enough Gold!",
                                Shop.this.getShopNameFromType());
                        return false;
                    }
                } else {
                    if (PartyManager.getGoldInBank() < Shop.this.cost) {
                        CommonDialogs.showErrorDialog("Not Enough Gold!",
                                Shop.this.getShopNameFromType());
                        return false;
                    }
                }
            } else {
                if (playerCharacter.getGold() < Shop.this.cost) {
                    CommonDialogs.showErrorDialog("Not Enough Gold!",
                            Shop.this.getShopNameFromType());
                    return false;
                }
            }
            return true;
        }

        private void shopStage6() {
            // Stage 6
            final PartyMember playerCharacter = PartyManager.getParty()
                    .getLeader();
            // Play transact sound
            SoundLoader.playSound(GameSound.TRANSACT);
            if (Shop.this.type == ShopTypes.SHOP_TYPE_WEAPONS) {
                playerCharacter.offsetGold(-Shop.this.cost);
                if (Shop.this.typeResult.equals(Shop.this.typeChoices[0])) {
                    final Equipment bought = EquipmentFactory
                            .createOneHandedWeapon(Shop.this.index,
                                    playerCharacter.getCaste().getCasteID(), 0);
                    playerCharacter.getItems().equipOneHandedWeapon(
                            playerCharacter, bought, Shop.this.handIndex, true);
                } else {
                    final Equipment bought = EquipmentFactory
                            .createTwoHandedWeapon(Shop.this.index,
                                    playerCharacter.getCaste().getCasteID(), 0);
                    playerCharacter.getItems().equipTwoHandedWeapon(
                            playerCharacter, bought, true);
                }
            } else if (Shop.this.type == ShopTypes.SHOP_TYPE_ARMOR) {
                playerCharacter.offsetGold(-Shop.this.cost);
                final Equipment bought = EquipmentFactory.createArmor(
                        Shop.this.index, Shop.this.typeIndex, 0);
                playerCharacter.getItems().equipArmor(playerCharacter, bought,
                        true);
            } else if (Shop.this.type == ShopTypes.SHOP_TYPE_HEALER) {
                playerCharacter.offsetGold(-Shop.this.cost);
                playerCharacter.healPercentage((Shop.this.index + 1) * 10);
            } else if (Shop.this.type == ShopTypes.SHOP_TYPE_REGENERATOR) {
                playerCharacter.offsetGold(-Shop.this.cost);
                playerCharacter
                        .regeneratePercentage((Shop.this.index + 1) * 10);
            } else if (Shop.this.type == ShopTypes.SHOP_TYPE_BANK) {
                if (Shop.this.index == 0) {
                    playerCharacter.offsetGold(-Shop.this.cost);
                    PartyManager.addGoldToBank(Shop.this.cost);
                } else {
                    PartyManager.removeGoldFromBank(Shop.this.cost);
                    playerCharacter.offsetGold(Shop.this.cost);
                }
            } else if (Shop.this.type == ShopTypes.SHOP_TYPE_SPELLS) {
                playerCharacter.offsetGold(-Shop.this.cost);
                if (Shop.this.index != -1) {
                    playerCharacter.getSpellBook().learnSpellByID(
                            Shop.this.index);
                }
            } else if (Shop.this.type == ShopTypes.SHOP_TYPE_ITEMS) {
                playerCharacter.offsetGold(-Shop.this.cost);
                playerCharacter.getItems().addItem(Shop.this.item);
            } else if (Shop.this.type == ShopTypes.SHOP_TYPE_SOCKS) {
                playerCharacter.offsetGold(-Shop.this.cost);
                playerCharacter.getItems().equipArmor(playerCharacter,
                        (Socks) Shop.this.item, true);
            } else if (Shop.this.type == ShopTypes.SHOP_TYPE_ENHANCEMENTS) {
                playerCharacter.offsetGold(-Shop.this.cost);
                playerCharacter.getItems().setEquipmentInSlot(Shop.this.index,
                        (Equipment) Shop.this.item);
                if (Shop.this.twoHanded) {
                    if (Shop.this.index == EquipmentSlotConstants.SLOT_MAINHAND) {
                        playerCharacter.getItems().setEquipmentInSlot(
                                EquipmentSlotConstants.SLOT_OFFHAND,
                                (Equipment) Shop.this.item);
                    } else if (Shop.this.index == EquipmentSlotConstants.SLOT_OFFHAND) {
                        playerCharacter.getItems().setEquipmentInSlot(
                                EquipmentSlotConstants.SLOT_MAINHAND,
                                (Equipment) Shop.this.item);
                    }
                }
            } else if (Shop.this.type == ShopTypes.SHOP_TYPE_FAITH_POWERS) {
                playerCharacter.offsetGold(-Shop.this.cost);
                playerCharacter.getItems().setEquipmentInSlot(Shop.this.index,
                        (Equipment) Shop.this.item);
                if (Shop.this.twoHanded) {
                    if (Shop.this.index == EquipmentSlotConstants.SLOT_MAINHAND) {
                        playerCharacter.getItems().setEquipmentInSlot(
                                EquipmentSlotConstants.SLOT_OFFHAND,
                                (Equipment) Shop.this.item);
                    } else if (Shop.this.index == EquipmentSlotConstants.SLOT_OFFHAND) {
                        playerCharacter.getItems().setEquipmentInSlot(
                                EquipmentSlotConstants.SLOT_MAINHAND,
                                (Equipment) Shop.this.item);
                    }
                }
            }
        }
    }
}
