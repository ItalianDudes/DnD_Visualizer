package it.italiandudes.dnd_visualizer.data.entities;

import it.italiandudes.dnd_visualizer.data.enums.EntityType;
import it.italiandudes.dnd_visualizer.data.map.Map;
import it.italiandudes.dnd_visualizer.data.user.RegisteredUser;
import it.italiandudes.dnd_visualizer.db.DBManager;
import javafx.application.Platform;
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
import java.sql.Types;
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
    @Nullable private String base64image;
    @Nullable private String imageExtension;
    private int ca;
    private int hp;
    private int maxHP;
    private int tempHP;
    @Nullable private String description;
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
            String base64image = result.getString("base64image");
            if (result.wasNull()) base64image = null;
            String imageExtension = result.getString("image_extension");
            if (result.wasNull()) imageExtension = null;
            if (base64image != null && imageExtension != null) {
                this.base64image = base64image;
                this.imageExtension = imageExtension;
            } else {
                this.base64image = null;
                this.imageExtension = null;
            }
            this.ca = result.getInt("ca");
            this.hp = result.getInt("hp");
            this.maxHP = result.getInt("max_hp");
            this.tempHP = result.getInt("temp_hp");
            this.description = result.getString("description");
            int playerID = result.getInt("player_owner_id");
            if (result.wasNull()) this.owner = null;
            else this.owner = new RegisteredUser(playerID);
            this.isVisibleToPlayers = result.getInt("player_visibility") != 0;
        } else {
            ps.close();
            throw new SQLException("EntityID not found");
        }
        ps.close();
        Platform.runLater(this::finishConfiguration);
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
            String base64image = result.getString("base64image");
            if (result.wasNull()) base64image = null;
            String imageExtension = result.getString("image_extension");
            if (result.wasNull()) imageExtension = null;
            if (base64image != null && imageExtension != null) {
                this.base64image = base64image;
                this.imageExtension = imageExtension;
            } else {
                this.base64image = null;
                this.imageExtension = null;
            }
            this.description = result.getString("description");
            this.ca = result.getInt("ca");
            this.hp = result.getInt("hp");
            this.maxHP = result.getInt("max_hp");
            this.tempHP = result.getInt("temp_hp");
            int playerID = result.getInt("player_owner_id");
            if (result.wasNull()) this.owner = null;
            else this.owner = new RegisteredUser(playerID);
            this.isVisibleToPlayers = result.getInt("player_visibility") != 0;
        } else {
            ps.close();
            throw new SQLException("Entity not found");
        }
        ps.close();
        Platform.runLater(this::finishConfiguration);
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
            getStyleClass().clear();
            getStyleClass().add("waypoint");
        }
        setMinWidth(42);
        setMinHeight(42);
        setPrefWidth(42);
        setPrefHeight(42);
        setMaxWidth(42);
        setMaxHeight(42);
        updateEntityLayoutCenter();
    }

    // Methods
    public void saveEntityData() throws SQLException { // Non-EDT Compatible
        String query = "UPDATE entities SET name=?, race=?, class=?, level=?, type=?, base64image=?, image_extension=?, ca=?, hp=?, max_hp=?, temp_hp=?, description=? WHERE id=?";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("The database connection doesn't exist");
        ps.setString(1, name);
        ps.setString(2, race);
        ps.setString(3, entityClass);
        ps.setInt(4, level);
        ps.setInt(5, type.ordinal());
        if (base64image != null && imageExtension != null) {
            ps.setString(6, base64image);
            ps.setString(7, imageExtension);
        } else {
            ps.setNull(6, Types.VARCHAR);
            ps.setNull(7, Types.VARCHAR);
        }
        ps.setInt(8, ca);
        ps.setInt(9, hp);
        ps.setInt(10, maxHP);
        ps.setInt(11, tempHP);
        ps.setString(12, description);
        ps.setInt(13, entityID);
        ps.executeUpdate();
        ps.close();
    }
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
        Platform.runLater(this::finishConfiguration);
    }
    public @Nullable String getDescription() {
        return description;
    }
    public void setDescription(@Nullable String description) {
        this.description = description;
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
    public @Nullable String getBase64image() {
        return base64image;
    }
    public void setBase64image(@Nullable String base64image) {
        this.base64image = base64image;
    }
    public @Nullable String getImageExtension() {
        return imageExtension;
    }
    public void setImageExtension(@Nullable String imageExtension) {
        this.imageExtension = imageExtension;
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
    public int getMaxHP() {
        return maxHP;
    }
    public void setMaxHP(int maxHP) {
        this.maxHP = maxHP;
    }
    public int getTempHP() {
        return tempHP;
    }
    public void setTempHP(int tempHP) {
        this.tempHP = tempHP;
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
        if (!(o instanceof Entity entity)) return false;
        return getEntityID() == entity.getEntityID() && getCreationDate() == entity.getCreationDate() && getLevel() == entity.getLevel() && ca == entity.ca && hp == entity.hp && getMaxHP() == entity.getMaxHP() && getTempHP() == entity.getTempHP() && isVisibleToPlayers() == entity.isVisibleToPlayers() && getMap().equals(entity.getMap()) && getName().equals(entity.getName()) && getRace().equals(entity.getRace()) && Objects.equals(getEntityClass(), entity.getEntityClass()) && getType() == entity.getType() && getCenter().equals(entity.getCenter()) && Objects.equals(getBase64image(), entity.getBase64image()) && Objects.equals(getImageExtension(), entity.getImageExtension()) && Objects.equals(getDescription(), entity.getDescription()) && Objects.equals(getOwner(), entity.getOwner());
    }
    @Override @NotNull
    public String toString() {
        return getName();
    }
}
