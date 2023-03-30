package DataAccessLayer.TransportDAL;

public class TransportsSitesDTO {
    public int transportId;
    public int siteId;

    public TransportsSitesDTO(int transportId, int siteId) {
        this.transportId = transportId;
        this.siteId = siteId;
    }

    public String fieldsToString() {
        return String.format("(\"%s\",\"%s\")", this.siteId,this.transportId);
    }
}
