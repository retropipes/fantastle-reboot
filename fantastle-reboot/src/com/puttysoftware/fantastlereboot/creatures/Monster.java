package com.puttysoftware.fantastlereboot.creatures;

import com.puttysoftware.fantastlereboot.ai.AIRoutine;
import com.puttysoftware.fantastlereboot.ai.RandomAIRoutinePicker;
import com.puttysoftware.fantastlereboot.creatures.faiths.Faith;
import com.puttysoftware.fantastlereboot.creatures.faiths.FaithManager;
import com.puttysoftware.fantastlereboot.items.Equipment;
import com.puttysoftware.fantastlereboot.items.EquipmentFactory;
import com.puttysoftware.fantastlereboot.items.EquipmentSlotConstants;
import com.puttysoftware.fantastlereboot.items.Shop;
import com.puttysoftware.fantastlereboot.resourcemanagers.GraphicsManager;
import com.puttysoftware.fantastlereboot.resourcemanagers.MonsterNames;
import com.puttysoftware.fantastlereboot.spells.SpellBookManager;
import com.puttysoftware.images.BufferedImageIcon;
import com.puttysoftware.randomrange.RandomRange;

public class Monster extends Creature {
    // Fields
    private final int levelDifference;
    private final int perfectBonusGold;
    private String type;
    private Element element;
    private static final double MINIMUM_EXPERIENCE_RANDOM_VARIANCE = -5.0 / 2.0;
    private static final double MAXIMUM_EXPERIENCE_RANDOM_VARIANCE = 5.0 / 2.0;
    private static final int BASE_FIGHTS_PER_LEVEL = 11;
    private static final double FIGHTS_PER_LEVEL_INCREMENT = 1.25;

    // Constructors
    public Monster() {
        super();
        final PlayerCharacter playerCharacter = PCManager.getPlayer();
        final int newLevel = playerCharacter.getMonsterLevel();
        this.setLevel(newLevel);
        this.levelDifference = newLevel - playerCharacter.getLevel();
        this.setVitality(this.getInitialVitality());
        this.setCurrentHP(this.getMaximumHP());
        this.setIntelligence(this.getInitialIntelligence());
        this.setCurrentMP(this.getMaximumMP());
        this.setStrength(this.getInitialStrength());
        this.setBlock(this.getInitialBlock());
        this.setAgility(this.getInitialAgility());
        this.setLuck(this.getInitialLuck());
        this.setGold(this.getInitialGold());
        this.setExperience(this.getInitialExperience());
        this.perfectBonusGold = this.getInitialPerfectBonusGold();
        this.image = this.getInitialImage();
        this.getInitialSpellBook();
        this.setAI(Monster.getInitialAI());
        // Create equipment
        final Equipment weapon = EquipmentFactory.createMonsterWeapon(newLevel);
        final Equipment helmet = EquipmentFactory.createArmor(newLevel,
                EquipmentSlotConstants.SLOT_HEAD);
        final Equipment necklace = EquipmentFactory.createArmor(newLevel,
                EquipmentSlotConstants.SLOT_NECK);
        final Equipment shield = EquipmentFactory.createArmor(newLevel,
                EquipmentSlotConstants.SLOT_OFFHAND);
        final Equipment robe = EquipmentFactory.createArmor(newLevel,
                EquipmentSlotConstants.SLOT_BODY);
        final Equipment cape = EquipmentFactory.createArmor(newLevel,
                EquipmentSlotConstants.SLOT_BACK);
        final Equipment shirt = EquipmentFactory.createArmor(newLevel,
                EquipmentSlotConstants.SLOT_UPPER_TORSO);
        final Equipment bracers = EquipmentFactory.createArmor(newLevel,
                EquipmentSlotConstants.SLOT_ARMS);
        final Equipment gloves = EquipmentFactory.createArmor(newLevel,
                EquipmentSlotConstants.SLOT_HANDS);
        final Equipment ring = EquipmentFactory.createArmor(newLevel,
                EquipmentSlotConstants.SLOT_FINGERS);
        final Equipment belt = EquipmentFactory.createArmor(newLevel,
                EquipmentSlotConstants.SLOT_LOWER_TORSO);
        final Equipment pants = EquipmentFactory.createArmor(newLevel,
                EquipmentSlotConstants.SLOT_LEGS);
        final Equipment boots = EquipmentFactory.createArmor(newLevel,
                EquipmentSlotConstants.SLOT_FEET);
        // Equip it
        this.getItems().equipOneHandedWeapon(weapon, true);
        this.getItems().equipArmor(helmet);
        this.getItems().equipArmor(necklace);
        this.getItems().equipArmor(shield);
        this.getItems().equipArmor(robe);
        this.getItems().equipArmor(cape);
        this.getItems().equipArmor(shirt);
        this.getItems().equipArmor(bracers);
        this.getItems().equipArmor(gloves);
        this.getItems().equipArmor(ring);
        this.getItems().equipArmor(belt);
        this.getItems().equipArmor(pants);
        this.getItems().equipArmor(boots);
    }

    // Methods
    public int getPerfectBonusGold() {
        return this.perfectBonusGold;
    }

    @Override
    public String getName() {
        return this.element.getName() + " " + this.type;
    }

    @Override
    public int getLevelDifference() {
        return this.levelDifference;
    }

    // Helper Methods
    private static AIRoutine getInitialAI() {
        return RandomAIRoutinePicker.getNextRoutine();
    }

