package Database;


import Users.Users;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

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
    private boolean logged = false;
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
    public ArrayList<String[]> getSalesTeam() throws ExecutionException, InterruptedException, SQLException
    {
        Task st = new Task<ArrayList<String[]>>()
        {
            @Override
            protected ArrayList<String[]> call() throws Exception {
                ArrayList<String[]> lst = new ArrayList<>();

                if(!this.isDone()) {
                    String query = "SELECT * FROM EMPLOYEES_INFORMATION";
                    rs = databaseConnection.this.st.executeQuery(query);
                    while (rs.next()) {
                       String[] str = {String.valueOf(rs.getInt("Bitrix_ID")), rs.getString("Name"), rs.getString("Password"), rs.getString("Email")};
                       lst.add(str);
                    }
                }
                this.done();
                databaseConnection.this.con.close();
                return lst;
            }
        };
        new Thread(st).start();
        return (ArrayList<String[]>) st.get();
    }

    public boolean checkForUsers(String username, String pass) throws InterruptedException, ExecutionException {

        this.operation = true;
        Task<Boolean> task = this.executeTask(username, pass);
        this.database_thread = new Thread(task);
        this.database_thread.start();
        return task.get();

    }
    private Task<Boolean> executeTask(String username, String pass)
    {
        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {


                try {

                    if (!this.isCancelled()) {
                        String query = "SELECT * FROM USERS_ADMIN WHERE username =" + "'" + username + "'" + " AND " + "pass=" + "'" + pass + "'";
                        rs = st.executeQuery(query);
                        boolean flag = false;
                        while (rs.next()) {

                            Users ref = new Users(rs.getInt("id"), username, pass);
                            System.out.println(ref);
                            flag = true;
                            logged = true;
                            return true;
                        }
                        if (!flag) {
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
                            return false;

                        }
                        this.cancel();
                    }
                }
                catch(SQLException e){
                    e.printStackTrace();
                }

                return true;

            }

        };
        return task;
    }

}
