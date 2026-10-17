package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


public class Reservation {

    public int id;
    public int spaceId;
    public LocalDate date;
    // Store as a LocalTime object
    public LocalTime startTime;
    public LocalTime endTime;

    public Reservation(int id, int spaceId, LocalDate date, LocalTime startTime, LocalTime endTime){
        this.id = id;
        this.spaceId = spaceId;
        this.date = date;

        if (date.isBefore(LocalDate.now())){
            throw new IllegalArgumentException("Date cannot be in the past");
        }
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

    private final List<Space> spaces = new ArrayList<>();

    public Reservation() {
        // Seed data — replace with data loaded from US-1 space list view
//        spaces.add(new Space(1001,"Conference Room A","Nevins Hall" , 8));
//        spaces.add(new Space(2001,"Auditorium", "Student Union",120));
//        spaces.add(new Space(3001,"Huddle Room B","Soccer Complex", 4));
//        spaces.add(new Space(6001,"Training Hall","Athletic Complex", 30));
    }

    /**
     * US-3: Filter spaces by minimum capacity.
     *
     * AC-1: Valid capacity -> returns spaces with capacity >= value.
     * AC-2: No matches -> caller displays "no matching spaces" message (empty list).
     * AC-3: Non-numeric or <= 0 input -> throws IllegalArgumentException (validation error).
     */
    public List<Space> filterByMinCapacity(String capacityInput) {
        int minCapacity = parseCapacity(capacityInput);

        List<Space> matches = new ArrayList<>();
        for (Space space : spaces) {
            if (space.getCapacity() >= minCapacity) {
                matches.add(space);
            }
        }
        return matches; // may be empty -> view shows "no matching spaces"
    }

    /** Parses and validates capacity input; throws for non-numeric or <= 0. */
    private int parseCapacity(String capacityInput) {
        if (capacityInput == null || capacityInput.trim().isEmpty()) {
            throw new IllegalArgumentException("Capacity is required.");
        }
        int value;
        try {
            value = Integer.parseInt(capacityInput.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Capacity must be a number.");
        }
        if (value <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0.");
        }
        return value;
    }

    public int getSpaceId() {
        return spaceId;
    }

    public int getId() {
        return id;
    }

    public void modify(LocalDate newDate, LocalTime newStartTime, LocalTime newEndTime) {
        this.date = newDate;
        this.startTime = newStartTime;
        this.endTime = newEndTime;
    }

    public Object getDate() {
        return date;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public String toString() {
        String msg = "ID: " + getId()
                + " Space ID: " + getSpaceId()
                + " Date: " + getDate()
                + " Start Time: " + getStartTime()
                + " End Time: " + getEndTime();
        return msg;
    }

    /** Demo / manual smoke test for the three acceptance criteria. */
    public static void main(String[] args) {
        Reservation app = new Reservation();
        String input = args.length > 0 ? args[0] : "10";

        try {
            List<Space> results = app.filterByMinCapacity(input);

            if (results.isEmpty()) {
                System.out.println("No matching spaces exist for capacity >= " + input + ".");
            } else {
                System.out.println("Spaces with capacity >= " + input + ":");
                for (Space s : results) {
                    System.out.println("  " + s);
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Validation error: " + e.getMessage()); // AC-3
        }
    }



}