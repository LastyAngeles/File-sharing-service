package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;


public class Main extends Application {

    public static void main(String[] args) {
        if (ClientCore.getInstance().connect()){
            System.out.println("Клиент подключился");
        }
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/loginScene.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setResizable(false);
        primaryStage.setTitle("PostBlock // Early Access");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}