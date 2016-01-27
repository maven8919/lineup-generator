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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + assists;
        result = prime * result + blocks;
        result = prime * result + points;
        result = prime * result + steals;
        result = prime * result + turnovers;
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
        MiscStats other = (MiscStats) obj;
        if (assists != other.assists)
            return false;
        if (blocks != other.blocks)
            return false;
        if (points != other.points)
            return false;
        if (steals != other.steals)
            return false;
        if (turnovers != other.turnovers)
            return false;
        return true;
    }

}
