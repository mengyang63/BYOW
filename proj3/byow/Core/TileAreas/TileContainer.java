package byow.Core.TileAreas;

import byow.Core.Point;
import byow.Core.TileObjects.Player;
import byow.Core.TileObjects.TileObject;
import byow.Core.TileObjects.Wall;
import byow.Core.World;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class TileContainer extends TileArea {

    private ArrayList<TileObject> elements;

    /* Constructor for TileContainer using stuff as a parameter. */
    public TileContainer(int width, int height, Point lowerLeft, TileContainer superContainer) {
        super(width, height, lowerLeft, superContainer);
        elements = new ArrayList<>();
    }

    /* Adds this tileContainer's contribution to the TETile set. */
    public void accumulateTiles(TETile[][] accumulatedSoFar, World world) {
        for (int x = 0; x < getWidth(); x += 1) {
            for (int y = 0; y < getHeight(); y += 1) {
                accumulatedSoFar[x + getGlobalLowerLeft().getX()]
                        [y + getGlobalLowerLeft().getY()] = Tileset.depthBasedFloor(getDepth());
            }
        }
        for (TileObject o: getElements()) {
            Point globPoint = o.getGlobalLowerLeft();
            accumulatedSoFar[globPoint.getX()][globPoint.getY()] = o.getTileType(world);
        }
    }


    /* Getters and Setters. */

    /* Adds an element to this container at its location.
     This will override anything that is already there.
     Major note. This is automatically called when making
     a new TileObject. Do not attempt to call this again. */
    public void addElement(TileObject o) {
        this.elements.add(o);
    }


    /* Removes an element from this TileContainer. */
    public void removeElement(TileObject o) {
        this.elements.remove(o);
    }

    /* Returns an arrayList of elements. */
    public List<TileObject> getElements() {
        return elements;
    }

    /* Returns the workable area of the room, usually meaning the area - the perimeter. */
    protected int getWorkableArea() {
        return this.getArea() -  this.getPerimeter();
    }

    /* Returns true if player1 is in this container. */
    public boolean containsPlayer1() {
        for (TileObject o: elements) {
            if (o instanceof Player) {
                Player asPlayer = (Player) o;
                if (((Player) o).getPlayerNumber() == 1) {
                    return true;
                }
            }
        }
        return false;
    }

    /* Returns true if player2 is in this container. */
    public boolean containsPlayer2() {
        for (TileObject o: elements) {
            if (o instanceof Player) {
                Player asPlayer = (Player) o;
                if (((Player) o).getPlayerNumber() == 2) {
                    return true;
                }
            }
        }
        return false;
    }

    /* Adds a player to the selected Container. */
    public void addPlayerRandomLocation(World world, int playerNumber) {
        new Player(randomInnerPoint(world), this, playerNumber);
    }

    /* Returns true if specific object is in the container. */
    public boolean contains(TileObject a) {
        for (int i = 0; i < elements.size(); i += 1) {
            TileObject b = elements.get(i);
            if (a.equals(b)) {
                return true;
            }
        }
        return false;
    }

    /* Iterates through all the objects in this container and progresses them one timestep. */
    public void timeStep(char currCommand, Set<TileObject> traversed, World world) {
        for (int i = 0; i < elements.size(); i += 1) {
            TileObject o = elements.get(i);
            if (o != null && !traversed.contains(o)) {
                o.timeStep(currCommand, world);
                traversed.add(o);
            }
        }
    }

    /* Adjusts position to make a local point reach a global point. */
    public void snapToAdjust(Point localPoint, Point adjustTo, int adjToDepth, World world) {
        Point globalPoint = Point.add(getGlobalLowerLeft(), localPoint);
        Point difference = Point.subtract(adjustTo, globalPoint);
        moveGlobalLowerLeft(Point.add(getGlobalLowerLeft(), difference), world);
        this.setDepth(adjToDepth);
    }

    /* Adds walls to the perimeter of the container. */
    public void addWalls(World world) {
        addWalls(this.getDepth(), world);
    }

    /* Adds walls considering a specific depth. */
    public void addWalls(int depth, World world) {
        for (int y = 0; y < this.getHeight(); y++) {
            if (y == 0 || y == this.getHeight() - 1) {
                for (int x = 0; x < this.getWidth(); x++) {
                    Wall w = new Wall(new Point(x, y), this);
                    w.setTile(Tileset.depthBasedWall(getDepth()));
                    TileObject pathwayTile = w.getCollision();
                    if (pathwayTile != null) {
                        elements.remove(w);
                        elements.remove(pathwayTile);
                    }
                }
            } else {
                for (int x = 0; x < this.getWidth(); x++) {
                    if (x == 0 || x == this.getWidth() - 1) {
                        Wall w = new Wall(new Point(x, y), this);
                        w.setTile(Tileset.depthBasedWall(getDepth()));
                        TileObject pathwayTile = w.getCollision();
                        if (pathwayTile != null) {
                            elements.remove(w);
                            elements.remove(pathwayTile);
                        }
                    }
                }
            }
        }
    }

    /* Sets up the random inner-workings of the container. */
    protected abstract void setUpRandomElements(World world);
}
