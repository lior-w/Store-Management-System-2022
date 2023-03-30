package ServiceLayer.SupplierServiceLayer.Obj;

import BusinessLayer.SupplierBusinessLayer.Supplier;


public class ServiceSupplier {

    public final int id;
    public final String name;
    public final int companyNumber;
    public final int bankAccount;
    public final String payWay;
    public final ServiceSupplierContract contract;
    public final boolean isShipping;
    public final boolean isConstantDay;
    public final boolean isQuantitiesDocument;

    public ServiceSupplier(Supplier supplier) {
        this.id = supplier.getId();
        this.name = supplier.getName();
        this.companyNumber = supplier.getCompanyNumber();
        this.bankAccount = supplier.getBankAccount();
        this.payWay = supplier.getPayWay().toLowerCase();
        this.contract = supplier.getContract() == null ? null : new ServiceSupplierContract(supplier.getContract());
        this.isShipping = supplier.isShipping();
        this.isConstantDay = supplier.isConstantDay();
        this.isQuantitiesDocument = supplier.hasQuantitiesDocument();
    }
}