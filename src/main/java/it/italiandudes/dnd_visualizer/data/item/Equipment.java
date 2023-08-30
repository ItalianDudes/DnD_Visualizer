package it.italiandudes.dnd_visualizer.data.item;

import it.italiandudes.dnd_visualizer.data.enums.Category;
import it.italiandudes.dnd_visualizer.data.enums.EquipmentType;
import it.italiandudes.dnd_visualizer.db.DBManager;
import it.italiandudes.dnd_visualizer.interfaces.ISavable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("unused")
public class Equipment extends Item implements ISavable {

    // Attributes
    @Nullable private Integer equipmentID;
    @NotNull private final EquipmentType type;
    private int lifeEffect = 0;
    private double lifePercentageEffect = 0;
    private int caEffect = 0;
    private int loadEffect = 0;
    private double loadPercentageEffect = 0;
    @Nullable private String otherEffects = null;

    // Constructors
    public Equipment(@NotNull final EquipmentType type) {
        super(Category.EQUIPMENT);
        this.type = type;
    }
    public Equipment(@NotNull Item item, @NotNull final EquipmentType type, final int lifeEffect, final double lifePercentageEffect,
                     final int caEffect, final int loadEffect, final double loadPercentageEffect, @Nullable final String otherEffects) {
        super(item);
        this.type = type;
        this.lifeEffect = lifeEffect;
        this.lifePercentageEffect = lifePercentageEffect;
        this.caEffect = caEffect;
        this.loadEffect = loadEffect;
        this.loadPercentageEffect = loadPercentageEffect;
        this.otherEffects = otherEffects;
    }
    public Equipment(@NotNull final String name) throws SQLException {
        super(name);
        String query = "SELECT * FROM equipments WHERE item_id=?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("The database is not connected");
        Integer itemID = getItemID();
        assert itemID != null;
        ps.setInt(1, itemID);
        ResultSet result = ps.executeQuery();
        if (result.next()) {
            this.equipmentID = result.getInt("id");
            this.type = EquipmentType.values()[result.getInt("type")];
            this.lifeEffect = result.getInt("life_effect");
            this.lifePercentageEffect = result.getDouble("life_percentage_effect");
            this.caEffect = result.getInt("ca_effect");
            this.loadEffect = result.getInt("load_effect");
            this.loadPercentageEffect = result.getDouble("load_percentage_effect");
            this.otherEffects = result.getString("other_effects");
            ps.close();
        } else {
            ps.close();
            throw new SQLException("Exist the item, but not the equipment");
        }
    }

    // Methods
    @Override
    public void saveIntoDatabase(@Nullable String oldName) throws SQLException {
        super.saveIntoDatabase(oldName);
        Integer itemID = getItemID();
        assert itemID != null;
        if (equipmentID == null) { // Insert
            String query = "INSERT INTO equipments (item_id, type, life_effect, life_percentage_effect, ca_effect, load_effect, load_percentage_effect, other_effects) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement ps = DBManager.preparedStatement(query);
            if (ps == null) throw new SQLException("The database is not connected");
            ps.setInt(1, itemID);
            ps.setInt(2, getType().getDatabaseValue());
            ps.setInt(3, getLifeEffect());
            ps.setDouble(4, getLifePercentageEffect());
            ps.setInt(5, getCaEffect());
            ps.setInt(6, getLoadEffect());
            ps.setDouble(7, getLoadPercentageEffect());
            ps.setString(8, getOtherEffects());
            ps.executeUpdate();
            ps.close();
            query = "SELECT id FROM equipments WHERE item_id = ?;";
            ps = DBManager.preparedStatement(query);
            if (ps == null) throw new SQLException("The database is not connected");
            ps.setInt(1, itemID);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                setEquipmentID(resultSet.getInt("id"));
                ps.close();
            } else {
                ps.close();
                throw new SQLException("Something strange happened on equipment insert! Equipment insert but doesn't result on select");
            }
        } else { // Update
            assert getEquipmentID()!=null;
            String query = "UPDATE equipments SET item_id=?, type=?, life_effect=?, life_percentage_effect=?, ca_effect=?, load_effect=?, load_percentage_effect=?, other_effects=? WHERE id=?;";
            PreparedStatement ps = DBManager.preparedStatement(query);
            if (ps == null) throw new SQLException("The database is not connected");
            ps.setInt(1, itemID);
            ps.setInt(2, getType().getDatabaseValue());
            ps.setInt(3, getLifeEffect());
            ps.setDouble(4, getLifePercentageEffect());
            ps.setInt(5, getCaEffect());
            ps.setInt(6, getLoadEffect());
            ps.setDouble(7, getLoadPercentageEffect());
            ps.setString(8, getOtherEffects());
            ps.setInt(9, getEquipmentID());
            ps.executeUpdate();
            ps.close();
        }
    }
    @Nullable
    public Integer getEquipmentID() {
        return equipmentID;
    }
    public void setEquipmentID(final int equipmentID) {
        if (this.equipmentID == null) this.equipmentID = equipmentID;
    }
    @NotNull
    public EquipmentType getType() {
        return type;
    }
    public int getLifeEffect() {
        return lifeEffect;
    }
    public void setLifeEffect(final int lifeEffect) {
        this.lifeEffect = lifeEffect;
    }
    public double getLifePercentageEffect() {
        return lifePercentageEffect;
    }
    public void setLifePercentageEffect(final double lifePercentageEffect) {
        this.lifePercentageEffect = lifePercentageEffect;
    }
    public int getCaEffect() {
        return caEffect;
    }
    public void setCaEffect(final int caEffect) {
        this.caEffect = caEffect;
    }
    public int getLoadEffect() {
        return loadEffect;
    }
    public void setLoadEffect(final int loadEffect) {
        this.loadEffect = loadEffect;
    }
    public double getLoadPercentageEffect() {
        return loadPercentageEffect;
    }
    public void setLoadPercentageEffect(final double loadPercentageEffect) {
        this.loadPercentageEffect = loadPercentageEffect;
    }
    @Nullable
    public String getOtherEffects() {
        return otherEffects;
    }
    public void setOtherEffects(@Nullable final String otherEffects) {
        this.otherEffects = otherEffects;
    }
}
