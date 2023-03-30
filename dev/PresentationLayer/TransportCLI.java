package PresentationLayer;

import BusinessLayer.EmployeeBusinessLayer.Role;
import BusinessLayer.TransportBusinessLayer.ProductTransport;
import DataAccessLayer.Repository;
import ServiceLayer.Response;
import ServiceLayer.ResponseT;
import BusinessLayer.EmployeeBusinessLayer.Pair;
import ServiceLayer.TransportServiceLayer.Objects.SSite;
import ServiceLayer.TransportServiceLayer.Objects.STransport;
import ServiceLayer.TransportServiceLayer.Objects.STransportDocument;
import ServiceLayer.TransportServiceLayer.Objects.SVehicle;
import ServiceLayer.TransportServiceLayer.TransportService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TransportCLI {
    TransportService ts;
    Scanner s;
    List<Role> roles;
    public TransportCLI(List<Role> roles) {
        ts = TransportService.getInstance();
        s = new Scanner(System.in);
        //ts.loadData();
        this.roles = roles;
    }
    private boolean validate(int size,String[] arr){
        if(arr.length!=size)
            System.out.println("invalid input");
        return arr.length==size;
    }
    public void start() {
        if(roles.contains(Role.LogisticManager)){
            logisticManagerMenu();
        }
        else if(roles.contains(Role.Manager)){
            managerMenu();
        }
        else if(roles.contains(Role.HRManager)){
            HRmanagerMenu();
        }
        else {
            System.out.println("No Actions Allowed.");
        }

    }
    private void logisticManagerMenu(){
        System.out.println("Welcome to Transport System!");
        int chooseFirst = 1;
        while (chooseFirst != 0) {
            System.out.println("Please choose an option:\n" +
                    "1.Transports.\n2.Transport Documents.\n3.Logistic" +
                    "\n4.Status\n(!)Press 0 to exit the System");
            chooseFirst = Integer.parseInt(s.nextLine());
            if (chooseFirst == 1) {
                System.out.println("1.Create Transport.\n2.Update Transport.\n3.Remove Transport\n4.Search Transport" +
                        "\n5.All Transports\n(!)Press 0 to return to main menu");
                int choose = Integer.parseInt(s.nextLine());
                if (choose == 1) {
                    System.out.println("Please enter date of transport, time of departure,truck license number," +
                            "driver ID,sites ID's (seperated by comma), transport documents ID's (seperated by comma) and shipping area.\n" +
                            "For example: 21/05/1978 12:00 1234569 20589745 41,12,32 South");
                    String[] input = s.nextLine().split(" ");
                    if(!validate(6,input))
                        continue;
                    Date date;
                    while (true) {
                        try {
                            date = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(input[0] + " " + input[1]);
                            break;
                        }
                        catch (Exception e) {
                            System.out.println("Invalid Input.");
                        }
                    }
                    String time = input[1];
                    String licenseNumber = input[2];
                    String driverID = input[3];
//                    String[] sources = input[4].split(",");
                    String[] destOrder = input[5].split(",");
                    String shippingArea = input[6];
                    ResponseT<STransport> trans = ts.createTransport(date, time, licenseNumber, driverID, new String[]{"999"}, destOrder, shippingArea);
                    System.out.println(trans.getError());
                } else if (choose == 2) {
                    System.out.println("1.Start Transport.\n2.Receive Order\n3.Finish Transport.\n4.Change Driver" +
                            "\n5.Change Vehicle\n6.Change Driver And Vehicle\n7.Add Transport Document\n" +
                            "8.Remove Transport Document\n(!)Press 0 to return to main menu");
                    choose = Integer.parseInt(s.nextLine());
                    if (choose == 1) {
                        System.out.println("Please enter transport ID:");
                        int input = Integer.parseInt(s.nextLine());
                        Response res = ts.startTransport(input);
                        System.out.println(res.getError());
                    } else if (choose == 2) {
                        int location=-1;
                        Date expirationDate = new Date();
                        boolean isInStorage = false;
                        System.out.println("Please enter transport ID:");
                        int input = Integer.parseInt(s.nextLine());
                        ResponseT<HashMap<Integer, List<Pair<ProductTransport, Integer>>>> res = ts.receiveOrder(input);
                        for (Integer docId : res.getData().keySet()) {
                            System.out.println(String.format("For Order %d:", docId));
                            for (Pair<ProductTransport, Integer> p : res.getData().get(docId)) {
                                System.out.println(String.format("For Product: %d, %s:\nEnter the qunatity " +
                                        "of defected products (Press 0 if None):", p.first.getID(), p.first.getName()));
                                Scanner scanner = new Scanner(System.in);
                                int def = scanner.nextInt();
                                int newQuan = p.second - def;
                                if (def > 0) {
                                    Response result = ts.updateProducts(docId, p.first.getID(), newQuan);
                                    if (result.getError().equals("Transport Document already Done")) {
                                        System.out.println("Transport already finished");
                                        continue;
                                    }
                                }
                                System.out.print("\nEnter Expiration Date (yyyy-MM-dd): ");
                                scanner = new Scanner(System.in);
                                String expDate = scanner.next();
                                try {
                                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                                    expirationDate = formatter.parse(expDate);
                                } catch (ParseException e) {
                                    System.out.println("Invalid Input");
                                    scanner.nextLine();
                                }
                                System.out.print("\nEnter Shelf Number: ");
                                scanner = new Scanner(System.in);
                                try {
                                    location = scanner.nextInt();
                                    if (location < 0) throw new Exception();
                                }
                                catch (Exception e) {
                                    System.out.println("Invalid Input");
                                    scanner.nextLine();
                                }
                                scanner = new Scanner(System.in);
                                System.out.print("\nIs The Item In Storage? (y/n)\nInput: ");
                                String inStorage = scanner.next();
                                if (inStorage.equals("y")) {
                                    isInStorage = true;
                                }
                                else if (inStorage.equals("n")) {
                                    isInStorage = false;
                                }
                                else System.out.println("Invalid Input");
                                ts.receiveProduct(p.first.getID(), newQuan, expirationDate,
                                        isInStorage, location, docId);
                            }
                        }
                    } else if (choose == 3) {
                        System.out.println("Please enter transport ID:");
                        int input = Integer.parseInt(s.nextLine());
                        Response res = ts.finishTransport(input);
                        System.out.println(res.getError());

                    } else if (choose == 4) {
                        System.out.println("Please enter transport ID and new driver ID:");
                        String[] input = s.nextLine().split(" ");
                        Response res = ts.changeDriver(Integer.parseInt(input[0]), input[1]);
                        System.out.println(res.getError());
                    } else if (choose == 5) {
                        System.out.println("Please enter transport ID and new vehicle license plate:");
                        String[] input = s.nextLine().split(" ");
                        Response res = ts.changeVehicle(Integer.parseInt(input[0]), input[1]);
                        System.out.println(res.getError());
                    } else if (choose == 6) {
                        System.out.println("Please enter transport ID, driver ID and the new vehicle license plate:");
                        String[] input = s.nextLine().split(" ");
                        Response res = ts.changeDriverAndVehicle(Integer.parseInt(input[0]), input[1], input[2]);
                        System.out.println(res.getError());
                    }else if (choose == 7) {
                        System.out.println("Please enter transport ID and the Transport Document ID:");
                        String[] input = s.nextLine().split(" ");
                        Response res = ts.addTransportDocToTransport(Integer.parseInt(input[0]), Integer.parseInt(input[1]));
                        System.out.println(res.getError());
                    } else if (choose == 8) {
                        System.out.println("Please enter transport ID and the Transport Document ID:");
                        String[] input = s.nextLine().split(" ");
                        Response res = ts.removeTransportDocFromTransport(Integer.parseInt(input[0]), Integer.parseInt(input[1]));
                        System.out.println(res.getError());
                    } else if (choose == 0) {
                        continue;
                    }
                } else if (choose == 3) {
                    System.out.println("Please enter transport ID:");
                    String input = s.nextLine();
                    Response res = ts.deleteTransport(Integer.parseInt(input));
                    System.out.println(res.getError());
                } else if (choose == 4) {
                    System.out.println("Please enter transport ID:");
                    String input = s.nextLine();
                    ResponseT<String> res = ts.searchTransport(Integer.parseInt(input));
                    if (res.getData() == null)
                        System.out.println(res.getError());
                    else System.out.println(res.getData());
                }
                else if (choose == 5) {
                    ResponseT<String> res = ts.allTransports();
                    if (res.getData() == null)
                        System.out.println(res.getError());
                    else System.out.println(res.getData());
                }else if (choose == 0) {
                    continue;
                }
            } else if (chooseFirst == 2) {
                System.out.println("1.Awaiting Transport Documents\n2.Search Transport Document" +
                        "\n3.All Transport Documents\n(!)Press 0 to return to main menu");
                int choose = Integer.parseInt(s.nextLine());
                if (choose == 1) {
                    ResponseT<String> res = ts.getAwaitingTransportDocs();
                    if (res.getData() == null)
                        System.out.println(res.getError());
                    else System.out.println(res.getData());
                } else if (choose == 2) {
                    System.out.println("Please enter Transport Document ID:");
                    int input = Integer.parseInt(s.nextLine());
                    ResponseT<String> res = ts.getTransportDocDetails(input);
                    if (res.getData() == null)
                        System.out.println(res.getError());
                    else System.out.println(res.getData());
                }
                else if (choose == 3) {
                    ResponseT<String> res = ts.transportDocsDetails();
                    if (res.getData() == null)
                        System.out.println(res.getError());
                    else System.out.println(res.getData());
                }
            } else if (chooseFirst == 3) {
                System.out.println("1.Drivers.\n2.Sites.\n3.Vehicle\n(!)Press 0 to return to main menu");
                int choose = Integer.parseInt(s.nextLine());
                if (choose == 1) {
                    System.out.println("1.Drivers Details\n(!)Press 0 to return to main menu");
                    int input = Integer.parseInt(s.nextLine());
                    if (input == 1) {
                        ResponseT<String> res = ts.getDriversDetails();
                        System.out.println(res.getData());
                    }

                } else if (choose == 2) {
                    System.out.println("1.Sites Details\n(!)Press 0 to return to main menu");
                    int input = Integer.parseInt(s.nextLine());
                    if (input == 1) {
                        ResponseT<String> res = ts.getSitesDetails();
                        System.out.println(res.getData());
                    }
                } else if (choose == 3) {
                    System.out.println("1.Add Vehicle.\n2.Remove Vehicle\n3.Update vehicle max weight\n4.Vehicles Details\n(!)Press 0 to return to main menu");
                    int input = Integer.parseInt(s.nextLine());
                    if (input == 1) {
                        System.out.println("Please enter license number, license type, net weight, max weight and model:");
                        String[] inputs = s.nextLine().split(" ");
                        ResponseT<SVehicle> res = ts.addVehicle(inputs[0], inputs[1], Integer.parseInt(inputs[2]),
                                Integer.parseInt(inputs[3]));
                        System.out.println(res.getError());
                    } else if (input == 2) {
                        System.out.println("Please enter vehicle license plate:");
                        String input1 = s.nextLine();
                        Response res = ts.removeVehicle(input1);
                        System.out.println(res.getError());
                    } else if (input == 3) {
                        System.out.println("Please enter vehicle id and new max weight:");
                        String[] inputs = s.nextLine().split(" ");
                        ResponseT<SVehicle> res = ts.updateVehicleMaxWeight(inputs[0], Integer.parseInt(inputs[1]));
                        System.out.println(res.getError());
                    }
                    else if (input == 4) {
                        ResponseT<String> res = ts.getVehiclesDetails();
                        System.out.println(res.getData());
                    }
                }
            } else if (chooseFirst == 4) {
                System.out.println("1.Current Status.\n2.Status by dates\n(!)Press 0 to return to main menu");
                int input = Integer.parseInt(s.nextLine());
                if (input == 1) {
                    ResponseT<String> res = ts.currentStatus();
                    if (res.getData() == null)
                        System.out.println(res.getError());
                    else System.out.println(res.getData());
                } else if (input == 2) {
                    System.out.println("Please enter begin date and end date:\n" +
                            "Example: 21/01/2013 14/02/2013");
                    String[] dates = s.nextLine().split(" ");
                    Date begin;
                    Date end;
                    while (true) {
                        try {
                            begin = new SimpleDateFormat("dd/MM/yyyy").parse(dates[0]);
                            end = new SimpleDateFormat("dd/MM/yyyy").parse(dates[1]);
                            break;
                        } catch (ParseException e) {
                            System.out.println("Invalid Input");
                        }
                    }
                    ResponseT<String> res = ts.statusByDates(begin, end);
                    if (res.getData() == null)
                        System.out.println(res.getError());
                    else System.out.println(res.getData());
                }
            }
        }
    }

    private void managerMenu(){
        System.out.println("Welcome to Transport System!");
        int chooseFirst = 1;
        while (chooseFirst != 0) {
            System.out.println("Please choose an option:\n" +
                    "1.Transports.\n2.Transport Documents.\n3.Logistic" +
                    "\n4.Status\n(!)Press 0 to exit the System");
            chooseFirst = Integer.parseInt(s.nextLine());
            if (chooseFirst == 1) {
                System.out.println("1.Search Transport\n2.All Transports\n(!)Press 0 to return to main menu");
                int choose = Integer.parseInt(s.nextLine());
                if (choose == 1) {
                    System.out.println("Please enter transport ID:");
                    String input = s.nextLine();
                    ResponseT<String> res = ts.searchTransport(Integer.parseInt(input));
                    if (res.getData() == null)
                        System.out.println(res.getError());
                    else System.out.println(res.getData());
                }
                else if (choose == 2) {
                    ResponseT<String> res = ts.allTransports();
                    if (res.getData() == null)
                        System.out.println(res.getError());
                    else System.out.println(res.getData());
                }else if (choose == 0) {
                    continue;
                }
            } else if (chooseFirst == 2) {
                System.out.println("1.Awaiting Transport Documents\n2.Search Transport Document" +
                        "\n3.All Transport Documents\n(!)Press 0 to return to main menu");
                int choose = Integer.parseInt(s.nextLine());
                if (choose == 1) {
                    ResponseT<String> res = ts.getAwaitingTransportDocs();
                    if (res.getData() == null)
                        System.out.println(res.getError());
                    else System.out.println(res.getData());
                } else if (choose == 2) {
                    System.out.println("Please enter Transport Document ID:");
                    int input = Integer.parseInt(s.nextLine());
                    ResponseT<String> res = ts.getTransportDocDetails(input);
                    if (res.getData() == null)
                        System.out.println(res.getError());
                    else System.out.println(res.getData());
                }
                else if (choose == 3) {
                    ResponseT<String> res = ts.transportDocsDetails();
                    if (res.getData() == null)
                        System.out.println(res.getError());
                    else System.out.println(res.getData());
                }
            } else if (chooseFirst == 3) {
                System.out.println("1.Drivers.\n2.Sites.\n3.Vehicle\n(!)Press 0 to return to main menu");
                int choose = Integer.parseInt(s.nextLine());
                if (choose == 1) {
                    System.out.println("1.Drivers Details\n(!)Press 0 to return to main menu");
                    int input = Integer.parseInt(s.nextLine());
                    if (input == 1) {
                        ResponseT<String> res = ts.getDriversDetails();
                        System.out.println(res.getData());
                    }

                } else if (choose == 2) {
                    System.out.println("1.Sites Details\n(!)Press 0 to return to main menu");
                    int input = Integer.parseInt(s.nextLine());
                    if (input == 1) {
                        ResponseT<String> res = ts.getSitesDetails();
                        System.out.println(res.getData());
                    }
                } else if (choose == 3) {
                    System.out.println("1.Vehicles Details\n(!)Press 0 to return to main menu");
                    int input = Integer.parseInt(s.nextLine());
                    if (input == 1) {
                        ResponseT<String> res = ts.getVehiclesDetails();
                        System.out.println(res.getData());
                    }
                }
            } else if (chooseFirst == 4) {
                System.out.println("1.Current Status.\n2.Status by dates\n(!)Press 0 to return to main menu");
                int input = Integer.parseInt(s.nextLine());
                if (input == 1) {
                    ResponseT<String> res = ts.currentStatus();
                    if (res.getData() == null)
                        System.out.println(res.getError());
                    else System.out.println(res.getData());
                } else if (input == 2) {
                    System.out.println("Please enter begin date and end date:\n" +
                            "Example: 21/01/2013 14/02/2013");
                    String[] dates = s.nextLine().split(" ");
                    Date begin;
                    Date end;
                    while (true) {
                        try {
                            begin = new SimpleDateFormat("dd/MM/yyyy").parse(dates[0]);
                            end = new SimpleDateFormat("dd/MM/yyyy").parse(dates[1]);
                            break;
                        } catch (ParseException e) {
                            System.out.println("Invalid Input");
                        }
                    }
                    ResponseT<String> res = ts.statusByDates(begin, end);
                    if (res.getData() == null)
                        System.out.println(res.getError());
                    else System.out.println(res.getData());
                }
            }
        }
        Repository.getInstance().closeConnection();
    }

    private void HRmanagerMenu(){
        System.out.println("Welcome to Transport System!");
        int chooseFirst = 1;
        while (chooseFirst != 0) {
            System.out.println("Please choose an option:\n" +
                    "1.Transports.\n2.Transport Documents.\n3.Logistic" +
                    "\n4.Status\n(!)Press 0 to exit the System");
            chooseFirst = Integer.parseInt(s.nextLine());
            if (chooseFirst == 1) {
                System.out.println("1.Search Transport\n2.All Transports\n(!)Press 0 to return to main menu");
                int choose = Integer.parseInt(s.nextLine());
                if (choose == 1) {
                    System.out.println("Please enter transport ID:");
                    String input = s.nextLine();
                    ResponseT<String> res = ts.searchTransport(Integer.parseInt(input));
                    if (res.getData() == null)
                        System.out.println(res.getError());
                    else System.out.println(res.getData());
                }
                else if (choose == 2) {
                    ResponseT<String> res = ts.allTransports();
                    if (res.getData() == null)
                        System.out.println(res.getError());
                    else System.out.println(res.getData());
                }else if (choose == 0) {
                    continue;
                }
            } else if (chooseFirst == 2) {
                System.out.println("1.Awaiting Transport Documents\n2.Search Transport Document" +
                        "\n3.All Transport Documents\n(!)Press 0 to return to main menu");
                int choose = Integer.parseInt(s.nextLine());
                if (choose == 1) {
                    ResponseT<String> res = ts.getAwaitingTransportDocs();
                    if (res.getData() == null)
                        System.out.println(res.getError());
                    else System.out.println(res.getData());
                } else if (choose == 2) {
                    System.out.println("Please enter Transport Document ID:");
                    int input = Integer.parseInt(s.nextLine());
                    ResponseT<String> res = ts.getTransportDocDetails(input);
                    if (res.getData() == null)
                        System.out.println(res.getError());
                    else System.out.println(res.getData());
                }
                else if (choose == 3) {
                    ResponseT<String> res = ts.transportDocsDetails();
                    if (res.getData() == null)
                        System.out.println(res.getError());
                    else System.out.println(res.getData());
                }
            } else if (chooseFirst == 3) {
                System.out.println("1.Drivers.\n2.Sites.\n3.Vehicle\n(!)Press 0 to return to main menu");
                int choose = Integer.parseInt(s.nextLine());
                if (choose == 1) {
                    System.out.println("1.Drivers Details\n(!)Press 0 to return to main menu");
                    int input = Integer.parseInt(s.nextLine());
                    if (input == 1) {
                        ResponseT<String> res = ts.getDriversDetails();
                        System.out.println(res.getData());
                    }

                } else if (choose == 2) {
                    System.out.println("1.Sites Details\n(!)Press 0 to return to main menu");
                    int input = Integer.parseInt(s.nextLine());
                    if (input == 1) {
                        ResponseT<String> res = ts.getSitesDetails();
                        System.out.println(res.getData());
                    }
                } else if (choose == 3) {
                    System.out.println("1.Vehicles Details\n(!)Press 0 to return to main menu");
                    int input = Integer.parseInt(s.nextLine());
                    if (input == 1) {
                        ResponseT<String> res = ts.getVehiclesDetails();
                        System.out.println(res.getData());
                    }
                }
            } else if (chooseFirst == 4) {
                System.out.println("1.Current Status.\n2.Status by dates\n(!)Press 0 to return to main menu");
                int input = Integer.parseInt(s.nextLine());
                if (input == 1) {
                    ResponseT<String> res = ts.currentStatus();
                    if (res.getData() == null)
                        System.out.println(res.getError());
                    else System.out.println(res.getData());
                } else if (input == 2) {
                    System.out.println("Please enter begin date and end date:\n" +
                            "Example: 21/01/2013 14/02/2013");
                    String[] dates = s.nextLine().split(" ");
                    Date begin;
                    Date end;
                    while (true) {
                        try {
                            begin = new SimpleDateFormat("dd/MM/yyyy").parse(dates[0]);
                            end = new SimpleDateFormat("dd/MM/yyyy").parse(dates[1]);
                            break;
                        } catch (ParseException e) {
                            System.out.println("Invalid Input");
                        }
                    }
                    ResponseT<String> res = ts.statusByDates(begin, end);
                    if (res.getData() == null)
                        System.out.println(res.getError());
                    else System.out.println(res.getData());
                }
            }
        }
    }
}