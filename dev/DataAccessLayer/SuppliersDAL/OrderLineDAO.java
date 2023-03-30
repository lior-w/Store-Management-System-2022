package DataAccessLayer.SuppliersDAL;

import BusinessLayer.SupplierBusinessLayer.Order;
import BusinessLayer.SupplierBusinessLayer.Order.OrderLine;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class OrderLineDAO  extends DAO<OrderLine> {
    private static final String tableName = "OrderLines";

    public OrderLineDAO() {
        super(tableName);
    }

    public boolean insert(OrderLine orderLine, int orderId) {
        Connection connection = null;
        boolean b = false;
        identityMap.cache(orderLine.id, orderLine);
        try {
            connection = connect();
            Statement stmt = connection.createStatement();
            String query = String.format("INSERT INTO %s VALUES ('%d', '%d', '%s', '%s', '%d', '%,.2f' ,'%,.2f', '%,.2f')", tableName, orderLine.id, orderId, orderLine.catalogNumber,
                    orderLine.productName, orderLine.amount, orderLine.price, orderLine.discount, orderLine.finalPrice);
            b =  stmt.executeUpdate(query) > 0;
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
    public Order initialize(Order order) {
        Connection connection = null;
        try {
            connection = connect();
            Statement stmt = connection.createStatement();
            ResultSet result = stmt.executeQuery(String.format("Select * from OrderLines where orderId = %d", order.getId()));
            while (result.next()) {
                order.addLine(result.getInt("id"), result.getString("catalogNumber"),
                        result.getString("ProductName"), result.getInt("amount"),
                        result.getFloat("price"), result.getFloat("discount"), result.getFloat("finalPrice"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection(connection);
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

    public boolean delete(int orderId, String CN) {
        Connection connection = null;
        int output = 0;
        try {
            connection = connect();
            Statement stmt = connection.createStatement();
            String sql = String.format("DELETE FROM %s WHERE orderId = %d AND catalogNumber = '%s'", tableName, orderId, CN);
            output = stmt.executeUpdate(sql);
            if (identityMap.contains(orderId)) identityMap.remove(orderId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection(connection);
        return output > 0;
    }
    
    

}
