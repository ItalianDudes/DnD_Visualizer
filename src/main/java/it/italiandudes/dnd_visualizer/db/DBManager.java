package it.italiandudes.dnd_visualizer.db;

import it.italiandudes.dnd_visualizer.javafx.utils.SheetDataHandler;
import it.italiandudes.dnd_visualizer.utils.Defs;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

@SuppressWarnings({"unused", "SqlDialectInspection", "DuplicatedCode", "SqlNoDataSourceInspection"})
public final class DBManager {

    // Attributes
    private static Connection dbConnection = null;
    private static final String DB_PREFIX = "jdbc:sqlite:";

    // Generic SQLite Connection Initializer
    private static void setConnection(@NotNull final String DB_ABSOLUTE_PATH) throws SQLException {
        String url = DB_PREFIX+DB_ABSOLUTE_PATH+"?allowMultiQueries=true";
        dbConnection = DriverManager.getConnection(DB_PREFIX + DB_ABSOLUTE_PATH);
        dbConnection.setAutoCommit(true);
        Statement st = dbConnection.createStatement();
        st.execute("PRAGMA foreign_keys = ON;");
    }

    // Methods
    public static void connectToDB(@NotNull final File DB_PATH) throws IOException, SQLException {
        if (!DB_PATH.exists() || DB_PATH.isDirectory()) throw new IOException("This db doesn't exist");
        setConnection(DB_PATH.getAbsolutePath());
    }
    public static void closeConnection() {
        if (dbConnection != null) {
            try {
                dbConnection.close();
            }catch (Exception ignored){}
        }
    }
    public static PreparedStatement preparedStatement(@NotNull final String query) throws SQLException {
        if (dbConnection != null) {
            //noinspection SqlSourceToSinkFlow
            return dbConnection.prepareStatement(query);
        }
        return null;
    }
    public static void commit() throws SQLException {
        if (dbConnection != null) dbConnection.commit();
    }

    // DB Creator
    private static void executeCommonSQL() throws SQLException {
        Scanner reader = new Scanner(Defs.Resources.getAsStream(Defs.Resources.SQL.SQL_COMMON), "UTF-8");
        String query;
        String buffer;
        StringBuilder queryReader = new StringBuilder();

        while (reader.hasNext()) {
            buffer = reader.nextLine();
            queryReader.append(buffer);
            if (buffer.endsWith(";")) {
                query = queryReader.toString();
                PreparedStatement ps = dbConnection.prepareStatement(query);
                ps.execute();
                ps.close();
                queryReader = new StringBuilder();
            } else {
                queryReader.append('\n');
            }
        }
        reader.close();
    }
    public static void createCampaign(@NotNull final String DB_PATH) throws SQLException {
        setConnection(DB_PATH);
        executeCommonSQL();
        Scanner reader = new Scanner(Defs.Resources.getAsStream(Defs.Resources.SQL.SQL_CAMPAIGN), "UTF-8");
        StringBuilder queryReader = new StringBuilder();
        String query;
        String buffer;

        while (reader.hasNext()) {
            buffer = reader.nextLine();
            queryReader.append(buffer);
            if (buffer.endsWith(";")) {
                query = queryReader.toString();
                PreparedStatement ps = dbConnection.prepareStatement(query);
                ps.execute();
                ps.close();
                queryReader = new StringBuilder();
            } else {
                queryReader.append('\n');
            }
        }
        reader.close();
        SheetDataHandler.writeKeyParameter(Defs.KeyParameters.DB_VERSION, Defs.CAMPAIGN_DB_VERSION);
    }
    public static void createSheet(@NotNull final String DB_PATH) throws SQLException {
        setConnection(DB_PATH);
        executeCommonSQL();
        Scanner reader = new Scanner(Defs.Resources.getAsStream(Defs.Resources.SQL.SQL_SHEET), "UTF-8");
        StringBuilder queryReader = new StringBuilder();
        String query;
        String buffer;

        while (reader.hasNext()) {
            buffer = reader.nextLine();
            queryReader.append(buffer);
            if (buffer.endsWith(";")) {
                query = queryReader.toString();
                PreparedStatement ps = dbConnection.prepareStatement(query);
                ps.execute();
                ps.close();
                queryReader = new StringBuilder();
            } else {
                queryReader.append('\n');
            }
        }
        reader.close();
        SheetDataHandler.writeKeyParameter(Defs.KeyParameters.DB_VERSION, Defs.SHEET_DB_VERSION);
    }

}
