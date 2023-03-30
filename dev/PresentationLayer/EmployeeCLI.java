package PresentationLayer;

import DataAccessLayer.Repository;
import ServiceLayer.EmployeeServiceLayer.EmployeeFacade;
import BusinessLayer.EmployeeBusinessLayer.Role;
import BusinessLayer.EmployeeBusinessLayer.TypeOfShift;

import java.time.LocalDate;
import java.util.*;

public class EmployeeCLI  {
    private final int NUMBER_OF_EMPLOYEE_FIELDS = 7; //magic number
    private final int NUMBER_OF_DRIVER_FIELDS = 8; //magic number
    public static final Scanner scanner=new Scanner(System.in);
    //================================================Fields===========================================================
    private Map menuMain;
    private int menuMainOption;

    private Map menuEdit;
    private int menuEditOption;

    private Map AREmployee;
    private int AREmployeeOption;
    private Map menuManagerMain;
    private int menuManagerMainOpt;

    private Map ARAvailableShift;
    private int ARAvailableShiftOption;

    private Map ARSkills;
    private int ARSkillOption;

    private Map AREmployeeToShift;
    private int AREmployeeToShiftOption;

    private Map ARConstraint;
    private int ARConstraintOption;

    private Map ARShift;
    private int ARShiftOption;

    private EmployeeFacade facade;

    //================================================Constructor===========================================================
    public EmployeeCLI(EmployeeFacade facade) {

        this.facade = facade;
    }
    
    public void empOptions(int ID){
        System.out.println(this.facade.printSchedule().getData());
        Scanner s = new Scanner((System.in));
        while (true) {
            System.out.println("1.Sign To Available Shift\n" +"2.Get Messeges\n"+
                    "3.Exit\n");
            int opt=s.nextInt();
            switch (opt) {
                case 1: {
                    LocalDate date = getDateFromUser("Please Enter Date Of Shift");
                    System.out.println("Please Enter Type Of Shift To Add");
                    String type = s.nextLine();
                    TypeOfShift typeOfShift;
                    typeOfShift = parseTypeOfShift(type);
                    while (typeOfShift == null) //Ask for valid input until received
                    {
                        System.out.println("Invalid Input, please enter again");
                        type = s.nextLine();
                        typeOfShift = parseTypeOfShift(type);
                    }
                    System.out.println(facade.addAvailableShift(ID + "", date, typeOfShift).getData());
                    break;
                }
                case 2: {
                    List<Role> temp=facade.getListofRoleByID(ID).getData();
                    String res="";
                    for(Role r:temp){
                        if(r==Role.LogisticManager)
                            res=res+facade.getMesseges(r.toString()).getData();
                    }
                    System.out.println(res);
                    break;
                }
                case 3:
                    return;
            }
        }

    }
    public boolean getEmpId(String choose){
//        System.out.println("Please enter your ID");
//        Scanner s = new Scanner((System.in));
//        int choose = s.nextInt();
        inValidInputDigits(choose+"");
        List<Role> roles =facade.getListofRoleByID(Integer.parseInt(choose)).getData();
        if(roles.contains(Role.Manager)){
            managerOptions();
            return false;
        }
        else if(roles.contains(Role.HRManager))
            return true;
        else{
            empOptions(Integer.parseInt(choose));
            return false;
        }

    }
    public void managerOptions(){
        while(true){
            this.menuManagerMainOpt = printMenu(menuManagerMain);
            switch (this.menuManagerMainOpt){
                case 1:{
                    System.out.println(this.facade.printSchedule().getData());
                    break;
                }
                case 2:{
                    String idToPrint = checkIdExist();
                    System.out.println(this.facade.printPersonalDetails(idToPrint).getData());
                    break;
                }
                case 3:{
                    Repository rep= Repository.getInstance();
                    rep.closeConnection();
                    return;
                }
            }
        }
    }

