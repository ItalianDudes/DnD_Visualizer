package it.italiandudes.dnd_visualizer.db.enums;

import java.util.Arrays;

@SuppressWarnings("unused")
public enum Rarity {
    COMMON,
    UNCOMMON,
    RARE,
    LEGENDARY,
    EXOTIC;

    public static final String[] RARITY_STRING = Arrays.stream(Rarity.values()).map(Enum::name).toArray(String[]::new);
}
