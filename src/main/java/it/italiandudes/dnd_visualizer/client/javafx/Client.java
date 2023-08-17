package it.italiandudes.dnd_visualizer.client.javafx;

import it.italiandudes.dnd_visualizer.DnD_Visualizer;
import it.italiandudes.dnd_visualizer.client.javafx.scene.SceneMainMenu;
import it.italiandudes.dnd_visualizer.client.javafx.util.ThemeHandler;
import it.italiandudes.dnd_visualizer.utils.Defs;
import it.italiandudes.idl.common.JarHandler;
import it.italiandudes.idl.common.Logger;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@SuppressWarnings("unused")
public final class Client extends Application {

    //Attributes
    private static Stage stage;
    private static Image DEFAULT_IMAGE = null;
    private static JSONObject SETTINGS = null;
    private static Color COLOR_THEME_BACKGROUND;

    @Override
    public void start(Stage stage) {
        Client.stage = stage;
        stage.setTitle(JFXDefs.AppInfo.NAME);
        stage.getIcons().add(JFXDefs.AppInfo.LOGO);
        stage.setScene(SceneMainMenu.getScene());
        stage.show();
        stage.setX((JFXDefs.SystemGraphicInfo.SCREEN_WIDTH - stage.getWidth()) / 2);
        stage.setY((JFXDefs.SystemGraphicInfo.SCREEN_HEIGHT - stage.getHeight()) / 2);
        stage.getScene().getWindow().addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, event -> {
            stage.hide();
            Logger.log("JavaFX Window Close Event fired, exiting Java process...");
            System.exit(0);
        });
        DEFAULT_IMAGE = JFXDefs.AppInfo.LOGO;
        COLOR_THEME_BACKGROUND = (Color) ((Region) Client.getStage().getScene().lookup(".root")).getBackground().getFills().get(0).getFill();

        // Notice into the logs that the application started Successfully
        Logger.log("Application started successfully!");
    }

    //Start Methods
    public static void start(String[] args) {
        File settingsFile = new File(Defs.Resources.JSON.JSON_CLIENT_SETTINGS);
        if (!settingsFile.exists() || !settingsFile.isFile()) {
            try {
                JarHandler.copyFileFromJar(new File(Defs.JAR_POSITION), Defs.Resources.JSON.DEFAULT_JSON_CLIENT_SETTINGS, settingsFile, true);
            } catch (IOException e) {
                Logger.log(e);
                return;
            }
        }
        try {
            SETTINGS = (JSONObject) DnD_Visualizer.JSON_PARSER.parse(new FileReader(settingsFile));
        } catch (IOException | ParseException e) {
            Logger.log(e);
            return;
        }
        ThemeHandler.setConfigTheme();
        launch(args);
    }

    //Methods
    @NotNull
    public static Stage getStage(){
        return stage;
    }
    @NotNull
    public static Image getDefaultImage() {
        return DEFAULT_IMAGE;
    }
    @NotNull
    public static JSONObject getSettings() {
        return SETTINGS;
    }
    @NotNull
    public static Color getBackgroundThemeColor() {
        return COLOR_THEME_BACKGROUND;
    }

}
