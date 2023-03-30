package ServiceLayer.StockServiceLayer;

import ServiceLayer.StockServiceLayer.obj.*;
import ServiceLayer.SupplierServiceLayer.Obj.ServiceOrder;
import ServiceLayer.SupplierServiceLayer.SupService;
import BusinessLayer.StockBusinessLayer.*;
import ServiceLayer.ResponseT;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StockService {

    private static StockService instance = null;
    private final StockController stockController;
    private final SupService supService;

    public static StockService getInstance() {
        if (instance == null) instance = new StockService();
        return instance;
    }

    private StockService() {
        this.supService = SupService.getInstance();
        stockController = new StockController();
    }

    public ResponseT<List<serviceProduct>> underMinimumQuantities() {
        try {
            List<Product> list = stockController.underMinimumQuantities();
            ArrayList<serviceProduct> output = new ArrayList<>();
            for (Product p : list) {
                output.add(new serviceProduct(p));
            }
            return ResponseT.fromValue(output);
        }
        catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<Integer> underMinimumQuantitiesNumber() {
        try {
            return ResponseT.fromValue(stockController.underMinimumQuantitiesNumber());
        }
        catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<List<serviceCategory>> getTopLevelCategories() {
        try {
            List<Category> list = stockController.getTopLevelCategories();
            List<serviceCategory> output = new ArrayList<>();
            for (Category c : list) {
                output.add(new serviceCategory(c));
            }
            return ResponseT.fromValue(output);
        }
        catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<Boolean> addProductUnit(int productId, Date expirationDate, int location, boolean isInStorage, serviceSupplierDiscount discount, int amount) {
        try {
            SupplierDiscount supDiscount = null;
            if (discount != null) supDiscount = new SupplierDiscount(discount.getDiscount(), discount.getSupplierID());
            boolean b = stockController.addProductUnit(productId, expirationDate, isInStorage, location, supDiscount, amount);
            return ResponseT.fromValue(b);
        }
        catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<serviceCategory> addCategory(String name, Integer superId) {
        try {
            serviceCategory c = new serviceCategory(stockController.addCategory(name, superId));
            return ResponseT.fromValue(c);
        }
        catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<Boolean> addProduct(String name, String producer, int minQuantity, int categoryId, float salePrice, float weight) {
        try {
            ResponseT<Boolean> res = ResponseT.fromValue(stockController.addProduct(name, producer, minQuantity,
                    categoryId, salePrice, weight));
            supService.makeShortageOrder(name, producer, minQuantity * 2);

            return res;
        }
        catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<serviceSaleDiscount> setSaleDiscount(int productId, float discount, Date start, Date end) {
        try {
            serviceSaleDiscount saleDiscount = new serviceSaleDiscount(stockController.setSaleDiscount(productId, start, end, discount));
            return ResponseT.fromValue(saleDiscount);
        }
        catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<serviceDefectedProductsReport> getDefectedProductUnits() {
        try {
            List<ProductUnit> defectedProductUnits = stockController.getDefectedProductUnits();
            List<serviceProductUnit> list = new ArrayList<>();
            for (ProductUnit p : defectedProductUnits) {
                list.add(new serviceProductUnit(p));
            }
            return ResponseT.fromValue(new serviceDefectedProductsReport(list));
        }
        catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<Boolean> markProductUnitAsDefected(int productID) {
        try {
            return ResponseT.fromValue(stockController.markProductUnitAsDefected(productID));
        }
        catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<Boolean> changeProductCategory(int productId, int categoryId) {
        try {
            return ResponseT.fromValue(stockController.changeProductCategory(productId, categoryId));
        }
        catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<List<serviceCategory>> getSubCategories(int categoryId) {
        try {
            List<serviceCategory> output = new ArrayList<>();
            for (Category c : stockController.getSubCategories(categoryId)) {
                output.add(new serviceCategory(c));
            }
            return ResponseT.fromValue(output);
        }
        catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<List<serviceProduct>> getProducts(int categoryId) {
        try {
            List<serviceProduct> output = new ArrayList<>();
            for (Product p : stockController.getProducts(categoryId)) {
                output.add(new serviceProduct(p));
            }
            return ResponseT.fromValue(output);
        }
        catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<List<serviceProductUnit>> getProductUnits(int id) {
        try {
            List<serviceProductUnit> output = new ArrayList<>();
            for (ProductUnit p : stockController.getProductUnits(id)) {
                output.add(new serviceProductUnit(p));
            }
            return ResponseT.fromValue(output);
        }
        catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<serviceSupplierDiscount> getSupplierDiscount(int supplierDiscountId) {
        try {
            serviceSupplierDiscount discount = new serviceSupplierDiscount(stockController.getSupplierDiscount(supplierDiscountId));
            return ResponseT.fromValue(discount);
        }
        catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<serviceSaleDiscount> getSaleDiscount(int saleDiscountId) {
        try {
            serviceSaleDiscount discount = new serviceSaleDiscount(stockController.getSaleDiscount(saleDiscountId));
            return ResponseT.fromValue(discount);
        }
        catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<Integer> removeProductUnit(int unitId) {
        try {
            Product product = stockController.getProduct(stockController.getProductUnit(unitId).getProductId());
            boolean makeShortageOrder = product.getAmount() >= product.getMinQuantity();
            if (stockController.removeProductUnit(unitId)) {
                if (product.getAmount() < product.getMinQuantity() && makeShortageOrder) {
                    int amount = product.getMinQuantity() * 2 - product.getAmount();
                    return supService.makeShortageOrder(product.getName(), product.getProducer(), amount);
                }
                return ResponseT.fromValue(-1);
            }
            else throw new Exception("Removal Failed.");
        }
        catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<Boolean> receiveOrder(int productId, int amount, Date expirationDate, boolean isInStorage, int location, int orderId) {
        SupplierDiscount discount = null;
        ResponseT<ServiceOrder> orderResponse = supService.getOrder(orderId);
        if (orderResponse.isError()) return ResponseT.fromError("Invalid Order ID.");
        else {
            ServiceOrder order = orderResponse.getData();
            Product product = stockController.getProduct(productId);
            for (ServiceOrder.ServiceOrderLine line : order.orderLines) {
                if (product.getName().equals(line.productName) && line.discount != 0)
                    discount = new SupplierDiscount((float) line.discount, order.supplierId);
            }
            if (discount == null) return ResponseT.fromError("Invalid Order ID.");
        }
        try {
            ResponseT<Boolean> res = ResponseT.fromValue(stockController.addProductUnit(productId, expirationDate, isInStorage, location, discount, amount));
            if (!res.isError()) supService.orderArrived(orderId);
            return res;
        }
        catch (SQLException e) {
            return ResponseT.fromError("Product Unit Addition Failed - " + e.getMessage());
        }
    }

    public ResponseT<Boolean> editProduct(int productId, String name, String producer, int minQuantity, float salePrice, float weight) {
        try {
            Product product = stockController.editProduct(productId, name, producer, minQuantity, salePrice, weight);
            if (product.getAmount() < product.getMinQuantity()) {
                int amount = product.getMinQuantity() * 2 - product.getAmount();
                SupService.getInstance().makeShortageOrder(product.getName(), product.getProducer(), amount);
            }
            return ResponseT.fromValue(true);
        }
        catch (Exception e) {
            return ResponseT.fromError("Product Edit Failed - " + e.getMessage());
        }
    }

    public ResponseT<Boolean> removeProduct(int productId) {
        try {
            return ResponseT.fromValue(stockController.removeProduct(productId));
        }
        catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<Boolean> removeCategory(int categoryId) {
        try {
            return ResponseT.fromValue(stockController.removeCategory(categoryId));
        }
        catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<Boolean> editCategory(int categoryId, String name) {
        try {
            return ResponseT.fromValue(stockController.editCategory(categoryId, name));
        }
        catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<serviceProduct> getProduct(int productId) {
        try {
            return ResponseT.fromValue(new serviceProduct(stockController.getProduct(productId)));
        }
        catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<serviceProductUnit> getProductUnit(int unitId) {
        try {
            return ResponseT.fromValue(new serviceProductUnit(stockController.getProductUnit(unitId)));
        }
        catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<Boolean> editProductUnit(int unitId, Date expirationDate, int location, boolean isInStorage) {
        try {
            return ResponseT.fromValue(stockController.editProductUnit(unitId, expirationDate, location, isInStorage));
        }
        catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<Boolean> editSaleDiscount(int discountId, float discount, Date start, Date end) {
        try {
            return ResponseT.fromValue(stockController.editSaleDiscount(discountId, discount, start, end));
        }
        catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<Boolean> removeSaleDiscount(int discountId) {
        try {
            return ResponseT.fromValue(stockController.removeSaleDiscount(discountId));
        }
        catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }
}
