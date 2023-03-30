package DataAccessLayer.TransportDAL;

public class TransportDocumentDTO {
    public int id;
    public String weekly;
    public int siteId;
    public int orderId;
    public int assignedTransportId;
    public String status;

    public TransportDocumentDTO(int id, String weekly, int siteId, int assignedTransportId, String status, int orderId) {
        this.id = id;
        this.weekly = weekly;
        this.siteId = siteId;
        this.assignedTransportId = assignedTransportId;
        this.status=status;
        this.orderId = orderId;
    }

    public String fieldsToString() {
        return String.format("(\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\")", this.id, this.weekly, this.siteId,this.assignedTransportId,this.status,this.orderId);
    }
}
