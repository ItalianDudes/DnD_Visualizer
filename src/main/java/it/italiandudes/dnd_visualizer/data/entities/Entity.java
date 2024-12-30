package it.italiandudes.dnd_visualizer.data.entities;

import it.italiandudes.dnd_visualizer.data.enums.EntityType;
import it.italiandudes.dnd_visualizer.data.map.Map;
import it.italiandudes.dnd_visualizer.data.user.RegisteredUser;
import it.italiandudes.dnd_visualizer.db.DBManager;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

@SuppressWarnings("unused")
public final class Entity extends StackPane {

    // Attributes
    private final int entityID;
    @NotNull private Map map;
    private final long creationDate;
    @NotNull private String name;
    @NotNull private String race;
    @Nullable private String entityClass;
    private int level;
    @NotNull private EntityType type;
    @NotNull private Point2D center;
    private int ca;
    private int hp;
    @Nullable private RegisteredUser owner;
    private boolean isVisibleToPlayers;

    // Constructors
    public Entity(final int entityID) throws SQLException, IOException {
        String query = "SELECT * FROM entities WHERE id=?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("Prepared Statement is null");
        ps.setInt(1, entityID);
        ResultSet result = ps.executeQuery();
        if (result.next()) {
            this.entityID = entityID;
            this.creationDate = result.getLong("creation_date");
            try {
                this.map = new Map(result.getInt("map_id"));
            } catch (IOException e) {
                ps.close();
                throw e;
            }
            this.name = result.getString("name");
            this.race = result.getString("race");
            this.entityClass = result.getString("class");
            if (result.wasNull()) this.entityClass = null;
            this.level = result.getInt("level");
            this.type = EntityType.values()[result.getInt("type")];
            this.center = new Point2D(result.getDouble("center_x"), result.getDouble("center_y"));
            this.ca = result.getInt("ca");
            this.hp = result.getInt("hp");
            int playerID = result.getInt("player_owner_id");
            if (result.wasNull()) this.owner = null;
            else this.owner = new RegisteredUser(playerID);
            this.isVisibleToPlayers = result.getInt("player_visibility") != 0;
        } else {
            ps.close();
            throw new SQLException("EntityID not found");
        }
        ps.close();
        finishConfiguration();
    }
    public Entity(final long creationDate) throws SQLException, IOException {
        String query = "SELECT * FROM entities WHERE creation_date=?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("Prepared Statement is null");
        ps.setLong(1, creationDate);
        ResultSet result = ps.executeQuery();
        if (result.next()) {
            this.entityID = result.getInt("id");
            try {
                this.map = new Map(result.getInt("map_id"));
            } catch (IOException e) {
                ps.close();
                throw e;
            }
            this.creationDate = creationDate;
            this.name = result.getString("name");
            this.race = result.getString("race");
            this.entityClass = result.getString("class");
            if (result.wasNull()) this.entityClass = null;
            this.level = result.getInt("level");
            this.type = EntityType.values()[result.getInt("type")];
            this.center = new Point2D(result.getDouble("center_x"), result.getDouble("center_y"));
            this.ca = result.getInt("ca");
            this.hp = result.getInt("hp");
            int playerID = result.getInt("player_owner_id");
            if (result.wasNull()) this.owner = null;
            else this.owner = new RegisteredUser(playerID);
            this.isVisibleToPlayers = result.getInt("player_visibility") != 0;
        } else {
            ps.close();
            throw new SQLException("Entity not found");
        }
        ps.close();
        finishConfiguration();
    }

    // Finish Configuration
    private void finishConfiguration() {
        getChildren().clear();
        ImageView image = new ImageView(type.getImage());
        image.setFitWidth(32);
        image.setFitHeight(32);
        getChildren().add(image);
        setAlignment(Pos.CENTER);
        if (type == EntityType.ENTITY_STRONG_ENEMY || type == EntityType.ENTITY_BOSS) {
            setBackground(new Background(new BackgroundFill(type.getColor(), new CornerRadii(5), null)));
        }
        getStyleClass().clear();
        getStyleClass().add("waypoint");
        setMinWidth(42);
        setMinHeight(42);
        setPrefWidth(42);
        setPrefHeight(42);
        setMaxWidth(42);
        setMaxHeight(42);
        updateEntityLayoutCenter();
    }