    /**
     * dev.Main Function for running all menus and functions in the program
     */
    public void start(String ID) {
        Scanner s = new Scanner(System.in);
        createMenu();
        boolean condition = getEmpId(ID);
        while (condition) {
            this.menuMainOption = printMenu(menuMain);
            switch (this.menuMainOption) {
                case (1): //Add/Remove employee
                {
                    this.AREmployeeOption = printMenu(AREmployee);
                    switch (this.AREmployeeOption) {
                        case (1): //Add Employee
                        {
                            String[] employeeFields = new String[NUMBER_OF_EMPLOYEE_FIELDS]; //0-FirstName 1-LastName 2-ID 3-BankAccountNumber 4-Salary 5-empConditions 6-skill

                            //First Name
                            System.out.println("Please Enter First Name");
                            employeeFields[0] = s.nextLine();
                            employeeFields[0] = inValidInputLetters(employeeFields[0]);

                            //Last Name
                            System.out.println("Please Enter Last Name");
                            employeeFields[1] = s.nextLine();
                            employeeFields[1] = inValidInputLetters(employeeFields[1]);

                            //ID
                            System.out.println("Please Enter ID");
                            employeeFields[2] = s.nextLine();
                            employeeFields[2] = inValidInputDigits(employeeFields[2]);

                            //Bank Account Number
                            System.out.println("Please Enter bank Account Number");
                            employeeFields[3] = s.nextLine();
                            while (!checkBankAccountNumber(employeeFields[3])) //Ask for valid input until received
                            {
                                System.out.println("Invalid Input, please enter again");
                                employeeFields[3] = s.nextLine();
                            }

                            //Salary
                            System.out.println("Please Enter salary");
                            employeeFields[4] = s.nextLine();
                            employeeFields[4] = inValidInputDigits(employeeFields[4]);

                            //Employee Conditions
                            System.out.println("Please Enter Employee conditions");
                            employeeFields[5] = s.nextLine();//free text , no need to check

                            //Skill
                            System.out.println("Please Enter 1 skill of the employee from the following options:");
                            System.out.println("Manager,Storekeeper,Cashier, Driver, Transporter,HRManager,Usher,PurchasingManager,LogisticManager");
                            employeeFields[6] = s.nextLine();

                            //---------All input from user is ready-------------
                            List<Role> l = new LinkedList<Role>();
                            Role typeOfEmployee;
                            typeOfEmployee = parseTypeOfEmp(employeeFields[6]);
                            while (typeOfEmployee == null) //Ask for valid input until received
                            {
                                System.out.println("Invalid Input, please enter again");
                                employeeFields[6] = s.nextLine();
                                typeOfEmployee = parseTypeOfEmp(employeeFields[6]);
                            }
                            l.add(typeOfEmployee); //Adding the skill to the new list created for the new employee
                            System.out.println(facade.addEmployee(employeeFields[0], employeeFields[1], employeeFields[2], employeeFields[3], Integer.parseInt(employeeFields[4]), employeeFields[5], LocalDate.now(), l).getData());
                            break;
                        }
                        case (2): //Remove Employee
                        {
                            System.out.println("Enter ID Of The Employee To Remove");
                            String id = s.nextLine();
                            id = inValidInputDigits(id);
                            System.out.println(facade.RemoveEmployee(id).getData());
                            break;
                        }
                        case (3): //back To main menu
                        {
                            continue;
                        }
                    }
                    break;
                }
                case (2): //Edit employee details
                {
                    String idToEdit = checkIdExist();
                    this.menuEditOption = printMenu(menuEdit);
                    switch (menuEditOption) {

                        case (1): //Edit First Name
                        {
                            System.out.println("Enter new First Name");
                            String firstName = s.nextLine();
                            firstName = inValidInputLetters(firstName);
                            System.out.println(facade.editFirstName(idToEdit, firstName).getData());
                            break;
                        }
                        case (2): //Edit Last Name
                        {
                            System.out.println("Enter new Last Name");
                            String lastName = s.nextLine();
                            lastName = inValidInputLetters(lastName);
                            System.out.println(facade.editLastName(idToEdit, lastName).getData());
                            break;
                        }
                        case (3): //Edit Bank Account Number
                        {
                            System.out.println("Enter new Bank Account Number");
                            String newBankAccountNumber = s.nextLine();
                            while (!checkBankAccountNumber(newBankAccountNumber)) //Ask for valid input until received
                            {
                                System.out.println("Invalid Input, please enter again");
                                newBankAccountNumber = s.nextLine();
                            }
                            System.out.println(facade.editBankAccountNumber(idToEdit, newBankAccountNumber).getData());
                            break;
                        }
                        case (4): //Edit Salary
                        {
                            System.out.println("Enter new Salary");
                            String newSalary = s.nextLine();
                            newSalary = inValidInputDigits(newSalary);
                            System.out.println(facade.editSalary(idToEdit, Integer.parseInt(newSalary)).getData());
                            break;
                        }
                        case (5): //Edit Employee Conditions
                        {
                            System.out.println("Enter new Employee Conditions");
                            String newEmpConditions = s.nextLine();
                            System.out.println(facade.editEmpConditions(idToEdit, newEmpConditions).getData());
                            break;
                        }
                        case (6): //Back To dev.Main Menu
                        {
                            continue;
                        }
                    }
                    break;
                }
                case (3): //Add/Remove available shift
                {
                    String idToEdit = checkIdExist();
                    this.ARAvailableShiftOption = printMenu(ARAvailableShift);
                    switch (ARAvailableShiftOption) {
                        case (1): //Add available shift
                        {
                            LocalDate date = getDateFromUser("Please Enter Date Of Shift");
                            System.out.println("Please Enter Type Of Shift To Add");
                            String type = s.nextLine();
                            TypeOfShift typeOfShift;
                            typeOfShift = parseTypeOfShift(type);
                            while (typeOfShift == null) //Ask for valid input until received
                            {
                                System.out.println("Invalid Input, please enter again");
                                type = s.nextLine();
                                typeOfShift = parseTypeOfShift(type);
                            }
                            System.out.println(facade.addAvailableShift(idToEdit, date, typeOfShift).getData());
                            break;
                        }
                        case (2): //Remove available shift
                        {
                            System.out.println("Please Enter Date Of Shift To Remove ");
                            boolean validDate = false;
                            LocalDate date = null;
                            while (!validDate) {
                                validDate = true;
                                try {
                                    date = getDateFromUser("Please enter date");
                                } catch (Exception e) {
                                    System.out.println("Invalid Input, please enter again");
                                    validDate = false;
                                }
                            }
                            System.out.println("Please Enter Type Of Shift To Remove");
                            String type = s.nextLine();
                            TypeOfShift typeOfShift;
                            typeOfShift = parseTypeOfShift(type);
                            while (typeOfShift == null) //Ask for valid input until received
                            {
                                System.out.println("Invalid Input, please enter again");
                                type = s.nextLine();
                                typeOfShift = parseTypeOfShift(type);
                            }
                            System.out.println(facade.removeAvailableShift(idToEdit, date, typeOfShift).getData());
                            break;
                        }
                        case (3): //Back to main menu
                        {
                            continue;
                        }
                    }
                    break;
                }
                case (4): //Add/Remove skills
                {
                    String idToEdit = checkIdExist();
                    this.ARSkillOption = printMenu(ARSkills);
                    switch (ARSkillOption) {
                        case (1): //Add skills
                        {
                            System.out.println("Please Enter new skill");
                            String skill = s.nextLine();
                            Role typeOfEmployee;
                            typeOfEmployee = parseTypeOfEmp(skill);
                            while (typeOfEmployee == null)  //Ask for valid input until received
                            {
                                System.out.println("Invalid Input, please enter again");
                                skill = s.nextLine();
                                typeOfEmployee = parseTypeOfEmp(skill);
                            }
                            System.out.println(facade.addSkill(idToEdit, typeOfEmployee).getData());
                            break;
                        }
                        case (2): //Remove skills
                        {
                            System.out.println("Please Enter skill to remove");
                            String skill = s.nextLine();
                            Role typeOfEmployee;
                            typeOfEmployee = parseTypeOfEmp(skill);
                            while (typeOfEmployee == null)  //Ask for valid input until received
                            {
                                System.out.println("Invalid Input, please enter again");
                                skill = s.nextLine();
                                typeOfEmployee = parseTypeOfEmp(skill);
                            }
                            System.out.println(facade.removeSkill(idToEdit, typeOfEmployee).getData());
                            break;
                        }
                        case (3): //Back to main menu
                        {
                            continue;
                        }
                    }
                    break;
                }
                case (5): //Add/Remove employee to shift
                {
                    String idToEdit = checkIdExist();
                    this.AREmployeeToShiftOption = printMenu(AREmployeeToShift);
                    switch (AREmployeeToShiftOption) {
                        case (1): //Add Employee to shift
                        {
                            System.out.println("Enter skill of the employee");
                            String skill = s.nextLine();
                            Role typeOfEmployee;
                            typeOfEmployee = parseTypeOfEmp(skill);
                            while (typeOfEmployee == null)  //Ask for valid input until received
                            {
                                System.out.println("Invalid Input, please enter again");
                                skill = s.nextLine();
                                typeOfEmployee = parseTypeOfEmp(skill);
                            }
                            LocalDate date = getDateFromUser("Please enter date");
                            System.out.println("Enter Type of shift");
                            String type = s.nextLine();
                            TypeOfShift typeOfShift;
                            typeOfShift = parseTypeOfShift(type);
                            while (typeOfShift == null)  //Ask for valid input until received
                            {
                                System.out.println("Invalid Input, please enter again");
                                type = s.nextLine();
                                typeOfShift = parseTypeOfShift(type);
                            }
                            System.out.println(facade.addEmployeeToShift(idToEdit, typeOfEmployee, date, typeOfShift).getData());
                            break;
                        }
                        case (2): //Remove Employee to shift
                        {
                            LocalDate date = getDateFromUser("Please enter date");
                            System.out.println("Enter Type of shift");
                            String type = s.nextLine();
                            TypeOfShift typeOfShift;
                            typeOfShift = parseTypeOfShift(type);
                            while (typeOfShift == null)  //Ask for valid input until received
                            {
                                System.out.println("Invalid Input, please enter again");
                                type = s.nextLine();
                                typeOfShift = parseTypeOfShift(type);
                            }
                            System.out.println(facade.removeEmployeeFromShift(idToEdit, date, typeOfShift).getData());
                            break;
                        }
                        case (3): //Back to main menu
                        {
                            continue;
                        }
                    }
                    break;
                }
                case (6): //Add/Remove constraints
                {
                    this.ARConstraintOption = printMenu(ARConstraint);
                    switch (ARConstraintOption) {
                        case (1): //Add Constraint
                        {
                            //Get Shift identifiers from user (date and type)
                            LocalDate date = getDateFromUser("Please enter date");
                            System.out.println("Enter Type of shift");
                            String type = s.nextLine();
                            TypeOfShift typeOfShift;
                            typeOfShift = parseTypeOfShift(type);
                            while (typeOfShift == null)  //Ask for valid input until received
                            {
                                System.out.println("Invalid Input, please enter again");
                                type = s.nextLine();
                                typeOfShift = parseTypeOfShift(type);
                            }
                            //Get constraint from user (TypeOfEmployee and Integer)
                            System.out.println("Enter Type of the employee to restrict in the shift");
                            String consType = s.nextLine();
                            Role typeOfEmployee = null;
                            typeOfEmployee = parseTypeOfEmp(consType);
                            while (typeOfEmployee == null)  //Ask for valid input until received
                            {
                                System.out.println("Invalid Input, please enter again");
                                consType = s.nextLine();
                                typeOfEmployee = parseTypeOfEmp(consType);
                            }
                            int numOfEmp = -1;
                            System.out.println("Enter amount of employees for this type in the shift");

                            while (numOfEmp == -1) {
                                try {
                                    numOfEmp = Integer.parseInt(inValidInputDigits(s.nextLine()));
                                } catch (Exception e) {
                                    System.out.println("Invalid Input, please enter again");
                                }
                            }
                            System.out.println(facade.addConstraintToShift(date, typeOfShift, typeOfEmployee, numOfEmp).getData());
                            break;
                        }
                        case (2): //Remove Constraint
                        {
                            //Get Shift identifiers from user (date and type)
                            LocalDate date = getDateFromUser("Please enter date");
                            System.out.println("Enter Type of shift");
                            String type = s.nextLine();
                            TypeOfShift typeOfShift;
                            typeOfShift = parseTypeOfShift(type);
                            while (typeOfShift == null)  //Ask for valid input until received
                            {
                                System.out.println("Invalid Input, please enter again");
                                type = s.nextLine();
                                typeOfShift = parseTypeOfShift(type);
                            }
                            //Get constraint from user (TypeOfEmployee only)
                            System.out.println("Enter Type of the employee of the constraint you want to remove");
                            String consType = s.nextLine();
                            Role typeOfEmployee;
                            typeOfEmployee = parseTypeOfEmp(consType);
                            while (typeOfEmployee == null)  //Ask for valid input until received
                            {
                                System.out.println("Invalid Input, please enter again");
                                consType = s.nextLine();
                                typeOfEmployee = parseTypeOfEmp(consType);
                            }
                            System.out.println(facade.removeConstraintToShift(date, typeOfShift, typeOfEmployee).getData());
                            break;
                        }
                        case (3): //Back to main menu
                        {
                            continue;
                        }
                    }
                    break;
                }
                case (7): //Add/Remove shift
                {
                    this.ARShiftOption = printMenu(ARShift);
                    switch (ARShiftOption) {
                        case (1): //Add shift
                        {
                            //Get Shift identifiers from user (date and type)
                            LocalDate date = getDateFromUser("Please enter date");
                            System.out.println("Enter Type of shift to add");
                            String type = s.nextLine();
                            TypeOfShift typeOfShift;
                            typeOfShift = parseTypeOfShift(type);
                            while (typeOfShift == null)  //Ask for valid input until received
                            {
                                System.out.println("Invalid Input, please enter again");
                                type = s.nextLine();
                                typeOfShift = parseTypeOfShift(type);
                            }
                            System.out.println(facade.addShift(date, typeOfShift).getData());
                            break;
                        }
                        case (2): //Remove Shift
                        {
                            //Get Shift identifiers from user (date and type)
                            LocalDate date = getDateFromUser("Please enter date");
                            System.out.println("Enter Type of shift to remove");
                            String type = s.nextLine();
                            TypeOfShift typeOfShift;
                            typeOfShift = parseTypeOfShift(type);
                            while (typeOfShift == null) //Ask for valid input until received
                            {
                                System.out.println("Invalid Input, please enter again");
                                type = s.nextLine();
                                typeOfShift = parseTypeOfShift(type);
                            }
                            System.out.println(facade.removeShift(date, typeOfShift).getData());
                            break;
                        }
                        case (3): //Back to main menu
                        {
                            continue;
                        }
                    }
                    break;
                }
                case (8): //Print schedule
                {
                    System.out.println(this.facade.printSchedule().getData());
                    break;
                }
                case (9): //Print Employee's Personal Details
                {
                    String idToPrint = checkIdExist();
                    System.out.println(this.facade.printPersonalDetails(idToPrint).getData());
                    break;
                }
                case (10): //switch account
                {
                    getEmpId(ID);
                    break;
                }
                case(11):{
                    System.out.println("Please Enter ID");
                    String id = s.nextLine();
                    id = inValidInputDigits(id);
                    System.out.println("Please Enter License Type");
                    String license = s.nextLine();
                    System.out.println(facade.updateDriverLicense(id,license).getData());
                    break;
                }
//                case (12): //set manager to a shift
//                {
//                    String idToEdit = checkIdExist();
//                    LocalDate date = getDateFromUser("Please enter date");
//                    System.out.println("Enter Type of shift");
//                    String type = s.nextLine();
//                    TypeOfShift typeOfShift;
//                    typeOfShift = parseTypeOfShift(type);
//                    while (typeOfShift == null)  //Ask for valid input until received
//                    {
//                        System.out.println("Invalid Input, please enter again");
//                        type = s.nextLine();
//                        typeOfShift = parseTypeOfShift(type);
//                    }
//                    System.out.println(facade.addManagerEmployeeToShift(idToEdit, date, typeOfShift).getData());
//                    break;
//                }
                case (12): //Add/Remove driver employee
                {
                    this.AREmployeeOption = printMenu(AREmployee);
                    switch (this.AREmployeeOption) {
                        case (1): //Add Driver Employee
                        {
                            String[] driverFields = new String[NUMBER_OF_DRIVER_FIELDS]; //0-FirstName 1-LastName 2-ID 3-BankAccountNumber 4-Salary 5-empConditions 6-skill

                            //First Name
                            System.out.println("Please Enter First Name");
                            driverFields[0] = s.nextLine();
                            driverFields[0] = inValidInputLetters(driverFields[0]);

                            //Last Name
                            System.out.println("Please Enter Last Name");
                            driverFields[1] = s.nextLine();
                            driverFields[1] = inValidInputLetters(driverFields[1]);

                            //ID
                            System.out.println("Please Enter ID");
                            driverFields[2] = s.nextLine();
                            driverFields[2] = inValidInputDigits(driverFields[2]);

                            //Bank Account Number
                            System.out.println("Please Enter bank Account Number");
                            driverFields[3] = s.nextLine();
                            while (!checkBankAccountNumber(driverFields[3])) //Ask for valid input until received
                            {
                                System.out.println("Invalid Input, please enter again");
                                driverFields[3] = s.nextLine();
                            }

                            //Salary
                            System.out.println("Please Enter salary");
                            driverFields[4] = s.nextLine();
                            driverFields[4] = inValidInputDigits(driverFields[4]);

                            //Employee Conditions
                            System.out.println("Please Enter Employee conditions");
                            driverFields[5] = s.nextLine();//free text , no need to check

                            //License
                            System.out.println("Please Enter License Type");
                            driverFields[7] = s.nextLine();



                            //---------All input from user is ready-------------
                            System.out.println(facade.addDriverEmployee(driverFields[0], driverFields[1], driverFields[2], driverFields[3], Integer.parseInt(driverFields[4]), driverFields[5], LocalDate.now(), driverFields[7]).getData());
                            break;
                        }
                        case (2): //Remove Driver Employee
                        {
                            System.out.println("Enter ID Of The Employee To Remove");
                            String id = s.nextLine();
                            id = inValidInputDigits(id);
                            System.out.println(facade.RemoveEmployee(id).getData());
                            break;
                        }
                        case (3): //back To main menu
                        {
                            continue;
                        }
                    }
                    break;
                }
                case (13): //Exit The Program
                {

                    List<Role> temp=facade.getListofRoleByID(Integer.parseInt(ID)).getData();
                    String res="";
                    for(Role r:temp){
                        if(r==Role.HRManager)
                            res=res+facade.getMesseges(r.toString()).getData();
                    }
                    System.out.println(res);
                    break;
                }
                case (14): //Exit The Program
                {
                    Repository rep= Repository.getInstance();
                    rep.closeConnection();
                    return;

                }

            }
        }
    }

