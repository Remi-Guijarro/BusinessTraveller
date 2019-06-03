import java.util.ArrayList;

public class Fitness {
    private ArrayList<City> route;
    private double distance;
    private double fitness; // inverse of route distance => a larger fitness score is better

    public Fitness(ArrayList<City> route) {
        this.route = route;
    }

    public double routeDistance() {
        if (distance == 0) {
            double pathDistance = 0;
            for (int i=0 ; i<route.size() ; ++i) {
                City fromCity = route.get(i);
                City toCity = null;

                if (i+1 < route.size()) {
                    toCity = route.get(i+1);
                } else {
                    toCity = route.get(0);
                }
                pathDistance += fromCity.getDistanceWith(toCity);
            }
            distance = pathDistance;
        }
        return distance;
    }

    public double routeFitness() {
        if (fitness == 0) {
            fitness = 1. / routeDistance();
        }
        return fitness;
    }
}
