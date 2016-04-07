package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;

import javax.mail.*;
import java.util.Properties;

/**
 * Created by aad on 4/7/16.
 */
public class ConnectionThread implements Runnable {
    private String username, password;

    public ConnectionThread(String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
    }

    public void run() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ObservableList<String> getAllMail() {
        ObservableList<email> emails = FXCollections.observableArrayList();

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                        return new javax.mail.PasswordAuthentication(username, password);
                    }
                });




        ObservableList<String> messages2 = FXCollections.observableArrayList();

        try {

            Store store = session.getStore("pop3s");

            store.connect("smtp.gmail.com", username, password);

            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            Message[] messages = emailFolder.getMessages();



            Label messageNum = new Label("Inbox (" + messages.length + " messages)");


            //messages2.add("Inbox (" + messages.length + " messages)");

            for (int i = 0, j = messages.length; i < j; i++) {
                Message message = messages[i];
                messages2.add(String.valueOf(i+1) + ". " + message.getFrom()[0] + ": " + message.getSubject());
//                System.out.println("Text: " + message.getContent().toString());
            }


            emailFolder.close(false);
            store.close();

        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return messages2;
    }
}
