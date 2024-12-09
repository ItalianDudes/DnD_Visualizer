package it.italiandudes.dnd_visualizer.data.waypoints;

import it.italiandudes.dnd_visualizer.data.enums.WaypointType;
import it.italiandudes.dnd_visualizer.data.item.Item;
import it.italiandudes.dnd_visualizer.data.map.Map;
import it.italiandudes.dnd_visualizer.db.DBManager;
import javafx.geometry.Point2D;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
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
    }

    // Methods
    public @Nullable Waypoint getWaypoint(@NotNull final Map map, @NotNull final String name, @NotNull final WaypointType type) {
        List<@NotNull Waypoint> waypoints = waypointList.stream().filter(w -> w.getMap().equals(map) && w.getName().equals(name) && w.getType().equals(type)).collect(Collectors.toList());
        if (waypoints.isEmpty()) return null;
        else return waypoints.get(0);
    }

    // Methods
    @Nullable
    public Waypoint registerWaypoint(@NotNull final Map map, @NotNull final String name, @NotNull final Point2D center, @NotNull final WaypointType type, @Nullable final Item item, final boolean isVisibleToPlayers) throws SQLException, IOException {
        if (getWaypoint(map, name, type) != null) return null;
        String query = "INSERT INTO waypoints (map_id, name, creation_date, center_x, center_y, type, item_id, player_visibility) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("Prepared Statement is null");
        ps.setInt(1, map.getMapID());
        ps.setString(2, name);
        long creationDate = System.currentTimeMillis();
        ps.setLong(3, creationDate);
        ps.setDouble(4, center.getX());
        ps.setDouble(5, center.getY());
        ps.setInt(6, type.ordinal());
        if (item == null) ps.setNull(7, Types.INTEGER);
        else {
            assert item.getItemID() != null;
            ps.setInt(7, item.getItemID());
        }
        ps.setInt(8, isVisibleToPlayers?1:0);
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
}
