package BusinessLayer.SupplierBusinessLayer;

import DataAccessLayer.StockDAL.ProductDAO;
import DataAccessLayer.SuppliersDAL.SupplierProductDAO;

import java.util.*;
import java.util.stream.Collectors;

public class ProductController {

    private static ProductController instance = null;
    private SupplierProductDAO supplierProductDAO;
    private SupplierController supplierController;

    private ProductController() {
        supplierProductDAO = new SupplierProductDAO();
        supplierController = SupplierController.getInstance();
    }

    public static ProductController getInstance() {
        if (instance == null) instance = new ProductController();
        return instance;
    }

    public List<SupplierProduct> getAllProducts() {
        return new ArrayList<SupplierProduct>(supplierProductDAO.getProductsForSupplier().keySet());
    }

    public List<SupplierProduct> getProducts(String name, String manufacturer) {
        return getAllProducts().stream().filter(product -> product.getName().equals(name) && product.getManufacturer().equals(manufacturer)).collect(Collectors.toList());
    }

    public List<SupplierProduct> getProducts(int supplierId) {
        Map<SupplierProduct, Integer> productIntegerMap = supplierProductDAO.getProductsForSupplier();
        return productIntegerMap.keySet().stream().filter(product -> productIntegerMap.get(product) == supplierId).collect(Collectors.toList());
    }

    public SupplierProduct getProduct(int supplierId, String catalogNumber) {
        for (SupplierProduct product : getProducts(supplierId)) {
            if (product.getCatalogNumber().equals(catalogNumber))
                return product;
        }
        return null;
    }

    public SupplierProduct getProduct(int supplierId, String productName, String manufacturer) {
        for (SupplierProduct product : getProducts(productName, manufacturer)) {
            Map<SupplierProduct, Integer> products =  supplierProductDAO.getProductsForSupplier();
            for (SupplierProduct p : products.keySet()) {
                if (product.getCatalogNumber().equals(p.getCatalogNumber())) {
                    if (products.get(p) == supplierId)
                        return product;
                }
            }
        }
        return null;
    }

    public double getPrice(int supplierId, String productName, String manufacturer, int amount) throws Exception {
        Supplier supplier = SupplierController.getInstance().getSupplier(supplierId);
        if (supplier == null) throw new Exception("supplier " + supplierId + " doesn't exist.");
        SupplierProduct product = getProduct(supplierId, productName, manufacturer);
        if (product == null) throw new Exception("supplier " + supplierId + " has no product called " + productName + " made by " + manufacturer + ".");
        double discount = SupplierController.getInstance().getDiscount(supplierId, productName, manufacturer, amount);
        return product.getPrice() * amount * (100 - discount) / 100;
    }

    public String getCatalogNumber(int supplierId, String productName, String manufacturer) throws Exception {
        Supplier supplier = SupplierController.getInstance().getSupplier(supplierId);
        if (supplier == null) throw new Exception("supplier " + supplierId + " doesn't exist.");
        SupplierProduct product = getProduct(supplierId, productName, manufacturer);
        if (product == null) throw new Exception("supplier " + supplierId + " has no product called " + productName + " made by " + manufacturer + ".");
        return product.getCatalogNumber();
    }

    public void addProductToSupplier(int supplierId, String catalogNumber, String productName, double price, String manufacturer) throws Exception {
        Supplier supplier = SupplierController.getInstance().getSupplier(supplierId);
        if (supplier == null) throw new Exception("supplier " + supplierId + " doesn't exist.");
        SupplierProduct product = new SupplierProduct(catalogNumber, productName, price, manufacturer);
//        productToSupplierMap.put(product, supplierId);
        supplierProductDAO.insert(product, supplierId);
    }

    public int getCheapestSupplier(String productName, String manufacturer, int amount) throws Exception {
        List<SupplierProduct> products = getProducts(productName, manufacturer);
        if (products.isEmpty()) throw new Exception("no supplier has a product called " + productName + " made by " + manufacturer + ".");
        SupplierProduct cheap = null;
        int cheapestSupplier = -1;
        double cheapPrice = -1;
        for (SupplierProduct product : products) {
            int supplierId = -1;
            Map<SupplierProduct, Integer> productsMap = supplierProductDAO.getProductsForSupplier();
            for (SupplierProduct p : productsMap.keySet()) {
                if (product.equal(p)) {
                    supplierId = productsMap.get(p);
                }
            }
//            int supplierId = supplierProductDAO.getProductsForSupplier().get(product);
            if (supplierController.isOrderable(supplierId)) {
                double price = getPrice(supplierId, productName, manufacturer, amount);
                if (cheap == null){
                    cheap = product;
                    cheapestSupplier = supplierId;
                    cheapPrice = price;
                }
                if (price < cheapPrice){
                    cheap = product;
                    cheapPrice = price;
                    cheapestSupplier = supplierId;
                }
            }
        }
        return cheapestSupplier;
    }

    public void removeProducts(int supplierId) {
        supplierProductDAO.delete(supplierId);
    }
    public void removeProduct (int supplierId, String catalogNumber){ supplierProductDAO.delete(supplierId, catalogNumber);}

    public void updateProduct(int supplierId, String catalogNumber, String field, String content) {
        supplierProductDAO.update(supplierId, catalogNumber, field, content);
    }

    public int productToID(SupplierProduct product) {
        ProductDAO dao = new ProductDAO();
        return dao.getId(product.getName(), product.getManufacturer());
    }

//    public void loadProducts() {
//        productToSupplierMap = supplierProductDAO.getProductsForSupplier();
//    }
}
