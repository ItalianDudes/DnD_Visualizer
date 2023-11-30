package it.italiandudes.dnd_visualizer.data.enums;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public enum EffectKnowledge {
    UNKNOWN(-1, "Sconosciuto"),
    NO(0, "No"),
    YES(1, "Si");

    // Attributes
    @NotNull
    public static final ArrayList<EffectKnowledge> knowledge = new ArrayList<>();
    static {
        knowledge.addAll(Arrays.asList(EffectKnowledge.values()));
    }
    private final int databaseValue;
    private final String name;

    // Constructors
    EffectKnowledge(final int databaseValue, final String name) {
        this.databaseValue = databaseValue;
        this.name = name;
    }

    // Methods
    public int getDatabaseValue() {
        return databaseValue;
    }
    public String getName() {
        return name;
    }
    @Override
    public String toString() {
        return getName();
    }
}
