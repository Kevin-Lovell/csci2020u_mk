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
    private ObservableList<email> emails = FXCollections.observableArrayList();

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

    //String host = "smtp.gmail.com";
        String host = "pop.gmail.com";
        Properties props = new Properties();
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.smtp.host", "smtp.gmail.com");
//        props.put("mail.smtp.port", "587");

//        props.put("mail.pop3.host", host);
//        props.put("mail.pop3.port", "465");
//        props.put("mail.pop3.starttls.enable", "true");
//        Session emailSession = Session.getDefaultInstance(props);

        props.setProperty("mail.store.protocol", "imaps");
//
//        Session session = Session.getInstance(props,
//                new javax.mail.Authenticator() {
//                    protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
//                        return new javax.mail.PasswordAuthentication(username, password);
//                    }
//                });

    host = "imap.gmail.com";


        ObservableList<String> messages2 = FXCollections.observableArrayList();

        try {

//            Store store = session.getStore("pop3s");

            Session session = Session.getInstance(props, null);
            Store store = session.getStore();

            store.connect(host, username, password);

            Folder emailFolder = store.getFolder("SENT");
            emailFolder.open(Folder.READ_ONLY);

            Message[] messages = emailFolder.getMessages();



            Label messageNum = new Label("Inbox (" + messages.length + " messages)");


            messages2.add("Inbox (" + messages.length + " messages)");

            for (int i = 0, j = messages.length; i < j; i++) {
                Message message = messages[i];
                emails.add(new email(message.getSentDate(), message.getFrom()[0], message.getSubject(), message));
                messages2.add(String.valueOf(i+1) + ". " + message.getFrom()[0] + ": " + message.getSubject());
//                System.out.println("Text: " + message.getContent().toString());
            }

            for(email e: emails) {
                try {
                    System.out.println(e.getDate().toString()+ " " + e.getAddressFrom()+ " " + e.getSubject());
                    Object content = e.getMessage().getContent();
                    if (content instanceof String)
                    {
                        String body = (String)content;
                        System.out.println("CONTENT:" + body);

                    }
                    else if (content instanceof Multipart)
                    {
                        Multipart mp = (Multipart) e.getMessage().getContent();
                        BodyPart bp = mp.getBodyPart(0);
                        System.out.println("CONTENT:" + bp.getContent());
                    }

                } catch (Exception mex) {
                    mex.printStackTrace();
                }
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
