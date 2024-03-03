package it.italiandudes.dnd_visualizer.data.enums;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public enum PrivilegeType {
    PRIVILEGE(0, "Privilegio"),
    TRAIT(1, "Tratto");

    // Attributes
    @NotNull public static final ArrayList<PrivilegeType> privilege_types = new ArrayList<>();
    static {
        privilege_types.addAll(Arrays.asList(PrivilegeType.values()));
    }
    private final int databaseValue;
    private final String name;

    // Constructors
    PrivilegeType(final int databaseValue, final String name) {
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
