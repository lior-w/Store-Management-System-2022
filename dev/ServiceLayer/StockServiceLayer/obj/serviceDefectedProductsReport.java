package ServiceLayer.StockServiceLayer.obj;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.List;

public class serviceDefectedProductsReport {
    private List<serviceProductUnit> defectedProducts;

    public serviceDefectedProductsReport(List<serviceProductUnit> list) {
        defectedProducts = list;
    }

    public String showReport() {
        String output = String.format("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" +
                                         "Defected Products Report - %s\n" +
                                         "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n", new SimpleDateFormat("yyyy-MM-dd").format(Date.from(Instant.now())));
        for (serviceProductUnit p : defectedProducts) {
            output += "\nID: " + p.getId() + " - Product ID: " + p.getProductId() + (p.isExpired()?" - EXPIRED":"") + " - Location: " + p.getLocation() + (p.isInStorage() ? " - IN STORAGE" : " - IN STORE");
        }
        return output;
    }
}
