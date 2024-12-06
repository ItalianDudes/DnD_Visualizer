package it.italiandudes.dnd_visualizer.javafx.controller;

import it.italiandudes.dnd_visualizer.javafx.Client;
import it.italiandudes.dnd_visualizer.javafx.JFXDefs;
import it.italiandudes.dnd_visualizer.javafx.alert.ConfirmationAlert;
import it.italiandudes.dnd_visualizer.javafx.alert.ErrorAlert;
import it.italiandudes.dnd_visualizer.javafx.alert.InformationAlert;
import it.italiandudes.dnd_visualizer.javafx.controller.campaign.ControllerAskVaultCode;
import it.italiandudes.dnd_visualizer.javafx.scene.SceneLoading;
import it.italiandudes.dnd_visualizer.javafx.scene.SceneSettingsEditor;
import it.italiandudes.dnd_visualizer.javafx.scene.SceneSheetViewer;
import it.italiandudes.dnd_visualizer.javafx.scene.campaign.SceneCampaignMap;
import it.italiandudes.dnd_visualizer.javafx.util.SheetDataHandler;
import it.italiandudes.dnd_visualizer.db.DBManager;
import it.italiandudes.dnd_visualizer.utils.Defs;
import it.italiandudes.dnd_visualizer.utils.DiscordRichPresenceManager;
import it.italiandudes.dnd_visualizer.utils.Updater;
import it.italiandudes.idl.common.JarHandler;
import it.italiandudes.idl.common.Logger;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.jar.Attributes;

public final class ControllerSceneMainMenu {

    // Attributes
    private static boolean lastOperationNewSheet = false;

