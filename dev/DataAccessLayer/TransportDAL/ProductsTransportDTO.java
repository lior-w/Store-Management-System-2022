package DataAccessLayer.TransportDAL;

public class ProductsTransportDTO {
    public int id;
    public String name;
    public int weight;

    public ProductsTransportDTO(int id, String name, int weight) {
        this.id = id;
        this.name = name;
        this.weight = weight;
    }

    public String fieldsToString() {
        return String.format("(\"%s\",\"%s\",\"%s\")", this.id, this.name, this.weight);
    }


}
