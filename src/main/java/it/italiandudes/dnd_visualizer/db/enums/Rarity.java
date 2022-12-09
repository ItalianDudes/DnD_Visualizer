package it.italiandudes.dnd_visualizer.db.enums;

import javafx.scene.paint.Color;

import java.util.Arrays;

@SuppressWarnings("unused")
public enum Rarity {
    COMMON(255,255,255,75),
    UNCOMMON(0,255,0,75),
    RARE(77,145,255,75),
    LEGENDARY(169,93,245,75),
    EXOTIC(255,234,0,75);

    //Attributes
    public static final String[] RARITY_STRING = Arrays.stream(Rarity.values()).map(Enum::name).toArray(String[]::new);
    private final Color color;

    //Constructors
    Rarity(int redHex, int greenHex, int blueHex, int opacity){
        this.color = new Color(redHex/255.0, greenHex/255.0, blueHex/255.0, opacity/100.0);
    }

    //Methods
    public Color getColor(){
        return color;
    }
}
