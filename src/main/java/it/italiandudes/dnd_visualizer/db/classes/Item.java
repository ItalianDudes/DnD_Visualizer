package it.italiandudes.dnd_visualizer.db.classes;

import it.italiandudes.dnd_visualizer.DnD_Visualizer;
import it.italiandudes.dnd_visualizer.db.DBDefs;
import it.italiandudes.dnd_visualizer.db.DBElement;
import it.italiandudes.dnd_visualizer.db.enums.Coin;
import it.italiandudes.dnd_visualizer.db.enums.Rarity;
import it.italiandudes.idl.common.Logger;
import it.italiandudes.idl.common.SQLiteHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("unused")
public class Item extends DBElement {

    //Attributes
    private int requiredLevel;
    @Nullable private String requiredKnowledge;
    @NotNull private final Cost cost;
    private double weight;
    private int itemID;
    @Nullable private String properties;

    //Constructors
    public Item(@NotNull String name, @Nullable String lore, @NotNull Rarity rarity, int requiredLevel, @Nullable String requiredKnowledge, @NotNull Cost cost, double weight, @Nullable String properties) {
        super(name, lore, rarity);
        itemID = -1;
        if(requiredLevel<1) throw new RuntimeException("\"Required Level\" must be equal or greater than 1");
        this.requiredLevel = requiredLevel;
        this.requiredKnowledge = requiredKnowledge;
        this.cost = cost;
        this.weight = weight;
        this.properties = properties;
    }
    public Item(int itemID, @NotNull String name, @Nullable String lore, @NotNull Rarity rarity, int requiredLevel, @Nullable String requiredKnowledge, @NotNull Cost cost, double weight, @Nullable String properties) {
        super(name, lore, rarity);
        this.itemID = itemID;
        if(requiredLevel<1) throw new RuntimeException("\"Required Level\" must be equal or greater than 1");
        this.requiredLevel = requiredLevel;
        this.requiredKnowledge = requiredKnowledge;
        this.cost = cost;
        this.weight = weight;
        this.properties = properties;
    }
    public Item(@NotNull Item item){
        super(item.getName(), item.getLore(), item.getRarity());
        itemID = item.getItemID();
        requiredLevel = item.requiredLevel;
        requiredKnowledge = item.requiredKnowledge;
        cost = item.cost;
        weight = item.weight;
        properties = item.properties;
    }
    public Item(@NotNull Connection dbConnection, String name) throws SQLException {
        super(name);
        itemID = -1;
        String query = "SELECT * FROM " + DBDefs.DB_TABLE_ITEMS + " WHERE name LIKE '"+name+"'";
        ResultSet resultSet = SQLiteHandler.readDataFromDB(dbConnection, query);
        itemID = resultSet.getInt(1);
        setName(resultSet.getString(2));
        setLore(resultSet.getString(3));
        requiredLevel = resultSet.getInt(4);
        requiredKnowledge = resultSet.getString(5);
        setRarity(Rarity.valueOf(resultSet.getString(6)));
        cost = new Cost(Coin.COPPER, Integer.parseInt(resultSet.getString(7)));
        weight = resultSet.getDouble(8);
        properties = resultSet.getString(9);
    }
    public Item(@NotNull ResultSet set) throws SQLException {
        super(set.getString(2));
        itemID = -1;
        itemID = set.getInt(1);
        setLore(set.getString(3));
        requiredLevel = set.getInt(4);
        requiredKnowledge = set.getString(5);
        setRarity(Rarity.valueOf(set.getString(6)));
        cost = new Cost(Coin.COPPER, Integer.parseInt(set.getString(7)));
        weight = set.getDouble(8);
        properties = set.getString(9);

    }

    //Methods
    public boolean updateIntoDB(@NotNull Connection dbConnection){
        String query = "UPDATE "+ DBDefs.DB_TABLE_ITEMS +" SET name = ? , lore = ?, required_level = ?, required_knowledge = ?, rarity = ?, cost = ?, weight = ?, properties = ? WHERE id = ?";
        try{
            PreparedStatement statement = DnD_Visualizer.getDbConnection().prepareStatement(query);
            statement.setString(1, getName());
            statement.setString(2, getLore());
            statement.setInt(3, requiredLevel);
            statement.setString(4, requiredKnowledge);
            statement.setString(5, getRarity().name());
            statement.setString(6, cost.toString());
            statement.setDouble(7, weight);
            statement.setString(8, properties);
            statement.setInt(9, itemID);
            statement.executeUpdate();
        }catch (SQLException e){
            Logger.log(e);
            return false;
        }
        return true;
    }
    @Override
    public boolean writeIntoDB(@NotNull Connection dbConnection) {
        if(itemID==-1) return false;
        String query = "INSERT INTO "+ DBDefs.DB_TABLE_ITEMS +"(name, lore, required_level, required_knowledge, rarity, cost, weight, properties) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = SQLiteHandler.prepareDataWriteIntoDB(dbConnection, query);
        try {
            statement.setString(1, getName());
            statement.setString(2, getLore());
            statement.setInt(3, requiredLevel);
            statement.setString(4, requiredKnowledge);
            statement.setString(5, getRarity().name());
            statement.setString(6, cost.toString());
            statement.setDouble(7, weight);
            statement.setString(8, properties);
            statement.execute();
            itemID = statement.getGeneratedKeys().getInt(1);
            System.out.println("Generated Key: "+itemID);
            return true;
        }catch (SQLException e){
            Logger.log(e);
            return false;
        }
    }
    public int getRequiredLevel() {
        return requiredLevel;
    }
    public int getItemID(){
        return itemID;
    }
    public void setRequiredLevel(int requiredLevel) {
        this.requiredLevel = requiredLevel;
    }
    @Nullable public String getRequiredKnowledge() {
        return requiredKnowledge;
    }
    public void setRequiredKnowledge(@Nullable String requiredKnowledge) {
        this.requiredKnowledge = requiredKnowledge;
    }
    @NotNull public Cost getCost() {
        return cost;
    }
    public double getWeight() {
        return weight;
    }
    public void setWeight(double weight) {
        this.weight = weight;
    }
    @Nullable public String getProperties() {
        return properties;
    }
    public void setProperties(@Nullable String properties) {
        this.properties = properties;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Item item = (Item) o;

        if (getRequiredLevel() != item.getRequiredLevel()) return false;
        if (Double.compare(item.getWeight(), getWeight()) != 0) return false;
        if (getItemID() != item.getItemID()) return false;
        if (getRequiredKnowledge() != null ? !getRequiredKnowledge().equals(item.getRequiredKnowledge()) : item.getRequiredKnowledge() != null)
            return false;
        if (!getCost().equals(item.getCost())) return false;
        return getProperties() != null ? getProperties().equals(item.getProperties()) : item.getProperties() == null;
    }
    @Override
    public int hashCode() {
        int result = super.hashCode();
        long temp;
        result = 31 * result + getRequiredLevel();
        result = 31 * result + (getRequiredKnowledge() != null ? getRequiredKnowledge().hashCode() : 0);
        result = 31 * result + getCost().hashCode();
        temp = Double.doubleToLongBits(getWeight());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + getItemID();
        result = 31 * result + (getProperties() != null ? getProperties().hashCode() : 0);
        return result;
    }
    @Override
    public String toString() {
        return "Item{" +
                "requiredLevel=" + requiredLevel +
                ", requiredKnowledge='" + requiredKnowledge + '\'' +
                ", cost=" + cost +
                ", weight=" + weight +
                ", itemID=" + itemID +
                ", properties='" + properties + '\'' +
                "} " + super.toString();
    }
}
