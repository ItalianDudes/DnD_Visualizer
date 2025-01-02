package it.italiandudes.dnd_visualizer.javafx.controllers.campaign;

import it.italiandudes.dnd_visualizer.data.entities.Entity;
import it.italiandudes.dnd_visualizer.data.entities.EntityManager;
import it.italiandudes.dnd_visualizer.data.enums.EntityType;
import it.italiandudes.dnd_visualizer.data.map.Map;
import it.italiandudes.dnd_visualizer.javafx.Client;
import it.italiandudes.dnd_visualizer.javafx.JFXDefs;
import it.italiandudes.dnd_visualizer.javafx.alerts.ErrorAlert;
import it.italiandudes.dnd_visualizer.javafx.alerts.InformationAlert;
import it.italiandudes.dnd_visualizer.javafx.utils.UIElementConfigurator;
import it.italiandudes.dnd_visualizer.utils.Defs;
import it.italiandudes.dnd_visualizer.utils.ImageUtils;
import it.italiandudes.idl.common.ImageHandler;
import it.italiandudes.idl.common.Logger;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

public final class ControllerSceneCampaignEntity {

    // Attributes
    private Map map = null;
    private Point2D center = null;
    private Entity entity = null;
    private String imageExtension = null;
    private volatile boolean configurationComplete = false;

    // Methods
    public void setMap(@NotNull final Map map) {
        this.map = map;
    }
    public void setCenter(@NotNull final Point2D center) {
        this.center = center;
    }
    public void setEntity(@Nullable Entity entity) {
        this.entity = entity;
    }
    @Nullable public Entity getEntity() {
        return entity;
    }
    public void configurationComplete() {
        configurationComplete = true;
    }

    // Graphic Elements
    @FXML private TextField textFieldName;
    @FXML private ComboBox<EntityType> comboBoxType;
    @FXML private TextField textFieldRace;
    @FXML private TextField textFieldClass;
    @FXML private Spinner<Integer> spinnerLevel;
    @FXML private Spinner<Integer> spinnerCA;
    @FXML private Spinner<Integer> spinnerHP;
    @FXML private Spinner<Integer> spinnerMaxHP;
    @FXML private Spinner<Integer> spinnerTempHP;
    @FXML private ImageView imageViewEntity;
    @FXML private TextArea textAreaDescription;
    @FXML private Button buttonSave;

