package Tests;

import BusinessLayer.SupplierBusinessLayer.*;
import BusinessLayer.TransportBusinessLayer.ShippingArea;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class OrderControllerTest {

    OrderController orderController = OrderController.getInstance();
    SupplierController supplierController = SupplierController.getInstance();
    ContactController contactController = ContactController.getInstance();
    ProductController productController = ProductController.getInstance();
    int supplierId;
    int orderId;

    @BeforeEach
    void setUp() throws Exception {
        supplierId = supplierController.addSupplier("yosi", 102030, 10000, Supplier.PayWay.CASH, "aaa 34, bbb", ShippingArea.Center);
        supplierController.setContract(supplierId, 1, true);
        contactController.addContactToSupplier(supplierId, "avi", "acb@def.com", "0501234567");
        productController.addProductToSupplier(supplierId, "123", "milk", 100, "tnuva");
        productController.addProductToSupplier(supplierId, "321", "water", 500, "neviot");
    }

    @AfterEach
    void tearDown() {
        supplierController.removeSupplier(supplierId);
        productController.removeProducts(supplierId);
        orderController.deleteOrder(orderId);
        contactController.removeContacts(supplierId);
    }

    @Test
    void makeShortageOrder_success() { // 9
        boolean res;
        Order order = null;
        try {
            orderId = orderController.makeShortageOrder("milk", "tnuva", 10);
            order = orderController.getOrder(orderId);
            res = true;
        } catch (Exception e) {
            res = false;
        }

        assertTrue(res);
        assertNotNull(order);
        assertEquals(orderId , order.getId());
    }

    @Test
    void makeWeeklyOrder_success() { // 10
        boolean res;
        Order order = null;
        Map<String, Integer> CN_AmountMap = new HashMap<>();
        CN_AmountMap.put("123", 70);
        CN_AmountMap.put("321", 100);
        try {
            orderId = orderController.makeWeeklyOrder(supplierId, CN_AmountMap);

            res = true;
        } catch (Exception e) {
            res = false;
        }
        order = orderController.getOrder(orderId);

        assertTrue(res);
        assertNotNull(order);
        assertEquals(orderId , order.getId());
        assertEquals(2, order.getOrderLines().size());
    }

    @Test
    void makeWeeklyOrder_fail() { // 11
        boolean res;
        Order order = null;
        Map<String, Integer> CN_AmountMap = new HashMap<>();
        CN_AmountMap.put("00000", 100);
        try {
            orderId = orderController.makeWeeklyOrder(supplierId, CN_AmountMap);
            res = true;
        } catch (Exception e) {
            res = false;
        }
        order = orderController.getOrder(orderId);

        assertFalse(res);
        assertNull(order);
    }
}