package ServiceLayer.EmployeeServiceLayer;

import BusinessLayer.EmployeeBusinessLayer.*;
import ServiceLayer.ResponseT;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class EmployeeFacade {
    private EmployeeManager employeeManager;
    private ShiftController shiftController;
    private Role RoleOfUser;
    public EmployeeFacade(Role role){
        this.employeeManager=EmployeeManager.getInstance();
        this.shiftController=new ShiftController(role,employeeManager);
    }
    public ResponseT<String> RemoveEmployee(String id) {
        try {
            return ResponseT.fromValue(employeeManager.removeEmployee(id, shiftController));
        }
        catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }
    public ResponseT<String> getMesseges(String id) {
        try {
            return ResponseT.fromValue(employeeManager.getMesseges(id));
        }
        catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }
    public ResponseT<String> sendMesseges(String id,String messege) {
        try {
            return ResponseT.fromValue(employeeManager.sendMesseges(id,messege));
        }
        catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }
    public ResponseT<String> addEmployee(String firstName, String lastName, String id, String bankAccountNumber, int salary, String empConditions, LocalDate startWorkingDate, List<Role> skills) {
        try {
            return ResponseT.fromValue(employeeManager.addEmployee(firstName, lastName, id, bankAccountNumber, salary, empConditions, startWorkingDate, skills));
        }
        catch (Exception e) {
            return ResponseT.fromError(e.getMessage());

        }
    }
    public ResponseT<String> editFirstName(String idToEdit, String firstName) {
        try {
            return ResponseT.fromValue(employeeManager.editFirstName(idToEdit, firstName));
        }
        catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<String> editLastName(String idToEdit, String lastName) {
        try {
            return ResponseT.fromValue(employeeManager.editLastName(idToEdit, lastName));
        }
        catch (Exception e){
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<String> editBankAccountNumber(String idToEdit, String newBankAccountNumber) {
        try {
            return ResponseT.fromValue(employeeManager.editBankAccountNumber(idToEdit, newBankAccountNumber));
        }
        catch (Exception e){
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<String> editSalary(String idToEdit, int newSalary) {
        try {
            return ResponseT.fromValue(employeeManager.editSalary(idToEdit, newSalary));
        }
        catch (Exception e){
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<String> editEmpConditions(String idToEdit, String newEmpConditions) {
        try {
            return ResponseT.fromValue(employeeManager.editEmpConditions(idToEdit, newEmpConditions));
        }
        catch (Exception e){
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<String> addAvailableShift(String idToEdit, LocalDate date, TypeOfShift type) {
        try {
            return ResponseT.fromValue(this.employeeManager.addAvailableShift(idToEdit, new Pair<LocalDate, TypeOfShift>(date, type)));
        }
        catch (Exception e){
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<String> removeAvailableShift(String idToEdit, LocalDate date, TypeOfShift type) {
        try {
            return ResponseT.fromValue(this.employeeManager.removeAvailableShift(idToEdit, new Pair<LocalDate, TypeOfShift>(date, type)));
        }
        catch (Exception e){
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<String> addSkill(String idToEdit, Role type) {
        try {
            return ResponseT.fromValue(this.employeeManager.addSkill(idToEdit, type));
        }
        catch (Exception e){
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<String> removeSkill(String idToEdit, Role type) {
        try {
            return ResponseT.fromValue(this.employeeManager.removeSkill(idToEdit, type));
        }
        catch (Exception e){
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<String> addEmployeeToShift(String idToEdit, Role typeEmp, LocalDate date, TypeOfShift typeShift) {
        try {
            return ResponseT.fromValue(this.shiftController.addEmployeeToShift(idToEdit, typeEmp, date, typeShift));
        }
        catch (Exception e){
            return ResponseT.fromError(e.getMessage());
        }
    }


    public ResponseT<String> checkIfEmpExist(String idToEdit) {
        try {
            return ResponseT.fromValue(this.employeeManager.checkIfEmpExist(idToEdit));
        }
        catch (Exception e){
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<String> addConstraintToShift(LocalDate date, TypeOfShift typeOfShift, Role typeOfEmployee, Integer numOfEmp) {
        try {
            return ResponseT.fromValue(this.shiftController.addConstraint(date, typeOfShift, typeOfEmployee, numOfEmp));
        }
        catch (Exception e){
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<String> removeConstraintToShift(LocalDate date, TypeOfShift typeOfShift, Role typeOfEmployee) {
        try {
            return ResponseT.fromValue(this.shiftController.removeConstraint(date, typeOfShift, typeOfEmployee));
        }
        catch (Exception e){
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<String> addShift(LocalDate date, TypeOfShift typeOfShift) {
        try {
            return ResponseT.fromValue(this.shiftController.addShift(date, typeOfShift));
        }
        catch (Exception e){
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<String> removeShift(LocalDate date, TypeOfShift typeOfShift) {
        try {
            return ResponseT.fromValue(this.shiftController.removeShift(date, typeOfShift));
        }
        catch (Exception e){
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<String> printSchedule() {
        try {
            return ResponseT.fromValue(this.shiftController.toString());
        }
        catch (Exception e){
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<String> printPersonalDetails(String idToPrint) {
        try {
            return ResponseT.fromValue(this.employeeManager.printPersonalDetails(idToPrint));
        }
        catch (Exception e){
            return ResponseT.fromError(e.getMessage());
        }
    }

    public Role getRoleOfUser() {
        return RoleOfUser;
    }

    public static Date LocalDateToDate(LocalDate d){
        ZoneId defaultZoneId = ZoneId.systemDefault();
        return Date.from(d.atStartOfDay(defaultZoneId).toInstant());
}

    public ResponseT<String> addConstraintToShift(LocalDate date, TypeOfShift typeOfShift, Role typeOfEmployee, int numOfEmp) {
        try {
            return ResponseT.fromValue(this.shiftController.addConstraint(date, typeOfShift, typeOfEmployee, numOfEmp));
        }
        catch (Exception e){
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<String> removeEmployeeFromShift(String idToEdit, LocalDate date, TypeOfShift typeShift) {
        try {
            return ResponseT.fromValue(this.shiftController.removeEmployeeFromShift(idToEdit, date, typeShift));        }
        catch (Exception e){
            return ResponseT.fromError(e.getMessage());
        }

    }
    public ResponseT<String> checkifManager(int id){
        try {
            return ResponseT.fromValue(this.employeeManager.isManager(id));        }
        catch (Exception e){
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<String> getSealedShifts(){
        try {
            return ResponseT.fromValue(this.shiftController.sealedShifts());
        }
        catch (Exception e){
            return ResponseT.fromError(e.getMessage());
        }
    }


    public ResponseT<Object> addManagerEmployeeToShift(String id, LocalDate date, TypeOfShift typeOfShift) {
        try {
            this.employeeManager.checkIfManager(id);
            this.shiftController.addEmployeeToShift(id,Role.CancellationCard,date,typeOfShift);
            return ResponseT.fromValue(this.shiftController.addEmployeeToShift(id,Role.Manager,date,typeOfShift));
        }
        catch (Exception e){
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<String> checkifHRManager(int choose) {
        try {
            return ResponseT.fromValue(this.employeeManager.isHRManager(choose));
        }
        catch (Exception e){
            return ResponseT.fromError(e.getMessage());
        }
    }


    public ResponseT<String> addDriverEmployee(String driverField, String driverField1, String driverField2, String driverField3, int parseInt, String driverField4, LocalDate date, String license) {
        try {
            return ResponseT.fromValue(this.employeeManager.addDriverEmployee(driverField,driverField1,driverField2,driverField3,parseInt,driverField4,date,license));
        }
        catch (Exception e){
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<String> updateDriverLicense(String id, String license) {
        try {
            return ResponseT.fromValue(this.employeeManager.updateDriverLicense(id,license));
        }
        catch (Exception e){
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<List<Role>> getListofRoleByID(int id) {
        try {
            return ResponseT.fromValue(employeeManager.getRolebyID(""+id));
        }
        catch (Exception e) {
            return ResponseT.fromError(e.getMessage());

        }
    }
}
