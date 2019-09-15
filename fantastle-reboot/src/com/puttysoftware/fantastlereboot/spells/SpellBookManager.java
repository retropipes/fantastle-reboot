package com.puttysoftware.fantastlereboot.spells;

import com.puttysoftware.fantastlereboot.creatures.castes.CasteConstants;
import com.puttysoftware.fantastlereboot.spells.books.AssassinSpellBook;
import com.puttysoftware.fantastlereboot.spells.books.BasherSpellBook;
import com.puttysoftware.fantastlereboot.spells.books.CurerSpellBook;
import com.puttysoftware.fantastlereboot.spells.books.DestroyerSpellBook;
import com.puttysoftware.fantastlereboot.spells.books.EclecticSpellBook;
import com.puttysoftware.fantastlereboot.spells.books.FoolSpellBook;
import com.puttysoftware.fantastlereboot.spells.books.GuruSpellBook;
import com.puttysoftware.fantastlereboot.spells.books.HighLevelSpellBook;
import com.puttysoftware.fantastlereboot.spells.books.HunterSpellBook;
import com.puttysoftware.fantastlereboot.spells.books.JumperSpellBook;
import com.puttysoftware.fantastlereboot.spells.books.KnightSpellBook;
import com.puttysoftware.fantastlereboot.spells.books.LocksmithSpellBook;
import com.puttysoftware.fantastlereboot.spells.books.LowLevelSpellBook;
import com.puttysoftware.fantastlereboot.spells.books.MidLevelSpellBook;
import com.puttysoftware.fantastlereboot.spells.books.MonkSpellBook;
import com.puttysoftware.fantastlereboot.spells.books.NinjaSpellBook;
import com.puttysoftware.fantastlereboot.spells.books.OverseerSpellBook;
import com.puttysoftware.fantastlereboot.spells.books.PickpocketSpellBook;
import com.puttysoftware.fantastlereboot.spells.books.RogueSpellBook;
import com.puttysoftware.fantastlereboot.spells.books.SpySpellBook;
import com.puttysoftware.fantastlereboot.spells.books.TeacherSpellBook;
import com.puttysoftware.fantastlereboot.spells.books.ToughLevelSpellBook;
import com.puttysoftware.fantastlereboot.spells.books.WarlockSpellBook;
import com.puttysoftware.fantastlereboot.spells.books.YellerSpellBook;

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
