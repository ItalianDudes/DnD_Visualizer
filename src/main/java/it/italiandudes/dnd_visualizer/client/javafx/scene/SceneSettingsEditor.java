package it.italiandudes.dnd_visualizer.client.javafx.scene;

import it.italiandudes.dnd_visualizer.client.javafx.JFXDefs;
import it.italiandudes.dnd_visualizer.client.javafx.util.ThemeHandler;
import it.italiandudes.dnd_visualizer.utils.Defs;
import it.italiandudes.idl.common.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public final class SceneSettingsEditor {

    //Scene Generator
    public static Scene getScene(){
        try {
            Scene scene = new Scene(FXMLLoader.load(Defs.Resources.get(JFXDefs.Resources.FXML.FXML_SETTINGS_EDITOR)));
            ThemeHandler.loadConfigTheme(scene);
            return scene;
        }catch (IOException e){
            Logger.log(e);
            return null;
        }
    }
}
