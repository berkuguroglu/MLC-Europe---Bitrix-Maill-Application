package Mail;

import Database.databaseConnection;
import com.sun.mail.smtp.SMTPSendFailedException;
import javafx.concurrent.Task;
import javafx.scene.control.*;
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
    private int size, taskId;
    public static Button process;
    public static HashMap<Integer, ArrayList<Task<Void>>> queue = new HashMap<>();
    private int responsiblePerson;
    private static ArrayList<String[]> sales_team;
    private boolean allowed = false;
    private String remove;
    private ArrayList<String> dialog;
    public Mail(int responsiblePerson, String recepient, String country, int size, int taskId, ArrayList<String> dialog, String remove) throws InterruptedException, ExecutionException, SQLException {


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
             this.size = size;
             this.dialog = dialog;
             this.remove = remove;
             this.taskId = taskId;
             allowed = true;
             System.out.println(this.size);
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
            if(Objects.equals(iter[0], String.valueOf(person)))
                return iter;
        }
        return null;
    }
    @Override
    protected Void call() throws Exception {


        if(!this.isDone() && !this.isCancelled() && allowed) {


            Random r = new Random();
            System.out.println("Preparing to send email");
            try {
                session = Session.getInstance(properties, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        PasswordAuthentication ps = new PasswordAuthentication(myAccountEmail, password);
                        return ps;
                    }
                });
                Message m = this.prepareMessage(session, myAccountEmail, recepient, country);
                Transport.send(m);
                if(Mail.this.taskId != -1) {
                    System.out.println(Mail.this.size + " " + Mail.queue.get(Mail.this.taskId).size());
                    if (Mail.queue.get(Mail.this.taskId).size() == Mail.this.size) {
                        System.out.println("Task completed");
                        dialog.remove(remove);
                    }
                }
                this.succeeded();

            }
            catch (SMTPSendFailedException ex)
            {
                ex.printStackTrace();
                this.failed();
            }
            catch (AuthenticationFailedException | AddressException ex)
            {

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
                    "As you know, MLC Europe GmbH in Germany is an international distributor of LCDs, diodes, relays, connectors, etc. The parts we help procure can be electronic, electrical and mechanical in nature as well. Our vast supply chain of vendors ensures that any time your regular suppliers cannot deliver the requested quantity on time, we will be ready to help save the situation. \n" +
                    "\n" +
                    "All I require is the part number and quantity, and I will be happy to source these components for you.\n" +
                    "\n" +
                    "Please do not hesitate to contact me in case of any questions, comments or requirements.\n" +
                    "\n" +
                    "\n" +
                    "Thank you.\n\n");
        }
        public static void putTeam() throws InterruptedException, ExecutionException, SQLException {

            databaseConnection db = new Database.databaseConnection();
            db.openConnection();
            Mail.sales_team = db.getSalesTeam();
            putMap();
        }


        static String produceMessage(String name, String country, String recep, String myAccount)
        {


            String result =
            "Hello, \n\n"
            + "I would like to talk about the needs of electronic components within your organization. My name is " + name.split("-", 5)[1].trim() + "."
            + content.get(country)
            +"\n"
            + name.split("-", 5)[1].trim() + "\n"
            + "International Sales Executive\n"
            + "MLC Europe GmbH\n\n\n"
            + "Tel:     +4961312109183\n"
            + "Email:   " + myAccount + "\n"
            + "Web:     www.mlceurope.com";
            return result;
        }

    }
}
