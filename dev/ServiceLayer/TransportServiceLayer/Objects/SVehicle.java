package ServiceLayer.TransportServiceLayer.Objects;


import BusinessLayer.TransportBusinessLayer.Vehicle;

public class SVehicle {
    protected String licenseNumber;
    protected String licenseType;
    protected int NET_WEIGHT;
    protected int MAX_WEIGHT;
    protected boolean available;
    protected String kind;

    public SVehicle(Vehicle v) {
        this.licenseNumber = v.getLicenseNumber();
        this.licenseType = v.getLicenseType();
        this.NET_WEIGHT = v.getNET_WEIGHT();
        this.MAX_WEIGHT = v.getMAX_WEIGHT();
    }
}
