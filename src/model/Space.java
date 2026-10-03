package model;

import java.util.List;
import java.util.Objects;

public class Space {

    int id;
    String name;
    String building;
    int capacity;
    List<String> features;

    public Space(int id, String name, String building, int capacity) {
        this(id, name, building, capacity, List.of());
    }

    public Space(int id,  String name, String building, int capacity, List<String> features) {
        if (id < 0 || name.isEmpty() || building.isEmpty() || capacity <= 0) {
            return;
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
        Space s = (Space) o;
        return Objects.equals(id, s.id);
    }

    public String toString() {
        return "ID: " + getId() + ", Name: " + getName() + ", Building: " + getBuilding() + ", Capacity: " + getCapacity() + ", Features: " + getFeatures();
    }
}
