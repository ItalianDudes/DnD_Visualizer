package it.italiandudes.dnd_visualizer.data.enums;

import it.italiandudes.dnd_visualizer.utils.Defs.Resources.SVG;
import it.italiandudes.dnd_visualizer.utils.SVGReader;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

public enum WaypointType {
    GENERIC_WAYPOINT("Waypoint", SVG.Entities.ENTITY_NPC, Color.WHITE),
    OBJECTIVE_MISSION_PRIMARY("Missione Primaria", SVG.Objectives.OBJECTIVE_MISSION, Color.YELLOW),
    SECONDARY_MISSION("Missione Secondaria", SVG.Objectives.OBJECTIVE_MISSION, Color.ORANGE),
    TIME_MISSION("Missione Evento", SVG.Objectives.OBJECTIVE_TIME_MISSION, Color.LIGHTBLUE),
    SPECIAL_MISSION("Missione Speciale", SVG.Objectives.OBJECTIVE_MISSION, Color.YELLOW),
    POINT_OF_INTEREST_MARKET("Negozio", SVG.PointsOfInterest.POI_MARKET, Color.GREEN),
    POINT_OF_INTEREST_TAVERN("Taverna", SVG.PointsOfInterest.POI_TAVERN, Color.GREEN),
    POINT_OF_INTEREST_OFFICE("Ufficio", SVG.PointsOfInterest.POI_OFFICE, Color.GREEN);

    // Attributes
    @NotNull private final String name;
    @NotNull private final String svgContent;
    @NotNull private final Color color;

    // Constructor
    WaypointType(@NotNull final String name, @NotNull final String svgPath, @NotNull final Color color) {
        this.name = name;
        this.svgContent = SVGReader.readSVGFileFromJar(svgPath);
        this.color = color;
    }

    // Methods
    public @NotNull String getName() {
        return name;
    }
    public @NotNull String getSVGContent() {
        return svgContent;
    }
    public @NotNull Color getColor() {
        return color;
    }
    @Override
    public @NotNull String toString() {
        return getName();
    }
}
