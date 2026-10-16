
package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Reservation {

    public int id;
    public int spaceId;
    public LocalDate date;
    public LocalTime startTime;
    public LocalTime endTime;

    private final List<Space> spaces = new ArrayList<>();

    public Reservation(int id, int spaceId, LocalDate date,
                       LocalTime startTime, LocalTime endTime) {
        this.id = id;
        this.spaceId = spaceId;
        this.date = date;

        validateTimes(startTime, endTime);

        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Reservation() {
        spaces.add(new Space(1001, "Conference Room A", "Nevins Hall", 8));
        spaces.add(new Space(2001, "Auditorium", "Student Union", 120));
        spaces.add(new Space(3001, "Huddle Room B", "Soccer Complex", 4));
        spaces.add(new Space(6001, "Training Hall", "Athletic Complex", 30));
    }

    private void validateTimes(LocalTime startTime, LocalTime endTime) {
        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("Start and end times are required.");
        }

        if (startTime.isBefore(LocalTime.of(8, 0))) {
            throw new IllegalArgumentException(
                    "Start time cannot be before 8:00am");
        }

        if (endTime.isBefore(startTime)) {
            throw new IllegalArgumentException(
                    "End time cannot be before the Start time");
        }

        if (endTime.isAfter(LocalTime.of(20, 0))) {
            throw new IllegalArgumentException(
                    "End time cannot be after 8:00pm");
        }

        long duration = endTime.toSecondOfDay() / 60
                - startTime.toSecondOfDay() / 60;

        if (duration > 120) {
            throw new IllegalArgumentException(
                    "Duration cannot be greater than 2 hours.");
        }
    }

    public List<Space> filterByMinCapacity(String capacityInput) {
        int minCapacity = parseCapacity(capacityInput);

        List<Space> matches = new ArrayList<>();

        for (Space space : spaces) {
            if (space.getCapacity() >= minCapacity) {
                matches.add(space);
            }
        }

        return matches;
    }

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
            throw new IllegalArgumentException(
                    "Capacity must be greater than 0.");
        }

        return value;
    }

    public int getSpaceId() {
        return spaceId;
    }

    public int getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void modify(LocalDate newDate, LocalTime newStartTime,
                       LocalTime newEndTime) {
        if (newDate == null) {
            throw new IllegalArgumentException("Date is required.");
        }

        validateTimes(newStartTime, newEndTime);

        this.date = newDate;
        this.startTime = newStartTime;
        this.endTime = newEndTime;
    }

    @Override
    public String toString() {
        return "ID: " + getId()
                + " Space ID: " + getSpaceId()
                + " Date: " + getDate()
                + " Start Time: " + getStartTime()
                + " End Time: " + getEndTime();
    }

    public static void main(String[] args) {
        Reservation app = new Reservation();
        String input = args.length > 0 ? args[0] : "10";

        try {
            List<Space> results = app.filterByMinCapacity(input);

            if (results.isEmpty()) {
                System.out.println(
                        "No matching spaces exist for capacity >= " + input + ".");
            } else {
                System.out.println("Spaces with capacity >= " + input + ":");

                for (Space space : results) {
                    System.out.println("  " + space);
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Validation error: " + e.getMessage());
        }
    }
}