    // Initialize
    @FXML @SuppressWarnings("DuplicatedCode")
    private void initialize() {
        imageViewEntity.setImage(JFXDefs.AppInfo.LOGO);
        spinnerLevel.getEditor().setTextFormatter(UIElementConfigurator.configureNewIntegerTextFormatter());
        spinnerLevel.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, 1, 1));
        spinnerCA.getEditor().setTextFormatter(UIElementConfigurator.configureNewIntegerTextFormatter());
        spinnerCA.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, 1, 1));
        spinnerHP.getEditor().setTextFormatter(UIElementConfigurator.configureNewIntegerTextFormatter());
        spinnerHP.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 1, 1));
        spinnerMaxHP.getEditor().setTextFormatter(UIElementConfigurator.configureNewIntegerTextFormatter());
        spinnerMaxHP.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 1, 1));
        spinnerTempHP.getEditor().setTextFormatter(UIElementConfigurator.configureNewIntegerTextFormatter());
        spinnerTempHP.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 1, 1));
        comboBoxType.getItems().addAll(EntityType.values());
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {
                        //noinspection StatementWithEmptyBody
                        while (!configurationComplete);
                        Platform.runLater(() -> {
                            if (entity != null) initExistingEntity();
                            buttonSave.setDisable(false);
                        });
                        return null;
                    }
                };
            }
        }.start();
    }

    // EDT
    @FXML private void removeImage() {
        imageViewEntity.setImage(JFXDefs.AppInfo.LOGO);
        imageExtension = null;
    }
    @FXML @SuppressWarnings("DuplicatedCode") private void openFileChooser() {
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
        if(imagePath != null) {
            File finalImagePath = imagePath;
            new Service<Void>() {
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
                                Platform.runLater(() -> imageViewEntity.setImage(SwingFXUtils.toFXImage(img, null)));
                                imageExtension = ImageHandler.getImageExtension(finalImagePath.getAbsolutePath());
                            }catch (IOException e) {
                                Platform.runLater(() -> new ErrorAlert("ERRORE", "Errore di Lettura", "Impossibile leggere il contenuto selezionato."));
                            }
                            return null;
                        }
                    };
                }
            }.start();
        }
    }
    @FXML private void back() {
        textFieldName.getScene().getWindow().hide();
    }
    @FXML private void save() {
        if (textFieldName.getText().replace(" ", "").isEmpty()) {
            new ErrorAlert("ERRORE", "Errore di Inserimento", "Non e' stato assegnato un nome all'entita'.");
            return;
        }
        if (textFieldRace.getText().replace(" ", "").isEmpty()) {
            new ErrorAlert("ERRORE", "Errore di Inserimento", "Non e' stato assegnata una razza all'entita'.");
            return;
        }
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override @SuppressWarnings("DuplicatedCode")
                    protected Void call() {
                        try {
                            EntityType type = comboBoxType.getSelectionModel().getSelectedItem();
                            if (type == null) {
                                Platform.runLater(() -> new ErrorAlert("ERRORE", "Errore di Inserimento", "Il tipo di entita' non puo' essere nullo."));
                                return null;
                            }
                            String name = textFieldName.getText();
                            String race = textFieldRace.getText();
                            int level = Integer.parseInt(spinnerLevel.getEditor().getText());
                            int hp = Integer.parseInt(spinnerHP.getEditor().getText());
                            int maxHP = Integer.parseInt(spinnerMaxHP.getEditor().getText());
                            int tempHP = Integer.parseInt(spinnerTempHP.getEditor().getText());
                            int ca = Integer.parseInt(spinnerCA.getEditor().getText());
                            Image image = imageViewEntity.getImage();
                            String base64image;
                            if (image != null && imageExtension != null) {
                                base64image = ImageUtils.fromFXImageToBase64(image, imageExtension);
                            } else {
                                base64image = null;
                                imageExtension = null;
                            }

                            if (entity == null) {
                                entity = EntityManager.getInstance().registerEntity(map, name, race, level, type, center, ca, hp, maxHP, tempHP);
                            } else {
                                entity.setName(textFieldName.getText());
                                entity.setRace(textFieldRace.getText());
                                entity.setType(type);
                                entity.setLevel(level);
                                entity.setHP(hp);
                                entity.setMaxHP(maxHP);
                                entity.setTempHP(tempHP);
                                entity.setCA(ca);
                            }

                            entity.setEntityClass(textFieldClass.getText());
                            entity.setBase64image(base64image);
                            entity.setImageExtension(imageExtension);
                            entity.setDescription(textAreaDescription.getText());
                            entity.saveEntityData();

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

    // Methods
    private void initExistingEntity() {
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override @SuppressWarnings("DuplicatedCode")
                    protected Void call() {
                        try {
                            imageExtension = entity.getImageExtension();

                            Image image = null;
                            try {
                                if (entity.getBase64image() != null && imageExtension != null) {
                                    image = ImageUtils.fromBase64ToFXImage(entity.getBase64image());
                                }
                            } catch (IOException e) {
                                Logger.log(e, Defs.LOGGER_CONTEXT);
                                entity.setBase64image(null);
                                entity.setImageExtension(null);
                                Platform.runLater(() -> new ErrorAlert("ERRORE", "Errore di lettura", "L'immagine ricevuta dal database non è leggibile"));
                                return null;
                            }

                            final Image finalImage = image;
                            Platform.runLater(() -> {
                                textFieldName.setText(entity.getName());
                                comboBoxType.getSelectionModel().select(entity.getType());
                                textFieldRace.setText(entity.getRace());
                                textFieldClass.setText(entity.getEntityClass());
                                spinnerLevel.getValueFactory().setValue(entity.getLevel());
                                spinnerCA.getValueFactory().setValue(entity.getCA());
                                spinnerHP.getValueFactory().setValue(entity.getHP());
                                spinnerMaxHP.getValueFactory().setValue(entity.getMaxHP());
                                spinnerTempHP.getValueFactory().setValue(entity.getTempHP());
                                textAreaDescription.setText(entity.getDescription());
                                if (finalImage != null && imageExtension != null) {
                                    imageViewEntity.setImage(finalImage);
                                } else {
                                    imageViewEntity.setImage(JFXDefs.AppInfo.LOGO);
                                }
                            });
                        } catch (Exception e) {
                            Logger.log(e, Defs.LOGGER_CONTEXT);
                            Platform.runLater(() -> {
                                new ErrorAlert("ERRORE", "Errore di Lettura", "Impossibile leggere l'elemento dal database");
                                back();
                            });
                        }
                        return null;
                    }
                };
            }
        }.start();
    }
}
