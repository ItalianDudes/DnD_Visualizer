package it.italiandudes.dnd_visualizer.javafx.controllers.campaign.tab;

import it.italiandudes.dnd_visualizer.db.DBManager;
import it.italiandudes.dnd_visualizer.javafx.Client;
import it.italiandudes.dnd_visualizer.javafx.alerts.ConfirmationAlert;
import it.italiandudes.dnd_visualizer.javafx.scene.SceneLoading;
import it.italiandudes.dnd_visualizer.javafx.scene.SceneMainMenu;
import javafx.fxml.FXML;

@SuppressWarnings("JavaExistingMethodCanBeUsed")
public final class ControllerSceneCampaignTabSettings {

    // EDT
    @FXML
    private void backToMenu() {
        if(!new ConfirmationAlert("MENU", "Tornare al Menu", "Vuoi davvero tornare al menu principale?\nRicorda: D&D Visualizer salva costantemente quindi i tuoi progressi sono al sicuro!").result) return;
        Client.setScene(SceneLoading.getScene());
        DBManager.closeConnection();
        Client.setScene(SceneMainMenu.getScene());
    }
    @FXML
    private void quit() {
        if(!new ConfirmationAlert("CHIUSURA", "Chiusura Applicazione", "Vuoi davvero chiudere l'applicazione?\nRicorda: D&D Visualizer salva costantemente quindi i tuoi progressi sono al sicuro!").result) return;
        Client.setScene(SceneLoading.getScene());
        DBManager.closeConnection();
        Client.exit();
    }
}
