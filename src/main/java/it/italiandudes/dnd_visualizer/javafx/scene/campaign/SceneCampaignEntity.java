package it.italiandudes.dnd_visualizer.javafx.scene.campaign;

import it.italiandudes.dnd_visualizer.data.entities.Entity;
import it.italiandudes.dnd_visualizer.data.map.Map;
import it.italiandudes.dnd_visualizer.javafx.JFXDefs;
import it.italiandudes.dnd_visualizer.javafx.components.SceneController;
import it.italiandudes.dnd_visualizer.javafx.controllers.campaign.ControllerSceneCampaignEntity;
import it.italiandudes.dnd_visualizer.utils.Defs;
import it.italiandudes.idl.common.Logger;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class SceneCampaignEntity {

    // Scene Generator
    @NotNull
    public static SceneController getScene(@NotNull final Map map, @NotNull final Point2D center) {
        try {
            FXMLLoader loader = new FXMLLoader(Defs.Resources.get(JFXDefs.Resources.FXML.Campaign.FXML_CAMPAIGN_ENTITY));
            Parent root = loader.load();
            ControllerSceneCampaignEntity controller = loader.getController();
            controller.setMap(map);
            controller.setCenter(center);
            controller.configurationComplete();
            return new SceneController(root, controller);
        } catch (IOException e) {
            Logger.log(e, Defs.LOGGER_CONTEXT);
            System.exit(-1);
            return null;
        }
    }
    @NotNull
    public static SceneController getScene(@NotNull final Entity entity) {
        try {
            FXMLLoader loader = new FXMLLoader(Defs.Resources.get(JFXDefs.Resources.FXML.Campaign.FXML_CAMPAIGN_ENTITY));
            Parent root = loader.load();
            ControllerSceneCampaignEntity controller = loader.getController();
            controller.setEntity(entity);
            controller.configurationComplete();
            return new SceneController(root, controller);
        } catch (IOException e) {
            Logger.log(e, Defs.LOGGER_CONTEXT);
            System.exit(-1);
            return null;
        }
    }
}
