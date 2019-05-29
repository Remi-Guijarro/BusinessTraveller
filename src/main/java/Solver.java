import java.awt.geom.Line2D;
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
    public static Result nearestNeighborSolution(ArrayList<City> cities, City departure) {
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

    /**check intersection**/
    private static boolean intersect(double x1, double y1,
                                     double x2, double y2,
                                     double x3, double y3,
                                     double x4, double y4) {
        //Initialize two lines
        Line2D.Double l1 = new Line2D.Double(x1,y1,x2,y2);
        Line2D.Double l2 = new Line2D.Double(x3,y3,x4,y4);

        //exclude case when lines are equal (i.e. same coordinates)
        if (l1.getP1().equals(l2.getP1()) &&
                l1.getP2().equals(l2.getP2()))
            return false;

        //exclude case when lines have a commun point (i.e. they do not actually intersect)
        if (l1.getP2().equals(l2.getP1()) || l1.getP1().equals(l2.getP2()))
            return false;

        return Line2D.linesIntersect(x1,y1,x2,y2,x3,y3,x4,y4);
    }

    /**********2ND HEURISTIC: PLANAR GRAPH**********/
    public static Result planarGraphSolution(ArrayList<City> cities, City departure) {
        Result r = nearestNeighborSolution(cities, departure);
        ArrayList<City> course = r.getCourse();

        for(int i=0; i<course.size()-1; ++i) {
            City c1 = course.get(i), c2 = course.get(i+1);
            double x1 = c1.getCoordinates().getX(), y1 = c1.getCoordinates().getY(),
                    x2 = c2.getCoordinates().getX(), y2 = c2.getCoordinates().getY();
            for(int j=0; j<course.size()-1; ++j) {
                City c3 = course.get(j), c4 = course.get(j+1);
                double x3 = c3.getCoordinates().getX(), y3 = c3.getCoordinates().getY(),
                        x4 = c4.getCoordinates().getX(), y4 = c4.getCoordinates().getY();
                if (intersect(x1,y1,x2,y2,x3,y3,x4,y4)) {
                    //System.out.println(c1+"-"+c2+" croise "+c3+"-"+c4);
                    Collections.swap(course,course.indexOf(c2), course.indexOf(c3));
                    //System.out.println(course);
                    //restart loop
                    i=0;
                    break;
                }
            }
        }

        //compute new distance
        double previousDistance = r.getDistance(), newDistance = 0;
        for (int i=0 ; i<course.size()-1 ; ++i) {
            newDistance += course.get(i).getDistanceWith(course.get(i+1));
        }

        return new Result(course, newDistance);

    }
}
