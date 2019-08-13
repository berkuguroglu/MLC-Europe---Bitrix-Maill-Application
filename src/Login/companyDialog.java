package Login;

import Login.secondPage.detailController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.io.IOException;
import java.util.ArrayList;

public class companyDialog extends Dialog<ArrayList<String>> {


        public companyDialog(String name, String responsiblePerson, String email, String country) {


                FXMLLoader loader = new FXMLLoader(getClass().getResource("Views/companysDetail.fxml"));
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    detailController controller = loader.getController();
                    controller.setLabel(name, responsiblePerson, email, country);

                }
            });
                Parent parent = null;
                try {
                    parent = loader.load();


                } catch (IOException e) {
                    e.printStackTrace();
                }
                getDialogPane().setContent(parent);
                getDialogPane().getButtonTypes().addAll(ButtonType.APPLY, ButtonType.CANCEL);
                setResultConverter(new Callback<ButtonType, ArrayList<String>>() {
                    @Override
                    public ArrayList<String> call(ButtonType buttonType) {

                        System.out.println(buttonType);
                        return new ArrayList<>();
                    }
                });

        }
}
