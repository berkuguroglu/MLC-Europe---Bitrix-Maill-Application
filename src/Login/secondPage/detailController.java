package Login.secondPage;

import Login.Interfaces.DialogConnection;
import Login.companyDialog;
import com.google.gson.JsonObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class detailController {




       @FXML
       private Label companyLabel;

       @FXML
       private CheckBox presale;

       @FXML
       private CheckBox sale;

       @FXML
       private ComboBox<String> email;

       @FXML
       private ComboBox<String> countries;

       private companyDialog parentDialog;


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

        public void setLabel(String companyName, String responsiblePerson, String email, String country, ArrayList<String> mails, companyDialog companyDialog)
        {
            ArrayList<String> list_country = new ArrayList<>();
            list_country.add(country);
            this.companyLabel.setText(companyName + " | " + responsiblePerson);
            this.email.setItems(FXCollections.observableList(mails));
            this.countries.setItems(FXCollections.observableList(list_country));
            this.parentDialog = companyDialog;
            this.email.getSelectionModel().select(email);
            this.countries.getSelectionModel().selectFirst();

        }


}


