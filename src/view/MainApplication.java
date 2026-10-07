package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import model.Space;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.FXML;

public class MainApplication extends Application {

    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("SpaceListView.fxml"));

        stage.setTitle("Space/Reservation Application");
        Group root = new Group();
        root.getChildren().add(loader.load());
        Scene scene = new Scene(root, 1280, 720);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

}
