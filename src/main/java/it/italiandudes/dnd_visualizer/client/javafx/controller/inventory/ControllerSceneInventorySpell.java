package it.italiandudes.dnd_visualizer.client.javafx.controller.inventory;

import it.italiandudes.dnd_visualizer.client.javafx.Client;
import it.italiandudes.dnd_visualizer.client.javafx.JFXDefs;
import it.italiandudes.dnd_visualizer.client.javafx.alert.ErrorAlert;
import it.italiandudes.dnd_visualizer.client.javafx.alert.InformationAlert;
import it.italiandudes.dnd_visualizer.client.javafx.controller.sheetviewer.TabInventory;
import it.italiandudes.dnd_visualizer.data.enums.Category;
import it.italiandudes.dnd_visualizer.data.enums.Rarity;
import it.italiandudes.dnd_visualizer.data.item.Item;
import it.italiandudes.dnd_visualizer.data.item.Spell;
import it.italiandudes.dnd_visualizer.utils.Defs;
import it.italiandudes.idl.common.ImageHandler;
import it.italiandudes.idl.common.Logger;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

@SuppressWarnings("unused")
public final class ControllerSceneInventorySpell {

    // Attributes
    private Spell spell = null;
    private String imageExtension = null;

    // Graphic Elements
    @FXML private TextField textFieldName;
    @FXML private ComboBox<String> comboBoxRarity;
    @FXML private TextField textFieldMR;
    @FXML private TextField textFieldMA;
    @FXML private TextField textFieldME;
    @FXML private TextField textFieldMO;
    @FXML private TextField textFieldMP;
    @FXML private TextField textFieldLevel;
    @FXML private TextField textFieldType;
    @FXML private TextField textFieldCastTime;
    @FXML private TextField textFieldSpellRange;
    @FXML private TextField textFieldComponents;
    @FXML private TextField textFieldDuration;
    @FXML private TextArea textAreaDescription;
    @FXML private ImageView imageViewItem;

