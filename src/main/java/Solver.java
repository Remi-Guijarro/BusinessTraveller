import org.paukov.combinatorics3.Generator;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Solver {
    /**
     * @param path , path to wanted cities file
     * @return
     */
    public static String naiveArrayListWay(String path){
        TreeMap<Integer,City> cities = null;
        List<List<Integer>> parcours = null;
        int bestParcour = 0;
        int bestDistance = Integer.MAX_VALUE;
        try {
            try {
                cities = CSVParser.parseCityList(path);
                Route standardRoute = new Route(cities);
                parcours = Generator.permutation(standardRoute.getCities().subMap(2, standardRoute.getCities().size()+1).keySet())
                        .simple()
                        .stream()
                        .collect(Collectors.toList());
                System.out.println(parcours.size());
                for(int i =0 ; i < parcours.size() ; i++){
                    int distance = 0;
                    parcours.get(i).add(0,1);
                    parcours.get(i).add(1);
                    for(int parcour = 1 ; parcour < parcours.get(i).size()-1 ; parcour++){
                        distance += standardRoute.getCities().get(parcour).getDistanceWith(standardRoute.getCities().get(parcour+1));
                    }
                    System.out.println(parcours.get(i) + " distance " + distance);
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
}
