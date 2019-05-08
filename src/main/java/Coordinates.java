public class Coordinates {
    private double x;
    private double y;

    public Coordinates(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return the X Coordinate
     */
    public double getX() {
        return x;
    }

    /**
     * Set the X Coordinate
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * @return the X Coordinate
     */
    public double getY() {
        return y;
    }

    /**
     * set Y Coordinate
     */
    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Coordinates{");
        sb.append("x=").append(x);
        sb.append(", y=").append(y);
        sb.append('}');
        return sb.toString();
    }
}
