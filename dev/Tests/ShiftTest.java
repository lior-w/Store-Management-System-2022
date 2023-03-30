package Tests;

import BusinessLayer.EmployeeBusinessLayer.Employee;
import BusinessLayer.EmployeeBusinessLayer.Role;
import BusinessLayer.EmployeeBusinessLayer.Shift;
import BusinessLayer.EmployeeBusinessLayer.TypeOfShift;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class ShiftTest {

    private Shift shift;
    private Employee e1;
    @BeforeEach
    public void setUp() throws Exception {
        shift = new Shift(0, TypeOfShift.Night, LocalDate.of(2023,11,20));
        LinkedList<Role> a=new LinkedList<>();
        a.add(Role.Manager);
        a.add(Role.Cashier);
        e1= new Employee("shani","harel","123030324","5678",0,"0",LocalDate.of(2021,11,20),a);

    }
    @DisplayName("employee cant be assigned to a skill he doesnt have")
    @Test
    void addEmployeeToShiftTest1() throws Exception {
        String mess="";
        try{
           shift.addEmployeeToShift(e1, Role.Driver);
        }
        catch (Exception e)
        {
            assertTrue(true);
            mess=e.getMessage();
            return;
        }
        fail("employee cant be assigned to a skill he doesnt have\n"+mess);

    }
    @Test
    void addEmployeeToShiftTest2() throws Exception {
        try{
            shift.addEmployeeToShift(e1,Role.Cashier);
        }
        catch (Exception e)
        {
            assertTrue(false);
            return;
        }
        assertTrue(true);

    }


    @Test
    void removeEmployee() {
        try{
            shift.addEmployeeToShift(e1,Role.Cashier);
            shift.removeEmployee("123030324");
        }
        catch (Exception e)
        {

            assertTrue(false);
        }
        assertFalse(shift.isEmployeeInShift("123030324"));
    }

    @Test
    void addConstraintTest1() throws Exception {
        try{
            shift.addConstraint(Role.Manager,1);
            shift.addConstraint(Role.Cashier,-2);
        }
        catch (Exception e)
        {
            assertTrue(true);
            return;
        }
        fail("Amount of Employees must be positive");
    }
//    @Test
//    void addConstraintTest2() throws Exception {
//        try{
//            shift.addConstraint(Role.Cashier,3);
//
//        }
//        catch (Exception e)
//        {
//            assertTrue(true);
//            return;
//        }
//        fail("Constraint of type ShiftManager must be 1 or greater");
//    }

    @Test
    void removeConstraint() throws Exception {
        try {
            shift.removeConstraint(Role.Cashier);
        }
        catch (Exception e)
        {
            assertTrue(true);
            return;
        }
        fail("trying to remove constraint that doesnt exist");

    }



}