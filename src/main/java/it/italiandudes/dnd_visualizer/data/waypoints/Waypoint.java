package it.italiandudes.dnd_visualizer.data.waypoints;

import it.italiandudes.dnd_visualizer.data.enums.WaypointType;
import it.italiandudes.dnd_visualizer.data.map.Map;
import it.italiandudes.dnd_visualizer.db.DBManager;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings({"unused", "DuplicatedCode"})
public final class Waypoint extends StackPane {

    // Attributes
    private final int waypointID;
    @NotNull private final Map map;
    private final long creationDate;
    @NotNull private String name;
    @NotNull private Point2D center;
    @NotNull private final WaypointType type;
    private boolean isVisibleToPlayers;

    // Constructors
    public Waypoint(
            final int waypointID, @NotNull final Map map, final long creationDate, @NotNull final String name,
            @NotNull final Point2D center, @NotNull final WaypointType type, final boolean isVisibleToPlayers) {
        this.waypointID = waypointID;
        this.map = map;
        this.creationDate = creationDate;
        this.name = name;
        this.center = center;
        this.type = type;
        this.isVisibleToPlayers = isVisibleToPlayers;
        Platform.runLater(this::finishConfiguration);
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
            this.isVisibleToPlayers = result.getInt("player_visibility") != 0;
        } else {
            ps.close();
            throw new SQLException("WaypointID non trovato");
        }
        ps.close();
        Platform.runLater(this::finishConfiguration);
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
            this.isVisibleToPlayers = result.getInt("player_visibility") != 0;
        } else {
            ps.close();
            throw new SQLException("Waypoint non trovato");
        }
        ps.close();
        Platform.runLater(this::finishConfiguration);
    }

    // Finish Configuration
    private void finishConfiguration() {
        ImageView image = new ImageView(type.getImage());
        image.setFitWidth(32);
        image.setFitHeight(32);
        getChildren().add(image);
        setAlignment(Pos.CENTER);
        setBackground(new Background(new BackgroundFill(type.getColor(), new CornerRadii(5), null)));
        getStyleClass().add("waypoint");
        setMinWidth(42);
        setMinHeight(42);
        setPrefWidth(42);
        setPrefHeight(42);
        setMaxWidth(42);
        setMaxHeight(42);
        updateWaypointLayoutCenter();
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
    public void setName(@NotNull String name) throws SQLException { // Live DB Operation, must be fast
        this.name = name;
        String query = "UPDATE waypoints SET name=? WHERE id=?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("Database connection is null");
        ps.setString(1, name);
        ps.setInt(1, waypointID);
        ps.executeUpdate();
        ps.close();
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
    public boolean isVisibleToPlayers() {
        return isVisibleToPlayers;
    }
    public void setVisibleToPlayers(boolean visibleToPlayers) throws SQLException { // Live DB Operation, must be fast
        if (isVisibleToPlayers == visibleToPlayers) return;
        isVisibleToPlayers = visibleToPlayers;
        String query = "UPDATE waypoints SET player_visibility=? WHERE id=?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("Database connection is null");
        ps.setInt(1, visibleToPlayers?1:0);
        ps.setInt(2, waypointID);
        ps.executeUpdate();
        ps.close();
    }
    public boolean waypointEquals(Object o) {
        if (!(o instanceof Waypoint)) return false;
        Waypoint waypoint = (Waypoint) o;
        return getWaypointID() == waypoint.getWaypointID() && getCreationDate() == waypoint.getCreationDate() && isVisibleToPlayers() == waypoint.isVisibleToPlayers() && getMap().equals(waypoint.getMap()) && getName().equals(waypoint.getName()) && getCenter().equals(waypoint.getCenter()) && getType() == waypoint.getType();
    }
    // NOTE: DO NOT OVERRIDE EQUALS AND HASHCODE
    @Override @NotNull
    public String toString() {
        return getName();
    }
}
