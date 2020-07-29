package byow.Core.TileObjects;

import byow.Core.Point;
import byow.Core.TileAreas.TileContainer;
import byow.Core.World;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;


public class Player extends TileObject {
    public static final TETile TILE = Tileset.AVATAR;
    private boolean isAlive;
    private int playerNumber;


    /* Player constructor. Players have a player number and an alive property. */
    public Player(Point p, TileContainer superContainer, int playerNumber) {
        super(p, superContainer, TILE);
        this.isAlive = true;
        this.playerNumber = playerNumber;
    }

    /* Timesteps the player, taking into account
    *  keyboard presses that procure movement. */
    @Override
    public void timeStep(char currCommand, World world) {
        if (isAlive) {
            if (playerNumber == 1) {
                switch (currCommand) {
                    case 'W':
                        applyAction(Action.ActionType.MOVE_UP, this, world);
                        break;
                    case 'A':
                        applyAction(Action.ActionType.MOVE_LEFT, this, world);
                        break;
                    case 'S':
                        applyAction(Action.ActionType.MOVE_DOWN, this, world);
                        break;
                    case 'D':
                        applyAction(Action.ActionType.MOVE_RIGHT, this, world);
                        break;
                    case 'w':
                        applyAction(Action.ActionType.MOVE_UP, this, world);
                        break;
                    case 'a':
                        applyAction(Action.ActionType.MOVE_LEFT, this, world);
                        break;
                    case 's':
                        applyAction(Action.ActionType.MOVE_DOWN, this, world);
                        break;
                    case 'd':
                        applyAction(Action.ActionType.MOVE_RIGHT, this, world);
                        break;
                    default:
                        return;
                }
            }
            if (playerNumber == 2) {
                switch (currCommand) {
                    case 'I':
                        applyAction(Action.ActionType.MOVE_UP, this, world);
                        break;
                    case 'J':
                        applyAction(Action.ActionType.MOVE_LEFT, this, world);
                        break;
                    case 'K':
                        applyAction(Action.ActionType.MOVE_DOWN, this, world);
                        break;
                    case 'L':
                        applyAction(Action.ActionType.MOVE_RIGHT, this, world);
                        break;
                    case 'i':
                        applyAction(Action.ActionType.MOVE_UP, this, world);
                        break;
                    case 'j':
                        applyAction(Action.ActionType.MOVE_LEFT, this, world);
                        break;
                    case 'k':
                        applyAction(Action.ActionType.MOVE_DOWN, this, world);
                        break;
                    case 'l':
                        applyAction(Action.ActionType.MOVE_RIGHT, this, world);
                        break;
                    default:
                        return;
                }
            }
        }
    }


    @Override
    public void applyAction(Action.ActionType type, TileObject actioneer, World world) {
        new PlayerAction(type, actioneer, this).execute(world);
    }

    private class PlayerAction extends Action {
        /* Defined by an action, sent from an actioneer, to a recipient. */
        PlayerAction(ActionType tileObjectActionType,
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
                    Player asPlayer = (Player) this.getRecipient();
                    asPlayer.isAlive = false;
                    asPlayer.setTile(Tileset.AVATAR_DEAD);
                    break;
                default:
            }
            TileObject collision = getCollision();
            if (collision != null && getType() != ActionType.ATTACK) {
                collision.applyAction(getType(), getRecipient(), world);
            }
        }
    }

    /* Returns the player number of this player. */
    public int getPlayerNumber() {
        return playerNumber;
    }
}
