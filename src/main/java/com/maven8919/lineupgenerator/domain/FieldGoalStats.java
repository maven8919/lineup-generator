package com.maven8919.lineupgenerator.domain;

public class FieldGoalStats {

    private final int fieldGoalsMade;
    private final int fieldGoalsAttempted;
    private final double fieldGoalsPercentage;

    public FieldGoalStats(final int fieldGoalsMade, final int fieldGoalsAttempted) {
        super();
        this.fieldGoalsMade = fieldGoalsMade;
        this.fieldGoalsAttempted = fieldGoalsAttempted;
        fieldGoalsPercentage = (double) fieldGoalsMade / (double) fieldGoalsAttempted;
    }

    public int getFieldGoalsMade() {
        return fieldGoalsMade;
    }

    public int getFieldGoalsAttempted() {
        return fieldGoalsAttempted;
    }

    public double getFieldGoalsPercentage() {
        return fieldGoalsPercentage;
    }

}
