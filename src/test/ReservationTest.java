```java
package test;

import model.Reservation;
import model.ReservationManager;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class ReservationTest {

    @Test
    void CreateReservationWithAllParameters() {
        Reservation reservation = new Reservation(
                1,
                1001,
                Date.valueOf("2026-10-20"),
                LocalTime.of(10, 0),
                LocalTime.of(11, 0)
        );

        assertEquals(1, reservation.getId());
        assertEquals(1001, reservation.getSpaceId());
        assertEquals(Date.valueOf("2026-10-20"), reservation.getDate());
        assertEquals(LocalTime.of(10, 0), reservation.getStartTime());
        assertEquals(LocalTime.of(11, 0), reservation.getEndTime());
    }

    @Test
    void testCreateReservation() {
        ReservationManager manager = new ReservationManager();

        Reservation reservation = manager.createReservation(
                1001,
                Date.valueOf("2026-10-20"),
                LocalTime.of(10, 0),
                LocalTime.of(11, 0)
        );

        assertNotNull(reservation);
        assertEquals(1, manager.getReservations().size());
    }

    @Test
    void testCancelReservation() {
        ReservationManager manager = new ReservationManager();

        Reservation reservation = manager.createReservation(
                1001,
                Date.valueOf("2026-10-20"),
                LocalTime.of(10, 0),
                LocalTime.of(11, 0)
        );

        manager.cancelReservation(reservation.getId());

        assertTrue(manager.getReservations().isEmpty());
    }

    @Test
    void testModifyReservation() {
        ReservationManager manager = new ReservationManager();

        Reservation reservation = manager.createReservation(
                1001,
                Date.valueOf("2026-10-20"),
                LocalTime.of(10, 0),
                LocalTime.of(11, 0)
        );

        Date newDate = Date.valueOf("2026-10-21");

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
    void testRejectOverlappingReservation() {
        ReservationManager manager = new ReservationManager();

        manager.createReservation(
                1001,
                Date.valueOf("2026-10-20"),
                LocalTime.of(10, 0),
                LocalTime.of(11, 0)
        );

        assertThrows(IllegalArgumentException.class, () ->
                manager.createReservation(
                        1001,
                        Date.valueOf("2026-10-20"),
                        LocalTime.of(10, 30),
                        LocalTime.of(11, 30)
                )
        );
    }
}
```
