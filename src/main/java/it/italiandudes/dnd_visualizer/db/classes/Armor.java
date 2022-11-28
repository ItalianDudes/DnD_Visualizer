package it.italiandudes.dnd_visualizer.db.classes;

import it.italiandudes.dnd_visualizer.db.DBDefs;
import it.italiandudes.dnd_visualizer.db.enums.Rarity;
import it.italiandudes.idl.common.Logger;
import it.italiandudes.idl.common.RawSerializer;
import it.italiandudes.idl.common.SQLiteHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

@SuppressWarnings("unused")
public final class Armor extends Item {

    //Attributes
    private int AP;
    private int cutCA;
    private int impactCA;
    private int thrustCA;
    @NotNull private String bodyPart;

    //Constructors
    public Armor(@NotNull String name, @Nullable String lore, @NotNull Rarity rarity,
                 int requiredLevel, @Nullable String requiredKnowledge, @NotNull Cost cost, double weight, @Nullable String properties,
                 int AP, int cutCA, int impactCA, int thrustCA, @NotNull String bodyPart) {
        super(name, lore, rarity, requiredLevel, requiredKnowledge, cost, weight, properties);
        if(AP<0) throw new RuntimeException("\"AP\" must be equal or greater than 0");
        this.AP = AP;
        if(cutCA<1) throw new RuntimeException("\"CA against Cut\" must be equal or greater than 1");
        this.cutCA = cutCA;
        if(impactCA<1) throw new RuntimeException("\"CA against Impact\" must be equal or greater than 1");
        this.impactCA = impactCA;
        if(thrustCA<1) throw new RuntimeException("\"CA against Thrust\" must be equal or greater than 1");
        this.thrustCA = thrustCA;
        this.bodyPart = bodyPart;
    }
    public Armor(@NotNull Connection dbConnection, String name) throws SQLException {
        super(dbConnection, name);
        String query = "SELECT * FROM "+DBDefs.DB_TABLE_ARMORS+" WHERE name LIKE '"+name+"'";
        ResultSet resultSet = SQLiteHandler.readDataFromDB(dbConnection, query);
        AP = resultSet.getInt(1);
        cutCA = resultSet.getInt(2);
        impactCA = resultSet.getInt(3);
        thrustCA = resultSet.getInt(4);
        bodyPart = resultSet.getString(5);
    }

    //Methods
    @Override
    public boolean writeIntoDB(@NotNull Connection dbConnection, boolean genID) {
        boolean esito = super.writeIntoDB(dbConnection, genID);
        String query = "INSERT INTO "+ DBDefs.DB_TABLE_ARMORS +"(ap, ca_against_cut, ca_against_impact, ca_against_thrust, body_part, itemID) VALUES(?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = SQLiteHandler.prepareDataWriteIntoDB(dbConnection, query);
        try {
            statement.setInt(1, AP);
            statement.setInt(2, cutCA);
            statement.setInt(3, impactCA);
            statement.setInt(4, thrustCA);
            statement.setString(5, bodyPart);
            statement.setInt(6, getItemID());
            statement.execute();
            return esito;
        }catch (SQLException e){
            Logger.log(e);
            return false;
        }
    }
    @Override
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
    public int getAP() {
        return AP;
    }
    public void setAP(int AP) {
        this.AP = AP;
    }
    public int getCutCA() {
        return cutCA;
    }
    public void setCutCA(int cutCA) {
        this.cutCA = cutCA;
    }
    public int getImpactCA() {
        return impactCA;
    }
    public void setImpactCA(int impactCA) {
        this.impactCA = impactCA;
    }
    public int getThrustCA() {
        return thrustCA;
    }
    public void setThrustCA(int thrustCA) {
        this.thrustCA = thrustCA;
    }
    @NotNull public String getBodyPart() {
        return bodyPart;
    }
    public void setBodyPart(@NotNull String bodyPart) {
        this.bodyPart = bodyPart;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Armor armor = (Armor) o;

        if (getAP() != armor.getAP()) return false;
        if (getCutCA() != armor.getCutCA()) return false;
        if (getImpactCA() != armor.getImpactCA()) return false;
        if (getThrustCA() != armor.getThrustCA()) return false;
        return getBodyPart().equals(armor.getBodyPart());
    }
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getAP();
        result = 31 * result + getCutCA();
        result = 31 * result + getImpactCA();
        result = 31 * result + getThrustCA();
        result = 31 * result + getBodyPart().hashCode();
        return result;
    }
    @Override
    public String toString() {
        return "Armor{" +
                "AP=" + AP +
                ", cutCA=" + cutCA +
                ", impactCA=" + impactCA +
                ", thrustCA=" + thrustCA +
                ", bodyPart='" + bodyPart + '\'' +
                "} " + super.toString();
    }
}
