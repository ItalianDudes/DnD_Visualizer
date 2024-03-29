package it.italiandudes.dnd_visualizer.client.javafx;

import it.italiandudes.dnd_visualizer.utils.Defs;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.stage.Screen;

@SuppressWarnings("unused")
public final class JFXDefs {

    //App Info
    public static final class AppInfo {
        public static final String NAME = "D&D Visualizer";
        public static final Image LOGO = new Image(Defs.Resources.get(Resources.Image.IMAGE_LOGO).toString());
    }

    // System Info
    public static final class SystemGraphicInfo {
        public static final Rectangle2D SCREEN_RESOLUTION = Screen.getPrimary().getBounds();
        public static final double SCREEN_WIDTH = SCREEN_RESOLUTION.getWidth();
        public static final double SCREEN_HEIGHT = SCREEN_RESOLUTION.getHeight();
    }

    //Resource Locations
    public static final class Resources {

        //FXML Location
        public static final class FXML {
            private static final String FXML_DIR = "/fxml/";
            public static final String FXML_LOADING = FXML_DIR + "SceneLoading.fxml";
            public static final String FXML_MAIN_MENU = FXML_DIR + "SceneMainMenu.fxml";
            public static final String FXML_SHEET_VIEWER = FXML_DIR + "SceneSheetViewer.fxml";
            public static final String FXML_SETTINGS_EDITOR = FXML_DIR + "SceneSettingsEditor.fxml";
            public static final String FXML_NOTE = FXML_DIR + "SceneNote.fxml";
            public static final String FXML_EFFECT = FXML_DIR + "SceneEffect.fxml";
            public static final String FXML_PRIVILEGE_OR_TRAIT = FXML_DIR + "ScenePrivilegeOrTrait.fxml";
            public static final class Inventory {
                private static final String FXML_INVENTORY_DIR = FXML_DIR + "inventory/";
                public static final String FXML_INVENTORY_ITEM = FXML_INVENTORY_DIR + "SceneInventoryItem.fxml";
                public static final String FXML_INVENTORY_SPELL = FXML_INVENTORY_DIR + "SceneInventorySpell.fxml";
                public static final String FXML_INVENTORY_ARMOR = FXML_INVENTORY_DIR + "SceneInventoryArmor.fxml";
                public static final String FXML_INVENTORY_WEAPON = FXML_INVENTORY_DIR + "SceneInventoryWeapon.fxml";
                public static final String FXML_INVENTORY_ADDON = FXML_INVENTORY_DIR + "SceneInventoryAddon.fxml";
            }
        }

        //GIF Location
        public static final class GIF {
            private static final String GIF_DIR = "/gif/";
            public static final String GIF_LOADING = GIF_DIR+"loading.gif";
            public static final String GIF_BACKGROUND_XMAS = GIF_DIR+"xmas.gif";
        }

        // CSS Location
        public static final class CSS {
            private static final String CSS_DIR = "/css/";
            public static final String CSS_LIGHT_THEME = CSS_DIR + "light_theme.css";
            public static final String CSS_DARK_THEME = CSS_DIR + "dark_theme.css";
        }

        //Image Location
        public static final class Image {
            private static final String IMAGE_DIR = "/image/";
            public static final String IMAGE_LOGO = IMAGE_DIR+"app-logo.png";
            public static final String IMAGE_FILE_EXPLORER = IMAGE_DIR+"file-explorer.png";
            public static final String IMAGE_PLAY = IMAGE_DIR + "play.png";
            public static final String IMAGE_STOP = IMAGE_DIR + "stop.png";
            public static final class Dice {
                private static final String DICE_DIR = IMAGE_DIR + "sheet/dice/";
                public static final String HEAD = DICE_DIR + "head.png";
                public static final String TAIL = DICE_DIR + "tail.png";
                public static final String D4 = DICE_DIR + "d4.png";
                public static final String D6 = DICE_DIR + "d6.png";
                public static final String D8 = DICE_DIR + "d8.png";
                public static final String D10 = DICE_DIR + "d10.png";
                public static final String D12 = DICE_DIR + "d12.png";
                public static final String D20 = DICE_DIR + "d20.png";
            }
        }

    }

}
