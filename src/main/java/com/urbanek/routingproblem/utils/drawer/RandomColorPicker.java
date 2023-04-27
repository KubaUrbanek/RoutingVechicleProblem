package com.urbanek.routingproblem.utils.drawer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomColorPicker {
    private static final List<Color> PREDEFINED_COLORS = new ArrayList<>();

    static {
        PREDEFINED_COLORS.add(Color.GRAY);
        PREDEFINED_COLORS.add(Color.BLACK);
        PREDEFINED_COLORS.add(Color.RED);
        PREDEFINED_COLORS.add(Color.MAGENTA);
        PREDEFINED_COLORS.add(Color.BLUE);
        PREDEFINED_COLORS.add(Color.DARK_GRAY);
        PREDEFINED_COLORS.add(Color.ORANGE);
        PREDEFINED_COLORS.add(Color.GREEN);
    }

    private static final Random rand = new Random();

    private static List<Color> AVAILABLE_COLORS = new ArrayList<>(PREDEFINED_COLORS);

    public static Color getRandomColor() {
        if (AVAILABLE_COLORS.isEmpty()) {
            AVAILABLE_COLORS = new ArrayList<>(PREDEFINED_COLORS);
        }

        int index = rand.nextInt(AVAILABLE_COLORS.size());

        return AVAILABLE_COLORS.remove(index);
    }
}
