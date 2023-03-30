package DataAccessLayer.TransportDAL;

import DataAccessLayer.Repository;

import java.sql.*;

public class TransportDocumentDAO {
    String tableName;
    public TransportDocumentDAO(){
        this.tableName = "TransportDocuments";
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

    public int insert(TransportDocumentDTO Ob) {
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
        }
        finally {
            assert conn != null;
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
                        ", status=\"%s\"" +
                        " WHERE id == %s ;",
                tableName, updatedOb.weekly,
                updatedOb.siteId,updatedOb.assignedTransportId,updatedOb.status,updatedOb.id);
        Statement s;
        try {
            conn = connect();
            s = conn.createStatement();
            return s.executeUpdate(updateString);
        } catch (Exception e) {
            return 0;
        }
        finally {
            assert conn != null;
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
                "WHERE %s=\"%s\";",tableName, "id", id);
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
            assert conn != null;
            closeConnection(conn);
        }
    }

    public TransportDocumentDTO get(int id){
        Connection conn = null;
        try {
            conn = connect();
            Statement statement = conn.createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM "+ tableName+"\n"+
                    "WHERE id="+id);
            TransportDocumentDTO dto = new TransportDocumentDTO(res.getInt(1),res.getString(2),
                    res.getInt(3),res.getInt(4),res.getString(5),res.getInt(6));
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
            assert conn != null;
            closeConnection(conn);
        }
    }

    public int assignedTransport(int id) {
        Connection conn = null;
        int maxi = -1;
        try {
            conn = connect();
            Statement statement = conn.createStatement();
            ResultSet res = statement.executeQuery("SELECT assignedTransport FROM " + tableName +
                    " WHERE id="+id);
            try {
                maxi = res.getInt(1);
            } catch (Exception e) {
                return maxi;
            }
            return maxi;
        } catch (SQLException e) {
            try {
                assert conn != null;
                conn.rollback();
            }
            catch (SQLException ex) {
            }
            return maxi;
        }
        finally {
            assert conn != null;
            closeConnection(conn);
        }
    }

    public TransportDocumentDTO getByOrderId(int id){
        Connection conn = null;
        try {
            conn = connect();
            Statement statement = conn.createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM "+ tableName+"\n"+
                    "WHERE orderId="+id);
            TransportDocumentDTO dto = new TransportDocumentDTO(res.getInt(1),res.getString(2),
                    res.getInt(3),res.getInt(4),res.getString(5),res.getInt(6));
            return dto;
        } catch (SQLException e) {
            try {
                conn.rollback();
            }
            catch (SQLException ex) {
            }
            return null;
        }
        finally {
            assert conn != null;
            closeConnection(conn);
        }
    }

}
