package it.italiandudes.dnd_visualizer.javafx.controller;

import it.italiandudes.dnd_visualizer.javafx.Client;
import it.italiandudes.dnd_visualizer.javafx.alert.ErrorAlert;
import it.italiandudes.dnd_visualizer.javafx.alert.InformationAlert;
import it.italiandudes.dnd_visualizer.javafx.scene.SceneMainMenu;
import it.italiandudes.dnd_visualizer.javafx.utils.Settings;
import it.italiandudes.dnd_visualizer.javafx.utils.ThemeHandler;
import it.italiandudes.dnd_visualizer.utils.Defs;
import it.italiandudes.dnd_visualizer.utils.DiscordRichPresenceManager;
import it.italiandudes.idl.common.Logger;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.json.JSONException;

import java.io.IOException;

public final class ControllerSceneSettingsEditor {

    // Attributes
    private static final Image DARK_MODE = new Image(Defs.Resources.getAsStream(Defs.Resources.Image.IMAGE_DARK_MODE));
    private static final Image LIGHT_MODE = new Image(Defs.Resources.getAsStream(Defs.Resources.Image.IMAGE_LIGHT_MODE));
    private static final Image TICK = new Image(Defs.Resources.getAsStream(Defs.Resources.Image.IMAGE_TICK));
    private static final Image CROSS = new Image(Defs.Resources.getAsStream(Defs.Resources.Image.IMAGE_CROSS));
    private static final Image WUMPUS = new Image(Defs.Resources.getAsStream(Defs.Resources.Image.IMAGE_WUMPUS));
    private static final Image NO_WUMPUS = new Image(Defs.Resources.getAsStream(Defs.Resources.Image.IMAGE_NO_WUMPUS));

    // Graphic Elements
    @FXML private ImageView imageViewEnableDarkMode;
    @FXML private ImageView imageViewEnableLoad;
    @FXML private ImageView imageViewEnablePassiveLoad;
    @FXML private ImageView imageViewCoinsIncreaseLoad;
    @FXML private ImageView imageViewEnableDiscordRichPresence;
    @FXML private ImageView imageViewEnableEventTheme;
    @FXML private ToggleButton toggleButtonEnableDarkMode;
    @FXML private ToggleButton toggleButtonEnableLoad;
    @FXML private ToggleButton toggleButtonEnablePassiveLoad;
    @FXML private ToggleButton toggleButtonCoinsIncreaseLoad;
    @FXML private ToggleButton toggleButtonEnableDiscordRichPresence;
    @FXML private ToggleButton toggleButtonEnableEventTheme;

    // Initialize
    @FXML
    private void initialize() {
        DiscordRichPresenceManager.updateRichPresenceState(DiscordRichPresenceManager.States.SETTINGS);
        toggleButtonEnableDarkMode.setSelected(Settings.getSettings().getBoolean(Defs.SettingsKeys.ENABLE_DARK_MODE));
        toggleButtonEnableLoad.setSelected(Settings.getSettings().getBoolean(Defs.SettingsKeys.ENABLE_LOAD));
        toggleButtonEnablePassiveLoad.setSelected(Settings.getSettings().getBoolean(Defs.SettingsKeys.ENABLE_PASSIVE_LOAD));
        toggleButtonCoinsIncreaseLoad.setSelected(Settings.getSettings().getBoolean(Defs.SettingsKeys.COINS_INCREASE_LOAD));
        toggleButtonEnableDiscordRichPresence.setSelected(Settings.getSettings().getBoolean(Defs.SettingsKeys.ENABLE_DISCORD_RICH_PRESENCE));
        toggleButtonEnableEventTheme.setSelected(Settings.getSettings().getBoolean(Defs.SettingsKeys.ENABLE_EVENT_THEME));
        if (toggleButtonEnableDarkMode.isSelected()) imageViewEnableDarkMode.setImage(DARK_MODE);
        else imageViewEnableDarkMode.setImage(LIGHT_MODE);
        if (toggleButtonEnableLoad.isSelected()) imageViewEnableLoad.setImage(TICK);
        else imageViewEnableLoad.setImage(CROSS);
        if (toggleButtonEnablePassiveLoad.isSelected()) imageViewEnablePassiveLoad.setImage(TICK);
        else imageViewEnablePassiveLoad.setImage(CROSS);
        if (toggleButtonCoinsIncreaseLoad.isSelected()) imageViewCoinsIncreaseLoad.setImage(TICK);
        else imageViewCoinsIncreaseLoad.setImage(CROSS);
        if (toggleButtonEnableDiscordRichPresence.isSelected()) imageViewEnableDiscordRichPresence.setImage(WUMPUS);
        else imageViewEnableDiscordRichPresence.setImage(NO_WUMPUS);
        if (toggleButtonEnableEventTheme.isSelected()) imageViewEnableEventTheme.setImage(TICK);
        else imageViewEnableEventTheme.setImage(CROSS);
    }

