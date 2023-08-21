package it.italiandudes.dnd_visualizer.client.javafx.controller.sheetviewer;

import it.italiandudes.dnd_visualizer.client.javafx.Client;
import it.italiandudes.dnd_visualizer.client.javafx.JFXDefs;
import it.italiandudes.dnd_visualizer.client.javafx.alert.ErrorAlert;
import it.italiandudes.dnd_visualizer.client.javafx.controller.ControllerSceneSheetViewer;
import it.italiandudes.dnd_visualizer.utils.Defs;
import it.italiandudes.idl.common.ImageHandler;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
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

    // Old Values
    private static String oldValueCurrentLifeDiceAmount = null;
    private static String oldValueMaxHP = "20";
    private static String oldValueCurrentHP = "20";
    private static String oldValueTempHP = "0";

    // Initialize
    public static void initialize(@NotNull final ControllerSceneSheetViewer controller) {
        onLostFocusFireActionEvent(controller);
        controller.imageViewCharacterImage.setImage(JFXDefs.AppInfo.LOGO);
        hpRemoverRectangle = new Rectangle(controller.imageViewCurrentHP.getFitWidth(), 0, Client.getBackgroundThemeColor());
        controller.spinnerLevel.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 1, 1));
        controller.spinnerProficiencyBonus.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 6, 2, 1));
        controller.spinnerInspiration.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0, 1));
        setOnChangeTriggers(controller);
        updateLifeDiceFaces(controller);
        controller.textFieldTotalLifeDiceAmount.setText(String.valueOf(controller.spinnerLevel.getValue())); // This must be after "updateLifeDiceFaces(controller)"
        oldValueCurrentLifeDiceAmount = controller.textFieldCurrentLifeDiceAmount.getText();
    }

    // OnChange Triggers Setter
    private static void setOnChangeTriggers(@NotNull final ControllerSceneSheetViewer controller) {
        controller.spinnerLevel.getEditor().textProperty().addListener(((observable, oldValue, newValue) -> controller.textFieldTotalLifeDiceAmount.setText(newValue)));
        controller.spinnerProficiencyBonus.getEditor().textProperty().addListener(((observable) -> updateProficiencyBonus(controller)));
        controller.textFieldTotalLifeDiceAmount.textProperty().addListener(((observable) -> updateLifeDiceAmount(controller)));
    }

    // Lost Focus On Action Fire Event
    private static void onLostFocusFireActionEvent(@NotNull final ControllerSceneSheetViewer controller) {
        controller.textFieldCurrentLifeDiceFaces.focusedProperty().addListener(((observable, oldValue, newValue) -> {
            if (!newValue) updateLifeDiceFaces(controller);
        }));
        controller.textFieldMaxHP.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) recalculateHealthPercentage(controller);
        });
        controller.textFieldCurrentHP.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) recalculateHealthPercentage(controller);
        });
        controller.textFieldTempHP.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) recalculateHealthPercentage(controller);
        });
        controller.textFieldCurrentLifeDiceAmount.focusedProperty().addListener(((observable, oldValue, newValue) -> {
            if (!newValue) validateNewCurrentLifeDiceAmount(controller);
        }));
        // TODO: add eventual TextField that need to run their onClick method
    }

    // EDT
    public static void validateNewCurrentLifeDiceAmount(@NotNull final ControllerSceneSheetViewer controller) {
        try {
            int currentLifeDiceAmount = Integer.parseInt(controller.textFieldCurrentLifeDiceAmount.getText());
            if (currentLifeDiceAmount < 0 || currentLifeDiceAmount > Integer.parseInt(controller.textFieldTotalLifeDiceAmount.getText())) throw new NumberFormatException();
            oldValueCurrentLifeDiceAmount = controller.textFieldCurrentLifeDiceAmount.getText();
        } catch (NumberFormatException e) {
            controller.textFieldCurrentLifeDiceAmount.setText(oldValueCurrentLifeDiceAmount);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Il numero di dadi vita attuali deve essere un numero intero compreso tra 0 e il numero di dati vita totali.");
        }
    }
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
            double lifeDiceFaces = Double.parseDouble(controller.textFieldCurrentLifeDiceFaces.getText());
            if (lifeDiceFaces <= 0) throw new NumberFormatException();
            String strFaces = new DecimalFormat("#").format(lifeDiceFaces);
            controller.textFieldTotalLifeDiceFaces.setText(strFaces);
            controller.textFieldCurrentLifeDiceFaces.setText(strFaces);
        } catch (NumberFormatException e) {
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Il numero di facce dei dadi vita deve essere maggiore di 0.");
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
            controller.textFieldMaxHP.setText(oldValueMaxHP);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "I punti ferita massimi devono essere un numero maggiore a 0.");
            return;
        }
        try {
            currentHP = (controller.textFieldCurrentHP.getText().replace(" ", "").isEmpty() ? 0 : Double.parseDouble(controller.textFieldCurrentHP.getText()));
            if (currentHP < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            controller.textFieldCurrentHP.setText(oldValueCurrentHP);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "I punti ferita attuali devono essere un numero maggiore o uguale a 0.");
            return;
        }
        try {
            tempHP = (controller.textFieldTempHP.getText().replace(" ", "").isEmpty() ? 0 : Double.parseDouble(controller.textFieldTempHP.getText()));
            if (tempHP < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            controller.textFieldTempHP.setText(oldValueTempHP);
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
        oldValueMaxHP = controller.textFieldMaxHP.getText();
        oldValueCurrentHP = controller.textFieldCurrentHP.getText();
        oldValueTempHP = controller.textFieldTempHP.getText();
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
                                Image fxImage = SwingFXUtils.toFXImage(img, null);
                                Platform.runLater(() -> {
                                    controller.imageViewCharacterImage.setImage(fxImage);
                                    controller.imageViewCharacterBodyImage.setImage(fxImage);
                                });
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
