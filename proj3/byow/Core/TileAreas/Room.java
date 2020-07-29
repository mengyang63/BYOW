package byow.Core.TileAreas;

import byow.Core.Point;
import byow.Core.TileObjects.TileObject;
import byow.Core.TileObjects.TileObjectSet;
import byow.Core.World;

public class Room extends PathsContainer {
    private static final int MIN_LENGTH = 5; //This includes exterior walls.
    private static final int MAX_LENGTH = 14; //This includes exterior walls.
    private static final int WORKABLE_AREA_ELEMENT_RATIO = 3;
        //How many inner spaces must exist per element.

    public Room(int width, int height, Point lowerLeft, TileContainer superContainer) {
        super(width, height, lowerLeft, superContainer);
    }

    /* Sets up random internal elements. */
    public void setUpRandomElements(World world) {
        int numElements = world.getPureRandom(0, this.getWorkableArea())
                / WORKABLE_AREA_ELEMENT_RATIO;
        for (int i = 0; i < numElements; i += 1) {
            TileObject o = TileObjectSet.getRandomTileObject(world, this);
            if (o.conflictsWithExistingElement()) {
                this.removeElement(o);
            }
        }
    }

    /* Creates a random room at a completely random location in the world. */
    public static Room newRandomRoom(World world) {
        return newRandomRoom(world, world, MIN_LENGTH, MAX_LENGTH);
    }

    /* Creates a random room at a completely random location within a tileArea.
    *  A subliminal requirement for this method is that the maxLength is at most
    *  1/2 of the superArea's minLength. */
    public static Room newRandomRoom(TileContainer superArea,
                                     World world, int minLength, int maxLength) {
        int randWidth = world.getPureRandom(minLength, maxLength + 1);
        int randHeight = world.getPureRandom(minLength, maxLength + 1);
        int randX = world.getPureRandom(randWidth, superArea.getWidth() - randWidth);
        int randY = world.getPureRandom(randHeight, superArea.getHeight() - randHeight);
        Room ret = new Room(randWidth, randHeight, new Point(randX, randY), world);
        ret.setUpRandomElements(world);
        ret.setSuperContainer(superArea);
        return ret;
    }
}
