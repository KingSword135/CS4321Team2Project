package persistence;

import model.Space;

import java.util.*;

import persistence.SpaceRepository;

public class InMemorySpaceRepository implements SpaceRepository {

    private List<Space> spaces;

    public InMemorySpaceRepository(List<Space> spaces) {
        this.spaces = new ArrayList<Space>(spaces);
    }

    @Override
    public List<Space> getAllSpaces() {
        List<Space> sorted = new ArrayList<>(spaces);
        sorted.sort(Comparator.comparing(Space::getName));
        return sorted;
    }

}

