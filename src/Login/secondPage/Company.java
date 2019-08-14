package Login.secondPage;

import Database.databaseConnection;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
   private SimpleStringProperty stater;
   private SimpleStringProperty status;
   private SimpleStringProperty date;
   private int respPersonID;
   public Company(int ID, String company_name, int responsible_person, String phone_number, JsonArray email, ArrayList<String[]> data, String state, String status, boolean newone, String date) throws IOException {

       this.ID = new SimpleStringProperty(String.valueOf(ID));
       this.companyName = new SimpleStringProperty(company_name);
       this.state = true;
       this.respPersonID = responsible_person;
       this.status = new SimpleStringProperty(status);
       this.stater = new SimpleStringProperty(state);
       this.date = new SimpleStringProperty(date);


       if(email != null)
       {
           this.email = new SimpleStringProperty(email.get(0).getAsJsonObject().get("VALUE").getAsString());

       }
       Platform.runLater(new Runnable() {
           @Override
           public void run() {
               for(String[] idx : data)
               {
                   if(idx[0].equals(String.valueOf(responsible_person))) {
                       setResponsiblePerson(idx[1]);
                       Company.list.add(Company.this);

                       if(email != null && newone) {
                           try {
                               databaseConnection db = new databaseConnection();
                               db.openConnection();
                               db.saveCompanies(Integer.parseInt(Company.this.getID()), Company.this.getCompanyName(), String.valueOf(Company.this.respPersonID), Company.this.getCountry(), Company.this.getStater(), Company.this.getStatus(), email.get(0).getAsJsonObject().get("VALUE").getAsString(), "Template");
                           } catch (ExecutionException e) {
                               e.printStackTrace();
                           } catch (InterruptedException e) {
                               e.printStackTrace();
                           }
                       }


                   }
               }

           }
       });

       String[] result = phone_number.split(" ", 2);
       setCountry(country_codes.getOrDefault(result[0], "Unknown"));

   }
    public Company(int ID, String company_name, int responsible_person, ArrayList<String[]> data, String email, String state, String status, String date, String country) throws IOException {

        this.ID = new SimpleStringProperty(String.valueOf(ID));
        this.companyName = new SimpleStringProperty(company_name);
        this.state = true;
        this.respPersonID = responsible_person;
        this.status = new SimpleStringProperty(status);
        this.stater = new SimpleStringProperty(state);
        this.date = new SimpleStringProperty(date);
        this.email = new SimpleStringProperty(email);
        this.country = new SimpleStringProperty(country);
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
   public String getStatus() {return this.status.get(); }
   public String getStater() {return this.stater.get(); }
   public void setStatus(String s) {this.status.set(s);}
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
   public void setDate(String value)
   {
       this.date = new SimpleStringProperty(value);
   }
   public String getDate()
   {
       return this.date.get();
   }
   public String getEmail()
   {
       return this.email.get();
   }
   public int getRespPersonID() {return this.respPersonID; }
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
