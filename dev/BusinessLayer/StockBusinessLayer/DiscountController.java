package BusinessLayer.StockBusinessLayer;

import DataAccessLayer.StockDAL.DiscountDAO;

import java.util.Date;

public class DiscountController {
    DiscountDAO discountDAO;

    public DiscountController() {
        discountDAO = new DiscountDAO();
    }

    public SupplierDiscount getSupplierDiscount(int id) {
        return (SupplierDiscount) discountDAO.get(id);
    }

    public SaleDiscount getSaleDiscount(int id) {
        return (SaleDiscount) discountDAO.get(id);
    }

    public boolean addSaleDiscount(SaleDiscount discount) {
        return discountDAO.insert(discount);
    }

    public boolean addSupplierDiscount(SupplierDiscount discount) {
        return discountDAO.insert(discount);
    }

    public void removeDiscount(int id) {//for test
        discountDAO.delete(id);
    }

    public boolean editSaleDiscount(int discountId, float discount, Date start, Date end) {
        SaleDiscount saleDiscount = (SaleDiscount)discountDAO.get(discountId);
        discountDAO.update(discountId, "discount", discount);
        saleDiscount.setDiscount(discount);
        discountDAO.update(discountId, "start", discount);
        saleDiscount.setStart(start);
        discountDAO.update(discountId, "end", discount);
        saleDiscount.setEnd(end);
        return true;
    }
}
