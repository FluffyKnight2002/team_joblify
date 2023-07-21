package com.ace_inspiration.team_joblify.utils;

import com.ace_inspiration.team_joblify.entity.Level;

public class LevelUtils {
    public static String getFormattedValue(Level level) {
        String formattedValue = level.name().toLowerCase();
        formattedValue = formattedValue.substring(0, 1).toUpperCase() + formattedValue.substring(1);
        formattedValue = formattedValue.replace("level", "Level");
        return formattedValue;
    }
}
