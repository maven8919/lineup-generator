package com.maven8919.lineupgenerator.domain;

public class ReboundStats {

    private final int offensiveRebounds;
    private final int defensiveRebounds;
    private final int totalRebounds;

    public ReboundStats(final int offensiveRebounds, final int defensiveRebounds) {
        super();
        this.offensiveRebounds = offensiveRebounds;
        this.defensiveRebounds = defensiveRebounds;
        this.totalRebounds = offensiveRebounds + defensiveRebounds;
    }

    public int getOffensiveRebounds() {
        return offensiveRebounds;
    }

    public int getDefensiveRebounds() {
        return defensiveRebounds;
    }

    public int getTotalRebounds() {
        return totalRebounds;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + defensiveRebounds;
        result = prime * result + offensiveRebounds;
        result = prime * result + totalRebounds;
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
        ReboundStats other = (ReboundStats) obj;
        if (defensiveRebounds != other.defensiveRebounds)
            return false;
        if (offensiveRebounds != other.offensiveRebounds)
            return false;
        if (totalRebounds != other.totalRebounds)
            return false;
        return true;
    }

}
