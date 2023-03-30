package BusinessLayer.SupplierBusinessLayer;

import DataAccessLayer.SuppliersDAL.OrderDAO;
import DataAccessLayer.SuppliersDAL.OrderLineDAO;
import DataAccessLayer.SuppliersDAL.OthersDAO;
import DataAccessLayer.SuppliersDAL.ShipTimeDAO;
import ServiceLayer.TransportServiceLayer.TransportService;

import java.util.List;
import java.util.Map;
import java.time.Instant;
import java.util.*;

public class OrderController {

    private static OrderController instance = null;
    private int orderId;
    private int orderLineId;
    private final String genericAddress = "Rager 99, Beer-Sheva";
    private OthersDAO othersDao;
    private OrderDAO orderDAO;
    private OrderLineDAO orderLineDAO;
    private ShipTimeDAO shipTimeDAO;
    private OrderController() {
        othersDao = new OthersDAO();
        orderId = othersDao.getInt("count order id");
        orderLineId = othersDao.getInt("count order line id");
        orderDAO = new OrderDAO();
        orderLineDAO = new OrderLineDAO();
        shipTimeDAO = new ShipTimeDAO();
    }

    public static OrderController getInstance() {
        if (instance == null) instance = new OrderController();
        return instance;
    }

    public List<Order> getAllOrders() {
        List<Order> orders = new LinkedList<>();
        for (int i = 0; i<orderId; i++) {
            if (orderDAO.get(i) != null) {
                Order order = orderDAO.get(i);
                orders.add(orderLineDAO.initialize(order));
            }
        }
        return orders;
    }

    public List<Order> getShortageOrders() {
        List<Order> orders = new ArrayList<>();
        for (Order order : getAllOrders()) {
            if (!order.isWeekly())
                orders.add(order);
        }
        return orders;
    }

    public List<Order> getWeeklyOrders() {
        List<Order> orders = new ArrayList<>();
        for (Order order : getAllOrders()) {
            if (order.isWeekly())
                orders.add(order);
        }
        return orders;
    }

    public List<Order> getOrders(int supplierId) {
        List<Order> orders = new ArrayList<>();
        for (Order order : getAllOrders()) {
            if (order.getSupplierId() == supplierId)
                orders.add(order);
        }
        return orders;
    }

    public Order getOrder(int orderId) {
        for (Order order : getAllOrders()) {
            if (order.getId() == orderId)
                return order;
        }
        return null;
    }

    public Order getWeeklyOrder(int supplierId) {
        for (Order order : getOrders(supplierId)) {
            if (order.isWeekly() && !order.isArrived()) return order;
        }
        return null;
    }

    public boolean hasWeeklyOrder(int supplierId) {
        return getWeeklyOrder(supplierId) != null;
    }

    public void orderArrived(int orderId) throws Exception {
        Order order = getOrder(orderId);
        order.arrived();
        orderDAO.update(orderId, "isArrived", 1);
        if (order.isWeekly()) makeSameWeeklyOrder(order);
    }

    private void makeSameWeeklyOrder(Order order) throws Exception {
        int supplierId = order.getSupplierId();
        Map<String, Integer> productMap = new HashMap<String, Integer>();
        for (Order.OrderLine line : order.getOrderLines()) {
            productMap.put(line.catalogNumber, line.amount);
        }
        makeWeeklyOrder(supplierId, productMap);
    }

    private boolean isWeeklyOrder(int orderId) {
        Order order = getOrder(orderId);
        return order.isWeekly();
    }

    private boolean isUpdatable(int orderId) {
        if (isWeeklyOrder(orderId)) {
            int today = Date.from(Instant.now()).getDay() + 1;
            int orderDay = shipTimeDAO.getDay(orderId);
            return !todayOrTomorrow(today, orderDay);
        }
        return false;
    }

    private boolean todayOrTomorrow(int today, int orderDay) {
        if (today == 7 && orderDay == 1) return true;
        return orderDay - today == 0 || orderDay - today == 1;

    }

    public int makeWeeklyOrder(int supplierId, Map<String, Integer> catalogNumberToAmountMap) throws Exception {
        int day = SupplierController.getInstance().getSupplierConstantDay(supplierId);
        if (day == 0) throw new Exception("supplier " + supplierId + " isn't doing weekly orders.");
        Order order = getWeeklyOrder(supplierId);
        if (order != null) throw new Exception("supplier " + supplierId + " already has a weekly order.");
        order = openOrder(supplierId, true);
        Map<Integer, Integer> productIdToAmount = new HashMap<Integer, Integer>();
        for (String CN : catalogNumberToAmountMap.keySet()) {
            SupplierProduct product = ProductController.getInstance().getProduct(supplierId, CN);
            if (product == null) {
                deleteOrder(order.getId());
                throw new Exception("supplier " + supplierId + " has no such product with CN " + CN);
            }
            int amount = catalogNumberToAmountMap.get(CN);
            addProductToOrder(supplierId, order, product, amount);
            int id = ProductController.getInstance().productToID(product);
            productIdToAmount.put(id, amount);
        }
        sendOrder(order);
        String area = SupplierController.getInstance().getArea(supplierId);
        makeTransport(supplierId, order.getId(), day, productIdToAmount, area);
        shipTimeDAO.insert(orderId, day);
        return order.getId();
    }

