package it.italiandudes.dnd_visualizer.client.javafx.controller;

import it.italiandudes.dnd_visualizer.client.javafx.Client;
import it.italiandudes.dnd_visualizer.client.javafx.alert.ErrorAlert;
import it.italiandudes.dnd_visualizer.client.javafx.alert.InformationAlert;
import it.italiandudes.dnd_visualizer.client.javafx.scene.SceneMainMenu;
import it.italiandudes.dnd_visualizer.client.javafx.util.ThemeHandler;
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

    //Graphic Elements
    @FXML private ImageView imageViewEnableDarkMode;
    @FXML private ImageView imageViewEnableLoad;
    @FXML private ImageView imageViewEnablePassiveLoad;
    @FXML private ImageView imageViewCoinsIncreaseLoad;
    @FXML private ToggleButton toggleButtonEnableDarkMode;
    @FXML private ToggleButton toggleButtonEnableLoad;
    @FXML private ToggleButton toggleButtonEnablePassiveLoad;
    @FXML private ToggleButton toggleButtonCoinsIncreaseLoad;

    //Initialize
    @FXML
    private void initialize() {
        DiscordRichPresenceManager.updateRichPresenceState(DiscordRichPresenceManager.States.SETTINGS);
        Client.getStage().setResizable(false);
        toggleButtonEnableDarkMode.setSelected(Client.getSettings().getBoolean(Defs.SettingsKeys.ENABLE_DARK_MODE));
        toggleButtonEnableLoad.setSelected(Client.getSettings().getBoolean(Defs.SettingsKeys.ENABLE_LOAD));
        toggleButtonEnablePassiveLoad.setSelected(Client.getSettings().getBoolean(Defs.SettingsKeys.ENABLE_PASSIVE_LOAD));
        toggleButtonCoinsIncreaseLoad.setSelected(Client.getSettings().getBoolean(Defs.SettingsKeys.COINS_INCREASE_LOAD));
        if (toggleButtonEnableDarkMode.isSelected()) imageViewEnableDarkMode.setImage(DARK_MODE);
        else imageViewEnableDarkMode.setImage(LIGHT_MODE);
        if (toggleButtonEnableLoad.isSelected()) imageViewEnableLoad.setImage(TICK);
        else imageViewEnableLoad.setImage(CROSS);
        if (toggleButtonEnablePassiveLoad.isSelected()) imageViewEnablePassiveLoad.setImage(TICK);
        else imageViewEnablePassiveLoad.setImage(CROSS);
        if (toggleButtonCoinsIncreaseLoad.isSelected()) imageViewCoinsIncreaseLoad.setImage(TICK);
        else imageViewCoinsIncreaseLoad.setImage(CROSS);
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
    private void backToMenu() {
        Client.getStage().setScene(SceneMainMenu.getScene());
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
                            Client.getSettings().put(Defs.SettingsKeys.ENABLE_DARK_MODE, toggleButtonEnableDarkMode.isSelected());
                            Client.getSettings().put(Defs.SettingsKeys.ENABLE_LOAD, toggleButtonEnableLoad.isSelected());
                            Client.getSettings().put(Defs.SettingsKeys.ENABLE_PASSIVE_LOAD, toggleButtonEnablePassiveLoad.isSelected());
                            Client.getSettings().put(Defs.SettingsKeys.COINS_INCREASE_LOAD, toggleButtonCoinsIncreaseLoad.isSelected());
                        } catch (JSONException e) {
                            Logger.log(e);
                        }
                        ThemeHandler.setConfigTheme();
                        try {
                            Client.writeJSONSettings();
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
