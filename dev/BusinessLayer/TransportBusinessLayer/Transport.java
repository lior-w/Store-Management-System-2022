package BusinessLayer.TransportBusinessLayer;
import DataAccessLayer.TransportDAL.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
public class Transport {
    private int ID;
    private Date dateOfTransport;
    private String timeOfDeparture;
    private Vehicle vehicle;
    private Driver driver;
    private Vector<Site> sources;
    private Vector<TransportDocument> destinations;
    private ShippingArea area;
    public TransportDAO td;
    private TransportsSitesDAO tsd;
    private TransportDocToTransportDAO tdttd;
    public String status;

    public Transport(int ID, Date dateOfTransport, String timeOfDeparture, Vehicle vehicle, Driver driver, Vector<Site> sources, Vector<TransportDocument> destinations, ShippingArea area) {
        this.ID = ID;
        this.dateOfTransport = dateOfTransport;
        this.timeOfDeparture = timeOfDeparture;
        this.vehicle = vehicle;
        this.driver = driver;
        this.sources = sources;
        this.destinations = destinations;
        this.area = area;
        this.td = new TransportDAO();
        this.tsd = new TransportsSitesDAO();
        this.tdttd = new TransportDocToTransportDAO();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String strDate= formatter.format(dateOfTransport);
        this.status = "Awaiting";
        td.insert(new TransportDTO(ID,strDate,timeOfDeparture.toString(),
                vehicle.getLicenseNumber(),driver.getId(),area.toString(),status));
        for(Site s: sources){
            tsd.insert(new TransportsSitesDTO(ID,s.getSiteID()));
        }
        for(TransportDocument doc: destinations){
            tdttd.insert(new TransportDocToTransportDTO(ID,doc.getID()));
            doc.assign();
            doc.setAssignedTransport(ID);
        }
    }
    public Transport(int ID, Date dateOfTransport, String timeOfDeparture, Vehicle vehicle, Driver driver, Vector<Site> sources, Vector<TransportDocument> destinations, ShippingArea area,String status) {
        this.ID = ID;
        this.dateOfTransport = dateOfTransport;
        this.timeOfDeparture = timeOfDeparture;
        this.vehicle = vehicle;
        this.driver = driver;
        this.sources = sources;
        this.destinations = destinations;
        this.area = area;
        this.td = new TransportDAO();
        this.tsd = new TransportsSitesDAO();
        this.tdttd = new TransportDocToTransportDAO();
        this.status = status;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Date getDateOfTransport() {
        return dateOfTransport;
    }

    public void setDateOfTransport(Date dateOfTransport) {
        this.dateOfTransport = dateOfTransport;
    }

    public String getTimeOfDeparture() {
        return timeOfDeparture;
    }

    public void setTimeOfDeparture(String timeOfDeparture) {
        this.timeOfDeparture = timeOfDeparture;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String strDate= formatter.format(dateOfTransport);
        td.update(new TransportDTO(ID,strDate,timeOfDeparture.toString(),
                vehicle.getLicenseNumber(),driver.getId(),area.toString(),status));
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String strDate= formatter.format(dateOfTransport);
        td.update(new TransportDTO(ID,strDate,timeOfDeparture.toString(),
                vehicle.getLicenseNumber(),driver.getId(),area.toString(),status));
    }

    public Vector<Site> getSources() {
        return sources;
    }

    public void setSources(Vector<Site> sources) {
        this.sources = sources;
    }

    public Vector<TransportDocument> getDestinations() {
        return destinations;
    }

    public void setDestinations(Vector<TransportDocument> destinations) {
        this.destinations = destinations;
    }

    public ShippingArea getArea() {
        return area;
    }

    public void setArea(ShippingArea area) {
        this.area = area;
    }
    public String transportDetails(){
        String data="Transport ID: "+ID+"\n";
        String source = "";
        String dest = "";
        for(Site s: sources)
            source+=" " +s.getSiteID();
        for(TransportDocument s: destinations)
            dest+=" "+s.getSite().getSiteID();
        data+="Date: "+dateOfTransport+"   Vehicle: "+vehicle.getLicenseNumber()+"   Driver: "+driver.getName()+
    "   Sources:"+source+"   Destinations:"+dest+"   Area: "+area+"\n";
        return data;
    }
    public int getTotalWeight(){
        int weight = 0;
        for (TransportDocument doc:destinations)
            weight+=doc.totalWeight();
        return weight;
    }

    public void addSource(Site s) throws Exception {
        if(sources.contains(s))
            throw new Exception("Source already include in transport");
        sources.add(s);
        tsd.insert(new TransportsSitesDTO(ID,s.getSiteID()));
    }

    public void addDestination(TransportDocument d) throws Exception {
        if(destinations.contains(d))
            throw new Exception("Transport document already include in transport");
        if(getTotalWeight()+d.totalWeight()>vehicle.getMAX_WEIGHT())
            throw new Exception("Transport vehicle cannot handle this weight.");
        destinations.add(d);
        d.setAssignedTransport(ID);
        tdttd.insert(new TransportDocToTransportDTO(ID,d.getID()));
    }

    public void removeSource(Site s) throws Exception {
        if(!sources.contains(s))
            throw new Exception("Source does not exists in transport");
        sources.remove(s);
        tsd.remove(ID,s.getSiteID());

    }

    public void removeDestination(TransportDocument d) throws Exception {
        if(!destinations.contains(d))
            throw new Exception("Transport document does not exist in transport");
        destinations.remove(d);
        d.setAssignedTransport(-1);
        tdttd.remove(ID,d.getID());
    }


    public int remove() {
        int res = td.remove(ID);
        for(TransportDocument doc: destinations){
            tdttd.remove(ID,doc.getID());
        }
        for (Site s:sources){
            tsd.remove(s.getSiteID(),ID);
        }
        return res;
    }

    public void finish() {
        this.status = "Done";
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String strDate= formatter.format(dateOfTransport);
        td.update(new TransportDTO(ID,strDate,timeOfDeparture.toString(),
                vehicle.getLicenseNumber(),driver.getId(),area.toString(),status));
    }
    public void inProgress() {
        this.status = "InProgress";
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String strDate= formatter.format(dateOfTransport);
        td.update(new TransportDTO(ID,strDate,timeOfDeparture.toString(),
                vehicle.getLicenseNumber(),driver.getId(),area.toString(),status));
    }

}
