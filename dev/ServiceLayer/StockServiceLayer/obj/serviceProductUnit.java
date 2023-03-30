package ServiceLayer.StockServiceLayer.obj;

import BusinessLayer.StockBusinessLayer.ProductUnit;

import java.time.Instant;
import java.util.Date;

public class serviceProductUnit {
    private int id;
    private int productId;
    private Date expirationDate;
    private boolean isDefected;
    private boolean inStorage;
    private int location;
    private int supplierDiscountId;

    public serviceProductUnit(ProductUnit p) {
        id = p.getId();
        productId = p.getProductId();
        expirationDate = p.getExpirationDate();
        isDefected = p.isDefected();
        inStorage = p.isInStorage();
        location = p.getLocation();
        supplierDiscountId = p.getSupplierDiscountId();
    }

    public int getId() {
        return id;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public boolean isDefected() {
        if (isExpired())
            isDefected = true;
        return isDefected;
    }

    public boolean isInStorage() {
        return inStorage;
    }

    public int getLocation() {
        return location;
    }

    public int getSupplierDiscountId() {
        return supplierDiscountId;
    }

    public int getProductId() {
        return productId;
    }

    public boolean isExpired() {
        return expirationDate.before(Date.from(Instant.now()));
    }

    public String toString() {
        return "ID: " + getId() + "\nProduct ID: " + getProductId() + "\nExpiration Date: " + getExpirationDate() +
                "\nShelf Location: " + getLocation() + "\nIn Storage? " + isInStorage();
    }
}
