package BusinessLayer.SupplierBusinessLayer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Order {


    public class OrderLine {
        public final int id;
        public int orderId;
        public final String catalogNumber;
        public final String productName;
        public final int amount;
        public final double price;
        public final double discount;
        public final double finalPrice;

        public OrderLine(SupplierProduct product, int amount, double discount, double finalPrice, int id, int orderId) {
            this.orderId = orderId;
            this.id = id;
            this.catalogNumber = product.getCatalogNumber();
            this.productName = product.getName();
            this.amount = amount;
            this.price = product.getPrice();
            this.discount = discount;
            this.finalPrice = finalPrice;
        }

        public OrderLine(int id, int orderId, String catalogNumber, String name, int amount, double price, double discount, double finalPrice) {
            this.id = id;
            this.catalogNumber = catalogNumber;
            this.productName = name;
            this.amount = amount;
            this.price = price;
            this.discount = discount;
            this.finalPrice = finalPrice;
        }

        public void InitializeOrderId(int orderId) {
            this.orderId = orderId;
        }

        public String toString() {
            return "CN: " + catalogNumber + " | " + "name: " + productName + " | " + "amount: " + amount + " | " + "price: " + price + " | " + "discount: " + discount + "%" + " | " + "final price: " + finalPrice;
        }
    }

    private final int orderId;
    private final int supplierId;
    private final String supplierName;
    private final String address;
    private final Date date;
    private final String contactPhone;
    private final List<OrderLine> orderLines;
    private boolean ready;
    private boolean arrived;
    private boolean weekly;
    private final Pattern VALID_ADDRESS_REGEX = Pattern.compile("^[[a-zA-Z-]+\\s]+\\d+\\,\\s[[a-zA-Z-]+\\s]+$", Pattern.CASE_INSENSITIVE);

    public Order(int orderId, Supplier supplier, String address, Contact contact, boolean weekly) throws Exception {
        if (!isValidAddress(address))
            throw new Exception("invalid address. please enter address in format: street name house number, city");
        this.orderId = orderId;
        this.supplierName = supplier.getName();
        this.supplierId = supplier.getId();
        this.address = address;
        this.date = Date.from(Instant.now());
        this.contactPhone = contact.getPhone();
        this.orderLines = new ArrayList<>();
        this.ready = false;
        this.arrived = false;
        this.weekly = weekly;
    }

    public Order(int orderId, String supplierName, int supplierId, String address, Contact contact, boolean weekly) throws Exception {
        if (!isValidAddress(address))
            throw new Exception("invalid address. please enter address in format: street name house number, city");
        this.orderId = orderId;
        this.supplierName = supplierName;
        this.supplierId = supplierId;
        this.address = address;
        this.date = Date.from(Instant.now());
        this.contactPhone = contact.getPhone();
        this.orderLines = new ArrayList<>();
        this.ready = false;
        this.arrived = false;
        this.weekly = weekly;
    }

    public Order(int orderId, String supplierName, int supplierId, String address, String contactPhone, boolean weekly, String date, boolean isReady, boolean isArrived) throws ParseException {
        this.orderId = orderId;
        this.supplierName = supplierName;
        this.supplierId = supplierId;
        this.address = address;
        this.date = new SimpleDateFormat("dd/MM/yyyy").parse(date);
        this.contactPhone = contactPhone;
        this.orderLines = new ArrayList<>();
        this.ready = isReady;
        this.arrived = isArrived;
        this.weekly = weekly;
    }

//    public Date bookNextWeek(){
//        int day = Calendar.DAY_OF_WEEK;
//        int constantDay = supplierId
//    }


    public void arrived() {
        arrived = true;
    }

    public boolean isArrived() {
        return arrived;
    }

    public int getId() {
        return orderId;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public String getAddress() {
        return address;
    }

    public String getDate() {
        return new SimpleDateFormat("d/M/y").format(date);
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public boolean isReady() {
        return ready;
    }

    public boolean isWeekly() {
        return weekly;
    }

    public OrderLine addLine(SupplierProduct product, int amount, double discount, double finalPrice, int id) {
        OrderLine orderLine = new OrderLine(product, amount, discount, finalPrice, id, this.orderId);
        orderLines.add(orderLine);
        return orderLine;
    }

    public int removeLine(SupplierProduct product) {
        int id = getOrderLineId(product.getCatalogNumber());
        orderLines.removeIf(line -> line.catalogNumber.equals(product.getCatalogNumber()));
        return id;
    }

    public boolean hasLine(SupplierProduct product) {
        for (OrderLine line : orderLines) {
            if (line.catalogNumber.equals(product.getCatalogNumber()))
                return true;
        }
        return false;
    }

    //    private OrderLine(int id, int orderId, String catalogNumber, String name ,int amount,double price, double discount, double finalPrice) {

    public void addLine(int id, String catalogNumber, String pName, int amount, double price, double discount, double finalPrice) {
        orderLines.removeIf(line -> line.catalogNumber.equals(catalogNumber));
        orderLines.add(new OrderLine(id, orderId, catalogNumber, pName, amount, price, discount, finalPrice));
    }

    public void deleteLine(SupplierProduct product) {
        orderLines.removeIf(line -> line.catalogNumber.equals(product.getCatalogNumber()));
    }

    public List<OrderLine> getOrderLines() {
        return orderLines;
    }

    public void ready() {
        ready = true;
    }

    private boolean isValidAddress(String address) {
        Matcher matcher = VALID_ADDRESS_REGEX.matcher(address);
        return matcher.find();
    }

    public boolean isEmpty() {
        return orderLines.isEmpty();
    }

    public String orderDetails() {

        return "supplier's name: " + supplierName + " | " + "address: " + address + " | " + "order's number: " + orderId + "\n"
                + "supplier's number: " + supplierId + " | " + "order's date: " + getDate() + " | " + "contact's phone: " + contactPhone + "\n"
                + "did arrived? " + arrived;

    }

    public int getOrderLineId(String catalogNumber) {
        for (OrderLine orderLine : orderLines) {
            if (orderLine.catalogNumber.equals(catalogNumber))
                return orderLine.id;
        }
        return -1;
    }

    public String toString() {
        String order = "##################################################\n"
                + orderDetails() + "\n"
                + "##################################################\n";
        for (OrderLine orderLine : orderLines) {
            order += orderLine.toString() + "\n";
        }
        return order + "##################################################";
    }
}