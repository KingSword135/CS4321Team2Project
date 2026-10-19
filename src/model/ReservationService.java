package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ReservationService {

    private final ReservationManager manager;
    private final List<Space> spaces;

    public ReservationUserStoriesService(
            ReservationManager manager, List<Space> spaces) {
        if (manager == null || spaces == null) {
            throw new IllegalArgumentException(
                    "Manager and spaces are required.");
        }

        this.manager = manager;
        this.spaces = new ArrayList<>(spaces);
    }

    // US-9: View all reservations in chronological order.
    public List<Reservation> viewReservations() {
        List<Reservation> result =
                new ArrayList<>(manager.getReservations());

        result.sort(
                Comparator.comparing(Reservation::getDate)
                        .thenComparing(Reservation::getStartTime));

        return result;
    }

    public String getReservationsMessage() {
        if (viewReservations().isEmpty()) {
            return "You have no reservations.";
        }

        return "Reservations found.";
    }

    // US-9: Get the space name for a reservation.
    public String getSpaceName(int spaceId) {
        for (Space space : spaces) {
            if (space.getId() == spaceId) {
                return space.getName();
            }
        }

        return "Unknown space";
    }

    // US-9: Get the features for a reservation's space.
    public List<String> getSpaceFeatures(int spaceId) {
        for (Space space : spaces) {
            if (space.getId() == spaceId) {
                return new ArrayList<>(space.getFeatures());
            }
        }

        return new ArrayList<>();
    }

    // US-11: Modify a reservation's date and time.
    public Reservation modifyReservation(
            int reservationId,
            LocalDate newDate,
            LocalTime newStartTime,
            LocalTime newEndTime) {

        Reservation reservation =
                manager.findReservation(reservationId);

        if (reservation == null) {
            throw new IllegalArgumentException(
                    "Reservation not found.");
        }

        manager.modifyReservation(
                reservationId,
                newDate,
                newStartTime,
                newEndTime);

        return reservation;
    }

    // US-11: Modify the space, date, and time.
    public Reservation modifyReservation(
            int reservationId,
            int newSpaceId,
            LocalDate newDate,
            LocalTime newStartTime,
            LocalTime newEndTime) {

        Reservation reservation =
                manager.findReservation(reservationId);

        if (reservation == null) {
            throw new IllegalArgumentException(
                    "Reservation not found.");
        }

        boolean spaceExists = false;

        for (Space space : spaces) {
            if (space.getId() == newSpaceId) {
                spaceExists = true;
                break;
            }
        }

        if (!spaceExists) {
            throw new IllegalArgumentException(
                    "Space not found.");
        }

        manager.modifyReservation(
                reservationId,
                newSpaceId,
                newDate,
                newStartTime,
                newEndTime);

        return reservation;
    }

    // US-12: Suggest available times for a space and date.
    public List<TimeSuggestion> suggestTimes(
            int spaceId,
            LocalDate date,
            int durationMinutes) {

        if (date == null) {
            throw new IllegalArgumentException(
                    "Date is required.");
        }

        if (durationMinutes < 30
                || durationMinutes > 120
                || durationMinutes % 30 != 0) {
            throw new IllegalArgumentException(
                    "Duration must be 30, 60, 90, or 120 minutes.");
        }

        boolean spaceExists = false;

        for (Space space : spaces) {
            if (space.getId() == spaceId) {
                spaceExists = true;
                break;
            }
        }

        if (!spaceExists) {
            throw new IllegalArgumentException(
                    "Space not found.");
        }

        List<TimeSuggestion> suggestions = new ArrayList<>();
        LocalTime start = LocalTime.of(8, 0);
        LocalTime closingTime = LocalTime.of(20, 0);

        while (!start.plusMinutes(durationMinutes)
                .isAfter(closingTime)) {

            LocalTime end = start.plusMinutes(durationMinutes);

            if (isAvailable(spaceId, date, start, end)) {
                suggestions.add(new TimeSuggestion(start, end));
            }

            start = start.plusMinutes(30);
        }

        return suggestions;
    }

    private boolean isAvailable(
            int spaceId,
            LocalDate date,
            LocalTime start,
            LocalTime end) {

        for (Reservation existing : manager.getReservations()) {
            if (existing.getSpaceId() != spaceId
                    || !existing.getDate().equals(date)) {
                continue;
            }

            LocalTime existingStart = existing.getStartTime();
            LocalTime existingEnd = existing.getEndTime();

            boolean overlaps =
                    start.isBefore(existingEnd)
                    && end.isAfter(existingStart);

            boolean tooCloseAfter =
                    !start.isBefore(existingEnd)
                    && start.isBefore(existingEnd.plusMinutes(10));

            boolean tooCloseBefore =
                    !end.isAfter(existingStart)
                    && end.plusMinutes(10).isAfter(existingStart);

            if (overlaps || tooCloseAfter || tooCloseBefore) {
                return false;
            }
        }

        return true;
    }

    // US-12: Message when no times are available.
    public String getSuggestionMessage(
            int spaceId,
            LocalDate date,
            int durationMinutes) {

        List<TimeSuggestion> suggestions =
                suggestTimes(spaceId, date, durationMinutes);

        if (suggestions.isEmpty()) {
            return "No available times for the selected space and date.";
        }

        return "Available times found: " + suggestions.size();
    }

    // Represents one suggested reservation time.
    public static class TimeSuggestion {
        private final LocalTime startTime;
        private final LocalTime endTime;

        public TimeSuggestion(
                LocalTime startTime, LocalTime endTime) {
            this.startTime = startTime;
            this.endTime = endTime;
        }

        public LocalTime getStartTime() {
            return startTime;
        }

        public LocalTime getEndTime() {
            return endTime;
        }

        @Override
        public String toString() {
            return startTime + " - " + endTime;
        }
    }
}
