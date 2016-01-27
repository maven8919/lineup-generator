package com.maven8919.lineupgenerator.domain;

import com.maven8919.lineupgenerator.utility.RoundUtility;

public class FreeThrowStats {

    private final int freeThrowsMade;
    private final int freeThrowsAttempted;
    private final double freeThrowsPercentage;

    public FreeThrowStats(final int freeThrowsMade, final int freeThrowsAttempted) {
        super();
        this.freeThrowsMade = freeThrowsMade;
        this.freeThrowsAttempted = freeThrowsAttempted;
        if (freeThrowsAttempted != 0) {
            this.freeThrowsPercentage = RoundUtility.round((double) freeThrowsMade / (double) freeThrowsAttempted, 3);
        } else {
            this.freeThrowsPercentage = 0.0;
        }
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + freeThrowsAttempted;
        result = prime * result + freeThrowsMade;
        long temp;
        temp = Double.doubleToLongBits(freeThrowsPercentage);
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
        FreeThrowStats other = (FreeThrowStats) obj;
        if (freeThrowsAttempted != other.freeThrowsAttempted)
            return false;
        if (freeThrowsMade != other.freeThrowsMade)
            return false;
        if (Double.doubleToLongBits(freeThrowsPercentage) != Double.doubleToLongBits(other.freeThrowsPercentage))
            return false;
        return true;
    }

}
