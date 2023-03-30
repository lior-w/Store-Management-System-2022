package DataAccessLayer.EmployeeDAL;

import DataAccessLayer.Repository;
import DataAccessLayer.TransportDAL.TransportsSitesDTO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class MessegeDAO {
    public int addMessege(String empID, String messegeToAdd)
    {
        Connection conn = Repository.getInstance().connect();
        String updateString;
        if(empID == null || messegeToAdd == null) return 0;
        updateString= String.format("INSERT INTO %s \n" +
                "VALUES (\"%s\",\"%s\");", "Messages", empID, messegeToAdd);
        Statement s;
        try
        {
            s = conn.createStatement();
            return s.executeUpdate(updateString);
        }
        catch (Exception e ){
            return 0;
        }
        finally {
            Repository.getInstance().closeConnection();
        }
    }
    public int removeMeseege(String empID)
    {
        Connection conn = Repository.getInstance().connect();
        String updateString;
        if(empID == null ) return 0;
        updateString= String.format("DELETE FROM %s \n" +
                "WHERE %s=\"%s\";", "Messages", "role", empID);
        Statement s;
        try
        {
            s = conn.createStatement();
            return s.executeUpdate(updateString);
        }
        catch (Exception e ){
            return 0;
        }finally {
            Repository.getInstance().closeConnection();
        }

    }
    public Vector<MessegeDTO> get(String id){
        Vector<MessegeDTO> result = new Vector<>();
        Repository rep = Repository.getInstance();
        try {
            Statement statement = rep.connect().createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM "+ "Messages"+"\n"+
                    "WHERE role="+"'"+id+"'");
            while (res.next()) {
                MessegeDTO dto = new MessegeDTO(""+res.getString(1),
                        ""+res.getString(2));
                result.add(dto);
            }
            removeMeseege(id);
            return result;
        } catch (SQLException e) {
            return null;
        }
        finally {
            rep.closeConnection();
        }
    }
}