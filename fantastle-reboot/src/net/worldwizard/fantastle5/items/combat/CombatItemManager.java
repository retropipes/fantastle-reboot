package net.worldwizard.fantastle5.items.combat;

import net.worldwizard.fantastle5.Fantastle5;
import net.worldwizard.fantastle5.Messager;
import net.worldwizard.fantastle5.PreferencesManager;
import net.worldwizard.fantastle5.creatures.Creature;
import net.worldwizard.fantastle5.creatures.PCManager;
import net.worldwizard.fantastle5.effects.Effect;
import net.worldwizard.fantastle5.items.ItemInventory;
import net.worldwizard.fantastle5.resourcemanagers.SoundManager;

public class CombatItemManager {
    // Fields
    private static final CombatItemList COMBAT_ITEMS = new CombatItemList();

    // Private Constructor
    private CombatItemManager() {
        // Do nothing
    }

    public static boolean selectAndUseItem(final Creature user) {
        boolean result = false;
        final CombatUsableItem i = CombatItemManager.selectItem(user);
        if (i != null) {
            result = CombatItemManager.useItem(i, user);
        }
        return result;
    }

    private static CombatUsableItem selectItem(final Creature caster) {
        final ItemInventory ii = caster.getItems();
        if (ii != null) {
            final String[] names = ii.generateCombatUsableStringArray();
            final String[] displayNames = ii
                    .generateCombatUsableDisplayStringArray();
            if (names != null && displayNames != null) {
                // Play using item sound
                if (Fantastle5.getApplication().getPrefsManager()
                        .getSoundEnabled(PreferencesManager.SOUNDS_BATTLE)) {
                    SoundManager.playSoundAsynchronously("spell");
                }
                String dialogResult = null;
                dialogResult = Messager.showInputDialog("Select an Item to Use",
                        "Select Item", displayNames, displayNames[0]);
                if (dialogResult != null) {
                    int index;
                    for (index = 0; index < displayNames.length; index++) {
                        if (dialogResult.equals(displayNames[index])) {
                            break;
                        }
                    }
                    final CombatUsableItem i = CombatItemManager.COMBAT_ITEMS
                            .getItemByName(names[index]);
                    if (ii.getUses(i) > 0) {
                        return i;
                    } else {
                        Messager.showErrorDialog(
                                "You try to use an item, but realize you've run out!",
                                "Select Item");
                        return null;
                    }
                } else {
                    return null;
                }
            } else {
                Messager.showErrorDialog(
                        "You try to use an item, but realize you don't have any!",
                        "Select Item");
                return null;
            }
        } else {
            Messager.showErrorDialog(
                    "You try to use an item, but realize you don't have any!",
                    "Select Item");
            return null;
        }
    }

    public static boolean useItem(final CombatUsableItem used,
            final Creature user) {
        if (used != null) {
            final Effect e = used.getEffect();
            // Play item's associated sound effect, if it has one
            final String snd = used.getSound();
            if (snd != null) {
                if (Fantastle5.getApplication().getPrefsManager()
                        .getSoundEnabled(PreferencesManager.SOUNDS_BATTLE)) {
                    SoundManager.playSoundAsynchronously(snd);
                }
            }
            e.resetEffect();
            final Creature target = CombatItemManager.resolveTarget(used);
            user.getItems().useItem(used);
            if (target.isEffectActive(e)) {
                target.extendEffect(e, e.getInitialRounds());
            } else {
                e.restoreEffect(target);
                target.applyEffect(e);
            }
            return true;
        } else {
            return false;
        }
    }

    private static Creature resolveTarget(final CombatUsableItem used) {
        final char target = used.getTarget();
        switch (target) {
        case 'P':
            return PCManager.getPlayer();
        case 'E':
            return Fantastle5.getApplication().getBattle().getEnemy();
        default:
            return null;
        }
    }
}
