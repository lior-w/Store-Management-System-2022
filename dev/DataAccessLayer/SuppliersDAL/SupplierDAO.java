package DataAccessLayer.SuppliersDAL;


import BusinessLayer.SupplierBusinessLayer.Supplier;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAO extends DAO<Supplier> {
    private static final String tableName = "Suppliers";
    private SupplierContractDAO supplierContractDAO;

    public SupplierDAO() {
        super(tableName);
        supplierContractDAO = new SupplierContractDAO();
    }

    public boolean insert(Supplier supplier) {
        Connection connection = null;
        boolean output = false;
        identityMap.cache(supplier.getId(), supplier);
        try {
            connection = connect();
            Statement stmt = connection.createStatement();
            String query =  String.format("INSERT INTO %s VALUES ('%d', '%s', '%d', '%d', '%s', '%s', '%s');", tableName, supplier.getId(), supplier.getName(),
                    supplier.getCompanyNumber(), supplier.getBankAccount() ,supplier.getPayWay(), supplier.getAddress(), supplier.getArea());
            output = stmt.executeUpdate(query) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null) closeConnection(connection);
        }
        return output;
    }

    public Supplier get(int id) {
        Supplier supplier = identityMap.retrieve(id);
        if (supplier == null) {
            Connection connection = null;
            try {
                connection = connect();
                Statement stmt = connection.createStatement();
                ResultSet result = stmt.executeQuery(String.format("Select * from Suppliers where id = %d", id));
                while (result.next()) {
                    supplier = new Supplier(result.getInt("id"), result.getString("name"), result.getInt("companyNumber"),
                            result.getInt("bankAccount"), result.getString("payWay"), result.getString("address"), result.getString("area"));
                }
                if (supplier != null) identityMap.cache(id, supplier);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            finally {
                if (connection != null) closeConnection(connection);
            }
        }
        try {
            supplier.setContract(supplierContractDAO.get(supplier.getId()));
        }catch(Exception e){
            // this suppler has no contract
        }
        return supplier;
    }

    public List<Supplier> getSuppliers() {
        List<Supplier> res = new ArrayList<>();
        Connection connection = null;
        try {
            connection = connect();
            Statement stmt = connection.createStatement();
            ResultSet result = stmt.executeQuery("Select * from Suppliers");
            while (result.next()) {
                res.add(new Supplier(result.getInt("id"), result.getString("name"), result.getInt("companyNumber"),
                        result.getInt("bankAccount"), result.getString("payWay"), result.getString("address"), result.getString("area")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null) closeConnection(connection);
        }
        return res;
    }



}
