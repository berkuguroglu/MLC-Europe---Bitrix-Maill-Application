package Mail;

import Database.databaseConnection;
import com.sun.mail.util.MailConnectException;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import javax.activation.DataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import static Mail.Mail.Templates.*;


public class Mail extends Task<Void> {


    private  Session session = null;
    protected String myAccountEmail, password, recepient;
    private Properties properties;
    private String country;
    private String salesname;
    private int responsiblePerson;
    private static ArrayList<String[]> sales_team;
    private boolean allowed = false;
    public Mail(int responsiblePerson, String recepient, String country) throws InterruptedException, ExecutionException, SQLException {


        this.responsiblePerson = responsiblePerson;
        this.recepient = recepient;
        this.country = country;
        properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.zoho.com");
        properties.put("mail.smtp.port", "587");
        String[] result = findResp(this.responsiblePerson);
         if(result != null) {

             this.myAccountEmail = result[3];
             this.password = result[2];
             this.salesname = result[1];
             allowed = true;
         }

    }
    private Message prepareMessage(Session session, String myAccountEmail, String recepient, String country){

        try{

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
            message.setSubject("MLC Europe");

            Multipart multipart = new MimeMultipart();

            String filename = getClass().getResource("MLC_Europe_GmbH_Catalog.pdf").getPath();
            String filetwo = getClass().getResource("MLC_Europe_GmbH_Line_Card.pdf").getPath();
            DataSource source =  new FileDataSource(filename);

            BodyPart content = new MimeBodyPart();
            content.setText(produceMessage(this.salesname, country, recepient, this.myAccountEmail));

            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);
            multipart.addBodyPart(messageBodyPart);
            BodyPart secondBodyPart = new MimeBodyPart();
            multipart.addBodyPart(content);


            source = new FileDataSource(filetwo);
            secondBodyPart.setDataHandler(new DataHandler(source));
            secondBodyPart.setFileName(filetwo);
            multipart.addBodyPart(secondBodyPart);

            message.setContent(multipart);
            return message;
        }catch (Exception ex){
            System.out.println(ex);
        }
        return null;

    }
    public String[] findResp(int person)
    {
        for(String[] iter : sales_team)
        {
            if(Objects.equals(iter[0], String.valueOf(this.responsiblePerson)))
                return iter;
        }
        return null;
    }
    @Override
    protected Void call() throws Exception {


        if(!this.isDone() && !this.isCancelled() && allowed) {


            Random r = new Random();
            Thread.sleep(r.nextInt(300));
            System.out.println("Preparing to send email");
            System.out.println(myAccountEmail + " " + recepient);
            try {
                session = Session.getInstance(properties, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        PasswordAuthentication ps = new PasswordAuthentication(myAccountEmail, password);
                        return ps;
                    }
                });
                Message m = this.prepareMessage(session, myAccountEmail, "it31@mlceurope.com", country);
                Transport.send(m);
                this.succeeded();

            }
            catch (AuthenticationFailedException | AddressException ex)
            {

                System.out.println(myAccountEmail);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setHeaderText("Look, there is an error!");
                alert.setContentText("Authenticatin failed for the user " + myAccountEmail + "\n Check it on ZOHO Control Panel.");
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                ex.printStackTrace(pw);
                String exceptionText = sw.toString();
                Label label = new Label("The exception stacktrace was:");
                TextArea textArea = new TextArea(exceptionText);
                textArea.setEditable(false);
                textArea.setWrapText(true);
                textArea.setMaxWidth(Double.MAX_VALUE);
                textArea.setMaxHeight(Double.MAX_VALUE);
                GridPane.setVgrow(textArea, Priority.ALWAYS);
                GridPane.setHgrow(textArea, Priority.ALWAYS);
                GridPane expContent = new GridPane();
                expContent.setMaxWidth(Double.MAX_VALUE);
                expContent.add(label, 0, 0);
                expContent.add(textArea, 0, 1);
                alert.getDialogPane().setExpandableContent(expContent);
                alert.show();
                this.failed();


            } catch (Exception exception)
            {
                exception.printStackTrace();
                System.out.println(myAccountEmail);
                this.failed();
            }


        }
        return null;
    }

    public static class Templates
    {


        static HashMap<String, String> content = new HashMap<>();
        public static void putMap()
        {
            content.put("English", "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "MLC Europe GmbH in Germany is an international distributor of hard to find and obsolete electronic components such as semiconductors, diodes, transistors, circuits, transformers, relays, connectors, etc. The parts we help procure can be electronic, electrical and mechanical in nature as well. Our vast supply chain of vendors ensures that any time your regular suppliers cannot deliver the requested quantity on time, we will be ready to help save the situation.\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "All I require is the part number and quantity, and I will be happy to source these components for you.\n" +
                    "We always rigorously test the components we procure on your behalf to ensure the authenticity and functionality of the parts and provide you with a guarantee of at least 45 days as well.\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "Please do not hesitate to contact me in case of any questions, comments or requirements.\n" +
                    "\n" +
                    "\n" +
                    "Thank you. ");
        }
        public static void putTeam() throws InterruptedException, ExecutionException, SQLException {
            databaseConnection db = new Database.databaseConnection();
            db.openConnection();
            Mail.sales_team = db.getSalesTeam();
            putMap();
        }


        static String produceMessage(String name, String country, String recep, String myAccount)
        {
            String[] split;
            split = recep.split("@", 2);
            String result =
            "Hello Mr/Ms " + split[0].toUpperCase() + ",\n\n"
            + "Thank you for taking time to talk to me earlier. My name, once more, is " + name
            + content.get(country)
            +"\n\n"
            + name + "\n"
            + "International Sales Executive\n"
            + "MLC Europe GmbH\n\n\n"
            + "Tel:     +4961312109183\n"
            + "Email:   " + myAccount + "\n"
            + "Web:     www.mlceurope.com";
            return result;
        }

    }
}
