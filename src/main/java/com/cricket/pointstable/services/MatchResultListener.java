package com.cricket.pointstable.services;

import com.cricket.pointstable.dtos.MatchResultKafkaMessage;
import com.cricket.pointstable.entities.Match;
import com.cricket.pointstable.entities.MatchResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MatchResultListener {

    private final TournamentService tournamentService;

    private final ObjectMapper objectMapper;

    public MatchResultListener(TournamentService tournamentService, ObjectMapper objectMapper) {
        this.tournamentService = tournamentService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "match_result", groupId = "match_result_group", containerFactory = "matchResultListenerContainerFactory")
    public void listenGroupMatchResult(String message) {
        try {
            MatchResultKafkaMessage matchResultKafkaMessage = objectMapper.readValue(message, MatchResultKafkaMessage.class);
            Match updatedMatch = tournamentService.processMatchResult(matchResultKafkaMessage);
            log.info("Processed result for match number: {}", updatedMatch.getMatchNumber());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
