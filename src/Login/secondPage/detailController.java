package Login.secondPage;

import Database.databaseConnection;
import Login.Interfaces.DialogConnection;
import Login.companyDialog;
import Mail.Mail;
import com.google.gson.JsonObject;
import com.mysql.cj.xdevapi.Table;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class detailController {


       @FXML
       private Label companyLabel;

       @FXML
       private CheckBox presale;

       @FXML
       private Button sendEmail;

       @FXML
       private CheckBox sale;

       @FXML
       private ComboBox<String> email;

       @FXML
       private ComboBox<String> countries;

       private companyDialog parentDialog;
       private int respid;
       private int compid;
       private TableView table;


    @FXML
        public void initialize()
        {

              this.presale.setOnMouseClicked(new EventHandler<MouseEvent>() {
                  @Override
                  public void handle(MouseEvent mouseEvent) {

                      if(presale.isSelected()) {
                          sale.setSelected(false);
                          DialogConnection dg = parentDialog;
                          dg.changeState("Presale");
                      }
                      else {
                          sale.setSelected(true);
                          DialogConnection dg = parentDialog;
                          dg.changeState("Sale");
                      }
                  }
              });
            this.sale.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {


                    if(sale.isSelected()) {
                        presale.setSelected(false);
                        DialogConnection dg = parentDialog;
                        dg.changeState("Sale");
                    }
                    else {
                        presale.setSelected(true);
                        DialogConnection dg = parentDialog;
                        dg.changeState("Presale");
                    }
                }
            });
            email.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    DialogConnection dg = parentDialog;
                    dg.changeEmail(email.getSelectionModel().getSelectedItem().toLowerCase());
                }
            });


        }

        public void setLabel(String companyName, String responsiblePerson, String email, String country, ArrayList<String> mails, companyDialog companyDialog, int rasped, int compid, TableView<Company> table)
        {
            ArrayList<String> list_country = new ArrayList<>();
            list_country.add(country);
            this.companyLabel.setText(companyName + " | " + responsiblePerson);
            this.email.setItems(FXCollections.observableList(mails));
            this.countries.setItems(FXCollections.observableList(list_country));
            this.parentDialog = companyDialog;
            this.email.getSelectionModel().select(email);
            this.countries.getSelectionModel().selectFirst();
            this.respid = rasped;
            System.out.println(rasped);
            this.compid = compid;
            this.table = table;
            sendEmail.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {

                    Mail obj = null;
                    try {
                        Mail.Templates.putTeam();
                        obj = new Mail(detailController.this.respid, detailController.this.email.getSelectionModel().getSelectedItem(), "English");

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    obj.setOnSucceeded(workerStateEvent -> {
                        Company temp = Company.find(compid);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Mail sent!");
                        temp.setStatus("Sent");
                        table.refresh();
                        Date dt = new Date();
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        temp.setDate(formatter.format(dt));
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {

                                databaseConnection db = new databaseConnection();
                                db.openConnection();
                                try {
                                    db.updateStatus(Integer.parseInt(temp.getID()), formatter.format(dt));
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    });
                    new Thread(obj).start();

                }
            });

        }


}

