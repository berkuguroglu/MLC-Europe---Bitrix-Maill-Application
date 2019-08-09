package Login.secondPage;

import Database.databaseConnection;
import Login.secondPage.Company;
import Mail.Mail;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class ControllerSecond implements EventHandler<MouseEvent> {

    private ArrayList<TableColumn<Company, String>> columns;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label count, sent;

    @FXML
    private TableView<Company> table;

    @FXML
    private TableColumn<Company, String> company_id;
    @FXML
    private TableColumn<Company, String> company_name;

    @FXML
    private TableColumn<Company, String> country;

    @FXML
    private TableColumn<Company, String> email;

    @FXML
    private Button process;

    @FXML
    private TableColumn<Company, String> templates;

    @FXML
    private TableView<?> table1;

    @FXML
    private TableColumn<?, ?> company_name1;

    @FXML
    private TableColumn<?, ?> company_type1;

    @FXML
    private TableColumn<?, ?> country1;

    @FXML
    private TableColumn<?, ?> email1;

    @FXML
    private TableColumn<Company, String> resp;

    @FXML
    private TableColumn<?, ?> templates1;

    @FXML
    void initialize() {

        this.table.refresh();
        this.columns = new ArrayList<>();
        table.setEditable(false);
        columns.add(company_name);
        columns.add(company_id);
        columns.add(resp);
        columns.add(email);
        columns.add(country);
        columns.add(templates);
        process.setOnMouseClicked(this);

    }

    public void enableTable()
    {

        ObservableList<Company> data = FXCollections.observableList(Company.list);
        for(int t = 0; t<data.size(); t++)
        System.out.println(data.get(t).getCompanyName());
        for(TableColumn<Company, String> el : columns)
        {
            el.setResizable(false);
            el.setEditable(false);

        }
        company_name.setCellValueFactory(new PropertyValueFactory<Company, String>("companyName"));
        resp.setCellValueFactory(new PropertyValueFactory<Company, String>("responsiblePerson"));
        company_id.setCellValueFactory(new PropertyValueFactory<Company, String>("ID"));
        country.setCellValueFactory(new PropertyValueFactory<Company, String>("country"));
        email.setCellValueFactory(new PropertyValueFactory<Company, String>("email"));

        table.setItems(data);
        sent.setText("Mail sent: ");
        count.setText("Companies processing: " + Company.list.size());
        table.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getClickCount() == 2 && table.getSelectionModel().getSelectedItem() != null)
            System.out.println(table.getSelectionModel().getSelectedItem().getCompanyName());
        });
    }



    @Override
    public void handle(MouseEvent mouseEvent)
    {
        databaseConnection db = new Database.databaseConnection();
        db.openConnection();
        Mail.Templates.putMap();
        ArrayList<String[]> stry = new ArrayList<>();
        try {
            stry = db.getSalesTeam();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for(String[] strr : stry)
        {
            System.out.println(strr[0] + strr[1] + strr[2] + strr[3]);
            Mail obj = null;
            try {
                obj = new Mail(strr[3], strr[2], "berk.ugo@gmail.com", "English");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            new Thread(obj).start();

        }
    }
}
