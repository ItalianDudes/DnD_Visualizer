package it.italiandudes.dnd_visualizer.javafx.controller.menu;

import it.italiandudes.dnd_visualizer.db.classes.Cost;
import it.italiandudes.dnd_visualizer.db.classes.Item;
import it.italiandudes.dnd_visualizer.db.enums.Coin;
import it.italiandudes.dnd_visualizer.db.enums.Rarity;
import it.italiandudes.dnd_visualizer.javafx.JFXDefs;
import it.italiandudes.dnd_visualizer.javafx.alert.ErrorAlert;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

@SuppressWarnings("unused")
public class ControllerSceneMenuItem {

    //Attributes
    @FXML
    private BorderPane mainPane;
    @FXML
    private TextField nameField;
    @FXML
    private TextField levelField;
    @FXML
    private TextField weightField;
    @FXML
    private TextField costField;
    @FXML
    private ComboBox<String> rarityField;
    @FXML
    private TextArea loreField;
    @FXML
    private TextArea reqKnowledgeField;
    @FXML
    private TextArea propertiesField;
    private int itemID;

    //Initialize
    @FXML
    public void initialize() {
        AnchorPane.setTopAnchor(mainPane, 0.0);
        AnchorPane.setBottomAnchor(mainPane, 0.0);
        AnchorPane.setRightAnchor(mainPane, 0.0);
        AnchorPane.setLeftAnchor(mainPane, 0.0);
        JFXDefs.MenuChoices.setRarityComboBox(rarityField);
        itemID = -1;
    }

    //EDT & Methods
    public void setDescripedItem(Item item){
        nameField.setText(item.getName());
        loreField.setText(item.getLore());
        reqKnowledgeField.setText(item.getRequiredKnowledge());
        levelField.setText(String.valueOf(item.getRequiredLevel()));
        propertiesField.setText(item.getProperties());
        rarityField.getSelectionModel().select(item.getRarity().name());
        costField.setText(String.valueOf(item.getCost().getCostByCoinType(Coin.COPPER)));
        weightField.setText(String.valueOf(item.getWeight()));
        itemID = item.getItemID();
    }
    public Item getDescribedItem(){
        String name = nameField.getText();
        if(name==null || name.equals("")){
            new ErrorAlert("ERRORE", "Errore durante la lettura dell'oggetto", "Il campo \"Nome\" non deve essere vuoto");
            return null;
        }
        Rarity rarity;
        try {
            rarity = Rarity.valueOf(rarityField.getValue());
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
                itemID,
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
        rarityField.getSelectionModel().selectFirst();
        levelField.clear();
        reqKnowledgeField.clear();
        costField.clear();
        weightField.clear();
        propertiesField.clear();
    }
}
