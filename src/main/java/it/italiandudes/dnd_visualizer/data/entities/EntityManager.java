package it.italiandudes.dnd_visualizer.data.entities;

import it.italiandudes.dnd_visualizer.data.enums.EntityType;
import it.italiandudes.dnd_visualizer.data.map.Map;
import it.italiandudes.dnd_visualizer.db.DBManager;
import it.italiandudes.dnd_visualizer.javafx.Client;
import it.italiandudes.dnd_visualizer.utils.Defs;
import it.italiandudes.idl.common.Logger;
import javafx.geometry.Point2D;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        try (ResultSet result = DBManager.dbAllRowsSearch("entities")) {
            while (result.next()) {
                entityList.add(new Entity(result.getInt("id")));
            }
        } catch (SQLException | IOException e) {
            Logger.log(e, Defs.LOGGER_CONTEXT);
            Client.exit();
        }
    }

    // Methods
    @NotNull
    public List<@NotNull Entity> getEntitiesFromMap(@NotNull final Map map) {
        return entityList.stream().filter(entity -> entity.getMap().equals(map)).collect(Collectors.toList());
    }
    @NotNull
    public Entity registerEntity(@NotNull final Map map, @NotNull final String name, @NotNull final String race, final int level, @NotNull final EntityType type, @NotNull Point2D center, final int ca, final int hp, final int maxHP, final int tempHP) throws SQLException, IOException {
        String query = "INSERT INTO entities (map_id, creation_date, name, race, level, type, center_x, center_y, ca, hp, max_hp, temp_hp) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("Prepared Statement is null");
        ps.setInt(1, map.getMapID());
        long creationDate = System.currentTimeMillis();
        ps.setLong(2, creationDate);
        ps.setString(3, name);
        ps.setString(4, race);
        ps.setInt(5, level);
        ps.setInt(6, type.ordinal());
        ps.setDouble(7, center.getX());
        ps.setDouble(8, center.getY());
        ps.setInt(9, ca);
        ps.setInt(10, hp);
        ps.setInt(11, maxHP);
        ps.setInt(12, tempHP);
        ps.executeUpdate();
        ps.close();
        Entity dbEntity = new Entity(creationDate);
        entityList.add(dbEntity);
        return dbEntity;
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
