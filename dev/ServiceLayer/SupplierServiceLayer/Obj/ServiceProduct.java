package ServiceLayer.SupplierServiceLayer.Obj;


import BusinessLayer.SupplierBusinessLayer.SupplierProduct;

public class ServiceProduct {

    public final String catalogNumber;
    public final String name;
    public final double price;
    public final String manufacturer;

    public ServiceProduct(SupplierProduct product) {
        this.catalogNumber = product.getCatalogNumber();
        this.name = product.getName();
        this.price = product.getPrice();
        this.manufacturer = product.getManufacturer();
    }
}
