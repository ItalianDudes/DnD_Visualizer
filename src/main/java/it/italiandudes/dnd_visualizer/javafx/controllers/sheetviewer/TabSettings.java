package it.italiandudes.dnd_visualizer.javafx.controllers.sheetviewer;

import it.italiandudes.dnd_visualizer.db.DBManager;
import it.italiandudes.dnd_visualizer.javafx.Client;
import it.italiandudes.dnd_visualizer.javafx.alerts.ConfirmationAlert;
import it.italiandudes.dnd_visualizer.javafx.scene.SceneLoading;
import it.italiandudes.dnd_visualizer.javafx.scene.SceneMainMenu;

public final class TabSettings {

    public static void backToMenu() {
        if(!new ConfirmationAlert("MENU", "Tornare al Menu", "Vuoi davvero tornare al menu principale?\nRicorda: D&D Visualizer salva costantemente quindi i tuoi progressi sono al sicuro!").result) return;
        Client.setScene(SceneLoading.getScene());
        DBManager.closeConnection();
        Client.setScene(SceneMainMenu.getScene());
    }
    public static void quit() {
        if(!new ConfirmationAlert("CHIUSURA", "Chiusura Applicazione", "Vuoi davvero chiudere l'applicazione?\nRicorda: D&D Visualizer salva costantemente quindi i tuoi progressi sono al sicuro!").result) return;
        Client.setScene(SceneLoading.getScene());
        DBManager.closeConnection();
        System.exit(0);
    }
}
