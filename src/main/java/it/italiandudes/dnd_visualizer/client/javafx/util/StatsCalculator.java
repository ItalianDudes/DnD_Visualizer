package it.italiandudes.dnd_visualizer.client.javafx.util;

public final class StatsCalculator {

    public static int calculateModifier(String statString) {
        return calculateModifier(Integer.parseInt(statString));
    }
    public static int calculateModifier(int stat) {
        return (int) Math.floor((stat-10.0)/2.0);
    }

}
