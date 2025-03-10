package it.italiandudes.dnd_visualizer.javafx;

import it.italiandudes.dnd_visualizer.DnD_Visualizer;
import it.italiandudes.dnd_visualizer.db.DBManager;
import it.italiandudes.dnd_visualizer.features.DiscordRichPresenceManager;
import it.italiandudes.dnd_visualizer.javafx.alerts.ErrorAlert;
import it.italiandudes.dnd_visualizer.javafx.components.SceneController;
import it.italiandudes.dnd_visualizer.javafx.scene.SceneLoading;
import it.italiandudes.dnd_visualizer.javafx.scene.SceneMainMenu;
import it.italiandudes.dnd_visualizer.javafx.utils.Settings;
import it.italiandudes.dnd_visualizer.javafx.utils.ThemeHandler;
import it.italiandudes.dnd_visualizer.utils.Defs;
import it.italiandudes.idl.common.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.Clipboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class Client extends Application {

    // Attributes
    private static Application INSTANCE = null;
    private static Clipboard SYSTEM_CLIPBOARD = null;
    private static Stage STAGE = null;
    private static SceneController SCENE = null;

    // JavaFX Entry Point
    @Override
    public void start(Stage stage) {
        INSTANCE = this;
        startupApplicationStageConfiguration(stage);
    }

    // Initial Stage Configuration
    private static void startupApplicationStageConfiguration(@NotNull final Stage stage) {
        if (DnD_Visualizer.isStartedFromLauncher()) {
            Logger.log("Application started from launcher, changing ContextClassLoader to Launcher...", Defs.LOGGER_CONTEXT);
            Thread.currentThread().setContextClassLoader(DnD_Visualizer.getLauncherClassLoader());
        }
        Logger.log("Initializing JavaFX Stage...", Defs.LOGGER_CONTEXT);
        SYSTEM_CLIPBOARD = Clipboard.getSystemClipboard();
        Client.STAGE = stage;
        stage.setResizable(true);
        stage.setTitle(JFXDefs.AppInfo.NAME);
        stage.getIcons().add(JFXDefs.AppInfo.LOGO);
        SCENE = SceneMainMenu.getScene();
        stage.setScene(new Scene(SCENE.getParent()));
        Logger.log("Loading Theme...", Defs.LOGGER_CONTEXT);
        ThemeHandler.loadConfigTheme(stage.getScene());
        stage.show();
        Logger.log("JavaFX Stage Initialized! Post initialization...", Defs.LOGGER_CONTEXT);
        stage.setX((JFXDefs.SystemGraphicInfo.SCREEN_WIDTH - stage.getWidth()) / 2);
        stage.setY((JFXDefs.SystemGraphicInfo.SCREEN_HEIGHT - stage.getHeight()) / 2);
        stage.getScene().getWindow().addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, event -> exit());

        // Fullscreen Event Listener
        stage.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.F11) {
                if (!stage.isFullScreen()) {
                    if (!stage.isMaximized()) {
                        stage.setMaximized(true);
                    }
                    stage.setFullScreen(true);
                }
            } else if (event.getCode() == KeyCode.ESCAPE) {
                if (stage.isFullScreen()) stage.setFullScreen(false);
            }
        });

        // Notice into the logs that the application started Successfully
        Logger.log("Post completed, D&D Visualizer started successfully!", Defs.LOGGER_CONTEXT);
    }

    // Start Method
    public static void start(String[] args) {
        Settings.loadSettingsFile();
        if (!DnD_Visualizer.isStartedFromLauncher()) launch(args);
        else Platform.runLater(() -> startupApplicationStageConfiguration(new Stage()));
    }

    // Methods
    @Nullable
    public static Application getApplicationInstance() {
        return INSTANCE;
    }
    @NotNull
    public static Clipboard getSystemClipboard() {
        return SYSTEM_CLIPBOARD;
    }
    @NotNull
    public static Stage getStage(){
        return STAGE;
    }
    @NotNull
    public static SceneController getScene() {
        return SCENE;
    }
    public static void setScene(@Nullable final SceneController newScene) {
        if (newScene == null) {
            Client.exit(-1);
            return;
        }
        SCENE = newScene;
        STAGE.getScene().setRoot(newScene.getParent());
    }
    @NotNull
    public static Stage initPopupStage(@Nullable final SceneController sceneController) {
        return genPopupStage(Objects.requireNonNull(sceneController));
    }
    @NotNull
    private static Stage genPopupStage(@NotNull final SceneController sceneController) {
        Stage popupStage = new Stage();
        popupStage.setResizable(true);
        popupStage.getIcons().add(JFXDefs.AppInfo.LOGO);
        popupStage.setTitle(JFXDefs.AppInfo.NAME);
        popupStage.initOwner(STAGE);
        popupStage.initModality(Modality.WINDOW_MODAL);
        popupStage.setScene(new Scene(sceneController.getParent()));
        ThemeHandler.loadConfigTheme(popupStage.getScene());
        return popupStage;
    }
    public static void showMessageAndGoToMenu(@NotNull final Throwable t) {
        Logger.log(t, Defs.LOGGER_CONTEXT);
        Platform.runLater(() -> {
            new ErrorAlert("ERRORE", "Errore di Database", "Si e' verificato un errore durante la comunicazione con il database, ritorno al menu principale.");
            setScene(SceneLoading.getScene());
            DBManager.closeConnection();
            setScene(SceneMainMenu.getScene());
        });


    }
    public static void exit() {
        exit(0);
    }
    public static void exit(final int code) {
        if (code != 0) {
            Logger.log("Exit Method Called with non-zero code " + code + ", this probably means an error has occurred.", Defs.LOGGER_CONTEXT);
        } else {
            Logger.log("Exit Method Called, exiting D&D Visualizer...", Defs.LOGGER_CONTEXT);
        }
        Platform.runLater(() -> STAGE.hide());
        DBManager.closeConnection();
        DiscordRichPresenceManager.shutdownRichPresence();
        DnD_Visualizer.appClosed();
        if (!DnD_Visualizer.isStartedFromLauncher()) {
            Logger.close();
            Platform.exit();
            System.exit(code);
        }
    }
}
