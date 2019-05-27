import org.paukov.combinatorics3.Generator;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Solver {

    public static String naiveArrayListWay(String path){
        TreeMap<Integer,City> cities = null;
        List<List<Integer>> parcours = null;
        int bestParcour = 0;
        double bestDistance = Integer.MAX_VALUE;
        try {
            try {
                cities = CSVParser.parseCityList(path);
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
            cities = CSVParser.parseCityList(path);
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
    }
}
