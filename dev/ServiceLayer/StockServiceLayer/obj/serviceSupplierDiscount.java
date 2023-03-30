package ServiceLayer.StockServiceLayer.obj;

import BusinessLayer.StockBusinessLayer.SupplierDiscount;

public class serviceSupplierDiscount {
    private int discountID;
    private float discount;
    private int supplierID;

    public serviceSupplierDiscount(SupplierDiscount discount) {
        discountID = discount.getId();
        this.discount = discount.getDiscount();
        supplierID = discount.getSupplierId();
    }

    public serviceSupplierDiscount(float discount, int discountedProductTypeID, int supplierID) {
        this.discount = discount;
        this.supplierID = supplierID;
    }

    public int getDiscountID() {
        return discountID;
    }

    public float getDiscount() {
        return discount;
    }

    public int getSupplierID() {
        return supplierID;
    }
}
