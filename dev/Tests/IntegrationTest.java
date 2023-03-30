package Tests;

import BusinessLayer.EmployeeBusinessLayer.*;
import BusinessLayer.StockBusinessLayer.Category;
import BusinessLayer.StockBusinessLayer.Product;
import BusinessLayer.StockBusinessLayer.ProductUnit;
import BusinessLayer.StockBusinessLayer.StockController;
import BusinessLayer.SupplierBusinessLayer.*;
import BusinessLayer.TransportBusinessLayer.*;
import DataAccessLayer.EmployeeDAL.DriverDAO;
import DataAccessLayer.EmployeeDAL.EmployeeDAO;
import DataAccessLayer.Repository;
import DataAccessLayer.StockDAL.CategoryDAO;
import DataAccessLayer.StockDAL.ProductDAO;
import DataAccessLayer.StockDAL.ProductUnitDAO;
import DataAccessLayer.SuppliersDAL.*;
import DataAccessLayer.TransportDAL.*;
import ServiceLayer.ResponseT;
import ServiceLayer.StockServiceLayer.StockService;
import ServiceLayer.SupplierServiceLayer.SupService;
import ServiceLayer.TransportServiceLayer.TransportService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

public class IntegrationTest {

    OrderController oc = OrderController.getInstance();
    TransportService transService = TransportService.getInstance();
    StockService stockService = StockService.getInstance();
    Category superCategory;
    Category category;
    Category subCategory;
    Product product;
    List<ProductUnit> units;
    Supplier supplier;
    SupplierContract contract;
    
    ProductUnitDAO unitDAO = new ProductUnitDAO();
    ProductDAO prodDAO = new ProductDAO();
    CategoryDAO catDAO = new CategoryDAO();
    SupplierDAO supplierDAO = new SupplierDAO();
    SupplierContractDAO scDAO = new SupplierContractDAO();
    QDDiscountDAO qddDAO = new QDDiscountDAO();
    SupplierProductDAO spDAO = new SupplierProductDAO();
    OrderLineDAO olDAO = new OrderLineDAO();
    OrderDAO orderDAO = new OrderDAO();
    ContactDAO cDAO = new ContactDAO();
    SiteDAO siteDAO = new SiteDAO();
    OrderLineDAO orderLineDAO = new OrderLineDAO();
    
    private EmployeeManager em = EmployeeManager.getInstance();
    private ShiftController sc = new ShiftController(Role.HRManager, em);
    private TransportController tc = TransportController.getInstance();
    Employee employee;
    Site site;
    Transport t;
    Vehicle vehicle;
    Driver driver;
    LinkedList<Role> temp;
    VehiclesDAO vehiclesDAO = new VehiclesDAO();
    TransportDocumentDAO docDAO = new TransportDocumentDAO();
    TransportDAO transportDAO = new TransportDAO();

