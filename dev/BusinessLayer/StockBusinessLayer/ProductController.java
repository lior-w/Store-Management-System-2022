package BusinessLayer.StockBusinessLayer;

import DataAccessLayer.StockDAL.ProductDAO;

import java.util.List;

public class ProductController {
    private final ProductDAO productDAO;

    public ProductController() {
        productDAO = new ProductDAO();
    }

    public Product addProduct(String name, String producer, int minQuantity,
                              int categoryId, float salePrice, float weight) throws Exception {
        Product product = new Product(name, minQuantity, categoryId, producer, salePrice, weight);
        if (productDAO.insert(product))
            return product;
        else throw new Exception("Product addition failed.");
    }

    public Product getProduct(int id) {
        return productDAO.get(id);
    }

    public boolean changeProductCategory(int productId, int categoryId) {
        productDAO.update(productId, "category", categoryId);
        productDAO.get(productId).setCategoryId(categoryId);
        return true;
    }

    public List<Product> underMinimumQuantities() {
        return productDAO.underMinimumQuantities();
    }

    public List<Product> getProducts(int categoryId) {
        return productDAO.getProducts(categoryId);
    }

    public void reduceAmount(int productId) {
        Product product = productDAO.get(productId);
        productDAO.update(productId, "amount", product.getAmount() - 1);
        product.setAmount(product.getAmount() - 1);
    }

    public void addAmount(int productId, int amount) {
        Product product = productDAO.get(productId);
        productDAO.update(productId, "amount", product.getAmount() + amount);
        product.setAmount(product.getAmount() + amount);
    }

    public void setSaleDiscount(int productId, int saleDiscount) {
        Product p = productDAO.get(productId);
        productDAO.update(productId, "saleDiscount", saleDiscount);
        p.setSaleDiscountId(saleDiscount);
    }

    public boolean removeProduct(int id) throws Exception {
        if (productDAO.get(id).getAmount() == 0) {
            return productDAO.delete(id);
        }
        else throw new Exception("Can't delete a product that still has stock.");
    }

    public Product editProduct(int productId, String name, String producer, int minQuantity, float salePrice, float weight) {
        Product p = productDAO.get(productId);
        productDAO.update(productId, "name", name);
        p.setName(name);
        productDAO.update(productId, "producer", producer);
        p.setProducer(producer);
        productDAO.update(productId, "minQuantity", minQuantity);
        p.setMinQuantity(minQuantity);
        productDAO.update(productId, "salePrice", salePrice);
        p.setSalePrice(salePrice);
        productDAO.update(productId, "weight", weight);
        p.setWeight(weight);
        return p;
    }

    public Product getProductByDiscount(int discountId) {
        return productDAO.getProductByDiscount(discountId);
    }
}
