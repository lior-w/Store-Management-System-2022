package DataAccessLayer.EmployeeDAL;

import BusinessLayer.EmployeeBusinessLayer.Pair;

import java.time.LocalDate;
import java.util.List;

public class DriverDTO extends EmployeeDTO {
    public String licence="";
    public DriverDTO(String firstName, String lastName, String id, String bankAccount, int salary, String empConditions, LocalDate startWorkingDate, List<String> skills, List<Pair<LocalDate, String>> availableShifts,String driverLicense){
        super(firstName, lastName, id, bankAccount, salary, empConditions, startWorkingDate, skills, availableShifts);
        this.licence=driverLicense;
    }
    @Override
    public String fieldsToString() {
        return String.format("(\"%s\",\"%s\",\"%s\",\"%s\",%s,\"%s\",\"%s\",\"%s\")", this.firstName, this.lastName, this.id, this.bankAccount, this.salary+"", this.empConditions,this.startWorkingDate.toString(),this.licence);
    }
}