    @BeforeEach
    void setUp() {
        try {
            Repository.getInstance().closeConnection();
            superCategory = new Category("supertest", null);
            catDAO.insert(superCategory);
            category = new Category("test", superCategory.getId());
            catDAO.insert(category);
            subCategory = new Category("subtest", category.getId());
            catDAO.insert(subCategory);
            product = new Product("test", 10, subCategory.getId(), "test", 5.0f, 1.0f);
            prodDAO.insert(product);
            stockService.addProductUnit(product.getId(), new SimpleDateFormat("yyyy-MM-dd").parse("2023-06-28"),3, true, null, 10);
            
            units = new ArrayList<>();
            units.addAll(unitDAO.getProductUnits(product.getId()));
            prodDAO.update(product.getId(), "amount", 10);
            supplier = new Supplier(666, "LiorHakelev", 724, 55329, Supplier.PayWay.CREDIT, "street 2, Tel-Aviv", ShippingArea.Center);
            supplierDAO.insert(supplier);
            contract = new SupplierContract(666, 2, true);
            SupplierProduct sProduct = new SupplierProduct("20", "test", 5.0f, "test");
            contract.addDiscount(sProduct, 1, 10);
            scDAO.insert(contract);
            qddDAO.insert(666, "20", "test", 1, 10);
            
            spDAO.insert(sProduct, 666);
            cDAO.insert(new Contact("roei", "rbirnfeld@gmail.com", "0509698240"), 666);
            
            temp = new LinkedList<>();
            vehicle = new Truck("666", "A", 1000, 5000);
            site = tc.findSite(supplier.getId());
            LinkedList<Role> t1 = new LinkedList<>();
            temp.add(Role.Cashier);
            t1.add(Role.Driver);
            sc.addShift(tc.convertToLocalDateViaInstant(new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("27/06/2022 12:00")),
                    TypeOfShift.Day);
            employee = new Employee("shani", "harel", "666777888", "5678", 0, "0", LocalDate.of(2020, 11, 15), temp);
            sc.addEmployeeToShift("205893654", Role.Driver,
                    tc.convertToLocalDateViaInstant(new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("27/06/2022 12:00")), TypeOfShift.Day);
            sc.addEmployeeToShift("111333444", Role.Storekeeper,
                    tc.convertToLocalDateViaInstant(new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("27/06/2022 12:00")), TypeOfShift.Day);
            driver = new Driver("Leenoy", "Yehezkhel", "777888999", "12345", 123456, "sad", LocalDate.of(2020, 11, 15), t1, "A");
            
        }
        catch (Exception ignored) {}
        finally { Repository.getInstance().closeConnection(); }
    }

    @AfterEach
    void cleanData() {
        Repository.getInstance().closeConnection();
        catDAO.delete(category.getId());
        catDAO.delete(subCategory.getId());
        catDAO.delete(superCategory.getId());
        prodDAO.delete(product.getId());
        for (ProductUnit unit : units) {
            unitDAO.delete(unit.getId());
        }
        supplierDAO.delete(supplier.getId());
        scDAO.delete(contract.getSupplierId());
        cDAO.delete(supplier.getId());
        spDAO.delete(666);
        qddDAO.delete(666);
        vehiclesDAO.remove(vehicle.getLicenseNumber());
        em.removeEmployee(employee.getId(), sc);
        try {
            sc.removeShift(tc.convertToLocalDateViaInstant(new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("27/06/2022 12:00")),
                    TypeOfShift.Day);
        }
        catch (Exception ignored) {}
        finally { Repository.getInstance().closeConnection(); }
        em.removeEmployee(driver.getId(), sc);
        Repository.getInstance().closeConnection();
    }

    @Test
    void createOrderAndTransportDocumentFromShortageOrder() {
        Repository.getInstance().closeConnection();
        int orderId = -1;
        ResponseT<Integer> res = stockService.removeProductUnit(units.get(0).getId());
        if (!res.isError()) {
            orderId = res.getData();
        }
        Order order = orderDAO.get(orderId);
        Order.OrderLine orderLine = olDAO.initialize(order).getOrderLines().get(0);
        assertEquals(orderLine.productName, product.getName());
        Repository.getInstance().closeConnection();
        TransportDocumentDTO dt = docDAO.getByOrderId(orderId);
        assertNotNull(dt);
        try {
            tc.removeTransportDocument(dt.id);
        }
        catch (Exception ignored){}
    }
    
    @Test
    void createTransportFromWeeklyOrder() {
        Repository.getInstance().closeConnection();
        SupService supService = SupService.getInstance();
        Map<String, Integer> catalogNumberToAmountMap = new HashMap<>();
        catalogNumberToAmountMap.put("20", 5);
        int orderId = -1;
        ResponseT<Integer> res = supService.makeWeeklyOrder(666, catalogNumberToAmountMap);
        if (!res.isError()) orderId = res.getData();
        assertNotEquals(orderId,-1);
        TransportDocumentDTO dt = docDAO.getByOrderId(orderId);
        assertNotNull(dt);
        try {
            Transport t = tc.findTransport(dt.assignedTransportId);
            assertNotNull(t);
        }
        catch (Exception ignored){}
        supService.removeWeeklyOrder(orderId);
    }
}
