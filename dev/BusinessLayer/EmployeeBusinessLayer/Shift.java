package BusinessLayer.EmployeeBusinessLayer;


import DataAccessLayer.EmployeeDAL.EmployeesInShiftDAO;
import DataAccessLayer.EmployeeDAL.ShiftDTO;

import java.time.LocalDate;
import java.util.*;

public class Shift{
    private int id;
    private TypeOfShift shiftType;
    private LocalDate date;
    private Map<Role, Integer> constraints;//hold the constaints that needs to be filled
    //private Map<Role, List<Employee>> employees;
    private List<Pair<String, Role>> currentShiftEmployees;
    private boolean isSealed;

    public Shift(int id, TypeOfShift shiftType, LocalDate date) throws Exception {
        shiftValidityCheck(date);
        this.id = id;
        this.shiftType = shiftType;
        this.date = date;
        //this.employees = new HashMap<>();
        this.isSealed = false;
        this.constraints=new HashMap<>();
        this.currentShiftEmployees=new ArrayList<>();

    }

    public Shift(int id, TypeOfShift shiftType, LocalDate date,Map<Role, Integer> constraints,
                 List<Pair<String, Role>> currentShiftEmployees) throws Exception {
        shiftValidityCheck(date);
        this.id = id;
        this.shiftType = shiftType;
        this.date = date;
        //this.employees = new HashMap<>();
        this.isSealed = false;
        this.constraints=constraints;
        this.currentShiftEmployees=currentShiftEmployees;

    }
    public ShiftDTO toDTO() {
        return new ShiftDTO(this.id, this.date.toString(),this.shiftType.toString(), this.isSealed ? 1 : 0 );
    }
    public Shift(ShiftDTO shift) {
        this.id = shift.Id;
        this.shiftType = TypeOfShift.valueOf(shift.shiftType);
        this.date = LocalDate.of(Integer.parseInt(shift.date.substring(0,4)),Integer.parseInt(shift.date.substring(5,7)),Integer.parseInt(shift.date.substring(8,10)));
        this.currentShiftEmployees = currShiftEmpDTOToBuss(shift.currentShiftEmployees);
        this.constraints = constraintDTOToBuss(shift.constraints);
        this.isSealed = shift.isSealed;
    }
    private List<Pair<String, Role>> currShiftEmpDTOToBuss(List<Pair<String, String>> currentShiftEmployees) {
        List<Pair<String, Role>> toReturn = new LinkedList<>();
        for (Pair<String, String> p : currentShiftEmployees) {
            toReturn.add(new Pair<String, Role>(p.getKey(), Role.valueOf(p.getValue())));
        }
        return toReturn;
    }
    private Map<Role, Integer> constraintDTOToBuss(Map<String, Integer> constraints) {
        Map<Role, Integer> toReturn = new HashMap<>();
        for (String type : constraints.keySet()) {
            toReturn.put(Role.valueOf(type), constraints.get(type));
        }
        return toReturn;
    }
    private Map<String, Integer> constraintsBussToDTO(Map<Role, Integer> constraintsBusiness) {
        Map<String, Integer> toReturn = new HashMap<>();
        for (Role currType : constraintsBusiness.keySet()) {
            toReturn.put(currType.toString(), constraintsBusiness.get(currType));
        }
        return toReturn;
    }
    private List<Pair<String, String>> currEmpBusinessToDTO(List<Pair<String, Role>> currEmpBusiness) {
        List<Pair<String, String>> toReturn = new LinkedList<>();
        for (Pair<String, Role> p : currEmpBusiness) {
            toReturn.add(new Pair(p.first, p.second.toString()));
        }
        return toReturn;
    }
    public List<Pair<String, Role>> getCurrentShiftEmployees() {
        return currentShiftEmployees;
    }
    public boolean isSealed() {return isSealed;}


    private void shiftValidityCheck(LocalDate date) throws Exception {
        if (date == null) {
            throw new Exception("Date can't be null");
        }
        long m = System.currentTimeMillis();
        if (date.isBefore(LocalDate.now())) {
            throw new Exception("Date of available shift cant be in the past");
        }
    }

