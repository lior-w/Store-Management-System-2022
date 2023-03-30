package DataAccessLayer.TransportDAL;

public class SiteDTO {
    public int id;
    public String address;
    public String contactName;
    public String ContactNumber;
    public String area;

    public SiteDTO(int id, String address, String contactName, String contactNumber,String area) {
        this.id = id;
        this.address = address;
        this.contactName = contactName;
        this.ContactNumber = contactNumber;
        this.area = area;
    }
    public String fieldsToString() {
        return String.format("(\"%s\",\"%s\",\"%s\",\"%s\",\"%s\")", this.id, this.address, this.contactName,this.ContactNumber, this.area);
    }
}
