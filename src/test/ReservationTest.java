package test;

import model.Reservation;
import model.ReservationManager;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class ReservationTest {

    @Test
    void testCreateReservation() {
        ReservationManager manager = new ReservationManager();

        Reservation reservation = manager.createReservation(
                1001,
                LocalDate.of(2026, 10, 20),
                LocalTime.of(10, 0),
                LocalTime.of(11, 0)
        );

        assertNotNull(reservation);
        assertEquals(1, reservation.getId());
        assertEquals(1001, reservation.getSpaceId());
        assertEquals(1, manager.getReservations().size());
    }

    @Test
    void testRejectOverlappingReservation() {
        ReservationManager manager = new ReservationManager();
        LocalDate date = LocalDate.of(2026, 10, 20);

        manager.createReservation(
                1001, date,
                LocalTime.of(10, 0),
                LocalTime.of(11, 0)
        );

        assertThrows(IllegalArgumentException.class, () ->
                manager.createReservation(
                        1001, date,
                        LocalTime.of(10, 30),
                        LocalTime.of(11, 30)
                )
        );
    }

    @Test
    void testCancelReservation() {
        ReservationManager manager = new ReservationManager();

        Reservation reservation = manager.createReservation(
                1001,
                LocalDate.of(2026, 10, 20),
                LocalTime.of(10, 0),
                LocalTime.of(11, 0)
        );

        manager.cancelReservation(reservation.getId());

        assertTrue(manager.getReservations().isEmpty());
    }

    @Test
    void testCancelNonexistentReservation() {
        ReservationManager manager = new ReservationManager();

        assertThrows(IllegalArgumentException.class, () ->
                manager.cancelReservation(999)
        );
    }

    @Test
    void testModifyReservation() {
        ReservationManager manager = new ReservationManager();

        Reservation reservation = manager.createReservation(
                1001,
                LocalDate.of(2026, 10, 20),
                LocalTime.of(10, 0),
                LocalTime.of(11, 0)
        );

        LocalDate newDate = LocalDate.of(2026, 10, 20);

        manager.modifyReservation(
                reservation.getId(),
                newDate,
                LocalTime.of(13, 0),
                LocalTime.of(14, 0)
        );

        assertEquals(newDate, reservation.getDate());
        assertEquals(LocalTime.of(13, 0), reservation.getStartTime());
        assertEquals(LocalTime.of(14, 0), reservation.getEndTime());
    }

    @Test
    void testRejectOverlappingModification() {
        ReservationManager manager = new ReservationManager();
        LocalDate Date = LocalDate.of(2026, 10, 20);

        Reservation first = manager.createReservation(
                1001, Date,
                LocalTime.of(10, 0),
                LocalTime.of(11, 0)
        );

        manager.createReservation(
                1001, Date,
                LocalTime.of(13, 0),
                LocalTime.of(14, 0)
        );

        assertThrows(IllegalArgumentException.class, () ->
                manager.modifyReservation(
                        first.getId(),
                        Date,
                        LocalTime.of(13, 30),
                        LocalTime.of(14, 30)
                )
        );
    }
}