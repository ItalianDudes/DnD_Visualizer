package it.italiandudes.dnd_visualizer.data.map;

import it.italiandudes.dnd_visualizer.db.DBManager;
import it.italiandudes.dnd_visualizer.utils.ImageUtils;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public final class MapManager {

    // List
    @NotNull private final HashSet<@NotNull Map> mapList;

    // Instance
    private static MapManager instance = null;

    // Instance Getter
    @NotNull
    public static MapManager getInstance() {
        if (instance == null) {
            instance = new MapManager();
        }
        return instance;
    }

    // Constructor
    private MapManager() {
        this.mapList = new HashSet<>();
    }

    // Methods
    public @NotNull HashSet<@NotNull Map> getMaps() {
        return mapList;
    }
    public @Nullable Map getMap(@NotNull final String name) {
        List<@NotNull Map> maps = mapList.stream().filter(m -> m.getName().equals(name)).collect(Collectors.toList());
        if (maps.isEmpty()) return null;
        else return maps.get(0);
    }
    @Nullable
    public Map registerMap(@NotNull final String name, @NotNull final Image mapImage, @NotNull final String mapExtension) throws SQLException, IOException {
        if (getMap(name) != null) return null;
        String query = "INSERT INTO entities (name, creation_date, base64map, map_extension) VALUES (?, ?, ?, ?);";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("Prepared Statement is null");
        ps.setString(1, name);
        long creationDate = System.currentTimeMillis();
        ps.setLong(2, creationDate);
        try {
            ps.setString(3, ImageUtils.fromFXImageToBase64(mapImage, mapExtension));
        } catch (IOException e) {
            ps.close();
            throw e;
        }
        ps.setString(4, mapExtension);
        ps.executeUpdate();
        ps.close();
        Map map = new Map(name);
        mapList.add(map);
        return map;
    }
    public void unregisterMap(@NotNull final Map map) throws SQLException {
        if (!mapList.contains(map)) return;
        mapList.remove(map);
        String query = "DELETE FROM maps WHERE id=?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("Prepared Statement is null");
        ps.setInt(1, map.getMapID());
        ps.executeUpdate();
        ps.close();
    }
}
