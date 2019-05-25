public class City {
    private Coordinates coordinates;
    private int id;
    private boolean visited;

    public City() {
        this.coordinates = new Coordinates(0,0);
        this.visited = false;
        this.id = 1;
    }

    public City(Coordinates coord,int id) {
        this.coordinates = coord;
        this.id = id;
    }

    public City(double x, double y,int id) {
        this.coordinates = new Coordinates(x, y);
        this.id = id;
    }

    /**
     * @return True if the city had been visited false otherwise
     */
    public boolean getVisited(){
        return this.visited;
    }

    /**
     * @return the id of the city
     */
    public int getid(){return id;}

    /**
     * Set the city as visited
     * @param state
     */
    public void setVisited(boolean state){this.visited = state;}


    /**
     * @param city
     * @return the distance between the current city and the given city
     */
    public double getDistanceWith(City city){
        return Math.sqrt(Math.pow((this.coordinates.getX()-city.coordinates.getX()),2) + Math.pow((this.coordinates.getY()-city.coordinates.getY()),2));
    }

    /**
     * @return the coordinates of the city
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    @Override
    public String toString() {
        return new String("La ville numéro"+this.id+" ayant pour cord"+this.getCoordinates().getX() + ";" + this.getCoordinates().getY());
    }
}
