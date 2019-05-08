public class DepartureCity extends City {
    int visitedCount;
    public DepartureCity() {
        super();
    }

    public DepartureCity(Coordinates coord,int id) {
        super(coord,id);
    }

    public DepartureCity(double x, double y,int id) {
        super(x,y,id);
    }

    /**
     * Set the city as visited only if it is visited twice as it is as DepartureCity it have to be visited twice
     * @param state
     */
    @Override
    public void setVisited(boolean state) {
        if(visitedCount == 2){
            super.setVisited(state);
        }
    }
}
