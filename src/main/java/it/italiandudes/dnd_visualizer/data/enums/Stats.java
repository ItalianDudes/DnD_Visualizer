package it.italiandudes.dnd_visualizer.data.enums;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public enum Stats {
    STRENGTH("Forza"),
    DEXTERITY("Destrezza"),
    CONSTITUTION("Costituzione"),
    INTELLIGENCE("Intelligenza"),
    WISDOM("Saggezza"),
    CHARISMA("Carisma");

    // Attributes
    public static final ArrayList<String> statsNames = new ArrayList<>();
    public static final ArrayList<String> shortStatsNames = new ArrayList<>();
    static {
        for (Stats stats : Stats.values()) {
            statsNames.add(stats.getName());
            shortStatsNames.add(stats.getName().toUpperCase().substring(0, 3));
        }
    }
    private final String name;

    // Constructors
    Stats(@NotNull final String name) {
        this.name = name;
    }

    // Methods
    public String getName() {
        return name;
    }
}
