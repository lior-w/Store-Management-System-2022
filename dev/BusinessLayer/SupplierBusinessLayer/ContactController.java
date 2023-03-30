package BusinessLayer.SupplierBusinessLayer;

import DataAccessLayer.SuppliersDAL.ContactDAO;
import java.util.List;

public class ContactController {

    private static ContactController instance = null;
    private ContactDAO contactDAO;

    private ContactController() {
        contactDAO = new ContactDAO();
    }

    public static ContactController getInstance() {
        if (instance == null) instance = new ContactController();
        return instance;
    }

    public List<Contact> getSupplierContacts(int supplierId) {
        return contactDAO.getContacts(supplierId);
    }

    public void addContactToSupplier(int supplierId, String name, String email, String phone) throws Exception {
        Contact contact = new Contact(name, email, phone);
        contactDAO.insert(contact, supplierId);
    }

    public void removeContacts(int supplierId) {
        contactDAO.delete(supplierId);
    }

    public void removeContact(int supplierId, String phone) {
        contactDAO.delete(supplierId, phone);
    }

    public void updateContact(int supplierId, String phone, String field, String content) {
        contactDAO.update(supplierId,phone, field, content);
    }
}
