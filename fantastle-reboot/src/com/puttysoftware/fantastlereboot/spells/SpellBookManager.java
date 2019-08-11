package com.puttysoftware.fantastlereboot.spells;

import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.Messager;
import com.puttysoftware.fantastlereboot.PreferencesManager;
import com.puttysoftware.fantastlereboot.creatures.Creature;
import com.puttysoftware.fantastlereboot.creatures.PCManager;
import com.puttysoftware.fantastlereboot.creatures.castes.CasteConstants;
import com.puttysoftware.fantastlereboot.effects.Effect;
import com.puttysoftware.fantastlereboot.loaders.assets.SoundManager;

public class SpellBookManager {
    // Fields
    private static boolean NO_SPELLS_FLAG = false;

    // Private Constructor
    private SpellBookManager() {
        // Do nothing
    }

    public static boolean selectAndCastSpell(final Creature caster) {
        boolean result = false;
        SpellBookManager.NO_SPELLS_FLAG = false;
        final Spell s = SpellBookManager.selectSpell(caster);
        if (s != null) {
            result = SpellBookManager.castSpell(s, caster);
            if (!result && !SpellBookManager.NO_SPELLS_FLAG) {
                Messager.showErrorDialog(
                        "You try to cast a spell, but realize you don't have enough MP!",
                        "Select Spell");
            }
        }
        return result;
    }

    private static Spell selectSpell(final Creature caster) {
        final SpellBook book = caster.getSpellBook();
        if (book != null) {
            final String[] names = book.getAllSpellNames();
            final String[] displayNames = book.getAllSpellNamesWithCosts();
            if (names != null && displayNames != null) {
                // Play casting spell sound
                if (FantastleReboot.getApplication().getPrefsManager()
                        .getSoundEnabled(PreferencesManager.SOUNDS_BATTLE)) {
                    SoundManager.playSoundAsynchronously("spell");
                }
                String dialogResult = null;
                dialogResult = Messager.showInputDialog(
                        "Select a Spell to Cast", "Select Spell", displayNames,
                        displayNames[0]);
                if (dialogResult != null) {
                    int index;
                    for (index = 0; index < displayNames.length; index++) {
                        if (dialogResult.equals(displayNames[index])) {
                            break;
                        }
                    }
                    final Spell s = book.getSpellByName(names[index]);
                    return s;
                } else {
                    return null;
                }
            } else {
                SpellBookManager.NO_SPELLS_FLAG = true;
                Messager.showErrorDialog(
                        "You try to cast a spell, but realize you don't know any!",
                        "Select Spell");
                return null;
            }
        } else {
            SpellBookManager.NO_SPELLS_FLAG = true;
            Messager.showErrorDialog(
                    "You try to cast a spell, but realize you don't know any!",
                    "Select Spell");
            return null;
        }
    }

    public static boolean castSpell(final Spell cast, final Creature caster) {
        if (cast != null) {
            final int casterMP = caster.getCurrentMP();
            final int cost = cast.getCost();
            if (casterMP >= cost) {
                // Cast Spell
                caster.drain(cost);
                final Effect b = cast.getEffect();
                // Play spell's associated sound effect, if it has one
                final String snd = cast.getSound();
                if (snd != null) {
                    if (FantastleReboot.getApplication().getPrefsManager()
                            .getSoundEnabled(
                                    PreferencesManager.SOUNDS_BATTLE)) {
                        SoundManager.playSoundAsynchronously(snd);
                    }
                }
                b.resetEffect();
                final Creature target = SpellBookManager.resolveTarget(cast);
                if (target.isEffectActive(b)) {
                    target.extendEffect(b, b.getInitialRounds());
                } else {
                    b.restoreEffect(target);
                    target.applyEffect(b);
                }
                return true;
            } else {
                // Not enough MP
                return false;
            }
        } else {
            return false;
        }
    }

    private static Creature resolveTarget(final Spell cast) {
        final char target = cast.getTarget();
        switch (target) {
        case 'P':
            return PCManager.getPlayer();
        case 'E':
            return FantastleReboot.getApplication().getBattle().getEnemy();
        default:
            return null;
        }
    }

    public static SpellBook getSpellBookByID(final int ID) {
        switch (ID) {
        case CasteConstants.CASTE_ASSASSIN:
            return new AssassinSpellBook();
        case CasteConstants.CASTE_BASHER:
            return new BasherSpellBook();
        case CasteConstants.CASTE_CURER:
            return new CurerSpellBook();
        case CasteConstants.CASTE_DESTROYER:
            return new DestroyerSpellBook();
        case CasteConstants.CASTE_ECLECTIC:
            return new EclecticSpellBook();
        case CasteConstants.CASTE_FOOL:
            return new FoolSpellBook();
        case CasteConstants.CASTE_GURU:
            return new GuruSpellBook();
        case CasteConstants.CASTE_HUNTER:
            return new HunterSpellBook();
        case CasteConstants.CASTE_JUMPER:
            return new JumperSpellBook();
        case CasteConstants.CASTE_KNIGHT:
            return new KnightSpellBook();
        case CasteConstants.CASTE_LOCKSMITH:
            return new LocksmithSpellBook();
        case CasteConstants.CASTE_MONK:
            return new MonkSpellBook();
        case CasteConstants.CASTE_NINJA:
            return new NinjaSpellBook();
        case CasteConstants.CASTE_OVERSEER:
            return new OverseerSpellBook();
        case CasteConstants.CASTE_PICKPOCKET:
            return new PickpocketSpellBook();
        case CasteConstants.CASTE_ROGUE:
            return new RogueSpellBook();
        case CasteConstants.CASTE_SPY:
            return new SpySpellBook();
        case CasteConstants.CASTE_TEACHER:
            return new TeacherSpellBook();
        case CasteConstants.CASTE_WARLOCK:
            return new WarlockSpellBook();
        case CasteConstants.CASTE_YELLER:
            return new YellerSpellBook();
        default:
            return null;
        }
    }

    public static SpellBook getEnemySpellBookByID(final int ID) {
        switch (ID) {
        case 0:
            return null;
        case 1:
            return new LowLevelSpellBook();
        case 2:
            return new MidLevelSpellBook();
        case 3:
            return new HighLevelSpellBook();
        case 4:
            return new ToughLevelSpellBook();
        default:
            return null;
        }
    }
}
