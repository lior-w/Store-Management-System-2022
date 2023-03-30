package Tests;

import BusinessLayer.StockBusinessLayer.DiscountController;
import BusinessLayer.StockBusinessLayer.SaleDiscount;
import BusinessLayer.StockBusinessLayer.SupplierDiscount;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.*;

class DiscountControllerTest {

    DiscountController dc;
    SaleDiscount saleDis;
    SupplierDiscount supDis;
    SaleDiscount saleDis2;
    SupplierDiscount supDis2;

    @BeforeEach
    void setUp() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("d-M-y");
        dc = new DiscountController();
        saleDis = new SaleDiscount(formatter.parse("26-01-2022"), formatter.parse("26-10-2022"), 25);
        supDis = new SupplierDiscount(10, 2);
        dc.addSaleDiscount(saleDis);
        dc.addSupplierDiscount(supDis);
    }

    @AfterEach
    void cleanData() {
        dc.removeDiscount(saleDis.getId());
        dc.removeDiscount(supDis.getId());
    }

    @Test
    void getSupplierDiscount() {
        assertEquals(dc.getSupplierDiscount(supDis.getId()),supDis);
        assertEquals(dc.getSupplierDiscount(supDis.getId()).getDiscount(), 10);
    }

    @Test
    void getSaleDiscount() {
        assertEquals(dc.getSaleDiscount(saleDis.getId()), saleDis);
        assertEquals(dc.getSaleDiscount(saleDis.getId()).getDiscount(), 25);
    }

    @Test
    void addSaleDiscount() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("d-M-y");
        saleDis2 = new SaleDiscount(formatter.parse("26-05-2022"), formatter.parse("26-06-2022"), 15);
        dc.addSaleDiscount(saleDis2);
        assertEquals(dc.getSaleDiscount(saleDis2.getId()).getDiscount(),15);
        dc.removeDiscount(saleDis2.getId());
    }

    @Test
    void addSupplierDiscount() {
        supDis2 = new SupplierDiscount(5, 4);
        dc.addSupplierDiscount(supDis2);
        assertEquals(dc.getSupplierDiscount(supDis2.getId()).getSupplierId(), 4);
        dc.removeDiscount(supDis2.getId());
    }
}