    // EDT
    @FXML
    private void toggleEnableDarkMode() {
        if (toggleButtonEnableDarkMode.isSelected()) {
            imageViewEnableDarkMode.setImage(DARK_MODE);
            ThemeHandler.loadDarkTheme(Client.getStage().getScene());
        }
        else {
            imageViewEnableDarkMode.setImage(LIGHT_MODE);
            ThemeHandler.loadLightTheme(Client.getStage().getScene());
        }
    }
    @FXML
    private void toggleEnableLoad() {
        if (toggleButtonEnableLoad.isSelected()) imageViewEnableLoad.setImage(TICK);
        else imageViewEnableLoad.setImage(CROSS);
    }
    @FXML
    private void toggleEnablePassiveLoad() {
        if (toggleButtonEnablePassiveLoad.isSelected()) imageViewEnablePassiveLoad.setImage(TICK);
        else imageViewEnablePassiveLoad.setImage(CROSS);
    }
    @FXML
    private void toggleCoinsIncreaseLoad() {
        if (toggleButtonCoinsIncreaseLoad.isSelected()) imageViewCoinsIncreaseLoad.setImage(TICK);
        else imageViewCoinsIncreaseLoad.setImage(CROSS);
    }
    @FXML
    private void toggleEnableDiscordRichPresence() {
        if (toggleButtonEnableDiscordRichPresence.isSelected()) imageViewEnableDiscordRichPresence.setImage(WUMPUS);
        else imageViewEnableDiscordRichPresence.setImage(NO_WUMPUS);
    }
    @FXML
    private void toggleEnableEventTheme() {
        if (toggleButtonEnableEventTheme.isSelected()) imageViewEnableEventTheme.setImage(TICK);
        else imageViewEnableEventTheme.setImage(CROSS);
    }
    @FXML
    private void backToMenu() {
        Client.setScene(SceneMainMenu.getScene());
    }
    @FXML
    private void save() {
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {
                        try {
                            Settings.getSettings().put(Defs.SettingsKeys.ENABLE_DARK_MODE, toggleButtonEnableDarkMode.isSelected());
                            Settings.getSettings().put(Defs.SettingsKeys.ENABLE_LOAD, toggleButtonEnableLoad.isSelected());
                            Settings.getSettings().put(Defs.SettingsKeys.ENABLE_PASSIVE_LOAD, toggleButtonEnablePassiveLoad.isSelected());
                            Settings.getSettings().put(Defs.SettingsKeys.COINS_INCREASE_LOAD, toggleButtonCoinsIncreaseLoad.isSelected());
                            Settings.getSettings().put(Defs.SettingsKeys.ENABLE_DISCORD_RICH_PRESENCE, toggleButtonEnableDiscordRichPresence.isSelected());
                            Settings.getSettings().put(Defs.SettingsKeys.ENABLE_EVENT_THEME, toggleButtonEnableEventTheme.isSelected());
                        } catch (JSONException e) {
                            Logger.log(e);
                        }
                        ThemeHandler.setConfigTheme();
                        if (!toggleButtonEnableDiscordRichPresence.isSelected()) {
                            DiscordRichPresenceManager.shutdownRichPresence();
                        }
                        try {
                            Settings.writeJSONSettings();
                            Platform.runLater(() -> new InformationAlert("SUCCESSO", "Salvataggio Impostazioni", "Impostazioni salvate e applicate con successo!"));
                        } catch (IOException e) {
                            Logger.log(e);
                            Platform.runLater(() -> new ErrorAlert("ERRORE", "Errore di I/O", "Si e' verificato un errore durante il salvataggio delle impostazioni."));
                        }
                        return null;
                    }
                };
            }
        }.start();
    }
}
