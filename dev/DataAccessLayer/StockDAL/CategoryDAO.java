package DataAccessLayer.StockDAL;

import BusinessLayer.StockBusinessLayer.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO extends DAO<Category> {
    private static final String tableName = "Categories";

    public CategoryDAO() {
        super(tableName);
    }

    public boolean insert(Category category) {
        Connection connection = null;
        boolean output = false;
        try {
            connection = connect();
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(String.format("INSERT INTO %s VALUES (NULL, '%s', %d)", tableName, category.getName(), category.getSuperId()));
            ResultSet res = stmt.getGeneratedKeys();
            if (res.next()) category.setId(res.getInt(1));
            output = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null) closeConnection(connection);
        }
        identityMap.cache(category.getId(), category);
        return output;
    }

    public Category get(int id) {
        Category category = identityMap.retrieve(id);
        if (category == null) {
            Connection connection = null;
            try {
                connection = connect();
                Statement stmt = connection.createStatement();
                ResultSet result = stmt.executeQuery(String.format("Select * from Categories where id = '%d'", id));
                while (result.next()) {
                    category = new Category(id, result.getString("name"), result.getInt("super"));
                }
                if (category != null) identityMap.cache(id, category);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            finally {
                if (connection != null) closeConnection(connection);
            }
        }
        return category;
    }

    public List<Category> getTopLevelCategories() {
        Connection connection = null;
        List<Category> subCategories = new ArrayList<>();
        try {
            connection = connect();
            Statement stmt = connection.createStatement();
            ResultSet result = stmt.executeQuery("Select * from Categories where super IS NULL");
            while (result.next()) {
                int id = result.getInt("id");
                Category category = new Category(id, result.getString("name"), null);
                subCategories.add(category);
                identityMap.cache(id, category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null) closeConnection(connection);
        }
        return subCategories;
    }

    public List<Category> getSubCategories(int id) {
        Connection connection = null;
        List<Category> subCategories = new ArrayList<>();
        try {
            connection = connect();
            Statement stmt = connection.createStatement();
            ResultSet result = stmt.executeQuery(String.format("Select * from Categories where super = '%d'", id));
            while (result.next()) {
                int subId = result.getInt("id");
                Category sub = new Category(subId, result.getString("name"), id);
                subCategories.add(sub);
                identityMap.cache(id, sub);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null) closeConnection(connection);
        }
        return subCategories;
    }

}
