package DataAccessLayer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Repository {
    public final static String url = "jdbc:sqlite:adss_v02.db";
    private static Connection c;
    private static Repository instance = null;

    private Repository() {
        //createTables();
    }

    public static Repository getInstance() {
        if (instance == null)
            instance = new Repository();
        return instance;
    }

    public Connection connect() {
        try {
            if(c==null) {
                c = DriverManager.getConnection("jdbc:sqlite:adss_v02.db");
            }
            return c;
        } catch (SQLException e) {
        }
        return c;
    }

    public void closeConnection() {
        try {
            if (c!=null) {
                if(!c.isClosed())
                    c.close();
                c=null;
            }
        } catch (SQLException e) {
        }
    }
    public List<Integer> getIds(String query){
        ResultSet rs = null;
        Statement stmt=null;
        ArrayList<Integer> Ids = new ArrayList<>();
        Connection con = connect();
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        if (rs == null) return null;
        try {
            while (rs.next())
                Ids.add(rs.getInt("ID"));
            stmt.close();
        } catch (SQLException e) {
        }
        finally {
            closeConnection();
        }
        return Ids;
    }
//    private void createTables() {
//        String SitesTable = "CREATE TABLE IF NOT EXISTS \"Sites\" (\n" +
//                "\t\"id\"\tTEXT,\n" +
//                "\t\"address\"\tTEXT,\n" +
//                "\t\"contactName\"\tTEXT,\n" +
//                "\t\"contactNumber\"\tTEXT,\n" +
//                "\t\"area\"\tTEXT,\n" +
//                "\tPRIMARY KEY(\"address\")\n"
//                +");";
//
//        String VehiclesTable = "CREATE TABLE IF NOT EXISTS \"Vehicles\" (\n" +
//                "\t\"numberPlate\"\tTEXT,\n" +
//                "\t\"licenseType\"\tTEXT,\n" +
//                "\t\"netWeight\"\tINTEGER,\n" +
//                "\t\"maxWeight\"\tINTEGER,\n" +
//                "\t\"kind\"\tTEXT,\n" +
//                "\tPRIMARY KEY(\"numberPlate\")\n"
//                +");";
//
//        String TransportsTable = "CREATE TABLE IF NOT EXISTS \"Transports\" (\n" +
//                "\t\"transportId\"\tINTEGER,\n" +
//                "\t\"DateOfTransport\"\tTEXT,\n" +
//                "\t\"timeOfDeparture\"\tTEXT,\n" +
//                "\t\"vehicleLicensePlate\"\tTEXT,\n" +
//                "\t\"driverId\"\tTEXT,\n" +
//                "\t\"area\"\tTEXT NOT NULL,\n" +
//                "\t\"status\"\tTEXT NOT NULL,\n" +
//                "\tPRIMARY KEY(\"transportId\"),\n" +
//                "\tFOREIGN KEY(\"driverId\") REFERENCES \"Drivers\"(\"id\"),\n" +
//                "\tFOREIGN KEY(\"vehicleLicensePlate\") REFERENCES \"Vehicles\"(\"licensePlate\")\n"
//                +");";
//
//
//        String DriversTable = "CREATE TABLE IF NOT EXISTS \"Drivers\" (\n" +
//                "\t\"FirstName\"\tTEXT,\n" +
//                "\t\"LastName\"\tTEXT,\n" +
//                "\t\"ID\"\tTEXT,\n" +
//                "\t\"BankAccountNumber\"\tTEXT,\n" +
//                "\t\"Salary\"\tINTEGER,\n" +
//                "\t\"EmpConditions\"\tTEXT,\n" +
//                "\t\"StartWorkingDate\"\tDATE,\n" +
//                "\t\"licenseType\"\tTEXT,\n" +
//                "\tPRIMARY KEY(\"ID\")\n" +
//                ");";
//
//        String ProductsTable = "CREATE TABLE IF NOT EXISTS \"Products\" (\n" +
//                "\t\"id\"\tINTEGER,\n" +
//                "\t\"name\"\tTEXT,\n" +
//                "\t\"weight\"\tINTEGER,\n" +
//                "\tPRIMARY KEY(\"id\")\n"
//                +");";
//
//        String TransportDocTable = "CREATE TABLE IF NOT EXISTS \"TransportDocuments\" (\n" +
//                "\t\"id\"\tINTEGER ,\n" +
//                "\t\"asPlanned\"\tTEXT,\n" +
//                "\t\"siteId\"\tINTEGER ,\n" +
//                "\t\"assignedTransport\"\tINTEGER ,\n"+
//                "\t\"status\"\tTEXT ,\n"+
//                "\tPRIMARY KEY(\"id\")\n"
//                +");";
//
//        String TransportDocProductsTable = "CREATE TABLE IF NOT EXISTS \"TransportDocumentsProducts\" (\n" +
//                "\t\"transportDocumentId\"\tINTEGER,\n" +
//                "\t\"productId\"\tINTEGER,\n" +
//                "\t\"quantity\"\tINTEGER,\n" +
//                "\tPRIMARY KEY(\"transportDocumentId\",\"productId\")\n" +
//                "\tFOREIGN KEY(\"transportDocumentId\") REFERENCES \"TransportDocuments\"(\"id\") ON DELETE CASCADE ,\n" +
//                "\tFOREIGN KEY(\"productId\") REFERENCES \"Products\"(\"id\") ON DELETE CASCADE \n"
//                +");";
//
//        String TransportsSitesTable = "CREATE TABLE IF NOT EXISTS \"TransportsSites\" (\n" +
//                "\t\"siteId\"\tINTEGER,\n" +
//                "\t\"transportId\"\tINTEGER,\n" +
//                "\tPRIMARY KEY(\"siteId\",\"transportId\"),\n" +
//                "\tFOREIGN KEY(\"siteId\") REFERENCES \"Sites\"(\"id\") ON DELETE CASCADE,\n" +
//                "\tFOREIGN KEY(\"transportId\") REFERENCES \"Transports\"(\"id\") ON DELETE CASCADE\n" +
//                ");";
//
//        String transDocToTransportTable = "CREATE TABLE IF NOT EXISTS \"TransDocToTransport\" (\n" +
//                "\t\"transportId\"\tINTEGER,\n" +
//                "\t\"transDocId\"\tINTEGER,\n" +
//                "\tPRIMARY KEY(\"transportId\",\"transDocId\")\n" +
//                "\tCONSTRAINT \"transId_fk\"\n FOREIGN KEY(\"transportId\") REFERENCES \"Transports\"(\"transportId\") ON DELETE CASCADE,\n" +
//                "\tCONSTRAINT \"transDocId_fk\"\n FOREIGN KEY(\"transDocId\") REFERENCES \"TransportDocuments\"(\"id\") ON DELETE CASCADE\n" +
//                ");";
//
//        String EmployeesTable = "CREATE TABLE IF NOT EXISTS \"Employees\" (\n" +
//                "\t\"FirstName\"\tTEXT,\n" +
//                "\t\"LastName\"\tTEXT,\n" +
//                "\t\"ID\"\tTEXT,\n" +
//                "\t\"BankAccountNumber\"\tTEXT,\n" +
//                "\t\"Salary\"\tINTEGER,\n" +
//                "\t\"EmpConditions\"\tTEXT,\n" +
//                "\t\"StartWorkingDate\"\tDATE,\n" +
//                "\tPRIMARY KEY(\"ID\")\n" +
//                ");";
//
//        String ShiftsTable = "CREATE TABLE IF NOT EXISTS \"Shifts\" (\n" +
//                "\t\"ID\"\tINTEGER PRIMARY KEY ,\n" +
//                "\t\"Date\"\tDate,\n" +
//                "\t\"TypeOfShift\"\tTEXT,\n" +
//                "\t\"IsSealed\"\tINTEGER\n" +
//                ");";
//
//        String AvailableShiftsForEmployees = "CREATE TABLE IF NOT EXISTS \"AvailableShiftsForEmployees\" (\n" +
//                "\t\"EmpID\"\tTEXT,\n" +
//                "\t\"Date\"\tDATE,\n" +
//                "\t\"Type\"\tTEXT,\n" +
//                "\t\"DriverID\"\tTEXT,\n" +
//                "\tPRIMARY KEY(\"EmpID\",\"Date\",\"Type\"),\n" +
//                "\tFOREIGN KEY(\"EmpID\") REFERENCES \"Employees\"(\"ID\") ON DELETE CASCADE\n" +
//                "\tFOREIGN KEY(\"DriverID\") REFERENCES \"Drivers\"(\"ID\") ON DELETE CASCADE\n" +
//                ");";
//
//        String EmployeesInShiftTable = "CREATE TABLE IF NOT EXISTS \"EmployeesInShift\" (\n" +
//                "\t\"EmployeeID\"\tTEXT,\n" +
//                "\t\"ShiftID\"\tINTEGER,\n" +
//                "\t\"RoleInShift\"\tTEXT,\n" +
//                "\t\"DriverID\"\tTEXT,\n" +
//                "\tFOREIGN KEY(\"EmployeeID\") REFERENCES \"Employees\"(\"ID\") ON DELETE CASCADE,\n" +
//                "\tFOREIGN KEY(\"ShiftID\") REFERENCES \"Shifts\"(\"ID\") ON DELETE CASCADE \n" +
//                "\tFOREIGN KEY(\"DriverID\") REFERENCES \"Drivers\"(\"ID\") ON DELETE CASCADE \n" +
//                ");";
//
//        String EmployeeSkillsTable = "CREATE TABLE IF NOT EXISTS \"EmployeeSkills\" (\n" +
//                "\t\"EmployeeID\"\tTEXT,\n" +
//                "\t\"TypeOfEmployee\"\tTEXT,\n" +
//                "\t\"DriverID\"\tTEXT,\n" +
//                "\tPRIMARY KEY(\"EmployeeID\",\"TypeOfEmployee\"),\n" +
//                "\tFOREIGN KEY(\"EmployeeID\") REFERENCES \"Employees\"(\"ID\") ON DELETE CASCADE\n" +
//                "\tFOREIGN KEY(\"DriverID\") REFERENCES \"Drivers\"(\"ID\") ON DELETE CASCADE\n" +
//                ");";
//
//        String ShiftConstraintsTable = "CREATE TABLE IF NOT EXISTS \"ShiftConstraints\" (\n" +
//                "\t\"ShiftID\"\tINTEGER,\n" +
//                "\t\"TypeOfEmployee\"\tTEXT,\n" +
//                "\t\"Amount\"\tINTEGER,\n" +
//                "\tPRIMARY KEY(\"ShiftID\",\"TypeOfEmployee\"),\n" +
//                "\tFOREIGN KEY(\"ShiftID\") REFERENCES \"Shifts\"(\"ID\") ON DELETE CASCADE\n"
//                +");";
//
//
//        Connection conn = connect();
//        if (conn == null) return;
//        try {
//            Statement stmt = conn.createStatement();
//            stmt.execute(SitesTable);
//            stmt.execute(VehiclesTable);
//            stmt.execute(DriversTable);
//            stmt.execute(TransportsTable);
//            stmt.execute(ProductsTable);
//            stmt.execute(TransportDocTable);
//            stmt.execute(TransportDocProductsTable);
//            stmt.execute(TransportsSitesTable);
//            stmt.execute(transDocToTransportTable);
//            stmt.execute(EmployeesTable);
//            stmt.execute(ShiftsTable);
//            stmt.execute(AvailableShiftsForEmployees);
//            stmt.execute(EmployeesInShiftTable);
//            stmt.execute(EmployeeSkillsTable);
//            stmt.execute(ShiftConstraintsTable);
//        } catch (SQLException exception) {
//            exception.printStackTrace();
//        } finally {
//            closeConnection();
//        }
//    }

}