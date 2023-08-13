package it.italiandudes.dnd_visualizer.client.javafx.controller;

import it.italiandudes.dnd_visualizer.client.javafx.Client;
import it.italiandudes.dnd_visualizer.client.javafx.alert.ErrorAlert;
import it.italiandudes.dnd_visualizer.client.javafx.util.StatsCalculator;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public final class ControllerSceneSheetViewer {

    //Graphic Elements
    @FXML private TextField textFieldStrength;
    @FXML private TextField textFieldDexterity;
    @FXML private TextField textFieldConstitution;
    @FXML private TextField textFieldIntelligence;
    @FXML private TextField textFieldWisdom;
    @FXML private TextField textFieldCharisma;
    @FXML private Label labelStrengthModifier;
    @FXML private Label labelDexterityModifier;
    @FXML private Label labelConstitutionModifier;
    @FXML private Label labelIntelligenceModifier;
    @FXML private Label labelWisdomModifier;
    @FXML private Label labelCharismaModifier;

    //Initialize
    @FXML
    private void initialize() {
        Client.getStage().setResizable(false);
    }

    // EDT
    @FXML
    private void removeFocus() {
        Client.getStage().getScene().getRoot().requestFocus();
    }
    @FXML
    private void changeStrengthModifier() {
        try {
            labelStrengthModifier.setText(String.valueOf(StatsCalculator.calculateModifier(textFieldStrength.getText())));
        } catch (NumberFormatException e){
            new ErrorAlert("ERROR", "INPUT ERROR", "This field allows only numbers");
        }
    }
    @FXML
    private void changeDexterityModifier() {
        try {
            labelDexterityModifier.setText(String.valueOf(StatsCalculator.calculateModifier(textFieldDexterity.getText())));
        } catch (NumberFormatException e){
            new ErrorAlert("ERROR", "INPUT ERROR", "This field allows only numbers");
        }
    }
    @FXML
    private void changeConstitutionModifier() {
        try {
            labelConstitutionModifier.setText(String.valueOf(StatsCalculator.calculateModifier(textFieldConstitution.getText())));
        } catch (NumberFormatException e){
            new ErrorAlert("ERROR", "INPUT ERROR", "This field allows only numbers");
        }
    }
    @FXML
    private void changeIntelligenceModifier() {
        try {
            labelIntelligenceModifier.setText(String.valueOf(StatsCalculator.calculateModifier(textFieldIntelligence.getText())));
        } catch (NumberFormatException e){
            new ErrorAlert("ERROR", "INPUT ERROR", "This field allows only numbers");
        }
    }
    @FXML
    private void changeWisdomModifier() {
        try {
            labelWisdomModifier.setText(String.valueOf(StatsCalculator.calculateModifier(textFieldWisdom.getText())));
        } catch (NumberFormatException e){
            new ErrorAlert("ERROR", "INPUT ERROR", "This field allows only numbers");
        }
    }
    @FXML
    private void changeCharismaModifier() {
        try {
            labelCharismaModifier.setText(String.valueOf(StatsCalculator.calculateModifier(textFieldCharisma.getText())));
        } catch (NumberFormatException e){
            new ErrorAlert("ERROR", "INPUT ERROR", "This field allows only numbers");
        }
    }
}
