package byow.Core.InputHandler;

/**
 * Created by hug.
 */
import edu.princeton.cs.introcs.StdDraw;

public class KeyboardInputSource implements InputSource {
    private static final boolean PRINT_TYPED_KEYS = false;

    public KeyboardInputSource() {
        StdDraw.text(0.5, 0.6, "New World: 'N' + Seed + 'S'");
        StdDraw.text(0.5, 0.4, "Load: 'L' Restart: ':R' Quit: ':Q'");
    }

    public char getNextKey() {
        //Note... in the initial hug implementation, the world would not return a space by default.
        // It was in a "while true" loop and would not automatically return when waiting.
        if (StdDraw.hasNextKeyTyped()) {
            char c = Character.toUpperCase(StdDraw.nextKeyTyped());
            if (PRINT_TYPED_KEYS) {
                System.out.print(c);
            }
            return c;
        }
        return ' ';
    }

    public boolean possibleNextInput() {
        return true;
    }

    public boolean shouldDisplay() {
        return true;
    }
}
