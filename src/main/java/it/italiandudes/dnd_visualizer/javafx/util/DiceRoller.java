package it.italiandudes.dnd_visualizer.javafx.util;

import java.util.Random;
import java.util.stream.IntStream;

@SuppressWarnings("unused")
public final class DiceRoller {

    // Attributes
    private static final Random RANDOMIZER = new Random();

    // Methods
    public static int rollD4() {
        return RANDOMIZER.nextInt(4) + 1;
    }
    public static int[] rollMultipleD4(final int amount) {
        if (amount < 1) throw new RuntimeException("The amount must be greater than 0");
        return IntStream.generate(() -> RANDOMIZER.nextInt(4) + 1)
                .limit(amount)
                .toArray();
    }
    public static int rollD6() {
        return RANDOMIZER.nextInt(6) + 1;
    }
    public static int[] rollMultipleD6(final int amount) {
        if (amount < 1) throw new RuntimeException("The amount must be greater than 0");
        return IntStream.generate(() -> RANDOMIZER.nextInt(6) + 1)
                .limit(amount)
                .toArray();
    }
    public static int rollD8() {
        return RANDOMIZER.nextInt(8) + 1;
    }
    public static int[] rollMultipleD8(final int amount) {
        if (amount < 1) throw new RuntimeException("The amount must be greater than 0");
        return IntStream.generate(() -> RANDOMIZER.nextInt(8) + 1)
                .limit(amount)
                .toArray();
    }
    public static int rollD10() {
        return RANDOMIZER.nextInt(10) + 1;
    }
    public static int[] rollMultipleD10(final int amount) {
        if (amount < 1) throw new RuntimeException("The amount must be greater than 0");
        return IntStream.generate(() -> RANDOMIZER.nextInt(10) + 1)
                .limit(amount)
                .toArray();
    }
    public static int rollD12() {
        return RANDOMIZER.nextInt(12) + 1;
    }
    public static int[] rollMultipleD12(final int amount) {
        if (amount < 1) throw new RuntimeException("The amount must be greater than 0");
        return IntStream.generate(() -> RANDOMIZER.nextInt(12) + 1)
                .limit(amount)
                .toArray();
    }
    public static int rollD20() {
        return RANDOMIZER.nextInt(20) + 1;
    }
    public static int[] rollMultipleD20(final int amount) {
        if (amount < 1) throw new RuntimeException("The amount must be greater than 0");
        return IntStream.generate(() -> RANDOMIZER.nextInt(20) + 1)
                .limit(amount)
                .toArray();
    }
    public static int rollD100() {
        return RANDOMIZER.nextInt(100) + 1;
    }
    public static int[] rollMultipleD100(final int amount) {
        if (amount < 1) throw new RuntimeException("The amount must be greater than 0");
        return IntStream.generate(() -> RANDOMIZER.nextInt(100) + 1)
                .limit(amount)
                .toArray();
    }
    public static int rollD1000() {
        return RANDOMIZER.nextInt(1000) + 1;
    }
    public static int[] rollMultipleD1000(final int amount) {
        if (amount < 1) throw new RuntimeException("The amount must be greater than 0");
        return IntStream.generate(() -> RANDOMIZER.nextInt(1000) + 1)
                .limit(amount)
                .toArray();
    }
    public static int rollNumber(final int max) {
        if (max <= 1) throw new RuntimeException("The max must be greater than 1");
        return RANDOMIZER.nextInt(max) + 1;
    }
    public static int[] rollMultipleNumbers(final int amount, final int max) {
        if (max <= 1) throw new RuntimeException("The max must be greater than 1");
        if (amount < 1) throw new RuntimeException("The amount must be greater than 0");
        return IntStream.generate(() -> RANDOMIZER.nextInt(max) + 1)
                .limit(amount)
                .toArray();
    }
}
