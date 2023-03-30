package BusinessLayer.EmployeeBusinessLayer;

import DataAccessLayer.EmployeeDAL.EmployeeDTO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

public class Employee {
    final int ID_LENGTH = 9;
    protected String firstName;
    protected String lastName;
    protected String id;
    protected String bankAccount;
    protected int salary;
    protected String empConditions;
    protected LocalDate startWorkingDate;
    protected List<Role> skills;
    protected List<Pair<LocalDate, TypeOfShift>> availableShifts;
    public Employee(String firstName, String lastName, String id, String bankAccountNumber, int salary, String empConditions, LocalDate startWorkingDate, List<Role> skills) throws Exception {
        validityCheckEmp(firstName, lastName, id, bankAccountNumber, salary, empConditions, startWorkingDate, skills);//Checks validity and throws exception if found invalid field
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        this.bankAccount = bankAccountNumber;
        this.salary = salary;
        this.empConditions = empConditions;
        this.startWorkingDate = startWorkingDate;
        this.skills = skills;
        this.availableShifts = new LinkedList<>();
    }
    public Employee(EmployeeDTO DTO) {
        this.firstName = DTO.firstName;
        this.lastName = DTO.lastName;
        this.id = DTO.id;
        this.bankAccount = DTO.bankAccount;
        this.salary = DTO.salary;
        this.empConditions = DTO.empConditions;
        this.startWorkingDate = DTO.startWorkingDate;
        List<Role> TypeList = new ArrayList<>();
        for (String el : DTO.skills)
            TypeList.add(Role.valueOf(el));
        this.skills = TypeList;
        List<Pair<LocalDate, TypeOfShift>> toReturn = new LinkedList<>();
        for (Pair<LocalDate, String> t : DTO.availableShifts) {
            toReturn.add(new Pair<>(t.getKey(), TypeOfShift.valueOf(t.getValue())));
        }
        this.availableShifts = toReturn;
    }
    public Employee(){}

    public List<Pair<LocalDate, TypeOfShift>> availableShiftsFromDTOToBus(List<Pair<LocalDate, String>> DTOAvaShifts) {
        List<Pair<LocalDate, TypeOfShift>> toReturn = new LinkedList<>();
        for (Pair<LocalDate, String> p : DTOAvaShifts) {
            toReturn.add(new Pair<>(p.first, TypeOfShift.valueOf(p.second)));
        }
        return toReturn;
    }


    private void validityCheckEmp(String firstName, String lastName, String id, String bankAccountNumber, int salary, String empConditions, LocalDate startWorkingDate, List<Role> skills) throws Exception {
        if (!nameValidation(firstName)) {
            throw new Exception("first name is not valid");
        }
        if (!nameValidation(lastName)) {
            throw new Exception("last name is not valid");
        }
        if (!isValidId(id)) {
            throw new Exception("invalid id");
        }
        if (skills == null || skills.size() == 0) {
            throw new Exception("employee must have skills");
        }
        if (salary < 0) {
            throw new Exception("Salary must be greater than 0");
        }
        if (bankAccountNumber == null || bankAccountNumber.length() <= 0) {
            throw new Exception("Bank account is empty");
        }
        if (empConditions == null) //can be empty
        {
            throw new Exception("Employee conditions cant be null");
        }
        if (startWorkingDate == null) {
            throw new Exception("Date was not inserted");
        }
    }
    private boolean nameValidation(String name) {
        return name != null && name.matches("(?i)(^[a-z]+)[a-z .,-]((?! .,-)$){1,25}$");
    }

    private boolean isValidId(String id) {
        if (id == null || id.length() != ID_LENGTH) {
            return false;
        }
        for (int i = 0; i < id.length(); i++) {
            if (id.charAt(i) < '0' || id.charAt(i) > '9') {
                return false;
            }
        }
        return true;
    }
    public void addSkill(Role type) throws Exception {
        if (this.skills.contains(type)) {
            throw new Exception("Skill already exists");
        }
        //this.skills=new ArrayList<>(skills);
        this.skills.add(type);
    }
    public void removeSkill(Role type) throws Exception {
        if (this.skills.size() <= 1) //Cant have an employee without skills
        {
            throw new Exception("Employee only has 1 skill and it cannot be removed");
        }
        if (!this.skills.contains(type)) {
            throw new Exception("Skill doesn't exist");
        }
        this.skills.remove(type);
    }
    public void addAvailableShift(Pair<LocalDate, TypeOfShift> shift) throws Exception {
        boolean found = false;
        LocalDate date = shift.getKey();
        if (date.isBefore(LocalDate.now())) //An employee cant request a shift in the past
        {
            throw new Exception("Date of available shift cant be in the past");
        }
        if (this.availableShifts.contains(shift)) {
            throw new Exception("Available shift already exist");
        }
        this.availableShifts.add(shift);
    }
    public void removeAvailableShift(Pair<LocalDate, TypeOfShift> shift) throws Exception {
        if (!this.availableShifts.contains(shift)) {
            throw new Exception("Available shift doesn't exist");
        }
        this.availableShifts.remove(shift);
    }

