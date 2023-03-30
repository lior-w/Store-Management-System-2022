package DataAccessLayer.TransportDAL;

public class TransportDTO {
    public int transportId;
    public String dateOfTransport;
    public String timeOfDeparture;
    public String vehicleLicensePlate;
    public String driverId;
    public String area;
    public String status;

    public TransportDTO(int transportId, String dateOfTransport, String timeOfDeparture,
                        String vehicleLicensePlate, String driverId, String area,String status) {
        this.transportId = transportId;
        this.dateOfTransport = dateOfTransport;
        this.timeOfDeparture = timeOfDeparture;
        this.vehicleLicensePlate = vehicleLicensePlate;
        this.driverId = driverId;
        this.area = area;
        this.status = status;
    }


    public String fieldsToString() {
        return String.format("(\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\")", this.transportId, this.dateOfTransport
                , this.timeOfDeparture,this.vehicleLicensePlate,this.driverId,this.area,this.status);
    }
}
