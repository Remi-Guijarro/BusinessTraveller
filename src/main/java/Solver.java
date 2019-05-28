import org.paukov.combinatorics3.Generator;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

final class Result {
    private final ArrayList<City> course;
    private final Double distance;

    public Result(ArrayList<City> course, Double distance) {
        this.course = course;
        this.distance = distance;
    }

    public ArrayList<City> getCourse() {
        return course;
    }

    public Double getDistance() {
        return distance;
    }
}

public class Solver {
    /**********PERMUTATION: LEXICOGRAPHICAL ORDER**********/
    public static ArrayList<ArrayList<City>> permutationNoMirror(
            ArrayList<City> cities) {

        ArrayList<ArrayList<City>> result = new ArrayList<>();
        Collections.sort(cities);
        boolean hasNext = true;
        int count = 0;

        // We'll only take the first half of (n-1)! to exclude mirrored values
        long firstHalfDelimiter =
                LongStream.rangeClosed(1, cities.size())
                        .reduce(1, (long x, long y) -> x * y) /2;

        while(hasNext) {
            result.add(new ArrayList<>(cities));
            count++;
            if (count == firstHalfDelimiter)
                return result;
            int k = 0, l = 0;
            hasNext = false;
            for (int i = cities.size()- 1; i > 0; i--) {
                if (cities.get(i).compareTo(cities.get(i-1)) > 0) {
                    k = i - 1;
                    hasNext = true;
                    break;
                }
            }

            for (int i = cities.size() - 1; i > k; i--) {
                if (cities.get(i).compareTo(cities.get(k)) > 0) {
                    l = i;
                    break;
                }
            }

            Collections.swap(cities, k, l);
            Collections.reverse(cities.subList(k+1, cities.size()));
        }
        return result;
    }

    /**********QUESTION 1: NAIVE SOLUTION**********/
    public static Result naiveSolution(ArrayList<City> cities, City departure) {
        //Create permutations (mirrors excluded) of all cities except departure
        //i.e. all possible courses
        cities.remove(departure);
        ArrayList<ArrayList<City>> permutations = permutationNoMirror(cities);

        ArrayList<City> shortestCourse = null;
        double shortestDistance = Double.MAX_VALUE;
        //Compute distance for all courses
        for(ArrayList<City> course : permutations) {
            course.add(departure); //We need to go back to departure in the end of the course

            double distance = 0;
            City previous = departure;
            for(City c : course) {
                distance += previous.getDistanceWith(c);
                previous = c;
            }
            //Is it the shortest distance?
            if (distance < shortestDistance) {
                shortestDistance = distance;
                shortestCourse = course;
            }
        }
        shortestCourse.add(0, departure);
        return new Result(shortestCourse, shortestDistance);
    }

    /*public static String naiveArrayListWay(String path){
        TreeMap<Integer,City> cities = null;
        List<List<Integer>> parcours = null;
        int bestParcour = 0;
        double bestDistance = Integer.MAX_VALUE;
        try {
            try {
                cities = CSVParser.parseCityTreeMap(path);
                Route standardRoute = new Route(cities);
                parcours = Generator.permutation(standardRoute.getCities().subMap(2, standardRoute.getCities().size()+1).keySet())
                        .simple()
                        .stream()
                        .collect(Collectors.toList());
                for(int i =0 ; i < parcours.size() ; i++){
                    HashSet<ArrayList<Integer>> calculated = new HashSet<>();
                    double distance = 0;
                    parcours.get(i).add(0,1);
                    parcours.get(i).add(1);
                    for(int z = 0 ; z< parcours.get(i).size() -1; z++) {
                        distance += standardRoute.getCities().get(parcours.get(i).get(z)).getDistanceWith(standardRoute.getCities().get(parcours.get(i).get(z+1)));
                        if(distance > bestDistance) {
                            break;
                        }
                    }
                    if(distance < bestDistance){
                        bestDistance = distance;
                        bestParcour = i;
                    }
                }
            } catch (FileNotFoundException e) {
                System.err.println("An error append while reading csv file");
                e.printStackTrace();
            }
        } catch (NullPointerException n){
            System.err.println("An error append while getting cities");
            n.printStackTrace();
        }
        return parcours.get(bestParcour).toString() +" : distance  => " + bestDistance;
    }

    public static String naiveWithoutSymetrieDoublon(String path){
        TreeMap<Integer,City> cities = null;
        try {
            cities = CSVParser.parseCityTreeMap(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Route standardRoute = new Route(cities);
        HashMap<Double,List<Integer>> set = new HashMap<>();
        Generator.permutation(standardRoute.getCities().subMap(2, standardRoute.getCities().size()+1).keySet())
                .simple()
                .stream()
                .forEach(item -> {
                    if(!set.containsKey(item)){
                        Collections.reverse(item);
                        if(!set.containsKey(item)){
                            item.add(0,1);
                            item.add(1);
                            double distance = 0;
                            for(int z = 0 ; z< item.size() -1; z++) {
                                distance += standardRoute.getCities().get(item.get(z)).getDistanceWith(standardRoute.getCities().get(item.get(z+1)));
                            }
                            set.put(distance,item);
                        }
                    }
                });
        ArrayList<Double> a =  new ArrayList<>(set.keySet());
        Collections.sort(a);
        double bestDistance = a.get(0);
        StringBuilder st =  new StringBuilder();
        set.get(bestDistance).stream().forEach(item -> {
            st.append(item + "-");
        });
        st.append(" : distance => " + bestDistance);
        return st.toString();
    }*/
}
