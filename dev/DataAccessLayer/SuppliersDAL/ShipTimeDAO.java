package DataAccessLayer.SuppliersDAL;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ShipTimeDAO extends DAO<Integer>{
    private static final String tableName = "shipTime";

    public ShipTimeDAO() {
        super(tableName);
    }
    public boolean insert(int orderId, int day) {
        Connection connection = null;
        boolean output = false;
        identityMap.cache(orderId, day);
        try {
            connection = connect();
            Statement stmt = connection.createStatement();
            String query = String.format("INSERT INTO %s VALUES ('%d', '%d', NULL)", tableName, orderId, day);
            output = stmt.executeUpdate(query) >0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null) closeConnection(connection);
        }
        return output;
    }

    public int getDay(int orderId) {
        int res = 0;
        Connection connection = null;
        try {
            connection = connect();
            Statement stmt = connection.createStatement();
            String query= String.format("Select * from " + tableName + " where orderId = '%d'", orderId);
            ResultSet result = stmt.executeQuery(query);
            while (result.next()) {
                res = result.getInt("weeklyDate");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null) closeConnection(connection);
        }

        return res;
    }

    @Override
    public boolean delete(int orderId) {
        Connection connection = null;
        int output = 0;
        try {
            connection = connect();
            Statement stmt = connection.createStatement();
            output = stmt.executeUpdate("DELETE FROM " + tableName + " WHERE orderId = " + orderId + "");
            if (identityMap.contains(orderId)) identityMap.remove(orderId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection(connection);
        return output > 0;
    }

}
