package it.italiandudes.dnd_visualizer.javafx.controllers.campaign;

import it.italiandudes.dnd_visualizer.javafx.components.SceneController;
import it.italiandudes.dnd_visualizer.javafx.controllers.campaign.tab.ControllerSceneCampaignTabMaps;
import it.italiandudes.dnd_visualizer.javafx.controllers.campaign.tab.ControllerSceneCampaignTabSettings;
import it.italiandudes.dnd_visualizer.javafx.scene.campaign.tab.SceneCampaignTabMaps;
import it.italiandudes.dnd_visualizer.javafx.scene.campaign.tab.SceneCampaignTabSettings;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import org.jetbrains.annotations.NotNull;

public final class ControllerSceneCampaignViewer {

    // Graphic Elements
    @FXML private Tab tabMaps;
    @FXML private Tab tabEntities;
    @FXML private Tab tabElements;
    @FXML private Tab tabNotes;
    @FXML private Tab tabSettings;

    // Tab Scene Controllers
    private SceneController sceneMaps;
    private SceneController sceneEntities;
    private SceneController sceneElements;
    private SceneController sceneNotes;
    private SceneController sceneSettings;

    // Initialize
    @FXML
    private void initialize() {
        this.sceneMaps = SceneCampaignTabMaps.getScene();
        this.sceneSettings = SceneCampaignTabSettings.getScene();
        tabMaps.setContent(sceneMaps.getParent());
        tabSettings.setContent(sceneSettings.getParent());
    }

    // Tab SceneController Getter
    @NotNull
    public ControllerSceneCampaignTabMaps getControllerTabMaps() {
        return (ControllerSceneCampaignTabMaps) sceneMaps.getController();
    }
    @NotNull
    public ControllerSceneCampaignTabSettings getControllerTabSettings() {
        return (ControllerSceneCampaignTabSettings) sceneSettings.getController();
    }
}
