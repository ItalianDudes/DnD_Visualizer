package it.italiandudes.dnd_visualizer.client.javafx.controller.sheetviewer;

import it.italiandudes.dnd_visualizer.client.javafx.Client;
import it.italiandudes.dnd_visualizer.client.javafx.alert.ErrorAlert;
import it.italiandudes.dnd_visualizer.client.javafx.controller.ControllerSceneSheetViewer;
import it.italiandudes.dnd_visualizer.utils.Defs;
import it.italiandudes.idl.common.ImageHandler;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.stage.FileChooser;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public final class TabCharacter {

    // EDT
    public static void recalculateHealthPercentage(@NotNull final ControllerSceneSheetViewer controller) {
        try {
            int hpPercentage = (int) ((Double.parseDouble(controller.textFieldCurrentHP.getText()) / Double.parseDouble(controller.textFieldMaxHP.getText())) * 100.0);
            double newHealthPercentage = (controller.imageViewCurrentHP.getFitHeight()-25) - (controller.imageViewCurrentHP.getFitHeight() * Double.parseDouble(controller.textFieldCurrentHP.getText())) / Double.parseDouble(controller.textFieldMaxHP.getText());
            if (newHealthPercentage<0) newHealthPercentage = 0;
            controller.hpRemoverRectangle.setHeight(newHealthPercentage);
            controller.stackPaneCurrentHP.getChildren().remove(controller.hpRemoverRectangle);
            controller.stackPaneCurrentHP.getChildren().add(controller.hpRemoverRectangle);
            controller.labelHPLeftPercentage.setText(hpPercentage > 500?">500%":hpPercentage+"%");
        } catch (NumberFormatException ignored) {}
    }
    public static void openCharacterImageFileChooser(@NotNull final ControllerSceneSheetViewer controller) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona un Contenuto Multimediale");
        // TODO: add supported extensions with for-each
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG", "*.png"));
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
                                Platform.runLater(() -> controller.imageViewCharacterImage.setImage(SwingFXUtils.toFXImage(img, null)));
                                controller.characterImageExtension = ImageHandler.getImageExtension(finalImagePath.getAbsolutePath());
                                controller.isCharacterImageSet = true;
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
