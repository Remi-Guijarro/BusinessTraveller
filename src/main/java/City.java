public class City {
    private Coordinates coordinates;
    private int id;
    private boolean visited;

    public City() {
        this.coordinates = new Coordinates(0,0);
        this.visited = false;
        this.id = 1;
    }

    public boolean getVisited(){
        return this.visited;
    }

    public int getid(){return id;}

    public void setVisited(boolean state){this.visited = state;}

    public City(Coordinates coord,int id) {
        this.coordinates = coord;
        this.id = id;
    }

    public City(double x, double y,int id) {
        this.coordinates = new Coordinates(x, y);
        this.id = id;
    }

    public double getDistanceWith(City city){
        return Math.sqrt(Math.pow((this.coordinates.getX()-city.coordinates.getX()),2) + Math.pow((this.coordinates.getY()-city.coordinates.getY()),2));
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }
}
