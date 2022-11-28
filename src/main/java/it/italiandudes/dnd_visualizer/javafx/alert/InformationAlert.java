package it.italiandudes.dnd_visualizer.javafx.alert;

import it.italiandudes.dnd_visualizer.DnD_Visualizer;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

@SuppressWarnings("unused")
public final class InformationAlert extends Alert {

    public InformationAlert(String title, String header, String content){
        super(AlertType.INFORMATION);
        ((Stage) getDialogPane().getScene().getWindow()).getIcons().add(DnD_Visualizer.appImage);
        if(title!=null) setTitle(title);
        if(header!=null) setHeaderText(header);
        if(content!=null) setContentText(content);
        show();
    }
    public InformationAlert(String header, String content){
        this(null, header, content);
    }
    public InformationAlert(){
        this(null,null,null);
    }

}
