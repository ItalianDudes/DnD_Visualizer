package it.italiandudes.dnd_visualizer.javafx.controllers.campaign.tab;

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
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;

@SuppressWarnings("DuplicatedCode")
public final class ControllerSceneCampaignTabMaps {

    // Constants
    public static final float ZOOM_FACTOR = 1.1f;

    // Attributes
    private Map map = null;
    private Point2D mapMoveStartPoint = null;
    private Point2D mapImageLayoutStartPoint = null;
    private double scaledWidth;
    private double scaledHeight;
    private boolean mapChanging = false;

    // Graphic
    @FXML private AnchorPane anchorPaneMarkerContainer;
    @FXML private Label labelMarkerName;
    @FXML private AnchorPane anchorPaneMapContainer;
    @FXML private AnchorPane anchorPaneWaypointContainer; // The container of the map and of all it's waypoints
    @FXML private ImageView imageViewMap; // The Map
    @FXML private ListView<Map> listViewMaps;

    // Initialize
    @FXML private void initialize() {
        listViewMaps.setCellFactory(lv -> new ListCell<Map>() {
            @Override
            protected void updateItem(Map map, boolean empty) {
                super.updateItem(map, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(map.getName());
                    if (map.equals(ControllerSceneCampaignTabMaps.this.map)) {
                        setStyle("-fx-background-color: green;-fx-font-weight: bold;");
                    } else {
                        setStyle(null);
                    }
                }
            }
        });
        anchorPaneWaypointContainer.widthProperty().addListener((observable, oldValue, newValue) -> scaledWidth = newValue.doubleValue() * anchorPaneWaypointContainer.getScaleX());
        anchorPaneWaypointContainer.heightProperty().addListener((observable, oldValue, newValue) -> scaledHeight = newValue.doubleValue() * anchorPaneWaypointContainer.getScaleY());
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
    }

