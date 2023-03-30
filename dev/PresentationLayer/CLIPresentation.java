package PresentationLayer;

import BusinessLayer.EmployeeBusinessLayer.Role;
import DataAccessLayer.Repository;
import ServiceLayer.EmployeeServiceLayer.EmployeeFacade;
import ServiceLayer.ResponseT;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class CLIPresentation {

    public static void main(String[] args) {
        StockCLI stockCLI;
        SupCLI supCLI;
        TransportCLI transportCLI;
        EmployeeCLI empCLI;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("1. Login\n2. Exit");
            while (true) {
                System.out.print("\nInput: ");
                scanner = new Scanner(System.in);
                int input;
                try {
                    input = scanner.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("Invalid Input.");
                    continue;
                }
                if (input < 1 || input > 2) {
                    System.out.println("Invalid Input.");
                    continue;
                } else if (input == 2) {
                    System.out.println("Goodbye.");
                    System.exit(0);
                }
                break;
            }
            int id = -1;
            System.out.print("Enter ID: ");
            try {
                id = scanner.nextInt();
            }
            catch (Exception e) {
                System.out.println("Invalid Input.");
                continue;
            }
            EmployeeFacade facade = new EmployeeFacade(Role.Manager);
            List<Role> employeeRoles = null;
            ResponseT<List<Role>> res = facade.getListofRoleByID(id);
            Repository rep= Repository.getInstance();
            rep.closeConnection();
            if (!res.isError()) {
                employeeRoles = res.getData();
                if (employeeRoles.isEmpty()) {
                    System.out.println("No Employee Exists By That ID");
                    continue;
                }
            }
            else {
                System.out.println("Error Occurred - " + res.getError());
                continue;
            }
            while (true) {
                boolean loggingOut = false;
                System.out.println("~~~~~~~~~~~~~~~~~~~~\n" +
                        "~~~~~~SUPER-LI~~~~~~\n" +
                        "~~~~~~~~~~~~~~~~~~~~");
                System.out.println("1. Stock Management System");
                System.out.println("2. Supplier Management System");
                System.out.println("3. Transport Management System");
                System.out.println("4. Employee Management System");
                System.out.println("5. Notifications");
                System.out.println("\n0. Logout");
                while (true) {
                    System.out.print("\nPlease Choose Which System To Enter: ");
                    scanner = new Scanner(System.in);
                    int input;
                    try {
                        input = scanner.nextInt();
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid Input.");
                        continue;
                    }
                    if (input < 0 || input > 5) {
                        System.out.println("Invalid Input.");
                    } else if (input == 1) {
                        stockCLI = new StockCLI();
                        stockCLI.start(employeeRoles);
                        break;
                    } else if (input == 2) {
                        supCLI = new SupCLI();
                        supCLI.start(employeeRoles);
                        break;
                    } else if (input == 3) {
                        transportCLI = new TransportCLI(employeeRoles);
                        transportCLI.start();
                        break;
                    } else if (input == 4) {
                        empCLI = new EmployeeCLI(facade);
                        empCLI.start(""+id);
                        break;
                    }else if (input == 5) {
                        for(Role r: employeeRoles){
                            System.out.println(facade.getMesseges(r.toString()).getData());
                        }
                        break;
                    }
                    else {
                        System.out.println("You Are Now Logged Out");
                        loggingOut = true;
                        break;
                    }
                }
                if (loggingOut) break;
            }
        }
    }
}
