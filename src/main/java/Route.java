import java.util.*;

public class Route {
    private TreeMap<Integer,City> cities;
    private double distance;

    public Route(TreeMap<Integer,City> cities){this.cities = cities; this.distance = 0;}

    /**
     * Add the departure city to the route
     * @param
     */
    /*public void addDeparture(City c){addCity(c);}*/

    public TreeMap<Integer,City> getCities(){return cities;}
    /**
     * Add a city to the route
     * @param c
     */
    public void addCity(City c){ this.cities.put(c.getid(),c);}

    /**
     * update the distance of the route by adding the distance between the last point and the new added city
     * @param city
     */

    /**
     * @return the distance of the route
     */
    public double getDistance(){return distance;}

    @Override
    public String toString() {
        StringBuilder st = new StringBuilder(" Route : ");
        cities.forEach( (key ,city) ->  st.append(key + "-"));
        return st.toString().substring(0,st.toString().length()-1);
    }
}
