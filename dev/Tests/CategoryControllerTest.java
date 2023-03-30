package Tests;

import BusinessLayer.StockBusinessLayer.Category;
import BusinessLayer.StockBusinessLayer.CategoryController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CategoryControllerTest {

    CategoryController cc;
    Category MilkProducts;
    Category Milk;
    Category Milk9p;
    Category Milk3p;

    @BeforeEach
    void setUp() throws Exception {
        cc = new CategoryController();
        MilkProducts = cc.addCategory("TestMilkProducts", null);
        Milk = cc.addCategory("TestMilk", MilkProducts.getId());
    }

    @AfterEach
    void cleanData() {
        cc.removeCategory(MilkProducts.getId());
        cc.removeCategory(Milk.getId());
    }

    @Test
    void getCategory() {
        assertEquals(cc.getCategory(MilkProducts.getId()),MilkProducts);
        assertNotEquals(cc.getCategory((MilkProducts.getId())),Milk);
        assertEquals(Milk.getSuperId(),MilkProducts.getId());
    }

    @Test
    void addCategory() throws Exception {
        Milk9p = cc.addCategory("TestMilk9p", Milk.getId());
        assertEquals(Milk9p.getSuperId(), Milk.getId());
        cc.removeCategory(Milk9p.getId());
    }

    @Test
    void getTopLevelCategories() {
        List<Category> topLevelCat = cc.getTopLevelCategories();
        for (Category c : topLevelCat) {
            assertNull(c.getSuperId());
        }
    }

    @Test
    void getSubCategories() throws Exception {
        Milk9p = cc.addCategory("TestMilk9p", Milk.getId());
        Milk3p = cc.addCategory("TestMilk3p", Milk.getId());
        List<Category> subLevelCat = cc.getSubCategories(Milk.getId());
        for (Category c : subLevelCat) {
            assertEquals(c.getSuperId(), Milk.getId());
        }
        cc.removeCategory(Milk9p.getId());
        cc.removeCategory(Milk3p.getId());
    }
}