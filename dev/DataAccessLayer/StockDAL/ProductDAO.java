package DataAccessLayer.StockDAL;

import DataAccessLayer.IdentityMap;
import BusinessLayer.StockBusinessLayer.Product;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO extends DAO<Product> {

    private static final String tableName = "Products";
    private IdentityMap<Product> identityMap;

    public ProductDAO() {
        super(tableName);
        identityMap = new IdentityMap<>();
    }

    public boolean insert(Product product) {
        Connection connection = null;
        boolean output = false;
        try {
            connection = connect();
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(String.format("INSERT INTO %s VALUES (NULL, '%s', '%d', '%d', '%s', '%f', '%d','%d', '%f')", tableName, product.getName(), product.getCategoryId(), product.getMinQuantity(),
                    product.getProducer(), product.getSalePrice(), product.getSaleDiscountId(), product.getAmount(), product.getWeight()));
            ResultSet res = stmt.getGeneratedKeys();
            if (res.next()) product.setId(res.getInt(1));
            output = true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) closeConnection(connection);
        }
        identityMap.cache(product.getId(), product);
        return output;
    }

    public Product get(int id) {
        Product product = identityMap.retrieve(id);
        if (product == null) {
            Connection connection = null;
            try {
                connection = connect();
                Statement stmt = connection.createStatement();
                ResultSet result = stmt.executeQuery(String.format("Select * from Products where id = '%d'", id));
                while (result.next()) {
                    product = new Product(result.getString(2), result.getInt(4), result.getInt(3),
                            result.getString(5), result.getFloat(6), result.getFloat("weight"));
                    product.setId(id);
                    product.setAmount(result.getInt(8));
                    int saleDiscountId = result.getInt(7);
                    if (saleDiscountId != -1) product.setSaleDiscountId(saleDiscountId);
                }
                identityMap.cache(id, product);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            finally {
                if (connection != null) closeConnection(connection);
            }
        }
        return product;
    }

    public int getId(String productName, String producer) {
        Connection connection = null;
        int id = -1;
        try {
            connection = connect();
            Statement stmt = connection.createStatement();
            ResultSet result = stmt.executeQuery(String.format("Select id from Products where name = '%s' and producer = '%s'", productName, producer));
            while (result.next()) {
                id = result.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null) closeConnection(connection);
        }
        return id;
    }

    public List<Product> underMinimumQuantities() {
        Connection connection = null;
        List<Product> products = new ArrayList<>();
        try {
            connection = connect();
            Statement stmt = connection.createStatement();
            ResultSet result = stmt.executeQuery("Select * from Products where amount < minQuantity");
            while (result.next()) {
                int id = result.getInt("id");
                if (identityMap.contains(id)) products.add(identityMap.retrieve(id));
                else {
                    Product product = new Product(result.getString(2), result.getInt(4), result.getInt(3),
                            result.getString(5), result.getFloat(6), result.getFloat("weight"));
                    product.setId(id);
                    product.setAmount(result.getInt(8));
                    int saleDiscountId = result.getInt(7);
                    if (saleDiscountId != -1) product.setSaleDiscountId(saleDiscountId);
                    identityMap.cache(id, product);
                    products.add(product);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null) closeConnection(connection);
        }
        return products;
    }

    public List<Product> getProducts(int categoryId) {
        Connection connection = null;
        List<Product> products = new ArrayList<>();
        try {
            connection = connect();
            Statement stmt = connection.createStatement();
            ResultSet result = stmt.executeQuery("Select * from Products where category = " + categoryId);
            while (result.next()) {
                int id = result.getInt("id");
                if (identityMap.contains(id)) products.add(identityMap.retrieve(id));
                else {
                    Product product = new Product(result.getString(2), result.getInt(4), result.getInt(3),
                            result.getString(5), result.getFloat(6), result.getFloat("weight"));
                    product.setId(id);
                    product.setAmount(result.getInt(8));
                    int saleDiscountId = result.getInt(7);
                    if (saleDiscountId != -1) product.setSaleDiscountId(saleDiscountId);
                    identityMap.cache(id, product);
                    products.add(product);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null) closeConnection(connection);
        }
        return products;
    }

    public Product getProductByDiscount(int discountId) {
        Connection connection = null;
        Product product = null;
        try {
            connection = connect();
            Statement stmt = connection.createStatement();
            ResultSet result = stmt.executeQuery(String.format("Select * from Products where saleDiscount = '%d'", discountId));
            if (result.next()) {
                product = new Product(result.getString(2), result.getInt(4), result.getInt(3),
                        result.getString(5), result.getFloat(6), result.getFloat("weight"));
                product.setId(result.getInt("id"));
                product.setAmount(result.getInt(8));
                int saleDiscountId = result.getInt(7);
                if (saleDiscountId != -1) product.setSaleDiscountId(saleDiscountId);
            }
            if (product != null) identityMap.cache(product.getId(), product);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null) closeConnection(connection);
        }
        return product;
    }

}
