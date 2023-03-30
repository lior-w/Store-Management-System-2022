package ServiceLayer.SupplierServiceLayer;

import BusinessLayer.StockBusinessLayer.Product;
import BusinessLayer.TransportBusinessLayer.ShippingArea;
import ServiceLayer.Response;
import ServiceLayer.ResponseT;
import ServiceLayer.SupplierServiceLayer.Obj.*;
import BusinessLayer.SupplierBusinessLayer.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class SupService {

    private static SupService instance = null;
    private final SupplierController supplierController;
    private final ProductController productController;
    private final OrderController orderController;
    private final ContactController contactController;


    private SupService() {
        supplierController = SupplierController.getInstance();
        productController = ProductController.getInstance();
        orderController = OrderController.getInstance();
        contactController = ContactController.getInstance();

    }

    public static SupService getInstance() {
        if (instance == null)
            instance = new SupService();
        return instance;
    }

    //********************supplier********************//

    public ResponseT<List<ServiceSupplier>> getSuppliers() {
        try {
            List<Supplier> list = supplierController.getSuppliers();
            return ResponseT.fromValue(list.stream().map(ServiceSupplier::new).collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<List<ServiceSupplier>> getSuppliersWithConstantDay() {
        try {
            List<Supplier> suppliers = supplierController.getSuppliersWithConstantDay();
            return ResponseT.fromValue(suppliers.stream().map(ServiceSupplier::new).collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<List<ServiceSupplier>> getSuppliersWithWeeklyOrder() {
        try {
            List<Supplier> suppliers = supplierController.getSuppliersWithWeeklyOrder();
            return ResponseT.fromValue(suppliers.stream().map(ServiceSupplier::new).collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<Integer> getSupplierConstantDay(int supplierId) {
        try {
            int day = supplierController.getSupplierConstantDay(supplierId);
            return ResponseT.fromValue(day);
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }

    }

    public ResponseT<ServiceSupplier> getSupplier(int supplierId) {
        try {
            Supplier supplier = supplierController.getSupplier(supplierId);
            return ResponseT.fromValue(new ServiceSupplier(supplier));
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public Response addSupplier(String name, int companyNumber, int bankAccount, Supplier.PayWay payWay, String address, ShippingArea area) {
        try {
            supplierController.addSupplier(name, companyNumber, bankAccount, payWay, address, area);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response removeSupplier(int supplierId) {
        try {
            supplierController.removeSupplier(supplierId);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response updateSupplier(int supplierId, String field, String content) {
        try {
            supplierController.updateSupplier(supplierId,field, content);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public ResponseT<ServiceSupplierContract> getContract(int supplierId) {
        try {
            SupplierContract contract = supplierController.getContract(supplierId);
            return ResponseT.fromValue(new ServiceSupplierContract(contract));
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public Response setContract(int supplierId, int constantDay, boolean independentShipping) {
        try {
            supplierController.setContract(supplierId, constantDay, independentShipping);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }


    public Response removeContract(int supplierId) {
        try {
            supplierController.removeContract(supplierId);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public ResponseT<Double> getDiscount(int supplierId, String productName, String manufacturer, int amount) {
        try {
            double discount = supplierController.getDiscount(supplierId, productName, manufacturer, amount);
            return ResponseT.fromValue(discount);
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public Response addDiscount(int supplierId, String productName, String manufacturer, int fromAmount, double discount) {
        try {
            supplierController.addDiscount(supplierId, productName, manufacturer, fromAmount, discount);
            return new Response();
        }
        catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response removeDiscount(int supplierId, String catalogNumbar, int fromAmount) {
        try {
            supplierController.removeDiscount(supplierId, catalogNumbar, fromAmount);
            return new Response();
        }
        catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public ResponseT<Map<Integer, Double>> getDiscounts(int supplierID, String productName, String manufacturer) {
        try {

            Map<Integer, Double> discounts = supplierController.getDiscounts(supplierID, productName, manufacturer);
            return ResponseT.fromValue(discounts);
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<Boolean> isShipping(int supplierId) {
        try {
            boolean ans = supplierController.isShipping(supplierId);
            return ResponseT.fromValue(ans);
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<Boolean> isConstantDay(int supplierId) {
        try {
            boolean ans = supplierController.isConstantDay(supplierId);
            return ResponseT.fromValue(ans);
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<Boolean> hasQuantitiesDocument(int supplierId) {
        try {
            boolean ans = supplierController.hasQuantitiesDocument(supplierId);
            return ResponseT.fromValue(ans);
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<Boolean> isOrderable(int supplierId) {
        try {
            boolean ans = supplierController.isOrderable(supplierId);
            return ResponseT.fromValue(ans);
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<String> getAddress(int supplierId) {
        try {
            String address = supplierController.getAddress(supplierId);
            return ResponseT.fromValue(address);
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<String> getArea(int supplierId) {
        try {
            String area = supplierController.getArea(supplierId);
            return ResponseT.fromValue(area);
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    //********************product********************//

    public ResponseT<List<ServiceProduct>> getAllProducts() {
        try {
            List<SupplierProduct> products = productController.getAllProducts();
            return ResponseT.fromValue(products.stream().map(ServiceProduct::new).collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<List<ServiceProduct>> getProducts(String name, String manufacturer) {
        try {
            List<SupplierProduct> products = productController.getProducts(name, manufacturer);
            return ResponseT.fromValue(products.stream().map(ServiceProduct::new).collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<List<ServiceProduct>> getProducts(int supplierId) {
        try {
            List<SupplierProduct> products = productController.getProducts(supplierId);
            return ResponseT.fromValue(products.stream().map(ServiceProduct::new).collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<ServiceProduct> getProduct(int supplierId, String catalogNumber) {
        try {
            SupplierProduct product = productController.getProduct(supplierId, catalogNumber);
            return ResponseT.fromValue(new ServiceProduct(product));
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<ServiceProduct> getProduct(int supplierId, String productName, String manufacturer) {
        try {
            SupplierProduct product = productController.getProduct(supplierId, productName, manufacturer);
            return ResponseT.fromValue(new ServiceProduct(product));
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<Double> getPrice(int supplierId, String productName, String manufacturer, int amount) {
        try {
            double price = productController.getPrice(supplierId, productName, manufacturer, amount);
            return ResponseT.fromValue(price);
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<String> getCatalogNumber(int supplierId, String productName, String manufacturer) {
        try {
            String CN = productController.getCatalogNumber(supplierId, productName, manufacturer);
            return ResponseT.fromValue(CN);
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public Response addProductToSupplier(int supplierId, String catalogNumber, String productName, double price, String manufacturer) {
        try {
            productController.addProductToSupplier(supplierId, catalogNumber, productName, price, manufacturer);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response removeProduct(int supplierId, String catalogNumber) {
        try {
            productController.removeProduct(supplierId, catalogNumber);
            return new Response();
        }
        catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response updateProduct(int supplierId, String CatalogNumber, String field, String content) {
        try {
            productController.updateProduct(supplierId, CatalogNumber, field, content);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public ResponseT<Integer> getCheapestSupplier(String productName, String manufacturer, int amount) {
        try {
            Integer supplierId = productController.getCheapestSupplier(productName, manufacturer, amount);
            return ResponseT.fromValue(supplierId);
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    //********************order********************//

    public ResponseT<List<ServiceOrder>> getAllOrders() {
        try {
            List<Order> orders = orderController.getAllOrders();
            return ResponseT.fromValue(orders.stream().map(ServiceOrder::new).collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<List<ServiceOrder>> getShortageOrders() {
        try {
            List<Order> orders = orderController.getShortageOrders();
            return ResponseT.fromValue(orders.stream().map(ServiceOrder::new).collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<List<ServiceOrder>> getWeeklyOrders() {
        try {
            List<Order> orders = orderController.getWeeklyOrders();
            return ResponseT.fromValue(orders.stream().map(ServiceOrder::new).collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<List<ServiceOrder>> getOrders(int supplierId) {
        try {
            List<Order> orders = orderController.getOrders(supplierId);
            return ResponseT.fromValue(orders.stream().map(ServiceOrder::new).collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<ServiceOrder> getOrder(int orderId) {
        try {
            ServiceOrder order = new ServiceOrder(orderController.getOrder(orderId));
            return ResponseT.fromValue(order);
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<ServiceOrder> getWeeklyOrder(int supplierId) {
        try {
            ServiceOrder order = new ServiceOrder(orderController.getWeeklyOrder(supplierId));
            return ResponseT.fromValue(order);
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<Boolean> hasWeeklyOrder(int supplierId) {
        try {
            boolean ans = orderController.hasWeeklyOrder(supplierId);
            return ResponseT.fromValue(ans);
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public Response orderArrived(int orderId) {
        try {
            orderController.orderArrived(orderId);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public ResponseT<Integer> makeWeeklyOrder(int supplierId, Map<String, Integer> catalogNumberToAmountMap) {
        try {
            int orderId = orderController.makeWeeklyOrder(supplierId, catalogNumberToAmountMap);
            return ResponseT.fromValue(orderId);
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public Response removeWeeklyOrder(int orderId) {
        try {
            orderController.deleteOrder(orderId);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public ResponseT<Integer> updateWeeklyOrder(int supplierId, Map<String, Integer> catalogNumberToAmountMap) {
        try {
            int orderId = orderController.updateWeeklyOrder(supplierId, catalogNumberToAmountMap);
            return ResponseT.fromValue(orderId);
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<Integer> makeShortageOrder(String productName, String manufacturer, int amount) {
        try {
            int orderId = orderController.makeShortageOrder(productName, manufacturer, amount);
            return ResponseT.fromValue(orderId);
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public Response removeShortageOrder(int orderId) {
        try {
            orderController.deleteOrder(orderId);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    //********************contact********************//

    public ResponseT<List<ServiceContact>> getSupplierContacts(int supplierId) {
        try {
            List<Contact> contacts = contactController.getSupplierContacts(supplierId);
            return ResponseT.fromValue(contacts.stream().map(ServiceContact::new).collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }
//
//    public ResponseT<ServiceContact> getContact(String phone) {
//        try {
//            Contact contact = contactController.getContact(phone);
//            return ResponseT.fromValue(new ServiceContact(contact));
//        } catch (Exception e) {
//            return ResponseT.fromError(e.getMessage());
//        }
//    }

    public Response addContactToSupplier(int supplierId, String name, String email, String phone) {
        try {
            contactController.addContactToSupplier(supplierId, name, email, phone);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response removeContact(int supplierId, String phone) {
        try {
            contactController.removeContact(supplierId, phone);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }


    public Response updateContact(int supplierId, String phone, String field, String content) {
        try {
            contactController.updateContact(supplierId,phone , field, content);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    //newaaaa
}

