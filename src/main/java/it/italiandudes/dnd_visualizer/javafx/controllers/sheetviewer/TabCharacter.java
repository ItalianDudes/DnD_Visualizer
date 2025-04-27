package it.italiandudes.dnd_visualizer.javafx.controllers.sheetviewer;

import it.italiandudes.dnd_visualizer.db.SheetKeyParameters;
import it.italiandudes.dnd_visualizer.features.DiscordRichPresenceManager;
import it.italiandudes.dnd_visualizer.javafx.Client;
import it.italiandudes.dnd_visualizer.javafx.JFXDefs;
import it.italiandudes.dnd_visualizer.javafx.alerts.ErrorAlert;
import it.italiandudes.dnd_visualizer.javafx.controllers.ControllerSceneSheetViewer;
import it.italiandudes.dnd_visualizer.javafx.utils.SheetDataHandler;
import it.italiandudes.dnd_visualizer.utils.Defs;
import it.italiandudes.idl.common.ImageHandler;
import it.italiandudes.idl.common.Logger;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.ContextMenuEvent;
import javafx.stage.FileChooser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.stream.Collectors;

public final class TabCharacter {

    // Attributes
    private static final Image IMAGE_CA = new Image(Defs.Resources.getAsStream(Defs.Resources.Image.IMAGE_CA));
    private static final Image IMAGE_GOLDEN_CA = new Image(Defs.Resources.getAsStream(Defs.Resources.Image.IMAGE_GOLDEN_CA));
    private static String characterImageExtension = null;

