package BusinessLayer.TransportBusinessLayer;

public class ProductTransport {
    private int ID;
    private String name;
    private int weight;


    public ProductTransport(int ID, String name, int weight) {
        this.ID  = ID;
        this.name = name;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getID() {
        return ID;
    }

    public String productDetails(){
        String data = "";
        data += "ID: "+ID+"   Name: "+name;
        return data;
    }
}
