package it.italiandudes.dnd_visualizer.javafx.controllers.campaign.tab;

import it.italiandudes.dnd_visualizer.data.entities.Entity;
import it.italiandudes.dnd_visualizer.data.entities.PlayerEntity;
import it.italiandudes.dnd_visualizer.data.enums.WaypointType;
import it.italiandudes.dnd_visualizer.data.map.Map;
import it.italiandudes.dnd_visualizer.data.map.MapManager;
import it.italiandudes.dnd_visualizer.data.waypoints.Waypoint;
import it.italiandudes.dnd_visualizer.data.waypoints.WaypointManager;
import it.italiandudes.dnd_visualizer.javafx.Client;
import it.italiandudes.dnd_visualizer.javafx.alerts.ErrorAlert;
import it.italiandudes.dnd_visualizer.utils.Defs;
import it.italiandudes.idl.common.ImageHandler;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;

public final class ControllerSceneCampaignTabMaps {

    // Constants
    public static final float ZOOM_FACTOR = 1.1f;

    // Attributes
    private Map map = null;
    private Point2D moveStartPoint = null;
    private Point2D imageLayoutStartPoint = null;
    private double scaledWidth;
    private double scaledHeight;

    // Graphic
    @FXML private AnchorPane anchorPaneMapContainer;
    @FXML private AnchorPane anchorPaneWaypointContainer; // The container of the map and of all it's waypoints
    @FXML private ImageView imageViewMap; // The Map
    @FXML private ListView<Map> listViewMaps;

