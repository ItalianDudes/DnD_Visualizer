package it.italiandudes.dnd_visualizer.data.enums;

import it.italiandudes.dnd_visualizer.utils.Defs;
import it.italiandudes.dnd_visualizer.utils.SVGReader;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

public enum ElementType {
    ELEMENT_ITEM("Oggetto", Defs.Resources.SVG.Elements.ELEMENT_ITEM, Color.GRAY),
    ELEMENT_SPELL("Incantesimo", Defs.Resources.SVG.Elements.ELEMENT_SPELL, Color.GRAY),
    ELEMENT_ARMOR("Armatura", Defs.Resources.SVG.Elements.ELEMENT_ARMOR, Color.GRAY),
    ELEMENT_ADDON("Addon", Defs.Resources.SVG.Elements.ELEMENT_ADDON, Color.GRAY),
    ELEMENT_WEAPON("Arma", Defs.Resources.SVG.Elements.ELEMENT_WEAPON, Color.GRAY);

    // Attributes
    @NotNull
    private final String name;
    @NotNull private final String svgContent;
    @NotNull private final Color color;

    // Constructor
    ElementType(@NotNull final String name, @NotNull final String svgPath, @NotNull final Color color) {
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
