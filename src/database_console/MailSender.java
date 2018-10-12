package database_console;

import java.util.Date;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;



/**
 * i will clean this up later..
 *
 * @author R-Mule
 */
public class MailSender {

    // File Name SendEmail.java
    private String USERNAME;
    private String PASSWORD;

    public MailSender() {
        // Email information such as from, to, subject and contents.

        USERNAME = "asmithpbe";
        PASSWORD = ConfigFileReader.getMailPassword();

        //gmail.sendMail(mailFrom, mailTo, mailSubject, mailText);
    }

    public void sendMail(String mailSubject,
            String mailText) throws Exception {

        Properties config = createConfiguration();

        // Creates a mail session. We need to supply username and
        // password for Gmail authentication.
        Session session = Session.getInstance(config, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        USERNAME,
                        PASSWORD
                );
            }
        });

        // Creates email message
        Message message = new MimeMessage(session);
        message.setSentDate(new Date());
        message.setFrom(new InternetAddress("asmithpbe@gmail.com"));
        message.setRecipient(Message.RecipientType.TO,
                new InternetAddress("asmit119@me.com"));
        message.setSubject(mailSubject);
        message.setText(mailText);

        // Send a message
        Transport.send(message, USERNAME, PASSWORD);
    }

    private Properties createConfiguration() {
        return new Properties() {
            {
                put("mail.smtp.auth", "true");
                put("mail.smtp.host", "smtp.gmail.com");
                put("mail.smtp.port", "587");
                put("mail.smtp.starttls.enable", "true");
            }
        };
    }
}
/*
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
*/
