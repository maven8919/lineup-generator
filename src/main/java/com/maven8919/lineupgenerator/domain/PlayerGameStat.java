package com.maven8919.lineupgenerator.domain;

public class PlayerGameStat {
    
    private final Player player;
    private final Team opponent;
    private final Location location;
    private final int minutes;
    private final FieldGoalStats fieldGoalStats;
    private final ThreePointStats threePointStats;
    private final FreeThrowStats freeThrowStats;
    private final ReboundStats reboundStats;
    private final MiscStats miscStats;
    
    public PlayerGameStat(final Player player, final Team opponent, final Location location, final int minutes, final FieldGoalStats fieldGoalStats, final ThreePointStats threePointStats,
            final FreeThrowStats freeThrowStats, final ReboundStats reboundStats, final MiscStats miscStats) {
        super();
        this.player = player;
        this.opponent = opponent;
        this.location = location;
        this.minutes = minutes;
        this.fieldGoalStats = fieldGoalStats;
        this.threePointStats = threePointStats;
        this.freeThrowStats = freeThrowStats;
        this.reboundStats = reboundStats;
        this.miscStats = miscStats;
    }
    
    public Player getPlayer() {
        return player;
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
    
}
