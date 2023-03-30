package Tests;

import BusinessLayer.TransportBusinessLayer.ShippingArea;
import DataAccessLayer.Repository;
import DataAccessLayer.StockDAL.ProductDAO;
import DataAccessLayer.StockDAL.ProductUnitDAO;
import DataAccessLayer.SuppliersDAL.*;
import ServiceLayer.ResponseT;
import ServiceLayer.StockServiceLayer.StockService;
import BusinessLayer.StockBusinessLayer.*;
import BusinessLayer.StockBusinessLayer.Product;
import BusinessLayer.SupplierBusinessLayer.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StockSupIntegrationTest {
    OrderController oc;
    StockService stockService;
    ProductUnitDAO unitDAO = new ProductUnitDAO();
    ProductDAO prodDAO = new ProductDAO();
    Product product;
    ProductUnit unit;
    SupplierDAO supplierDAO;
    SupplierContractDAO scDAO;
    QDDiscountDAO qddDAO;
    SupplierProductDAO spDAO;
    OrderLineDAO olDAO;
    ContactDAO cDAO;

    @BeforeEach
    void setUp() throws Exception {
        oc = OrderController.getInstance();
        stockService = StockService.getInstance();
        product = new Product("TnuvaMilk9p",  1, 2,"Tnuva", 9, 2);
        prodDAO.insert(product);
        unit = new ProductUnit(new SimpleDateFormat("yyyy-MM-dd").parse("2023-06-28"), true, false,3, -1, product.getId());
        unitDAO.insert(unit);
        prodDAO.update(product.getId(), "amount", 1);
        supplierDAO = new SupplierDAO();
        supplierDAO.insert(new Supplier(666, "LiorHakelev", 724, 55329, Supplier.PayWay.CREDIT, "street 2, Tel-Aviv", ShippingArea.Center));
        scDAO = new SupplierContractDAO();
        scDAO.insert(new SupplierContract(666, 1, true));
        qddDAO = new QDDiscountDAO();
        qddDAO.insert(666, "20", "Tnuva", 1, 10);
        cDAO = new ContactDAO();
        cDAO.insert(new Contact("roei", "rbirnfeld@gmail.com", "0509698240"), 666);
        spDAO = new SupplierProductDAO();
        spDAO.insert(new SupplierProduct("20", "TnuvaMilk9p", 9, "Tnuva"), 666);
    }

    @AfterEach
    void cleanData() {
        prodDAO.delete(product.getId());
        unitDAO.delete(unit.getId());
        supplierDAO.delete(666);
        scDAO.delete(666);
        qddDAO.delete( 666, "20", 1);
        spDAO.delete(666);
        cDAO.delete(666);
    }

    @Test
    void makeShortageOrderAuto() {
        int orderId = -1;
        ResponseT<Integer> res = stockService.removeProductUnit(unit.getId());
        if (!res.isError()) {
            orderId = res.getData();
        }
        Order order = new OrderDAO().get(orderId);
        Assertions.assertNotNull(order);
        olDAO = new OrderLineDAO();
        Order.OrderLine orderLine = olDAO.initialize(order).getOrderLines().get(0);
        assertEquals(orderLine.productName, product.getName());
        Repository.getInstance().closeConnection();
        olDAO.delete(orderLine.id);
        new OrderDAO().delete(orderId);
    }
}
