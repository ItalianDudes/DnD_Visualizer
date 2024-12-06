package it.italiandudes.dnd_visualizer.data.user;

import it.italiandudes.dnd_visualizer.db.DBManager;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("unused")
public final class RegisteredUser {

    // Attributes
    private final int playerID;
    private final long registerDate;
    @NotNull private String playerName;
    @NotNull private String sha512password;

    // Constructors
    public RegisteredUser(@NotNull final String name) throws SQLException {
        String query = "SELECT * FROM registered_users WHERE name=?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("Prepared Statement is null");
        ps.setString(1, name);
        ResultSet result = ps.executeQuery();
        if (result.next()) {
            this.playerID = result.getInt("id");
            this.registerDate = result.getLong("register_date");
            this.playerName = name;
            this.sha512password = result.getString("sha512password");
        } else {
            ps.close();
            throw new SQLException("User not found");
        }
        ps.close();
    }
    public RegisteredUser(final int playerID) throws SQLException {
        String query = "SELECT * FROM registered_users WHERE id=?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("Prepared Statement is null");
        ps.setInt(1, playerID);
        ResultSet result = ps.executeQuery();
        if (result.next()) {
            this.playerID = playerID;
            this.registerDate = result.getLong("register_date");
            this.playerName = result.getString("player_name");
            this.sha512password = result.getString("sha512password");
        } else {
            ps.close();
            throw new SQLException("UserID not found");
        }
        ps.close();
    }

    // Methods
    public int getPlayerID() {
        return playerID;
    }
    public long getRegisterDate() {
        return registerDate;
    }
    public @NotNull String getPlayerName() {
        return playerName;
    }
    public void setPlayerName(@NotNull String playerName) {
        this.playerName = playerName;
    }
    public @NotNull String getSha512password() {
        return sha512password;
    }
    public void setSha512password(@NotNull String sha512password) {
        this.sha512password = sha512password;
    }
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof RegisteredUser)) return false;

        RegisteredUser that = (RegisteredUser) o;
        return getPlayerID() == that.getPlayerID() && getRegisterDate() == that.getRegisterDate() && getPlayerName().equals(that.getPlayerName()) && getSha512password().equals(that.getSha512password());
    }
    @Override
    public int hashCode() {
        int result = getPlayerID();
        result = 31 * result + Long.hashCode(getRegisterDate());
        result = 31 * result + getPlayerName().hashCode();
        result = 31 * result + getSha512password().hashCode();
        return result;
    }
    @Override @NotNull
    public String toString() {
        return getPlayerName();
    }
}
