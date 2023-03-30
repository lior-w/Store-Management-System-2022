package DataAccessLayer.EmployeeDAL;

public class ShiftConstraintsDTO {
    public int shiftId;
    public String type;
    public int amount;

    public ShiftConstraintsDTO(int shiftId, String type, int amount) {
        this.shiftId = shiftId;
        this.type = type;
        this.amount = amount;
    }
}
