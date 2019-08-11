package com.puttysoftware.fantastlereboot.items;

import javax.swing.JOptionPane;

import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.Messager;
import com.puttysoftware.fantastlereboot.PreferencesManager;
import com.puttysoftware.fantastlereboot.assets.GameSound;
import com.puttysoftware.fantastlereboot.creatures.PCManager;
import com.puttysoftware.fantastlereboot.creatures.PlayerCharacter;
import com.puttysoftware.fantastlereboot.items.combat.CombatItemList;
import com.puttysoftware.fantastlereboot.loaders.SoundLoader;

public class Shop implements ShopTypes {
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

    // Constructors
    public Shop(final int shopType) {
        this.type = shopType;
        this.itemList = FantastleReboot.getApplication().getCombatItems();
        this.index = 0;
    }

    public static int getEquipmentCost(final int x) {
        return 2 * x * x * x + 16 * x * x + 30 * x;
    }

    private String getGoldTotals() {
        final PlayerCharacter playerCharacter = PCManager.getPlayer();
        if (this.type == ShopTypes.SHOP_TYPE_BANK) {
            return "Gold on Hand: " + playerCharacter.getGold()
                    + "\nGold in Bank: " + playerCharacter.getGoldInBank()
                    + "\n";
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
            return (int) (20 * i * i + 20
                    + Math.log((double) k + 1) / Math.log(2));
        }
    }

