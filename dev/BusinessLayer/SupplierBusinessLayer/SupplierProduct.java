package BusinessLayer.SupplierBusinessLayer;


public class SupplierProduct {

    private final String catalogNumber;
    private final String name;
    private final double price;
    private final String manufacturer;

    public SupplierProduct(String catalogNumber, String name, double price, String manufacturer) {
        this.catalogNumber = catalogNumber;
        this.name = name;
        this.price = price;
        this.manufacturer = manufacturer;
    }

    public String getCatalogNumber() {
        return catalogNumber;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public boolean equal(SupplierProduct p) {
        return catalogNumber.equals(p.getCatalogNumber()) && name.equals(p.getName())
                && price == p.getPrice() && manufacturer.equals(p.getManufacturer());
    }


}
