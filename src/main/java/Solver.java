import java.awt.geom.Line2D;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

final class CourseRunnable implements Runnable{
    public Result result;
    private ArrayList<ArrayList<City>> permutations;
    private City departure;

    public CourseRunnable( ArrayList<ArrayList<City>> permutations, City departure) {
        this.permutations = permutations;
        this.departure = departure;
    }

    @Override
    public void run() {
        ArrayList<City> shortestCourse = null;
        double shortestDistance = Double.MAX_VALUE;
        //Compute distance for all courses
        for(ArrayList<City> course : permutations) {
            course.add(departure);
            double distance = 0;
            City previous = departure;
            for(City c : course) {
                distance += previous.getDistanceWith(c);
                previous = c;
            }
            if (distance < shortestDistance) {
                shortestDistance = distance;
                shortestCourse = course;
            }
        }
        shortestCourse.add(0, departure);
        this.result = new Result(shortestCourse, shortestDistance);
    }
}

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

    /**********NAIVE SOLUTION: THREADED**********/
    public static Result naiveSolutionThreaded(ArrayList<City> cities, City departure) {
        //Create permutations (mirrors excluded) of all cities except departure
        //i.e. all possible courses
        cities.remove(departure);

        ArrayList<ArrayList<City>> permutations = permutationNoMirror(cities);
        /* IMPROVING */
        /*for(int i = 0 ; i < 4 ; i++){
            CourseRunnable courseRunnable = new CourseRunnable(new ArrayList<>(permutations.subList(permutations.size() * i,permutations.size() *(i+1/4))),departure);

        }*/
        CourseRunnable first = new CourseRunnable(new ArrayList<>(permutations.subList(0,permutations.size() *1/4)),departure);
        CourseRunnable second = new CourseRunnable(new ArrayList<>(permutations.subList(permutations.size() * 1/4,permutations.size() * 2/4)) ,departure);
        CourseRunnable third = new CourseRunnable(new ArrayList<>(permutations.subList(permutations.size()* 2/4,permutations.size() * 3/4)) ,departure);
        CourseRunnable fourth  = new CourseRunnable(new ArrayList<>(permutations.subList(permutations.size()* 3/4,permutations.size() * 4/4)) ,departure);

        Thread th1 = new Thread(first);
        Thread th2 = new Thread(second);
        Thread th3 = new Thread(third);
        Thread th4 = new Thread(fourth);
        th1.run();
        th2.run();
        th3.run();
        th4.run();
        try {
            th1.join();
            th2.join();
            Result firstBest = first.result.getDistance() > second.result.getDistance() ? second.result : first.result;
            th3.join();
            th4.join();
            Result secondBest = third.result.getDistance() > fourth.result.getDistance() ? fourth.result : third.result;
            return firstBest.getDistance() > secondBest.getDistance() ? secondBest : firstBest;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**********1ST HEURISTIC: NEAREST NEIGHBOR**********/

    /**PERMUTATION: LEXICOGRAPHICAL ORDER**/
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

    /** SOLUTION **/
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

    /**********2ND HEURISTIC: PLANAR GRAPH**********/
    /**CHECK INTERSECTION**/
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

    /**SOLUTION**/
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


    /**********GENETIC ALGORITHM**********/
    private static ArrayList<City> createRandomRoute(ArrayList<City> cities) {
        ArrayList<City> copy = new ArrayList<>(cities);
        City departure = cities.get(0);
        copy.remove(departure);
        Collections.shuffle(copy);
        copy.add(0, departure);
        return copy;
    }

    // We only use this function to create initial population. Subsequent generations will be
    // produced through breeding and mutation.
    private static ArrayList<ArrayList<City>> initialPopulation(int populationSize, ArrayList<City> cities) {
        ArrayList<ArrayList<City>> population = new ArrayList<>();

        for (int i=0 ; i<populationSize ; ++i) {
            population.add(createRandomRoute(cities));
        }
        return population;
    }

    private static HashMap<Integer, Double> rankRoutes(ArrayList<ArrayList<City>> population) {
        // <route ID, fitness score>
        HashMap<Integer, Double> fitnessResult = new HashMap<>();
        for (int i=0 ; i<population.size() ; ++i) {
            fitnessResult.put(i, new Fitness(population.get(i)).routeFitness());
        }


/*        fitnessResult.put(0, 0.008);
        fitnessResult.put(1, 0.005);
        fitnessResult.put(2, 0.009);
        fitnessResult.put(3, 0.001);
        fitnessResult.put(4, 0.003);
        fitnessResult.put(5, 0.009);
        fitnessResult.put(6, 0.002);*/




        // Sort the hashmap by value in descending order, i.e. from the worst fitness score to the best
            //create a list from elements of hashmap
            List<Map.Entry<Integer, Double>> list = new LinkedList<>(fitnessResult.entrySet());
            //sort the list
            Collections.sort(list, (o1, o2) -> {
                if(o1.getValue() > o2.getValue())
                    return -1;
                else if (o1.getValue() < o2.getValue())
                    return 1;
                else
                    return 0;
            });
            //put data from sorted list to hashmap
            HashMap<Integer, Double> sorted = new LinkedHashMap<>();
            for (Map.Entry<Integer, Double> aa : list) {
                sorted.put(aa.getKey(), aa.getValue());
            }

            return sorted;
    }

    private static ArrayList<Double> cumulativeSum(ArrayList<Double> list) {
        ArrayList<Double> result = new ArrayList<>();
        Double sum = 0.;
        for (Double d : list) {
            sum += d;
            result.add(sum);
        }
        return result;
    }

    private static ArrayList<Double> cumulativePercentage(ArrayList<Double> list) {
        ArrayList<Double> cumulativePercentage = new ArrayList<>();
        ArrayList<Double> cumulativeSum = cumulativeSum(list);
        double total = cumulativeSum.get(cumulativeSum.size()-1);
        for (Double d : cumulativeSum) {
            cumulativePercentage.add(100*d/total);
        }
        return cumulativePercentage;
    }

    private static ArrayList<Integer> selection(HashMap<Integer, Double> populationRanked, int eliteSize) {
        ArrayList<Integer> selectionResults = new ArrayList<>();

        ArrayList<Double> cumulativePercentage = cumulativePercentage(new ArrayList<>(populationRanked.values()));

        for (int i =0 ; i<eliteSize ; ++i) {
            selectionResults.add((Integer)populationRanked.keySet().toArray()[i]);
        }
        //System.out.println(selectionResults);
        for (int i=0 ; i<populationRanked.size()-eliteSize ; ++i) {
            double pick = 100*Math.random();
            //System.out.println("outer: here i="+i);
            for (int j=0 ; j<populationRanked.size() ; ++j) {
                //System.out.println("\tinner: here i="+j);
                if (pick <= cumulativePercentage.get(j)) {
                    selectionResults.add((Integer)populationRanked.keySet().toArray()[j]);
                    break;
                }
            }
        }
        //System.out.println(selectionResults);
        return selectionResults;
    }

    //extract selectionResults from population to get the mating pool
    private static ArrayList<ArrayList<City>> matingPool(ArrayList<ArrayList<City>> population,
            ArrayList<Integer> selectionResults) {
        ArrayList<ArrayList<City>> matingPool = new ArrayList<>();
        for (int i=0 ; i<selectionResults.size() ; ++i) {
            int index = selectionResults.get(i);
            matingPool.add(population.get(index));
        }
        return matingPool;
    }

    public static ArrayList<City> breed(ArrayList<City> parent1, ArrayList<City> parent2) {
        ArrayList<City> childP1 = new ArrayList<>();
        ArrayList<City> childP2 = new ArrayList<>(parent2);

        int geneA = (int) (Math.random()*parent1.size());
        int geneB = (int) (Math.random()*parent1.size());

        int startGene = Math.min(geneA, geneB);
        int endGene = Math.max(geneA, geneB);

        for (int i=startGene ; i<endGene ; ++i) {
            childP1.add(parent1.get(i));
        }

        for (City c : childP1) {
            childP2.remove(c);
        }

        ArrayList<City> child = new ArrayList<>(childP1);
        child.addAll(childP2);

        return child;
    }

    private static <T> ArrayList<T> randomSample(ArrayList<T> list, int sampleSize) {
        Random rand = new Random();
        ArrayList<T> copy = new ArrayList<T>(list);
        ArrayList<T> result = new ArrayList<>();
        for (int i=0 ; i<sampleSize ; ++i) {
            int randomIndex = rand.nextInt(copy.size());
            result.add(copy.get(randomIndex));
            copy.remove(randomIndex);
        }
        return result;
    }

    private static ArrayList<ArrayList<City>> breedPopulation(ArrayList<ArrayList<City>> matingPool, int eliteSize) {
        ArrayList<ArrayList<City>> children = new ArrayList<>();
        int length = matingPool.size()-eliteSize;
        ArrayList<ArrayList<City>> pool = randomSample(matingPool, matingPool.size());

        for (int i=0 ; i<eliteSize ; ++i) {
            children.add(matingPool.get(i));
        }

        for (int i=0 ; i<length ; ++i) {
            ArrayList<City> child = breed(pool.get(i), pool.get(matingPool.size()-i-1));
            children.add(child);
        }
        return children;
    }

    private static ArrayList<City> mutate(ArrayList<City> individual, double mutationRate) {
        for (int swapped=0 ; swapped<individual.size() ; ++swapped) {
            if (Math.random() < mutationRate) {
                int swapWith = (int) (Math.random()*individual.size());

                Collections.swap(individual, swapped, swapWith);
            }
        }
        return individual;
    }

    private static ArrayList<ArrayList<City>> mutatePopulation(ArrayList<ArrayList<City>> population, double mutationRate) {
        ArrayList<ArrayList<City>> mutatedPopulation = new ArrayList<>();

        for(int individual=0 ; individual<population.size() ; ++individual) {
            ArrayList<City> mutatedIndividual = mutate(population.get(individual), mutationRate);
            mutatedPopulation.add(mutatedIndividual);
        }

        return mutatedPopulation;
    }

    private static ArrayList<ArrayList<City>> nextGeneration(ArrayList<ArrayList<City>> currentGen, int eliteSize, double mutationRate) {
        HashMap<Integer, Double> populationRanked = rankRoutes(currentGen);
        ArrayList<Integer> selectionResults = selection(populationRanked, eliteSize);
        ArrayList<ArrayList<City>> matingPool = matingPool(currentGen, selectionResults);
        ArrayList<ArrayList<City>> children = breedPopulation(matingPool, eliteSize);
        ArrayList<ArrayList<City>> nextGeneration = mutatePopulation(children, mutationRate);
        return nextGeneration;
    }

    public static Result geneticSolution(ArrayList<City> cities, City departure, int populationSize, int eliteSize,
                                         double mutationRate, int generations) {
        ArrayList<ArrayList<City>> pop = initialPopulation(populationSize, cities);
        //System.out.println("initial distance: "+1/(Double)rankRoutes(pop).values().toArray()[0]);

        for (int i=0 ; i<generations ; ++i) {
            pop = nextGeneration(pop, eliteSize, mutationRate);
        }

        //System.out.println("final distance: "+1/(Double)rankRoutes(pop).values().toArray()[0]);
        //Set<Integer> keys = rankRoutes(pop).keySet();
        //Integer[] routesIndex = keys.toArray(new Integer[keys.size()]);

        ArrayList<ArrayList<City>> bestRoutes = (ArrayList<ArrayList<City>>) pop
                .stream()
                .filter(x -> x.get(0) == departure)
                .collect(Collectors.toList());

        //From the list of best routes starting with departure, choose the shortest one
        int bestRouteIndex = 0;
        double bestDistance = Double.POSITIVE_INFINITY;
        for(int i=0 ; i<bestRoutes.size() ; ++i) {
            double distance = 0;
            for (int j=0 ; j<bestRoutes.get(i).size()-1 ;++j) {
                distance +=bestRoutes.get(i).get(j).getDistanceWith(bestRoutes.get(i).get(j+1));
            }
            distance +=bestRoutes.get(i).get(bestRoutes.get(i).size()-1).getDistanceWith(departure);
            if (bestDistance > distance) {
                bestRouteIndex = i;
                bestDistance = distance;
            }
        }

        return new Result(bestRoutes.get(bestRouteIndex), bestDistance);
    }
}


