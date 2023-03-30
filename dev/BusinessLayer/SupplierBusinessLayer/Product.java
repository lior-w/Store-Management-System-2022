package BusinessLayer.SupplierBusinessLayer;

public class Product {

    private final String name;
    private final String manufacturer;

    public Product(String name, String manufacturer) {
        this.name = name;
        this.manufacturer = manufacturer;
    }

    public String getName() {
        return name;
    }

    public String getManufacturer() {
        return manufacturer;
    }
}
