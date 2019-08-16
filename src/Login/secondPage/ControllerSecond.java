package Login.secondPage;

import Database.databaseConnection;
import Login.Bitrix.fetchAPI;
import Login.companyDialog;
import Mail.Mail;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import restRequest.request;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
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

    @FXML ProgressIndicator indicator;

    @FXML
    private TableColumn<Company, String> templates;


    @FXML
    private TableColumn<Company, String> resp;

    @FXML
    private TableColumn<Company, String> sale;

    @FXML
    private TableColumn<Company, String> status;

    @FXML
    private TableColumn<Company, String> date;

    @FXML private Button bitrixbutton;
    @FXML private TextField bitrixfield;

    @FXML private DatePicker datepicker;


    @FXML
    void initialize() {

        this.columns = new ArrayList<>();
        table.setEditable(false);
        columns.add(company_name);
        columns.add(company_id);
        columns.add(resp);
        columns.add(email);
        columns.add(country);
        columns.add(templates);
        columns.add(status);
        columns.add(sale);
        process.setOnMouseClicked(this);
        indicator.setVisible(false);
        bitrixbutton.setOnMouseClicked(this::bitrix);
        datepicker.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();

                setDisable(date.compareTo(today) > 0 );
            }
        });
        datepicker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                databaseConnection db = new databaseConnection();
                db.openConnection();
                Date dt = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

                try {
                    Task task;
                    if(formatter.format(dt).equals(datepicker.getValue().toString())) {
                        task = db.getCompanies(datepicker.getValue().toString(), true);
                        table.setDisable(true);
                        indicator.setVisible(true);
                        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                            @Override
                            public void handle(WorkerStateEvent workerStateEvent) {
                                table.refresh();
                                table.setDisable(false);
                                indicator.setVisible(false);
                                bitrixbutton.setDisable(false);
                                bitrixfield.setDisable(false);
                                process.setDisable(false);
                            }
                        });
                    }
                    else
                    {
                        task = db.getCompanies(datepicker.getValue().toString(), false);
                        table.setDisable(true);
                        indicator.setVisible(true);
                        bitrixbutton.setDisable(true);
                        bitrixfield.setDisable(true);
                        process.setDisable(true);
                        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                            @Override
                            public void handle(WorkerStateEvent workerStateEvent) {
                                table.refresh();
                                table.setDisable(false);
                                indicator.setVisible(false);
                            }
                        });

                    }

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private void bitrix(MouseEvent mouseEvent) {

        try {
            int value = 0;
            try {
                value = Integer.parseInt(this.bitrixfield.getText());
            }
            catch (NumberFormatException ex) {
                  value = 0;
            }
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Bitrix Fetch API");
            alert.setHeaderText("Information");
            alert.setContentText("You want to fetch " + value + " companies more. \nIt may take several minutes. ");
            Optional<ButtonType> result = alert.showAndWait();
            if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
                this.table.setDisable(true);
                this.bitrixbutton.setDisable(true);
                this.bitrixfield.setDisable(true);
                this.process.setDisable(true);
                this.indicator.setVisible(true);
                fetchAPI fetch = new fetchAPI(value);
                new Thread(fetch).start();
                fetch.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent workerStateEvent) {
                        table.setDisable(false);
                        indicator.setVisible(false);
                        ControllerSecond.this.bitrixbutton.setDisable(false);
                        ControllerSecond.this.bitrixfield.setDisable(false);
                        process.setDisable(false);
                        table.refresh();
                        enableTable();
                    }
                });

            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.enableTable();
               table.refresh();
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
        email.setCellValueFactory(new PropertyValueFactory<Company, String>("email"));
        sale.setCellValueFactory(new PropertyValueFactory<Company, String>("stater"));
        status.setCellValueFactory(new PropertyValueFactory<Company, String>("status"));
        date.setCellValueFactory(new PropertyValueFactory<Company, String>("date"));


        table.setItems(data);
        count.setText("Companies processing: " + Company.list.size());
        table.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getClickCount() == 2 && table.getSelectionModel().getSelectedItem() != null)
            {

                String URL = "https://mlcomponents.bitrix24.com/rest/12/b6pt3a9mlu6prvpl/crm.company.get?id=" + table.getSelectionModel().getSelectedItem().getID().trim();
                JsonObject result = null;
                ArrayList<String> mails = new ArrayList<>();
                try {
                    request req;
                    req = new request(URL);
                    JsonParser parser = new JsonParser();
                    result = (JsonObject) parser.parse(req.getResult());
                    if(result.get("result").getAsJsonObject().has("EMAIL"))
                    {
                        for(JsonElement object : result.get("result").getAsJsonObject().get("EMAIL").getAsJsonArray())
                        {
                            mails.add(object.getAsJsonObject().get("VALUE").getAsString());
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                Dialog<ArrayList<String>> d = new companyDialog(table.getSelectionModel().getSelectedItem().getCompanyName(), table.getSelectionModel().getSelectedItem().getResponsiblePerson(), table.getSelectionModel().getSelectedItem().getEmail(), table.getSelectionModel().getSelectedItem().getCountry(), mails);
                d.show();

            }
        });
    }


    @Override
    public void handle(MouseEvent mouseEvent)
    {
        try {
            Mail.Templates.putTeam();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ObservableList<Company> data = FXCollections.observableList(Company.list);
        this.process.setDisable(true);
        for(int t = 0; t<data.size(); t++) {

            try {
                if(data.get(t).getStatus().equals("Waiting")) { // we will see
                    Mail obj = new Mail(data.get(t).getRespPersonID(), data.get(t).getEmail(), "English");
                    new Thread(obj).start();
                    int finalT = t;
                    obj.setOnSucceeded(workerStateEvent -> {
                        // sent.setText(String.valueOf(Integer.parseInt(sent.getText().trim().split(":", 2)[1]) + 1));
                        data.get(finalT).setStatus("Sent");
                        Date dt = new Date();
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        data.get(finalT).setDate(formatter.format(dt));
                        table.refresh();
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {

                                databaseConnection db = new databaseConnection();
                                db.openConnection();
                                try {
                                    db.updateStatus(Integer.parseInt(data.get(finalT).getID()), formatter.format(dt));
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        System.out.println(finalT + " " + data.size());
                        if (finalT == data.size() - 1)
                            ControllerSecond.this.table.setDisable(false);
                    });
                    obj.setOnRunning(workerStateEvent -> {

                        data.get(finalT).setStatus("Sending..");
                        table.refresh();
                    });
                    obj.setOnFailed(workerStateEvent -> {

                        data.get(finalT).setStatus("Failed");
                        table.refresh();

                    });
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }



        }


    }

}
