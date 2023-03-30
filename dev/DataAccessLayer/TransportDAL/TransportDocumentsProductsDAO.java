package DataAccessLayer.TransportDAL;

import DataAccessLayer.Repository;

import java.sql.*;
import java.util.Vector;

public class TransportDocumentsProductsDAO {
    String tableName;
    public TransportDocumentsProductsDAO(){
        tableName = "TransportDocumentsProducts";
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

    public int insert(TransportDocumentsProductsDTO Ob) {
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
            assert conn != null;
            closeConnection(conn);
        }
        return ans;
    }

    public int update(TransportDocumentsProductsDTO updatedOb)//not allowed to change ID
    {
        Connection conn = null;
        if (updatedOb == null) return 0;
        String updateString = String.format("UPDATE %s" +
                        " SET  quantity= \"%s\" " +
                        " WHERE transportDocumentId == %s AND productId == %s;",
                tableName, updatedOb.quantity, updatedOb.transDocId,
                updatedOb.productId);
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

    public String InsertStatement(String Values) {
        return String.format("INSERT INTO %s \n" +
                "VALUES %s;", tableName, Values);
    }

    public int remove(int id, int id1) {
        Connection conn = null;
        String updateString;
        updateString= String.format("DELETE FROM %s \n" +
                "WHERE %s=\"%s\" AND %s=\"%s\";", tableName, "transportDocumentId",
                id,"productId" ,id1);
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

    public Vector<TransportDocumentsProductsDTO> get(int id){
        Vector<TransportDocumentsProductsDTO> result = new Vector<>();
        Connection conn = null;
        try {
            conn = connect();
            Statement statement = conn.createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM "+ tableName+"\n"+
                    "WHERE transportDocumentId="+id);
            while (res.next()) {
                TransportDocumentsProductsDTO dto = new TransportDocumentsProductsDTO(res.getInt(1),
                        res.getInt(2), res.getInt(3));
                result.add(dto);
            }
            return result;
        } catch (SQLException e) {
            try {
                conn.rollback();
            }
            catch (SQLException ex) {
            }
            return new Vector<>();
        }
        finally {
            assert conn != null;
            closeConnection(conn);
        }
    }

    public Vector<TransportDocumentsProductsDTO> get(int docId,int productId){
        Vector<TransportDocumentsProductsDTO> result = new Vector<>();
        Connection conn = null;
        try {
            conn = connect();
            Statement statement = conn.createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM "+ tableName+"\n"+
                    "WHERE transportDocumentId="+docId +" AND productId="+productId);
            while (res.next()) {
                TransportDocumentsProductsDTO dto = new TransportDocumentsProductsDTO(res.getInt(1),
                        res.getInt(2), res.getInt(3));
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
}
