import org.junit.Test;

import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class CSVParserTest {
    private double delta = .01;

    @Test
    public void parseCityListValid() {
        // Get the absolute path
        String csv = Paths.get("").toAbsolutePath().normalize().toString() + "/src/main/resources/data/test10.csv";

        ArrayList<City> cities = null;
        try {
            cities = CSVParser.parseCityList(csv);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Check that ArrayList has been initialized
        if (cities == null) fail();
        // Check that ArrayList had correct number of values
        if (cities.size() != 10) fail();

        // Test the 10 values
        // TODO: use arraylist
        City current;
        current = cities.get(0);
        assertEquals(2., current.getCoordinates().getX(), delta);
        assertEquals(36.05, current.getCoordinates().getY(), delta);

        current = cities.get(1);
        assertEquals(6.53, current.getCoordinates().getX(), delta);
        assertEquals(37.38, current.getCoordinates().getY(), delta);

        current = cities.get(2);
        assertEquals(7.2, current.getCoordinates().getX(), delta);
        assertEquals(16.23, current.getCoordinates().getY(), delta);
    }
}