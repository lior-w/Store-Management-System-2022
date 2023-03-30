package ServiceLayer.TransportServiceLayer.Objects;


import BusinessLayer.TransportBusinessLayer.ShippingArea;
import BusinessLayer.TransportBusinessLayer.Site;

public class SSite {
    private int siteID;
    private String adderess;
    private String contactName;
    private String contactNumber;
    private ShippingArea area;

    public SSite(Site s) {
        this.siteID = s.getSiteID();
        this.adderess = s.getAdderess();
        this.contactName = s.getContactName();
        this.contactNumber = s.getContactNumber();
        this.area = s.getArea();
    }
}
