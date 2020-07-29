package byow.Core.TileAreas;

import byow.Core.Point;
import byow.Core.World;

public class Hallway extends PathsContainer {
    private static final int MIN_SMALL_LENGTH = 3;
    private static final int MIN_LONG_LENGTH = 5;
    private static final int MAX_SMALL_LENGTH = 3;
    private static final int MAX_LONG_LENGTH = 14;
    private static final int WORKABLE_AREA_ELEMENT_RATIO = 6;
        //How many inner spaces must exist per element.
    private static final int PROJ_VERT = 0;
    private static final int PROJ_HOR = 1;

    public Hallway(int width, int height, Point lowerLeft, TileContainer superContainer) {
        super(width, height, lowerLeft, superContainer);
    }

    /* Sets up random elements. */
    public void setUpRandomElements(World world) {
        //Having any element at all in a hallway tends to ruin the game experience.
        /*
        int numElements = world.getPureRandom(0, this.getWorkableArea())
                / WORKABLE_AREA_ELEMENT_RATIO;
        for (int i = 0; i < numElements; i += 1) {
            TileObject o = TileObjectSet.getRandomTileObject(world, this);
            if (o.conflictsWithExistingElement()) {
                this.removeElement(o);
            }
        }
        */
    }


    /* Creates a random hallway at a completely random location in the world. */
    public static Hallway newRandomHallway(World world) {
        int randProj = world.getPureRandom(0, 2);
        int randWidth;
        int randHeight;
        switch (randProj) {
            case PROJ_HOR:
                randWidth = world.getPureRandom(MIN_LONG_LENGTH, MAX_LONG_LENGTH + 1);
                randHeight = world.getPureRandom(MIN_SMALL_LENGTH, MAX_SMALL_LENGTH + 1);
                break;
            case PROJ_VERT:
                randWidth = world.getPureRandom(MIN_SMALL_LENGTH, MAX_SMALL_LENGTH + 1);
                randHeight = world.getPureRandom(MIN_LONG_LENGTH, MAX_LONG_LENGTH + 1);
                break;
            default:
                randWidth = 0;
                randHeight = 0;
        }

        int randX = world.getPureRandom(randWidth, world.getWidth() - randWidth);
        int randY = world.getPureRandom(randHeight, world.getHeight() - randHeight);
        Hallway ret = new Hallway(randWidth, randHeight, new Point(randX, randY), world);
        ret.setUpRandomElements(world);
        return ret;
    }
}

