package BusinessLayer.SupplierBusinessLayer;

public class SupplierContract {


    private int supplierId;
    private int constantDay;
    private boolean independentShipping;
    private QuantitiesDocument quantitiesDocument;

    public SupplierContract(int supplierId, int constantDay, boolean independentShipping) throws Exception {
        this.supplierId = supplierId;
        if (constantDay < 0 || constantDay > 7) throw new Exception("constant day has to be between 1 to 7 or 0 for no constant day");
        this.constantDay = constantDay;
        this.independentShipping = independentShipping;
        this.quantitiesDocument = null;
    }
//    public SupplierContract(int supplierId, int constantDay, boolean independentShipping) throws Exception {
//        this.supplierId = supplierId;
//        if (constantDay < 0 || constantDay > 7) throw new Exception("constant day has to be between 1 to 7 or 0 for no constant day");
//        this.constantDay = constantDay;
//        this.independentShipping = independentShipping;
//        this.quantitiesDocument = null;
//    }

//    public String getConstantDay() {
//        return constantDay.toString().toLowerCase();
//    }

    public boolean hasConstantDay() {
        return constantDay > 0;
    }

    public int getConstantDay() {
        return constantDay;
    }

    public boolean isIndependentShipping() {
        return independentShipping;
    }

    public QuantitiesDocument getQuantitiesDocument() {
        return quantitiesDocument;
    }
    public int getSupplierId(){return supplierId;}

    public boolean isQuantitiesDocument() {
        return quantitiesDocument != null;
    }

    public void addDiscount(SupplierProduct product, int fromAmount, double discount) {
        if (quantitiesDocument == null) {
            quantitiesDocument = new QuantitiesDocument();

        }
        quantitiesDocument.addLine(product, fromAmount, discount);
    }
    public void setConstantDay(int constantDay){
        this.constantDay = constantDay;
    }
    public void setIndependentShipping(boolean independentShipping){
        this.independentShipping = independentShipping;
    }

}