package it.italiandudes.dnd_visualizer.data.enums;

@SuppressWarnings("unused")
public enum Category {
    ITEM(0, "Oggetto"),
    EQUIPMENT(1, "Equipaggiamento"),
    SPELL(2, "Incantesimo");

    // Attributes
    private final int databaseValue;
    private final String name;

    // Constructors
    Category(final int databaseValue, final String name) {
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
