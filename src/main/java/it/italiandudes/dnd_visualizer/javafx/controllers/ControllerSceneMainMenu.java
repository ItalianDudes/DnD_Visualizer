package it.italiandudes.dnd_visualizer.javafx.controllers;

import it.italiandudes.dnd_visualizer.DnD_Visualizer;
import it.italiandudes.dnd_visualizer.db.DBManager;
import it.italiandudes.dnd_visualizer.db.SheetKeyParameters;
import it.italiandudes.dnd_visualizer.javafx.Client;
import it.italiandudes.dnd_visualizer.javafx.JFXDefs;
import it.italiandudes.dnd_visualizer.javafx.alerts.ConfirmationAlert;
import it.italiandudes.dnd_visualizer.javafx.alerts.ErrorAlert;
import it.italiandudes.dnd_visualizer.javafx.alerts.InformationAlert;
import it.italiandudes.dnd_visualizer.javafx.alerts.YesNoAlert;
import it.italiandudes.dnd_visualizer.javafx.components.SceneController;
import it.italiandudes.dnd_visualizer.javafx.controllers.campaign.ControllerAskVaultCode;
import it.italiandudes.dnd_visualizer.javafx.scene.SceneLoading;
import it.italiandudes.dnd_visualizer.javafx.scene.SceneSettingsEditor;
import it.italiandudes.dnd_visualizer.javafx.scene.SceneSheetViewer;
import it.italiandudes.dnd_visualizer.javafx.scene.campaign.SceneCampaignViewer;
import it.italiandudes.dnd_visualizer.javafx.utils.SheetDataHandler;
import it.italiandudes.dnd_visualizer.javafx.utils.ThemeHandler;
import it.italiandudes.dnd_visualizer.utils.Defs;
import it.italiandudes.dnd_visualizer.features.DiscordRichPresenceManager;
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
import javafx.scene.input.ClipboardContent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.jar.Attributes;

public final class ControllerSceneMainMenu {

    // Attributes
    private static boolean lastOperationNewSheet = false;

