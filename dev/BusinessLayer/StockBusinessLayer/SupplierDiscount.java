package BusinessLayer.StockBusinessLayer;

public class SupplierDiscount extends Discount {
    private int supplierId;

    public SupplierDiscount(float discount, int supplierId) {
        super(discount);
        this.supplierId = supplierId;
    }

    public int getSupplierId() {
        return supplierId;
    }

}