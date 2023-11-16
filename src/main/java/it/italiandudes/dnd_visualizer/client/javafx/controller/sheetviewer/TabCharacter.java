package it.italiandudes.dnd_visualizer.client.javafx.controller.sheetviewer;

import it.italiandudes.dnd_visualizer.client.javafx.Client;
import it.italiandudes.dnd_visualizer.client.javafx.JFXDefs;
import it.italiandudes.dnd_visualizer.client.javafx.alert.ErrorAlert;
import it.italiandudes.dnd_visualizer.client.javafx.controller.ControllerSceneSheetViewer;
import it.italiandudes.dnd_visualizer.client.javafx.util.SheetDataHandler;
import it.italiandudes.dnd_visualizer.utils.Defs;
import it.italiandudes.dnd_visualizer.utils.Defs.KeyParameters;
import it.italiandudes.dnd_visualizer.utils.DiscordRichPresenceManager;
import it.italiandudes.idl.common.ImageHandler;
import it.italiandudes.idl.common.Logger;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Base64;

public final class TabCharacter {

    // Attributes
    private static final Image IMAGE_CA = new Image(Defs.Resources.getAsStream(Defs.Resources.Image.IMAGE_CA));
    private static final Image IMAGE_GOLDEN_CA = new Image(Defs.Resources.getAsStream(Defs.Resources.Image.IMAGE_GOLDEN_CA));
    private static Rectangle hpRemoverRectangle;
    private static Rectangle caRemoverRectangle;
    private static String characterImageExtension = null;

    // Methods
    public static void setCharacterImageExtension(@Nullable final String characterImageExtension) {
        TabCharacter.characterImageExtension = characterImageExtension;
    }
    public static String getCharacterImageExtension() {
        return characterImageExtension;
    }

    // Old Values
    private static String oldValueCurrentLifeDiceAmount = null;
    private static String oldValueCalculatedMaxHP = "20";
    private static String oldValueMaxHP = "20";
    private static String oldValueCurrentHP = "20";
    private static String oldValueTempHP = "0";

