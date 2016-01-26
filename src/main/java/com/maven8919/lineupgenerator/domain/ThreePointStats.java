package com.maven8919.lineupgenerator.domain;

public class ThreePointStats {

    private final int threePointsMade;
    private final int threePointsAttempted;
    private final double threePointsPercentage;

    public ThreePointStats(final int threePointsMade, final int threePointsAttempted) {
        super();
        this.threePointsMade = threePointsMade;
        this.threePointsAttempted = threePointsAttempted;
        this.threePointsPercentage = (double) threePointsMade / (double) threePointsAttempted;
    }

    public int getThreePointsMade() {
        return threePointsMade;
    }

    public int getThreePointsAttempted() {
        return threePointsAttempted;
    }

    public double getThreePointsPercentage() {
        return threePointsPercentage;
    }

}
