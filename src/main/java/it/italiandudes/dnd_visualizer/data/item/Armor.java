package it.italiandudes.dnd_visualizer.data.item;

import it.italiandudes.dnd_visualizer.data.enums.ArmorSlot;
import it.italiandudes.dnd_visualizer.data.enums.ArmorWeightCategory;
import it.italiandudes.dnd_visualizer.data.enums.EquipmentType;
import it.italiandudes.dnd_visualizer.db.DBManager;
import it.italiandudes.dnd_visualizer.interfaces.ISavable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("unused")
public final class Armor extends Equipment implements ISavable {

    // Attributes
    @Nullable private Integer armorID;
    @NotNull private ArmorSlot slot;
    @NotNull private ArmorWeightCategory weightCategory;

    // Constructors
    public Armor(@NotNull final ArmorSlot slot) {
        super(EquipmentType.ARMOR);
        this.slot = slot;
        this.weightCategory = ArmorWeightCategory.LIGHT;
    }
    public Armor(@NotNull final Item item, final int equipmentID, final int armorID, @NotNull final ArmorSlot slot,
                 final int lifeEffect, final double lifeEffectPerc, final int loadEffect, final double loadEffectPerc,
                 final int caEffect, @Nullable final String otherEffects, @NotNull final ArmorWeightCategory weightCategory, final boolean isEquipped) {
        super(item, EquipmentType.ARMOR, lifeEffect, lifeEffectPerc, caEffect, loadEffect, loadEffectPerc, otherEffects, isEquipped);
        this.slot = slot;
        this.setEquipmentID(equipmentID);
        this.armorID = armorID;
        this.weightCategory = weightCategory;
    }
    public Armor(@NotNull final Item item, @NotNull final ArmorSlot slot, final int lifeEffect, final double lifeEffectPerc,
                 final int loadEffect, final double loadEffectPerc, final int caEffect, @Nullable final String otherEffects,
                 @NotNull final ArmorWeightCategory weightCategory, final boolean isEquipped) {
        super(item, EquipmentType.ARMOR, lifeEffect, lifeEffectPerc, caEffect, loadEffect, loadEffectPerc, otherEffects, isEquipped);
        this.slot = slot;
        this.weightCategory = weightCategory;
    }
    public Armor(@NotNull final String armorName) throws SQLException {
        super(armorName);
        String query = "SELECT * FROM armors WHERE equipment_id=?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("The database is not connected");
        assert getEquipmentID()!=null;
        ps.setInt(1, getEquipmentID());
        ResultSet resultSet = ps.executeQuery();
        if (resultSet.next()) {
            this.armorID = resultSet.getInt("id");
            this.slot = ArmorSlot.values()[resultSet.getInt("slot")+1];
            this.weightCategory = ArmorWeightCategory.values()[resultSet.getInt("weight_category")];
            ps.close();
        } else {
            ps.close();
            throw new SQLException("Exist the equipment, but not the armor");
        }
    }

    // Methods
    @Override
    public void saveIntoDatabase(@Nullable final String oldName) throws SQLException {
        super.saveIntoDatabase(oldName);
        Integer equipmentID = getEquipmentID();
        assert equipmentID != null;
        if (armorID == null) { // Insert
            String query = "INSERT INTO armors (equipment_id, slot, weight_category) VALUES (?, ?, ?);";
            PreparedStatement ps = DBManager.preparedStatement(query);
            if (ps == null) throw new SQLException("The database is not connected");
            ps.setInt(1, equipmentID);
            ps.setInt(2, slot.getDatabaseValue());
            ps.setInt(3, weightCategory.getDatabaseValue());
            ps.executeUpdate();
            ps.close();
            query = "SELECT id FROM armors WHERE equipment_id=?;";
            ps = DBManager.preparedStatement(query);
            if (ps == null) throw new SQLException("The database is not connected");
            ps.setInt(1, equipmentID);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                setArmorID(resultSet.getInt("id"));
                ps.close();
            } else {
                ps.close();
                throw new SQLException("Something strange happened on armor insert! Armor insert but doesn't result on select");
            }
        } else { // Update
            String query = "UPDATE armors SET slot=?, weight_category=? WHERE id=?;";
            PreparedStatement ps = DBManager.preparedStatement(query);
            if (ps == null) throw new SQLException("The database is not connected");
            ps.setInt(1, slot.getDatabaseValue());
            ps.setInt(2, weightCategory.getDatabaseValue());
            ps.setInt(3, armorID);
            ps.executeUpdate();
            ps.close();
        }
    }
    @Nullable
    public Integer getArmorID() {
        return armorID;
    }

    public void setArmorID(final int armorID) {
        if (this.armorID == null) this.armorID = armorID;
    }
    @NotNull
    public ArmorSlot getSlot() {
        return slot;
    }
    public void setSlot(@NotNull final ArmorSlot slot) {
        this.slot = slot;
    }
    @NotNull
    public ArmorWeightCategory getWeightCategory() {
        return weightCategory;
    }
    public void setWeightCategory(@NotNull final ArmorWeightCategory weightCategory) {
        this.weightCategory = weightCategory;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Armor)) return false;
        if (!super.equals(o)) return false;

        Armor armor = (Armor) o;

        if (getArmorID() != null ? !getArmorID().equals(armor.getArmorID()) : armor.getArmorID() != null) return false;
        if (getSlot() != armor.getSlot()) return false;
        return weightCategory == armor.weightCategory;
    }
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getArmorID() != null ? getArmorID().hashCode() : 0);
        result = 31 * result + getSlot().hashCode();
        result = 31 * result + weightCategory.hashCode();
        return result;
    }
}
