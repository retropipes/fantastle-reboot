package com.puttysoftware.fantastlereboot.spells;

import com.puttysoftware.fantastlereboot.creatures.jobs.JobConstants;
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
    case JobConstants.ASSASSIN:
      return new AssassinSpellBook();
    case JobConstants.BASHER:
      return new BasherSpellBook();
    case JobConstants.CURER:
      return new CurerSpellBook();
    case JobConstants.DESTROYER:
      return new DestroyerSpellBook();
    case JobConstants.ECLECTIC:
      return new EclecticSpellBook();
    case JobConstants.FOOL:
      return new FoolSpellBook();
    case JobConstants.GURU:
      return new GuruSpellBook();
    case JobConstants.HUNTER:
      return new HunterSpellBook();
    case JobConstants.JUMPER:
      return new JumperSpellBook();
    case JobConstants.KNIGHT:
      return new KnightSpellBook();
    case JobConstants.LOCKSMITH:
      return new LocksmithSpellBook();
    case JobConstants.MONK:
      return new MonkSpellBook();
    case JobConstants.NINJA:
      return new NinjaSpellBook();
    case JobConstants.OVERSEER:
      return new OverseerSpellBook();
    case JobConstants.PICKPOCKET:
      return new PickpocketSpellBook();
    case JobConstants.ROGUE:
      return new RogueSpellBook();
    case JobConstants.SPY:
      return new SpySpellBook();
    case JobConstants.TEACHER:
      return new TeacherSpellBook();
    case JobConstants.WARLOCK:
      return new WarlockSpellBook();
    case JobConstants.YELLER:
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
