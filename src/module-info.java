module JavaFxApplication {

    requires javafx.fxml;
    requires javafx.controls;
    requires java.sql;
    requires mysql.connector.java;
    requires gson;
    requires david.webb;
    requires java.mail;
    opens Login.secondPage;
    opens Login;
}