package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Space {

    private int id;
    private String name;
    private String building;
    private int capacity;
    private List<String> features;

    public Space() {
        this.features = new ArrayList<>();
    }

    public Space(int id, String name, String building, int capacity) {
        this(id, name, building, capacity, List.of());
    }

    public Space(int id, String name, String building,
                 int capacity, List<String> features) {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Space ID must be greater than zero.");
        }

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Space name is required.");
        }

        if (building == null || building.isBlank()) {
            throw new IllegalArgumentException(
                    "Building name is required.");
        }

        if (capacity <= 0) {
            throw new IllegalArgumentException(
                    "Capacity must be greater than zero.");
        }

        if (features == null) {
            throw new IllegalArgumentException(
                    "Features list cannot be null.");
        }

        for (String feature : features) {
            if (feature == null || feature.isBlank()) {
                throw new IllegalArgumentException(
                        "Features cannot be empty.");
            }
        }

        this.id = id;
        this.name = name.trim();
        this.building = building.trim();
        this.capacity = capacity;
        this.features = new ArrayList<>(features);
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
        return new ArrayList<>(features);
    }

    // Check whether the space has a particular feature.
    public boolean hasFeature(String feature) {
        if (feature == null || feature.isBlank()) {
            return false;
        }

        return features.stream()
                .anyMatch(item -> item.equalsIgnoreCase(feature.trim()));
    }

    // Check whether the space can hold a group of people.
    public boolean canAccommodate(int people) {
        return people > 0 && capacity >= people;
    }

    // Check whether the space has all requested features.
    public boolean hasAllFeatures(List<String> requiredFeatures) {
        if (requiredFeatures == null) {
            return false;
        }

        for (String feature : requiredFeatures) {
            if (!hasFeature(feature)) {
                return false;
            }
        }

        return true;
    }

    // Check whether the space matches a search.
    public boolean matchesSearch(String searchText) {
        if (searchText == null || searchText.isBlank()) {
            return true;
        }

        String search = searchText.trim().toLowerCase();

        return name.toLowerCase().contains(search)
                || building.toLowerCase().contains(search)
                || features.stream().anyMatch(
                        feature -> feature.toLowerCase().contains(search));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Space)) {
            return false;
        }

        Space space = (Space) o;
        return id == space.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String spaceDetails() {
        String msg = "";
        if (features != null && !features.isEmpty()) {
            msg += "Name: " + getName() + " Building: " + getBuilding() +  " Capacity: " + getCapacity() + ", Features: " + String.join(", ", features);
        }
        else {
            msg += "No features available";
        }
        return msg;
    }

    @Override
    public String toString() {
        return "ID: " + id
                + ", Name: " + name
                + ", Building: " + building
                + ", Capacity: " + capacity
                + ", Features: " + features;
    }

    public static void main(String[] args) {
        List<Space> spaces = List.of(
                new Space(1, "Study Room", "Odum Library",
                        6, List.of("Whiteboard")),
                new Space(2, "Study Nook", "Odum Library",
                        4, List.of("Whiteboard")),
                new Space(3, "Computer Lab", "Nevins",
                        25, List.of("Whiteboard", "Projector",
                        "Lab Computers")),
                new Space(4, "Science Lab", "Bailey",
                        30, List.of("Whiteboard", "Projector",
                        "Science Equipment")),
                new Space(5, "Classroom 1234", "Nevins",
                        25, List.of("Whiteboard", "Projector"))
        );

        for (Space space : spaces) {
            System.out.println(space);
        }
    }
}
