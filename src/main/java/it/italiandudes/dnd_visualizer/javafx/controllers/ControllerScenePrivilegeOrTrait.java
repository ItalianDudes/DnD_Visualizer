package it.italiandudes.dnd_visualizer.javafx.controllers;

import it.italiandudes.dnd_visualizer.data.PrivilegeOrTrait;
import it.italiandudes.dnd_visualizer.data.enums.PrivilegeType;
import it.italiandudes.dnd_visualizer.javafx.alerts.ErrorAlert;
import it.italiandudes.dnd_visualizer.javafx.alerts.InformationAlert;
import it.italiandudes.dnd_visualizer.javafx.controllers.sheetviewer.TabPrivilegesAndTraits;
import it.italiandudes.dnd_visualizer.utils.Defs;
import it.italiandudes.idl.common.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.jetbrains.annotations.NotNull;

public final class ControllerScenePrivilegeOrTrait {

    // Attributes
    private PrivilegeOrTrait privilegeOrTrait = null;

    //Graphic Elements
    @FXML private TextField textFieldName;
    @FXML private ComboBox<PrivilegeType> comboBoxType;
    @FXML private TextArea textAreaContent;

    // Initialize
    @FXML
    private void initialize() {
        comboBoxType.setItems(FXCollections.observableList(PrivilegeType.privilege_types));
        String privilegeName = TabPrivilegesAndTraits.getPrivilegeName();
        if (privilegeName != null) initExistingPrivilegeOrTrait(privilegeName);
    }

    // EDT
    @FXML
    private void save() {
        if (textFieldName.getText().replace(" ", "").isEmpty()) {
            new ErrorAlert("ERRORE", "Errore di Inserimento", "Non e' stato assegnato un titolo alla nota.");
            return;
        }
        PrivilegeType type = comboBoxType.getSelectionModel().getSelectedItem();
        if (type == null) {
            new ErrorAlert("ERRORE", "Errore di Inserimento", "Non e' stato scritto se è un privilegio o un tratto.");
            return;
        }
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<>() {
                    @Override
                    protected Void call() {

                        try {

                            String name = textFieldName.getText();
                            String content = textAreaContent.getText();

                            String oldName = null;
                            if (privilegeOrTrait == null) {
                                if (PrivilegeOrTrait.checkIfExist(name)) {
                                    Platform.runLater(() -> new ErrorAlert("ERRORE", "Errore di Inserimento", "Esiste gia' un privilegio o un tratto con questo nome!"));
                                    return null;
                                }
                                privilegeOrTrait = new PrivilegeOrTrait(
                                        null,
                                        name,
                                        type,
                                        content
                                );
                            } else {
                                oldName = privilegeOrTrait.getName();
                                privilegeOrTrait = new PrivilegeOrTrait(
                                        privilegeOrTrait.getId(),
                                        name,
                                        type,
                                        content
                                );
                            }

                            privilegeOrTrait.saveIntoDatabase(oldName);
                            Platform.runLater(() -> new InformationAlert("SUCCESSO", "Salvataggio dei Dati", "Salvataggio dei dati completato con successo!"));
                        } catch (Exception e) {
                            Logger.log(e, Defs.LOGGER_CONTEXT);
                            Platform.runLater(() -> {
                                new ErrorAlert("ERRORE", "Errore di Lettura", "Impossibile leggere l'elemento dal database");
                                textFieldName.getScene().getWindow().hide();
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
        textFieldName.getScene().getWindow().hide();
    }

    // Methods
    private void initExistingPrivilegeOrTrait(@NotNull final String name) {
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<>() {
                    @Override
                    protected Void call() {
                        try {
                            privilegeOrTrait = new PrivilegeOrTrait(name);
                            Platform.runLater(() -> {
                                textFieldName.setText(name);
                                comboBoxType.getSelectionModel().select(privilegeOrTrait.getType());
                                textAreaContent.setText(privilegeOrTrait.getContent());
                            });
                        } catch (Exception e) {
                            Logger.log(e, Defs.LOGGER_CONTEXT);
                            Platform.runLater(() -> {
                                new ErrorAlert("ERRORE", "Errore di Lettura", "Impossibile leggere l'elemento dal database");
                                textFieldName.getScene().getWindow().hide();
                            });
                        }
                        return null;
                    }
                };
            }
        }.start();
    }

}
