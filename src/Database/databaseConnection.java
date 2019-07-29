package Database;


import Users.Users;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.*;

public class databaseConnection {


    private static databaseConnection databaseObject = null;
    private Connection con;
    private Statement st;
    private ResultSet rs;
    private String server_adress = "160.153.18.110";
    private String username = "mailappadmin";
    private String password = "admin";
    private String databasename = "mailapp";
    private Thread database_thread;
    private boolean operation;



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

        try
        {
            this.con = DriverManager.getConnection("jdbc:mysql://"+server_adress+"/"+databasename, username, password);
            st = con.createStatement();
            return true;
        }
        catch (SQLException ex)
        {
            return false;
        }

    }
    public void checkForUsers(String username, String pass) throws InterruptedException {

        this.operation = true;
        this.database_thread = new Thread(new Runnable() {

            @Override
            public void run() {


                if(operation) {

                    try {

                        String query = "SELECT * FROM USERS_ADMIN WHERE username =" + "'" + username + "'" + " AND " + "pass=" + "'" + pass + "'";
                        System.out.println("check");
                        rs = st.executeQuery(query);
                        boolean flag = false;
                        while (rs.next()) {

                            new Users(rs.getInt("id"), username, pass);
                            flag = true;
                        }
                        if(!flag)
                        {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.getButtonTypes().setAll(new ButtonType("Try Again"));
                                    alert.setTitle("Login Error");
                                    alert.setHeaderText("");
                                    alert.setContentText("Your password or username is wrong.");
                                    alert.showAndWait();
                                }
                            });

                        }

                    } catch (Exception ex) {
                        System.out.print(ex);

                    }
                }

            }
        });
        this.database_thread.start();




    }

}
