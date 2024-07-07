package com.cricket.pointstable.enums;

import com.cricket.pointstable.entities.MatchResult;
import com.cricket.pointstable.entities.PointsTable;
import com.cricket.pointstable.entities.Score;

public enum MatchResultType {
    NO_RESULT, COMPLETED {
        @Override
        public void calculateNetRunRate(PointsTable.PointsTableEntry teamOneEntry, PointsTable.PointsTableEntry teamTwoEntry, MatchResult matchResult) {
            Score teamOneScore = matchResult.getTeamOneScore();
            Score teamTwoScore = matchResult.getTeamTwoScore();
            teamOneEntry.setForRuns(teamOneEntry.getForRuns() + teamOneScore.getRuns());
            teamOneEntry.setForBalls(teamOneEntry.getForBalls() + (teamOneScore.isAllOut() ? teamOneScore.getTotalBalls(): teamOneScore.getBalls()));
            teamOneEntry.setAgainstRuns(teamOneEntry.getAgainstRuns() + teamTwoScore.getRuns());
            teamOneEntry.setAgainstBalls(teamOneEntry.getAgainstBalls() + (teamTwoScore.isAllOut() ? teamTwoScore.getTotalBalls(): teamTwoScore.getBalls()));
            teamTwoEntry.setForRuns(teamTwoEntry.getForRuns() + teamTwoScore.getRuns());
            teamTwoEntry.setForBalls(teamTwoEntry.getForBalls() + (teamTwoScore.isAllOut() ? teamTwoScore.getTotalBalls(): teamTwoScore.getBalls()));
            teamTwoEntry.setAgainstRuns(teamTwoEntry.getAgainstRuns() + teamOneScore.getRuns());
            teamTwoEntry.setAgainstBalls(teamTwoEntry.getAgainstBalls() + (teamOneScore.isAllOut() ? teamOneScore.getTotalBalls(): teamOneScore.getBalls()));
            teamOneEntry.setNetRunRate(((teamOneEntry.getForRuns()/teamOneEntry.getForBalls())*6) - ((teamOneEntry.getAgainstRuns()/teamOneEntry.getAgainstBalls())*6));
            teamTwoEntry.setNetRunRate(((teamTwoEntry.getForRuns()/teamTwoEntry.getForBalls())*6) - ((teamTwoEntry.getAgainstRuns()/teamTwoEntry.getAgainstBalls())*6));
        }
    }, COMPLETED_WITH_DLS {
        @Override
        public void calculateNetRunRate(PointsTable.PointsTableEntry teamOneEntry, PointsTable.PointsTableEntry teamTwoEntry, MatchResult matchResult) {
            calculateNRRWithDLSScore(teamOneEntry, teamTwoEntry, matchResult);
        }
    };
    private static void calculateNRRWithDLSScore(PointsTable.PointsTableEntry teamOneEntry, PointsTable.PointsTableEntry teamTwoEntry, MatchResult matchResult) {
        Score teamTwoScore = matchResult.getTeamTwoScore();
        teamOneEntry.setForRuns(teamOneEntry.getForRuns() + teamTwoScore.getTarget() - 1);
        teamOneEntry.setForBalls(teamOneEntry.getForBalls() + teamTwoScore.getTotalBalls());
        teamOneEntry.setAgainstRuns(teamOneEntry.getAgainstRuns() + teamTwoScore.getRuns());
        teamOneEntry.setAgainstBalls(teamOneEntry.getAgainstBalls() + teamTwoScore.getBalls());
        teamTwoEntry.setForRuns(teamTwoEntry.getForRuns() + teamTwoScore.getRuns());
        teamTwoEntry.setForBalls(teamTwoEntry.getForBalls() + teamTwoScore.getBalls());
        teamTwoEntry.setAgainstRuns(teamTwoEntry.getAgainstRuns() + teamTwoScore.getTarget() - 1);
        teamTwoEntry.setAgainstBalls(teamTwoEntry.getAgainstBalls() + teamTwoScore.getTotalBalls());
        teamOneEntry.setNetRunRate(((teamOneEntry.getForRuns()/ teamOneEntry.getForBalls())/6) - ((teamOneEntry.getAgainstRuns()/ teamOneEntry.getAgainstBalls())/6));
        teamTwoEntry.setNetRunRate(((teamTwoEntry.getForRuns()/ teamTwoEntry.getForBalls())/6) - ((teamTwoEntry.getAgainstRuns()/ teamTwoEntry.getAgainstBalls())/6));
    }
    public void calculateNetRunRate(PointsTable.PointsTableEntry teamOneEntry, PointsTable.PointsTableEntry teamTwoEntry, MatchResult matchResult) {
    }

    public enum MatchStatus {
        NOT_STARTED, IN_PROGRESS, COMPLETED
    }
}
