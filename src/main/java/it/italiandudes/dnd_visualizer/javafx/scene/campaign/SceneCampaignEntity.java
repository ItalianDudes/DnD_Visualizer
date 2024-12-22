package it.italiandudes.dnd_visualizer.javafx.scene.campaign;

import it.italiandudes.dnd_visualizer.data.enums.EntityType;
import it.italiandudes.dnd_visualizer.javafx.JFXDefs;
import it.italiandudes.dnd_visualizer.javafx.components.SceneController;
import it.italiandudes.dnd_visualizer.javafx.controllers.campaign.ControllerSceneCampaignEntity;
import it.italiandudes.dnd_visualizer.utils.Defs;
import it.italiandudes.idl.common.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class SceneCampaignEntity {

    // Scene Generator
    @NotNull
    public static SceneController getScene(@NotNull final EntityType type) {
        try {
            FXMLLoader loader = new FXMLLoader(Defs.Resources.get(JFXDefs.Resources.FXML.Campaign.FXML_CAMPAIGN_ENTITY));
            Parent root = loader.load();
            ControllerSceneCampaignEntity controller = loader.getController();
            controller.setEntityType(type);
            controller.configurationComplete();
            return new SceneController(root, controller);
        } catch (IOException e) {
            Logger.log(e);
            System.exit(-1);
            return null;
        }
    }
}