    private int getInitialStrength() {
        final PlayerCharacter playerCharacter = PCManager.getPlayer();
        final RandomRange r1 = new RandomRange(0,
                playerCharacter.getPermanentAttack());
        final RandomRange r2 = new RandomRange(0,
                this.getLevel() + playerCharacter.getStrength());
        return (int) (r1.generate()
                + r2.generate() * this.adjustForLevelDifference());
    }

    private int getInitialBlock() {
        final PlayerCharacter playerCharacter = PCManager.getPlayer();
        final RandomRange r1 = new RandomRange(0,
                playerCharacter.getPermanentDefense());
        final RandomRange r2 = new RandomRange(0,
                this.getLevel() + playerCharacter.getBlock());
        return (int) (r1.generate()
                + r2.generate() * this.adjustForLevelDifference());
    }

    private long getInitialExperience() {
        final PlayerCharacter playerCharacter = PCManager.getPlayer();
        int min, max;
        min = (int) (playerCharacter.getLevel()
                * Monster.MINIMUM_EXPERIENCE_RANDOM_VARIANCE);
        max = (int) (playerCharacter.getLevel()
                * Monster.MAXIMUM_EXPERIENCE_RANDOM_VARIANCE);
        final RandomRange r = new RandomRange(min, max);
        final long toNext = playerCharacter.getExpToNextLevel(
                playerCharacter.getLevel() + 1, playerCharacter.getKills());
        final long toCurrent = playerCharacter.getExpToNextLevel(
                playerCharacter.getLevel(), playerCharacter.getKills());
        final long needed = toNext - toCurrent;
        final long factor = Monster.getFightsPerLevel();
        final long exp = (long) ((needed / factor + r.generate())
                * this.adjustForLevelDifference());
        if (exp < 0L) {
            return 0L;
        } else {
            return exp;
        }
    }

    private int getInitialGold() {
        final PlayerCharacter playerCharacter = PCManager.getPlayer();
        final int needed = Shop.getEquipmentCost(playerCharacter.getLevel() + 1)
                * 24;
        final int factor = Monster.getFightsPerLevel();
        final int averageHealingCost = Shop.getHealingCost(
                playerCharacter.getLevel(),
                playerCharacter.getMaximumHP() * 2 / 3,
                playerCharacter.getMaximumHP(), playerCharacter.getKills());
        final int averageRegenerationCost = Shop.getRegenerationCost(
                playerCharacter.getLevel(), playerCharacter.getMaximumMP() / 2,
                playerCharacter.getMaximumMP(), playerCharacter.getKills());
        final int max = needed / factor * 2 + averageHealingCost
                + averageRegenerationCost;
        final RandomRange r = new RandomRange(0, max);
        return (int) (r.generate() * this.adjustForLevelDifference());
    }

    private int getInitialPerfectBonusGold() {
        final PlayerCharacter playerCharacter = PCManager.getPlayer();
        final int needed = Shop.getEquipmentCost(playerCharacter.getLevel() + 1)
                * 24;
        final int factor = Monster.getFightsPerLevel();
        final int min = needed / factor / 4;
        final int max = needed / factor / 2;
        final RandomRange r = new RandomRange(min, max);
        return (int) (r.generate() * this.adjustForLevelDifference());
    }

    private int getInitialAgility() {
        final PlayerCharacter playerCharacter = PCManager.getPlayer();
        final RandomRange r = new RandomRange(
                (int) (0.5 * playerCharacter.getAgility()),
                (int) (1.5 * playerCharacter.getAgility()));
        return (int) (r.generate() * this.adjustForLevelDifference());
    }

    private int getInitialVitality() {
        final PlayerCharacter playerCharacter = PCManager.getPlayer();
        final RandomRange r = new RandomRange(1, playerCharacter.getVitality());
        return (int) (r.generate() * this.adjustForLevelDifference());
    }

    private int getInitialIntelligence() {
        final PlayerCharacter playerCharacter = PCManager.getPlayer();
        final RandomRange r = new RandomRange(0,
                playerCharacter.getIntelligence());
        return (int) (r.generate() * this.adjustForLevelDifference());
    }

    private int getInitialLuck() {
        final PlayerCharacter playerCharacter = PCManager.getPlayer();
        final RandomRange r = new RandomRange(1, playerCharacter.getLuck());
        return (int) (r.generate() * this.adjustForLevelDifference());
    }

    private void getInitialSpellBook() {
        int bookID = (int) Math.ceil(this.getLevel() / 20.0);
        if (bookID > 4) {
            bookID = 4;
        }
        this.setSpellBook(SpellBookManager.getEnemySpellBookByID(bookID));
    }

    private double adjustForLevelDifference() {
        return this.levelDifference / 4.0 + 1.0;
    }

    private static int getFightsPerLevel() {
        final PlayerCharacter playerCharacter = PCManager.getPlayer();
        return (int) (Monster.BASE_FIGHTS_PER_LEVEL
                + (playerCharacter.getLevel() - 1)
                        * Monster.FIGHTS_PER_LEVEL_INCREMENT);
    }

    @Override
    protected BufferedImageIcon getInitialImage() {
        if (this.getLevel() == 0) {
            return null;
        } else {
            final String[] types = MonsterNames.getAllNames();
            final RandomRange r = new RandomRange(0, types.length - 1);
            this.type = types[r.generate()];
            this.element = new Element(FaithManager.getRandomFaith());
            return GraphicsManager.getMonsterImage(this.type, this.element);
        }
    }

    @Override
    public Faith getFaith() {
        return this.element.getFaith();
    }
}
