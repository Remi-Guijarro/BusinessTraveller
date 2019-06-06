import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class BusinessTraveller {

    private static double startTime;
    private static String executionInfo;

    /**
     * start a timer in milli second
     */
    private static void startTimer(){
        startTime = System.nanoTime();
    }

    /**
     * @return the duration ( In second ) since startTimer() and now
     */
    private static double getSecondDuration(){
        return ((double)System.nanoTime() - startTime) / 1000000000;
    }

    /**
     * @return the memory usage in MB
     */
    private static double getMemoryUsage(){
        return ((double) Runtime.getRuntime().totalMemory() -  Runtime.getRuntime().freeMemory() ) / 1048576;
    }

    /**
     * Store the duration and memory usage
     */
    private static void storeExecutionInfo(){
        executionInfo = " Duration : " + getSecondDuration() + " seconds\n" +
                        " Memory usage : " + getMemoryUsage()+ " MB ";
    }

    private static void displayResult(Result r) {
        System.out.println("Solution:");
        System.out.println(" "+formatList(r.getCourse(), '-'));
        System.out.println(" Distance: "+r.getDistance());
        System.out.println("Performance:");
        System.out.println(executionInfo);
    }

    private static <T> String formatList(List<T> list, char separator) {
        StringBuilder result = new StringBuilder();
        for (int i=0 ; i<list.size()-1 ; ++i) {
            result.append(list.get(i)).append(separator);
        }
        result.append(list.get(list.size()-1));
        return result.toString();
    }

    public static void main(String[] args){
        if (args.length != 1)
        {
            System.err.println("Error: one argument required: path to csv file.");
            return;
        }
        //String csv = Paths.get("").toAbsolutePath().normalize().toString() + "/src/main/resources/data/test10.csv";
        //String csv = Paths.get("").toAbsolutePath().normalize().toString() + "/src/main/resources/data/djibouti38.csv";
        String csv = args[0];
        ArrayList<City> cities = null;
        try {
            cities = CSVParser.parseCityList(csv);
        } catch (FileNotFoundException e) {
            System.err.println("File " + csv + " do not exist.");
            return;
        }
        assert cities != null;

        /*System.out.println("--------------------------------------------------------------------------------------------");
        System.out.println("Naive solution");
        System.out.println("--------------------------------------------------------------------------------------------");
        startTimer();
        Result r1 = Solver.naiveSolution(new ArrayList<>(cities), cities.get(0));
        storeExecutionInfo();
        displayResult(r1);*/

        /*System.out.println("--------------------------------------------------------------------------------------------");
        System.out.println("Naive solution threaded");
        System.out.println("--------------------------------------------------------------------------------------------");
        startTimer();
        Result r2 = Solver.naiveSolutionThreaded(new ArrayList<>(cities), cities.get(0));
        storeExecutionInfo();
        displayResult(r2);*/


        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.println("First heuristic: nearest neighbour");
        System.out.println("--------------------------------------------------------------------------------------------");
        startTimer();
        Result r3 = Solver.nearestNeighborSolution(new ArrayList<>(cities), cities.get(0));
        storeExecutionInfo();
        displayResult(r3);

        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.println("Second heuristic: no intersection -> planar graph");
        System.out.println("--------------------------------------------------------------------------------------------");
        startTimer();
        Result r4 = Solver.planarGraphSolution(new ArrayList<>(cities), cities.get(0));
        storeExecutionInfo();
        displayResult(r4);

        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.println("Third heuristic: genetic algorithm");
        System.out.println("--------------------------------------------------------------------------------------------");
        startTimer();
        Result r5 = Solver.geneticSolution(new ArrayList<>(cities), cities.get(0), 50, 20, 0.01, 500);
        storeExecutionInfo();
        displayResult(r5);

    }
}
