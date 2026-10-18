package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class ReservationTest {

    private Reservation reservation;
    private LocalDate date;

    @BeforeEach
    void setUp() {
        date = LocalDate.of(2026, 10, 20);

        reservation = new Reservation(
                1,
                1001,
                date,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0)
        );
    }

    // US-5: Create a valid reservation

    @Test
    void acceptsValidReservation() {
        assertEquals(1, reservation.getId());
        assertEquals(1001, reservation.getSpaceId());
        assertEquals(date, reservation.getDate());
        assertEquals(LocalTime.of(9, 0), reservation.getStartTime());
        assertEquals(LocalTime.of(10, 0), reservation.getEndTime());
    }

    @Test
    void rejectsStartTimeBeforeEightAm() {
        assertThrows(IllegalArgumentException.class, () ->
                new Reservation(
                        2, 1001, date,
                        LocalTime.of(7, 59),
                        LocalTime.of(9, 0)));
    }

    @Test
    void allowsStartTimeAtEightAm() {
        assertDoesNotThrow(() ->
                new Reservation(
                        2, 1001, date,
                        LocalTime.of(8, 0),
                        LocalTime.of(9, 0)));
    }

    @Test
    void rejectsEndTimeAfterEightPm() {
        assertThrows(IllegalArgumentException.class, () ->
                new Reservation(
                        2, 1001, date,
                        LocalTime.of(19, 0),
                        LocalTime.of(20, 1)));
    }

    @Test
    void allowsEndTimeAtEightPm() {
        assertDoesNotThrow(() ->
                new Reservation(
                        2, 1001, date,
                        LocalTime.of(19, 0),
                        LocalTime.of(20, 0)));
    }

    @Test
    void rejectsEndTimeBeforeStartTime() {
        assertThrows(IllegalArgumentException.class, () ->
                new Reservation(
                        2, 1001, date,
                        LocalTime.of(11, 0),
                        LocalTime.of(10, 0)));
    }

    @Test
    void rejectsEndTimeEqualToStartTime() {
        assertThrows(IllegalArgumentException.class, () ->
                new Reservation(
                        2, 1001, date,
                        LocalTime.of(10, 0),
                        LocalTime.of(10, 0)));
    }

    @Test
    void rejectsReservationLongerThanTwoHours() {
        assertThrows(IllegalArgumentException.class, () ->
                new Reservation(
                        2, 1001, date,
                        LocalTime.of(9, 0),
                        LocalTime.of(11, 1)));
    }

    @Test
    void allowsReservationExactlyTwoHoursLong() {
        assertDoesNotThrow(() ->
                new Reservation(
                        2, 1001, date,
                        LocalTime.of(9, 0),
                        LocalTime.of(11, 0)));
    }

    @Test
    void rejectsNullStartTime() {
        assertThrows(IllegalArgumentException.class, () ->
                new Reservation(
                        2, 1001, date,
                        null,
                        LocalTime.of(10, 0)));
    }

    @Test
    void rejectsNullEndTime() {
        assertThrows(IllegalArgumentException.class, () ->
                new Reservation(
                        2, 1001, date,
                        LocalTime.of(9, 0),
                        null));
    }

    // US-11: Modify a reservation using valid times

    @Test
    void modifiesReservationWithValidDateAndTimes() {
        LocalDate newDate = date.plusDays(1);

        reservation.modify(
                newDate,
                LocalTime.of(11, 0),
                LocalTime.of(12, 0));

        assertEquals(newDate, reservation.getDate());
        assertEquals(LocalTime.of(11, 0), reservation.getStartTime());
        assertEquals(LocalTime.of(12, 0), reservation.getEndTime());
    }

    @Test
    void rejectsModificationWithEndBeforeStart() {
        assertThrows(IllegalArgumentException.class, () ->
                reservation.modify(
                        date,
                        LocalTime.of(11, 0),
                        LocalTime.of(10, 0)));
    }

    @Test
    void rejectsModificationLongerThanTwoHours() {
        assertThrows(IllegalArgumentException.class, () ->
                reservation.modify(
                        date,
                        LocalTime.of(9, 0),
                        LocalTime.of(11, 1)));
    }

    @Test
    void rejectsModificationOutsideBusinessHours() {
        assertThrows(IllegalArgumentException.class, () ->
                reservation.modify(
                        date,
                        LocalTime.of(7, 0),
                        LocalTime.of(8, 0)));
    }

    @Test
    void rejectsModificationWithNullDate() {
        assertThrows(IllegalArgumentException.class, () ->
                reservation.modify(
                        null,
                        LocalTime.of(11, 0),
                        LocalTime.of(12, 0)));
    }
}