    /**
     * Asks for ID from the user until valid ID, that exists in the list of employees is inserted
     *
     * @return Valid ID that exists in the list of employees
     */
    private String checkIdExist() {
        Scanner s = new Scanner(System.in);
        String idExist ;
        System.out.println("Enter ID Of The Employee");
        String idToEdit = s.nextLine();
        idToEdit = inValidInputDigits(idToEdit);
        idExist = (facade.checkIfEmpExist(idToEdit)).getData();
        while (!idExist.equals("true")) {
            System.out.println("Invalid Input, please enter again");
            idToEdit = s.nextLine();
            idToEdit = inValidInputDigits(idToEdit);
            idExist = facade.checkIfEmpExist(idToEdit).getData();
        }
        return idToEdit;
    }

    /**
     * Converts a string to a TypeOfShift object with the value of the string
     *
     * @param type
     * @return if type is valid TypeOfShift, returns converted TypeOfShift object, else return null
     */
    private TypeOfShift parseTypeOfShift(String type) {
        TypeOfShift typeOfShift;
        try {
            typeOfShift = TypeOfShift.valueOf(type);
        } catch (Exception e) {
            typeOfShift = null;
        }
        return typeOfShift;
    }

    /**
     * Converts a string to a TypeOfEmployee object with the value of the string
     *
     * @param type
     * @return if type is valid TypeOfEmployee, returns converted TypeOfEmployee object, else return null
     */
    private Role parseTypeOfEmp(String type) {
        Role typeOfEmployee;
        try {
            typeOfEmployee = Role.valueOf(type);
        } catch (Exception e) {
            typeOfEmployee = null; //return null if conversion failed
        }
        return typeOfEmployee;
    }

