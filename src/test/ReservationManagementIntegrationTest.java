package test;

import model.Reservation;
import model.ReservationManager;
import model.ReservationUserStoriesService;
import model.Space;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationManagementIntegrationTest {

    private ReservationManager manager;
    private ReservationUserStoriesService service;

    private final LocalDate date = LocalDate.of(2026, 11, 2);

    @BeforeEach
    void setUp() {
        manager = new ReservationManager();

        List<Space> spaces = List.of(
                new Space(1, "Study Room", "Library", 6,
                        List.of("Whiteboard")),
                new Space(2, "Computer Lab", "Nevins", 25,
                        List.of("Whiteboard", "Projector"))
        );

        service = new ReservationUserStoriesService(manager, spaces);
    }

    // US-10, AT: Canceling a reservation removes it from the list.
    @Test
    void cancelRemovesReservation() {
        Reservation reservation = manager.createReservation(
                1, date, LocalTime.of(9, 0), LocalTime.of(10, 0));

        manager.cancelReservation(reservation.getId());

        assertTrue(manager.getReservations().isEmpty());
        assertNull(manager.findReservation(reservation.getId()));
    }

    // US-10, AT: Canceling an unknown reservation is rejected.
    @Test
    void cancelRejectsUnknownReservation() {
        assertThrows(IllegalArgumentException.class,
                () -> manager.cancelReservation(999));
    }

    // US-10, AT: Canceling a reservation frees its time slot.
    @Test
    void canceledTimeCanBeReservedAgain() {
        Reservation original = manager.createReservation(
                1, date, LocalTime.of(9, 0), LocalTime.of(10, 0));

        manager.cancelReservation(original.getId());

        Reservation replacement = manager.createReservation(
                1, date, LocalTime.of(9, 0), LocalTime.of(10, 0));

        assertNotNull(replacement);
        assertEquals(1, manager.getReservations().size());
    }

    // US-11, AT: A reservation can be moved to a new date and time.
    @Test
    void modifyChangesDateAndTime() {
        Reservation reservation = manager.createReservation(
                1, date, LocalTime.of(9, 0), LocalTime.of(10, 0));

        LocalDate newDate = date.plusDays(1);

        manager.modifyReservation(
                reservation.getId(),
                newDate,
                LocalTime.of(11, 0),
                LocalTime.of(12, 0));

        Reservation updated = manager.findReservation(reservation.getId());

        assertEquals(newDate, updated.getDate());
        assertEquals(LocalTime.of(11, 0), updated.getStartTime());
        assertEquals(LocalTime.of(12, 0), updated.getEndTime());
    }

    // US-11, AT: A reservation can be moved to another space.
    @Test
    void modifyChangesSpace() {
        Reservation reservation = manager.createReservation(
                1, date, LocalTime.of(9, 0), LocalTime.of(10, 0));

        manager.modifyReservation(
                reservation.getId(),
                2,
                date,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0));

        assertEquals(2,
                manager.findReservation(reservation.getId()).getSpaceId());
    }

    // US-11, AT: An invalid modification is rejected without changing the reservation.
    @Test
    void invalidModificationKeepsOriginalReservation() {
        Reservation reservation = manager.createReservation(
                1, date, LocalTime.of(9, 0), LocalTime.of(10, 0));

        assertThrows(IllegalArgumentException.class,
                () -> manager.modifyReservation(
                        reservation.getId(),
                        date,
                        LocalTime.of(11, 0),
                        LocalTime.of(10, 0)));

        Reservation unchanged = manager.findReservation(reservation.getId());

        assertEquals(LocalTime.of(9, 0), unchanged.getStartTime());
        assertEquals(LocalTime.of(10, 0), unchanged.getEndTime());
    }

    // US-12, AT: Suggested times are returned in chronological order.
    @Test
    void suggestedTimesAreChronological() {
        List<ReservationUserStoriesService.TimeSuggestion> suggestions =
                service.suggestTimes(1, date, 60);

        assertFalse(suggestions.isEmpty());

        for (int i = 1; i < suggestions.size(); i++) {
            assertFalse(
                    suggestions.get(i).getStartTime().isBefore(
                            suggestions.get(i - 1).getStartTime())
            );
        }
    }

    // US-12, AT: Suggested times do not overlap existing reservations.
    @Test
    void suggestionsDoNotOverlapExistingReservations() {
        manager.createReservation(
                1, date, LocalTime.of(9, 0), LocalTime.of(10, 0));

        List<ReservationUserStoriesService.TimeSuggestion> suggestions =
                service.suggestTimes(1, date, 60);

        for (ReservationUserStoriesService.TimeSuggestion suggestion
                : suggestions) {
            boolean overlaps = suggestion.getStartTime()
                    .isBefore(LocalTime.of(10, 0))
                    && suggestion.getEndTime()
                    .isAfter(LocalTime.of(9, 0));

            assertFalse(overlaps);
        }
    }

    // US-12, AT: Invalid requested duration is rejected.
    @Test
    void suggestionsRejectInvalidDuration() {
        assertThrows(IllegalArgumentException.class,
                () -> service.suggestTimes(1, date, 180));
    }

    // US-12, AT: A missing date is rejected.
    @Test
    void suggestionsRequireDate() {
        assertThrows(IllegalArgumentException.class,
                () -> service.suggestTimes(1, null, 60));
    }
}
