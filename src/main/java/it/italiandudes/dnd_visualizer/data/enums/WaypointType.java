package it.italiandudes.dnd_visualizer.data.enums;

import it.italiandudes.dnd_visualizer.utils.Defs;
import it.italiandudes.dnd_visualizer.utils.Defs.Resources.Image.MapMarkers;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

public enum WaypointType {
    GENERIC_WAYPOINT("Waypoint", MapMarkers.MARKER_GENERIC, Color.WHITE),
    OBJECTIVE_MISSION_PRIMARY("Missione Primaria", MapMarkers.Objectives.OBJECTIVE_MISSION, Color.YELLOW),
    SECONDARY_MISSION("Missione Secondaria", MapMarkers.Objectives.OBJECTIVE_MISSION, Color.ORANGE),
    TIME_MISSION("Missione Evento", MapMarkers.Objectives.OBJECTIVE_TIME_MISSION, Color.CYAN),
    SPECIAL_MISSION("Missione Speciale", MapMarkers.Objectives.OBJECTIVE_MISSION, Color.YELLOW),
    POINT_OF_INTEREST_MARKET("Negozio", MapMarkers.PointsOfInterest.POI_MARKET, Color.GREEN),
    POINT_OF_INTEREST_TAVERN("Taverna", MapMarkers.PointsOfInterest.POI_TAVERN, Color.GREEN),
    POINT_OF_INTEREST_OFFICE("Ufficio", MapMarkers.PointsOfInterest.POI_OFFICE, Color.GREEN),
    POINT_OF_INTEREST_HOME("Casa", MapMarkers.PointsOfInterest.POI_HOME, Color.GREEN);

    // Attributes
    @NotNull private final String name;
    @NotNull private final Image image;
    @NotNull private final Color color;

    // Constructor
    WaypointType(@NotNull final String name, @NotNull final String imagePath, @NotNull final Color color) {
        this.name = name;
        this.image = new Image(Defs.Resources.getAsStream(imagePath));
        this.color = color;
    }

    // Methods
    public @NotNull String getName() {
        return name;
    }
    public @NotNull Image getImage() {
        return image;
    }
    public @NotNull Color getColor() {
        return color;
    }
    @Override
    public @NotNull String toString() {
        return getName();
    }
}
