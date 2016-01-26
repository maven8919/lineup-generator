package com.maven8919.lineupgenerator.domain;

public class MiscStats {

    private final int assists;
    private final int turnovers;
    private final int steals;
    private final int blocks;
    private final int points;

    public MiscStats(final int assists, final int turnovers, final int steals, final int blocks, final int points) {
        super();
        this.assists = assists;
        this.turnovers = turnovers;
        this.steals = steals;
        this.blocks = blocks;
        this.points = points;
    }

    public int getAssists() {
        return assists;
    }

    public int getTurnovers() {
        return turnovers;
    }

    public int getSteals() {
        return steals;
    }

    public int getBlocks() {
        return blocks;
    }

    public int getPoints() {
        return points;
    }

}
