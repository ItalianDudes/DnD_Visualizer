package it.italiandudes.dnd_visualizer.utils;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

@SuppressWarnings("unused")
public final class Randomizer {

    // Attributes
    @NotNull private static final Random RANDOMIZER = new Random();

    // Methods
    @NotNull
    public static Random getRandomizer() {
        return RANDOMIZER;
    }
    public static int randomBetween(final int min, final int max) {
        return RANDOMIZER.nextInt((max + 1) - min) + min;
    }
}
