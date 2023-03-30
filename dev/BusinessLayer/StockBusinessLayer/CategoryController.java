package BusinessLayer.StockBusinessLayer;

import DataAccessLayer.StockDAL.CategoryDAO;

import java.util.*;

public class CategoryController {

    private final CategoryDAO catDAO;

    public CategoryController() {
        catDAO = new CategoryDAO();
    }

    public Category getCategory(int id) {
        return catDAO.get(id);
    }

    public boolean removeCategory(int id) {
        return catDAO.delete(id);
    }

    public Category addCategory(String name, Integer superId) throws Exception {
        Category c = new Category(name, superId);
        if (!catDAO.insert(c)) throw new Exception("Adding of new category failed.");
        else return c;
    }

    public List<Category> getTopLevelCategories() {
        return catDAO.getTopLevelCategories();
    }

    public List<Category> getSubCategories(int categoryId) {
        return catDAO.getSubCategories(categoryId);
    }

    public boolean editCategory(int categoryId, String name) {
        return catDAO.update(categoryId, "name", name) == 1;
    }
}
