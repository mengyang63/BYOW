package byow.Core;

import byow.Core.InputHandler.InputSource;
import byow.Core.InputHandler.KeyboardInputSource;
import byow.Core.InputHandler.StringInputDevice;
import byow.TileEngine.TERenderer;
import edu.princeton.cs.introcs.StdDraw;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class GameLoop {
    private String sourceString;
    private static final String SAVE_FILE = "save_data.txt";
    private World world;
    private TERenderer graphicsHandler;
    private static final double LOOP_INTERVAL = 60;


    /* Constructor makes a world and starts gameloop. */
    public GameLoop(InputSource iS) {
        sourceString = "";
        world = initWorld(iS);
        if (iS.shouldDisplay()) {
            graphicsHandler = new TERenderer();
            graphicsHandler.initialize(world.getWidth(), (int) (1.1 * world.getHeight()));
        }
        startLoop(iS);
    }

    /* Initializes the world before WASD and other commands are set. */
    private World initWorld(InputSource currSource) {
        boolean isReadingSequence = false;
        int currIndexOfSequence = 0;
        while (currSource.possibleNextInput()) {
            char currChar = currSource.getNextKey();

            if (currChar == 'L' || currChar == 'l') {
                StringInputDevice load = new StringInputDevice(getSaveData());
                sourceString = "";
                world = initWorld(load);
                startLoop(load);
                return world;
            }

            if (currChar == 'N' || currChar == 'n') {
                isReadingSequence = true;
            }

            if (isReadingSequence) {
                if (currChar != ' ') {
                    sourceString += currChar;
                    currIndexOfSequence += 1;
                }
                if (currChar == 'S' || currChar == 's') {
                    String seedString = sourceString.substring(1, currIndexOfSequence - 1);
                    try {
                        Long seed = Long.parseLong(seedString);
                        return new World(seed);
                    } catch (NumberFormatException e) {
                        System.out.println("Please type a valid world generation. "
                                + "(One that starts wit N, contains a seed, and ends with S. "
                                + "Note: We will have to edit this later to "
                                + "work with the gui and not just the debugger.");
                        sourceString = "";
                        return initWorld(currSource);
                    }
                }
            }

        }
        throw new IllegalArgumentException("You are no longer able to "
                + "provide an input and have not provided a valid world.");
    }



    /* Once a world has been made, this loop will snap inputs to a specific timing pattern. */
    private void startLoop(InputSource currSource) {
        boolean isReadingSequence = false;
        double timer = System.currentTimeMillis();
        //Begin loop.
        while (currSource.possibleNextInput()) {
            if (currSource instanceof StringInputDevice
                    || timer < System.currentTimeMillis() - LOOP_INTERVAL
                    || timer > System.currentTimeMillis() + LOOP_INTERVAL) {
                timer = System.currentTimeMillis();
                char currCommand = currSource.getNextKey();

                if (isReadingSequence) {
                    if (currCommand == 'Q' || currCommand == 'q') {
                        saveStringToFile(sourceString);
                        return;
                    }
                    if (currCommand == 'R' || currCommand == 'r') {
                        world = initWorld(new StringInputDevice(sourceString));
                        startLoop(new KeyboardInputSource());
                        //System.exit(0);
                        return;
                    }
                } else {
                    if (currCommand == ':') {
                        isReadingSequence = true;
                    } else {
                        sourceString += currCommand;
                        world.timeStep(currCommand);
                        if (currSource.shouldDisplay()) {
                            graphicsHandler.renderFrame(world.getTiles(true));
                            StdDraw.show();
                        }
                    }
                }

            }
        }
    }



    /* Get the string from the save file to load from. */
    private String getSaveData() {
        try {
            File saveData = new File(SAVE_FILE);
            Scanner sc = new Scanner(saveData);
            if (sc.hasNextLine()) {
                return sc.nextLine();
            }
            return null;
        } catch (FileNotFoundException e) {
            System.out.println("SaveFile not found.");
            return "N0S";
        }
    }

    /* Saves the string to the save file. */
    private void saveStringToFile(String s) {
        try {
            FileWriter saveData = new FileWriter(SAVE_FILE);
            BufferedWriter bW = new BufferedWriter(saveData);
            bW.write(s);
            bW.flush();
            bW.close();
        } catch (IOException noFile) {
            System.out.println("SaveFile not found.");
        }
    }


    /* World getter. */
    public World getWorld() {
        return world;
    }

    /* The String Source that created this world. */
    public String getSourceString() {
        return sourceString;
    }

}
