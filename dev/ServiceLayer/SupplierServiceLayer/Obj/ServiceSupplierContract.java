package ServiceLayer.SupplierServiceLayer.Obj;

import BusinessLayer.SupplierBusinessLayer.SupplierContract;

public class ServiceSupplierContract {

    public final int constantDay;
    public final boolean independentShipping;
    public final ServiceQuantitiesDocument quantitiesDocument;

    public ServiceSupplierContract(SupplierContract supplierContract) {
        this.constantDay = supplierContract.getConstantDay();
        this.independentShipping = supplierContract.isIndependentShipping();
        this.quantitiesDocument = supplierContract.getQuantitiesDocument() == null ? null : new ServiceQuantitiesDocument(supplierContract.getQuantitiesDocument());
    }
}
