package DataAccessLayer.EmployeeDAL;

import DataAccessLayer.Repository;

import java.time.LocalDate;
import java.util.List;
import java.sql.*;
import java.util.ArrayList;


public abstract class EmployeeShiftDAO<T> {
    protected String tableName;

    public abstract int insert(T Ob);

    public abstract int update(T updatedOb);


    public abstract T makeDTO(Integer id);

    public int delete(String colName,String value)
    {
        String DELETE_SQL=String.format("Delete From %s WHERE %s=\"%s\"",tableName,colName,value);
        int rowsAffected=-1;
        Connection con= Repository.getInstance().connect();
        try {
            Statement stmt=con.createStatement();
            rowsAffected=stmt.executeUpdate(DELETE_SQL);
        } catch (SQLException e) {
        }
        return rowsAffected;
    }
    protected String InsertStatement(String Values) {
        return String.format("INSERT INTO %s \n" +
                "VALUES %s;", tableName, Values);
    }

    public ResultSet get(String nameOfTable, String colName, String value, Connection con) {
        String SELECT_SQL = String.format("SELECT * FROM %s WHERE \"%s\"=\"%s\"", nameOfTable, colName, value);
        ResultSet rs = null;
        try {
            Statement stmt = con.createStatement();
            rs = stmt.executeQuery(SELECT_SQL);
        } catch (SQLException e) {
        }

        return rs;
    }


    protected ResultSet getWithInt(String nameOfTable, String colName, int value, Connection con) {
        String SELECT_SQL = String.format("SELECT * FROM %s WHERE %s=%s", nameOfTable, colName, value);
        ResultSet rs = null;
        try {
            Statement stmt = con.createStatement();
            rs = stmt.executeQuery(SELECT_SQL);
        } catch (SQLException e) {
        }
        return rs;
    }

    protected ResultSet get2int(String nameOfTable, String colName1, int value1, String colName2, int value2, Connection con) {
        String SELECT_SQL = String.format("SELECT * FROM %s WHERE %s=%d AND %s=%d", nameOfTable, colName1, value1, colName2, value2);
        ResultSet rs = null;
        try {
            Statement stmt = con.createStatement();
            rs = stmt.executeQuery(SELECT_SQL);
        } catch (SQLException e) {
        }
        return rs;
    }

    public abstract void addSkill(String id, String toString);

    public abstract void removeSkill(String id, String toString);

    public abstract void addAvailableShifts(String id, LocalDate first, String toString);

    public abstract void removeAvailableShifts(String id, LocalDate first, String toString);
}

