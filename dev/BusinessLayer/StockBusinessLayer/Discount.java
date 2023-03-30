package BusinessLayer.StockBusinessLayer;


public abstract class Discount {

    protected int id;
    protected float discount;

    public Discount() {}

    public Discount(float discount) {
        this.discount = discount;
    }

    public int getId() {
        return id;
    }

    public float getDiscount() {
        return discount;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }
}