    // Initialize
    public static void initialize(@NotNull final ControllerSceneSheetViewer controller) {
        controller.imageViewCharacterImage.setImage(JFXDefs.AppInfo.LOGO);
        hpRemoverRectangle = new Rectangle(controller.imageViewCurrentHP.getFitWidth(), 0, Client.getBackgroundThemeColor());
        caRemoverRectangle = new Rectangle(controller.imageViewCurrentCA.getFitWidth(), 0, Client.getBackgroundThemeColor());
        controller.spinnerLevel.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 1, 1));
        controller.spinnerProficiencyBonus.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 6, 2, 1));
        controller.spinnerInspiration.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0, 1));
        onLostFocusFireActionEvent(controller);
        setOnChangeTriggers(controller);
        updateLifeDiceFaces(controller);
        controller.textFieldTotalLifeDiceAmount.setText(String.valueOf(controller.spinnerLevel.getValue())); // This must be after "updateLifeDiceFaces(controller)"
        oldValueCurrentLifeDiceAmount = controller.textFieldCurrentLifeDiceAmount.getText();
        controller.sliderDSTSuccesses.valueProperty().addListener((observable, oldValue, newValue) -> controller.sliderDSTSuccesses.setValue(newValue.intValue()));
        controller.sliderDSTFailures.valueProperty().addListener((observable, oldValue, newValue) -> controller.sliderDSTFailures.setValue(newValue.intValue()));
        readTabData(controller);
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
                        String characterName = SheetDataHandler.readKeyParameter(KeyParameters.TabCharacter.CHARACTER_NAME);
                        String characterClass = SheetDataHandler.readKeyParameter(KeyParameters.TabCharacter.CLASS);
                        String level = SheetDataHandler.readKeyParameter(KeyParameters.TabCharacter.LEVEL);
                        String background = SheetDataHandler.readKeyParameter(KeyParameters.TabCharacter.BACKGROUND);
                        String playerName = SheetDataHandler.readKeyParameter(KeyParameters.TabCharacter.PLAYER_NAME);
                        String race = SheetDataHandler.readKeyParameter(KeyParameters.TabCharacter.RACE);
                        String alignment = SheetDataHandler.readKeyParameter(KeyParameters.TabCharacter.ALIGNMENT);
                        String exp = SheetDataHandler.readKeyParameter(KeyParameters.TabCharacter.EXP);
                        String base64CharacterImage = SheetDataHandler.readKeyParameter(KeyParameters.CHARACTER_IMAGE);
                        String characterImageExtension = SheetDataHandler.readKeyParameter(KeyParameters.CHARACTER_IMAGE_EXTENSION);
                        Image characterImage = null;
                        try {
                            if (base64CharacterImage != null) {
                                if (characterImageExtension == null) throw new IllegalArgumentException("Image without declared extension");
                                byte[] imageBytes = Base64.getDecoder().decode(base64CharacterImage);
                                ByteArrayInputStream imageStream = new ByteArrayInputStream(imageBytes);
                                BufferedImage bufferedImageCharacter = ImageIO.read(imageStream);
                                characterImage = SwingFXUtils.toFXImage(bufferedImageCharacter, null);
                            }
                        } catch (IllegalArgumentException | IOException e) {
                            Logger.log(e);
                            Platform.runLater(() -> new ErrorAlert("ERRORE", "ERRORE DI LETTURA", "L'immagine ricevuta dal database non è leggibile."));
                        }
                        String maxHP = SheetDataHandler.readKeyParameter(KeyParameters.TabCharacter.MAX_HP);
                        String currentHP = SheetDataHandler.readKeyParameter(KeyParameters.TabCharacter.CURRENT_HP);
                        String tempHP = SheetDataHandler.readKeyParameter(KeyParameters.TabCharacter.TEMP_HP);
                        String currentLifeDicesAmount = SheetDataHandler.readKeyParameter(KeyParameters.TabCharacter.CURRENT_LIFE_DICES);
                        String lifeDiceFaces = SheetDataHandler.readKeyParameter(KeyParameters.TabCharacter.LIFE_DICE_FACES);
                        String proficiencyBonus = SheetDataHandler.readKeyParameter(KeyParameters.TabCharacter.PROFICIENCY_BONUS);
                        String inspiration = SheetDataHandler.readKeyParameter(KeyParameters.TabCharacter.INSPIRATION);
                        String speed = SheetDataHandler.readKeyParameter(KeyParameters.TabCharacter.SPEED);
                        String personalityTraits = SheetDataHandler.readKeyParameter(KeyParameters.TabCharacter.PERSONALITY_TRAITS);
                        String ideals = SheetDataHandler.readKeyParameter(KeyParameters.TabCharacter.IDEALS);
                        String bonds = SheetDataHandler.readKeyParameter(KeyParameters.TabCharacter.BONDS);
                        String flaws = SheetDataHandler.readKeyParameter(KeyParameters.TabCharacter.FLAWS);
                        Image finalCharacterImage = characterImage;
                        Platform.runLater(() -> {
                            if (characterName != null) {
                                controller.textFieldCharacterName.setText(characterName);
                                DiscordRichPresenceManager.setCharacterName(characterName);
                            }
                            if (characterClass != null) controller.textFieldClass.setText(characterClass);
                            if (level != null) {
                                controller.spinnerLevel.getValueFactory().setValue(Integer.parseInt(level));
                                controller.textFieldTotalLifeDiceAmount.setText(level);
                                DiscordRichPresenceManager.setLevel(level);
                            }
                            if (background != null) controller.textFieldBackground.setText(background);
                            if (playerName != null) controller.textFieldPlayerName.setText(playerName);
                            if (race != null) controller.textFieldRace.setText(race);
                            if (alignment != null) controller.textFieldAlignment.setText(alignment);
                            if (exp != null) controller.textFieldExperience.setText(exp);
                            if (finalCharacterImage != null) {
                                controller.imageViewCharacterImage.setImage(finalCharacterImage);
                                controller.imageViewCharacterBodyImage.setImage(finalCharacterImage);
                                TabCharacter.characterImageExtension = characterImageExtension;
                            }
                            if (maxHP != null) controller.textFieldMaxHP.setText(maxHP);
                            if (currentHP != null) controller.textFieldCurrentHP.setText(currentHP);
                            if (tempHP != null) controller.textFieldTempHP.setText(tempHP);
                            if (currentLifeDicesAmount != null) controller.textFieldCurrentLifeDiceAmount.setText(currentLifeDicesAmount);
                            else controller.textFieldCurrentLifeDiceAmount.setText(String.valueOf(controller.spinnerLevel.getValue()));
                            if (lifeDiceFaces != null) {
                                controller.textFieldCurrentLifeDiceFaces.setText(lifeDiceFaces);
                                controller.textFieldTotalLifeDiceFaces.setText(lifeDiceFaces);
                            }
                            if (proficiencyBonus != null) controller.spinnerProficiencyBonus.getValueFactory().setValue(Integer.parseInt(proficiencyBonus));
                            if (inspiration != null) controller.spinnerInspiration.getValueFactory().setValue(Integer.parseInt(inspiration));
                            if (speed != null) controller.textFieldSpeed.setText(speed);
                            if (personalityTraits != null) controller.textAreaPersonalityTraits.setText(personalityTraits);
                            if (ideals != null) controller.textAreaIdeals.setText(ideals);
                            if (bonds != null) controller.textAreaBonds.setText(bonds);
                            if (flaws != null) controller.textAreaFlaws.setText(flaws);
                        });
                        return null;
                    }
                };
            }
        }.start();
    }

    // OnChange Triggers Setter
    private static void setOnChangeTriggers(@NotNull final ControllerSceneSheetViewer controller) {
        controller.textFieldCharacterName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.replace(" ", "").isEmpty()) DiscordRichPresenceManager.setCharacterName(null);
            else DiscordRichPresenceManager.setCharacterName(newValue);
            SheetDataHandler.writeKeyParameter(KeyParameters.TabCharacter.CHARACTER_NAME, newValue);
        });
        controller.textFieldClass.textProperty().addListener((observable, oldValue, newValue) -> {
            SheetDataHandler.writeKeyParameter(KeyParameters.TabCharacter.CLASS, newValue);
            controller.textFieldSpellCasterClass.setText(newValue);
        });
        controller.spinnerLevel.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            SheetDataHandler.writeKeyParameter(KeyParameters.TabCharacter.LEVEL, newValue);
            if (newValue.replace(" ", "").isEmpty()) DiscordRichPresenceManager.setLevel(null);
            else DiscordRichPresenceManager.setLevel(newValue);
            controller.textFieldTotalLifeDiceAmount.setText(newValue);
            updateLifeDiceAmount(controller);
        });
        controller.textFieldBackground.textProperty().addListener(((observable, oldValue, newValue) -> SheetDataHandler.writeKeyParameter(KeyParameters.TabCharacter.BACKGROUND, newValue)));
        controller.textFieldPlayerName.textProperty().addListener((observable, oldValue, newValue) -> SheetDataHandler.writeKeyParameter(KeyParameters.TabCharacter.PLAYER_NAME, newValue));
        controller.textFieldRace.textProperty().addListener((observable, oldValue, newValue) -> SheetDataHandler.writeKeyParameter(KeyParameters.TabCharacter.RACE, newValue));
        controller.textFieldAlignment.textProperty().addListener((observable, oldValue, newValue) -> SheetDataHandler.writeKeyParameter(KeyParameters.TabCharacter.ALIGNMENT, newValue));
        controller.textFieldExperience.textProperty().addListener((observable, oldValue, newValue) -> SheetDataHandler.writeKeyParameter(KeyParameters.TabCharacter.EXP, newValue));
        controller.textFieldMaxHP.textProperty().addListener((observable, oldValue, newValue) -> {
            SheetDataHandler.writeKeyParameter(KeyParameters.TabCharacter.MAX_HP, newValue);
            TabEquipment.updateMaxCalculatedHP(controller);
        });
        controller.textFieldCurrentHP.textProperty().addListener((observable, oldValue, newValue) -> {
            SheetDataHandler.writeKeyParameter(KeyParameters.TabCharacter.CURRENT_HP, newValue);
            recalculateHealthPercentage(controller);
        });
        controller.textFieldTempHP.textProperty().addListener((observable, oldValue, newValue) -> {
            SheetDataHandler.writeKeyParameter(KeyParameters.TabCharacter.TEMP_HP, newValue);
            recalculateHealthPercentage(controller);
        });
        controller.textFieldCurrentLifeDiceFaces.textProperty().addListener(((observable, oldValue, newValue) -> SheetDataHandler.writeKeyParameter(KeyParameters.TabCharacter.LIFE_DICE_FACES, newValue)));
        controller.textFieldCurrentLifeDiceAmount.textProperty().addListener((observable, oldValue, newValue) -> SheetDataHandler.writeKeyParameter(KeyParameters.TabCharacter.CURRENT_LIFE_DICES, newValue));
        controller.spinnerProficiencyBonus.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            SheetDataHandler.writeKeyParameter(KeyParameters.TabCharacter.PROFICIENCY_BONUS, newValue);
            updateProficiencyBonus(controller);
        });
        controller.spinnerInspiration.getEditor().textProperty().addListener((observable, oldValue, newValue) -> SheetDataHandler.writeKeyParameter(KeyParameters.TabCharacter.INSPIRATION, newValue));
        controller.textFieldSpeed.textProperty().addListener((observable, oldValue, newValue) -> SheetDataHandler.writeKeyParameter(KeyParameters.TabCharacter.SPEED, newValue));
        controller.textAreaPersonalityTraits.textProperty().addListener((observable, oldValue, newValue) -> SheetDataHandler.writeKeyParameter(KeyParameters.TabCharacter.PERSONALITY_TRAITS, newValue));
        controller.textAreaIdeals.textProperty().addListener((observable, oldValue, newValue) -> SheetDataHandler.writeKeyParameter(KeyParameters.TabCharacter.IDEALS, newValue));
        controller.textAreaBonds.textProperty().addListener((observable, oldValue, newValue) -> SheetDataHandler.writeKeyParameter(KeyParameters.TabCharacter.BONDS, newValue));
        controller.textAreaFlaws.textProperty().addListener((observable, oldValue, newValue) -> SheetDataHandler.writeKeyParameter(KeyParameters.TabCharacter.FLAWS, newValue));
    }

    // Lost Focus On Action Fire Event
    private static void onLostFocusFireActionEvent(@NotNull final ControllerSceneSheetViewer controller) {
        controller.textFieldClass.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) controller.textFieldSpellCasterClass.setText(controller.textFieldClass.getText());
        });
        controller.textFieldCurrentLifeDiceFaces.focusedProperty().addListener(((observable, oldValue, newValue) -> {
            if (!newValue) updateLifeDiceFaces(controller);
        }));
        controller.textFieldMaxHP.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) TabEquipment.updateMaxCalculatedHP(controller);
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
        TabAbility.updateParameters(controller);
    }
    public static void updateCASymbol(@NotNull final ControllerSceneSheetViewer controller, int CA) {
        double newCAPercentage = controller.imageViewCurrentCA.getFitHeight() - (controller.imageViewCurrentCA.getFitHeight() * CA / Defs.MAX_CA);
        if (newCAPercentage<0) newCAPercentage = 0;
        if (CA >= Defs.MAX_CA) controller.imageViewCurrentCA.setImage(TabCharacter.IMAGE_GOLDEN_CA);
        else controller.imageViewCurrentCA.setImage(TabCharacter.IMAGE_CA);
        caRemoverRectangle.setHeight(newCAPercentage);
        controller.stackPaneCurrentCA.getChildren().remove(caRemoverRectangle);
        controller.stackPaneCurrentCA.getChildren().add(caRemoverRectangle);
        controller.labelCA.setText(String.valueOf(CA));
    }
    public static void recalculateHealthPercentage(@NotNull final ControllerSceneSheetViewer controller) {
        double maxHP, currentHP, tempHP;
        try {
            maxHP = (controller.textFieldCalculatedMaxHP.getText().replace(" ", "").isEmpty()? 0 : Double.parseDouble(controller.textFieldCalculatedMaxHP.getText()));
        } catch (NumberFormatException e) {
            controller.textFieldCalculatedMaxHP.setText(oldValueCalculatedMaxHP);
            new ErrorAlert("ERRORE", "ERRORE DI ELABORAZIONE", "I punti ferita massimi calcolati devono essere un numero maggiore a 0.");
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
        oldValueCalculatedMaxHP = controller.textFieldCalculatedMaxHP.getText();
        oldValueCurrentHP = controller.textFieldCurrentHP.getText();
        oldValueTempHP = controller.textFieldTempHP.getText();
    }
    public static void updateCalculatedMaxHP(@NotNull final ControllerSceneSheetViewer controller, final int lifeEffect, final double lifePercentageEffect) {
        double maxHP;
        try {
            maxHP = (controller.textFieldMaxHP.getText().replace(" ", "").isEmpty() ? 0 : Double.parseDouble(controller.textFieldMaxHP.getText()));
            if (maxHP <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            controller.textFieldMaxHP.setText(oldValueMaxHP);
            new ErrorAlert("ERRORE", "ERRORE DI ELABORAZIONE", "I punti ferita massimi devono essere un numero maggiore a 0.");
            return;
        }
        oldValueMaxHP = controller.textFieldMaxHP.getText();
        double calculatedMaxHP = maxHP + lifeEffect;
        calculatedMaxHP += (calculatedMaxHP*lifePercentageEffect) / 100;
        calculatedMaxHP = calculatedMaxHP>=1?calculatedMaxHP:1;
        controller.textFieldCalculatedMaxHP.setText(String.format("%.0f", calculatedMaxHP));
        recalculateHealthPercentage(controller);
    }
    @SuppressWarnings("DuplicatedCode")
    public static void openCharacterImageFileChooser(@NotNull final ControllerSceneSheetViewer controller) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona un Contenuto Multimediale");
        for (String ext : Defs.Resources.SQL.SUPPORTED_IMAGE_EXTENSIONS) {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(ext.toUpperCase(), "*."+ext));
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
                                Image fxImage = SwingFXUtils.toFXImage(img, null);
                                Platform.runLater(() -> {
                                    controller.imageViewCharacterImage.setImage(fxImage);
                                    controller.imageViewCharacterBodyImage.setImage(fxImage);
                                });
                                characterImageExtension = ImageHandler.getImageExtension(finalImagePath.getAbsolutePath());
                                SheetDataHandler.writeKeyParameter(KeyParameters.CHARACTER_IMAGE, KeyParameters.CHARACTER_IMAGE_EXTENSION, fxImage, characterImageExtension);
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
