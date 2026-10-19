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
            throw new IllegalArgumentException(
                    "Start and end times are required.");
        }

        if (startTime.isBefore(LocalTime.of(8, 0))) {
            throw new IllegalArgumentException(
                    "Start time cannot be before 8:00 AM.");
        }

        if (!endTime.isAfter(startTime)) {
            throw new IllegalArgumentException(
                    "End time must be after the start time.");
        }

        if (endTime.isAfter(LocalTime.of(20, 0))) {
            throw new IllegalArgumentException(
                    "End time cannot be after 8:00 PM.");
        }

        long duration = endTime.toSecondOfDay() / 60
                - startTime.toSecondOfDay() / 60;

        if (duration > 120) {
            throw new IllegalArgumentException(
                    "Duration cannot be greater than 2 hours.");
        }
    }

    public List<Space> filterByMinCapacity(String capacityInput) {
        int minCapacity;

        try {
            minCapacity = Integer.parseInt(capacityInput);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Capacity must be a valid number.");
        }

        List<Space> filteredSpaces = new ArrayList<>();

        for (Space space : spaces) {
            if (space.getCapacity() >= minCapacity) {
                filteredSpaces.add(space);
            }
        }

        return filteredSpaces;
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

    public void modify(LocalDate newDate,
                       LocalTime newStartTime,
                       LocalTime newEndTime) {
        modify(spaceId, newDate, newStartTime, newEndTime);
    }

    public void modify(int newSpaceId,
                       LocalDate newDate,
                       LocalTime newStartTime,
                       LocalTime newEndTime) {

        if (newDate == null) {
            throw new IllegalArgumentException("Date is required.");
        }

        validateTimes(newStartTime, newEndTime);

        this.spaceId = newSpaceId;
        this.date = newDate;
        this.startTime = newStartTime;
        this.endTime = newEndTime;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", spaceId=" + spaceId +
                ", date=" + date +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }

    public static void main(String[] args) {
        System.out.println("Reservation system started.");
    }
}
