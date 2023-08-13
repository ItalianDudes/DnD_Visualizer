package it.italiandudes.dnd_visualizer.javafx.scene;

import it.italiandudes.dnd_visualizer.javafx.JFXDefs;
import it.italiandudes.dnd_visualizer.utils.Defs;
import it.italiandudes.idl.common.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

@SuppressWarnings("unused")
public final class SceneLoading {

    //Scene Generator
    public static Scene getScene(){
        try {
            return new Scene(FXMLLoader.load(Defs.Resources.get(JFXDefs.Resources.FXML.FXML_LOADING)));
        }catch (IOException e){
            Logger.log(e);
            return null;
        }
    }
}