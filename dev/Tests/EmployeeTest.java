package Tests;

import BusinessLayer.EmployeeBusinessLayer.*;
import BusinessLayer.TransportBusinessLayer.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class EmployeeTest {
    private EmployeeManager em = EmployeeManager.getInstance();
    private ShiftController sc = new ShiftController(Role.HRManager,em);
    private TransportController tc = TransportController.getInstance();
    Employee e;
    Site s;
    TransportDocument doc;
    Transport t;
    Vehicle v;
    Driver d;
    @BeforeEach
    public void setUp() throws Exception {
        LinkedList<Role> temp=new LinkedList<>();
        LinkedList<Role> t1 = new LinkedList<>();
        temp.add(Role.Cashier);
        t1.add(Role.Driver);
        e=new Employee("shani","harel","123456789","5678",0,"0", LocalDate.of(2020,11,15),temp);

        s = new Site(100,"Hello 7, Tel Aviv", "Ben",
                "0542456754",ShippingArea.South);
        v=new Truck("132","A",10000,1000);
        //doc = tc.createTransportDocument("1","2 4,1 5");
        d = new Driver("beni","alo","205893654","12345",123456,"sad",LocalDate.of(2020,11,15),t1,"A");
    }
    @DisplayName("Add the same skill to a shift twice")
    @Test
    void addSkill() throws Exception {
        try{
            e.addSkill(Role.HRManager);
            e.addSkill(Role.HRManager);
        }
        catch (Exception e)
        {
            assertTrue(true);
            return;
        }
        fail("is already exits");
    }

    @Test
    void addAvailableShiftTest2() throws Exception {
        e.addAvailableShift(new Pair<>(LocalDate.of(2023,11,11),TypeOfShift.Day));
        try{
            e.addAvailableShift(new Pair<>(LocalDate.of(2023,11,11),TypeOfShift.Day));
        }
        catch (Exception e)
        {
            assertTrue(true);
            return;
        }
        fail("the shift is already exits");

    }
    @DisplayName("removing a shift that doesnt exist")
    @Test
    void removeAvailableShift() throws Exception {
        try{
            e.removeAvailableShift(new Pair<LocalDate,TypeOfShift>(LocalDate.of(2025,11,11),TypeOfShift.Day));
        }
        catch (Exception e)
        {
            assertTrue(true);
            return;
        }
        fail("the shift is not exits");
    }
    @DisplayName("Updating First Name")
    @Test
    void updatingEmpDetails() throws Exception {
        try{
            e.setFirstName("nadav");
            assertTrue("nadav".equals(e.getFirstName()));
        }
        catch (Exception e)
        {
            assertTrue(true);
            return;
        }
    }
    @DisplayName("Updating conditions")
    @Test
    void updatingEmpDetails2() throws Exception {
        try{
            e.setEmpConditions("have to sit doen every 10 mins");
            assertTrue("have to sit doen every 10 mins".equals(e.getEmpConditions()));
        }
        catch (Exception e)
        {
            assertTrue(true);
            return;
        }
    }
}