package it.italiandudes.dnd_visualizer;

import it.italiandudes.dnd_visualizer.db.DBManager;
import it.italiandudes.dnd_visualizer.javafx.Client;
import it.italiandudes.dnd_visualizer.utils.DiscordRichPresenceManager;
import it.italiandudes.idl.common.InfoFlags;
import it.italiandudes.idl.common.Logger;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public final class DnD_Visualizer {

    // Launcher Class Loader
    @Nullable private static ClassLoader launcherClassLoader = null;

    // Methods
    public static @Nullable ClassLoader getLauncherClassLoader() {
        return launcherClassLoader;
    }

    // Launcher Main Method
    @SuppressWarnings("unused")
    public static void launcherMain(ClassLoader loader, String[] args) {
        launcherClassLoader = loader;
        if (loader != null) Thread.currentThread().setContextClassLoader(loader);
        main(args);
    }

    // Main Method
    public static void main(String[] args) {

        // Initializing the logger
        try {
            Logger.init();
        } catch (IOException e) {
            Logger.log("An error has occurred during Logger initialization, exit...");
            return;
        }

        // Configure the shutdown hooks
        Runtime.getRuntime().addShutdownHook(new Thread(Logger::close));
        Runtime.getRuntime().addShutdownHook(new Thread(DBManager::closeConnection));
        Runtime.getRuntime().addShutdownHook(new Thread(DiscordRichPresenceManager::shutdownRichPresence));

        // Start the client
        try {
            Client.start(args);
        } catch (NoClassDefFoundError e) {
            Logger.log("ERROR: TO RUN THIS JAR YOU NEED JAVA 8 WITH BUILT-IN JAVAFX!", new InfoFlags(true, true, true, true));
            Logger.log(e);
            System.exit(0);
        }
    }
}
