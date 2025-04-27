package it.italiandudes.dnd_visualizer.utils;

import it.italiandudes.dnd_visualizer.DnD_Visualizer;
import it.italiandudes.idl.common.TargetPlatform;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;

public final class Defs {

    // App File Name
    public static final String APP_FILE_NAME = "DnD_Visualizer";

    // Current Platform
    @Nullable public static final TargetPlatform CURRENT_PLATFORM = TargetPlatform.getCurrentPlatform();

    // Logger Context
    public static final String LOGGER_CONTEXT = "D&D Visualizer";

    // DB Versions
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

        // Project Resources Root
        public static final String PROJECT_RESOURCES_ROOT = "/it/italiandudes/dnd_visualizer/resources/";

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
            public static final String CLIENT_SETTINGS = "settings.json";
            public static final String DEFAULT_JSON_SETTINGS = PROJECT_RESOURCES_ROOT + "json/" + CLIENT_SETTINGS;
        }

        // Images
        public static final class Image {
            private static final String IMAGE_DIR = PROJECT_RESOURCES_ROOT + "image/";
            public static final String IMAGE_CA = IMAGE_DIR + "sheet/character/ac_full.png";
            public static final String IMAGE_GOLDEN_CA = IMAGE_DIR + "sheet/character/ac_overflow.png";
            public static final String IMAGE_DARK_MODE = IMAGE_DIR + "dark_mode.png";
            public static final String IMAGE_LIGHT_MODE = IMAGE_DIR + "light_mode.png";
            public static final String IMAGE_TICK = IMAGE_DIR + "tick.png";
            public static final String IMAGE_CROSS = IMAGE_DIR + "cross.png";
            public static final String IMAGE_WUMPUS = IMAGE_DIR + "wumpus.png";
            public static final String IMAGE_NO_WUMPUS = IMAGE_DIR + "no_wumpus.png";
            public static final class MapMarkers {
                private static final String MAP_MARKERS_DIR = IMAGE_DIR + "markers/";
                public static final class Elements {
                    private static final String ELEMENTS_DIR = MAP_MARKERS_DIR + "elements/";
                    public static final String ELEMENT_ADDON = ELEMENTS_DIR + "addon.png";
                    public static final String ELEMENT_ARMOR = ELEMENTS_DIR + "armor.png";
                    public static final String ELEMENT_ITEM = ELEMENTS_DIR + "item.png";
                    public static final String ELEMENT_SPELL = ELEMENTS_DIR + "spell.png";
                    public static final String ELEMENT_WEAPON = ELEMENTS_DIR + "weapon.png";
                }
                public static final class Entities {
                    private static final String ENTITIES_DIR = MAP_MARKERS_DIR + "entities/";
                    public static final String ENTITY_ALLY = ENTITIES_DIR + "ally.png";
                    public static final String ENTITY_BOSS = ENTITIES_DIR + "boss.png";
                    public static final String ENTITY_ENEMY = ENTITIES_DIR + "enemy.png";
                    public static final String ENTITY_NPC = ENTITIES_DIR + "npc.png";
                    public static final String ENTITY_PLAYER = ENTITIES_DIR + "player.png";
                    public static final String ENTITY_STRONG_ENEMY = ENTITIES_DIR + "strong_enemy.png";
                }
                public static final class Objectives {
                    private static final String OBJECTIVES_DIR = MAP_MARKERS_DIR + "objectives/";
                    public static final String OBJECTIVE_MISSION = OBJECTIVES_DIR + "mission.png";
                    public static final String OBJECTIVE_TIME_MISSION = OBJECTIVES_DIR + "time_mission.png";
                }
                public static final class PointsOfInterest {
                    private static final String POI_DIR = MAP_MARKERS_DIR + "points_of_interest/";
                    public static final String POI_MARKET = POI_DIR + "market.png";
                    public static final String POI_TAVERN = POI_DIR + "tavern.png";
                    public static final String POI_OFFICE = POI_DIR + "office.png";
                    public static final String POI_HOME = POI_DIR + "home.png";
                }
                public static final String MARKER_GENERIC = MAP_MARKERS_DIR + "generic.png";
                public static final String MARKER_COIN_DEPOSIT = MAP_MARKERS_DIR + "coin_deposit.png";
            }
        }

        // SQL
        public static final class SQL {
            private static final String SQL_DIR = PROJECT_RESOURCES_ROOT + "sql/";
            public static final String SQL_COMMON = SQL_DIR + "common.sql";
            public static final String SQL_SHEET = SQL_DIR + "sheet.sql";
            public static final String SQL_CAMPAIGN = SQL_DIR + "campaign.sql";
            public static String[] SUPPORTED_IMAGE_EXTENSIONS = {"png", "jpg", "jpeg"};
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
