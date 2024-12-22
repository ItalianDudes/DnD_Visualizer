package it.italiandudes.dnd_visualizer.data.coin_deposits;

import it.italiandudes.dnd_visualizer.data.map.Map;
import it.italiandudes.dnd_visualizer.db.DBManager;
import it.italiandudes.dnd_visualizer.utils.Defs;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

@SuppressWarnings({"unused", "DuplicatedCode"})
public final class CoinDeposit extends StackPane {

    // Constants
    public static final Image COIN_DEPOSIT_IMAGE = new Image(Defs.Resources.getAsStream(Defs.Resources.Image.MapMarkers.MARKER_COIN_DEPOSIT));
    public static final Color COLOR = Color.YELLOW;

    // Attributes
    private final int coinDepositID;
    @NotNull private final Map map;
    private final long creationDate;
    @NotNull private Point2D center;
    private boolean isVisibleToPlayers;
    private int mr;
    private int ma;
    private int me;
    private int mo;
    private int mp;

    // Constructors
    public CoinDeposit(
            final int coinDepositID, @NotNull final Map map, final long creationDate,
            @NotNull final Point2D center, final boolean isVisibleToPlayers,
            final int mr, final int ma, final int me, final int mo, final int mp) {
        this.coinDepositID = coinDepositID;
        this.map = map;
        this.creationDate = creationDate;
        this.center = center;
        this.isVisibleToPlayers = isVisibleToPlayers;
        this.mr = mr;
        this.ma = ma;
        this.me = me;
        this.mo = mo;
        this.mp = mp;
        finishConfiguration();
    }
    public CoinDeposit(final int id) throws SQLException, IOException {
        super();
        String query = "SELECT * FROM coin_deposits WHERE id=?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("The prepared statement is null");
        ps.setInt(1, id);
        ResultSet result = ps.executeQuery();
        if (result.next()) {
            this.coinDepositID = id;
            this.creationDate = result.getLong("creation_date");
            try {
                this.map = new Map(result.getInt("map_id"));
            } catch (IOException e) {
                ps.close();
                throw e;
            }
            this.center = new Point2D(result.getDouble("center_x"), result.getDouble("center_y"));
            this.isVisibleToPlayers = result.getInt("player_visibility") != 0;
            this.mr = result.getInt("mr");
            this.ma = result.getInt("ma");
            this.me = result.getInt("me");
            this.mo = result.getInt("mo");
            this.mp = result.getInt("mp");
        } else {
            ps.close();
            throw new SQLException("CoinDepositID non trovato");
        }
        ps.close();
        finishConfiguration();
    }
    public CoinDeposit(final long creationDate) throws SQLException, IOException {
        super();
        String query = "SELECT * FROM coin_deposits WHERE creation_date=?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("The prepared statement is null");
        ps.setLong(1, creationDate);
        ResultSet result = ps.executeQuery();
        if (result.next()) {
            this.coinDepositID = result.getInt("id");
            this.creationDate = creationDate;
            try {
                this.map = new Map(result.getInt("map_id"));
            } catch (IOException e) {
                ps.close();
                throw e;
            }
            this.center = new Point2D(result.getDouble("center_x"), result.getDouble("center_y"));
            this.isVisibleToPlayers = result.getInt("player_visibility") != 0;
            this.mr = result.getInt("mr");
            this.ma = result.getInt("ma");
            this.me = result.getInt("me");
            this.mo = result.getInt("mo");
            this.mp = result.getInt("mp");
        } else {
            ps.close();
            throw new SQLException("CoinDeposit non trovato");
        }
        ps.close();
        finishConfiguration();
    }

    // Finish Configuration
    private void finishConfiguration() {
        ImageView image = new ImageView(COIN_DEPOSIT_IMAGE);
        image.setFitWidth(32);
        image.setFitHeight(32);
        getChildren().add(image);
        setAlignment(Pos.CENTER);
        setBackground(new Background(new BackgroundFill(COLOR, new CornerRadii(5), null)));
        getStyleClass().add("waypoint");
        setMinWidth(42);
        setMinHeight(42);
        setPrefWidth(42);
        setPrefHeight(42);
        setMaxWidth(42);
        setMaxHeight(42);
        updateCoinDepositLayoutCenter();
    }