    public int updateWeeklyOrder(int supplierId, Map<String, Integer> catalogNumberToAmountMap) throws Exception {
        Order order = getWeeklyOrder(supplierId);
        if (order == null) throw new Exception("supplier " + supplierId + " has no weekly order.");
        if (!isUpdatable(order.getId())) throw new Exception("order " + order.getId() + " can't be updated because it will arrive today or tomorrow.");
        Map<Integer, Integer> productIdToAmount = new HashMap<Integer, Integer>();
        for (String CN : catalogNumberToAmountMap.keySet()) {
            SupplierProduct product = ProductController.getInstance().getProduct(supplierId, CN);
            int amount = catalogNumberToAmountMap.get(CN);
            if (hasLine(order, product)) {
                removeLine(order, product);
            }
            Order.OrderLine orderLine = addProductToOrder(supplierId, order, product, amount);
            orderLineDAO.delete(order.getId(), CN);
            orderLineDAO.insert(orderLine, order.getId());
            int id = ProductController.getInstance().productToID(product);
            productIdToAmount.put(id, amount);
        }
        updateTransport(order.getId(), productIdToAmount);
        return order.getId();
    }

    public void removeLine(Order order, SupplierProduct product) {
        int orderLineId = order.removeLine(product);
        orderLineDAO.delete(orderLineId);
    }

    public boolean hasLine(Order order, SupplierProduct product) {
        return order.hasLine(product);
    }

    public int makeShortageOrder(String productName, String manufacturer, int amount) throws Exception {
        int cheapestSupplierId = ProductController.getInstance().getCheapestSupplier(productName, manufacturer, amount);
        Order order = openOrder(cheapestSupplierId, false);
        SupplierProduct product = ProductController.getInstance().getProduct(cheapestSupplierId, productName, manufacturer);
        int id = ProductController.getInstance().productToID(product);
        Map<Integer, Integer> productIdToAmount = new HashMap<Integer, Integer>();
        productIdToAmount.put(id, amount);
        addProductToOrder(cheapestSupplierId, order, product, amount);
        sendOrder(order);
        String area = SupplierController.getInstance().getArea(cheapestSupplierId);
        makeTransport(cheapestSupplierId, order.getId(), 0, productIdToAmount, area);
        return order.getId();
    }

    private Order.OrderLine addProductToOrder(int supplierId, Order order, SupplierProduct product, int amount) throws Exception {
        Supplier supplier = SupplierController.getInstance().getSupplier(supplierId);
        if (supplier == null) throw new Exception("supplier " + supplierId + " doesn't exist.");
        double discount = SupplierController.getInstance().getDiscount(supplier.getId(), product.getName(), product.getManufacturer(), amount);
        double finalPrice = ProductController.getInstance().getPrice(supplier.getId(), product.getName(), product.getManufacturer(), amount);
        Order.OrderLine orderLine = order.addLine(product, amount, discount, finalPrice, orderLineId);
        orderLineId++;
        othersDao.updateInt("count order line id", orderLineId);
        return orderLine;
    }

    private Order openOrder(int supplierId, boolean weekly) throws Exception {
        Supplier supplier = SupplierController.getInstance().getSupplier(supplierId);
        if (supplier == null) throw new Exception("supplier " + supplierId + " doesn't exist.");
//        if (!SupplierController.getInstance().isOrderable(supplierId)) throw new Exception("supplier " + supplierId + " is not ready to take orders.");
        List<Contact> contacts = ContactController.getInstance().getSupplierContacts(supplierId);
        Contact contact = contacts.get(0);
        Order order = new Order(orderId, supplier, genericAddress, contact, weekly);
        orderId++;
        othersDao.updateInt("count order id", orderId);
        orderDAO.insert(order);
//        orders.add(order);
        return order;
    }

    private void sendOrder(Order order) {
        for(Order.OrderLine orderLine : order.getOrderLines())
            orderLineDAO.insert(orderLine, order.getId());
        order.ready();
    }

    public void deleteOrder(int orderId){
        orderDAO.delete(orderId);
        orderLineDAO.delete(orderId);
        shipTimeDAO.delete(orderId);
        cancelTransport(orderId);
    }

    public void makeTransport(int supplierId, int orderId, int day, Map<Integer, Integer> productsToAmounts, String area) {
        TransportService.getInstance().makeTransportSupp(supplierId, orderId, day,  productsToAmounts, area);
        System.out.println("###############################################\n" +
                "#####################----MAKE----########################\n" +
                "#######################################################");
    }

    public void updateTransport(int orderId, Map<Integer, Integer> productsToAmounts) {
        TransportService.getInstance().updateTransport(orderId, productsToAmounts);
        System.out.println("###############################################\n" +
                "#####################----UPDATE----########################\n" +
                "#######################################################");
    }

    public void cancelTransport(int orderId) {
        TransportService.getInstance().removeTransportDocumentSupp(orderId);
        System.out.println("###############################################\n" +
                "#####################----REMOVE----########################\n" +
                "#######################################################");
    }


}
