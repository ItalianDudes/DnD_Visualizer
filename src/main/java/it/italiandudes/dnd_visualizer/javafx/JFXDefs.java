package it.italiandudes.dnd_visualizer.javafx;

@SuppressWarnings("unused")
public final class JFXDefs {

    //FXML Locations
    public static final String FXML_DIR = "/fxml/";
    public static final String STARTUP_FXML = FXML_DIR+"SceneStartup.fxml";
    public static final String LOADING_FXML = FXML_DIR+"SceneLoading.fxml";
    public static final String MENU_FXML = FXML_DIR+"SceneMenu.fxml";
    public static final String FXML_MENU_DIR = FXML_DIR+"menu/";
    public static final String MENU_ITEM_FXML = FXML_MENU_DIR+"SceneMenuItem.fxml";
    public static final String MENU_LANGUAGE_FXML = FXML_MENU_DIR+"SceneMenuLanguage.fxml";

    public static final class MenuChoices {
        private static final String FXML_PREFIX = "/fxml/menu/";
        private static final String[] CHOICE_NAME = {
                "Oggetto",
                "Lingua",
                "Specie",
                "--VISUALIZZA--"
        };
        private static final String[] ASSOCIATED_FXML = {
                FXML_PREFIX +"SceneMenuItem.fxml",
                FXML_PREFIX +"SceneMenuLanguage.fxml",
                FXML_PREFIX+"SceneMenuSpecie.fxml",
                FXML_PREFIX +"SceneMenuViewer.fxml"
        };
        public static String[] getChoiceNames(){
            return CHOICE_NAME;
        }
        public static String[] getFxmlPrefix() {
            return ASSOCIATED_FXML;
        }

        public static final class ItemMenuChoices {

            private static final String FXML_PREFIX = "/fxml/menu/item/";
            private static final String[] CHOICE_NAME = {
                    "Armatura"
            };
            private static final String[] ASSOCIATED_FXML = {
                    FXML_PREFIX+"SceneMenuItemArmor.fxml"
            };
            public static String[] getChoiceNames(){
                return CHOICE_NAME;
            }
            public static String[] getFxmlPrefix(){
                return ASSOCIATED_FXML;
            }

        }

    }

}
