package BusinessLayer.TransportBusinessLayer;
import DataAccessLayer.TransportDAL.*;

import java.util.HashMap;

public class TransportDocument {
    private int ID;

    private int orderId;
    private HashMap<ProductTransport,Integer> products;
    private boolean weekly;
    private Site site;
    private int assignedTransport;
    private String status;
    public TransportDocument(int ID, HashMap<ProductTransport, Integer> products, Site site,boolean weekly,int orderId) {
        this.ID = ID;
        this.products = products;
        this.site = site;
        this.weekly = weekly;
        this.orderId = orderId;
        TransportDocumentDAO tdd = new TransportDocumentDAO();
        TransportDocumentsProductsDAO tdpd = new TransportDocumentsProductsDAO();
        this.assignedTransport=tdd.assignedTransport(ID);
        this.status = "Awaiting";
        tdd.insert(new TransportDocumentDTO(ID, ""+weekly,
                site.getSiteID(),assignedTransport,status,orderId));
        for (ProductTransport p:products.keySet()){
            tdpd.insert(new TransportDocumentsProductsDTO(ID,p.getID(),products.get(p)));
        }
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public HashMap<ProductTransport, Integer> getProducts() {
        return products;
    }

    public void setProducts(HashMap<ProductTransport, Integer> products) {
        this.products = products;
    }

    public boolean isWeekly() {
        return weekly;
    }

    public void setWeekly(boolean weekly) {
        this.weekly = weekly;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public int getAssignedTransport() {
        return assignedTransport;
    }

    public void setAssignedTransport(int assignedTransport) {
        this.assignedTransport = assignedTransport;
        if(assignedTransport!=-1) {
            this.status = "Assigned";
        }
        else {
            this.status = "Awaiting";
        }
        TransportDocumentDAO tdd = new TransportDocumentDAO();
        tdd.update(new TransportDocumentDTO(ID, ""+weekly,
                site.getSiteID(),assignedTransport,status,orderId));
    }

    public int totalWeight(){
        int weight = 0;
        for(ProductTransport p: products.keySet()){
            weight += p.getWeight()*products.get(p);
        }
        return weight;
    }

    public String printDoc(){
        String data = "\nID:"+ID+"\nTotal Weight: "+totalWeight()+"   Area: "+site.getArea()+"   To Address: "+site.getAdderess()+
                "   As Weekly: "+weekly;
        if(assignedTransport==-1)
            data+="   Assigned to transport: False\n";
        else data+="   Assigned to transport: "+assignedTransport+"\n";
        data += "Products:\n";
        for(ProductTransport p: products.keySet()){
            data += p.productDetails()+"   Quantity:"+products.get(p)+"\n";
        }
        data+="Total Weight: "+totalWeight()+" Kg\n";
        return data;
    }

    public void removeProduct(ProductTransport p) throws Exception {
        TransportDocumentsProductsDAO tdpd = new TransportDocumentsProductsDAO();
        if(!products.containsKey(p))
            throw new Exception("This product does not exist in the system");
        products.remove(p);
        tdpd.remove(ID,p.getID());
    }

    public void addProduct(ProductTransport p, int quantity){
        TransportDocumentsProductsDAO tdpd = new TransportDocumentsProductsDAO();
        if(products.containsKey(p)){
            products.put(p,quantity);
            tdpd.update(new TransportDocumentsProductsDTO(ID,p.getID(),quantity));
        }
        else {
            products.put(p,quantity);
            tdpd.insert(new TransportDocumentsProductsDTO(ID,p.getID(),quantity));
        }

    }

    public int remove(){
        TransportDocumentDAO tdd = new TransportDocumentDAO();
        TransportDocumentsProductsDAO tdpd = new TransportDocumentsProductsDAO();
        int res = tdd.remove(ID);
        for(ProductTransport p: products.keySet()){
            res+=tdpd.remove(ID,p.getID());
        }
        TransportDocToTransportDAO tdttd = new TransportDocToTransportDAO();
        if(assignedTransport!=-1){tdttd.remove(assignedTransport,ID);}
        return res;
    }

    public void finish() {
        TransportDocumentDAO tdd = new TransportDocumentDAO();
        this.status = "Done";
        tdd.update(new TransportDocumentDTO(ID, ""+weekly,
                site.getSiteID(),assignedTransport,status,orderId));
    }

    public void awaits() {
        TransportDocumentDAO tdd = new TransportDocumentDAO();
        this.status = "Awaiting";
        tdd.update(new TransportDocumentDTO(ID, ""+weekly,
                site.getSiteID(),assignedTransport,status,orderId));
    }

    public void assign() {
        TransportDocumentDAO tdd = new TransportDocumentDAO();
        this.status = "Assigned";
        tdd.update(new TransportDocumentDTO(ID, ""+weekly,
                site.getSiteID(),assignedTransport,status,orderId));
    }
}
