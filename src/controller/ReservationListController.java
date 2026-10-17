package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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

    ReservationRepository reservationRepository;

    @FXML
    private TableView<Reservation> ReservationTable;

    private ObservableList<Reservation> reservations = FXCollections.observableArrayList();

    private TableColumn<Reservation, String> SpaceNameColumn;

    private TableColumn<Reservation, Date> DateColumn;

    private TableColumn<Reservation, String> StartTimeColumn;

    private TableColumn<Reservation, String> EndTimeColumn;

    private TableColumn<Reservation, List<String>> featuresColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ReservationTable.setEditable(false);
        SpaceNameColumn = new TableColumn<>("Space Name");
        DateColumn = new TableColumn<>("Date");
        StartTimeColumn = new TableColumn<>("Start Time");
        EndTimeColumn = new TableColumn<>("End Time");
        featuresColumn = new TableColumn<>("Features");
        ReservationTable.setItems(reservations);
        reservations.setAll();
    }

    public void setReservationRepository(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public void setSpaceRepository(SpaceRepository spaceRepository) {
        this.spaceRepository = spaceRepository;
    }

    public Reservation getSelectedReservation() {
        return ReservationTable.getSelectionModel().getSelectedItem();
    }

    public void loadReservations() {
        if (spaceRepository == null) {
            throw new IllegalStateException("Space repository has not been set");
        }

        if (reservationRepository == null) {
            throw new IllegalStateException("Reservation repository has not been set");
        }

        mapSpaceToBuildingID = spaceRepository.getAllSpaces()
                .stream()
                .collect(Collectors.toMap(Space::getId, Space::getName));

        reservations.setAll(reservationRepository.getAllReservations());
    }
}

