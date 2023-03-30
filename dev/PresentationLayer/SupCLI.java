package PresentationLayer;

import BusinessLayer.EmployeeBusinessLayer.Role;
import BusinessLayer.SupplierBusinessLayer.Supplier;
import BusinessLayer.TransportBusinessLayer.ShippingArea;
import ServiceLayer.Response;
import ServiceLayer.ResponseT;
import ServiceLayer.SupplierServiceLayer.Obj.*;
import ServiceLayer.SupplierServiceLayer.SupService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class SupCLI {

    private Scanner scanner;
    SupService service;

    public SupCLI() {
        service = SupService.getInstance();
        scanner = new Scanner(System.in);
    }

    public void start(List<Role> roles) {
//        stockEmployeeMainMenu();
        if (roles.contains(Role.Manager)) managerMainMenu();
        else if (roles.contains(Role.Storekeeper)) stockEmployeeMainMenu();
        else {
            System.out.println("No Actions Allowed.");
        }
    }

    private void managerMainMenu() {
        boolean run = true;
        while (run) {
            String menu = "1. Suppliers Management\n" +
                    "2. Generated Reports\n" +
                    "\n" +
                    "0. Back to main menu";
            System.out.println();
            System.out.println(menu);
            System.out.print("Input: ");
            System.out.println();
            int chosenOp = scanner.nextInt();
            try {
                switch (chosenOp) {
                    case (0):
                        System.out.println();
                        run = false;
                        break;
                    case (1):
                        System.out.println();
                        suppliersManagementMenu();
                        break;
                    case (2):
                        System.out.println();
                        reportsMenu();
                        break;
                }
            } catch (Exception e) {
                System.out.println("error " + e.toString());
                System.out.println(":(");
            }
        }
    }

    private void stockEmployeeMainMenu() {
        boolean run = true;
        while (run) {
            String menu = "Welcome to Super-Li's supplier management system!\n"
                    + "~~~~~~~\n"
                    + "1. Suppliers Management\n"
                    + "2. Products Management\n"
                    + "3. Orders Management\n"
                    + "4. Generated Reports\n"
                    + "\n"
                    + "0. Back to main menu";
            System.out.println();
            System.out.println(menu);
            System.out.print("Input: ");
            System.out.println();
            int chosenOp = scanner.nextInt();
            try {
                switch (chosenOp) {
                    case (0):
                        System.out.println();
                        run = false;
                        break;
                    case (1):
                        System.out.println();
                        suppliersManagementMenu();
                        break;
                    case (2):
                        System.out.println();
                        productsManagementMenu();
                        break;
                    case (3):
                        System.out.println();
                        ordersManagementMenu();
                        break;
                    case (4):
                        System.out.println();
                        reportsMenu();
                        break;
                }
            } catch (Exception e) {
                System.out.println("error " + e.toString());
                System.out.println(":(");
            }
        }
    }

    //2.1 DONE
    private void suppliersManagementMenu() {
        boolean runMenu = true;
        while (runMenu) {
            String menu = "Suppliers Management Menu\n"
                    + "~~~~~~~\n"
                    + "1. Add supplier\n"
                    + "2. Remove supplier\n"
                    + "3. Update supplier details\n"
                    + "4. Sign a contract with supplier\n"
                    + "5. Remove contract\n"
                    + "6. Add a contact to supplier\n"
                    + "7. Remove contact\n"
                    + "8. Update contact details\n"
                    + "9. Print suppliers details\n"
                    + "\n"
                    + "0. Back";
            System.out.println();
            System.out.println(menu);
            System.out.print("Input: ");
            System.out.println("");
            int chosenOp = scanner.nextInt();
            try {
                switch (chosenOp) {
                    case (0):
                        System.out.println();
                        runMenu = false;
                        break;
                    case (1):
                        System.out.println();
                        addSupplierMenu();
                        break;
                    case (2):
                        System.out.println();
                        removeSupplierMenu();
                        break;
                    case (3):
                        System.out.println();
                        updateSupplierMenu();
                        break;
                    case (4):
                        System.out.println();
                        signContractMenu();
                        break;
                    case (5):
                        System.out.println();
                        removeContractMenu();
                        break;
                    case (6):
                        System.out.println();
                        addContactMenu();
                        break;
                    case (7):
                        System.out.println();
                        removeContactMenu();
                        break;
                    case (8):
                        System.out.println();
                        updateContactMenu();
                        break;
                    case (9):
                        System.out.println();
                        printSuppliersDetails();
                        break;
                }
            } catch (Exception e) {
                System.out.println("error " + e.toString());
                System.out.println(":(");
            }
        }
    }

    //2.1.1 DONE
    private void addSupplierMenu() {
        Response response;
        String name;
        int companyNumber, bankAccount, payWayInt;
        Supplier.PayWay payWay;
        System.out.println("enter supplier name");
        name = scanner.next();
        System.out.println("enter company number");
        companyNumber = scanner.nextInt();
        System.out.println("enter bank account");
        bankAccount = scanner.nextInt();
        System.out.println("choose supplier's pay way:\n" +
                "0. cash\n" +
                "1. credit");
        payWayInt = scanner.nextInt();
        payWay = payWayInt == 0 ? Supplier.PayWay.CASH : Supplier.PayWay.CREDIT;
        System.out.println("enter supplier address");
        scanner = new Scanner(System.in);
        String address = scanner.nextLine();
        scanner = new Scanner(System.in);
        System.out.println("choose area:\n" +
                "0. North\n" +
                "1. Center\n" +
                "2. South");
        int areaInt = scanner.nextInt();
        ShippingArea area = areaInt == 0 ? ShippingArea.North : areaInt == 1 ? ShippingArea.Center : ShippingArea.South;
        response = service.addSupplier(name, companyNumber, bankAccount, payWay, address, area);
        if (response.isError()) {
            System.out.println(response.getError());
        }
        else System.out.println("the supplier " + name + " added to the system :)");
    }

    //2.1.2 DONE
    private void removeSupplierMenu() {
        ResponseT<List<ServiceSupplier>> suppliersResponse = service.getSuppliers();
        if (suppliersResponse.isError()) {
            System.out.println(suppliersResponse.getError());
        }
        else {
            List<ServiceSupplier> suppliers = suppliersResponse.getData();
            if (suppliers.isEmpty()) {
                System.out.println("***There are no suppliers in the system***");
            }
            else {
                System.out.println("Current suppliers in the system:");
                System.out.println();
                for (ServiceSupplier supplier : suppliers) {
                    System.out.printf("supplier ID: %d | name: %s | company number: %d\n", supplier.id, supplier.name, supplier.companyNumber);
                }
                System.out.println();
                System.out.println("Choose the ID of the supplier you want to remove:");
                int supplierId = scanner.nextInt();
                Response removeSupplierResponse = service.removeSupplier(supplierId);
                if (removeSupplierResponse.isError()) {
                    System.out.println(removeSupplierResponse.getError());
                } else System.out.printf("supplier %d was successfully removed form the system\n", supplierId);
            }
        }
    }

    //2.1.3 DONE
    private void updateSupplierMenu() {
        ResponseT<List<ServiceSupplier>> suppliersResponse = service.getSuppliers();
        if (suppliersResponse.isError()) {
            System.out.println(suppliersResponse.getError());
        }
        else {
            List<ServiceSupplier> suppliers = suppliersResponse.getData();
            if (suppliers.isEmpty()) {
                System.out.println("***There are no suppliers in the system***");
            }
            else {
                for (ServiceSupplier supplier : suppliers) {
                    ResponseT<Integer> constantDayResponse = service.getSupplierConstantDay(supplier.id);
                    if (constantDayResponse.isError()) {
                        System.out.println(constantDayResponse.getError());
                    }
                    else {
                        System.out.println("Current suppliers in the system:");
                        System.out.println();
                        int constantDay = constantDayResponse.getData();
                        System.out.printf("ID: %d | name: %s | company number: %d | bank account: %d | pay way: %s | independent shipping: %b | constant day: %d\n",
                                supplier.id, supplier.name, supplier.companyNumber, supplier.bankAccount, supplier.payWay, supplier.isShipping, constantDay);
                    }
                }
                System.out.println();
                System.out.println("Choose the ID of the supplier you want to update:");
                int supplierId = scanner.nextInt();
                System.out.println("Choose the field you want to change:");
                String fields = "1. name\n" +
                        "2. company number\n" +
                        "3. bank account\n" +
                        "4. pay way\n" +
                        "5. independent shipping\n" +
                        "6. constant day\n" +
                        "7. address\n" +
                        "8. area";
                System.out.println(fields);
                int fieldInt = scanner.nextInt();
                if (fieldInt < 1 || fieldInt > 8) {
                    System.out.println("you have to choose a field from the list");
                }
                else {
                    System.out.println("Insert your new value for the field you chose:");
                    String content = scanner.next();
                    String field;
                    if (fieldInt == 1) field = "name";
                    else if (fieldInt == 2) field = "companyNumber";
                    else if (fieldInt == 3) field = "bankAccount";
                    else if (fieldInt == 4) field = "payWay";
                    else if (fieldInt == 5) field = "IndependentShipping";
                    else if (fieldInt == 6) field = "constantDay";
                    else if (fieldInt == 7) field = "address";
                    else field = "area";
                    Response updateResponse = service.updateSupplier(supplierId, field, content);
                    if (updateResponse.isError()) {
                        System.out.println(updateResponse.getError());
                    }
                    else System.out.printf("supplier %d was updated successfully", supplierId);
                }
            }
        }
    }

    //2.1.4 DONE
    private void signContractMenu() {
        Response response;
        boolean independentShipping;
        int supplierId;
        ResponseT<List<ServiceSupplier>> suppliersResponse = service.getSuppliers();
        if (suppliersResponse.isError()) {
            System.out.println(suppliersResponse.getError());
        }
        else {
            List<ServiceSupplier> suppliers = suppliersResponse.getData();
            System.out.println("choose the ID of the supplier you want to sign a contract:");
            for (ServiceSupplier supplier : suppliers) {
                System.out.print("supplier's ID: " + supplier.id + ", name: " + supplier.name + ", pay way: " + supplier.payWay);
                if (supplier.contract != null) System.out.println(" (has a contract)");
                else System.out.println();
            }
            System.out.println("enter supplier ID number");
            supplierId = scanner.nextInt();
            System.out.println("does this supplier comes on regular basses?\n"
                    + "0. no\n"
                    + "1. yes");
            int constantDay = scanner.nextInt();
            if (constantDay != 0) {
                System.out.println("on what day the supplier delivers?");
                System.out.println("1. Sunday\n"
                        + "2. Monday\n"
                        + "3. Tuesday\n"
                        + "4. Wednesday\n"
                        + "5. Tuesday\n"
                        + "6. Friday\n"
                        + "7. Saturday");
                constantDay = scanner.nextInt();
            }
            System.out.println("does this supplier can deliver for himself?\n"
                    + "0. no\n"
                    + "1. yes");
            int choice = scanner.nextInt();
            independentShipping = choice != 0;
            response = service.setContract(supplierId, constantDay, independentShipping);
            if (response.isError()) {
                System.out.println(response.getError());
            }
            else {
                ResponseT<ServiceSupplier> supplierResponse = service.getSupplier(supplierId);
                if (supplierResponse.isError()) {
                    System.out.println(supplierResponse.getError());
                }
                else {
                    ServiceSupplier supplier = supplierResponse.getData();
                    System.out.println("you signed a contract with " + supplier.name);
                }
            }
        }
    }

    //2.1.5 DONE
    private void removeContractMenu() {
        ResponseT<List<ServiceSupplier>> suppliersResponse = service.getSuppliers();
        if (suppliersResponse.isError()) {
            System.out.println(suppliersResponse.getError());
        }
        else {
            List<ServiceSupplier> suppliers = suppliersResponse.getData();
            if (suppliers.isEmpty()) {
                System.out.println("***There are no suppliers in the system***");
            } else {
                System.out.println("Choose the ID of the supplier you want to remove his contract");
                System.out.println();
                for (ServiceSupplier supplier : suppliers) {
                    System.out.printf("ID: %d | name: %s\n", supplier.id, supplier.name);
                }
                System.out.println();
                int supplierId = scanner.nextInt();
                Response removeContract = service.removeContract(supplierId);
                if (removeContract.isError()) {
                    System.out.println(removeContract.getError());
                }
                else System.out.printf("The contract of supplier %d was removed successfully", supplierId);
            }
        }
    }

    //2.1.6 DONE
    private void addContactMenu() {
        Response response;
        String name, email, phone;
        int supplierId;
        System.out.println("enter supplier ID number");
        supplierId = scanner.nextInt();
        System.out.println("enter contact name");
        name = scanner.next();
        System.out.println("enter contact email");
        email = scanner.next();
        System.out.println("enter contact phone");
        phone = scanner.next();
        response = service.addContactToSupplier(supplierId, name, email,phone);
        if (response.isError()) {
            System.out.println(response.getError());
        }
        else System.out.println("the contact " + name + " added to the system :)");
    }

    //2.1.7 DONE
    private void removeContactMenu() {
        ResponseT<List<ServiceSupplier>> suppliersResponse = service.getSuppliers();
        if (suppliersResponse.isError()) {
            System.out.println(suppliersResponse.getError());
        }
        else {
            List<ServiceSupplier> suppliers = suppliersResponse.getData();
            if (suppliers.isEmpty()) {
                System.out.println("***There are no suppliers in the system***");
            }
            else {
                System.out.println("Choose the ID of the supplier you want to remove contact from");
                System.out.println();
                for (ServiceSupplier supplier : suppliers) {
                    System.out.printf("ID: %d | name: %s\n", supplier.id, supplier.name);
                }
                System.out.println();
                int supplierId = scanner.nextInt();
                ResponseT<List<ServiceContact>> contactsResponse = service.getSupplierContacts(supplierId);
                if (contactsResponse.isError()) {
                    System.out.println(contactsResponse.getError());
                }
                else {
                    List<ServiceContact> contacts = contactsResponse.getData();
                    if (contacts.isEmpty()) {
                        System.out.println("***The supplier has no contacts***");
                    }
                    else {
                        String[] phones = new String[contacts.size()];
                        int i = 0;
                        for (ServiceContact contact : contacts) {
                            phones[i] = contact.phone;
                            System.out.printf("%d. name: %s | phone: %s | email: %s\n", i, contact.name, contact.phone, contact.email);
                            i++;
                        }
                        System.out.println();
                        System.out.println("Choose the contact you want to remove:");
                        int choice = scanner.nextInt();
                        if (choice < 0 || choice >= contacts.size()) {
                            System.out.println("you have to choose a contact from the list");
                        }
                        else {
                            String phone = phones[choice];
                            Response removeResponse = service.removeContact(supplierId, phone);
                            if (removeResponse.isError()) {
                                System.out.println(removeResponse.getError());
                            }
                            else System.out.println("The contact was removed successfully");
                        }
                    }
                }
            }
        }
    }

    //2.1.8 DONE
    private void updateContactMenu() {
        ResponseT<List<ServiceSupplier>> suppliersResponse = service.getSuppliers();
        if (suppliersResponse.isError()) {
            System.out.println(suppliersResponse.getError());
        }
        else {
            List<ServiceSupplier> suppliers = suppliersResponse.getData();
            if (suppliers.isEmpty()) {
                System.out.println("***There are no suppliers in the system***");
            }
            else {
                System.out.println("Choose the ID of the supplier you want to update a contact from");
                System.out.println();
                for (ServiceSupplier supplier : suppliers) {
                    System.out.printf("ID: %d | name: %s\n", supplier.id, supplier.name);
                }
                System.out.println();
                int supplierId = scanner.nextInt();
                ResponseT<List<ServiceContact>> contactsResponse = service.getSupplierContacts(supplierId);
                if (contactsResponse.isError()) {
                    System.out.println(contactsResponse.getError());
                }
                else {
                    List<ServiceContact> contacts = contactsResponse.getData();
                    if (contacts.isEmpty()) {
                        System.out.println("***The supplier has no contacts***");
                    }
                    else {
                        String[] phones = new String[contacts.size()];
                        int i = 0;
                        for (ServiceContact contact : contacts) {
                            phones[i] = contact.phone;
                            System.out.printf("%d. name: %s | phone: %s | email: %s\n", i, contact.name, contact.phone, contact.email);
                            i++;
                        }
                        System.out.println();
                        System.out.println("Choose the contact you want to update:");
                        int choice = scanner.nextInt();
                        if (choice < 0 || choice >= contacts.size()) {
                            System.out.println("you have to choose a contact from the list");
                        }
                        else {
                            String phone = phones[choice];
                            System.out.println("Choose the field you want to change:");
                            String fields = "1. name\n" +
                                    "2. phone\n" +
                                    "3. email";
                            System.out.println(fields);
                            int fieldInt = scanner.nextInt();
                            if (fieldInt < 1 || fieldInt > 3) {
                                System.out.println("you have to choose a field from the list");
                            }
                            else {
                                System.out.println("Insert your new value for the field you chose:");
                                String content = scanner.next();
                                String field;
                                if (fieldInt == 1) field = "name";
                                else if (fieldInt == 2) field = "phone";
                                else field = "email";
                                Response updateResponse = service.updateContact(supplierId, phone, field, content);
                                if (updateResponse.isError()) {
                                    System.out.println(updateResponse.getError());
                                } else System.out.println("the contact was updated successfully");
                            }
                        }
                    }
                }
            }
        }
    }

    //2.1.9 DONE
    private void printSuppliersDetails() {
        ResponseT<List<ServiceSupplier>> suppliersResponse = service.getSuppliers();
        if (suppliersResponse.isError()) {
            System.out.println(suppliersResponse.getError());
        }
        else {
            List<ServiceSupplier> suppliers = suppliersResponse.getData();
            if (suppliers.isEmpty())
                System.out.println("There are no suppliers in the system.");
            else {
                System.out.println("###### suppliers ######");
                for (ServiceSupplier supplier : suppliers) {
                    System.out.println();
                    System.out.println("~~~~~~~~~~~");
                    System.out.println("ID: " + supplier.id + ", name: " + supplier.name + "");
                    System.out.println("~~ contract ~~");
                    ServiceSupplierContract supplierContract = service.getContract(supplier.id).getData();
                    if (supplierContract == null)
                        System.out.println("NO contract!");
                    else {
                        System.out.println("independent Shipping? " + supplierContract.independentShipping);
                        System.out.println("quantities Document? " + (supplierContract.quantitiesDocument != null));
                        System.out.println("constantDay? " + supplierContract.constantDay);
                    }
                    System.out.println("~~ contacts ~~");
                    ResponseT<List<ServiceContact>> contactsResponse = service.getSupplierContacts(supplier.id);
                    if (contactsResponse.isError()) {
                        System.out.println(contactsResponse.getError() + "gggggggg");
                        break;
                    }
                    List<ServiceContact> contacts = contactsResponse.getData();
                    for (ServiceContact contact : contacts)
                        System.out.println(contact.name + ": phone number: " + contact.phone + ", " +
                                "email: " + contact.email);
                    System.out.println("~~ products ~~");
                    ResponseT<List<ServiceProduct>> productsResponse = service.getProducts(supplier.id);
                    if (productsResponse.isError()) {
                        System.out.println(productsResponse.getError());
                        break;
                    }
                    List<ServiceProduct> products = productsResponse.getData();
                    for (ServiceProduct product : products) {
                        System.out.println(product.catalogNumber + ", " + product.name + ", price: "
                                + product.price + ", from the manufacturer: " + product.manufacturer);
                        ResponseT<Map<Integer, Double>> discountsResponse = service.getDiscounts(supplier.id, product.name, product.manufacturer);
                        if (discountsResponse.isError()) {
                            System.out.println(discountsResponse.getError());
                            break;
                        }
                        Map<Integer, Double> discounts = discountsResponse.getData();
                        if (!discounts.isEmpty()) {
                            System.out.println("** discounts for " + product.name + ":");
                            for (Integer fromAmount : discounts.keySet())
                                System.out.println(discounts.get(fromAmount) +
                                        " precent discount from " + fromAmount + " units.");
                            System.out.println("**");
                        } else {
                            System.out.println("** there are no discounts for " + product.name + ".**");
                        }
                    }
                }
            }
        }
    }

    //2.2 DONE
    private void productsManagementMenu() {
        boolean runMenu = true;
        while (runMenu) {
            String menu = "Products Management Menu\n"
                    + "~~~~~~~\n"
                    + "1. Add product\n"
                    + "2. Remove product\n"
                    + "3. Update product\n"
                    + "4. Add discount\n"
                    + "5. Remove discount\n"
                    + "\n"
                    + "0. Back to suppliers main menu";
            System.out.println();
            System.out.println(menu);
            System.out.print("Input: ");
            System.out.println("");
            int chosenOp = scanner.nextInt();
            try {
                switch (chosenOp) {
                    case (0):
                        System.out.println();
                        runMenu = false;
                        break;
                    case (1):
                        System.out.println();
                        addProductMenu();
                        break;
                    case (2):
                        System.out.println();
                        removeProductMenu();
                        break;
                    case (3):
                        System.out.println();
                        updateProductMenu();
                        break;
                    case (4):
                        System.out.println();
                        addDiscountMenu();
                        break;
                    case (5):
                        System.out.println();
                        removeDiscountMenu();
                        break;
                }
            } catch (Exception e) {
                System.out.println("error " + e.toString());
                System.out.println(":(");
            }
        }
    }

    //2.2.1 DONE
    private void addProductMenu() {
        Response response;
        int supplierId;
        double price;
        String productName, manufacturer, catalogNumber;
        System.out.println("enter supplier ID");
        supplierId = scanner.nextInt();
        System.out.println("enter product name");
        productName = scanner.next();
        System.out.println("enter catalog number");
        catalogNumber = scanner.next();
        System.out.println("enter price");
        price = scanner.nextDouble();
        System.out.println("enter manufacturer");
        manufacturer = scanner.next();
        response = service.addProductToSupplier(supplierId, catalogNumber, productName, price, manufacturer);
        if (response.isError()) {
            System.out.println(response.getError());
        }
        else System.out.println("the product " + productName + " added to the system :)");
    }

    //2.2.2 DONE
    private void removeProductMenu() {
        ResponseT<List<ServiceSupplier>> suppliersResponse = service.getSuppliers();
        if (suppliersResponse.isError()) {
            System.out.println(suppliersResponse.getError());
        }
        else {
            List<ServiceSupplier> suppliers = suppliersResponse.getData();
            if (suppliers.isEmpty()) {
                System.out.println("***There are no suppliers in the system***");
            }
            else {
                System.out.println("Choose the ID of the supplier you want to remove product from");
                System.out.println();
                for (ServiceSupplier supplier : suppliers) {
                    System.out.printf("ID: %d | name: %s\n", supplier.id, supplier.name);
                }
                System.out.println();
                int supplierId = scanner.nextInt();
                ResponseT<List<ServiceProduct>> productsResponse = service.getProducts(supplierId);
                if (productsResponse.isError()) {
                    System.out.println(productsResponse.getError());
                }
                else {
                    List<ServiceProduct> products = productsResponse.getData();
                    if (products.isEmpty()) {
                        System.out.println("***The supplier has no products***");
                    }
                    else {
                        String[] catalogNumbers = new String[products.size()];
                        int i = 0;
                        for (ServiceProduct product : products) {
                            catalogNumbers[i] = product.catalogNumber;
                            System.out.printf("%d. CN: %s | name: %s | price: %f | manufacturer: %s\n", i, product.catalogNumber, product.name, product.price, product.manufacturer);
                            i++;
                        }
                        System.out.println();
                        System.out.println("Choose the product you want to update:");
                        int choice = scanner.nextInt();
                        if (choice < 0 || choice >= products.size()) {
                            System.out.println("you have to choose a product from the list");
                        }
                        else {
                            String CN = catalogNumbers[choice];
                            Response removeResponse = service.removeProduct(supplierId, CN);
                            if (removeResponse.isError()) {
                                System.out.println(removeResponse.getError());
                            } else System.out.println("the product was removed successfully");
                        }
                    }
                }
            }
        }
    }

    //2.2.3 DONE
    private void updateProductMenu() {
        ResponseT<List<ServiceSupplier>> suppliersResponse = service.getSuppliers();
        if (suppliersResponse.isError()) {
            System.out.println(suppliersResponse.getError());
        }
        else {
            List<ServiceSupplier> suppliers = suppliersResponse.getData();
            if (suppliers.isEmpty()) {
                System.out.println("***There are no suppliers in the system***");
            }
            else {
                System.out.println("Choose the ID of the supplier you want to update product from");
                System.out.println();
                for (ServiceSupplier supplier : suppliers) {
                    System.out.printf("ID: %d | name: %s\n", supplier.id, supplier.name);
                }
                System.out.println();
                int supplierId = scanner.nextInt();
                ResponseT<List<ServiceProduct>> productsResponse = service.getProducts(supplierId);
                if (productsResponse.isError()) {
                    System.out.println(productsResponse.getError());
                }
                else {
                    List<ServiceProduct> products = productsResponse.getData();
                    if (products.isEmpty()) {
                        System.out.println("***The supplier has no products***");
                    }
                    else {
                        String[] catalogNumbers = new String[products.size()];
                        int i = 0;
                        for (ServiceProduct product : products) {
                            catalogNumbers[i] = product.catalogNumber;
                            System.out.printf("%d. CN: %s | name: %s | price: %f | manufacturer: %s\n", i, product.catalogNumber, product.name, product.price, product.manufacturer);
                            i++;
                        }
                        System.out.println();
                        System.out.println("Choose the product you want to update:");
                        int choice = scanner.nextInt();
                        if (choice < 0 || choice >= products.size()) {
                            System.out.println("you have to choose a product from the list");
                        }
                        else {
                            String CN = catalogNumbers[choice];
                            System.out.println("Choose the field you want to change:");
                            String fields = "1. catalog number\n" +
                                    "2. name\n" +
                                    "3. price\n" +
                                    "4. manufacturer";
                            System.out.println(fields);
                            int fieldInt = scanner.nextInt();
                            if (fieldInt < 1 || fieldInt > 4) {
                                System.out.println("you have to choose a field from the list");
                            } else {
                                System.out.println("Insert your new value for the field you chose:");
                                String content = scanner.next();
                                String field;
                                if (fieldInt == 1) field = "catalogNumber";
                                else if (fieldInt == 2) field = "name";
                                else if (fieldInt == 3) field = "price";
                                else field = "manufacturer";
                                Response updateResponse = service.updateProduct(supplierId, CN, field, content);
                                if (updateResponse.isError()) {
                                    System.out.println(updateResponse.getError());
                                } else System.out.println("the product was updated successfully");
                            }
                        }
                    }
                }
            }
        }
    }

    //2.2.4 DONE
    private void addDiscountMenu() {
        Response response;
        String productName, manufacturer;
        int supplierId, fromAmount;
        double discount;
        System.out.println("enter supplier ID number");
        supplierId = scanner.nextInt();
        System.out.println("enter product name");
        productName = scanner.next();
        System.out.println("enter product manufacturer");
        manufacturer = scanner.next();
        System.out.println("from which amount will the discount be?");
        fromAmount = scanner.nextInt();
        System.out.println("how many percent?");
        discount = scanner.nextDouble();
        response = service.addDiscount(supplierId, productName, manufacturer, fromAmount, discount);
        if (response.isError()) System.out.println(response.getError());
        else System.out.println("added " + discount + " percent discount to " + productName + " from " + fromAmount + " units.");
    }

    //2.2.5 DONE
    private void removeDiscountMenu() {
        ResponseT<List<ServiceSupplier>> suppliersResponse = service.getSuppliers();
        if (suppliersResponse.isError()) {
            System.out.println(suppliersResponse.getError());
        }
        else {
            List<ServiceSupplier> suppliers = suppliersResponse.getData();
            if (suppliers.isEmpty()) {
                System.out.println("***There are no suppliers in the system***");
            } else {
                System.out.println("Choose the ID of the supplier you want to remove a discount from");
                System.out.println();
                for (ServiceSupplier supplier : suppliers) {
                    System.out.printf("ID: %d | name: %s\n", supplier.id, supplier.name);
                }
                System.out.println();
                int supplierId = scanner.nextInt();
                ResponseT<List<ServiceProduct>> productsResponse = service.getProducts(supplierId);
                if (productsResponse.isError()) {
                    System.out.println(productsResponse.getError());
                }
                else {
                    List<ServiceProduct> products = productsResponse.getData();
                    if (products.isEmpty()) {
                        System.out.printf("***supplier %d has no products***", supplierId);
                    }
                    else {
                        for (ServiceProduct product : products) {
                            ResponseT<Map<Integer, Double>> discountResponse = service.getDiscounts(supplierId, product.name, product.manufacturer);
                            if (discountResponse.isError()) {
                                System.out.println(discountResponse.getError());
                                break;
                            }
                            else {
                                Map<Integer, Double> discount = discountResponse.getData();
                                if (!discount.isEmpty()) {
                                    System.out.printf("Discounts for product: CN: %s | name: %s | manufacturer: %s\n",product.catalogNumber, product.name, product.manufacturer);
                                    for (Integer amount : discount.keySet()) {
                                        System.out.printf(discount.get(amount) + " discount from " + amount + " + units");
                                    }
                                    System.out.println();
                                }
                            }
                            System.out.println("Choose the product CN:");
                            String CN = scanner.next();
                            System.out.println("Choose discount from amount:");
                            int amount = scanner.nextInt();
                            Response removeResponse = service.removeDiscount(supplierId, CN, amount);
                            if (removeResponse.isError()) {
                                System.out.println(removeResponse.getError());
                            }
                            else System.out.println("The discount was removed successfully");
                        }
                    }
                }
            }
        }
    }

    //2.3 DONE
    private void ordersManagementMenu() {
        boolean runMenu = true;
        while (runMenu) {
            String menu = "Orders Management Menu\n"
                    + "~~~~~~~\n"
                    + "1. Make shortage order\n"
                    + "2. Remove shortage order\n"
                    + "3. Make weekly order\n"
                    + "4. Remove weekly order\n"
                    + "5. Update weekly order\n"
                    + "6. Print Orders\n"
                    + "\n"
                    + "0. Back to suppliers main menu";
            System.out.println();
            System.out.println(menu);
            System.out.print("Input: ");
            System.out.println("");
            int chosenOp = scanner.nextInt();
            try {
                switch (chosenOp) {
                    case (0):
                        System.out.println();
                        runMenu = false;
                        break;
                    case (1):
                        System.out.println();
                        makeShortageOrderMenu();
                        break;
                    case (2):
                        System.out.println();
                        removeShortageOrderMenu();
                        break;
                    case (3):
                        System.out.println();
                        makeWeeklyOrderMenu();
                        break;
                    case (4):
                        System.out.println();
                        removeWeeklyOrderMenu();
                        break;
                    case (5):
                        System.out.println();
                        updateWeeklyOrderMenu();
                        break;
                    case (6):
                        System.out.println();
                        printOrdersDetails();
                        break;
                }
            } catch (Exception e) {
                System.out.println("error " + e.toString());
                System.out.println(":(");
            }
        }
    }

    //2.3.1 DONE
    private void makeShortageOrderMenu() { // public ResponseT<Integer> makeShortageOrder(String productName, String manufacturer, int amount)
        scanner.nextLine();
        System.out.println("enter the name of the product you have a shortage with:");
        String productName = scanner.nextLine();
        System.out.println("enter the manufacturer of the product:");
        String manufacturer = scanner.nextLine();
        System.out.println("how many units do you need?");
        int amount = scanner.nextInt();
        ResponseT<Integer> response = service.makeShortageOrder(productName, manufacturer, amount);
        if (response.isError()) {
            System.out.println(response.getError());

        }
        else {
            int orderId = response.getData();
            ResponseT<ServiceOrder> orderResponse = service.getOrder(orderId);
            if (orderResponse.isError()) {
                System.out.println(orderResponse.getError());
            }
            else {
                ServiceOrder order = orderResponse.getData();
                System.out.println();
                System.out.println("your shortage order has been sent, here is your order:");
                System.out.println();
                System.out.println(order.orderString);
                System.out.println();
            }
        }
    }

    //2.3.2 DONE
    private void removeShortageOrderMenu() {
        ResponseT<List<ServiceOrder>> ordersResponse = service.getShortageOrders();
        if (ordersResponse.isError()) {
            System.out.println(ordersResponse.getError());
        }
        else {
            List<ServiceOrder> orders = ordersResponse.getData();
            if (orders.isEmpty()) {
                System.out.println("***There are no orders in the system***");
            } else {
                System.out.println("Choose the ID of the order you want to remove");
                System.out.println();
                for (ServiceOrder order : orders) {
                    System.out.println(order.orderString);
                }
                System.out.println();
                int orderId = scanner.nextInt();
                Response removeResponse = service.removeShortageOrder(orderId);
                if (removeResponse.isError()) {
                    System.out.println(removeResponse.getError());
                }
                else System.out.printf("Order %d was removed successfully", orderId);
            }
        }
    }

    //2.3.3 DONE
    private void makeWeeklyOrderMenu() { // ResponseT<Integer> makeWeeklyOrder(int supplierId, Map<String, Integer> catalogNumberToAmountMap)
        Map<String, Integer> catalogNumberToAmount = new HashMap<String, Integer>();
        ResponseT<List<ServiceSupplier>> suppliersResponse = service.getSuppliers();
        if (suppliersResponse.isError()) {
            System.out.println(suppliersResponse.getError());
        }
        else {
            List<ServiceSupplier> suppliers = suppliersResponse.getData();
            System.out.println("choose the ID of the supplier you want to make a weekly order from:");
            for (ServiceSupplier supplier : suppliers) {
                System.out.print("supplier's ID: " + supplier.id + ", name: " + supplier.name + ", pay way: " + supplier.payWay);
                ResponseT<Boolean> hasWeeklyOrderResponse = service.hasWeeklyOrder(supplier.id);
                if (hasWeeklyOrderResponse.isError()) {
                    System.out.println(hasWeeklyOrderResponse.getError());
                    break;
                }
                boolean hasWeekly = hasWeeklyOrderResponse.getData();
                if (hasWeekly) System.out.println(" (has a weekly order)");
                else System.out.println();
            }
            int supplierId = scanner.nextInt();
            ResponseT<List<ServiceProduct>> productsResponse = service.getProducts(supplierId);
            if (productsResponse.isError()) {
                System.out.println(productsResponse.getError());
            }
            else {
                List<ServiceProduct> products = productsResponse.getData();
                System.out.println("choose a product from the list:");
                boolean finish = false;
                while (!finish) {
                    String[] catalogNumbers = new String[products.size()];
                    int i = 0;
                    for (ServiceProduct product : products) {
                        catalogNumbers[i] = product.catalogNumber;
                        System.out.println(i + ". CN: " + product.catalogNumber + ", name: " + product.name + ", price: "
                                + product.price + ", manufacturer: " + product.manufacturer);
                        ResponseT<Map<Integer, Double>> discountsResponse = service.getDiscounts(supplierId, product.name, product.manufacturer);
                        if (discountsResponse.isError()) {
                            System.out.println(discountsResponse.getError());
                            break;
                        }
                        Map<Integer, Double> discounts = discountsResponse.getData();
                        if (!discounts.isEmpty()) {
                            System.out.println("** discounts for " + product.name + ":");
                            for (Integer fromAmount : discounts.keySet())
                                System.out.println(discounts.get(fromAmount) +
                                        " precent discount from " + fromAmount + " units.");
                            System.out.println("**");
                        } else {
                            System.out.println("** there are no discounts for " + product.name + ".**");
                        }
                        i++;
                    }
                    int productIndex = scanner.nextInt();
                    System.out.println("how many units of this product?");
                    int amount = scanner.nextInt();
                    catalogNumberToAmount.put(catalogNumbers[productIndex], amount);
                    System.out.println("would you like to add another product?\n"
                            + "0. no\n"
                            + "1. yes");
                    if (scanner.nextInt() == 0) finish = true;
                }
                ResponseT<Integer> response = service.makeWeeklyOrder(supplierId, catalogNumberToAmount);
                if (response.isError()) {
                    System.out.println(response.getError());
                }
                else {
                    int orderId = response.getData();
                    ResponseT<Integer> dayResponse = service.getSupplierConstantDay(supplierId);
                    if (dayResponse.isError()) {
                        System.out.println(dayResponse.getError());
                    }
                    else {
                        int day = dayResponse.getData();
                        String dayString = day == 1 ? "sunday" : day == 2 ? "monday" : day == 3 ? "tuesday" : day == 4 ? "wednesday" : day == 5 ? "thursday" : day == 6 ? "friday" : day == 7 ? "saturday" : null;
                        System.out.println("order " + orderId + " is sent to the supplier and will be supplied every " + dayString + ".");
                        ResponseT<ServiceOrder> orderResponse = service.getOrder(orderId);
                        if (orderResponse.isError()) {
                            System.out.println(orderResponse.getError());
                        }
                        else {
                            ServiceOrder order = orderResponse.getData();
                            System.out.println();
                            System.out.println("here is your order:");
                            System.out.println();
                            System.out.println(order.orderString);
                            System.out.println();
                        }
                    }
                }
            }
        }
    }

    //2.3.4 DONE
    private void removeWeeklyOrderMenu() {
        ResponseT<List<ServiceOrder>> ordersResponse = service.getWeeklyOrders();
        if (ordersResponse.isError()) {
            System.out.println(ordersResponse.getError());
        }
        else {
            List<ServiceOrder> orders = ordersResponse.getData();
            if (orders.isEmpty()) {
                System.out.println("***There are no orders in the system***");
            } else {
                System.out.println("Choose the ID of the order you want to remove");
                System.out.println();
                for (ServiceOrder order : orders) {
                    System.out.println(order.orderString);
                }
                System.out.println();
                int orderId = scanner.nextInt();
                Response removeResponse = service.removeShortageOrder(orderId);
                if (removeResponse.isError()) {
                    System.out.println(removeResponse.getError());
                }
                else System.out.printf("Order %d was removed successfully", orderId);
            }
        }
    }

    //2.3.5 DONE
    private void updateWeeklyOrderMenu() {
        Map<String, Integer> catalogNumberToAmount = new HashMap<String, Integer>();
        ResponseT<List<ServiceSupplier>> suppliersResponse = service.getSuppliersWithWeeklyOrder();
        if (suppliersResponse.isError()) {
            System.out.println(suppliersResponse.getError());
        }
        else {
            List<ServiceSupplier> suppliers = suppliersResponse.getData();
            System.out.println("choose the ID of the supplier you want to update his weekly order:");
            for (ServiceSupplier supplier : suppliers) {
                System.out.println("supplier's ID: " + supplier.id + ", name: " + supplier.name + ", pay way: " + supplier.payWay);
            }
            int supplierId = scanner.nextInt();
            ResponseT<ServiceOrder> orderResponse = service.getWeeklyOrder(supplierId);
            if (orderResponse.isError()) {
                System.out.println(orderResponse.getError());
            }
            else {
                ServiceOrder order = orderResponse.getData();
                System.out.println("here is the order you want to update:");
                System.out.println();
                System.out.println(order.orderString);
                System.out.println();
                System.out.println("(if you choose a product that is already in the order, the new amount will override the old one)");
                System.out.println();
                ResponseT<List<ServiceProduct>> productsResponse = service.getProducts(supplierId);
                if (productsResponse.isError()) {
                    System.out.println(productsResponse.getError());
                }
                else {
                    List<ServiceProduct> products = productsResponse.getData();
                    System.out.println("choose a product from the list:");
                    boolean finish = false;
                    while (!finish) {
                        String[] catalogNumbers = new String[products.size()];
                        int i = 0;
                        for (ServiceProduct product : products) {
                            catalogNumbers[i] = product.catalogNumber;
                            System.out.println(i + ". CN: " + product.catalogNumber + ", name: " + product.name + ", price: "
                                    + product.price + ", manufacturer: " + product.manufacturer);
                            ResponseT<Map<Integer, Double>> discountsResponse = service.getDiscounts(supplierId, product.name, product.manufacturer);
                            if (discountsResponse.isError()) {
                                System.out.println(discountsResponse.getError());
                                break;
                            }
                            Map<Integer, Double> discounts = discountsResponse.getData();
                            if (!discounts.isEmpty()) {
                                System.out.println("** discounts for " + product.name + ":");
                                for (Integer fromAmount : discounts.keySet())
                                    System.out.println(discounts.get(fromAmount) +
                                            " precent discount from " + fromAmount + " units.");
                                System.out.println("**");
                            } else {
                                System.out.println("** there are no discounts for " + product.name + ".**");
                            }
                            i++;
                        }
                        int productIndex = scanner.nextInt();
                        System.out.println("how many units of this product?");
                        int amount = scanner.nextInt();
                        catalogNumberToAmount.put(catalogNumbers[productIndex], amount);
                        System.out.println("would you like to add another product?\n"
                                + "0. no\n"
                                + "1. yes");
                        if (scanner.nextInt() == 0) finish = true;
                    }
                    ResponseT<Integer> response = service.updateWeeklyOrder(supplierId, catalogNumberToAmount);
                    if (response.isError()) {
                        System.out.println(response.getError());
                    }
                    else {
                        int orderId = response.getData();
                        ResponseT<Integer> dayResponse = service.getSupplierConstantDay(supplierId);
                        if (dayResponse.isError()) {
                            System.out.println(dayResponse.getError());
                        }
                        else {
                            int day = dayResponse.getData();
                            String dayString = day == 1 ? "sunday" : day == 2 ? "monday" : day == 3 ? "tuesday" : day == 4 ? "wednesday" : day == 5 ? "thursday" : day == 6 ? "friday" : day == 7 ? "saturday" : null;
                            System.out.println("order " + orderId + " is updated and will be supplied every " + dayString + ".");
                            ResponseT<ServiceOrder> updatedOrderResponse = service.getOrder(orderId);
                            if (updatedOrderResponse.isError()) {
                                System.out.println(updatedOrderResponse.getError());
                            }
                            else {
                                ServiceOrder updatedOrder = updatedOrderResponse.getData();
                                System.out.println();
                                System.out.println("here is your updated order:");
                                System.out.println();
                                System.out.println(updatedOrder.orderString);
                                System.out.println();
                            }
                        }
                    }
                }
            }
        }
    }

    //2.3.6 DONE
    private void printOrdersDetails() {
        System.out.println();
        for (ServiceOrder order : service.getAllOrders().getData()) {
            System.out.println(order.orderString);
            System.out.println();
        }
    }

    //2.4
    private void reportsMenu() {
        boolean runMenu = true;
        while (runMenu) {
            String menu = "Reports Menu\n"
                    + "~~~~~~~\n"
                    + "1. Print Suppliers details\n"
                    + "2. Print Orders\n"
                    + "\n"
                    + "0. Back to suppliers main menu";
            System.out.println();
            System.out.println(menu);
            System.out.print("Input: ");
            System.out.println("");
            int chosenOp = scanner.nextInt();
            try {
                switch (chosenOp) {
                    case (0):
                        System.out.println();
                        runMenu = false;
                        break;
                    case (1):
                        System.out.println();
                        printSuppliersDetails();
                        break;
                    case (2):
                        System.out.println();
                        printOrdersDetails();
                        break;
                }
            } catch (Exception e) {
                System.out.println("error " + e.toString());
                System.out.println(":(");
            }
        }
    }
}