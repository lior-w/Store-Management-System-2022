package DataAccessLayer.SuppliersDAL;

import BusinessLayer.SupplierBusinessLayer.Order;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;

public class OrderDAO  extends DAO<Order> {
    private static final String tableName = "Orders";


    public OrderDAO() {
        super(tableName);
    }

    public boolean insert(Order order) {
        Connection connection = null;
        boolean b = false;
        identityMap.cache(order.getId(), order);
        try {
            connection = connect();
            Statement stmt = connection.createStatement();
            String query =String.format("INSERT INTO %s VALUES ('%d', '%d', '%d', '%s', '%s', '%s', '%s', '%d', '%d')",
                    tableName, order.getId(), (order.isWeekly() ? 1 : 0), order.getSupplierId(), order.getSupplierName(), order.getAddress(),
                    order.getDate(), order.getContactPhone(), (order.isReady() ? 1 : 0), (order.isArrived()? 1 : 0));
            b = stmt.executeUpdate(query) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            }
            catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        }
        finally {
            if (connection != null) closeConnection(connection);
        }
        return b;
    }

    public Order get(int id) {
        Order order = identityMap.retrieve(id);
        if (order == null) {
            Connection connection = null;
            try {
                connection = connect();
                Statement stmt = connection.createStatement();
                ResultSet result = stmt.executeQuery(String.format("Select * from Orders where orderId = %d", id));
                while (result.next()) {
                    try {
                        order = new Order(id, result.getString("supplierName"), result.getInt("supplierId"),
                                result.getString("address"), result.getString("contactPhone"),
                                result.getInt("weekly") == 1, result.getString("date"),
                                result.getInt("isReady") == 1, result.getInt("isArrived") == 1);
                    }catch(ParseException e){
                        System.out.println(e.getMessage());
                    }
                }
                if (order != null) identityMap.cache(id, order);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            closeConnection(connection);
        }
        return order;
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
