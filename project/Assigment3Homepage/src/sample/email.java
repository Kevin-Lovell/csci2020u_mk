package sample;

import javax.mail.Address;
import javax.mail.Message;
import java.util.Date;
import java.util.Objects;

/**
 * Created by aad on 4/7/16.
 */
public class email {
    private String subject;
    private Date date;
    private Address addressFrom;
    private Message message;

    public email(Date date, Address address, String subject, Message message) {
        this.setAddressFrom(address);
        this.setDate(date);
        this.setMessage(message);
        this.setSubject(subject);
    }

    public String getSubject() {
        return  subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Address getAddressFrom() {
        return addressFrom;
    }

    public void setAddressFrom(Address addressFrom) {
        this.addressFrom = addressFrom;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}