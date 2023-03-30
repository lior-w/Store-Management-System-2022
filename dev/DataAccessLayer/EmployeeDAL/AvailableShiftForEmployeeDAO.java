package DataAccessLayer.EmployeeDAL;

import DataAccessLayer.Repository;

import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;

public class AvailableShiftForEmployeeDAO {
    public int addAvailableShifts(String empID, LocalDate date, String typeOfShift) {
        Connection conn = Repository.getInstance().connect();
        String updateString;
        if (empID == null || date == null || typeOfShift == null) return 0;
        updateString = String.format("INSERT INTO %s \n" +
                "VALUES (\"%s\",\"%s\",\"%s\",%s);", "AvailableShiftsForEmployees", empID, date.toString(), typeOfShift, null);
        Statement s;
        try {
            s = conn.createStatement();
            return s.executeUpdate(updateString);
        } catch (Exception e) {
            return 0;
        }
        finally {
            if (conn != null) Repository.getInstance().closeConnection();
        }
    }

    public int removeAvailableShifts(String empID, LocalDate date, String typeOfShift) {
        Connection conn = Repository.getInstance().connect();
        String updateString;
        if (empID == null || date == null || typeOfShift == null) return 0;
        updateString = String.format("DELETE FROM %s \n" +
                "WHERE %s=\"%s\" AND %s=\"%s\" AND %s=\"%s\";", "AvailableShiftsForEmployees", "EmpID", empID, "Date", date, "Type", typeOfShift);
        Statement s;
        try {
            s = conn.createStatement();
            return s.executeUpdate(updateString);
        } catch (Exception e) {
            return 0;
        }
        finally {
            if (conn != null) Repository.getInstance().closeConnection();
        }
    }
}
