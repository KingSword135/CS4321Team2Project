package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ReservationSchedule {

    private static final LocalTime OPEN = LocalTime.of(8, 0);
    private static final LocalTime CLOSE = LocalTime.of(20, 0);
    private static final int SLOT_MINUTES = 30;
    private static final int MAX_DAYS = 7;

    private final ReservationManager manager;

    public ReservationSchedule(ReservationManager manager) {
        if (manager == null) {
            throw new IllegalArgumentException(
                    "Reservation manager is required.");
        }
        this.manager = manager;
    }

    public List<ScheduleSlot> getDaySchedule(
            int spaceId, LocalDate date) {

        if (date == null) {
            throw new IllegalArgumentException("Date is required.");
        }

        return buildSchedule(spaceId, date, date);
    }

    public List<ScheduleSlot> getRangeSchedule(
            int spaceId, LocalDate startDate, LocalDate endDate) {

        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException(
                    "Start date and end date are required.");
        }

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException(
                    "End date cannot be before start date.");
        }

        long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;

        if (days > MAX_DAYS) {
            throw new IllegalArgumentException(
                    "Date range cannot exceed 7 days.");
        }

        return buildSchedule(spaceId, startDate, endDate);
    }

    private List<ScheduleSlot> buildSchedule(
            int spaceId, LocalDate startDate, LocalDate endDate) {

        List<Reservation> reservations = manager.getReservations();
        List<ScheduleSlot> slots = new ArrayList<>();

        for (LocalDate date = startDate;
             !date.isAfter(endDate);
             date = date.plusDays(1)) {

            for (LocalTime start = OPEN;
                 start.isBefore(CLOSE);
                 start = start.plusMinutes(SLOT_MINUTES)) {

                LocalTime end = start.plusMinutes(SLOT_MINUTES);

                List<Reservation> overlapping = new ArrayList<>();

                for (Reservation reservation : reservations) {
                    if (reservation.getSpaceId() == spaceId
                            && reservation.getDate().equals(date)
                            && start.isBefore(reservation.getEndTime())
                            && end.isAfter(reservation.getStartTime())) {
                        overlapping.add(reservation);
                    }
                }

                overlapping.sort(
                        Comparator.comparing(Reservation::getStartTime));

                slots.add(new ScheduleSlot(
                        date, start, end,
                        overlapping.isEmpty() ? "Available" : "Reserved",
                        overlapping));
            }
        }

        return slots;
    }

    public static class ScheduleSlot {
        private final LocalDate date;
        private final LocalTime startTime;
        private final LocalTime endTime;
        private final String status;
        private final List<Reservation> reservations;

        public ScheduleSlot(LocalDate date, LocalTime startTime,
                            LocalTime endTime, String status,
                            List<Reservation> reservations) {
            this.date = date;
            this.startTime = startTime;
            this.endTime = endTime;
            this.status = status;
            this.reservations = new ArrayList<>(reservations);
        }

        public LocalDate getDate() {
            return date;
        }

        public LocalTime getStartTime() {
            return startTime;
        }

        public LocalTime getEndTime() {
            return endTime;
        }

        public String getStatus() {
            return status;
        }

        public List<Reservation> getReservations() {
            return new ArrayList<>(reservations);
        }

        public String getReservationDetails() {
            if (reservations.isEmpty()) {
                return "";
            }

            StringBuilder details = new StringBuilder();

            for (Reservation reservation : reservations) {
                if (!details.isEmpty()) {
                    details.append("; ");
                }

                details.append(reservation.getStartTime())
                        .append("–")
                        .append(reservation.getEndTime());
            }

            return details.toString();
        }
    }
}
