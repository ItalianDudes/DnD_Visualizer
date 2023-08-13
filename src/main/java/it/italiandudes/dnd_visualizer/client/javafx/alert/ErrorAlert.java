package it.italiandudes.dnd_visualizer.client.javafx.alert;

import it.italiandudes.dnd_visualizer.client.javafx.JFXDefs;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

@SuppressWarnings("unused")
public final class ErrorAlert extends Alert {

    //Constructors
    public ErrorAlert(String title, String header, String content){
        super(AlertType.ERROR);
        this.setResizable(true);
        ((Stage) getDialogPane().getScene().getWindow()).getIcons().add(JFXDefs.AppInfo.LOGO);
        if(title!=null) setTitle(title);
        if(header!=null) setHeaderText(header);
        if(content!=null) setContentText(content);
        showAndWait();
    }
    public ErrorAlert(String header, String content){
        this(null, header, content);
    }
    public ErrorAlert(){
        this(null,null,null);
    }

}