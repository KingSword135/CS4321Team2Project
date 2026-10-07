package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Space;
import persistence.DataLoader;
import persistence.InMemorySpaceRepository;
import persistence.SpaceRepository;

import java.io.IOException;
import java.util.List;

public class MainController {

    private SpaceRepository spaceRepository;

    public MainController() {

    }

    public void run() throws Exception {
        DataLoader loader = new DataLoader();
        List<Space> spaces = loader.loadSpaces();
        this.spaceRepository = new InMemorySpaceRepository(spaces);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("SpaceListView.fxml"));


        VBox vbox = fxmlLoader.load();
    }

    public SpaceRepository getSpaceRepository() {
        return spaceRepository;
    }
}