    // Initialize
    @FXML @SuppressWarnings("DuplicatedCode")
    private void initialize() {
        Client.getStage().setResizable(true);
        imageViewItem.setImage(JFXDefs.AppInfo.LOGO);
        comboBoxRarity.setItems(FXCollections.observableList(Rarity.colorNames));
        comboBoxRarity.getSelectionModel().selectFirst();
        comboBoxRarity.buttonCellProperty().bind(Bindings.createObjectBinding(() -> {

            Rarity identifiedRarity = null;
            for (Rarity rarity : Rarity.values()) {
                if (rarity.getTextedRarity().equals(comboBoxRarity.getSelectionModel().getSelectedItem())) {
                    identifiedRarity = rarity;
                }
            }

            if (identifiedRarity == null) return null;

            final Color color = identifiedRarity.getColor();

            // Get the arrow button of the combo-box
            StackPane arrowButton = (StackPane) comboBoxRarity.lookup(".arrow-button");
            return new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setBackground(Background.EMPTY);
                        setText("");
                    } else {
                        setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
                        setText(item);
                    }
                    // Set the background of the arrow also
                    if (arrowButton != null)
                        arrowButton.setBackground(getBackground());
                }
            };
        }, comboBoxRarity.valueProperty()));
        String spellName = TabInventory.getElementName();
        if (spellName != null) {
            initExistingSpell(spellName);
        }
    }

    // EDT
    @FXML
    private void removeImage() {
        imageViewItem.setImage(JFXDefs.AppInfo.LOGO);
        imageExtension = null;
    }
    @FXML @SuppressWarnings("DuplicatedCode")
    private void openFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona un Contenuto Multimediale");
        for (String extension : Defs.Resources.SQL.SUPPORTED_IMAGE_EXTENSIONS) {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(extension, "*."+extension));
        }
        fileChooser.setInitialDirectory(new File(Defs.JAR_POSITION).getParentFile());
        File imagePath;
        try {
            imagePath = fileChooser.showOpenDialog(Client.getStage().getScene().getWindow());
        } catch (IllegalArgumentException e) {
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            imagePath = fileChooser.showOpenDialog(Client.getStage().getScene().getWindow());
        }
        if(imagePath!=null) {
            File finalImagePath = imagePath;
            Service<Void> imageReaderService = new Service<Void>() {
                @Override
                protected Task<Void> createTask() {
                    return new Task<Void>() {
                        @Override
                        protected Void call() {
                            try {
                                BufferedImage img = ImageIO.read(finalImagePath);
                                if (img == null) {
                                    Platform.runLater(() -> new ErrorAlert("ERRORE", "Errore di Lettura", "Impossibile leggere il contenuto selezionato."));
                                    return null;
                                }
                                Platform.runLater(() -> imageViewItem.setImage(SwingFXUtils.toFXImage(img, null)));
                                imageExtension = ImageHandler.getImageExtension(finalImagePath.getAbsolutePath());
                            }catch (IOException e) {
                                Platform.runLater(() -> new ErrorAlert("ERRORE", "Errore di Lettura", "Impossibile leggere il contenuto selezionato."));
                            }
                            return null;
                        }
                    };
                }
            };
            imageReaderService.start();
        }
    }
    @FXML
    private void backToElementList() {
        textFieldName.getScene().getWindow().hide();
    }
    @FXML
    private void save() {
        if (textFieldName.getText().replace(" ", "").isEmpty()) {
            new ErrorAlert("ERRORE", "Errore di Inserimento", "Non e' stato assegnato un nome alla magia.");
            return;
        }
        Service<Void> saveService = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override @SuppressWarnings("DuplicatedCode")
                    protected Void call() {
                        try {
                            double weight;
                            String oldName = null;
                            int mr, ma, me, mo, mp;
                            try {
                                String strMR = textFieldMR.getText();
                                if (strMR == null || strMR.replace(" ", "").isEmpty()) {
                                    mr = 0;
                                } else {
                                    mr = Integer.parseInt(strMR);
                                }
                                String strMA = textFieldMA.getText();
                                if (strMA == null || strMA.replace(" ", "").isEmpty()) {
                                    ma = 0;
                                } else {
                                    ma = Integer.parseInt(strMA);
                                }
                                String strME = textFieldME.getText();
                                if (strME == null || strME.replace(" ", "").isEmpty()) {
                                    me = 0;
                                } else {
                                    me = Integer.parseInt(strME);
                                }
                                String strMO = textFieldMO.getText();
                                if (strMO == null || strMO.replace(" ", "").isEmpty()) {
                                    mo = 0;
                                } else {
                                    mo = Integer.parseInt(strMO);
                                }
                                String strMP = textFieldMP.getText();
                                if (strMP == null || strMP.replace(" ", "").isEmpty()) {
                                    mp = 0;
                                } else {
                                    mp = Integer.parseInt(strMP);
                                }
                                if (mr < 0 || ma < 0 || me < 0 || mo < 0 || mp < 0) throw new NumberFormatException("A number is negative");
                            } catch (NumberFormatException e) {
                                Platform.runLater(() -> new ErrorAlert("ERRORE", "Errore di Inserimento", "Le valute devono essere dei numeri interi positivi!"));
                                return null;
                            }
                            int level;
                            try {
                                String strLevel = textFieldLevel.getText();
                                if (strLevel == null || strLevel.replace(" ", "").isEmpty()) {
                                    level = 0;
                                } else {
                                    level = Integer.parseInt(strLevel);
                                }
                                if (level < 0 || level > 9) throw new NumberFormatException("The level is less than 0 or greater than 9");
                            } catch (NumberFormatException e) {
                                Platform.runLater(() -> new ErrorAlert("ERRORE", "Errore di Inserimento", "Il livello deve essere un numero intero positivo compreso tra 0 e 9!"));
                                return null;
                            }
                            if (spell == null) {
                                if (Item.checkIfExist(textFieldName.getText())) {
                                    Platform.runLater(() -> new ErrorAlert("ERRORE", "Errore di Inserimento", "Esiste gia' qualcosa con questo nome registrato!"));
                                    return null;
                                }
                                Item item = new Item(
                                        null,
                                        imageViewItem.getImage(),
                                        imageExtension,
                                        textFieldName.getText(),
                                        mr,
                                        ma,
                                        me,
                                        mo,
                                        mp,
                                        textAreaDescription.getText(),
                                        comboBoxRarity.getSelectionModel().getSelectedItem(),
                                        Category.SPELL,
                                        0,
                                        1
                                );
                                spell = new Spell(
                                        item,
                                        null,
                                        level,
                                        textFieldType.getText(),
                                        textFieldCastTime.getText(),
                                        textFieldSpellRange.getText(),
                                        textFieldComponents.getText(),
                                        textFieldDuration.getText()
                                );
                            } else {
                                oldName = spell.getName();
                                Item item = new Item(
                                        spell.getItemID(),
                                        imageViewItem.getImage(),
                                        imageExtension,
                                        textFieldName.getText(),
                                        mr,
                                        ma,
                                        me,
                                        mo,
                                        mp,
                                        textAreaDescription.getText(),
                                        comboBoxRarity.getSelectionModel().getSelectedItem(),
                                        Category.SPELL,
                                        0,
                                        1
                                );
                                spell = new Spell(
                                        item,
                                        spell.getSpellID(),
                                        level,
                                        textFieldType.getText(),
                                        textFieldCastTime.getText(),
                                        textFieldSpellRange.getText(),
                                        textFieldComponents.getText(),
                                        textFieldDuration.getText()
                                );
                            }

                            spell.saveIntoDatabase(oldName);
                            Platform.runLater(() -> new InformationAlert("SUCCESSO", "Aggiornamento Dati", "Aggiornamento dei dati effettuato con successo!"));
                        } catch (Exception e) {
                            Logger.log(e);
                            Platform.runLater(() -> {
                                new ErrorAlert("ERRORE", "Errore di Salvataggio", "Si e' verificato un errore durante il salvataggio dei dati");
                                textFieldName.getScene().getWindow().hide();
                            });
                        }
                        return null;
                    }
                };
            }
        };

        saveService.start();
    }

    // Methods
    private void initExistingSpell(@NotNull final String spellName) {
        Service<Void> itemInitializerService = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override @SuppressWarnings("DuplicatedCode")
                    protected Void call() throws Exception {
                        try {

                            spell = new Spell(spellName);

                            imageExtension = spell.getImageExtension();
                            int CC = spell.getCostCopper();
                            int CP = CC / 1000;
                            CC -= CP * 1000;
                            int CG = CC / 100;
                            CC -= CG * 100;
                            int CE = CC / 50;
                            CC -= CE * 50;
                            int CS = CC / 10;
                            CC -= CS * 10;

                            BufferedImage bufferedImage = null;
                            try {
                                if (spell.getBase64image() != null && imageExtension != null) {
                                    byte[] imageBytes = Base64.getDecoder().decode(spell.getBase64image());
                                    ByteArrayInputStream imageStream = new ByteArrayInputStream(imageBytes);
                                    bufferedImage = ImageIO.read(imageStream);
                                } else if (spell.getBase64image() != null && imageExtension == null) {
                                    throw new IllegalArgumentException("Image without declared extension");
                                }
                            } catch (IllegalArgumentException e) {
                                Logger.log(e);
                                spell.setBase64image(null);
                                spell.setImageExtension(null);
                                Platform.runLater(() -> new ErrorAlert("ERRORE", "Errore di lettura", "L'immagine ricevuta dal database non è leggibile"));
                                return null;
                            }

                            int finalCC = CC;
                            BufferedImage finalBufferedImage = bufferedImage;
                            Platform.runLater(() -> {

                                textFieldName.setText(spell.getName());
                                comboBoxRarity.getSelectionModel().select(spell.getRarity().getTextedRarity());
                                textFieldMR.setText(String.valueOf(finalCC));
                                textFieldMA.setText(String.valueOf(CS));
                                textFieldME.setText(String.valueOf(CE));
                                textFieldMO.setText(String.valueOf(CG));
                                textFieldMP.setText(String.valueOf(CP));
                                textAreaDescription.setText(spell.getDescription());
                                if (finalBufferedImage != null && imageExtension != null) {
                                    imageViewItem.setImage(SwingFXUtils.toFXImage(finalBufferedImage, null));
                                } else {
                                    imageViewItem.setImage(JFXDefs.AppInfo.LOGO);
                                }

                                textFieldCastTime.setText(spell.getCastTime());
                                textFieldDuration.setText(spell.getDuration());
                                textFieldComponents.setText(spell.getComponents());
                                textFieldLevel.setText(String.valueOf(spell.getLevel()));
                                textFieldSpellRange.setText(spell.getRange());
                                textFieldType.setText(spell.getType());
                            });

                        } catch (Exception e) {
                            Logger.log(e);
                            Platform.runLater(() -> {
                                new ErrorAlert("ERRORE", "Errore di Lettura", "Impossibile leggere l'elemento dal database");
                                textFieldName.getScene().getWindow().hide();
                            });
                            throw e;
                        }
                        return null;
                    }
                };
            }
        };

        itemInitializerService.start();
    }
}
