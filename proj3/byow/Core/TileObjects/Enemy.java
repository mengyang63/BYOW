package byow.Core.TileObjects;

import byow.Core.Point;
import byow.Core.TileAreas.TileContainer;
import byow.Core.World;
import byow.TileEngine.TETile;

public abstract class Enemy extends TileObject {

    public Enemy(Point position, TileContainer superContainer, TETile tile) {
        super(position, superContainer, tile);
        if (this.conflictsWithExistingElement()) {
            superContainer.removeElement(this);
        }
    }


    @Override
    public void timeStep(char currCommand, World world) {
        move(currCommand, world);
    }

    /* Defines how an enemy will interact with a player. */
    public abstract void interactWithPlayer(Player player, World world);

    /* Moves the enemy. */
    public abstract void move(char currCommand, World world);
}
