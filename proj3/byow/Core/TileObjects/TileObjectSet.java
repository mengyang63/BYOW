package byow.Core.TileObjects;

import byow.Core.Point;
import byow.Core.TileAreas.TileContainer;
import byow.Core.World;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 *
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 *
 * Ex:
 *      world[x][y] = Tileset.FLOOR;
 *
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class TileObjectSet {
    private static final int PLAYER = 0;
    private static final int PATHWAYTILE = 1;
    private static final int WALL = 2;
    private static final int ROCK = 3;
    private static final int WANDER_ENEMY = 4;
    private static final int[] TILE_OBJECTS = {ROCK, ROCK, ROCK, WANDER_ENEMY};

    public static TileObject getRandomTileObject(World world, TileContainer superContainer) {
        int choice = TILE_OBJECTS[world.getPureRandom(0, TILE_OBJECTS.length)];
        Point randInnerPoint = superContainer.randomInnerPoint(world);
        switch (choice) {
            case ROCK:
                return new Rock(randInnerPoint, superContainer);
            case WANDER_ENEMY:
                return new WanderEnemy(randInnerPoint, superContainer);
            default:
                return null;
        }
    }
}

