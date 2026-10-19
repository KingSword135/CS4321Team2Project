package test;

import model.ReservationManager;
import model.ReservationSchedule;
import model.ReservationSchedule.ScheduleSlot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationScheduleTest {

    private ReservationManager manager;
    private ReservationSchedule schedule;
    private LocalDate date;

    @BeforeEach
    void setUp() {
        manager = new ReservationManager();
        schedule = new ReservationSchedule(manager);
        date = LocalDate.of(2026, 10, 20);
    }

    // US-6: View the schedule for one day

    @Test
    void dayScheduleContains24HalfHourSlots() {
        List<ScheduleSlot> slots =
                schedule.getDaySchedule(1001, date);

        assertEquals(24, slots.size());
        assertEquals(LocalTime.of(8, 0),
                slots.get(0).getStartTime());
        assertEquals(LocalTime.of(8, 30),
                slots.get(0).getEndTime());
        assertEquals(LocalTime.of(19, 30),
                slots.get(23).getStartTime());
        assertEquals(LocalTime.of(20, 0),
                slots.get(23).getEndTime());
    }

    @Test
    void emptyDayHasOnlyAvailableSlots() {
        List<ScheduleSlot> slots =
                schedule.getDaySchedule(1001, date);

        assertTrue(slots.stream().allMatch(
                ScheduleSlot::isAvailable));
    }

    @Test
    void emptyDayDisplaysNoReservationsMessage() {
        assertEquals(
                "No reservations for this space on this date.",
                schedule.getDayMessage(1001, date));
    }

    @Test
    void reservationMarksOverlappingSlotsAsReserved() {
        manager.createReservation(
                1001, date,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0));

        List<ScheduleSlot> slots =
                schedule.getDaySchedule(1001, date);

        assertTrue(slots.get(2).isReserved());
        assertTrue(slots.get(3).isReserved());
        assertTrue(slots.get(4).isAvailable());
    }

    @Test
    void reservationNotAlignedToSlotBoundaryIsShown() {
        manager.createReservation(
                1001, date,
                LocalTime.of(9, 10),
                LocalTime.of(9, 20));

        List<ScheduleSlot> slots =
                schedule.getDaySchedule(1001, date);

        ScheduleSlot nineToNineThirty = slots.get(2);

        assertTrue(nineToNineThirty.isReserved());
        assertTrue(nineToNineThirty.getReservationDetails()
                .contains("09:10"));
        assertTrue(nineToNineThirty.getReservationDetails()
                .contains("09:20"));
    }

    @Test
    void reservationsForAnotherSpaceDoNotMarkSlotsReserved() {
        manager.createReservation(
                2001, date,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0));

        List<ScheduleSlot> slots =
                schedule.getDaySchedule(1001, date);

        assertTrue(slots.stream().allMatch(
                ScheduleSlot::isAvailable));
    }

    @Test
    void missingDateIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> schedule.getDaySchedule(1001, null));
    }

    // US-7: View the schedule across a date range

    @Test
    void dateRangeIsInclusiveAndChronological() {
        LocalDate endDate = date.plusDays(2);

        List<ScheduleSlot> slots =
                schedule.getRangeSchedule(1001, date, endDate);

        assertEquals(72, slots.size());
        assertEquals(date, slots.get(0).getDate());
        assertEquals(date.plusDays(1), slots.get(24).getDate());
        assertEquals(endDate, slots.get(48).getDate());
    }

    @Test
    void sevenDayRangeIsAllowed() {
        List<ScheduleSlot> slots =
                schedule.getRangeSchedule(
                        1001, date, date.plusDays(6));

        assertEquals(168, slots.size());
    }

    @Test
    void rangeLongerThanSevenDaysIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> schedule.getRangeSchedule(
                        1001, date, date.plusDays(7)));
    }

    @Test
    void reversedDateRangeIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> schedule.getRangeSchedule(
                        1001, date.plusDays(1), date));
    }

    @Test
    void missingRangeDateIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> schedule.getRangeSchedule(
                        1001, null, date));
    }

    // US-8: Visually distinguish reserved and available time

    @Test
    void reservedAndAvailableSlotsHaveDifferentStatuses() {
        manager.createReservation(
                1001, date,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0));

        List<ScheduleSlot> slots =
                schedule.getDaySchedule(1001, date);

        assertEquals("Reserved", slots.get(2).getStatus());
        assertEquals("Available", slots.get(4).getStatus());
        assertNotEquals(
                slots.get(2).getStatus(),
                slots.get(4).getStatus());
    }
}
