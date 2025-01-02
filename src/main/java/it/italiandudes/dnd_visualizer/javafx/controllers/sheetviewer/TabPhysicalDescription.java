package it.italiandudes.dnd_visualizer.javafx.controllers.sheetviewer;

import it.italiandudes.dnd_visualizer.javafx.Client;
import it.italiandudes.dnd_visualizer.javafx.JFXDefs;
import it.italiandudes.dnd_visualizer.javafx.alerts.ErrorAlert;
import it.italiandudes.dnd_visualizer.javafx.controllers.ControllerSceneSheetViewer;
import it.italiandudes.dnd_visualizer.javafx.utils.SheetDataHandler;
import it.italiandudes.dnd_visualizer.utils.Defs;
import it.italiandudes.dnd_visualizer.db.SheetKeyParameters;
import it.italiandudes.idl.common.ImageHandler;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

public final class TabPhysicalDescription {

    // Initialize
    public static void initialize(@NotNull final ControllerSceneSheetViewer controller) {
        controller.imageViewCharacterBodyImage.setImage(JFXDefs.AppInfo.LOGO);
        readTabData(controller);
        setOnChangeTriggers(controller);
    }

    // Data Reader
    private static void readTabData(@NotNull final ControllerSceneSheetViewer controller) {
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {
                        String age = SheetDataHandler.readKeyParameter(SheetKeyParameters.TabPhysicalDescription.AGE);
                        String height = SheetDataHandler.readKeyParameter(SheetKeyParameters.TabPhysicalDescription.HEIGHT);
                        String weight = SheetDataHandler.readKeyParameter(SheetKeyParameters.TabPhysicalDescription.WEIGHT);
                        String eyes = SheetDataHandler.readKeyParameter(SheetKeyParameters.TabPhysicalDescription.EYES);
                        String skin = SheetDataHandler.readKeyParameter(SheetKeyParameters.TabPhysicalDescription.SKIN);
                        String hair = SheetDataHandler.readKeyParameter(SheetKeyParameters.TabPhysicalDescription.HAIR);
                        String physicalDescription = SheetDataHandler.readKeyParameter(SheetKeyParameters.TabPhysicalDescription.PHYSICAL_DESCRIPTION);
                        Platform.runLater(() -> {
                            if (age != null) controller.textFieldAge.setText(age);
                            if (height != null) controller.textFieldHeight.setText(height);
                            if (weight != null) controller.textFieldWeight.setText(weight);
                            if (eyes != null) controller.textFieldEyes.setText(eyes);
                            if (skin != null) controller.textFieldSkin.setText(skin);
                            if (hair != null) controller.textFieldHair.setText(hair);
                            if (physicalDescription != null) controller.textAreaPhysicalDescription.setText(physicalDescription);
                            if (controller.imageViewCharacterImage.getImage() != null) controller.imageViewCharacterBodyImage.setImage(controller.imageViewCharacterImage.getImage());
                        });
                        return null;
                    }
                };
            }
        }.start();
    }

    // OnChange Triggers Setter
    private static void setOnChangeTriggers(@NotNull final ControllerSceneSheetViewer controller) {
        controller.textFieldAge.textProperty().addListener((observable, oldValue, newValue) -> SheetDataHandler.writeKeyParameter(SheetKeyParameters.TabPhysicalDescription.AGE, newValue));
        controller.textFieldHeight.textProperty().addListener((observable, oldValue, newValue) -> SheetDataHandler.writeKeyParameter(SheetKeyParameters.TabPhysicalDescription.HEIGHT, newValue));
        controller.textFieldWeight.textProperty().addListener((observable, oldValue, newValue) -> SheetDataHandler.writeKeyParameter(SheetKeyParameters.TabPhysicalDescription.WEIGHT, newValue));
        controller.textFieldEyes.textProperty().addListener((observable, oldValue, newValue) -> SheetDataHandler.writeKeyParameter(SheetKeyParameters.TabPhysicalDescription.EYES, newValue));
        controller.textFieldSkin.textProperty().addListener((observable, oldValue, newValue) -> SheetDataHandler.writeKeyParameter(SheetKeyParameters.TabPhysicalDescription.SKIN, newValue));
        controller.textFieldHair.textProperty().addListener((observable, oldValue, newValue) -> SheetDataHandler.writeKeyParameter(SheetKeyParameters.TabPhysicalDescription.HAIR, newValue));
        controller.textAreaPhysicalDescription.textProperty().addListener((observable, oldValue, newValue) -> SheetDataHandler.writeKeyParameter(SheetKeyParameters.TabPhysicalDescription.PHYSICAL_DESCRIPTION, newValue));
    }

    // EDT
    @SuppressWarnings("DuplicatedCode")
    public static void openCharacterBodyImageFileChooser(@NotNull final ControllerSceneSheetViewer controller) {
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
                                Image fxImage = SwingFXUtils.toFXImage(img, null);
                                Platform.runLater(() -> {
                                    controller.imageViewCharacterBodyImage.setImage(fxImage);
                                    controller.imageViewCharacterImage.setImage(fxImage);
                                });
                                TabCharacter.setCharacterImageExtension(ImageHandler.getImageExtension(finalImagePath.getAbsolutePath()));
                                SheetDataHandler.writeKeyParameter(SheetKeyParameters.CHARACTER_IMAGE, SheetKeyParameters.CHARACTER_IMAGE_EXTENSION, img, TabCharacter.getCharacterImageExtension());
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
}
