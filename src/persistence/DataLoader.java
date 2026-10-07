package persistence;

import model.Space;

import java.util.ArrayList;
import java.util.List;

public class DataLoader {
    public DataLoader() {

    }

    public List<Space> loadSpaces() {
        List<String> list1 = new ArrayList<>();
        Space a1 = new Space(1, "Room 1", "Nevins", 6, list1);
        Space a2 = new Space(6, "Room 4", "Nevins", 6, list1);
        Space a3 = new Space(12, "Room 2", "Nevins", 6, list1);
        Space a4 = new Space(7, "Room 8", "Nevins", 6, list1);
        Space a5 = new Space(14, "Room 7", "Nevins", 6, list1);
        Space a6 = new Space(19, "Room 6", "Nevins", 6);
        ArrayList<Space> list = new ArrayList<>();
        list.add(a1);
        list.add(a2);
        list.add(a3);
        list.add(a4);
        list.add(a5);
        list.add(a6);
        return list;
    }

    public static void main(String[] args) {
        DataLoader loader = new DataLoader();
        loader.loadSpaces();
    }
}
