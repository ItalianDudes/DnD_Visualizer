package it.italiandudes.dnd_visualizer.data.item;

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
public final class Weapon extends Equipment implements ISavable {

    // Attributes
    @Nullable private Integer weaponID;
    @Nullable private String weaponCategory;
    @Nullable private String properties;

    // Constructors
    public Weapon() {
        super(EquipmentType.WEAPON);
    }
    public Weapon(@NotNull final Item item, final int equipmentID, final int weaponID, @Nullable final String weaponCategory,
                  @Nullable final String properties, final int lifeEffect, final double lifeEffectPerc, final int loadEffect,
                  final double loadEffectPerc, final int caEffect, @Nullable final String otherEffects) {
        super(item, EquipmentType.WEAPON, lifeEffect, lifeEffectPerc, caEffect, loadEffect, loadEffectPerc, otherEffects);
        this.weaponCategory = weaponCategory;
        this.properties = properties;
        this.setEquipmentID(equipmentID);
        this.weaponID = weaponID;
    }
    public Weapon(@NotNull final Item item, @Nullable final String weaponCategory, @Nullable final String properties,
                  final int lifeEffect, final double lifeEffectPerc, final int loadEffect, final double loadEffectPerc,
                  final int caEffect, @Nullable final String otherEffects) {
        super(item, EquipmentType.WEAPON, lifeEffect, lifeEffectPerc, caEffect, loadEffect, loadEffectPerc, otherEffects);
        this.weaponCategory = weaponCategory;
        this.properties = properties;
    }
    public Weapon(@NotNull final String weaponName) throws SQLException {
        super(weaponName);
        String query = "SELECT * FROM weapons WHERE equipment_id=?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("The database is not connected");
        assert getEquipmentID() != null;
        ps.setInt(1, getEquipmentID());
        ResultSet result = ps.executeQuery();
        if (result.next()) {
            this.weaponID = result.getInt("id");
            this.weaponCategory = result.getString("category");
            this.weaponID = result.getInt("id");
            this.properties = result.getString("properties");
            ps.close();
        } else {
            ps.close();
            throw new SQLException("Exist the equipment, but not the weapon");
        }
    }

    // Methods
    @Override
    public void saveIntoDatabase(@Nullable final String oldName) throws SQLException {
        super.saveIntoDatabase(oldName);
        Integer equipmentID = getEquipmentID();
        assert equipmentID != null;
        if (weaponID == null) { // Insert
            String query = "INSERT INTO weapons (equipment_id, category, properties) VALUES (?, ?, ?);";
            PreparedStatement ps = DBManager.preparedStatement(query);
            if (ps == null) throw new SQLException("The database is not connected");
            ps.setInt(1, equipmentID);
            ps.setString(2, getWeaponCategory());
            ps.setString(3, getProperties());
            ps.executeUpdate();
            ps.close();
            query = "SELECT id FROM weapons WHERE equipment_id = ?;";
            ps = DBManager.preparedStatement(query);
            if (ps == null) throw new SQLException("The database is not connected");
            ps.setInt(1, equipmentID);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                setWeaponID(resultSet.getInt("id"));
                ps.close();
            } else {
                ps.close();
                throw new SQLException("Something strange happened on weapon insert! Weapon insert but doesn't result on select");
            }
        } else { // Update
            String query = "UPDATE weapons SET category=?, properties=? WHERE id=?;";
            PreparedStatement ps = DBManager.preparedStatement(query);
            if (ps == null) throw new SQLException("The database is not connected");
            ps.setString(1, getWeaponCategory());
            ps.setString(2, getProperties());
            ps.setInt(3, getWeaponID());
            ps.executeUpdate();
            ps.close();
        }
    }
    @Nullable
    public Integer getWeaponID() {
        return weaponID;
    }
    public void setWeaponID(final int weaponID) {
        if (this.weaponID == null) this.weaponID = weaponID;
    }
    @Nullable
    public String getWeaponCategory() {
        return weaponCategory;
    }
    public void setCategory(@Nullable final String weaponCategory) {
        this.weaponCategory = weaponCategory;
    }
    @Nullable
    public String getProperties() {
        return properties;
    }
    public void setProperties(@Nullable final String properties) {
        this.properties = properties;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Weapon)) return false;
        if (!super.equals(o)) return false;
        Weapon weapon = (Weapon) o;
        return Objects.equals(getWeaponID(), weapon.getWeaponID()) && Objects.equals(getWeaponCategory(), weapon.getWeaponCategory()) && Objects.equals(getProperties(), weapon.getProperties());
    }
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getWeaponID(), getWeaponCategory(), getProperties());
    }
}
