package BusinessLayer.EmployeeBusinessLayer;

import BusinessLayer.TransportBusinessLayer.Driver;
import DataAccessLayer.EmployeeDAL.*;
import DataAccessLayer.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class EmployeeManager {
    private Map<String, Employee> employees;
    private static EmployeeManager instance = null;
    private EmployeeDAO employeeDAO = new EmployeeDAO();
    private DriverDAO driverDAO = new DriverDAO();
    private ShiftDAO shiftDAO = new ShiftDAO();
    private EmployeesInShiftDAO empinShift=new EmployeesInShiftDAO();
    private MessegeDAO messegeDAO=new MessegeDAO();
    public static EmployeeManager getInstance() {
        if (instance == null)
            instance = new EmployeeManager();
        return instance;
    }
    public EmployeeManager(){
        this.employees=new HashMap<>();
    }
    public String getMesseges(String role){
        try {
            Vector<MessegeDTO> temp = messegeDAO.get(role);
            String res = "";
            for (MessegeDTO dto : temp) {
                res = res + "\n(!)" + dto.messege;
            }
            return res;
        }catch (Exception e) {
            return e.getMessage();
        }
    }
    public String sendMesseges(String role,String messege){
        try {
            messegeDAO.addMessege(role,messege);
            return "succesfully send the messege";
        }catch (Exception e) {
            return e.getMessage();
        }
    }

    public String addEmployee(String firstName, String lastName, String id, String bankAccountNumber, int salary, String empConditions, LocalDate startWorkingDate, List<Role> skills) {
        try {
            Employee employee = new Employee(firstName, lastName, id, bankAccountNumber, salary, empConditions, startWorkingDate, skills); //throws exception if fields are invalid
            if (getEmpFromDatabase(id)!=null)
                throw new Exception("ID already exists");
            this.employeeDAO.insert(employee.toDTO());//add to DB
            addSkill(id,skills.get(0));
            this.employees.put(id, employee);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "Employee added successfully";
    }

    public boolean checkStoreKeeper(LocalDate date,String s){
        int id=shiftDAO.getShiftIdByDateAndType(date,s);
        if(id==-1)
            return false;
        return empinShift.getStoreKeeper(id);
    }
    public boolean checkDriver(LocalDate date,String s){
        int id=shiftDAO.getShiftIdByDateAndType(date,s);
        if(id==-1)
            return false;
        return empinShift.getDriver(id);
    }

    public List<String> getDriverFromShift(LocalDate date,String s){
        int id=shiftDAO.getShiftIdByDateAndType(date,s);
        return empinShift.getDriverFromShift(id);
    }

    public String addDriverEmployee(String firstName, String lastName, String id, String bankAccountNumber, int salary, String empConditions, LocalDate startWorkingDate, String license) {
        try {
            List<Role> skills = new LinkedList<>();
            skills.add(Role.Driver);
            Driver driver = new Driver(firstName, lastName, id, bankAccountNumber, salary, empConditions, startWorkingDate, skills, license); //throws exception if fields are invalid
            this.employees.put(id, driver);
            this.driverDAO.insert(driver.toDTO());//add to DB check
            this.driverDAO.addSkill(id, Role.Driver.toString());
        } catch (Exception e) {
            return e.getMessage();
        }
        return "Driver Employee added successfully";
    }
    public List<Driver> getallDrivers(){
        List<DriverDTO> L1=driverDAO.getAll();
        LinkedList<Driver> ret=new LinkedList<>();
        for(DriverDTO d :L1){
            Driver e=new Driver(d);
            ret.add(e);
            employees.put(e.id,e);
        }
        return ret;
    }


    public Employee getEmpFromDatabase(String id) {
        ResultSet rs=employeeDAO.get("Employees","ID",id, Repository.getInstance().connect());
        getallDrivers();
        if (employees.get(id)!=null)
            return employees.get(id);
        EmployeeDTO dto=employeeDAO.makeDTO(id);
        if (dto==null)
            return null;
        Employee e1=new Employee(dto);
        if(!employees.containsKey(id))
            employees.put(e1.getId(),e1);
        return e1;
    }

    public String isManager(int id){
        try {
            if (!this.employees.containsKey(id+"")||!employees.get(id+"").isManager())
                throw new Exception("ID doesnt exists");
        } catch (Exception e) {
            return e.getMessage();
        }
        return "Employee added successfully";
    }
    public String isHRManager(int id){
        try {

            Employee e1=getEmpFromDatabase(id+"");
            if (!this.employees.containsKey(id+"")||e1==null||!e1.isHRManager())
                throw new Exception("ID doesnt exists");
        } catch (Exception e) {
            return e.getMessage();
        }
        return "Employee added successfully";
    }

    private void checkIfDriverAndUpdateDAO(Employee e, EmployeeDTO updatedOb) throws SQLException {
        if (e.getSkills().contains(Role.Driver)) //driver
            this.driverDAO.update((DriverDTO) updatedOb); //DB
        else
            this.employeeDAO.update(updatedOb); //DB
    }

    public String removeEmployee(String id, ShiftController shiftController) {
        Employee emp = getEmployeeByID(id);
        boolean da = false;
        List<Driver> temp = getallDrivers();
        if (employees.remove(id) == null) {
            for (Driver d : temp) {
                if (d.getId().equals(id)) {
                    da = true;
                    emp=d;
                }
            }
            if (!da)
                return "Employee not found";
        }

        //remove the employee from all of its shifts
        shiftDAO.removeEmployeeFromShiftbyId(id);
        List<Shift> shiftWithEmp = shiftController.getShiftWithEmp(id);
        for (Shift s : shiftWithEmp) {
            s.removeEmployee(id);
        }
        for(Role r:emp.getSkills()){
            if (r==Role.Driver)//driver
            {
                this.driverDAO.removeSkill(id, r.toString());
            } else {
                this.employeeDAO.removeSkill(id, r.toString());
            }
        }
        if (emp.getSkills().contains(Role.Driver)) {
            this.driverDAO.delete("ID", id);
            return "Driver removed successfully";
        } else {
            this.employeeDAO.delete("ID", id);//DB

            return "Employee removed successfully";
        }
    }



    public String editFirstName(String id, String firstName) {
        try {
            Employee e = getEmpIfExists(id);
            e.setFirstName(firstName);
            EmployeeDTO updatedOb = e.toDTO();
            checkIfDriverAndUpdateDAO(e, updatedOb);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "First Name was edited successfully";
    }
    public String editLastName(String id, String lastName) {
        try {
            Employee e = getEmpIfExists(id);
            e.setLastName(lastName);
            EmployeeDTO updatedOb = e.toDTO();
            checkIfDriverAndUpdateDAO(e, updatedOb);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "Last Name was edited successfully";
    }
    public String editBankAccountNumber(String id, String bankAccountNumber) {
        try {
            Employee e = getEmpIfExists(id);
            e.setBankAccountNumber(bankAccountNumber);
            EmployeeDTO updatedOb = e.toDTO();
            checkIfDriverAndUpdateDAO(e, updatedOb);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "Bank Account number was edited successfully";
    }
    public String editSalary(String id, int salary) {
        try {
            Employee e = getEmpIfExists(id);
            e.setSalary(salary);
            EmployeeDTO updatedOb = e.toDTO();
            checkIfDriverAndUpdateDAO(e, updatedOb);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "Salary was edited successfully";
    }
    public String editEmpConditions(String id, String empConditions) {
        try {
            Employee e = getEmpIfExists(id);
            e.setEmpConditions(empConditions);
            EmployeeDTO updatedOb = e.toDTO();
            checkIfDriverAndUpdateDAO(e, updatedOb);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "employee conditions was edited successfully";
    }
    public String addSkill(String id, Role type) {
        try {
            Employee emp = getEmpIfExists(id);
            emp.addSkill(type);
            if (emp.getSkills().contains(Role.Driver)) //driver
                this.driverDAO.addSkill(id, type.toString());
            else
                this.employeeDAO.addSkill(id, type.toString());
        } catch (Exception e) {
            return e.getMessage();
        }
        return "skill added successfully";
    }
    public String removeSkill(String id, Role type) {
        try {
            Employee e = getEmpIfExists(id);
            e.removeSkill(type);
            if (e.getSkills().contains(Role.Driver))//driver
            {
                this.driverDAO.removeSkill(id, type.toString());//DB @todo
            } else {
                this.employeeDAO.removeSkill(id, type.toString());//DB @todo
            }
        } catch (Exception e) {
            return e.getMessage();
        }
        return "skill was removed successfully";
    }

    /**
     * Adds a shift to the list of available shifts for the user by id
     *
     * @param id
     * @param shift
     * @return Success/Fail Message
     */
    public String addAvailableShift(String id, Pair<LocalDate, TypeOfShift> shift) {
        try {
            Employee e = getEmpIfExists(id);
            e.addAvailableShift(shift);
            if (e.getSkills().contains(Role.Driver))//driver
            {
                this.driverDAO.addAvailableShifts(id, shift.first, shift.second.toString());//DB
            } else {
                this.employeeDAO.addAvailableShifts(id, shift.first, shift.second.toString());//DB
            }
        } catch (Exception e) {
            return e.getMessage();
        }
        return "Available shift added successfully";
    }

    /**
     * Removes a shift from the list of available shifts for the user by id
     *
     * @param id
     * @param shift
     * @return Success/Fail Message
     */
    public String removeAvailableShift(String id, Pair<LocalDate, TypeOfShift> shift) {
        try {
            Employee e = getEmpIfExists(id);
            e.removeAvailableShift(shift);
            if (e.getSkills().contains(Role.Driver))//driver
            {
                this.driverDAO.removeAvailableShifts(id, shift.first, shift.second.toString());//DB
            } else {
                this.employeeDAO.removeAvailableShifts(id, shift.first, shift.second.toString());//DB
            }
        } catch (Exception e) {
            return e.getMessage();
        }
        return "Available shift was removed successfully";
    }

    public String checkIfEmpExist(String idToEdit) {
        if(getEmployeeByID(idToEdit)==null)
            return "emp not exist";
        return String.valueOf(this.employees.containsKey(idToEdit));
    }

    public String printPersonalDetails(String idToPrint) {
        try {
            return this.employees.get(idToPrint).toString();
        } catch (Exception e) {
            return e.getMessage();
        }
    }




    public Employee getEmpIfExists(String id) throws Exception {
        if (getEmpFromDatabase(id)==null) {
            throw new Exception("No such employee");
        }
        return this.employees.get(id);
    }

    public Employee getEmployeeByID(String id) {
        Employee emp=employees.get(id);
        if (emp!=null)
            return emp;
        return getEmpFromDatabase(id);
    }






    public void checkIfManager(String id) throws Exception {
        if(!getEmployeeByID(id).skills.contains(Role.CancellationCard)||!getEmployeeByID(id).skills.contains(Role.Manager))
            throw new Exception("Not a Manager");
    }

    public String updateDriverLicense(String id, String license) {
        try {
            List<Driver> l=getallDrivers();
            Employee e1 = getEmployeeByID(id);
            if(!e1.skills.contains(Role.Driver)){
                throw new Exception("Not a Driver");
            }
            ((Driver)e1).setLicenseType(license);
            EmployeeDTO updatedOb = ((Driver)e1).toDTO();
            checkIfDriverAndUpdateDAO(e1, updatedOb);

        } catch (Exception e) {
            return e.getMessage();
        }
        return "licensed Type Updated succesfully";
    }

    public List<Role> getRolebyID(String id) {
        try{
            Employee e=getEmpIfExists(id);
            if (e==null){
                return new LinkedList<Role>();
            }else
                return e.getSkills();
        }
        catch (Exception e) {
            return new LinkedList<Role>();
        }
    }
}