package it.italiandudes.dnd_visualizer.utils;

import it.italiandudes.dnd_visualizer.DnD_Visualizer;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

@SuppressWarnings("unused")
public final class Defs {

    // Jar App Position
    public static final String JAR_POSITION = new File(DnD_Visualizer.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getAbsolutePath();

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

        // JSON
        public static final class JSON {
            public static final String JSON_CLIENT_SETTINGS = "client_settings.json";
            public static final String DEFAULT_JSON_CLIENT_SETTINGS = "/json/" + JSON_CLIENT_SETTINGS;
        }

        // SQL
        public static final class SQL {
            private static final String SQL_DIR = "/sql/";
            public static final String SQL_SHEET = SQL_DIR + "sheet.sql";
        }
    }

    // Key Parameters for the sheet
    public static final class KeyParameters {
        public static final String CHARACTER_NAME = "character_name";
        public static final String CLASS = "class";
        public static final String LEVEL = "level";
        public static final String BACKGROUND = "background";
        public static final String PLAYER_NAME = "player_name";
        public static final String RACE = "race";
        public static final String ALIGNMENT = "alignment";
        public static final String EXP = "exp";
        public static final String INSPIRATION_POINTS = "inspiration_points";
        public static final String PROFICIENCY_BONUS = "proficiency_bonus";
        public static final String STRENGTH = "strength";
        public static final String DEXTERITY = "dexterity";
        public static final String CONSTITUTION = "constitution";
        public static final String INTELLIGENCE = "intelligence";
        public static final String WISDOM = "wisdom";
        public static final String CHARISMA = "charisma";
        public static final String MONEY_COPPER = "money_copper";
        public static final String MONEY_SILVER = "money_silver";
        public static final String MONEY_ELECTRUM = "money_electrum";
        public static final String MONEY_GOLD = "money_gold";
        public static final String MONEY_PLATINUM = "money_platinum";
        public static final String MAX_HP = "max_hp";
        public static final String CURRENT_HP = "current_hp";
        public static final String TEMP_HP = "temp_hp";
        public static final String HIT_DICE_FACES = "hit_dice_faces"; // Faces of the hit dice: for example 1d6, the value is "6"
        public static final String CURRENT_HIT_DICE = "current_hit_dice"; // Available hit dice, just to save the value for an eventual save during game
        // public static final String ARMOR_CLASS = "armor_class"; // I'm probably going to hate myself later, but I'll try to calculate it
        public static final String SPEED = "speed";
        public static final String SUCCESS_DEATH_SAVES = "success_death_saves";
        public static final String FAILURES_DEATH_SAVES = "failures_death_saves";
        public static final String CULT_NAME = "cult_name";
        public static final String CULT_SYMBOL = "cult_symbol";
        public static final String AGE = "age";
        public static final String HEIGHT = "height";
        public static final String WEIGHT = "weight";
        public static final String EYES = "eyes";
        public static final String SKIN = "skin";
        public static final String HAIR = "hair";
        public static final String CHARACTER_IMAGE = "character_image";
        public static final String CHARACTER_BACKSTORY = "character_backstory";
        public static final String SPELLCASTING_ABILITY = "spellcasting_ability";
        public static final String MAX_SLOT_1 = "max_slot_1";
        public static final String MAX_SLOT_2 = "max_slot_2";
        public static final String MAX_SLOT_3 = "max_slot_3";
        public static final String MAX_SLOT_4 = "max_slot_4";
        public static final String MAX_SLOT_5 = "max_slot_5";
        public static final String MAX_SLOT_6 = "max_slot_6";
        public static final String MAX_SLOT_7 = "max_slot_7";
        public static final String MAX_SLOT_8 = "max_slot_8";
        public static final String MAX_SLOT_9 = "max_slot_9";
        public static final String CURRENT_SLOT_1 = "current_slot_1";
        public static final String CURRENT_SLOT_2 = "current_slot_2";
        public static final String CURRENT_SLOT_3 = "current_slot_3";
        public static final String CURRENT_SLOT_4 = "current_slot_4";
        public static final String CURRENT_SLOT_5 = "current_slot_5";
        public static final String CURRENT_SLOT_6 = "current_slot_6";
        public static final String CURRENT_SLOT_7 = "current_slot_7";
        public static final String CURRENT_SLOT_8 = "current_slot_8";
        public static final String CURRENT_SLOT_9 = "current_slot_9";
    }
}