    /**
     * Checks validity of input (Letters only) and keeps asking for a new input until input is valid
     *
     * @param s
     * @return Valid input from user
     */
    private String inValidInputLetters(String s) {
        Scanner scan = new Scanner(System.in);
        while (!checkAllLetters(s)) {
            System.out.println("Invalid Input, please enter again");
            s = scan.nextLine();
        }
        return s;
    }

    /**
     * Checks validity of input (Digits only) and keeps asking for a new input until input is valid
     *
     * @param s
     * @return Valid input from user
     */
    private String inValidInputDigits(String s) {
        Scanner scan = new Scanner(System.in);
        while (!checkAllDigits(s)) {
            System.out.println("Invalid Input, please enter again");
            s = scan.nextLine();
        }
        return s;
    }

    /**
     * Checks validity of string for a bank account number
     *
     * @param bankAccountNumber
     * @return
     */
    private boolean checkBankAccountNumber(String bankAccountNumber) {
        if (bankAccountNumber.length() <= 0)
            return false;
        for (int i = 0; i < bankAccountNumber.length(); i++) {
            char ch = bankAccountNumber.charAt(i);
            if ((ch < '0' || ch > '9') && ch != '/' && ch != '-') {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if every character in the given string is representing an English letter
     *
     * @param s
     * @return
     */
    private boolean checkAllLetters(String s) {
        if (s.length() <= 0)
            return false;
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (!(ch >= 'a' && ch <= 'z') && !(ch >= 'A' && ch <= 'Z')) //is an uppercase letter or lower case letter
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if every character in the given string is representing a number
     *
     * @param num
     * @return
     */
    private boolean checkAllDigits(String num) {
        if (num.length() <= 0)
            return false;
        for (int i = 0; i < num.length(); i++) //iterating string
        {
            char ch = num.charAt(i);
            if (ch < '0' || ch > '9') //is a char representing a number
            {
                return false;
            }
        }
        return true;
    }

    //Menus Initialization and creations-----------------------


    protected void createMenu() {
        createMenuMain();
        createARAvailableShift();
        createARSkills();
        createEditMenu();
        createEmployeeToShift();
        createAREmployee();
        createARConstraint();
        createARShift();
        createManagerMenuMain();
    }

    //Creations
    private void createMenuMain() {
        menuMain = new HashMap<>();
        menuMain.put(1, "Add/Remove Employee");
        menuMain.put(2, "Edit employee details");
        menuMain.put(3, "Add/Remove available shift");
        menuMain.put(4, "Add/Remove skills");
        menuMain.put(5, "Add/Remove Employee to shift");
        menuMain.put(6, "Add/Remove Constraint To Existing shift");
        menuMain.put(7, "Add/Remove Shift ");
        menuMain.put(8, "Print Schedule");
        menuMain.put(9, "Print Employee Personal Details");
        menuMain.put(10, "Choose Diffrent account");
        menuMain.put(11, "Update Driver License Type ");
        menuMain.put(12, "Add/Remove Driver ");
        menuMain.put(13, "Get Notifications");
        menuMain.put(14, "Exit");
    }
    private void createManagerMenuMain() {
        menuManagerMain = new HashMap<>();
        menuManagerMain.put(1, "Print Schedule");
        menuManagerMain.put(2, "Print Employee Personal Details");
        menuManagerMain.put(3, "Exit");
    }

    private void createAREmployee() {
        AREmployee = new HashMap<>();
        AREmployee.put(1, "Add Employee");
        AREmployee.put(2, "Remove Employee");
        AREmployee.put(3, "Back to main menu");
    }

    private void createEditMenu() {
        menuEdit = new HashMap<>();
        menuEdit.put(1, "Edit First Name");
        menuEdit.put(2, "Edit Last Name");
        menuEdit.put(3, "Edit Bank Account Number");
        menuEdit.put(4, "Edit Salary");
        menuEdit.put(5, "Edit Employee Conditions");
        menuEdit.put(6, "Back To dev.Main Menu");
    }

    private void createARAvailableShift() {
        ARAvailableShift = new HashMap<>();
        ARAvailableShift.put(1, "Add Available Shift");
        ARAvailableShift.put(2, "Remove Available Shift");
        ARAvailableShift.put(3, "Back To dev.Main Menu");
    }

    private void createARSkills() {
        ARSkills = new HashMap<>();
        ARSkills.put(1, "Add Skill");
        ARSkills.put(2, "Remove Skill");
        ARSkills.put(3, "Back To dev.Main Menu");
    }

    private void createARConstraint() {
        ARConstraint = new HashMap<>();
        ARConstraint.put(1, "Add Constraint");
        ARConstraint.put(2, "Remove Constraint");
        ARConstraint.put(3, "Back To dev.Main Menu");
    }

    private void createARShift() {
        ARShift = new HashMap<>();
        ARShift.put(1, "Add Shift");
        ARShift.put(2, "Remove Shift");
        ARShift.put(3, "Back To dev.Main Menu");
    }

    private void createEmployeeToShift() {
        AREmployeeToShift = new HashMap<>();
        AREmployeeToShift.put(1, "Add Employee To Shift");
        AREmployeeToShift.put(2, "Remove Employee From Shift");
        AREmployeeToShift.put(3, "Back To dev.Main Menu");
    }

    //Print

    /**
     * @param menu
     * @return returns the option that was chosen is the menu
     */
    private int printMenu(Map<Integer, String> menu) {
        System.out.println("\n------------------------------------\n");
        boolean flag = false;
        int choose = -1;
        Scanner s = new Scanner((System.in));
        for (int i = 1; i <= menu.keySet().size(); i++) {
            System.out.println(i + ") " + menu.get(i));
        }
        while (!flag) {
            try {
                flag = false;
                choose = s.nextInt();
                flag = isWithinBounds(choose, menu.size());
            } catch (Exception e) {
                System.out.println("Invalid Input, please enter again");
                flag = true;
            }
        }
        return choose;
    }

    private boolean isWithinBounds(int choose, int bound) throws Exception {
        if (choose < 1 || choose > bound) {
            System.out.println("Option chosen is out of bounds, please enter again");
            return false;
        }
        return true;
    }
    private int getNonNegativeNumber(String prompt){
        int output=-1;
        System.out.println(prompt);
        for(output= checkPositiveNumber(scanner.nextLine()); output==-1; output= checkPositiveNumber(scanner.nextLine()))
            System.out.println("Wrong input, please enter a non negative number");
        return output;
    }

    private LocalDate getDateFromUser(String prompt) {
        System.out.println(prompt);
        int day = getNonNegativeNumber("Please enter day:");
        while (day > 30 || day < 1)
            day = getNonNegativeNumber("Please enter day:");
        int month = getNonNegativeNumber("Please enter month:");
        while (month > 12 || month < 1)
            month = getNonNegativeNumber("Please enter month:");
        int year = getNonNegativeNumber("Please enter year:");
        while (year < 2021 || year > 2050)
            year = getNonNegativeNumber("Please enter year:");
        return LocalDate.of(year, month, day);
    }
    private int checkPositiveNumber(String number) {
        try {
            int num = Integer.parseInt(number);
            return num >= 0 ? num : -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}