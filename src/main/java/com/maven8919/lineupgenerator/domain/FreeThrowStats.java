package com.maven8919.lineupgenerator.domain;

public class FreeThrowStats {

    private final int freeThrowsMade;
    private final int freeThrowsAttempted;
    private final double freeThrowsPercentage;

    public FreeThrowStats(final int freeThrowsMade, final int freeThrowsAttempted) {
        super();
        this.freeThrowsMade = freeThrowsMade;
        this.freeThrowsAttempted = freeThrowsAttempted;
        this.freeThrowsPercentage = (double) freeThrowsMade / (double) freeThrowsAttempted;
    }

    public int getFreeThrowsMade() {
        return freeThrowsMade;
    }

    public int getFreeThrowsAttempted() {
        return freeThrowsAttempted;
    }

    public double getFreeThrowsPercentage() {
        return freeThrowsPercentage;
    }

}
