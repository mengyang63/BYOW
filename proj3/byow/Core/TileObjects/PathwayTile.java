package byow.Core.TileObjects;

import byow.Core.Point;
import byow.Core.TileAreas.TileContainer;
import byow.Core.World;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class PathwayTile extends TileObject {
    public static final TETile TILE = Tileset.GRASS;


    public PathwayTile(Point p, TileContainer superContainer) {
        super(p, superContainer, TILE);
    }

    @Override
    public void timeStep(char currCommand, World world) { }



    @Override
    public void applyAction(Action.ActionType type, TileObject actioneer, World world) {
        new PathwayTile.PathwayTileAction(type, actioneer, this).execute(world);
    }

    private class PathwayTileAction extends Action {
        /* Defined by an action, sent from an actioneer, to a recipient. */
        PathwayTileAction(ActionType tileObjectActionType,
                                 TileObject actioneer, TileObject recipient) {
            super(tileObjectActionType, actioneer, recipient);
        }

        @Override
        public void execute(World world) { }
    }
}
