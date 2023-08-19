package it.italiandudes.dnd_visualizer.data;

import it.italiandudes.dnd_visualizer.data.enums.Category;
import it.italiandudes.dnd_visualizer.data.enums.Rarity;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@SuppressWarnings("unused")
public final class ElementPreview {

    // Attributes
    private final int id;
    private final String name;
    private final Category category;
    private final Rarity rarity;
    private final double weight;
    private final int costCopper;

    // Constructors
    public ElementPreview(final int id, @NotNull final String name, final Category category, final Rarity rarity, final double weight, final int costCopper) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.costCopper = costCopper;
        this.rarity = rarity;
        this.weight = weight;
    }

    // Methods
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public Category getCategory() {
        return category;
    }
    public Rarity getRarity() {
        return rarity;
    }
    public String getRarityColor() {
        int red = (int)(this.rarity.getColor().getRed()*255);
        int green = (int)(this.rarity.getColor().getGreen()*255);
        int blue = (int)(this.rarity.getColor().getBlue()*255);
        return String.format("#%02X%02X%02X", red, green, blue);
    }
    public double getWeight() {
        return weight;
    }
    public int getCostCopper() {
        return costCopper;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElementPreview that = (ElementPreview) o;
        return id == that.id && Double.compare(weight, that.weight) == 0 && costCopper == that.costCopper && Objects.equals(name, that.name) && category == that.category && rarity == that.rarity;
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, name, category, rarity, weight, costCopper);
    }
    @Override
    public String toString() {
        return name;
    }
}
