package byow.Core.TileAreas;

import byow.Core.Point;
import byow.Core.TileObjects.PathwayTile;
import byow.Core.TileObjects.TileObject;
import byow.Core.World;
import byow.TileEngine.TETile;

import java.util.Set;

public class RoomRoom extends PathsContainer {
    private static final int MIN_LENGTH = 14; //This includes exterior walls.
    private static final int MAX_LENGTH = 22; //This includes exterior walls.
    private static final int CHANCE_TO_MOVE_ROOM = 30;
    private Room myRoom;


    /* RoomRooms are defined by their inner Room. */
    public RoomRoom(int width, int height, Point lowerLeft, TileContainer superContainer) {
        super(width, height, lowerLeft, superContainer);
        myRoom = null;
    }

    /* Sets up random elements, namely an internal room. */
    public void setUpRandomElements(World world) {
        myRoom = Room.newRandomRoom(this, world, 5,
                (Math.min(this.getWidth(), this.getHeight()) / 2) - 1);
        myRoom.setDepth(this.getDepth());
        getLinkedContainers().add(myRoom);
        myRoom.getLinkedContainers().add(this);
        Point entryPoint = myRoom.randomOuterPoint(world, 1);
        new PathwayTile(entryPoint, myRoom);
        myRoom.addWalls(world.getPureRandom(-1, 2), world);
    }

    /* Creates a random room room at a completely random location in the world. */
    public static RoomRoom newRandomRoomRoom(World world) {
        int randWidth = world.getPureRandom(MIN_LENGTH, MAX_LENGTH + 1);
        int randHeight = world.getPureRandom(MIN_LENGTH, MAX_LENGTH + 1);
        int randX = world.getPureRandom(randWidth, world.getWidth() - randWidth);
        int randY = world.getPureRandom(randHeight, world.getHeight() - randHeight);
        RoomRoom ret = new RoomRoom(randWidth, randHeight, new Point(randX, randY), world);
        ret.setUpRandomElements(world);
        return ret;
    }


    /* Accumulates tiles of room regardless of what depth they are. */
    @Override
    public void accumulateTiles(TETile[][] accumulatedSoFar, World world, boolean depthMatters) {
        super.accumulateTiles(accumulatedSoFar, world, depthMatters);
        myRoom.accumulateTiles(accumulatedSoFar, world);
    }


    /* We will move the internal room every x chance timesteps. */
    @Override
    public void timeStep(char currCommand, Set<TileObject> traversed, World world) {
        myRoom.setDepth(getDepth());
        super.timeStep(currCommand, traversed, world);
        int randChance = world.getPureRandom(0, CHANCE_TO_MOVE_ROOM);
        if (randChance == 0) {
            int randProj = getRandomProj(world);
            Point moveVec;
            Point endVec;
            switch (randProj) {
                case TileArea.PROJ_UP:
                    moveVec = new Point(0, 1);
                    endVec = Point.add(myRoom.getLocalLowerLeft(), moveVec);
                    myRoom.getLocalLowerLeft().setX(endVec.getX());
                    myRoom.getLocalLowerLeft().setY(endVec.getY());
                    if (myRoom.conflictsWithExistingElement()) {
                        moveVec = new Point(0, -1);
                        endVec = Point.add(myRoom.getLocalLowerLeft(), moveVec);
                        myRoom.getLocalLowerLeft().setX(endVec.getX());
                        myRoom.getLocalLowerLeft().setY(endVec.getY());
                    }
                    break;
                case TileArea.PROJ_DOWN:
                    moveVec = new Point(0, -1);
                    endVec = Point.add(myRoom.getLocalLowerLeft(), moveVec);
                    myRoom.getLocalLowerLeft().setX(endVec.getX());
                    myRoom.getLocalLowerLeft().setY(endVec.getY());
                    if (myRoom.conflictsWithExistingElement()) {
                        moveVec = new Point(0, 1);
                        endVec = Point.add(myRoom.getLocalLowerLeft(), moveVec);
                        myRoom.getLocalLowerLeft().setX(endVec.getX());
                        myRoom.getLocalLowerLeft().setY(endVec.getY());
                    }
                    break;
                case TileArea.PROJ_RIGHT:
                    moveVec = new Point(1, 0);
                    endVec = Point.add(myRoom.getLocalLowerLeft(), moveVec);
                    myRoom.getLocalLowerLeft().setX(endVec.getX());
                    myRoom.getLocalLowerLeft().setY(endVec.getY());
                    if (myRoom.conflictsWithExistingElement()) {
                        moveVec = new Point(-1, 0);
                        endVec = Point.add(myRoom.getLocalLowerLeft(), moveVec);
                        myRoom.getLocalLowerLeft().setX(endVec.getX());
                        myRoom.getLocalLowerLeft().setY(endVec.getY());
                    }
                    break;
                case TileArea.PROJ_LEFT:
                    moveVec = new Point(-1, 0);
                    endVec = Point.add(myRoom.getLocalLowerLeft(), moveVec);
                    myRoom.getLocalLowerLeft().setX(endVec.getX());
                    myRoom.getLocalLowerLeft().setY(endVec.getY());
                    if (myRoom.conflictsWithExistingElement()) {
                        moveVec = new Point(1, 0);
                        endVec = Point.add(myRoom.getLocalLowerLeft(), moveVec);
                        myRoom.getLocalLowerLeft().setX(endVec.getX());
                        myRoom.getLocalLowerLeft().setY(endVec.getY());
                    }
                    break;
                default:
            }
        }
    }
    /* Returns the room within this room. */
    public Room getMyRoom() {
        return myRoom;
    }
}
