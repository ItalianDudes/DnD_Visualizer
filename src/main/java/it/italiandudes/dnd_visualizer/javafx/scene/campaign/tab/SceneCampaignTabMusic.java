package it.italiandudes.dnd_visualizer.javafx.scene.campaign.tab;

import it.italiandudes.dnd_visualizer.javafx.Client;
import it.italiandudes.dnd_visualizer.javafx.JFXDefs;
import it.italiandudes.dnd_visualizer.javafx.components.SceneController;
import it.italiandudes.dnd_visualizer.javafx.controllers.campaign.tab.ControllerSceneCampaignTabMusic;
import it.italiandudes.dnd_visualizer.utils.Defs;
import it.italiandudes.idl.common.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public final class SceneCampaignTabMusic {

    // Scene Generator
    public static SceneController getScene() {
        try {
            FXMLLoader loader = new FXMLLoader(Defs.Resources.get(JFXDefs.Resources.FXML.Campaign.Tab.FXML_CAMPAIGN_TAB_MUSIC));
            Parent root = loader.load();
            ControllerSceneCampaignTabMusic controller = loader.getController();
            return new SceneController(root, controller);
        } catch (IOException e) {
            Logger.log(e, Defs.LOGGER_CONTEXT);
            Client.exit(-1);
            return null;
        }
    }
}
