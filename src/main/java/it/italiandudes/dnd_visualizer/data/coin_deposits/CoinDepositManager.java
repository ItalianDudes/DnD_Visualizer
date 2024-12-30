package it.italiandudes.dnd_visualizer.data.coin_deposits;

import it.italiandudes.dnd_visualizer.data.map.Map;
import it.italiandudes.dnd_visualizer.db.DBManager;
import it.italiandudes.dnd_visualizer.javafx.Client;
import it.italiandudes.dnd_visualizer.utils.Defs;
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
public final class CoinDepositManager {

    // List
    @NotNull private final HashSet<@NotNull CoinDeposit> coinDepositList;

    // Instance
    private static CoinDepositManager instance = null;

    // Instance Getter
    @NotNull
    public static CoinDepositManager getInstance() {
        if (instance == null) {
            instance = new CoinDepositManager();
        }
        return instance;
    }

    // Constructor
    private CoinDepositManager() {
        this.coinDepositList = new HashSet<>();
        try (ResultSet result = DBManager.dbAllRowsSearch("coin_deposits")) {
            while (result.next()) {
                int coinDepositsID = result.getInt("id");
                Map map = new Map(result.getInt("map_id"));
                long creationDate = result.getLong("creation_date");
                Point2D center = new Point2D(result.getDouble("center_x"), result.getDouble("center_y"));
                boolean playerVisibility = result.getInt("player_visibility") != 0;
                int mr = result.getInt("mr");
                int ma = result.getInt("ma");
                int me = result.getInt("me");
                int mo = result.getInt("mo");
                int mp = result.getInt("mp");

                coinDepositList.add(new CoinDeposit(coinDepositsID, map, creationDate, center, playerVisibility, mr, ma, me, mo, mp));
            }
        } catch (SQLException | IOException e) {
            Logger.log(e, Defs.LOGGER_CONTEXT);
            Client.exit();
        }
    }

    // Methods
    public @Nullable CoinDeposit getCoinDeposit(@NotNull final Map map, @NotNull final Point2D center) {
        List<CoinDeposit> deposits = coinDepositList.stream().filter(deposit -> deposit.getMap().getMapID() == map.getMapID() && deposit.getCenter().equals(center)).collect(Collectors.toList());
        if (deposits.isEmpty()) return null;
        else return deposits.get(0);
    }
    public @NotNull HashSet<@NotNull CoinDeposit> getMapCoinDeposits(@NotNull final Map map) {
        return coinDepositList.stream().filter(coinDeposit -> coinDeposit.getMap().equals(map)).collect(Collectors.toCollection(HashSet::new));
    }
    @Nullable
    public CoinDeposit registerCoinDeposit(@NotNull final Map map, @NotNull final Point2D center, final boolean isVisibleToPlayers) throws SQLException, IOException {
        if (getCoinDeposit(map, center) != null) return null;
        String query = "INSERT INTO coin_deposits (map_id, creation_date, center_x, center_y, player_visibility) VALUES (?, ?, ?, ?, ?);";
        long creationDate;
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("Prepared Statement is null");
        ps.setInt(1, map.getMapID());
        creationDate = System.currentTimeMillis();
        ps.setLong(2, creationDate);
        ps.setDouble(3, center.getX());
        ps.setDouble(4, center.getY());
        ps.setInt(5, isVisibleToPlayers ? 1 : 0);
        ps.executeUpdate();
        ps.close();
        CoinDeposit coinDeposit = new CoinDeposit(creationDate);
        coinDepositList.add(coinDeposit);
        return coinDeposit;
    }
    public void unregisterCoinDeposit(@NotNull final CoinDeposit coinDeposit) throws SQLException {
        if (!coinDepositList.contains(coinDeposit)) return;
        coinDepositList.remove(coinDeposit);
        String query = "DELETE FROM coin_deposits WHERE id=?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("Prepared Statement is null");
        ps.setInt(1, coinDeposit.getCoinDepositID());
        ps.executeUpdate();
        ps.close();
    }
    public void unregisterAllMapCoinDeposits(@NotNull final Map map) throws SQLException {
        coinDepositList.removeAll(getMapCoinDeposits(map));
        String query = "DELETE FROM coin_deposits WHERE map_id=?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("Prepared Statement is null");
        ps.setInt(1, map.getMapID());
        ps.executeUpdate();
        ps.close();
    }
}
