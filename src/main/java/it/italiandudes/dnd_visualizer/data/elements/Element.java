package it.italiandudes.dnd_visualizer.data.elements;

import it.italiandudes.dnd_visualizer.data.enums.ElementType;
import it.italiandudes.dnd_visualizer.data.item.*;
import it.italiandudes.dnd_visualizer.data.map.Map;
import it.italiandudes.dnd_visualizer.db.DBManager;
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
import java.util.Objects;

@SuppressWarnings({"unused", "DuplicatedCode"})
public final class Element extends StackPane {

    // Attributes
    private final int elementID;
    @NotNull private final Map map;
    private final long creationDate;
    @NotNull private String name;
    @NotNull private Point2D center;
    @NotNull private final ElementType type;
    @NotNull private final Item item;
    private boolean isVisibleToPlayers;

    // Constructors
    public Element(
            final int elementID, @NotNull final Map map, final long creationDate, @NotNull final String name,
            @NotNull final Point2D center, @NotNull final ElementType type, @NotNull final Item item,
            final boolean isVisibleToPlayers) {
        this.elementID = elementID;
        this.map = map;
        this.creationDate = creationDate;
        this.name = name;
        this.center = center;
        this.type = type;
        this.item = item;
        this.isVisibleToPlayers = isVisibleToPlayers;
        finishConfiguration();
    }
    public Element(final int id) throws SQLException, IOException {
        super();
        String query = "SELECT * FROM elements WHERE id=?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("The prepared statement is null");
        ps.setInt(1, id);
        ResultSet result = ps.executeQuery();
        if (result.next()) {
            this.elementID = id;
            this.creationDate = result.getLong("creation_date");
            try {
                this.map = new Map(result.getInt("map_id"));
            } catch (IOException e) {
                ps.close();
                throw e;
            }
            this.name = result.getString("name");
            this.center = new Point2D(result.getDouble("center_x"), result.getDouble("center_y"));
            this.type = ElementType.values()[result.getInt("type")];
            switch (type) {
                case ELEMENT_ITEM:
                    item = new Item(result.getInt("item_id"));
                    break;

                case ELEMENT_ARMOR:
                    item = new Armor(name);
                    break;

                case ELEMENT_WEAPON:
                    item = new Weapon(name);
                    break;

                case ELEMENT_ADDON:
                    item = new Addon(name);
                    break;

                case ELEMENT_SPELL:
                    item = new Spell(name);
                    break;

                default: // INVALID
                    throw new RuntimeException("Invalid ElementType!");
            }
            this.isVisibleToPlayers = result.getInt("player_visibility") != 0;
        } else {
            ps.close();
            throw new SQLException("ElementID non trovato");
        }
        ps.close();
        finishConfiguration();
    }
    public Element(final long creationDate) throws SQLException, IOException {
        super();
        String query = "SELECT * FROM elements WHERE creation_date=?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("The prepared statement is null");
        ps.setLong(1, creationDate);
        ResultSet result = ps.executeQuery();
        if (result.next()) {
            this.elementID = result.getInt("id");
            this.creationDate = creationDate;
            try {
                this.map = new Map(result.getInt("map_id"));
            } catch (IOException e) {
                ps.close();
                throw e;
            }
            this.name = result.getString("name");
            this.center = new Point2D(result.getDouble("center_x"), result.getDouble("center_y"));
            this.type = ElementType.values()[result.getInt("type")];
            switch (type) {
                case ELEMENT_ITEM:
                    item = new Item(result.getInt("item_id"));
                    break;

                case ELEMENT_ARMOR:
                    item = new Armor(name);
                    break;

                case ELEMENT_WEAPON:
                    item = new Weapon(name);
                    break;

                case ELEMENT_ADDON:
                    item = new Addon(name);
                    break;

                case ELEMENT_SPELL:
                    item = new Spell(name);
                    break;

                default: // INVALID
                    throw new RuntimeException("Invalid ElementType!");
            }
            this.isVisibleToPlayers = result.getInt("player_visibility") != 0;
        } else {
            ps.close();
            throw new SQLException("Elemento non trovato");
        }
        ps.close();
        finishConfiguration();
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
        updateElementLayoutCenter();
    }

    // Methods
    private void updateElementLayoutCenter() {
        setLayoutX(center.getX() - getPrefWidth()/2);
        setLayoutY(center.getY() - getPrefHeight()/2);
    }
    public int getElementID() {
        return elementID;
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
        String query = "UPDATE elements SET name=? WHERE id=?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("Database connection is null");
        ps.setString(1, name);
        ps.setInt(1, elementID);
        ps.executeUpdate();
        ps.close();
    }
    public @NotNull Point2D getCenter() {
        return center;
    }
    public void setCenter(@NotNull Point2D center) throws SQLException { // Live DB Operation, must be fast
        this.center = center;
        updateElementLayoutCenter();
        String query = "UPDATE elements SET center_x=?, center_y=? WHERE id=?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("Database connection is null");
        ps.setDouble(1, center.getX());
        ps.setDouble(2, center.getY());
        ps.setInt(3, elementID);
        ps.executeUpdate();
        ps.close();
    }
    public @NotNull ElementType getType() {
        return type;
    }
    public @NotNull Item getItem() {
        return item;
    }
    public boolean isVisibleToPlayers() {
        return isVisibleToPlayers;
    }
    public void setVisibleToPlayers(boolean visibleToPlayers) throws SQLException { // Live DB Operation, must be fast
        if (isVisibleToPlayers == visibleToPlayers) return;
        isVisibleToPlayers = visibleToPlayers;
        String query = "UPDATE elements SET player_visibility=? WHERE id=?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("Database connection is null");
        ps.setInt(1, visibleToPlayers?1:0);
        ps.setInt(2, elementID);
        ps.executeUpdate();
        ps.close();
    }
    public boolean elementEquals(Object o) {
        if (!(o instanceof Element)) return false;
        Element element = (Element) o;
        return getElementID() == element.getElementID() && getCreationDate() == element.getCreationDate() && isVisibleToPlayers() == element.isVisibleToPlayers() && Objects.equals(getMap(), element.getMap()) && Objects.equals(getName(), element.getName()) && Objects.equals(getCenter(), element.getCenter()) && getType() == element.getType() && Objects.equals(getItem(), element.getItem());
    }
    // NOTE: DO NOT OVERRIDE EQUALS AND HASHCODE
    @Override @NotNull
    public String toString() {
        return getName();
    }
}
