package byow.Core.TileObjects;

import byow.Core.Point;
import byow.Core.TileAreas.TileContainer;
import byow.Core.World;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class Wall extends TileObject {
    public static final TETile TILE = Tileset.WALL;


    public Wall(Point p, TileContainer superContainer) {
        super(p, superContainer, TILE);
    }

    @Override
    public void timeStep(char currCommand, World world) { }

    @Override
    public void applyAction(Action.ActionType type, TileObject actioneer, World world) {
        new WallAction(type, actioneer, this).execute(world);
    }

    private class WallAction extends Action {
        /* Defined by an action, sent from an actioneer, to a recipient. */
        WallAction(ActionType tileObjectActionType,
                          TileObject actioneer, TileObject recipient) {
            super(tileObjectActionType, actioneer, recipient);
        }

        @Override
        public void execute(World world) {
            switch (getType()) {
                case MOVE_UP:
                    getActioneer().applyAction(ActionType.MOVE_DOWN, getRecipient(), world);
                    break;
                case MOVE_LEFT:
                    getActioneer().applyAction(ActionType.MOVE_RIGHT, getRecipient(), world);
                    break;
                case MOVE_DOWN:
                    getActioneer().applyAction(ActionType.MOVE_UP, getRecipient(), world);
                    break;
                case MOVE_RIGHT:
                    getActioneer().applyAction(ActionType.MOVE_LEFT, getRecipient(), world);
                    break;
                default:
            }
        }
    }
}
