package it.italiandudes.dnd_visualizer.javafx.controllers.inventory;

import it.italiandudes.dnd_visualizer.data.enums.Category;
import it.italiandudes.dnd_visualizer.data.enums.Rarity;
import it.italiandudes.dnd_visualizer.data.item.Item;
import it.italiandudes.dnd_visualizer.data.item.ItemContainer;
import it.italiandudes.dnd_visualizer.javafx.Client;
import it.italiandudes.dnd_visualizer.javafx.JFXDefs;
import it.italiandudes.dnd_visualizer.javafx.alerts.ConfirmationAlert;
import it.italiandudes.dnd_visualizer.javafx.alerts.ErrorAlert;
import it.italiandudes.dnd_visualizer.javafx.alerts.InformationAlert;
import it.italiandudes.dnd_visualizer.javafx.controllers.sheetviewer.TabInventory;
import it.italiandudes.dnd_visualizer.javafx.utils.SheetDataHandler;
import it.italiandudes.dnd_visualizer.javafx.utils.UIElementConfigurator;
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
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.stream.Collectors;

public class ControllerSceneInventoryItem {

    // Attributes
    private Item item = null;
    private String imageExtension = null;
    private String itemName = null;
    private ItemContainer itemContainer = null;
    private volatile boolean configurationComplete = false;

    // Methods
    public void setItemContainer(@NotNull final ItemContainer itemContainer) {
        this.itemContainer = itemContainer;
    }
    public void setItemName(@NotNull final String itemName) {
        this.itemName = itemName;
    }
    public void configurationComplete() {
        configurationComplete = true;
    }

    // Graphic Elements
    @FXML private TextField textFieldName;
    @FXML private TextField textFieldWeight;
    @FXML private Spinner<Integer> spinnerQuantity;
    @FXML private ComboBox<String> comboBoxRarity;
    @FXML private TextField textFieldMR;
    @FXML private TextField textFieldMA;
    @FXML private TextField textFieldME;
    @FXML private TextField textFieldMO;
    @FXML private TextField textFieldMP;
    @FXML private TextArea textAreaDescription;
    @FXML private ImageView imageViewItem;
    @FXML private Button buttonExport;
    @FXML private Button buttonSave;

    // Old Values
    private int oldValueQuantity = 0;

