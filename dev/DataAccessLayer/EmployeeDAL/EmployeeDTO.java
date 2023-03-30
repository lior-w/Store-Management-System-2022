package DataAccessLayer.EmployeeDAL;

import BusinessLayer.EmployeeBusinessLayer.Pair;

import java.time.LocalDate;
import java.util.List;

public class EmployeeDTO {
    final int ID_LENGTH = 9;
    public String firstName;
    public String lastName;
    public String id;
    public String bankAccount;
    public int salary;
    public String empConditions;
    public LocalDate startWorkingDate;
    public List<String> skills;
    public List<Pair<LocalDate, String>> availableShifts;

    public EmployeeDTO(String firstName, String lastName, String id, String bankAccount, int salary, String empConditions, LocalDate startWorkingDate, List<String> skills, List<Pair<LocalDate, String>> availableShifts) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        this.bankAccount = bankAccount;
        this.salary = salary;
        this.empConditions = empConditions;
        this.startWorkingDate = startWorkingDate;
        this.skills = skills;
        this.availableShifts = availableShifts;
    }
    public EmployeeDTO(){}

    public String fieldsToString() {
        return String.format("(\"%s\",\"%s\",\"%s\",\"%s\",%s,\"%s\",\"%s\")", this.firstName, this.lastName, this.id, this.bankAccount, Integer.toString(this.salary), this.empConditions,this.startWorkingDate.toString());
    }

    public int getNumberOfAvailableShifts() {
        return 0;//@todo
    }

    public String getAvailableShifts(int index) {return "";//@todo
    }

    public int getNumberOfSkills() {
        return 0;//@todo
    }

    public String getSkills(int index) {return "";//@todo
    }
}
