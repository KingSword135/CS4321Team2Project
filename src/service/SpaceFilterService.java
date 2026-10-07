package service;

import model.Space;
import persistence.SpaceRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Business logic for the Discover Spaces filters (US-3, US-4).
 * The controller calls this service; the service pulls spaces from
 * the repository and returns filtered results for the US-1 list view.
 */
public class SpaceFilterService {

    private final SpaceRepository repository;

    public SpaceFilterService(SpaceRepository repository) {
        this.repository = repository;
    }

    // ---------- US-3: Filter by minimum capacity ----------

    /**
     * AC-1: Valid capacity   -> spaces with capacity >= value.
     * AC-2: No matches       -> empty list (view shows "no matching spaces").
     * AC-3: Non-numeric/<=0  -> IllegalArgumentException (validation error).
     */
    public List<Space> filterByMinCapacity(String capacityInput) {
        int minCapacity = parseCapacity(capacityInput);

        List<Space> matches = new ArrayList<>();
        for (Space space : repository.getAllSpaces()) {
            if (space.getCapacity() >= minCapacity) {
                matches.add(space);
            }
        }
        return matches;
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

    // ---------- US-4: Filter by features ----------

    /**
     * AC-1: One feature selected  -> spaces containing that feature.
     * AC-2: Multiple features     -> spaces containing ALL selected features (AND).
     * AC-3: No matches            -> empty list (view shows "no matching spaces").
     * AC-4: No features selected  -> IllegalArgumentException prompting selection.
     */
    public List<Space> filterByFeatures(List<String> selectedFeatures) {
        if (selectedFeatures == null || selectedFeatures.isEmpty()) {
            throw new IllegalArgumentException("Please select at least one feature.");
        }

        List<String> required = normalize(selectedFeatures);

        List<Space> matches = new ArrayList<>();
        for (Space space : repository.getAllSpaces()) {
            if (normalize(space.getFeatures()).containsAll(required)) {
                matches.add(space);
            }
        }
        return matches;
    }

    /** Case-insensitive, trimmed feature names; skips null/blank entries. */
    private List<String> normalize(List<String> features) {
        List<String> result = new ArrayList<>();
        if (features == null) {
            return result;
        }
        for (String f : features) {
            if (f != null && !f.trim().isEmpty()) {
                result.add(f.trim().toLowerCase());
            }
        }
        return result;
    }
}