package it.italiandudes.dnd_visualizer.data.elements;

import it.italiandudes.dnd_visualizer.data.enums.ElementType;
import it.italiandudes.dnd_visualizer.data.item.Item;
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
public final class ElementManager {

    // List
    @NotNull private final HashSet<@NotNull Element> elementList;

    // Instance
    private static ElementManager instance = null;

    // Instance Getter
    @NotNull
    public static ElementManager getInstance() {
        if (instance == null) {
            instance = new ElementManager();
        }
        return instance;
    }

    // Constructor
    private ElementManager() {
        this.elementList = new HashSet<>();
        try (ResultSet result = DBManager.dbAllRowsSearch("elements")) {
            while (result.next()) {
                int elementID = result.getInt("id");
                Map map = new Map(result.getInt("map_id"));
                long creationDate = result.getLong("creation_date");
                String name = result.getString("name");
                Point2D center = new Point2D(result.getDouble("center_x"), result.getDouble("center_y"));
                ElementType type = ElementType.values()[result.getInt("type")];
                Item item = new Item(result.getInt("item_id"));
                boolean playerVisibility = result.getInt("player_visibility") != 0;
                elementList.add(new Element(elementID, map, creationDate, name, center, type, item, playerVisibility));
            }
        } catch (SQLException | IOException e) {
            Logger.log(e);
            Client.exit();
        }
    }

    // Methods
    public @Nullable Element getElement(@NotNull final Map map, @NotNull final String name) {
        List<@NotNull Element> elements = elementList.stream().filter(w -> w.getMap().equals(map) && w.getName().equals(name)).collect(Collectors.toList());
        if (elements.isEmpty()) return null;
        else return elements.get(0);
    }
    public @NotNull HashSet<@NotNull Element> getMapElements(@NotNull final Map map) {
        return elementList.stream().filter(element -> element.getMap().equals(map)).collect(Collectors.toCollection(HashSet::new));
    }
    @Nullable
    public Element registerElement(@NotNull final Map map, @NotNull final Point2D center, @NotNull final ElementType type, @NotNull final Item item, final boolean isVisibleToPlayers) throws SQLException, IOException {
        String name = item.getName();
        if (getElement(map, name) != null) return null;
        String query = "INSERT INTO elements (map_id, name, creation_date, center_x, center_y, type, item_id, player_visibility) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
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
        assert item.getItemID() != null;
        ps.setInt(7, item.getItemID());
        ps.setInt(8, isVisibleToPlayers ? 1 : 0);
        ps.executeUpdate();
        ps.close();
        Element element = new Element(creationDate);
        elementList.add(element);
        return element;
    }
    public void unregisterElement(@NotNull final Element element) throws SQLException {
        if (!elementList.contains(element)) return;
        elementList.remove(element);
        String query = "DELETE FROM elements WHERE id=?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("Prepared Statement is null");
        ps.setInt(1, element.getElementID());
        ps.executeUpdate();
        ps.close();
        query = "DELETE FROM items WHERE id=?;";
        ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("Prepared Statement is null");
        assert element.getItem().getItemID() != null;
        ps.setInt(1, element.getItem().getItemID());
        ps.executeUpdate();
        ps.close();
    }
    public void unregisterAllMapElements(@NotNull final Map map) throws SQLException {
        HashSet<Element> elements = getMapElements(map);
        for (Element element : elements) {
            unregisterElement(element);
        }
        String query = "DELETE FROM elements WHERE map_id=?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("Prepared Statement is null");
        ps.setInt(1, map.getMapID());
        ps.executeUpdate();
        ps.close();
    }
}
