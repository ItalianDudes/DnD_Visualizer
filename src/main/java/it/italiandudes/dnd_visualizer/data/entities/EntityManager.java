package it.italiandudes.dnd_visualizer.data.entities;

import it.italiandudes.dnd_visualizer.data.enums.EntityType;
import it.italiandudes.dnd_visualizer.data.map.Map;
import it.italiandudes.dnd_visualizer.data.user.RegisteredUser;
import it.italiandudes.dnd_visualizer.db.DBManager;
import javafx.geometry.Point2D;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public final class EntityManager {

    // Map
    @NotNull private final ArrayList<@NotNull Entity> entityList;

    // Instance
    private static EntityManager instance = null;

    // Instance Getter
    @NotNull
    public static EntityManager getInstance() {
        if (instance == null) {
            instance = new EntityManager();
        }
        return instance;
    }

    // Constructor
    private EntityManager() {
        this.entityList = new ArrayList<>();
    }

    // Methods
    @NotNull
    public List<@NotNull Entity> getEntitiesFromMap(@NotNull final Map map) {
        return entityList.stream().filter(entity -> entity.getMap().equals(map)).collect(Collectors.toList());
    }
    @NotNull
    public Entity registerEntity(@NotNull final Map map, @NotNull final String name, @NotNull final String race, @Nullable final String entityClass, final int level, @NotNull final EntityType type, @NotNull final Point2D center, final int ca, final int hp, @Nullable final RegisteredUser owner, final boolean isVisibileToPlayers) throws SQLException, IOException {
        String query = "INSERT INTO entities (map_id, creation_date name, race, class, level, type, center_x, center_y, ca, hp, player_owner_id, player_visibility) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("Prepared Statement is null");
        ps.setInt(1, map.getMapID());
        long creationDate = System.currentTimeMillis();
        ps.setLong(2, creationDate);
        ps.setString(3, name);
        ps.setString(4, race);

        if (entityClass == null) ps.setNull(5, Types.VARCHAR);
        else ps.setString(5, entityClass);

        ps.setInt(6, level);
        ps.setInt(7, type.ordinal());
        ps.setDouble(8, center.getX());
        ps.setDouble(9, center.getY());
        ps.setInt(10, ca);
        ps.setInt(11, hp);
        if (owner == null) ps.setNull(12, Types.INTEGER);
        else ps.setInt(12, owner.getPlayerID());
        ps.setInt(13, isVisibileToPlayers?1:0);
        ps.executeUpdate();
        ps.close();
        Entity entity = new Entity(creationDate);
        entityList.add(entity);
        return entity;
    }
    public void unregisterEntity(@NotNull final Entity entity) throws SQLException {
        if (!entityList.contains(entity)) return;
        entityList.remove(entity);
        String query = "DELETE FROM entities WHERE id=?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("Prepared Statement is null");
        ps.setInt(1, entity.getEntityID());
        ps.executeUpdate();
        ps.close();
    }
}
