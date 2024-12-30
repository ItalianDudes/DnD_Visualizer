package it.italiandudes.dnd_visualizer.javafx.controllers.campaign;

import it.italiandudes.dnd_visualizer.data.entities.Entity;
import it.italiandudes.dnd_visualizer.data.enums.EntityType;
import it.italiandudes.dnd_visualizer.javafx.Client;
import it.italiandudes.dnd_visualizer.javafx.JFXDefs;
import it.italiandudes.dnd_visualizer.javafx.alerts.ErrorAlert;
import it.italiandudes.dnd_visualizer.javafx.alerts.InformationAlert;
import it.italiandudes.dnd_visualizer.javafx.utils.UIElementConfigurator;
import it.italiandudes.dnd_visualizer.utils.Defs;
import it.italiandudes.idl.common.ImageHandler;
import it.italiandudes.idl.common.Logger;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
    private Entity entity = null;
    private String imageExtension = null;
    private EntityType entityType = null;
    private String name = null;
    private volatile boolean configurationComplete = false;

    // Methods
    public void setEntityType(@NotNull final EntityType type) {
        this.entityType = type;
    }
    public void setName(@NotNull final String name) {
        this.name = name;
    }
    @Nullable
    public Entity getEntity() {
        return entity;
    }
    public void configurationComplete() {
        configurationComplete = true;
    }

    // Graphic Elements
    @FXML private TextField textFieldName;
    @FXML private TextField textFieldRace;
    @FXML private TextField textFieldClass;
    @FXML private Spinner<Integer> spinnerLevel;
    @FXML private Spinner<Integer> spinnerCA;
    @FXML private Spinner<Integer> spinnerHP;
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
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {
                        //noinspection StatementWithEmptyBody
                        while (!configurationComplete);
                        Platform.runLater(() -> {
                            // if (name != null) initExistingEntity();
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
                                Platform.runLater(() -> imageViewEntity.setImage(SwingFXUtils.toFXImage(img, null)));
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
    @FXML private void backToSheet() {
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
    /*
    private void initExistingEntity() {
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override @SuppressWarnings("DuplicatedCode")
                    protected Void call() {
                        try {
                            imageExtension = entity.getImageExtension();

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

                            BufferedImage finalBufferedImage = bufferedImage;
                            Platform.runLater(() -> {

                                textFieldName.setText(entity.getName());
                                if (finalBufferedImage != null && imageExtension != null) {
                                    imageViewEntity.setImage(SwingFXUtils.toFXImage(finalBufferedImage, null));
                                } else {
                                    imageViewEntity.setImage(JFXDefs.AppInfo.LOGO);
                                }
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
    }*/
}
