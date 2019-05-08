import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Solver {
    /**
     * @param path , path to wanted cities file
     * @return a naive way to solve the problem by connecting each city using the given order, once the graph is
     *  a complete graph every city can be connected to any other, this solution don't take account of the distance
     *  between cities or anything
     */
    public static String naiveArrayListWay(String path){
        Route route = new Route();
        ArrayList<City> cities = null;
        try {
            try {
                cities = CSVParser.parseCityList(path);
                DepartureCity departureCity = new DepartureCity(cities.get(0).getCoordinates(),1);
                route.addDeparture(departureCity);
                cities.stream().filter(item -> item.getid() > departureCity.getid()).forEach( item -> {
                    if(!item.getVisited()){
                        item.setVisited(true);
                        route.addCity(item);
                    }
                });
                route.addArrival();
            } catch (FileNotFoundException e) {
                System.err.println("An error append while reading csv file");
                e.printStackTrace();
            }
        } catch (NullPointerException n){
            System.err.println("An error append while getting cities");
            n.printStackTrace();
        }
        return route.toString() + " \n Distance : "+ route.getDistance();
    }
}
