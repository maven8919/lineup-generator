package com.maven8919.lineupgenerator.view;

import com.maven8919.lineupgenerator.domain.Position;

public class PlayerSummaryView {

    private Position position;
    private String playerName;
    private double forecastedPoints;
    private int salary;

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public double getForecastedPoints() {
        return forecastedPoints;
    }

    public void setForecastedPoints(double forecastedPoints) {
        this.forecastedPoints = forecastedPoints;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

}
