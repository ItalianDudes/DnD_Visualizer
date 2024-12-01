package it.italiandudes.dnd_visualizer.client.javafx.controller.sheetviewer;

import it.italiandudes.dnd_visualizer.client.javafx.Client;
import it.italiandudes.dnd_visualizer.client.javafx.alert.ErrorAlert;
import it.italiandudes.dnd_visualizer.client.javafx.controller.ControllerSceneSheetViewer;
import it.italiandudes.dnd_visualizer.client.javafx.scene.SceneMainMenu;
import it.italiandudes.dnd_visualizer.client.javafx.scene.SceneNote;
import it.italiandudes.dnd_visualizer.data.Note;
import it.italiandudes.dnd_visualizer.db.DBManager;
import it.italiandudes.idl.common.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public final class TabNotes {

    // Attributes
    private static String noteTitle = null;

    // Methods
    public static String getNoteTitle() {
        return noteTitle;
    }

    // Initialize
    public static void initialize(@NotNull final ControllerSceneSheetViewer controller) {
        controller.tableColumnNotesID.setCellValueFactory(new PropertyValueFactory<>("id"));
        controller.tableColumnNotesTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        controller.tableColumnNotesCreationDate.setCellValueFactory(new PropertyValueFactory<>("formattedCreationDate"));
        controller.tableColumnNotesLastEdit.setCellValueFactory(new PropertyValueFactory<>("formattedLastEdit"));
        controller.tableViewNotes.setPlaceholder(new Label("Non ci sono note salvate."));
        updateNotes(controller);
    }

    // EDT
    private static void updateNotes(@NotNull final ControllerSceneSheetViewer controller) {
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        String query = "SELECT * FROM notes;";
                        PreparedStatement ps = DBManager.preparedStatement(query);
                        if (ps == null) {
                            Platform.runLater(() -> {
                                new ErrorAlert("ERRORE", "Errore di Connessione al Database", "Non e' stato possibile consultare il database");
                                Client.getStage().setScene(SceneMainMenu.getScene());
                            });
                            return null;
                        }
                        ResultSet result = ps.executeQuery();
                        ArrayList<Note> notes = new ArrayList<>();
                        while (result.next()) {
                            Note note = new Note(
                                    result.getInt("id"),
                                    result.getString("title"),
                                    result.getString("content"),
                                    result.getDate("creation_date"),
                                    result.getDate("last_edit")
                            );
                            notes.add(note);
                        }
                        ps.close();
                        Platform.runLater(() -> controller.tableViewNotes.setItems(FXCollections.observableList(notes)));
                        return null;
                    }
                };
            }
        }.start();
    }
    public static void addNote(@NotNull final ControllerSceneSheetViewer controller) {
        noteTitle = null;
        Stage popupStage = Client.initPopupStage(SceneNote.getScene());
        popupStage.showAndWait();
        updateNotes(controller);
    }
    public static void deleteNote(@NotNull final ControllerSceneSheetViewer controller) {
        Note note = controller.tableViewNotes.getSelectionModel().getSelectedItem();
        if (note == null) return;
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {
                        try {
                            String query = "DELETE FROM notes WHERE id=?;";
                            PreparedStatement ps = DBManager.preparedStatement(query);
                            if (ps == null) throw new SQLException("The database connection doesn't exist");
                            ps.setInt(1, note.getId());
                            ps.executeUpdate();
                            ps.close();
                            Platform.runLater(() -> updateNotes(controller));
                        } catch (Exception e) {
                            Logger.log(e);
                            Platform.runLater(() -> new ErrorAlert("ERRORE", "Errore di Rimozione", "Si e' verificato un errore durante la rimozione dell'elemento."));
                        }
                        return null;
                    }
                };
            }
        }.start();
    }
    public static void editNote(@NotNull final ControllerSceneSheetViewer controller) {
        Note note = controller.tableViewNotes.getSelectionModel().getSelectedItem();
        if (note == null) return;
        noteTitle = note.getTitle();
        Stage popupStage = Client.initPopupStage(SceneNote.getScene());
        popupStage.showAndWait();
        updateNotes(controller);
    }
}
