package model;

import java.sql.Date;
import java.time.LocalTime;



public class Reservation {

    public int id;
    public int spaceId;
    public Date date;
    // Store as a LocalTime object
    public LocalTime startTime;
    public LocalTime endTime;

    public Reservation(int id, int spaceId, Date date, LocalTime startTime, LocalTime endTime){
        this.id = id;
        this.spaceId = spaceId;
        this.date = date;
        // "Is startTime before 8:00am?"
        if(startTime.isBefore(LocalTime.of(8, 0))){
            throw new IllegalArgumentException("Start time cannot be before 8:00am");
        }
        else{
            this.startTime = startTime;
        }
        if(endTime.isBefore(startTime)){
            throw new IllegalArgumentException("End time cannot be before the Start time");
        }
        else if(endTime.isAfter(LocalTime.of(20, 0))){
            throw new IllegalArgumentException("End time cannot be after 8:00pm");
        }
        else{
             this.endTime = endTime;
        }
       // Convert to minutes and compare
        long startMinutes = startTime.toSecondOfDay() / 60;
        long endMinutes = endTime.toSecondOfDay() / 60;

        if (endMinutes - startMinutes > 120) { // 2 hours = 120 minutes
            // duration > 2 hours
            throw new IllegalArgumentException("Duration cannot be greater than 2 hours.");
        }   
    }
    public static void main(String[] args) {
        System.out.println("Sprint project started");
    }
}