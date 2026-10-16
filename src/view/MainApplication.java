package view;

import controller.SpaceListController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
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

import java.io.IOException;
import java.util.List;

public class MainApplication extends Application {

    SpaceRepository spaceRepository;
    DataLoader loader = new DataLoader();

    Label label = new Label("Valdosta Space & Reservation Application");
    Label label2 = new Label("Spaces");
    Button addReservation = new Button("Add Reservation");

    public void start(Stage stage) throws Exception {
        try {
            stage.setTitle("Space/Reservation Application");
            Pane root = buildGUI();
            Scene scene = new Scene(root, 1280, 720);
            scene.getStylesheets().add(getClass().getResource("stylization.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Pane buildGUI() throws IOException {
        return buildPane();
    }

    private Pane buildPane() throws IOException {
        BorderPane pane = new BorderPane();
        pane.setTop(buildTopPane());
        pane.setLeft(buildLeftPane());
        pane.setBottom(buildBottomPane());
        return pane;
    }

    private HBox buildTopPane() {
        HBox topHbox = new HBox(100);
        topHbox.setPrefHeight(80);
        topHbox.getChildren().add(label);
        topHbox.getStyleClass().add ("topHbox");
        return topHbox;
    }

    private VBox buildLeftPane() throws IOException {
        List<Space> spaces = loader.loadSpaces();
        spaceRepository = new InMemorySpaceRepository(spaces);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("SpaceListView.fxml"));
        VBox pane1 = fxmlLoader.load();
        SpaceListController controller = fxmlLoader.getController();
        controller.setSpaceRepository(spaceRepository);
        controller.loadSpaces();
        return pane1;
    }

    private VBox buildRightPane() throws IOException {
        VBox pane = new VBox();
        return pane;
    }

    private HBox buildBottomPane() {
        HBox pane = new HBox();
        return pane;
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

}