    // Methods
    @SuppressWarnings("unused")
    public static boolean isLastOperationNewSheet() {
        return lastOperationNewSheet;
    }
    @SuppressWarnings("DuplicatedCode")
    private void updateApp(@NotNull final SceneController thisScene, @NotNull final String latestVersion) {
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
            Client.setScene(thisScene);
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
                                    Client.exit();
                                } else {
                                    Client.setScene(thisScene);
                                }
                            });
                        } catch (IOException e) {
                            Logger.log(e, Defs.LOGGER_CONTEXT);
                            Platform.runLater(() -> {
                                new ErrorAlert("ERRORE", "Errore di Download", "Si e' verificato un errore durante il download della nuova versione dell'app.");
                                Client.setScene(thisScene);
                            });
                        }
                        return null;
                    }
                };
            }
        }.start();
    }
    private void downloadLauncher(@NotNull final SceneController thisScene) {
        JFXDefs.startServiceTask(() -> {
            String latestVersion;
            try {
                URL url = new URL("https://github.com/ItalianDudes/ID_Launcher/releases/latest");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.getResponseCode();
                connection.disconnect();
                latestVersion = connection.getURL().toString().split("/tag/")[1];
            } catch (IOException e) {
                Logger.log(e, Defs.LOGGER_CONTEXT);
                Platform.runLater(() -> {
                    new ErrorAlert("ERRORE", "Errore di Connessione", "Si e' verificato un errore durante la connessione a GitHub.");
                    Client.setScene(thisScene);
                });
                return;
            }
            if (latestVersion == null) return;
            final String finalLatestVersion = latestVersion;
            Platform.runLater(() -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Download ID Launcher");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Java Executable File", "*.jar"));
                fileChooser.setInitialFileName("ID_Launcher"+"-"+ finalLatestVersion +".jar");
                fileChooser.setInitialDirectory(new File(Defs.JAR_POSITION).getParentFile());
                File launcherDest;
                try {
                    launcherDest = fileChooser.showSaveDialog(Client.getStage().getScene().getWindow());
                } catch (IllegalArgumentException e) {
                    fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
                    launcherDest = fileChooser.showSaveDialog(Client.getStage().getScene().getWindow());
                }
                if (launcherDest == null) {
                    Client.setScene(thisScene);
                    return;
                }

                final File finalLauncherDest = launcherDest;
                JFXDefs.startServiceTask(() -> {
                    String downloadURL = "https://github.com/ItalianDudes/ID_Launcher/releases/latest/download/ID_Launcher-"+ finalLatestVersion +".jar";
                    try {
                        URL url = new URL(downloadURL);
                        InputStream is = url.openConnection().getInputStream();
                        Files.copy(is, Paths.get(finalLauncherDest.getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);
                        is.close();
                    } catch (IOException e) {
                        Logger.log(e, Defs.LOGGER_CONTEXT);
                        Platform.runLater(() -> {
                            new ErrorAlert("ERRORE", "Errore di Download", "Si e' verificato un errore durante il download del launcher.");
                            Client.setScene(thisScene);
                        });
                        return;
                    }
                    Platform.runLater(() -> {
                        if (new YesNoAlert("CHIUDERE?", "Chiusura App", "Il launcher e' stato scaricato. Vuoi chiudere il D&D Visualizer?").result) {
                            Client.exit();
                        }
                    });
                });
            });
        });
    }

    // Graphic Elements
    @FXML private ImageView imageViewLogo;
    @FXML private Button buttonNewCampaign;
    @FXML private Button buttonOpenCampaign;
    @FXML private Button buttonUnlockCampaign;
    @FXML private Button buttonUpdater;

    // Initialize
    @FXML
    private void initialize() {
        lastOperationNewSheet = false;
        imageViewLogo.setImage(JFXDefs.AppInfo.LOGO);
        DiscordRichPresenceManager.updateRichPresenceState(DiscordRichPresenceManager.States.MENU);
        if (DnD_Visualizer.isStartedFromLauncher()) {
            buttonUpdater.setDisable(true);
        }
    }

    // EDT
    @FXML
    private void showReportBanner() {
        ClipboardContent link = new ClipboardContent();
        link.putString("https://github.com/ItalianDudes/DnD_Visualizer/issues");
        Client.getSystemClipboard().setContent(link);
        new InformationAlert("INFO", "Grazie!", "ItalianDudes e' sempre felice di ricevere segnalazioni da parte degli utenti circa le sue applicazioni.\nE' stato aggiunto alla tua clipboard di sistema il link per accedere alla pagina github per aggiungere il tuo report riguardante problemi o idee varie.\nGrazie ancora!");
    }
    @FXML
    private void checkForUpdates() {
        SceneController thisScene = Client.getScene();
        Client.setScene(SceneLoading.getScene());
        if (!DnD_Visualizer.isStartedFromLauncher()) {
            boolean response = new YesNoAlert("NOVITA", "ItalianDudes Launcher", "C'e' una novita'!\nE' possibile scaricare un launcher che gestisca tutte le applicazioni sviluppate da ItalianDudes.\nIl launcher permette una migliore gestione degli aggiornamenti e permette di sapere le novita' sugli ultimi aggiornamenti.\nVuoi scaricare il launcher?").result;
            if (response) {
                downloadLauncher(thisScene);
                return;
            }
        }
        JFXDefs.startServiceTask(() -> {
            String latestVersion = Updater.getLatestVersion();
            if (latestVersion == null) {
                Platform.runLater(() -> {
                    new ErrorAlert("ERRORE", "Errore di Connessione", "Si e' verificato un errore durante il controllo della versione.");
                    Client.setScene(thisScene);
                });
                return;
            }

            String currentVersion = null;
            try {
                Attributes attributes = JarHandler.ManifestReader.readJarManifest(Defs.JAR_POSITION);
                currentVersion = JarHandler.ManifestReader.getValue(attributes, "Version");
            } catch (IOException e) {
                Logger.log(e, Defs.LOGGER_CONTEXT);
            }

            if (Updater.getLatestVersion(currentVersion, latestVersion).equals(currentVersion)) {
                Platform.runLater(() -> {
                    new InformationAlert("AGGIORNAMENTO", "Controllo Versione", "La versione corrente e' la piu' recente.");
                    Client.setScene(thisScene);
                });
                return;
            }

            String finalCurrentVersion = currentVersion;
            Platform.runLater(() -> {
                if (new ConfirmationAlert("AGGIORNAMENTO", "Trovata Nuova Versione", "E' stata trovata una nuova versione. Vuoi scaricarla?\nVersione Corrente: "+ finalCurrentVersion +"\nNuova Versione: "+latestVersion).result) {
                    updateApp(thisScene, latestVersion);
                } else {
                    Platform.runLater(() -> Client.setScene(thisScene));
                }

            });
        });
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

        SceneController thisScene = Client.getScene();
        Client.setScene(SceneLoading.getScene());

        File finalSheetDB = fileSheet;
        JFXDefs.startServiceTask(() -> {
            try {
                DBManager.createSheet(finalSheetDB.getAbsolutePath());
            } catch (SQLException e) {
                Logger.log(e, Defs.LOGGER_CONTEXT);
                Platform.runLater(() -> {
                    new ErrorAlert("ERROR", "I/O Error", "An error has occurred during sheet opening");
                    Client.setScene(thisScene);
                });
                return;
            }

            lastOperationNewSheet = true;
            Platform.runLater(() -> Client.setScene(SceneSheetViewer.getScene()));
        });
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

        SceneController thisScene = Client.getScene();
        Client.setScene(SceneLoading.getScene());

        File finalSheetDB = fileSheet;
        JFXDefs.startServiceTask(() -> {
            try {
                DBManager.connectToDB(finalSheetDB);
            } catch (IOException | SQLException e) {
                Logger.log(e, Defs.LOGGER_CONTEXT);
                Platform.runLater(() -> {
                    new ErrorAlert("ERRORE", "Errore di I/O", "Si e' verificato un errore durante l'apertura della scheda.");
                    Client.setScene(thisScene);
                });
                return;
            }

            String dbVersion = SheetDataHandler.readKeyParameter(SheetKeyParameters.DB_VERSION);

            if  (dbVersion == null || !dbVersion.equals(Defs.SHEET_DB_VERSION)) {
                String sheetVersion = (dbVersion!=null?dbVersion:"NA");
                String supportedVersion = Defs.SHEET_DB_VERSION;
                Platform.runLater(() -> {
                    new ErrorAlert("ERRORE", "Errore della Scheda", "La versione della scheda selezionata non e' supportata.\nVersione Supportata: "+supportedVersion+"\nVersione Scheda: "+sheetVersion);
                    Client.setScene(thisScene);
                });
                return;
            }

            Platform.runLater(() -> Client.setScene(SceneSheetViewer.getScene()));
        });
    }
    @FXML
    private void askVaultCode() {
        try {
            Stage popupStage = new Stage();
            popupStage.getIcons().add(JFXDefs.AppInfo.LOGO);
            popupStage.setTitle(JFXDefs.AppInfo.NAME);
            popupStage.initOwner(Client.getStage());
            popupStage.initModality(Modality.WINDOW_MODAL);
            popupStage.setScene(new Scene(FXMLLoader.load(Defs.Resources.get(Defs.Resources.PROJECT_RESOURCES_ROOT + "fxml/campaign/AskVaultCode.fxml"))));
            ThemeHandler.loadConfigTheme(popupStage.getScene());
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
            Logger.log(e, Defs.LOGGER_CONTEXT);
            Client.exit(-1);
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

        SceneController thisScene = Client.getScene();
        Client.setScene(SceneLoading.getScene());

        File finalSheetDB = fileSheet;
        JFXDefs.startServiceTask(() -> {
            try {
                DBManager.createCampaign(finalSheetDB.getAbsolutePath());
            } catch (SQLException e) {
                Logger.log(e, Defs.LOGGER_CONTEXT);
                Platform.runLater(() -> {
                    new ErrorAlert("ERRORE", "Errore di I/O", "Si e' verificato un errore durante l'apertura della campagna.");
                    Client.setScene(thisScene);
                });
                return;
            }
            Platform.runLater(() -> Client.setScene(SceneCampaignViewer.getScene()));
        });
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

        SceneController thisScene = Client.getScene();
        Client.setScene(SceneLoading.getScene());

        File finalSheetDB = fileSheet;
        JFXDefs.startServiceTask(() -> {
            try {
                DBManager.connectToDB(finalSheetDB);
            } catch (IOException | SQLException e) {
                Logger.log(e, Defs.LOGGER_CONTEXT);
                Platform.runLater(() -> {
                    new ErrorAlert("ERRORE", "Errore di I/O", "Si e' verificato un errore durante l'apertura della campagna.");
                    Client.setScene(thisScene);
                });
                return;
            }

            String dbVersion = SheetDataHandler.readKeyParameter(SheetKeyParameters.DB_VERSION);

            if  (dbVersion == null || !dbVersion.equals(Defs.CAMPAIGN_DB_VERSION)) {
                String campaignVersion = (dbVersion!=null?dbVersion:"NA");
                String supportedVersion = Defs.CAMPAIGN_DB_VERSION;
                Platform.runLater(() -> {
                    new ErrorAlert("ERRORE", "Errore della Campagna", "La versione della campagna selezionata non e' supportata.\nVersione Supportata: "+supportedVersion+"\nVersione Scheda: "+campaignVersion);
                    Client.setScene(thisScene);
                });
                return;
            }

            Platform.runLater(() -> Client.setScene(SceneCampaignViewer.getScene()));
        });
    }
    @FXML
    private void openSettingsEditor() {
        Client.setScene(SceneSettingsEditor.getScene());
    }
}
