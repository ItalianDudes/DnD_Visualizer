package it.italiandudes.dnd_visualizer.client.javafx.controller;

import it.italiandudes.dnd_visualizer.client.javafx.alert.ErrorAlert;
import it.italiandudes.dnd_visualizer.client.javafx.alert.InformationAlert;
import it.italiandudes.dnd_visualizer.client.javafx.controller.sheetviewer.TabNotes;
import it.italiandudes.dnd_visualizer.data.Note;
import it.italiandudes.idl.common.Logger;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.jetbrains.annotations.NotNull;

import java.sql.Date;
import java.time.Instant;

public final class ControllerSceneNote {

    // Attributes
    private Note note = null;

    //Graphic Elements
    @FXML private TextField textFieldTitle;
    @FXML private TextField textFieldCreationDate;
    @FXML private TextField textFieldLastEdit;
    @FXML private TextArea textAreaContent;

    // Initialize
    @FXML
    private void initialize() {
        String noteTitle = TabNotes.getNoteTitle();
        if (noteTitle != null) initExistingNote(noteTitle);
    }

    // EDT
    @FXML
    private void save() {
        if (textFieldTitle.getText().replace(" ", "").isEmpty()) {
            new ErrorAlert("ERRORE", "Errore di Inserimento", "Non e' stato assegnato un titolo alla nota.");
            return;
        }
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {

                        try {

                            String title = textFieldTitle.getText();
                            String content = textAreaContent.getText();

                            String oldTitle = null;
                            if (note == null) {
                                if (Note.checkIfExist(title)) {
                                    Platform.runLater(() -> new ErrorAlert("ERRORE", "Errore di Inserimento", "Esiste gia' una nota con questo titolo!"));
                                    return null;
                                }
                                note = new Note(
                                        null,
                                        title,
                                        content,
                                        null,
                                        null
                                );
                            } else {
                                oldTitle = note.getTitle();
                                note = new Note(
                                        note.getId(),
                                        title,
                                        content,
                                        note.getCreationDate(),
                                        new Date(Instant.now().toEpochMilli())
                                );
                            }

                            note.saveIntoDatabase(oldTitle);
                            Platform.runLater(() -> new InformationAlert("SUCCESSO", "Salvataggio dei Dati", "Salvataggio dei dati completato con successo!"));
                        } catch (Exception e) {
                            Logger.log(e);
                            Platform.runLater(() -> {
                                new ErrorAlert("ERRORE", "Errore di Lettura", "Impossibile leggere l'elemento dal database");
                                textFieldTitle.getScene().getWindow().hide();
                            });
                        }
                        return null;
                    }
                };
            }
        }.start();
    }
    @FXML
    private void backToSheet() {
        textFieldTitle.getScene().getWindow().hide();
    }

    // Methods
    private void initExistingNote(@NotNull final String title) {
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {
                        try {
                            note = new Note(title);
                            Platform.runLater(() -> {
                                textFieldTitle.setText(title);
                                textAreaContent.setText(note.getContent());
                                textFieldCreationDate.setText(note.getFormattedCreationDate());
                                textFieldLastEdit.setText(note.getFormattedLastEdit());
                            });
                        } catch (Exception e) {
                            Logger.log(e);
                            Platform.runLater(() -> {
                                new ErrorAlert("ERRORE", "Errore di Lettura", "Impossibile leggere l'elemento dal database");
                                textFieldTitle.getScene().getWindow().hide();
                            });
                        }
                        return null;
                    }
                };
            }
        }.start();
    }

}
