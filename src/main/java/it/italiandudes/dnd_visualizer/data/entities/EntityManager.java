package it.italiandudes.dnd_visualizer.data.entities;

import it.italiandudes.dnd_visualizer.data.map.Map;
import it.italiandudes.dnd_visualizer.data.user.RegisteredUser;
import it.italiandudes.dnd_visualizer.db.DBManager;
import javafx.geometry.Point2D;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public final class EntityManager {

    // List
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
    public Entity registerEntity(@NotNull final Map map, @NotNull final Entity entity, @NotNull Point2D center, @Nullable final RegisteredUser owner, final boolean isVisibileToPlayers) throws SQLException, IOException {
        String query = "INSERT INTO entities (map_id, creation_date name, race, class, level, type, center_x, center_y, ca, hp, player_owner_id, player_visibility) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("Prepared Statement is null");
        ps.setInt(1, map.getMapID());
        long creationDate = System.currentTimeMillis();
        ps.setLong(2, creationDate);
        ps.setString(3, entity.getName());
        ps.setString(4, entity.getRace());

        if (entity.getEntityClass() == null) ps.setNull(5, Types.VARCHAR);
        else ps.setString(5, entity.getEntityClass());

        ps.setInt(6, entity.getLevel());
        ps.setInt(7, entity.getType().ordinal());
        ps.setDouble(8, center.getX());
        ps.setDouble(9, center.getY());
        ps.setInt(10, entity.getCA());
        ps.setInt(11, entity.getHP());
        if (owner == null) ps.setNull(12, Types.INTEGER);
        else ps.setInt(12, owner.getPlayerID());
        ps.setInt(13, isVisibileToPlayers?1:0);
        ps.executeUpdate();
        ps.close();
        Entity dbEntity = new Entity(creationDate);
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
