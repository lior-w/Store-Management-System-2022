package BusinessLayer.TransportBusinessLayer;

import BusinessLayer.EmployeeBusinessLayer.Employee;
import BusinessLayer.EmployeeBusinessLayer.EmployeeManager;
import BusinessLayer.EmployeeBusinessLayer.Pair;
import BusinessLayer.EmployeeBusinessLayer.Role;
import DataAccessLayer.Repository;
import DataAccessLayer.TransportDAL.*;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class TransportController {
    private static TransportController tc;
    private EmployeeManager employeeManager;
    private Vector<Site> sites;
    private Vector<Vehicle> vehicles;
    private HashMap<String, Vector<TransportDocument>> transportDocs;
    private HashMap<String, Vector<Transport>> transports;
    private Vector<ProductTransport> productTransports;
    int transportIDCounter;
    int transportDocCounter;
    int siteIDs;
    private boolean dataLoaded = false;

    private TransportController() {
        this.employeeManager = EmployeeManager.getInstance();
        this.sites = new Vector<>();
        this.vehicles = new Vector<>();
        this.transportDocs = new HashMap<>();
        this.transportDocs.put("Awaiting", new Vector<>());
        this.transportDocs.put("Assigned", new Vector<>());
        this.transportDocs.put("Done", new Vector<>());
        this.transports = new HashMap<>();
        this.transports.put("Awaiting", new Vector<>());
        this.transports.put("InProgress", new Vector<>());
        this.transports.put("Done", new Vector<>());
        this.productTransports = new Vector<>();
        this.transportDocCounter = maxT("TransportDocuments", "id") + 1;
        this.transportIDCounter = maxT("Transports", "transportId") + 1;
        this.siteIDs = maxT("Sites", "id") + 1;
    }

    public static TransportController getInstance() {
        if (tc == null) {
            tc = new TransportController();
        }
        return tc;
    }

    public HashMap<String, Vector<Transport>> getTransports() {
        return transports;
    }

    public List<Driver> getDrivers() {
        return employeeManager.getallDrivers();
    }

    public Vector<ProductTransport> getProducts() {
        return productTransports;
    }

    public Vector<Site> getSites() {
        return sites;
    }

    public void setSites(Vector<Site> sites) {
        this.sites = sites;
    }

    public void setVehicles(Vector<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public HashMap<String, Vector<TransportDocument>> getTransportDocs() {
        return transportDocs;
    }

    public void setTransportDocs(HashMap<String, Vector<TransportDocument>> transportDocs) {
        this.transportDocs = transportDocs;
    }

    public int getTransportIDCounter() {
        return transportIDCounter;
    }

    public void setTransportIDCounter(int transportIDCounter) {
        this.transportIDCounter = transportIDCounter;
    }

    public int getTransportDocCounter() {
        return transportDocCounter;
    }

    public void setTransportDocCounter(int transportDocCounter) {
        this.transportDocCounter = transportDocCounter;
    }

    public Transport createTransport(Date dateOfTransport, String timeOfDeparture, String licenseNumber, String driverID, String[] sources, String[] idDest, String area) throws Exception {
        loadTransports();
        Vector<Site> newSite = new Vector<>();
        Vector<TransportDocument> newDoc = new Vector<>();
        ShippingArea shippingArea;
        int weight = 0;
        //check date not over due
        //check valid time of departure
        if (dateOfTransport.before(new Date()))
            throw new Exception("Date of Transport already past or invalid.");
        LocalDate ld = convertToLocalDateViaInstant(dateOfTransport);
        int hour = extractHour(timeOfDeparture);
        checkForStroreKeeper(hour,ld);
        checkForDriver(hour,ld);
        //get truck by license number and check availability on date
        Vehicle vehicle = findVehicle(licenseNumber);
        Driver driver = findDriver(driverID);
        for (Transport t : transports.get("Awaiting")) {
            if (t.getVehicle().getLicenseNumber().equals(licenseNumber))
                if (compareDates(dateOfTransport, t.getDateOfTransport()))
                    throw new Exception("This vehicle is unavailable in this date");
            if (t.getDriver().getId().equals(driverID))
                if (compareDates(dateOfTransport, t.getDateOfTransport()))
                    throw new Exception("This driver is unavailable in this date");
        }
        //get sources by ID
        for (String site : sources) {
            Site s = findSite(Integer.parseInt(site));
            newSite.add(s);
        }
        for (String id : idDest) {
            TransportDocument doc = findTransDoc(Integer.parseInt(id));
            if (transportDocs.get("Assigned").contains(doc))
                throw new Exception("Transport document assigned to another transport");
            newDoc.add(doc);
            weight += doc.totalWeight();
        }
        if (!vehicle.getLicenseType().equals(driver.getLicenseType()))
            throw new Exception("Unmatching license types.");
        if (weight > vehicle.getMAX_WEIGHT())
            throw new Exception("OVER WEIGHT");
        if (area.toLowerCase().equals("south"))
            shippingArea = ShippingArea.South;
        else if (area.toLowerCase().equals("center"))
            shippingArea = ShippingArea.Center;
        else if (area.toLowerCase().equals("north"))
            shippingArea = ShippingArea.North;
        else throw new Exception("Invalid shipping area");
        Transport newTrans = new Transport(transportIDCounter++, dateOfTransport, timeOfDeparture, vehicle, driver, newSite, newDoc, shippingArea);
        transports.get("Awaiting").add(newTrans);
        for (TransportDocument t : newDoc) {
            transportDocs.get("Awaiting").remove(t);
            transportDocs.get("Assigned").add(t);
            t.setAssignedTransport(newTrans.getID());
        }
        return newTrans;
    }

    public Vehicle findVehicle(String licenseNumber) throws Exception {
        for (Vehicle v : vehicles) {
            if (v.getLicenseNumber().equals(licenseNumber))
                return v;
        }
        VehiclesDAO vd = new VehiclesDAO();
        VehiclesDTO dto = vd.get(licenseNumber);
        if (dto != null) {
            Truck t = new Truck(dto.licensePlate, dto.licenseType, dto.netWeight,
                    dto.maxWeight);
            vehicles.add(t);
            return t;
        }
        throw new Exception("There is no vehicle with this license number.");
    }

    public Driver findDriver(String id) throws Exception {
        Employee d = employeeManager.getEmployeeByID(id);
        if (d.getSkills().contains(Role.Driver))
            return (Driver) d;
        throw new Exception("There is no driver with this ID");
    }

    public Site findSite(int id) throws Exception {
        for (Site s : sites) {
            if (s.getSiteID() == id)
                return s;
        }
        if(id==999)
        {
            return new Site(999,"Rehuven Rubin 7, Beer Sheva","Ben","0542456744",ShippingArea.South);
        }
        SiteDAO sd = new SiteDAO();
        SiteDTO dto = sd.get(id);
        if (dto != null) {
            ShippingArea sp;
            if (dto.area == "Center") {
                sp = ShippingArea.Center;
            } else if (dto.area == "South")
                sp = ShippingArea.North;
            else sp = ShippingArea.South;
            Site s = new Site(dto.id, dto.address, dto.contactName, dto.ContactNumber,
                    sp);
            sites.add(s);
            return s;
        }
        throw new Exception("There is no site in the system with id: " + id);
    }

    public TransportDocument findTransDoc(int id) throws Exception {
        for (String key : transportDocs.keySet()) {
            for (TransportDocument t : transportDocs.get(key)) {
                if (t.getID() == id)
                    return t;
            }
        }
        TransportDocumentDAO dao = new TransportDocumentDAO();
        TransportDocumentDTO dto = dao.get(id);
        if (dto != null) {
            HashMap<ProductTransport, Integer> prods = new HashMap<>();
            TransportDocumentsProductsDAO tdpd = new TransportDocumentsProductsDAO();
            Vector<TransportDocumentsProductsDTO> v = tdpd.get(dto.id);
            ProductsTransportDAO pd = new ProductsTransportDAO();
            for (TransportDocumentsProductsDTO t : v) {
                ProductTransport p = findProduct(t.productId);
                prods.put(p, t.quantity);
            }
            boolean weekly = dto.weekly.equals("true");
            Site s = findSite(dto.siteId);
            TransportDocument doc = new TransportDocument(dto.id, prods,s,weekly,dto.orderId);
            transportDocs.get(dto.status).add(doc);
            return doc;
        }
        throw new Exception("There is no transport document in the system with id: " + id);
    }

    private boolean compareDates(Date d1, Date d2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(d1);
        cal2.setTime(d2);
        boolean sameDay = cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
        return sameDay;
    }

    private ProductTransport findProduct(int ID) throws Exception {
        for (ProductTransport p : productTransports) {
            if (p.getID() == ID)
                return p;
        }
        ProductsTransportDAO pd = new ProductsTransportDAO();
        ProductsTransportDTO dto = pd.get(ID);
        if (dto != null) {
            ProductTransport p = new ProductTransport(dto.id, dto.name, dto.weight);
            productTransports.add(p);
            return p;
        }
        throw new Exception("The product does not exist");
    }

    public TransportDocument createTransportDocument(String destinationID, String order,int orderId) throws Exception {
        Site site = findSite(Integer.parseInt(destinationID));
        HashMap<ProductTransport, Integer> newOrder = new HashMap<>();
        int quantity;
        ProductTransport productTransport;
        String[] orders = order.split(",");
        for (String o : orders) {
            String[] o1 = o.split(" ");
            productTransport = findProduct(Integer.parseInt(o1[0]));
            quantity = Integer.parseInt(o1[1]);
            if (newOrder.containsKey(productTransport)) {
                newOrder.put(productTransport, newOrder.get(productTransport) + quantity);
            } else newOrder.put(productTransport, quantity);
        }
        TransportDocument newDoc = new TransportDocument(transportDocCounter++, newOrder, site,true,orderId);
        transportDocs.get("Awaiting").add(newDoc);
        return newDoc;
    }

    public TransportDocument createTransportDocumentFromSupp(int destinationID, Map<Integer,Integer> order,int orderId,boolean weekly) throws Exception {
        Site site = findSite(destinationID);
        HashMap<ProductTransport, Integer> newOrder = new HashMap<>();
        int quantity;
        ProductTransport productTransport;
        for (Integer o : order.keySet()) {
            productTransport = findProduct(o);
            quantity = order.get(o);
            if (newOrder.containsKey(productTransport)) {
                newOrder.put(productTransport, newOrder.get(productTransport) + quantity);
            } else newOrder.put(productTransport, quantity);
        }
        TransportDocument newDoc = new TransportDocument(transportDocCounter++, newOrder, site,weekly,orderId);
        transportDocs.get("Awaiting").add(newDoc);
        return newDoc;
    }

    public void removeTransportDocument(int docID) throws Exception {
        TransportDocument doc = findTransDoc(docID);
        if (transportDocs.get("Awaiting").contains(doc)) {
            transportDocs.get("Awaiting").remove(doc);
            doc.remove();
        } else if (transportDocs.get("Assigned").contains(doc) || transportDocs.get("Done").contains(doc)) {
            throw new Exception("Transport document already assigned to transport: " + doc.getAssignedTransport() + ". to remove this transport document please" +
                    "go to update transport.");
        } else throw new Exception("Transport document does not exist");
    }

    public void removeTransportDocumentSupp(int orderId) throws Exception {
        TransportDocumentDAO tdd = new TransportDocumentDAO();
        TransportDocumentDTO d = tdd.getByOrderId(orderId);
        TransportDocument doc = findTransDoc(d.id);
        if (transportDocs.get("Awaiting").contains(doc)) {
            transportDocs.get("Awaiting").remove(doc);
            doc.remove();
        } else if (transportDocs.get("Assigned").contains(doc)) {
            removeTransportDocFromTransport(doc.getAssignedTransport(),doc.getID());
        } else throw new Exception("Transport document does not exist");
    }

    public String currentStatus() {
        String data = "Drivers:\nAvailable:\n";
        loadVehicles();
        loadTransports();
        List<Driver> freeD = getDrivers();
        Vector<Driver> takenD = new Vector<>();
        Vector<Vehicle> freeV = (Vector<Vehicle>) vehicles.clone();
        Vector<Vehicle> takenV = new Vector<>();
        for (Transport t : transports.get("InProgress")) {
            takenD.add(t.getDriver());
            takenV.add(t.getVehicle());
        }
        for (Driver d : takenD) {
            for (Driver d1 : freeD) {
                if(d1.getId().equals(d.getId())) {
                    freeD.remove(d1);
                    break;
                }
            }
        }
        for (Vehicle v : takenV) {
            for (Vehicle d1 : freeV) {
                if(d1.getLicenseNumber().equals(v.getLicenseNumber())) {
                    freeV.remove(d1);
                    break;
                }
            }
        }
        for (Driver d : freeD)
            data += "ID: " + d.getId() + "   Name: " + d.getName() + "   License Type:" + d.getLicenseType() + "\n";
        data += "\nUnavailable:\n";
        for (Driver d : takenD)
            data += "ID: " + d.getId() + "   Name: " + d.getName() + "   License Type:" + d.getLicenseType() + "\n";
        data += "\nVehicles:\nAvailable:\n";
        for (Vehicle v : freeV)
            data += "Number Plate: " + v.getLicenseNumber() + "   License Type: " + v.getLicenseType() + "   Net Weight: "
                    + v.getNET_WEIGHT() + "   Max Weight: " + v.getMAX_WEIGHT() + "   Kind: " + v.getKind() + "\n";
        data += "\nUnavailable:\n";
        for (Vehicle v : takenV)
            data += "Number Plate: " + v.getLicenseNumber() + "   License Type: " + v.getLicenseType() + "   Net Weight: "
                    + v.getNET_WEIGHT() + "   Max Weight: " + v.getMAX_WEIGHT() + "   Kind: " + v.getKind() + "\n";
        data += "\nTransports In Progress:\n";
        for (Transport t : transports.get("InProgress"))
            data += t.transportDetails();
        return data;
    }

    public String transportDocumentsStatus() {
        String data = "";
        data += "Transport Documents:\nAwaiting to assign:";
        for (TransportDocument t : transportDocs.get("awaiting"))
            data += t.printDoc();
        data += "\nAssigned:\n";
        for (TransportDocument t : transportDocs.get("Assigned"))
            data += t.printDoc();
        data += "\nDone:\n";
        for (TransportDocument t : transportDocs.get("Done"))
            data += t.printDoc();
        return data;
    }

    public String statusByDates(Date beginDate, Date endDate) throws Exception {
        loadTransports();
        String data = "";
        Vector<Transport> trans = new Vector<>();
        for (String key : transports.keySet())
            for (Transport t : transports.get(key)) {
                if (t.getDateOfTransport().after(beginDate) && t.getDateOfTransport().before(endDate))
                    trans.add(t);
            }
        sortByDates(trans);
        for (Transport t : trans)
            data += t.transportDetails();
        return data;
    }

    private void sortByDates(Vector<Transport> trans) throws Exception {
        try {
            Collections.sort(trans, new Comparator<Transport>() {
                @Override
                public int compare(Transport o1, Transport o2) {
                    return o1.getDateOfTransport().compareTo(o2.getDateOfTransport());
                }
            });
        } catch (Exception e) {
            throw new Exception("Invalid dates");
        }
    }

    public String getTransportDetails(int id) throws Exception {
        Transport t = findTransport(id);
        return t.transportDetails();
    }

    public Transport findTransport(int id) throws Exception {
        for (String key : transports.keySet())
            for (Transport t : transports.get(key)) {
                if (t.getID() == id)
                    return t;
            }
        TransportDAO td = new TransportDAO();
        TransportDTO dto = td.get(id);
        if (dto != null) {
            TransportsSitesDAO tsd = new TransportsSitesDAO();
            Vector<TransportDocument> transDoc = new Vector<>();
            Vector<Site> sitesNew = new Vector<>();
            TransportDocToTransportDAO tsttd = new TransportDocToTransportDAO();
            Vector<TransportDocToTransportDTO> tdttdt = tsttd.get(id);
            Vector<TransportsSitesDTO> tsdt = tsd.get(id);
            for (TransportDocToTransportDTO d : tdttdt) {
                TransportDocument doc = findTransDoc(d.tranDocId);
                transDoc.add(doc);
            }
            for (TransportsSitesDTO t : tsdt) {
                Site s = findSite(t.siteId);
                sitesNew.add(s);
            }
            Date date = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(dto.dateOfTransport + " " +
                    dto.timeOfDeparture);
            Vehicle v = findVehicle(dto.vehicleLicensePlate);
            Driver d = findDriver(dto.driverId);
            ShippingArea sp;
            if (dto.area.equals("Center")) {
                sp = ShippingArea.Center;
            } else if (dto.area.equals("South"))
                sp = ShippingArea.North;
            else sp = ShippingArea.South;
            Transport t = new Transport(dto.transportId, date, dto.timeOfDeparture, v, d,
                    sitesNew, transDoc, sp,dto.status);
            transports.get(dto.status).add(t);
            return t;
        }
        throw new Exception("No transport exist with this id.");
    }

    public String getAwaitingTransportDocs() {
        loadTransportDocs();
        String data = "";
        for (TransportDocument t : transportDocs.get("Awaiting")) {
            data += t.printDoc();
        }
        return data;
    }

    public String getNotAsPlannedTransportDocument() {
        String data = "";
        for (String key : transportDocs.keySet()) {
            for (TransportDocument t : transportDocs.get(key)) {
                if (!t.isWeekly())
                    data += t.printDoc();
            }
        }
        return data;
    }

    public void changeDriver(int transId, String driverId) throws Exception {
        Transport t = findTransport(transId);
        Driver d = findDriver(driverId);
        if (transports.get("Done").contains(t))
            throw new Exception("Transport already finished");
        if (!d.getLicenseType().equals(t.getVehicle().getLicenseType()))
            throw new Exception("Driver does not have the necessary license type");
        t.setDriver(d);
    }

    public void changeVehicle(int transId, String licenseNumber) throws Exception {
        Transport t = findTransport(transId);
        Vehicle v = findVehicle(licenseNumber);
        if (transports.get("Done").contains(t))
            throw new Exception("Transport already finished");
        if (!v.getLicenseType().equals(t.getDriver().getLicenseType()))
            throw new Exception("Driver does not have the necessary license type");
        if (v.getMAX_WEIGHT() < t.getTotalWeight())
            throw new Exception("This vehicle can not carry the total transport weight");
        t.setVehicle(v);
    }

    public void changeDriverAndVehicle(int transId, String driverId, String licenseNumber) throws Exception {
        Transport t = findTransport(transId);
        Vehicle v = findVehicle(licenseNumber);
        Driver d = findDriver(driverId);
        if (transports.get("Done").contains(t))
            throw new Exception("Transport already finished");
        if (d.getLicenseType() != v.getLicenseType())
            throw new Exception("Driver and vehicle license types are not match");
        if (v.getMAX_WEIGHT() < t.getTotalWeight())
            throw new Exception("This vehicle can not carry the total transport weight");
        t.setDriver(d);
        t.setVehicle(v);
    }

    public void addSourceToTransport(int transId, int sourceID) throws Exception {
        Site s = findSite(sourceID);
        Transport t = findTransport(transId);
        if (transports.get("Done").contains(t))
            throw new Exception("Transport already finished");
        t.addSource(s);
    }

    public void addTransportDocToTransport(int transId, int destID) throws Exception {
        Transport t = findTransport(transId);
        TransportDocument d = findTransDoc(destID);
        if (transports.get("Done").contains(t))
            throw new Exception("Transport already finished");
        if (!transportDocs.get("Awaiting").contains(d))
            throw new Exception("Transport document is assigned to another transport");
        t.addDestination(d);
        transportDocs.get("Awaiting").remove(d);
        transportDocs.get("Assigned").add(d);
        d.assign();
    }

    public void removeSourceFromTransport(int transId, int sourceID) throws Exception {
        Site s = findSite(sourceID);
        Transport t = findTransport(transId);
        if (transports.get("Done").contains(t))
            throw new Exception("Transport already finished");
        t.removeSource(s);

    }

    public void removeTransportDocFromTransport(int transId, int destID) throws Exception {
        Transport t = findTransport(transId);
        TransportDocument d = findTransDoc(destID);
        if (transports.get("Done").contains(t))
            throw new Exception("Transport already finished");
        t.removeDestination(d);
        transportDocs.get("Assigned").remove(d);
        transportDocs.get("Awaiting").add(d);
        d.awaits();
        if (t.getDestinations().size() == 0) {
            transports.remove(t);
            t.remove();
        }
    }

    public void startTransport(int transId) throws Exception {
        Transport t = findTransport(transId);
        if (!transports.get("Awaiting").contains(t))
            throw new Exception("Transport already started or finished");
        transports.get("Awaiting").remove(t);
        transports.get("InProgress").add(t);
        t.inProgress();
    }

    public void finishTransport(int transId) throws Exception {
        Transport t = findTransport(transId);
        LocalDate now = LocalDate.now();
        now.toString();
        if (!transports.get("InProgress").contains(t))
            throw new Exception("Transport already finished or not started");
        Date d = new Date();
        int hour = d.getHours();
        if(checkForStroreKeeper(hour,convertToLocalDateViaInstant(d))) {
            transports.get("InProgress").remove(t);
            transports.get("Done").add(t);
            t.finish();
            for (TransportDocument doc : t.getDestinations()) {
                transportDocs.get("Assigned").remove(doc);
                transportDocs.get("Done").add(doc);
                doc.finish();
            }
        }
    }

    public Site addSite(String address, String contactName, String contactNumber, String area) throws Exception {
        ShippingArea shippingArea;
        if (area.toLowerCase().equals("south"))
            shippingArea = ShippingArea.South;
        else if (area.toLowerCase().equals("center"))
            shippingArea = ShippingArea.Center;
        else if (area.toLowerCase().equals("north"))
            shippingArea = ShippingArea.South;
        else throw new Exception("Invalid shipping area");
        Site s = new Site(siteIDs++, address, contactName, contactNumber, shippingArea);
        for (Site site : sites) {
            if (s.getAdderess() == site.getAdderess())
                throw new Exception("Site already exist");
        }
        sites.add(s);
        return s;
    }

    public void removeSite(int siteID) throws Exception {
        Site s = findSite(siteID);
        sites.remove(s);
        s.sd.remove(s.getSiteID());
    }

    public Vehicle addVehicle(String licenseNumber, String licenseType, int net_weight, int max_weight, String model) throws Exception {
        loadVehicles();
        if (!model.toLowerCase().equals("truck"))
            throw new Exception("No suck model exist in the system");
        Vehicle vehicle = new Truck(licenseNumber, licenseType, net_weight, max_weight);
        for (Vehicle v : vehicles) {
            if (v.getLicenseNumber().equals(licenseNumber))
                throw new Exception("Vehicle already exist");
        }
        vehicles.add(vehicle);
        return vehicle;
    }

    public void removeVehicle(String licensePlate) throws Exception {
        Vehicle s = findVehicle(licensePlate);
        vehicles.remove(s);
        s.vd.remove(s.licenseNumber);
    }

    public void deleteTransport(int transId) throws Exception {
        Transport t = findTransport(transId);
        if (!transports.get("Awaiting").contains(t))
            throw new Exception("Transport already in progress or finished");
        for (TransportDocument doc : t.getDestinations()) {
            transportDocs.get("Assigned").remove(doc);
            transportDocs.get("Awaiting").add(doc);
            doc.setAssignedTransport(-1);
        }
        transports.get("Awaiting").remove(t);
        t.remove();
    }

    public String searchTransport(int id) throws Exception {
        Transport t = findTransport(id);
        return t.transportDetails();
    }

    public void updateProducts(int docId, int pId, int quantity) throws Exception {
        TransportDocument doc = findTransDoc(docId);
        ProductTransport p = findProduct(pId);
        if (transportDocs.get("Done").contains(doc))
            throw new Exception("Transport Document already Done");
        if (doc.getAssignedTransport() == -1) {
            doc.addProduct(p, quantity);
        } else {
            Transport t = findTransport(doc.getAssignedTransport());
            if (doc.getProducts().containsKey(p)) {
                if(quantity<=0)
                    removeProduct(docId,pId);
                if (doc.getProducts().get(p) > quantity)
                    doc.addProduct(p, quantity);
                else {
                    if (t.getVehicle().getMAX_WEIGHT() >= t.getTotalWeight() - doc.getProducts().get(p) * p.getWeight() + quantity * p.getWeight()) {
                        doc.addProduct(p, quantity);
                    } else throw new Exception("New quantity caused an over weight.");
                }
            } else {
                if (t.getVehicle().getMAX_WEIGHT() >= t.getTotalWeight() + quantity * p.getWeight()) {
                    doc.addProduct(p, quantity);
                } else throw new Exception("New quantity caused an over weight.");
            }
        }
    }

    public void removeProduct(int docId, int pId) throws Exception {
        ProductTransport p = findProduct(pId);
        TransportDocument t = findTransDoc(docId);
        t.removeProduct(p);
    }

    public String searchTransportDoc(int docID) throws Exception {
        TransportDocument d = findTransDoc(docID);
        return d.printDoc();
    }

//    public Driver addDriver(String id, String name, String licenseType) throws Exception {
//        for(Driver d: drivers){
//            if(d.getId()==id)
//                throw new Exception("This driver already in the system.");
//        }
//        Driver newDriver = new Driver(id,name,licenseType);
//        drivers.add(newDriver);
//        return newDriver;
//    }
//
//    public void removeDriver(String id) throws Exception {
//        for(Driver d: drivers){
//            if(d.getId()==id) {
//                drivers.remove(d);
//                return;
//            }
//        }
//        throw new Exception("This driver ID was not found in the system");
//    }
//
//    public Driver updateDriverLicenseType(int id, String licenseType) throws Exception {
//        Driver d = findDriver(id);
//        d.setLicenseType(licenseType);
//        return d;
//    }
//
//    public Site updateSiteContact(int id, String name, String number) throws Exception {
//        Site s = findSite(id);
//        s.updateContact(name, number);
//        return s;
//    }

    public Vehicle updateVehicleMaxWeight(String licensePlate, int maxWeight) throws Exception {
        Vehicle v = findVehicle(licensePlate);
        v.setMAX_WEIGHT(maxWeight);
        return v;
    }

    public String getDriversDetails() {
        String s = "";
        int i = 1;
        List<Driver> ds = getDrivers();
        for (Driver d : ds) {
            s += i + ". ID: " + d.getId() + "   Name: " + d.getName() + "   License type: " + d.getLicenseType() + "\n";
            i++;
        }
        return s;
    }

    public String getVehiclesDetails() {
        loadVehicles();
        String s = "";
        int i = 1;
        for (Vehicle v : vehicles) {
            s += i + ". License Plate: " + v.getLicenseNumber() + "   Type: " + v.getLicenseType() + "   Max Weight: " + v.getMAX_WEIGHT() + "\n";
            i++;
        }
        return s;
    }

    public String getSitesDetails() {
        loadSites();
        String s = "";
        int i = 1;
        for (Site si : sites) {
            s += i + ". ID: " + si.getSiteID() + "   Address: " + si.getAdderess() + "   Contact Name: " + si.getContactName() + "   Contact Number: " +
                    si.getContactNumber() + "   Area: " + si.getArea() + "\n";
            i++;
        }
        return s;
    }

    public String transportDocsDetails(){
        loadTransportDocs();
        String data = "Awaiting:\n";
        for(TransportDocument d:transportDocs.get("Awaiting")){
            data+=d.printDoc();
        }
        data += "\nAssigned:\n";
        for(TransportDocument d:transportDocs.get("Assigned")){
            data+=d.printDoc();
        }
        data += "\nDone:\n";
        for(TransportDocument d:transportDocs.get("Done")){
            data+=d.printDoc();
        }
        return data;
    }



    public String allTransports() {
        for (int i = 1; i < transportIDCounter; i++) {
            try {
                findTransport(i);
            } catch (Exception ignored) {
            }
        }
        String data = "";
        for (String key : transports.keySet()) {
            data += "\n" + key + ":\n";
            for (Transport t : transports.get(key))
                data += t.transportDetails();
        }
        return data;
    }

    public TransportController cleanUp() {
        return new TransportController();
    }

    private int maxT(String tableName, String id) {
        Repository rep = Repository.getInstance();
        int maxi = 1;
        try {
            Statement statement = rep.connect().createStatement();
            ResultSet res = statement.executeQuery("SELECT MAX(" + id + ") FROM " + tableName);
            try {
                maxi = res.getInt(1);
            } catch (Exception ignored) { }
        } catch (SQLException ignored) {}
        finally { Repository.getInstance().closeConnection(); }
        return maxi;
    }

    private void loadVehicles() {
        Repository rep = Repository.getInstance();
        try {
            Statement statement = rep.connect().createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM Vehicles");
            while (res.next()) {
                findVehicle(res.getString(1));
            }
        } catch (Exception e) {
            return;
        }
        finally {
            rep.closeConnection();
        }
    }

    private void loadSites() {
        Repository rep = Repository.getInstance();
        try {
            Statement statement = rep.connect().createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM Suppliers");
            while (res.next()) {
                findSite(res.getInt(1));
            }
        } catch (Exception e) {
            return;
        }
        finally {
            rep.closeConnection();
        }
    }

    private void loadTransports() {
        for (int i = 1; i < transportIDCounter; i++) {
            try {
                findTransport(i);
            } catch (Exception e) {
                continue;
            }
        }
    }

    private void loadTransportDocs() {
        for (int i = 1; i < transportDocCounter; i++) {
            try {
                findTransDoc(i);
            } catch (Exception e) {
                continue;
            }
        }
    }

    public LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    private int extractHour(String time){
        String hour = time.substring(0, 2);
        if(hour.substring(0,1).equals("0"))
            hour = hour.substring(1,2);
        return Integer.parseInt(hour);
    }

    public boolean checkForStroreKeeper(int hour,LocalDate ld) throws Exception {
        if (hour <= 20 && hour >= 8) {
            if (!employeeManager.checkStoreKeeper(ld, "Day")) {
                throw new Exception("No store keeper");
            }
        }
        else {
            if (!employeeManager.checkStoreKeeper(ld, "Night")) {
                throw new Exception("No store keeper");
            }
        }
        return true;
    }

    public boolean checkForDriver(int hour,LocalDate ld) throws Exception {
        if (hour <= 20 && hour >= 8) {
            if (!employeeManager.checkDriver(ld, "Day")) {
                throw new Exception("No Driver");
            }
        }
        else {
            if (!employeeManager.checkDriver(ld, "Night")) {
                throw new Exception("No Driver");
            }
        }
        return true;
    }

    public HashMap<Integer,List<Pair<ProductTransport,Integer>>> receiveOrder(int transId) throws Exception {
        Transport t = findTransport(transId);
        HashMap<Integer,List<Pair<ProductTransport,Integer>>> data = new HashMap<>();
        for(TransportDocument doc:t.getDestinations()){
            for(ProductTransport p:doc.getProducts().keySet()){
                if(data.get(doc.getOrderId())!=null){
                    data.get(doc.getOrderId()).add(new Pair<>(p,doc.getProducts().get(p)));
                }
                else {
                    data.put(doc.getOrderId(),new ArrayList<>());
                    data.get(doc.getOrderId()).add(new Pair<>(p,doc.getProducts().get(p)));
                }
            }
        }
        return data;
    }

    public void loadData() throws Exception {
        if (!dataLoaded) {
            vehicles.add(new Truck("132", "A", 10000, 20000));
            vehicles.add(new Truck("465", "B", 5000, 1000));
            vehicles.add(new Truck("789", "C", 500000, 600000));
            vehicles.add(new Truck("314", "C", 10000, 20000));
            addSite("Tel Aviv, Shvil Hahartzit 10", "Ben", "0542456744", "center");
            addSite("Ramat-Gan, Aba Hillel 1", "Regev", "0542147895", "South");
            addSite("Ashkelon, Denver 3", "Nadav", "054741254", "North");
            addSite("Tel Aviv, Kinker 10", "Yossi", "054879654", "center");
            addSite("Beer Sheba, Rehuven Rubin 7", "Tzlil", "054789456", "south");
            dataLoaded = true;
        }
    }

    public void makeTransportSupp(int supplierId, int orderId, int day, Map<Integer, Integer> productsToAmounts,String area) throws Exception {
        boolean weekly = day!=0;
        TransportDocument doc = createTransportDocumentFromSupp(supplierId,productsToAmounts,orderId,weekly);
        if(weekly) {
            Date date = Date.from(Objects.requireNonNull(getDay(day)).atZone(ZoneId.systemDefault()).toInstant());
            loadTransports();
            loadVehicles();
            ArrayList<String> availableDrivers = new ArrayList<>();
            boolean availableDriver = true;
            LocalDate ld = convertToLocalDateViaInstant(date);
            int hour = extractHour("12:00");
            try {
                checkForStroreKeeper(hour, ld);
                checkForDriver(hour, ld);
                List<String> driverIds = employeeManager.getDriverFromShift(ld, "Day");
                for (String id : driverIds) {
                    for (Transport t : transports.get("Awaiting")) {
                        if (date.compareTo(t.getDateOfTransport()) == 0) {
                            if (t.getDriver().getId().equals(id)) {
                                availableDriver = false;
                                break;
                            }
                        }
                    }
                    if (availableDriver) {
                        availableDrivers.add(id);
                    }
                    availableDriver = true;
                }
                if (availableDrivers.size() == 0)
                    throw new Exception("There is no available driver.");
                ArrayList<Vehicle> availableVehicles = new ArrayList<>();
                boolean availableVehicle = true;
                for (Vehicle v : vehicles) {
                    for (Transport t : transports.get("Awaiting")) {
                        if (date.compareTo(t.getDateOfTransport()) == 0) {
                            if (t.getVehicle().getLicenseNumber().equals(v.getLicenseNumber())) {
                                availableVehicle = false;
                                break;
                            }
                        }
                    }
                    if (availableVehicle) {
                        availableVehicles.add(v);
                    }
                    availableVehicle = true;
                }
                Transport t = null;
                for (String dId : availableDrivers) {
                    for (Vehicle v : availableVehicles) {
                        Driver driver = findDriver(dId);
                        if (driver.getLicenseType().equals(v.getLicenseType())) {
                            try {
                                Vector<Site> vs = new Vector<>();
                                Vector<TransportDocument> vt = new Vector<>();
                                vt.add(doc);
                                Site site = findSite(999);
                                vs.add(site);
                                t = new Transport(transportIDCounter++,date, "12:00", v,
                                        driver, vs, vt, ShippingArea.valueOf(area));
                                doc.setAssignedTransport(t.getID());
                                return;
                            } catch (Exception ignored) {
                            }
                        }
                    }
                }
                String toSend = String.format("Could not create transport for order ID %d. Transport Document" +
                        "with ID %d added to the system", doc.getOrderId(), doc.getID());
                employeeManager.sendMesseges(Role.HRManager.toString(), toSend);
                employeeManager.sendMesseges(Role.LogisticManager.toString(), toSend);
                throw new Exception("Could not find suitable driver and truck.");
            } catch (Exception e) {
                String toSend = String.format("Could not create transport for order ID %d. Transport Document" +
                        "with ID %d added to the system", doc.getOrderId(), doc.getID());
                employeeManager.sendMesseges(Role.HRManager.toString(), toSend);
                employeeManager.sendMesseges(Role.LogisticManager.toString(), toSend);
                System.out.println(e.getMessage()+". Transport Document added to Transport System and a message sent to " +
                        "the relevant employees.");
            }
        }
    }
    public void updateTransport(int orderId, Map<Integer, Integer> productsToAmounts) throws Exception {
        TransportDocumentDAO dao = new TransportDocumentDAO();
        TransportDocumentDTO dto = dao.getByOrderId(orderId);
        for(Integer p: productsToAmounts.keySet()){
            updateProducts(dto.id,p,productsToAmounts.get(p));
        }
    }
    private LocalDateTime getDay(int day) {
        LocalDateTime dateTime = LocalDateTime.now();
        switch (day){
            case 1: {
                return dateTime.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
            }
            case 2: {
                return dateTime.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
            }
            case 3: {
                return dateTime.with(TemporalAdjusters.next(DayOfWeek.TUESDAY));
            }
            case 4: {
                return dateTime.with(TemporalAdjusters.next(DayOfWeek.WEDNESDAY));
            }
            case 5: {
                return dateTime.with(TemporalAdjusters.next(DayOfWeek.THURSDAY));
            }
            case 6: {
                return dateTime.with(TemporalAdjusters.next(DayOfWeek.FRIDAY));
            }
            case 7: {
                return dateTime.with(TemporalAdjusters.next(DayOfWeek.SATURDAY));
            }
        }
        return null;
    }
}

