package BusinessLayer.StockBusinessLayer;

import java.sql.SQLException;
import java.util.*;

public class StockController {

    private final CategoryController catCon;
    private final ProductController prodCon;
    private final ProductUnitController prodUnitCon;
    private final DiscountController discountCon;

    public StockController() {
        catCon = new CategoryController();
        prodCon = new ProductController();
        prodUnitCon = new ProductUnitController();
        discountCon = new DiscountController();
    }

    public boolean addProduct(String name, String producer, int minQuantity,
                              int categoryId, float salePrice, float weight) throws Exception{
        prodCon.addProduct(name, producer, minQuantity, categoryId, salePrice, weight);
        return true;
    }

    public SaleDiscount setSaleDiscount(int productId, Date start, Date end, float discount) throws Exception {
        SaleDiscount saleDiscount = new SaleDiscount(start, end, discount);
        Product p = prodCon.getProduct(productId);
        if (p == null) throw new Exception("Product Not Found.");
        else {
            prodCon.setSaleDiscount(productId, saleDiscount.getId());
            discountCon.addSaleDiscount(saleDiscount);
        }

        return saleDiscount;
    }

    public Category addCategory(String name, Integer superId) throws Exception {
        return catCon.addCategory(name, superId);
    }

    public List<ProductUnit> getDefectedProductUnits() {
        return prodUnitCon.getDefectedProductUnits();
    }

    public boolean markProductUnitAsDefected(int productID) {
        return prodUnitCon.markProductUnitAsDefected(productID);
    }

    public boolean changeProductCategory(int productId, int categoryId) {
        return prodCon.changeProductCategory(productId, categoryId);
    }

    public Category getCategory(int id) {
        return catCon.getCategory(id);
    }

    public boolean addProductUnit(int productId, Date expirationDate, boolean isInStorage, int location, SupplierDiscount discount, int amount) throws SQLException {
        if (discount != null) discountCon.addSupplierDiscount(discount);
        boolean addSuccess = prodUnitCon.addProductUnit(productId, expirationDate, isInStorage, location, discount != null ? discount.getId() : -1, amount);
        if (addSuccess) {
            prodCon.addAmount(productId, amount);
        }
        return addSuccess;
    }

    public List<Product> underMinimumQuantities() {
        return prodCon.underMinimumQuantities();
    }

    public List<Category> getTopLevelCategories() {
        return catCon.getTopLevelCategories();
    }

    public int underMinimumQuantitiesNumber() {
        return underMinimumQuantities().size();
    }

    public List<Category> getSubCategories(int categoryId) {
        return catCon.getSubCategories(categoryId);
    }

    public List<Product> getProducts(int categoryId) {
        return prodCon.getProducts(categoryId);
    }

    public List<ProductUnit> getProductUnits(int id) {
        return prodUnitCon.getProductUnits(id);
    }

    public SupplierDiscount getSupplierDiscount(int supplierDiscountId) {
        return discountCon.getSupplierDiscount(supplierDiscountId);
    }

    public SaleDiscount getSaleDiscount(int saleDiscountId) {
        return discountCon.getSaleDiscount(saleDiscountId);
    }

    public boolean removeProductUnit(int unitId) {
        int productId = prodUnitCon.getProductUnit(unitId).getProductId();
        boolean removalSuccess = prodUnitCon.removeProductUnit(unitId);
        if (removalSuccess) prodCon.reduceAmount(productId);
        return removalSuccess;
    }

    public ProductUnit getProductUnit(int unitId) {
        return prodUnitCon.getProductUnit(unitId);
    }

    public Product getProduct(int productId) {
        return prodCon.getProduct(productId);
    }

    public Product editProduct(int productId, String name, String producer, int minQuantity, float salePrice, float weight) {
        return prodCon.editProduct(productId, name, producer, minQuantity, salePrice, weight);
    }

    public boolean removeProduct(int productId) throws Exception {
        return prodCon.removeProduct(productId);
    }

    public boolean removeCategory(int categoryId) throws Exception {
        if (getSubCategories(categoryId).isEmpty() && getProducts(categoryId).isEmpty())
            return catCon.removeCategory(categoryId);
        else throw new Exception("Category can't be deleted while having sub-categories and/or products.");
    }

    public boolean editCategory(int categoryId, String name) {
        return catCon.editCategory(categoryId, name);
    }

    public boolean editProductUnit(int unitId, Date expirationDate, int location, boolean isInStorage) {
        return prodUnitCon.editProductUnit(unitId, expirationDate, location, isInStorage);
    }

    public boolean editSaleDiscount(int discountId, float discount, Date start, Date end) {
        return discountCon.editSaleDiscount(discountId, discount, start,end);
    }

    public boolean removeSaleDiscount(int discountId) {
        discountCon.removeDiscount(discountId);
        Product product = prodCon.getProductByDiscount(discountId);
        prodCon.setSaleDiscount(product.getId(), -1);
        return true;
    }
}
