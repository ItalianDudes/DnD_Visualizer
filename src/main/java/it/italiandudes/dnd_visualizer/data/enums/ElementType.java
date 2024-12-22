package it.italiandudes.dnd_visualizer.data.enums;

import it.italiandudes.dnd_visualizer.utils.Defs;
import it.italiandudes.dnd_visualizer.utils.Defs.Resources.Image.MapMarkers;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

public enum ElementType {
    ELEMENT_ITEM("Oggetto", MapMarkers.Elements.ELEMENT_ITEM, Color.GRAY),
    ELEMENT_SPELL("Incantesimo", MapMarkers.Elements.ELEMENT_SPELL, Color.GRAY),
    ELEMENT_ARMOR("Armatura", MapMarkers.Elements.ELEMENT_ARMOR, Color.GRAY),
    ELEMENT_ADDON("Addon", MapMarkers.Elements.ELEMENT_ADDON, Color.GRAY),
    ELEMENT_WEAPON("Arma", MapMarkers.Elements.ELEMENT_WEAPON, Color.GRAY);

    // Attributes
    @NotNull private final String name;
    @NotNull private final Image image;
    @NotNull private final Color color;

    // Constructor
    ElementType(@NotNull final String name, @NotNull final String imagePath, @NotNull final Color color) {
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
