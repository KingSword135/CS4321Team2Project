package model;

import java.util.List;
import java.util.Objects;

public class Space {

    private int id;
    private String name;
    private String building;
    private int capacity;
    private List<String> features;

    public Space(int id, String name, String building, int capacity) {
        this(id, name, building, capacity, List.of());
    }

    public Space(int id, String name, String building, int capacity, List<String> features) {
        if (id < 0 || name.isEmpty() || building.isEmpty() || capacity <= 0) {
            throw new IllegalArgumentException("Invalid space information");
        }

        this.id = id;
        this.name = name;
        this.building = building;
        this.capacity = capacity;
        this.features = features;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBuilding() {
        return building;
    }

    public int getCapacity() {
        return capacity;
    }

    public List<String> getFeatures() {
        return features;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Space)) {
            return false;
        }

        Space s = (Space) o;
        return id == s.id;
    }

    @Override
    public String toString() {
        return "ID: " + getId()
                + ", Name: " + getName()
                + ", Building: " + getBuilding()
                + ", Capacity: " + getCapacity()
                + ", Features: " + getFeatures();
    }

    public static void main(String[] args) {
        Space room1 = new Space(1, "Study Room", "Odum Library", 6, List.of("Whiteboard"));
        Space room2 = new Space(2, "Study Nook", "Odum Library", 4, List.of("Whiteboard"));
        Space room3 = new Space(3, "Computer Lab", "Nevins", 25, List.of("Whiteboard", "Projector", "Lab Computers"));
        Space room4 = new Space(4, "Science Lab", "Bailey", 30, List.of("Whiteboard", "Projector", "Science Equipment"));
        Space room5 = new Space(5, "Classroom 1234", "Nevins", 25, List.of("Whiteboard", "Projector"));

        System.out.println(room1);
        System.out.println(room2);
        System.out.println(room3);
        System.out.println(room4);
        System.out.println(room5);

    }
}
