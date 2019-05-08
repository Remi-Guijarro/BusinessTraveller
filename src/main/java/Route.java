import java.util.ArrayList;
import java.util.List;

public class Route {
    private ArrayList<City> cities;
    private double distance;
    public void addDeparture(City c){addCity(c);}
    public Route(){cities = new ArrayList<>(); this.distance = 0;}
    public void addCity(City c){updateDistance(c); this.cities.add(c);}
    public void addArrival(){addCity(cities.get(0));}
    public ArrayList<City> getCities(){return cities;}
    private void updateDistance(City city){
        if(cities.size() <= 0)
            distance += 0;
        else
            distance += cities.get(cities.size()-1).getDistanceWith(city);
    }
    public double getDistance(){return distance;}

    @Override
    public String toString() {
        StringBuilder st = new StringBuilder(" Route : ");
        cities.forEach(city ->  st.append(city.getid() + "-"));
        return st.toString().substring(0,st.toString().length()-1);
    }
}
