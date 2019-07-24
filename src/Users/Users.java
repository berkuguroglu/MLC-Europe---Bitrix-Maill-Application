package Users;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Users {

   static private Users CURRENT_USER;
   private ArrayList<String[]> customers;
   private String USER_NAME;
   private String USER_PASS;
   private int USER_ID;
   public Users(int USER_ID, String USER_NAME, String USER_PASS)
   {
       Users.CURRENT_USER = this;
       this.USER_NAME = USER_NAME;
       this.USER_PASS = USER_PASS;
       this.USER_ID = USER_ID;

   }

}
