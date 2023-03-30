package ServiceLayer.StockServiceLayer.obj;

import BusinessLayer.StockBusinessLayer.SaleDiscount;

import java.text.SimpleDateFormat;
import java.util.Date;

public class serviceSaleDiscount {
    private final int discountID;
    private final float discount;
    private final Date start;
    private final Date end;

    public serviceSaleDiscount(SaleDiscount saleDiscount) {
        this.discountID = saleDiscount.getId();
        this.discount = saleDiscount.getDiscount();
        this.start = saleDiscount.getStart();
        this.end = saleDiscount.getEnd();
    }

    public int getDiscountID() {
        return discountID;
    }

    public float getDiscount() {
        return discount;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public String toString() {
        return "ID: " + getDiscountID() + "\nDiscount: " + getDiscount() + "\nStart Date: " + new SimpleDateFormat("yyyy-MM-dd").format(getStart() +
                "\nEnd Date: " +  new SimpleDateFormat("yyyy-MM-dd").format(getStart()));
    }
}
