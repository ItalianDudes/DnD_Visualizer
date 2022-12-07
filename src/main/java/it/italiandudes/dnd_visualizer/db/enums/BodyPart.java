package it.italiandudes.dnd_visualizer.db.enums;

import java.util.Arrays;

public enum BodyPart { //TODO: Finish adding Body Parts

    //Enum
    HEAD("Head");

    //Attributes
    private final String part;
    public static final String[] BODY_PART_STRING = Arrays.stream(BodyPart.values()).map(Enum::name).toArray(String[]::new);

    //Constructors
    BodyPart(String part){
        this.part = part;
    }

    //Methods
    @Override
    public String toString() {
        return part;
    }



}