    // CA Influences
    private static boolean caInfluenceStrength = false;
    private static boolean caInfluenceDexterity = true;
    private static boolean caInfluenceConstitution = false;
    private static boolean caInfluenceIntelligence = false;
    private static boolean caInfluenceWisdom = false;
    private static boolean caInfluenceCharisma = false;

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
                return new Task<>() {
                    @Override
                    protected Void call() {
                        String characterName = SheetDataHandler.readKeyParameter(SheetKeyParameters.TabCharacter.CHARACTER_NAME);
                        String characterClass = SheetDataHandler.readKeyParameter(SheetKeyParameters.TabCharacter.CLASS);
                        String level = SheetDataHandler.readKeyParameter(SheetKeyParameters.TabCharacter.LEVEL);
                        String background = SheetDataHandler.readKeyParameter(SheetKeyParameters.TabCharacter.BACKGROUND);
                        String playerName = SheetDataHandler.readKeyParameter(SheetKeyParameters.TabCharacter.PLAYER_NAME);
                        String race = SheetDataHandler.readKeyParameter(SheetKeyParameters.TabCharacter.RACE);
                        String alignment = SheetDataHandler.readKeyParameter(SheetKeyParameters.TabCharacter.ALIGNMENT);
                        String exp = SheetDataHandler.readKeyParameter(SheetKeyParameters.TabCharacter.EXP);
                        String base64CharacterImage = SheetDataHandler.readKeyParameter(SheetKeyParameters.CHARACTER_IMAGE);
                        String characterImageExtension = SheetDataHandler.readKeyParameter(SheetKeyParameters.CHARACTER_IMAGE_EXTENSION);
                        Image characterImage = null;
                        try {
                            if (base64CharacterImage != null) {
                                if (characterImageExtension == null)
                                    throw new IllegalArgumentException("Image without declared extension");
                                byte[] imageBytes = Base64.getDecoder().decode(base64CharacterImage);
                                ByteArrayInputStream imageStream = new ByteArrayInputStream(imageBytes);
                                BufferedImage bufferedImageCharacter = ImageIO.read(imageStream);
                                characterImage = SwingFXUtils.toFXImage(bufferedImageCharacter, null);
                            }
                        } catch (IllegalArgumentException | IOException e) {
                            Logger.log(e, Defs.LOGGER_CONTEXT);
                            Platform.runLater(() -> new ErrorAlert("ERRORE", "ERRORE DI LETTURA", "L'immagine ricevuta dal database non è leggibile."));
                        }
                        String maxHP = SheetDataHandler.readKeyParameter(SheetKeyParameters.TabCharacter.MAX_HP);
                        String currentHP = SheetDataHandler.readKeyParameter(SheetKeyParameters.TabCharacter.CURRENT_HP);
                        String tempHP = SheetDataHandler.readKeyParameter(SheetKeyParameters.TabCharacter.TEMP_HP);
                        String currentLifeDicesAmount = SheetDataHandler.readKeyParameter(SheetKeyParameters.TabCharacter.CURRENT_LIFE_DICES);
                        String lifeDiceFaces = SheetDataHandler.readKeyParameter(SheetKeyParameters.TabCharacter.LIFE_DICE_FACES);
                        String proficiencyBonus = SheetDataHandler.readKeyParameter(SheetKeyParameters.TabCharacter.PROFICIENCY_BONUS);
                        String inspiration = SheetDataHandler.readKeyParameter(SheetKeyParameters.TabCharacter.INSPIRATION);
                        String speed = SheetDataHandler.readKeyParameter(SheetKeyParameters.TabCharacter.SPEED);
                        String personalityTraits = SheetDataHandler.readKeyParameter(SheetKeyParameters.TabCharacter.PERSONALITY_TRAITS);
                        String ideals = SheetDataHandler.readKeyParameter(SheetKeyParameters.TabCharacter.IDEALS);
                        String bonds = SheetDataHandler.readKeyParameter(SheetKeyParameters.TabCharacter.BONDS);
                        String flaws = SheetDataHandler.readKeyParameter(SheetKeyParameters.TabCharacter.FLAWS);
                        String caStrength = SheetDataHandler.readKeyParameter(SheetKeyParameters.TabCharacter.CA_STRENGTH);
                        String caDexterity = SheetDataHandler.readKeyParameter(SheetKeyParameters.TabCharacter.CA_DEXTERITY);
                        String caConstitution = SheetDataHandler.readKeyParameter(SheetKeyParameters.TabCharacter.CA_CONSTITUTION);
                        String caIntelligence = SheetDataHandler.readKeyParameter(SheetKeyParameters.TabCharacter.CA_INTELLIGENCE);
                        String caWisdom = SheetDataHandler.readKeyParameter(SheetKeyParameters.TabCharacter.CA_WISDOM);
                        String caCharisma = SheetDataHandler.readKeyParameter(SheetKeyParameters.TabCharacter.CA_CHARISMA);
                        caInfluenceStrength = caStrength != null && (Integer.parseInt(caStrength) != 0);
                        if (caDexterity != null) {
                            caInfluenceDexterity = Integer.parseInt(caDexterity) != 0;
                        }
                        caInfluenceConstitution = caConstitution != null && (Integer.parseInt(caConstitution) != 0);
                        caInfluenceIntelligence = caIntelligence != null && (Integer.parseInt(caIntelligence) != 0);
                        caInfluenceWisdom = caWisdom != null && (Integer.parseInt(caWisdom) != 0);
                        caInfluenceCharisma = caCharisma != null && (Integer.parseInt(caCharisma) != 0);

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
                            if (currentLifeDicesAmount != null)
                                controller.textFieldCurrentLifeDiceAmount.setText(currentLifeDicesAmount);
                            else
                                controller.textFieldCurrentLifeDiceAmount.setText(String.valueOf(controller.spinnerLevel.getValue()));
                            if (lifeDiceFaces != null) {
                                controller.textFieldCurrentLifeDiceFaces.setText(lifeDiceFaces);
                                controller.textFieldTotalLifeDiceFaces.setText(lifeDiceFaces);
                            }
                            if (proficiencyBonus != null)
                                controller.spinnerProficiencyBonus.getValueFactory().setValue(Integer.parseInt(proficiencyBonus));
                            if (inspiration != null)
                                controller.spinnerInspiration.getValueFactory().setValue(Integer.parseInt(inspiration));
                            if (speed != null) controller.textFieldSpeed.setText(speed);
                            if (personalityTraits != null)
                                controller.textAreaPersonalityTraits.setText(personalityTraits);
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

    // CA Mods
    public static int getCAMods(@NotNull final ControllerSceneSheetViewer controller) {
        int mods = 0;
        mods += caInfluenceStrength?Integer.parseInt(controller.labelModStrength.getText().replace("#", "0")):0;
        mods += caInfluenceDexterity?Integer.parseInt(controller.labelModDexterity.getText().replace("#", "0")):0;
        mods += caInfluenceConstitution?Integer.parseInt(controller.labelModConstitution.getText().replace("#", "0")):0;
        mods += caInfluenceIntelligence?Integer.parseInt(controller.labelModIntelligence.getText().replace("#", "0")):0;
        mods += caInfluenceWisdom?Integer.parseInt(controller.labelModWisdom.getText().replace("#", "0")):0;
        mods += caInfluenceCharisma?Integer.parseInt(controller.labelModCharisma.getText().replace("#", "0")):0;
        return mods;
    }

    // OnChange Triggers Setter
    private static void setOnChangeTriggers(@NotNull final ControllerSceneSheetViewer controller) {
        controller.textFieldCharacterName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.replace(" ", "").isEmpty()) DiscordRichPresenceManager.setCharacterName(null);
            else DiscordRichPresenceManager.setCharacterName(newValue);
            SheetDataHandler.writeKeyParameter(SheetKeyParameters.TabCharacter.CHARACTER_NAME, newValue);
        });
        controller.textFieldClass.textProperty().addListener((observable, oldValue, newValue) -> {
            SheetDataHandler.writeKeyParameter(SheetKeyParameters.TabCharacter.CLASS, newValue);
            controller.textFieldSpellCasterClass.setText(newValue);
        });
        controller.spinnerLevel.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            SheetDataHandler.writeKeyParameter(SheetKeyParameters.TabCharacter.LEVEL, newValue);
            if (newValue.replace(" ", "").isEmpty()) DiscordRichPresenceManager.setLevel(null);
            else DiscordRichPresenceManager.setLevel(newValue);
            controller.textFieldTotalLifeDiceAmount.setText(newValue);
            updateLifeDiceAmount(controller);
        });
        controller.textFieldBackground.textProperty().addListener(((observable, oldValue, newValue) -> SheetDataHandler.writeKeyParameter(SheetKeyParameters.TabCharacter.BACKGROUND, newValue)));
        controller.textFieldPlayerName.textProperty().addListener((observable, oldValue, newValue) -> SheetDataHandler.writeKeyParameter(SheetKeyParameters.TabCharacter.PLAYER_NAME, newValue));
        controller.textFieldRace.textProperty().addListener((observable, oldValue, newValue) -> SheetDataHandler.writeKeyParameter(SheetKeyParameters.TabCharacter.RACE, newValue));
        controller.textFieldAlignment.textProperty().addListener((observable, oldValue, newValue) -> SheetDataHandler.writeKeyParameter(SheetKeyParameters.TabCharacter.ALIGNMENT, newValue));
        controller.textFieldExperience.textProperty().addListener((observable, oldValue, newValue) -> SheetDataHandler.writeKeyParameter(SheetKeyParameters.TabCharacter.EXP, newValue));
        controller.textFieldMaxHP.textProperty().addListener((observable, oldValue, newValue) -> {
            SheetDataHandler.writeKeyParameter(SheetKeyParameters.TabCharacter.MAX_HP, newValue);
            TabEquipment.updateMaxCalculatedHP(controller);
        });
        controller.textFieldCurrentHP.textProperty().addListener((observable, oldValue, newValue) -> {
            SheetDataHandler.writeKeyParameter(SheetKeyParameters.TabCharacter.CURRENT_HP, newValue);
            recalculateHealthPercentage(controller);
        });
        controller.textFieldTempHP.textProperty().addListener((observable, oldValue, newValue) -> {
            SheetDataHandler.writeKeyParameter(SheetKeyParameters.TabCharacter.TEMP_HP, newValue);
            recalculateHealthPercentage(controller);
        });
        controller.textFieldCurrentLifeDiceFaces.textProperty().addListener(((observable, oldValue, newValue) -> SheetDataHandler.writeKeyParameter(SheetKeyParameters.TabCharacter.LIFE_DICE_FACES, newValue)));
        controller.textFieldCurrentLifeDiceAmount.textProperty().addListener((observable, oldValue, newValue) -> SheetDataHandler.writeKeyParameter(SheetKeyParameters.TabCharacter.CURRENT_LIFE_DICES, newValue));
        controller.spinnerProficiencyBonus.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            SheetDataHandler.writeKeyParameter(SheetKeyParameters.TabCharacter.PROFICIENCY_BONUS, newValue);
            updateProficiencyBonus(controller);
        });
        controller.spinnerInspiration.getEditor().textProperty().addListener((observable, oldValue, newValue) -> SheetDataHandler.writeKeyParameter(SheetKeyParameters.TabCharacter.INSPIRATION, newValue));
        controller.textFieldSpeed.textProperty().addListener((observable, oldValue, newValue) -> SheetDataHandler.writeKeyParameter(SheetKeyParameters.TabCharacter.SPEED, newValue));
        controller.textAreaPersonalityTraits.textProperty().addListener((observable, oldValue, newValue) -> SheetDataHandler.writeKeyParameter(SheetKeyParameters.TabCharacter.PERSONALITY_TRAITS, newValue));
        controller.textAreaIdeals.textProperty().addListener((observable, oldValue, newValue) -> SheetDataHandler.writeKeyParameter(SheetKeyParameters.TabCharacter.IDEALS, newValue));
        controller.textAreaBonds.textProperty().addListener((observable, oldValue, newValue) -> SheetDataHandler.writeKeyParameter(SheetKeyParameters.TabCharacter.BONDS, newValue));
        controller.textAreaFlaws.textProperty().addListener((observable, oldValue, newValue) -> SheetDataHandler.writeKeyParameter(SheetKeyParameters.TabCharacter.FLAWS, newValue));
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
    public static void openCAContextMenu(@NotNull final ControllerSceneSheetViewer controller, @NotNull final ContextMenuEvent event) {
        ContextMenu caMenu = new ContextMenu();

        CheckBox strength = new CheckBox("Aggiungi/Rimuovi Forza");
        strength.setSelected(caInfluenceStrength);
        MenuItem strengthItem = new MenuItem();
        strengthItem.setGraphic(strength);
        strength.setOnAction(e -> {
            caInfluenceStrength = strength.isSelected();
            SheetDataHandler.writeKeyParameter(SheetKeyParameters.TabCharacter.CA_STRENGTH, String.valueOf(caInfluenceStrength?1:0));
            TabEquipment.updateCA(controller);
        });

        CheckBox dexterity = new CheckBox("Aggiungi/Rimuovi Destrezza");
        dexterity.setSelected(caInfluenceDexterity);
        MenuItem dexterityItem = new MenuItem();
        dexterityItem.setGraphic(dexterity);
        dexterity.setOnAction(e -> {
            caInfluenceDexterity = dexterity.isSelected();
            SheetDataHandler.writeKeyParameter(SheetKeyParameters.TabCharacter.CA_DEXTERITY, String.valueOf(caInfluenceDexterity?1:0));
            TabEquipment.updateCA(controller);
        });

        CheckBox constitution = new CheckBox("Aggiungi/Rimuovi Costituzione");
        constitution.setSelected(caInfluenceConstitution);
        MenuItem constitutionItem = new MenuItem();
        constitutionItem.setGraphic(constitution);
        constitution.setOnAction(e -> {
            caInfluenceConstitution = constitution.isSelected();
            SheetDataHandler.writeKeyParameter(SheetKeyParameters.TabCharacter.CA_CONSTITUTION, String.valueOf(caInfluenceConstitution?1:0));
            TabEquipment.updateCA(controller);
        });

        CheckBox intelligence = new CheckBox("Aggiungi/Rimuovi Intelligenza");
        intelligence.setSelected(caInfluenceIntelligence);
        MenuItem intelligenceItem = new MenuItem();
        intelligenceItem.setGraphic(intelligence);
        intelligence.setOnAction(e -> {
            caInfluenceIntelligence = intelligence.isSelected();
            SheetDataHandler.writeKeyParameter(SheetKeyParameters.TabCharacter.CA_INTELLIGENCE, String.valueOf(caInfluenceIntelligence?1:0));
            TabEquipment.updateCA(controller);
        });

        CheckBox wisdom = new CheckBox("Aggiungi/Rimuovi Saggezza");
        wisdom.setSelected(caInfluenceWisdom);
        MenuItem wisdomItem = new MenuItem();
        wisdomItem.setGraphic(wisdom);
        wisdom.setOnAction(e -> {
            caInfluenceWisdom = wisdom.isSelected();
            SheetDataHandler.writeKeyParameter(SheetKeyParameters.TabCharacter.CA_WISDOM, String.valueOf(caInfluenceWisdom?1:0));
            TabEquipment.updateCA(controller);
        });

        CheckBox charisma = new CheckBox("Aggiungi/Rimuovi Carisma");
        charisma.setSelected(caInfluenceCharisma);
        MenuItem charismaItem = new MenuItem();
        charismaItem.setGraphic(charisma);
        charisma.setOnAction(e -> {
            caInfluenceCharisma = charisma.isSelected();
            SheetDataHandler.writeKeyParameter(SheetKeyParameters.TabCharacter.CA_CHARISMA, String.valueOf(caInfluenceCharisma?1:0));
            TabEquipment.updateCA(controller);
        });

        caMenu.getItems().addAll(strengthItem, dexterityItem, constitutionItem, intelligenceItem, wisdomItem, charismaItem);
        caMenu.setAutoHide(true);
        caMenu.show(Client.getStage(), event.getScreenX(), event.getScreenY());
    }
    public static void updateCASymbol(@NotNull final ControllerSceneSheetViewer controller, int CA) {
        if (CA >= Defs.MAX_CA) {
            controller.imageViewCurrentCA.setImage(TabCharacter.IMAGE_GOLDEN_CA);
        } else {
            controller.imageViewCurrentCA.setImage(TabCharacter.IMAGE_CA);
            double viewportHeight = Math.min(CA * controller.imageViewCurrentCA.getFitHeight() / Defs.MAX_CA, controller.imageViewCurrentCA.getFitHeight());
            if (viewportHeight > 0) {
                controller.imageViewCurrentCA.setVisible(true);
                controller.imageViewCurrentCA.setViewport(new Rectangle2D(0, controller.imageViewCurrentCA.getFitHeight() - viewportHeight, controller.imageViewCurrentCA.getFitWidth(), viewportHeight));
            } else {
                controller.imageViewCurrentCA.setVisible(false);
            }
        }
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
        if (maxHP > 0) {
            try {
                currentHP = (controller.textFieldCurrentHP.getText().replace(" ", "").isEmpty() ? 0 : Double.parseDouble(controller.textFieldCurrentHP.getText()));
                if (currentHP < 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                controller.textFieldCurrentHP.setText(oldValueCurrentHP);
                new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "I punti ferita attuali devono essere un numero maggiore o uguale a 0.");
                return;
            }
        } else {
            currentHP = 0;
            oldValueCurrentHP = "0";
            controller.textFieldCurrentHP.setText("0");
        }
        try {
            tempHP = (controller.textFieldTempHP.getText().replace(" ", "").isEmpty() ? 0 : Double.parseDouble(controller.textFieldTempHP.getText()));
            if (tempHP < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            controller.textFieldTempHP.setText(oldValueTempHP);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "I punti ferita temporanei devono essere un numero maggiore o uguale a 0.");
            return;
        }

        double calculatedCurrentHP = currentHP + tempHP;
        int hpPercentage = (int) (calculatedCurrentHP / maxHP * 100);
        controller.labelHPLeftPercentage.setText(hpPercentage > 500?">500%":hpPercentage+"%");
        calculatedCurrentHP = Math.min(calculatedCurrentHP, maxHP);
        double viewPortHeight = Math.min(calculatedCurrentHP * controller.imageViewCurrentHP.getFitHeight() / maxHP, controller.imageViewCurrentHP.getFitHeight());
        if (viewPortHeight > 0) {
            controller.imageViewCurrentHP.setVisible(true);
            controller.imageViewCurrentHP.setViewport(new Rectangle2D(0, controller.imageViewCurrentHP.getFitHeight() - viewPortHeight, controller.imageViewCurrentHP.getFitWidth(), viewPortHeight));
        } else {
            controller.imageViewCurrentHP.setVisible(false);
        }
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
        calculatedMaxHP = calculatedMaxHP>=0?calculatedMaxHP:0;
        controller.textFieldCalculatedMaxHP.setText(String.format("%.0f", calculatedMaxHP));
        recalculateHealthPercentage(controller);
    }
    @SuppressWarnings("DuplicatedCode")
    public static void openCharacterImageFileChooser(@NotNull final ControllerSceneSheetViewer controller) {
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
                                Image fxImage = SwingFXUtils.toFXImage(img, null);
                                Platform.runLater(() -> {
                                    controller.imageViewCharacterImage.setImage(fxImage);
                                    controller.imageViewCharacterBodyImage.setImage(fxImage);
                                });
                                characterImageExtension = ImageHandler.getImageExtension(finalImagePath.getAbsolutePath());
                                SheetDataHandler.writeKeyParameter(SheetKeyParameters.CHARACTER_IMAGE, SheetKeyParameters.CHARACTER_IMAGE_EXTENSION, img, characterImageExtension);
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
}
