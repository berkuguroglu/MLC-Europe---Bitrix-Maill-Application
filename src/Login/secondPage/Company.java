package Login.secondPage;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.beans.property.SimpleStringProperty;
import restRequest.request;

import java.io.IOException;
import java.util.ArrayList;

public class Company
{

   private boolean state;
   public static ArrayList<Company> list = new ArrayList<Company>();
   private final String AUTH = "https://mlcomponents.bitrix24.com/rest/12/b6pt3a9mlu6prvpl";
   private String thirdMethod = "/user.get?id=";
   private ArrayList<SimpleStringProperty> mails;
   private SimpleStringProperty ID;
   public SimpleStringProperty  companyName;
   private SimpleStringProperty responsiblePerson;
   public Company(int ID, String company_name, int responsible_person) throws IOException {
       this.ID = new SimpleStringProperty(String.valueOf(ID));
       this.companyName = new SimpleStringProperty(company_name);
       this.state = true;
       mails = new ArrayList<>();
       request r = new request(AUTH + thirdMethod + responsible_person);
       JsonObject obj = (JsonObject) new JsonParser().parse(r.getResult());
       this.responsiblePerson = new SimpleStringProperty(obj.get("result").getAsJsonArray().get(0).getAsJsonObject().get("NAME").getAsString() + " " + obj.get("result").getAsJsonArray().get(0).getAsJsonObject().get("LAST_NAME").getAsString());


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





}
