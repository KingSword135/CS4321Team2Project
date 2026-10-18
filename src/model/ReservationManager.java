
package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ReservationManager {

    private final List<Reservation> reservations;
    private int nextId;

    public ReservationManager() {
        reservations = new ArrayList<>();
        nextId = 1;
    }

    // US-5: Create reservation
    public Reservation createReservation(
            int spaceId,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime) {

        Reservation reservation =
                new Reservation(nextId, spaceId, date, startTime, endTime);

        if (!isTimeAvailable(spaceId, date, startTime, endTime, -1)) {
            throw new IllegalArgumentException(
                    "The selected time is unavailable or does not have a 10-minute buffer.");
        }

        reservations.add(reservation);
        nextId++;

        return reservation;
    }

    // US-10: Cancel reservation
    public void cancelReservation(int reservationId) {
        Reservation reservation = findReservation(reservationId);

        if (reservation == null) {
            throw new IllegalArgumentException("Reservation not found.");
        }

        reservations.remove(reservation);
    }

    // US-11: Modify date and time without changing the space
    public void modifyReservation(
            int reservationId,
            LocalDate newDate,
            LocalTime newStartTime,
            LocalTime newEndTime) {

        Reservation reservation = findReservation(reservationId);

        if (reservation == null) {
            throw new IllegalArgumentException("Reservation not found.");
        }

        modifyReservation(
                reservationId,
                reservation.getSpaceId(),
                newDate,
                newStartTime,
                newEndTime);
    }

    // US-11: Modify space, date, and time
    public void modifyReservation(
            int reservationId,
            int newSpaceId,
            LocalDate newDate,
            LocalTime newStartTime,
            LocalTime newEndTime) {

        Reservation reservation = findReservation(reservationId);

        if (reservation == null) {
            throw new IllegalArgumentException("Reservation not found.");
        }

        // Validate before changing the existing reservation.
        Reservation candidate = new Reservation(
                reservationId,
                newSpaceId,
                newDate,
                newStartTime,
                newEndTime);

        if (!isTimeAvailable(
                newSpaceId,
                newDate,
                newStartTime,
                newEndTime,
                reservationId)) {

            throw new IllegalArgumentException(
                    "The new time is unavailable or does not have a 10-minute buffer.");
        }

        reservation.modify(
                newSpaceId,
                candidate.getDate(),
                candidate.getStartTime(),
                candidate.getEndTime());
    }

    // Find reservation by ID
    public Reservation findReservation(int reservationId) {
        for (Reservation reservation : reservations) {
            if (reservation.getId() == reservationId) {
                return reservation;
            }
        }

        return null;
    }

    // Check overlap and the 10-minute buffer
    private boolean isTimeAvailable(
            int spaceId,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime,
            int ignoredReservationId) {

        if (date == null || startTime == null || endTime == null) {
            return false;
        }

        for (Reservation existing : reservations) {
            if (existing.getId() == ignoredReservationId) {
                continue;
            }

            if (existing.getSpaceId() != spaceId
                    || !existing.getDate().equals(date)) {
                continue;
            }

            LocalTime existingStart = existing.getStartTime();
            LocalTime existingEnd = existing.getEndTime();

            // Require at least 10 minutes between reservations.
            if (startTime.isBefore(existingEnd.plusMinutes(10))
                    && endTime.isAfter(existingStart.minusMinutes(10))) {
                return false;
            }
        }

        return true;
    }

    // US-9: Return reservations sorted by date and start time
    public List<Reservation> getReservations() {
        List<Reservation> sortedReservations =
                new ArrayList<>(reservations);

        sortedReservations.sort(
                Comparator.comparing(Reservation::getDate)
                        .thenComparing(Reservation::getStartTime));

        return sortedReservations;
    }
}
