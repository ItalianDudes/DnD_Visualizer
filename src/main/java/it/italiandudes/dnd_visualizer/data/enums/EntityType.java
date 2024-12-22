package it.italiandudes.dnd_visualizer.data.enums;

import it.italiandudes.dnd_visualizer.utils.Defs;
import it.italiandudes.dnd_visualizer.utils.Defs.Resources.Image.MapMarkers;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public enum EntityType {
    ENTITY_PLAYER("Giocatore", MapMarkers.Entities.ENTITY_PLAYER, Color.WHITE),
    ENTITY_NPC("NPC", MapMarkers.Entities.ENTITY_NPC, Color.LIGHTGRAY),
    ENTITY_ALLY("Alleato", MapMarkers.Entities.ENTITY_ALLY, Color.CYAN),
    ENTITY_ENEMY("Nemico", MapMarkers.Entities.ENTITY_ENEMY, Color.RED),
    ENTITY_STRONG_ENEMY("Nemico Potente", MapMarkers.Entities.ENTITY_STRONG_ENEMY, Color.RED),
    ENTITY_BOSS("Boss", MapMarkers.Entities.ENTITY_BOSS, Color.DARKRED);

    // Attributes
    @NotNull private final String name;
    @NotNull private final Image image;
    @NotNull private final Color color;

    // Constructor
    EntityType(@NotNull final String name, @NotNull final String imagePath, @NotNull final Color color) {
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
