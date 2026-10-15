package persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import model.Space;

public class SpaceJSONReader {

    SpaceJSONReader() {

    }

    public void writeSpaces(List<Space> spaces) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        File file = new File("src/persistence/spaces.json");
        try {
            mapper.writeValue(file, spaces);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readSpaces() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        File file = new File("src/persistence/spaces.json");
        List<Space> spaces = mapper.readValue(file, new TypeReference<>() {});
        System.out.println(spaces);
    }

    public static void main(String[] args) throws IOException {
        SpaceJSONReader reader = new SpaceJSONReader();
        List<Space> spaces = new ArrayList<>();
        spaces.add(new Space(1001,"Conference Room A","Nevins Hall" , 8));
        spaces.add(new Space(2001,"Auditorium", "Student Union",120));
        spaces.add(new Space(3001,"Huddle Room B","Soccer Complex", 4));
        spaces.add(new Space(4001,"Training Hall","Athletic Complex", 30));
//        reader.writeSpaces(spaces);
        reader.readSpaces();
    }
}
