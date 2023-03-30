package BusinessLayer.SupplierBusinessLayer;

import BusinessLayer.TransportBusinessLayer.ShippingArea;
import DataAccessLayer.*;
import DataAccessLayer.SuppliersDAL.OthersDAO;
import DataAccessLayer.SuppliersDAL.SupplierDAO;
import DataAccessLayer.SuppliersDAL.SupplierProductDAO;
import DataAccessLayer.SuppliersDAL.SupplierContractDAO;
import DataAccessLayer.SuppliersDAL.QDDiscountDAO;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SupplierController {

    private static SupplierController instance = null;
    private int supplierId;
    private SupplierDAO supplierDAO;
    private OthersDAO othersDao;
    private SupplierContractDAO SupplierContractDAO;
    private QDDiscountDAO QDDiscountDAO;
    private ContactController contactController;
    private SupplierProductDAO supplierProductDAO;


    private SupplierController() {
        supplierId = 0;
        supplierDAO = new SupplierDAO();
        othersDao = new OthersDAO();
        supplierId = othersDao.getInt("count supplier id");
        System.out.println(supplierId);
        SupplierContractDAO = new SupplierContractDAO();
        QDDiscountDAO = new QDDiscountDAO();
        contactController = ContactController.getInstance();
        supplierProductDAO = new SupplierProductDAO();
    }

    public static SupplierController getInstance() {
        if (instance == null) instance = new SupplierController();
        return instance;
    }

    public List<Supplier> getSuppliers() {
        return supplierDAO.getSuppliers();
    }

    public List<Supplier> getSuppliersWithConstantDay() {
        return supplierDAO.getSuppliers().stream().filter(Supplier::isConstantDay).collect(Collectors.toList());
    }

    public List<Supplier> getSuppliersWithWeeklyOrder() {
        return supplierDAO.getSuppliers().stream().filter(supplier -> OrderController.getInstance().hasWeeklyOrder(supplier.getId())).collect(Collectors.toList());
    }

    public int getSupplierConstantDay(int supplierId) {
        SupplierContract contract = getContract(supplierId);
        if (contract == null)
            return -1;
        return contract.getConstantDay();
    }

    public Supplier getSupplier(int supplierId) {
        return supplierDAO.get(supplierId);
    }

    public int addSupplier(String name, int companyNumber, int bankAccount, Supplier.PayWay payWay, String address, ShippingArea area) {
        Supplier supplier = new Supplier(supplierId, name, companyNumber, bankAccount, payWay, address, area);
        supplierId++;
        supplierDAO.insert(supplier);
        othersDao.updateInt("count supplier id", supplierId);
        return supplier.getId();
    }

    public SupplierContract getContract(int supplierId) {
        Supplier supplier = getSupplier(supplierId);
        if (supplier != null) return supplier.getContract();
        return null;
    }

    public void setContract(int supplierId, int constantDay, boolean independentShipping) throws Exception {
        Supplier supplier = getSupplier(supplierId);
        if (supplier == null) throw new Exception("supplier " + supplierId + " doesn't exist.");
        if (supplier.getContract() != null) throw new Exception("supplier " + supplierId + " already has a contract.");
        SupplierContract supplierContract = new SupplierContract(supplierId, constantDay, independentShipping);
        supplier.setContract(supplierContract);
        SupplierContractDAO.insert(supplierContract);
    }

    public double getDiscount(int supplierId, String productName, String manufacturer, int amount) throws Exception {
        Map<Integer, Double> discounts = getDiscounts(supplierId, productName, manufacturer);
        if (discounts.isEmpty()) return 0;
        List<Integer> list = discounts.keySet().stream().filter(integer -> integer <= amount).collect(Collectors.toList());
        if (list.isEmpty()) return 0;
        int max = 0;
        for (int i : list) {
            if (i > max) max = i;
        }
        return discounts.get(max);
    }

    public void addDiscount(int supplierId, String productName, String manufacturer, int fromAmount, double discount) throws Exception {
        Supplier supplier = getSupplier(supplierId);
        if (supplier == null) throw new Exception("supplier " + supplierId + " doesn't exist.");
        SupplierContract contract = getContract(supplierId);
        if (contract == null) throw new Exception("supplier " + supplierId + " has no contract.");
        SupplierProduct product = ProductController.getInstance().getProduct(supplierId, productName, manufacturer);
        if (product == null) throw new Exception ("supplier " + supplierId + " has no product called " + productName + ".");
        if (!contract.isQuantitiesDocument()) {
            SupplierContractDAO.update(supplierId, "quantitiesDocument", 1);
        }
        contract.addDiscount(product, fromAmount, discount);
        QDDiscountDAO.insert(supplierId, product.getCatalogNumber(), productName, fromAmount, discount);
    }

    public Map<Integer, Double> getDiscounts(int supplierId, String productName, String manufacturer) throws Exception {
        Supplier supplier = getSupplier(supplierId);
        if (supplier == null) throw new Exception("supplier " + supplierId + " doesn't exist.");
        SupplierContract contract = getContract(supplierId);
        if (contract == null) throw new Exception("supplier " + supplierId + " has no contract.");
        SupplierProduct product = ProductController.getInstance().getProduct(supplierId, productName, manufacturer);
        if (product == null) throw new Exception ("supplier " + supplierId + " has no product called " + productName + ".");
        return QDDiscountDAO.getDiscounts(product, supplierId);
    }

    public boolean isShipping(int supplierId) throws Exception {
        Supplier supplier = getSupplier(supplierId);
        if (supplier == null) throw new Exception("supplier " + supplierId + " doesn't exist.");
        return supplier.isShipping();
    }

    public boolean isConstantDay(int supplierId) throws Exception {
        Supplier supplier = getSupplier(supplierId);
        if (supplier == null) throw new Exception("supplier " + supplierId + " doesn't exist.");
        return supplier.isConstantDay();
    }

    public boolean hasQuantitiesDocument(int supplierId) throws Exception {
        Supplier supplier = getSupplier(supplierId);
        if (supplier == null) throw new Exception("supplier " + supplierId + " doesn't exist.");
        return supplier.hasQuantitiesDocument();
    }

    public boolean isOrderable(int supplierId) {
        SupplierContract contract = getContract(supplierId);
        List<SupplierProduct> products = ProductController.getInstance().getProducts(supplierId);
        List<Contact> contacts = ContactController.getInstance().getSupplierContacts(supplierId);
        return contract != null && !products.isEmpty() && !contacts.isEmpty();
    }

    public void removeSupplier(int supplierId) {
        supplierDAO.delete(supplierId);
        SupplierContractDAO.delete(supplierId);
        contactController.removeContacts(supplierId);
        supplierProductDAO.delete(supplierId);
        removeContract(supplierId);
    }

    public void updateSupplier(int supplierId, String field, String content) throws Exception {
        Supplier supplier = getSupplier(supplierId);
        if (supplier == null) throw new Exception("no supplier has ID: " + supplierId);
        if (field.equals("day")) {
            SupplierContractDAO.update(supplierId, field, content);
            supplier.getContract().setConstantDay(Integer.parseInt(content));
        }
        else if (field.equals("isIndependent")){
            SupplierContractDAO.update(supplierId, field, content);
            supplier.getContract().setIndependentShipping(Integer.parseInt(content) == 1);
        }
        else
            supplierDAO.update(supplierId, field, content);
    }

    public void removeDiscount(int supplierId, String catalogNumbar, int fromAmount) {
        QDDiscountDAO.delete(supplierId, catalogNumbar, fromAmount);
    }

    public void removeContract(int supplierId) {
        SupplierContractDAO.delete(supplierId);
        QDDiscountDAO.delete(supplierId);
    }

    public String getAddress(int supplierId) throws Exception {
        Supplier supplier = getSupplier(supplierId);
        if (supplier == null) throw new Exception("no supplier has ID: " + supplierId);
        return supplier.getAddress();
    }

    public String getArea(int supplierId) throws Exception {
        Supplier supplier = getSupplier(supplierId);
        if (supplier == null) throw new Exception("no supplier has ID: " + supplierId);
        return supplier.getArea();
    }

}
