package Login.secondPage;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;

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



        @FXML
        public void initialize()
        {
              this.presale.setSelected(true);
              this.presale.setOnMouseClicked(new EventHandler<MouseEvent>() {
                  @Override
                  public void handle(MouseEvent mouseEvent) {
                      if(presale.isSelected())
                        sale.setSelected(false);
                      else
                        sale.setSelected(true);
                  }
              });
            this.sale.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if(presale.isSelected())
                        presale.setSelected(false);
                    else
                        presale.setSelected(true);
                }
            });
        }

        public void setLabel(String companyName, String responsiblePerson, String email, String country)
        {
            ArrayList<String> list = new ArrayList<>();
            ArrayList<String> list_country = new ArrayList<>();
            list.add(email);
            list_country.add(country);
            this.companyLabel.setText(companyName + " | " + responsiblePerson);
            this.email.setItems(FXCollections.observableList(list));
            this.countries.setItems(FXCollections.observableList(list_country));

        }


}


