public class BusinessTraveller {

    private static double startTime;

    private static void startTimer(){
        startTime = System.nanoTime();
    }

    private static double getSecondDuration(){
        return ((double)System.nanoTime() - startTime) / 1000000000;
    }

    private static double getMemoryUsage(){
        return ((double) Runtime.getRuntime().totalMemory() -  Runtime.getRuntime().freeMemory() ) / 1048576;
    }

    public static void main(String[] args){
        startTimer();
        try {
            Thread.sleep(345);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(" Duration : " + getSecondDuration() + " seconds");
        System.out.println(" Memory usage : " + getMemoryUsage()+ " MB ");
    }
}
