package it.italiandudes.dnd_visualizer.db;

import it.italiandudes.dnd_visualizer.db.enums.Rarity;
import it.italiandudes.idl.common.Logger;
import it.italiandudes.idl.common.RawSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.util.Base64;

@SuppressWarnings("unused")
public abstract class DBElement implements Serializable {

    //Attributes
    @NotNull private String name;
    @Nullable private String lore;
    @NotNull private Rarity rarity;

    //Constructors
    protected DBElement(@NotNull String name, @Nullable String lore, @NotNull Rarity rarity){
        this.name = name;
        this.lore = lore;
        this.rarity = rarity;
    }
    protected DBElement(@NotNull String name, @Nullable String lore){
        this(name, lore, Rarity.COMMON);
    }
    protected DBElement(@NotNull String name, @NotNull Rarity rarity){
        this(name, null, rarity);
    }
    protected DBElement(@NotNull String name){
        this(name, null, Rarity.COMMON);
    }

    //Methods
    @NotNull public String getName() {
        return name;
    }
    public void setName(@NotNull String name) {
        this.name = name;
    }
    @Nullable public String getLore() {
        return lore;
    }
    public void setLore(@Nullable String lore) {
        if(lore == null) lore = "";
        this.lore = lore;
    }
    @NotNull public Rarity getRarity() {
        return rarity;
    }
    public void setRarity(@NotNull Rarity rarity) {
        this.rarity = rarity;
    }
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public abstract boolean writeIntoDB(@NotNull Connection dbConnection);
    public String getBase64() {
        ByteArrayOutputStream objByte = new ByteArrayOutputStream();
        try {
            RawSerializer.sendObject(objByte, this);
        }catch (IOException e){
            Logger.log(e);
            return null;
        }
        return Base64.getEncoder().encodeToString(objByte.toByteArray());
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DBElement dbElement = (DBElement) o;

        if (!getName().equals(dbElement.getName())) return false;
        if (getLore() != null ? !getLore().equals(dbElement.getLore()) : dbElement.getLore() != null) return false;
        return getRarity() == dbElement.getRarity();
    }
    @Override
    public int hashCode() {
        int result = getName().hashCode();
        result = 31 * result + (getLore() != null ? getLore().hashCode() : 0);
        result = 31 * result + getRarity().hashCode();
        return result;
    }
    @Override
    public String toString() {
        return "DBElement{" +
                "name='" + name + '\'' +
                ", lore='" + lore + '\'' +
                ", rarity=" + rarity +
                '}';
    }
}
