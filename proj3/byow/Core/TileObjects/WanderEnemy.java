package byow.Core.TileObjects;

import byow.Core.Point;
import byow.Core.TileAreas.TileArea;
import byow.Core.TileAreas.TileContainer;
import byow.Core.World;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;


public class WanderEnemy extends Enemy {

    public static final TETile TILE = Tileset.WANDER_ENEMY;


    public WanderEnemy(Point position, TileContainer superContainer) {
        super(position, superContainer, TILE);
    }

    @Override
    public void applyAction(Action.ActionType type, TileObject actioneer, World world) {
        new WanderEnemyAction(type, actioneer, this).execute(world);
    }

    /* Defines how an enemy will interact with a player. */
    public void interactWithPlayer(Player player, World world) {
        player.applyAction(Action.ActionType.ATTACK, this, world);
    }

    /* Moves the enemy. */
    public void move(char currCommand, World world) {
        int randInt = world.getPureRandom(-2, 100);
        switch (randInt) {
            case TileArea.PROJ_UP:
                this.setTile(Tileset.WANDER_ENEMY_ACTIVE);
                applyAction(Action.ActionType.MOVE_UP, this, world);
                break;
            case TileArea.PROJ_DOWN:
                this.setTile(Tileset.WANDER_ENEMY_ACTIVE);
                applyAction(Action.ActionType.MOVE_DOWN, this, world);
                break;
            case TileArea.PROJ_LEFT:
                this.setTile(Tileset.WANDER_ENEMY_ACTIVE);
                applyAction(Action.ActionType.MOVE_LEFT, this, world);
                break;
            case TileArea.PROJ_RIGHT:
                this.setTile(Tileset.WANDER_ENEMY_ACTIVE);
                applyAction(Action.ActionType.MOVE_RIGHT, this, world);
                break;
            default:
                this.setTile(Tileset.WANDER_ENEMY);
        }
    }



    private class WanderEnemyAction extends Action {
        /* Defined by an action, sent from an actioneer, to a recipient. */
        WanderEnemyAction(Action.ActionType tileObjectActionType,
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
                case ATTACK:

                default:
            }
            TileObject collision = getCollision();
            if (collision != null) {
                if (collision instanceof Player) {
                    interactWithPlayer((Player) collision, world);
                }
                collision.applyAction(getType(), getRecipient(), world);
            }
        }

    }
}