    // Initialize
    @FXML @SuppressWarnings("DuplicatedCode")
    private void initialize() {
        setOnChangeTriggers();
        onLostFocusFireActionEvent();
        imageViewItem.setImage(JFXDefs.AppInfo.LOGO);
        comboBoxRarity.setItems(FXCollections.observableList(Rarity.colorNames));
        comboBoxRarity.getSelectionModel().selectFirst();
        spinnerQuantity.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0, 1));
        spinnerQuantity.getEditor().setTextFormatter(UIElementConfigurator.configureNewIntegerTextFormatter());
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
                    setTextFill(Color.BLACK);
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

        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<>() {
                    @Override
                    protected Void call() {
                        while (!configurationComplete) Thread.onSpinWait();

                        if (itemName == null) itemName = TabInventory.getElementName();
                        JSONObject itemStructure = TabInventory.getElementStructure();

                        Platform.runLater(() -> {
                            if (itemName != null) initExistingItem(itemName);
                            else if (itemStructure != null) initExistingItem(itemStructure);
                            else if (item != null) initExistingItem();
                            buttonExport.setDisable(false);
                            buttonSave.setDisable(false);
                        });

                        return null;
                    }
                };
            }
        }.start();
    }

    // OnChange Triggers Setter
    private void setOnChangeTriggers() {
        spinnerQuantity.getEditor().textProperty().addListener((observable -> validateQuantity()));
    }

    // Lost Focus On Action Fire Event
    private void onLostFocusFireActionEvent() {
        spinnerQuantity.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) validateQuantity();
        });
    }

    // EDT
    @SuppressWarnings("DuplicatedCode")
    private void validateQuantity() {
        try {
            int qty = Integer.parseInt(spinnerQuantity.getEditor().getText());
            if (qty < 0) throw new NumberFormatException();
            oldValueQuantity = qty;
            spinnerQuantity.getValueFactory().setValue(qty);
        } catch (NumberFormatException e) {
            spinnerQuantity.getValueFactory().setValue(oldValueQuantity);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "La quantita' deve essere un numero intero maggiore o uguale a 0.");
        }
    }
    @FXML
    private void removeImage() {
        imageViewItem.setImage(JFXDefs.AppInfo.LOGO);
        imageExtension = null;
    }
    @FXML @SuppressWarnings("DuplicatedCode")
    private void openFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona un Contenuto Multimediale");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image", Arrays.stream(Defs.Resources.SQL.SUPPORTED_IMAGE_EXTENSIONS).map(ext -> "*." + ext).collect(Collectors.toList())));
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
            Service<Void> imageReaderService = new Service<>() {
                @Override
                protected Task<Void> createTask() {
                    return new Task<>() {
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
                            } catch (IOException e) {
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
    private void backToSheet() {
        textFieldName.getScene().getWindow().hide();
    }
    @FXML
    private void save() {
        if (textFieldName.getText().replace(" ", "").isEmpty()) {
            new ErrorAlert("ERRORE", "Errore di Inserimento", "Non e' stato assegnato un nome all'oggetto.");
            return;
        }
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<>() {
                    @Override
                    @SuppressWarnings("DuplicatedCode")
                    protected Void call() {
                        try {
                            double weight;
                            try {
                                String textWeight = textFieldWeight.getText();
                                if (textWeight == null || textWeight.replace(" ", "").isEmpty()) {
                                    weight = 0;
                                } else {
                                    weight = Double.parseDouble(textFieldWeight.getText());
                                    if (weight < 0) throw new NumberFormatException("The weight is less than 0");
                                }
                            } catch (NumberFormatException e) {
                                Logger.log(e, Defs.LOGGER_CONTEXT);
                                Platform.runLater(() -> new ErrorAlert("ERRORE", "Errore di Inserimento", "Il peso deve essere un numero a virgola mobile positivo!"));
                                return null;
                            }

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
                                if (mr < 0 || ma < 0 || me < 0 || mo < 0 || mp < 0)
                                    throw new NumberFormatException("A number is negative");
                            } catch (NumberFormatException e) {
                                Platform.runLater(() -> new ErrorAlert("ERRORE", "Errore di Inserimento", "Le valute devono essere dei numeri interi positivi!"));
                                return null;
                            }
                            if (item == null) {
                                if (Item.checkIfExist(textFieldName.getText())) {
                                    Platform.runLater(() -> new ErrorAlert("ERRORE", "Errore di Inserimento", "Esiste gia' qualcosa con questo nome registrato!"));
                                    return null;
                                }
                                item = new Item(
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
                                        Category.ITEM,
                                        weight,
                                        spinnerQuantity.getValue()
                                );
                            } else {
                                oldName = item.getName();
                                item = new Item(
                                        item.getItemID(),
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
                                        Category.ITEM,
                                        weight,
                                        spinnerQuantity.getValue()
                                );
                            }

                            item.saveIntoDatabase(oldName);
                            if (itemContainer != null) itemContainer.setItem(item);
                            Platform.runLater(() -> new InformationAlert("SUCCESSO", "Salvataggio dei Dati", "Salvataggio dei dati completato con successo!"));
                        } catch (Exception e) {
                            Logger.log(e, Defs.LOGGER_CONTEXT);
                            Platform.runLater(() -> {
                                new ErrorAlert("ERRORE", "Errore di Salvataggio", "Si e' verificato un errore durante il salvataggio dei dati");
                                textFieldName.getScene().getWindow().hide();
                            });
                        }
                        return null;
                    }
                };
            }
        }.start();
    }
    @FXML
    private void exportItemStructure() {
        if (textFieldName.getText().replace(" ", "").isEmpty()) {
            new ErrorAlert("ERRORE", "Errore di Inserimento", "Non e' stato assegnato un nome all'oggetto.");
            return;
        }
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<>() {
                    @Override
                    @SuppressWarnings("DuplicatedCode")
                    protected Void call() {
                        try {
                            double weight;
                            try {
                                String textWeight = textFieldWeight.getText();
                                if (textWeight == null || textWeight.replace(" ", "").isEmpty()) {
                                    weight = 0;
                                } else {
                                    weight = Double.parseDouble(textFieldWeight.getText());
                                    if (weight < 0) throw new NumberFormatException("The weight is less than 0");
                                }
                            } catch (NumberFormatException e) {
                                Logger.log(e, Defs.LOGGER_CONTEXT);
                                Platform.runLater(() -> new ErrorAlert("ERRORE", "Errore di Inserimento", "Il peso deve essere un numero a virgola mobile positivo!"));
                                return null;
                            }

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
                                if (mr < 0 || ma < 0 || me < 0 || mo < 0 || mp < 0)
                                    throw new NumberFormatException("A number is negative");
                            } catch (NumberFormatException e) {
                                Platform.runLater(() -> new ErrorAlert("ERRORE", "Errore di Inserimento", "Le valute devono essere dei numeri interi positivi!"));
                                return null;
                            }

                            Item exportableItem = new Item(
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
                                    Category.ITEM,
                                    weight,
                                    spinnerQuantity.getValue()
                            );

                            String itemCode = exportableItem.exportElement();
                            Platform.runLater(() -> {
                                if (new ConfirmationAlert("ESPORTAZIONE", "Esportazione dei Dati", "Codice elemento pronto, vuoi esportarlo su file?").result) {
                                    Platform.runLater(() -> SheetDataHandler.exportElementCodeIntoFile(itemCode));
                                } else {
                                    ClipboardContent content = new ClipboardContent();
                                    content.putString(itemCode);
                                    Client.getSystemClipboard().setContent(content);
                                    new InformationAlert("SUCCESSO", "Esportazione dei Dati", "Dati esportati con successo nella clipboard di sistema!");
                                }
                            });
                        } catch (Exception e) {
                            Logger.log(e, Defs.LOGGER_CONTEXT);
                            Platform.runLater(() -> {
                                new ErrorAlert("ERRORE", "Errore di Esportazione", "Si e' verificato un errore durante l'esportazione dei dati");
                                textFieldName.getScene().getWindow().hide();
                            });
                        }
                        return null;
                    }
                };
            }
        }.start();
    }

    // Methods
    private void initExistingItem(@NotNull final JSONObject itemStructure) {
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<>() {
                    @Override
                    @SuppressWarnings("DuplicatedCode")
                    protected Void call() {

                        try {
                            Item tempItem = new Item(itemStructure);

                            imageExtension = tempItem.getImageExtension();
                            int CC = tempItem.getCostCopper();
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
                                if (tempItem.getBase64image() != null && imageExtension != null) {
                                    byte[] imageBytes = Base64.getDecoder().decode(tempItem.getBase64image());
                                    ByteArrayInputStream imageStream = new ByteArrayInputStream(imageBytes);
                                    bufferedImage = ImageIO.read(imageStream);
                                } else if (tempItem.getBase64image() != null && imageExtension == null) {
                                    throw new IllegalArgumentException("Image without declared extension");
                                }
                            } catch (IllegalArgumentException e) {
                                Logger.log(e, Defs.LOGGER_CONTEXT);
                                tempItem.setBase64image(null);
                                tempItem.setImageExtension(null);
                                Platform.runLater(() -> new ErrorAlert("ERRORE", "Errore di lettura", "L'immagine ricevuta dal database non è leggibile"));
                                return null;
                            }

                            int finalCC = CC;
                            BufferedImage finalBufferedImage = bufferedImage;

                            Platform.runLater(() -> {

                                textFieldName.setText(tempItem.getName());
                                textFieldWeight.setText(String.valueOf(tempItem.getWeight()));
                                comboBoxRarity.getSelectionModel().select(tempItem.getRarity().getTextedRarity());
                                textFieldMR.setText(String.valueOf(finalCC));
                                textFieldMA.setText(String.valueOf(CS));
                                textFieldME.setText(String.valueOf(CE));
                                textFieldMO.setText(String.valueOf(CG));
                                textFieldMP.setText(String.valueOf(CP));
                                textAreaDescription.setText(tempItem.getDescription());
                                if (finalBufferedImage != null && imageExtension != null) {
                                    imageViewItem.setImage(SwingFXUtils.toFXImage(finalBufferedImage, null));
                                } else {
                                    imageViewItem.setImage(JFXDefs.AppInfo.LOGO);
                                }
                                spinnerQuantity.getValueFactory().setValue(tempItem.getQuantity());
                            });

                        } catch (Exception e) {
                            Logger.log(e, Defs.LOGGER_CONTEXT);
                            Platform.runLater(() -> {
                                new ErrorAlert("ERRORE", "Errore di Importazione", "La struttura dei dati non e' valida.");
                                textFieldName.getScene().getWindow().hide();
                            });
                        }
                        return null;
                    }
                };
            }
        }.start();
    }
    private void initExistingItem() {
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<>() {
                    @Override
                    @SuppressWarnings("DuplicatedCode")
                    protected Void call() {
                        try {
                            imageExtension = item.getImageExtension();
                            int CC = item.getCostCopper();
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
                                if (item.getBase64image() != null && imageExtension != null) {
                                    byte[] imageBytes = Base64.getDecoder().decode(item.getBase64image());
                                    ByteArrayInputStream imageStream = new ByteArrayInputStream(imageBytes);
                                    bufferedImage = ImageIO.read(imageStream);
                                } else if (item.getBase64image() != null && imageExtension == null) {
                                    throw new IllegalArgumentException("Image without declared extension");
                                }
                            } catch (IllegalArgumentException e) {
                                Logger.log(e, Defs.LOGGER_CONTEXT);
                                item.setBase64image(null);
                                item.setImageExtension(null);
                                Platform.runLater(() -> new ErrorAlert("ERRORE", "Errore di lettura", "L'immagine ricevuta dal database non è leggibile"));
                                return null;
                            }

                            int finalCC = CC;
                            BufferedImage finalBufferedImage = bufferedImage;
                            Platform.runLater(() -> {

                                textFieldName.setText(item.getName());
                                textFieldWeight.setText(String.valueOf(item.getWeight()));
                                comboBoxRarity.getSelectionModel().select(item.getRarity().getTextedRarity());
                                textFieldMR.setText(String.valueOf(finalCC));
                                textFieldMA.setText(String.valueOf(CS));
                                textFieldME.setText(String.valueOf(CE));
                                textFieldMO.setText(String.valueOf(CG));
                                textFieldMP.setText(String.valueOf(CP));
                                textAreaDescription.setText(item.getDescription());
                                if (finalBufferedImage != null && imageExtension != null) {
                                    imageViewItem.setImage(SwingFXUtils.toFXImage(finalBufferedImage, null));
                                } else {
                                    imageViewItem.setImage(JFXDefs.AppInfo.LOGO);
                                }
                                spinnerQuantity.getValueFactory().setValue(item.getQuantity());
                            });

                        } catch (Exception e) {
                            Logger.log(e, Defs.LOGGER_CONTEXT);
                            Platform.runLater(() -> {
                                new ErrorAlert("ERRORE", "Errore di Lettura", "Impossibile leggere l'elemento dal database");
                                textFieldName.getScene().getWindow().hide();
                            });
                        }
                        return null;
                    }
                };
            }
        }.start();
    }
    private void initExistingItem(@NotNull final String itemName) {
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<>() {
                    @Override
                    @SuppressWarnings("DuplicatedCode")
                    protected Void call() {
                        try {
                            item = new Item(itemName);
                            initExistingItem();
                        } catch (Exception e) {
                            Logger.log(e, Defs.LOGGER_CONTEXT);
                            Platform.runLater(() -> {
                                new ErrorAlert("ERRORE", "Errore di Lettura", "Impossibile leggere l'elemento dal database");
                                textFieldName.getScene().getWindow().hide();
                            });
                        }
                        return null;
                    }
                };
            }
        }.start();
    }
}
