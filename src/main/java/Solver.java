import java.util.*;
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
    private static ArrayList<ArrayList<City>> permutationNoMirror(
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
    /**********NAIVE SOLUTION**********/
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

    /**********1ST HEURISTIC: NEAREST NEIGHBOR**********/
    public static Result nearestNeighbor(ArrayList<City> cities, City departure) {
        double totalDistance = 0;
        ArrayList<City> course = new ArrayList<>();
        course.add(departure);

        while(cities.size() > 1) {
            City current = course.get(course.size()-1); //current city is the last city of the course
            City next = null;

            double minDistance = Double.MAX_VALUE;
            for (City c : cities) {
                if (!current.equals(c)) {
                    double currentDistance = current.getDistanceWith(c);
                    if (currentDistance < minDistance) {
                        minDistance = currentDistance;
                        next = c;
                    }
                }
            }

            assert next != null;
            course.add(next);
            totalDistance += minDistance;
            cities.remove(current);
        }

        totalDistance+=departure.getDistanceWith(course.get(course.size()-1));
        course.add(departure);

        return new Result(course, totalDistance);
    }

}
