package Mail;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Mail{


    private final Session session;
    private String myAccountEmail, password, recepient;
    public Mail(String myAccountEmail, String password, String recepient) {

        this.myAccountEmail = myAccountEmail;
        this.password = password;
        this.recepient = recepient;
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.zoho.com");
        properties.put("mail.smtp.port", "587");

        session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myAccountEmail,password);
            }
        });
    }

    public void sendMail() throws Exception {
        System.out.println("Preparing to send email");
        Message message = this.prepareMessage(session, myAccountEmail, recepient);
        Transport.send(message);
        System.out.println("Message sent succesfully");


    }
    private Message prepareMessage(Session session, String myAccountEmail,String recepient){

        try{
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
            message.setSubject("My first from Java APP");
            message.setText("Hello Mr/Ms, \n "
                    +"\n\n\n\n"
                    + "International Sales Executive\n"
                    + "MLC Europe GmbH\n"
                    + "Tel:         +4961312109183 \n"
                    +" Email:   " + recepient+ "\n"+
                    "Web:     www.mlceurope.com");
            return message;
        }catch (Exception ex){
            Logger.getLogger(Mail.class.getName()).log(Level.SEVERE,null,ex);
        }
        return null;

    }
}
