package byow.Core.TileObjects;

import byow.Core.Point;
import byow.Core.TileAreas.TileContainer;
import byow.Core.World;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class Rock extends TileObject {
    public static final TETile TILE = Tileset.ROCK;


    public Rock(Point p, TileContainer superContainer) {
        super(p, superContainer, TILE);
        if (this.conflictsWithExistingElement()) {
            superContainer.removeElement(this);
        }
    }

    @Override
    public void timeStep(char currCommand, World world) {
    }

    @Override
    public void applyAction(Action.ActionType type, TileObject actioneer, World world) {
        new Rock.RockAction(type, actioneer, this).execute(world);
    }

    private class RockAction extends Action {
        /* Defined by an action, sent from an actioneer, to a recipient. */
        RockAction(ActionType tileObjectActionType,
                          TileObject actioneer, TileObject recipient) {
            super(tileObjectActionType, actioneer, recipient);
        }

        @Override
        public void execute(World world) {
            Point moveVector;
            switch (getType()) {
                case MOVE_UP:
                    moveVector = new Point(0, 1);
                    moveGlobalLowerLeft(Point.add(getGlobalLowerLeft(), moveVector), world);
                    break;
                case MOVE_LEFT:
                    moveVector = new Point(-1, 0);
                    moveGlobalLowerLeft(Point.add(getGlobalLowerLeft(), moveVector), world);
                    break;
                case MOVE_DOWN:
                    moveVector = new Point(0, -1);
                    moveGlobalLowerLeft(Point.add(getGlobalLowerLeft(), moveVector), world);
                    break;
                case MOVE_RIGHT:
                    moveVector = new Point(1, 0);
                    moveGlobalLowerLeft(Point.add(getGlobalLowerLeft(), moveVector), world);
                    break;
                default:
            }
            TileObject collision = getCollision();
            if (collision != null) {
                collision.applyAction(getType(), getRecipient(), world);
            }
        }
    }
}
