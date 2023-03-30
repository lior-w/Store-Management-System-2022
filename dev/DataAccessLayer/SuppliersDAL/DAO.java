package DataAccessLayer.SuppliersDAL;
import DataAccessLayer.IdentityMap;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class DAO<T> {
    private final String databasePath = "jdbc:sqlite:adss_v02.db";
    private String tableName;
    protected IdentityMap<T> identityMap;

    public DAO(String tableName) {
        this.tableName = tableName;
        identityMap = new IdentityMap<>();
    }
    public Connection connect() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(databasePath);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void closeConnection(Connection connection) {
        try {
            if (!connection.isClosed())
                connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean delete(int id) {
        Connection connection = null;
        int output = 0;
        try {
            connection = connect();
            Statement stmt = connection.createStatement();
            output = stmt.executeUpdate("DELETE FROM " + tableName + " WHERE id = " + id + "");
            if (identityMap.contains(id)) identityMap.remove(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection(connection);
        return output > 0;
    }

    public <E> int update(int id, String columnName, E value) {
        Connection connection = null;
        int output = 0;
        try {
            connection = connect();
            Statement stmt = connection.createStatement();
            output = stmt.executeUpdate("UPDATE " + tableName + " SET " + columnName + " = " + value + " WHERE id = " + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection(connection);
        return output;
    }
}