    private String getShopNameFromType() {
        switch (this.type) {
        case SHOP_TYPE_WEAPONS:
            return "Weapons";
        case SHOP_TYPE_ARMOR:
            return "Armor";
        case SHOP_TYPE_HEALER:
            return "Healer";
        case SHOP_TYPE_BANK:
            return "Bank";
        case SHOP_TYPE_REGENERATOR:
            return "Regenerator";
        case SHOP_TYPE_SPELLS:
            return "Spells";
        case SHOP_TYPE_ITEMS:
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
    }

    private boolean shopStage1() {
        // Stage 1
        // Play enter shop sound
        if (FantastleReboot.getApplication().getPrefsManager()
                .getSoundEnabled(PreferencesManager.SOUNDS_SHOP)) {
            SoundLoader.playSound(GameSound.SHOP);
        }
        if (this.type == ShopTypes.SHOP_TYPE_WEAPONS) {
            this.typeChoices = WeaponConstants.WEAPON_CHOICES;
            this.typeDefault = 0;
        } else if (this.type == ShopTypes.SHOP_TYPE_ARMOR) {
            this.typeChoices = ArmorConstants.ARMOR_CHOICES;
            this.typeDefault = 0;
        }
        if (this.typeChoices != null) {
            this.typeResult = Messager.showInputDialog(
                    this.getGoldTotals() + "Select Type",
                    this.getShopNameFromType(), this.typeChoices,
                    this.typeChoices[this.typeDefault]);
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
            if (this.type == ShopTypes.SHOP_TYPE_ARMOR && this.typeIndex > 1) {
                // Adjust typeIndex, position 2 is blank
                this.typeIndex++;
            }
        }
        return true;
    }

    private boolean shopStage2() {
        // Stage 2
        final PlayerCharacter playerCharacter = PCManager.getPlayer();
        if (this.type == ShopTypes.SHOP_TYPE_WEAPONS) {
            if (this.typeResult.equals(this.typeChoices[0])) {
                this.choices = EquipmentFactory.createOneHandedWeaponNames(
                        playerCharacter.getCaste().getCasteID());
                // Choose Hand
                this.handChoices = WeaponConstants.HAND_CHOICES;
                this.handDefault = 0;
                this.handResult = Messager.showInputDialog(
                        this.getGoldTotals() + "Select Hand",
                        this.getShopNameFromType(), this.handChoices,
                        this.handChoices[this.handDefault]);
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
        } else if (this.type == ShopTypes.SHOP_TYPE_ARMOR) {
            this.choices = EquipmentFactory.createArmorNames(this.typeIndex);
        } else if (this.type == ShopTypes.SHOP_TYPE_HEALER
                || this.type == ShopTypes.SHOP_TYPE_REGENERATOR) {
            this.choices = new String[10];
            int x;
            for (x = 0; x < this.choices.length; x++) {
                this.choices[x] = Integer.toString((x + 1) * 10) + "%";
            }
            this.defaultChoice = 9;
        } else if (this.type == ShopTypes.SHOP_TYPE_BANK) {
            this.choices = new String[2];
            this.choices[0] = "Deposit";
            this.choices[1] = "Withdraw";
        } else if (this.type == ShopTypes.SHOP_TYPE_SPELLS) {
            this.choices = playerCharacter.getSpellBook()
                    .getAllSpellsToLearnNames();
            if (this.choices == null) {
                this.choices = new String[1];
                this.choices[0] = "No Spells Left To Learn";
            }
        } else if (this.type == ShopTypes.SHOP_TYPE_ITEMS) {
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
        } else {
            // Invalid shop type
            return false;
        }
        return true;
    }

    private boolean shopStage3() {
        // Stage 3
        final PlayerCharacter playerCharacter = PCManager.getPlayer();
        // Check
        if (this.type == ShopTypes.SHOP_TYPE_HEALER && playerCharacter
                .getCurrentHP() == playerCharacter.getMaximumHP()) {
            Messager.showDialog("You don't need healing.");
            return false;
        } else if (this.type == ShopTypes.SHOP_TYPE_REGENERATOR
                && playerCharacter.getCurrentMP() == playerCharacter
                        .getMaximumMP()) {
            Messager.showDialog("You don't need regeneration.");
            return false;
        } else if (this.type == ShopTypes.SHOP_TYPE_SPELLS && playerCharacter
                .getSpellBook().getSpellsKnownCount() == playerCharacter
                        .getSpellBook().getMaximumSpellsKnownCount()) {
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
        if (this.type != ShopTypes.SHOP_TYPE_SPELLS) {
            for (this.index = 0; this.index < this.choices.length; this.index++) {
                if (this.result.equals(this.choices[this.index])) {
                    break;
                }
            }
        } else {
            this.index = playerCharacter.getSpellBook()
                    .getSpellIDByName(this.result);
        }
        return true;
    }

    private boolean shopStage4() {
        // Stage 4
        final PlayerCharacter playerCharacter = PCManager.getPlayer();
        this.cost = 0;
        if (this.type == ShopTypes.SHOP_TYPE_WEAPONS
                || this.type == ShopTypes.SHOP_TYPE_ARMOR) {
            this.cost = Shop.getEquipmentCost(this.index + 1);
            if (this.type == ShopTypes.SHOP_TYPE_WEAPONS) {
                if (this.typeResult != null) {
                    if (this.typeIndex == 1) {
                        // Two-Handed Weapon, price doubled
                        this.cost *= 2;
                    }
                }
            }
        } else if (this.type == ShopTypes.SHOP_TYPE_HEALER) {
            this.cost = Shop.getHealingCost(playerCharacter.getLevel(),
                    playerCharacter.getCurrentHP(),
                    playerCharacter.getMaximumHP(), playerCharacter.getKills());
        } else if (this.type == ShopTypes.SHOP_TYPE_REGENERATOR) {
            this.cost = Shop.getRegenerationCost(playerCharacter.getLevel(),
                    playerCharacter.getCurrentMP(),
                    playerCharacter.getMaximumMP(), playerCharacter.getKills());
        } else if (this.type == ShopTypes.SHOP_TYPE_BANK) {
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
                    Messager.showErrorDialog(
                            "Amount must be greater than zero.",
                            this.getShopNameFromType());
                    stage4Result = "";
                }
            }
        } else if (this.type == ShopTypes.SHOP_TYPE_SPELLS) {
            this.cost = Shop.getSpellCost(this.index,
                    playerCharacter.getKills());
        } else if (this.type == ShopTypes.SHOP_TYPE_ITEMS) {
            this.item = this.itemList.getAllItems()[this.index];
            this.cost = this.item.getBuyPrice();
        }
        if (this.type != ShopTypes.SHOP_TYPE_BANK) {
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
        final PlayerCharacter playerCharacter = PCManager.getPlayer();
        if (this.type == ShopTypes.SHOP_TYPE_BANK) {
            if (this.index == 0) {
                if (playerCharacter.getGold() < this.cost) {
                    Messager.showErrorDialog("Not Enough Gold!",
                            this.getShopNameFromType());
                    return false;
                }
            } else {
                if (playerCharacter.getGoldInBank() < this.cost) {
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
        final PlayerCharacter playerCharacter = PCManager.getPlayer();
        // Play transact sound
        if (FantastleReboot.getApplication().getPrefsManager()
                .getSoundEnabled(PreferencesManager.SOUNDS_SHOP)) {
            SoundLoader.playSound(GameSound.TRANSACT);
        }
        if (this.type == ShopTypes.SHOP_TYPE_WEAPONS) {
            playerCharacter.offsetGold(-this.cost);
            if (this.typeResult.equals(this.typeChoices[0])) {
                final Equipment bought = EquipmentFactory.createOneHandedWeapon(
                        this.index, playerCharacter.getCaste().getCasteID());
                playerCharacter.getItems().equipOneHandedWeapon(bought,
                        this.handIndex);
            } else {
                final Equipment bought = EquipmentFactory.createTwoHandedWeapon(
                        this.index, playerCharacter.getCaste().getCasteID());
                playerCharacter.getItems().equipTwoHandedWeapon(bought);
            }
        } else if (this.type == ShopTypes.SHOP_TYPE_ARMOR) {
            playerCharacter.offsetGold(-this.cost);
            final Equipment bought = EquipmentFactory.createArmor(this.index,
                    this.typeIndex);
            playerCharacter.getItems().equipArmor(bought);
        } else if (this.type == ShopTypes.SHOP_TYPE_HEALER) {
            playerCharacter.offsetGold(-this.cost);
            playerCharacter.healPercentage((this.index + 1) * 10);
        } else if (this.type == ShopTypes.SHOP_TYPE_REGENERATOR) {
            playerCharacter.offsetGold(-this.cost);
            playerCharacter.regeneratePercentage((this.index + 1) * 10);
        } else if (this.type == ShopTypes.SHOP_TYPE_BANK) {
            if (this.index == 0) {
                playerCharacter.offsetGold(-this.cost);
                playerCharacter.addGoldToBank(this.cost);
            } else {
                playerCharacter.removeGoldFromBank(this.cost);
                playerCharacter.offsetGold(this.cost);
            }
        } else if (this.type == ShopTypes.SHOP_TYPE_SPELLS) {
            playerCharacter.offsetGold(-this.cost);
            if (this.index != -1) {
                playerCharacter.getSpellBook().learnSpellByID(this.index);
            }
        } else if (this.type == ShopTypes.SHOP_TYPE_ITEMS) {
            playerCharacter.offsetGold(-this.cost);
            playerCharacter.getItems().addItem(this.item);
        }
    }
}
