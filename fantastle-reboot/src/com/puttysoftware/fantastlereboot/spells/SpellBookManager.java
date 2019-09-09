package com.puttysoftware.fantastlereboot.spells;

import com.puttysoftware.fantastlereboot.creatures.castes.CasteConstants;

public class SpellBookManager {
    // Private Constructor
    private SpellBookManager() {
        // Do nothing
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
