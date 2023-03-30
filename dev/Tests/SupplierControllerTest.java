package Tests;

import BusinessLayer.SupplierBusinessLayer.ProductController;
import BusinessLayer.SupplierBusinessLayer.Supplier;
import BusinessLayer.SupplierBusinessLayer.SupplierController;
import BusinessLayer.TransportBusinessLayer.ShippingArea;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SupplierControllerTest {

    SupplierController controller = SupplierController.getInstance();
    List<Integer> supID = new ArrayList<>();
    int currentId;
    String CN = "0101";
    String productName = "p1";
    double price = 10;
    String manuf = "m1";


    @BeforeEach
    void setUp() {
        currentId = controller.addSupplier("sup", 1, 1, Supplier.PayWay.CASH, "aaa 22, bbb", ShippingArea.Center);
        supID.add(currentId);
        try {
            ProductController.getInstance().addProductToSupplier(currentId, CN, productName, price, manuf);
        } catch (Exception ignore) {}
    }

    @AfterEach
    void tearDown() {
        for(Integer id : supID) {
            try {
                controller.removeSupplier(id);
                ProductController.getInstance().removeProducts(id);
            } catch (Exception e) {
                System.out.println("ex");
            }

        }
        supID = new ArrayList<>();
    }

    @Test
    void addSupplier_success() throws Exception { // 1
        int suppliers = controller.getSuppliers().size();
        try {
            supID.add(controller.addSupplier("lior", 0, 10, Supplier.PayWay.CASH, "aaa 22, bbb", ShippingArea.Center));
        } catch (Exception ignore) { }

        assertEquals(suppliers + 1, controller.getSuppliers().size());
    }

    @Test
    void setContract_success() { // 2
        boolean res;
        try {
            controller.setContract(currentId, 1, true);
            res = true;
        } catch (Exception e) {
            res = false;
        }

        assertTrue(res);
        assertNotNull(controller.getSupplier(currentId).getContract());
    }

    @Test
    void setContract_fail() { // 3
        boolean res1;
        boolean res2;
        try {
            controller.setContract(currentId, 1, true);
            res1 = true;
        } catch (Exception e) {
            res1 = false;
        }
        try {
            controller.setContract(currentId + 1, 1, true);
            res2 = true;
        } catch (Exception e) {
            res2 = false;
        }

        assertTrue(res1);
        assertFalse(res2);
        assertNotNull(controller.getContract(currentId));
    }

    @Test
    void addDiscount_success() { // 4
        boolean res;
        int discounts = 0;
        double dis1 = 0;
        double dis2 = 0;
        try {
            controller.setContract(currentId, 1, true);
            controller.addDiscount(currentId, productName, manuf, 100, 20);
            res = true;
        } catch (Exception e) {
            res = false;
        }

        try {
            discounts = controller.getDiscounts(currentId, productName, manuf).size();
            dis1 = controller.getDiscount(currentId, productName, manuf, 99);
            dis2 = controller.getDiscount(currentId, productName, manuf, 100);
        } catch (Exception e) {
            fail();
        }

        assertTrue(res);
        assertEquals(1, discounts);
        assertEquals(0, dis1);
        assertEquals(20, dis2);
    }

    @Test
    void addDiscount_fail() { // 5
        boolean res1;
        boolean res2;
        try {
            controller.addDiscount(currentId, productName, manuf, 100, 20);
            res1 = true;
        } catch (Exception e) {
            res1 = false;
        }
        try {
            controller.setContract(currentId, 1, true);
            controller.addDiscount(currentId, "noSuchProduct", manuf, 100, 20);
            res2 = true;
        } catch (Exception e) {
            res2 = false;
        }

        assertFalse(res1);
        assertFalse(res2);
    }
}