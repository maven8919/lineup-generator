package com.maven8919.lineupgenerator.domain;

public enum Location {
    HOME, AWAY;

    public static Location getLocationFromString(String opponent) {
        Location result = null;
        if (opponent.startsWith("@")) {
            result = AWAY;
        } else {
            result = HOME;
        }
        return result;
    }
}
