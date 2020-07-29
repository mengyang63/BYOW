package byow.Core;

import byow.Core.TileAreas.PathsContainer;
import byow.Core.TileAreas.Room;
import byow.Core.TileAreas.TileContainer;
import byow.Core.TileObjects.TileObject;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class World extends TileContainer {
    private static final int WIDTH = 80;
    private static final int HEIGHT = 48;
    private static final int MAX_PATHS = 200;
    private static final Point LOWER_LEFT = new Point(0, 0);
    private static final Point UPPER_RIGHT = new Point(World.WIDTH, World.HEIGHT);
    private long seed;
    private Random random;
    private PathsContainer root1;
    private PathsContainer root2;
    private int maxPaths;


    /* Constructor takes in a seed to make random decisions with. */
    public World(long seed) {
        super(WIDTH, HEIGHT, new Point(0, 0), null);
        this.maxPaths = MAX_PATHS;
        this.seed = seed;
        setUpRandomElements(this);
        timeStepHelper(' ', new HashSet<TileObject>(),
                root1, new HashSet<PathsContainer>());
    }

    /* Getters and setters. */

    /* Tiles for rendering. */
    public TETile[][] getTiles(boolean depthMatters) {
        TETile[][] ret = new TETile[getWidth()][getHeight()];
        for (int x = 0; x < getWidth(); x += 1) {
            for (int y = 0; y < getHeight(); y += 1) {
                ret[x][y] = Tileset.NOTHING;
            }
        }
        root1.accumulateTiles(ret, this, depthMatters);
        root2.accumulateTiles(ret, this, depthMatters);
        return ret;
    }


    /* Root getter. */
    public PathsContainer getRoot1() {
        return root1;
    }

    /* Root getter. */
    public PathsContainer getRoot2() {
        return root2;
    }

    /* Seed getter. */
    public long getSeed() {
        return seed;
    }

    /* Returns the number of paths the world can sustain. */
    public int getMaxPaths() {
        return maxPaths;
    }

    /* Gets the random of this World. */
    public Random getRandom() {
        return random;
    }

    /* Updates the root to contain the player. */
    private void updateRoots() {
        root1 = root1.getPlayer1Container();
        root2 = root2.getPlayer2Container();
    }


    /* Takes in a command character and applies it to *(all tiles)* */
    public void timeStep(char currCommand) {
        HashSet<TileObject> steppedObjects = new HashSet<>();
        root1.timeStep(currCommand, steppedObjects, this);
        for (TileContainer c : root1.getLinkedContainers()) {
            c.timeStep(currCommand, steppedObjects, this);
        }
        updateRoots();
        root2.timeStep(currCommand, steppedObjects, this);
        for (TileContainer c : root2.getLinkedContainers()) {
            c.timeStep(currCommand, steppedObjects, this);
        }
        updateRoots();
    }

    /* This runs all of our stuff one time before the game starts. */
    private void timeStepHelper(char currCommand, Set<TileObject> steppedObjects,
                                PathsContainer container, Set<PathsContainer> traversed) {
        container.timeStep(currCommand, steppedObjects, this);
        for (PathsContainer c: container.getLinkedContainers()) {
            if (!traversed.contains(c)) {
                traversed.add(c);
                timeStepHelper(currCommand, steppedObjects, c, traversed);
            }

        }
        updateRoots();
    }



    /* Sets up the random elements of this world. */
    @Override
    protected void setUpRandomElements(World world) {
        this.random = new Random(seed);
        root1 = Room.newRandomRoom(this);
        root1.addPlayerRandomLocation(this, 1);
        root2 = root1;
        root2.addPlayerRandomLocation(this, 2);
        root1.extend(this);
        updateRoots();
    }

    /* Returns a random integer between the min (inclusive) and max (exclusive). */
    public int getPureRandom(int min, int max) {
        return RandomUtils.uniform(random, min, max);
    }
}
