package DataAccessLayer.TransportDAL;

public class TransportDocumentsProductsDTO {
    public int transDocId;
    public int productId;
    public int quantity;

    public TransportDocumentsProductsDTO(int transDocId, int productId, int quantity) {
        this.transDocId = transDocId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public String fieldsToString() {
        return String.format("(\"%s\",\"%s\",\"%s\")", this.transDocId, this.productId, this.quantity);
    }
}
