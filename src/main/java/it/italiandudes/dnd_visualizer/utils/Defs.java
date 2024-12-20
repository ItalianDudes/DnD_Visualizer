package it.italiandudes.dnd_visualizer.utils;

import it.italiandudes.dnd_visualizer.DnD_Visualizer;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;

public final class Defs {

    // App File Name
    public static final String APP_FILE_NAME = "DnD_Visualizer";

    // DB Version
    public static final String SHEET_DB_VERSION = "1.4";
    public static final String CAMPAIGN_DB_VERSION = "0.0";

    // Jar App Position
    public static final String JAR_POSITION;
    static {
        try {
            JAR_POSITION = new File(DnD_Visualizer.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getAbsolutePath();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    // JSON Settings
    public static final class SettingsKeys {
        public static final String ENABLE_DARK_MODE = "enableDarkMode";
        public static final String ENABLE_LOAD = "enableLoad";
        public static final String ENABLE_PASSIVE_LOAD = "enablePassiveLoad";
        public static final String COINS_INCREASE_LOAD = "coinsIncreaseLoad";
        public static final String ENABLE_DISCORD_RICH_PRESENCE = "enableDiscordRichPresence";
        public static final String ENABLE_EVENT_THEME = "enableEventTheme";
        public static final String ENABLE_TUTORIAL = "enableTutorial";
    }

    // Resources Location
    public static final class Resources {

        //Resource Getters
        public static URL get(@NotNull final String resourceConst) {
            return Objects.requireNonNull(DnD_Visualizer.class.getResource(resourceConst));
        }
        public static InputStream getAsStream(@NotNull final String resourceConst) {
            return Objects.requireNonNull(DnD_Visualizer.class.getResourceAsStream(resourceConst));
        }

        // Sheet Extension
        public static final String SHEET_EXTENSION = "dnd5e.sheet";

        // Campaign Extension
        public static final String CAMPAIGN_EXTENSION = "dnd5e.campaign";

        // Inventory Extension
        public static final String INVENTORY_EXTENSION = "dnd5e.inventory";

        // Element Extension
        public static final String ELEMENT_EXTENSION = "dnd5e.element";

        // JSON
        public static final class JSON {
            public static final String JSON_CLIENT_SETTINGS = "client_settings.json";
            public static final String DEFAULT_JSON_CLIENT_SETTINGS = "/json/" + JSON_CLIENT_SETTINGS;
        }

        // Images
        public static final class Image {
            private static final String IMAGE_DIR = "/image/";
            public static final String IMAGE_CA = IMAGE_DIR + "sheet/character/ac_full.png";
            public static final String IMAGE_GOLDEN_CA = IMAGE_DIR + "sheet/character/ac_overflow.png";
            public static final String IMAGE_DARK_MODE = IMAGE_DIR + "dark_mode.png";
            public static final String IMAGE_LIGHT_MODE = IMAGE_DIR + "light_mode.png";
            public static final String IMAGE_TICK = IMAGE_DIR + "tick.png";
            public static final String IMAGE_CROSS = IMAGE_DIR + "cross.png";
            public static final String IMAGE_WUMPUS = IMAGE_DIR + "wumpus.png";
            public static final String IMAGE_NO_WUMPUS = IMAGE_DIR + "no_wumpus.png";
        }

        // SVG
        public static final class SVG {
            private static final String SVG_DIR = "/svg/";
            public static final class Elements {
                private static final String ELEMENTS_DIR = SVG_DIR + "elements/";
                public static final String ELEMENT_ADDON = ELEMENTS_DIR + "addon.svg";
                public static final String ELEMENT_ARMOR = ELEMENTS_DIR + "armor.svg";
                public static final String ELEMENT_ITEM = ELEMENTS_DIR + "item.svg";
                public static final String ELEMENT_SPELL = ELEMENTS_DIR + "spell.svg";
                public static final String ELEMENT_WEAPON = ELEMENTS_DIR + "weapon.svg";
            }
            public static final class Entities {
                private static final String ENTITIES_DIR = SVG_DIR + "entities/";
                public static final String ENTITY_PLAYER = ENTITIES_DIR + "player.svg";
                public static final String ENTITY_NPC = ENTITIES_DIR + "npc.svg";
                public static final String ENTITY_ENEMY = ENTITIES_DIR + "enemy.svg";
                public static final String ENTITY_STRONG_ENEMY = ENTITIES_DIR + "strong_enemy.svg";
                public static final String ENTITY_BOSS = ENTITIES_DIR + "boss.svg";
            }
            public static final class Objectives {
                private static final String OBJECTIVES_DIR = SVG_DIR + "objectives/";
                public static final String OBJECTIVE_MISSION = OBJECTIVES_DIR + "mission.svg";
                public static final String OBJECTIVE_TIME_MISSION = OBJECTIVES_DIR + "time_mission.svg";
            }
            public static final class PointsOfInterest {
                private static final String POI_DIR = SVG_DIR + "points_of_interest/";
                public static final String POI_MARKET = POI_DIR + "market.svg";
                public static final String POI_TAVERN = POI_DIR + "tavern.svg";
                public static final String POI_OFFICE = POI_DIR + "office.svg";
                public static final String POI_HOME = POI_DIR + "home.svg";
            }
            public static final String SVG_COIN_DEPOSIT = SVG_DIR + "coin_deposit.svg";
        }

        // SQL
        public static final class SQL {
            private static final String SQL_DIR = "/sql/";
            public static final String SQL_COMMON = SQL_DIR + "common.sql";
            public static final String SQL_SHEET = SQL_DIR + "sheet.sql";
            public static final String SQL_CAMPAIGN = SQL_DIR + "campaign.sql";
            public static String[] SUPPORTED_IMAGE_EXTENSIONS = {"png", "jpg", "jpeg"};
        }
    }

    // Key Parameters for the sheet
    public static final class KeyParameters {
        public static final String DB_VERSION = "db_version";
        public static final String CHARACTER_IMAGE = "character_image";
        public static final String CHARACTER_IMAGE_EXTENSION = "character_image_extension";
        public static final class TabCharacter {
            public static final String CHARACTER_NAME = "character_name";
            public static final String CLASS = "class";
            public static final String LEVEL = "level";
            public static final String BACKGROUND = "background";
            public static final String PLAYER_NAME = "player_name";
            public static final String RACE = "race";
            public static final String ALIGNMENT = "alignment";
            public static final String EXP = "exp";
            public static final String MAX_HP = "max_hp";
            public static final String CURRENT_HP = "current_hp";
            public static final String TEMP_HP = "temp_hp";
            public static final String CURRENT_LIFE_DICES = "current_life_dices";
            public static final String LIFE_DICE_FACES = "life_dice_faces";
            public static final String PROFICIENCY_BONUS = "proficiency_bonus";
            public static final String INSPIRATION = "inspiration";
            public static final String SPEED = "speed";
            public static final String PERSONALITY_TRAITS = "personality_traits";
            public static final String IDEALS = "ideals";
            public static final String BONDS = "bonds";
            public static final String FLAWS = "flaws";
            public static final String CA_STRENGTH = "ca_strength";
            public static final String CA_DEXTERITY = "ca_dexterity";
            public static final String CA_CONSTITUTION = "ca_constitution";
            public static final String CA_INTELLIGENCE = "ca_intelligence";
            public static final String CA_WISDOM = "ca_wisdom";
            public static final String CA_CHARISMA = "ca_charisma";
        }
        public static final class TabAbility {
            public static final String STRENGTH = "strength";
            public static final String DEXTERITY = "dexterity";
            public static final String CONSTITUTION = "constitution";
            public static final String INTELLIGENCE = "intelligence";
            public static final String WISDOM = "wisdom";
            public static final String CHARISMA = "charisma";
            public static final String PROF_ST_STRENGTH = "prof_st_strength";
            public static final String PROF_ST_DEXTERITY = "prof_st_dexterity";
            public static final String PROF_ST_CONSTITUTION = "prof_st_constitution";
            public static final String PROF_ST_INTELLIGENCE = "prof_st_intelligence";
            public static final String PROF_ST_WISDOM = "prof_st_wisdom";
            public static final String PROF_ST_CHARISMA = "prof_st_charisma";
            public static final String PROF_ATHLETICS = "prof_athletics";
            public static final String PROF_ACROBATICS = "prof_acrobatics";
            public static final String PROF_STEALTH = "prof_stealth";
            public static final String PROF_SLEIGHT_OF_HAND = "prof_sleight_of_hand";
            public static final String PROF_ARCANA = "prof_arcana";
            public static final String PROF_INVESTIGATION = "prof_investigation";
            public static final String PROF_NATURE = "prof_nature";
            public static final String PROF_RELIGION = "prof_religion";
            public static final String PROF_HISTORY = "prof_history";
            public static final String PROF_ANIMAL_HANDLING = "prof_animal_handling";
            public static final String PROF_INSIGHT = "prof_insight";
            public static final String PROF_MEDICINE = "prof_medicine";
            public static final String PROF_PERCEPTION = "prof_perception";
            public static final String PROF_SURVIVAL = "prof_survival";
            public static final String PROF_DECEPTION = "prof_deception";
            public static final String PROF_INTIMIDATION = "prof_intimidation";
            public static final String PROF_PERFORMANCE = "prof_performance";
            public static final String PROF_PERSUASION = "prof_persuasion";
        }
        public static final class TabTraitsAndCompetences {
            public static final String FEATURES_AND_TRAITS = "features_and_traits";
            public static final String LANGUAGES = "languages";
            public static final String ARMORS = "armors";
            public static final String WEAPONS = "weapons";
            public static final String TOOLS = "tools";
        }
        public static final class TabInventory {
            public static final String COPPER_COINS = "copper_coins";
            public static final String SILVER_COINS = "silver_coins";
            public static final String ELECTRUM_COINS = "electrum_coins";
            public static final String GOLD_COINS = "gold_coins";
            public static final String PLATINUM_COINS = "platinum_coins";
        }
        public static final class TabSpells {
            public static final String CASTER_STAT = "caster_stat";
            public static final String SLOT_TOTAL_1 = "slot_total_1";
            public static final String SLOT_TOTAL_2 = "slot_total_2";
            public static final String SLOT_TOTAL_3 = "slot_total_3";
            public static final String SLOT_TOTAL_4 = "slot_total_4";
            public static final String SLOT_TOTAL_5 = "slot_total_5";
            public static final String SLOT_TOTAL_6 = "slot_total_6";
            public static final String SLOT_TOTAL_7 = "slot_total_7";
            public static final String SLOT_TOTAL_8 = "slot_total_8";
            public static final String SLOT_TOTAL_9 = "slot_total_9";
            public static final String SLOT_SPENT_1 = "slot_spent_1";
            public static final String SLOT_SPENT_2 = "slot_spent_2";
            public static final String SLOT_SPENT_3 = "slot_spent_3";
            public static final String SLOT_SPENT_4 = "slot_spent_4";
            public static final String SLOT_SPENT_5 = "slot_spent_5";
            public static final String SLOT_SPENT_6 = "slot_spent_6";
            public static final String SLOT_SPENT_7 = "slot_spent_7";
            public static final String SLOT_SPENT_8 = "slot_spent_8";
            public static final String SLOT_SPENT_9 = "slot_spent_9";
        }
        public static final class TabPhysicalDescription {
            public static final String AGE = "age";
            public static final String HEIGHT = "height";
            public static final String WEIGHT = "weight";
            public static final String EYES = "eyes";
            public static final String SKIN = "skin";
            public static final String HAIR = "hair";
            public static final String PHYSICAL_DESCRIPTION = "physical_description";
        }
        public static final class TabStory {
            public static final String CHARACTER_BACKSTORY = "character_backstory";
            public static final String CULT_IMAGE = "cult_image";
            public static final String CULT_IMAGE_EXTENSION = "cult_image_extension";
            public static final String CULT_DESCRIPTION = "cult_description";
            public static final String ALLIES_AND_ORGANIZATIONS = "allies_and_organizations";
        }
    }

    // CA (not a real max, just used to calculate its percentage and fill the rectangle)
    public static final double MAX_CA = 30.0;

    // Weight Constants
    public static final class Load {
        public static final double COIN_LOAD_DIVISOR = 1000.0;
        public static final double TOTAL_PASSIVE_LOAD_MULTIPLIER = 10;
        public static final double TOTAL_ACTIVE_LOAD_MULTIPLIER = 5;
    }
}
