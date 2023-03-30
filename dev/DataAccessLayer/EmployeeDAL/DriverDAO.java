package DataAccessLayer.EmployeeDAL;

import BusinessLayer.EmployeeBusinessLayer.Pair;
import DataAccessLayer.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class DriverDAO extends DAO<DriverDTO> {
    private AvailableShiftForEmployeeDAO availableShiftForEmployeeDAO;
    private EmployeesSkillsDAO employeesSkillsDAO;
    public DriverDAO() {
        this.tableName = "Drivers";
        availableShiftForEmployeeDAO = new AvailableShiftForEmployeeDAO();
        employeesSkillsDAO = new EmployeesSkillsDAO();
    }

    //=====================================Employee===================================

    public int insert(DriverDTO Ob) {
        int ans = 0;
        Connection conn = Repository.getInstance().connect();
        if (Ob == null) return 0;
        String toInsertEmp = Ob.fieldsToString();
        Statement s;
        try {
            s = conn.createStatement();
            s.executeUpdate(InsertStatement(toInsertEmp));
            int resAs = insertToAvailableShifts(Ob);
            int resES = insertToEmployeeSkills(Ob);
            if (resAs + resES == 2) //If both inserts worked
                ans = 1;
            else {
                ans = 0;
            }
        } catch (Exception e) {
            ans = 0;
        }
        finally {
            Repository.getInstance().closeConnection();
        }
        return ans;
    }

    private int insertToAvailableShifts(DriverDTO Ob) {
        Connection conn = Repository.getInstance().connect();
        if (Ob == null) return 0;
        for (int index = 0; index < Ob.getNumberOfAvailableShifts(); index++) {
            String toInsertAvailableShifts = String.format("INSERT INTO %s \n" +
                    "VALUES %s;", "AvailableShiftsForEmployees", Ob.getAvailableShifts(index));
            Statement s;
            try {
                s = conn.createStatement();
                s.executeUpdate(toInsertAvailableShifts);
            } catch (Exception e) {
                return 0;
            }
            finally {
                Repository.getInstance().closeConnection();
            }
        }
        return 1;
    }

    private int insertToEmployeeSkills(DriverDTO Ob) {
        Connection conn = Repository.getInstance().connect();
        if (Ob == null) return 0;
        for (int index = 0; index < Ob.getNumberOfSkills(); index++) {
            String toInsertSkills = String.format("INSERT INTO %s \n" +
                    "VALUES %s;", "EmployeeSkills", Ob.getSkills(index));
            Statement s;
            try {
                s = conn.createStatement();
                s.executeUpdate(toInsertSkills);
            } catch (Exception e) {
                return 0;
            }
            finally {
                Repository.getInstance().closeConnection();
            }
        }
        return 1;
    }


    public DriverDTO makeDTO(String ID)  {
        DriverDTO output = null;
        Connection con =Repository.getInstance().connect();
        try {
            Statement statement = con.createStatement();
            ResultSet RS = statement.executeQuery("SELECT * FROM "+ tableName+"\n"+
                    "WHERE id="+ID);
            if (!RS.next()){
                return null;
            }
            List<String> skills = getSkillsList(ID+"", con);
            if (skills == null) {
                return null;
            }
            List<Pair<LocalDate, String>> availableShifts = getAvailableShiftList(RS.getString(3)/*id*/, con);
            if (availableShifts == null) {
                return null;
            }
            output = new DriverDTO(/*first name*/RS.getString(1), /*last name*/RS.getString(2), /*Id*/RS.getString(3),
                    /*bank account number*/RS.getString(4), /*salary*/RS.getInt(5),/*empConditions*/ RS.getString(6),
                    /*start working date*/LocalDate.parse(RS.getString(7)), skills, availableShifts,/*licencetype*/RS.getString(8));
        } catch (Exception e) {
            output = null;
        }
        finally {
            Repository.getInstance().closeConnection();
        }

        return output;
    }

    private List<Pair<LocalDate, String>> getAvailableShiftList(String empId, Connection conn) {
        List<Pair<LocalDate, String>> ans = new LinkedList<>();
        ResultSet rs = get("AvailableShiftsForEmployees", "EmpID", empId, conn);
        try {
            while (rs.next()) {
                String dateSTR = rs.getString(2);
                LocalDate date = LocalDate.parse(dateSTR);
                String type = rs.getString(3);
                Pair<LocalDate, String> p = new Pair<>(date, type);//have to check
                ans.add(p);
            }
        } catch (Exception e) {
            return null;
        }
        return ans;
    }

    private List<String> getSkillsList(String empId, Connection conn) {
        List<String> ans = new LinkedList<>();
        ResultSet rs = get("EmployeeSkills", "EmployeeID", empId, conn);
        try {
            while (rs.next()) {
                ans.add(rs.getString(2));//have to check
            }
        } catch (Exception e) {
            return null;
        }
        return ans;
    }


    public int update(DriverDTO updatedOb) throws SQLException//not allowed to change ID
    {
        Connection conn = Repository.getInstance().connect();
        if (updatedOb == null) return 0;
        String updateString = String.format("UPDATE %s" +
                        " SET \"FirstName\"= \"%s\", \"LastName\"= \"%s\" " +
                        ", \"BankAccountNumber\"=\"%s\", \"Salary\"=%s,  \"EmpConditions\"=\"%s\", \"StartWorkingDate\"=\"%s\",\"licenseType\"=\"%s\"  " +
                        "WHERE \"ID\" == \"%s\";",
                tableName, updatedOb.firstName, updatedOb.lastName,
                updatedOb.bankAccount, updatedOb.salary, updatedOb.empConditions, updatedOb.startWorkingDate,updatedOb.licence, updatedOb.id);
        Statement s;
        try {
            s = conn.createStatement();
            return s.executeUpdate(updateString);
        } catch (Exception e) {
            return 0;
        }
    }

    //=====================================EmployeeSkills===================================
    public int addSkill(String empID, String skillToAdd) {
        return employeesSkillsDAO.addSkill(empID, skillToAdd);
    }

    public int removeSkill(String empID, String skillToRemove) {
        return employeesSkillsDAO.removeSkill(empID, skillToRemove);
    }

    //=====================================AvailableShift===================================
    public int addAvailableShifts(String empID, LocalDate date, String typeOfShift) {
        return availableShiftForEmployeeDAO.addAvailableShifts(empID, date, typeOfShift);
    }

    public int removeAvailableShifts(String empID, LocalDate date, String typeOfShift) {
        return availableShiftForEmployeeDAO.removeAvailableShifts(empID, date, typeOfShift);
    }
    //================================= Helpers==========================================



}


