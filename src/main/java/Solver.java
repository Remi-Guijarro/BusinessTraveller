import org.paukov.combinatorics3.Generator;
import org.paukov.combinatorics3.PermutationGenerator;

import java.io.FileNotFoundException;
import java.sql.SQLOutput;
import java.util.*;
import java.util.stream.Collectors;

public class Solver {

    public class HashReverseSet extends HashSet<ArrayList<Integer>>{

        public void addReverse(ArrayList<Integer> toadd) {
            if(!this.contains(toadd)) {

            }
        }

    }

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
}
