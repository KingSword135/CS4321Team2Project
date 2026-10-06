package persistence;

import model.Space;

import java.util.ArrayList;
import java.util.List;

public class DataLoader {
    public DataLoader() {

    }

    public void load() {
        List<String> list1 = new ArrayList<>();
        Space a1 = new Space(1, "Room 1", "Nevins", 6, list1);
        Space a2 = new Space(1, "Room 1", "Nevins", 6, list1);
        Space a3 = new Space(1, "Room 1", "Nevins", 6, list1);
        Space a4 = new Space(1, "Room 1", "Nevins", 6, list1);
        Space a5 = new Space(1, "Room 1", "Nevins", 6, list1);
        Space a6 = new Space(1, "Room 1", "Nevins", 6);
        ArrayList<Space> list = new ArrayList<>();
        list.add(a1);
    }

    public static void main(String[] args) {
        DataLoader loader = new DataLoader();
        loader.load();
    }
}
