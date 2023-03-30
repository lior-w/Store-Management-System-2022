package Tests;

import BusinessLayer.SupplierBusinessLayer.ContactController;
import BusinessLayer.SupplierBusinessLayer.Supplier;
import BusinessLayer.SupplierBusinessLayer.SupplierController;
import BusinessLayer.TransportBusinessLayer.ShippingArea;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ContactControllerTest {

    ContactController controller = ContactController.getInstance();
    int supplierId;

    @BeforeEach
    void setUp() {
        supplierId = SupplierController.getInstance().addSupplier("sup", 1, 1, Supplier.PayWay.CASH, "aaa 22, bbb", ShippingArea.Center);
    }

    @AfterEach
    void tearDown() {
        SupplierController.getInstance().removeSupplier(supplierId);
        controller.removeContacts(supplierId);
    }

    @Test
    public void addContactToSupplier_success() { // 12
        boolean res;
        try {
            controller.addContactToSupplier(supplierId, "con1", "con@gmail.com", "0541111111");
            res = true;
        } catch (Exception e) {
            res = false;
        }

        assertTrue(res);
        assertEquals(1, controller.getSupplierContacts(supplierId).size());
    }

    @Test
    public void addContactToSupplier_fail() { // 13
        boolean res1;
        boolean res2;
        try {
            controller.addContactToSupplier(supplierId, "con1", "11@gmail.com", "0501234567");
            res1 = true;
        } catch (Exception e) {
            res1 = false;
        }
        try {
            controller.addContactToSupplier(supplierId, "con1", "con@gmail.com", "050123456");
            res2 = true;
        } catch (Exception e) {
            res2 = false;
        }

        assertFalse(res1);
        assertFalse(res2);
        assertEquals(0, controller.getSupplierContacts(supplierId).size());
    }
}
