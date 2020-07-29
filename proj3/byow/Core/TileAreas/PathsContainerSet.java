package byow.Core.TileAreas;

import byow.Core.World;


/**
 * Contains constant tile containers, to avoid having to remake the same tiles in different parts of
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

public class PathsContainerSet {
    public static final int ROOM = 0;
    public static final int HALLWAY = 1;
    public static final int ROOMROOM = 2;
    public static final int[] TILE_CONTAINERS = {ROOM, ROOM, ROOM, ROOM, ROOM,
                                                 HALLWAY, HALLWAY, HALLWAY, ROOMROOM};

    /* Returns a random Tile Container. */
    public static PathsContainer getRandomTileContainer(World world) {
        int choice = TILE_CONTAINERS[world.getPureRandom(0, TILE_CONTAINERS.length)];
        PathsContainer ret;
        switch (choice) {
            case ROOM:
                ret = Room.newRandomRoom(world);
                break;
            case HALLWAY:
                ret = Hallway.newRandomHallway(world);
                break;
            case ROOMROOM:
                ret = RoomRoom.newRandomRoomRoom(world);
                break;
            default:
                ret = null;
        }
        return ret;
    }
}

