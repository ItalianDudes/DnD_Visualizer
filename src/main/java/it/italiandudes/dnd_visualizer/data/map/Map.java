package it.italiandudes.dnd_visualizer.data.map;

import it.italiandudes.dnd_visualizer.db.DBManager;
import it.italiandudes.dnd_visualizer.utils.ImageUtils;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class Map {

    // Attributes
    private final int mapID;
    @NotNull private String name;
    private final long creationDate;
    @NotNull private final Image map;
    @NotNull private final String mapExtension;

    // Constructors
    public Map(final int mapID, @NotNull final String name, final long creationDate,
               @NotNull final String base64map, @NotNull final String mapExtension) throws IOException {
        this.mapID = mapID;
        this.name = name;
        this.creationDate = creationDate;
        this.map = ImageUtils.fromBase64ToFXImage(base64map);
        this.mapExtension = mapExtension;
    }
    public Map(final int mapID) throws SQLException, IOException {
        String query = "SELECT * FROM maps WHERE id=?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("Prepared Statement is null");
        ps.setInt(1, mapID);
        ResultSet result = ps.executeQuery();
        if (result.next()) {
            this.mapID = mapID;
            this.name = result.getString("name");
            this.creationDate = result.getLong("creation_date");
            try {
                this.map = ImageUtils.fromBase64ToFXImage(result.getString("base64map"));
            } catch (IOException e) {
                ps.close();
                throw e;
            }
            this.mapExtension = result.getString("map_extension");
        } else {
            ps.close();
            throw new SQLException("MapID not found");
        }
        ps.close();
    }
    public Map(@NotNull final String name) throws SQLException, IOException {
        String query = "SELECT * FROM maps WHERE name=?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("Prepared Statement is null");
        ps.setString(1, name);
        ResultSet result = ps.executeQuery();
        if (result.next()) {
            this.name = name;
            this.mapID = result.getInt("id");
            this.creationDate = result.getLong("creation_date");
            try {
                this.map = ImageUtils.fromBase64ToFXImage(result.getString("base64map"));
            } catch (IOException e) {
                ps.close();
                throw e;
            }
            this.mapExtension = result.getString("map_extension");
        } else {
            ps.close();
            throw new SQLException("Map not found");
        }
        ps.close();
    }

    // Methods
    public int getMapID() {
        return mapID;
    }
    public @NotNull String getName() {
        return name;
    }
    public void setName(@NotNull String name) throws SQLException { // Live DB Operation, must be fast
        this.name = name;
        String query = "UPDATE maps SET name=? WHERE id=?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("Database connection is null");
        ps.setString(1, name);
        ps.setInt(2, mapID);
        ps.executeUpdate();
        ps.close();
    }
    public long getCreationDate() {
        return creationDate;
    }
    public @NotNull Image getMap() {
        return map;
    }
    public @NotNull String getMapExtension() {
        return mapExtension;
    }
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Map)) return false;

        Map map = (Map) o;
        return getMapID() == map.getMapID() && getCreationDate() == map.getCreationDate() && getName().equals(map.getName());
    }
    @Override
    public int hashCode() {
        int result = getMapID();
        result = 31 * result + getName().hashCode();
        result = 31 * result + Long.hashCode(getCreationDate());
        return result;
    }
    @Override @NotNull
    public String toString() {
        return getName();
    }
}