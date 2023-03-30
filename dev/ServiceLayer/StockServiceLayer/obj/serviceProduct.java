package ServiceLayer.StockServiceLayer.obj;

import BusinessLayer.StockBusinessLayer.Product;

public class serviceProduct {
    private String name;
    private int id;
    private int minQuantity;
    private int categoryId;
    private String producer;
    private float salePrice;
    private int saleDiscount;
    private int amount;
    private float weight;

    public serviceProduct(Product p) {
        name = p.getName();
        id = p.getId();
        minQuantity = p.getMinQuantity();
        categoryId = p.getCategoryId();
        producer = p.getProducer();
        salePrice = p.getSalePrice();
        saleDiscount = p.getSaleDiscountId();
        amount = p.getAmount();
        weight = p.getWeight();
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getMinQuantity() {
        return minQuantity;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getProducer() {
        return producer;
    }

    public float getSalePrice() {
        return salePrice;
    }

    public int getSaleDiscount() {
        return saleDiscount;
    }

    public int getAmount() {
        return amount;
    }

    public float getWeight() {
        return weight;
    }

    public String toString() {
        return "ID: " + getId() + "\nName: " + getName() + "\nMinimum Quantity: " + getMinQuantity() +
                "\nProducer: " + getProducer() + "\nSale Price: " + getSalePrice() + "\nWeight: " + getWeight();
    }
}