    // Initialize
    @FXML private void initialize() {

        anchorPaneWaypointContainer.widthProperty().addListener((observable, oldValue, newValue) -> scaledWidth = newValue.doubleValue() * anchorPaneWaypointContainer.getScaleX());
        anchorPaneWaypointContainer.heightProperty().addListener((observable, oldValue, newValue) -> scaledHeight = newValue.doubleValue() * anchorPaneWaypointContainer.getScaleY());

        // DELETE FROM HERE
        /*
        imageViewMap.setImage(JFXDefs.AppInfo.LOGO);
        imageViewMap.setFitWidth(imageViewMap.getImage().getWidth());
        imageViewMap.setFitHeight(imageViewMap.getImage().getHeight());
        anchorPaneWaypointContainer.setPrefWidth(imageViewMap.getFitWidth());
        anchorPaneWaypointContainer.setPrefHeight(imageViewMap.getFitHeight());
        scaledWidth = anchorPaneWaypointContainer.getWidth();
        scaledHeight = anchorPaneWaypointContainer.getHeight();
        Rectangle clip = new Rectangle(anchorPaneMapContainer.getPrefWidth(), anchorPaneMapContainer.getPrefHeight());
        clip.widthProperty().bind(anchorPaneMapContainer.widthProperty());
        clip.heightProperty().bind(anchorPaneMapContainer.heightProperty());
        anchorPaneMapContainer.setClip(clip);*/
        // TO HERE

        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {
                        HashSet<Map> maps = MapManager.getInstance().getMaps();
                        Platform.runLater(() -> listViewMaps.getItems().addAll(maps));
                        return null;
                    }
                };
            }
        }.start();

        /*
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        if (!WAYPOINTS_FILE.exists() || !WAYPOINTS_FILE.isFile()) {
                            waypoints = new JSONObject();
                            waypoints.put("waypoints", new JSONArray());
                            JSONManager.writeJSON(waypoints, WAYPOINTS_FILE);
                            return null;
                        }
                        if (waypoints == null) waypoints = JSONManager.readJSON(WAYPOINTS_FILE);
                        JSONArray wp = waypoints.getJSONArray("waypoints");
                        for (int i=0; i<wp.length(); i++) {
                            JSONObject waypoint = wp.getJSONObject(i);
                            Platform.runLater(() -> {
                                Circle c = new Circle(waypoint.getDouble("x"), waypoint.getDouble("y"), 3, Color.RED);
                                anchorPaneWaypointContainer.getChildren().add(c);
                            });
                        }
                        return null;
                    }
                };
            }
        }.start();*/
    }

    // Methods
    private void changeMap(@NotNull final Map map) {
        this.map = map;
        Platform.runLater(() -> {
            clearWaypointsFromWaypointContainer();
            imageViewMap.setImage(map.getMap());
            imageViewMap.setFitWidth(imageViewMap.getImage().getWidth());
            imageViewMap.setFitHeight(imageViewMap.getImage().getHeight());
            anchorPaneWaypointContainer.setPrefWidth(imageViewMap.getFitWidth());
            anchorPaneWaypointContainer.setPrefHeight(imageViewMap.getFitHeight());
            scaledWidth = anchorPaneWaypointContainer.getWidth();
            scaledHeight = anchorPaneWaypointContainer.getHeight();
            Rectangle clip = new Rectangle(anchorPaneMapContainer.getPrefWidth(), anchorPaneMapContainer.getPrefHeight());
            clip.widthProperty().bind(anchorPaneMapContainer.widthProperty());
            clip.heightProperty().bind(anchorPaneMapContainer.heightProperty());
            anchorPaneMapContainer.setClip(clip);
            System.out.println(WaypointManager.getInstance().getMapWaypoints(map));
            anchorPaneWaypointContainer.getChildren().addAll(WaypointManager.getInstance().getMapWaypoints(map));
        });
    }
    private void clearWaypointsFromWaypointContainer() {
        anchorPaneWaypointContainer.getChildren().stream().filter(node -> node instanceof Waypoint || node instanceof Entity || node instanceof PlayerEntity).forEach(node -> anchorPaneWaypointContainer.getChildren().remove(node));
    }

    // Map Movement Management
    private double layoutToCornerX(double layoutX) {
        double widthIncrease = scaledWidth - anchorPaneWaypointContainer.getWidth();
        return layoutX - (widthIncrease/2);
    }
    private double layoutToCornerY(double layoutY) {
        double heightIncrease = scaledHeight - anchorPaneWaypointContainer.getHeight();
        return layoutY - (heightIncrease/2);
    }
    private double cornerToLayoutX(double cornerX) {
        double widthIncrease = scaledWidth - anchorPaneWaypointContainer.getWidth();
        return cornerX + (widthIncrease/2);
    }
    private double cornerToLayoutY(double cornerY) {
        double heightIncrease = scaledHeight - anchorPaneWaypointContainer.getHeight();
        return cornerY + (heightIncrease/2);
    }
    @FXML private void moveMap(@NotNull final MouseEvent event) {
        if (moveStartPoint == null || imageLayoutStartPoint == null) return;
        double moveOnX = event.getScreenX() - moveStartPoint.getX();
        double moveOnY = event.getScreenY() - moveStartPoint.getY();
        anchorPaneWaypointContainer.setLayoutX(imageLayoutStartPoint.getX() + moveOnX);
        anchorPaneWaypointContainer.setLayoutY(imageLayoutStartPoint.getY() + moveOnY);
        mapAtBorders();
    }
    @FXML private void startMove(@NotNull final MouseEvent event) {
        moveStartPoint = new Point2D(event.getScreenX(), event.getScreenY());
        imageLayoutStartPoint = new Point2D(anchorPaneWaypointContainer.getLayoutX(), anchorPaneWaypointContainer.getLayoutY());
    }
    @FXML private void stopMove() {
        moveStartPoint = null;
        imageLayoutStartPoint = null;
    }
    private void mapAtBorders() {
        double leftBound = 0;
        double rightBound = anchorPaneMapContainer.getWidth();
        double bottomBound =  anchorPaneMapContainer.getHeight();
        double widthDifference = scaledWidth - anchorPaneMapContainer.getWidth();
        double heightDifference = scaledHeight - anchorPaneMapContainer.getHeight();
        double topBound = 0;

        double cornerX = layoutToCornerX(anchorPaneWaypointContainer.getLayoutX());
        double cornerY = layoutToCornerY(anchorPaneWaypointContainer.getLayoutY());

        if (widthDifference <= 0) {
            if (cornerX < leftBound) {
                anchorPaneWaypointContainer.setLayoutX(cornerToLayoutX(leftBound));
            }
            if (cornerX + scaledWidth > rightBound) {
                anchorPaneWaypointContainer.setLayoutX(cornerToLayoutX(rightBound - scaledWidth));
            }
        } else {
            if (cornerX > leftBound) {
                anchorPaneWaypointContainer.setLayoutX(cornerToLayoutX(leftBound));
            }
            if (cornerX + scaledWidth < rightBound) {
                anchorPaneWaypointContainer.setLayoutX(cornerToLayoutX(rightBound - scaledWidth));
            }
        }

        if (heightDifference <= 0) {
            if (cornerY < topBound) {
                anchorPaneWaypointContainer.setLayoutY(cornerToLayoutY(topBound));
            }
            if (cornerY + scaledHeight > bottomBound) {
                anchorPaneWaypointContainer.setLayoutY(cornerToLayoutY(bottomBound - scaledHeight));
            }
        } else {
            if (cornerY > topBound) {
                anchorPaneWaypointContainer.setLayoutY(cornerToLayoutY(topBound));
            }
            if (cornerY + scaledHeight < bottomBound) {
                anchorPaneWaypointContainer.setLayoutY(cornerToLayoutY(bottomBound - scaledHeight));
            }
        }

    }

    // Waypoint Creator
    @FXML private void openMapListContextMenu(@NotNull final ContextMenuEvent event) {
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.setAutoHide(true);

        Menu addMapMenu = new Menu("Aggiungi Mappa");
        TextField nameField = new TextField();
        nameField.setPromptText("Nome");
        MenuItem addMapOption = new MenuItem();
        addMapOption.setGraphic(nameField);
        addMapOption.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Seleziona un'Immagine");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Immagine", Arrays.stream(Defs.Resources.SQL.SUPPORTED_IMAGE_EXTENSIONS).map(ext -> "*." + ext).collect(Collectors.toList())));
            fileChooser.setInitialDirectory(new File(Defs.JAR_POSITION).getParentFile());
            File imagePath;
            try {
                imagePath = fileChooser.showOpenDialog(Client.getStage().getScene().getWindow());
            } catch (IllegalArgumentException ex) {
                fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
                imagePath = fileChooser.showOpenDialog(Client.getStage().getScene().getWindow());
            }
            if (imagePath == null) return;
            try {
                Image image = new Image(Files.newInputStream(imagePath.toPath()));
                Map map = MapManager.getInstance().registerMap(nameField.getText(), image, ImageHandler.getImageExtension(imagePath.getAbsolutePath()));
                if (map == null) {
                    new ErrorAlert("ERRORE", "Errore di Inserimento", "Questa mappa gia' esiste!");
                    return;
                }
                listViewMaps.getItems().add(map);
                changeMap(map);
            } catch (SQLException | IOException ex) {
                Client.showMessageAndGoToMenu(ex);
                return;
            }
            nameField.clear();
        });
        addMapMenu.getItems().add(addMapOption);
        contextMenu.getItems().add(addMapMenu);

        Map map = listViewMaps.getSelectionModel().getSelectedItem();
        if (map != null) {
            MenuItem selectMap = new MenuItem("Seleziona Mappa");
            selectMap.setOnAction(e -> changeMap(map));
            contextMenu.getItems().addAll(selectMap);
        }

        contextMenu.show(Client.getStage(), event.getScreenX(), event.getScreenY());
    }
    @FXML private void contextMenu(@NotNull final ContextMenuEvent event) {
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.setAutoHide(true);

        Menu addWaypointMenu = new Menu("Aggiungi Waypoint");
        TextField nameField = new TextField();
        nameField.setPromptText("Nome");
        MenuItem addWaypointOption = new MenuItem();
        addWaypointOption.setGraphic(nameField);
        addWaypointOption.setOnAction(e -> {
            Point2D center = anchorPaneWaypointContainer.screenToLocal(event.getScreenX(), event.getScreenY());
            try {
                Waypoint waypoint = WaypointManager.getInstance().registerWaypoint(map, nameField.getText(), center, WaypointType.GENERIC_WAYPOINT, null, false);
                anchorPaneWaypointContainer.getChildren().add(waypoint);
            } catch (SQLException | IOException ex) {
                Client.showMessageAndGoToMenu(ex);
                return;
            }
            nameField.clear();
        });
        addWaypointMenu.getItems().add(addWaypointOption);

        MenuItem reset = new MenuItem("Reimposta Mappa");
        reset.setOnAction(e -> {
            anchorPaneWaypointContainer.setScaleX(1);
            anchorPaneWaypointContainer.setScaleY(1);
            anchorPaneWaypointContainer.setLayoutX(0);
            anchorPaneWaypointContainer.setLayoutY(0);
            scaledWidth = anchorPaneWaypointContainer.getWidth();
            scaledHeight = anchorPaneWaypointContainer.getHeight();
        });

        contextMenu.getItems().addAll(addWaypointMenu, reset);
        contextMenu.show(Client.getStage(), event.getScreenX(), event.getScreenY());
    }

    // Focus
    @FXML private void askFocus() {
        anchorPaneWaypointContainer.requestFocus();
    }

    // Map Zoom
    @FXML private void mouseWheelZoom(@NotNull final ScrollEvent event) {
        if (event.getDeltaY() > 0) { // Zoom In
            anchorPaneWaypointContainer.setScaleX(anchorPaneWaypointContainer.getScaleX() * ZOOM_FACTOR);
            anchorPaneWaypointContainer.setScaleY(anchorPaneWaypointContainer.getScaleY() * ZOOM_FACTOR);
        } else if (event.getDeltaY() < 0) { // Zoom Out (not less than 1)
            anchorPaneWaypointContainer.setScaleX(anchorPaneWaypointContainer.getScaleX() / ZOOM_FACTOR);
            anchorPaneWaypointContainer.setScaleY(anchorPaneWaypointContainer.getScaleY() / ZOOM_FACTOR);
        }
        scaledWidth = anchorPaneWaypointContainer.getWidth() * anchorPaneWaypointContainer.getScaleX();
        scaledHeight = anchorPaneWaypointContainer.getHeight() * anchorPaneWaypointContainer.getScaleY();
        mapAtBorders();
    }
}
