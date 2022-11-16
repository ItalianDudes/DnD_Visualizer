package it.italiandudes.dnd_visualizer.javafx.scene;

import it.italiandudes.dnd_visualizer.DnD_Visualizer;
import it.italiandudes.idl.common.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public final class SceneLoading {

    public static Scene getScene(){
        try {
            return new Scene(FXMLLoader.load(Objects.requireNonNull(DnD_Visualizer.class.getResource("/fxml/SceneLoading.fxml"))));
        }catch (IOException e){
            if(Logger.isInitialized()){
                Logger.log(e);
            }else{
                e.printStackTrace();
            }
            return null;
        }
    }

}
