package it.italiandudes.dnd_visualizer.javafx;

import it.italiandudes.dnd_visualizer.db.DBDefs;
import it.italiandudes.dnd_visualizer.db.enums.BodyPart;
import it.italiandudes.dnd_visualizer.db.enums.Rarity;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;

@SuppressWarnings("unused")
public final class JFXDefs {

    //FXML Locations
    public static final String FXML_DIR = "/fxml/";
    public static final String STARTUP_FXML = FXML_DIR+"SceneStartup.fxml";
    public static final String LOADING_FXML = FXML_DIR+"SceneLoading.fxml";
    public static final String MENU_FXML = FXML_DIR+"SceneMenu.fxml";
    public static final String ELEMENT_EDITOR_FXML = FXML_DIR+"SceneElementEditor.fxml";
    public static final String FXML_MENU_DIR = FXML_DIR+"menu/";
    public static final String MENU_ITEM_FXML = FXML_MENU_DIR+"SceneMenuItem.fxml";
    public static final String MENU_LANGUAGE_FXML = FXML_MENU_DIR+"SceneMenuLanguage.fxml";

    public static final class MenuChoices {

        private static final String FXML_PREFIX = "/fxml/menu/";
        public static final String FXML_VIEW = FXML_PREFIX+"SceneMenuViewer.fxml";
        private static final String[] CHOICE_NAME = {
                "Oggetto",
                "Lingua",
                "Specie",
                "Classe",
                "Armatura",
                "Munizioni",
                "Arma Corpo a Corpo",
                "Arma a Distanza",
                "Magia",
        };
        private static final String[] ASSOCIATED_FXML = {
                FXML_PREFIX +"SceneMenuItem.fxml",
                FXML_PREFIX +"SceneMenuLanguage.fxml",
                FXML_PREFIX+"SceneMenuSpecie.fxml",
                FXML_PREFIX+"SceneMenuClass.fxml",
                FXML_PREFIX+"SceneMenuArmor.fxml",
                FXML_PREFIX+"SceneMenuAmmunition.fxml",
                FXML_PREFIX+"SceneMenuMeleeWeapon.fxml",
                FXML_PREFIX+"SceneMenuRangedWeapon.fxml",
                FXML_PREFIX+"SceneMenuSpell.fxml"
        };
        public static String getFXMLbyChoiceName(String choiceName){
            int i = getPosByChoiceName(choiceName);
            if(i==-1) return null;
            return ASSOCIATED_FXML[i];
        }
        public static String getDBTableNameFromChoiceName(String choiceName){
            int i = getPosByChoiceName(choiceName);
            if(i==-1) return null;
            return DBDefs.DB_TABLE_LIST[i];
        }
        private static int getPosByChoiceName(String choiceName){
            for(int i=0;i<CHOICE_NAME.length;i++){
                if(CHOICE_NAME[i].equals(choiceName))
                    return i;
            }
            return -1;
        }
        public static String[] getChoiceNames(){
            return CHOICE_NAME;
        }
        public static String[] getFxmlPrefix() {
            return ASSOCIATED_FXML;
        }
        public static ObservableList<String> getRarityComboBoxList(){
            return FXCollections.observableArrayList(Rarity.RARITY_STRING);
        }
        public static void setRarityComboBox(ComboBox<String> rarityComboBox){
            rarityComboBox.setItems(getRarityComboBoxList());
            rarityComboBox.getSelectionModel().selectFirst();
            rarityComboBox.buttonCellProperty().bind(Bindings.createObjectBinding(() -> new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setBackground(Background.EMPTY);
                        setText("");
                    } else {
                        setBackground(new Background(new BackgroundFill(Rarity.valueOf(item).getColor(), CornerRadii.EMPTY, Insets.EMPTY)));
                        setText(item);
                    }
                    ((StackPane) rarityComboBox.lookup(".arrow-button")).setBackground(getBackground());
                }
            }));
        }
        public static ObservableList<String> getBodyPartComboBoxList(){
            return FXCollections.observableArrayList(BodyPart.BODY_PART_STRING);
        }

    }

}
