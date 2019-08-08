package Login.secondPage;

import Login.Bitrix.bitrixAPI;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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
   private ArrayList<String> mails;
   private SimpleStringProperty ID;
   private static HashMap<String, String> country_codes = new HashMap<>();
   public SimpleStringProperty  companyName;
   private SimpleStringProperty responsiblePerson;
   private SimpleStringProperty country;
   private SimpleStringProperty email;
   public Company(int ID, String company_name, int responsible_person, String phone_number, JsonArray email, ArrayList<String[]> data) throws IOException {

       this.ID = new SimpleStringProperty(String.valueOf(ID));
       this.companyName = new SimpleStringProperty(company_name);
       this.state = true;
       if(email != null) this.email = new SimpleStringProperty(email.get(0).getAsJsonObject().get("VALUE").getAsString());
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
       Platform.runLater(new Runnable() {
           @Override
           public void run() {
               String[] result = phone_number.split(" ", 2);
               System.out.println(result[0]);
               setCountry(country_codes.getOrDefault(result[0], "Unknown"));
           }
       });
   }
   public String getCompanyName()
   {
       return this.companyName.get();
   }
   public String getCountry()
   {
       return this.country.get();
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
   public void setCountry(String value)
   {
       this.country = new SimpleStringProperty(value);
   }
   public String getEmail()
   {
       return this.email.get();
   }
   public static void setCountries()
   {
       country_codes.put("+90", "Turkey");
       country_codes.put("+91", "India");
       country_codes.put("+49", "Germany");
       country_codes.put("+33", "France");
       country_codes.put("+30", "Greece");
       country_codes.put("+1", "USA or Canada");
       country_codes.put("+1", "Bulgaria");
       country_codes.put("+46", "Sweden");
       country_codes.put("+31", "Netherlands");
       country_codes.put("+44", "United Kingdom");
       country_codes.put("+43", "Austria");

   }


}
