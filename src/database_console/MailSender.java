package database_console;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author R-Mule
 */
public class MailSender {
    // File Name SendEmail.java

    public MailSender() {

    }

    public void sendMail(String subject,String content) throws AddressException, MessagingException {
         //ConfigFileReader cfr = new ConfigFileReader();

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.timeout", "1000");
        props.put("mail.smtp.connectiontimeout", "1000");
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("asmithpbe", ConfigFileReader.getMailPassword());
            }
        });

       // try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("asmithpbe@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse("asmit119@me.com"));
            message.setSubject(subject);
            message.setText(content);

            Transport.send(message);

           // System.out.println("MAIL SENT!");
      //  } catch (MessagingException e) {

        //    throw new RuntimeException(e);

      //  }
    }
}
