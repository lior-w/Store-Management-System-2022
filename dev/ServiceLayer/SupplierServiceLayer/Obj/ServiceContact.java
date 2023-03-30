package ServiceLayer.SupplierServiceLayer.Obj;

import BusinessLayer.SupplierBusinessLayer.Contact;

public class ServiceContact {

    public final String name;
    public final String email;
    public final String phone;

    public ServiceContact(Contact contact) {
        this.name = contact.getName();
        this.email = contact.getEmail();
        this.phone = contact.getPhone();
    }
}
