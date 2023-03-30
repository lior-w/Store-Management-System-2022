package DataAccessLayer.TransportDAL;

public class VehiclesDTO {
    public String licensePlate;
    public String licenseType;
    public int netWeight;
    public int maxWeight;
    public String kind;

    public VehiclesDTO(String licensePlate, String licenseType, int netWeight, int maxWeight, String kind) {
        this.licensePlate = licensePlate;
        this.licenseType = licenseType;
        this.netWeight = netWeight;
        this.maxWeight = maxWeight;
        this.kind = kind;
    }

    public String fieldsToString() {
        return String.format("(\"%s\",\"%s\",\"%s\",\"%s\",\"%s\")", this.licensePlate, this.licenseType
                , this.netWeight,this.maxWeight,this.kind);
    }
}
