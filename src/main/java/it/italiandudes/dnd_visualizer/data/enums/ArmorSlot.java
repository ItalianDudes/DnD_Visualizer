package it.italiandudes.dnd_visualizer.data.enums;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public enum ArmorSlot {
    FULL_SET(0, "Set Completo"),
    HEAD(1, "Testa"),
    LEFT_ARM(2, "Braccio SX"),
    RIGHT_ARM(3, "Braccio DX"),
    LEFT_HAND(4, "Mano SX"),
    RIGHT_HAND(5, "Mano DX"),
    CHEST(6, "Petto"),
    BACK(7, "Schiena"),
    LEFT_LEG(8, "Gamba SX"),
    RIGHT_LEG(9, "Gamba DX"),
    LEFT_FOOT(10, "Piede SX"),
    RIGHT_FOOT(11, "Piede DX"),
    LEFT_BRACELET(12, "Bracciale SX"),
    RIGHT_BRACELET(13, "Bracciale DX"),
    RING_1(14, "Anello 1"),
    RING_2(15, "Anello 2"),
    RING_3(16, "Anello 3"),
    RING_4(17, "Anello 4"),
    NECKLACE(18, "Collana")
    ;

    // Attributes
    @NotNull
    public static final ArrayList<ArmorSlot> armorSlots = new ArrayList<>();
    static {
        armorSlots.addAll(Arrays.asList(ArmorSlot.values()));
    }
    private final int databaseValue;
    private final String name;

    // Constructors
    ArmorSlot(final int databaseValue, final String name) {
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
