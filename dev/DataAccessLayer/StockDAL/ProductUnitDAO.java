package DataAccessLayer.StockDAL;

import BusinessLayer.StockBusinessLayer.ProductUnit;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductUnitDAO extends DAO<ProductUnit> {
    private static final String tableName = "ProductUnits";

    public ProductUnitDAO() {
        super(tableName);
    }

    public boolean insert(ProductUnit unit) {
        Connection connection = null;
        boolean output = false;
        try {
            connection = connect();
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(String.format("INSERT INTO %s VALUES (NULL, '%d', '%s', '%b', '%b', '%d', '%d')", tableName, unit.getProductId(), new SimpleDateFormat("yyyy-MM-dd").format(unit.getExpirationDate()),
                    unit.isDefected(), unit.isInStorage(), unit.getLocation(), unit.getSupplierDiscountId()));
            ResultSet res = stmt.getGeneratedKeys();
            if (res.next()) unit.setId(res.getInt(1));
            output = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null) closeConnection(connection);
        }
        identityMap.cache(unit.getId(), unit);
        return output;
    }

    public boolean insertAmount(int productId, Date expirationDate, boolean isInStorage, int location, int discountId, int amount) {
        Connection connection = null;
        boolean output = false;
        try {
            connection = connect();
            Statement stmt = connection.createStatement();
            int rowUpdatesCounter = 0;
            for (int i = 0; i < amount; i++) {
                rowUpdatesCounter += stmt.executeUpdate(String.format("INSERT INTO %s VALUES (NULL, '%d', '%s', '%d', '%d', '%d', '%d')", tableName, productId, new SimpleDateFormat("yyyy-MM-dd").format(expirationDate),
                        0, isInStorage ? 1 : 0, location, discountId));
                ResultSet res = stmt.getGeneratedKeys();
                if (res.next()) {
                    int newId = res.getInt(1);
                    identityMap.cache(newId, get(newId));
                }
                output = true;
            }
            if (rowUpdatesCounter < amount) throw new SQLException("Not all inserts were successful");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null) closeConnection(connection);
        }
        return output;
    }


    public ProductUnit get(int id) {
        ProductUnit unit = identityMap.retrieve(id);
        if (unit == null) {
            Connection connection = null;
            try {
                connection = connect();
                Statement stmt = connection.createStatement();
                ResultSet result = stmt.executeQuery(String.format("Select * from ProductUnits where id = '%d'", id));
                while (result.next()) {
                    Date expDate = new SimpleDateFormat("yyyy-MM-dd").parse(result.getString(3));
                    unit = new ProductUnit(expDate, result.getBoolean(5), result.getBoolean(4),
                            result.getInt(6), result.getInt(7), result.getInt(2));
                    unit.setId(result.getInt(1));
                }
                identityMap.cache(id, unit);
            } catch (SQLException | ParseException e) {
                e.printStackTrace();
            }
            finally {
                if (connection != null) closeConnection(connection);
            }
        }
        return unit;
    }

    public List<ProductUnit> getDefectedProductUnits() {
        List<ProductUnit> defectedUnits = new ArrayList<>();
        Connection connection = null;
        try {
            connection = connect();
            Statement stmt = connection.createStatement();
            ResultSet result = stmt.executeQuery("Select * from ProductUnits where isDefected = 1 OR datetime(expirationDate) <= datetime('now')");
            while (result.next()) {
                Date expDate = new SimpleDateFormat("yyyy-MM-dd").parse(result.getString(3));
                ProductUnit unit = new ProductUnit(expDate, result.getBoolean(5), result.getBoolean(4),
                        result.getInt(6), result.getInt(7), result.getInt(2));
                int unitId = result.getInt(1);
                unit.setId(unitId);
                identityMap.cache(unitId, unit);
                defectedUnits.add(unit);
            }
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null) closeConnection(connection);
        }
        return defectedUnits;
    }

    public List<ProductUnit> getProductUnits(int id) {
        List<ProductUnit> units = new ArrayList<>();
        Connection connection = null;
        try {
            connection = connect();
            Statement stmt = connection.createStatement();
            ResultSet result = stmt.executeQuery("Select * from ProductUnits where typeId = " + id);
            while (result.next()) {
                Date expDate = new SimpleDateFormat("yyyy-MM-dd").parse(result.getString(3));
                ProductUnit unit = new ProductUnit(expDate, result.getBoolean(5), result.getBoolean(4),
                        result.getInt(6), result.getInt(7), result.getInt(2));
                int unitId = result.getInt(1);
                unit.setId(unitId);
                identityMap.cache(unitId, unit);
                units.add(unit);
            }
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null) closeConnection(connection);
        }
        return units;
    }
}
