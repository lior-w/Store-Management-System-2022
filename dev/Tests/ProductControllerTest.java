package Tests;

import DataAccessLayer.StockDAL.ProductDAO;
import DataAccessLayer.StockDAL.ProductUnitDAO;
import BusinessLayer.StockBusinessLayer.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductControllerTest {

    ProductController pd;
    Product TnuvaMilk9p;
    Product TaraMilk3p;
    Product testAmount;
    ProductDAO pDAO;
    ProductUnitDAO puDAO;

    @BeforeEach
    void setUp() throws Exception {
        pDAO = new ProductDAO();
        puDAO = new ProductUnitDAO();
        pd = new ProductController();
        TnuvaMilk9p = new Product("TnuvaMilk9p",50, 2,"Tnuva", 9, 2);
        TaraMilk3p = new Product("TaraMilk3p",30, 2, "Tara",  6, 2);
        pDAO.insert(TnuvaMilk9p);
        pDAO.insert(TaraMilk3p);
        
    }

    @AfterEach
    void tearDown() {
        try {
            for (ProductUnit unit : puDAO.getProductUnits(TaraMilk3p.getId())) {
                puDAO.delete(unit.getId());
            }
            for (ProductUnit unit : puDAO.getProductUnits(TnuvaMilk9p.getId())) {
                puDAO.delete(unit.getId());
            }
            pDAO.delete(TnuvaMilk9p.getId());
            pDAO.delete(TaraMilk3p.getId());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void getProduct() {
        Product testProduct = pd.getProduct(TaraMilk3p.getId());
        assertEquals(testProduct.getId(),TaraMilk3p.getId());
        assertEquals(testProduct.getAmount(),TaraMilk3p.getAmount());
        assertEquals(testProduct.getProducer(),TaraMilk3p.getProducer());
        assertEquals(testProduct.getCategoryId(),TaraMilk3p.getCategoryId());
        assertEquals(testProduct.getSaleDiscountId(),TaraMilk3p.getSaleDiscountId());
        assertEquals(testProduct.getMinQuantity(),TaraMilk3p.getMinQuantity());
        assertEquals(testProduct.getName(),TaraMilk3p.getName());
        assertEquals(testProduct.getWeight(),TaraMilk3p.getWeight());
        assertEquals(testProduct.getSalePrice(),TaraMilk3p.getSalePrice());
    }

    @Test
    void addProduct() throws Exception {
        Product test = pd.addProduct("test", "Tnuva", 40, 2, 7, 2);
        assertEquals(pd.getProduct(test.getId()), test);
        pd.removeProduct(test.getId());
    }

    @Test
    void addAmount() throws Exception {
        testAmount = pd.addProduct("testAmount", "test", 50, 5, 12, 2);
        assertEquals(testAmount.getAmount(), 0);
        pd.addAmount(testAmount.getId(), 50);
        assertEquals(testAmount.getAmount(), 50);
        for (ProductUnit unit : puDAO.getProductUnits(testAmount.getId())) {
            puDAO.delete(unit.getId());
        }
        testAmount.setAmount(0);
        pd.removeProduct(testAmount.getId());
    }

    @Test
    void reduceAmount() throws Exception {
        testAmount = pd.addProduct("testAmount", "test", 50, 5, 12, 2);
        assertEquals(testAmount.getAmount(), 0);
        pd.addAmount(testAmount.getId(), 50);
        pd.reduceAmount(testAmount.getId());
        assertEquals(testAmount.getAmount(), 49);
        for (ProductUnit unit : puDAO.getProductUnits(testAmount.getId())) {
            puDAO.delete(unit.getId());
        }
        testAmount.setAmount(0);
        pd.removeProduct(testAmount.getId());
    }

    @Test
    void underMinimumQuantities() throws Exception {
        testAmount = pd.addProduct("testAmount", "test", 50, 5, 12, 2);
        List<Product> minQuanProd = pd.underMinimumQuantities();
        assertTrue(minQuanProd.contains(testAmount));
        pd.addAmount(testAmount.getId(), 60);
        minQuanProd = pd.underMinimumQuantities();
        assertFalse(minQuanProd.contains(testAmount));
        for (ProductUnit unit : puDAO.getProductUnits(testAmount.getId())) {
            puDAO.delete(unit.getId());
        }
        testAmount.setAmount(0);
        pd.removeProduct(testAmount.getId());
    }

    @Test
    void getProducts() {
        List<Product> listProd = pd.getProducts(2);
        int size = listProd.size();
        pd.changeProductCategory(TnuvaMilk9p.getId(), 1);
        listProd = pd.getProducts(2);
        assertNotEquals(size, listProd.size());
        assertEquals(size-1, listProd.size());
    }

    @Test
    void setSaleDiscount() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("d-M-y");
        int originalSaleID = pd.getProduct(TnuvaMilk9p.getId()).getSaleDiscountId();
        SaleDiscount sd = new SaleDiscount(formatter.parse("26-01-2022"), formatter.parse("26-10-2022"), 25);
        DiscountController dc = new DiscountController();
        dc.addSaleDiscount(sd);
        pd.setSaleDiscount(TnuvaMilk9p.getId(), sd.getId());
        assertNotEquals(pd.getProduct(TnuvaMilk9p.getId()).getSaleDiscountId(), originalSaleID);
    }
}