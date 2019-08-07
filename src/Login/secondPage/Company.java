package Login.secondPage;

import Login.Bitrix.bitrixAPI;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import restRequest.request;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class Company
{

   private boolean state;
   public static ArrayList<Company> list = new ArrayList<Company>();
   private ArrayList<SimpleStringProperty> mails;
   private SimpleStringProperty ID;
   public SimpleStringProperty  companyName;
   private SimpleStringProperty responsiblePerson;
   public Company(int ID, String company_name, int responsible_person, ArrayList<String[]> data) throws IOException {
       this.ID = new SimpleStringProperty(String.valueOf(ID));
       this.companyName = new SimpleStringProperty(company_name);
       this.state = true;
       mails = new ArrayList<>();
       Platform.runLater(new Runnable() {
           @Override
           public void run() {
               for(String[] idx : data)
               {
                   if(idx[0].equals(String.valueOf(responsible_person))) {
                       setResponsiblePerson(idx[1]);
                       Company.list.add(Company.this);

                   }
               }
           }
       });
   }
   public String getCompanyName()
   {
       return this.companyName.get();
   }
   public String getID()
   {
       return this.ID.get();
   }
   public String getResponsiblePerson()
   {
       return this.responsiblePerson.get();
   }
   public void setResponsiblePerson(String value)
   {
       this.responsiblePerson = new SimpleStringProperty(value);
   }


}
