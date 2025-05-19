package com.example.candidateonboardingsystem.controller;

import com.example.candidateonboardingsystem.dto.*;
import com.example.candidateonboardingsystem.model.BankInfo;
import com.example.candidateonboardingsystem.model.CandidateModel;
import com.example.candidateonboardingsystem.repository.BankInfoRepository;
import com.example.candidateonboardingsystem.repository.CandidateDocumentRepository;
import com.example.candidateonboardingsystem.service.CandidateService;
import com.example.candidateonboardingsystem.service.EmailService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/candidates")
public class CandidateController {

    private static final Logger logger = LoggerFactory.getLogger(CandidateController.class);
    private final CandidateService candidateService;
    private final CandidateDocumentRepository candidateDocumentRepository;
    private final BankInfoRepository bankInfoRepository;

    @Autowired
    public CandidateController(CandidateService candidateService, CandidateDocumentRepository candidateDocumentRepository, BankInfoRepository bankInfoRepository) {
        this.candidateService = candidateService;
        this.candidateDocumentRepository = candidateDocumentRepository;
        this.bankInfoRepository = bankInfoRepository;
    }

    @GetMapping("/all")
    public List<CandidateModel> getAllCandidates() {
        logger.info("Fetching all candidates...");
        return candidateService.getAllCandidates();
    }

    @GetMapping("/hired")
    public List<CandidateModel> getHiredCandidates() {
        logger.info("Fetching hired candidates...");
        return candidateService.getHiredCandidates();
    }

    @GetMapping("/onboarded")
    public List<CandidateModel> getAllOnBoardedCandidates() {
        logger.info("Fetching onboarded candidates...");
        return candidateService.getHiredCandidates();
    }

    @GetMapping("/count")
    public long getCandidateCount() {
        long count = candidateService.countCandidates();
        logger.info("Total candidate count: {}", count);
        return count;
    }

    @PostMapping("/{id}/status")
    public ResponseEntity<String> updateCandidateStatus(@PathVariable Long id, @RequestBody StatusUpdateRequest request) {
        logger.info("Updating candidate status for ID: {}", id);
        try {
            CandidateModel.Status status = CandidateModel.Status.valueOf(request.getStatus().toUpperCase());
            boolean updated = candidateService.updateStatus(id, String.valueOf(status));
            if (updated) {
                logger.info("Candidate ID {} status updated to {}", id, status);
                return ResponseEntity.ok("Status updated");
            } else {
                logger.warn("Candidate ID {} not found for status update", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Candidate not found");
            }
        } catch (IllegalArgumentException e) {
            logger.error("Invalid status value provided: {}", request.getStatus());
            return ResponseEntity.badRequest().body("Invalid status value");
        }
    }

    @PutMapping("/{id}/onboard-status")
    public ResponseEntity<String> updateOnboardingStatus(@PathVariable Long id, @RequestBody OnboardingStatusUpdateRequest request) {
        logger.info("Updating onboarding status for ID: {}", id);
        try {
            CandidateModel.OnboardingStatus onboardingStatus = CandidateModel.OnboardingStatus.valueOf(request.getOnboardingstatus().toUpperCase());
            boolean updated = candidateService.updateOnboardingStatus(id, String.valueOf(onboardingStatus));
            if (updated) {
                logger.info("Onboarding status updated for candidate ID {}: {}", id, onboardingStatus);
                return ResponseEntity.ok("Onboarding status updated");
            } else {
                logger.warn("Candidate ID {} not found for onboarding status update", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Candidate not found");
            }
        } catch (IllegalArgumentException e) {
            logger.error("Invalid onboarding status value provided: {}", request.getOnboardingstatus());
            return ResponseEntity.badRequest().body("Invalid onboarding status value");
        }
    }

    @PutMapping("/{id}/personal-info")
    public ResponseEntity<String> updatePersonalInfo(@PathVariable Long id, @RequestBody PersonalInfoRequest request) {
        logger.info("Updating personal info for candidate ID {}", id);
        boolean updated = candidateService.updatePersonalInfo(id, request);
        if (updated) {
            logger.info("Personal info updated for candidate ID {}", id);
            return ResponseEntity.ok("Personal info updated successfully");
        } else {
            logger.warn("Candidate ID {} not found for personal info update", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Candidate not found");
        }
    }

    @PutMapping("/{id}/bank-info")
    public boolean updateBankInfo(@PathVariable Long id, BankInfoRequest request) {
        logger.info("Attempting to update bank info for candidate ID: {}", id);

        Optional<BankInfo> optionalBankInfo = bankInfoRepository.findById(id);

        if (optionalBankInfo.isPresent()) {
            BankInfo bankInfo = optionalBankInfo.get();

            bankInfo.setAccountNumber(request.getAccountNumber());
            bankInfo.setBankName(request.getBankName());
            bankInfo.setIfscCode(request.getIfscCode());

            bankInfoRepository.save(bankInfo);

            logger.info("Bank info updated successfully for candidate ID: {}", id);
            return true;
        } else {
            logger.warn("Bank info not found for candidate ID: {}", id);
            return false;
        }
    }




    @PutMapping("/{id}/educational-info")
    public ResponseEntity<String> updateEducationalInfo(@PathVariable Long id, @RequestBody EducationalInfoRequest request) {
        logger.info("Updating educational info for candidate ID {}", id);
        boolean updated = candidateService.updateEducationalInfo(id, request);
        if (updated) {
            logger.info("Educational info updated for candidate ID {}", id);
            return ResponseEntity.ok("Educational info updated successfully");
        } else {
            logger.warn("Candidate ID {} not found for educational info update", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Candidate not found");
        }
    }



    @Autowired
    private EmailService emailService;

    @PostMapping("/send/{candidateId}")
    public ResponseEntity<String> sendMailToCandidate(@PathVariable Long candidateId) {
        logger.info("Sending email to candidate ID {}", candidateId);
        return emailService.sendMailToCandidate(candidateId);
    }
}
