package it.italiandudes.dnd_visualizer.javafx.controller.menu;

import it.italiandudes.dnd_visualizer.db.classes.Armor;
import it.italiandudes.dnd_visualizer.db.enums.BodyPart;
import it.italiandudes.dnd_visualizer.javafx.JFXDefs;
import it.italiandudes.dnd_visualizer.javafx.alert.ErrorAlert;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public final class ControllerSceneMenuArmor extends ControllerSceneMenuItem {

    //Attributes
    @FXML private TextField apField;
    @FXML private TextField impactCAField;
    @FXML private TextField cutCAField;
    @FXML private TextField thrustCAField;
    @FXML private ComboBox<String> bodyPartField;

    //Initialize
    @FXML @Override
    public void initialize(){
        super.initialize();
        bodyPartField.setItems(JFXDefs.MenuChoices.getBodyPartComboBoxList());
        bodyPartField.getSelectionModel().selectFirst();
    }

    //EDT & Methods
    public void setDescribedArmor(Armor armor){
        super.setDescripedItem(armor);
        apField.setText(String.valueOf(armor.getAP()));
        impactCAField.setText(String.valueOf(armor.getImpactCA()));
        thrustCAField.setText(String.valueOf(armor.getThrustCA()));
        cutCAField.setText(String.valueOf(armor.getCutCA()));
        bodyPartField.getSelectionModel().select(armor.getBodyPart());
    }
    public Armor getDescribedArmor(){
        int ap;
        try{
            ap = Integer.parseInt(apField.getText());
        }catch (NumberFormatException e){
            new ErrorAlert("ERRORE", "Errore durante la lettura dell'oggetto", "Il campo \"Punti Armatura\" deve contenere un numero intero maggiore o uguale a 0");
            return null;
        }
        int impactCA;
        try{
            impactCA = Integer.parseInt(impactCAField.getText());
        }catch (NumberFormatException e){
            new ErrorAlert("ERRORE", "Errore durante la lettura dell'oggetto", "Il campo \"CA contro Impatto\" deve contenere un numero intero maggiore o uguale a 0");
            return null;
        }
        int thrustCA;
        try{
            thrustCA = Integer.parseInt(thrustCAField.getText());
        }catch (NumberFormatException e){
            new ErrorAlert("ERRORE", "Errore durante la lettura dell'oggetto", "Il campo \"CA contro Perforazione\" deve contenere un numero intero maggiore o uguale a 0");
            return null;
        }
        int cutCA;
        try{
            cutCA = Integer.parseInt(impactCAField.getText());
        }catch (NumberFormatException e){
            new ErrorAlert("ERRORE", "Errore durante la lettura dell'oggetto", "Il campo \"CA contro Taglio\" deve contenere un numero intero maggiore o uguale a 0");
            return null;
        }
        String bodyPart = bodyPartField.getSelectionModel().getSelectedItem();
        return new Armor(super.getDescribedItem(), ap, cutCA, impactCA, thrustCA, bodyPart);
    }
    @Override
    public void clearAllFields() {
        super.clearAllFields();
        apField.clear();
        impactCAField.clear();
        cutCAField.clear();
        thrustCAField.clear();
        bodyPartField.getSelectionModel().selectFirst();
    }
}
