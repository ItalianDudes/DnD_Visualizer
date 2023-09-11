package it.italiandudes.dnd_visualizer.data.enums;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

@SuppressWarnings("unused")
public enum DiceRepresentation {
    COIN(2, "Moneta"),
    D4(4, "4 Facce"),
    D6(6, "6 Facce"),
    D8(8, "8 Facce"),
    D10(10, "10 Facce"),
    D12(12, "12 Facce"),
    D20(20, "20 Facce"),
    D100(100, "100 Facce")
    ;

    // Attributes
    @NotNull
    public static final ArrayList<DiceRepresentation> DICES = new ArrayList<>();
    static {
        DICES.addAll(Arrays.asList(DiceRepresentation.values()));
    }
    private final int faces;
    private final String showedText;

    // Constructors
    DiceRepresentation(final int faces, final String showedText) {
        this.faces = faces;
        this.showedText = showedText;
    }

    // Methods
    public int getFaces() {
        return faces;
    }
    public String getShowedText() {
        return showedText;
    }
}
