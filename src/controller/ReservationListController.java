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
import persistence.InMemorySpaceRepository;
import persistence.ReservationRepository;
import persistence.SpaceRepository;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class ReservationListController implements Initializable {

    public Map<Integer, String> mapSpaceToBuildingID;

    public ReservationListController() {

    }

    SpaceRepository spaceRepository;

    @FXML
    private TableView<Reservation> reservationTable;

    private ObservableList<Reservation> reservations = FXCollections.observableArrayList();

    private TableColumn<Reservation, String> SpaceNameColumn;

    private TableColumn<Reservation, Date> DateColumn;

    private TableColumn<Reservation, String> StartTimeColumn;

    private TableColumn<Reservation, String> EndTimeColumn;

    private TableColumn<Reservation, List<String>> featuresColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mapSpaceToBuildingID = spaceRepository.getAllSpaces().stream().collect(Collectors.toMap(Space::getId, Space::getName));
        reservationTable.setEditable(false);
        SpaceNameColumn = new TableColumn<>("Space Name");
        DateColumn = new TableColumn<>("Date");
        StartTimeColumn = new TableColumn<>("Start Time");
        EndTimeColumn = new TableColumn<>("End Time");
        featuresColumn = new TableColumn<>("Features");
        reservationTable.setItems(reservations);
        reservations.setAll();
    }

    public void setReservationRepository() {

    }

    public Reservation getSelectedReservation() {
        return reservationTable.getSelectionModel().getSelectedItem();
    }

    public void loadAllReservations() {

    }
}
