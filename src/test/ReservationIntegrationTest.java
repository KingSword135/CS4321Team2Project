package test;

import model.Reservation;
import model.ReservationManager;
import model.ReservationSchedule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationIntegrationTest {

    private ReservationManager manager;
    private ReservationSchedule schedule;

    private final LocalDate firstDay = LocalDate.of(2026, 11, 2);

    @BeforeEach
    void setUp() {
        manager = new ReservationManager();
        schedule = new ReservationSchedule(manager);
    }

    // US-7, AT: The range includes all dates from start through end.
    @Test
    void rangeScheduleIncludesEveryDate() {
        List<ReservationSchedule.ScheduleSlot> slots =
                schedule.getRangeSchedule(
                        1, firstDay, firstDay.plusDays(2));

        assertEquals(72, slots.size());
        assertEquals(firstDay, slots.get(0).getDate());
        assertEquals(firstDay.plusDays(2),
                slots.get(slots.size() - 1).getDate());
    }

    // US-7, AT: End date cannot be earlier than start date.
    @Test
    void rejectsRangeWithEndBeforeStart() {
        assertThrows(IllegalArgumentException.class,
                () -> schedule.getRangeSchedule(
                        1, firstDay, firstDay.minusDays(1)));
    }

    // US-7, AT: A range cannot exceed seven calendar days.
    @Test
    void rejectsRangeLongerThanSevenDays() {
        assertThrows(IllegalArgumentException.class,
                () -> schedule.getRangeSchedule(
                        1, firstDay, firstDay.plusDays(7)));
    }

    // US-8, AT: Available slots have the Available status.
    @Test
    void emptyScheduleSlotsAreAvailable() {
        List<ReservationSchedule.ScheduleSlot> slots =
                schedule.getDaySchedule(1, firstDay);

        assertTrue(slots.stream()
                .allMatch(ReservationSchedule.ScheduleSlot::isAvailable));
    }

    // US-8, AT: Reserved slots are distinct from available slots.
    @Test
    void reservedAndAvailableSlotsHaveDifferentStatuses() {
        manager.createReservation(
                1, firstDay,
                LocalTime.of(9, 0), LocalTime.of(10, 0));

        List<ReservationSchedule.ScheduleSlot> slots =
                schedule.getDaySchedule(1, firstDay);

        ReservationSchedule.ScheduleSlot reserved = slots.stream()
                .filter(slot -> slot.getStartTime()
                        .equals(LocalTime.of(9, 0)))
                .findFirst()
                .orElseThrow();

        ReservationSchedule.ScheduleSlot available = slots.stream()
                .filter(slot -> slot.getStartTime()
                        .equals(LocalTime.of(11, 0)))
                .findFirst()
                .orElseThrow();

        assertTrue(reserved.isReserved());
        assertTrue(available.isAvailable());
        assertNotEquals(reserved.getStatus(), available.getStatus());
    }

    // US-9, AT: Reservations are returned in date and start-time order.
    @Test
    void reservationsAreSortedChronologically() {
        manager.createReservation(
                1, firstDay.plusDays(1),
                LocalTime.of(10, 0), LocalTime.of(11, 0));

        manager.createReservation(
                1, firstDay,
                LocalTime.of(13, 0), LocalTime.of(14, 0));

        manager.createReservation(
                1, firstDay,
                LocalTime.of(9, 0), LocalTime.of(10, 0));

        List<Reservation> reservations = manager.getReservations();

        assertEquals(3, reservations.size());
        assertEquals(firstDay, reservations.get(0).getDate());
        assertEquals(LocalTime.of(9, 0),
                reservations.get(0).getStartTime());
        assertEquals(LocalTime.of(13, 0),
                reservations.get(1).getStartTime());
        assertEquals(firstDay.plusDays(1),
                reservations.get(2).getDate());
    }

    // US-9, AT: An empty reservation list is returned when none exist.
    @Test
    void returnsEmptyListWhenNoReservationsExist() {
        assertTrue(manager.getReservations().isEmpty());
    }
}
