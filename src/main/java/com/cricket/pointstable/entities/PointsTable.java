package com.cricket.pointstable.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PointsTable {
    private List<PointsTableEntry> pointsTableEntries;
    @Getter
    @Setter
    public static class PointsTableEntry implements Comparable<PointsTableEntry> {
        private Team team;
        private int matchesPlayed;
        private int matchesWon;
        private int matchesLost;
        private int matchesTied;
        private int matchesNoResult;
        private int points;
        private int forRuns;
        private double forBalls;
        private int againstRuns;
        private double againstBalls;
        private double netRunRate;

        @Override
        public int compareTo(PointsTableEntry o) {
            if(this.points == o.points) {
                double diff = o.netRunRate - this.netRunRate;
                if(diff == 0) {
                    return o.forRuns - this.forRuns;
                } else if(diff > 0) {
                    return 1;
                } else {
                    return -1;
                }
            }
            return (o.points - this.points);
        }
    }
}
