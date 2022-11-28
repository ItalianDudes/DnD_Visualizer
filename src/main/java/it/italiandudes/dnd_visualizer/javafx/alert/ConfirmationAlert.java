package it.italiandudes.dnd_visualizer.javafx.alert;

import it.italiandudes.dnd_visualizer.DnD_Visualizer;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

@SuppressWarnings("unused")
public final class ConfirmationAlert extends Alert {

    public ConfirmationAlert(String title, String header, String content){
        super(Alert.AlertType.CONFIRMATION);
        ((Stage) getDialogPane().getScene().getWindow()).getIcons().add(DnD_Visualizer.appImage);
        if(title!=null) setTitle(title);
        if(header!=null) setHeaderText(header);
        if(content!=null) setContentText(content);
        show();
    }
    public ConfirmationAlert(String header, String content){
        this(null, header, content);
    }
    public ConfirmationAlert(){
        this(null,null,null);
    }

}
