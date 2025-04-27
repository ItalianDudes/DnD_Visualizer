package it.italiandudes.dnd_visualizer.data.bags;

import it.italiandudes.dnd_visualizer.data.enums.AddonSlot;
import it.italiandudes.dnd_visualizer.data.enums.Category;
import it.italiandudes.dnd_visualizer.data.enums.Rarity;
import it.italiandudes.dnd_visualizer.data.item.Addon;
import it.italiandudes.dnd_visualizer.data.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public final class DnDBag {

    // Constants
    public static final Addon BAG = new Addon(new Item(null, null, null, "Zaino", 200, null, Rarity.COMMON, 2.5, Category.EQUIPMENT, 1), AddonSlot.BACKPACK, 0, 0, 0, 0, 0, null, false);

    // Attributes
    @NotNull private final String name;
    @NotNull private final ArrayList<Item> items;
    private final boolean includeBag;

    // Constructors
    public DnDBag(@NotNull final String name, @NotNull final ArrayList<Item> items, final boolean includeBag) {
        this.name = name;
        this.items = items;
        this.includeBag = includeBag;
    }
    public DnDBag(@NotNull final String name, final boolean includeBag, Item... items) {
        this(name, new ArrayList<>(Arrays.asList(items)), includeBag);
    }

    // Methods
    public @NotNull String getName() {
        return name;
    }
    public @NotNull ArrayList<Item> getItems() {
        return items;
    }
    public boolean isIncludeBag() {
        return includeBag;
    }
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DnDBag dnDBag)) return false;
        return isIncludeBag() == dnDBag.isIncludeBag() && getName().equals(dnDBag.getName()) && getItems().equals(dnDBag.getItems());
    }
    @Override
    public int hashCode() {
        int result = getName().hashCode();
        result = 31 * result + getItems().hashCode();
        result = 31 * result + Boolean.hashCode(isIncludeBag());
        return result;
    }
    @Override @NotNull
    public String toString() {
        return getName();
    }
}
