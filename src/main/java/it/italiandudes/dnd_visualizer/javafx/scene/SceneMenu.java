package it.italiandudes.dnd_visualizer.javafx.scene;

import it.italiandudes.dnd_visualizer.DnD_Visualizer;
import it.italiandudes.idl.common.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.Objects;

public final class SceneMenu {

    //Methods
    public static Scene getScene(){
        try {
            return new Scene(FXMLLoader.load(Objects.requireNonNull(DnD_Visualizer.class.getResource("/fxml/SceneMenu.fxml"))));
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
