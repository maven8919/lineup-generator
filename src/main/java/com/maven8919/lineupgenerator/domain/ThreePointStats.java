package com.maven8919.lineupgenerator.domain;

import com.maven8919.lineupgenerator.utility.RoundUtility;

public class ThreePointStats {

    private final int threePointsMade;
    private final int threePointsAttempted;
    private final double threePointsPercentage;

    public ThreePointStats(final int threePointsMade, final int threePointsAttempted) {
        super();
        this.threePointsMade = threePointsMade;
        this.threePointsAttempted = threePointsAttempted;
        if (threePointsAttempted != 0) {
            this.threePointsPercentage = RoundUtility.round((double) threePointsMade / (double) threePointsAttempted, 3);
        } else {
            this.threePointsPercentage = 0.0;
        }
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + threePointsAttempted;
        result = prime * result + threePointsMade;
        long temp;
        temp = Double.doubleToLongBits(threePointsPercentage);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ThreePointStats other = (ThreePointStats) obj;
        if (threePointsAttempted != other.threePointsAttempted)
            return false;
        if (threePointsMade != other.threePointsMade)
            return false;
        if (Double.doubleToLongBits(threePointsPercentage) != Double.doubleToLongBits(other.threePointsPercentage))
            return false;
        return true;
    }

}
