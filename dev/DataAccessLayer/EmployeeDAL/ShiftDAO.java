package DataAccessLayer.EmployeeDAL;
import BusinessLayer.EmployeeBusinessLayer.Pair;
import DataAccessLayer.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.*;
public class ShiftDAO extends EmployeeShiftDAO<ShiftDTO> {
    private ShiftConstraintsDAO shiftConstraintsDAO;
    private EmployeesInShiftDAO employeesInShiftDAO;

    public ShiftDAO() {
        this.tableName = "Shifts";
        shiftConstraintsDAO = new ShiftConstraintsDAO();
        employeesInShiftDAO = new EmployeesInShiftDAO();
    }

    //=================================shift===================================
    @Override
    public int insert(ShiftDTO Ob) {
        Connection conn = Repository.getInstance().connect();
        if (Ob == null) return 0;
        String toInsertShift = this.InsertStatement(Ob.fieldsToString());
        Statement s;
        try {
            s = conn.createStatement();
            s.executeUpdate(toInsertShift);
            int resConstraints = insertToShiftConstraints(Ob);
            int resEmpInShift = insertToEmployeeInShift(Ob);
            if (resConstraints + resEmpInShift == 2) {
                return 1;
            }
            return 0;
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public int update(ShiftDTO updatedOb)//not allowed to change ID
    {
        Connection conn = Repository.getInstance().connect();
        if (updatedOb == null) return 0;
        String updateString = String.format("UPDATE %s" +
                        " SET  Date= \"%s\" " +
                        ", TypeOfShift=\"%s\", IsSealed=%s" +
                        " WHERE ID == %s ;",
                tableName, updatedOb.date.toString(), updatedOb.shiftType,
                updatedOb.isSealed ? 1 : 0, updatedOb.Id);
        Statement s;
        try {
            s = conn.createStatement();
            return s.executeUpdate(updateString);
        } catch (Exception e) {
            return 0;
        }
    }


    public ShiftDTO makeDTO(ResultSet RS) {
        return null;
    }


    public int getShiftIdByDateAndType(LocalDate date, String type) {
        Connection conn = Repository.getInstance().connect();
        String updateString = String.format("select ID From Shifts where Date = \"%s\" AND TypeOfShift=\"%s\"", date.toString(), type);
        ResultSet rs = null;
        try {
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(updateString);
            if (rs == null) {
                return -1;
            }
            int x = rs.getInt(1);
            return x;
        } catch (Exception e) {
            return -1;
        }
        finally {
            Repository.getInstance().closeConnection();
        }
    }

    public int updateConstraint(LocalDate date, String typeOfShift, String TypeOfEmployee, int amount) {
        int ShiftID = getShiftIdByDateAndType(date, typeOfShift);
        Connection conn = Repository.getInstance().connect();
        if (TypeOfEmployee == null || ShiftID < 0 || amount < 0) return 0;
        String updateString = String.format("UPDATE %s" +
                        " SET  " +
                        "  \"Amount\"=%s" +
                        " WHERE \"ShiftID\" == %s AND \"TypeOfEmployee\" == \"%s\" ;",
                "ShiftConstraints", amount, ShiftID, TypeOfEmployee);
        Statement s;
        try {
            s = conn.createStatement();
            return s.executeUpdate(updateString);
        } catch (Exception e) {
            return 0;
        }
    }
    public int maxT( String id) {
        Repository rep = Repository.getInstance();
        int maxi = 1;
        try {
            Statement statement = rep.connect().createStatement();
            ResultSet res = statement.executeQuery("SELECT MAX(" + id + ") FROM " + tableName);
            try {
                maxi = res.getInt(1);
            } catch (Exception ignored) { }
        } catch (SQLException ignored) {}
        finally { Repository.getInstance().closeConnection(); }
        return maxi;
    }

    @Override
    public ShiftDTO makeDTO(Integer id) {
        ShiftDTO output;
        Connection con =Repository.getInstance().connect();
        try {
            Statement statement = con.createStatement();
            ResultSet RS = statement.executeQuery("SELECT * FROM "+ tableName+"\n"+
                    "WHERE id="+id);
            output = new ShiftDTO(/*id*/RS.getInt(1),/*date*/RS.getString(2),/*type*/RS.getString(3),
                    RS.getInt(4));
        } catch (Exception e) {
            output = null;
        }
        return output;
    }

    @Override
    public void addSkill(String id, String toString) {

    }

    @Override
    public void removeSkill(String id, String toString) {

    }

    @Override
    public void addAvailableShifts(String id, LocalDate first, String toString) {

    }

    @Override
    public void removeAvailableShifts(String id, LocalDate first, String toString) {

    }

    public int removeShift(int ShiftID) {
        Connection conn = Repository.getInstance().connect();
        String updateString;
        if (ShiftID < 0) return 0;
        updateString = String.format("DELETE FROM %s \n" +
                "WHERE %s=%s;", "Shifts", "ID", ShiftID);
        Statement s;
        try {
            s = conn.createStatement();
            return s.executeUpdate(updateString);
        } catch (Exception e) {
            return 0;
        }
    }

    //==================================shiftConstraints
    public int insertToShiftConstraints(ShiftDTO Ob) {
        return shiftConstraintsDAO.insertToShiftConstraints(Ob);
    }

    public int insertToEmployeeInShift(ShiftDTO Ob) {
        return shiftConstraintsDAO.insertToEmployeeInShift(Ob);
    }

    public int addConstraints(int ShiftID, String TypeOfEmployee, int amount) {
        return shiftConstraintsDAO.addConstraints(ShiftID, TypeOfEmployee, amount);
    }

    public int removeConstraints(int ShiftID, String TypeOfEmployee) {
        return shiftConstraintsDAO.removeConstraints(ShiftID, TypeOfEmployee);
    }

    //====================================empInShift

    public int addEmployeeToShift(String EmployeeID, int ShiftID, String RoleInShift) {
        return employeesInShiftDAO.addEmployeeToShift(EmployeeID, ShiftID, RoleInShift);
    }

    public int addDriverToShift(String EmployeeID, int ShiftID, String RoleInShift) {
        return employeesInShiftDAO.addDriverToShift(EmployeeID, ShiftID, RoleInShift);
    }

    public int removeEmployeeFromShift(String EmployeeID, int ShiftID, String RoleInShift) {
        return employeesInShiftDAO.removeEmployeeFromShift(EmployeeID, ShiftID, RoleInShift);
    }
    public int removeEmployeeFromShiftbyId(String EmployeeID) {
        return employeesInShiftDAO.removeEmployeeFromShiftbyid(EmployeeID);
    }

    public int removeDriverFromShift(String EmployeeID, int ShiftID, String RoleInShift) {
        return employeesInShiftDAO.removeDriverFromShift(EmployeeID, ShiftID, RoleInShift);
    }

    //==============================misc=======================================


    private List<Pair<String, String>> getcurrentShiftEmployeesList(int shiftId) {
        List<Pair<String, String>> ans = new LinkedList<>();
        Connection conn = Repository.getInstance().connect();
        ResultSet rs = getWithInt("EmployeesInShift", "ShiftID", shiftId, conn);
        try {
            while (rs.next()) {
                Pair<String, String> p;
                if (rs.getString(1) != null) //Employee is not a driver
                    p = new Pair<>(rs.getString(1), rs.getString(3)/*type of employee*/);
                else //driver
                    p = new Pair<>(rs.getString(4), rs.getString(3)/*type of employee*/);
                ans.add(p);
            }
        } catch (Exception e) {
            return null;
        }
        return ans;
    }

    private Map<String, Integer> getconstraintsList(int shiftId) {
        Connection conn = Repository.getInstance().connect();
        Map<String, Integer> ans = new HashMap<>();
        ResultSet rs = getWithInt("ShiftConstraints", "ShiftID", shiftId, conn);
        try {
            while (rs.next()) {
                ans.put(rs.getString(2)/*type of employee*/, rs.getInt(3)/*amount*/);
            }
        } catch (Exception e) {
            return null;
        }
        return ans;
    }


    public ShiftDTO gets(int id) {
        Repository rep = Repository.getInstance();
        try {
            Statement statement = rep.connect().createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM "+ tableName+"\n"+
                    "WHERE ID="+id);
            ShiftDTO dto = new ShiftDTO(res.getInt(1),res.getString(2),
                    res.getString(3),res.getInt(4));
            return dto;
        } catch (SQLException e) {
            return null;
        }
    }
}
