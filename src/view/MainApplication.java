package view;

import controller.SpaceListController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import model.Space;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import persistence.DataLoader;
import persistence.InMemorySpaceRepository;
import persistence.SpaceRepository;

import java.util.List;

public class MainApplication extends Application {

    public void start(Stage stage) throws Exception {

        SpaceRepository spaceRepository;
        DataLoader loader = new DataLoader();
        List<Space> spaces = loader.loadSpaces();
        spaceRepository = new InMemorySpaceRepository(spaces);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("SpaceListView.fxml"));
        Pane pane1 = fxmlLoader.load();
        SpaceListController controller = fxmlLoader.getController();
        controller.setSpaceRepository(spaceRepository);
        controller.loadSpaces();

        stage.setTitle("Space/Reservation Application");
        Group root = new Group();
        root.getChildren().add(pane1);
        Scene scene = new Scene(root, 1280, 720);
        scene.getStylesheets().add(getClass().getResource("stylization.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

}
