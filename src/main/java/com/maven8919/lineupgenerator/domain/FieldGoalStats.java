package com.maven8919.lineupgenerator.domain;

import com.maven8919.lineupgenerator.utility.RoundUtility;

public class FieldGoalStats {

    private final int fieldGoalsMade;
    private final int fieldGoalsAttempted;
    private final double fieldGoalsPercentage;

    public FieldGoalStats(final int fieldGoalsMade, final int fieldGoalsAttempted) {
        super();
        this.fieldGoalsMade = fieldGoalsMade;
        this.fieldGoalsAttempted = fieldGoalsAttempted;
        if (fieldGoalsAttempted != 0) {
            this.fieldGoalsPercentage = RoundUtility.round((double) fieldGoalsMade / (double) fieldGoalsAttempted, 3);
        } else {
            this.fieldGoalsPercentage = 0.0;
        }
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + fieldGoalsAttempted;
        result = prime * result + fieldGoalsMade;
        long temp;
        temp = Double.doubleToLongBits(fieldGoalsPercentage);
        result = prime * result + (int) (temp ^ (temp >>> 32));
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
        FieldGoalStats other = (FieldGoalStats) obj;
        if (fieldGoalsAttempted != other.fieldGoalsAttempted)
            return false;
        if (fieldGoalsMade != other.fieldGoalsMade)
            return false;
        if (Double.doubleToLongBits(fieldGoalsPercentage) != Double.doubleToLongBits(other.fieldGoalsPercentage))
            return false;
        return true;
    }

}
