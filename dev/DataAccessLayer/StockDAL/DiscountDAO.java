package DataAccessLayer.StockDAL;

import BusinessLayer.StockBusinessLayer.Discount;
import BusinessLayer.StockBusinessLayer.SaleDiscount;
import BusinessLayer.StockBusinessLayer.SupplierDiscount;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DiscountDAO extends DAO<Discount> {

    private static final String tableName = "Discounts";

    public DiscountDAO() {
        super(tableName);
    }

    public boolean insert(Discount discount) {
        Connection connection = null;
        boolean output = false;
        try {
            connection = connect();
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(String.format("INSERT INTO %s VALUES (NULL, '%f')", tableName, discount.getDiscount()));
            ResultSet res = stmt.getGeneratedKeys();
            if (res.next()) discount.setId(res.getInt(1));
            output = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null) closeConnection(connection);
        }
        return output;
    }

    public boolean insert(SaleDiscount discount) {
        if (!insert((Discount)discount)) return false;
        Connection connection = null;
        boolean output = false;
        try {
            connection = connect();
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(String.format("INSERT INTO SaleDiscounts VALUES ('%d', '%s', '%s')", discount.getId(), new SimpleDateFormat("yyyy-MM-dd").format(discount.getStart()),
                    new SimpleDateFormat("yyyy-MM-dd").format(discount.getEnd())));
            identityMap.cache(discount.getId(), discount);
            output = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null) closeConnection(connection);
        }
        return output;
    }

    public boolean insert(SupplierDiscount discount) {
        if (!insert((Discount)discount)) return false;
        Connection connection = null;
        boolean output = false;
        try {
            connection = connect();
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(String.format("INSERT INTO SupplierDiscounts VALUES ('%d', '%d')", discount.getId(), discount.getSupplierId()));
            identityMap.cache(discount.getId(), discount);
            output = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null) closeConnection(connection);
        }
        return output;
    }


    public Discount get(int id) {
        Discount discount = identityMap.retrieve(id);
        if (discount == null) {
            Connection connection = null;
            try {
                connection = connect();
                Statement stmt = connection.createStatement();
                Statement stmt2 = connection.createStatement();
                ResultSet discountResult = stmt.executeQuery(String.format("Select * from Discounts where id = '%d'", id));
                ResultSet extraResult = stmt2.executeQuery(String.format("Select * from SaleDiscounts where Id = '%s'", id));
                if (!extraResult.next()) {
                    extraResult = stmt2.executeQuery(String.format("Select * from SupplierDiscounts where Id = '%s'", id));
                    if (!extraResult.next()) return null;
                    discount = new SupplierDiscount(discountResult.getFloat("discount"), extraResult.getInt("supplierId"));
                    discount.setId(id);
                }
                else {
                    discount = new SaleDiscount(new SimpleDateFormat("yyyy-MM-dd").parse(extraResult.getString("start")), new SimpleDateFormat("yyyy-MM-dd").parse(extraResult.getString("end")), discountResult.getFloat("discount"));
                    discount.setId(id);
                }

                identityMap.cache(id, discount);
            } catch (SQLException | ParseException e) {
                e.printStackTrace();
            }
            finally {
                if (connection != null) closeConnection(connection);
            }
        }
        return discount;
    }

    @Override
    public <E> int update(int id, String columnName, E value) {
        Connection connection = null;
        int output = 0;
        try {
            connection = connect();
            Statement stmt = connection.createStatement();
            if (columnName.equals("discount")) {
                output = stmt.executeUpdate("UPDATE " + tableName + " SET " + columnName + " = '" + value + "' WHERE id = " + id);
            }
            else if (columnName.equals("start") || columnName.equals("end")){
                output = stmt.executeUpdate("UPDATE SaleDiscounts SET " + columnName + " = '" + new SimpleDateFormat("yyyy-MM-dd").format((Date)value) + "' WHERE id = " + id);
            }
            else if (columnName.equals("supplierId")){
                output = stmt.executeUpdate("UPDATE SupplierDiscounts SET " + columnName + " = '" + value + "' WHERE id = " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null) closeConnection(connection);
        }
        return output;
    }

}
