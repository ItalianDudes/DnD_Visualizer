package it.italiandudes.dnd_visualizer.data;

import it.italiandudes.dnd_visualizer.db.DBManager;
import it.italiandudes.dnd_visualizer.data.interfaces.ISavable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Objects;

@SuppressWarnings("unused")
public final class Note implements ISavable {

    // Attributes
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private Integer id = null;
    @NotNull private String title;
    @Nullable private String content = null;
    @Nullable private Date creationDate = null;
    @Nullable private Date lastEdit = null;

    // Constructors
    public Note() {
        this.title = "";
    }
    public Note(@NotNull final Note note) {
        this.title = note.title;
        this.id = note.id;
        this.content = note.content;
        this.creationDate = note.creationDate;
        this.lastEdit = note.lastEdit;
    }
    public Note(@NotNull final String title) throws SQLException {
        String query = "SELECT * FROM notes WHERE title=?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("The database connection is not initialized");
        ps.setString(1, title);
        ResultSet result = ps.executeQuery();
        this.title = title;
        this.id = result.getInt("id");
        this.creationDate = result.getDate("creation_date");
        this.lastEdit = result.getDate("last_edit");
        this.content = result.getString("content");
        ps.close();
    }
    public Note(@Nullable final Integer id, @NotNull final String title, @Nullable final String content, @Nullable final Date creationDate, @Nullable final Date lastEdit) {
        this.title = title;
        this.id = id;
        this.content = content;
        this.creationDate = creationDate;
        this.lastEdit = lastEdit;
    }

    // Methods
    @SuppressWarnings("DuplicatedCode")
    public static boolean checkIfExist(@NotNull final String title) throws SQLException {
        String query = "SELECT id FROM notes WHERE title=?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("There's no connection with the database");
        ps.setString(1, title);
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
        String noteCheckerQuery = "SELECT id FROM notes WHERE title=?;";
        PreparedStatement ps = DBManager.preparedStatement(noteCheckerQuery);
        if (ps == null) throw new SQLException("The database connection doesn't exist");
        ps.setString(1, oldName);
        ResultSet result = ps.executeQuery();
        String query;
        int noteID;
        if (result.next()) { // Update
            noteID = result.getInt("id");
            ps.close();
            query = "UPDATE notes SET title=?, content=?, creation_date=?, last_edit=? WHERE id=?;";
            ps = DBManager.preparedStatement(query);
            if (ps == null) throw new SQLException("The database connection doesn't exist");
            ps.setString(1, getTitle());
            ps.setString(2, getContent());
            ps.setDate(3, getCreationDate());
            ps.setDate(4, getLastEdit());
            ps.setInt(5, noteID);
            ps.executeUpdate();
            ps.close();
        } else { // Insert
            ps.close();
            query = "INSERT INTO notes (title, content) VALUES (?, ?);";
            ps = DBManager.preparedStatement(query);
            if (ps == null) throw new SQLException("The database connection doesn't exist");
            ps.setString(1, getTitle());
            ps.setString(2, getContent());
            ps.executeUpdate();
            ps.close();
            query = "SELECT id FROM notes WHERE title=?;";
            ps = DBManager.preparedStatement(query);
            if (ps == null) throw new SQLException("The database connection doesn't exist");
            ps.setString(1, getTitle());
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
    public String getTitle() {
        return title;
    }
    public void setTitle(@NotNull final String title) {
        if (title.replace(" ", "").isEmpty()) throw new RuntimeException("The note title can't be empty!");
        this.title = title;
    }
    @Nullable
    public String getContent() {
        return content;
    }
    public void setContent(@Nullable final String content) {
        this.content = content;
    }
    @Nullable
    public Date getCreationDate() {
        return creationDate;
    }
    public String getFormattedCreationDate() {
        return DATE_FORMAT.format(creationDate);
    }
    public void setCreationDate(@NotNull final Date creationDate) {
        this.creationDate = creationDate;
    }
    @Nullable
    public Date getLastEdit() {
        return lastEdit;
    }
    public String getFormattedLastEdit() {
        return DATE_FORMAT.format(lastEdit);
    }
    public void setLastEdit(@NotNull final Date lastEdit) {
        this.lastEdit = lastEdit;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Note)) return false;
        Note note = (Note) o;
        return Objects.equals(getId(), note.getId()) && Objects.equals(getTitle(), note.getTitle()) && Objects.equals(getContent(), note.getContent()) && Objects.equals(getCreationDate(), note.getCreationDate()) && Objects.equals(getLastEdit(), note.getLastEdit());
    }
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getContent(), getCreationDate(), getLastEdit());
    }
    @Override
    public String toString() {
        return title;
    }
}