    public void addEmployeeToShift(Employee e1, Role type) throws Exception {
        if (e1 == null) {
            throw new Exception("employee cant be null");
        }
        if (!e1.getSkills().contains((type))) {
            throw new Exception("employee cant be assigned to a skill he doesnt have");
        }
        if (!checkConstraints(type)) {
            throw new Exception("employee cant be assigned to a shift if it doesnot match to his constrains");
        }
        if (this.isEmployeeAndSkillInShift(e1.getId(), type)) {
            throw new Exception("employee cant be assigned to a shift he is already in");
        }
        currentShiftEmployees.add(new Pair<>(e1.getId(),type));
        EmployeesInShiftDAO eid = new EmployeesInShiftDAO();
        eid.addEmployeeToShift(e1.getId(),id,type.toString());
//        if(constraints.containsKey(type)){
//            if(constraints.get(type)>1){
//                Integer a=constraints.get(type)-1;
//                constraints.put(type,a);
//            }else{
//                constraints.remove(type);
//                if(constraints.size()==0){
//                    isSealed=true;
//                }
//            }
//        }
    }

    /**
     * Check numbers of employees in the shift doesn't exceed the requested number of employees
     *
     * @param type
     * @return
     * @throws Exception
     */
    private boolean checkConstraints(Role type) throws Exception {
        if (this.constraints.containsKey(type)) {
            Integer numOfType = this.constraints.get(type);
            if (getNumberOfCurrType(type) >= numOfType) {
                throw new Exception(("number of employees of this type is exceeded"));
            }
        }
        return true;

    }

    private int getNumberOfCurrType(Role type) {
        int ans = 0;
        for (Pair p : currentShiftEmployees) {
            if (p.getValue() == type) {
                ans++;
            }
        }
        return ans;
    }

    /**
     * Compares each amount of employees of each type with requested amount in the shift
     *
     * @return
     */
    public boolean checkFull() {
        Map<Role, Integer> numOfEmp = new HashMap<>(); //Counts the number of employees of each type in the current shift
        boolean check1=false;//check if we have manager and cancelation card holder
        for (Pair pair : currentShiftEmployees) {
            if(pair.getValue()==Role.Manager){
                for(Pair pair2 : currentShiftEmployees){
                    if(pair.getKey().equals(pair2.getKey())&&pair2.getValue()==Role.CancellationCard)
                        check1=true;
                }
            }
            Role typeOfCurrEmp = (Role) pair.getValue(); //Type of the current employee
            if (!numOfEmp.containsKey(typeOfCurrEmp)) //If current type was yet to be found
            {
                numOfEmp.put(typeOfCurrEmp, 1);//Insert new element with value of 1
            } else //Increment number of types found
            {
                numOfEmp.put(typeOfCurrEmp, numOfEmp.get(typeOfCurrEmp) + 1);
            }
        }
        for (Role type : constraints.keySet()) {
            if (constraints.get(type) != numOfEmp.get(type)) {
                return false; //Found a type of employee in the constraints that isnt satisfied or exceeded maximum value
            }
        }
        if(check1)
            return true;
        return false;
    }

