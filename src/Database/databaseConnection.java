package Database;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class databaseConnection {


    private static databaseConnection databaseObject = null;
    private Connection con;
    private Statement st;
    private ResultSet rs;
    private String server_adress = "160.153.18.110";
    private String username = "mailappadmin";
    private String password = "admin";
    private String databasename = "mailapp";


    public databaseConnection()
    {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseConnection.databaseObject = this;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }
    public boolean openConnection()
    {
        return false;
    }
    public void checkForUsers(String username, String pass)
    {
        try
        {

            String query = "SELECT * FROM USERS_ADMIN WHERE username =" + username + "AND" + "pass=" + pass;
            rs = st.executeQuery(query) ;
            while(rs.next())
            {


            }

        }catch(Exception ex) {
            System.out.print(ex);

        }


    }
}
