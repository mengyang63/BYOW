package byow.TileEngine;

import java.awt.Color;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 *
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 *
 * Ex:
 *      world[x][y] = Tileset.FLOOR;
 *
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class Tileset {
    public static final TETile AVATAR = new TETile('@', Color.white, Color.black, "you");
    public static final TETile AVATAR_DEAD = new TETile('@',
            Color.red, Color.black, "Dead. (Use :R to restart.)");
    public static final TETile WALL = new TETile('#', Color.darkGray, new Color(100, 128, 128),
            "wall");
    public static final TETile FLOOR = new TETile('·', new Color(128, 192, 128), Color.black,
            "floor");
    public static final TETile NOTHING = new TETile(' ',
            Color.black, Color.black, "nothing/unknown");
    public static final TETile GRASS = new TETile('"', Color.green, Color.black, "grass");
    public static final TETile WATER = new TETile('≈', Color.blue, Color.black, "water");
    public static final TETile FLOWER = new TETile('❀', Color.magenta, Color.pink, "flower");
    public static final TETile LOCKED_DOOR = new TETile('█', Color.orange, Color.black,
            "locked door");
    public static final TETile UNLOCKED_DOOR = new TETile('▢', Color.orange, Color.black,
            "unlocked door");
    public static final TETile ROCK = new TETile('▒',
            Color.darkGray, Color.gray, "Rock (Can Be Pushed)");
    public static final TETile WANDER_ENEMY = new TETile('▲', Color.magenta, Color.black, "Enemy");
    public static final TETile WANDER_ENEMY_ACTIVE = new TETile('▲',
            Color.magenta, Color.red, "Enemy (Active). Careful.");
    public static final TETile TREE = new TETile('♠', Color.green, Color.black, "tree");


    public static TETile depthBasedWall(int depth) {
        int red = Math.min(255, Math.max(0, 100 - 40 * depth));
        int green = Math.min(255, Math.max(0, 128 + 35 * depth));
        int blue = Math.min(255, Math.max(0, 128 + 42 * depth));
        return new TETile('#', Color.darkGray, new Color(red, green, blue), "wall");
    }

    public static TETile depthBasedFloor(int depth) {
        int red = Math.min(255, Math.max(0, 128 - 40 * depth));
        int green = Math.min(255, Math.max(0, 192 + 35 * depth));
        int blue = Math.min(255, Math.max(0, 128 + 42 * depth));
        return new TETile('·', new Color(red, green, blue), Color.black, "floor on level " + depth);
    }
}


