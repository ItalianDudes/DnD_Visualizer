package it.italiandudes.dnd_visualizer.javafx.utils;

import it.italiandudes.dnd_visualizer.db.DBManager;
import it.italiandudes.dnd_visualizer.javafx.Client;
import it.italiandudes.dnd_visualizer.javafx.alerts.ErrorAlert;
import it.italiandudes.dnd_visualizer.javafx.alerts.InformationAlert;
import it.italiandudes.dnd_visualizer.utils.Defs;
import it.italiandudes.idl.common.Logger;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.stage.FileChooser;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

public final class SheetDataHandler {
    private static boolean isKeyParameterPresent(@NotNull final String KEY) throws SQLException {
        String query = "SELECT * FROM key_parameters WHERE param_key=?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("The database connection doesn't exist");
        ps.setString(1, KEY);
        ResultSet result = ps.executeQuery();
        if (result.next()) {
            ps.close();
            return true;
        } else {
            ps.close();
            return false;
        }
    }
    public static void exportElementCodeIntoFile(@NotNull final String elementCode) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Esportazione di un Elemento");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("DND5E Element", "*."+ Defs.Resources.ELEMENT_EXTENSION));
        fileChooser.setInitialDirectory(new File(Defs.JAR_POSITION).getParentFile());
        File elementPath;
        try {
            elementPath = fileChooser.showSaveDialog(Client.getStage().getScene().getWindow());
        } catch (IllegalArgumentException e) {
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            elementPath = fileChooser.showSaveDialog(Client.getStage().getScene().getWindow());
        }
        if (elementPath != null) {
            File finalElementPath = elementPath;
            new Service<Void>() {
                @Override
                protected Task<Void> createTask() {
                    return new Task<>() {
                        @Override
                        protected Void call() {
                            try (FileWriter writer = new FileWriter(finalElementPath)) {
                                writer.append(elementCode);
                            } catch (IOException e) {
                                Logger.log(e, Defs.LOGGER_CONTEXT);
                                Platform.runLater(() -> new ErrorAlert("ERRORE", "Errore di I/O", "Si e' verificato un errore durante la scrittura dell'elemento."));
                                return null;
                            }
                            Platform.runLater(() -> new InformationAlert("SUCCESSO", "Esportazione Elemento", "Elemento esportato con successo!"));
                            return null;
                        }
                    };
                }
            }.start();
        }
    }
    public static void writeKeyParameter(@NotNull final String imageKey, @NotNull final String imageExtensionKey, @NotNull final BufferedImage image, @NotNull final String imageExtension) {
        ByteArrayOutputStream imageByteStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, imageExtension, imageByteStream);
            writeKeyParameter(imageKey, Base64.getEncoder().encodeToString(imageByteStream.toByteArray()));
            writeKeyParameter(imageExtensionKey, imageExtension);
        } catch (IOException e) {
            Logger.log(e, Defs.LOGGER_CONTEXT);
            Platform.runLater(() -> new ErrorAlert("ERRORE", "ERRORE DI SCRITTURA", "L'immagine e' corrotta o non e' scrivibile."));
        }
    }
    public static void writeKeyParameter(@NotNull final String KEY, @NotNull final String VALUE) { // EDT Compatible
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<>() {
                    @Override
                    protected synchronized Void call() {
                        String query;
                        PreparedStatement ps = null;
                        try {
                            if (isKeyParameterPresent(KEY)) { // Update
                                query = "UPDATE key_parameters SET param_value=? WHERE param_key=?;";
                                ps = DBManager.preparedStatement(query);
                                if (ps == null) throw new SQLException("The database connection doesn't exist");
                                ps.setString(1, VALUE);
                                ps.setString(2, KEY);
                            } else { // Insert
                                query = "INSERT OR REPLACE INTO key_parameters (param_key, param_value) VALUES (?, ?);";
                                ps = DBManager.preparedStatement(query);
                                if (ps == null) throw new SQLException("The database connection doesn't exist");
                                ps.setString(1, KEY);
                                ps.setString(2, VALUE);
                            }
                            ps.executeUpdate();
                            ps.close();
                        } catch (SQLException e) {
                            try {
                                if (ps != null) ps.close();
                            } catch (SQLException ignored) {
                            }
                            Logger.log(e, Defs.LOGGER_CONTEXT);
                            Platform.runLater(() -> new ErrorAlert("ERRORE", "ERRORE DI SCRITTURA", "Si e' verificato un errore durante la scrittura di un parametro.\nKEY: " + KEY + "\nVALUE: " + VALUE));
                        }
                        return null;
                    }
                };
            }
        }.start();
    }
    public static String readKeyParameter(@NotNull final String KEY) { // NOT EDT Compatible
        PreparedStatement ps = null;
        try {
            String query = "SELECT param_value FROM key_parameters WHERE param_key=?;";
            ps = DBManager.preparedStatement(query);
            if (ps == null) throw new SQLException("The database connection doesn't exist");
            ps.setString(1, KEY);
            ResultSet result = ps.executeQuery();
            if (result.next()) {
                String value = result.getString("param_value");
                ps.close();
                return value;
            } else {
                ps.close();
                return null;
            }
        } catch (SQLException e) {
            try {
                if (ps != null) ps.close();
            } catch (SQLException ignored) {}
            Logger.log(e, Defs.LOGGER_CONTEXT);
            Platform.runLater(() -> new ErrorAlert("ERRORE", "ERRORE DI LETTURA", "Si e' verificato un errore durante la lettura di un parametro."));
            return null;
        }
    }

}
