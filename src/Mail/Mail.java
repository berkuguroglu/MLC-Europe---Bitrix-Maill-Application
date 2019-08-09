package Mail;

import javafx.concurrent.Task;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.ExecutionException;


public class Mail extends Task<Void> {


    private final Session session;
    protected String myAccountEmail, password, recepient;
    private Properties properties;
    private String country;
    private int limit = 10;
    public Mail(String myAccountEmail, String password, String recepient, String country) throws InterruptedException, ExecutionException, SQLException {

        this.myAccountEmail = myAccountEmail;
        this.password = password;
        this.recepient = recepient;
        this.country = country;
        properties = new Properties();
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
    private Message prepareMessage(Session session, String myAccountEmail, String recepient, String country){

        try{

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
            message.setSubject("MLC Europe");
            message.setText(Templates.produceMessage("MLC Europe", country, recepient, this.myAccountEmail));
            return message;
        }catch (Exception ex){
            System.out.println(ex);
        }
        return null;

    }

    @Override
    protected Void call() throws Exception {


        if(!this.isDone() && !this.isCancelled()) {


            System.out.println("Preparing to send email");
            Message message = this.prepareMessage(session, myAccountEmail, recepient, country);
            Transport.send(message);
            System.out.println("Message sent succesfully");
            this.succeeded();

        }
        return null;
    }

    public static class Templates
    {


        static HashMap<String, String> content = new HashMap<>();
        public static void putMap()
        {
            content.put("English", "bla bla bla bla");
        }


        static String produceMessage(String title, String country, String recep, String myAccount)
        {
            String[] split;
            split = recep.split("@", 2);
            String result =
            "Hello Mr/Ms, " + split[0].toUpperCase() + "\n"
            + content.get(country)
            +"\n\n\n\n"
            + "International Sales Executive\n"
            + "MLC Europe GmbH\n\n\n"
            + "Tel:     +4961312109183\n"
            + "Email:   " + myAccount + "\n"
            + "Web:     www.mlceurope.com";
            return result;
        }

    }
}
