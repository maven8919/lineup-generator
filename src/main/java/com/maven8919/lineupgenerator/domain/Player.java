package com.maven8919.lineupgenerator.domain;

import java.util.ArrayList;
import java.util.List;

public class Player implements Comparable<Player> {
    
    private static final int MINIMUM_MINUTES_FOR_BEING_A_RELEVANT_STAT_LINE = 5;
    private final String playerName;
    private final Position position;
    private final int salary;
    private final List<StatLine> statLines;
    
    public Player(final String playerName, final Position position, final int salary) {
        super();
        this.playerName = playerName;
        this.position = position;
        this.salary = salary;
        this.statLines = new ArrayList<>();
    }
    
    public void addPlayerGameStat(StatLine playerGameStat) {
        statLines.add(playerGameStat);
    }
    
    public List<StatLine> getStatLines() {
        return statLines;
    }

    public String getPlayerName() {
        return playerName;
    }

    public Position getPosition() {
        return position;
    }

    public int getSalary() {
        return salary;
    }

    public double avarageMinutes() {
        int minutes = 0;
        int count = 0;
        for (StatLine statLine : statLines) {
            if (statLine.getMinutes() > MINIMUM_MINUTES_FOR_BEING_A_RELEVANT_STAT_LINE) {
                minutes += statLine.getMinutes();
                count++;
            }
        }
        return count > 0 ? (double) minutes / (double) count : 0.0;
    }

    @Override
    public int compareTo(Player otherPlayer) {
        Double avgPointsPerMinute = this.getAvgPointsPerMinute() * this.avarageMinutes();
        Double otherAvgPointsPerMinute = otherPlayer.getAvgPointsPerMinute() * otherPlayer.avarageMinutes();
        return avgPointsPerMinute.compareTo(otherAvgPointsPerMinute); 
    }

    public double getAvgPointsPerMinute() {
        double totalDFScore = 0.0;
        double totalMinutes = 0;
        for (StatLine statLine : statLines) {
            totalDFScore += statLine.getDfScore();
            totalMinutes += statLine.getMinutes();
        }
        return totalMinutes > 0 ? totalDFScore / totalMinutes : 0.0;
    }

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((playerName == null) ? 0 : playerName.hashCode());
		result = prime * result + ((position == null) ? 0 : position.hashCode());
		result = prime * result + salary;
		result = prime * result + ((statLines == null) ? 0 : statLines.hashCode());
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
		Player other = (Player) obj;
		if (playerName == null) {
			if (other.playerName != null)
				return false;
		} else if (!playerName.equals(other.playerName))
			return false;
		if (position != other.position)
			return false;
		if (salary != other.salary)
			return false;
		if (statLines == null) {
			if (other.statLines != null)
				return false;
		} else if (!statLines.equals(other.statLines))
			return false;
		return true;
	}
    
}
