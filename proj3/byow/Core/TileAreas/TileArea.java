package byow.Core.TileAreas;

import byow.Core.Point;
import byow.Core.TileObjects.TileObject;
import byow.Core.World;

import java.util.HashSet;
import java.util.Set;

public class TileArea {
    private Point lowerLeft;
    private int width;
    private int height;
    private TileContainer superContainer;
    private int depth;
    public static final int PROJ_UP = 1;
    public static final int PROJ_DOWN = -1;
    public static final int PROJ_RIGHT = 2;
    public static final int PROJ_LEFT = -2;

    /* A TileArea is defined by its width and height, a superContainer, and a local lowerLeft. */
    public TileArea(int width, int height, Point lowerLeft, TileContainer superContainer) {
        this.width = width;
        this.height = height;
        this.superContainer = superContainer;
        this.lowerLeft = lowerLeft;
    }

    /* Returns a seed-random local point on the perimeter of two points. Inclusively. */
    public Point randomOuterPoint(World world) {
        return randomOuterPoint(world, 0);
    }

    /* Returns a seed-random local point on perimeter, attenuating an amount off of corners. */
    public Point randomOuterPoint(World world, int cornerAttenuation) {
        int randomProj = getRandomProj(world);
        return randomOuterPointProjected(world, randomProj, cornerAttenuation);
    }

    /* Returns a seed-random proj direction. */
    public static int getRandomProj(World world) {
        int num = world.getPureRandom(-2, 3);
        if (num == 0) {
            return getRandomProj(world);
        }
        return num;
    }

    /* Returns seed-random local point on the perimeter, for given projection. */
    protected Point randomOuterPointProjected(World world, int randomProj) {
        return randomOuterPointProjected(world, randomProj, 0);
    }

    /* Returns seed-random local point on the perimeter,
    for a given projection and corner attenuation. */
    protected Point randomOuterPointProjected(World world, int randomProj, int cornerAttenuation) {
        int randX;
        int randY;
        switch (randomProj) {
            case PROJ_UP:
                randX = world.getPureRandom(cornerAttenuation, getWidth() - cornerAttenuation);
                randY = getHeight() - 1;
                break;
            case PROJ_DOWN:
                randX = world.getPureRandom(cornerAttenuation, getWidth() - cornerAttenuation);
                randY = 0;
                break;
            case PROJ_LEFT:
                randX = 0;
                randY = world.getPureRandom(cornerAttenuation, getHeight() - cornerAttenuation);
                break;
            case PROJ_RIGHT:
                randX = getWidth() - 1;
                randY = world.getPureRandom(cornerAttenuation, getHeight() - cornerAttenuation);
                break;
            default:
                randX = 0;
                randY = 0;
                break;
        }
        return new Point(randX, randY);
    }

    /* Returns a seed-random local point, strictly not along perimeter. */
    public Point randomInnerPoint(World world) {
        if (getWidth() < 3 || getHeight() < 3) {
            return null;
        }
        int randX = world.getPureRandom(1, getWidth() - 1);
        int randY = world.getPureRandom(1, getHeight() - 1);
        return new Point(randX, randY);
    }

    /* Returns a seed-random local point. Inclusively. */
    public Point randomPoint(World world) {
        int randX = world.getPureRandom(0, getWidth());
        int randY = world.getPureRandom(0, getHeight());
        return new Point(randX, randY);
    }

    /* Returns whether a global point is in bounds. */
    public boolean globalPointInBounds(Point p) {
        if (this.getGlobalLowerLeft().getX() <= p.getX()
            && this.getGlobalLowerLeft().getY() <= p.getY()
            && this.getGlobalUpperRight().getX() >= p.getX()
            && this.getGlobalUpperRight().getY() >= p.getY()) {
            return true;
        }
        return false;
    }

    /* Returns whether two TileAreas are intersecting. */
    public static boolean checkCollision(TileArea a, TileArea b) {
        if (a.getArea() == 1 && b.getArea() == 1) {
            return (a.getGlobalLowerLeft().equals(b.getGlobalLowerLeft()));
        }
        if (checkXCollision(a, b) && checkYCollision(a, b)) {
            return true;
        }
        return false;
    }

    /* Returns whether two TileAreas are intersecting. */
    public boolean checkCollision(TileArea b) {
        return checkCollision(this, b);
    }

    /* Returns whether two TileAreas are intersecting in the xDirection. */
    private static boolean checkXCollision(TileArea a, TileArea b) {
        if (a.getGlobalLowerLeft().getX() <= b.getGlobalLowerLeft().getX()
                && a.getGlobalUpperRight().getX() >= b.getGlobalLowerLeft().getX()) {
            return true;
        }
        if (a.getGlobalUpperRight().getX() >= b.getGlobalUpperRight().getX()
                && a.getGlobalLowerLeft().getX() <= b.getGlobalUpperRight().getX()) {
            return true;
        }
        if (b.getGlobalLowerLeft().getX() <= a.getGlobalLowerLeft().getX()
                && b.getGlobalUpperRight().getX() >= a.getGlobalLowerLeft().getX()) {
            return true;
        }
        if (b.getGlobalUpperRight().getX() >= a.getGlobalUpperRight().getX()
                && b.getGlobalLowerLeft().getX() <= a.getGlobalUpperRight().getX()) {
            return true;
        }
        return false;
    }

