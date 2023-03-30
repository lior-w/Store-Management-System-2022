package ServiceLayer.StockServiceLayer.obj;

import BusinessLayer.StockBusinessLayer.Category;

public class serviceCategory {
    private int categoryId;
    private String categoryName;
    private Integer superId;

    public serviceCategory(Category c) {
        categoryId = c.getId();
        categoryName = c.getName();
        superId = c.getSuperId();
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getSuperId() {
        return superId;
    }
}
