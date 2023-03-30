package ServiceLayer.TransportServiceLayer;

import BusinessLayer.EmployeeBusinessLayer.Pair;
import BusinessLayer.TransportBusinessLayer.*;
import ServiceLayer.Response;
import ServiceLayer.ResponseT;
import ServiceLayer.StockServiceLayer.StockService;
import ServiceLayer.TransportServiceLayer.Objects.SSite;
import ServiceLayer.TransportServiceLayer.Objects.STransport;
import ServiceLayer.TransportServiceLayer.Objects.STransportDocument;
import ServiceLayer.TransportServiceLayer.Objects.SVehicle;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransportService {
    private static TransportService ts;
    public TransportController tc;
    public StockService ss = StockService.getInstance();
    private TransportService() {
        tc = TransportController.getInstance();

    }
    public static TransportService getInstance(){
        if(ts==null){
            return new TransportService();
        }
        return ts;
    }

    public ResponseT<STransport> createTransport(Date date, String time, String licenseNumber, String driverID, String[] sources, String[] destOrder, String shippingArea) {
        try {
            Transport t = tc.createTransport(date, time, licenseNumber, driverID, sources, destOrder, shippingArea);
            STransport sTransport = new STransport(t);
            return ResponseT.fromValue(sTransport, "Transport added successfully");
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<Boolean> receiveProduct(int productId, int amount, Date expirationDate, boolean isInStorage, int location, int orderId) {
        return ss.receiveOrder(productId,amount, expirationDate,
                                    isInStorage, location, orderId);
    }


    public ResponseT<STransportDocument> createTransportDocument(String destinationID, String order,int orderId) {
        try {
            TransportDocument doc = tc.createTransportDocument(destinationID, order,orderId);
            STransportDocument sDoc = new STransportDocument(doc);
            return ResponseT.fromValue(sDoc, "Transport document added successfully.");
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<STransportDocument> createTransportDocumentFromSupp(int destinationID, HashMap<Integer,Integer> order,int orderId,boolean weekly) {
        try {
            TransportDocument doc = tc.createTransportDocumentFromSupp(destinationID, order,orderId,weekly);
            STransportDocument sDoc = new STransportDocument(doc);
            return ResponseT.fromValue(sDoc, "Transport document added successfully.");
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public Response removeTransportDocument(int docID) {
        try {
            tc.removeTransportDocument(docID);
            return new Response("Transport document removed successfully.");
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public Response removeTransportDocumentSupp(int orderId) {
        try {
            tc.removeTransportDocumentSupp(orderId);
            return new Response("Transport document removed successfully.");
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<String> currentStatus() {
        try {
            String data = tc.currentStatus();
            return ResponseT.fromValue(data);
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<String> statusByDates(Date beginDate, Date endDate) {
        try {
            String data = tc.statusByDates(beginDate, endDate);
            return ResponseT.fromValue(data, "");
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<String> getTransportDetails(int id) {
        try {
            String data = tc.getTransportDetails(id);
            return ResponseT.fromValue(data, "");
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<String> getAwaitingTransportDocs() {
        try {
            String data = tc.getAwaitingTransportDocs();
            return ResponseT.fromValue(data, "");
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<String> getNotAsPlannedTransportDocument() {
        try {
            String data = tc.getNotAsPlannedTransportDocument();
            return ResponseT.fromValue(data, "");
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public Response changeDriver(int transId, String driverId) {
        try {
            tc.changeDriver(transId, driverId);
            return new Response("Driver changed successfully.");
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public Response changeVehicle(int transId, String licenseNumber) {
        try {
            tc.changeVehicle(transId, licenseNumber);
            return new Response("Vehicle changed successfully.");
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public Response changeDriverAndVehicle(int transId, String driverId, String licenseNumber) {
        try {
            tc.changeDriverAndVehicle(transId, driverId, licenseNumber);
            return new Response("Vehicle changed successfully.");
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public Response addSourceToTransport(int transId, int sourceID) {
        try {
            tc.addSourceToTransport(transId, sourceID);
            return new Response("Source added successfully.");
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public Response deleteTransport(int transId) {
        try {
            tc.deleteTransport(transId);
            return new Response("Transport deleted successfully.");
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public Response addTransportDocToTransport(int transId, int destID) {
        try {
            tc.addTransportDocToTransport(transId, destID);
            return new Response("Transport document added successfully.");
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public Response removeSourceFromTransport(int transId, int sourceID) {
        try {
            tc.removeSourceFromTransport(transId, sourceID);
            return new Response("Source removed successfully.");
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public Response removeTransportDocFromTransport(int transId, int destID) {
        try {
            tc.removeTransportDocFromTransport(transId, destID);
            return new Response("Transport document removed successfully.");
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public Response startTransport(int transId) {
        try {
            tc.startTransport(transId);
            return new Response("Transport started.");
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public Response finishTransport(int transId) {
        try {
            tc.finishTransport(transId);
            return new Response("Transport finished.");
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<SSite> addSite(String address, String contactName, String contactNumber, String area) {
        try {
            Site s = tc.addSite(address, contactName, contactNumber, area);
            SSite ss = new SSite(s);
            return ResponseT.fromValue(ss, "Site added");
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public Response removeSite(int siteID) {
        try {
            tc.removeSite(siteID);
            return ResponseT.fromValue(null, "Site removed.");
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<SVehicle> addVehicle(String licenseNumber, String licenseType, int NET_WEIGHT, int MAX_WEIGHT) {
        try {
            Vehicle v = tc.addVehicle(licenseNumber, licenseType, NET_WEIGHT, MAX_WEIGHT, "truck");
            SVehicle sv = new SVehicle(v);
            return ResponseT.fromValue(sv, "Vehicle added");
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public Response removeVehicle(String licensePlate) {
        try {
            tc.removeVehicle(licensePlate);
            return new Response("Vehicle removed.");
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public ResponseT<String> searchTransport(int id) {
        try {
            String data = tc.searchTransport(id);
            return ResponseT.fromValue(data, "");
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public Response updateProducts(int docId, int pId, int quantity) {
        try {
            tc.updateProducts(docId, pId, quantity);
            return new Response("Product removed.");
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response removeProduct(int docId, int pId) {
        try {
            tc.removeProduct(docId, pId);
            return new Response("Product removed.");
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public ResponseT<String> getTransportDocDetails(int docID) {
        try {
            String data = tc.searchTransportDoc(docID);
            return ResponseT.fromValue(data, "");
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }
    public ResponseT<String> getDriversDetails() {
        try {
            String data = tc.getDriversDetails();
            return ResponseT.fromValue(data, "");
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }
    public ResponseT<String> getSitesDetails() {
        try {
            String data = tc.getSitesDetails();
            return ResponseT.fromValue(data, "");
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }
    public ResponseT<String> getVehiclesDetails() {
        try {
            String data = tc.getVehiclesDetails();
            return ResponseT.fromValue(data, "");
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

//    public ResponseT<SDriver> addDriver(int id, String name, String licenseType) {
//        try {
//            Driver d = tc.addDriver(id, name, licenseType);
//            SDriver sDoc = new SDriver(d);
//            return new ResponseT<>(sDoc, "Driver added successfully.");
//        } catch (Exception e) {
//            return new ResponseT<>(null, e.getMessage());
//        }
//    }
//
//    public Response removeDriver(String id) {
//        try {
//            tc.removeDriver(id);
//            return new Response("Driver removed successfully");
//        } catch (Exception e) {
//            return new Response(e.getMessage());
//        }
//    }
//
//    public ResponseT<SDriver> updateDriverLicenseType(int id, String licenseType) {
//        try {
//            Driver d = tc.updateDriverLicenseType(id, licenseType);
//            SDriver sDriver = new SDriver(d);
//            return new ResponseT<>(sDriver, "Driver updated successfully.");
//        } catch (Exception e) {
//            return new ResponseT<>(null, e.getMessage());
//        }
//    }

//    public ResponseT<SSite> updateSiteContact(int siteID, String name, String number) {
//        try {
//            Site s = tc.updateSiteContact(siteID, name, number);
//            SSite sSite = new SSite(s);
//            return ResponseT.fromValue(sSite, "Site updated successfully.");
//        } catch (Exception e) {
//            return ResponseT.fromError(e.getMessage());
//        }
//    }

    public ResponseT<SVehicle> updateVehicleMaxWeight(String licensePlate, int maxWeight) {
        try {
            Vehicle v = tc.updateVehicleMaxWeight(licensePlate, maxWeight);
            SVehicle sVehicle = new SVehicle(v);
            return ResponseT.fromValue(sVehicle, "Vehicle updated successfully.");
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<String> allTransports() {
        try {
            String data = tc.allTransports();
            return ResponseT.fromValue(data, "");
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<String> transportDocsDetails() {
        try {
            String data = tc.transportDocsDetails();
            return ResponseT.fromValue(data, "");
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }
    public ResponseT<HashMap<Integer, List<Pair<ProductTransport,Integer>>>> receiveOrder(int transId){
        try {
            HashMap<Integer, List<Pair<ProductTransport,Integer>>> data = tc.receiveOrder(transId);
            return ResponseT.fromValue(data, "");
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public Response loadData(){
        try {
            tc.loadData();
            return new Response("Data Loaded.");
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response makeTransportSupp(int supplierId, int orderId, int day, Map<Integer, Integer> productsToAmounts, String area) {
        try {
            tc.makeTransportSupp(supplierId, orderId,day,productsToAmounts,area);
            return new Response("Transport added successfully.");
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response updateTransport(int orderId, Map<Integer, Integer> productsToAmounts) {
        try {
            tc.updateTransport(orderId, productsToAmounts);
            return new Response("Transport Document updated successfully.");
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

}
