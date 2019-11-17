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
    case JobConstants.JOB_ASSASSIN:
      return new AssassinSpellBook();
    case JobConstants.JOB_BASHER:
      return new BasherSpellBook();
    case JobConstants.JOB_CURER:
      return new CurerSpellBook();
    case JobConstants.JOB_DESTROYER:
      return new DestroyerSpellBook();
    case JobConstants.JOB_ECLECTIC:
      return new EclecticSpellBook();
    case JobConstants.JOB_FOOL:
      return new FoolSpellBook();
    case JobConstants.JOB_GURU:
      return new GuruSpellBook();
    case JobConstants.JOB_HUNTER:
      return new HunterSpellBook();
    case JobConstants.JOB_JUMPER:
      return new JumperSpellBook();
    case JobConstants.JOB_KNIGHT:
      return new KnightSpellBook();
    case JobConstants.JOB_LOCKSMITH:
      return new LocksmithSpellBook();
    case JobConstants.JOB_MONK:
      return new MonkSpellBook();
    case JobConstants.JOB_NINJA:
      return new NinjaSpellBook();
    case JobConstants.JOB_OVERSEER:
      return new OverseerSpellBook();
    case JobConstants.JOB_PICKPOCKET:
      return new PickpocketSpellBook();
    case JobConstants.JOB_ROGUE:
      return new RogueSpellBook();
    case JobConstants.JOB_SPY:
      return new SpySpellBook();
    case JobConstants.JOB_TEACHER:
      return new TeacherSpellBook();
    case JobConstants.JOB_WARLOCK:
      return new WarlockSpellBook();
    case JobConstants.JOB_YELLER:
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
