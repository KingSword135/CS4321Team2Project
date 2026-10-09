package model;

import java.sql.Date;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ReservationManager {

    private final List<Reservation> reservations;
    private int nextId;

    public ReservationManager() {
        reservations = new ArrayList<>();
        nextId = 1;
    }

    // US-5: Create a reservation
    public Reservation createReservation(int spaceId, Date date, LocalTime startTime, LocalTime endTime) {
        if (!isTimeAvailable(spaceId, date, startTime, endTime)) {
            throw new IllegalArgumentException("The selected time is not available.");
        }

        Reservation reservation = new Reservation(nextId, spaceId, date, startTime, endTime);
        reservations.add(reservation);
        nextId++;

        return reservation;
    }

    // US-10: Cancel a reservation
    public void cancelReservation(int reservationId) {
        Reservation reservation = findReservation(reservationId);

        if (reservation == null) {
            throw new IllegalArgumentException("Reservation not found.");
        }

        reservations.remove(reservation);
    }

    // US-11: Modify a reservation
    public void modifyReservation(int reservationId, Date newDate, LocalTime newStartTime, LocalTime newEndTime) {
        Reservation reservation = findReservation(reservationId);

        if (reservation == null) {
            throw new IllegalArgumentException("Reservation not found.");
        }

        if (!isTimeAvailable(reservation.getSpaceId(), newDate, newStartTime, newEndTime, reservationId)) {
            throw new IllegalArgumentException("The new time is not available.");
        }

        reservation.modify(newDate, newStartTime, newEndTime);
    }

    public Reservation findReservation(int reservationId) {
        for (Reservation reservation : reservations) {
            if (reservation.getId() == reservationId) {
                return reservation;
            }
        }

        return null;
    }

    private boolean isTimeAvailable(int spaceId, Date date, LocalTime startTime, LocalTime endTime) {
        return isTimeAvailable(spaceId, date, startTime, endTime, -1);
    }

    private boolean isTimeAvailable(int spaceId, Date date, LocalTime startTime, LocalTime endTime,
                                    int ignoredReservationId) {
        for (Reservation reservation : reservations) {
            if (reservation.getId() == ignoredReservationId) {
                continue;
            }

            if (reservation.getSpaceId() == spaceId && reservation.getDate().equals(date)) {
                boolean overlaps = startTime.isBefore(reservation.getEndTime())
                        && endTime.isAfter(reservation.getStartTime());

                if (overlaps) {
                    return false;
                }
            }
        }

        return true;
    }

    public List<Reservation> getReservations() {
        return new ArrayList<>(reservations);
    }
}
