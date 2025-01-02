package it.italiandudes.dnd_visualizer.javafx.utils;

import it.italiandudes.dnd_visualizer.utils.Defs;
import it.italiandudes.dnd_visualizer.utils.JSONManager;
import it.italiandudes.idl.common.JarHandler;
import it.italiandudes.idl.common.Logger;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public final class Settings {

    // Settings
    private static JSONObject SETTINGS = null;

    // Settings Loader
    public static void loadSettingsFile() {
        File settingsFile = new File(Defs.Resources.JSON.CLIENT_SETTINGS);
        if (!settingsFile.exists() || !settingsFile.isFile()) {
            try {
                JarHandler.copyFileFromJar(new File(Defs.JAR_POSITION), Defs.Resources.JSON.DEFAULT_JSON_SETTINGS, settingsFile, true);
            } catch (IOException e) {
                Logger.log(e, Defs.LOGGER_CONTEXT);
                return;
            }
        }
        try {
            SETTINGS = JSONManager.readJSON(settingsFile);
            fixJSONSettings();
        } catch (IOException | JSONException e) {
            Logger.log(e, Defs.LOGGER_CONTEXT);
        }
    }

    // Settings Checker
    private static void fixJSONSettings() throws JSONException, IOException {
        try {
            SETTINGS.getBoolean(Defs.SettingsKeys.ENABLE_DARK_MODE);
        } catch (JSONException e) {
            SETTINGS.remove(Defs.SettingsKeys.ENABLE_DARK_MODE);
            SETTINGS.put(Defs.SettingsKeys.ENABLE_DARK_MODE, true);
        }
        try {
            SETTINGS.getBoolean(Defs.SettingsKeys.ENABLE_LOAD);
        } catch (JSONException e) {
            SETTINGS.remove(Defs.SettingsKeys.ENABLE_LOAD);
            SETTINGS.put(Defs.SettingsKeys.ENABLE_LOAD, true);
        }
        try {
            SETTINGS.getBoolean(Defs.SettingsKeys.ENABLE_PASSIVE_LOAD);
        } catch (JSONException e) {
            SETTINGS.remove(Defs.SettingsKeys.ENABLE_PASSIVE_LOAD);
            SETTINGS.put(Defs.SettingsKeys.ENABLE_PASSIVE_LOAD, true);
        }
        try {
            SETTINGS.getBoolean(Defs.SettingsKeys.COINS_INCREASE_LOAD);
        } catch (JSONException e) {
            SETTINGS.remove(Defs.SettingsKeys.COINS_INCREASE_LOAD);
            SETTINGS.put(Defs.SettingsKeys.COINS_INCREASE_LOAD, true);
        }
        try {
            SETTINGS.getBoolean(Defs.SettingsKeys.ENABLE_DISCORD_RICH_PRESENCE);
        } catch (JSONException e) {
            SETTINGS.remove(Defs.SettingsKeys.ENABLE_DISCORD_RICH_PRESENCE);
            SETTINGS.put(Defs.SettingsKeys.ENABLE_DISCORD_RICH_PRESENCE, true);
        }
        try {
            SETTINGS.getBoolean(Defs.SettingsKeys.ENABLE_EVENT_THEME);
        } catch (JSONException e) {
            SETTINGS.remove(Defs.SettingsKeys.ENABLE_EVENT_THEME);
            SETTINGS.put(Defs.SettingsKeys.ENABLE_EVENT_THEME, true);
        }
        try {
            SETTINGS.getBoolean(Defs.SettingsKeys.ENABLE_TUTORIAL);
        } catch (JSONException e) {
            SETTINGS.remove(Defs.SettingsKeys.ENABLE_TUTORIAL);
            SETTINGS.put(Defs.SettingsKeys.ENABLE_TUTORIAL, true);
        }
        JSONManager.writeJSON(SETTINGS, new File(Defs.Resources.JSON.CLIENT_SETTINGS));
    }

    // Settings Writer
    public static void writeJSONSettings() throws IOException {
        JSONManager.writeJSON(SETTINGS, new File(Defs.Resources.JSON.CLIENT_SETTINGS));
    }

    // Settings Getter
    @NotNull
    public static JSONObject getSettings() {
        return SETTINGS;
    }
}
