package BusinessLayer.TransportBusinessLayer;
import DataAccessLayer.TransportDAL.VehiclesDTO;

public class Truck  extends Vehicle {
    public Truck(String licenseNumber, String licenseType, int NET_WEIGHT, int MAX_WEIGHT) {
        super(licenseNumber, licenseType, NET_WEIGHT, MAX_WEIGHT);
        this.kind = "Truck";
        vd.insert(new VehiclesDTO(licenseNumber,licenseType,NET_WEIGHT,MAX_WEIGHT,kind));
    }

}
