package byow.Core.TileObjects;

import byow.Core.World;

public abstract class Action {
    private ActionType type;
    private TileObject actioneer;
    private TileObject recipient;



    /* Defined by an action, sent from an actioneer, to a recipient. */
    public Action(ActionType tileObjectActionType, TileObject actioneer, TileObject recipient) {
        this.type = tileObjectActionType;
        this.actioneer = actioneer;
        this.recipient = recipient;
    }



    /* Executes the given command. */
    public abstract void execute(World world);


    /* Getters */
    public ActionType getType() {
        return type;
    }
    public TileObject getActioneer() {
        return actioneer;
    }
    public TileObject getRecipient() {
        return recipient;
    }

    /* Describes the action type. */
    public enum ActionType {
        MOVE_LEFT, MOVE_RIGHT, MOVE_UP, MOVE_DOWN, ATTACK;
    }
}
