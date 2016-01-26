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

}
