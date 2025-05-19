package com.example.candidateonboardingsystem.service;

import com.example.candidateonboardingsystem.config.RabbitMQConfig;
import com.example.candidateonboardingsystem.dto.JobOfferNotificationDTO;
import com.example.candidateonboardingsystem.model.CandidateModel;
import com.example.candidateonboardingsystem.repository.CandidateModelRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobOfferService {

    private static final Logger logger = LoggerFactory.getLogger(JobOfferService.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private CandidateModelRepository candidateModelRepository;

    public boolean notifyCandidate(@Valid JobOfferNotificationDTO dto) {
        try {
            CandidateModel candidate = candidateModelRepository.findById(dto.getCandidateId())
                    .orElseThrow(() -> new RuntimeException("Candidate not found with id: " + dto.getCandidateId()));
            logger.info("Notification sent successfully for candidate id {}", candidate.getOnboardStatus(),candidate.getStatus());
            if (candidate.getOnboardStatus() == CandidateModel.OnboardingStatus.COMPLETED &&
                    candidate.getStatus() == CandidateModel.Status.OFFERED) {

                rabbitTemplate.convertAndSend(
                        RabbitMQConfig.EXCHANGE,
                        RabbitMQConfig.ROUTING_KEY,
                        dto
                );
                logger.info("Notification sent successfully for candidate id {}", dto.getCandidateId());
                return true;
            } else {
                logger.warn("Candidate with id {} is not eligible for notification. Current status: {}, onboardStatus: {}",
                        dto.getCandidateId(), candidate.getStatus(), candidate.getOnboardStatus());
                return false;
            }
        } catch (Exception e) {
            logger.error("Error while notifying candidate with id {}", dto.getCandidateId(), e);
            return false;
        }
    }
}
