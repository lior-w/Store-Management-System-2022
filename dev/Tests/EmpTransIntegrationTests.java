package Tests;

import BusinessLayer.EmployeeBusinessLayer.*;
import BusinessLayer.TransportBusinessLayer.*;
import DataAccessLayer.Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class EmpTransIntegrationTests {
    private EmployeeManager em = EmployeeManager.getInstance();
    private ShiftController sc = new ShiftController(Role.HRManager, em);
    private TransportController tc = TransportController.getInstance();
    Repository res = Repository.getInstance();
    Employee e;
    Site s;
    TransportDocument doc;
    Transport t;
    Vehicle v;
    Driver d;
    LinkedList<Role> temp;

    @BeforeEach
    public void setUp() throws Exception {
        temp = new LinkedList<>();
        v = tc.findVehicle("314");
        s = tc.findSite(2);
        doc = tc.findTransDoc(5);
        LinkedList<Role> t1 = new LinkedList<>();
        temp.add(Role.Cashier);
        t1.add(Role.Driver);
        sc.addShift( tc.convertToLocalDateViaInstant(new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("12/11/2023 12:00")),
                TypeOfShift.Day);
        e = new Employee("shani", "harel", "123456789", "5678", 0, "0", LocalDate.of(2020, 11, 15), temp);
        sc.addEmployeeToShift("205893654",Role.Driver,
                tc.convertToLocalDateViaInstant(new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("11/11/2023 12:00")),TypeOfShift.Day);
        sc.addEmployeeToShift("111333444",Role.Storekeeper,
                tc.convertToLocalDateViaInstant(new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("12/11/2023 12:00")),TypeOfShift.Day);
//        s = new Site(100,"Hello 7, Tel Aviv", "Ben",
//                "0542456754",ShippingArea.South);
//        v=new Truck("132","A",10000,1000);
//        doc = tc.createTransportDocument("1","2 4,1 5");
        d = new Driver("beni", "alo", "205893654", "12345", 123456, "sad", LocalDate.of(2020, 11, 15), t1, "C");


    }

    @Test
    void addDriver() throws Exception {
        try {
            em.addDriverEmployee("beni", "alo", "205893654", "12345", 123456, "sad", LocalDate.of(2020, 11, 15), "A");
            assertTrue(em.getEmployeeByID("205893654") != null);
        } catch (Exception e) {
            fail("is already exits");
            return;
        }
    }

    @Test
    void addDriverNOTSUCCEED() throws Exception {
        try {
            em.addDriverEmployee("beni", "alo", "205893654", "12345", 123456, "sad", LocalDate.of(2020, 11, 15), "A");
            em.addDriverEmployee("beni", "alo", "205893654", "12345", 123456, "sad", LocalDate.of(2020, 11, 15), "D");
            assertTrue(em.getEmployeeByID("205893654") != null);
        } catch (Exception e) {
            fail("is already exits");
            return;
        }

    }

    @Test
    void updateDriverLicenseSuccess() throws Exception {
        try {
            em.addDriverEmployee("beni", "alo", "205893654", "12345", 123456, "sad", LocalDate.of(2020, 11, 15), "A");
            em.updateDriverLicense("205893654", "C");
            assertTrue(((Driver) em.getEmployeeByID("205893654")).licenseType == "C");
        } catch (Exception e) {
            fail("is already exits");
            return;
        }

    }

    @Test
    void createTransportFailNoStorekeeper() throws Exception {
        try {
            tc.createTransport(new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("12/12/2023 12:00"),"12:00","132","205893654",new String[]{"1"},new String[]{"2"},"Center");
            fail();
        } catch (Exception e) {
            assertTrue(true);
            return;
        }
    }

    @Test
    void createTransportFailNoDriver() throws Exception {
        try {
            tc.createTransport(new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("12/11/2023 12:00"),"12:00","132","205893654",new String[]{"1"},new String[]{"2"},"Center");
            fail();
        } catch (Exception e) {
            assertTrue(true);
            return;
        }
    }

    @Test
    void createTransportSuccess() throws Exception {
        try {
            Transport t1 = tc.createTransport(new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("11/11/2023 12:00"),"12:00",
                    v.getLicenseNumber(),d.getId(), new String[]{String.format("%d",s.getSiteID())},
                    new String[]{String.format("%d",doc.getID())},"Center");
            tc.deleteTransport(t1.getID());
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void checkForStorekeeperInShiftFail() throws Exception {
        try {
            em.addEmployee("beni", "alo", "205893654", "12345", 123456, "sad", LocalDate.of(2020, 11, 15), temp);
            tc.checkForStroreKeeper(12, tc.convertToLocalDateViaInstant(new Date()));
            fail();
        } catch (Exception e) {
            assertTrue(true);
            return;
        }
    }

    @Test
    void checkForDriverInShiftFail() throws Exception {
        try {
            em.addEmployee("beni", "alo", "205893654", "12345", 123456, "sad", LocalDate.of(2020, 11, 15), temp);
            tc.checkForDriver(12, tc.convertToLocalDateViaInstant(new Date()));
            fail();
        } catch (Exception e) {
            assertTrue(true);
            return;
        }
    }

    @Test
    void checkForStorekeeperInShiftSuccess() throws Exception {
        try {
            em.addEmployee("beni", "alo", "205893654", "12345", 123456, "sad", LocalDate.of(2020, 11, 15), temp);
            tc.checkForStroreKeeper(12, tc.convertToLocalDateViaInstant(new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("21/05/2022 12:00")));
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void checkForDriverInShiftSuccess() throws Exception {
        try {
            em.addEmployee("beni", "alo", "205893654", "12345", 123456, "sad", LocalDate.of(2020, 11, 15), temp);
            tc.checkForStroreKeeper(12, tc.convertToLocalDateViaInstant(new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("12/11/2023 12:00")));
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }

}