package com.maven8919.lineupgenerator.domain;

public class Player {
    
    private final String playerName;
    private final Position position;
    private final int salary;
    
    public Player(final String playerName, final Position position, final int salary) {
        super();
        this.playerName = playerName;
        this.position = position;
        this.salary = salary;
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
    
}