    // Methods
    private void updateCoinDepositLayoutCenter() {
        setLayoutX(center.getX() - getPrefWidth()/2);
        setLayoutY(center.getY() - getPrefHeight()/2);
    }
    public int getCoinDepositID() {
        return coinDepositID;
    }
    public @NotNull Map getMap() {
        return map;
    }
    public long getCreationDate() {
        return creationDate;
    }
    public @NotNull Point2D getCenter() {
        return center;
    }
    public void setCenter(@NotNull Point2D center) throws SQLException { // Live DB Operation, must be fast
        this.center = center;
        updateCoinDepositLayoutCenter();
        String query = "UPDATE coin_deposits SET center_x=?, center_y=? WHERE id=?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("Database connection is null");
        ps.setDouble(1, center.getX());
        ps.setDouble(2, center.getY());
        ps.setInt(3, coinDepositID);
        ps.executeUpdate();
        ps.close();
    }
    public boolean isVisibleToPlayers() {
        return isVisibleToPlayers;
    }
    public void setVisibleToPlayers(boolean visibleToPlayers) throws SQLException { // Live DB Operation, must be fast
        if (isVisibleToPlayers == visibleToPlayers) return;
        isVisibleToPlayers = visibleToPlayers;
        String query = "UPDATE coin_deposits SET player_visibility=? WHERE id=?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("Database connection is null");
        ps.setInt(1, visibleToPlayers?1:0);
        ps.setInt(2, coinDepositID);
        ps.executeUpdate();
        ps.close();
    }
    public int getMr() {
        return mr;
    }
    public void setMr(int mr) throws SQLException { // Live DB Operation, must be fast
        if (this.mr == mr) return;
        this.mr = mr;
        String query = "UPDATE coin_deposits SET mr=? WHERE id=?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("Database connection is null");
        ps.setInt(1, mr);
        ps.setInt(2, coinDepositID);
        ps.executeUpdate();
        ps.close();
    }
    public int getMa() {
        return ma;
    }
    public void setMa(int ma) throws SQLException { // Live DB Operation, must be fast
        if (this.ma == ma) return;
        this.ma = ma;
        String query = "UPDATE coin_deposits SET ma=? WHERE id=?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("Database connection is null");
        ps.setInt(1, ma);
        ps.setInt(2, coinDepositID);
        ps.executeUpdate();
        ps.close();
    }
    public int getMe() {
        return me;
    }
    public void setMe(int me) throws SQLException { // Live DB Operation, must be fast
        if (this.me == me) return;
        this.me = me;
        String query = "UPDATE coin_deposits SET me=? WHERE id=?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("Database connection is null");
        ps.setInt(1, me);
        ps.setInt(2, coinDepositID);
        ps.executeUpdate();
        ps.close();
    }
    public int getMo() {
        return mo;
    }
    public void setMo(int mo) throws SQLException { // Live DB Operation, must be fast
        if (this.mo == mo) return;
        this.mo = mo;
        String query = "UPDATE coin_deposits SET mo=? WHERE id=?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("Database connection is null");
        ps.setInt(1, mo);
        ps.setInt(2, coinDepositID);
        ps.executeUpdate();
        ps.close();
    }
    public int getMp() {
        return mp;
    }
    public void setMp(int mp) throws SQLException { // Live DB Operation, must be fast
        if (this.mp == mp) return;
        this.mp = mp;
        String query = "UPDATE coin_deposits SET mp=? WHERE id=?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("Database connection is null");
        ps.setInt(1, mp);
        ps.setInt(2, coinDepositID);
        ps.executeUpdate();
        ps.close();
    }
    public boolean coinDepositEquals(Object o) {
        if (!(o instanceof CoinDeposit)) return false;
        CoinDeposit that = (CoinDeposit) o;
        return getCoinDepositID() == that.getCoinDepositID() && getCreationDate() == that.getCreationDate() && isVisibleToPlayers() == that.isVisibleToPlayers() && getMr() == that.getMr() && getMa() == that.getMa() && getMe() == that.getMe() && getMo() == that.getMo() && getMp() == that.getMp() && Objects.equals(getMap(), that.getMap()) && Objects.equals(getCenter(), that.getCenter());
    }
    // NOTE: DO NOT OVERRIDE EQUALS AND HASHCODE
    @Override @NotNull
    public String toString() {
        return "MR: " + mr + '\n' + "MA: " + ma + '\n' + "ME: " + me + '\n' + "MO: " + mo + '\n' + "MP: " + mp;
    }
}
