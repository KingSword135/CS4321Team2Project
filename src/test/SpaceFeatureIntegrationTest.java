package test;

import controller.SpaceFilterController;
import model.Reservation;
import model.ReservationManager;
import model.ReservationSchedule;
import model.Space;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.InMemorySpaceRepository;
import persistence.SpaceRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SpaceFeatureIntegrationTest {

    private SpaceRepository repository;
    private SpaceFilterController filterController;
    private ReservationManager manager;
    private ReservationSchedule schedule;

    private final LocalDate date = LocalDate.of(2026, 11, 2);

    @BeforeEach
    void setUp() {
        repository = new InMemorySpaceRepository(List.of(
                new Space(1, "Study Room", "Library", 6,
                        List.of("Whiteboard")),
                new Space(2, "Computer Lab", "Nevins", 25,
                        List.of("Whiteboard", "Projector")),
                new Space(3, "Science Lab", "Bailey", 30,
                        List.of("Projector", "Lab Equipment"))
        ));

        filterController = new SpaceFilterController(repository);
        manager = new ReservationManager();
        schedule = new ReservationSchedule(manager);
    }

    // US-4, AT-1: Filter by one feature.
    @Test
    void filtersSpacesByOneFeature() {
        List<Space> results =
                filterController.filterByFeatures(List.of("Whiteboard"));

        assertEquals(2, results.size());
        assertTrue(results.stream()
                .allMatch(space -> space.hasFeature("Whiteboard")));
    }

    // US-4, AT-2: Spaces must contain ALL selected features.
    @Test
    void filtersSpacesByAllSelectedFeatures() {
        List<Space> results = filterController.filterByFeatures(
                List.of("Whiteboard", "Projector"));

        assertEquals(1, results.size());
        assertEquals("Computer Lab", results.get(0).getName());
    }

    // US-4, AT-3: No matches returns an empty list.
    @Test
    void featureFilterReturnsEmptyWhenNothingMatches() {
        assertTrue(filterController
                .filterByFeatures(List.of("3D Printer"))
                .isEmpty());
    }

    // US-4, AT-4: No selected features is invalid.
    @Test
    void featureFilterRequiresAtLeastOneFeature() {
        assertThrows(IllegalArgumentException.class,
                () -> filterController.filterByFeatures(List.of()));

        assertThrows(IllegalArgumentException.class,
                () -> filterController.filterByFeatures(null));
    }

    // US-5, AT: A valid reservation is created and stored.
    @Test
    void createsAndStoresReservation() {
        Reservation reservation = manager.createReservation(
                1, date, LocalTime.of(9, 0), LocalTime.of(10, 0));

        assertNotNull(reservation);
        assertEquals(1, manager.getReservations().size());
        assertEquals(1, manager.getReservations().get(0).getSpaceId());
    }

    // US-5, AT: Overlapping reservations are rejected.
    @Test
    void rejectsOverlappingReservation() {
        manager.createReservation(
                1, date, LocalTime.of(9, 0), LocalTime.of(10, 0));

        assertThrows(IllegalArgumentException.class,
                () -> manager.createReservation(
                        1, date, LocalTime.of(9, 30),
                        LocalTime.of(10, 30)));
    }

    // US-6, AT: The day's schedule contains 24 half-hour slots.
    @Test
    void dayScheduleContainsTwentyFourSlots() {
        List<ReservationSchedule.ScheduleSlot> slots =
                schedule.getDaySchedule(1, date);

        assertEquals(24, slots.size());
        assertEquals(LocalTime.of(8, 0), slots.get(0).getStartTime());
        assertEquals(LocalTime.of(19, 30),
                slots.get(23).getStartTime());
    }

    // US-6, AT: A reservation makes its time slot show as reserved.
    @Test
    void reservationAppearsAsReservedInDaySchedule() {
        manager.createReservation(
                1, date, LocalTime.of(9, 0), LocalTime.of(10, 0));

        List<ReservationSchedule.ScheduleSlot> slots =
                schedule.getDaySchedule(1, date);

        ReservationSchedule.ScheduleSlot nineAmSlot = slots.stream()
                .filter(slot -> slot.getStartTime()
                        .equals(LocalTime.of(9, 0)))
                .findFirst()
                .orElseThrow();

        assertTrue(nineAmSlot.isReserved());
        assertEquals("Reserved", nineAmSlot.getStatus());
    }

    // US-6, AT: A date without reservations displays an empty-day message.
    @Test
    void emptyDayHasNoReservationsMessage() {
        assertFalse(schedule.hasReservations(1, date));
        assertEquals(
                "No reservations for this space on this date.",
                schedule.getDayMessage(1, date)
        );
    }
}
