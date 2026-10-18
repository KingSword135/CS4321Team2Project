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

    @Test
    void dayScheduleContains24HalfHourSlots() {
        List<ScheduleSlot> slots =
                schedule.getDaySchedule(1001, date);

        assertEquals(24, slots.size());
        assertEquals(LocalTime.of(8, 0),
                slots.get(0).getStartTime());
        assertEquals(LocalTime.of(20, 0),
                slots.get(23).getEndTime());
    }

    @Test
    void emptyDayHasOnlyAvailableSlots() {
        List<ScheduleSlot> slots =
                schedule.getDaySchedule(1001, date);

        assertTrue(slots.stream().allMatch(
                slot -> slot.getStatus().equals("Available")));
    }

    @Test
    void reservationMarksOverlappingSlotAsReserved() {
        manager.createReservation(
                1001, date,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0));

        List<ScheduleSlot> slots =
                schedule.getDaySchedule(1001, date);

        ScheduleSlot nineToNineThirty = slots.get(2);
        ScheduleSlot nineThirtyToTen = slots.get(3);
        ScheduleSlot tenToTenThirty = slots.get(4);

        assertEquals("Reserved", nineToNineThirty.getStatus());
        assertEquals("Reserved", nineThirtyToTen.getStatus());
        assertEquals("Available", tenToTenThirty.getStatus());
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

        assertEquals("Reserved", nineToNineThirty.getStatus());
        assertEquals("09:10–09:20",
                nineToNineThirty.getReservationDetails());
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
                slot -> slot.getStatus().equals("Available")));
    }

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
        assertThrows(IllegalArgumentException.class, () ->
                schedule.getRangeSchedule(
                        1001, date, date.plusDays(7)));
    }

    @Test
    void reversedDateRangeIsRejected() {
        assertThrows(IllegalArgumentException.class, () ->
                schedule.getRangeSchedule(
                        1001, date.plusDays(1), date));
    }

    @Test
    void missingDateIsRejected() {
        assertThrows(IllegalArgumentException.class, () ->
                schedule.getDaySchedule(1001, null));
    }
}
