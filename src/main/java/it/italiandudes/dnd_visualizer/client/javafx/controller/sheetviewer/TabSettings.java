package it.italiandudes.dnd_visualizer.client.javafx.controller.sheetviewer;

import it.italiandudes.dnd_visualizer.client.javafx.Client;
import it.italiandudes.dnd_visualizer.client.javafx.alert.ConfirmationAlert;
import it.italiandudes.dnd_visualizer.client.javafx.scene.SceneLoading;
import it.italiandudes.dnd_visualizer.client.javafx.scene.SceneMainMenu;
import it.italiandudes.dnd_visualizer.db.DBManager;

public final class TabSettings {

    public static void backToMenu() {
        if(!new ConfirmationAlert("MENU", "Tornare al Menu", "Vuoi davvero tornare al menu principale?\nRicorda: D&D Visualizer salva costantemente quindi i tuoi progressi sono al sicuro!").result) return;
        Client.getStage().setScene(SceneLoading.getScene());
        DBManager.closeConnection();
        Client.getStage().setScene(SceneMainMenu.getScene());
    }
    public static void quit() {
        if(!new ConfirmationAlert("CHIUSURA", "Chiusura Applicazione", "Vuoi davvero chiudere l'applicazione?\nRicorda: D&D Visualizer salva costantemente quindi i tuoi progressi sono al sicuro!").result) return;
        Client.getStage().setScene(SceneLoading.getScene());
        DBManager.closeConnection();
        System.exit(0);
    }
}
