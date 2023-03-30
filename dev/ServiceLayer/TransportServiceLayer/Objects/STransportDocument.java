package ServiceLayer.TransportServiceLayer.Objects;

import BusinessLayer.TransportBusinessLayer.ProductTransport;
import BusinessLayer.TransportBusinessLayer.TransportDocument;

import java.util.HashMap;

public class STransportDocument {
    private int ID;
    private HashMap<ProductTransport,Integer> products;
    private boolean weekly;
    private SSite site;

    public STransportDocument(TransportDocument td) {
        this.ID = td.getID();
        this.products = new HashMap<>();
        for (ProductTransport key: td.getProducts().keySet())
        {
            this.products.put(key,td.getProducts().get(key));
        }
        this.site = new SSite(td.getSite());
        this.weekly =td.isWeekly();
    }
}
