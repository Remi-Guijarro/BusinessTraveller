public class BusinessTraveller {

    private static double startTime;

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
     * Display the duration and memory usage
     */
    public static void displayExecutionInfo(){
        System.out.println(" Duration : " + getSecondDuration() + " seconds");
        System.out.println(" Memory usage : " + getMemoryUsage()+ " MB ");
    }

    public static void main(String[] args){
        startTimer();
        System.out.println(Solver.naiveArrayListWay("C:\\Users\\Rémi\\Documents\\Ensiie\\AdvancedProgramming\\BusinessTraveller\\src\\main\\resources\\data\\test10.csv") + "\n");
        displayExecutionInfo();
    }
}
