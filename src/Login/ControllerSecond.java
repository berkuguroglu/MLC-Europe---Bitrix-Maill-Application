package Login;

import Login.secondPage.Company;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerSecond {

    ObservableList<Company> data = FXCollections.observableList(Company.list);


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<Company> table;

    @FXML
    private TableColumn<Company, String> company_id;
    @FXML
    private TableColumn<Company, String> company_name;

    @FXML
    private TableColumn<?, ?> company_type;

    @FXML
    private TableColumn<?, ?> country;

    @FXML
    private TableColumn<?, ?> email;

    @FXML
    private TableColumn<?, ?> templates;

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


    }

    public void enableTable()
    {
        company_name.setCellValueFactory(new PropertyValueFactory<Company, String>("companyName"));
        resp.setCellValueFactory(new PropertyValueFactory<Company, String>("responsiblePerson"));
        company_id.setCellValueFactory(new PropertyValueFactory<Company, String>("ID"));
        table.setItems(data);
    }
}
