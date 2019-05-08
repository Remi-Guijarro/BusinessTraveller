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

    @Override
    public void setVisited(boolean state) {
        if(visitedCount == 2){
            super.setVisited(state);
        }
    }
}