    // Methods
    private void changeMap(@Nullable final Map map) {
        if (mapChanging) return;
        mapChanging = true;
        this.map = map;
        Platform.runLater(() -> {
            listViewMaps.refresh();
            clearWaypointsFromWaypointContainer();

            if (map != null) {
                imageViewMap.setImage(map.getMap());
                imageViewMap.setFitWidth(imageViewMap.getImage().getWidth());
                imageViewMap.setFitHeight(imageViewMap.getImage().getHeight());
                anchorPaneWaypointContainer.setPrefWidth(imageViewMap.getFitWidth());
                anchorPaneWaypointContainer.setPrefHeight(imageViewMap.getFitHeight());
            } else {
                imageViewMap.setImage(null);
                imageViewMap.setFitWidth(0);
                imageViewMap.setFitHeight(0);
                anchorPaneWaypointContainer.setPrefWidth(0);
                anchorPaneWaypointContainer.setPrefHeight(0);
            }
            resetMapParameters();

            Rectangle clip = new Rectangle(anchorPaneMapContainer.getPrefWidth(), anchorPaneMapContainer.getPrefHeight());
            clip.widthProperty().bind(anchorPaneMapContainer.widthProperty());
            clip.heightProperty().bind(anchorPaneMapContainer.heightProperty());
            anchorPaneMapContainer.setClip(clip);

            if (map != null) {
                HashSet<Waypoint> waypoints = WaypointManager.getInstance().getMapWaypoints(map);
                waypoints.forEach(waypoint -> waypoint.setOnScroll(this::mouseWheelZoom));
                waypoints.forEach(waypoint ->  waypoint.setOnMouseDragged(ev -> moveWaypoint(ev, waypoint)));
                waypoints.forEach(waypoint -> waypoint.setOnContextMenuRequested(this::mapContextMenu));
                waypoints.forEach(this::configureWaypointHover);
                anchorPaneWaypointContainer.getChildren().addAll(waypoints);
            }

            mapChanging = false;
        });
    }
    private void clearWaypointsFromWaypointContainer() {
        anchorPaneWaypointContainer.getChildren().clear();
        anchorPaneWaypointContainer.getChildren().add(imageViewMap);
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
        if (mapMoveStartPoint == null || mapImageLayoutStartPoint == null || map == null) return;
        double moveOnX = event.getScreenX() - mapMoveStartPoint.getX();
        double moveOnY = event.getScreenY() - mapMoveStartPoint.getY();
        anchorPaneWaypointContainer.setLayoutX(mapImageLayoutStartPoint.getX() + moveOnX);
        anchorPaneWaypointContainer.setLayoutY(mapImageLayoutStartPoint.getY() + moveOnY);
        mapAtBorders();
    }
    @FXML private void startMove(@NotNull final MouseEvent event) {
        if (map == null) return;
        mapMoveStartPoint = new Point2D(event.getScreenX(), event.getScreenY());
        mapImageLayoutStartPoint = new Point2D(anchorPaneWaypointContainer.getLayoutX(), anchorPaneWaypointContainer.getLayoutY());
    }
    @FXML private void stopMove() {
        if (map == null) return;
        mapMoveStartPoint = null;
        mapImageLayoutStartPoint = null;
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

    // Waypoint Move
    private void moveWaypoint(@NotNull final MouseEvent event, @NotNull final Waypoint waypoint) {
        try {
            Point2D center = anchorPaneWaypointContainer.screenToLocal(event.getScreenX(), event.getScreenY());
            double newCenterX = center.getX();
            double newCenterY = center.getY();
            if (newCenterX < 0) newCenterX = 0;
            else if (newCenterX >= anchorPaneWaypointContainer.getPrefWidth()) newCenterX = anchorPaneWaypointContainer.getPrefWidth();
            if (newCenterY < 0) newCenterY = 0;
            else if (newCenterY >= anchorPaneWaypointContainer.getPrefHeight()) newCenterY = anchorPaneWaypointContainer.getPrefHeight();
            waypoint.setCenter(new Point2D(newCenterX, newCenterY));
        } catch (SQLException e) {
            Client.showMessageAndGoToMenu(e);
        }
    }

    // Waypoint Creator
    private void configureWaypointHover(@NotNull final Waypoint waypoint) {
        waypoint.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                waypoint.setScaleX(2);
                waypoint.setScaleY(2);
                labelMarkerName.setText(waypoint.getName());
                anchorPaneMarkerContainer.setVisible(true);
            } else {
                waypoint.setScaleX(1);
                waypoint.setScaleY(1);
                labelMarkerName.setText("");
                anchorPaneMarkerContainer.setVisible(false);
            }
        });
    }
    private void addMap(@NotNull final TextField nameField) {
        String name = nameField.getText();
        if (name.isEmpty() || name.replace("\t","").replace(" ", "").isEmpty()) return;
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
    }
    @FXML private void openMapListContextMenu(@NotNull final ContextMenuEvent event) {
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.setAutoHide(true);

        Menu addMapMenu = new Menu("Aggiungi Mappa");
        TextField nameField = new TextField();
        nameField.setPromptText("Nome");
        MenuItem addMapOption = new MenuItem();
        addMapOption.setGraphic(nameField);
        addMapOption.setOnAction(e -> addMap(nameField));
        addMapMenu.getItems().add(addMapOption);
        contextMenu.getItems().add(addMapMenu);
        Map map = listViewMaps.getSelectionModel().getSelectedItem();

        if (map != null) {
            MenuItem removeMap = new MenuItem("Rimuovi Mappa");
            removeMap.setOnAction(e -> {
                try {
                    changeMap(null);
                    WaypointManager.getInstance().unregisterAllMapWaypoints(map);
                    MapManager.getInstance().unregisterMap(map);
                    listViewMaps.getItems().remove(map);
                } catch (SQLException ex) {
                    Client.showMessageAndGoToMenu(ex);
                }
            });
            contextMenu.getItems().add(removeMap);

            MenuItem selectMap = new MenuItem("Seleziona Mappa");
            selectMap.setOnAction(e -> changeMap(map));
            contextMenu.getItems().addAll(selectMap);
        }

        contextMenu.show(Client.getStage(), event.getScreenX(), event.getScreenY());
    }
    private void addWaypoint(@NotNull final TextField nameField, @NotNull final Point2D center) {
        String name = nameField.getText();
        if (name.isEmpty() || name.replace("\t","").replace(" ", "").isEmpty()) return;
        try {
            Waypoint waypoint = WaypointManager.getInstance().registerWaypoint(map, name, center, WaypointType.ELEMENT_WEAPON, null, false);
            if (waypoint != null) {
                waypoint.setOnScroll(this::mouseWheelZoom);
                waypoint.setOnMouseDragged(ev -> moveWaypoint(ev, waypoint));
                waypoint.setOnContextMenuRequested(this::mapContextMenu);
                configureWaypointHover(waypoint);
                anchorPaneWaypointContainer.getChildren().add(waypoint);
            } else {
                new ErrorAlert("ERRORE", "Errore di Inserimento", "Un waypoint con queste caratteristiche e' gia' presente.");
            }
        } catch (SQLException | IOException ex) {
            Client.showMessageAndGoToMenu(ex);
            return;
        }
        nameField.clear();
    }
    @FXML private void mapContextMenu(@NotNull final ContextMenuEvent event) {
        if (map == null) return;
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.setAutoHide(true);

        Menu addWaypointMenu = new Menu("Aggiungi Waypoint");
        TextField nameField = new TextField();
        nameField.setPromptText("Nome");
        MenuItem addWaypointOption = new MenuItem();
        addWaypointOption.setGraphic(nameField);
        addWaypointOption.setOnAction(e -> addWaypoint(nameField, anchorPaneWaypointContainer.screenToLocal(event.getScreenX(), event.getScreenY())));
        addWaypointMenu.getItems().add(addWaypointOption);
        contextMenu.getItems().add(addWaypointMenu);

        if (event.getSource() instanceof Waypoint) {
            Waypoint waypoint = (Waypoint) event.getSource();

            MenuItem removeWaypoint = new MenuItem("Rimuovi Waypoint");
            removeWaypoint.setOnAction(e -> {
                try {
                    WaypointManager.getInstance().unregisterWaypoint(waypoint);
                    anchorPaneWaypointContainer.getChildren().remove(waypoint);
                } catch (SQLException ex) {
                    Client.showMessageAndGoToMenu(ex);
                }
            });
            contextMenu.getItems().add(removeWaypoint);
        }

        MenuItem reset = new MenuItem("Reimposta Mappa");
        reset.setOnAction(e -> resetMapParameters());
        contextMenu.getItems().add(reset);

        contextMenu.show(Client.getStage(), event.getScreenX(), event.getScreenY());
    }
    private void resetMapParameters() {
        anchorPaneWaypointContainer.setScaleX(1);
        anchorPaneWaypointContainer.setScaleY(1);
        anchorPaneWaypointContainer.setLayoutX(0);
        anchorPaneWaypointContainer.setLayoutY(0);
        scaledWidth = anchorPaneWaypointContainer.getWidth();
        scaledHeight = anchorPaneWaypointContainer.getHeight();
    }

    // Focus
    @FXML private void askFocus() {
        anchorPaneWaypointContainer.requestFocus();
    }

    // Map Zoom
    @FXML private void mouseWheelZoom(@NotNull final ScrollEvent event) {
        if (map == null) return;
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
