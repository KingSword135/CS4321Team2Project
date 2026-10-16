package persistence;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import model.Reservation;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ArrayList;

public class ReservationJSONReadWriter {

    private List<Reservation> reservations;

    public ReservationJSONReadWriter() {

    }

    public void WriteToFile(List<Reservation> reservations) {

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        File file = new File("src/persistence/reservations.json");

        try {
            mapper.writeValue(file, reservations);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public List<Reservation> ReadFromFile() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        File file = new File("src/persistence/reservations.json");
        List<Reservation> reservations = mapper.readValue(file, new TypeReference<>() {
        });
        return reservations;
    }

    public static void main (String[] args) throws IOException {
        //Test if this even works...

        ReservationJSONReadWriter reservationJSONReadWriter = new ReservationJSONReadWriter();
        ArrayList<Reservation> reservations = new ArrayList<>();
        LocalDate date = LocalDate.of(2026, 6, 21);
        LocalTime startTime = LocalTime.of(10,30);
        LocalTime endTime = LocalTime.NOON;
        Reservation re1 = new Reservation(1,4,date, startTime,endTime);
        reservations.add(re1);
        reservationJSONReadWriter.WriteToFile(reservations);
        reservationJSONReadWriter.ReadFromFile();
    }


}
