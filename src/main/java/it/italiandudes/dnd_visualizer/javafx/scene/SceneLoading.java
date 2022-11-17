package it.italiandudes.dnd_visualizer.javafx.scene;

import it.italiandudes.dnd_visualizer.DnD_Visualizer;
import it.italiandudes.idl.common.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.Objects;

public final class SceneLoading {

    //Attributes
    private static final Parent loadingFXML;
    static {
        Parent finalAssignedValue;
        try {
            finalAssignedValue = FXMLLoader.load(Objects.requireNonNull(DnD_Visualizer.class.getResource("/fxml/SceneLoading.fxml")));
        } catch (IOException e) {
            finalAssignedValue = null;
            if(Logger.isInitialized()){
                Logger.log(e);
            }else{
                e.printStackTrace();
            }
        }
        loadingFXML = finalAssignedValue;
    }

    //Methods
    public static Scene getScene(){
        return new Scene(loadingFXML);
    }

}
