package it.italiandudes.dnd_visualizer;

import it.italiandudes.dnd_visualizer.client.javafx.Client;
import it.italiandudes.idl.common.Logger;

import java.io.IOException;

public final class DnD_Visualizer {

    // Main Method
    public static void main(String[] args) {

        // Initializing the logger
        try {
            Logger.init();
        } catch (IOException e) {
            Logger.log("An error has occurred during Logger initialization, exit...");
        }

        // Configure the shutdown hooks
        Runtime.getRuntime().addShutdownHook(new Thread(Logger::close));

        // Start the client
        Client.start(args);
    }
}
