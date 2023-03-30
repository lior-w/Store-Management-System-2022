package DataAccessLayer.EmployeeDAL;


import DataAccessLayer.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class ShiftConstraintsDAO {
    String tableName;
    public ShiftConstraintsDAO() {
        tableName = "ShiftConstraints";
    }

    public int insertToShiftConstraints(ShiftDTO Ob) {
        Connection conn = Repository.getInstance().connect();
        if (Ob == null) return 0;
        try {
            for (String type : Ob.getConstraints().keySet()) {
                String toInsertConstraints = String.format("INSERT INTO %s \n" +
                        "VALUES %s;", "ShiftConstraints", Ob.getConstraint(type));

                Statement s;

                s = conn.createStatement();
                s.executeUpdate(toInsertConstraints);


            }
        } catch (Exception e) {
            return 0;
        }


        return 1;
    }
    public int insertToEmployeeInShift(ShiftDTO Ob) {
        Connection conn = Repository.getInstance().connect();
        if (Ob == null) return 0;
        try {
            for (int index = 0; index < Ob.getNumberOfEmpInShift(); index++) {
                String toInsert = String.format("INSERT INTO %s \n" +
                        "VALUES %s;", "EmployeesInShift", Ob.getEmployees(index));
                Statement s;

                s = conn.createStatement();
                s.executeUpdate(toInsert);

            }
        } catch (Exception e) {
            return 0;
        }

        return 1;
    }

    public int addConstraints(int ShiftID,String TypeOfEmployee, int amount)
    {
        Connection conn = Repository.getInstance().connect();
        String updateString;
        if(TypeOfEmployee == null || ShiftID < 0 || amount<0) return 0;
        updateString= String.format("INSERT INTO %s \n" +
                "VALUES (%s,\"%s\",%s);", "ShiftConstraints", ShiftID, TypeOfEmployee,amount);
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


    public int removeConstraints(int ShiftID,String TypeOfEmployee)
    {
        Connection conn = Repository.getInstance().connect();
        String updateString;
        if(TypeOfEmployee == null || ShiftID < 0 ) return 0;
        updateString= String.format("DELETE FROM %s \n" +
                "WHERE %s=%s AND %s=\"%s\"", "ShiftConstraints", "ShiftID", ShiftID,"TypeOfEmployee" ,TypeOfEmployee);
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


    public Vector<ShiftConstraintsDTO> get(int id) {
        Vector<ShiftConstraintsDTO> result = new Vector<>();
        Repository rep = Repository.getInstance();
        try {
            Statement statement = rep.connect().createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM "+ tableName+"\n"+
                    "WHERE ShiftID="+id);
            while (res.next()) {
                ShiftConstraintsDTO dto = new ShiftConstraintsDTO(res.getInt(1),
                        res.getString(2),res.getInt(3));
                result.add(dto);
            }
            return result;
        } catch (SQLException e) {
            return null;
        }
    }
}