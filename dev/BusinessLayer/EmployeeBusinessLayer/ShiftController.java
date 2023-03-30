package BusinessLayer.EmployeeBusinessLayer;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import DataAccessLayer.EmployeeDAL.*;
import DataAccessLayer.Repository;


public class ShiftController {
    private int shiftId = 0;
    private Role typeOfLoggedIn;
    private Map<String, DailySchedule> schedule;
    private EmployeeManager employeeManager;
    private ShiftDAO shiftDAO=new ShiftDAO();


    //========================================================Constructor====================================================

    public ShiftController(Role role, EmployeeManager employeeManager) {
        this.employeeManager = employeeManager;
        this.typeOfLoggedIn = role;
        this.schedule = new HashMap<>();
        this.shiftId=shiftDAO.maxT("ID")+1;

    }


    public String sealedShifts() throws Exception {
        StringBuilder ret=new StringBuilder();
        for(DailySchedule sch:schedule.values()){
            for(Shift shift:sch.getShifts()){
                if(shift.isSealed())
                    ret.append(shift.toString(employeeManager));
            }
        }
        return ret.toString();
    }



    //========================================================Methods====================================================

    /**
     * Creates and adds a new shift to the and daily schedule and adds the new daily schedule to the schedule
     * Only an HRManager can add shifts
     *
     * @param date
     * @param type
     * @return Success/Fail message
     */
    public String addShift(LocalDate date, TypeOfShift type) {
        if (isShiftExists(date, type))
            return "Shift already exists";
        try {
            Shift toAddShift = new Shift(shiftId, type, date);
            if (!schedule.containsKey(date)) {
                DailySchedule dailySchedule = new DailySchedule(toAddShift);
                this.schedule.put(date.toString(), dailySchedule);
            } else {
                schedule.get(date).addShift(toAddShift);
            }
            this.shiftDAO.insert(toAddShift.toDTO());
        } catch (Exception e) {
            return e.getMessage();
        }
        shiftId++;// ----------------- AI -----------------------------
        return "Shift added successfully";
    }

    private boolean isShiftExists(LocalDate date, TypeOfShift type) {
        if (shiftDAO.getShiftIdByDateAndType(date,type.toString())==-1)
            return false;
        DailySchedule cur = schedule.get(date.toString());
        return true;
    }

    /**
     * Removes the shift at date "date" and of type "type" from the schedule
     * Only a HRManager can remove shifts
     *
     * @param date
     * @param type
     * @return Success/Fail message
     */

    public String removeShift(LocalDate date, TypeOfShift type) {
        loadShifts();
        if (!isShiftExists(date, type))
            return "Shift doesn't exist";
        DailySchedule dailySchedule = this.schedule.get(date.toString());
        int shiftIdToRemove = dailySchedule.getShift(type).getId();
        dailySchedule.removeShift(date, type);
        this.shiftDAO.removeShift(shiftIdToRemove);
        return "Shift was removed successfully";
    }

