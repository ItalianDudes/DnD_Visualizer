package it.italiandudes.dnd_visualizer.javafx.controller.campaign;

import it.italiandudes.dnd_visualizer.data.map.Map;
import it.italiandudes.dnd_visualizer.javafx.Client;
import it.italiandudes.dnd_visualizer.javafx.JFXDefs;
import it.italiandudes.dnd_visualizer.utils.Defs;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.File;

public final class ControllerSceneCampaignMap {

    // Constants
    public static final File WAYPOINTS_FILE = new File("waypoints.json");
    public static final float ZOOM_FACTOR = 1.1f;

    // Constructor Attributes
    private Map map = null;
    private volatile boolean configurationComplete = false;

    // Constructor Methods
    public void setMap(@NotNull final Map map) {
        if (this.map != null) this.map = map;
    }
    public void configurationComplete() {
        configurationComplete = true;
    }

    // Attributes
    private Point2D moveStartPoint = null;
    private Point2D imageLayoutStartPoint = null;
    private JSONObject waypoints = null;
    private double scaledWidth;
    private double scaledHeight;

    // Graphic
    @FXML private AnchorPane anchorPaneMapContainer;
    @FXML private AnchorPane anchorPaneWaypointContainer; // The container of the map and of all it's waypoints
    @FXML private ImageView imageViewMap; // The Map

    // Initialize
    @FXML private void initialize() {

        // DELETE FROM HERE
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
        anchorPaneMapContainer.setClip(clip);
        anchorPaneWaypointContainer.widthProperty().addListener((observable, oldValue, newValue) -> scaledWidth = newValue.doubleValue() * anchorPaneWaypointContainer.getScaleX());
        anchorPaneWaypointContainer.heightProperty().addListener((observable, oldValue, newValue) -> scaledHeight = newValue.doubleValue() * anchorPaneWaypointContainer.getScaleY());
        // TO HERE

        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {
                        //noinspection StatementWithEmptyBody
                        while (!configurationComplete);
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
                        anchorPaneWaypointContainer.widthProperty().addListener((observable, oldValue, newValue) -> scaledWidth = newValue.doubleValue() * anchorPaneWaypointContainer.getScaleX());
                        anchorPaneWaypointContainer.heightProperty().addListener((observable, oldValue, newValue) -> scaledHeight = newValue.doubleValue() * anchorPaneWaypointContainer.getScaleY());
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
    private void saveWaypoints() {
        /*
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        JSONManager.writeJSON(waypoints, WAYPOINTS_FILE);
                        return null;
                    }
                };
            }
        }.start();*/
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
    @FXML private void contextMenu(@NotNull final ContextMenuEvent event) {
        ContextMenu menu = new ContextMenu();
        menu.setAutoHide(true);

        MenuItem add = new MenuItem("Aggiungi Waypoint");
        add.setOnAction(e -> {
            Point2D center = anchorPaneWaypointContainer.screenToLocal(event.getScreenX(), event.getScreenY());
            JSONObject waypoint = new JSONObject();
            double x = center.getX();
            double y = center.getY();
            waypoint.put("x", x);
            waypoint.put("y", y);
            waypoints.getJSONArray("waypoints").put(waypoint);
            saveWaypoints();
            Circle circle = new Circle(x, y, 3, Color.RED);
            anchorPaneWaypointContainer.getChildren().add(circle);
        });

        MenuItem reset = new MenuItem("Reimposta Mappa");
        reset.setOnAction(e -> {
            anchorPaneWaypointContainer.setScaleX(1);
            anchorPaneWaypointContainer.setScaleY(1);
            anchorPaneWaypointContainer.setLayoutX(0);
            anchorPaneWaypointContainer.setLayoutY(0);
            scaledWidth = anchorPaneWaypointContainer.getWidth();
            scaledHeight = anchorPaneWaypointContainer.getHeight();
        });

        menu.getItems().addAll(add, reset);
        menu.show(Client.getStage(), event.getScreenX(), event.getScreenY());
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
