import java.util.ArrayList;
import java.util.List;

public class Route {
    private ArrayList<City> cities;
    private double distance;


    public Route(){cities = new ArrayList<>(); this.distance = 0;}

    /**
     * Add the departure city to the route
     * @param c
     */
    public void addDeparture(City c){addCity(c);}


    /**
     * Add a city to the route
     * @param c
     */
    public void addCity(City c){updateDistance(c); this.cities.add(c);}


    /**
     * Add the arrival, it takes no arguments as the arrival city have to be the same as the DepartureCity
     */
    public void addArrival(){addCity(cities.get(0));}

    /**
     * @return the cities list
     */
    public ArrayList<City> getCities(){return cities;}

    /**
     * update the distance of the route by adding the distance between the last point and the new added city
     * @param city
     */
    private void updateDistance(City city){
        if(cities.size() <= 0)
            distance += 0;
        else
            distance += cities.get(cities.size()-1).getDistanceWith(city);
    }

    /**
     * @return the distance of the route
     */
    public double getDistance(){return distance;}

    @Override
    public String toString() {
        StringBuilder st = new StringBuilder(" Route : ");
        cities.forEach(city ->  st.append(city.getid() + "-"));
        return st.toString().substring(0,st.toString().length()-1);
    }
}
