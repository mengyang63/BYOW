package byow.Core.TileObjects;

import byow.Core.Point;
import byow.Core.TileAreas.TileArea;
import byow.Core.TileAreas.TileContainer;
import byow.Core.World;
import byow.TileEngine.TETile;


public abstract class TileObject extends TileArea {

    private TETile tile;


    /* Constructor sets the tiletype and position. */
    public TileObject(Point position, TileContainer superContainer, TETile tile) {
        super(1, 1, position, superContainer);
        superContainer.addElement(this);
        this.tile = tile;
    }

    /* Getters and setters. */
    public Point getLocalPosition() {
        return getLocalLowerLeft();
    }
    public TETile getTileType(World world) {
        return tile;
    }
    public void setTile(TETile tile) {
        this.tile = tile;
    }


    /* Must override so that containers which a tileObject leaves, are informed. */
    @Override
    public void moveGlobalLowerLeft(Point p, World world) {
        TileContainer oldContainer = this.getSuperContainer();
        super.moveGlobalLowerLeft(p, world);
        if (!getSuperContainer().equals(oldContainer)) {
            oldContainer.removeElement(this);
            this.getSuperContainer().addElement(this);
        }
    }

    /* Must override getDepth so that it always returns the depth of the container. */
    @Override
    public int getDepth() {
        return getSuperContainer().getDepth();
    }

    /* Applies a new action to this TileObject. */
    public abstract void applyAction(Action.ActionType type, TileObject actioneer, World world);

    /* The timestep function. By default this does nothing. Meant to be overridden. */
    public abstract void timeStep(char currCommand, World world);
}