    /**
     * Adds employee with id "id" to the shift with date "date" and of type "type"
     *
     * @param id
     * @param toSkill
     * @param date
     * @param type
     * @return Success/Fail message
     */
    public String addEmployeeToShift(String id, Role toSkill, LocalDate date, TypeOfShift type) {
        loadShifts();
        if (!isShiftExists(date, type)) //Cant add an employee to a shift that doesn't exist
            return "Shift doesn't exist";
        try {
            Shift s = getShift(date, type);
            Employee toAdd = employeeManager.getEmployeeByID(id);
            s.addEmployeeToShift(toAdd,toSkill);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "Employee added successfully to shift";
    }

    /**
     * Remove employee with id "id" from shift with date "date" and of type "type"
     *
     * @param id
     * @param date
     * @param type
     * @return Success/Fail message
     */
    public String removeEmployeeFromShift(String id, LocalDate date, TypeOfShift type) {
        if (!isShiftExists(date, type))
            return "Shift doesn't exist";
        Shift s = getShift(date, type);
        if (!s.isEmployeeInShift(id)) //Check if the employee is in the shift
            return "Shift doesn't contain this employee";
        //check this change
        Role typeOfEmp = s.getTypeOfSpecificEmployee(id);//already check if shift contain this employee
        if (!s.removeEmployee(id))
            return "Employee was not removed from shift";
        if (this.employeeManager.getEmployeeByID(id).getSkills().contains(Role.Driver))
            this.shiftDAO.removeEmployeeFromShift(id, s.getId(), typeOfEmp.toString());
        else
            this.shiftDAO.removeDriverFromShift(id, s.getId(), typeOfEmp.toString());
        return "Employee removed successfully from shift";
    }

    /**
     * Adds a constraint/Edits an existing constraint to a specific shift
     * Only HRManager can add constraints
     *
     * @param date
     * @param typeOfShift
     * @param typeOfEmployee
     * @param numOfEmp
     * @return Success/Fail message
     */
    public String addConstraint(LocalDate date, TypeOfShift typeOfShift, Role typeOfEmployee, Integer numOfEmp) {
        try {
            if (!isShiftExists(date, typeOfShift))
                return "No such shift";
            DailySchedule dailySchedule = this.schedule.get(date.toString());
            if (!dailySchedule.isTypeOfShiftExists(typeOfShift))
                return "No such shift";
            Shift shift = dailySchedule.getShift(typeOfShift);
            boolean isInShift = getShift(date, typeOfShift).containsConstraint(typeOfEmployee);
            shift.addConstraint(typeOfEmployee, numOfEmp);
            if (isInShift)
                this.shiftDAO.updateConstraint(date, typeOfShift.toString(), typeOfEmployee.toString(), numOfEmp);
            else
                this.shiftDAO.addConstraints(shift.getId(), typeOfEmployee.toString(), numOfEmp);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "Constraint added successfully";
    }

    /**
     * Removes a constraint from a specific shift
     *
     * @param date
     * @param typeOfShift
     * @param typeOfEmployee
     * @return Success/Fail message
     */
    public String removeConstraint(LocalDate date, TypeOfShift typeOfShift, Role typeOfEmployee) {
        try {
            if (!isShiftExists(date, typeOfShift))
                return "no such shift";
            DailySchedule dailySchedule = this.schedule.get(date.toString());
            if (!dailySchedule.isTypeOfShiftExists(typeOfShift))
                return "no such shift";
            Shift shift = dailySchedule.getShift(typeOfShift);
            shift.removeConstraint(typeOfEmployee);
            this.shiftDAO.removeConstraints(shift.getId(), typeOfEmployee.toString());
        } catch (Exception e) {
            return e.getMessage();
        }
        return "constraint removed successfully";
    }

    /**
     * Checks if the requested shift contains the specific employee identified by it's ID
     *
     * @param id
     * @param date
     * @param type
     * @return true is the shift requested contains the specific employee, false if not contains
     */
    public boolean shiftContainsEmployee(String id, LocalDate date, TypeOfShift type) throws Exception {
        if (getShift(date, type) == null)
            throw new Exception("Shift doesn't exist");
        return this.getShift(date, type).isEmployeeInShift(id);
    }

    /**
     * Checks if the requested shift contains an employee that is assigned to the skill "empType"
     *
     * @param empType
     * @param date
     * @param shiftType
     * @return true if contains, false if not contains
     */
    public boolean shiftContainsTypeOfEmployee(Role empType, LocalDate date, TypeOfShift shiftType) throws Exception {
        if (getShift(date, shiftType) == null)
            throw new Exception("shift doesn't exist.");
        return this.getShift(date, shiftType).isTypeEmployeeInShift(empType);
    }



    public int getNumOfConstraint(LocalDate date, TypeOfShift type, Role empType) {
        Shift s = getShift(date, type);
        Map<Role, Integer> cons = s.getConstraints();
        int curr = -1;
        for (Role currType : cons.keySet()) {
            if (empType == currType) {
                curr = cons.get(currType);
            }
        }
        return curr;
    }


    public String toString() {
        loadShifts();
        StringBuilder builder = new StringBuilder();
        for (String d : schedule.keySet()) {
            builder.append("\nDate Of Daily Schedule: " + d);
            try {
                builder.append("\n" + schedule.get(d.toString()).toString(this.employeeManager));
            } catch (Exception e) {
            }
        }
        builder.append("\n");
        return builder.toString();
    }


    private void restoreMaxShiftID(List<ShiftDTO> allShift) {
        int max = 0;
        int currId = 0;
        for (ShiftDTO s : allShift) {
            currId = s.Id;
            if (currId > max) {
                max = currId;
            }
        }
        this.shiftId = max + 1;
    }

    //-----------------------------------------------------getters-----------------------------------------------

    public Map<String, DailySchedule> getSchedule() {
        return this.schedule;
    }

    public Shift getShift(LocalDate date, TypeOfShift type) {
        DailySchedule ds = schedule.get(date.toString());
        if (ds == null) {
            return null;
        }
        Shift s = ds.getShift(type);
        return s;
    }

    //---------------------------------------------------setters--------------------------------------------------
    public void setTypeOfLoggedIn(Role typeOfLoggedIn) {
        this.typeOfLoggedIn = typeOfLoggedIn;
    }

    public List<Shift> getShiftWithEmp(String id) {
        List<Shift> toReturn = new LinkedList<>();
        for (String d : schedule.keySet()) {
            DailySchedule daily = schedule.get(d.toString());
            for (Shift s : daily.getShifts()) {
                if (s.isEmployeeInShift(id))
                    toReturn.add(s);
            }
        }
        return toReturn;
    }

    public void loadShifts() {
        Repository rep = Repository.getInstance();
        try {
            Statement statement = rep.connect().createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM Shifts");
            while (res.next()) {
                findShift(res.getInt(1));
            }
        } catch (Exception e) {
            return;
        }
    }

    private Shift findShift(int id) throws Exception {
        for(DailySchedule ds:schedule.values()){
            for(Shift shift: ds.getShifts()){
                if(shift.getId()==id)
                    return shift;
            }
        }
        ShiftDTO sd = shiftDAO.gets(id);
        if(sd!=null) {
            ShiftConstraintsDAO sc = new ShiftConstraintsDAO();
            EmployeesInShiftDAO eisd = new EmployeesInShiftDAO();
            Vector<EmployeesInShiftDTO> eisdt = eisd.gets(sd.Id);
            Vector<ShiftConstraintsDTO> scdt= sc.get(sd.Id);
            List<Pair<String,Role>> emps = new ArrayList<>();
            Map<Role,Integer> cons = new HashMap<>();
            for(EmployeesInShiftDTO emp:eisdt){
                if(emp.driverId==null) {
                    emps.add(new Pair<String,Role>(emp.employeeId,Role.valueOf(emp.role)));
                }
                else emps.add(new Pair<String,Role>(emp.driverId,Role.valueOf(emp.role)));
            }
            for(ShiftConstraintsDTO c:scdt){
                cons.put(Role.valueOf(c.type),c.amount);
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(sd.date, formatter);
            Shift newShift = new Shift(sd.Id,TypeOfShift.valueOf(sd.shiftType),localDate,cons,emps);
            if(schedule.get(sd.date.toString())!=null)
                schedule.get(sd.date.toString()).addShift(newShift);
            else schedule.put(sd.date.toString(),new DailySchedule(newShift));
            return newShift;
        }
        throw new Exception("Shift not found");
    }

}
