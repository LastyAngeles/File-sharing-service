package client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import messages.DeleteMessage;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private ClientCore client = ClientCore.getInstance();
    private Stage stage;
    private Parent root;

    @FXML
    private ListView<String> listView;
    @FXML
    private Button apploadButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button downloadButton;

    ObservableList<String> obList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        obList.addAll(client.getCurrentPathList());
        for (String t : client.getCurrentPathList()){
            System.out.println(t);
        }
        listView.getItems().addAll(obList);
        listView.getSelectionModel().getSelectedItem();
    }

    public void appload(ActionEvent event){
        FileChooser chooser = new FileChooser();
        try {
            Path path = chooser.showOpenDialog(null).toPath();
            if (path != null){
                client.sendFiles(path);
                refresh();
            }
        }
        catch (NullPointerException ex){
        }
    }

    public void delete(ActionEvent event){
        String tmp = listView.getSelectionModel().getSelectedItem();
        if (tmp != null) {
            DeleteMessage delMsg = new DeleteMessage(tmp);
            client.sendFiles(delMsg);
            refresh();
        }
    }

    public void download(ActionEvent event){
        String tmp = listView.getSelectionModel().getSelectedItem();
        System.out.println(tmp);
        if (tmp != null){
            DirectoryChooser dirChooser = new DirectoryChooser();
            dirChooser.setInitialDirectory(Paths.get("C:\\Users\\" + System.getProperty("user.name") + "\\Downloads").toFile());
            System.out.println(dirChooser.getInitialDirectory());
            try {
                Path path = dirChooser.showDialog(downloadButton.getScene().getWindow()).toPath();
                if (path != null){
                    client.downloadFile(tmp, path);
                }
            }
            catch (NullPointerException ex){

            }
        }
    }

    public void refresh(){
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        obList.setAll(client.getCurrentPathList());
        listView.setItems(obList);
    }

    public void dragOver(ActionEvent event){
    }
}
