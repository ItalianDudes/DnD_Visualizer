package it.italiandudes.dnd_visualizer.data.user;

import it.italiandudes.dnd_visualizer.db.DBManager;
import org.apache.commons.codec.digest.DigestUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;

@SuppressWarnings("unused")
public final class UserManager {

    // Registered User List
    @NotNull final HashSet<@NotNull RegisteredUser> registeredUserList;

    // Instance
    private static UserManager instance = null;

    // Instance Getter
    @NotNull
    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    // Constructor
    private UserManager() {
        this.registeredUserList = new HashSet<>();
    }

    // Methods
    @Nullable
    public RegisteredUser getRegisteredUser(@NotNull final String name) {
        @NotNull List<RegisteredUser> users = registeredUserList.stream().filter(user -> user.getPlayerName().equals(name)).toList();
        if (users.isEmpty()) return null;
        else return users.getFirst();
    }
    @Nullable
    public RegisteredUser getRegisteredUser(final int playerID) {
        @NotNull List<RegisteredUser> users = registeredUserList.stream().filter(user -> user.getPlayerID() == playerID).toList();
        if (users.isEmpty()) return null;
        else return users.getFirst();
    }
    public boolean isRegistered(@NotNull final String name) {
        return registeredUserList.stream().anyMatch(registeredUser -> registeredUser.getPlayerName().equals(name));
    }
    @Nullable
    public RegisteredUser registerUser(@NotNull final String name, @NotNull String password, final boolean needToHash) throws SQLException {
        if (isRegistered(name)) return null;
        if (needToHash) password = DigestUtils.sha512Hex(password);
        String query = "INSERT INTO registered_users (register_date, player_name, sha512_password) VALUES (?, ?, ?);";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("Prepared Statement is null");
        long registerDate = System.currentTimeMillis();
        ps.setLong(1, registerDate);
        ps.setString(2, name);
        ps.setString(3, password);
        ps.executeUpdate();
        ps.close();
        RegisteredUser user = new RegisteredUser(name);
        registeredUserList.add(user);
        return user;
    }
    public void unregisterUser(@NotNull final RegisteredUser user) throws SQLException {
        if (!registeredUserList.contains(user)) return;
        String query = "DELETE FROM registered_users WHERE id=?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("Prepared Statement is null");
        ps.setInt(1, user.getPlayerID());
        ps.executeUpdate();
        ps.close();
    }
}
