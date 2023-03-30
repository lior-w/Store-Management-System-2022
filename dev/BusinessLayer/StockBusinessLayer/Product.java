package BusinessLayer.StockBusinessLayer;

public class Product {
    private String name;
    private int id;
    private int minQuantity;
    private int categoryId;
    private String producer;
    private float salePrice;
    private int saleDiscountId = -1;
    private int amount = 0;
    private float weight;

    public Product(String name, int minQuantity, int categoryId, String producer, float salePrice, float weight) {
        this.name = name;
        this.minQuantity = minQuantity;
        this.categoryId = categoryId;
        this.producer = producer;
        this.salePrice = salePrice;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMinQuantity() {
        return minQuantity;
    }

    public String getProducer() {
        return producer;
    }

    public float getSalePrice() {
        return salePrice;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMinQuantity(int minQuantity) {
        this.minQuantity = minQuantity;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public void setSalePrice(float salePrice) {
        this.salePrice = salePrice;
    }

    public int getSaleDiscountId() {
        return saleDiscountId;
    }

    public void setSaleDiscountId(int saleDiscountId) {
        this.saleDiscountId = saleDiscountId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }
}










