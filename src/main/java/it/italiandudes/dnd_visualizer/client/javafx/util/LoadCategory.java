package it.italiandudes.dnd_visualizer.client.javafx.util;

import org.jetbrains.annotations.NotNull;

public enum LoadCategory {
    LIGHT(0, 30, "Leggero"),
    NORMAL(30, 70, "Normale"),
    HEAVY(70, 100, "Pesante"),
    OVERLOAD(100, Double.MAX_VALUE,"Sovraccarico"),
    ERROR(-1, -1, "Errore")
    ;

    // Attributes
    private final double loadThresholdMinPercentage;
    private final double loadThresholdMaxPercentage;
    private final String loadIdentifier;

    // Constructors
    LoadCategory(final double loadThresholdMinPercentage, final double loadThresholdMaxPercentage, @NotNull final String loadIdentifier) {
        this.loadThresholdMinPercentage = loadThresholdMinPercentage;
        this.loadThresholdMaxPercentage = loadThresholdMaxPercentage;
        this.loadIdentifier = loadIdentifier;
    }

    // Methods
    public double getLoadThresholdMinPercentage() {
        return loadThresholdMinPercentage;
    }
    public double getLoadThresholdMaxPercentage() {
        return loadThresholdMaxPercentage;
    }
    public String getLoadIdentifier() {
        return loadIdentifier;
    }
}
