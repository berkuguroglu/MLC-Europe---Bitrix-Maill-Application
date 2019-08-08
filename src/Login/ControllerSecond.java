package Login;

import Login.secondPage.Company;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ControllerSecond {

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

    }

    public void enableTable()
    {

        ObservableList<Company> data = FXCollections.observableList(Company.list);
        for(TableColumn<Company, String> el : columns)
        {
            el.setResizable(false);
            el.setEditable(false);

        }
        company_name.setCellValueFactory(new PropertyValueFactory<Company, String>("companyName"));
        resp.setCellValueFactory(new PropertyValueFactory<Company, String>("responsiblePerson"));
        company_id.setCellValueFactory(new PropertyValueFactory<Company, String>("ID"));
        country.setCellValueFactory(new PropertyValueFactory<Company, String>("country"));

        table.setItems(data);
        sent.setText("Mail sent: ");
        count.setText("Companies processing: " + Company.list.size());
        table.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getClickCount() == 2 && table.getSelectionModel().getSelectedItem() != null)

                System.out.println(table.getSelectionModel().getSelectedItem().getCompanyName());
            }
        });
    }
}
