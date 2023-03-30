package BusinessLayer.SupplierBusinessLayer;
import BusinessLayer.TransportBusinessLayer.ShippingArea;

import javax.security.auth.kerberos.KerberosTicket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
public class Supplier {

    public enum PayWay { CASH, CREDIT }

    private final int id;
    private String name;
    private int companyNumber;
    private int bankAccount;
    private PayWay payWay;
    private SupplierContract contract;
    private String address;
    private ShippingArea area;


    public Supplier(int id, String name, int companyNumber, int bankAccount, PayWay payWay, String address, ShippingArea area) {
        this.id = id;
        this.name = name;
        this.companyNumber = companyNumber;
        this.bankAccount = bankAccount;
        this.payWay = payWay;
        this.contract = null;
        this.address = address;
        this.area = area;
    }

    public Supplier(int id, String name, int companyNumber, int bankAccount, String payWay, String address, String area) {
        this.id = id;
        this.name = name;
        this.companyNumber = companyNumber;
        this.bankAccount = bankAccount;
        this.payWay = PayWay.valueOf(payWay);
        this.contract = null;
        this.address = address;
        this.area = ShippingArea.valueOf(area);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCompanyNumber() {
        return companyNumber;
    }

    public void setCompanyNumber(int companyNumber) {
        this.companyNumber = companyNumber;
    }

    public int getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(int bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getPayWay() {
        return payWay.toString();
    }

    public void setPayWay(PayWay payWay) {
        this.payWay = payWay;
    }

    public SupplierContract getContract() {
        return contract;
    }

    public void setContract(SupplierContract contract) {
        this.contract = contract;
    }

    public boolean isShipping() {
        if (contract != null)
            return contract.isIndependentShipping();
        return false;
    }

    public boolean isConstantDay() {
        if (contract != null)
            return contract.hasConstantDay();
        return false;
    }

    public boolean hasQuantitiesDocument() {
        if (contract != null)
            return contract.isQuantitiesDocument();
        return false;
    }

    public String getAddress() {
        return address;
    }

    public String getArea() {
        return area.toString();
    }
}
