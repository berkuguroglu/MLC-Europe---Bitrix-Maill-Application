package Login;

import Login.Bitrix.bitrixAPI;
import Users.Users;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Main extends Application implements Login.Interfaces.LoginConnection {


    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        FXMLLoader loader= new FXMLLoader(getClass().getResource("Views/login.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("MLC Europe - Login Page");

        primaryStage.setScene(new Scene(root, primaryStage.getWidth(), primaryStage.getHeight()));
        primaryStage.setResizable(false);
        Login.Controller cont = loader.getController();
        Platform.runLater(() -> {
            cont.setStage(this);
            primaryStage.show();

        });

    }

    public static void main(String[] args) {



        launch(args);



    }
    private void openSecondPage() throws IOException {
      FXMLLoader secondPageLoader = new FXMLLoader(getClass().getResource("Views/secondpage.fxml"));
      Parent secondPage = secondPageLoader.load();
      Stage secondStage = new Stage();
      secondStage.setTitle("Main Page");
      secondStage.setResizable(false);
      secondStage.setScene(new Scene(secondPage, secondStage.getWidth(), secondStage.getHeight()));
      secondStage.show();



    }

    @Override
    public boolean loginClicked(boolean clicked, String username, String pass)
    {
        Database.databaseConnection db_class = new Database.databaseConnection();
        db_class.openConnection();
        try {
            if(db_class.checkForUsers(username, pass))
            {
                    //openSecondPage();
                    Thread thread = new Thread(new bitrixAPI());
                    thread.start();
                    //this.primaryStage.close();

            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return true;
    }

}
