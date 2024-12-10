package it.italiandudes.dnd_visualizer.data.waypoints;

import it.italiandudes.dnd_visualizer.data.enums.WaypointType;
import it.italiandudes.dnd_visualizer.data.item.Item;
import it.italiandudes.dnd_visualizer.data.map.Map;
import it.italiandudes.dnd_visualizer.db.DBManager;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.shape.SVGPath;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

@SuppressWarnings({"unused", "DuplicatedCode"})
public final class Waypoint extends StackPane {

    // Attributes
    private final int waypointID;
    @NotNull private final Map map;
    private final long creationDate;
    @NotNull private String name;
    @NotNull private Point2D center;
    @NotNull private final WaypointType type;
    @Nullable private Item item;
    private boolean isVisibleToPlayers;

    // Constructors
    public Waypoint(
            final int waypointID, @NotNull final Map map, final long creationDate, @NotNull final String name,
            @NotNull final Point2D center, @NotNull final WaypointType type, @Nullable final Item item,
            final boolean isVisibleToPlayers) {
        this.waypointID = waypointID;
        this.map = map;
        this.creationDate = creationDate;
        this.name = name;
        this.center = center;
        this.type = type;
        this.item = item;
        this.isVisibleToPlayers = isVisibleToPlayers;
        finishConfiguration();
    }
    public Waypoint(final int id) throws SQLException, IOException {
        super();
        String query = "SELECT * FROM waypoints WHERE id=?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("The prepared statement is null");
        ps.setInt(1, id);
        ResultSet result = ps.executeQuery();
        if (result.next()) {
            this.waypointID = id;
            this.creationDate = result.getLong("creation_date");
            try {
                this.map = new Map(result.getInt("map_id"));
            } catch (IOException e) {
                ps.close();
                throw e;
            }
            this.name = result.getString("name");
            this.center = new Point2D(result.getDouble("center_x"), result.getDouble("center_y"));
            this.type = WaypointType.values()[result.getInt("type")];
            int itemID = result.getInt("item_id");
            if (result.wasNull()) {
                this.item = null;
            } else {
                this.item = new Item(itemID);
            }
            this.isVisibleToPlayers = result.getInt("player_visibility") != 0;
        } else {
            ps.close();
            throw new SQLException("WaypointID non trovato");
        }
        ps.close();
        finishConfiguration();
    }
    public Waypoint(final long creationDate) throws SQLException, IOException {
        super();
        String query = "SELECT * FROM waypoints WHERE creation_date=?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("The prepared statement is null");
        ps.setLong(1, creationDate);
        ResultSet result = ps.executeQuery();
        if (result.next()) {
            this.waypointID = result.getInt("id");
            this.creationDate = creationDate;
            try {
                this.map = new Map(result.getInt("map_id"));
            } catch (IOException e) {
                ps.close();
                throw e;
            }
            this.name = result.getString("name");
            this.center = new Point2D(result.getDouble("center_x"), result.getDouble("center_y"));
            this.type = WaypointType.values()[result.getInt("type")];
            int itemID = result.getInt("item_id");
            if (result.wasNull()) {
                this.item = null;
            } else {
                this.item = new Item(itemID);
            }
            this.isVisibleToPlayers = result.getInt("player_visibility") != 0;
        } else {
            ps.close();
            throw new SQLException("Waypoint non trovato");
        }
        ps.close();
        finishConfiguration();
    }

    // Finish Configuration
    private void finishConfiguration() {
        SVGPath icon = new SVGPath();
        icon.setContent(type.getSVGContent());
        Pane pane = new Pane();
        pane.setShape(icon);
        pane.getStyleClass().add("waypoint-icon");
        getChildren().add(pane);
        setAlignment(Pos.CENTER);
        setBackground(new Background(new BackgroundFill(type.getColor(), new CornerRadii(5), null)));
        getStyleClass().add("waypoint");
        setMinWidth(32);
        setMinHeight(32);
        setPrefWidth(32);
        setPrefHeight(32);
        setMaxWidth(32);
        setMaxHeight(32);
        setLayoutX(center.getX() - getPrefWidth()/2);
        setLayoutY(center.getY() - getPrefHeight()/2);
    }

    // Methods
    private void updateWaypointLayoutCenter() {
        setLayoutX(center.getX() - getPrefWidth()/2);
        setLayoutY(center.getY() - getPrefHeight()/2);
    }
    public int getWaypointID() {
        return waypointID;
    }
    public @NotNull Map getMap() {
        return map;
    }
    public long getCreationDate() {
        return creationDate;
    }
    public @NotNull String getName() {
        return name;
    }
    public void setName(@NotNull String name) {
        this.name = name;
    }
    public @NotNull Point2D getCenter() {
        return center;
    }
    public void setCenter(@NotNull Point2D center) throws SQLException { // Live DB Operation, must be fast
        this.center = center;
        updateWaypointLayoutCenter();
        String query = "UPDATE waypoints SET center_x=?, center_y=? WHERE id=?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("Database connection is null");
        ps.setDouble(1, center.getX());
        ps.setDouble(2, center.getY());
        ps.setInt(3, waypointID);
        ps.executeUpdate();
        ps.close();
    }
    public @NotNull WaypointType getType() {
        return type;
    }
    public @Nullable Item getItem() {
        return item;
    }
    public void setItem(@Nullable Item item) {
        this.item = item;
    }
    public boolean isVisibleToPlayers() {
        return isVisibleToPlayers;
    }
    public void setVisibleToPlayers(boolean visibleToPlayers) {
        isVisibleToPlayers = visibleToPlayers;
    }
    public boolean waypointEquals(Object o) {
        if (!(o instanceof Waypoint)) return false;

        Waypoint waypoint = (Waypoint) o;
        return getWaypointID() == waypoint.getWaypointID() && getCreationDate() == waypoint.getCreationDate() && isVisibleToPlayers() == waypoint.isVisibleToPlayers() && getMap().equals(waypoint.getMap()) && getName().equals(waypoint.getName()) && getCenter().equals(waypoint.getCenter()) && getType() == waypoint.getType() && Objects.equals(getItem(), waypoint.getItem());
    }
    // NOTE: DO NOT OVERRIDE EQUALS AND HASHCODE
    @Override @NotNull
    public String toString() {
        return getName();
    }
}
