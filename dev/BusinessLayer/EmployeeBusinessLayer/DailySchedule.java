package BusinessLayer.EmployeeBusinessLayer;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class DailySchedule {
    private List<Shift> shifts;

    public DailySchedule(Shift shift) {
        shifts = new LinkedList<>();
        shifts.add(shift);
    }

    public DailySchedule(List<Shift> shifts) //Assumes shifts is not null and in not empty
    {
        this.shifts = shifts;
    }



    public void addShift(Shift shift) throws Exception {
        for(Shift s: shifts){
            if (s.getShiftType().equals(shift.getShiftType()))
                throw new Exception("shift type already exist");
        }
        this.shifts.add(shift);
    }

    public boolean isTypeOfShiftExists(TypeOfShift t) {
        for (Shift s : shifts)
            if (s.getShiftType().equals(t) )
                return true;
        return false;
    }

    /**
     * Doesnt do anything if shift doesn't exist
     *
     * @param date
     * @param type
     */
    public void removeShift(LocalDate date, TypeOfShift type) {
        int loc = getShiftLocation(type);
        if (loc != -1)
            this.shifts.remove(loc);
    }

    /**
     * Returns -1 if shift doesn't exist
     *
     * @param type
     * @return shift's location or -1
     */
    private int getShiftLocation(TypeOfShift type) {
        for (int i = 0; i < shifts.size(); i++)
            if (this.shifts.get(i).getShiftType() == type)
                return i;
        return -1;
    }

    public Shift getShift(TypeOfShift type) { //Assumes that shift already exists
        int location = getShiftLocation(type);
        if (location == -1) {
            return null;
        }
        return shifts.get(location);
    }

    public List<Shift> getShifts() {
        return shifts;
    }

    public String toString(EmployeeManager employeeManager) throws Exception {
        if (employeeManager == null)
            return "StaffControlerIsNull";
        StringBuilder builder = new StringBuilder();
        builder.append("Daily Schedule: \n\t");
        for (Shift s : shifts) {
            builder.append("\n\t" + s.toString(employeeManager));
        }
        builder.append("\n");
        return builder.toString();
    }
}
