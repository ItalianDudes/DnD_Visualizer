package it.italiandudes.dnd_visualizer.javafx.alert;

import it.italiandudes.dnd_visualizer.DnD_Visualizer;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

@SuppressWarnings("unused")
public class ErrorAlert extends Alert {

    public ErrorAlert(String title, String header, String content){
        super(AlertType.ERROR);
        ((Stage) getDialogPane().getScene().getWindow()).getIcons().add(DnD_Visualizer.appImage);
        if(title!=null) setTitle(title);
        if(header!=null) setHeaderText(header);
        if(content!=null) setContentText(content);
        show();
    }
    public ErrorAlert(String header, String content){
        this(null, header, content);
    }
    public ErrorAlert(){
        this(null,null,null);
    }

}
