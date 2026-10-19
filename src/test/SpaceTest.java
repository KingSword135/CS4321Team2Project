
package test;

import model.Space;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SpaceTest {

    // Test creating a space with all parameters.

    @Test
    void createSpaceWithAllParameters() {
        int id = 12;
        String name = "Conference Room";
        String building = "Nevins Hall";
        int capacity = 5;
        List<String> features = new ArrayList<>();
        features.add("Whiteboard");

        Space space = new Space(
                id, name, building, capacity, features);

        assertEquals(id, space.getId());
        assertEquals(name, space.getName());
        assertEquals(building, space.getBuilding());
        assertEquals(capacity, space.getCapacity());
        assertEquals(features, space.getFeatures());
    }

    // Test creating a space without providing features.

    @Test
    void createSpaceWithFourParameters() {
        Space space = new Space(
                12, "Conference Room", "Nevins Hall", 5);

        assertEquals(12, space.getId());
        assertEquals("Conference Room", space.getName());
        assertEquals("Nevins Hall", space.getBuilding());
        assertEquals(5, space.getCapacity());
        assertEquals(List.of(), space.getFeatures());
    }

    // Test that multiple features are stored.

    @Test
    void createSpaceWithMultipleFeatures() {
        List<String> features =
                List.of("Whiteboard", "Projector", "Computer");

        Space space = new Space(
                13, "Study Room", "Library", 8, features);

        assertEquals(3, space.getFeatures().size());
        assertTrue(space.getFeatures().contains("Whiteboard"));
        assertTrue(space.getFeatures().contains("Projector"));
        assertTrue(space.getFeatures().contains("Computer"));
    }

    // Test that a space with a capacity of one is allowed.

    @Test
    void createSpaceWithMinimumValidCapacity() {
        Space space = new Space(
                14, "Small Room", "Library", 1);

        assertEquals(1, space.getCapacity());
    }

    // Test invalid space IDs.

    @Test
    void negativeSpaceIdIsRejected() {
        assertThrows(IllegalArgumentException.class, () ->
                new Space(-1, "Room", "Library", 5));
    }

    // Test invalid names.

    @Test
    void emptySpaceNameIsRejected() {
        assertThrows(IllegalArgumentException.class, () ->
                new Space(15, "", "Library", 5));
    }

    @Test
    void emptyBuildingNameIsRejected() {
        assertThrows(IllegalArgumentException.class, () ->
                new Space(15, "Room", "", 5));
    }

    // Test invalid capacity values.

    @Test
    void zeroCapacityIsRejected() {
        assertThrows(IllegalArgumentException.class, () ->
                new Space(15, "Room", "Library", 0));
    }

    @Test
    void negativeCapacityIsRejected() {
        assertThrows(IllegalArgumentException.class, () ->
                new Space(15, "Room", "Library", -1));
    }

    // Test equality based on space ID.

    @Test
    void spacesWithSameIdAreEqual() {
        Space first = new Space(
                20, "Room A", "Library", 5);

        Space second = new Space(
                20, "Room B", "Nevins Hall", 10);

        assertEquals(first, second);
    }

    @Test
    void spacesWithDifferentIdsAreNotEqual() {
        Space first = new Space(
                20, "Room A", "Library", 5);

        Space second = new Space(
                21, "Room A", "Library", 5);

        assertNotEquals(first, second);
    }
}
