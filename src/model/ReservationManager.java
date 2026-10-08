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
