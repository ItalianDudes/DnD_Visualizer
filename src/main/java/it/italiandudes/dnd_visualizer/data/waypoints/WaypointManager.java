package it.italiandudes.dnd_visualizer.data.waypoints;

import it.italiandudes.dnd_visualizer.data.enums.WaypointType;
import it.italiandudes.dnd_visualizer.data.map.Map;
import it.italiandudes.dnd_visualizer.db.DBManager;
import it.italiandudes.dnd_visualizer.javafx.Client;
import it.italiandudes.idl.common.Logger;
import javafx.geometry.Point2D;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public final class WaypointManager {

    // List
    @NotNull private final HashSet<@NotNull Waypoint> waypointList;

    // Instance
    private static WaypointManager instance = null;

    // Instance Getter
    @NotNull
    public static WaypointManager getInstance() {
        if (instance == null) {
            instance = new WaypointManager();
        }
        return instance;
    }

    // Constructor
    private WaypointManager() {
        this.waypointList = new HashSet<>();
        try (ResultSet result = DBManager.dbAllRowsSearch("waypoints")) {
            while (result.next()) {
                int waypointID = result.getInt("id");
                Map map = new Map(result.getInt("map_id"));
                long creationDate = result.getLong("creation_date");
                String name = result.getString("name");
                Point2D center = new Point2D(result.getDouble("center_x"), result.getDouble("center_y"));
                WaypointType type = WaypointType.values()[result.getInt("type")];
                boolean playerVisibility = result.getInt("player_visibility") != 0;
                waypointList.add(new Waypoint(waypointID, map, creationDate, name, center, type, playerVisibility));
            }
        } catch (SQLException | IOException e) {
            Logger.log(e);
            Client.exit();
        }
    }

    // Methods
    public @Nullable Waypoint getWaypoint(@NotNull final Map map, @NotNull final String name, @NotNull final WaypointType type) {
        List<@NotNull Waypoint> waypoints = waypointList.stream().filter(w -> w.getMap().equals(map) && w.getName().equals(name) && w.getType().equals(type)).collect(Collectors.toList());
        if (waypoints.isEmpty()) return null;
        else return waypoints.get(0);
    }
    public @NotNull HashSet<@NotNull Waypoint> getMapWaypoints(@NotNull final Map map) {
        return waypointList.stream().filter(waypoint -> waypoint.getMap().equals(map)).collect(Collectors.toCollection(HashSet::new));
    }
    @Nullable
    public Waypoint registerWaypoint(@NotNull final Map map, @NotNull final String name, @NotNull final Point2D center, @NotNull final WaypointType type, final boolean isVisibleToPlayers) throws SQLException, IOException {
        if (getWaypoint(map, name, type) != null) return null;
        String query = "INSERT INTO waypoints (map_id, name, creation_date, center_x, center_y, type, player_visibility) VALUES (?, ?, ?, ?, ?, ?, ?);";
        long creationDate;
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("Prepared Statement is null");
        ps.setInt(1, map.getMapID());
        ps.setString(2, name);
        creationDate = System.currentTimeMillis();
        ps.setLong(3, creationDate);
        ps.setDouble(4, center.getX());
        ps.setDouble(5, center.getY());
        ps.setInt(6, type.ordinal());
        ps.setInt(7, isVisibleToPlayers ? 1 : 0);
        ps.executeUpdate();
        ps.close();
        Waypoint waypoint = new Waypoint(creationDate);
        waypointList.add(waypoint);
        return waypoint;
    }
    public void unregisterWaypoint(@NotNull final Waypoint waypoint) throws SQLException {
        if (!waypointList.contains(waypoint)) return;
        waypointList.remove(waypoint);
        String query = "DELETE FROM waypoints WHERE id=?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("Prepared Statement is null");
        ps.setInt(1, waypoint.getWaypointID());
        ps.executeUpdate();
        ps.close();
    }
    public void unregisterAllMapWaypoints(@NotNull final Map map) throws SQLException {
        waypointList.removeAll(getMapWaypoints(map));
        String query = "DELETE FROM waypoints WHERE map_id=?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("Prepared Statement is null");
        ps.setInt(1, map.getMapID());
        ps.executeUpdate();
        ps.close();
    }
}
