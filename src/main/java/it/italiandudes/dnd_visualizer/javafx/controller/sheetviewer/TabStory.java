package it.italiandudes.dnd_visualizer.javafx.controller.sheetviewer;

import it.italiandudes.dnd_visualizer.javafx.Client;
import it.italiandudes.dnd_visualizer.javafx.JFXDefs;
import it.italiandudes.dnd_visualizer.javafx.alert.ErrorAlert;
import it.italiandudes.dnd_visualizer.javafx.controller.ControllerSceneSheetViewer;
import it.italiandudes.dnd_visualizer.javafx.utils.SheetDataHandler;
import it.italiandudes.dnd_visualizer.utils.Defs;
import it.italiandudes.dnd_visualizer.utils.Defs.KeyParameters;
import it.italiandudes.idl.common.ImageHandler;
import it.italiandudes.idl.common.Logger;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.stream.Collectors;

public final class TabStory {

    // Attributes
    private static String symbolImageExtension = null;

    // Initialize
    public static void initialize(@NotNull final ControllerSceneSheetViewer controller) {
        controller.imageViewSymbolImage.setImage(JFXDefs.AppInfo.LOGO);
        readTabData(controller);
        setOnChangeTriggers(controller);
    }

    // Data Reader
    @SuppressWarnings("DuplicatedCode")
    private static void readTabData(@NotNull final ControllerSceneSheetViewer controller) {
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {
                        String characterBackstory = SheetDataHandler.readKeyParameter(KeyParameters.TabStory.CHARACTER_BACKSTORY);
                        String cultDescription = SheetDataHandler.readKeyParameter(KeyParameters.TabStory.CULT_DESCRIPTION);
                        String base64CultImage = SheetDataHandler.readKeyParameter(KeyParameters.TabStory.CULT_IMAGE);
                        String cultImageExtension = SheetDataHandler.readKeyParameter(KeyParameters.TabStory.CULT_IMAGE_EXTENSION);
                        Image cultImage = null;
                        try {
                            if (base64CultImage != null) {
                                if (cultImageExtension == null) throw new IllegalArgumentException("Image without declared extension");
                                byte[] imageBytes = Base64.getDecoder().decode(base64CultImage);
                                ByteArrayInputStream imageStream = new ByteArrayInputStream(imageBytes);
                                BufferedImage bufferedImageCharacter = ImageIO.read(imageStream);
                                cultImage = SwingFXUtils.toFXImage(bufferedImageCharacter, null);
                            }
                        } catch (IllegalArgumentException | IOException e) {
                            Logger.log(e);
                            Platform.runLater(() -> new ErrorAlert("ERRORE", "ERRORE DI LETTURA", "L'immagine ricevuta dal database non è leggibile."));
                        }
                        String alliesAndOrganizations = SheetDataHandler.readKeyParameter(KeyParameters.TabStory.ALLIES_AND_ORGANIZATIONS);
                        Image finalCultImage = cultImage;
                        Platform.runLater(() -> {
                            if (characterBackstory != null) controller.textAreaBackstory.setText(characterBackstory);
                            if (cultDescription != null) controller.textAreaCult.setText(cultDescription);
                            if (finalCultImage != null) controller.imageViewSymbolImage.setImage(finalCultImage);
                            if (alliesAndOrganizations != null) controller.textAreaAlliesAndOrganizations.setText(alliesAndOrganizations);
                        });
                        return null;
                    }
                };
            }
        }.start();
    }

    // OnChange Triggers Setter
    private static void setOnChangeTriggers(@NotNull final ControllerSceneSheetViewer controller) {
        controller.textAreaBackstory.textProperty().addListener((observable, oldValue, newValue) -> SheetDataHandler.writeKeyParameter(KeyParameters.TabStory.CHARACTER_BACKSTORY, newValue));
        controller.textAreaCult.textProperty().addListener((observable, oldValue, newValue) -> SheetDataHandler.writeKeyParameter(KeyParameters.TabStory.CULT_DESCRIPTION, newValue));
        controller.imageViewSymbolImage.imageProperty().addListener((observable, oldValue, newValue) -> SheetDataHandler.writeKeyParameter(KeyParameters.TabStory.CULT_IMAGE, KeyParameters.TabStory.CULT_IMAGE_EXTENSION, newValue, symbolImageExtension));
        controller.textAreaAlliesAndOrganizations.textProperty().addListener((observable, oldValue, newValue) -> SheetDataHandler.writeKeyParameter(KeyParameters.TabStory.ALLIES_AND_ORGANIZATIONS, newValue));
    }

    // EDT
    @SuppressWarnings("DuplicatedCode")
    public static void openSymbolImageFileChooser(@NotNull final ControllerSceneSheetViewer controller) {
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
                                Platform.runLater(() -> controller.imageViewSymbolImage.setImage(SwingFXUtils.toFXImage(img, null)));
                                symbolImageExtension = ImageHandler.getImageExtension(finalImagePath.getAbsolutePath());
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
