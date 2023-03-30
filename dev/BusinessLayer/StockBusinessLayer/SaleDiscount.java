package BusinessLayer.StockBusinessLayer;

import java.util.Date;

public class SaleDiscount extends Discount{

    private Date start;
    private Date end;

    public SaleDiscount(Date start, Date end, float discount) {
        super(discount);
        this.start = start;
        this.end = end;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public void setEnd(Date end) {
        this.end = end;
    }
}