    /* Returns whether two TileAreas are intersecting in the yDirection. */
    private static boolean checkYCollision(TileArea a, TileArea b) {
        if (a.getGlobalLowerLeft().getY() <= b.getGlobalLowerLeft().getY()
                && a.getGlobalUpperRight().getY() >= b.getGlobalLowerLeft().getY()) {
            return true;
        }
        if (a.getGlobalUpperRight().getY() >= b.getGlobalUpperRight().getY()
                && a.getGlobalLowerLeft().getY() <= b.getGlobalUpperRight().getY()) {
            return true;
        }
        if (b.getGlobalLowerLeft().getY() <= a.getGlobalLowerLeft().getY()
                && b.getGlobalUpperRight().getY() >= a.getGlobalLowerLeft().getY()) {
            return true;
        }
        if (b.getGlobalUpperRight().getY() >= a.getGlobalUpperRight().getY()
                && b.getGlobalLowerLeft().getY() <= a.getGlobalUpperRight().getY()) {
            return true;
        }
        return false;
    }

    /* Returns whether a TileContainer (a) is completely within another (b). */
    public boolean checkContainedIn(TileArea b) {
        if (this.getGlobalLowerLeft().getX() < b.getGlobalLowerLeft().getX()) {
            return false;
        }
        if (this.getGlobalLowerLeft().getY() < b.getGlobalLowerLeft().getY()) {
            return false;
        }
        if (this.getGlobalUpperRight().getX() > b.getGlobalUpperRight().getX()) {
            return false;
        }
        if (this.getGlobalUpperRight().getY() > b.getGlobalUpperRight().getY()) {
            return false;
        }
        return true;
    }

    /* Returns the tileObject in this object's supercontainer that this tileObject collides with. */
    public TileObject getCollision() {
        for (TileObject o: superContainer.getElements()) {
            if (checkCollision(this, o) && !o.equals(this)) {
                return o;
            }
        }
        return null;
    }

    /* Returns whether a given tile area collides with an existing one in this container. */
    public boolean conflictsWithExistingElement() {
        if (this.getCollision() != null) {
            return true;
        }
        return false;
    }


    /* Getters. */
    public Point getLocalLowerLeft() {
        return lowerLeft;
    }
    public Point getLocalUpperRight() {
        return Point.add(lowerLeft, new Point(width - 1, height - 1));
    }
    public Point getGlobalLowerLeft() {
        if (superContainer == null) {
            return lowerLeft;
        }
        return Point.add(lowerLeft, superContainer.getGlobalLowerLeft());
    }
    public Point getGlobalUpperRight() {
        return Point.add(getGlobalLowerLeft(), new Point(width - 1, height - 1));
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public int getDepth() {
        return depth;
    }
    public TileContainer getSuperContainer() {
        return superContainer;
    }

    /* Returns the area of a TileArea. */
    public int getArea() {
        return width * height;
    }

    /* Returns the perimeter of a TileArea. */
    public int getPerimeter() {
        return 2 * getWidth() + 2 * (getHeight() - 2);
    }


    /* Returns the superContainer of this TileArea, first updating its superContainer. */
    private TileContainer getSuperContainer(World world) {
        HashSet<PathsContainer> visited = new HashSet<>();
        TileContainer ret = getSuperContainer(world.getRoot1(), visited);
        if (ret != null) {
            return ret;
        }
        return world;
    }

    /* Helper function to getSuperContainer. */
    private TileContainer getSuperContainer(PathsContainer currRoot, Set<PathsContainer> visited) {
        if (!visited.contains(currRoot)) {
            visited.add(currRoot);
            if (this.checkContainedIn(currRoot)) {
                return currRoot;
            }
            for (PathsContainer c: currRoot.getLinkedContainers()) {
                TileContainer superC = getSuperContainer(c, visited);
                if (superC != null) {
                    return superC;
                }
            }
        }
        return null;
    }


    /* Setters, */
    public void setWidth(int width) {
        this.width = width;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public void setDepth(int depth) {
        this.depth = depth;
    }
    public void setSuperContainer(TileContainer superContainer) {
        this.superContainer = superContainer;
    }


    /* Sets the global lowerLeft of this TileArea, not changing width and height. */
    public void moveGlobalLowerLeft(Point p, World world) {
        if (!superContainer.globalPointInBounds(p)) {
            this.superContainer = world;
            this.lowerLeft = p;
            this.superContainer = getSuperContainer(world);
        }
        this.lowerLeft = Point.subtract(p, superContainer.getGlobalLowerLeft());
        if (this.superContainer instanceof RoomRoom) {
            RoomRoom asRoomRoom = (RoomRoom) this.superContainer;
            if (this.checkContainedIn(asRoomRoom.getMyRoom())) {
                this.superContainer = asRoomRoom.getMyRoom();
                this.lowerLeft = Point.subtract(p, superContainer.getGlobalLowerLeft());
            }
        }
    }
}

