package BusinessLayer.TransportBusinessLayer;
import DataAccessLayer.TransportDAL.SiteDAO;
import DataAccessLayer.TransportDAL.SiteDTO;

public class Site {
    private int siteID;
    private String adderess;
    private String contactName;
    private String contactNumber;
    private ShippingArea area;
    SiteDAO sd;

    public Site(int siteID,String adderess, String contactName, String contactNumber, ShippingArea area) {
        this.siteID = siteID;
        this.adderess = adderess;
        this.contactName = contactName;
        this.contactNumber = contactNumber;
        this.area = area;
        this.sd = new SiteDAO();
        sd.insert(new SiteDTO(siteID,adderess,contactName,contactNumber,area.toString()));
    }

    public int getSiteID() {
        return siteID;
    }

    public void setSiteID(int siteID) {
        this.siteID = siteID;
    }

    public String getAdderess() {
        return adderess;
    }

    public void setAdderess(String adderess) {
        this.adderess = adderess;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public int remove(){
        int res = sd.remove(siteID);
        return res;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public ShippingArea getArea() {
        return area;
    }

    public void setArea(ShippingArea area) {
        this.area = area;
    }
    public void updateContact(String name, String number){
        setContactName(name);
        setContactNumber(number);
        sd.update(new SiteDTO(siteID,adderess,contactName,contactNumber,area.toString()));
    }
}
