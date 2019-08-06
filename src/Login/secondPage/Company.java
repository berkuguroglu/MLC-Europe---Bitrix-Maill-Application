package Login.secondPage;

import com.google.gson.JsonObject;

import java.util.ArrayList;

public class Company
{

   static ArrayList<JsonObject> list = new ArrayList<>();
   static ArrayList<Company> company_objects = new ArrayList<>();
   private ArrayList<String> mails;
   private String company_name;
   private int responsible_person;
   public Company(String company_name, int responsible_person)
   {
       mails = new ArrayList<>();

   }



}
