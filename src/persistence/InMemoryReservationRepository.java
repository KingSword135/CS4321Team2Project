package persistence;

import model.Reservation;

import java.util.List;

public class InMemoryReservationRepository implements ReservationRepository {

    private List<Reservation> reservations;

    public InMemoryReservationRepository(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    @Override
    public List<Reservation> getAllReservations() {
        return reservations;
    }

}
