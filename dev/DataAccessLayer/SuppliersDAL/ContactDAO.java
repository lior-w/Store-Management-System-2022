package DataAccessLayer.SuppliersDAL;

import BusinessLayer.SupplierBusinessLayer.Contact;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class ContactDAO  extends DAO<List<Contact>> {
    private static final String tableName = "Contacts";

    public ContactDAO() {
        super(tableName);
    }

    public boolean insert(Contact contact, int supplierId) {
        Connection connection = null;
        boolean output = false;
        List<Contact> list = identityMap.retrieve(supplierId);
        if(list == null){
            list = new LinkedList<>();
        }
        list.add(contact);
        identityMap.cache(supplierId, list);
        try {
            connection = connect();
            Statement stmt = connection.createStatement();
            output = stmt.executeUpdate(String.format("INSERT INTO %s VALUES ('%d', '%s', '%s', '%s')", tableName, supplierId, contact.getPhone(), contact.getName(), contact.getEmail())) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null) closeConnection(connection);
        }
        return output;
    }

    @Override
    public boolean delete(int supplierId) {
        Connection connection = null;
        int output = 0;
        try {
            connection = connect();
            Statement stmt = connection.createStatement();
            output = stmt.executeUpdate("DELETE FROM " + tableName + " WHERE supplierId = " + supplierId + "");
            if (identityMap.contains(supplierId)) identityMap.remove(supplierId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null) closeConnection(connection);
        }
        return output > 0;
    }

    public boolean delete(int supplierId, String phone){
        Connection connection = null;
        int output = 0;
        try {
            connection = connect();
            Statement stmt = connection.createStatement();
            output = stmt.executeUpdate("DELETE FROM " + tableName + " WHERE supplierId = " + supplierId +
                    " AND phone = '" + phone + "'");
            if (identityMap.contains(supplierId)) identityMap.remove(supplierId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null) closeConnection(connection);
        }
        return output > 0;
    }
    public List<Contact> getContacts(int supplierId) {
        List<Contact> contacts = identityMap.retrieve(supplierId);
        if (contacts == null) {
            contacts = new LinkedList<>();
            Connection connection = null;
            try {
                connection = connect();
                Statement stmt = connection.createStatement();
                ResultSet result = stmt.executeQuery(String.format("Select * from Contacts where supplierId = %d", supplierId));
                while (result.next()) {
                    try {
                        contacts.add(new Contact(result.getString("name"), result.getString("email")
                                , result.getString("phone")));
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
                if (!contacts.isEmpty()) identityMap.cache(supplierId, contacts);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            finally {
                if (connection != null) closeConnection(connection);
            }
        }
        return contacts;
    }

    public <E> int update(int supplierId, String phone, String columnName, E value) {
        Connection connection = null;
        int output = 0;
        try {
            connection = connect();
            Statement stmt = connection.createStatement();
            output = stmt.executeUpdate("UPDATE " + tableName + " SET " + columnName + " = '" + value
                    + "' WHERE SupplierId = " + supplierId + " AND phone = '" + phone + "'");
            identityMap.remove(supplierId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null) closeConnection(connection);
        }
        return output;
    }



}
