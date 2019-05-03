public class City {
    private Coordinates coordinates;

    public City() {
        this.coordinates = new Coordinates(0,0);
    }

    public City(Coordinates coord) {
        this.coordinates = coord;
    }

    public City(double x, double y) {
        this.coordinates = new Coordinates(x, y);
    }

    public double getDistanceWith(City city){
        return Math.sqrt(Math.pow((this.coordinates.getX()-city.coordinates.getX()),2) + Math.pow((this.coordinates.getY()-city.coordinates.getY()),2));
    }
}
