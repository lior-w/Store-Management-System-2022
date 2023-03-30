package DataAccessLayer.EmployeeDAL;

public class EmployeesInShiftDTO {
    public String employeeId;
    public int shiftId;
    public String role;
    public String driverId;

    public EmployeesInShiftDTO(String employeeId, int shiftId, String role, String driverId) {
        this.employeeId = employeeId;
        this.shiftId = shiftId;
        this.role = role;
        this.driverId = driverId;
    }
}
