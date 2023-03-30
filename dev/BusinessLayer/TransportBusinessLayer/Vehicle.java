package BusinessLayer.TransportBusinessLayer;
import DataAccessLayer.TransportDAL.VehiclesDAO;
import DataAccessLayer.TransportDAL.VehiclesDTO;

public abstract class Vehicle {
    protected String licenseNumber;
    protected String licenseType;
    protected int NET_WEIGHT;
    protected int MAX_WEIGHT;
    protected String kind;
    protected VehiclesDAO vd;

    public Vehicle(String licenseNumber, String licenseType, int NET_WEIGHT, int MAX_WEIGHT) {
        this.licenseNumber = licenseNumber;
        this.licenseType = licenseType;
        this.NET_WEIGHT = NET_WEIGHT;
        this.MAX_WEIGHT = MAX_WEIGHT;
        vd = new VehiclesDAO();
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(String licenseType) {
        this.licenseType = licenseType;
    }

    public int getNET_WEIGHT() {
        return NET_WEIGHT;
    }

    public void setNET_WEIGHT(int NET_WEIGHT) {
        this.NET_WEIGHT = NET_WEIGHT;
    }

    public int getMAX_WEIGHT() {
        return MAX_WEIGHT;
    }

    public void setMAX_WEIGHT(int MAX_WEIGHT) {
        this.MAX_WEIGHT = MAX_WEIGHT;
        vd.update(new VehiclesDTO(licenseNumber,licenseType,NET_WEIGHT,MAX_WEIGHT,kind));
    }

    public String getKind() {
        return kind;
    }

    public static Vehicle makeVehicle(VehiclesDTO vehiclesDTO){
        return new Truck(vehiclesDTO.licensePlate,vehiclesDTO.licenseType,
                vehiclesDTO.netWeight,vehiclesDTO.maxWeight);
    }
}
