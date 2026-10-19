package view;

import controller.ReservationListController;
import controller.SpaceFilterController;
import controller.SpaceListController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import model.Reservation;
import model.ReservationManager;
import model.Space;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;
import persistence.*;

import java.io.IOException;
import java.util.List;

import java.time.*;

public class MainApplication extends Application {

    SpaceRepository spaceRepository;
    ReservationRepository reservationRepository;
    ReservationManager reservationManager = new ReservationManager();

    //Load in data...
    DataLoader loader = new DataLoader();
    FXMLLoader fxmlLoader1 = new FXMLLoader();
    FXMLLoader fxmlLoader2 = new FXMLLoader();

    //Necessary controllers
    SpaceListController controller1;
    ReservationListController controller2;

    Label label1 = new Label("Valdosta Space & Reservation Application");
    Label label2 = new Label("Spaces");
    Label label3 = new Label("Space Features: ");

    //Buttons
    Button viewDetails = new Button("View Details");
    Button filterCapacities = new Button("Filter Capacities");
    Button filterFeatures = new Button("Filter Features");

    //TextFields
    TextField capacityTextField = new TextField();
    TextField features = new TextField();
    TextField spaceFeatures = new TextField();
    TextField startingTime = new TextField();
    TextField endingTime = new TextField();

    DatePicker datePicker = new DatePicker();

