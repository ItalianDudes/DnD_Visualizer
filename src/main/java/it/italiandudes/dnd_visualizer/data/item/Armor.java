package it.italiandudes.dnd_visualizer.data.item;

import it.italiandudes.dnd_visualizer.data.enums.ArmorSlot;
import it.italiandudes.dnd_visualizer.data.enums.EquipmentType;
import it.italiandudes.dnd_visualizer.db.DBManager;
import it.italiandudes.dnd_visualizer.interfaces.ISavable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

@SuppressWarnings("unused")
public final class Armor extends Equipment implements ISavable {

    // Attributes
    @Nullable private Integer armorID;
    @NotNull private ArmorSlot slot;

    // Constructors
    public Armor(@NotNull final ArmorSlot slot) {
        super(EquipmentType.ARMOR);
        this.slot = slot;
    }
    public Armor(@NotNull final Item item, @NotNull final ArmorSlot slot, final int lifeEffect, final double lifeEffectPerc,
                 final int loadEffect, final double loadEffectPerc, final int caEffect, @Nullable final String otherEffects) {
        super(item, EquipmentType.ARMOR, lifeEffect, lifeEffectPerc, caEffect, loadEffect, loadEffectPerc, otherEffects);
        this.slot = slot;
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
            this.slot = ArmorSlot.values()[resultSet.getInt("slot")];
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
            String query = "INSERT INTO armors (equipment_id, slot) VALUES (?, ?);";
            PreparedStatement ps = DBManager.preparedStatement(query);
            if (ps == null) throw new SQLException("The database is not connected");
            ps.setInt(1, equipmentID);
            ps.setInt(2, slot.getDatabaseValue());
            ps.executeUpdate();
            ps.close();
            query = "SELECT id FROM armors WHERE equipment_id = ?;";
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
            String query = "UPDATE armors SET equipment_id=?, slot=? WHERE id=?;";
            PreparedStatement ps = DBManager.preparedStatement(query);
            if (ps == null) throw new SQLException("The database is not connected");
            ps.setInt(1, equipmentID);
            ps.setInt(2, slot.getDatabaseValue());
            ps.setInt(3, getArmorID());
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
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Armor)) return false;
        if (!super.equals(o)) return false;
        Armor armor = (Armor) o;
        return Objects.equals(getArmorID(), armor.getArmorID()) && getSlot() == armor.getSlot();
    }
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getArmorID(), getSlot());
    }
}
