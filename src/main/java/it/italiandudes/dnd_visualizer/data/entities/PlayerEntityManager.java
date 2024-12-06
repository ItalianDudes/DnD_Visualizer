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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public final class PlayerEntityManager {

    // Map
    @NotNull private final ArrayList<@NotNull PlayerEntity> playerEntityList;

    // Instance
    private static PlayerEntityManager instance = null;

    // Instance Getter
    @NotNull
    public static PlayerEntityManager getInstance() {
        if (instance == null) {
            instance = new PlayerEntityManager();
        }
        return instance;
    }

    // Constructor
    private PlayerEntityManager() {
        this.playerEntityList = new ArrayList<>();
    }

    // Methods
    public @Nullable PlayerEntity getPlayerEntity(@NotNull final String name) {
        List<@NotNull PlayerEntity> playerEntities = playerEntityList.stream().filter(p -> p.getName().equals(name)).collect(Collectors.toList());
        if (playerEntities.isEmpty()) return null;
        else return playerEntities.get(0);
    }
    @Nullable
    public PlayerEntity registerEntity(@NotNull final Map map, @NotNull final String name, @NotNull final String race, @NotNull final String entityClass, final int level, @NotNull final Point2D center, final int ca, final int hp, @NotNull final RegisteredUser owner) throws SQLException, IOException {
        if (getPlayerEntity(name) != null) return null;
        String query = "INSERT INTO player_entities (map_id, creation_date name, race, class, level, center_x, center_y, ca, hp, player_owner_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("Prepared Statement is null");
        ps.setInt(1, map.getMapID());
        long creationDate = System.currentTimeMillis();
        ps.setLong(2, creationDate);
        ps.setString(3, name);
        ps.setString(4, race);
        ps.setString(5, entityClass);
        ps.setInt(6, level);
        ps.setDouble(7, center.getX());
        ps.setDouble(8, center.getY());
        ps.setInt(9, ca);
        ps.setInt(10, hp);
        ps.setInt(11, owner.getPlayerID());
        ps.executeUpdate();
        ps.close();
        PlayerEntity playerEntity = new PlayerEntity(creationDate);
        playerEntityList.add(playerEntity);
        return playerEntity;
    }
    public void unregisterEntity(@NotNull final PlayerEntity playerEntity) throws SQLException {
        if (!playerEntityList.contains(playerEntity)) return;
        playerEntityList.remove(playerEntity);
        String query = "DELETE FROM player_entities WHERE id=?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("Prepared Statement is null");
        ps.setInt(1, playerEntity.getPlayerEntityID());
        ps.executeUpdate();
        ps.close();
    }
}
