package it.italiandudes.dnd_visualizer;

import it.italiandudes.dnd_visualizer.javafx.scene.SceneStartup;
import it.italiandudes.idl.common.Logger;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

@SuppressWarnings("unused")
public final class DnD_Visualizer extends Application {

    //Attributes
    private static Stage stage;
    private static Connection dbConnection = null;
    public static final Image appImage = new Image(Objects.requireNonNull(DnD_Visualizer.class.getResource("/icon/dnd-logo.png")).toString());

    @Override
    public void start(Stage stage) {
        DnD_Visualizer.stage = stage;
        stage.setTitle("D&D Visualizer");
        stage.getIcons().add(appImage);
        stage.setScene(SceneStartup.getScene());
        stage.show();
    }

    public static Stage getStage(){
        return stage;
    }
    public static Connection getDbConnection(){
        return dbConnection;
    }
    public static boolean setDbConnection(Connection dbConnection){
        if(DnD_Visualizer.dbConnection==null) {
            DnD_Visualizer.dbConnection = dbConnection;
            return true;
        }
        return false;
    }
    public static void closeAndClearDBConnection(){
        try{
            if(dbConnection!=null && !dbConnection.isClosed())
                dbConnection.close();
        }catch (SQLException e){
            if(Logger.isInitialized()){
                Logger.log(e);
            }else{
                e.printStackTrace();
            }
        }
        dbConnection = null;
    }

    //Main Method
    public static void main(String[] args) {
        try{
            Logger.init();
        }catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if(dbConnection!=null){
                try{
                    if(!dbConnection.isClosed())
                        dbConnection.close();
                }catch (SQLException e){
                    if(Logger.isInitialized()){
                        Logger.log(e);
                    }else{
                        e.printStackTrace();
                    }
                }
                Logger.close();
            }
        }));
        launch(args);
    }

    //Constants
    public static final class Defs {

        public static final class MenuChoices {
            private static final String fxmlPrefix = "/fxml/menu/";
            private static final String[] choiceName = {
                    "Oggetto",
                    "Lingua",
                    "Specie"
            };
            private static final String[] associatedFXML = {
                    fxmlPrefix+"SceneMenuItem.fxml",
                    null, //TODO: Lingua FXML
                    null //TODO: Specie FXML
            };
            public static String[] getChoiceNames(){
                return choiceName;
            }
            public static String[] getFxmlPrefix() {
                return associatedFXML;
            }
        }

        //This Jar Executable Location
        public static final File jarExecutablePath = new File(DnD_Visualizer.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile();

    }

}
