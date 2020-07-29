package byow.Core.TileAreas;

import byow.Core.Point;
import byow.Core.TileObjects.PathwayTile;
import byow.Core.World;
import byow.TileEngine.TETile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class PathsContainer extends TileContainer {

    private List<PathsContainer> links;

    public PathsContainer(int width, int height, Point lowerLeft, TileContainer superContainer) {
        super(width, height, lowerLeft, superContainer);
        this.links = new ArrayList<>();
    }

    /* Accumulates all of its children's elements into one, two-dimensional tile array.
     * Only adds children who contain the same depth as this root TileContainer when
     * depth does Matter. Adds all when depth doesn't. */
    public void accumulateTiles(TETile[][] accumulatedSoFar, World world, boolean depthMatters) {
        HashSet<TileContainer> visited = new HashSet<>();
        accumulateTiles(accumulatedSoFar, world, getDepth(), visited, depthMatters);
    }

    /* AccumulateTiles helper. */
    protected void accumulateTiles(TETile[][] accumulatedSoFar, World world,
                                 int depth, Set<TileContainer> visited, boolean depthMatters) {
        if (!depthMatters || this.getDepth() == depth) {
            super.accumulateTiles(accumulatedSoFar, world);
        }
        for (PathsContainer c: getLinkedContainers()) {
            if (!visited.contains(c)) {
                visited.add(c);
                c.accumulateTiles(accumulatedSoFar, world, depth, visited, depthMatters);
            }
        }
    }

    /* Assuming that this container used to contain the player
    and that the player must be in an adjacent container,
    finds the root with the player. */
    public PathsContainer getPlayer1Container() {
        if (this.containsPlayer1()) {
            return this;
        }
        for (int i = 0; i < links.size(); i += 1) {
            PathsContainer t = links.get(i);
            if (t.containsPlayer1()) {
                return t;
            }
        }
        throw new IllegalCallerException("None of the neighboring containers contains the player.");
    }

    /* Similarly to getPlayer1Container. */
    public PathsContainer getPlayer2Container() {
        if (this.containsPlayer2()) {
            return this;
        }
        for (int i = 0; i < links.size(); i += 1) {
            PathsContainer t = links.get(i);
            if (t.containsPlayer2()) {
                return t;
            }
        }
        throw new IllegalCallerException("None of the neighboring containers contains the player.");
    }

    /* Returns whether a given tile area collides with an existing room. */
    public boolean conflictsWithExistingRoom(World world) {
        HashSet<PathsContainer> visited = new HashSet<>();
        return conflictsWithExistingRoom(world.getRoot1(), visited);
    }

    /* Helper to conflictsWithExistingRoom. */
    private boolean conflictsWithExistingRoom(PathsContainer currRoot,
                                              Set<PathsContainer> visited) {
        if (!visited.contains(currRoot)) {
            if (this.checkCollision(currRoot)) {
                return true;
            }
            visited.add(currRoot);
            for (PathsContainer c : currRoot.getLinkedContainers()) {
                if (this.conflictsWithExistingRoom(c, visited)) {
                    return true;
                }
            }
        }
        return false;
    }

    /* Returns a list of adjacent/linked Containers. */
    public List<PathsContainer> getLinkedContainers() {
        return links;
    }

    /* Extends a given container relative to a world,
    filling it until an attempted-to-be-added container
    extends off of the world's dimensions. */
    public void extend(World world) {
        extend(world, 1);
    }

    /* Helper to extend. */
    public void extend(World world, int pathsSoFar) {
        if (pathsSoFar < world.getMaxPaths()) {

            int randNumExtensions = world.getPureRandom(10, 30);

            for (int i = 0; i < randNumExtensions; i += 1) {
                PathsContainer newContainer = PathsContainerSet.getRandomTileContainer(world);

                if (newContainer != null) {
                    int randProj = getRandomProj(world);
                    Point myLocalPoint = this.randomOuterPointProjected(world, randProj, 1);
                    Point otherSnapToPoint =
                            newContainer.randomOuterPointProjected(world, -randProj, 1);
                    Point mySnapToPoint;
                    switch (randProj) {
                        case TileArea.PROJ_UP:
                            mySnapToPoint = Point.add(myLocalPoint, new Point(0, 1));
                            break;
                        case TileArea.PROJ_LEFT:
                            mySnapToPoint = Point.add(myLocalPoint, new Point(-1, 0));
                            break;
                        case TileArea.PROJ_RIGHT:
                            mySnapToPoint = Point.add(myLocalPoint, new Point(1, 0));
                            break;
                        case TileArea.PROJ_DOWN:
                            mySnapToPoint = Point.add(myLocalPoint, new Point(0, -1));
                            break;
                        default:
                            mySnapToPoint = new Point(0, 0);
                    }

                    int snapToDepth = this.getDepth();
                    if (world.getPureRandom(0, 6) == 0) {
                        snapToDepth -= world.getPureRandom(-1, 2);
                    }

                    newContainer.snapToAdjust(otherSnapToPoint,
                            Point.add(this.getGlobalLowerLeft(), mySnapToPoint),
                            snapToDepth,
                            world);
                    if (!newContainer.conflictsWithExistingRoom(world)
                            && newContainer.checkContainedIn(world)) {
                        links.add(newContainer);
                        newContainer.getLinkedContainers().add(this);
                        new PathwayTile(myLocalPoint, this);
                        new PathwayTile(otherSnapToPoint, newContainer);
                        newContainer.extend(world, pathsSoFar + 1);
                    }
                }
            }
            this.addWalls(world);
        }
    }
}