    public void start(Stage stage) throws Exception {
        try {
            stage.setTitle("Space/Reservation Application");
            Pane root = buildGUI();
            Scene scene = new Scene(root, 1280, 500);
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
        pane.setRight(buildRightPane());
        pane.setBottom(buildBottomPane());
        return pane;
    }

    private HBox buildTopPane() {
        HBox topHbox = new HBox(20);
        topHbox.setPrefHeight(80);
        topHbox.getChildren().add(label1);
        label1.getStyleClass().add ("label1");
        label1.setPadding(new Insets(10,0,0,0))    ;
        label3.getStyleClass().add ("label1");
        label3.setPadding(new Insets(10,0,0,0))    ;
        topHbox.getChildren().add(label3);
        topHbox.setId("topHbox");
//        topHbox.getStyleClass().add ("topHbox");
        spaceFeatures.setAlignment(Pos.CENTER);
        spaceFeatures.setEditable(false);
        spaceFeatures.setPrefWidth(400);
        topHbox.getChildren().add(spaceFeatures);
        topHbox.setMargin(spaceFeatures, new Insets(10, 10, 10, 10));
//        Image img = new Image("src/view/valdosta-state-university-logo-freelogovectors.net_.png");
//        ImageView ImageView = new ImageView();
//        ImageView.setImage(img);
//        ImageView.setFitWidth(100);
//        ImageView.setFitHeight(100);
//        topHbox.getChildren().add(ImageView);
        return topHbox;
    }

    private VBox buildLeftPane() throws IOException {
        List<Space> spaces = loader.loadSpaces();
        spaceRepository = new InMemorySpaceRepository(spaces);
        fxmlLoader1.setLocation(getClass().getResource("SpaceListView.fxml"));
        VBox pane1 = fxmlLoader1.load();
        controller1 = fxmlLoader1.getController();
        controller1.setSpaceRepository(spaceRepository);
        controller1.loadSpaces();
        pane1.getStyleClass().add("tableview");
        return pane1;
    }

    private Pane buildDetails1Pane() {
        HBox pane = new HBox(10);
        pane.setPadding(new Insets(10,10,10,10));
        viewDetails.setOnAction(new showFeaturesOfSpace());
        capacityTextField.setPrefWidth(40);
        pane.getChildren().add(viewDetails);
        pane.getChildren().add(filterCapacities);
        filterCapacities.setOnAction(new filterByCapacity());
        pane.getChildren().add(capacityTextField);
        Button resetCapacity = new Button("Reset");
        resetCapacity.setOnAction(new resetCapacityTable());
        pane.getChildren().add(resetCapacity);
        return pane;
    }

    private Pane buildDetails2Pane() {
        HBox pane = new HBox(10);
        pane.setPadding(new Insets(10,10,10,10));
        pane.getChildren().add(filterFeatures);
        pane.getChildren().add(features);
        return pane;
    }
    private Pane buildDetails3Pane() {
        HBox pane = new HBox(10);
        pane.setPadding(new Insets(10,10,10,10));

        datePicker.setPromptText("Date");
        startingTime.setPromptText("Starting Time (HH:MM)");
        endingTime.setPromptText("Ending Time (HH:MM)");
        pane.getChildren().add(datePicker);
        pane.getChildren().add(startingTime);
        pane.getChildren().add(endingTime);
        return pane;
    }

    private Pane makeReservationPanel() {
        HBox pane = new HBox(10);
        Button buildReservation = new Button("Build Reservation");
        Button deleteReservation = new Button("Delete Reservation");
        Button modifyReservation = new Button("Modify Reservation");
        pane.getChildren().add(buildReservation);
        pane.getChildren().add(deleteReservation);
        pane.getChildren().add(modifyReservation);
        buildReservation.setAlignment(Pos.CENTER);
        buildReservation.setOnAction(new buildReservation());
        deleteReservation.setOnAction(new deleteReservation());
        modifyReservation.setOnAction(new modifyReservationInTable());
        pane.setAlignment(Pos.CENTER);
        buildReservation.setOnAction(new buildReservation());
        return pane;
    }

    private Pane buildCentralPane() {
        VBox pane = new VBox(10);
        pane.setMaxHeight(400);
        pane.getChildren().add(buildDetails1Pane());
        pane.getChildren().add(buildDetails2Pane());
        pane.getChildren().add(buildDetails3Pane());
        pane.getChildren().add(makeReservationPanel());
        pane.getStyleClass().add("pane");
        return pane;
    }

    private VBox buildRightPane() throws IOException {
        reservationManager.loadReservations();
        List<Reservation> reservations = reservationManager.getReservations();
        reservationRepository = new InMemoryReservationRepository(reservations);
        fxmlLoader2.setLocation(getClass().getResource("ReservationListView.fxml"));
        VBox pane1 = fxmlLoader2.load();
        controller2 = fxmlLoader2.getController();
        controller2.setSpaceRepository(spaceRepository);
        controller2.setReservationRepository(reservationRepository);
        controller2.loadReservations();
        pane1.getStyleClass().add("tableview");
        return pane1;
    }

    private HBox buildBottomPane() {
        HBox pane = new HBox();
        pane.setPrefWidth(1280);
        pane.setId("bottomHbox");
        pane.getStyleClass().add("bottomHbox");
        Label blank = new Label();
        pane.getChildren().add(blank);
        return pane;
    }

    private void alert(String message) {
        Alert errormessage = new Alert(Alert.AlertType.ERROR);
        errormessage.setContentText(message);
        errormessage.show();
    }

    private void getErrors(LocalDate date, LocalTime timestart, LocalTime timeend) {
        if (date.isBefore(LocalDate.now())) {
            alert("Date cannot be before today");
            return;
        }

        if (timestart.isAfter(timeend)) {
            alert("End time cannot be before the Start time");
            return;
        }

        if (timestart.isBefore(LocalTime.of(8,0))) {
            alert("Start time cannot be before 8:00 AM");
            return;
        }

        if (timeend.isAfter(LocalTime.of(20,0))) {
            alert("End time cannot be after 8:00 PM");
            return;
        }
        if (timeend.toSecondOfDay() / 60 - timestart.toSecondOfDay() / 60 > 120) {
            alert("Reservation cannot be longer than 2 hours");
            return;
        }
    }

    private class showFeaturesOfSpace implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent actionEvent) {
            if (controller1.getSelectedSpace() != null) {
                spaceFeatures.setText(controller1.getSelectedSpace().spaceDetails());
            }
            else {
                spaceFeatures.setText("No space selected");
            }
        }
    }

    private class buildReservation implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent actionEvent) {
            LocalDate date = datePicker.getValue();
            String start_time[] = startingTime.getText().split(":");
            String end_time[] = endingTime.getText().split(":");

            LocalTime timestart = LocalTime.of(Integer.parseInt(start_time[0]),Integer.parseInt(start_time[1]));
            LocalTime timeend = LocalTime.of(Integer.parseInt(end_time[0]),Integer.parseInt(end_time[1]));

            getErrors(date,timestart,timeend);

            reservationManager.createReservation(controller1.getSelectedSpace().getId(),date,timestart,timeend);
            ReservationJSONReadWriter rs = new ReservationJSONReadWriter();
            rs.WriteToFile(reservationManager.getReservations());
            reservationRepository = new InMemoryReservationRepository(reservationManager.getReservations());
            controller2.setReservationRepository(reservationRepository);
            controller2.refreshTable();
            System.out.println("Reservation created");
        }
    }

    private class deleteReservation implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent actionEvent) {
            Reservation ReservationtoCancel = controller2.getSelectedReservation();
            reservationManager.cancelReservation(ReservationtoCancel.getId());
            ReservationJSONReadWriter rs = new ReservationJSONReadWriter();
            rs.WriteToFile(reservationManager.getReservations());
            reservationRepository = new InMemoryReservationRepository(reservationManager.getReservations());
            controller2.setReservationRepository(reservationRepository);
            controller2.refreshTable();
            System.out.println("Reservation deleted");
        }
    }

    private class filterByCapacity implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent actionEvent) {
            SpaceFilterController spaceFilterController = new SpaceFilterController(spaceRepository);
            String capacity = capacityTextField.getText();
            if (Integer.parseInt(capacity) <= 0 || !capacity.matches("\\d+")) {
                alert("Capacity must be greater than 0 or a number");
                capacityTextField.setText("");
                return;
            }
            List<Space> filteredSpaces = spaceFilterController.filterByMinCapacity(capacity);
            capacityTextField.setText("");
            SpaceRepository copiedSpaceRepository = new InMemorySpaceRepository(filteredSpaces);
            controller1.setSpaceRepository(copiedSpaceRepository);
            controller1.refreshTable();
            System.out.println("Filtered spaces: " + filteredSpaces.size());
        }
    }

    private class resetCapacityTable implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent actionEvent) {
            controller1.setSpaceRepository(spaceRepository);
            controller1.refreshTable();
            System.out.println("Space table reset");
        }
    }

    private class modifyReservationInTable implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent actionEvent) {
            Space space1 = controller1.getSelectedSpace();
            Reservation reservation1 = controller2.getSelectedReservation();
            int id = reservation1.getId();
            int spaceId = space1.getId();
            String start_time[] = startingTime.getText().split(":");
            String end_time[] = endingTime.getText().split(":");
            LocalTime startTime = LocalTime.of(Integer.parseInt(start_time[0]), Integer.parseInt(start_time[1]));
            LocalTime endTime = LocalTime.of(Integer.parseInt(end_time[0]), Integer.parseInt(end_time[1]));
            reservationManager.modifyReservation(id, spaceId, datePicker.getValue(), startTime, endTime);
            if (startTime.isAfter(endTime)) {
                alert("New reservation start time is after end time");
            }
            controller2.refreshTable();
        }
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

}
