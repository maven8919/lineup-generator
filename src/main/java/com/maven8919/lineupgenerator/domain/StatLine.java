package com.maven8919.lineupgenerator.domain;

import com.maven8919.lineupgenerator.utility.RoundUtility;

public class StatLine {
    
    private static final double TURNOVER_VALUE = -1.0;
    private static final double BLOCK_VALUE = 2.0;
    private static final double STEAL_VALUE = 2.0;
    private static final double ASSIST_VALUE = 1.5;
    private static final double REBOUND_VALUE = 1.2;
    private static final double POINTS_VALUE = 1.0;
    private static final double THREE_POINTER_VALUE = 0.5;
    
    private final Team opponent;
    private final Location location;
    private final int minutes;
    private final FieldGoalStats fieldGoalStats;
    private final ThreePointStats threePointStats;
    private final FreeThrowStats freeThrowStats;
    private final ReboundStats reboundStats;
    private final MiscStats miscStats;
    private final double dfScore;
    
    public StatLine(final Team opponent, final Location location, final int minutes, final FieldGoalStats fieldGoalStats, final ThreePointStats threePointStats,
            final FreeThrowStats freeThrowStats, final ReboundStats reboundStats, final MiscStats miscStats) {
        super();
        this.opponent = opponent;
        this.location = location;
        this.minutes = minutes;
        this.fieldGoalStats = fieldGoalStats;
        this.threePointStats = threePointStats;
        this.freeThrowStats = freeThrowStats;
        this.reboundStats = reboundStats;
        this.miscStats = miscStats;
        this.dfScore = calculateDfScore();
    }

    private double calculateDfScore() {
        double _3PScore = THREE_POINTER_VALUE * threePointStats.getThreePointsMade();
        double pointsScore = POINTS_VALUE * miscStats.getPoints();
        double reboundsScore = REBOUND_VALUE * reboundStats.getTotalRebounds();
        double assistsScore = ASSIST_VALUE * miscStats.getAssists();
        double stealsScore = STEAL_VALUE * miscStats.getSteals();
        double blocksScore = BLOCK_VALUE * miscStats.getBlocks();
        double turnoversScore = TURNOVER_VALUE * miscStats.getTurnovers();
        return RoundUtility.round(_3PScore + pointsScore + reboundsScore + assistsScore + stealsScore + blocksScore + turnoversScore, 3);
    }

    public double getDfScore() {
        return dfScore;
    }

    public Team getOpponent() {
        return opponent;
    }

    public Location getLocation() {
        return location;
    }

    public int getMinutes() {
        return minutes;
    }

    public FieldGoalStats getFieldGoalStats() {
        return fieldGoalStats;
    }

    public ThreePointStats getThreePointStats() {
        return threePointStats;
    }

    public FreeThrowStats getFreeThrowStats() {
        return freeThrowStats;
    }

    public ReboundStats getReboundStats() {
        return reboundStats;
    }

    public MiscStats getMiscStats() {
        return miscStats;
    }

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(dfScore);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((fieldGoalStats == null) ? 0 : fieldGoalStats.hashCode());
		result = prime * result + ((freeThrowStats == null) ? 0 : freeThrowStats.hashCode());
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		result = prime * result + minutes;
		result = prime * result + ((miscStats == null) ? 0 : miscStats.hashCode());
		result = prime * result + ((opponent == null) ? 0 : opponent.hashCode());
		result = prime * result + ((reboundStats == null) ? 0 : reboundStats.hashCode());
		result = prime * result + ((threePointStats == null) ? 0 : threePointStats.hashCode());
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
		StatLine other = (StatLine) obj;
		if (Double.doubleToLongBits(dfScore) != Double.doubleToLongBits(other.dfScore))
			return false;
		if (fieldGoalStats == null) {
			if (other.fieldGoalStats != null)
				return false;
		} else if (!fieldGoalStats.equals(other.fieldGoalStats))
			return false;
		if (freeThrowStats == null) {
			if (other.freeThrowStats != null)
				return false;
		} else if (!freeThrowStats.equals(other.freeThrowStats))
			return false;
		if (location != other.location)
			return false;
		if (minutes != other.minutes)
			return false;
		if (miscStats == null) {
			if (other.miscStats != null)
				return false;
		} else if (!miscStats.equals(other.miscStats))
			return false;
		if (opponent != other.opponent)
			return false;
		if (reboundStats == null) {
			if (other.reboundStats != null)
				return false;
		} else if (!reboundStats.equals(other.reboundStats))
			return false;
		if (threePointStats == null) {
			if (other.threePointStats != null)
				return false;
		} else if (!threePointStats.equals(other.threePointStats))
			return false;
		return true;
	}
    
}
