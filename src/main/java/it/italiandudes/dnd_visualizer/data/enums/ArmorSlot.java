package it.italiandudes.dnd_visualizer.data.enums;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public enum ArmorSlot {
    NO_ARMOR(-1, ""),
    FULL_SET(0, "Set Completo"),
    HEAD(1, "Testa"),
    LEFT_SHOULDER(2, "Spalla SX"),
    RIGHT_SHOULDER(3, "Spalla DX"),
    LEFT_ARM(4, "Braccio SX"),
    RIGHT_ARM(5, "Braccio DX"),
    LEFT_FOREARM(6, "Avambraccio SX"),
    RIGHT_FOREARM(7, "Avambraccio DX"),
    LEFT_HAND(8, "Mano SX"),
    RIGHT_HAND(9, "Mano DX"),
    CHEST(10, "Petto"),
    ABDOMEN(11, "Addome"),
    BACK(12, "Schiena"),
    MANTLE(13, "Mantello"),
    LEFT_LEG(14, "Gamba SX"),
    RIGHT_LEG(15, "Gamba DX"),
    LEFT_KNEE(16, "Ginocchio SX"),
    RIGHT_KNEE(17, "Ginocchio DX"),
    LEFT_FOOT(18, "Piede SX"),
    RIGHT_FOOT(19, "Piede DX"),
    LEFT_BRACELET(20, "Bracciale SX"),
    RIGHT_BRACELET(21, "Bracciale DX"),
    LEFT_EARRING(22, "Orecchino SX"),
    RIGHT_EARRING(23, "Orecchino DX"),
    NECKLACE(24, "Collana"),
    RING_1(25, "Anello 1"),
    RING_2(26, "Anello 2"),
    RING_3(27, "Anello 3"),
    RING_4(28, "Anello 4"),
    ;

    // Attributes
    @NotNull
    public static final ArrayList<ArmorSlot> armorSlots = new ArrayList<>();
    static {
        for (ArmorSlot slot : ArmorSlot.values()) {
            if (slot.databaseValue >= 0) armorSlots.add(slot);
        }
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
