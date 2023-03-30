package BusinessLayer.SupplierBusinessLayer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Contact {

    private final String name;
    private final String email;
    private final String phone;
    private final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[a-zA-Z][a-zA-Z0-9._]*[@][a-zA-Z]+[.][a-zA-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private final Pattern VALID_PHONE_NUMBER_REGEX = Pattern.compile("^[0][1-9]\\d{8}$", Pattern.CASE_INSENSITIVE);

    public Contact(String name, String email, String phone) throws Exception {
        this.name = name;
        if (!isValidEmail(email))
            throw new Exception(email + " is not a valid email address.");
        this.email = email;
        if (!isValidPhoneNumber(phone))
            throw new Exception(phone + " is not a valid phone number.");
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public boolean isValidEmail(String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.find();
    }

    public boolean isValidPhoneNumber(String phone) {
        Matcher matcher = VALID_PHONE_NUMBER_REGEX.matcher(phone);
        return matcher.find();
    }
}