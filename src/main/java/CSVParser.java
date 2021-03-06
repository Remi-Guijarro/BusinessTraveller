import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CSVParser {

    /**
     * Parse CSV file to an ArrayList of City objects.
     *
     * @param csvFile Absolute path to the csv file.
     * @return An ArrayList of the cities. The list is populated in the same order as the cities are read in the file.
     * @throws FileNotFoundException The path to the file is invalid.
     */
    //ArrayList always give O(1) time complexity (map can give O(n) in worst case)
    public static ArrayList<City> parseCityList(String csvFile) throws FileNotFoundException {
        BufferedReader br = null;
        String line = "";
        int n = 0;
        ArrayList<City> cities = new ArrayList<>();
        br = new BufferedReader(new FileReader(csvFile));

        // Handle first line
        try {
            line = br.readLine();
            try {
                n = Integer.valueOf(line);
            } catch(NumberFormatException e) {
                e.printStackTrace();
                System.err.println("Could not read CSV: first line must be the number of cities.");
                return null;
            }

            int count = 0;
            while((line = br.readLine()) != null) {
                double x, y;
                String[] coordinates = line.split(",");
                x = Double.valueOf(coordinates[0]);
                y = Double.valueOf(coordinates[1]);

                // Create the city
                City city = new City(new Coordinates(x,y));
                // Add the city to the list
                cities.add(city);

                count++;
            }

            if(count != n) {
                System.err.println("CSV format incorrect: the number of cities (first line) is not consistent with the " +
                        "number of coordinates (following lines).");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cities;
    }

    //UNUSED
    /*public static TreeMap<Integer,City> parseCityTreeMap(String csvFile) throws FileNotFoundException {
        BufferedReader br = null;
        String line = "";
        int n = 1;
        TreeMap<Integer,City> cities = new TreeMap<>();
        try {
            br = new BufferedReader(new FileReader(csvFile));

            // Handle first line
            line = br.readLine();
            try {
                n = Integer.valueOf(line) +1;
            } catch(NumberFormatException e) {
                e.printStackTrace();
                System.err.println("Could not read CSV: first line must be the number of cities.");
                return null;
            }

            int count = 1;
            while((line = br.readLine()) != null) {
                double x, y;
                String[] coordinates = line.split(",");
                x = Double.valueOf(coordinates[0]);
                y = Double.valueOf(coordinates[1]);
                City city = null;

                // create the city
                if(count == 1){
                    city = new DepartureCity(new Coordinates(x,y),count);
                } else{
                    city = new City(new Coordinates(x,y),count);
                }

                // Add the city to the Map
                cities.put(city.getId(),city);

                count++;
            }
            if(count != n) {
                System.err.println("CSV format incorrect: the number of cities (first line) is not consistent with the " +
                        "number of coordinates (following lines).");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return cities;
    }*/
}
