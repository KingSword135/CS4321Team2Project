package persistence;

import model.Space;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import persistence.SpaceRepository;

public class InMemorySpaceRepository implements SpaceRepository {

    private List<Space> spaces;

    public InMemorySpaceRepository(List<Space> spaces) {
        this.spaces = new ArrayList<Space>(spaces);
    }

    @Override
    public List<Space> getAllSpaces() {
        ArrayList<Space> sorted_spaces = (ArrayList<Space>)spaces;
        return sorted_spaces;
    }

}

