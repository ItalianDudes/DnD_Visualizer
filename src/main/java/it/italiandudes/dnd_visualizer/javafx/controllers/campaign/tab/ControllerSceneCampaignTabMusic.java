package it.italiandudes.dnd_visualizer.javafx.controllers.campaign.tab;

import it.italiandudes.dnd_visualizer.javafx.Client;
import it.italiandudes.dnd_visualizer.utils.Defs;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;

public final class ControllerSceneCampaignTabMusic {

    // Constants
    private static final double AUDIO_SCALE_FACTOR = 0.5;
    private static final double Y_AXIS_UPPER_BOUND = 100;
    private static final double Y_NO_AUDIO_FLATLINE = Y_AXIS_UPPER_BOUND / 2;

    // Attributes
    private ArrayList<XYChart.Data<Number, Number>> seriesData;
    private AudioSpectrumListener audioSpectrumListener;
    private MediaPlayer audioPlayer;

    // Graphic Elements
    @FXML private ListView<String> listViewTracks;
    @FXML private AnchorPane anchorPaneChartContainer;

    // Initialize
    @FXML
    private void initialize() {
        NumberAxis xAxis = new NumberAxis(0, 75, 75);
        xAxis.setTickLabelsVisible(false);
        xAxis.getStyleClass().add("xAxis");
        NumberAxis yAxis = new NumberAxis(0, Y_AXIS_UPPER_BOUND, Y_AXIS_UPPER_BOUND);
        yAxis.setTickLabelsVisible(false);
        yAxis.getStyleClass().add("yAxis");
        LineChart<Number, Number> audioSignalChart = new LineChart<>(xAxis, yAxis);
        audioSignalChart.setCreateSymbols(false);
        audioSignalChart.setLegendVisible(false);
        audioSignalChart.setAnimated(false);
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        seriesData = new ArrayList<>();
        for (int i = 0; i < xAxis.getUpperBound(); i++) {
            seriesData.add(new XYChart.Data<>(i, Y_NO_AUDIO_FLATLINE));
            series.getData().add(seriesData.get(i));
        }
        audioSignalChart.getData().add(series);
        AnchorPane.setTopAnchor(audioSignalChart, 0.0);
        AnchorPane.setLeftAnchor(audioSignalChart, 0.0);
        AnchorPane.setBottomAnchor(audioSignalChart, 0.0);
        AnchorPane.setRightAnchor(audioSignalChart, 0.0);
        audioSignalChart.setMinSize(LineChart.USE_COMPUTED_SIZE, LineChart.USE_COMPUTED_SIZE);
        audioSignalChart.setPrefSize(LineChart.USE_COMPUTED_SIZE, LineChart.USE_COMPUTED_SIZE);
        audioSignalChart.setMaxSize(LineChart.USE_COMPUTED_SIZE, LineChart.USE_COMPUTED_SIZE);
        anchorPaneChartContainer.getChildren().add(audioSignalChart);
        audioSpectrumListener = (double timestamp, double duration, float[] magnitudes, float[] phases) -> {
            for (int i = 0; i < seriesData.size(); i++) {
                seriesData.get(i).setYValue(magnitudes[i] * Math.sin(phases[i] * AUDIO_SCALE_FACTOR) + Y_AXIS_UPPER_BOUND/2);
            }
        };
    }

    // Methods
    private void reconfigureAudioPlayer(@NotNull final String AUDIO_URI) {
        if (audioSpectrumListener == null) return;
        if (audioPlayer != null) {
            audioPlayer.stop();
            for (XYChart.Data<Number, Number> seriesDatum : seriesData) {
                seriesDatum.setYValue(Y_NO_AUDIO_FLATLINE);
            }
        }
        Media audioMedia = new Media(AUDIO_URI);
        audioPlayer = new MediaPlayer(audioMedia);
        audioPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        audioPlayer.setAudioSpectrumListener(audioSpectrumListener);
    }

    // EDT
    @FXML
    private void openTracksContextMenu(@NotNull final ContextMenuEvent event) {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem addTrack = new MenuItem("Aggiungi Traccia");
        addTrack.setOnAction(e -> changeTrack());
        contextMenu.getItems().add(addTrack);

        contextMenu.setAutoHide(true);
        contextMenu.show(Client.getStage(), event.getScreenX(), event.getScreenY());
    }
    @FXML
    private void changeTrack() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona un Contenuto Multimediale");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Musica", "*.mp3", "*.wav"));
        fileChooser.setInitialDirectory(new File(Defs.JAR_POSITION).getParentFile());
        File trackPath;
        try {
            trackPath = fileChooser.showOpenDialog(Client.getStage().getScene().getWindow());
        } catch (IllegalArgumentException e) {
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            trackPath = fileChooser.showOpenDialog(Client.getStage().getScene().getWindow());
        }
        if(trackPath!=null) {
            reconfigureAudioPlayer(trackPath.toURI().toString());
        }
    }
    @FXML
    private void switchPlay() {
        if (audioPlayer == null) return;
        if (audioPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            audioPlayer.pause();
            for (XYChart.Data<Number, Number> seriesDatum : seriesData) {
                seriesDatum.setYValue(Y_NO_AUDIO_FLATLINE);
            }
        } else {
            audioPlayer.play();
        }
    }
}
