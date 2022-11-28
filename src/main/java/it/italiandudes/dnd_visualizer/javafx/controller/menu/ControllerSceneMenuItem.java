package it.italiandudes.dnd_visualizer.javafx.controller.menu;

import it.italiandudes.dnd_visualizer.DnD_Visualizer;
import it.italiandudes.dnd_visualizer.db.DBDefs;
import it.italiandudes.dnd_visualizer.db.classes.Cost;
import it.italiandudes.dnd_visualizer.db.classes.Item;
import it.italiandudes.dnd_visualizer.db.enums.Coin;
import it.italiandudes.dnd_visualizer.db.enums.Rarity;
import it.italiandudes.dnd_visualizer.javafx.JFXDefs;
import it.italiandudes.dnd_visualizer.javafx.alert.ErrorAlert;
import it.italiandudes.idl.common.Logger;
import it.italiandudes.idl.common.SQLiteHandler;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Objects;

@SuppressWarnings("unused")
public final class ControllerSceneMenuItem {

    //Attributes
    private static HashMap<String, String> choiceDictionary = null;
    @FXML
    private BorderPane mainPane;
    @FXML
    private Pane nestedFXMLPanel;
    @SuppressWarnings("FieldCanBeLocal")
    private FXMLLoader nestedFXML;
    @FXML
    private ComboBox<String> choiceComboBox;
    @FXML
    private TextField nameField;
    @FXML
    private TextField levelField;
    @FXML
    private TextField weightField;
    @FXML
    private TextField costField;
    @FXML
    private TextField rarityField;
    @FXML
    private TextArea loreField;
    @FXML
    private TextArea reqKnowledgeField;
    @FXML
    private TextArea propertiesField;

    //Initialize
    public void initialize() {
        AnchorPane.setTopAnchor(mainPane, 0.0);
        AnchorPane.setBottomAnchor(mainPane, 0.0);
        AnchorPane.setRightAnchor(mainPane, 0.0);
        AnchorPane.setLeftAnchor(mainPane, 0.0);
        if(nestedFXMLPanel == null) nestedFXMLPanel = new Pane();
        if(choiceDictionary == null){
            choiceDictionary = new HashMap<>();
            for(int i = 0; i< JFXDefs.MenuChoices.ItemMenuChoices.getChoiceNames().length; i++){
                choiceDictionary.put(JFXDefs.MenuChoices.ItemMenuChoices.getChoiceNames()[i], JFXDefs.MenuChoices.ItemMenuChoices.getFxmlPrefix()[i]);
            }
        }
        if(choiceComboBox == null) choiceComboBox = new ComboBox<>();
        choiceComboBox.setItems(FXCollections.observableArrayList(JFXDefs.MenuChoices.ItemMenuChoices.getChoiceNames()));
        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {
                        String query = "SELECT * FROM "+ DBDefs.DB_TABLE_ITEMS + " WHERE name LIKE 'PANINO'";
                        ResultSet s = SQLiteHandler.readDataFromDB(DnD_Visualizer.getDbConnection(), query);
                        Platform.runLater(() -> {
                            try {
                                nameField.setText(s.getString(2));
                                loreField.setText(s.getString(3));
                            }catch (SQLException e){
                                Logger.log(e);
                            }
                        });
                        return null;
                    }
                };
            }
        };
        service.start();
    }

    //Handler
    public Item getDescribedItem(){
        String name = nameField.getText();
        if(name==null || name.equals("")){
            new ErrorAlert("ERRORE", "Errore durante la lettura dell'oggetto", "Il campo \"Nome\" non deve essere vuoto");
            return null;
        }
        Rarity rarity;
        try {
            rarity = Rarity.valueOf(rarityField.getText());
        }catch (IllegalArgumentException e){
            new ErrorAlert("ERRORE", "Errore durante la lettura dell'oggetto", "Il campo \"Rarita'\" deve contenere una rarita' valida");
            return null;
        }
        int level;
        try{
            level = Integer.parseInt(levelField.getText());
            if(level<1) throw new NumberFormatException();
        }catch (NumberFormatException e){
            new ErrorAlert("ERRORE", "Errore durante la lettura dell'oggetto", "Il campo \"Livello Richiesto\" deve contenere un numero intero maggiore o uguale a 1");
            return null;
        }
        int cost;
        try{
            cost = Integer.parseInt(costField.getText());
            if(cost<0) throw new NumberFormatException();
        }catch (NumberFormatException e){
            new ErrorAlert("ERRORE", "Errore durante la lettura dell'oggetto", "Il campo \"Costo\" deve contenere un numero intero maggiore o uguale a 0");
            return null;
        }
        double weight;
        try{
            weight = Double.parseDouble(weightField.getText());
        }catch (NumberFormatException e){
            new ErrorAlert("ERRORE", "Errore durante la lettura dell'oggetto", "Il campo \"Peso\" deve contenere un numero decimale");
            return null;
        }
        return new Item(
                name,
                loreField.getText(),
                rarity,
                level,
                reqKnowledgeField.getText(),
                new Cost(Coin.COPPER, Integer.parseInt(costField.getText())),
                weight,
                propertiesField.getText()
        );
    }
    public void clearAllFields(){
        nameField.clear();
        loreField.clear();
        rarityField.clear();
        levelField.clear();
        reqKnowledgeField.clear();
        costField.clear();
        weightField.clear();
        propertiesField.clear();
    }
    @FXML
    private void changeShowedPane(ActionEvent event){
        String item = choiceComboBox.getValue();
        try {
            nestedFXMLPanel.getChildren().clear();
            nestedFXML = new FXMLLoader(Objects.requireNonNull(getClass().getResource(choiceDictionary.get(item))));
            nestedFXMLPanel.getChildren().add(nestedFXML.load());
        } catch (IOException e) {
            if (Logger.isInitialized()) {
                Logger.log(e);
            } else {
                e.printStackTrace();
            }
        }
    }
}
