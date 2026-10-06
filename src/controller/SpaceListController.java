package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Space;

import java.net.URL;
import java.util.ResourceBundle;

public class SpaceListController implements Initializable {

    @FXML
    private TableView<Space> SpaceTable;

    @FXML
    private TableColumn<Space, String> NameColumn;

    @FXML
    private TableColumn<Space, String> BuildingColumn;

    @FXML
    private TableColumn<Space, Integer> CapacityColumn;

    @FXML
    private Label EmptyStateLabel;

    public SpaceListController() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO (task 3.2): bind observable list and load spaces from repository
        // TODO (task 3.3): toggle emptyStateLabel visibility
    }

    public Space getSelectedSpace() {
        // TODO (task 3.2 / US-2): return current table selection
        return null;
    }
}