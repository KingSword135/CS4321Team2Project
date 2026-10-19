package persistence;

import model.Reservation;
import model.Space;

import java.util.List;

public interface ReservationRepository {

    public List<Reservation> getAllReservations();

}
