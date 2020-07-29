package byow.Core;


public class Point {
    private int x;
    private int y;

    /* Constructor. */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /* Getters and Setters. */
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }


    /* Returns euclidian distance SQUARED between two points. */
    public static double distance(Point a, Point b) {
        return (b.getX() - a.getX()) * (b.getX() - a.getX())
                + (b.getY() - a.getY()) * (b.getY() - a.getY());
    }

    /* Returns the difference between x coordinates + the difference in y coordinates. */
    public static int simpleDistance(Point a, Point b) {
        return Math.abs(b.getX() - a.getX()) + Math.abs(b.getY() - a.getY());
    }

    /* Returns the point made by adding two points. */
    public static Point add(Point a, Point b) {
        Point ret = new Point(a.getX(), a.getY());
        ret.setX(ret.getX() + b.getX());
        ret.setY(ret.getY() + b.getY());
        return ret;
    }

    /* Returns the point made by subtracting two points. */
    public static Point subtract(Point a, Point b) {
        Point ret = new Point(a.getX(), a.getY());
        ret.setX(ret.getX() - b.getX());
        ret.setY(ret.getY() - b.getY());
        return ret;
    }

    /* Tostring method for points. */
    @Override
    public String toString() {
        return "(" + this.x + "," + this.y + ")";
    }


    /* Equals method for points. */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Point) {
            Point asPoint = (Point) obj;
            if (asPoint.x == this.x && asPoint.y == this.y) {
                return true;
            }
        }
        return false;
    }

    /* Hashcode for points. */
    @Override
    public int hashCode() {
        return this.x + this.y;
    }
}
