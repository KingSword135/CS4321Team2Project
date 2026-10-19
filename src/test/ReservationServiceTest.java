package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationServiceTest {

    private ReservationManager manager;
    private ReservationUserStoriesService service;
    private LocalDate date;

    @BeforeEach
    void setUp() {
        manager = new ReservationManager();
        date = LocalDate.of(2026, 11, 2);

        List<Space> spaces = List.of(
                new Space(1, "Study Room A", "Library", 4,
                        List.of("Whiteboard", "Computer")),
                new Space(2, "Study Room B", "Library", 6,
                        List.of("Projector"))
        );

        service = new ReservationUserStoriesService(manager, spaces);
    }

    // US-9: View reservations

    @Test
    void viewReservationsReturnsReservations() {
        manager.createReservation(
                1, date, LocalTime.of(9, 0), LocalTime.of(10, 0));

        List<Reservation> reservations = service.viewReservations();

        assertEquals(1, reservations.size());
        assertEquals(1, reservations.get(0).getSpaceId());
    }

    @Test
    void viewReservationsAreSortedByDateAndStartTime() {
        manager.createReservation(
                1, date.plusDays(1),
                LocalTime.of(9, 0), LocalTime.of(10, 0));

        manager.createReservation(
                1, date,
                LocalTime.of(11, 0), LocalTime.of(12, 0));

        manager.createReservation(
                2, date,
                LocalTime.of(9, 0), LocalTime.of(10, 0));

        List<Reservation> reservations = service.viewReservations();

        assertEquals(3, reservations.size());
        assertEquals(date, reservations.get(0).getDate());
        assertEquals(LocalTime.of(9, 0),
                reservations.get(0).getStartTime());
        assertEquals(LocalTime.of(11, 0),
                reservations.get(1).getStartTime());
        assertEquals(date.plusDays(1), reservations.get(2).getDate());
    }

    @Test
    void emptyReservationsShowsMessage() {
        assertTrue(service.viewReservations().isEmpty());
        assertEquals("You have no reservations.",
                service.getReservationsMessage());
    }

    @Test
    void getsSpaceNameAndFeatures() {
        assertEquals("Study Room A", service.getSpaceName(1));
        assertEquals(
                List.of("Whiteboard", "Computer"),
                service.getSpaceFeatures(1));
    }

    // US-11: Modify reservations

    @Test
    void modifiesReservationDateAndTime() {
        Reservation original = manager.createReservation(
                1, date,
                LocalTime.of(9, 0), LocalTime.of(10, 0));

        Reservation modified = service.modifyReservation(
                original.getId(),
                date.plusDays(1),
                LocalTime.of(11, 0),
                LocalTime.of(12, 0));

        assertEquals(date.plusDays(1), modified.getDate());
        assertEquals(LocalTime.of(11, 0), modified.getStartTime());
        assertEquals(LocalTime.of(12, 0), modified.getEndTime());
    }

    @Test
    void modifiesReservationSpace() {
        Reservation original = manager.createReservation(
                1, date,
                LocalTime.of(9, 0), LocalTime.of(10, 0));

        Reservation modified = service.modifyReservation(
                original.getId(),
                2,
                date,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0));

        assertEquals(2, modified.getSpaceId());
    }

    @Test
    void rejectsModificationWhenReservationDoesNotExist() {
        assertThrows(IllegalArgumentException.class,
                () -> service.modifyReservation(
                        999,
                        date,
                        LocalTime.of(9, 0),
                        LocalTime.of(10, 0)));
    }

    @Test
    void rejectsModificationThatConflictsWithAnotherReservation() {
        Reservation first = manager.createReservation(
                1, date,
                LocalTime.of(9, 0), LocalTime.of(10, 0));

        manager.createReservation(
                1, date,
                LocalTime.of(11, 0), LocalTime.of(12, 0));

        assertThrows(IllegalArgumentException.class,
                () -> service.modifyReservation(
                        first.getId(),
                        date,
                        LocalTime.of(11, 0),
                        LocalTime.of(12, 0)));
    }

    // US-12: Suggest available times

    @Test
    void suggestsAvailableTimesInChronologicalOrder() {
        List<ReservationUserStoriesService.TimeSuggestion> suggestions =
                service.suggestTimes(1, date, 60);

        assertFalse(suggestions.isEmpty());

        assertEquals(LocalTime.of(8, 0),
                suggestions.get(0).getStartTime());

        for (int i = 1; i < suggestions.size(); i++) {
            assertTrue(
                    suggestions.get(i).getStartTime()
                            .isAfter(suggestions.get(i - 1).getStartTime()));
        }
    }

    @Test
    void suggestionsDoNotOverlapExistingReservations() {
        manager.createReservation(
                1, date,
                LocalTime.of(9, 0), LocalTime.of(10, 0));

        List<ReservationUserStoriesService.TimeSuggestion> suggestions =
                service.suggestTimes(1, date, 60);

        for (ReservationUserStoriesService.TimeSuggestion suggestion
                : suggestions) {
            assertFalse(
                    suggestion.getStartTime().isBefore(LocalTime.of(10, 10))
                    && suggestion.getEndTime().isAfter(LocalTime.of(8, 50)));
        }
    }

    @Test
    void suggestionsStayWithinBusinessHours() {
        List<ReservationUserStoriesService.TimeSuggestion> suggestions =
                service.suggestTimes(1, date, 120);

        for (ReservationUserStoriesService.TimeSuggestion suggestion
                : suggestions) {
            assertFalse(suggestion.getStartTime()
                    .isBefore(LocalTime.of(8, 0)));
            assertFalse(suggestion.getEndTime()
                    .isAfter(LocalTime.of(20, 0)));
        }
    }

    @Test
    void rejectsInvalidSuggestionDuration() {
        assertThrows(IllegalArgumentException.class,
                () -> service.suggestTimes(1, date, 45));
    }

    @Test
    void rejectsMissingSuggestionDate() {
        assertThrows(IllegalArgumentException.class,
                () -> service.suggestTimes(1, null, 60));
    }

    @Test
    void reportsWhenNoTimesAreAvailable() {
        // Reserve the full business day.
        manager.createReservation(
                1, date,
                LocalTime.of(8, 0), LocalTime.of(10, 0));

        manager.createReservation(
                1, date,
                LocalTime.of(10, 10), LocalTime.of(12, 10));

        manager.createReservation(
                1, date,
                LocalTime.of(12, 20), LocalTime.of(14, 20));

        manager.createReservation(
                1, date,
                LocalTime.of(14, 30), LocalTime.of(16, 30));

        manager.createReservation(
                1, date,
                LocalTime.of(16, 40), LocalTime.of(18, 40));

        manager.createReservation(
                1, date,
                LocalTime.of(18, 50), LocalTime.of(20, 0));

        assertTrue(service.suggestTimes(1, date, 60).isEmpty());

        assertEquals(
                "No available times for the selected space and date.",
                service.getSuggestionMessage(1, date, 60));
    }
}
