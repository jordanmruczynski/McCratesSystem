package pl.jordii.mccratessystem.util;

import java.awt.*;
import java.util.Map;
import java.util.Set;

public class ColorNmFormatter {

    private ColorNmFormatter() {
        throw new AssertionError();
    }


    private static Map<String, Color> colors = Map.ofEntries(
            Map.entry("BLACK", Color.BLACK),
            Map.entry("BLUE", Color.BLUE),
            Map.entry("CYAN", Color.CYAN),
            Map.entry("DARK_GRAY", Color.DARK_GRAY),
            Map.entry("GRAY", Color.GRAY),
            Map.entry("GREEN", Color.GREEN),
            Map.entry("LIGHT_GRAY", Color.LIGHT_GRAY),
            Map.entry("MAGENTA", Color.MAGENTA),
            Map.entry("ORANGE", Color.ORANGE),
            Map.entry("PINK", Color.PINK),
            Map.entry("RED", Color.RED),
            Map.entry("WHITE", Color.WHITE),
            Map.entry("YELLOW", Color.YELLOW)
    );

    public static Color getColor(String color) {
        return colors.getOrDefault(color.toUpperCase(), Color.WHITE);
    }

    public static Set<String> getColors() {
        return colors.keySet();
    }

}
