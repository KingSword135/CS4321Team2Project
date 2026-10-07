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
import model.Space;
import persistence.SpaceRepository;

import java.net.URL;
import java.util.*;

public class SpaceListController implements Initializable {

    @FXML
    private TableView<Space> SpaceTable;

    private ObservableList<Space> spaces = FXCollections.observableArrayList();

    @FXML
    private TableColumn<Space, String> NameColumn;
    @FXML
    private TableColumn<Space, String> BuildingColumn;
    @FXML
    private TableColumn<Space, Integer> CapacityColumn;
    @FXML
    private Label EmptyStateLabel;

    private SpaceRepository spaceRepository;

    public SpaceListController() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        NameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        BuildingColumn.setCellValueFactory(new PropertyValueFactory<>("building"));
        CapacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        SpaceTable.setItems(spaces);
    }

    public void setSpaceRepository(SpaceRepository spaceRepository) {
        this.spaceRepository = spaceRepository;
    }

    public Space getSelectedSpace() {
        return SpaceTable.getSelectionModel().getSelectedItem();
    }

}