    // Methods
    private void updateEntityLayoutCenter() {
        setLayoutX(center.getX() - getPrefWidth()/2);
        setLayoutY(center.getY() - getPrefHeight()/2);
    }
    public int getEntityID() {
        return entityID;
    }
    public @NotNull Map getMap() {
        return map;
    }
    public void setMap(@NotNull Map map) {
        this.map = map;
    }
    public long getCreationDate() {
        return creationDate;
    }
    public @NotNull String getName() {
        return name;
    }
    public void setName(@NotNull final String name) throws SQLException { // Live DB Operation, must be fast
        this.name = name;
        String query = "UPDATE entities SET name=? WHERE id=?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("Database connection is null");
        ps.setString(1, name);
        ps.setInt(1, entityID);
        ps.executeUpdate();
        ps.close();
    }
    public @NotNull String getRace() {
        return race;
    }
    public void setRace(@NotNull String race) {
        this.race = race;
    }
    public @Nullable String getEntityClass() {
        return entityClass;
    }
    public void setEntityClass(@Nullable String entityClass) {
        this.entityClass = entityClass;
    }
    public int getLevel() {
        return level;
    }
    public void setLevel(int level) {
        this.level = level;
    }
    public @NotNull EntityType getType() {
        return type;
    }
    public void setType(@NotNull EntityType type) throws SQLException { // Live DB Operation, must be fast
        this.type = type;
        String query = "UPDATE entities SET type=? WHERE id=?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("Database connection is null");
        ps.setString(1, name);
        ps.setInt(1, type.ordinal());
        ps.executeUpdate();
        ps.close();
        finishConfiguration();
    }
    public @NotNull Point2D getCenter() {
        return center;
    }
    public void setCenter(@NotNull final Point2D center) throws SQLException { // Live DB Operation, must be fast
        this.center = center;
        updateEntityLayoutCenter();
        String query = "UPDATE entities SET center_x=?, center_y=? WHERE id=?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("Database connection is null");
        ps.setDouble(1, center.getX());
        ps.setDouble(2, center.getY());
        ps.setInt(3, entityID);
        ps.executeUpdate();
        ps.close();
    }
    public int getCA() {
        return ca;
    }
    public void setCA(int ca) {
        this.ca = ca;
    }
    public int getHP() {
        return hp;
    }
    public void setHP(int hp) {
        this.hp = hp;
    }
    public @Nullable RegisteredUser getOwner() {
        return owner;
    }
    public void setOwner(@Nullable final RegisteredUser owner) {
        this.owner = owner;
    }
    public boolean isVisibleToPlayers() {
        return isVisibleToPlayers;
    }
    public void setVisibleToPlayers(boolean visibleToPlayers) throws SQLException { // Live DB Operation, must be fast
        if (isVisibleToPlayers == visibleToPlayers) return;
        isVisibleToPlayers = visibleToPlayers;
        String query = "UPDATE entities SET player_visibility=? WHERE id=?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("Database connection is null");
        ps.setInt(1, visibleToPlayers?1:0);
        ps.setInt(2, entityID);
        ps.executeUpdate();
        ps.close();
    }
    public boolean entityEquals(Object o) {
        if (!(o instanceof Entity)) return false;
        Entity entity = (Entity) o;
        return getEntityID() == entity.getEntityID() && getCreationDate() == entity.getCreationDate() && getLevel() == entity.getLevel() && ca == entity.ca && hp == entity.hp && isVisibleToPlayers() == entity.isVisibleToPlayers() && Objects.equals(getMap(), entity.getMap()) && Objects.equals(getName(), entity.getName()) && Objects.equals(getRace(), entity.getRace()) && Objects.equals(getEntityClass(), entity.getEntityClass()) && getType() == entity.getType() && Objects.equals(getCenter(), entity.getCenter()) && Objects.equals(getOwner(), entity.getOwner());
    }
    @Override @NotNull
    public String toString() {
        return getName();
    }
}
