package DataAccessLayer.EmployeeDAL;

import BusinessLayer.EmployeeBusinessLayer.Pair;

import java.util.List;
import java.util.Map;

public class ShiftDTO {
    public int Id;
    public String shiftType;
    public String date;
    public Map<String, Integer> constraints;//hold the constaints that needs to be filled
    public List<Pair<String, String>> employees;
    public List<Pair<String, String>> currentShiftEmployees;
    public boolean isSealed;

    public ShiftDTO(int id, String date, String shiftType, Integer isSealed) {
        this.Id = id;
        this.shiftType = shiftType;
        this.date = date;
        this.currentShiftEmployees = employees;
        this.isSealed =  isSealed==1 ;
    }

    public String getDate() {
        return date;
    }

    public Map<String, Integer> getConstraints() {
        return constraints;
    }

    public String getEmployees(int index) {
        return String.format("(\"%s\",%s,\"%s\")", this.currentShiftEmployees.get(index).first, this.Id, this.currentShiftEmployees.get(index).second);
    }

    public List<Pair<String, String>> getCurrentShiftEmployees() {
        return currentShiftEmployees;
    }

    public String fieldsToString() {
        return String.format("(%s,\"%s\",\"%s\",%s)", this.Id, date.toString(), this.shiftType, isSealed ? 1 : 0);
    }
    public boolean isSealed() {
        return isSealed;
    }
    public String getConstraint(String type) {
        Integer amount = this.constraints.get(type);
        return String.format("(%s,\"%s\",%s)", this.Id, type, amount);
    }
    public int getNumberOfEmpInShift() {
        return this.currentShiftEmployees.size();
    }
}
