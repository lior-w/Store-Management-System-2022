package DataAccessLayer.SuppliersDAL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class OthersDAO extends DAO<String> {
    private static final String tableName = "Others";

    public OthersDAO() {
        super(tableName);
    }

    public int getInt(String value) {
        int res = 0;
        Connection connection = null;
        try {
            connection = connect();
            Statement stmt = connection.createStatement();
            String query= String.format("Select intVal from Others where key = '%s'", value);
            ResultSet result = stmt.executeQuery(query);
            while (result.next()) {
                res = result.getInt("intVal");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null) closeConnection(connection);
        }

        return res;
    }

    public String getStr(String value) {
        String res = "";
        Connection connection = null;
        try {
            connection = connect();
            Statement stmt = connection.createStatement();
            ResultSet result = stmt.executeQuery(String.format("Select textVal from Others where key = %s", value));
            while (result.next()) {
                res = result.getString("textVal");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            closeConnection(connection);
        }

        return res;
    }
    public int updateInt(String key, int value) {
        Connection connection = null;
        int output = 0;
        try {
            connection = connect();
            Statement stmt = connection.createStatement();
            String query = "UPDATE " + tableName + " SET intVal = " + value + " WHERE key = '" + key + "'";
            output = stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            assert connection != null;
            closeConnection(connection);
        }
        return output;
    }

    public int updateStr(String key, String value) {
        Connection connection = null;
        int output = 0;
        try {
            connection = connect();
            Statement stmt = connection.createStatement();
            output = stmt.executeUpdate("UPDATE " + tableName +
                    " SET textVal = '" + value + "' WHERE key = " + key);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            closeConnection(connection);
        }
        return output;
    }
}
