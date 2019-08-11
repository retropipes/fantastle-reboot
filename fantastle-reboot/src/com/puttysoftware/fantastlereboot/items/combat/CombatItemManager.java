package com.puttysoftware.fantastlereboot.items.combat;

import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.Messager;
import com.puttysoftware.fantastlereboot.PreferencesManager;
import com.puttysoftware.fantastlereboot.assets.GameSound;
import com.puttysoftware.fantastlereboot.creatures.Creature;
import com.puttysoftware.fantastlereboot.creatures.PCManager;
import com.puttysoftware.fantastlereboot.effects.Effect;
import com.puttysoftware.fantastlereboot.items.ItemInventory;
import com.puttysoftware.fantastlereboot.loaders.SoundLoader;

public class CombatItemManager {
    // Fields
    private static final CombatItemList COMBAT_ITEMS = new CombatItemList();

    // Private Constructor
    private CombatItemManager() {
        // Do nothing
    }

    public static boolean selectAndUseItem(final Creature user) {
        boolean result = false;
        if (FantastleReboot.getApplication().getPrefsManager()
                .getSoundEnabled(PreferencesManager.SOUNDS_BATTLE)) {
            SoundLoader.playSound(GameSound.SPECIAL);
        }
        final CombatUsableItem used = CombatItemManager.selectItem(user);
        if (used != null) {
            final Effect e = used.getEffect();
            e.resetEffect();
            final Creature target = CombatItemManager.resolveTarget(used);
            user.getItems().useItem(used);
            if (target.isEffectActive(e)) {
                target.extendEffect(e, e.getInitialRounds());
            } else {
                e.restoreEffect(target);
                target.applyEffect(e);
            }
            result = true;
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
                if (FantastleReboot.getApplication().getPrefsManager()
                        .getSoundEnabled(PreferencesManager.SOUNDS_BATTLE)) {
                    SoundLoader.playSound(GameSound.PARTY_SPELL);
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
         // Play using item sound
            if (FantastleReboot.getApplication().getPrefsManager()
                    .getSoundEnabled(PreferencesManager.SOUNDS_BATTLE)) {
                SoundLoader.playSound(GameSound.MONSTER_SPELL);
            }
            final Effect e = used.getEffect();
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
            return FantastleReboot.getApplication().getBattle().getEnemy();
        default:
            return null;
        }
    }
}