    public String toString() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        StringBuilder builder = new StringBuilder();
        builder.append("Employee: ");
        builder.append("\n\tFirst Name: " + firstName);
        builder.append("\n\tLast Name: " + lastName);
        builder.append("\n\tID: " + id);
        builder.append("\n\tBank Account Number: " + bankAccount);
        builder.append("\n\tSalary: " + salary);
        builder.append("\n\tEmployee Conditions: " + empConditions);
        builder.append("\n\tStart Working Date: " + startWorkingDate.toString());
        builder.append("\n\tSkills:");
        for (Role type : skills)
            builder.append("\n\t\t" + type.toString());
        builder.append("\n");
        builder.append("\n\tAvailable Shifts:");
        for (Pair<LocalDate, TypeOfShift> p : availableShifts) {
            builder.append("\n\t\tDate: " + dateFormat.format(p.getKey()));
            builder.append("\n\t\tType: " + p.getValue().toString() + "\n");
        }
        builder.append("\n");
        return builder.toString();
    }
    public LocalDate getStartWorkingDate() {
        return startWorkingDate;
    }

    public int getSalary() {
        return salary;
    }

    public List<Role> getSkills() {
        return skills;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public List<Pair<LocalDate, TypeOfShift>> getAvailableShifts() {
        return availableShifts;
    }

    public String getEmpConditions() {
        return empConditions;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }
    public void setAvailableShifts(List<Pair<LocalDate, TypeOfShift>> availableShifts) throws Exception {
        if (availableShifts == null)
            throw new Exception("available shifts cant be null");
        this.availableShifts = availableShifts;
    }

    public void setBankAccountNumber(String bankAccountNumber) throws Exception {
        if (bankAccountNumber == null || bankAccountNumber.length() <= 0)
            throw new Exception("Bank account is empty");
        this.bankAccount = bankAccountNumber;
    }

    public void setEmpConditions(String empConditions) throws Exception {
        if (empConditions == null) //can be empty
            throw new Exception("Employee conditions cant be null");
        this.empConditions = empConditions;
    }

    public void setFirstName(String firstName) throws Exception {
        if (!nameValidation(firstName))
            throw new Exception("Invalid First Name");
        this.firstName = firstName;
    }

    public void setId(String id) throws Exception {
        if (!isValidId(id))
            throw new Exception("Invalid id");
        this.id = id;
    }

    public void setLastName(String lastName) throws Exception {
        if (!nameValidation(lastName))
            throw new Exception("Invalid Last Name");
        this.lastName = lastName;
    }

    public void setSalary(int salary) throws Exception {
        if (salary < 0)
            throw new Exception("Salary must be greater than 0");
        this.salary = salary;
    }

    public void setSkills(List<Role> skills) throws Exception {
        if (skills == null || skills.size() == 0)
            throw new Exception("employee must have skills");
        this.skills = skills;
    }

    public void setStartWorkingDate(LocalDate startWorkingDate) throws Exception {
        if (startWorkingDate == null)
            throw new Exception("Date was not inserted");
        this.startWorkingDate = startWorkingDate;
    }
    public boolean isManager(){
        if(skills.contains(Role.Manager)&&skills.contains(Role.CancellationCard))
            return true;
        return false;
    }
    public boolean isHRManager(){
        if(skills.contains(Role.HRManager))
            return true;
        return false;
    }
    public EmployeeDTO toDTO() {
        return new EmployeeDTO(this.firstName, this.lastName, this.id, this.bankAccount, this.salary, this.empConditions, this.startWorkingDate, skillsToDTO(this.skills), availableShiftsToDTO(this.availableShifts));
    }
    public List<String> skillsToDTO(List<Role> skillsBusiness) {
        List<String> skills = new LinkedList<>();
        for (Role type : skillsBusiness) {
            skills.add(type.toString());
        }
        return skills;
    }
    public List<Pair<LocalDate, String>> availableShiftsToDTO(List<Pair<LocalDate, TypeOfShift>> availableShiftBusiness) {
        List<Pair<LocalDate, String>> availableShift = new LinkedList<>();
        for (Pair<LocalDate, TypeOfShift> p : availableShiftBusiness)
            availableShift.add(new Pair<LocalDate, String>(p.first, p.second.toString()));
        return availableShift;
    }



}
