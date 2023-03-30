package ServiceLayer.TransportServiceLayer.Objects;


import BusinessLayer.TransportBusinessLayer.ProductTransport;

public class SProduct {
    private int ID;
    private String name;
    private int weight;


    public SProduct(ProductTransport p) {
        this.ID  = p.getID();
        this.name = p.getName();
        this.weight = p.getWeight();
    }
}