    public boolean isEmployeeInShift(String id) {
        for (Pair<String, Role> p : currentShiftEmployees) {
            if (p.getKey().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public boolean isEmployeeAndSkillInShift(String id, Role skill) {
        for (Pair p : currentShiftEmployees) {

            if (p.getKey().equals(id) && p.getValue() == skill) {
                return true;
            }
        }
        return false;
    }

    public boolean removeEmployee(String id) {
        Pair<String, Role> toRemove = null;
        for (Pair<String, Role> p : currentShiftEmployees) {
            {
                String currId = p.getKey();
                if (currId.equals(id)) {
                    toRemove = p;
                    break;
                }
            }
        }
        if (!currentShiftEmployees.remove(toRemove)) {
            return false;
        }
        isSealed = sealShift();
        return true;
    }

    /**
     * Adds a new constraint/Edits a value of an existing constraint
     *
     * @param typeOfEmployee
     * @param numOfEmp
     * @throws Exception
     */
    public void addConstraint(Role typeOfEmployee, Integer numOfEmp) throws Exception {
        if (numOfEmp < 0) {
            throw new Exception("Amount of Employees must be positive");
        }
        if (typeOfEmployee == Role.Manager && numOfEmp <= 0) {
            throw new Exception("Constraint of type ShiftManager must be 1 or greater");
        }
        this.constraints.put(typeOfEmployee, numOfEmp);
        isSealed = sealShift();
    }

    /**
     * Removes a constraint from the shift
     * ShiftManager constraint cant be removed since it is mandatory constraint - a exception will be thrown if type is ShiftManager
     *
     * @param typeOfEmployee
     * @throws Exception
     */
    public void removeConstraint(Role typeOfEmployee) throws Exception {
        if (typeOfEmployee == Role.Manager) {
            throw new Exception("Number of ShiftManagers in a shift must be restricted");
        }
        if (this.constraints.remove(typeOfEmployee) == null) {
            throw new Exception("No such restriction");
        }
        isSealed = sealShift();
    }

    private String printStatus() {
        if (isSealed)
            return "Sealed";
        return "Open";
    }

    public boolean sealShift() {
        return this.checkFull();
    }

    public String toString(EmployeeManager EmployeeManager) throws Exception {
        if (EmployeeManager == null)
            return "EmployeeManager is null";
        StringBuilder builder = new StringBuilder();
        builder.append("Shift: \n");
        builder.append("\t\tType: " + shiftType);
        builder.append("\n\t\tShift Date: " + date.toString());
        builder.append("\n\t\tConstraints:");
        for (Role type : constraints.keySet()) {
            builder.append("\n\t\t\tRole: " + type.toString());
            builder.append("\n\t\t\tAmount: " + constraints.get(type).toString());
        }
        builder.append("\n");
        builder.append("\n\t\tCurrent Shift Employees:");
        for (Pair<String, Role> p : currentShiftEmployees) {
            Employee emp = EmployeeManager.getEmpIfExists(p.getKey());
            builder.append("\n\t\t\tName: " + emp.getFirstName() + " " + emp.getLastName());
            builder.append("\n\t\t\tType: " + p.getValue().toString());
            List<Role> currentEmpSkills = emp.getSkills();
            builder.append("\n\t\t\tSkills: \n\t\t\t\t");
            for (Role type : currentEmpSkills) {
                builder.append((type.toString()) + " | ");
            }
            builder.append("\n");
        }
        builder.append("\n\t\tShift status: " + printStatus());
        builder.append("\n");
        return builder.toString();
    }

    public boolean isTypeEmployeeInShift(Role role) {

        for (Pair<String, Role> p : currentShiftEmployees) {

            if (p.getValue() == role) {
                return true;
            }
        }
        return false;
    }

    public Role getTypeOfSpecificEmployee(String empID) {
        if (this.isEmployeeInShift(empID)) {
            for (Pair<String, Role> p : getCurrentShiftEmployees()) {
                if (p.getKey().equals(empID)) {
                    return p.getValue();
                }
            }
        }
        return null;
    }


    public boolean containsConstraint(Role typeOfEmployee) {
        for (Role typeEmp : constraints.keySet()) {
            if (typeEmp == typeOfEmployee) {
                return true;
            }
        }
        return false;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TypeOfShift getShiftType() {
        return shiftType;
    }


    public void setShiftType(TypeOfShift shiftType) {
        this.shiftType = shiftType;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) throws Exception {
        if (date == null) {
            throw new Exception("date can't be null");
        }
        long m = System.currentTimeMillis();
        if (date.isBefore(LocalDate.now())) {
            throw new Exception("date of available shift cant be in the past");
        }
        this.date = date;
    }

    public Map<Role, Integer> getConstraints() {
        return constraints;
    }

    public void setConstraints(Map<Role, Integer> constraints) {
        this.constraints = constraints;
    }

//    public Map<Role, List<Employee>> getEmployees() {
//        return employees;
//    }
//
//    public void setEmployees(Map<Role, List<Employee>> employees) {
//        this.employees = employees;
//    }


}

