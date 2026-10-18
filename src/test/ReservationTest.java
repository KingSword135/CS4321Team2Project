package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationManagerTest {

    private ReservationManager manager;
    private LocalDate date;

    @BeforeEach
    void setUp() {
        manager = new ReservationManager();
        date = LocalDate.of(2026, 10, 20);
    }

    // US-5: Create a reservation

    @Test
    void createReservationSuccessfully() {
        Reservation reservation = manager.createReservation(
                1001, date,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0));

        assertNotNull(reservation);
        assertEquals(1, reservation.getId());
        assertEquals(1001, reservation.getSpaceId());
        assertEquals(date, reservation.getDate());
        assertEquals(LocalTime.of(9, 0), reservation.getStartTime());
        assertEquals(LocalTime.of(10, 0), reservation.getEndTime());
    }

    @Test
    void createdReservationAppearsInReservationList() {
        manager.createReservation(
                1001, date,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0));

        assertEquals(1, manager.getReservations().size());
    }

    @Test
    void overlappingReservationIsRejected() {
        manager.createReservation(
                1001, date,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0));

        assertThrows(IllegalArgumentException.class, () ->
                manager.createReservation(
                        1001, date,
                        LocalTime.of(9, 30),
                        LocalTime.of(10, 30)));
    }

    @Test
    void sameTimeForDifferentSpaceIsAllowed() {
        manager.createReservation(
                1001, date,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0));

        assertDoesNotThrow(() ->
                manager.createReservation(
                        2001, date,
                        LocalTime.of(9, 0),
                        LocalTime.of(10, 0)));
    }

    @Test
    void missingDateIsRejected() {
        assertThrows(IllegalArgumentException.class, () ->
                manager.createReservation(
                        1001, null,
                        LocalTime.of(9, 0),
                        LocalTime.of(10, 0)));
    }

    // US-10: Cancel a reservation

    @Test
    void cancelReservationRemovesItFromList() {
        Reservation reservation = manager.createReservation(
                1001, date,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0));

        manager.cancelReservation(reservation.getId());

        assertTrue(manager.getReservations().isEmpty());
        assertNull(manager.findReservation(reservation.getId()));
    }

    @Test
    void cancellingUnknownReservationIsRejected() {
        assertThrows(IllegalArgumentException.class, () ->
                manager.cancelReservation(999));
    }

    @Test
    void cancelledTimeCanBeReservedAgain() {
        Reservation reservation = manager.createReservation(
                1001, date,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0));

        manager.cancelReservation(reservation.getId());

        assertDoesNotThrow(() ->
                manager.createReservation(
                        1001, date,
                        LocalTime.of(9, 0),
                        LocalTime.of(10, 0)));
    }

    // US-11: Modify a reservation

    @Test
    void modifyReservationChangesDateAndTimes() {
        Reservation reservation = manager.createReservation(
                1001, date,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0));

        LocalDate newDate = date.plusDays(1);

        manager.modifyReservation(
                reservation.getId(),
                newDate,
                LocalTime.of(11, 0),
                LocalTime.of(12, 0));

        assertEquals(newDate, reservation.getDate());
        assertEquals(LocalTime.of(11, 0), reservation.getStartTime());
        assertEquals(LocalTime.of(12, 0), reservation.getEndTime());
    }

    @Test
    void modifyingToOverlappingTimeIsRejected() {
        Reservation first = manager.createReservation(
                1001, date,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0));

        manager.createReservation(
                1001, date,
                LocalTime.of(13, 0),
                LocalTime.of(14, 0));

        assertThrows(IllegalArgumentException.class, () ->
                manager.modifyReservation(
                        first.getId(),
                        date,
                        LocalTime.of(13, 0),
                        LocalTime.of(14, 0)));
    }

    @Test
    void modifyingReservationDoesNotConflictWithItself() {
        Reservation reservation = manager.createReservation(
                1001, date,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0));

        assertDoesNotThrow(() ->
                manager.modifyReservation(
                        reservation.getId(),
                        date,
                        LocalTime.of(9, 0),
                        LocalTime.of(10, 0)));
    }

    @Test
    void modifyingUnknownReservationIsRejected() {
        assertThrows(IllegalArgumentException.class, () ->
                manager.modifyReservation(
                        999,
                        date,
                        LocalTime.of(9, 0),
                        LocalTime.of(10, 0)));
    }

    @Test
    void failedModificationDoesNotChangeOriginalReservation() {
        Reservation reservation = manager.createReservation(
                1001, date,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0));

        manager.createReservation(
                1001, date,
                LocalTime.of(13, 0),
                LocalTime.of(14, 0));

        assertThrows(IllegalArgumentException.class, () ->
                manager.modifyReservation(
                        reservation.getId(),
                        date,
                        LocalTime.of(13, 0),
                        LocalTime.of(14, 0)));

        assertEquals(LocalTime.of(9, 0), reservation.getStartTime());
        assertEquals(LocalTime.of(10, 0), reservation.getEndTime());
    }

    // Reservation lookup and list behavior

    @Test
    void findReservationReturnsNullForUnknownId() {
        assertNull(manager.findReservation(999));
    }

    @Test
    void getReservationsReturnsAllReservations() {
        manager.createReservation(
                1001, date,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0));

        manager.createReservation(
                2001, date,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0));

        List<Reservation> reservations = manager.getReservations();

        assertEquals(2, reservations.size());
    }
}

