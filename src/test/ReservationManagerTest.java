package test;

import model.Reservation;
import model.ReservationManager;
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

    // US-5: Create reservations

    @Test
    void testCreateReservation() {
        Reservation reservation = manager.createReservation(
                1001, date,
                LocalTime.of(10, 0),
                LocalTime.of(11, 0));

        assertNotNull(reservation);
        assertEquals(1, reservation.getId());
        assertEquals(1001, reservation.getSpaceId());
        assertEquals(date, reservation.getDate());
        assertEquals(1, manager.getReservations().size());
    }

    @Test
    void testCreateMultipleReservations() {
        Reservation first = manager.createReservation(
                1001, date,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0));

        Reservation second = manager.createReservation(
                1001, date,
                LocalTime.of(10, 10),
                LocalTime.of(11, 0));

        assertNotEquals(first.getId(), second.getId());
        assertEquals(2, manager.getReservations().size());
    }

    @Test
    void testRejectOverlappingReservation() {
        manager.createReservation(
                1001, date,
                LocalTime.of(10, 0),
                LocalTime.of(11, 0));

        assertThrows(IllegalArgumentException.class, () ->
                manager.createReservation(
                        1001, date,
                        LocalTime.of(10, 30),
                        LocalTime.of(11, 30)));
    }

    @Test
    void testAllowReservationsInDifferentSpacesAtSameTime() {
        manager.createReservation(
                1001, date,
                LocalTime.of(10, 0),
                LocalTime.of(11, 0));

        assertDoesNotThrow(() ->
                manager.createReservation(
                        2001, date,
                        LocalTime.of(10, 0),
                        LocalTime.of(11, 0)));
    }

    @Test
    void testRejectNullDate() {
        assertThrows(IllegalArgumentException.class, () ->
                manager.createReservation(
                        1001, null,
                        LocalTime.of(10, 0),
                        LocalTime.of(11, 0)));
    }

    @Test
    void testRejectStartBeforeEightAM() {
        assertThrows(IllegalArgumentException.class, () ->
                manager.createReservation(
                        1001, date,
                        LocalTime.of(7, 30),
                        LocalTime.of(8, 30)));
    }

    @Test
    void testRejectEndAfterEightPM() {
        assertThrows(IllegalArgumentException.class, () ->
                manager.createReservation(
                        1001, date,
                        LocalTime.of(19, 30),
                        LocalTime.of(20, 30)));
    }

    @Test
    void testRejectEndEqualToStart() {
        assertThrows(IllegalArgumentException.class, () ->
                manager.createReservation(
                        1001, date,
                        LocalTime.of(10, 0),
                        LocalTime.of(10, 0)));
    }

    @Test
    void testRejectEndBeforeStart() {
        assertThrows(IllegalArgumentException.class, () ->
                manager.createReservation(
                        1001, date,
                        LocalTime.of(11, 0),
                        LocalTime.of(10, 0)));
    }

    @Test
    void testRejectDurationOverTwoHours() {
        assertThrows(IllegalArgumentException.class, () ->
                manager.createReservation(
                        1001, date,
                        LocalTime.of(9, 0),
                        LocalTime.of(11, 1)));
    }

    @Test
    void testAllowExactlyTwoHours() {
        assertDoesNotThrow(() ->
                manager.createReservation(
                        1001, date,
                        LocalTime.of(9, 0),
                        LocalTime.of(11, 0)));
    }

    @Test
    void testRejectBufferUnderTenMinutes() {
        manager.createReservation(
                1001, date,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0));

        assertThrows(IllegalArgumentException.class, () ->
                manager.createReservation(
                        1001, date,
                        LocalTime.of(10, 9),
                        LocalTime.of(11, 0)));
    }

    @Test
    void testAllowExactlyTenMinuteBuffer() {
        manager.createReservation(
                1001, date,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0));

        assertDoesNotThrow(() ->
                manager.createReservation(
                        1001, date,
                        LocalTime.of(10, 10),
                        LocalTime.of(11, 0)));
    }

    // US-9: View reservations

    @Test
    void testGetReservationsReturnsAllReservations() {
        manager.createReservation(
                1001, date,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0));

        manager.createReservation(
                2001, date,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0));

        assertEquals(2, manager.getReservations().size());
    }

    @Test
    void testGetReservationsReturnsEmptyListWhenNoneExist() {
        assertTrue(manager.getReservations().isEmpty());
    }

    @Test
    void testGetReservationsSortedByDateAndStartTime() {
        LocalDate laterDate = date.plusDays(1);

        Reservation later = manager.createReservation(
                1001, laterDate,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0));

        Reservation afternoon = manager.createReservation(
                1001, date,
                LocalTime.of(13, 0),
                LocalTime.of(14, 0));

        Reservation morning = manager.createReservation(
                1001, date,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0));

        List<Reservation> result = manager.getReservations();

        assertEquals(morning.getId(), result.get(0).getId());
        assertEquals(afternoon.getId(), result.get(1).getId());
        assertEquals(later.getId(), result.get(2).getId());
    }

    // US-10: Cancel reservations

    @Test
    void testCancelReservation() {
        Reservation reservation = manager.createReservation(
                1001, date,
                LocalTime.of(10, 0),
                LocalTime.of(11, 0));

        manager.cancelReservation(reservation.getId());

        assertTrue(manager.getReservations().isEmpty());
        assertNull(manager.findReservation(reservation.getId()));
    }

    @Test
    void testCancelNonexistentReservation() {
        assertThrows(IllegalArgumentException.class, () ->
                manager.cancelReservation(999));
    }

    @Test
    void testCanceledTimeCanBeReservedAgain() {
        Reservation reservation = manager.createReservation(
                1001, date,
                LocalTime.of(10, 0),
                LocalTime.of(11, 0));

        manager.cancelReservation(reservation.getId());

        assertDoesNotThrow(() ->
                manager.createReservation(
                        1001, date,
                        LocalTime.of(10, 0),
                        LocalTime.of(11, 0)));
    }

    // US-11: Modify reservations

    @Test
    void testModifyReservationDateAndTime() {
        Reservation reservation = manager.createReservation(
                1001, date,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0));

        LocalDate newDate = date.plusDays(1);

        manager.modifyReservation(
                reservation.getId(),
                newDate,
                LocalTime.of(13, 0),
                LocalTime.of(14, 0));

        assertEquals(newDate, reservation.getDate());
        assertEquals(LocalTime.of(13, 0), reservation.getStartTime());
        assertEquals(LocalTime.of(14, 0), reservation.getEndTime());
    }

    @Test
    void testRejectOverlappingModification() {
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
                        LocalTime.of(13, 10),
                        LocalTime.of(14, 0)));
    }

    @Test
    void testModifyDoesNotConflictWithItself() {
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
    void testRejectInvalidModificationDuration() {
        Reservation reservation = manager.createReservation(
                1001, date,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0));

        assertThrows(IllegalArgumentException.class, () ->
                manager.modifyReservation(
                        reservation.getId(),
                        date,
                        LocalTime.of(9, 0),
                        LocalTime.of(11, 1)));
    }

    @Test
    void testFailedModificationDoesNotChangeReservation() {
        Reservation reservation = manager.createReservation(
                1001, date,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0));

        assertThrows(IllegalArgumentException.class, () ->
                manager.modifyReservation(
                        reservation.getId(),
                        date,
                        LocalTime.of(7, 0),
                        LocalTime.of(8, 0)));

        assertEquals(date, reservation.getDate());
        assertEquals(LocalTime.of(9, 0), reservation.getStartTime());
        assertEquals(LocalTime.of(10, 0), reservation.getEndTime());
    }

    @Test
    void testModifyNonexistentReservation() {
        assertThrows(IllegalArgumentException.class, () ->
                manager.modifyReservation(
                        999,
                        date,
                        LocalTime.of(10, 0),
                        LocalTime.of(11, 0)));
    }
}
