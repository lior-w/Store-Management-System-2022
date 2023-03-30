package ServiceLayer.TransportServiceLayer.Objects;



import BusinessLayer.TransportBusinessLayer.ShippingArea;
import BusinessLayer.TransportBusinessLayer.Site;
import BusinessLayer.TransportBusinessLayer.Transport;
import BusinessLayer.TransportBusinessLayer.TransportDocument;

import java.util.Date;
import java.util.Vector;

public class STransport {
    private int ID;
    private Date dateOfTransport;
    private String timeOfDeparture;
    private SVehicle vehicle;
    private SDriver driver;
    private Vector<SSite> sources;
    private Vector<STransportDocument> destinations;
    private ShippingArea area;

    public STransport(Transport t) {
        this.ID = t.getID();
        this.dateOfTransport = t.getDateOfTransport();
        this.timeOfDeparture = t.getTimeOfDeparture();
        this.vehicle = new SVehicle(t.getVehicle());
        this.driver = new SDriver(t.getDriver());
        this.sources = new Vector<>();
        for (Site s : t.getSources()){
            sources.add(new SSite(s));
        }
        this.destinations = new Vector<>();
        for (TransportDocument td : t.getDestinations()){
            destinations.add(new STransportDocument(td));
        }
        this.area = t.getArea();
    }
}
