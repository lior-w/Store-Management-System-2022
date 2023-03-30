package DataAccessLayer.SuppliersDAL;

import BusinessLayer.SupplierBusinessLayer.SupplierProduct;
import DataAccessLayer.IdentityMap;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class SupplierProductDAO extends DAO<SupplierProduct> {
    private static final String tableName = "SuppliersProducts";
    private Map<SupplierProduct, Integer> productToSupplierMap;


    public SupplierProductDAO() {
        super(tableName);
        identityMap = new IdentityMap<>();
    }

    public boolean insert(SupplierProduct supplierProduct, int supplierId) {
        Connection connection = null;
        boolean output = false;
//        productToSupplierMap.put(supplierProduct, supplierId);
        try {
            connection = connect();
            Statement stmt = connection.createStatement();
            String query = String.format("INSERT INTO %s VALUES ('%d', '%s', '%s', '%s', '%e')", tableName, supplierId ,supplierProduct.getName(), supplierProduct.getManufacturer()
                    ,supplierProduct.getCatalogNumber() ,supplierProduct.getPrice());
            output = stmt.executeUpdate(query)>0 ;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null) closeConnection(connection);
        }
        return output;
    }


    public Map<SupplierProduct, Integer> getProductsForSupplier() {
        Connection connection = null;
        productToSupplierMap = new HashMap<SupplierProduct, Integer>();
        try {
            connection = connect();
            Statement stmt = connection.createStatement();
            ResultSet result = stmt.executeQuery(String.format("Select * from SuppliersProducts"));
            while (result.next()) {
                try {
                    productToSupplierMap.put(new SupplierProduct(result.getString("CatalogNumber"),result.getString("name"),
                            result.getDouble("price"), result.getString("manufacturer")), result.getInt("supplierId"));
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null) closeConnection(connection);
        }
        return productToSupplierMap;
    }

    public <E> int update(int supplierId,String catalogNumber, String columnName, E value) {
        Connection connection = null;
        int output = 0;
        try {
            connection = connect();
            Statement stmt = connection.createStatement();
            output = stmt.executeUpdate("UPDATE " + tableName + " SET " + columnName + " = '" + value +
                    "' WHERE supplierId = " + supplierId + " AND catalogNumber = '" + catalogNumber + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null) closeConnection(connection);
        }
        return output;
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

    public boolean delete(int supplierId, String catalogNumber) {
        Connection connection = null;
        int output = 0;
        try {
            connection = connect();
            Statement stmt = connection.createStatement();
            output = stmt.executeUpdate("DELETE FROM " + tableName + " WHERE supplierId = " + supplierId +
                    " AND catalogNumber = '" + catalogNumber + "'");
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
