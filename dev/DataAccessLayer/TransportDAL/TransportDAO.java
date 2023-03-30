package DataAccessLayer.TransportDAL;

import DataAccessLayer.Repository;

import java.sql.*;

public class TransportDAO {
    String tableName;
    public TransportDAO(){
        this.tableName = "Transports";
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

    public int insert(TransportDTO Ob) {
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
            ans = 0;
            try {
                assert conn != null;
                conn.rollback();
            }
            catch (SQLException ex) {
            }
        }
        finally {
            closeConnection(conn);
        }
        return ans;
    }

    public int update(TransportDTO updatedOb)//not allowed to change ID
    {
        Connection conn = null;
        if (updatedOb == null) return 0;
        String updateString = String.format("UPDATE %s" +
                        " SET  DateOfTransport=\"%s\"" +
                        ", timeOfDeparture=\"%s\"" +
                        ", vehicleLicensePlate=\"%s\"" +
                        ", driverId=\"%s\"" +
                        ", area=\"%s\"" +
                        ", status=\"%s\"" +
                        " WHERE transportId == %s ;",
                tableName, updatedOb.dateOfTransport,
                updatedOb.timeOfDeparture,updatedOb.vehicleLicensePlate,updatedOb.driverId,
                updatedOb.area,updatedOb.status,updatedOb.transportId);
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
           closeConnection(conn);
        }
    }

    public String InsertStatement(String Values) {
        return String.format("INSERT INTO %s \n" +
                "VALUES %s;", tableName, Values);
    }

    public int remove(int id) {
        Connection conn = null;
        String updateString;
        updateString= String.format("DELETE FROM %s \n" +
                "WHERE %s=\"%s\";",tableName, "transportId", id);
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

    public TransportDTO get(int id){
        Connection conn = null;
        try {
            conn = connect();
            Statement statement = conn.createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM "+ tableName+"\n"+
                    "WHERE transportId="+id);
            TransportDTO dto = new TransportDTO(res.getInt(1),res.getString(2),
                    res.getString(3),res.getString(4),res.getString(5),
                    res.getString(6),res.getString(7));
            return dto;
        } catch (SQLException e) {
            return null;
        }
        finally {
            closeConnection(conn);
        }
    }
}
