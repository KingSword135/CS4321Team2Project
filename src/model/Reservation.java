package model;

import java.time.*;

public class Reservation {

    private int id;
    private int spaceId;
    private LocalDateTime date;
    private LocalTime startTime;
    private LocalTime endTime;

    public Reservation(int id, int spaceId, LocalDateTime date, LocalTime startTime, LocalTime endTime) {
        this.id = id;
        this.spaceId = spaceId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}