package it.italiandudes.dnd_visualizer.javafx.controllers.campaign.tab;

import it.italiandudes.dnd_visualizer.data.elements.Element;
import it.italiandudes.dnd_visualizer.data.elements.ElementManager;
import it.italiandudes.dnd_visualizer.data.enums.ElementType;
import it.italiandudes.dnd_visualizer.data.enums.WaypointType;
import it.italiandudes.dnd_visualizer.data.item.Item;
import it.italiandudes.dnd_visualizer.data.item.ItemContainer;
import it.italiandudes.dnd_visualizer.data.map.Map;
import it.italiandudes.dnd_visualizer.data.map.MapManager;
import it.italiandudes.dnd_visualizer.data.waypoints.Waypoint;
import it.italiandudes.dnd_visualizer.data.waypoints.WaypointManager;
import it.italiandudes.dnd_visualizer.javafx.Client;
import it.italiandudes.dnd_visualizer.javafx.alerts.ErrorAlert;
import it.italiandudes.dnd_visualizer.javafx.components.SceneController;
import it.italiandudes.dnd_visualizer.javafx.scene.inventory.*;
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
import javafx.stage.Stage;
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
    private double layoutToCornerX(final double layoutX) {
        double widthIncrease = scaledWidth - anchorPaneWaypointContainer.getWidth();
        return layoutX - (widthIncrease/2);
    }
    private double layoutToCornerY(final double layoutY) {
        double heightIncrease = scaledHeight - anchorPaneWaypointContainer.getHeight();
        return layoutY - (heightIncrease/2);
    }
    private double cornerToLayoutX(final double cornerX) {
        double widthIncrease = scaledWidth - anchorPaneWaypointContainer.getWidth();
        return cornerX + (widthIncrease/2);
    }
    private double cornerToLayoutY(final double cornerY) {
        double heightIncrease = scaledHeight - anchorPaneWaypointContainer.getHeight();
        return cornerY + (heightIncrease/2);
    }

    // EDT
    @FXML private void askFocus() {
        anchorPaneWaypointContainer.requestFocus();
    }

    // Map EDT
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
            removeMap.setOnAction(e -> removeMap(map));

            Menu renameMapMenu = new Menu("Rinomina Mappa");
            TextField renameNameField = new TextField();
            renameNameField.setPromptText("Nuovo Nome");
            MenuItem renameMapOption = new MenuItem();
            renameMapOption.setGraphic(renameNameField);
            renameMapOption.setOnAction(e -> renameMap(map, renameNameField));
            renameMapMenu.getItems().add(renameMapOption);

            MenuItem selectMap = new MenuItem("Seleziona Mappa");
            selectMap.setOnAction(e -> changeMap(map));

            contextMenu.getItems().addAll(removeMap, renameMapMenu, selectMap);
        }

        contextMenu.show(Client.getStage(), event.getScreenX(), event.getScreenY());
    }
    @FXML private void openMapContextMenu(@NotNull final ContextMenuEvent event) {
        if (map == null) return;

        Point2D center = anchorPaneWaypointContainer.screenToLocal(event.getScreenX(), event.getScreenY());

        ContextMenu contextMenu = new ContextMenu();
        contextMenu.setAutoHide(true);

        Menu addWaypointMenu = new Menu("Aggiungi Waypoint");
        TextField waypointNameField = new TextField();
        waypointNameField.setPromptText("Nome");
        MenuItem addWaypointOption = new MenuItem();
        addWaypointOption.setGraphic(waypointNameField);
        addWaypointOption.setOnAction(e -> addWaypoint(waypointNameField, center));
        addWaypointMenu.getItems().add(addWaypointOption);
        contextMenu.getItems().add(addWaypointMenu);

        Menu addElementMenu = new Menu("Aggiungi Elemento");
        MenuItem addItem = new MenuItem("Aggiungi Oggetto");
        addItem.setOnAction(e -> addElement(center, ElementType.ELEMENT_ITEM));
        Menu addEquipmentMenu = new Menu("Aggiungi Equipaggiamento");
        MenuItem addArmor = new MenuItem("Aggiungi Armatura");
        addArmor.setOnAction(e -> addElement(center, ElementType.ELEMENT_ARMOR));
        MenuItem addWeapon = new MenuItem("Aggiungi Arma");
        addWeapon.setOnAction(e -> addElement(center, ElementType.ELEMENT_WEAPON));
        MenuItem addAddon = new MenuItem("Aggiungi Addon");
        addAddon.setOnAction(e -> addElement(center, ElementType.ELEMENT_ADDON));
        addEquipmentMenu.getItems().addAll(addArmor, addWeapon, addAddon);
        MenuItem addSpell = new MenuItem("Aggiungi Incantesimo");
        addSpell.setOnAction(e -> addElement(center, ElementType.ELEMENT_SPELL));
        addElementMenu.getItems().addAll(addItem, addEquipmentMenu, addSpell);
        contextMenu.getItems().add(addElementMenu);

        if (event.getSource() instanceof Element) {
            Element element = (Element) event.getSource();

            MenuItem editElement = new MenuItem("Modifica Elemento");
            editElement.setOnAction(e -> editElement(element));

            Menu changeVisibilityMenu = new Menu("Modifica Visibilita'");
            MenuItem visibilityOn = new MenuItem("Rendi Visibile ai Giocatori");
            visibilityOn.setOnAction(e -> editElementPlayerVisibility(element, true));
            MenuItem visibilityOff = new MenuItem("Rendi Invisibile ai Giocatori");
            visibilityOff.setOnAction(e -> editElementPlayerVisibility(element, false));
            changeVisibilityMenu.getItems().addAll(visibilityOn, visibilityOff);

            MenuItem removeElement = new MenuItem("Rimuovi Elemento");
            removeElement.setOnAction(e -> removeElement(element));

            contextMenu.getItems().addAll(editElement, changeVisibilityMenu, removeElement);
        } else if (event.getSource() instanceof Waypoint) {
            Waypoint waypoint = (Waypoint) event.getSource();

            Menu renameWaypointMenu = new Menu("Rinomina Waypoint");
            TextField renameNameField = new TextField();
            renameNameField.setPromptText("Nuovo Nome");
            MenuItem renameWaypointOption = new MenuItem();
            renameWaypointOption.setGraphic(renameNameField);
            renameWaypointOption.setOnAction(e -> renameWaypoint(waypoint, renameNameField));
            renameWaypointMenu.getItems().add(renameWaypointOption);

            Menu changeVisibilityMenu = new Menu("Modifica Visibilita'");
            MenuItem visibilityOn = new MenuItem("Rendi Visibile ai Giocatori");
            visibilityOn.setOnAction(e -> editWaypointPlayerVisibility(waypoint, true));
            MenuItem visibilityOff = new MenuItem("Rendi Invisibile ai Giocatori");
            visibilityOff.setOnAction(e -> editWaypointPlayerVisibility(waypoint, false));
            changeVisibilityMenu.getItems().addAll(visibilityOn, visibilityOff);

            MenuItem removeWaypoint = new MenuItem("Rimuovi Waypoint");
            removeWaypoint.setOnAction(e -> removeWaypoint(waypoint));

            contextMenu.getItems().addAll(renameWaypointMenu, changeVisibilityMenu, removeWaypoint);
        }

        MenuItem reset = new MenuItem("Reimposta Mappa");
        reset.setOnAction(e -> resetMapParameters());
        contextMenu.getItems().add(reset);

        contextMenu.show(Client.getStage(), event.getScreenX(), event.getScreenY());
    }
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

    // Map Methods
    private void clearMarkersFromWaypointContainer() {
        anchorPaneWaypointContainer.getChildren().clear();
        anchorPaneWaypointContainer.getChildren().add(imageViewMap);
    }
    private void addMap(@NotNull final TextField nameField) {
        String name = nameField.getText();
        if (name == null || name.isEmpty() || name.replace("\t","").replace(" ", "").isEmpty()) return;
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
    private void removeMap(@NotNull final Map map) {
        try {
            changeMap(null);
            WaypointManager.getInstance().unregisterAllMapWaypoints(map);
            ElementManager.getInstance().unregisterAllMapElements(map);
            MapManager.getInstance().unregisterMap(map);
            listViewMaps.getItems().remove(map);
        } catch (SQLException ex) {
            Client.showMessageAndGoToMenu(ex);
        }
    }
    private void renameMap(@NotNull final Map map, @NotNull final TextField nameField) {
        String newName = nameField.getText();
        if (newName == null || newName.isEmpty() || newName.replace("\t", "").replace(" ", "").isEmpty()) return;
        try {
            map.setName(newName);
            listViewMaps.refresh();
        } catch (SQLException e) {
            Client.showMessageAndGoToMenu(e);
        }
    }
    private void resetMapParameters() {
        anchorPaneWaypointContainer.setScaleX(1);
        anchorPaneWaypointContainer.setScaleY(1);
        anchorPaneWaypointContainer.setLayoutX(0);
        anchorPaneWaypointContainer.setLayoutY(0);
        scaledWidth = anchorPaneWaypointContainer.getWidth();
        scaledHeight = anchorPaneWaypointContainer.getHeight();
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
    private void changeMap(@Nullable final Map map) {
        if (mapChanging) return;
        mapChanging = true;
        this.map = map;
        Platform.runLater(() -> {
            listViewMaps.refresh();
            clearMarkersFromWaypointContainer();

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
                waypoints.forEach(waypoint -> waypoint.setOnContextMenuRequested(this::openMapContextMenu));
                waypoints.forEach(this::configureWaypointHover);
                anchorPaneWaypointContainer.getChildren().addAll(waypoints);
                HashSet<Element> elements = ElementManager.getInstance().getMapElements(map);
                elements.forEach(element -> element.setOnScroll(this::mouseWheelZoom));
                elements.forEach(element -> element.setOnMouseDragged(ev -> moveElement(ev, element)));
                elements.forEach(element -> element.setOnContextMenuRequested(this::openMapContextMenu));
                elements.forEach(this::configureElementHover);
                anchorPaneWaypointContainer.getChildren().addAll(elements);
            }
            mapChanging = false;
        });
    }

    // Waypoint
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
    private void addWaypoint(@NotNull final TextField nameField, @NotNull final Point2D center) {
        String name = nameField.getText();
        if (name.isEmpty() || name.replace("\t","").replace(" ", "").isEmpty()) return;
        try {
            Waypoint waypoint = WaypointManager.getInstance().registerWaypoint(map, name, center, WaypointType.GENERIC_WAYPOINT, false);
            if (waypoint != null) {
                waypoint.setOnScroll(this::mouseWheelZoom);
                waypoint.setOnMouseDragged(ev -> moveWaypoint(ev, waypoint));
                waypoint.setOnContextMenuRequested(this::openMapContextMenu);
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
    private void renameWaypoint(@NotNull final Waypoint waypoint, @NotNull final TextField nameField) {
        String newName = nameField.getText();
        if (newName == null || newName.isEmpty() || newName.replace("\t", "").replace(" ", "").isEmpty()) return;
        try {
            waypoint.setName(newName);
        } catch (SQLException e) {
            Client.showMessageAndGoToMenu(e);
        }
    }
    private void editWaypointPlayerVisibility(@NotNull final Waypoint waypoint, final boolean visibility) {
        try {
            waypoint.setVisibleToPlayers(visibility);
        } catch (SQLException e) {
            Client.showMessageAndGoToMenu(e);
        }
    }
    private void removeWaypoint(@NotNull final Waypoint waypoint) {
        try {
            WaypointManager.getInstance().unregisterWaypoint(waypoint);
            anchorPaneWaypointContainer.getChildren().remove(waypoint);
        } catch (SQLException ex) {
            Client.showMessageAndGoToMenu(ex);
        }
    }
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

    // Element
    private void configureElementHover(@NotNull final Element element) {
        element.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                element.setScaleX(2);
                element.setScaleY(2);
                labelMarkerName.setText(element.getName());
                anchorPaneMarkerContainer.setVisible(true);
            } else {
                element.setScaleX(1);
                element.setScaleY(1);
                labelMarkerName.setText("");
                anchorPaneMarkerContainer.setVisible(false);
            }
        });
    }
    @Nullable
    private Item showElementCreator(@NotNull final ElementType type) {
        SceneController scene;
        ItemContainer container = new ItemContainer();
        switch (type) {
            case ELEMENT_ITEM:
                scene = SceneInventoryItem.getScene(container);
                break;

            case ELEMENT_ARMOR:
                scene = SceneInventoryArmor.getScene(container);
                break;

            case ELEMENT_WEAPON:
                scene = SceneInventoryWeapon.getScene(container);
                break;

            case ELEMENT_ADDON:
                scene = SceneInventoryAddon.getScene(container);
                break;

            case ELEMENT_SPELL:
                scene = SceneInventorySpell.getScene(container);
                break;

            default: // Invalid
                new ErrorAlert("ERRORE", "ERRORE NEL DATABASE", "L'elemento selezionato non possiede una categoria valida o non e' stata ancora implementata nell'applicazione.");
                return null;
        }

        Stage popupStage = Client.initPopupStage(scene);
        popupStage.showAndWait();
        return container.getItem();
    }
    private void addElement(@NotNull final Point2D center, @NotNull final ElementType type) {
        Item item = showElementCreator(type);
        if (item == null) return;
        try {
            Element element = ElementManager.getInstance().registerElement(map, center, type, item, false);
            if (element != null) {
                element.setOnScroll(this::mouseWheelZoom);
                element.setOnMouseDragged(ev -> moveElement(ev, element));
                element.setOnContextMenuRequested(this::openMapContextMenu);
                configureElementHover(element);
                anchorPaneWaypointContainer.getChildren().add(element);
            } else {
                new ErrorAlert("ERRORE", "Errore di Inserimento", "Un elemento con queste caratteristiche e' gia' presente.");
            }
        } catch (SQLException | IOException ex) {
            Client.showMessageAndGoToMenu(ex);
        }
    }
    private void editElement(@NotNull final Element element) {
        SceneController scene;
        ItemContainer container = new ItemContainer();
        String name = element.getItem().getName();
        switch (element.getType()) {
            case ELEMENT_ITEM:
                scene = SceneInventoryItem.getScene(container, name);
                break;

            case ELEMENT_ARMOR:
                scene = SceneInventoryArmor.getScene(container, name);
                break;

            case ELEMENT_WEAPON:
                scene = SceneInventoryWeapon.getScene(container, name);
                break;

            case ELEMENT_ADDON:
                scene = SceneInventoryAddon.getScene(container, name);
                break;

            case ELEMENT_SPELL:
                scene = SceneInventorySpell.getScene(container, name);
                break;

            default: // Invalid
                new ErrorAlert("ERRORE", "ERRORE NEL DATABASE", "L'elemento selezionato non possiede una categoria valida o non e' stata ancora implementata nell'applicazione.");
                return;
        }

        Stage popupStage = Client.initPopupStage(scene);
        popupStage.showAndWait();
        try {
            if (container.getItem() == null) return;
            element.setName(container.getItem().getName());
        } catch (SQLException e) {
            Client.showMessageAndGoToMenu(e);
        }
    }
    private void editElementPlayerVisibility(@NotNull final Element element, final boolean visibility) {
        try {
            element.setVisibleToPlayers(visibility);
        } catch (SQLException e) {
            Client.showMessageAndGoToMenu(e);
        }
    }
    private void removeElement(@NotNull final Element element) {
        try {
            ElementManager.getInstance().unregisterElement(element);
            anchorPaneWaypointContainer.getChildren().remove(element);
        } catch (SQLException ex) {
            Client.showMessageAndGoToMenu(ex);
        }
    }
    private void moveElement(@NotNull final MouseEvent event, @NotNull final Element element) {
        try {
            Point2D center = anchorPaneWaypointContainer.screenToLocal(event.getScreenX(), event.getScreenY());
            double newCenterX = center.getX();
            double newCenterY = center.getY();
            if (newCenterX < 0) newCenterX = 0;
            else if (newCenterX >= anchorPaneWaypointContainer.getPrefWidth()) newCenterX = anchorPaneWaypointContainer.getPrefWidth();
            if (newCenterY < 0) newCenterY = 0;
            else if (newCenterY >= anchorPaneWaypointContainer.getPrefHeight()) newCenterY = anchorPaneWaypointContainer.getPrefHeight();
            element.setCenter(new Point2D(newCenterX, newCenterY));
        } catch (SQLException e) {
            Client.showMessageAndGoToMenu(e);
        }
    }
}
