package view;

import controller.ReservationListController;
import controller.SpaceListController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import model.Reservation;
import model.Space;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import org.w3c.dom.events.Event;
import persistence.DataLoader;
import persistence.InMemorySpaceRepository;
import persistence.SpaceRepository;

import java.io.IOException;
import java.util.List;

public class MainApplication extends Application {

    SpaceRepository spaceRepository;
    DataLoader loader = new DataLoader();
    FXMLLoader fxmlLoader = new FXMLLoader();
    SpaceListController controller1;

    Label label = new Label("Valdosta Space & Reservation Application");
    Label label2 = new Label("Spaces");
    Label label3 = new Label("Space Features: ");
    Button addReservation = new Button("Add Reservation");
    Button viewDetails = new Button("View Details");
    Button filterCapacities = new Button("Filter Capacities");
    TextField capacity = new TextField();
    Button filterFeatures = new Button("Filter Features");
    TextField features = new TextField();
    TextField spaceFeatures = new TextField();

    public void start(Stage stage) throws Exception {
        try {
            stage.setTitle("Space/Reservation Application");
            Pane root = buildGUI();
            Scene scene = new Scene(root, 1280, 800);
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
        pane.setCenter(buildCentralPane());
        pane.setBottom(buildBottomPane());
        return pane;
    }

    private HBox buildTopPane() {
        HBox topHbox = new HBox(20);
        topHbox.setPrefHeight(80);
        topHbox.getChildren().add(label);
        topHbox.getChildren().add(label3);
        spaceFeatures.setAlignment(Pos.CENTER);
        spaceFeatures.setEditable(false);
        spaceFeatures.setPrefWidth(400);
        topHbox.getChildren().add(spaceFeatures);
        topHbox.getStyleClass().add ("topHbox");
        return topHbox;
    }

    private VBox buildLeftPane() throws IOException {
        List<Space> spaces = loader.loadSpaces();
        spaceRepository = new InMemorySpaceRepository(spaces);
        fxmlLoader.setLocation(getClass().getResource("SpaceListView.fxml"));
        VBox pane1 = fxmlLoader.load();
        controller1 = fxmlLoader.getController();
        controller1.setSpaceRepository(spaceRepository);
        controller1.loadSpaces();
        return pane1;
    }

    private Pane buildDetails1Pane() {
        HBox pane = new HBox(10);

        viewDetails.setOnAction(new showFeaturesOfSpace());
        pane.getChildren().add(viewDetails);
        pane.getChildren().add(filterCapacities);
        pane.getChildren().add(capacity);

        return pane;
    }

    private Pane buildDetails2Pane() {
        HBox pane = new HBox(10);
        pane.getChildren().add(filterFeatures);
        pane.getChildren().add(features);
        return pane;
    }

    private Pane buildCentralPane() {
        VBox pane = new VBox(10);
        pane.getChildren().add(buildDetails1Pane());
        pane.getChildren().add(buildDetails2Pane());
        pane.getStyleClass().add("pane");
        return pane;
    }

    private VBox buildRightPane() throws IOException {
        VBox pane = new VBox();
//        List<Reservation> reservations = loader.loadReservations();
//        ReservationListController controller = new ReservationListController();
//        controller.setReservationRepository(new InMemoryReservationRepository(reservations));
//        controller.loadReservations();
//        pane.getChildren().add(controller.getRoot());
        return pane;
    }

    private HBox buildBottomPane() {
        HBox pane = new HBox();
        return pane;
    }

    private class showFeaturesOfSpace implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent actionEvent) {
            spaceFeatures.setText(controller1.getSelectedSpace().spaceDetails());
        }
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

}
