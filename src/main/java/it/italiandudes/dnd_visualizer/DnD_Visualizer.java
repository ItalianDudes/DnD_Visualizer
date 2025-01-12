package it.italiandudes.dnd_visualizer;

import it.italiandudes.dnd_visualizer.db.DBManager;
import it.italiandudes.dnd_visualizer.features.DiscordRichPresenceManager;
import it.italiandudes.dnd_visualizer.javafx.Client;
import it.italiandudes.dnd_visualizer.utils.Defs;
import it.italiandudes.idl.common.InfoFlags;
import it.italiandudes.idl.common.Logger;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public final class DnD_Visualizer {

    // Attributes
    private static volatile boolean appClosed = false;

    // Launcher Class Loader
    @Nullable private static ClassLoader launcherClassLoader = null;

    // Launcher Methods
    public static @Nullable ClassLoader getLauncherClassLoader() {
        return launcherClassLoader;
    }
    public static boolean isStartedFromLauncher() {
        return launcherClassLoader != null;
    }

    // Launcher Main Method
    @SuppressWarnings("unused")
    public static void launcherMain(ClassLoader loader, String[] args) {
        launcherClassLoader = loader;
        if (loader != null) Thread.currentThread().setContextClassLoader(loader);
        main(args);
    }
    public static void appClosed() {
        appClosed = true;
    }

    // Launcher Blocker
    @SuppressWarnings("unused")
    public static void launcherLockUntilAppClose() {
        //noinspection StatementWithEmptyBody
        while (!appClosed);
    }

    // Main Method
    public static void main(String[] args) {

        // Initializing the logger
        try {
            Logger.init();
            Logger.log("Logger initialized!", Defs.LOGGER_CONTEXT);
        } catch (IOException e) {
            Logger.log("An error has occurred during Logger initialization, exit...", Defs.LOGGER_CONTEXT);
            return;
        }

        // Configure the shutdown hooks
        Logger.log("Configuring Shutdown Hooks...", Defs.LOGGER_CONTEXT);
        Runtime.getRuntime().addShutdownHook(new Thread(Logger::close));
        Runtime.getRuntime().addShutdownHook(new Thread(DBManager::closeConnection));
        Runtime.getRuntime().addShutdownHook(new Thread(DiscordRichPresenceManager::shutdownRichPresence));
        Logger.log("Shutdown Hooks configured!", Defs.LOGGER_CONTEXT);

        // Start the client
        try {
            Logger.log("Starting UI...", Defs.LOGGER_CONTEXT);
            Client.start(args);
        } catch (NoClassDefFoundError e) {
            Logger.log("ERROR: TO RUN THIS JAR YOU NEED JAVA 8 WITH BUILT-IN JAVAFX!", new InfoFlags(true, true, true, true), Defs.LOGGER_CONTEXT);
            Logger.log(e, Defs.LOGGER_CONTEXT);
            if (!isStartedFromLauncher()) {
                Logger.close();
                System.exit(-1);
            }
        }
    }
}
