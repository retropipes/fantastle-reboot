package com.puttysoftware.fantastlereboot.oldcreatures;

import com.puttysoftware.fantastlereboot.ai.BossAIRoutine;
import com.puttysoftware.fantastlereboot.creatures.Creature;
import com.puttysoftware.fantastlereboot.loaders.old.GraphicsManager;
import com.puttysoftware.fantastlereboot.oldcreatures.faiths.Faith;
import com.puttysoftware.fantastlereboot.oldcreatures.faiths.FaithConstants;
import com.puttysoftware.fantastlereboot.oldcreatures.faiths.FaithManager;
import com.puttysoftware.fantastlereboot.spells.BossSpellBook;
import com.puttysoftware.images.BufferedImageIcon;

public class Boss extends Creature {
    // Fields
    public static final int FIGHT_LEVEL = 60;
    private static final double STRENGTH_MULTIPLIER = 1.3;
    private static final double BLOCK_MULTIPLIER = 1.3;
    private static final double VITALITY_MULTIPLIER = 1.6;
    private static final double INTELLIGENCE_MULTIPLIER = 1.3;
    private static final double AGILITY_MULTIPLIER = 1.5;
    private static final double LUCK_MULTIPLIER = 1.4;
    private static final int BONUS_LEVEL = 10;

    // Constructors
    public Boss() {
        super();
        this.setLevel(Boss.FIGHT_LEVEL + Boss.BONUS_LEVEL);
        this.setVitality(Boss.getInitialVitality());
        this.setCurrentHP(this.getMaximumHP());
        this.setIntelligence(Boss.getInitialIntelligence());
        this.setCurrentMP(this.getMaximumMP());
        this.setStrength(Boss.getInitialStrength());
        this.setBlock(Boss.getInitialBlock());
        this.setAgility(Boss.getInitialAgility());
        this.setLuck(Boss.getInitialLuck());
        this.setGold(0);
        this.setExperience(0L);
        this.setSpellBook(new BossSpellBook());
        this.setAI(new BossAIRoutine());
    }

    private static int getInitialStrength() {
        final PlayerCharacter playerCharacter = PCManager.getPlayer();
        return (int) (playerCharacter.getStrength() * Boss.STRENGTH_MULTIPLIER);
    }

    private static int getInitialBlock() {
        final PlayerCharacter playerCharacter = PCManager.getPlayer();
        return (int) (playerCharacter.getBlock() * Boss.BLOCK_MULTIPLIER);
    }

    private static int getInitialVitality() {
        final PlayerCharacter playerCharacter = PCManager.getPlayer();
        return (int) (playerCharacter.getVitality() * Boss.VITALITY_MULTIPLIER);
    }

    private static int getInitialIntelligence() {
        final PlayerCharacter playerCharacter = PCManager.getPlayer();
        return (int) (playerCharacter.getIntelligence()
                * Boss.INTELLIGENCE_MULTIPLIER);
    }

    private static int getInitialAgility() {
        final PlayerCharacter playerCharacter = PCManager.getPlayer();
        return (int) (playerCharacter.getAgility() * Boss.AGILITY_MULTIPLIER);
    }

    private static int getInitialLuck() {
        final PlayerCharacter playerCharacter = PCManager.getPlayer();
        return (int) (playerCharacter.getLuck() * Boss.LUCK_MULTIPLIER);
    }

    // Accessors
    @Override
    public String getFightingWhatString() {
        return "You're fighting The Boss";
    }

    @Override
    protected BufferedImageIcon getInitialImage() {
        return GraphicsManager.getBossImage();
    }

    @Override
    public String getName() {
        return "The Boss";
    }

    @Override
    public Faith getFaith() {
        return FaithManager.getFaith(FaithConstants.FAITH_BLEND);
    }
}