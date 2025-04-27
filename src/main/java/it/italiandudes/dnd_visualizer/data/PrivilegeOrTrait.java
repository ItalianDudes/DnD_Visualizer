package it.italiandudes.dnd_visualizer.data;

import it.italiandudes.dnd_visualizer.data.enums.PrivilegeType;
import it.italiandudes.dnd_visualizer.data.interfaces.ISavable;
import it.italiandudes.dnd_visualizer.db.DBManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("unused")
public final class PrivilegeOrTrait implements ISavable {

    // Attributes
    private Integer id = null;
    @NotNull private String name;
    @NotNull private PrivilegeType type;
    @Nullable private String content = null;

    // Constructors
    public PrivilegeOrTrait() {
        this.name = "";
        type = PrivilegeType.PRIVILEGE;
    }
    public PrivilegeOrTrait(@NotNull final PrivilegeOrTrait privilegeOrTrait) {
        this.id = privilegeOrTrait.id;
        this.name = privilegeOrTrait.name;
        this.content = privilegeOrTrait.content;
        this.type = privilegeOrTrait.type;
    }
    public PrivilegeOrTrait(@NotNull final String name) throws SQLException {
        String query = "SELECT * FROM privileges_and_traits WHERE name=?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("The database connection is not initialized");
        ps.setString(1, name);
        ResultSet result = ps.executeQuery();
        this.id = result.getInt("id");
        this.name = result.getString("name");
        this.type = PrivilegeType.values()[result.getInt("type")];
        this.content = result.getString("content");
        ps.close();
    }
    public PrivilegeOrTrait(@Nullable final Integer id, @NotNull final String name, @NotNull final PrivilegeType type, @Nullable final String content) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.content = content;
    }

    // Methods
    @SuppressWarnings("DuplicatedCode")
    public static boolean checkIfExist(@NotNull final String name) throws SQLException {
        String query = "SELECT id FROM privileges_and_traits WHERE name=?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("There's no connection with the database");
        ps.setString(1, name);
        ResultSet result = ps.executeQuery();
        if (result.next()) {
            result.getInt("id");
            ps.close();
            return true;
        } else {
            ps.close();
            return false;
        }
    }
    @Override
    public void saveIntoDatabase(@Nullable final String oldName) throws SQLException {
        String noteCheckerQuery = "SELECT id FROM privileges_and_traits WHERE name=?;";
        PreparedStatement ps = DBManager.preparedStatement(noteCheckerQuery);
        if (ps == null) throw new SQLException("The database connection doesn't exist");
        ps.setString(1, oldName);
        ResultSet result = ps.executeQuery();
        String query;
        int privilegeID;
        if (result.next()) { // Update
            privilegeID = result.getInt("id");
            ps.close();
            query = "UPDATE privileges_and_traits SET name=?, content=?, type=? WHERE id=?;";
            ps = DBManager.preparedStatement(query);
            if (ps == null) throw new SQLException("The database connection doesn't exist");
            ps.setString(1, name);
            ps.setString(2, content);
            ps.setInt(3, type.getDatabaseValue());
            ps.setInt(4, privilegeID);
            ps.executeUpdate();
            ps.close();
        } else { // Insert
            ps.close();
            query = "INSERT INTO privileges_and_traits (name, content, type) VALUES (?, ?, ?);";
            ps = DBManager.preparedStatement(query);
            if (ps == null) throw new SQLException("The database connection doesn't exist");
            ps.setString(1, name);
            ps.setString(2, content);
            ps.setInt(3, type.getDatabaseValue());
            ps.executeUpdate();
            ps.close();
            query = "SELECT id FROM privileges_and_traits WHERE name=?;";
            ps = DBManager.preparedStatement(query);
            if (ps == null) throw new SQLException("The database connection doesn't exist");
            ps.setString(1, name);
            result = ps.executeQuery();
            if (result.next()) {
                setId(result.getInt("id"));
                ps.close();
            } else {
                ps.close();
                throw new SQLException("Something strange happened on note insert! Note insert but doesn't result on select");
            }
        }
    }
    public Integer getId() {
        return id;
    }
    public void setId(@NotNull final Integer id) {
        if (this.id != null) return;
        this.id = id;
    }
    @NotNull
    public String getName() {
        return name;
    }
    public void setName(@NotNull final String name) {
        if (name.replace(" ", "").isEmpty()) throw new RuntimeException("The note title can't be empty!");
        this.name = name;
    }
    @NotNull
    public PrivilegeType getType() {
        return type;
    }
    public void setType(@NotNull final PrivilegeType type) {
        this.type = type;
    }
    @Nullable
    public String getContent() {
        return content;
    }
    public void setContent(@Nullable final String content) {
        this.content = content;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PrivilegeOrTrait that)) return false;
        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
        if (!getName().equals(that.getName())) return false;
        if (getType() != that.getType()) return false;
        return getContent() != null ? getContent().equals(that.getContent()) : that.getContent() == null;
    }
    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + getName().hashCode();
        result = 31 * result + getType().hashCode();
        result = 31 * result + (getContent() != null ? getContent().hashCode() : 0);
        return result;
    }
    @Override @NotNull
    public String toString() {
        return name;
    }
}
