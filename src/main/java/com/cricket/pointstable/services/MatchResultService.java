package com.cricket.pointstable.services;

import com.cricket.pointstable.dtos.MatchResultKafkaMessage;
import com.cricket.pointstable.entities.Match;
import com.cricket.pointstable.entities.MatchResult;
import com.cricket.pointstable.repos.MatchResultRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MatchResultService {
    private final KafkaTemplate<String, String> kafkaTemplate;

    private final MatchResultRepository matchResultRepository;

    private final ObjectMapper objectMapper;

    public MatchResultService(KafkaTemplate<String, String> kafkaTemplate, MatchResultRepository matchResultRepository, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.matchResultRepository = matchResultRepository;
        this.objectMapper = objectMapper;
    }
    public MatchResult createMatchResult(MatchResult matchResult) {
        return matchResultRepository.save(matchResult);
    }
    public void publishMatchResult(MatchResultKafkaMessage result) {
        String resultStr = null;
        try {
            resultStr = objectMapper.writeValueAsString(result);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        kafkaTemplate.send("match_result", resultStr);
        log.info("Published match result for matchId: {}", result.getMatchId());
    }

    public Page<MatchResult> getMatchesResultsWithAddToPointsTableTrue(boolean addToPointsTable, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("match.matchNumber"));
        return matchResultRepository.findByAddToPointsTable(addToPointsTable, pageable);
    }
}
