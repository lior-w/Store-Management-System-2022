package DataAccessLayer.SuppliersDAL;

import BusinessLayer.SupplierBusinessLayer.SupplierContract;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SupplierContractDAO  extends DAO<SupplierContract> {
    private static final String tableName = "SupplierContracts";

    public SupplierContractDAO() {
        super(tableName);
    }

    public boolean insert(SupplierContract supplierContract) {
        Connection connection = null;
        boolean output = false;
        identityMap.cache(supplierContract.getSupplierId(), supplierContract);
        try {
            connection = connect();
            Statement stmt = connection.createStatement();
            int independentShipping = supplierContract.isIndependentShipping() ? 1 : 0;
            int quantitiesDocument = supplierContract.isQuantitiesDocument() ? 1 : 0;
            output = stmt.executeUpdate(String.format("INSERT INTO %s VALUES ('%d', '%s', '%d', '%d')", tableName,
                    supplierContract.getSupplierId(), supplierContract.getConstantDay(),
                    independentShipping , quantitiesDocument))>0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null) closeConnection(connection);
        }
        return output;
    }

    public SupplierContract get(int id) {
        SupplierContract supplierContract = identityMap.retrieve(id);
        if (supplierContract == null) {
            Connection connection = null;
            try {
                connection = connect();
                Statement stmt = connection.createStatement();
                ResultSet result = stmt.executeQuery(String.format("Select * from SupplierContracts where id = %d", id));
                while (result.next()) {
                    try {
                        supplierContract = new SupplierContract(
                                result.getInt("id"), result.getInt("day"),
                                result.getInt("isIndependent") == 1);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
                if (supplierContract != null) identityMap.cache(id, supplierContract);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            finally {
                if (connection != null) closeConnection(connection);
            }
        }
        return supplierContract;
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
        finally {
            if (connection != null) closeConnection(connection);
        }
        return output;
    }

}
