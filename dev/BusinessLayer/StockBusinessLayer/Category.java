package BusinessLayer.StockBusinessLayer;

public class Category {

    private int id;
    private String name;
    private Integer superId;

    public Category(String name, Integer superId) {
        this.name = name;
        this.superId = superId;
    }

    public Category(int id, String name, Integer superId) {
        this.id = id;
        this.name = name;
        this.superId = superId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSuperId() {
        return superId;
    }

    public void setSuperId(int superId) {
        this.superId = superId;
    }
}
