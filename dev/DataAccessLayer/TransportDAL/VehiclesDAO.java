package DataAccessLayer.TransportDAL;

import BusinessLayer.TransportBusinessLayer.Vehicle;
import DataAccessLayer.Repository;

import java.sql.*;

public class VehiclesDAO {
    String tableName;
    public VehiclesDAO(){
        this.tableName = "Vehicles";
    }
    public Connection connect() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:adss_v02.db");
        } catch (SQLException e) {
        }
        return connection;
    }

    public void closeConnection(Connection connection) {
        try {
            if (!connection.isClosed())
                connection.close();
        } catch (SQLException e) {
        }
    }

    public int insert(VehiclesDTO Ob) {
        int ans = 0;
        Connection conn = null;
        if (Ob == null) return 0;
        String toInsertProd = Ob.fieldsToString();
        Statement s;
        try {
            conn = connect();
            s = conn.createStatement();
            ans = s.executeUpdate(InsertStatement(toInsertProd));
        } catch (Exception e) {
            try {
                assert conn != null;
                conn.rollback();
            }
            catch (SQLException ex) {
            }
            ans = 0;
        }
        finally {
            closeConnection(conn);
        }
        return ans;
    }
    public int update(VehiclesDTO updatedOb)//not allowed to change ID
    {
        Connection conn = null;
        if (updatedOb == null) return 0;
        String updateString = String.format("UPDATE %s" +
                        " SET  maxWeight=\"%s\""+
                        " WHERE numberPlate == %s ;",
                tableName, updatedOb.maxWeight,
                updatedOb.licensePlate);
        Statement s;
        try {
            conn = connect();
            s = conn.createStatement();
            return s.executeUpdate(updateString);
        } catch (Exception e) {
            try {
                assert conn != null;
                conn.rollback();
            }
            catch (SQLException ex) {
            }
            return 0;
        }
        finally {
            assert conn != null;
            closeConnection(conn);
        }
    }
    public int remove(String v){
        Connection conn = null;
        String updateString;
        if(v == null) return 0;
        updateString= String.format("DELETE FROM %s \n" +
                "WHERE %s=\"%s\";",tableName, "numberPlate", v);
        Statement s;
        try
        {
            conn = connect();
            s = conn.createStatement();
            return s.executeUpdate(updateString);
        }
        catch (Exception e ){
            try {
                assert conn != null;
                conn.rollback();
            }
            catch (SQLException ex) {
            }
            return 0;
        }
        finally {
            closeConnection(conn);
        }
    }

    public String InsertStatement(String Values) {
        return String.format("INSERT INTO %s \n" +
                "VALUES %s;", tableName, Values);
    }

    public VehiclesDTO get(String numberPlate){
        Connection conn = null;
        try {
            conn = connect();
            Statement statement = conn.createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM "+ tableName+"\n"+
                    "WHERE numberPlate="+numberPlate);
            VehiclesDTO dto = new VehiclesDTO(res.getString(1),res.getString(2),
                    res.getInt(3),res.getInt(4),res.getString(5));
            return dto;
        } catch (SQLException e) {
            try {
                assert conn != null;
                conn.rollback();
            }
            catch (SQLException ex) {
            }
            return null;
        }
        finally {
            closeConnection(conn);
        }
    }
}
