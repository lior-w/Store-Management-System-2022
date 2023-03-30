package DataAccessLayer.TransportDAL;

public class TransportDocToTransportDTO {
    public int transportId;
    public int tranDocId;

    public TransportDocToTransportDTO(int transportId, int tranDocId) {
        this.transportId = transportId;
        this.tranDocId = tranDocId;
    }

    public String fieldsToString() {
        return String.format("(\"%s\",\"%s\")", this.transportId, this.tranDocId);
    }
}
