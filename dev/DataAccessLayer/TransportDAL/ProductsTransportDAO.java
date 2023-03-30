package DataAccessLayer.TransportDAL;

import DataAccessLayer.Repository;

import java.sql.*;

public class ProductsTransportDAO {
    String tableName;
    private final String databasePath = "jdbc:sqlite:adss_v02.db";

    public ProductsTransportDAO(){
        this.tableName = "Products";
    }

    public Connection connect() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(databasePath);
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

    public int insert(ProductsTransportDTO Ob) {
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

    public String InsertStatement(String Values) {
        return String.format("INSERT INTO %s \n" +
                "VALUES %s;", tableName, Values);
    }

    public ProductsTransportDTO get(int id){
        Connection conn = null;
        try {
            conn = connect();
            Statement statement = conn.createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM "+ tableName+"\n"+
                    "WHERE id="+id);
            ProductsTransportDTO dto = new ProductsTransportDTO(res.getInt("id"),res.getString("name"),
                    res.getInt("weight"));
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
