import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.ArrayList;

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

    public static void main(String[] args){

        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.println("Question 1: naive solution");
        System.out.println("--------------------------------------------------------------------------------------------");

        String csv = Paths.get("").toAbsolutePath().normalize().toString() + "/src/main/resources/data/test10.csv";
        ArrayList<City> cities = null;
        try {
            cities = CSVParser.parseCityList(csv);
        } catch (FileNotFoundException e) {
            System.err.println("File " + csv + " do not exist.");
            return;
        }
        startTimer();
        assert cities != null;
        Result r = Solver.naiveSolution(cities, cities.get(0));
        storeExecutionInfo();
        System.out.println("Solution:");
        System.out.println(" "+r.getCourse());
        System.out.println(" Distance: "+r.getDistance());
        System.out.println("Performance:");
        System.out.println(executionInfo);

    }
}
