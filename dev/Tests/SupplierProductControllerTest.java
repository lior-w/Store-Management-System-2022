package Tests;

import BusinessLayer.SupplierBusinessLayer.*;
import BusinessLayer.TransportBusinessLayer.ShippingArea;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class SupplierProductControllerTest {

    ProductController controller = ProductController.getInstance();
    List<Integer> supID = new ArrayList<>();
    int supplierId;
    int supplierId2;
    String CN = "0101";
    String productName = "p1";
    double price = 10;
    String manuf = "m1";


    @BeforeEach
    void setUp() {
        supplierId = SupplierController.getInstance().addSupplier("sup", 1, 1, Supplier.PayWay.CASH, "aaa 22, bbb", ShippingArea.Center);
        supID.add(supplierId);
        supplierId2 = SupplierController.getInstance().addSupplier("sup2", 2, 2, Supplier.PayWay.CASH, "aaa 22, bbb", ShippingArea.Center);
        supID.add(supplierId2);
        try {
            controller.addProductToSupplier(supplierId, CN, productName, price, manuf);
            controller.addProductToSupplier(supplierId2, CN, productName, price / 2, manuf);
            SupplierController.getInstance().setContract(supplierId, 1, true);
            SupplierController.getInstance().setContract(supplierId2, 1, true);
            SupplierController.getInstance().addDiscount(supplierId, productName, manuf, 100, 60);
            ContactController.getInstance().addContactToSupplier(supplierId, "name1", "n1@gmail.com", "0541111111");
            ContactController.getInstance().addContactToSupplier(supplierId2, "name2", "n2@gmail.com", "0541111112");
        } catch (Exception ignore) {}
    }

    @AfterEach
    void tearDown() {
        for(Integer id : supID) {
            try {
                SupplierController.getInstance().removeSupplier(id);
                controller.removeProducts(id);
            } catch (Exception e) {
                System.out.println("ex");
            }

        }
        supID = new ArrayList<>();
    }

    @Test
    void addProductToSupplier_success() { // 6
        boolean res;
        try {
            controller.addProductToSupplier(supplierId, "0202", "p2", 20, "m2");
            res = true;
        } catch (Exception e) {
            res = false;
            System.out.println(e.getMessage());
        }

        assertTrue(res);
        assertEquals(2, controller.getProducts(supplierId).size());
    }

    @Test
    void addProductToSupplier_fail() { // 7
        boolean res;
        try {
            controller.addProductToSupplier(supplierId2 + 1, "0303", "noSupplier", 0, "m0");
            res = true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            res = false;
        }

        assertFalse(res);
    }
}
