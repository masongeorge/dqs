package Helpers;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailHandler {
    private String user = "lc12dqs@gmail.com";
    private String pass = "lc12dqs123";

    public void SendEmail(String to, String subject, String body) {
        Properties props = System.getProperties();
        String host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", user);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(user));

            InternetAddress toAddress = new InternetAddress(to);
            message.addRecipient(Message.RecipientType.TO, toAddress);

            message.setSubject(subject);
            message.setContent(body, "text/html");
            Transport transport = session.getTransport("smtp");
            transport.connect(host, user, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        }
        catch (AddressException ae) {
            ae.printStackTrace();
        }
        catch (MessagingException me) {
            me.printStackTrace();
        }
    }

    public void SendRegisterInfo(String Name, String Email, String Password) {
        String message = "";
        message += "Thank you ";
        message += Name;
        message += " for creating an account at LC 12.</br></br>";
        message += "Your email is: <b>";
        message += Email;
        message += "</b></br>";
        message += "Your password is: <b>";
        message += Password;
        message += "</b></br></br>";
        message += "LC 12 Administrator";

        this.SendEmail(Email, "Thank you for creating an account at LC 12", message);
    }
}
