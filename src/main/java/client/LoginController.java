package client;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    private ClientCore client = ClientCore.getInstance();
    private Stage stage;
    private Parent root;

    @FXML
    private Label idLabelStatus;
    @FXML
    private TextField userName;
    @FXML
    private TextField password;
    @FXML
    private Button loginButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void login(ActionEvent event) throws IOException{
        if (client.tryAuth(userName.getText(), password.getText())){
            if (event.getSource() == loginButton){
                stage = (Stage) loginButton.getScene().getWindow();
                root = FXMLLoader.load(getClass().getResource("/primary.fxml"));

                stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent event) {
                        System.out.println("Окно было закрыто");
                        client.setFlagForThread(false);
                        client.saveClose();
                    }
                });

                Scene mainScene = new Scene(root);
                stage.setScene(mainScene);
                stage.setResizable(true);
                stage.show();
            }
        }
    }
}