package Login;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application implements Login.Interfaces.LoginConnection {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader= new FXMLLoader(getClass().getResource("Views/login.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Hello World");
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

    @Override
    public boolean loginClicked(boolean clicked)
    {
        System.out.println(clicked);
        return true;
    }

}
