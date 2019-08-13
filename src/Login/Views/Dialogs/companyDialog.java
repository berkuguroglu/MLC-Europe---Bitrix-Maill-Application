package Login.Views.Dialogs;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Dialog;

import java.io.IOException;
import java.util.ArrayList;

public class companyDialog extends Dialog<ArrayList<String>> {


        public companyDialog(String data) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/LoginDialog.fxml"));
                Parent root = loader.load();
                getDialogPane().setContent(root);

                setResultConverter(buttonType -> {
                    SomeDataType someData = ... ;
                    return someData ;
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
}
