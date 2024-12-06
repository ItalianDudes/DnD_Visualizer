package it.italiandudes.dnd_visualizer.data.enums;

import it.italiandudes.dnd_visualizer.utils.Defs.Resources.SVG;
import it.italiandudes.dnd_visualizer.utils.SVGReader;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public enum EntityType {
    ENTITY_NPC("NPC", SVG.Entities.ENTITY_NPC, Color.LIGHTGRAY),
    ENTITY_PLAYER("Giocatore", SVG.Entities.ENTITY_PLAYER, Color.WHITE),
    ENTITY_ALLY("Alleato", SVG.Entities.ENTITY_NPC, Color.CYAN),
    ENTITY_ENEMY("Nemico", SVG.Entities.ENTITY_ENEMY, Color.RED),
    ENTITY_STRONG_ENEMY("Nemico Potente", SVG.Entities.ENTITY_STRONG_ENEMY, Color.RED),
    ENTITY_BOSS("Boss", SVG.Entities.ENTITY_BOSS, Color.DARKRED);

    // Attributes
    @NotNull private final String name;
    @NotNull private final String svgContent;
    @NotNull private final Color color;

    // Constructor
    EntityType(@NotNull final String name, @NotNull final String svgPath, @NotNull final Color color) {
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
