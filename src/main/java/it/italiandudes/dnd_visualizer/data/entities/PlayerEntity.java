package it.italiandudes.dnd_visualizer.data.entities;

import it.italiandudes.dnd_visualizer.data.enums.EntityType;
import it.italiandudes.dnd_visualizer.data.map.Map;
import it.italiandudes.dnd_visualizer.data.user.RegisteredUser;
import it.italiandudes.dnd_visualizer.db.DBManager;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.SVGPath;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

@SuppressWarnings("unused")
public final class PlayerEntity extends StackPane {

    // Attributes
    private final int playerEntityID;
    @NotNull private Map map;
    private final long creationDate;
    @NotNull private String name;
    @NotNull private String race;
    @NotNull private String entityClass;
    private int level;
    @NotNull private Point2D center;
    private int ca;
    private int hp;
    @Nullable private RegisteredUser owner;

    // Constructors
    public PlayerEntity(final int playerEntityID) throws SQLException, IOException {
        String query = "SELECT * FROM player_entities WHERE id=?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("Prepared Statement is null");
        ps.setInt(1, playerEntityID);
        ResultSet result = ps.executeQuery();
        if (result.next()) {
            this.playerEntityID = playerEntityID;
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
            this.level = result.getInt("level");
            this.center = new Point2D(result.getDouble("center_x"), result.getDouble("center_y"));
            this.ca = result.getInt("ca");
            this.hp = result.getInt("hp");
            int playerID = result.getInt("player_owner_id");
            if (result.wasNull()) this.owner = null;
            else this.owner = new RegisteredUser(playerID);
        } else {
            ps.close();
            throw new SQLException("PlayerEntityID not found");
        }
        ps.close();
        finishConfiguration();
    }
    public PlayerEntity(final long creationDate) throws SQLException, IOException {
        String query = "SELECT * FROM player_entities WHERE creation_date=?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("Prepared Statement is null");
        ps.setLong(1, creationDate);
        ResultSet result = ps.executeQuery();
        if (result.next()) {
            this.playerEntityID = result.getInt("id");
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
            this.level = result.getInt("level");
            this.center = new Point2D(result.getDouble("center_x"), result.getDouble("center_y"));
            this.ca = result.getInt("ca");
            this.hp = result.getInt("hp");
            int playerID = result.getInt("player_owner_id");
            if (result.wasNull()) this.owner = null;
            else this.owner = new RegisteredUser(playerID);
        } else {
            ps.close();
            throw new SQLException("PlayerEntity not found");
        }
        ps.close();
        finishConfiguration();
    }

    // Finish Configuration
    private void finishConfiguration() {
        SVGPath icon = new SVGPath();
        icon.setContent(EntityType.ENTITY_PLAYER.getSVGContent());
        Pane pane = new Pane();
        pane.setShape(icon);
        pane.getStyleClass().add("waypoint-icon");
        getChildren().add(pane);
        setAlignment(Pos.CENTER);
        setBackground(new Background(new BackgroundFill(EntityType.ENTITY_PLAYER.getColor(), null, null)));
        getStyleClass().add("waypoint");
        setLayoutX(center.getX());
        setLayoutY(center.getY());
    }

    // Methods
    public int getPlayerEntityID() {
        return playerEntityID;
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
    public void setName(@NotNull String name) {
        this.name = name;
    }
    public @NotNull String getRace() {
        return race;
    }
    public void setRace(@NotNull String race) {
        this.race = race;
    }
    public @NotNull String getEntityClass() {
        return entityClass;
    }
    public void setEntityClass(@NotNull String entityClass) {
        this.entityClass = entityClass;
    }
    public int getLevel() {
        return level;
    }
    public void setLevel(int level) {
        this.level = level;
    }
    public @NotNull Point2D getCenter() {
        return center;
    }
    public void setCenter(@NotNull Point2D center) {
        this.center = center;
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
    public void setOwner(@Nullable RegisteredUser owner) {
        this.owner = owner;
    }
    public boolean playerEntityEquals(Object o) {
        if (!(o instanceof PlayerEntity)) return false;
        PlayerEntity that = (PlayerEntity) o;
        return getPlayerEntityID() == that.getPlayerEntityID() && getCreationDate() == that.getCreationDate() && getLevel() == that.getLevel() && ca == that.ca && hp == that.hp && Objects.equals(getMap(), that.getMap()) && Objects.equals(getName(), that.getName()) && Objects.equals(getRace(), that.getRace()) && Objects.equals(getEntityClass(), that.getEntityClass()) && Objects.equals(getCenter(), that.getCenter()) && Objects.equals(getOwner(), that.getOwner());
    }
    @Override @NotNull
    public String toString() {
        return getName();
    }
}
