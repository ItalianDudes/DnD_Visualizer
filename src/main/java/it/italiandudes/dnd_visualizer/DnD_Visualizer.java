package it.italiandudes.dnd_visualizer;

import it.italiandudes.dnd_visualizer.javafx.scene.SceneStartup;
import it.italiandudes.idl.common.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

public final class DnD_Visualizer extends Application {

    //Attributes
    private static Stage stage;
    private static Connection dbConnection;

    @Override
    public void start(Stage stage) {
        DnD_Visualizer.stage = stage;
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

        //This Jar Executable Location
        public static final File jarExecutablePath = new File(DnD_Visualizer.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile();

    }

}
