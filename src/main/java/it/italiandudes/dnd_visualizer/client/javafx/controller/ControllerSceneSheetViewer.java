package it.italiandudes.dnd_visualizer.client.javafx.controller;

import it.italiandudes.dnd_visualizer.client.javafx.Client;
import it.italiandudes.dnd_visualizer.client.javafx.alert.ErrorAlert;
import it.italiandudes.dnd_visualizer.utils.Defs;
import it.italiandudes.idl.common.ImageHandler;
import it.italiandudes.idl.common.Logger;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@SuppressWarnings("unused")
public final class ControllerSceneSheetViewer {

    // Attributes
    private Rectangle hpRemoverRectangle;
    private boolean isCharacterImageSet = false;
    private String characterImageExtension = null;

    //Graphic Elements
    @FXML private ImageView imageViewCharacterImage;
    @FXML private ImageView imageViewCurrentHP;
    @FXML private StackPane stackPaneCurrentHP;
    @FXML private Label labelHPLeftPercentage;
    @FXML private TextField textFieldMaxHP;
    @FXML private TextField textFieldCurrentHP;

    //Initialize
    @FXML
    private void initialize() {
        Client.getStage().setResizable(true);
        imageViewCharacterImage.setImage(Client.getDefaultImage());
        hpRemoverRectangle = new Rectangle(imageViewCurrentHP.getFitWidth(), 0, Client.getBackgroundThemeColor());
    }

    // EDT
    @FXML
    private void recalculateHealthPercentage() {
        try {
            int hpPercentage = (int) ((Double.parseDouble(textFieldCurrentHP.getText()) / Double.parseDouble(textFieldMaxHP.getText())) * 100.0);
            double newHealthPercentage = (imageViewCurrentHP.getFitHeight()-25) - (imageViewCurrentHP.getFitHeight() * Double.parseDouble(textFieldCurrentHP.getText())) / Double.parseDouble(textFieldMaxHP.getText());
            if (newHealthPercentage<0) newHealthPercentage = 0;
            hpRemoverRectangle.setHeight(newHealthPercentage);
            stackPaneCurrentHP.getChildren().remove(hpRemoverRectangle);
            stackPaneCurrentHP.getChildren().add(hpRemoverRectangle);
            labelHPLeftPercentage.setText(hpPercentage > 500?">500%":hpPercentage+"%");
        } catch (NumberFormatException ignored) {}
    }
    @FXML
    private void openCharacterImageFileChooser() {
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
                                Platform.runLater(() -> imageViewCharacterImage.setImage(SwingFXUtils.toFXImage(img, null)));
                                characterImageExtension = ImageHandler.getImageExtension(finalImagePath.getAbsolutePath());
                                isCharacterImageSet = true;
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