    // Methods
    public static boolean isLastOperationNewSheet() {
        return lastOperationNewSheet;
    }
    @SuppressWarnings("DuplicatedCode")
    private void updateApp(@NotNull final Scene thisScene, @NotNull final String latestVersion) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Aggiornamento D&D Visualizer");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Java Executable File", "*.jar"));
        fileChooser.setInitialFileName(Defs.APP_FILE_NAME+"-"+latestVersion+".jar");
        fileChooser.setInitialDirectory(new File(Defs.JAR_POSITION).getParentFile());
        File fileNewApp;
        try {
            fileNewApp = fileChooser.showSaveDialog(Client.getStage().getScene().getWindow());
        } catch (IllegalArgumentException e) {
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            fileNewApp = fileChooser.showSaveDialog(Client.getStage().getScene().getWindow());
        }
        if (fileNewApp == null) {
            Client.getStage().setScene(thisScene);
            return;
        }
        File finalFileNewApp = fileNewApp;
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {
                        try {
                            Updater.downloadNewVersion(finalFileNewApp.getAbsoluteFile().getParent() + File.separator + Defs.APP_FILE_NAME+"-"+latestVersion+".jar");
                            Platform.runLater(() -> {
                                if (new ConfirmationAlert("AGGIORNAMENTO", "Aggiornamento", "Download della nuova versione completato! Vuoi chiudere questa app?").result) {
                                    System.exit(0);
                                } else {
                                    Client.getStage().setScene(thisScene);
                                }
                            });
                        } catch (IOException e) {
                            Logger.log(e);
                            Platform.runLater(() -> {
                                new ErrorAlert("ERRORE", "Errore di Download", "Si e' verificato un errore durante il download della nuova versione dell'app.");
                                Client.getStage().setScene(thisScene);
                            });
                        }
                        return null;
                    }
                };
            }
        }.start();
    }

    //Graphic Elements
    @FXML private ImageView imageViewLogo;
    @FXML private Button buttonNewCampaign;
    @FXML private Button buttonOpenCampaign;
    @FXML private Button buttonUnlockCampaign;

    // Initialize
    @FXML
    private void initialize() {
        lastOperationNewSheet = false;
        Client.getStage().setResizable(true);
        imageViewLogo.setImage(JFXDefs.AppInfo.LOGO);
        DiscordRichPresenceManager.updateRichPresenceState(DiscordRichPresenceManager.States.MENU);
    }

    // EDT
    @FXML
    private void checkForUpdates() {
        Scene thisScene = Client.getStage().getScene();
        Client.getStage().setScene(SceneLoading.getScene());
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {
                        String latestVersion = Updater.getLatestVersion();
                        if (latestVersion == null) {
                            Platform.runLater(() -> {
                                new ErrorAlert("ERRORE", "Errore di Connessione", "Si e' verificato un errore durante il controllo della versione.");
                                Client.getStage().setScene(thisScene);
                            });
                            return null;
                        }

                        String currentVersion = null;
                        try {
                            Attributes attributes = JarHandler.ManifestReader.readJarManifest(Defs.JAR_POSITION);
                            currentVersion = JarHandler.ManifestReader.getValue(attributes, "Version");
                        } catch (IOException e) {
                            Logger.log(e);
                        }

                        if (Updater.getLatestVersion(currentVersion, latestVersion).equals(currentVersion)) {
                            Platform.runLater(() -> {
                                new InformationAlert("AGGIORNAMENTO", "Controllo Versione", "La versione corrente e' la piu' recente.");
                                Client.getStage().setScene(thisScene);
                            });
                            return null;
                        }

                        String finalCurrentVersion = currentVersion;
                        Platform.runLater(() -> {
                            if (new ConfirmationAlert("AGGIORNAMENTO", "Trovata Nuova Versione", "E' stata trovata una nuova versione. Vuoi scaricarla?\nVersione Corrente: "+ finalCurrentVersion +"\nNuova Versione: "+latestVersion).result) {
                                updateApp(thisScene, latestVersion);
                            } else {
                                Platform.runLater(() -> Client.getStage().setScene(thisScene));
                            }

                        });
                        return null;
                    }
                };
            }
        }.start();
    }
    @FXML
    private void newSheet() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Create the Sheet");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("DND5E Sheet", "*." + Defs.Resources.SHEET_EXTENSION));
        fileChooser.setInitialDirectory(new File(Defs.JAR_POSITION).getParentFile());
        File fileSheet;
        try {
            fileSheet = fileChooser.showSaveDialog(Client.getStage().getScene().getWindow());
        } catch (IllegalArgumentException e) {
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            fileSheet = fileChooser.showSaveDialog(Client.getStage().getScene().getWindow());
        }
        if (fileSheet == null) return;

        Scene thisScene = Client.getStage().getScene();
        Client.getStage().setScene(SceneLoading.getScene());

        File finalSheetDB = fileSheet;
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {
                        try {
                            DBManager.createSheet(finalSheetDB.getAbsolutePath());
                        } catch (SQLException e) {
                            Logger.log(e);
                            Platform.runLater(() -> {
                                new ErrorAlert("ERROR", "I/O Error", "An error has occurred during sheet opening");
                                Client.getStage().setScene(thisScene);
                            });
                            return null;
                        }

                        lastOperationNewSheet = true;
                        Platform.runLater(() -> Client.getStage().setScene(SceneSheetViewer.getScene()));
                        return null;
                    }
                };
            }
        }.start();
    }
    @FXML
    private void openSheet() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select the Sheet");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("DND5E Sheet", "*." + Defs.Resources.SHEET_EXTENSION));
        fileChooser.setInitialDirectory(new File(Defs.JAR_POSITION).getParentFile());
        File fileSheet;
        try {
            fileSheet = fileChooser.showOpenDialog(Client.getStage().getScene().getWindow());
        } catch (IllegalArgumentException e) {
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            fileSheet = fileChooser.showOpenDialog(Client.getStage().getScene().getWindow());
        }
        if (fileSheet == null) return;

        Scene thisScene = Client.getStage().getScene();
        Client.getStage().setScene(SceneLoading.getScene());

        File finalSheetDB = fileSheet;
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {
                        try {
                            DBManager.connectToDB(finalSheetDB);
                        } catch (IOException | SQLException e) {
                            Logger.log(e);
                            Platform.runLater(() -> {
                                new ErrorAlert("ERRORE", "Errore di I/O", "Si e' verificato un errore durante l'apertura della scheda.");
                                Client.getStage().setScene(thisScene);
                            });
                            return null;
                        }

                        String dbVersion = SheetDataHandler.readKeyParameter(Defs.KeyParameters.DB_VERSION);

                        if  (dbVersion == null || !dbVersion.equals(Defs.SHEET_DB_VERSION)) {
                            String sheetVersion = (dbVersion!=null?dbVersion:"NA");
                            String supportedVersion = Defs.SHEET_DB_VERSION;
                            Platform.runLater(() -> {
                                new ErrorAlert("ERRORE", "Errore della Scheda", "La versione della scheda selezionata non e' supportata.\nVersione Supportata: "+supportedVersion+"\nVersione Scheda: "+sheetVersion);
                                Client.getStage().setScene(thisScene);
                            });
                            return null;
                        }

                        Platform.runLater(() -> Client.getStage().setScene(SceneSheetViewer.getScene()));
                        return null;
                    }
                };
            }
        }.start();
    }
    @FXML
    private void askVaultCode() {
        try {
            Stage popupStage = Client.initPopupStage(new Scene(FXMLLoader.load(Defs.Resources.get("/fxml/campaign/AskVaultCode.fxml"))));
            popupStage.setResizable(false);
            popupStage.showAndWait();
            if (ControllerAskVaultCode.USER_ATTEMPT == null) return;
            if (!ControllerAskVaultCode.PASSWORD.equals(ControllerAskVaultCode.USER_ATTEMPT)) {
                new ErrorAlert("ERRORE", "Vault-Code Errato", "Il Vault-Code inserito e' errato, sei sicuro che ne valga davvero la pena? Se si riprova.");
            } else {
                new InformationAlert("AUMENTO PRIVILEGI", "Privilegi Aumentati", "I tuoi privilegi sono passati da \"Utente\" a \"Sviluppatore\", puoi procedere.");
                buttonNewCampaign.setDisable(false);
                buttonOpenCampaign.setDisable(false);
                buttonUnlockCampaign.setDisable(true);
            }
            ControllerAskVaultCode.USER_ATTEMPT = null;

        } catch (IOException e) {
            Logger.log(e);
            System.exit(-1);
        }
    }
    @FXML
    private void newCampaign() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Create the Campaign");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("DND5E Campaign", "*." + Defs.Resources.CAMPAIGN_EXTENSION));
        fileChooser.setInitialDirectory(new File(Defs.JAR_POSITION).getParentFile());
        File fileSheet;
        try {
            fileSheet = fileChooser.showSaveDialog(Client.getStage().getScene().getWindow());
        } catch (IllegalArgumentException e) {
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            fileSheet = fileChooser.showSaveDialog(Client.getStage().getScene().getWindow());
        }
        if (fileSheet == null) return;

        Scene thisScene = Client.getStage().getScene();
        Client.getStage().setScene(SceneLoading.getScene());

        File finalSheetDB = fileSheet;
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {
                        try {
                            DBManager.createCampaign(finalSheetDB.getAbsolutePath());
                        } catch (SQLException e) {
                            Logger.log(e);
                            Platform.runLater(() -> {
                                new ErrorAlert("ERRORE", "Errore di I/O", "Si e' verificato un errore durante l'apertura della campagna.");
                                Client.getStage().setScene(thisScene);
                            });
                            return null;
                        }

                        Platform.runLater(() -> Client.getStage().setScene(SceneCampaignMap.getScene()));
                        return null;
                    }
                };
            }
        }.start();
    }
    @FXML
    private void openCampaign() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select the Campaign");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("DND5E Campaign", "*." + Defs.Resources.CAMPAIGN_EXTENSION));
        fileChooser.setInitialDirectory(new File(Defs.JAR_POSITION).getParentFile());
        File fileSheet;
        try {
            fileSheet = fileChooser.showOpenDialog(Client.getStage().getScene().getWindow());
        } catch (IllegalArgumentException e) {
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            fileSheet = fileChooser.showOpenDialog(Client.getStage().getScene().getWindow());
        }
        if (fileSheet == null) return;

        Scene thisScene = Client.getStage().getScene();
        Client.getStage().setScene(SceneLoading.getScene());

        File finalSheetDB = fileSheet;
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {
                        try {
                            DBManager.connectToDB(finalSheetDB);
                        } catch (IOException | SQLException e) {
                            Logger.log(e);
                            Platform.runLater(() -> {
                                new ErrorAlert("ERRORE", "Errore di I/O", "Si e' verificato un errore durante l'apertura della campagna.");
                                Client.getStage().setScene(thisScene);
                            });
                            return null;
                        }

                        String dbVersion = SheetDataHandler.readKeyParameter(Defs.KeyParameters.DB_VERSION);

                        if  (dbVersion == null || !dbVersion.equals(Defs.CAMPAIGN_DB_VERSION)) {
                            String campaignVersion = (dbVersion!=null?dbVersion:"NA");
                            String supportedVersion = Defs.CAMPAIGN_DB_VERSION;
                            Platform.runLater(() -> {
                                new ErrorAlert("ERRORE", "Errore della Campagna", "La versione della campagna selezionata non e' supportata.\nVersione Supportata: "+supportedVersion+"\nVersione Scheda: "+campaignVersion);
                                Client.getStage().setScene(thisScene);
                            });
                            return null;
                        }

                        Platform.runLater(() -> Client.getStage().setScene(SceneCampaignMap.getScene()));
                        return null;
                    }
                };
            }
        }.start();
    }
    @FXML
    private void openSettingsEditor() {
        Client.getStage().setScene(SceneSettingsEditor.getScene());
    }
}
