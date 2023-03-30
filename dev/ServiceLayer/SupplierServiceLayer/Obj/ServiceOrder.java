package ServiceLayer.SupplierServiceLayer.Obj;

import BusinessLayer.SupplierBusinessLayer.Order;

import java.util.*;
import java.util.stream.Collectors;

public class ServiceOrder {

    public class ServiceOrderLine {
        public final String catalogNumber;
        public final String productName;
        public final int amount;
        public final double price;
        public final double discount;
        public final double finalPrice;
        public final String lineString;

        private ServiceOrderLine(Order.OrderLine line) {
            this.catalogNumber = line.catalogNumber;
            this.productName = line.productName;
            this.amount = line.amount;
            this.price = line.price;
            this.discount = line.discount;
            this.finalPrice = line.finalPrice;
            this.lineString = line.toString();
        }
    }

    public final int orderId;
    public final int supplierId;
    public final String supplierName;
    public final String address;
    public final String date;
    public final String contactPhone;
    public final List<ServiceOrderLine> orderLines;
    public final boolean ready;
    public final boolean arrived;
    public final boolean weekly;
    public final String orderDetails;
    public final String orderString;

    public ServiceOrder(Order order) {
        this.orderId = order.getId();
        this.supplierId = order.getSupplierId();
        this.supplierName = order.getSupplierName();
        this.address = order.getAddress();
        this.date = order.getDate();
        this.contactPhone = order.getContactPhone();
        this.orderLines = order.getOrderLines().stream().map(ServiceOrderLine::new).collect(Collectors.toList());
        this.ready = order.isReady();
        this.arrived = order.isArrived();
        this.weekly = order.isWeekly();
        this.orderDetails = order.orderDetails();
        this.orderString = order.toString();
    }
}