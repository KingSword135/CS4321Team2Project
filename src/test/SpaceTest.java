package test;

import model.Space;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SpaceTest {
    @Test
    void createSpaceWithAllParameters() {
        int id = 12;
        String name = "test";
        String building = "test";
        int capacity = 5;
        List<String> features = new ArrayList<>();
        features.add("Whiteboard");
        Space space = new Space(id, name, building, capacity, features);

        System.out.println(space.toString());

        assertEquals(id, space.getId());
        assertEquals(name, space.getName());
        assertEquals(building, space.getBuilding());
        assertEquals(capacity, space.getCapacity());
        assertEquals(features, space.getFeatures());

    }

    @Test
    void createSpaceWithFourParameters() {
        int id = 12;
        String name = "test";
        String building = "test";
        int capacity = 5;
        Space space = new Space(id, name, building, capacity);

        assertEquals(id, space.getId());
        assertEquals(name, space.getName());
        assertEquals(building, space.getBuilding());
        assertEquals(capacity, space.getCapacity());
        assertEquals(List.of(), space.getFeatures());

    }
}