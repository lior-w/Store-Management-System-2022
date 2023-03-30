package BusinessLayer.StockBusinessLayer;

import java.time.Instant;
import java.util.Date;

public class ProductUnit {
    private int id;
    private int productId;
    private Date expirationDate;
    private boolean isDefected;
    private boolean inStorage;
    private int location;
    private int supplierDiscountId;

    public ProductUnit(Date expirationDate, boolean inStorage, boolean isDefected, int location, int supplierDiscountId, int productId) {
        this.expirationDate = expirationDate;
        this.inStorage = inStorage;
        this.isDefected = isDefected;
        this.location = location;
        this.supplierDiscountId = supplierDiscountId;
        this.productId = productId;
    }

    public boolean isDefected() {
        if (isExpired())
            isDefected = true;
        return isDefected;
    }

    public void defected() {
        isDefected = true;
    }

    public int getId() {
        return id;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public boolean isInStorage() {
        return inStorage;
    }

    public int getLocation() {
        return location;
    }

    public void setInStorage(boolean inStorage) {
        this.inStorage = inStorage;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public int getSupplierDiscountId() {
        return supplierDiscountId;
    }

    public int getProductId() {
        return productId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isExpired() {
        return expirationDate.before(Date.from(Instant.now()));
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
        isDefected();
    }
}

