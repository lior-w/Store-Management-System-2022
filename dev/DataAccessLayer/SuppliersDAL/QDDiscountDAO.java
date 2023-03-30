package DataAccessLayer.SuppliersDAL;

import BusinessLayer.SupplierBusinessLayer.SupplierController;
import BusinessLayer.SupplierBusinessLayer.SupplierProduct;
import DataAccessLayer.IdentityMap;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class QDDiscountDAO extends DAO<Integer> {
    private static final String tableName = "QDDiscounts";

    private SupplierController controller;
    private List<List<String>> DiscountsList;

    public QDDiscountDAO() {
        super(tableName);
        identityMap = new IdentityMap<>();
        DiscountsList = new LinkedList<>();
    }

    public boolean insert(int supplierId, String catalogNumber, String pname, int fromAmount, double discount) {
        Connection connection = null;
        boolean output = false;
        try {
            connection = connect();
            Statement stmt = connection.createStatement();
            output = stmt.executeUpdate(String.format("INSERT INTO %s VALUES ('%d', '%s', '%s', '%d', '%e')", tableName, supplierId , catalogNumber, pname, fromAmount, discount))>0 ;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null) closeConnection(connection);
        }
        return output;
    }

    public Map<Integer, Double> getDiscounts(SupplierProduct product, int supplierId) {
        Connection connection = null;
        Map<Integer, Double> discount = new HashMap<>();
        try {
            connection = connect();
            Statement stmt = connection.createStatement();
            ResultSet result = stmt.executeQuery(String.format("Select * from QDDiscounts Where supplierId = " +
                    supplierId + " AND catalogNumber = '" + product.getCatalogNumber() + "';"));
            controller = SupplierController.getInstance();
            while (result.next()) {
                discount.put(result.getInt("fromAmount"), result.getDouble("discount"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null) closeConnection(connection);
        }
        return discount;
    }

    @Override
    public boolean delete(int supplierId) {
        Connection connection = null;
        int output = 0;
        try {
            connection = connect();
            Statement stmt = connection.createStatement();
            output = stmt.executeUpdate("DELETE FROM " + tableName + " WHERE supplierId = " + supplierId + "");
            if (identityMap.contains(supplierId)) identityMap.remove(supplierId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null) closeConnection(connection);
        }
        return output > 0;
    }

    public boolean delete(int supplierId, String catalogNumber, int fromAmount) {
        Connection connection = null;
        int output = 0;
        try {
            connection = connect();
            Statement stmt = connection.createStatement();
            String query = "DELETE FROM " + tableName + " WHERE supplierId = " + supplierId + " AND catalogNumber = '" + catalogNumber + "' AND fromAmount = " + fromAmount;
            output = stmt.executeUpdate(query);
            if (identityMap.contains(supplierId)) identityMap.remove(supplierId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null) closeConnection(connection);
        }
        return output > 0;
    }
}
