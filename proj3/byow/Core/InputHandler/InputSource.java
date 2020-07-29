package byow.Core.InputHandler;

/**
 * Created by hug.
 */
public interface InputSource {
    char getNextKey();
    boolean possibleNextInput();
    boolean shouldDisplay();
}
