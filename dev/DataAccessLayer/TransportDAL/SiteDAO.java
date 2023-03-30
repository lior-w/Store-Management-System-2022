package DataAccessLayer.TransportDAL;

import BusinessLayer.EmployeeBusinessLayer.Pair;
import DataAccessLayer.Repository;

import javax.swing.text.ParagraphView;
import java.sql.*;
import java.util.Vector;

public class SiteDAO {
    String tableName;
    public SiteDAO(){
        this.tableName = "Sites";
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

    public int insert(SiteDTO Ob) {
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
            assert conn != null;
            closeConnection(conn);
        }
        return ans;
    }

    public int update(SiteDTO updatedOb)//not allowed to change ID
    {
        Connection conn = null;
        if (updatedOb == null) return 0;
        String updateString = String.format("UPDATE %s" +
                        " SET  contactName= \"%s\" " +
                        ", contactNumber=\"%s\"" +
                        " WHERE id == %s ;",
                tableName, updatedOb.contactName, updatedOb.ContactNumber,
                updatedOb.id);
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
    public int remove(int id){
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

//    public SiteDTO get(int id){
//        Connection conn = Repository.getInstance().connect();
//        try {
//            Statement statement = conn.createStatement();
//            ResultSet res = statement.executeQuery("SELECT * FROM "+ tableName+"\n"+
//                    "WHERE id="+id);
//            SiteDTO dto = new SiteDTO(res.getInt(1),res.getString(2),
//                    res.getString(3),res.getString(4),res.getString(5));
//            return dto;
//        } catch (SQLException e) {
//            return null;
//        }
//    }

    public SiteDTO get(int id){
        Connection conn = null;
        try {
            conn = connect();
            Statement statement = conn.createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM Suppliers"+"\n"+
                    "WHERE id="+id);
            Pair<String,String> contact = getSiteContact(id);
            SiteDTO dto = new SiteDTO(id,res.getString("address"),contact.first,contact.second,
                    res.getString("area"));
            return dto;
        } catch (SQLException e) {
            return null;
        }
        finally {
            closeConnection(conn);
        }
    }

    public Pair<String,String> getSiteContact(int id){
        Connection conn = null;
        try {
            conn = connect();
            Statement statement = conn.createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM Contacts"+"\n"+
                    "WHERE supplierId="+id);
            Pair<String,String> contact = new Pair(res.getString("name"),res.getString("phone"));
            return contact;
        } catch (SQLException e) {
            return null;
        }

        finally {
            closeConnection(conn);
        }
    }
}
