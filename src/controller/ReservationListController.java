package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Reservation;
import model.Space;
import persistence.SpaceRepository;

import java.net.URL;
import java.util.*;

public class ReservationListController implements Initializable {
    public ReservationListController() {

    }

    @FXML
    private TableView<Reservation> reservationTable;

    private ObservableList<Reservation> reservations = FXCollections.observableArrayList();

    private TableColumn<Space, String> SpaceNameColumn;

    private TableColumn<Reservation, Date> DateColumn;

    private TableColumn<Reservation, String> StartTimeColumn;

    private TableColumn<Reservation, String> EndTimeColumn;

    private TableColumn<Reservation, List<String>> featuresColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SpaceNameColumn = new TableColumn<>("Space Name");
        DateColumn = new TableColumn<>("Date");
        StartTimeColumn = new TableColumn<>("Start Time");
        EndTimeColumn = new TableColumn<>("End Time");
        featuresColumn = new TableColumn<>("Features");
        reservationTable.setItems(reservations);
        reservations.setAll();
    }

    public void loadAllReservations() {

    }
}
