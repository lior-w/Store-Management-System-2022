package DataAccessLayer.TransportDAL;

import DataAccessLayer.Repository;

import java.sql.*;
import java.util.Vector;

public class TransportsSitesDAO {
    String tableName;
    public TransportsSitesDAO(){
        this.tableName = "TransportsSites";
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

    public int insert(TransportsSitesDTO Ob) {
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
                ex.printStackTrace();
            }
            ans = 0;
        }
        finally {
            closeConnection(conn);
        }
        return ans;
    }

    public int update(TransportDocumentDTO updatedOb)//not allowed to change ID
    {
        Connection conn = null;
        if (updatedOb == null) return 0;
        String updateString = String.format("UPDATE %s" +
                        " SET  weekly=\"%s\"" +
                        ", siteId=\"%s\"" +
                        ", assignedTransport=\"%s\"" +
                        " WHERE id == %s ;",
                tableName, updatedOb.weekly,
                updatedOb.siteId,updatedOb.assignedTransportId,updatedOb.id);
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

    public int remove(int id, int siteID) {
        Connection conn = null;
        String updateString;
        updateString= String.format("DELETE FROM %s \n" +
                "WHERE %s=\"%s\" AND %s=\"%s\";", tableName, "siteId", id,"transportId" ,siteID);
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

    public Vector<TransportsSitesDTO> get(int id){
        Vector<TransportsSitesDTO> result = new Vector<>();
        Connection conn = null;
        try {
            conn = connect();
            Statement statement = conn.createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM "+ tableName+"\n"+
                    "WHERE transportId="+id);
            while (res.next()) {
                TransportsSitesDTO dto = new TransportsSitesDTO(res.getInt(2),
                        res.getInt(1));
                result.add(dto);
            }
            return result;
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

    public Vector<TransportsSitesDTO> get(int transId,int siteId){
        Vector<TransportsSitesDTO> result = new Vector<>();
        Connection conn = null;
        try {
            conn = connect();
            Statement statement = conn.createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM "+ tableName+"\n"+
                    "WHERE transportId="+transId+" AND siteId="+siteId);
            TransportsSitesDTO dto = new TransportsSitesDTO(res.getInt(2),
                        res.getInt(1));
            result.add(dto);
            return result;
        } catch (SQLException e) {
            try {
                assert conn != null;
                conn.rollback();
            }
            catch (SQLException ex) {
            }
            return new Vector<>();
        }
        finally {
            closeConnection(conn);
        }
    }
}
