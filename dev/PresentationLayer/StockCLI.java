package PresentationLayer;

import BusinessLayer.EmployeeBusinessLayer.Role;
import ServiceLayer.ResponseT;
import ServiceLayer.StockServiceLayer.*;
import ServiceLayer.StockServiceLayer.obj.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class StockCLI {

    private final StockService stockService;

    public StockCLI() {
        stockService = StockService.getInstance();
    }

    public void start(List<Role> employeeRoles) {
        if (employeeRoles.contains(Role.Storekeeper)) StockEmployeeMainMenu();
        else if (employeeRoles.contains(Role.Manager)) BranchManagerMainMenu();
        else System.out.println("No Actions Allowed.");
    }

    public void BranchManagerMainMenu() {
        String mainMenu = String.format("~~~~~~~~~~~~~~~~~~~\n" +
                "Super-Li Stock Menu\n" +
                "~~~~~~~~~~~~~~~~~~~\n" +
                "1. Notifications - (%d)\n" +
                "2. Generated Reports\n" +
                "0. Exit\n", getNotificationNumber());
        System.out.println(mainMenu);
        while (true) {
            System.out.print("Input: ");
            Scanner scanner = new Scanner(System.in);
            int input;
            try {
                input = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Invalid Input.");
                continue;
            }
            switch (input) {
                case 1:
                    showNotificationsMenu();
                    break;
                case 2:
                    showGenerateReportMenu();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid Input.");
            } break;
        }
    }

    public void StockEmployeeMainMenu() {
        while (true) {
            String mainMenu = String.format("~~~~~~~~~~~~~~~~~~~\n" +
                    "Super-Li Stock Menu\n" +
                    "~~~~~~~~~~~~~~~~~~~\n" +
                    "1. Notifications - (%d)\n" +
                    "2. Manage Categories\n" +
                    "3. Manage Products\n" +
                    "4. Manage Product Units\n" +
                    "5. Manage Discounts\n" +
                    "6. Generate Reports\n" +
                    "0. Exit\n", getNotificationNumber());
            System.out.println(mainMenu);

            while (true) {
                System.out.print("Input: ");
                Scanner scanner = new Scanner(System.in);
                int input;
                try {
                    input = scanner.nextInt();
                }
                catch (Exception e) {
                    System.out.println("Invalid Input.");
                    continue;
                }
                switch (input) {
                    case 1:
                        showNotificationsMenu();
                        break;
                    case 2:
                        showManageCategoriesMenu();
                        break;
                    case 3:
                        showManageProductsMenu();
                        break;
                    case 4:
                        showManageProductUnitsMenu();
                        break;
                    case 5:
                        showManageDiscountsMenu();
                        break;
                    case 6:
                        showGenerateReportMenu();
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Invalid Input");
                        continue;
                }
                break;
            }
        }
    }

    private void showManageCategoriesMenu() {
        System.out.print("~~~~~~~~~~~~~~~~~~~~~~\n" +
                           "Manage Categories Menu\n" +
                           "~~~~~~~~~~~~~~~~~~~~~~\n" +
                           "1. Add Category\n" +
                           "2. Edit Category\n" +
                           "3. Remove Category\n" +
                           "0. Return\n\n");
        while (true) {
            System.out.print("Input: ");
            Scanner scanner = new Scanner(System.in);
            int input;
            try {
                input = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Invalid Input.");
                continue;
            }
            switch (input) {
                case 1:
                    showAddCategoryMenu();
                    break;
                case 2:
                    showEditCategoryMenu();
                    break;
                case 3:
                    showRemoveCategoryMenu();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid Input");
            }
            break;
        }
    }

    private void showManageProductsMenu() {
        System.out.print("~~~~~~~~~~~~~~~~~~~~\n" +
                "Manage Products Menu\n" +
                "~~~~~~~~~~~~~~~~~~~~\n" +
                "1. Add Product\n" +
                "2. Edit Product\n" +
                "3. Remove Product\n" +
                "4. Change Product Category\n" +
                "0. Return\n\n");
        while (true) {
            System.out.print("Input: ");
            Scanner scanner = new Scanner(System.in);
            int input;
            try {
                input = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Invalid Input.");
                continue;
            }
            switch (input) {
                case 1:
                    showAddProductMenu();
                    break;
                case 2:
                    showEditProductMenu();
                    break;
                case 3:
                    showRemoveProductMenu();
                    break;
                case 4:
                    showChangeProductCategory();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid Input");
            } break;
        }
    }

    private void showManageProductUnitsMenu() {
        System.out.print("~~~~~~~~~~~~~~~~~~~~~~~~~\n" +
                "Manage Product Units Menu\n" +
                "~~~~~~~~~~~~~~~~~~~~~~~~~\n" +
                "1. Add Product Unit\n" +
                "2. Edit Product Unit\n" +
                "3. Remove Product Unit\n" +
                "4. Mark Product Unit As Defected\n" +
                "0. Return\n\n");
        while (true) {
            System.out.print("Input: ");
            Scanner scanner = new Scanner(System.in);
            int input;
            try {
                input = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Invalid Input.");
                continue;
            }
            switch (input) {
                case 1:
                    showAddProductUnitMenu();
                    break;
                case 2:
                    showEditProductUnitMenu();
                    break;
                case 3:
                    showRemoveProductUnitMenu();
                    break;
                case 4:
                    showMarkProductUnitAsDefectedMenu();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid Input");
            }break;
        }
    }

    private void showManageDiscountsMenu() {
        System.out.print("~~~~~~~~~~~~~~~~~~~~~\n" +
                "Manage Discounts Menu\n" +
                "~~~~~~~~~~~~~~~~~~~~~\n" +
                "1. Add Sale Discount\n" +
                "2. Edit Sale Discount\n" +
                "3. Remove Sale Discount\n" +
                "0. Return\n\n");
        while (true) {
            System.out.print("Input: ");
            Scanner scanner = new Scanner(System.in);
            int input;
            try {
                input = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Invalid Input.");
                continue;
            }
            switch (input) {
                case 1:
                    showAddSaleDiscountMenu();
                    break;
                case 2:
                    showEditSaleDiscountMenu();
                    break;
                case 3:
                    showRemoveSaleDiscountMenu();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid Input");
            } break;
        }
    }

    private void showRemoveSaleDiscountMenu() {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~\n" +
                "Edit Sale Discount Info\n" +
                "~~~~~~~~~~~~~~~~~~~~~~~");
        Scanner scanner;
        boolean identifyById = booleanQuestion("Identify Sale Discount By Id?");
        int discountId;
        serviceSaleDiscount discount = null;
        if (identifyById) {
            while (true) {
                scanner = new Scanner(System.in);
                System.out.print("Enter ID: ");
                try {
                    discountId = scanner.nextInt();
                } catch (Exception e) {
                    System.out.println("Invalid Input.");
                    continue;
                }
                ResponseT<serviceSaleDiscount> res = stockService.getSaleDiscount(discountId);
                if (!res.isError()) discount = res.getData();
                else {
                    System.out.println("No Sale Discount With That Id.");
                    continue;
                }
                break;
            }
        }
        else {
            serviceProduct product = showProducts();
            if (product != null) {
                discountId = product.getSaleDiscount();
            }
            else {
                System.out.println("Error Occurred");
                return;
            }
        }
        System.out.println(discount);
        if (booleanQuestion("Are you sure you'd like to remove this discount?")) {
            ResponseT<Boolean> res = stockService.removeSaleDiscount(discountId);
            if (!res.isError()) System.out.println("Sale Discount Removed Successfully.");
            else System.out.println("Sale Discount Removed Unsuccessfully.");
        }
    }

    private void showEditSaleDiscountMenu() {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~\n" +
                "Edit Sale Discount Info\n" +
                "~~~~~~~~~~~~~~~~~~~~~~~");

        Scanner scanner;
        serviceSaleDiscount discount;
        int discountId;
        boolean identifyById = booleanQuestion("Identify Sale Discount By Id?");

        if (identifyById) {
            while (true) {
                scanner = new Scanner(System.in);
                System.out.print("Enter ID: ");
                try {
                    discountId = scanner.nextInt();
                } catch (Exception e) {
                    System.out.println("Invalid Input.");
                    continue;
                }
                ResponseT<serviceSaleDiscount> res = stockService.getSaleDiscount(discountId);
                if (!res.isError()) discount = res.getData();
                else {
                    System.out.println("No Sale Discount With That Id.");
                    continue;
                }
                break;
            }
        }
        else {
            serviceProduct product = showProducts();
            if (product != null) {
                discountId = product.getSaleDiscount();
                ResponseT<serviceSaleDiscount> res = stockService.getSaleDiscount(discountId);
                if (!res.isError()) discount = res.getData();
                else return;
            }
            else return;
        }
        float discountPercentage = discount.getDiscount();
        Date start = discount.getStart();
        Date end = discount.getEnd();
        System.out.println("Current Product Unit:");
        System.out.println(discount);
        if (booleanQuestion("Change Discount Amount?")) {
            while (true) {
                scanner = new Scanner(System.in);
                System.out.print("Enter New Discount (0-100): ");
                try {
                    discountPercentage = scanner.nextFloat();
                    if (discountPercentage <= 0) throw new Exception();
                    break;
                } catch (Exception e) {
                    System.out.println("Invalid Input");
                    scanner.nextLine();
                }
            }
        }
        while (true) {
            if (booleanQuestion("Change Start Date?")) {
                while (true) {
                    System.out.print("Enter New Start Date (yyyy-MM-dd): ");
                    scanner = new Scanner(System.in);
                    String startDate = scanner.nextLine();
                    try {
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        start = formatter.parse(startDate);
                        break;
                    } catch (ParseException e) {
                        System.out.println("Invalid Input");
                        scanner.nextLine();
                    }
                }
            }
            if (booleanQuestion("Change End Date?")) {
                while (true) {
                    System.out.print("Enter New End Date (yyyy-MM-dd): ");
                    scanner = new Scanner(System.in);
                    String endDate = scanner.nextLine();
                    try {
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        end = formatter.parse(endDate);
                        break;
                    } catch (ParseException e) {
                        System.out.println("Invalid Input");
                        scanner.nextLine();
                    }
                }
            }
            if (start.after(end)) {
                System.out.println("Start Date Can Not Be After End Date.");
            }
            else break;
        }

        ResponseT<Boolean> r = stockService.editSaleDiscount(discountId, discountPercentage, start, end);
        if (r.isError()) System.out.println("Edit Failed - " + r.getError());
        else System.out.println("Edit Succeeded.");
    }


    private void showEditProductUnitMenu() {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~\n" +
                "Edit Product Unit Info\n" +
                "~~~~~~~~~~~~~~~~~~~~~");

        Scanner scanner;
        serviceProductUnit unit;
        int unitId;
        boolean identifyById = booleanQuestion("Identify Product Unit By Id?");

        if (identifyById) {
            while (true) {
                scanner = new Scanner(System.in);
                System.out.print("Enter ID: ");
                try {
                    unitId = scanner.nextInt();
                } catch (Exception e) {
                    System.out.println("Invalid Input.");
                    continue;
                }
                ResponseT<serviceProductUnit> res = stockService.getProductUnit(unitId);
                if (!res.isError()) unit = res.getData();
                else {
                    System.out.println("No Product Unit With That Id.");
                    continue;
                }
                break;
            }
        }
        else {
            unit = showProductUnits();
            if (unit != null)
                unitId = unit.getId();
            else return;
        }
        Date expirationDate = unit.getExpirationDate();
        int location = unit.getLocation();
        boolean isInStorage = unit.isInStorage();
        System.out.println("Current Product Unit:");
        System.out.println(unit);
        if (booleanQuestion("Change Expiration Date?")) {
            while (true) {
                System.out.print("Enter New Expiration Date (yyyy-MM-dd): ");
                scanner = new Scanner(System.in);
                String expDate = scanner.next();
                try {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    expirationDate = formatter.parse(expDate);
                    break;
                }
                catch (ParseException e) {
                    System.out.println("Invalid Input");
                    scanner.nextLine();
                }
            }
        }
        if (booleanQuestion("Change Product Location?")) {
            while (true) {
                scanner = new Scanner(System.in);
                System.out.print("Enter New Location: ");
                try {
                    location = scanner.nextInt();
                    if (location < 1) throw new Exception();
                    break;
                } catch (Exception e) {
                    System.out.println("Invalid Input");
                    scanner.nextLine();
                }
            }
        }
        if (booleanQuestion("Change Location In Storage Or On Shelves?")) {
            isInStorage = !isInStorage;
        }

        ResponseT<Boolean> r = stockService.editProductUnit(unitId, expirationDate, location, isInStorage);
        if (r.isError()) System.out.println("Edit Failed - " + r.getError());
        else System.out.println("Edit Succeeded.");
    }

    private boolean booleanQuestion(String question) {
        String input = "";
        while (true) {
            System.out.print(question + "(y/n): ");
            Scanner scanner = new Scanner(System.in);
            input = scanner.nextLine();
            if (!input.equals("y") && !input.equals("n")) System.out.println("Invalid Input.");
            else break;
        }
        return input.equals("y");
    }

    private void showEditProductMenu() {
        System.out.println("~~~~~~~~~~~~~~~~\n" +
                "Edit Product Info\n" +
                "~~~~~~~~~~~~~~~~");
        String input = "";
        Scanner scanner;
        serviceProduct product;
        int productId;
        boolean identifyById = booleanQuestion("Identify Category By Id?");

        if (identifyById) {
            while (true) {
                scanner = new Scanner(System.in);
                System.out.print("Enter ID: ");
                try {
                    productId = scanner.nextInt();
                } catch (Exception e) {
                    System.out.println("Invalid Input.");
                    continue;
                }
                ResponseT<serviceProduct> res = stockService.getProduct(productId);
                if (!res.isError()) product = res.getData();
                else {
                    System.out.println("No Product With That Id.");
                    continue;
                }
                break;
            }
        }
        else {
            product = showProducts();
            if (product != null)
                productId = product.getId();
            else return;
        }
        String name = product.getName();
        int minQuantity = product.getMinQuantity();
        String producer = product.getProducer();
        float salePrice = product.getSalePrice();
        float weight = product.getWeight();
        scanner = new Scanner(System.in);
        System.out.println("Current Product:");
        System.out.println(product);
        if (booleanQuestion("Change Product Name?")) {
            System.out.print("Enter New Product Name: ");
            name = scanner.nextLine();
        }
        if (booleanQuestion("Change Product Minimum Quantity?")) {
            while (true) {
                scanner = new Scanner(System.in);
                System.out.print("Enter New Product Minimum Quantity: ");
                try {
                    minQuantity = scanner.nextInt();
                    if (minQuantity < 1) throw new Exception();
                    break;
                } catch (Exception e) {
                    System.out.println("Invalid Input");
                    scanner.nextLine();
                }
            }
        }
        if (booleanQuestion("Change Producer Name?")) {
            scanner = new Scanner(System.in);
            System.out.print("Enter Producer Name: ");
            producer = scanner.next();
            scanner.nextLine();
        }
        if (booleanQuestion("Change Sale Price?")) {
            while (true) {
                scanner = new Scanner(System.in);
                System.out.print("Enter Product Sale Price: ");
                try {
                    salePrice = scanner.nextFloat();
                    if (salePrice <= 0) throw new Exception();
                    break;
                } catch (Exception e) {
                    System.out.println("Invalid Input");
                    scanner.nextLine();
                }
            }
        }
        if (booleanQuestion("Change Product Weight?")) {
            while (true) {
                scanner = new Scanner(System.in);
                System.out.print("Enter Product Weight: ");
                try {
                    weight = scanner.nextFloat();
                    if (weight <= 0) throw new Exception();
                    break;
                } catch (Exception e) {
                    System.out.println("Invalid Input");
                    scanner.nextLine();
                }
            }
        }

        ResponseT<Boolean> r = stockService.editProduct(productId, name, producer, minQuantity, salePrice, weight);
        if (r.isError()) System.out.println("Edit Failed - " + r.getError());
        else System.out.println("Edit Succeeded.");
    }



    private void showEditCategoryMenu() {
        System.out.println("~~~~~~~~~~~~~~~~~~\n" +
                           "Edit Category Menu\n" +
                           "~~~~~~~~~~~~~~~~~~\n");
        String input = "";
        String name;
        int categoryId;
        while (true) {
            System.out.print("Identify Category By Id? (y/n): ");
            Scanner scanner = new Scanner(System.in);
            input = scanner.nextLine();
            if (!input.equals("y") && !input.equals("n")) System.out.println("Invalid Input.");
            else break;
        }
        if (input.equals("y")) {

            while (true) {
                Scanner scanner = new Scanner(System.in);
                System.out.print("Enter ID: ");

                try {
                    categoryId = scanner.nextInt();
                } catch (Exception e) {
                    System.out.println("Invalid Input.");
                    continue;
                }
                break;
            }
        }
        else {
            serviceCategory category = showCategories(null);
            if (category != null)
                categoryId = category.getCategoryId();
            else return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter New Name: ");
        name = scanner.nextLine();

        ResponseT<Boolean> response = stockService.editCategory(categoryId, name);
        if (response.isError()) System.out.println(response.getError());
        else if (!response.getData()) System.out.println("Category Edited Unsuccessfully");
        else System.out.println("Category Edited Successfully.");
    }


    private void showRemoveCategoryMenu() {
        System.out.println("~~~~~~~~~~~~~~~~~~~~\n" +
                           "Remove Category Menu\n" +
                           "~~~~~~~~~~~~~~~~~~~~\n");
        String input = "";
        while (true) {
            System.out.print("Identify Category By Id? (y/n): ");
            Scanner scanner = new Scanner(System.in);
            input = scanner.nextLine();
            if (!input.equals("y") && !input.equals("n")) System.out.println("Invalid Input.");
            else break;
        }
        if (input.equals("y")) {
            int categoryId;
            while (true) {
                Scanner scanner = new Scanner(System.in);
                System.out.print("Enter ID: ");

                try {
                    categoryId = scanner.nextInt();
                } catch (Exception e) {
                    System.out.println("Invalid Input.");
                    continue;
                }
                break;
            }
            ResponseT<Boolean> response = stockService.removeCategory(categoryId);
            if (response.isError()) System.out.println(response.getError());
            else System.out.println("Category Removed Successfully.");
            return;
        }
        serviceCategory category = showCategories(null);
        if (category == null) return;
        ResponseT<Boolean> removalResponse = stockService.removeProduct(category.getCategoryId());
        if (removalResponse.isError()) System.out.println(removalResponse.getError());
        else System.out.println("Product Removed Successfully.");
    }


    private void showRemoveProductMenu() {
        System.out.println("~~~~~~~~~~~~~~~~~~~\n" +
                           "Remove Product Menu\n" +
                           "~~~~~~~~~~~~~~~~~~~\n");
        String input = "";
        while (true) {
            System.out.print("Identify Product By Id? (y/n): ");
            Scanner scanner = new Scanner(System.in);
            input = scanner.nextLine();
            if (!input.equals("y") && !input.equals("n")) System.out.println("Invalid Input.");
            else break;
        }
        if (input.equals("y")) {
            int productId;
            while (true) {
                Scanner scanner = new Scanner(System.in);
                System.out.print("Enter ID: ");

                try {
                    productId = scanner.nextInt();
                } catch (Exception e) {
                    System.out.println("Invalid Input.");
                    continue;
                }
                break;
            }
            ResponseT<Boolean> response = stockService.removeProduct(productId);
            if (response.isError()) System.out.println(response.getError());
            else System.out.println("Product Removed Successfully.");
            return;
        }
        serviceProduct product = showProducts();
        if (product == null) return;
        ResponseT<Boolean> removalResponse = stockService.removeProduct(product.getId());
        if (removalResponse.isError()) System.out.println(removalResponse.getError());
        else System.out.println("Product Removed Successfully.");
    }


    private void showRemoveProductUnitMenu() {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~\n" +
                           "Remove Product Unit Menu\n" +
                           "~~~~~~~~~~~~~~~~~~~~~~~~\n");
        String input = "";
        while (true) {
            System.out.print("Identify Product Unit By Id? (y/n): ");
            Scanner scanner = new Scanner(System.in);
            input = scanner.nextLine();
            if (!input.equals("y") && !input.equals("n")) System.out.println("Invalid Input.");
            else break;
        }
        if (input.equals("y")) {
            int unitId;
            while (true) {
                Scanner scanner = new Scanner(System.in);
                System.out.print("Enter ID: ");
                try {
                    unitId = scanner.nextInt();
                }
                catch (Exception e) {
                    System.out.println("Invalid Input");
                    continue;
                }
                break;
            }
            ResponseT<Integer> response = stockService.removeProductUnit(unitId);
            if (response.isError()) System.out.println(response.getError());
            else System.out.println("Product Unit Removed Successfully.");
            return;

        }
        serviceProductUnit unit = showProductUnits();
        if (unit == null) return;
        ResponseT<Integer> removalResponse = stockService.removeProductUnit(unit.getId());
        if (removalResponse.isError()) System.out.println(removalResponse.getError());
        else System.out.println("Unit Removed Successfully.");
    }

    private serviceProductUnit showProductUnits() {
        serviceProduct product = showProducts();
        if (product == null) return null;
        ResponseT<List<serviceProductUnit>> response = stockService.getProductUnits(product.getId());
        if (response.isError()){
            System.out.println(response.getError());
            return null;
        }
        List<serviceProductUnit> units = response.getData();
        int indexCounter = 1;
        for (serviceProductUnit unit : units) {
            System.out.println(indexCounter++ + ". ID: " + unit.getId() + " - Expiration Date: " + new SimpleDateFormat("yyyy-MM-dd").format(unit.getExpirationDate()) +
                    " - " + (unit.isDefected() ? unit.isExpired() ? "DEFECTED - EXPIRED - " : "DEFECTED - " : "") + "Location: " + unit.getLocation() + " - " +
                    (unit.isInStorage()? "IN STORAGE" : "ON SHELVES"));
        }
        System.out.println("0. Return");
        System.out.print("Choose Unit: ");
        Scanner scanner = new Scanner(System.in);
        int input = scanner.nextInt();
        if (input == 0) return null;
        else return units.get(input - 1);
    }

    private void showAddProductMenu() {
        System.out.println("~~~~~~~~~~~~~~~~~\n" +
                           "Add Product Menu:\n" +
                           "~~~~~~~~~~~~~~~~~");
        serviceCategory category = showCategories(null);
        if (category == null) return;
        if (!showInputProduct(category)) {
            System.out.println("Product Addition Failed");
        }
        else {
            System.out.println("Product Added Successfully");
        }
    }

    private boolean showInputProduct(serviceCategory category) {
        System.out.println("~~~~~~~~~~~~~~~~~~\n" +
                           "Input Product Info\n" +
                           "~~~~~~~~~~~~~~~~~~");
        String name;
        int minQuantity;
        String producer;
        float salePrice;
        float weight;
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Product Name: ");
        name = scanner.nextLine();
        while (true) {
            scanner = new Scanner(System.in);
            System.out.print("Enter Product Minimum Quantity: ");
            try {
                minQuantity = scanner.nextInt();
                if (minQuantity < 1) throw new Exception();
                break;
            } catch (Exception e) {
                System.out.println("Invalid Input");
                scanner.nextLine();
            }
        }

        scanner = new Scanner(System.in);
        System.out.print("Enter Producer Name: ");
        producer = scanner.next();
        scanner.nextLine();

        while (true) {
            scanner = new Scanner(System.in);
            System.out.print("Enter Product Sale Price: ");
            try {
                salePrice = scanner.nextFloat();
                if (salePrice <= 0) throw new Exception();
                break;
            } catch (Exception e) {
                System.out.println("Invalid Input");
                scanner.nextLine();
            }
        }
        while (true) {
            scanner = new Scanner(System.in);
            System.out.print("Enter Product Weight: ");
            try {
                weight = scanner.nextFloat();
                if (weight <= 0) throw new Exception();
                break;
            } catch (Exception e) {
                System.out.println("Invalid Input");
                scanner.nextLine();
            }
        }

        ResponseT<Boolean> r = stockService.addProduct(name, producer, minQuantity, category.getCategoryId(), salePrice, weight);
        return !r.isError();
    }

    private void showNotificationsMenu() {
        String menu = "~~~~~~~~~~~~~~\n" +
                "Notifications:\n" +
                "~~~~~~~~~~~~~~\n\n" +
                "The following products are under the minimal quantity and have been ordered.\n\n";
        List<serviceProduct> list = getNotifications();
        if (list.isEmpty()) {
            menu += "No notifications\n";
        }
        else {
            int indexCounter = 1;
            for (serviceProduct p : list) {
                menu += indexCounter++ + ". " + parseNotification(p) + "\n";
            }
        }
        menu += "0. Return\n";
        System.out.println(menu);
        System.out.print("Input: ");
        Scanner scanner = new Scanner(System.in);
        int input = -1;
        while (input != 0) {
            input = scanner.nextInt();
            if (input != 0) System.out.println("Invalid Input");
        }
    }

    private void showAddProductUnitMenu() {
        System.out.println("~~~~~~~~~~~~~~~~~\n" +
                           "Add Product Menu:\n" +
                           "~~~~~~~~~~~~~~~~~\n");
        while (true) {

            serviceProduct product = showProducts();
            if (product == null) return;
            if (showInputProductUnitMenu(product)) break;
        }
    }

    private serviceCategory showCategories(serviceCategory c) {
        List<serviceCategory> categories = null;
        if (c == null) {    // i.e we want to see the top level of categories
            ResponseT<List<serviceCategory>> response = stockService.getTopLevelCategories();
            if (!response.isError()) {
                categories = response.getData();
                if (categories.isEmpty()) {
                    System.out.println("No Categories Present In The System.");
                    return null;
                }
            } else {
                System.out.println(response.getError());
            }
        }
        else {  // i.e we are looking for the sub-categories of c
            ResponseT<List<serviceCategory>> response = stockService.getSubCategories(c.getCategoryId());
            if (response.isError()) {
                System.out.println(response.getError());
                return null;
            }
            else {
                categories = response.getData();
            }
        }
        if (categories != null && !categories.isEmpty()) {
            while (true) {
                int indexCounter = 1;
                String menu = c != null ? "\n" + c.getCategoryName() + " Sub-Categories:\n\n" : "\nCategories:\n\n";
                for (serviceCategory sub : categories) {
                    menu += indexCounter++ + ". " + sub.getCategoryName() + "\n";
                }
                menu += "0. Return";
                System.out.println(menu);
                int input = -1;
                while (input < 0 || input >= indexCounter) {
                    System.out.print("Choose Category:");
                    Scanner scanner = new Scanner(System.in);
                    input = scanner.nextInt();
                    if (input >= indexCounter || input < 0) System.out.println("Invalid Input");
                    else if (input == 0) {
                        return null;    //we would like to return to the last menu
                    } else break;
                }
                serviceCategory selectedCategory = showCategories(categories.get(input - 1));
                if (selectedCategory != null) return selectedCategory;
                else return c;
            }
        } return c;
    }

    private serviceProduct showProducts() {
        serviceCategory category = showCategories(null);
        if (category != null) {
            ResponseT<List<serviceProduct>> response = stockService.getProducts(category.getCategoryId());
            if (response.isError()) {
                System.out.println(response.getError());
                return null;
            }
            List<serviceProduct> products = response.getData();
            if (products != null) {
                while (true) {
                    int indexCounter = 1;
                    String menu = "\n" + category.getCategoryName() + " Products:\n\n";
                    for (serviceProduct p : products) {
                        menu += indexCounter++ + ". " + p.getName() + "\n";
                    }
                    menu += "0. Return";
                    System.out.println(menu);
                    int input = -1;
                    while (input < 0 || input >= indexCounter) {
                        System.out.print("Choose Product:");
                        Scanner scanner = new Scanner(System.in);
                        input = scanner.nextInt();
                        if (input >= indexCounter || input < 0) System.out.println("Invalid Input");
                        else if (input == 0) return null;
                        else {
                            return products.get(input - 1);
                        }
                    }
                }
            } else {
                System.out.println("No Product Types In This Category.");
                return null;
            }
        }
        return null;
    }

    private boolean showInputProductUnitMenu(serviceProduct p) {
        System.out.println("~~~~~~~~~~~~~~~~~~~\n" +
                           "Input Product Info:\n" +
                           "~~~~~~~~~~~~~~~~~~~\n");
        Date expirationDate;
        int location;
        int supplierID;
        boolean isInStorage;
        serviceSupplierDiscount discount = null;
        int amount;

        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter Product Amount: ");
            try {

                amount = scanner.nextInt();
                if (amount < 1) throw new Exception();
                break;
            }
            catch (Exception e) {
                System.out.println("Invalid Input");
                scanner.nextLine();
            }
        }
        while (true) {
            System.out.print("\nEnter Expiration Date (yyyy-MM-dd): ");
            Scanner scanner = new Scanner(System.in);
            String expDate = scanner.next();
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                expirationDate = formatter.parse(expDate);
                break;
            }
            catch (ParseException e) {
                System.out.println("Invalid Input");
                scanner.nextLine();
            }
        }
        while (true) {
            System.out.print("\nEnter Shelf Number: ");
            Scanner scanner = new Scanner(System.in);
            try {
                location = scanner.nextInt();
                if (location < 0) throw new Exception();
                break;
            }
            catch (Exception e) {
                System.out.println("Invalid Input");
                scanner.nextLine();
            }
        }
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("\nIs The Item In Storage? (y/n)\nInput: ");
            String inStorage = scanner.next();
            if (inStorage.equals("y")) {
                isInStorage = true;
                break;
            }
            else if (inStorage.equals("n")) {
                isInStorage = false;
                break;
            }
            else System.out.println("Invalid Input");
        }

        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("\nHas The Item Been Discounted? (y/n)\nInput: ");
            String discounted = scanner.next();
            if (discounted.equals("n")) {
                break;
            } else if (discounted.equals("y")) {
                int discountAmount;
                while (true) {
                    scanner = new Scanner(System.in);
                    System.out.print("\nEnter Supplier ID: ");
                    try {
                        supplierID = scanner.nextInt();
                        if (supplierID <= 0) throw new Exception();
                        break;
                    }
                    catch (Exception e) {
                        System.out.println("Invalid Input");
                        scanner.nextLine();
                    }
                }
                while (true) {
                    System.out.print("\nEnter Discount Percentage: (0-100) ");
                    try {
                        scanner = new Scanner(System.in);
                        discountAmount = scanner.nextInt();
                        if (discountAmount > 100 || discountAmount < 0) throw new Exception();
                        discount = new serviceSupplierDiscount(discountAmount, p.getId(), supplierID);
                        break;
                    }
                    catch (Exception e) {
                        System.out.println("Invalid Input");
                        scanner.nextLine();
                    }
                }
                break;
            }
            else System.out.println("Invalid Input");
        }
        ResponseT<Boolean> r = stockService.addProductUnit(p.getId(), expirationDate, location, isInStorage, discount, amount);
        if (!r.isError()) {
            System.out.println("Product Units Added Successfully");
            return r.getData();
        }
        else {
            System.out.println("Product Units Addition Failed - " + r.getError());
            return false;
        }
    }

    private String parseNotification(serviceProduct p) {
        return p.getName() + " - ID: " + p.getId() + " - Quantity/Min Quantity: " + p.getAmount() + "/" + p.getMinQuantity();
    }

    private int getNotificationNumber() {
        ResponseT<Integer> r = stockService.underMinimumQuantitiesNumber();
        if (!r.isError()) {
            return r.getData();
        }
        else return -1;
    }

    private List<serviceProduct> getNotifications() {
        ResponseT<List<serviceProduct>> r = stockService.underMinimumQuantities();
        if (!r.isError()) {
            return r.getData();
        }
        else return new ArrayList<>();
    }

    private void showAddCategoryMenu() {
        System.out.println("~~~~~~~~~~~~~~~~~~\n" +
                           "Add Category Menu:\n" +
                           "~~~~~~~~~~~~~~~~~~\n");
        ResponseT<List<serviceCategory>> response = stockService.getTopLevelCategories();
        if (response.isError()) {
            System.out.println(response.getError());
            return;
        }
        List<serviceCategory> categories = response.getData();
        if (categories != null) {
            int indexCounter = 1;
            System.out.println("Choose Category:");
            for (serviceCategory c : categories) {
                System.out.println(indexCounter++ + ". " + c.getCategoryName());
            }
            System.out.println(indexCounter + ". New Category");
            System.out.println("0. Exit");
            while (true) {
                Scanner scanner = new Scanner(System.in);
                System.out.print("\nInput:");
                int input = scanner.nextInt();
                if (input > indexCounter || input < 0) {
                    System.out.println("Invalid Input.");
                } else if (input == indexCounter) {
                    serviceCategory newCat = showInputCategoryMenu(null);
                    System.out.println("Create " + newCat.getCategoryName() + " Sub-Category: ");
                    newCat = showInputCategoryMenu(newCat.getCategoryId());
                    System.out.println("Create " + newCat.getCategoryName() + " Sub-Category: ");
                    showInputCategoryMenu(newCat.getCategoryId());
                    break;
                }
                else if (input == 0) return;
                else {
                    serviceCategory superCategory = categories.get(input - 1);
                    response = stockService.getSubCategories(superCategory.getCategoryId());
                    if (response.isError()) {
                        System.out.println(response.getError());
                        return;
                    }
                    categories = response.getData();
                    indexCounter = 1;
                    System.out.println("Choose Category:");
                    for (serviceCategory c : categories) {
                        System.out.println(indexCounter++ + ". " + c.getCategoryName());
                    }
                    System.out.println(indexCounter + ". New Category");
                    System.out.println("0. Exit");
                    while (true) {
                        scanner = new Scanner(System.in);
                        System.out.print("\nInput:");
                        input = scanner.nextInt();
                        if (input > indexCounter || input < 0) {
                            System.out.println("Invalid Input.");
                        } else if (input == indexCounter) {
                            serviceCategory newCat = showInputCategoryMenu(superCategory.getCategoryId());
                            System.out.println("Create " + newCat.getCategoryName() + " Sub-Category: ");
                            showInputCategoryMenu(newCat.getCategoryId());
                            break;
                        }
                        else if (input == 0) return;
                        else {
                            superCategory = categories.get(input - 1);
                            showInputCategoryMenu(superCategory.getCategoryId());
                            break;
                        }
                    } break;
                }
            }
        }
    }

    private serviceCategory showInputCategoryMenu(Integer superId) {
        String categoryName;
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter Category Name: ");
            categoryName = scanner.nextLine();
            if (categoryName.isEmpty()) {
                System.out.println("Invalid Input.");
            }
            else {
                ResponseT<serviceCategory> response = stockService.addCategory(categoryName, superId);
                if (response.isError()) {
                    System.out.println(response.getError());
                }
                return response.getData();
            }
        }
    }

    private void showAddSaleDiscountMenu() {
        serviceProduct product;
        float discount;
        Date start;
        Date end;
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~\n" +
                           "Add Sale Discount Menu\n" +
                           "~~~~~~~~~~~~~~~~~~~~~~\n");
        product = showProducts();
        if (product == null) {
            System.out.println("Error Occurred.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        while (true) {
            System.out.print("Enter Discount Amount (0-100): ");
            discount = scanner.nextFloat();
            scanner.nextLine();
            if (discount <= 0 || discount > 100) System.out.println("Invalid Input");
            else {
                break;
            }
        }

        while (true) {
            System.out.print("\nEnter Discount Start Date (yyyy-MM-dd): ");
            String startDate = scanner.next();
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                start = formatter.parse(startDate);
                break;
            }
            catch (ParseException e) {
                System.out.println("Invalid Input");
                scanner.nextLine();
            }
        }

        while (true) {
            System.out.print("\nEnter Discount End Date (yyyy-MM-dd): ");
            String endDate = scanner.next();
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                end = formatter.parse(endDate);
                break;
            }
            catch (ParseException e) {
                System.out.println("Invalid Input");
                scanner.nextLine();
            }
        }

        ResponseT<serviceSaleDiscount> r = stockService.setSaleDiscount(product.getId(), discount, start, end);
        if (!r.isError()) {
            System.out.println("Sale Discount Added Successfully");
        }
        else {
            System.out.println("Sale Discount Addition Failed - " + r.getError());
        }
    }

    private void showGenerateReportMenu() {
        System.out.println("~~~~~~~~~~~~~~~~~~~~\n" +
                           "Generate Report Menu\n" +
                           "~~~~~~~~~~~~~~~~~~~~\n");
        System.out.println("1. Generate Inventory Report");
        System.out.println("2. Generate Defected Products Report");
        System.out.println("\n0. Return");
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Input: ");
            int input = scanner.nextInt();
            scanner.nextLine();
            switch(input) {
                case 1:
                    generateInventoryReport();
                    break;
                case 2:
                    generateDefectedProductsReport();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid Input");
                    continue;
            }
            break;
        }
    }

    private void generateDefectedProductsReport() {
        int input;
        ResponseT<serviceDefectedProductsReport> r = stockService.getDefectedProductUnits();
        if (!r.isError()) {
            serviceDefectedProductsReport report = r.getData();
            System.out.println(report.showReport());
            System.out.println("\n0. Return");
            while (true) {
                Scanner scanner = new Scanner(System.in);
                System.out.print("Input: ");
                input = scanner.nextInt();
                scanner.nextLine();
                if (input == 0) return;
                System.out.println("Invalid Input.");
            }
        }
        else {
            System.out.println("Error Occurred - " + r.getError());
        }
    }

    private void generateInventoryReport() {
        serviceInventoryReport report = new serviceInventoryReport();
        System.out.println(report.showReport(stockService));
    }

    private void showMarkProductUnitAsDefectedMenu() {
        serviceProduct product = showProducts();
        if (product != null) {
            int indexCounter = 1;
            ResponseT<List<serviceProductUnit>> response = stockService.getProductUnits(product.getId());
            if (response.isError()) {
                System.out.println(response.getError());
                return;
            }
            List<serviceProductUnit> units = response.getData();
            for (serviceProductUnit unit : units) {
                System.out.println(indexCounter++ + ". " + unit.getId() + " - Shelf: " +
                        unit.getLocation() + " - " + (unit.isInStorage() ? "In Storage" : "In Store") + " - "
                        + (unit.isDefected() ? "DEFECTED" : "")
                        + (unit.getExpirationDate().before(Date.from(Instant.now())) ? " (EXPIRED)" : "")
                        + " - Expiration Date: " + new SimpleDateFormat("yyyy-MM-dd").format(unit.getExpirationDate()));
            }
            int input;
            while (true) {
                Scanner scanner = new Scanner(System.in);
                System.out.print("Choose Product To Mark As Defected: ");
                input = scanner.nextInt();
                scanner.nextLine();
                if (input < 0 || input >= indexCounter) System.out.println("Invalid Input.");
                else {
                    if (input == 0) return;
                    else {
                        ResponseT<Boolean> response_1 = stockService.markProductUnitAsDefected(units.get(input - 1).getId());
                        if (!response_1.isError()) {
                            System.out.println("Item Marked As Defected Successfully.");
                        } else {
                            System.out.println(response.getError());
                        }
                        return;
                    }
                }
            }
        }
    }

    private void showChangeProductCategory() {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" +
                           "Change Product Category Menu\n" +
                           "~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");

        serviceProduct product = showProducts();
        if (product == null) return;
        System.out.println("Choose Category To Move To:");
        serviceCategory inCategory = showCategories(null);
        if (inCategory != null) {
            ResponseT<Boolean> response = stockService.changeProductCategory(product.getId(), inCategory.getCategoryId());
            if (response.isError()) System.out.println(response.getError());
            else System.out.println("Category changed successfully.");
        }
    }
}
