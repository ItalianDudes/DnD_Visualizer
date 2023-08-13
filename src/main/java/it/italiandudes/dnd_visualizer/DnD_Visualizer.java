package it.italiandudes.dnd_visualizer;

import it.italiandudes.dnd_visualizer.client.javafx.Client;
import it.italiandudes.dnd_visualizer.utils.DBManager;
import it.italiandudes.idl.common.Logger;
import org.json.simple.parser.JSONParser;

import java.io.IOException;

@SuppressWarnings("unused")
public final class DnD_Visualizer {

    // Instance Attributes
    public static final JSONParser JSON_PARSER = new JSONParser();

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
        Runtime.getRuntime().addShutdownHook(new Thread(DBManager::closeConnection));

        // Start the client
        Client.start(args);
    }
}
