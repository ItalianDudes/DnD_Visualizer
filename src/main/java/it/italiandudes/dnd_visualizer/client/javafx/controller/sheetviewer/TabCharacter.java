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
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

public final class TabCharacter {

    // Attributes
    private static Rectangle hpRemoverRectangle;
    private static boolean isCharacterImageSet = false;
    private static String characterImageExtension = null;

    // Initialize
    public static void initialize(@NotNull final ControllerSceneSheetViewer controller) {
        controller.imageViewCharacterImage.setImage(Client.getDefaultImage());
        hpRemoverRectangle = new Rectangle(controller.imageViewCurrentHP.getFitWidth(), 0, Client.getBackgroundThemeColor());
        controller.spinnerLevel.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 1, 1));
        controller.spinnerProficiencyBonus.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 6, 2, 1));
        controller.spinnerInspiration.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0, 1));
        setOnChangeTriggers(controller);
        updateLifeDiceFaces(controller);
        controller.textFieldTotalLifeDiceAmount.setText(String.valueOf(controller.spinnerLevel.getValue())); // This must be after "updateLifeDiceFaces(controller)"
    }

    // OnChange Triggers Setter
    public static void setOnChangeTriggers(@NotNull final ControllerSceneSheetViewer controller) {
        controller.spinnerLevel.getEditor().textProperty().addListener(((observable, oldValue, newValue) -> controller.textFieldTotalLifeDiceAmount.setText(newValue)));
        controller.spinnerProficiencyBonus.getEditor().textProperty().addListener(((observable) -> updateProficiencyBonus(controller)));
        controller.textFieldTotalLifeDiceAmount.textProperty().addListener(((observable) -> updateLifeDiceAmount(controller)));
    }

    // EDT
    public static void updateLifeDiceAmount(@NotNull final ControllerSceneSheetViewer controller) {
        int level = controller.spinnerLevel.getValue();
        try {
            int currentLifeDiceAmount = Integer.parseInt(controller.textFieldCurrentLifeDiceAmount.getText());
            if (currentLifeDiceAmount > level) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            controller.textFieldCurrentLifeDiceAmount.setText(String.valueOf(level));
        }
    }
    public static void updateLifeDiceFaces(@NotNull final ControllerSceneSheetViewer controller) {
        try {
            double lifeDiceFaces = Double.parseDouble(controller.textFieldTotalLifeDiceFaces.getText());
            if (lifeDiceFaces <= 0) throw new NumberFormatException();
            String strFaces = new DecimalFormat("#").format(lifeDiceFaces);
            controller.textFieldTotalLifeDiceFaces.setText(strFaces);
            controller.textFieldCurrentLifeDiceFaces.setText(strFaces);
        } catch (NumberFormatException e) {
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Il numero di facce dei dadi vita totali deve essere maggiore di 0.");
        }
    }
    public static void updateProficiencyBonus(@NotNull final ControllerSceneSheetViewer controller) {
        // TODO: Update all parameters that depends from Proficiency Bonus
        TabAbility.updateParameters(controller);
    }
    public static void recalculateHealthPercentage(@NotNull final ControllerSceneSheetViewer controller) {
        double maxHP, currentHP, tempHP;
        try {
            maxHP = (controller.textFieldMaxHP.getText().replace(" ", "").isEmpty() ? 0 : Double.parseDouble(controller.textFieldMaxHP.getText()));
            if (maxHP <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "I punti ferita massimi devono essere un numero maggiore a 0.");
            return;
        }
        try {
            currentHP = (controller.textFieldCurrentHP.getText().replace(" ", "").isEmpty() ? 0 : Double.parseDouble(controller.textFieldCurrentHP.getText()));
            if (currentHP < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "I punti ferita attuali devono essere un numero maggiore o uguale a 0.");
            return;
        }
        try {
            tempHP = (controller.textFieldTempHP.getText().replace(" ", "").isEmpty() ? 0 : Double.parseDouble(controller.textFieldTempHP.getText()));
            if (tempHP < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "I punti ferita temporanei devono essere un numero maggiore o uguale a 0.");
            return;
        }
        int hpPercentage = (int) (((currentHP + tempHP) / maxHP) * 100.0);
        double newHealthPercentage = (controller.imageViewCurrentHP.getFitHeight()-25) - (controller.imageViewCurrentHP.getFitHeight() * (currentHP + tempHP) / maxHP);
        if (newHealthPercentage<0) newHealthPercentage = 0;
        hpRemoverRectangle.setHeight(newHealthPercentage);
        controller.stackPaneCurrentHP.getChildren().remove(hpRemoverRectangle);
        controller.stackPaneCurrentHP.getChildren().add(hpRemoverRectangle);
        controller.labelHPLeftPercentage.setText(hpPercentage > 500?">500%":hpPercentage+"%");
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
