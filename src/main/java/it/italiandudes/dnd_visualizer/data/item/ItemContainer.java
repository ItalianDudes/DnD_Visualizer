package it.italiandudes.dnd_visualizer.data.item;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class ItemContainer {

    // Attributes
    @Nullable private Item item = null;

    // Constructors
    public ItemContainer() {}
    public ItemContainer(@NotNull final Item item) {
        this.item = item;
    }

    // Methods
    @Nullable
    public Item getItem() {
        return item;
    }
    public void setItem(@Nullable final Item item) {
        this.item = item;
    }
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ItemContainer that)) return false;
        return Objects.equals(item, that.item);
    }
    @Override
    public int hashCode() {
        return Objects.hashCode(item);
    }
}
