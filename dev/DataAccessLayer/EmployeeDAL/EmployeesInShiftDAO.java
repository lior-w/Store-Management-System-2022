package DataAccessLayer.EmployeeDAL;

import DataAccessLayer.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class EmployeesInShiftDAO {
    String tableName;
    public EmployeesInShiftDAO() {
        this.tableName  = "EmployeesInShift";
    }
    public int addEmployeeToShift(String EmployeeID,int ShiftID ,String RoleInShift)
    {
        Connection conn = Repository.getInstance().connect();
        String updateString;
        if(EmployeeID == null || ShiftID < 0 || RoleInShift == null) return 0;
        updateString= String.format("INSERT INTO %s \n" +
                "VALUES (\"%s\",%s,\"%s\",%s);", "EmployeesInShift", EmployeeID, ShiftID,RoleInShift, null);
        Statement s;
        try
        {
            s = conn.createStatement();
            return s.executeUpdate(updateString);
        }
        catch (Exception e ){
            return 0;
        }
    }
    public int addDriverToShift(String EmployeeID,int ShiftID ,String RoleInShift)
    {
        Connection conn = Repository.getInstance().connect();
        String updateString;
        if(EmployeeID == null || ShiftID < 0 || RoleInShift == null) return 0;
        updateString= String.format("INSERT INTO %s \n" +
                "VALUES (%s,%s,\"%s\",\"%s\");", "EmployeesInShift",null, ShiftID,RoleInShift,  EmployeeID);
        Statement s;
        try
        {
            s = conn.createStatement();
            return s.executeUpdate(updateString);
        }
        catch (Exception e ){
            return 0;
        }
    }


    public int removeEmployeeFromShift( String EmployeeID,int ShiftID,String RoleInShift)
    {
        Connection conn = Repository.getInstance().connect();
        String updateString;
        if(EmployeeID == null || ShiftID < 0 || RoleInShift==null) return 0;
        updateString= String.format("DELETE FROM %s \n" +
                "WHERE %s=\"%s\" AND %s=%s AND %s=\"%s\";", "EmployeesInShift", "EmployeeID", EmployeeID,"ShiftID" ,ShiftID, "RoleInShift", RoleInShift);
        Statement s;
        try
        {
            s = conn.createStatement();
            return s.executeUpdate(updateString);
        }
        catch (Exception e )
        {
            return 0;
        }

    }
    public int removeDriverFromShift( String EmployeeID,int ShiftID,String RoleInShift)
    {
        Connection conn = Repository.getInstance().connect();
        String updateString;
        if(EmployeeID == null || ShiftID < 0 || RoleInShift==null) return 0;
        updateString= String.format("DELETE FROM %s \n" +
                "WHERE %s=\"%s\" AND %s=%s AND %s=\"%s\";", "EmployeesInShift", "DriverID", EmployeeID,"ShiftID" ,ShiftID, "RoleInShift", RoleInShift);
        Statement s;
        try
        {
            s = conn.createStatement();
            return s.executeUpdate(updateString);
        }
        catch (Exception e )
        {
            return 0;
        }

    }
    public boolean getStoreKeeper(int shiftId) {
        Repository rep = Repository.getInstance();
        try {
            Statement statement = rep.connect().createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM EmployeesInShift" + "\n" +
                    "WHERE ShiftID=" + shiftId);
            while (res.next()) {
                if(res.getString(3).equals("Storekeeper"))
                    return true;
            }
            return false;
        } catch (SQLException e) {
            return false;
        }
        finally {
            Repository.getInstance().closeConnection();
        }
    }
    public boolean getDriver(int shiftId) {
        Repository rep = Repository.getInstance();
        try {
            Statement statement = rep.connect().createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM EmployeesInShift" + "\n" +
                    "WHERE ShiftID=" + shiftId);
            while (res.next()) {
                if(res.getString(3).equals("Driver"))
                    return true;
            }
            return false;
        } catch (SQLException e) {
            return false;
        }
        finally {
            Repository.getInstance().closeConnection();
        }
    }

    public List<String> getDriverFromShift(int shiftId) {
        ArrayList<String> ids = new ArrayList<>();
        Repository rep = Repository.getInstance();
        try {
            Statement statement = rep.connect().createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM EmployeesInShift" + "\n" +
                    "WHERE ShiftID=" + shiftId);
            while (res.next()) {
                if(res.getString(3).equals("Driver"))
                    ids.add(res.getString("EmployeeID"));
            }
            return ids;
        } catch (SQLException e) {
            return null;
        }
        finally {
            Repository.getInstance().closeConnection();
        }
    }

    public Vector<EmployeesInShiftDTO> gets(int id) {
        Vector<EmployeesInShiftDTO> result = new Vector<>();
        Repository rep = Repository.getInstance();
        try {
            Statement statement = rep.connect().createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM "+ tableName+"\n"+
                    "WHERE ShiftID="+id);
            while (res.next()) {
                EmployeesInShiftDTO dto = new EmployeesInShiftDTO(res.getString(1),
                        res.getInt(2),res.getString(3),res.getString(4));
                result.add(dto);
            }
            return result;
        } catch (SQLException e) {
            return null;
        }
    }


    public int removeEmployeeFromShiftbyid( String EmployeeID)
    {
        Connection conn = Repository.getInstance().connect();
        String updateString;
        if(EmployeeID == null ) return 0;
        updateString= String.format("DELETE FROM %s \n" +
                "WHERE %s=\"%s\"", "EmployeesInShift", "EmployeeID", EmployeeID);
        Statement s;
        try
        {
            s = conn.createStatement();
            return s.executeUpdate(updateString);
        }
        catch (Exception e )
        {
            return 0;
        }

    }
}
