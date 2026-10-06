package model;

import java.util.ArrayList;
import java.util.List;

public class Reservation {

    private final List<Space> spaces = new ArrayList<>();

    public Reservation() {
        // Seed data — replace with data loaded from US-1 space list view
        spaces.add(new Space("Conference Room A", 8));
        spaces.add(new Space("Auditorium", 120));
        spaces.add(new Space("Huddle Room B", 4));
        spaces.add(new Space("Training Hall", 30));
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

    /** Simple space model (align with your US-1 Space class). */
    public static class Space {
        private final String name;
        private final int capacity;

        public Space(String name, int capacity) {
            this.name = name;
            this.capacity = capacity;
        }

        public String getName() { return name; }
        public int getCapacity() { return capacity; }

        @Override
        public String toString() {
            return name + " (capacity: " + capacity + ")";
        }
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