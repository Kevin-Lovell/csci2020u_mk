package sample;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/*Works Cited
http://www.mkyong.com/java/javamail-api-sending-email-via-gmail-smtp-example/
*/

public class Main {

    public static void main(String[] args) {

        final String username = "csci2020utest@gmail.com";
        final String password = "thisclassisgood";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("csci2020utest@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse("mitchchd@gmail.com"));
            message.setSubject("Subject Text");
            message.setText("Text tes," + "\n\n another test");

            Transport.send(message);

            System.out.println("Message Sent Successfully");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}



