package test;

import controller.SpaceFilterController;
import model.Space;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.InMemorySpaceRepository;
import persistence.SpaceRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SpaceIntegrationTest {

    private SpaceRepository repository;
    private SpaceFilterController filterController;

    @BeforeEach
    void setUp() {
        repository = new InMemorySpaceRepository(List.of(
                new Space(1, "Study Room", "Odum Library",
                        6, List.of("Whiteboard")),
                new Space(2, "Computer Lab", "Nevins",
                        25, List.of("Whiteboard", "Projector")),
                new Space(3, "Classroom", "Nevins",
                        30, List.of("Projector")),
                new Space(4, "Small Room", "Bailey",
                        4, List.of()),
                new Space(5, "Science Lab", "Bailey",
                        20, List.of("Projector", "Lab Equipment"))
        ));

        filterController = new SpaceFilterController(repository);
    }

    // US-1, AT-1: Spaces display name, building, and capacity.
    @Test
    void spacesContainRequiredInformation() {
        Space space = repository.getAllSpaces().get(0);

        assertNotNull(space.getName());
        assertNotNull(space.getBuilding());
        assertTrue(space.getCapacity() > 0);
    }

    // US-1, AT-2: Spaces are sorted alphabetically by name.
    @Test
    void spacesAreSortedAlphabetically() {
        List<Space> spaces = repository.getAllSpaces();

        assertEquals(
                List.of("Classroom", "Computer Lab", "Science Lab",
                        "Small Room", "Study Room"),
                spaces.stream().map(Space::getName).toList()
        );
    }

    // US-1, AT-3: An empty dataset returns no spaces.
    @Test
    void emptyRepositoryReturnsEmptyList() {
        SpaceRepository emptyRepository =
                new InMemorySpaceRepository(List.of());

        assertTrue(emptyRepository.getAllSpaces().isEmpty());
    }

    // US-2, AT-1: Space details include features.
    @Test
    void selectedSpaceHasAllDetailFields() {
        Space space = repository.getAllSpaces().stream()
                .filter(s -> s.getId() == 2)
                .findFirst()
                .orElseThrow();

        assertEquals("Computer Lab", space.getName());
        assertEquals("Nevins", space.getBuilding());
        assertEquals(25, space.getCapacity());
        assertEquals(
                List.of("Whiteboard", "Projector"),
                space.getFeatures()
        );
    }

    // US-2, AT-3: A space without features has an empty feature list.
    @Test
    void spaceCanHaveNoFeatures() {
        Space space = repository.getAllSpaces().stream()
                .filter(s -> s.getId() == 4)
                .findFirst()
                .orElseThrow();

        assertTrue(space.getFeatures().isEmpty());
    }

    // US-3, AT-1: Capacity filter returns spaces meeting the minimum.
    @Test
    void filtersSpacesByMinimumCapacity() {
        List<Space> results =
                filterController.filterByMinCapacity("20");

        assertFalse(results.isEmpty());
        assertTrue(results.stream()
                .allMatch(space -> space.getCapacity() >= 20));
    }

    // US-3, AT-2: No matches returns an empty list.
    @Test
    void capacityFilterReturnsEmptyWhenNothingMatches() {
        assertTrue(
                filterController.filterByMinCapacity("1000").isEmpty()
        );
    }

    // US-3, AT-3: Invalid capacity input is rejected.
    @Test
    void capacityFilterRejectsInvalidInput() {
        assertThrows(IllegalArgumentException.class,
                () -> filterController.filterByMinCapacity("abc"));

        assertThrows(IllegalArgumentException.class,
                () -> filterController.filterByMinCapacity("0"));

        assertThrows(IllegalArgumentException.class,
                () -> filterController.filterByMinCapacity("-5"));
    }
}
