package com.cricket.pointstable.jobs;


import com.cricket.pointstable.controllers.MatchController;
import com.cricket.pointstable.dtos.MatchResultKafkaMessage;
import com.cricket.pointstable.entities.Match;
import com.cricket.pointstable.entities.MatchResult;
import com.cricket.pointstable.services.MatchResultService;
import com.cricket.pointstable.services.MatchService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class TestPointsTableCronJob {

    private final MatchService matchService;
    private final MatchResultService matchResultService;

    public TestPointsTableCronJob(MatchService matchService, MatchResultService matchResultService) {
        this.matchService = matchService;
        this.matchResultService = matchResultService;
    }

    @Scheduled(cron = "0/30 * * ? * *")
    public void scheduleTaskWithCronExpression() {
        matchService.getMatchesWithAddToPointsTableTrue(true, 0, 10).forEach(match -> {
            MatchResult matchResult = match.getMatchResult();
            MatchResultKafkaMessage matchResultKafkaMessage = new MatchResultKafkaMessage();
            matchResultKafkaMessage.setMatchId(match.getId());
            matchResultKafkaMessage.setMatchResultType(matchResult.getMatchResultType());
            matchResultKafkaMessage.setTeamOneScore(matchResult.getTeamOneScore());
            matchResultKafkaMessage.setTeamTwoScore(matchResult.getTeamTwoScore());
            matchResultKafkaMessage.setAddToPointsTable(matchResult.isAddToPointsTable());
            matchResultService.publishMatchResult(matchResultKafkaMessage);
            matchResult.setAddToPointsTable(false);
            matchService.saveMatch(match);
        });
    }
}
