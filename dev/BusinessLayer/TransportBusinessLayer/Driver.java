package BusinessLayer.TransportBusinessLayer;
import BusinessLayer.EmployeeBusinessLayer.Employee;
import BusinessLayer.EmployeeBusinessLayer.Role;
import DataAccessLayer.EmployeeDAL.DriverDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Driver extends Employee {
    public String licenseType;

    public Driver(String firstName, String lastName, String id, String bankAccountNumber, int salary, String empConditions, LocalDate startWorkingDate, List<Role> skills,String licenseType) throws Exception {
        super(firstName, lastName, id, bankAccountNumber, salary, empConditions, startWorkingDate, skills);
        this.licenseType = licenseType;
    }
    public String getLicenseType() {
        return licenseType;
    }
    public String getName(){
        return firstName+" "+lastName;
    }
    public void setLicenseType(String licenseType) {
        this.licenseType = licenseType;
    }
    @Override
    public DriverDTO toDTO() {
        return new DriverDTO(this.firstName, this.lastName, this.id, this.bankAccount, this.salary, this.empConditions, this.startWorkingDate, skillsToDTO(this.skills), availableShiftsToDTO(this.availableShifts),licenseType);
    }

    public Driver(DriverDTO DTO) {
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
        this.availableShifts = availableShiftsFromDTOToBus(DTO.availableShifts);
        this.licenseType=DTO.licence;
    }
}
